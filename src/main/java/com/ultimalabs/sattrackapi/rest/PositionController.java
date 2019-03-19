package com.ultimalabs.sattrackapi.rest;

import com.ultimalabs.sattrackapi.position.model.SatellitePosition;
import com.ultimalabs.sattrackapi.position.service.PositionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Positions REST controller
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/positions")
public class PositionController {

    /**
     * Predict service
     */
    private final PositionService positionService;

    @GetMapping("/satNum/{satNum}")
    public SatellitePosition positionBySatNum(@PathVariable int satNum) {
        return positionService.positionBySatelliteNumber(satNum);
    }

    @GetMapping("/id/{designator}/")
    public SatellitePosition positionByInternationalDesignator(@PathVariable String designator) {
        return positionService.positionByInternationalDesignator(designator);
    }

}
