package com.ultimalabs.sattrackapi.common.model;

import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;

/**
 * Earth parameters
 * <p>
 * Holds equatorial radius, FLATTENING and Earth frame
 */
public class EarthParams {

    private EarthParams() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Equatorial radius in meters
     */
    public static final double EQUATORIAL_RADIUS = Constants.WGS84_EARTH_EQUATORIAL_RADIUS;

    /**
     * Earth FLATTENING
     */
    public static final double FLATTENING = Constants.WGS84_EARTH_FLATTENING;

    /**
     * Earth frame
     */
    public static final Frame iers2010Frame = FramesFactory.getITRF(IERSConventions.IERS_2010, true);
}
