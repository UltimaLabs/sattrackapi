package com.ultimalabs.sattrackapi.predict.service;

public interface PredictService {

    String getVisibility(String searchString, double longitude, double latitude, double altitude, double minElevation);

}
