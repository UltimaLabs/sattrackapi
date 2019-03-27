package com.ultimalabs.sattrackapi.predict.service;

import com.ultimalabs.sattrackapi.predict.model.PassEventData;

public interface PredictService {

    PassEventData getNextEventWithDetails(String searchString, double longitude, double latitude, double altitude, double minElevation, double stepSize);

    PassEventData getNextEventWithoutDetails(String searchString, double longitude, double latitude, double altitude, double minElevation);

}
