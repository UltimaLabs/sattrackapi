package com.ultimalabs.sattrackapi.predict.service;

import com.ultimalabs.sattrackapi.common.model.EarthParams;
import com.ultimalabs.sattrackapi.common.util.DoubleRound;
import com.ultimalabs.sattrackapi.predict.model.PassEventData;
import com.ultimalabs.sattrackapi.tle.model.TLEPlus;
import com.ultimalabs.sattrackapi.tle.service.TleFetcherService;
import lombok.Getter;
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

import java.util.Collections;
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
    public PassEventData getVisibility(String searchString, double latitude, double longitude, double altitude, double minElevation) {
        TLEPlus tle = tleFetcherService.getTle(searchString);
        if (tle == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return getNextEvent(tle, latitude, longitude, altitude, minElevation);
    }

    private PassEventData getNextEvent(TLEPlus tle, double lat, double lon, double alt, double minEl) {

        AbsoluteDate now = new AbsoluteDate(new Date(), TimeScalesFactory.getUTC());
        TLEPropagator propagator = TLEPropagator.selectExtrapolator(tle);
        propagator.propagate(now);

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

        propagator.addEventDetector(visibilityDetector);
        VisibilityHandler handler = (VisibilityHandler) ((ElevationDetector) visibilityDetector).getHandler();

        propagator.propagate(now.shiftedBy(259200.));

        // TODO add test for no rise event
        if (handler.getRise() == null) {
            return null;
        }

        // TODO add test for event with rise only
        if (handler.getSet() == null) {
            return null;
        }

        return new PassEventData(
                now.getDate().toString(),
                DoubleRound.round(handler.getRise().offsetFrom(now, TimeScalesFactory.getUTC()), 2),
                handler.getRise().toString(),
                handler.getSet().toString(),
                DoubleRound.round(handler.getSet().offsetFrom(handler.getRise(), TimeScalesFactory.getUTC()), 2),
                Collections.emptyList()
        );

    }

    /**
     * Handler for visibility event
     */
    @Getter
    private static class VisibilityHandler implements EventHandler<ElevationDetector> {

        /**
         * Satellite rise time
         */
        private AbsoluteDate rise;

        /**
         * Satellite set time
         */
        private AbsoluteDate set;

        public Action eventOccurred(final SpacecraftState s, final ElevationDetector detector,
                                    final boolean increasing) {
            if (increasing) {
                this.rise = s.getDate();
                return Action.CONTINUE;
            } else {
                this.set = s.getDate();
                return Action.STOP;
            }
        }
    }

}
