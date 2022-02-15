package com.ultimalabs.sattrackapi.predict.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class PassesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final int iss = 25544;
    private final double lat = 46.1613;
    private final double lon = 15.7534;
    private final int alt = 200;
    private final int minEl = 25;
    private final int step = 30;

    private final String passesWithouDetailsUrl = "/api/v1/passes/{searchString}/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}";
    private final String passesWithDetailsUrl = "/api/v1/passes/{searchString}/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/step/{step}";
    private final String npassesWithouDetailsUrl = "/api/v1/passes/n/5/{searchString}/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}";
    private final String passesWithouDetailsCustomTLEUrl = "/api/v1/passes/{searchString}/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/name/{name}/line1/{line1}/line2/{line2}";
    private final String passesWithDetailCustomTLEsUrl = "/api/v1/passes/{searchString}/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/step/{step}/name/{name}/line1/{line1}/line2/{line2}";
    private final String npassesWithouDetailsCustomTLEUrl = "/api/v1/passes/n/5/{searchString}/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/name/{name}/line1/{line1}/line2/{line2}";
    private String validTleLine1 = "1 00001U 98067A   08264.51782528 -.00002182  00000-0 -11606-4 0  2927";
    private String validTleLine2 = "2 00001  51.6416 247.4627 0006703 130.5360 325.0288 15.72125391563537";

    @DisplayName("Pass without details by Satellite Number - OK")
    @Test
    public void getValidTle1() throws Exception {
        this.mockMvc.perform(get(passesWithouDetailsUrl,
                iss, lat, lon, alt, minEl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Pass without details by International Designator (short) - OK")
    @Test
    public void getValidTle2() throws Exception {
        this.mockMvc.perform(get(passesWithouDetailsUrl,
                "98067A", lat, lon, alt, minEl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Pass without details by International Designator (long) - OK")
    @Test
    public void getValidTle3() throws Exception {
        this.mockMvc.perform(get(passesWithouDetailsUrl,
                "1998-067A", lat, lon, alt, minEl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Pass without details - 404 Not Found 1")
    @Test
    public void getNonexistingTle1() throws Exception {
        this.mockMvc.perform(get(passesWithouDetailsUrl,
                "99999", lat, lon, alt, minEl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Pass without details - 404 Not Found 2")
    @Test
    public void getNonexistingTle2() throws Exception {
        this.mockMvc.perform(get(passesWithouDetailsUrl,
                "ABAB-097Aaa", lat, lon, alt, minEl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Pass without details - 400 Bad Request, search string too short")
    @Test
    public void getTleShortSatId() throws Exception {
        this.mockMvc.perform(get(passesWithouDetailsUrl,
                "111", lat, lon, alt, minEl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass without details - 400 Bad Request, search string too long")
    @Test
    public void getTleLongSatId() throws Exception {
        this.mockMvc.perform(get(
                passesWithouDetailsUrl,
                "999999999999999", lat, lon, alt, minEl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass with details by Satellite Number - OK")
    @Test
    public void getValidTle1WithDetails() throws Exception {
        this.mockMvc.perform(get(passesWithouDetailsUrl,
                iss, lat, lon, alt, minEl, step)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Pass with details by International Designator (short) - OK")
    @Test
    public void getValidTle2WithDetails() throws Exception {
        this.mockMvc.perform(get(passesWithDetailsUrl,
                "98067A", lat, lon, alt, minEl, step)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Pass with details by International Designator (long) - OK")
    @Test
    public void getValidTle3WithDetails() throws Exception {
        this.mockMvc.perform(get(passesWithDetailsUrl,
                "1998-067A", lat, lon, alt, minEl, step)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Next n passes without details - OK")
    @Test
    void getNextNPasses() throws Exception {
        this.mockMvc.perform(get(npassesWithouDetailsUrl,
                "1998-067A", lat, lon, alt, minEl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Pass with details - 404 Not Found 1")
    @Test
    public void getNonexistingTle1WithDetails() throws Exception {
        this.mockMvc.perform(get(passesWithDetailsUrl,
                "99999", lat, lon, alt, minEl, step)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Pass with details - 404 Not Found 2")
    @Test
    public void getNonexistingTle2WithDetails() throws Exception {
        this.mockMvc.perform(get(passesWithDetailsUrl,
                "ABAB-097Aaa", lat, lon, alt, minEl, step)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Pass with details - 400 Bad Request, search string too short")
    @Test
    public void getTleShortSatIdWithDetails() throws Exception {
        this.mockMvc.perform(get(passesWithDetailsUrl,
                "111", lat, lon, alt, minEl, step)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass with details - 400 Bad Request, search string too long")
    @Test
    public void getTleLongSatIdWithDetails() throws Exception {
        this.mockMvc.perform(get(passesWithDetailsUrl,
                "999999999999999", lat, lon, alt, minEl, step)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass without details invalid lat 1")
    @Test
    public void passWithoutDetailsInvalidLat1() throws Exception {
        this.mockMvc.perform(get(passesWithouDetailsUrl,
                iss, -120, lon, alt, minEl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass without details invalid lat 2")
    @Test
    public void passWithoutDetailsInvalidLat2() throws Exception {
        this.mockMvc.perform(get(passesWithouDetailsUrl,
                iss, 120, lon, alt, minEl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass without details invalid lon 1")
    @Test
    public void passWithoutDetailsInvalidLon1() throws Exception {
        this.mockMvc.perform(get(passesWithouDetailsUrl,
                iss, lat, -120, alt, minEl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass without details invalid lon 2")
    @Test
    public void passWithoutDetailsInvalidLon2() throws Exception {
        this.mockMvc.perform(get(passesWithouDetailsUrl,
                iss, lat, 120, alt, minEl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass without details invalid altitude 1")
    @Test
    public void passWithoutDetailsInvalidAlt1() throws Exception {
        this.mockMvc.perform(get(passesWithouDetailsUrl,
                iss, lat, lon, -5, minEl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass without details invalid altitude 2")
    @Test
    public void passWithoutDetailsInvalidAlt2() throws Exception {
        this.mockMvc.perform(get(passesWithouDetailsUrl,
                iss, lat, lon, "999999999999999", minEl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass without details invalid minimum elevation 1")
    @Test
    public void passWithoutDetailsInvalidMinEl1() throws Exception {
        this.mockMvc.perform(get(passesWithouDetailsUrl,
                iss, lat, lon, alt, -1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass without details invalid minimum elevation 2")
    @Test
    public void passWithoutDetailsInvalidMinEl2() throws Exception {
        this.mockMvc.perform(get(passesWithouDetailsUrl,
                iss, lat, lon, alt, 100)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass with details invalid lat 1")
    @Test
    public void passWithDetailsInvalidLat1() throws Exception {
        this.mockMvc.perform(get(passesWithDetailsUrl,
                iss, -120.99, lon, alt, minEl, step)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass with details invalid lat 2")
    @Test
    public void passWithDetailsInvalidLat2() throws Exception {
        this.mockMvc.perform(get(passesWithDetailsUrl,
                iss, 120.9999, lon, alt, minEl, step)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass with details invalid lon 1")
    @Test
    public void passWithDetailsInvalidLon1() throws Exception {
        this.mockMvc.perform(get(passesWithDetailsUrl,
                iss, lat, -120, alt, minEl, step)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass with details invalid lon 2")
    @Test
    public void passWithDetailsInvalidLon2() throws Exception {
        this.mockMvc.perform(get(passesWithDetailsUrl,
                iss, lat, 120, alt, minEl, step)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass with details invalid altitude 1")
    @Test
    public void passWithDetailsInvalidAlt1() throws Exception {
        this.mockMvc.perform(get(passesWithDetailsUrl,
                iss, lat, lon, -5, minEl, step)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass with details invalid altitude 2")
    @Test
    public void passWithDetailsInvalidAlt2() throws Exception {
        this.mockMvc.perform(get(passesWithDetailsUrl,
                iss, lat, lon, "999999999999999", minEl, step)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass with details invalid minimum elevation 1")
    @Test
    public void passWithDetailsInvalidMinEl1() throws Exception {
        this.mockMvc.perform(get(passesWithDetailsUrl,
                iss, lat, lon, alt, -1.5, step)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass with details invalid minimum elevation 2")
    @Test
    public void passWithDetailsInvalidMinEl2() throws Exception {
        this.mockMvc.perform(get(passesWithDetailsUrl,
                iss, lat, lon, alt, 100, step)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass with details negative step")
    @Test
    public void passWithDetailsInvalidStep1() throws Exception {
        this.mockMvc.perform(get(passesWithDetailsUrl,
                iss, lat, lon, alt, minEl, -1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass with details step too small")
    @Test
    public void passWithDetailsInvalidStep2() throws Exception {
        this.mockMvc.perform(get(passesWithDetailsUrl,
                iss, lat, lon, alt, minEl, 0.001)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Passes without details with invalid TLE parameters 1 - 400")
    @Test
    public void badRequestInvalidTLEParams1() throws Exception {
        this.mockMvc.perform(get(passesWithouDetailsCustomTLEUrl,
                iss, lat, lon, alt, minEl, "null", "invalid TLE line1", "invalid TLE line2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Passes with details with invalid TLE parameters 2 - 400")
    @Test
    public void badRequestInvalidTLEParams2() throws Exception {
        this.mockMvc.perform(get(passesWithDetailCustomTLEsUrl,
                iss, lat, lon, alt, minEl, 0.001, "null", "invalid TLE line1", "invalid TLE line2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("N passes without details with invalid TLE parameters 3 - 400")
    @Test
    public void badRequestInvalidTLEParams3() throws Exception {
        this.mockMvc.perform(get(npassesWithouDetailsCustomTLEUrl,
                iss, lat, lon, alt, minEl, "null", "invalid TLE line1", "invalid TLE line2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Passes without details with custom TLE 1 - OK")
    @Test
    public void okRequestForCustomTLE1() throws Exception {
        this.mockMvc.perform(get(passesWithouDetailsCustomTLEUrl,
                iss, lat, lon, alt, minEl, "ISS", validTleLine1, validTleLine2)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Passes with details with custom TLE 2 - OK")
    @Test
    public void okRequestForCustomTLE2() throws Exception {
        this.mockMvc.perform(get(passesWithDetailCustomTLEsUrl,
                iss, lat, lon, alt, minEl, step, "ISS", validTleLine1, validTleLine2)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("N passes without details for custom tle - OK")
    @Test
    public void okRequestForCustomTLE3() throws Exception {

        this.mockMvc.perform(get(npassesWithouDetailsUrl,
                iss, lat, lon, alt, minEl, "ISS", validTleLine1, validTleLine2)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Passes without details with incomplete custom TLE 1 - OK")
    @Test
    public void okRequestForIncompleteCustomTLE1() throws Exception {
        this.mockMvc.perform(get(passesWithouDetailsCustomTLEUrl,
                iss, lat, lon, alt, minEl, "ISS", validTleLine1, "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Passes with details with incomplete custom TLE 2 - OK")
    @Test
    public void okRequestForIncompleteCustomTLE2() throws Exception {
        this.mockMvc.perform(get(passesWithDetailCustomTLEsUrl,
                iss, lat, lon, alt, minEl, step, "ISS", validTleLine1, "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("N passes without details with incomplete custom tle - OK")
    @Test
    public void okRequestForIncompleteCustomTLE3() throws Exception {

        this.mockMvc.perform(get(npassesWithouDetailsUrl,
                iss, lat, lon, alt, minEl, "ISS", validTleLine1, "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}