package com.ultimalabs.sattrackapi.rest;

import com.ultimalabs.sattrackapi.predict.service.PredictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Predict REST controller
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/events")
public class PredictController {

    /**
     * Predict service
     */
    private final PredictService predictService;

    @GetMapping("/satNum/{satNum}/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}")
    public String visibilityBySatNum(@PathVariable int satNum, @PathVariable double lat,
                                     @PathVariable double lon, @PathVariable double alt,
                                     @PathVariable double minEl) {
        return predictService.visibilityBySatelliteNumber(satNum, lat, lon, alt, minEl);
    }

    @GetMapping("/id/{designator}/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}")
    public String visibilityByInternationalDesignator(@PathVariable String designator,
                                                      @PathVariable double lat, @PathVariable double lon,
                                                      @PathVariable double alt, @PathVariable double minEl) {

        return predictService.visibilityByInternationalDesignator(designator, lat, lon, alt, minEl);
    }

}
