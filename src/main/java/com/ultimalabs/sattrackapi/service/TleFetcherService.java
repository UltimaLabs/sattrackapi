package com.ultimalabs.sattrackapi.service;

import com.ultimalabs.sattrackapi.config.SatTrackConfig;
import com.ultimalabs.sattrackapi.model.RichTLE;
import com.ultimalabs.sattrackapi.util.UrlDataReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Fetches TLEs from remote URLs
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TleFetcherService {

    /**
     * Config object
     */
    private final SatTrackConfig config;

    /**
     * A set of TLE objects
     */
    private Set<RichTLE> tleSet = new HashSet<>();

    /**
     * Fetches TLE data from remote URLs
     */
    @PostConstruct
    @Scheduled(cron = "${sattrack.tleUpdateCron}")
    public void buildTleObjects() {

        List<String> tleUrls = config.getTleUrls();

        List<String> allTleFileData = new ArrayList<>();

        for (String tleUrl : tleUrls) {
            allTleFileData.addAll(UrlDataReader.readStringDataFromUrl(tleUrl));
        }

    }


}
