package com.ultimalabs.sattrackapi.predict.service;

import com.ultimalabs.sattrackapi.common.model.EarthParams;
import com.ultimalabs.sattrackapi.common.util.DoubleRound;
import com.ultimalabs.sattrackapi.predict.model.PassEventDataPoint;
import com.ultimalabs.sattrackapi.predict.model.SatellitePass;
import com.ultimalabs.sattrackapi.predict.util.PredictUtil;
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
import org.orekit.propagation.events.ElevationExtremumDetector;
import org.orekit.propagation.events.EventEnablingPredicateFilter;
import org.orekit.propagation.events.EventsLogger;
import org.orekit.propagation.events.EventsLogger.LoggedEvent;
import org.orekit.propagation.events.handlers.ContinueOnEvent;
import org.orekit.propagation.events.handlers.EventHandler;
import org.orekit.propagation.sampling.OrekitFixedStepHandler;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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
     * Returns next visibility event without pass details
     *
     * @param searchString Satellite Number or International Designator
     * @param longitude    observer longitude
     * @param latitude     observer latitude
     * @param altitude     observer altitude
     * @param minElevation minimal elevation
     * @return next visibility event, without the details
     */
    @Override
    public SatellitePass getNextEventWithoutDetails(String searchString, double latitude, double longitude, double altitude, double minElevation) {
        return getEventData(getTle(searchString), latitude, longitude, altitude, minElevation, 0.);
    }

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
    public SatellitePass getNextEventWithDetails(String searchString, double latitude, double longitude, double altitude, double minElevation, double stepSize) {
        return getEventData(getTle(searchString), latitude, longitude, altitude, minElevation, stepSize);
    }

    /**
     * Returns TLE object based on search string
     *
     * @param searchString Satellite Number or International Designator
     * @return TLE object
     */
    private TLEPlus getTle(String searchString) {
        return tleFetcherService.getTle(searchString);
    }

    /**
     * Returns next pass data
     *
     * @param tle      TLE object
     * @param lat      observer latitude
     * @param lon      observer longitude
     * @param alt      observer altitude
     * @param minEl    minimum elevation for visibility event
     * @param stepSize resolution for pass event details, in seconds;
     *                 if zero is passed as parameter, no details are returned
     * @return pass event data
     */
    private SatellitePass getEventData(TLEPlus tle, double lat, double lon, double alt, double minEl, double stepSize) {

        AbsoluteDate now = new AbsoluteDate(new Date(), TimeScalesFactory.getUTC());
        TLEPropagator propagator = TLEPropagator.selectExtrapolator(tle);
        EventsLogger logger = new EventsLogger();
        propagator.propagate(now);

        final BodyShape earth = new OneAxisEllipsoid(EarthParams.EQUATORIAL_RADIUS, EarthParams.FLATTENING, EarthParams.iers2010Frame);

        final GeodeticPoint observer = new GeodeticPoint(FastMath.toRadians(lat), FastMath.toRadians(lon), alt);
        final TopocentricFrame observerFrame = new TopocentricFrame(earth, observer, "observer");

        // Event definition
        final double maxCheck = 60.0;
        final double threshold = 10e-6;
        final double elevation = FastMath.toRadians(minEl);

        final ElevationDetector visibilityDetector =
                new ElevationDetector(maxCheck, threshold, observerFrame).
                        withConstantElevation(elevation).
                        withHandler(new VisibilityHandler());

        final ElevationExtremumDetector raw =
                new ElevationExtremumDetector(maxCheck, threshold, observerFrame).
                        withHandler(new ContinueOnEvent<>());

        final EventEnablingPredicateFilter<ElevationExtremumDetector> aboveGroundElevationDetector =
                new EventEnablingPredicateFilter<>(raw,
                        (state, eventDetector, g) -> eventDetector.getElevation(state) > elevation).withMaxCheck(maxCheck);

        propagator.addEventDetector(logger.monitorDetector(aboveGroundElevationDetector));
        propagator.addEventDetector(logger.monitorDetector(visibilityDetector));

        // Propagate from now to the first raising or for the fixed duration of 72 hours
        propagator.propagate(now.shiftedBy(259200.));

        // if all went well, we have a list with three events:
        // 0 - rise
        // 1 - midpoint
        // 2 - set
        if (logger.getLoggedEvents().size() != 3) {
            return null;
        }

        List<LoggedEvent> loggedEvents = logger.getLoggedEvents();

        LoggedEvent riseEvent = loggedEvents.get(0);
        LoggedEvent midPointEvent = loggedEvents.get(1);
        LoggedEvent setEvent = loggedEvents.get(2);

        AbsoluteDate riseDate = loggedEvents.get(0).getState().getDate();
        AbsoluteDate setDate = loggedEvents.get(2).getState().getDate();

        if (stepSize == 0.) {
            return new SatellitePass(
                    tle.getTle(),
                    now.getDate().toString(),
                    DoubleRound.round(riseDate.offsetFrom(now, TimeScalesFactory.getUTC()), 2),
                    PredictUtil.getEventDetails(riseEvent.getState(), observerFrame),
                    PredictUtil.getEventDetails(midPointEvent.getState(), observerFrame),
                    PredictUtil.getEventDetails(setEvent.getState(), observerFrame),
                    DoubleRound.round(setDate.offsetFrom(riseDate, TimeScalesFactory.getUTC()), 2),
                    Collections.emptyList()
            );
        }

        TLEPropagator masterModePropagator = TLEPropagator.selectExtrapolator(tle);
        masterModePropagator.propagate(riseDate);
        StepHandler stepHandler = new StepHandler(observerFrame);
        masterModePropagator.setMasterMode(stepSize, stepHandler);
        masterModePropagator.propagate(setDate);

        return new SatellitePass(
                tle.getTle(),
                now.getDate().toString(),
                DoubleRound.round(riseDate.offsetFrom(now, TimeScalesFactory.getUTC()), 2),
                PredictUtil.getEventDetails(riseEvent.getState(), observerFrame),
                PredictUtil.getEventDetails(midPointEvent.getState(), observerFrame),
                PredictUtil.getEventDetails(setEvent.getState(), observerFrame),
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
                return Action.CONTINUE;
            }

            return Action.STOP;

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

        private List<PassEventDataPoint> passDetails = new ArrayList<>();

        /**
         * Handle the current step
         *
         * @param currentState current state at step time
         * @param isLast       if true, this is the last integration step
         */
        public void handleStep(SpacecraftState currentState, boolean isLast) {

            passDetails.add(PredictUtil.getEventDetails(currentState, observerFrame));

        }

    }


}
