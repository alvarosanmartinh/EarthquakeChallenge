package com.glogic.challenge.service;

import com.glogic.challenge.model.FeatureCollection;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Date;

public interface EarthquakeService {
    ResponseEntity<FeatureCollection> getEarthquakesBetweenDates(Date starttime, Date endtime);

    ResponseEntity<FeatureCollection> getEarthquakesBetweenMagnitudes(BigDecimal minMagnitude,
                                                                      BigDecimal maxMagnitude);
}
