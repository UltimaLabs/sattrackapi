package com.ultimalabs.sattrackapi.service;

import com.ultimalabs.sattrackapi.config.SatTrackConfig;
import com.ultimalabs.sattrackapi.model.NamedTLE;
import com.ultimalabs.sattrackapi.model.TleDataStore;
import com.ultimalabs.sattrackapi.util.TleDataStoreBuilder;
import com.ultimalabs.sattrackapi.util.UrlDataReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
     * Get the TLE by Satellite Catalog Number
     *
     * @param id Satellite Catalog Number
     * @return TLE or null if Catalog Number was not found
     */
    @Override
    public NamedTLE getTleBySatelliteId(int id) {
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
    public NamedTLE getTleByInternationalDesignator(String designator) {
        if (tleStore == null) {
            return null;
        }

        return tleStore.getTleMapBySatelliteId().get(designator);
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
            tleTextData.addAll(UrlDataReader.readStringDataFromUrl(tleUrl));
        }

        tleStore = TleDataStoreBuilder.buildTleMaps(tleTextData);

    }

}
