package com.ultimalabs.sattrackapi.rest;

import com.ultimalabs.sattrackapi.predict.service.PredictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Sat Track REST controller
 *
 * @author Darko Topolko
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/predict")
public class PredictController {

    /**
     * Predict service
     */
    private final PredictService predictService;

    @GetMapping("/satelliteNumber/{id}/lon/{lon}/lat/{lat}/alt/{alt}/minEl/{minEl}")
    public String visibilityBySatNum(@PathVariable int id, @PathVariable double lon,
                                     @PathVariable double lat, @PathVariable double alt,
                                     @PathVariable double minEl) {
        predictService.visibilityBySatelliteNumber(id, lon, lat, alt, minEl);
        return "Ok.";

    }

    @GetMapping("/internationalDesignator/{designator}/lon/{lon}/lat/{lat}/alt/{alt}/minEl/{minEl}")
    public String visibilityByInternationalDesignator(@PathVariable String designator,
                                                      @PathVariable double lon, @PathVariable double lat,
                                                      @PathVariable double alt, @PathVariable double minEl) {

        predictService.visibilityByInternationalDesignator(designator, lon, lat, alt, minEl);
        return "Ok.";
    }

}
