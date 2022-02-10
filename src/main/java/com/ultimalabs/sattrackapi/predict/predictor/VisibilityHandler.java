package com.ultimalabs.sattrackapi.predict.predictor;

import org.hipparchus.ode.events.Action;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.events.ElevationDetector;
import org.orekit.propagation.events.handlers.EventHandler;

/**
 * Handler for visibility events
 */
class VisibilityHandler implements EventHandler<ElevationDetector> {

    /**
     * Handle the event
     *
     * @param s          SpaceCraft state to be used in the evaluation
     * @param detector   object with appropriate type that can be used in determining correct return state
     * @param increasing with the event occurred in an "increasing" or "decreasing" slope direction
     * @return the Action that the calling detector should pass back to the evaluation system
     */
    @Override
    public Action eventOccurred(final SpacecraftState s, final ElevationDetector detector,
                                final boolean increasing) {
        if (increasing) {
            return Action.CONTINUE;
        }
        return Action.STOP;
    }
}