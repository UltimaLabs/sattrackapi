package com.ultimalabs.sattrackapi.predict.model.dto;

import lombok.Data;

@Data
public class ObserverParams {
    private final double longitude;
    private final double latitude;
    private final double altitude;
    private final double minElevation;
}
