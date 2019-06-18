package com.ultimalabs.sattrackapi.position.controller;

import com.ultimalabs.sattrackapi.position.model.SatellitePosition;
import com.ultimalabs.sattrackapi.position.service.PositionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;

/**
 * Positions REST controller
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/positions")
public class PositionController {

    /**
     * Position service
     */
    private final PositionService positionService;

    @GetMapping("/{searchString}")
    public SatellitePosition getPosition(
            @Size(min = 5, max = 11, message = "Satellite identifier must be between 5 and 11 characters long")
            @PathVariable String searchString) {
        return positionService.getPosition(searchString);
    }

}
