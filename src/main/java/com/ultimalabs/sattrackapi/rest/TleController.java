package com.ultimalabs.sattrackapi.rest;

import com.ultimalabs.sattrackapi.tle.model.TLEPlus;
import com.ultimalabs.sattrackapi.tle.service.TleFetcherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Sat Track REST controller
 *
 * @author Darko Topolko
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tle")
public class TleController {

    /**
     * TLE fetcher service
     */
    private final TleFetcherService tleFetcherService;

    @GetMapping("/satelliteNumber/{id}")
    public String getTleByNumber(@PathVariable int id) {
        TLEPlus tle = tleFetcherService.getTleBySatelliteId(id);

        if (tle == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return tle.getTle();

    }

    @GetMapping("/internationalDesignator/{designator}")
    public String getTleByInternationalDesignator(@PathVariable String designator) {

        TLEPlus tle = tleFetcherService.getTleByInternationalDesignator(designator);

        if (tle == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return tle.getTle();
    }

}
