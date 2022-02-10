package com.ultimalabs.sattrackapi.predict.predictor;

import com.ultimalabs.sattrackapi.common.model.EarthParams;
import com.ultimalabs.sattrackapi.common.util.DoubleRound;
import com.ultimalabs.sattrackapi.predict.model.SatellitePass;
import com.ultimalabs.sattrackapi.predict.util.PredictUtil;
import com.ultimalabs.sattrackapi.tle.model.TLEPlus;
import lombok.Data;
import org.hipparchus.util.FastMath;
import org.orekit.bodies.BodyShape;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.frames.TopocentricFrame;
import org.orekit.propagation.analytical.tle.TLEPropagator;
import org.orekit.propagation.events.ElevationDetector;
import org.orekit.propagation.events.ElevationExtremumDetector;
import org.orekit.propagation.events.EventEnablingPredicateFilter;
import org.orekit.propagation.events.EventsLogger;
import org.orekit.propagation.events.EventsLogger.LoggedEvent;
import org.orekit.propagation.events.handlers.ContinueOnEvent;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;

import java.util.Collections;
import java.util.List;

@Data
public class Predictor {
    private final TLEPlus tle;
    private final TLEPropagator propagator;
    private final EventsLogger logger;
    private final TopocentricFrame observerFrame;
    private final double minElevation;
    private final VisibilityHandler visibilityHandler;
    private final StepHandler stepHandler;

    private LoggedEvent riseEvent;
    private LoggedEvent midPointEvent;
    private LoggedEvent setEvent;
    private AbsoluteDate riseDate;
    private AbsoluteDate setDate;

    public Predictor(TLEPlus tle, double observerLatitude, double observerLongitude, double observerAltitude, double minElevation) {
        this.tle = tle;
        this.propagator = TLEPropagator.selectExtrapolator(tle);
        this.logger = new EventsLogger();
        this.observerFrame = getObserverFrame(observerLatitude, observerLongitude, observerAltitude);
        this.minElevation = minElevation;

        this.visibilityHandler = new VisibilityHandler();
        this.stepHandler = new StepHandler(this.observerFrame);
    }

    /**
     * Returns next pass data extracted fromm logged events during propagation without pass details
     * @return pass event data
     * @throws LoggedEventsException Thrown when number of logged events is not valid
     */
    public SatellitePass getEventData(AbsoluteDate from, AbsoluteDate to) throws LoggedEventsException {

        // Propagate from now to the first raising or for the fixed duration of 72 hours
        propagateAndLogNextSatellitePass(from, to);

        return new SatellitePass(
                tle.getTle(),
                from.getDate().toString(),
                DoubleRound.round(riseDate.offsetFrom(from, TimeScalesFactory.getUTC()), 2),
                PredictUtil.getEventDetails(riseEvent.getState(), observerFrame),
                PredictUtil.getEventDetails(midPointEvent.getState(), observerFrame),
                PredictUtil.getEventDetails(setEvent.getState(), observerFrame),
                DoubleRound.round(setDate.offsetFrom(riseDate, TimeScalesFactory.getUTC()), 2),
                Collections.emptyList()
        );
    }

    /**
     *
     * Returns next pass data extracted fromm logged events during propagation and adds pass details
     * @param from - Absolute date from which to log pass events
     * @param to - Absolute date up to which to log pass events
     * @param stepSize - Duration of the steps in seconds after which some custom function is called during integration
     * @return pass event data with pass details
     * @throws LoggedEventsException Thrown when number of logged events is not valid
     */
    public SatellitePass getEventDataWithDetails(AbsoluteDate from, AbsoluteDate to, double stepSize) throws LoggedEventsException {

        // Propagate from now to the first raising or for the fixed duration of 72 hours
        propagateAndLogNextSatellitePass(from, to);

        TLEPropagator masterModePropagator = TLEPropagator.selectExtrapolator(tle);
        masterModePropagator.propagate(riseDate);
        masterModePropagator.setMasterMode(stepSize, stepHandler);
        masterModePropagator.propagate(setDate);

        return new SatellitePass(
                tle.getTle(),
                from.getDate().toString(),
                DoubleRound.round(riseDate.offsetFrom(from, TimeScalesFactory.getUTC()), 2),
                PredictUtil.getEventDetails(riseEvent.getState(), observerFrame),
                PredictUtil.getEventDetails(midPointEvent.getState(), observerFrame),
                PredictUtil.getEventDetails(setEvent.getState(), observerFrame),
                DoubleRound.round(setDate.offsetFrom(riseDate, TimeScalesFactory.getUTC()), 2),
                stepHandler.getPassDetails()
        );
    }

    /**
     * Propagates, logs all the events that occurred and sets the appropriate class fields
     * @param propagateFrom - Absolute date to propagate from
     * @param propagateTo - Absolute date to propagate to
     * @throws LoggedEventsException Thrown when number of logged events is not valid
     */
    private void propagateAndLogNextSatellitePass(AbsoluteDate propagateFrom, AbsoluteDate propagateTo) throws LoggedEventsException {

        addEventDetectorsToPropagator(minElevation);

        propagator.propagate(propagateFrom, propagateTo);

        // if all went well, we have a list with three events: rise, midpoint, set
        if (logger.getLoggedEvents().size() == 3)
            setLoggedEventsData();
        else
            throw new LoggedEventsException(logger.getLoggedEvents());
    }
    
    /**
     * Adds event detectors to the propagator and clears previously logged events from the logger
     * @param minEl - Minimal elevation from which to start event detection
     */
    private void addEventDetectorsToPropagator(double minEl) {
        // Event definition
        final double maxCheck = 60.0;
        final double threshold = 10e-6;
        final double elevation = FastMath.toRadians(minEl);

        final ElevationDetector visibilityDetector =
                new ElevationDetector(maxCheck, threshold, observerFrame).
                        withConstantElevation(elevation).
                        withHandler(visibilityHandler);

        final ElevationExtremumDetector raw =
                new ElevationExtremumDetector(maxCheck, threshold, observerFrame).
                        withHandler(new ContinueOnEvent<>());

        final EventEnablingPredicateFilter<ElevationExtremumDetector> aboveGroundElevationDetector =
                new EventEnablingPredicateFilter<>(raw,
                        (state, eventDetector, g) -> eventDetector.getElevation(state) > elevation).withMaxCheck(maxCheck);

        logger.clearLoggedEvents();

        propagator.clearEventsDetectors();
        propagator.addEventDetector(logger.monitorDetector(aboveGroundElevationDetector));
        propagator.addEventDetector(logger.monitorDetector(visibilityDetector));
    }

    /**
     * Sets all logged events data to the aproppriate class fields (riseEvent, riseDate, midPointEvent, setEvent, setDate)
     */
    private void setLoggedEventsData() {
        List<LoggedEvent> loggedEvents = logger.getLoggedEvents();

        riseEvent = loggedEvents.get(0);
        midPointEvent = loggedEvents.get(1);
        setEvent = loggedEvents.get(2);

        riseDate = loggedEvents.get(0).getState().getDate();
        setDate = loggedEvents.get(2).getState().getDate();
    }

    /**
     * Creates the observer frame based on the given coordinates
     * @param lat - Observer latitude
     * @param lon - Observer longitude
     * @param alt - Observer altitude
     * @return Observer frame
     */
    private static TopocentricFrame getObserverFrame(double lat, double lon, double alt) {

        final BodyShape earth = new OneAxisEllipsoid(EarthParams.EQUATORIAL_RADIUS, EarthParams.FLATTENING, EarthParams.iers2010Frame);
        final GeodeticPoint observer = new GeodeticPoint(FastMath.toRadians(lat), FastMath.toRadians(lon), alt);

        return new TopocentricFrame(earth, observer, "observer");
    }
}
