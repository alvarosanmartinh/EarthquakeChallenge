package com.glogic.challenge.service;

import com.glogic.challenge.model.FeatureCollection;
import com.glogic.challenge.model.FeatureCount;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface EarthquakeService {
    ResponseEntity<FeatureCollection> getEarthquakesBetweenDates(Date starttime,
                                                                 Date endtime);

    ResponseEntity<FeatureCollection> getEarthquakesBetweenMagnitudes(BigDecimal minMagnitude,
                                                                      BigDecimal maxMagnitude);

    ResponseEntity<FeatureCount> getEarthquakesByCountriesBetweenDates(String countryCode,
                                                                       String anotherCountrCode,
                                                                       Date startDate,
                                                                       Date endDate);

    ResponseEntity<FeatureCollection> getEarthquakesByCountry(String countryCode);

    ResponseEntity<List<FeatureCollection>> getEarthquakesBetweenTwoRangesOfDates(Date firstStartDate,
                                                                            Date firstEndDate,
                                                                            Date secondStartDate,
                                                                            Date secondEndDate);
}
