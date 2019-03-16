package com.ultimalabs.sattrackapi.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NamedTLETest {

    private final String validLine1 = "1 25544U 98067A   19072.58486381 -.00000050  00000-0  67055-5 0  9996";
    private final String validLine2 = "2 25544  51.6411 116.5260 0004049 100.8410  14.7809 15.52801380160405";

    @DisplayName("Test null satellite name")
    @Test
    void nullSatelliteName() {
        assertFalse(NamedTLE.isValidSatelliteName(null));
    }

    @DisplayName("Test empty satellite name")
    @Test
    void emptySatelliteName() {
        assertFalse(NamedTLE.isValidSatelliteName(""));
    }

    @DisplayName("Satellite name can start with '1'")
    @Test
    void SatelliteNameDoesNotStartWith1() {
        assertTrue(NamedTLE.isValidSatelliteName("1FOO"));
    }

    @DisplayName("Satellite name can start with '2'")
    @Test
    void SatelliteNameDoesNotStartWith2() {
        assertTrue(NamedTLE.isValidSatelliteName("2 BAR"));
    }

    @DisplayName("Valid line1 is not a valid satellite name")
    @Test
    void line1IsNotValidSatelliteName() {
        assertFalse(NamedTLE.isValidSatelliteName(validLine1));
    }

    @DisplayName("Valid line2 is not a valid satellite name")
    @Test
    void line2IsNotValidSatelliteName() {
        assertFalse(NamedTLE.isValidSatelliteName(validLine2));
    }

    @DisplayName("Test valid satellite name")
    @Test
    void validSatelliteName() {
        assertTrue(NamedTLE.isValidSatelliteName("ISS"));
    }

    @DisplayName("Test null tle line")
    @Test
    void nullTleLine() {
        assertFalse(NamedTLE.looksLikeTleLine(null, 1));
    }

    @DisplayName("Test empty tle line")
    @Test
    void emptyTleLine() {
        assertFalse(NamedTLE.looksLikeTleLine("", 1));
    }

    @DisplayName("Test too short tle line")
    @Test
    void shortTleLine() {
        assertFalse(NamedTLE.looksLikeTleLine("1          ", 1));
    }

    @DisplayName("Test valid tle line 1")
    @Test
    void validTleLine1() {
        assertTrue(NamedTLE.looksLikeTleLine(validLine1, 1));
    }

    @DisplayName("Test valid tle line 2")
    @Test
    void validTleLine2() {
        assertTrue(NamedTLE.looksLikeTleLine(validLine2, 2));
    }

}