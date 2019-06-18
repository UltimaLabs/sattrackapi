package com.ultimalabs.sattrackapi.predict.controller;

import com.ultimalabs.sattrackapi.predict.model.PassEventData;
import com.ultimalabs.sattrackapi.predict.service.PredictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * Passes REST controller
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/passes")
public class PassesController {

    /**
     * Predict service
     */
    private final PredictService predictService;

    @GetMapping("/{searchString}/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}")
    public PassEventData passWithoutDetails(

            @Size(min = 5, max = 11, message = "Satellite identifier must be between 5 and 11 characters long")
            @PathVariable String searchString,

            @Min(value = -90, message = "Latitude should not be less than -90")
            @Max(value = 90, message = "Latitude should not be greater than 90")
            @PathVariable double lat,

            @Min(value = -90, message = "Longitude should not be less than -90")
            @Max(value = 90, message = "Longitude should not be greater than 90")
            @PathVariable double lon,

            @Min(value = 0, message = "Altitude should be greater or equal to zero")
            @Max(value = Integer.MAX_VALUE, message = "Altitude value is too large")
            @PathVariable double alt,

            @Min(value = 0, message = "Elevation should not be less than 0")
            @Max(value = 90, message = "Elevation should not be greater than 90")
            @PathVariable double minEl

    ) {
        return predictService.getNextEventWithoutDetails(searchString, lat, lon, alt, minEl);
    }

    @GetMapping("/{searchString}/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/step/{stepSize}")
    public PassEventData passWithDetails(

            @Size(min = 5, max = 11, message = "Satellite identifier must be between 5 and 11 characters long")
            @PathVariable String searchString,

            @Min(value = -90, message = "Latitude should not be less than -90")
            @Max(value = 90, message = "Latitude should not be greater than 90")
            @PathVariable double lat,

            @Min(value = -90, message = "Longitude should not be less than -90")
            @Max(value = 90, message = "Longitude should not be greater than 90")

            @PathVariable double lon,
            @Min(value = 0, message = "Altitude should be greater or equal to zero")
            @Max(value = Integer.MAX_VALUE, message = "Altitude value is too large")

            @PathVariable double alt,
            @Min(value = 0, message = "Elevation should not be less than 0")
            @Max(value = 90, message = "Elevation should not be greater than 90")

            @PathVariable double minEl,
            @Min(value = 0, message = "Step size should not be less than 0.01")
            @PathVariable double stepSize

    ) {
        return predictService.getNextEventWithDetails(searchString, lat, lon, alt, minEl, stepSize);
    }

}
