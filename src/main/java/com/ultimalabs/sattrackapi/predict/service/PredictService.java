package com.ultimalabs.sattrackapi.predict.service;

import com.ultimalabs.sattrackapi.predict.model.SatellitePass;
import com.ultimalabs.sattrackapi.predict.model.dto.ObserverParams;
import com.ultimalabs.sattrackapi.predict.model.dto.TLEParams;

import java.util.List;

public interface PredictService {

    SatellitePass getNextEvent(TLEParams tleParams, ObserverParams observerParams);

    SatellitePass getNextEventWithDetails(TLEParams tleParams, ObserverParams observerParams, double stepSize);

    List<SatellitePass> getNextEvents(int numberOfDays, TLEParams tleParams, ObserverParams observerParams);

}
