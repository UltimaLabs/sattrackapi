package com.ultimalabs.sattrackapi.position.service;

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
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.frames.TopocentricFrame;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.tle.TLEPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

/**
 * Position service
 *
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
     * Equatorial radius in meters
     */
    private final double equatorialRadius = Constants.WGS84_EARTH_EQUATORIAL_RADIUS;

    /**
     * Earth flattening
     */
    private final double earthFlattening = Constants.WGS84_EARTH_FLATTENING;

    /**
     * Earth frame
     */
    private final Frame earthFrame = FramesFactory.getITRF(IERSConventions.IERS_2010, true);

    /**
     * Returns a satellite's position
     *
     * @param searchString Satellite Number or International Designator
     * @return satellite's position or null if satellite can't be found
     */
    @Override
    public SatellitePosition getPosition(String searchString) {
        TLEPlus tle = tleFetcherService.getTle(searchString);
        if (tle == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return calculatePosition(tle);
    }

    /**
     * Calculates satellite's position
     *
     * @param tle TLE object
     * @return position object
     */
    private SatellitePosition calculatePosition(TLEPlus tle) {

        final BodyShape earth = new OneAxisEllipsoid(equatorialRadius, earthFlattening, earthFrame);
        // observer coordinates are irrelevant in this context
        final GeodeticPoint station = new GeodeticPoint(FastMath.toRadians(0), FastMath.toRadians(0), 0);
        final TopocentricFrame stationFrame = new TopocentricFrame(earth, station, "ground station");

        TLEPropagator propagator = TLEPropagator.selectExtrapolator(tle);
        AbsoluteDate now = new AbsoluteDate(new Date(), TimeScalesFactory.getUTC());
        SpacecraftState finalState = propagator.propagate(now);

        final GeodeticPoint gp = convertToGeodeticPoint(finalState, stationFrame, earth);

        return new SatellitePosition(FastMath.toDegrees(gp.getLatitude()), FastMath.toDegrees(gp.getLongitude()));

    }

    /**
     * Transforms a SpacecraftState into a GeodeticPoint on the surface of the
     * given body.
     *
     * @param state the state to be transformed
     * @param frame the frame associated with the surface of the body
     * @param body  the body
     * @return the transformed GeodeticPoint
     * @throws OrekitException
     */
    private GeodeticPoint convertToGeodeticPoint(final SpacecraftState state,
                                                 final TopocentricFrame frame,
                                                 final BodyShape body)
            throws OrekitException {

        Vector3D pos = state.getPVCoordinates(frame).getPosition();
        return body.transform(pos, frame, state.getDate());
    }

}
