package com.ultimalabs.sattrackapi.predict.predictor;

import lombok.RequiredArgsConstructor;
import org.orekit.propagation.events.EventsLogger.LoggedEvent;

import java.util.List;

@RequiredArgsConstructor
public class LoggedEventsException extends RuntimeException{

    private final List<LoggedEvent> loggedEvents;
    private final String satelliteName;

    @Override
    public String getMessage() {
        return "Could not log rise, midpoint and set events for satellite '"+ satelliteName +"'. Number of successfully logged events: " + loggedEvents.size();
    }
}
