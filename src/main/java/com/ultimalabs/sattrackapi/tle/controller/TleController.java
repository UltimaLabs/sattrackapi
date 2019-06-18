package com.ultimalabs.sattrackapi.tle.controller;

import com.ultimalabs.sattrackapi.tle.model.TLEPlus;
import com.ultimalabs.sattrackapi.tle.service.TleFetcherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;

/**
 * TLE REST controller
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/tles")
public class TleController {

    /**
     * TLE fetcher service
     */
    private final TleFetcherService tleFetcherService;

    @GetMapping("/{searchString}")
    public String getTleByNumber(
            @Size(min = 5, max = 11, message = "Satellite identifier must be between 5 and 11 characters long")
            @PathVariable String searchString) {
        TLEPlus tle = tleFetcherService.getTle(searchString);
        return tle.getTle();
    }
}
