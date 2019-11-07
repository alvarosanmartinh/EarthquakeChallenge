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

@RestController
public class EarthquakeController {

    @Autowired
    private EarthquakeService earthQuakeService;

    @RequestMapping(value = "getEarthquakesBetweenDates", method = RequestMethod.GET)
    public ResponseEntity<FeatureCollection> getEarthquakesBetweenDates(
            @RequestParam("startDate") @DateTimeFormat(pattern = Constants.DATE_FORMAT) Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = Constants.DATE_FORMAT) Date endDate
            ) {
        return earthQuakeService.getEarthquakesBetweenDates(startDate, endDate);
    }

    @RequestMapping(value = "getEarthquakesBetweenMagnitudes", method = RequestMethod.GET)
    public ResponseEntity<FeatureCollection> getEarthquakesBetweenMagnitudes(
            @RequestParam("minMagnitude") @NumberFormat(pattern = Constants.MAGNITUDE_FORMAT) BigDecimal minMagnitude,
            @RequestParam("maxMagnitude") @NumberFormat(pattern = Constants.MAGNITUDE_FORMAT) BigDecimal maxMagnitude
    ) {
        return earthQuakeService.getEarthquakesBetweenMagnitudes(minMagnitude, maxMagnitude);
    }


    @RequestMapping(value = "getEarthquakesByCountriesBetweenDates", method = RequestMethod.GET)
    public ResponseEntity<FeatureCount> getEarthquakesByCountriesBetweenDates(
            @RequestParam("countryCode") String countryCode,
            @RequestParam("anotherCountryCode") String anotherCountryCode,
            @RequestParam("startDate") @DateTimeFormat(pattern = Constants.DATE_FORMAT) Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = Constants.DATE_FORMAT) Date endDate
    ) {
        return earthQuakeService.getEarthquakesByCountriesBetweenDates(countryCode, anotherCountryCode, startDate, endDate);
    }


    @RequestMapping(value = "getEarthquakesByCountry", method = RequestMethod.GET)
    public ResponseEntity<FeatureCollection> getEarthquakesByCountry(
            @RequestParam("countryCode") String countryCode
    ) {
        return earthQuakeService.getEarthquakesByCountry(countryCode);
    }
}
