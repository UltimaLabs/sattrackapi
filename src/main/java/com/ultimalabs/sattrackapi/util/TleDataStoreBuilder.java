package com.ultimalabs.sattrackapi.util;

import com.ultimalabs.sattrackapi.model.NamedTLE;
import com.ultimalabs.sattrackapi.model.TleDataStore;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds a TLE data store
 */
@Slf4j
public class TleDataStoreBuilder {

    private TleDataStoreBuilder() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Builds TLE data store from a list of strings
     *
     * @param tleTextData TLE data as a list of strings
     * @return built TLE data store
     */
    public static TleDataStore buildTleMaps(List<String> tleTextData) {

        if (tleTextData == null || tleTextData.isEmpty()) {
            return null;
        }

        Map<Integer, NamedTLE> tleBySatelliteId = new HashMap<>();
        Map<String, NamedTLE> tleByInternationalDesignator = new HashMap<>();

        String line;
        String lineMinus1;
        String lineMinus2;

        int numContructedTles = 0;

        // we're looking for a valid line2 and then working our way back
        // to fetch the data for a valid TLE
        for (int i = 0; i < tleTextData.size(); i++) {

            line = tleTextData.get(i);

            if (!NamedTLE.looksLikeTleLine(line, 2) || i == 0) {
                continue;
            }

            lineMinus1 = tleTextData.get(i - 1);

            if (i > 1) {
                lineMinus2 = tleTextData.get(i - 2);
            } else {
                lineMinus2 = null;
            }

            NamedTLE singleTle = buildSingleTle(lineMinus2, lineMinus1, line);

            if (singleTle != null) {

                numContructedTles++;

                tleBySatelliteId.put(singleTle.getSatelliteNumber(), singleTle);

                // Orekit's native methods apparently return wrong values so
                // we're pulling intDesignator from text data
                String intDesignator = lineMinus1.substring(9, 16).trim();
                tleByInternationalDesignator.put(intDesignator, singleTle);
            }

        }

        if (tleBySatelliteId.isEmpty() || tleByInternationalDesignator.isEmpty()) {
            return null;
        }

        log.info("Number of constructed TLEs: " + numContructedTles);

        return new TleDataStore(tleBySatelliteId, tleByInternationalDesignator);

    }

    /**
     * Builds a single NamedTLE object
     *
     * @param satName satellite name
     * @param line1   TLE line1
     * @param line2   TLE line2
     * @return TLE object or null in case of error
     */
    private static NamedTLE buildSingleTle(String satName, String line1, String line2) {

        if (!NamedTLE.looksLikeTleLine(line1, 1) || !NamedTLE.looksLikeTleLine(line2, 2)) {
            return null;
        }

        if (!NamedTLE.isFormatOK(line1, line2)) {
            return null;
        }

        if (satName != null) {
            satName = satName.trim();
        }

        if (!NamedTLE.isValidSatelliteName(satName)) {
            return new NamedTLE(null, line1, line2);
        }

        return new NamedTLE(satName, line1, line2);

    }

}
