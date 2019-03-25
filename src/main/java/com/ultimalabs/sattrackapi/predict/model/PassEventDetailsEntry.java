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
     * Distance, in meters
     */
    private final double dst;

    /**
     * Doppler shift, in Hz
     */
    private final double dop;

}
