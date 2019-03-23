package com.ultimalabs.sattrackapi.predict.model;

import lombok.Data;

/**
 * Satellite pass details
 */
@Data
public class PassEventDetailsEntry {

    /**
     * Date and time for this data item
     */
    private final String t;

    /**
     * Azimuth
     */
    private final double az;

    /**
     * Elevation
     */
    private final double el;

    /**
     * Range, in kilometers
     */
    private final double rng;

    /**
     * Doppler shift, in Hz
     */
    private final double dop;

}
