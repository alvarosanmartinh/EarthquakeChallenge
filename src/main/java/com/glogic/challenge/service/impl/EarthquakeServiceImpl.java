package com.glogic.challenge.service.impl;

import com.glogic.challenge.model.FeatureCollection;
import com.glogic.challenge.service.EarthquakeService;
import com.glogic.challenge.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class EarthquakeServiceImpl implements EarthquakeService {

    @Autowired
    private RestTemplate restTemplate;

    private UriComponentsBuilder builder;
    private SimpleDateFormat simpleDateFormat;
    private DecimalFormat decimalFormat;

    @Override
    public ResponseEntity<FeatureCollection> getEarthquakesBetweenDates(Date startDate, Date endDate) {
        simpleDateFormat = new SimpleDateFormat(Constants.dateFormat);

        builder = UriComponentsBuilder
                .fromUriString(Constants.queryApiUrl)
                .queryParam("starttime", simpleDateFormat.format(startDate))
                .queryParam("endtime", simpleDateFormat.format(endDate));

        return callService(builder.toUriString());
    }

    @Override
    public ResponseEntity<FeatureCollection> getEarthquakesBetweenMagnitudes(BigDecimal minMagnitude,
                                                                             BigDecimal maxMagnitude) {
        decimalFormat = new DecimalFormat(Constants.magnitudeFormat);

        builder = UriComponentsBuilder
                .fromUriString(Constants.queryApiUrl)
                .queryParam("minmagnitude", minMagnitude)
                .queryParam("maxmagnitude", maxMagnitude);

        return callService(builder.toUriString());
    }

    private ResponseEntity<FeatureCollection> callService(String url) {
        try {
            FeatureCollection response = restTemplate.getForObject(url, FeatureCollection.class);
            return ResponseEntity.ok(response);
        } catch (HttpClientErrorException clientException) {
            return ResponseEntity.badRequest().body(new FeatureCollection());
        } catch (HttpServerErrorException serverException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new FeatureCollection());
        }
    }
}
