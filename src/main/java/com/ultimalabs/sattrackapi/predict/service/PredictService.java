package com.ultimalabs.sattrackapi.predict.service;

import com.ultimalabs.sattrackapi.predict.model.SatellitePass;
import com.ultimalabs.sattrackapi.predict.model.dto.ObserverParams;
import com.ultimalabs.sattrackapi.predict.model.dto.TLEParams;

import java.util.List;

public interface PredictService {

    SatellitePass getNextEventWithoutDetails(TLEParams tleParams, ObserverParams observerParams);

    SatellitePass getNextEventWithDetails(TLEParams tleParams, ObserverParams observerParams, double stepSize);

    List<SatellitePass> getNextEventsWithoutDetails(int n, TLEParams tleParams, ObserverParams observerParams);

}
