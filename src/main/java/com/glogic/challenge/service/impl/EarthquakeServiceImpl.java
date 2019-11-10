package com.glogic.challenge.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glogic.challenge.model.Feature;
import com.glogic.challenge.model.FeatureCollection;
import com.glogic.challenge.model.FeatureCount;
import com.glogic.challenge.service.EarthquakeService;
import com.glogic.challenge.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
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

    private RestTemplate restTemplate;

    /**
     * Instantiates a new Earthquake service.
     *
     * @param builder the builder
     */
    public EarthquakeServiceImpl(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    private UriComponentsBuilder builder;
    private SimpleDateFormat simpleDateFormat;
    private DecimalFormat decimalFormat;

    @Override
    public ResponseEntity<FeatureCollection> getEarthquakesBetweenDates(Date startDate,
                                                                        Date endDate) {
        simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        String formattedStartDate = "0";
        String formattedEndDate = "0";

        try {
            formattedStartDate = simpleDateFormat.format(startDate);
            formattedEndDate = simpleDateFormat.format(endDate);
        } catch (Exception ignored) {
        }

        builder = UriComponentsBuilder
                .fromUriString(Constants.QUERY_API_URL)
                .queryParam("starttime", formattedStartDate)
                .queryParam("endtime", formattedEndDate);

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

        if (featureCollectionResponseEntity.getStatusCode() == HttpStatus.OK)
            response.add(featureCollectionResponseEntity.getBody());
        else {
            return ResponseEntity.status(featureCollectionResponseEntity.getStatusCode()).body(new ArrayList<>());
        }

        featureCollectionResponseEntity = getEarthquakesBetweenDates(secondStartDate, secondEndDate);

        if (featureCollectionResponseEntity.getStatusCode() == HttpStatus.OK)
            response.add(featureCollectionResponseEntity.getBody());
        else {
            return ResponseEntity.status(featureCollectionResponseEntity.getStatusCode()).body(new ArrayList<>());
        }

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<FeatureCollection> getEarthquakesBetweenMagnitudes(BigDecimal minMagnitude,
                                                                             BigDecimal maxMagnitude) {
        String formattedMinMagnitude = "null";
        String formattedMaxMagnitude = "null";

        try {
            formattedMinMagnitude = decimalFormat.format(minMagnitude);
            formattedMaxMagnitude = decimalFormat.format(maxMagnitude);
        } catch (Exception ignored) {
        }
        builder = UriComponentsBuilder
                .fromUriString(Constants.QUERY_API_URL)
                .queryParam("minmagnitude", formattedMinMagnitude)
                .queryParam("maxmagnitude", formattedMaxMagnitude);

        return callEarthquakesApiService(builder.toUriString());
    }

    @Override
    public ResponseEntity<FeatureCount> getEarthquakesByCountriesBetweenDates(String countryCode,
                                                                              String anotherCountrCode,
                                                                              Date startDate,
                                                                              Date endDate) throws IOException {
        simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        String formattedStartDate = "0";
        String formattedEndDate = "0";

        try {
            formattedStartDate = simpleDateFormat.format(startDate);
            formattedEndDate = simpleDateFormat.format(endDate);
        } catch (Exception ignored) {
        }

        FeatureCount response = new FeatureCount(new BigDecimal(0), new BigDecimal(20000));
        List<List<String>> countryCoordinates = new ArrayList<>();
        List<String> countryCodes = new ArrayList<>();

        countryCodes.add(countryCode);
        countryCodes.add(anotherCountrCode);

        countryCoordinates.addAll(findCountriesCoordinatesOnJsonFileByCountryCode(countryCodes));

        if (countryCoordinates.size() < 1) {
            return ResponseEntity.badRequest().body(response);
        }

        for (List<String> countryCoordinate : countryCoordinates) {
            builder = UriComponentsBuilder
                    .fromUriString(Constants.COUNT_API_URL)
                    .queryParam("starttime", formattedStartDate)
                    .queryParam("endtime", formattedEndDate)
                    .queryParam("latitude", countryCoordinate.get(0))
                    .queryParam("longitude", countryCoordinate.get(1))
                    .queryParam("maxradius", 90);

            ResponseEntity<FeatureCount> featureCountResponseEntity =
                    callEarthquakesCountApiService(builder.toUriString());
            if (featureCountResponseEntity.getStatusCode() == HttpStatus.OK) {
                response.setCount(response.getCount().add(featureCountResponseEntity.getBody().getCount()));
            } else {
                return featureCountResponseEntity;
            }
        }

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<FeatureCollection> getEarthquakesByCountry(String countryCode) throws IOException {

        FeatureCollection response = new FeatureCollection();
        List<List<String>> countryCoordinates = new ArrayList<>();

        List<String> countryCodes = new ArrayList<>();
        countryCodes.add(countryCode);

        countryCoordinates.addAll(findCountriesCoordinatesOnJsonFileByCountryCode(countryCodes));

        if (countryCoordinates.size() < 1) {
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

        for (String code : codes) {
            if (null != map.get(code))
                response.add((List<String>) map.get(code));
        }

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
