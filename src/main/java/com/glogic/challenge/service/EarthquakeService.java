package com.glogic.challenge.service;

import com.glogic.challenge.model.FeatureCollection;
import com.glogic.challenge.model.FeatureCount;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * The interface Earthquake service.
 */
public interface EarthquakeService {
    /**
     * Gets earthquakes between dates.
     *
     * @param starttime the starttime
     * @param endtime   the endtime
     * @return the earthquakes between dates
     */
    ResponseEntity<FeatureCollection> getEarthquakesBetweenDates(Date starttime,
                                                                 Date endtime);

    /**
     * Gets earthquakes between magnitudes.
     *
     * @param minMagnitude the min magnitude
     * @param maxMagnitude the max magnitude
     * @return the earthquakes between magnitudes
     */
    ResponseEntity<FeatureCollection> getEarthquakesBetweenMagnitudes(BigDecimal minMagnitude,
                                                                      BigDecimal maxMagnitude);

    /**
     * Gets earthquakes by countries between dates.
     *
     * @param countryCode       the country code
     * @param anotherCountrCode the another countr code
     * @param startDate         the start date
     * @param endDate           the end date
     * @return the earthquakes by countries between dates
     */
    ResponseEntity<FeatureCount> getEarthquakesByCountriesBetweenDates(String countryCode,
                                                                       String anotherCountrCode,
                                                                       Date startDate,
                                                                       Date endDate) throws IOException;

    /**
     * Gets earthquakes by country.
     *
     * @param countryCode the country code
     * @return the earthquakes by country
     */
    ResponseEntity<FeatureCollection> getEarthquakesByCountry(String countryCode) throws IOException;

    /**
     * Gets earthquakes between two ranges of dates.
     *
     * @param firstStartDate  the first start date
     * @param firstEndDate    the first end date
     * @param secondStartDate the second start date
     * @param secondEndDate   the second end date
     * @return the earthquakes between two ranges of dates
     */
    ResponseEntity<List<FeatureCollection>> getEarthquakesBetweenTwoRangesOfDates(Date firstStartDate,
                                                                            Date firstEndDate,
                                                                            Date secondStartDate,
                                                                            Date secondEndDate);
}
