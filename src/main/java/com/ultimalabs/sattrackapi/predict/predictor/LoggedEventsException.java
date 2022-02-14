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
        return "Error occured while logging events for satellite '"+ satelliteName +"' occurred during propagation. Number of logged events: " + loggedEvents.size();
    }
}
