package com.ultimalabs.sattrackapi.predict.service;

import com.ultimalabs.sattrackapi.tle.model.TLEPlus;
import com.ultimalabs.sattrackapi.tle.service.TleFetcherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;
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
     * Equatorial radius in meters
     */
    private final double equatorialRadius = Constants.WGS84_EARTH_EQUATORIAL_RADIUS;

    /**
     * Earth flattening
     */
    private final double earthFlattening = Constants.WGS84_EARTH_FLATTENING;

    /**
     * Earth frame
     */
    private final Frame earthFrame = FramesFactory.getITRF(IERSConventions.IERS_2010, true);

    /**
     * Predict next event of satellite given by Satellite Number
     *
     * @param satNum       Satellite Number
     * @param longitude    observer longitude
     * @param latitude     observer latitude
     * @param altitude     observer altitude
     * @param minElevation minimal elevation
     */
    @Override
    public String visibilityBySatelliteNumber(int satNum, double longitude, double latitude, double altitude, double minElevation) {
        TLEPlus tle = tleFetcherService.getTleBySatelliteId(satNum);
        if (tle == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return getNextEvent(tle, longitude, latitude, altitude, minElevation);
    }

    /**
     * Predict next event of satellite given by International Designator
     *
     * @param intDesignator International Designator
     * @param longitude     observer longitude
     * @param latitude      observer latitude
     * @param altitude      observer altitude
     * @param minElevation  minimal elevation
     */
    @Override
    public String visibilityByInternationalDesignator(String intDesignator, double longitude, double latitude, double altitude, double minElevation) {
        TLEPlus tle = tleFetcherService.getTleByInternationalDesignator(intDesignator);
        if (tle == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return getNextEvent(tle, longitude, latitude, altitude, minElevation);
    }

    private String getNextEvent(TLEPlus tle, double lon, double lat, double alt, double minEl) {
        return null;
    }

}
