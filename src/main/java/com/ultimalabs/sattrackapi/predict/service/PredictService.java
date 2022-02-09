package com.ultimalabs.sattrackapi.predict.service;

import com.ultimalabs.sattrackapi.predict.model.SatellitePass;

import java.util.List;

public interface PredictService {

    SatellitePass getNextEventWithDetails(String searchString, double longitude, double latitude, double altitude, double minElevation, double stepSize);

    SatellitePass getNextEventWithoutDetails(String searchString, double longitude, double latitude, double altitude, double minElevation);

    List<SatellitePass> getMultipleNextEventsWithoutDetails(Integer numberOfEvents, String searchString, double longitude, double latitude, double altitude, double minElevation);

}
