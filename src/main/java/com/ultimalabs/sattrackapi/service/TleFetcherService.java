package com.ultimalabs.sattrackapi.service;

import com.ultimalabs.sattrackapi.model.NamedTLE;

public interface TleFetcherService {

    NamedTLE getTleBySatelliteId(int id);

    NamedTLE getTleByInternationalDesignator(String designator);

}
