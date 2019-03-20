package com.ultimalabs.sattrackapi.common.model;

import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;

/**
 * Earth parameters
 * <p>
 * Holds equatorial radius, flattening and Earth frame
 */
public class EarthParams {

    /**
     * Equatorial radius in meters
     */
    public final static double equatorialRadius = Constants.WGS84_EARTH_EQUATORIAL_RADIUS;

    /**
     * Earth flattening
     */
    public final static double flattening = Constants.WGS84_EARTH_FLATTENING;

    /**
     * Earth frame
     */
    public final static Frame iers2010Frame = FramesFactory.getITRF(IERSConventions.IERS_2010, true);
}
