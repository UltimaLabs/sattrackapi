package com.ultimalabs.sattrackapi.predict.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Event data for a satellite pass
 */
@Data
public class PassEventData {

    /**
     * Current date and time
     */
    private Date now;

    /**
     * Satellite rise time
     */
    private Date rise;

    /**
     * Satellite set time
     */
    private Date set;

    /**
     * Pass duration
     */
    private double duration;

    /**
     * Pass details
     */
    private List<PassEventDetails> details;

}
