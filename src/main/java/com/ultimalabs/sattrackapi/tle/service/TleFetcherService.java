package com.ultimalabs.sattrackapi.tle.service;

import com.ultimalabs.sattrackapi.tle.model.TLEPlus;

public interface TleFetcherService {

    TLEPlus getTle(String searchString);

    TLEPlus getTleBySatelliteId(int id);

    TLEPlus getTleByInternationalDesignator(String designator);

}
