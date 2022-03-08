package com.ultimalabs.sattrackapi.predict.service;

import com.ultimalabs.sattrackapi.predict.model.SatellitePass;
import com.ultimalabs.sattrackapi.predict.model.dto.ObserverParams;
import com.ultimalabs.sattrackapi.predict.model.dto.TLEParams;
import com.ultimalabs.sattrackapi.predict.predictor.LoggedEventsException;
import com.ultimalabs.sattrackapi.predict.predictor.Predictor;
import com.ultimalabs.sattrackapi.tle.model.TLEPlus;
import com.ultimalabs.sattrackapi.tle.service.TleFetcherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
class PredictServiceImpl implements PredictService {

    public static final double THREE_DAYS_IN_SECONDS = 259200.;

    private final TleFetcherService tleFetcherService;

    /**
     * Returns next visibility event without pass details
     * @param tleParams TLE parameters object containing satellite identifier or satellite name and tle lines
     * @param observerParams Observer parameters object containing longitude, latitude, altitude and min elevation values
     * @return next visibility event, without the details
     */
    @Override
    public SatellitePass getNextEvent(TLEParams tleParams, ObserverParams observerParams) {
        try {
            Predictor predictor = getPredictor(tleParams, observerParams);
            AbsoluteDate now = getNowAsAbsoluteDate();

            return predictor.getEventData(now, now.shiftedBy(THREE_DAYS_IN_SECONDS));

        } catch (LoggedEventsException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Returns next visibility event with pass details
     * @param tleParams TLE parameters object containing satellite identifier or satellite name and tle lines
     * @param observerParams Observer parameters object containing longitude, latitude, altitude and min elevation values
     * @param stepSize     step resolution for the master mode propagator
     * @return next visibility event, with details
     */
    @Override
    public SatellitePass getNextEventWithDetails(TLEParams tleParams, ObserverParams observerParams, double stepSize) {
        try {
            Predictor predictor = getPredictor(tleParams, observerParams);
            AbsoluteDate now = getNowAsAbsoluteDate();

            return predictor.getEventDataWithDetails(now, now.shiftedBy(THREE_DAYS_IN_SECONDS), stepSize);

        } catch (LoggedEventsException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Returns a number of next visibility events without pass details
     * @param numberOfDays Next number of days during which we look for the passes
     * @param tleParams TLE parameters object containing satellite identifier or satellite name and tle lines
     * @param observerParams Observer parameters object containing longitude, latitude, altitude and min elevation values
     * @return Satellite passes in the specified next number of days
     */
    @Override
    public List<SatellitePass> getNextEvents(int numberOfDays, TLEParams tleParams, ObserverParams observerParams) {

        List<SatellitePass> events = new ArrayList<>();
        Predictor predictor = getPredictor(tleParams, observerParams);

        try {
            boolean isWithinLimit;

            do {
                AbsoluteDate from = shiftDateForNextPass(predictor.getSetDate());

                SatellitePass event = predictor.getEventData(from, from.shiftedBy(THREE_DAYS_IN_SECONDS));

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                String dateInString = event.getRisePoint().getT();
                Date date = formatter.parse(dateInString);

                AbsoluteDate riseDate = new AbsoluteDate(date, TimeScalesFactory.getUTC());
                AbsoluteDate nDaysAfterRiseDate = getNowAsAbsoluteDate().shiftedBy(numberOfDays * ChronoUnit.DAYS.getDuration().toSeconds());

                isWithinLimit = riseDate.isBefore(nDaysAfterRiseDate);

                if(isWithinLimit)
                    events.add(event);

            } while(isWithinLimit);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return events;
    }

    /**
     * Returns instantiated predictor object with the givem TLE and observer params
     * @param tleParams TLE data
     * @param observerParams Observer geographic coordinates
     * @return Instantiated predictor object
     */
    private Predictor getPredictor(TLEParams tleParams, ObserverParams observerParams) {
        return new Predictor(
                getTle(tleParams),
                observerParams.getLatitude(),
                observerParams.getLongitude(),
                observerParams.getAltitude(),
                observerParams.getMinElevation()
        );
    }

    /**
     * Shifts the date of the previous pass for the specified value. If the previous pass date is null, it returns the current date time
     * @param setDateOfPreviousPass Value of how much should the previous pass date be shifted
     * @return Shifted date
     */
    private AbsoluteDate shiftDateForNextPass(AbsoluteDate setDateOfPreviousPass) {
        if(setDateOfPreviousPass == null)
            return getNowAsAbsoluteDate();
        else
            return setDateOfPreviousPass.shiftedBy(0.1);
    }

    /**
     * Returns TLE object based on TLE parameters
     * @param tleParams TLE parameters object containing satellite identifier or satellite name and tle lines
     * @return TLE object
     */
    private TLEPlus getTle(TLEParams tleParams) {
        if(tleParams.getLine1() == null || tleParams.getLine2() == null)
            return tleFetcherService.getTle(tleParams.getSatelliteIdentifier());
        else
            try {
                return new TLEPlus(tleParams.getSatelliteName(), tleParams.getLine1(), tleParams.getLine2());
            } catch(Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, buildInvalidTleParamsMsg(tleParams));
            }
    }

    /**
     * Get the 'now' time value as an AbsoluteDate object
     * @return now as an absolute date
     */
    private AbsoluteDate getNowAsAbsoluteDate() { return new AbsoluteDate(new Date(), TimeScalesFactory.getUTC());}

    /**
     * Build the bad request response message in case of invalid TLE parameters
     * @param tleParams - Invalid TLE parameters
     * @return built error response message
     */
    private String buildInvalidTleParamsMsg(TLEParams tleParams) {
        return String.format("Invalid TLE parameters passed in the URL. %s", tleParams);
    }
}
