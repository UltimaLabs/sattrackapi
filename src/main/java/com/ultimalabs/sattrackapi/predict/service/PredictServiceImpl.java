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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
class PredictServiceImpl implements PredictService {

    private final TleFetcherService tleFetcherService;

    /**
     * Returns next visibility event without pass details
     * @param tleParams TLE parameters object containing satellite identifier or satellite name and tle lines
     * @param observerParams Observer parameters object containing longitude, latitude, altitude and min elevation values
     * @return next visibility event, without the details
     */
    @Override
    public SatellitePass getNextEventWithoutDetails(TLEParams tleParams, ObserverParams observerParams) {
        try {
            Predictor predictor = new Predictor(
                    getTle(tleParams),
                    observerParams.getLatitude(),
                    observerParams.getLongitude(),
                    observerParams.getAltitude(),
                    observerParams.getMinElevation()
            );

            AbsoluteDate now = getNowAsAbsoluteDate();
            return predictor.getEventData(now, now.shiftedBy(259200.));
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
            Predictor predictor = new Predictor(
                    getTle(tleParams),
                    observerParams.getLatitude(),
                    observerParams.getLongitude(),
                    observerParams.getAltitude(),
                    observerParams.getMinElevation()
            );
            AbsoluteDate now = getNowAsAbsoluteDate();
            return predictor.getEventDataWithDetails(now, now.shiftedBy(259200.), stepSize);
        } catch (LoggedEventsException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Returns a number of next visibility events without pass details
     * @param n            Number of visibility events to log and return
     * @param tleParams TLE parameters object containing satellite identifier or satellite name and tle lines
     * @param observerParams Observer parameters object containing longitude, latitude, altitude and min elevation values
     * @return next n visibility events, without the details
     */
    @Override
    public List<SatellitePass> getNextEventsWithoutDetails(int n, TLEParams tleParams, ObserverParams observerParams) {
        try {
            List<SatellitePass> events = new ArrayList<>();
            Predictor predictor = new Predictor(
                    getTle(tleParams),
                    observerParams.getLatitude(),
                    observerParams.getLongitude(),
                    observerParams.getAltitude(),
                    observerParams.getMinElevation()
            );

            for (int i = 0; i < n; i++) {
                AbsoluteDate from = shiftDateForNextPass(predictor.getSetDate());
                events.add(predictor.getEventData(from, from.shiftedBy(259200.)));
            }

            return events;

        } catch (LoggedEventsException e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

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
            return new TLEPlus(tleParams.getSatelliteName(), tleParams.getLine1(), tleParams.getLine2());
    }

    /**
     * Get the 'now' time value as an AbsoluteDate object
     * @return now as an absolute date
     */
    private AbsoluteDate getNowAsAbsoluteDate() { return new AbsoluteDate(new Date(), TimeScalesFactory.getUTC());}
}
