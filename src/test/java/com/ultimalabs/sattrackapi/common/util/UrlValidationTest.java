package com.ultimalabs.sattrackapi.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UrlValidationTest {

    @DisplayName("Test valid HTTPS String URL")
    @Test
    void validStringUrl1() {
        assertTrue(UrlValidation.isValid("https://www.ultimalabs.com"));
    }

    @DisplayName("Test valid HTTP String URL")
    @Test
    void validStringUrl2() {
        assertTrue(UrlValidation.isValid("http://www.ultimalabs.com"));
    }

    @DisplayName("Test invalid String URL 1")
    @Test
    void invalidStringUrl1() {
        assertFalse(UrlValidation.isValid(""));
    }

    @DisplayName("Test invalid String URL 2")
    @Test
    void invalidStringUrl2() {
        assertFalse(UrlValidation.isValid("something"));
    }

    @DisplayName("Test invalid FTP String URL")
    @Test
    void invalidFtpStringUrl2() {
        assertFalse(UrlValidation.isValid("ftp://ftp.ultimalabs.com"));
    }

}