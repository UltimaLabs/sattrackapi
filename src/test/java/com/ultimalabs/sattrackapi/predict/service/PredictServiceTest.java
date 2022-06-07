package com.ultimalabs.sattrackapi.predict.service;

import com.ultimalabs.sattrackapi.predict.model.SatellitePass;
import com.ultimalabs.sattrackapi.predict.model.dto.ObserverParams;
import com.ultimalabs.sattrackapi.predict.model.dto.TLEParams;
import com.ultimalabs.sattrackapi.tle.model.TLEPlus;
import com.ultimalabs.sattrackapi.tle.service.TleFetcherService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PredictServiceTest {

    static TLEPlus tle;
    static TLEParams tleParamsWithSatId;
    static TLEParams tleParamsWithTleLines;
    static double invalidMinElevation = 1000;
    static ObserverParams observerParams;
    static ObserverParams observerParamsWithInvalidMinEl;


    @Mock TleFetcherService tleFetcherService;
    @InjectMocks PredictServiceImpl predictService;

    @BeforeAll
    static void orekitDataSetUp() {
        // Orekit setup: at least a single file, "tai-utc.dat" should be present in "src/test/resources" folder
        File orekitData = new File(".");
        DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
        manager.addProvider(new DirectoryCrawler(orekitData));

        tle = new TLEPlus(
                "ISS",
                "1 25544U 98067A   08264.51782528 -.00002182  00000-0 -11606-4 0  2927",
                "2 25544  51.6416 247.4627 0006703 130.5360 325.0288 15.72125391563537"
        );
        tleParamsWithSatId = new TLEParams("25544");
        tleParamsWithTleLines = new TLEParams(
                "ISS",
                "ISS",
                "1 25544U 98067A   08264.51782528 -.00002182  00000-0 -11606-4 0  2927",
                "2 25544  51.6416 247.4627 0006703 130.5360 325.0288 15.72125391563537"
        );
        observerParams = new ObserverParams(15.9819, 45.815, 400, 1.);
        observerParamsWithInvalidMinEl = new ObserverParams(15.9819, 45.815, 400, invalidMinElevation);
    }

    @BeforeEach
    void setUpTleFetcherReturnValue() {
        when(tleFetcherService.getTle(anyString())).thenReturn(tle);
    }

    @Test
    void returnNullWhenFailedEventLogging() {

        assertNull(predictService.getNextEvent(tleParamsWithSatId, observerParamsWithInvalidMinEl));
        assertNull(predictService.getNextEventWithDetails(tleParamsWithSatId, observerParamsWithInvalidMinEl, 1.));
        assertEquals(Collections.emptyList(), predictService.getNextEvents(10, tleParamsWithSatId, observerParamsWithInvalidMinEl));
    }

    @Test
    void returnEventDataWithoutDetails() {

        assertNotNull(predictService.getNextEvent(tleParamsWithSatId, observerParams));
    }

    @Test
    void returnEventDataWithDetails() {

        assertNotNull(predictService.getNextEventWithDetails(tleParamsWithSatId, observerParams, 1.));
    }

    @Test
    void returnEventsData() {

        int numberOfDays = 5;

        List<SatellitePass> events = predictService.getNextEvents(numberOfDays, tleParamsWithSatId, observerParams);

        assertNotNull(events);
        assertFalse(events.isEmpty());
    }

    @Test
    void returnEventDataForNextNDays() {

        int numberOfDays = 5;

        List<SatellitePass> events = predictService.getNextEvents(numberOfDays, tleParamsWithSatId, observerParams);

        LocalDateTime firstEvent = LocalDateTime.parse(events.get(0).getRisePoint().getT().replace("Z", ""));
        LocalDateTime lastEvent = LocalDateTime.parse(events.get(events.size() - 1).getRisePoint().getT().replace("Z", ""));

        assertTrue(lastEvent.isAfter(firstEvent));
        assertTrue(numberOfDays >= lastEvent.compareTo(firstEvent));

    }
}