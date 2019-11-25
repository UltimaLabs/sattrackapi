package com.ultimalabs.sattrackapi.predict.util;

import com.ultimalabs.sattrackapi.common.util.DoubleRound;
import com.ultimalabs.sattrackapi.predict.model.PassEventDetailsEntry;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.util.FastMath;
import org.orekit.frames.TopocentricFrame;
import org.orekit.frames.Transform;
import org.orekit.propagation.SpacecraftState;
import org.orekit.utils.PVCoordinates;

/**
 * Predict utility class
 */
public class PredictUtil {

    private PredictUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Creates a PassEventDetailsEntry from a spacecraft state and observer frame
     *
     * @param s             spacecraft state
     * @param observerFrame observer frame
     * @return pass event details
     */
    public static PassEventDetailsEntry getEventDetails(SpacecraftState s, TopocentricFrame observerFrame) {

        // get transform between state reference frame
        // and observer at current time
        Transform transform = s.getFrame().getTransformTo(observerFrame,
                s.getDate());

        // get position-velocity in ground station frame
        PVCoordinates pv = transform.transformPVCoordinates(s.getPVCoordinates());
        Vector3D position = pv.getPosition();
        Vector3D velocity = pv.getVelocity();

        // extract pointing data
        double azimuth = FastMath.toDegrees(position.getAlpha() * -1.) + 90;

        if (azimuth < 0.) {
            azimuth += 360.;
        }

        double elevation = FastMath.toDegrees(position.getDelta());
        double distance = s.getPVCoordinates(observerFrame).getPosition().getNorm();
        double doppler = position.normalize().dotProduct(velocity);

        return new PassEventDetailsEntry(
                s.getDate().toString(),
                DoubleRound.round(azimuth, 2),
                DoubleRound.round(elevation, 2),
                DoubleRound.round(distance, 0),
                DoubleRound.round(doppler, 0)
        );

    }

}
