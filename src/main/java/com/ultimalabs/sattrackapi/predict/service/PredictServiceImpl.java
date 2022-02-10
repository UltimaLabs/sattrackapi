package com.ultimalabs.sattrackapi.predict.service;

import com.ultimalabs.sattrackapi.predict.model.SatellitePass;
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
     * @param searchString Satellite Number or International Designator
     * @param longitude    observer longitude
     * @param latitude     observer latitude
     * @param altitude     observer altitude
     * @param minElevation minimal elevation
     * @return next visibility event, without the details
     */
    @Override
    public SatellitePass getNextEventWithoutDetails(String searchString, double latitude, double longitude, double altitude, double minElevation) {
        try {
            Predictor predictor = new Predictor(getTle(searchString), latitude, longitude, altitude, minElevation);
            AbsoluteDate now = getNowAsAbsoluteDate();
            return predictor.getEventData(now, now.shiftedBy(259200.));
        } catch (LoggedEventsException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Returns next visibility event with pass details
     * @param searchString Satellite Number or International Designator
     * @param longitude    observer longitude
     * @param latitude     observer latitude
     * @param altitude     observer altitude
     * @param minElevation minimal elevation
     * @param stepSize     step resolution for the master mode propagator
     * @return next visibility event, with details
     */
    @Override
    public SatellitePass getNextEventWithDetails(String searchString, double latitude, double longitude, double altitude, double minElevation, double stepSize) {
        try {
            Predictor predictor = new Predictor(getTle(searchString), latitude, longitude, altitude, minElevation);
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
     * @param searchString Satellite Number or International Designator
     * @param longitude    observer longitude
     * @param latitude     observer latitude
     * @param altitude     observer altitude
     * @param minElevation minimal elevation
     * @return next n visibility events, without the details
     */
    @Override
    public List<SatellitePass> getNextEventsWithoutDetails(int n, String searchString, double longitude, double latitude, double altitude, double minElevation) {
        try {
            List<SatellitePass> events = new ArrayList<>();
            Predictor predictor = new Predictor(getTle(searchString), latitude, longitude, altitude, minElevation);

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
     * Returns TLE object based on search string
     * @param searchString Satellite Number or International Designator
     * @return TLE object
     */
    private TLEPlus getTle(String searchString) {
        return tleFetcherService.getTle(searchString);
    }

    /**
     * Get the 'now' time value as an AbsoluteDate object
     * @return now as an absolute date
     */
    private AbsoluteDate getNowAsAbsoluteDate() { return new AbsoluteDate(new Date(), TimeScalesFactory.getUTC());}
}
