package com.ultimalabs.sattrackapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration loaded from appplication.yml
 */
@Data
@Validated
@Component
@ConfigurationProperties("sattrack")
public class SatTrackConfig {

    /**
     * List of TLE data source URLs
     */
    @NotNull
    private List<String> tleUrls = new ArrayList<>();

}
