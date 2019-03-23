package com.ultimalabs.sattrackapi.common.util;

import org.apache.commons.validator.routines.UrlValidator;

/**
 * URL validation utility class
 */
class UrlValidation {
    private static String[] schemes = {"http", "https"};
    private static UrlValidator urlValidator = new UrlValidator(schemes);

    private UrlValidation() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Validation of a string URL
     *
     * @param url URL passed as a String
     * @return true if URL is valid
     */
    static boolean isValid(String url) {

        return urlValidator.isValid(url);

    }

}
