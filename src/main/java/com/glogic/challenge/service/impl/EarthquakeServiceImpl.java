package com.glogic.challenge.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glogic.challenge.model.Feature;
import com.glogic.challenge.model.FeatureCollection;
import com.glogic.challenge.model.FeatureCount;
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

import javax.xml.ws.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The type Earthquake service.
 */
@Service
public class EarthquakeServiceImpl implements EarthquakeService {

    @Autowired
    private RestTemplate restTemplate;

    private UriComponentsBuilder builder;
    private SimpleDateFormat simpleDateFormat;
    private DecimalFormat decimalFormat;

    @Override
    public ResponseEntity<FeatureCollection> getEarthquakesBetweenDates(Date startDate,
                                                                        Date endDate) {
        simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

        builder = UriComponentsBuilder
                .fromUriString(Constants.QUERY_API_URL)
                .queryParam("starttime", simpleDateFormat.format(startDate))
                .queryParam("endtime", simpleDateFormat.format(endDate));

        return callEarthquakesApiService(builder.toUriString());
    }

    @Override
    public ResponseEntity<List<FeatureCollection>> getEarthquakesBetweenTwoRangesOfDates(Date firstStartDate,
                                                                                         Date firstEndDate,
                                                                                         Date secondStartDate,
                                                                                         Date secondEndDate) {
        ArrayList<FeatureCollection> response = new ArrayList<>();

        ResponseEntity<FeatureCollection> featureCollectionResponseEntity =
                getEarthquakesBetweenDates(firstStartDate, firstEndDate);

        if(featureCollectionResponseEntity.getStatusCode() == HttpStatus.OK)
            response.add(featureCollectionResponseEntity.getBody());
        else {
            return ResponseEntity.status(featureCollectionResponseEntity.getStatusCode()).body(new ArrayList<>());
        }

        featureCollectionResponseEntity = getEarthquakesBetweenDates(secondStartDate, secondEndDate);

        if(featureCollectionResponseEntity.getStatusCode() == HttpStatus.OK)
            response.add(featureCollectionResponseEntity.getBody());
        else {
            return ResponseEntity.status(featureCollectionResponseEntity.getStatusCode()).body(new ArrayList<>());
        }

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<FeatureCollection> getEarthquakesBetweenMagnitudes(BigDecimal minMagnitude,
                                                                             BigDecimal maxMagnitude) {
        builder = UriComponentsBuilder
                .fromUriString(Constants.QUERY_API_URL)
                .queryParam("minmagnitude", minMagnitude)
                .queryParam("maxmagnitude", maxMagnitude);

        return callEarthquakesApiService(builder.toUriString());
    }

    @Override
    public ResponseEntity<FeatureCount> getEarthquakesByCountriesBetweenDates(String countryCode,
                                                                              String anotherCountrCode,
                                                                              Date startDate,
                                                                              Date endDate) {
        simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

        FeatureCount response = new FeatureCount(new BigDecimal(0),new BigDecimal(20000));
        List<List<String>> countryCoordinates = new ArrayList<>();
        List<String> countryCodes = new ArrayList<>();

        countryCodes.add(countryCode);
        countryCodes.add(anotherCountrCode);

        try{
            countryCoordinates.addAll(findCountriesCoordinatesOnJsonFileByCountryCode(countryCodes));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(countryCoordinates.size()<1){
            return ResponseEntity.ok(response);
        }

        for (List<String> countryCoordinate: countryCoordinates) {
            builder = UriComponentsBuilder
                    .fromUriString(Constants.COUNT_API_URL)
                    .queryParam("starttime", simpleDateFormat.format(startDate))
                    .queryParam("endtime", simpleDateFormat.format(endDate))
                    .queryParam("latitude", countryCoordinate.get(0))
                    .queryParam("longitude", countryCoordinate.get(1))
                    .queryParam("maxradius", 90);

            ResponseEntity<FeatureCount> featureCountResponseEntity =
                    callEarthquakesCountApiService(builder.toUriString());
            if(featureCountResponseEntity.getStatusCode() == HttpStatus.OK){
                response.setCount(response.getCount().add(featureCountResponseEntity.getBody().getCount()));
            }else{
                return featureCountResponseEntity;
            }
        }

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<FeatureCollection> getEarthquakesByCountry(String countryCode) {

        FeatureCollection response = new FeatureCollection();
        List<List<String>> countryCoordinates = new ArrayList<>();

        List<String> countryCodes = new ArrayList<>();
        countryCodes.add(countryCode);

        try{
            countryCoordinates.addAll(findCountriesCoordinatesOnJsonFileByCountryCode(countryCodes));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(countryCoordinates.size()<1){
            return ResponseEntity.ok(response);
        }

        builder = UriComponentsBuilder
                .fromUriString(Constants.QUERY_API_URL)
                .queryParam("latitude", countryCoordinates.get(0).get(0))
                .queryParam("longitude", countryCoordinates.get(0).get(1))
                .queryParam("maxradiuskm", Constants.API_MAX_RADIUS_KM)
                .queryParam("limit", Constants.API_LIMIT_RESULT_SIZE);

        return callEarthquakesApiService(builder.toUriString());
    }



    private List<List<String>> findCountriesCoordinatesOnJsonFileByCountryCode(List<String> codes) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<List<String>> response = new ArrayList<>();

        File file = ResourceUtils.getFile("classpath:countriesCoordinates.json");
        Map map = objectMapper.readValue(new FileInputStream(file), Map.class);

        if(null != map.get(codes.get(0)))
            response.add((List<String>) map.get(codes.get(0)));
        if(null != map.get(codes.get(1)))
            response.add((List<String>) map.get(codes.get(1)));

        return response;
    }

    private ResponseEntity<FeatureCollection> callEarthquakesApiService(String url) {
        try {
            FeatureCollection response = restTemplate.getForObject(url, FeatureCollection.class);
            return ResponseEntity.ok(response);
        } catch (HttpClientErrorException clientException) {
            return ResponseEntity.badRequest().body(new FeatureCollection());
        } catch (HttpServerErrorException serverException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new FeatureCollection());
        }
    }


    private ResponseEntity<FeatureCount> callEarthquakesCountApiService(String url) {
        try {
            FeatureCount response = restTemplate.getForObject(url, FeatureCount.class);
            return ResponseEntity.ok(response);
        } catch (HttpClientErrorException clientException) {
            return ResponseEntity.badRequest().body(new FeatureCount());
        } catch (HttpServerErrorException serverException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new FeatureCount());
        }
    }

}
