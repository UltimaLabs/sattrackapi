package com.ultimalabs.sattrackapi.position.controller;

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
class PositionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("Get position by Satellite Number - OK")
    @Test
    public void getValidTle1() throws Exception {
        this.mockMvc.perform(get("/api/v1/positions/25544")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Get position by International Designator (short) - OK")
    @Test
    public void getValidTle2() throws Exception {
        this.mockMvc.perform(get("/api/v1/positions/98067A")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Get position by International Designator (long) - OK")
    @Test
    public void getValidTle3() throws Exception {
        this.mockMvc.perform(get("/api/v1/positions/1998-067A")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Get position - 404 Not Found 1")
    @Test
    public void getNonexistingTle1() throws Exception {
        this.mockMvc.perform(get("/api/v1/positions/99999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Get position - 404 Not Found 2")
    @Test
    public void getNonexistingTle2() throws Exception {
        this.mockMvc.perform(get("/api/v1/positions/ABAB-097Aaa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Get position - 400 Bad Request, search string too short")
    @Test
    public void getTleShortSatId() throws Exception {
        this.mockMvc.perform(get("/api/v1/positions/111")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Get position - 400 Bad Request, search string too long")
    @Test
    public void getTleLongSatId() throws Exception {
        this.mockMvc.perform(get("/api/v1/positions/999999999999999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}