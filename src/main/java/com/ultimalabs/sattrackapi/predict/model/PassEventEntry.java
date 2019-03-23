package com.ultimalabs.sattrackapi.predict.model;

import lombok.Data;

/**
 * Satellite pass details
 */
@Data
public class PassEventEntry {

    /**
     * Date and time for this data item
     */
    private String time;

    /**
     * Azimuth
     */
    private double az;

    /**
     * Elevation
     */
    private double el;

    /**
     * Range, in kilometers
     */
    private int range;

    /**
     * Doppler shift, in Hz
     */
    private int doppler;

}
