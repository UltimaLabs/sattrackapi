package com.ultimalabs.sattrackapi.predict.service;

import com.ultimalabs.sattrackapi.common.model.EarthParams;
import com.ultimalabs.sattrackapi.tle.model.TLEPlus;
import com.ultimalabs.sattrackapi.tle.service.TleFetcherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hipparchus.util.FastMath;
import org.orekit.bodies.BodyShape;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.frames.TopocentricFrame;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.tle.TLEPropagator;
import org.orekit.propagation.events.ElevationDetector;
import org.orekit.propagation.events.EventDetector;
import org.orekit.propagation.events.handlers.EventHandler;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

/**
 * Predict service
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PredictServiceImpl implements PredictService {

    /**
     * TLE fetcher service
     */
    private final TleFetcherService tleFetcherService;

    /**
     * Returns next visibility event
     *
     * @param searchString Satellite Number or International Designator
     * @param longitude    observer longitude
     * @param latitude     observer latitude
     * @param altitude     observer altitude
     * @param minElevation minimal elevation
     */
    @Override
    public String getVisibility(String searchString, double latitude, double longitude, double altitude, double minElevation) {
        TLEPlus tle = tleFetcherService.getTle(searchString);
        if (tle == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return getNextEvent(tle, latitude, longitude, altitude, minElevation);
    }

    private String getNextEvent(TLEPlus tle, double lat, double lon, double alt, double minEl) {

        AbsoluteDate now = new AbsoluteDate(new Date(), TimeScalesFactory.getUTC());
        TLEPropagator propagator = TLEPropagator.selectExtrapolator(tle);
        propagator.propagate(now);
        log.info("Now : " + now.getDate());

        final BodyShape earth = new OneAxisEllipsoid(EarthParams.EQUATORIAL_RADIUS, EarthParams.FLATTENING, EarthParams.iers2010Frame);

        final GeodeticPoint observer = new GeodeticPoint(lat, lon, alt);
        final TopocentricFrame observerFrame = new TopocentricFrame(earth, observer, "observer");

        // Event definition
        final double maxcheck = 100.0;
        final double threshold = 0.001;
        final double elevation = FastMath.toRadians(minEl);
        final EventDetector visibilityDetector =
                new ElevationDetector(maxcheck, threshold, observerFrame).
                        withConstantElevation(elevation).
                        withHandler(new VisibilityHandler());

        // Add event to be detected
        propagator.addEventDetector(visibilityDetector);

        // Propagate from the initial date to the first raising or for the fixed duration
        SpacecraftState finalState = propagator.propagate(now.shiftedBy(259200.));

        log.info("Final state : " + finalState.getDate());

        return null;
    }

    /**
     * Handler for visibility event
     */
    private static class VisibilityHandler implements EventHandler<ElevationDetector> {

        public Action eventOccurred(final SpacecraftState s, final ElevationDetector detector,
                                    final boolean increasing) {
            if (increasing) {
                log.info("Visibility on " + detector.getTopocentricFrame().getName()
                        + " begins at " + s.getDate());
                return Action.CONTINUE;
            } else {
                log.info("Visibility on " + detector.getTopocentricFrame().getName()
                        + " ends at " + s.getDate());
                return Action.STOP;
            }
        }

    }

}
