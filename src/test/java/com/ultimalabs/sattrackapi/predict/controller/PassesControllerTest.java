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

    private final String passesWithouDetaulsUrl = "/api/v1/passes/{searchString}/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/name/{name}/line1/{line1}/line2/{line2}";
    private final String passesWithDetaulsUrl = "/api/v1/passes/{searchString}/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/step/{step}/name/{name}/line1/{line1}/line2/{line2}";
    private final String nPassesWithouDetaulsUrl = "/api/v1/passes/n/5/{searchString}/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/name/{name}/line1/{line1}/line2/{line2}";

    @DisplayName("Pass without details by Satellite Number - OK")
    @Test
    public void getValidTle1() throws Exception {
        this.mockMvc.perform(get(passesWithouDetaulsUrl,
                iss, lat, lon, alt, minEl, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Pass without details by International Designator (short) - OK")
    @Test
    public void getValidTle2() throws Exception {
        this.mockMvc.perform(get(passesWithouDetaulsUrl,
                "98067A", lat, lon, alt, minEl, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Pass without details by International Designator (long) - OK")
    @Test
    public void getValidTle3() throws Exception {
        this.mockMvc.perform(get(passesWithouDetaulsUrl,
                "1998-067A", lat, lon, alt, minEl, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Pass without details - 404 Not Found 1")
    @Test
    public void getNonexistingTle1() throws Exception {
        this.mockMvc.perform(get(passesWithouDetaulsUrl,
                "99999", lat, lon, alt, minEl, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Pass without details - 404 Not Found 2")
    @Test
    public void getNonexistingTle2() throws Exception {
        this.mockMvc.perform(get(passesWithouDetaulsUrl,
                "ABAB-097Aaa", lat, lon, alt, minEl, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Pass without details - 400 Bad Request, search string too short")
    @Test
    public void getTleShortSatId() throws Exception {
        this.mockMvc.perform(get(passesWithouDetaulsUrl,
                "111", lat, lon, alt, minEl, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass without details - 400 Bad Request, search string too long")
    @Test
    public void getTleLongSatId() throws Exception {
        this.mockMvc.perform(get(
                passesWithouDetaulsUrl,
                "999999999999999", lat, lon, alt, minEl, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass with details by Satellite Number - OK")
    @Test
    public void getValidTle1WithDetails() throws Exception {
        this.mockMvc.perform(get(passesWithouDetaulsUrl,
                iss, lat, lon, alt, minEl, step, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Pass with details by International Designator (short) - OK")
    @Test
    public void getValidTle2WithDetails() throws Exception {
        this.mockMvc.perform(get(passesWithDetaulsUrl,
                "98067A", lat, lon, alt, minEl, step, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Pass with details by International Designator (long) - OK")
    @Test
    public void getValidTle3WithDetails() throws Exception {
        this.mockMvc.perform(get(passesWithDetaulsUrl,
                "1998-067A", lat, lon, alt, minEl, step, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Next n passes without details - OK")
    @Test
    void getNextNPasses() throws Exception {
        this.mockMvc.perform(get(nPassesWithouDetaulsUrl,
                "1998-067A", lat, lon, alt, minEl, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Pass with details - 404 Not Found 1")
    @Test
    public void getNonexistingTle1WithDetails() throws Exception {
        this.mockMvc.perform(get(passesWithDetaulsUrl,
                "99999", lat, lon, alt, minEl, step, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Pass with details - 404 Not Found 2")
    @Test
    public void getNonexistingTle2WithDetails() throws Exception {
        this.mockMvc.perform(get(passesWithDetaulsUrl,
                "ABAB-097Aaa", lat, lon, alt, minEl, step, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Pass with details - 400 Bad Request, search string too short")
    @Test
    public void getTleShortSatIdWithDetails() throws Exception {
        this.mockMvc.perform(get(passesWithDetaulsUrl,
                "111", lat, lon, alt, minEl, step, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass with details - 400 Bad Request, search string too long")
    @Test
    public void getTleLongSatIdWithDetails() throws Exception {
        this.mockMvc.perform(get(passesWithDetaulsUrl,
                "999999999999999", lat, lon, alt, minEl, step, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass without details invalid lat 1")
    @Test
    public void passWithoutDetailsInvalidLat1() throws Exception {
        this.mockMvc.perform(get(passesWithouDetaulsUrl,
                iss, -120, lon, alt, minEl, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass without details invalid lat 2")
    @Test
    public void passWithoutDetailsInvalidLat2() throws Exception {
        this.mockMvc.perform(get(passesWithouDetaulsUrl,
                iss, 120, lon, alt, minEl, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass without details invalid lon 1")
    @Test
    public void passWithoutDetailsInvalidLon1() throws Exception {
        this.mockMvc.perform(get(passesWithouDetaulsUrl,
                iss, lat, -120, alt, minEl, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass without details invalid lon 2")
    @Test
    public void passWithoutDetailsInvalidLon2() throws Exception {
        this.mockMvc.perform(get(passesWithouDetaulsUrl,
                iss, lat, 120, alt, minEl, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass without details invalid altitude 1")
    @Test
    public void passWithoutDetailsInvalidAlt1() throws Exception {
        this.mockMvc.perform(get(passesWithouDetaulsUrl,
                iss, lat, lon, -5, minEl, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass without details invalid altitude 2")
    @Test
    public void passWithoutDetailsInvalidAlt2() throws Exception {
        this.mockMvc.perform(get(passesWithouDetaulsUrl,
                iss, lat, lon, "999999999999999", minEl, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass without details invalid minimum elevation 1")
    @Test
    public void passWithoutDetailsInvalidMinEl1() throws Exception {
        this.mockMvc.perform(get(passesWithouDetaulsUrl,
                iss, lat, lon, alt, -1, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass without details invalid minimum elevation 2")
    @Test
    public void passWithoutDetailsInvalidMinEl2() throws Exception {
        this.mockMvc.perform(get(passesWithouDetaulsUrl,
                iss, lat, lon, alt, 100, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass with details invalid lat 1")
    @Test
    public void passWithDetailsInvalidLat1() throws Exception {
        this.mockMvc.perform(get(passesWithDetaulsUrl,
                iss, -120.99, lon, alt, minEl, step, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass with details invalid lat 2")
    @Test
    public void passWithDetailsInvalidLat2() throws Exception {
        this.mockMvc.perform(get(passesWithDetaulsUrl,
                iss, 120.9999, lon, alt, minEl, step, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass with details invalid lon 1")
    @Test
    public void passWithDetailsInvalidLon1() throws Exception {
        this.mockMvc.perform(get(passesWithDetaulsUrl,
                iss, lat, -120, alt, minEl, step, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass with details invalid lon 2")
    @Test
    public void passWithDetailsInvalidLon2() throws Exception {
        this.mockMvc.perform(get(passesWithDetaulsUrl,
                iss, lat, 120, alt, minEl, step, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass with details invalid altitude 1")
    @Test
    public void passWithDetailsInvalidAlt1() throws Exception {
        this.mockMvc.perform(get(passesWithDetaulsUrl,
                iss, lat, lon, -5, minEl, step, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass with details invalid altitude 2")
    @Test
    public void passWithDetailsInvalidAlt2() throws Exception {
        this.mockMvc.perform(get(passesWithDetaulsUrl,
                iss, lat, lon, "999999999999999", minEl, step, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass with details invalid minimum elevation 1")
    @Test
    public void passWithDetailsInvalidMinEl1() throws Exception {
        this.mockMvc.perform(get(passesWithDetaulsUrl,
                iss, lat, lon, alt, -1.5, step, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass with details invalid minimum elevation 2")
    @Test
    public void passWithDetailsInvalidMinEl2() throws Exception {
        this.mockMvc.perform(get(passesWithDetaulsUrl,
                iss, lat, lon, alt, 100, step, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass with details negative step")
    @Test
    public void passWithDetailsInvalidStep1() throws Exception {
        this.mockMvc.perform(get(passesWithDetaulsUrl,
                iss, lat, lon, alt, minEl, -1, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass with details step too small")
    @Test
    public void passWithDetailsInvalidStep2() throws Exception {
        this.mockMvc.perform(get(passesWithDetaulsUrl,
                iss, lat, lon, alt, minEl, 0.001, "null", "null", "null")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}