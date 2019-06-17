package com.ultimalabs.sattrackapi.position.service;

import com.ultimalabs.sattrackapi.common.model.EarthParams;
import com.ultimalabs.sattrackapi.common.util.DoubleRound;
import com.ultimalabs.sattrackapi.position.model.SatellitePosition;
import com.ultimalabs.sattrackapi.tle.model.TLEPlus;
import com.ultimalabs.sattrackapi.tle.service.TleFetcherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.util.FastMath;
import org.orekit.bodies.BodyShape;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.frames.TopocentricFrame;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.tle.TLEPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Position service
 * <p>
 * Calculates satellite's position.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PositionServiceImpl implements PositionService {

    /**
     * TLE fetcher service
     */
    private final TleFetcherService tleFetcherService;

    /**
     * Returns a satellite's position
     *
     * @param searchString Satellite Number or International Designator
     * @return satellite's position or null if satellite can't be found
     */
    @Override
    public SatellitePosition getPosition(String searchString) {
        TLEPlus tle = tleFetcherService.getTle(searchString);
        return calculatePosition(tle);
    }

    /**
     * Calculates satellite's position
     *
     * @param tle TLE object
     * @return position object
     */
    private SatellitePosition calculatePosition(TLEPlus tle) {

        final BodyShape earth = new OneAxisEllipsoid(EarthParams.EQUATORIAL_RADIUS, EarthParams.FLATTENING, EarthParams.iers2010Frame);
        // observer/ground station coordinates are irrelevant in this context
        final GeodeticPoint station = new GeodeticPoint(FastMath.toRadians(0), FastMath.toRadians(0), 0);
        final TopocentricFrame stationFrame = new TopocentricFrame(earth, station, "ground station");

        TLEPropagator propagator = TLEPropagator.selectExtrapolator(tle);
        AbsoluteDate now = new AbsoluteDate(new Date(), TimeScalesFactory.getUTC());
        SpacecraftState finalState = propagator.propagate(now);

        final GeodeticPoint gp = convertToGeodeticPoint(finalState, stationFrame, earth);

        return new SatellitePosition(DoubleRound.round(FastMath.toDegrees(gp.getLatitude()), 4), DoubleRound.round(FastMath.toDegrees(gp.getLongitude()), 4));

    }

    /**
     * Transforms a SpacecraftState into a GeodeticPoint on the surface of the
     * given body.
     *
     * @param state the state to be transformed
     * @param frame the frame associated with the surface of the body
     * @param body  the body
     * @return the transformed GeodeticPoint
     */
    private GeodeticPoint convertToGeodeticPoint(final SpacecraftState state,
                                                 final TopocentricFrame frame,
                                                 final BodyShape body) {

        Vector3D pos = state.getPVCoordinates(frame).getPosition();
        return body.transform(pos, frame, state.getDate());
    }

}
