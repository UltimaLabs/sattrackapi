package com.ultimalabs.sattrackapi.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlDataReaderTest {

    @DisplayName("Test reading data from an invalid URL")
    @Test
    void readStringFromInvalidUrl1() {
        assertNull(UrlDataReader.readStringFromUrl(""));
    }

    @DisplayName("Test reading data from a nonexistent domain")
    @Test
    void readStringFromInvalidUrl2() {
        assertNull(UrlDataReader.readStringFromUrl("http://www.this-domain-most-probably-does-not-exist.com/"));
    }

    @DisplayName("Test reading data with 404 response")
    @Test
    void readStringFromInvalidUrl3() {
        assertNull(UrlDataReader.readStringFromUrl("https://www.ultimalabs.com/nosuchfile.txt"));
    }

    @DisplayName("Test reading a valid resource")
    @Test
    void readStringFromInvalidUrl4() {
        assertTrue(UrlDataReader.readStringFromUrl("https://download.ultimalabs.com/files/tle/galileo.txt").length() > 1024);
    }

}