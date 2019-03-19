package com.ultimalabs.sattrackapi.predict.service;

public interface PredictService {

    String visibilityBySatelliteNumber(int satNum, double longitude, double latitude, double altitude, double minElevation);

    String visibilityByInternationalDesignator(String intDesignator, double longitude, double latitude, double altitude, double minElevation);

}
