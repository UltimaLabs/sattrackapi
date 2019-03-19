package com.ultimalabs.sattrackapi.position.service;

import com.ultimalabs.sattrackapi.position.model.SatellitePosition;

public interface PositionService {

    SatellitePosition positionBySatelliteNumber(int satNum);

    SatellitePosition positionByInternationalDesignator(String intDesignator);

}
