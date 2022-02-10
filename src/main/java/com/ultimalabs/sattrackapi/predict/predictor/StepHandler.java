package com.ultimalabs.sattrackapi.predict.predictor;

import com.ultimalabs.sattrackapi.predict.model.PassEventDataPoint;
import com.ultimalabs.sattrackapi.predict.util.PredictUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.orekit.frames.TopocentricFrame;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.sampling.OrekitFixedStepHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Specialized step handler
 * <p>
 * This class extends the step handler in order to save the parameters at the given step
 */
@Getter
@RequiredArgsConstructor
class StepHandler implements OrekitFixedStepHandler {

    private final TopocentricFrame observerFrame;

    private final List<PassEventDataPoint> passDetails = new ArrayList<>();

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