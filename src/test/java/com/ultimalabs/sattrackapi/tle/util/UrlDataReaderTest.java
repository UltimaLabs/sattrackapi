package com.ultimalabs.sattrackapi.tle.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UrlDataReaderTest {

    @DisplayName("Test reading data from an invalid URL")
    @Test
    void readStringFromInvalidUrl1() {
        assertTrue(UrlDataReader.readStringDataFromUrl("").isEmpty());
    }

    @DisplayName("Test reading data from a nonexistent domain")
    @Test
    void readStringFromInvalidUrl2() {
        assertTrue(UrlDataReader.readStringDataFromUrl("http://www.this-domain-most-probably-does-not-exist.com/").isEmpty());
    }

    @DisplayName("Test reading data with 404 response")
    @Test
    void readStringFromInvalidUrl3() {
        assertTrue(UrlDataReader.readStringDataFromUrl("https://www.ultimalabs.com/nosuchfile.txt").isEmpty());
    }

    @DisplayName("Test reading a valid resource")
    @Test
    void readStringFromInvalidUrl4() {
        assertFalse(UrlDataReader.readStringDataFromUrl("https://download.ultimalabs.com/files/tle/galileo.txt").isEmpty());
    }

}