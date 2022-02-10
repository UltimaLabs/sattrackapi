package com.ultimalabs.sattrackapi.predict.predictor;

import com.ultimalabs.sattrackapi.predict.model.SatellitePass;
import com.ultimalabs.sattrackapi.tle.model.TLEPlus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;

import java.io.File;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PredictorTest {
    double observerLatitude = 45.815;
    double observerLongitude = 15.9819;
    double observerAltitude = 400;
    double minElevation = 1;
    AbsoluteDate now = new AbsoluteDate(new Date(), TimeScalesFactory.getUTC());
    Predictor predictor;

    @BeforeAll
    static void orekitDataSetUp() {
        // Orekit setup: at least a single file, "tai-utc.dat" should be present in "src/test/resources" folder
        File orekitData = new File(".");
        DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
        manager.addProvider(new DirectoryCrawler(orekitData));
    }

    @BeforeEach
    void predictorSetUp() {
        TLEPlus tle = new TLEPlus(
                "ISS",
                "1 25544U 98067A   08264.51782528 -.00002182  00000-0 -11606-4 0  2927",
                "2 25544  51.6416 247.4627 0006703 130.5360 325.0288 15.72125391563537"
        );
        predictor = new Predictor(
                tle, observerLatitude, observerLongitude, observerAltitude, minElevation
        );
    }

    @Test
    void throwExceptionWhenLoggedIncorrectNumberOfEvents() {
        assertThrows(LoggedEventsException.class, () -> predictor.getEventData(now, now));
        assertThrows(LoggedEventsException.class, () ->  predictor.getEventDataWithDetails(now, now, 1.));
    }

    @Test
    void shouldLogEventsAfterPropagation() {

        SatellitePass eventData = predictor.getEventData(now, now.shiftedBy(259200.));

        assertEquals(3, predictor.getLogger().getLoggedEvents().size());
        assertNotNull(predictor.getRiseEvent());
        assertNotNull(predictor.getRiseDate());
        assertNotNull(predictor.getSetEvent());
        assertNotNull(predictor.getSetDate());
        assertNotNull(predictor.getMidPointEvent());
        assertTrue(eventData.getEventDetails().isEmpty());
    }

    @Test
    void shouldLogEventsAfterPropagationWithDetails() {

        SatellitePass eventDataWithDetails = predictor.getEventDataWithDetails(now, now.shiftedBy(259200.), 1.);

        assertEquals(3, predictor.getLogger().getLoggedEvents().size());
        assertNotNull(predictor.getRiseEvent());
        assertNotNull(predictor.getRiseDate());
        assertNotNull(predictor.getSetEvent());
        assertNotNull(predictor.getSetDate());
        assertNotNull(predictor.getMidPointEvent());

        assertFalse(eventDataWithDetails.getEventDetails().isEmpty());
    }
}