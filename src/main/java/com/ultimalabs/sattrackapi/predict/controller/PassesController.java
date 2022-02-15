package com.ultimalabs.sattrackapi.predict.controller;

import com.ultimalabs.sattrackapi.predict.model.SatellitePass;
import com.ultimalabs.sattrackapi.predict.model.dto.ObserverParams;
import com.ultimalabs.sattrackapi.predict.model.dto.TLEParams;
import com.ultimalabs.sattrackapi.predict.service.PredictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;

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
    public SatellitePass passWithoutDetails(

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
        return predictService.getNextEventWithoutDetails(
                new TLEParams(searchString),
                new ObserverParams(lon, lat, alt, minEl)
        );
    }

    @GetMapping("/{searchString}/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/name/{name}/line1/{line1}/line2/{line2}")
    public SatellitePass passWithoutDetailsForCustomTLE(

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

            @PathVariable(required = false) Optional<String> name,
            @PathVariable(required = false) Optional<String> line1,
            @PathVariable(required = false) Optional<String> line2

    ) {
        return predictService.getNextEventWithoutDetails(
                buildTleParamsForCustomTLE(searchString, name, line1, line2),
                new ObserverParams(lon, lat, alt, minEl)
        );
    }

    @GetMapping("/{searchString}/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/step/{stepSize}")
    public SatellitePass passWithDetails(

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

            @DecimalMin(value = "0.01", message = "Step size should not be less than 0.01")
            @PathVariable double stepSize
    ) {
        return predictService.getNextEventWithDetails(
                new TLEParams(searchString),
                new ObserverParams(lon, lat, alt, minEl),
                stepSize
        );
    }

    @GetMapping("/{searchString}/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/step/{stepSize}/name/{name}/line1/{line1}/line2/{line2}")
    public SatellitePass passWithDetailsForCustomTLE(

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

            @DecimalMin(value = "0.01", message = "Step size should not be less than 0.01")
            @PathVariable double stepSize,

            @PathVariable(required = false) Optional<String> name,
            @PathVariable(required = false) Optional<String> line1,
            @PathVariable(required = false) Optional<String> line2
    ) {
        return predictService.getNextEventWithDetails(
                buildTleParamsForCustomTLE(searchString, name, line1, line2),
                new ObserverParams(lon, lat, alt, minEl),
                stepSize
        );
    }

    @GetMapping("/n/{n}/{searchString}/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}")
    public List<SatellitePass> nPassesWithoutDetails(

            @Min(value = 1, message = "Must request at least one next satellite pass")
            @Max(value = 20, message = "To many next satellite passes requested, max number is 20")
            @PathVariable int n,

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
        return predictService.getNextEventsWithoutDetails(
                n,
                new TLEParams(searchString),
                new ObserverParams(lon, lat, alt, minEl)
        );
    }

    @GetMapping("/n/{n}/{searchString}/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/name/{name}/line1/{line1}/line2/{line2}")
    public List<SatellitePass> nPassesWithoutDetailsForCustomTLE(

            @Min(value = 1, message = "Must request at least one next satellite pass")
            @Max(value = 20, message = "To many next satellite passes requested, max number is 20")
            @PathVariable int n,

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

            @PathVariable(required = false) Optional<String> name,
            @PathVariable(required = false) Optional<String> line1,
            @PathVariable(required = false) Optional<String> line2
    ) {
        return predictService.getNextEventsWithoutDetails(
                n,
                buildTleParamsForCustomTLE(searchString, name, line1, line2),
                new ObserverParams(lon, lat, alt, minEl)
        );
    }

    /**
     * Builds the TLEParams object based on custom TLE values
     * @param satelliteIdentifier - Satellite Id
     * @param satelliteName - Satellite name
     * @param tleLine1 - First TLE line
     * @param tleLine2 - Second TLE line
     * @return Built TLEParams object based on the given TLE values
     */
    private TLEParams buildTleParamsForCustomTLE(String satelliteIdentifier, Optional<String> satelliteName, Optional<String> tleLine1, Optional<String> tleLine2) {
        String name = satelliteName.orElse(null);
        String line1 = (tleLine1.isEmpty() || tleLine1.get().equals("null")) ? null : tleLine1.get();
        String line2 = (tleLine2.isEmpty() || tleLine2.get().equals("null")) ? null : tleLine2.get();

        return new TLEParams(satelliteIdentifier, name, line1, line2);
    }
}
