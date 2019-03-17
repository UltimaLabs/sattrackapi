package com.ultimalabs.sattrackapi.util;

import com.ultimalabs.sattrackapi.model.TLEPlus;
import com.ultimalabs.sattrackapi.model.TleDataStore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TleDataStoreBuilderTest {

    private final TleDataStore issWithoutName = TleDataStoreBuilder.buildTleMaps(Arrays.asList(
            "1 25544U 98067A   19072.58486381 -.00000050  00000-0  67055-5 0  9996",
            "2 25544  51.6411 116.5260 0004049 100.8410  14.7809 15.52801380160405"
    ));

    private TleDataStore issWithName = TleDataStoreBuilder.buildTleMaps(Arrays.asList(
            "ISS (ZARYA)             ",
            "1 25544U 98067A   19072.58486381 -.00000050  00000-0  67055-5 0  9996",
            "2 25544  51.6411 116.5260 0004049 100.8410  14.7809 15.52801380160405"
    ));

    private TleDataStore issInvalid1 = TleDataStoreBuilder.buildTleMaps(Collections.singletonList(
            "1 25544U 98067A   19072.58486381 -.00000050  00000-0  67055-5 0  9996"
    ));

    private TleDataStore issInvalid2 = TleDataStoreBuilder.buildTleMaps(Collections.singletonList(
            "2 25544  51.6411 116.5260 0004049 100.8410  14.7809 15.52801380160405"
    ));

    private TleDataStore onlyNoaa18Valid = TleDataStoreBuilder.buildTleMaps(Arrays.asList(
            "1 25544U 98067A   19072.58486381 -.00000050  00000-0  67055-5 0  9996",
            "NOAA 18 [B]             ",
            "1 28654U 05018A   19072.51504762  .00000017  00000-0  34275-4 0  9991",
            "2 28654  99.1102 117.9653 0015254  91.8549 268.4370 14.12409019711802",
            "NOAA 19 [+]             ",
            "2 33591  99.1742  61.2940 0013053 292.9494  67.0299 14.12338669520180"
    ));

    @BeforeAll
    static void setup() {
        // Orekit setup: at least a single file, "tai-utc.dat"
        // should be present in "src/test/resources" folder
        File orekitData = new File(".");
        DataProvidersManager manager = DataProvidersManager.getInstance();
        manager.addProvider(new DirectoryCrawler(orekitData));
    }

    @DisplayName("Single TLE without name, count")
    @Test
    void singleTleWithoutNameCount() {
        assertEquals(1, issWithoutName.getTleMapBySatelliteId().size());
    }

    @DisplayName("Single TLE with name, count")
    @Test
    void singleTleWithNameCount() {
        assertEquals(1, issWithName.getTleMapBySatelliteId().size());
    }

    @DisplayName("Test null parameter")
    @Test
    void testNullParameter() {
        assertNull(TleDataStoreBuilder.buildTleMaps(null));
    }

    @DisplayName("Test empty list parameter")
    @Test
    void testEmptyListParameter() {
        assertNull(TleDataStoreBuilder.buildTleMaps(new LinkedList<>()));
    }

    @DisplayName("No valid TLEs 1")
    @Test
    void noValidTles1() {
        assertNull(issInvalid1);
    }

    @DisplayName("No valid TLEs 2")
    @Test
    void noValidTles2() {
        assertNull(issInvalid2);
    }

    @DisplayName("Just a single valid TLE")
    @Test
    void singleValidTle() {
        assertEquals(1, onlyNoaa18Valid.getTleMapBySatelliteId().size());
    }

    @DisplayName("No invalid TLEs 1")
    @Test
    void NoInvalidTles1() {
        assertNull(onlyNoaa18Valid.getTleMapBySatelliteId().get(25544));
    }

    @DisplayName("No invalid TLEs 2")
    @Test
    void NoInvalidTles2() {
        assertNull(onlyNoaa18Valid.getTleMapBySatelliteId().get(33591));
    }

    @DisplayName("Fetch TLE by Satellite Number")
    @Test
    void fetchBySatId() {
        assertEquals(25544, issWithoutName.getTleMapBySatelliteId().get(25544).getSatelliteNumber());
    }

    @DisplayName("Fetch TLE by International Designator")
    @Test
    void fetchByIntDesignator() {
        assertEquals(25544, issWithoutName.getTleMapByInternationalDesignator().get("98067A").getSatelliteNumber());
    }

    @DisplayName("Compare TLE's fetched by Satellite Id and International Designator")
    @Test
    void compareTles1() {
        TLEPlus byId = issWithoutName.getTleMapBySatelliteId().get(25544);
        TLEPlus byIntDesignator = issWithoutName.getTleMapByInternationalDesignator().get("98067A");

        assertEquals(byId, byIntDesignator);

    }

}