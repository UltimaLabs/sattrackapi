package com.ultimalabs.sattrackapi.predict.service;

import com.ultimalabs.sattrackapi.predict.model.SatellitePass;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PredictServiceTest {

    static TLEPlus tle;

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
    }

    @BeforeEach
    void setUpTleFetcherReturnValue() {
        when(tleFetcherService.getTle(anyString())).thenReturn(tle);
    }

    @Test
    void returnNullWhenFailedEventLogging() {

        double invalidMinElevation = 1000;

        assertNull(predictService.getNextEventWithoutDetails(
                "25544", 15.9819, 45.815, 400, invalidMinElevation)
        );
        assertNull(predictService.getNextEventWithDetails(
                "25544", 15.9819, 45.815, 400, invalidMinElevation, 1.)
        );
        assertEquals(Collections.emptyList(), predictService.getNextEventsWithoutDetails(
                10,"25544", 15.9819, 45.815, 400, invalidMinElevation)
        );
    }

    @Test
    void returnEventDataWithoutDetails() {

        assertNotNull(predictService.getNextEventWithoutDetails(
                "25544", 15.9819, 45.815, 400, 1)
        );
    }

    @Test
    void returnEventDataWithDetails() {

        assertNotNull(predictService.getNextEventWithDetails(
                "25544", 15.9819, 45.815, 400, 1, 1.)
        );
    }

    @Test
    void returnANumberOfEventDataWithoutDetails() {

        int numberOfEvents = 5;

        List<SatellitePass> events = predictService.getNextEventsWithoutDetails(
                numberOfEvents,"25544", 15.9819, 45.815, 400, 1);

        assertNotNull(events);
        assertEquals(numberOfEvents, events.size());
    }
}