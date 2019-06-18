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

    private String iss = "25544";
    private double lat = 46.1613;
    private double lon = 15.7534;
    private int alt = 200;
    private int minEl = 25;
    private int step = 30;

    @DisplayName("Pass without details by Satellite Number - OK")
    @Test
    public void getValidTle1() throws Exception {
        this.mockMvc.perform(get("/api/v1/passes/25544/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/", lat, lon, alt, minEl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Pass without details by International Designator (short) - OK")
    @Test
    public void getValidTle2() throws Exception {
        this.mockMvc.perform(get("/api/v1/passes/98067A/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/", lat, lon, alt, minEl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Pass without details by International Designator (long) - OK")
    @Test
    public void getValidTle3() throws Exception {
        this.mockMvc.perform(get("/api/v1/passes/1998-067A/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/", lat, lon, alt, minEl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Pass without details - 404 Not Found 1")
    @Test
    public void getNonexistingTle1() throws Exception {
        this.mockMvc.perform(get("/api/v1/passes/99999/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/", lat, lon, alt, minEl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Pass without details - 404 Not Found 2")
    @Test
    public void getNonexistingTle2() throws Exception {
        this.mockMvc.perform(get("/api/v1/passes/ABAB-097Aaa/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/", lat, lon, alt, minEl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Pass without details - 400 Bad Request, search string too short")
    @Test
    public void getTleShortSatId() throws Exception {
        this.mockMvc.perform(get("/api/v1/passes/111/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/", lat, lon, alt, minEl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass without details - 400 Bad Request, search string too long")
    @Test
    public void getTleLongSatId() throws Exception {
        this.mockMvc.perform(get("/api/v1/passes/999999999999999/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/", lat, lon, alt, minEl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass with details by Satellite Number - OK")
    @Test
    public void getValidTle1WithDetails() throws Exception {
        this.mockMvc.perform(get("/api/v1/passes/25544/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/step/{step}/", lat, lon, alt, minEl, step)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Pass with details by International Designator (short) - OK")
    @Test
    public void getValidTle2WithDetails() throws Exception {
        this.mockMvc.perform(get("/api/v1/passes/98067A/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/step/{step}/", lat, lon, alt, minEl, step)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Pass with details by International Designator (long) - OK")
    @Test
    public void getValidTle3WithDetails() throws Exception {
        this.mockMvc.perform(get("/api/v1/passes/1998-067A/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/step/{step}/", lat, lon, alt, minEl, step)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Pass with details - 404 Not Found 1")
    @Test
    public void getNonexistingTle1WithDetails() throws Exception {
        this.mockMvc.perform(get("/api/v1/passes/99999/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/step/{step}/", lat, lon, alt, minEl, step)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Pass with details - 404 Not Found 2")
    @Test
    public void getNonexistingTle2WithDetails() throws Exception {
        this.mockMvc.perform(get("/api/v1/passes/ABAB-097Aaa/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/step/{step}/", lat, lon, alt, minEl, step)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Pass with details - 400 Bad Request, search string too short")
    @Test
    public void getTleShortSatIdWithDetails() throws Exception {
        this.mockMvc.perform(get("/api/v1/passes/111/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/step/{step}/", lat, lon, alt, minEl, step)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass with details - 400 Bad Request, search string too long")
    @Test
    public void getTleLongSatIdWithDetails() throws Exception {
        this.mockMvc.perform(get("/api/v1/passes/999999999999999/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/", lat, lon, alt, minEl, step)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass without details invalid lat 1")
    @Test
    public void passWithoutDetailsInvalidLat1() throws Exception {
        this.mockMvc.perform(get("/api/v1/passes/{iss}/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/", iss, -120, lon, alt, minEl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass without details invalid lat 2")
    @Test
    public void passWithoutDetailsInvalidLat2() throws Exception {
        this.mockMvc.perform(get("/api/v1/passes/{iss}/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/", iss, 120, lon, alt, minEl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass without details invalid lon 1")
    @Test
    public void passWithoutDetailsInvalidLon1() throws Exception {
        this.mockMvc.perform(get("/api/v1/passes/{iss}/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/", iss, lat, -120, alt, minEl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass without details invalid lon 2")
    @Test
    public void passWithoutDetailsInvalidLon2() throws Exception {
        this.mockMvc.perform(get("/api/v1/passes/{iss}/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/", iss, lat, 120, alt, minEl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass without details invalid altitude 1")
    @Test
    public void passWithoutDetailsInvalidAlt1() throws Exception {
        this.mockMvc.perform(get("/api/v1/passes/{iss}/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/", iss, lat, lon, -5, minEl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass without details invalid altitude 2")
    @Test
    public void passWithoutDetailsInvalidAlt2() throws Exception {
        this.mockMvc.perform(get("/api/v1/passes/{iss}/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/", iss, lat, lon, "999999999999999", minEl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @DisplayName("Pass without details invalid minimum elevation 1")
    @Test
    public void passWithoutDetailsInvalidMinEl1() throws Exception {
        this.mockMvc.perform(get("/api/v1/passes/{iss}/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/", iss, lat, lon, alt, -1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Pass without details invalid minimum elevation 2")
    @Test
    public void passWithoutDetailsInvalidMinEl2() throws Exception {
        this.mockMvc.perform(get("/api/v1/passes/{iss}/lat/{lat}/lon/{lon}/alt/{alt}/minEl/{minEl}/", iss, lat, lon, alt, 100)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}