package com.ultimalabs.sattrackapi.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ultimalabs.sattrackapi.config.SatTrackConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

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

    @GetMapping("/test")
    public String testController() {
	return "Hello world";
    }

}
