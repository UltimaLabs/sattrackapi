package com.ultimalabs.sattrackapi.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * URL data reader utility class
 */
@Slf4j
public class UrlDataReader {

    private UrlDataReader() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Read string data from an URL
     *
     * @param dataUrl data URL
     * @return contents of a file given by URL
     */
    public static String readStringFromUrl(String dataUrl) {

        // create the url
        if (!UrlValidation.isValid(dataUrl)) {
            log.error("Invalid URL: " + dataUrl);
            return null;
        }

        URL url;

        try {
            url = new URL(dataUrl);
        } catch (MalformedURLException e) {
            log.error("Invalid URL " + dataUrl + ": " + e);
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
                result.append(System.lineSeparator());
            }
            log.info("Successfully read data from " + dataUrl);
            return result.toString();
        } catch (IOException e) {
            log.error("There was an error reading data from " + dataUrl + ": " + e);
            return null;
        }

    }

}
