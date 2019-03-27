package com.ultimalabs.sattrackapi.predict.service;

import com.ultimalabs.sattrackapi.common.model.EarthParams;
import com.ultimalabs.sattrackapi.common.util.DoubleRound;
import com.ultimalabs.sattrackapi.predict.model.PassEventData;
import com.ultimalabs.sattrackapi.predict.model.PassEventDetailsEntry;
import com.ultimalabs.sattrackapi.tle.model.TLEPlus;
import com.ultimalabs.sattrackapi.tle.service.TleFetcherService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.util.FastMath;
import org.orekit.bodies.BodyShape;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.frames.TopocentricFrame;
import org.orekit.frames.Transform;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.tle.TLEPropagator;
import org.orekit.propagation.events.ElevationDetector;
import org.orekit.propagation.events.EventDetector;
import org.orekit.propagation.events.handlers.EventHandler;
import org.orekit.propagation.sampling.OrekitFixedStepHandler;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.PVCoordinates;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
     * Returns next visibility event with pass details
     *
     * @param searchString Satellite Number or International Designator
     * @param longitude    observer longitude
     * @param latitude     observer latitude
     * @param altitude     observer altitude
     * @param minElevation minimal elevation
     * @param stepSize     step resolution for the master mode propagator
     * @return next visibility event, with details
     */
    @Override
    public PassEventData getNextEventWithDetails(String searchString, double latitude, double longitude, double altitude, double minElevation, double stepSize) {
        TLEPlus tle = tleFetcherService.getTle(searchString);
        if (tle == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return getNextEvent(tle, latitude, longitude, altitude, minElevation, stepSize);
    }

    @Override
    public PassEventData getNextEventWithoutDetails(String searchString, double latitude, double longitude, double altitude, double minElevation) {
        TLEPlus tle = tleFetcherService.getTle(searchString);
        if (tle == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return null;

    }

    private PassEventData getNextEvent(TLEPlus tle, double lat, double lon, double alt, double minEl, double stepSize) {

        AbsoluteDate now = new AbsoluteDate(new Date(), TimeScalesFactory.getUTC());
        TLEPropagator propagator = TLEPropagator.selectExtrapolator(tle);
        propagator.propagate(now);

        final BodyShape earth = new OneAxisEllipsoid(EarthParams.EQUATORIAL_RADIUS, EarthParams.FLATTENING, EarthParams.iers2010Frame);

        final GeodeticPoint observer = new GeodeticPoint(FastMath.toRadians(lat), FastMath.toRadians(lon), alt);
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
        VisibilityHandler visibilityHandler = (VisibilityHandler) ((ElevationDetector) visibilityDetector).getHandler();

        // Propagate from now to the first raising or for the fixed duration of 48 hours
        propagator.propagate(now.shiftedBy(172800.));

        // TODO add test for no rise event
        if (visibilityHandler.getRise() == null) {
            return null;
        }

        // TODO add test for event with rise only
        if (visibilityHandler.getSet() == null) {
            return null;
        }

        AbsoluteDate riseDate = visibilityHandler.getRise();
        AbsoluteDate setDate = visibilityHandler.getSet();

        TLEPropagator masterModePropagator = TLEPropagator.selectExtrapolator(tle);
        masterModePropagator.propagate(riseDate);
        StepHandler stepHandler = new StepHandler(observerFrame);
        masterModePropagator.setMasterMode(stepSize, stepHandler);
        masterModePropagator.propagate(setDate);

        return new PassEventData(
                now.getDate().toString(),
                DoubleRound.round(riseDate.offsetFrom(now, TimeScalesFactory.getUTC()), 2),
                riseDate.toString(),
                setDate.toString(),
                DoubleRound.round(setDate.offsetFrom(riseDate, TimeScalesFactory.getUTC()), 2),
                stepHandler.getPassDetails()
        );

    }

    /**
     * Handler for visibility events
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

        /**
         * Handle the event
         *
         * @param s          SpaceCraft state to be used in the evaluation
         * @param detector   object with appropriate type that can be used in determining correct return state
         * @param increasing with the event occurred in an "increasing" or "decreasing" slope direction
         * @return the Action that the calling detector should pass back to the evaluation system
         */
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

    /**
     * Specialized step handler
     * <p>
     * This class extends the step handler in order to save the parameters at the given step
     */
    @Getter
    @RequiredArgsConstructor
    private static class StepHandler implements OrekitFixedStepHandler {

        private final TopocentricFrame observerFrame;

        private List<PassEventDetailsEntry> passDetails = new ArrayList<>();

        /**
         * Handle the current step
         *
         * @param currentState current state at step time
         * @param isLast       if true, this is the last integration step
         */
        public void handleStep(SpacecraftState currentState, boolean isLast) {

            // get transform between state reference frame
            // and observer at current time
            Transform transform = currentState.getFrame().getTransformTo(observerFrame,
                    currentState.getDate());

            // get position-velocity in ground station frame
            PVCoordinates pv = transform.transformPVCoordinates(currentState.getPVCoordinates());
            Vector3D position = pv.getPosition();
            Vector3D velocity = pv.getVelocity();

            // extract pointing data
            double azimuth = FastMath.toDegrees(position.getAlpha() * -1.) + 90;

            if (azimuth < 0.) {
                azimuth += 360.;
            }

            double elevation = FastMath.toDegrees(position.getDelta());
            double distance = currentState.getPVCoordinates(observerFrame).getPosition().getNorm();
            double doppler = position.normalize().dotProduct(velocity);

            passDetails.add(new PassEventDetailsEntry(
                    currentState.getDate().toString(),
                    DoubleRound.round(azimuth, 2),
                    DoubleRound.round(elevation, 2),
                    DoubleRound.round(distance, 0),
                    DoubleRound.round(doppler, 0)
            ));

        }

    }


}
