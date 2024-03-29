package com.ultimalabs.sattrackapi.tle.service;

import com.ultimalabs.sattrackapi.common.config.SatTrackConfig;
import com.ultimalabs.sattrackapi.common.util.UrlDataReader;
import com.ultimalabs.sattrackapi.tle.model.TLEPlus;
import com.ultimalabs.sattrackapi.tle.model.TleDataStore;
import com.ultimalabs.sattrackapi.tle.util.TleDataStoreBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Fetches TLE data
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TleFetcherServiceImpl implements TleFetcherService {

    /**
     * Store of TLE objects
     */
    private TleDataStore tleStore;

    /**
     * Config object
     */
    private final SatTrackConfig config;

    /**
     * Search for TLE
     * <p>
     * Searches for TLE based on search string which can contain
     * either Satellite Number or an International Designator (shorter
     * variant with two-digit year without dash or a four-digit year
     * with dash).
     *
     * @param searchString Satellite Number or International designator
     * @return TLE or null if TLE was not found
     */
    @Override
    public TLEPlus getTle(String searchString) {

        TLEPlus foundTle = null;

        // Satellite Number
        if (TleFetcherServiceImpl.isInteger(searchString)) {
            foundTle = getTleBySatelliteId(Integer.parseInt(searchString));
        }

        // International Designator
        if (searchString.length() >= 6) {

            // longer International Designator variant, maybe
            if (searchString.charAt(4) == '-') {
                String shortDesignator = searchString.substring(2, 4) + searchString.substring(5);
                foundTle = getTleByInternationalDesignator(shortDesignator);
            } else {
                // shorter International Designator variant
                foundTle = getTleByInternationalDesignator(searchString);
            }
        }


        if (foundTle == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No satellite matching the given identifier was found.");
        }

        return foundTle;


    }

    /**
     * Get the TLE by Satellite Catalog Number
     *
     * @param id Satellite Catalog Number
     * @return TLE or null if Catalog Number was not found
     */
    @Override
    public TLEPlus getTleBySatelliteId(int id) {
        if (tleStore == null) {
            return null;
        }
        return tleStore.getTleMapBySatelliteId().get(id);
    }

    /**
     * Get the TLE by International Designator
     *
     * @param designator International Designator
     * @return TLE or null if International Designator was not found
     */
    @Override
    public TLEPlus getTleByInternationalDesignator(String designator) {
        if (tleStore == null) {
            return null;
        }
        return tleStore.getTleMapByInternationalDesignator().get(designator);
    }

    /**
     * Orekit initialization and intial TLE data fetch
     * <p>
     * For Orekit initialization part, see:
     * https://www.orekit.org/forge/projects/orekit/wiki/Configuration
     */
    @PostConstruct
    private void init() {

        tleStore = null;

        File orekitData = new File(config.getOrekitDataFolder());
        DataProvidersManager manager = DataProvidersManager.getInstance();
        manager.addProvider(new DirectoryCrawler(orekitData));

        refreshTleData();
    }

    /**
     * Initializes or refreshes TLE data
     */
    @Scheduled(cron = "${sattrack.tleUpdateCron}")
    private void refreshTleData() {

        List<String> tleTextData = new ArrayList<>();

        for (String tleUrl : config.getTleUrls()) {

            List<String> tleStrings = UrlDataReader.readStringDataFromUrl(tleUrl);

            if (tleStrings.isEmpty()) {
                log.error("There was an error fetching TLE data from " + tleUrl + ". TLE data refresh canceled.");
                return;
            }

            tleTextData.addAll(tleStrings);
        }

        tleStore = TleDataStoreBuilder.buildTleMaps(tleTextData);

    }

    /**
     * Checks whether string contains a valid non-negative integer
     *
     * @param str string to check
     * @return true if string is an integer, false otherwise
     */
    private static boolean isInteger(String str) {

        if (str == null) {
            return false;
        }

        if (str.isEmpty()) {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }

        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

}
