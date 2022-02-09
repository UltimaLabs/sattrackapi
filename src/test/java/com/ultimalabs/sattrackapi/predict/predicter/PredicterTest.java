package com.ultimalabs.sattrackapi.predict.predicter;

import com.ultimalabs.sattrackapi.tle.model.TLEPlus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PredicterTest {
    double observerLatitude = 45.815;
    double observerLongitude = 15.9819;
    double observerAltitude = 400;
    double minElevation = 1;

    @BeforeAll
    static void setup() {
        // Orekit setup: at least a single file, "tai-utc.dat" should be present in "src/test/resources" folder
        File orekitData = new File(".");
        DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
        manager.addProvider(new DirectoryCrawler(orekitData));
    }

    @Test
    void initPredicter() {

        TLEPlus tle = new TLEPlus(
                "ISS",
                "1 25544U 98067A   08264.51782528 -.00002182  00000-0 -11606-4 0  2927",
                "2 25544  51.6416 247.4627 0006703 130.5360 325.0288 15.72125391563537"
        );

        Predicter predicter = new Predicter(
                tle, observerLatitude, observerLongitude, observerAltitude, minElevation
        );

        assertNotNull(predicter);
    }
}