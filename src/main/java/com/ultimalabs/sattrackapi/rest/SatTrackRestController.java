package com.ultimalabs.sattrackapi.rest;

import com.ultimalabs.sattrackapi.config.SatTrackConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Sat Track REST controller
 * 
 * @author Darko Topolko
 */
@Log
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class SatTrackRestController {

    /**
     * Config object
     */
    private final SatTrackConfig config;

    @GetMapping("/hello")
    public String testController() {
        return "Hello there";
    }

}
