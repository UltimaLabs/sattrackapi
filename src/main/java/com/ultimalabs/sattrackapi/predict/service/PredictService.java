package com.ultimalabs.sattrackapi.predict.service;

import com.ultimalabs.sattrackapi.predict.model.PassEventData;

public interface PredictService {

    PassEventData getVisibility(String searchString, double longitude, double latitude, double altitude, double minElevation);

}
