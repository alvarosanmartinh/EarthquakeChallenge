package com.glogic.challenge.util;

public class Constants {
    private static final String BASE_API_URL = "https://earthquake.usgs.gov/fdsnws/event/1/";
    private static final String FORMAT_GEOJSON = "?format=geojson";
    public static final String QUERY_API_URL = BASE_API_URL + "query" + FORMAT_GEOJSON;
    public static final String COUNT_API_URL = BASE_API_URL + "count" + FORMAT_GEOJSON;
    public static final String DATE_FORMAT = "yyyy-mm-dd";
    public static final String MAGNITUDE_FORMAT = "0.0";
    public static final Integer API_LIMIT_RESULT_SIZE = 100;
}
