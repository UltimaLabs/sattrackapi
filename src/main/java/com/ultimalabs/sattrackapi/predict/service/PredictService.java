package com.ultimalabs.sattrackapi.predict.service;

public interface PredictService {

    void visibilityBySatelliteNumber(int satNum, double longitude, double latitude, double altitude, double minElevation);

    void visibilityByInternationalDesignator(String intDesignator, double longitude, double latitude, double altitude, double minElevation);

}
