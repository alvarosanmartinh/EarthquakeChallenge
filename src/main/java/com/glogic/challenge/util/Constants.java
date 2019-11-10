package com.glogic.challenge.util;

/**
 * The Constants class/file, used to store several constants which may be used at any place of the project.
 */
public class Constants {
    /**
     * The URL used to connect to the Earthquakes API
     */
    private static final String BASE_API_URL = "https://earthquake.usgs.gov/fdsnws/event/1/";
    /**
     * The most used format to add to the API URL, with this format the responses will be JSON
     */
    private static final String FORMAT_GEOJSON = "?format=geojson";
    /**
     * The constant QUERY_API_URL, composed from the base API URL, and the parameter query, so it responds with details
     * instead of the quantity of results.
     */
    public static final String QUERY_API_URL = BASE_API_URL + "query" + FORMAT_GEOJSON;
    /**
     * The constant COUNT_API_URL, composed from the base API URL, and the parameter count, so it responds with the
     * quantity of results instead of the details.
     */
    public static final String COUNT_API_URL = BASE_API_URL + "count" + FORMAT_GEOJSON;
    /**
     * The constant DATE_FORMAT, used to format the dates before they are added to the API URL as parameters.
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    /**
     * The constant MAGNITUDE_FORMAT, used to format the magnitudes before they are added to the API URL as parameters.
     */
    public static final String MAGNITUDE_FORMAT = "0.0";
    /**
     * The constant API_MAX_RADIUS_KM, used as parameter of the API URL, so it returns results with this km radius.
     */
    public static final Integer API_MAX_RADIUS_KM = 1000;
    /**
     * The constant API_LIMIT_RESULT_SIZE, used as a parameter of the API URL, so it returns this quantity of details
     * when the parameter query is used at the URL too.
     */
    public static final Integer API_LIMIT_RESULT_SIZE = 500;
}
