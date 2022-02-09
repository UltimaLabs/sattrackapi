package com.ultimalabs.sattrackapi.predict.service;

import com.ultimalabs.sattrackapi.predict.model.SatellitePass;
import com.ultimalabs.sattrackapi.predict.predicter.LoggedEventsException;
import com.ultimalabs.sattrackapi.predict.predicter.Predicter;
import com.ultimalabs.sattrackapi.tle.model.TLEPlus;
import com.ultimalabs.sattrackapi.tle.service.TleFetcherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Predict service
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PredictServiceImpl implements PredictService {

    /**
     * TLE fetcher service
     */
    private final TleFetcherService tleFetcherService;

    /**
     * Returns next visibility event without pass details
     *
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
            Predicter predicter = new Predicter(getTle(searchString), latitude, longitude, altitude, minElevation);
            return predicter.getEventData();
        } catch (LoggedEventsException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Returns next visibility event with pass details
     *
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
            Predicter predicter = new Predicter(getTle(searchString), latitude, longitude, altitude, minElevation);
            return predicter.getEventDataWithDetails(stepSize);
        } catch (LoggedEventsException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<SatellitePass> getMultipleNextEventsWithoutDetails(Integer numberOfEvents, String searchString, double longitude, double latitude, double altitude, double minElevation) {
        return null;
    }

    /**
     * Returns TLE object based on search string
     * @param searchString Satellite Number or International Designator
     * @return TLE object
     */
    private TLEPlus getTle(String searchString) {
        return tleFetcherService.getTle(searchString);
    }
}
