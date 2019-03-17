package com.ultimalabs.sattrackapi.service;

import com.ultimalabs.sattrackapi.model.TLEPlus;

public interface TleFetcherService {

    TLEPlus getTleBySatelliteId(int id);

    TLEPlus getTleByInternationalDesignator(String designator);

}
