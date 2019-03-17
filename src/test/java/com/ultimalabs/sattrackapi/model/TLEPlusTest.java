package com.ultimalabs.sattrackapi.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class TLEPlusTest {

    private final String validLine1 = "1 25544U 98067A   19072.58486381 -.00000050  00000-0  67055-5 0  9996";
    private final String validLine2 = "2 25544  51.6411 116.5260 0004049 100.8410  14.7809 15.52801380160405";

    private final TLEPlus issWithoutName = new TLEPlus(null,
            "1 25544U 98067A   19072.58486381 -.00000050  00000-0  67055-5 0  9996",
            "2 25544  51.6411 116.5260 0004049 100.8410  14.7809 15.52801380160405"
    );

    private final TLEPlus issWithName = new TLEPlus("ISS (ZARYA)             ",
            "1 25544U 98067A   19072.58486381 -.00000050  00000-0  67055-5 0  9996",
            "2 25544  51.6411 116.5260 0004049 100.8410  14.7809 15.52801380160405"
    );

    @BeforeAll
    static void setup() {
        // Orekit setup: at least a single file, "tai-utc.dat"
        // should be present in "src/test/resources" folder
        File orekitData = new File(".");
        DataProvidersManager manager = DataProvidersManager.getInstance();
        manager.addProvider(new DirectoryCrawler(orekitData));
    }

    @DisplayName("Test null satellite name")
    @Test
    void nullSatelliteName() {
        assertFalse(TLEPlus.isValidSatelliteTitle(null));
    }

    @DisplayName("Test empty satellite name")
    @Test
    void emptySatelliteName() {
        assertFalse(TLEPlus.isValidSatelliteTitle(""));
    }

    @DisplayName("Satellite name can start with '1'")
    @Test
    void SatelliteNameDoesNotStartWith1() {
        assertTrue(TLEPlus.isValidSatelliteTitle("1FOO"));
    }

    @DisplayName("Satellite name can start with '2'")
    @Test
    void SatelliteNameDoesNotStartWith2() {
        assertTrue(TLEPlus.isValidSatelliteTitle("2 BAR"));
    }

    @DisplayName("Valid line1 is not a valid satellite name")
    @Test
    void line1IsNotValidSatelliteName() {
        assertFalse(TLEPlus.isValidSatelliteTitle(validLine1));
    }

    @DisplayName("Valid line2 is not a valid satellite name")
    @Test
    void line2IsNotValidSatelliteName() {
        assertFalse(TLEPlus.isValidSatelliteTitle(validLine2));
    }

    @DisplayName("Test valid satellite name")
    @Test
    void validSatelliteName() {
        assertTrue(TLEPlus.isValidSatelliteTitle("ISS"));
    }

    @DisplayName("Test null tle line")
    @Test
    void nullTleLine() {
        assertFalse(TLEPlus.looksLikeTleLine(null, 1));
    }

    @DisplayName("Test empty tle line")
    @Test
    void emptyTleLine() {
        assertFalse(TLEPlus.looksLikeTleLine("", 1));
    }

    @DisplayName("Test too short tle line")
    @Test
    void shortTleLine() {
        assertFalse(TLEPlus.looksLikeTleLine("1          ", 1));
    }

    @DisplayName("Test valid tle line 1")
    @Test
    void validTleLine1() {
        assertTrue(TLEPlus.looksLikeTleLine(validLine1, 1));
    }

    @DisplayName("Test valid tle line 2")
    @Test
    void validTleLine2() {
        assertTrue(TLEPlus.looksLikeTleLine(validLine2, 2));
    }

    @DisplayName("Test satellite name trimming")
    @Test
    void nameTrim() {
        assertEquals("ISS (ZARYA)", issWithName.getName());
    }

    @DisplayName("Test empty name in constructed TLE")
    @Test
    void emptySatelliteNameInTle() {
        assertEquals("", issWithoutName.getName());
    }

    @DisplayName("Test getting international designator")
    @Test
    void getIntDesignator() {
        assertEquals("98067A", issWithoutName.getInternationalDesignator());
    }

    @DisplayName("Test getting TLE without name")
    @Test
    void getTleWithoutName() {

        final String expected = "1 25544U 98067A   19072.58486381 -.00000050  00000-0  67055-5 0  9996" +
                System.lineSeparator() +
                "2 25544  51.6411 116.5260 0004049 100.8410  14.7809 15.52801380160405";

        assertEquals(expected, issWithoutName.getTle());
    }

    @DisplayName("Test getting TLE with name")
    @Test
    void getTleWithName() {

        final String expected = "ISS (ZARYA)             " +
                System.lineSeparator() +
                "1 25544U 98067A   19072.58486381 -.00000050  00000-0  67055-5 0  9996" +
                System.lineSeparator() +
                "2 25544  51.6411 116.5260 0004049 100.8410  14.7809 15.52801380160405";

        assertEquals(expected, issWithName.getTle());
    }

}