package com.ultimalabs.sattrackapi.rest;

import com.ultimalabs.sattrackapi.predict.service.PredictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Passes REST controller
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/passes")
public class PassesController {

    /**
     * Predict service
     */
    private final PredictService predictService;

    @GetMapping("/{searchString}/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}")
    public String visibilityByInternationalDesignator(@PathVariable String searchString,
                                                      @PathVariable double lat, @PathVariable double lon,
                                                      @PathVariable double alt, @PathVariable double minEl) {

        return predictService.getVisibility(searchString, lat, lon, alt, minEl);
    }

}
