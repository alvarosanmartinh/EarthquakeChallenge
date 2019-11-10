package com.glogic.challenge.controller;

import com.glogic.challenge.model.FeatureCollection;
import com.glogic.challenge.model.FeatureCount;
import com.glogic.challenge.service.EarthquakeService;
import com.glogic.challenge.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * The Earthquake REST controller.
 */
@RestController
public class EarthquakeController {

    private final EarthquakeService earthQuakeService;

    public EarthquakeController(EarthquakeService earthQuakeService) {
        this.earthQuakeService = earthQuakeService;
    }

    /**
     * Gets earthquakes between dates.
     *
     * @param startDate the start date
     * @param endDate   the end date
     * @return the earthquakes between dates
     */
    @RequestMapping(value = "getEarthquakesBetweenDates", method = RequestMethod.GET)
    public ResponseEntity<FeatureCollection> getEarthquakesBetweenDates(
            @RequestParam("startDate") @DateTimeFormat(pattern = Constants.DATE_FORMAT) Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = Constants.DATE_FORMAT) Date endDate
    ) {
        return earthQuakeService.getEarthquakesBetweenDates(startDate, endDate);
    }

    /**
     * Gets earthquakes between magnitudes.
     *
     * @param minMagnitude the min magnitude
     * @param maxMagnitude the max magnitude
     * @return the earthquakes between magnitudes
     */
    @RequestMapping(value = "getEarthquakesBetweenMagnitudes", method = RequestMethod.GET)
    public ResponseEntity<FeatureCollection> getEarthquakesBetweenMagnitudes(
            @RequestParam("minMagnitude") @NumberFormat(pattern = Constants.MAGNITUDE_FORMAT) BigDecimal minMagnitude,
            @RequestParam("maxMagnitude") @NumberFormat(pattern = Constants.MAGNITUDE_FORMAT) BigDecimal maxMagnitude
    ) {
        return earthQuakeService.getEarthquakesBetweenMagnitudes(
                minMagnitude,
                maxMagnitude);
    }

    /**
     * Gets earthquakes by countries between dates.
     *
     * @param countryCode        the country code
     * @param anotherCountryCode the another country code
     * @param startDate          the start date
     * @param endDate            the end date
     * @return the earthquakes by countries between dates
     */
    @RequestMapping(value = "getEarthquakesByCountriesBetweenDates", method = RequestMethod.GET)
    public ResponseEntity<FeatureCount> getEarthquakesByCountriesBetweenDates(
            @RequestParam("countryCode") String countryCode,
            @RequestParam("anotherCountryCode") String anotherCountryCode,
            @RequestParam("startDate") @DateTimeFormat(pattern = Constants.DATE_FORMAT) Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = Constants.DATE_FORMAT) Date endDate
    ) {
        return earthQuakeService.getEarthquakesByCountriesBetweenDates(
                countryCode,
                anotherCountryCode,
                startDate,
                endDate);
    }

    /**
     * Gets earthquakes by country.
     *
     * @param countryCode the country code
     * @return the earthquakes by country
     */
    @RequestMapping(value = "getEarthquakesByCountry", method = RequestMethod.GET)
    public ResponseEntity<FeatureCollection> getEarthquakesByCountry(
            @RequestParam("countryCode") String countryCode
    ) {
        return earthQuakeService.getEarthquakesByCountry(countryCode);
    }

    /**
     * Gets earthquakes between two ranges of dates.
     *
     * @param firstStartDate  the first start date
     * @param firstEndDate    the first end date
     * @param secondStartDate the second start date
     * @param secondEndDate   the second end date
     * @return the earthquakes between two ranges of dates
     */
    @RequestMapping(value = "getEarthquakesBetweenTwoRangesOfDates", method = RequestMethod.GET)
    public ResponseEntity<List<FeatureCollection>> getEarthquakesBetweenTwoRangesOfDates(
            @RequestParam("firstStartDate") @DateTimeFormat(pattern = Constants.DATE_FORMAT) Date firstStartDate,
            @RequestParam("firstEndDate") @DateTimeFormat(pattern = Constants.DATE_FORMAT) Date firstEndDate,
            @RequestParam("secondStartDate") @DateTimeFormat(pattern = Constants.DATE_FORMAT) Date secondStartDate,
            @RequestParam("secondEndDate") @DateTimeFormat(pattern = Constants.DATE_FORMAT) Date secondEndDate
    ) {
        return earthQuakeService.getEarthquakesBetweenTwoRangesOfDates(
                firstStartDate,
                firstEndDate,
                secondStartDate,
                secondEndDate);
    }

}
