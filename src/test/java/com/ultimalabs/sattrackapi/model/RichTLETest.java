package com.ultimalabs.sattrackapi.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RichTLETest {

    @DisplayName("Test null satellite name")
    @Test
    void nullSatelliteName() {
        assertFalse(RichTLE.isValidSatelliteName(null));
    }

    @DisplayName("Test empty satellite name")
    @Test
    void emptySatelliteName() {
        assertFalse(RichTLE.isValidSatelliteName(""));
    }

    @DisplayName("Satellite name can't start with '1'")
    @Test
    void SatelliteNameDoesNotStartWith1() {
        assertFalse(RichTLE.isValidSatelliteName("1FOO"));
    }

    @DisplayName("Satellite name can't start with '2'")
    @Test
    void SatelliteNameDoesNotStartWith2() {
        assertFalse(RichTLE.isValidSatelliteName("2 BAR"));
    }

    @DisplayName("Test valid satellite name")
    @Test
    void validSatelliteName() {
        assertTrue(RichTLE.isValidSatelliteName("ISS"));
    }

    @DisplayName("Test null tle line")
    @Test
    void nullTleLine() {
        assertFalse(RichTLE.looksLikeTleLine(null, 1));
    }

    @DisplayName("Test empty tle line")
    @Test
    void emptyTleLine() {
        assertFalse(RichTLE.looksLikeTleLine("", 1));
    }

    @DisplayName("Test too short tle line")
    @Test
    void shortTleLine() {
        assertFalse(RichTLE.looksLikeTleLine("1          ", 1));
    }

    @DisplayName("Test valid tle line 1")
    @Test
    void validTleLine1() {
        assertTrue(RichTLE.looksLikeTleLine("1 25544U 98067A   19072.58486381 -.00000050  00000-0  67055-5 0  9996", 1));
    }

    @DisplayName("Test valid tle line 2")
    @Test
    void validTleLine2() {
        assertTrue(RichTLE.looksLikeTleLine("2 25544  51.6411 116.5260 0004049 100.8410  14.7809 15.52801380160405", 2));
    }

}