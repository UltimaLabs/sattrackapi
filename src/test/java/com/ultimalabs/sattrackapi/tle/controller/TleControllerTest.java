package com.ultimalabs.sattrackapi.tle.controller;

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
class TleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("Fetch TLE by Satellite Number - OK")
    @Test
    public void getValidTle1() throws Exception {
        this.mockMvc.perform(get("/api/v1/tles/25544")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Fetch TLE by International Designator (short) - OK")
    @Test
    public void getValidTle2() throws Exception {
        this.mockMvc.perform(get("/api/v1/tles/98067A")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Fetch TLE by International Designator (long) - OK")
    @Test
    public void getValidTle3() throws Exception {
        this.mockMvc.perform(get("/api/v1/tles/1998-067A")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Get TLE - 404 Not Found 1")
    @Test
    public void getNonexistingTle1() throws Exception {
        this.mockMvc.perform(get("/api/v1/tles/99999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Get TLE - 404 Not Found 2")
    @Test
    public void getNonexistingTle2() throws Exception {
        this.mockMvc.perform(get("/api/v1/tles/ABAB-097Aaa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Get TLE - 400 Bad Request, search string too short")
    @Test
    public void getTleShortSatId() throws Exception {
        this.mockMvc.perform(get("/api/v1/tles/111")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Get TLE - 400 Bad Request, search string too long")
    @Test
    public void getTleLongSatId() throws Exception {
        this.mockMvc.perform(get("/api/v1/tles/999999999999999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}