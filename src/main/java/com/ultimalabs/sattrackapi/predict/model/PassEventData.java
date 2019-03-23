package com.ultimalabs.sattrackapi.predict.model;

import lombok.Data;

import java.util.List;

/**
 * Event data for a satellite pass
 */
@Data
public class PassEventData {

    /**
     * Current date and time
     */
    private final String now;

    /**
     * Wait time for the rise event (rise - now), in seconds
     */
    private final Double wait;

    /**
     * Satellite rise time
     */
    private final String rise;

    /**
     * Satellite set time
     */
    private final String set;

    /**
     * Pass duration, in seconds
     */
    private final double duration;

    /**
     * Pass event entries
     */
    private final List<PassEventDetailsEntry> eventDetails;

}
