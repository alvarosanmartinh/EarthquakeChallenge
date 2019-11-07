package com.glogic.challenge.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glogic.challenge.model.FeatureCollection;
import com.glogic.challenge.service.EarthquakeService;
import com.glogic.challenge.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EarthquakeServiceImpl implements EarthquakeService {

    @Autowired
    private RestTemplate restTemplate;

    private UriComponentsBuilder builder;
    private SimpleDateFormat simpleDateFormat;
    private DecimalFormat decimalFormat;

    @Override
    public ResponseEntity<FeatureCollection> getEarthquakesBetweenDates(Date startDate, Date endDate) {
        simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

        builder = UriComponentsBuilder
                .fromUriString(Constants.QUERY_API_URL)
                .queryParam("starttime", simpleDateFormat.format(startDate))
                .queryParam("endtime", simpleDateFormat.format(endDate));

        return callService(builder.toUriString());
    }

    @Override
    public ResponseEntity<FeatureCollection> getEarthquakesBetweenMagnitudes(BigDecimal minMagnitude,
                                                                             BigDecimal maxMagnitude) {
        builder = UriComponentsBuilder
                .fromUriString(Constants.QUERY_API_URL)
                .queryParam("minmagnitude", minMagnitude)
                .queryParam("maxmagnitude", maxMagnitude);

        return callService(builder.toUriString());
    }

    @Override
    public ResponseEntity<List<FeatureCollection>> getEarthquakesInsideCircle(String countryCode, String anotherCountrCode) {

        List<FeatureCollection> response = new ArrayList<>();
        List<List<String>> countryCoordinates = new ArrayList<>();

        try{
            countryCoordinates.addAll(findCountriesCoordinatesByCountryCode(countryCode, anotherCountrCode));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(countryCoordinates.size()>1){
            return ResponseEntity.ok(response);
        }

        for (List<String> countryCoordinate: countryCoordinates) {
            builder = UriComponentsBuilder
                    .fromUriString(Constants.QUERY_API_URL)
                    .queryParam("latitude", countryCoordinate.get(0))
                    .queryParam("longitude", countryCoordinate.get(1))
                    .queryParam("maxradius", 90)
                    .queryParam("limit", Constants.API_LIMIT_RESULT_SIZE);

            response.add(restTemplate.getForObject(builder.toUriString(), FeatureCollection.class));
        }
        return ResponseEntity.ok(response);
    }

    private List<List<String>> findCountriesCoordinatesByCountryCode(String countryCode, String anotherCountrCode) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<List<String>> response = new ArrayList<>();

        File file = ResourceUtils.getFile("classpath:countriesCoordinates.json");
        Map map = objectMapper.readValue(new FileInputStream(file), Map.class);

        if(null != map.get(countryCode))
            response.add((List<String>) map.get(countryCode));
        if(null != map.get(anotherCountrCode))
            response.add((List<String>) map.get(anotherCountrCode));

        return response;
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
