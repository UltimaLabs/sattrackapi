package com.ultimalabs.sattrackapi.predict.model;

import lombok.Data;

import java.util.List;

/**
 * Event data for a satellite pass
 */
@Data
public class PassEventData {

    /**
     * Satellite rise time
     */
    private String rise;

    /**
     * Satellite set time
     */
    private String set;

    /**
     * Pass duration
     */
    private double duration;

    /**
     * Pass details
     */
    private List<PassEventDetails> details;

}
