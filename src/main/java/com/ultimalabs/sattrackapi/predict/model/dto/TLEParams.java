package com.ultimalabs.sattrackapi.predict.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@RequiredArgsConstructor
public class TLEParams {
    private final String satelliteIdentifier;
    @Nullable private final String satelliteName;
    @Nullable private final String line1;
    @Nullable private final String line2;

    public TLEParams(String satelliteIdentifier) {
        this.satelliteIdentifier = satelliteIdentifier;
        this.satelliteName = null;
        this.line1 = null;
        this.line2 = null;
    }
}
