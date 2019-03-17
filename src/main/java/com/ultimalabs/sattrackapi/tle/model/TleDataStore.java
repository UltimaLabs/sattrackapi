package com.ultimalabs.sattrackapi.tle.model;

import lombok.Data;

import java.util.Map;

/**
 * Data store for TLE HashMaps
 */
@Data
public class TleDataStore {

    /**
     * Map of TLE objects, indexed by Satellite Catalog Number
     */
    private final Map<Integer, TLEPlus> tleMapBySatelliteId;

    /**
     * Map of TLE objects, indexed by International Designator
     */
    private final Map<String, TLEPlus> tleMapByInternationalDesignator;

}
