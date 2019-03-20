package com.ultimalabs.sattrackapi.predict.service;

import com.ultimalabs.sattrackapi.tle.model.TLEPlus;
import com.ultimalabs.sattrackapi.tle.service.TleFetcherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
     * Returns next visibility event
     *
     * @param searchString Satellite Number or International Designator
     * @param longitude    observer longitude
     * @param latitude     observer latitude
     * @param altitude     observer altitude
     * @param minElevation minimal elevation
     */
    @Override
    public String getVisibility(String searchString, double longitude, double latitude, double altitude, double minElevation) {
        TLEPlus tle = tleFetcherService.getTle(searchString);
        if (tle == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return getNextEvent(tle, longitude, latitude, altitude, minElevation);
    }

    private String getNextEvent(TLEPlus tle, double lon, double lat, double alt, double minEl) {
        return null;
    }

}
