package com.ultimalabs.sattrackapi.position.model;

import lombok.Data;

/**
 * Holds satellite position coordinates
 */
@Data
public class SatellitePosition {

    /**
     * Latitude
     */
    private final double lat;

    /**
     * Longitude
     */
    private final double lon;

}
