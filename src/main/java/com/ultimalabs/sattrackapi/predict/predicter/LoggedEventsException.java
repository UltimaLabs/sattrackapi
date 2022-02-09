package com.ultimalabs.sattrackapi.predict.predicter;

import lombok.RequiredArgsConstructor;
import org.orekit.propagation.events.EventsLogger.LoggedEvent;

import java.util.List;

@RequiredArgsConstructor
public class LoggedEventsException extends RuntimeException{

    private final List<LoggedEvent> loggedEvents;

    @Override
    public String getMessage() {
        return "Error occured while logging events occurred during propagation. Number of logged events: " + loggedEvents.size();
    }
}
