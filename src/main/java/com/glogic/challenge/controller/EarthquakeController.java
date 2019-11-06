package com.glogic.challenge.controller;

import com.glogic.challenge.model.FeatureCollection;
import com.glogic.challenge.service.EarthquakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;

@RestController
public class EarthquakeController {

    @Autowired
    private EarthquakeService earthQuakeService;

    @RequestMapping(value = "getEarthquakesBetweenDates", method = RequestMethod.GET)
    public ResponseEntity<FeatureCollection> getEarthquakesBetweenDates(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-mm-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-mm-dd") Date endDate
            ) {
        return earthQuakeService.getEarthquakesBetweenDates(startDate, endDate);
    }

    @RequestMapping(value = "getEarthquakesBetweenMagnitudes", method = RequestMethod.GET)
    public ResponseEntity<FeatureCollection> getEarthquakesBetweenMagnitudes(
            @RequestParam("minMagnitude") @NumberFormat(pattern = "#.#") BigDecimal minMagnitude,
            @RequestParam("maxMagnitude") @NumberFormat(pattern = "#.#") BigDecimal maxMagnitude
    ) {
        return earthQuakeService.getEarthquakesBetweenMagnitudes(minMagnitude, maxMagnitude);
    }
}
