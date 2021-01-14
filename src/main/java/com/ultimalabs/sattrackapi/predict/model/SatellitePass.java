package com.ultimalabs.sattrackapi.predict.model;

import lombok.Data;

import java.util.List;

/**
 * Event data for a satellite pass
 */
@Data
public class SatellitePass {

    /**
     * TLE used for propagation
     */
    private final String tle;

    /**
     * Current date and time
     */
    private final String now;

    /**
     * Wait time for the rise event (rise - now), in seconds
     */
    private final Double wait;

    /**
     * Satellite rise time data point
     */
    private final PassEventDataPoint risePoint;

    /**
     * Pass midpoint
     */
    private final PassEventDataPoint midPoint;

    /**
     * Satellite set time data point
     */
    private final PassEventDataPoint setPoint;

    /**
     * Pass duration, in seconds
     */
    private final double duration;

    /**
     * Pass event entries
     */
    private final List<PassEventDataPoint> eventDetails;

}
