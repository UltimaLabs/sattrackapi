package com.ultimalabs.sattrackapi.service;

import com.ultimalabs.sattrackapi.config.SatTrackConfig;
import com.ultimalabs.sattrackapi.util.UrlDataReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.util.List;

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
     * Raw TLE data - concatenation of all the TLE files
     */
    private String rawTleData;

    /**
     * Fetches TLE data from remote URLs
     */
    @PostConstruct
    @Scheduled(cron = "${sattrack.tleUpdateCron}")
    public void fetchTleData() {

        List<String> tleUrls = config.getTleUrls();

        StringBuilder allTleFileData = new StringBuilder();

        for (String tleUrl : tleUrls) {
            String content = UrlDataReader.readStringFromUrl(tleUrl);
            if (tleUrl.equals("https://download.ultimalabs.com/files/tle/other.txt")) {
                log.info("Content of 'others.txt':\n" + content);
            }
            allTleFileData.append(UrlDataReader.readStringFromUrl(tleUrl));
        }

        rawTleData = allTleFileData.toString();

    }

}
