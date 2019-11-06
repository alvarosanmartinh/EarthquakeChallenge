package com.glogic.challenge.util;

public class Constants {
    private static final String baseApiUrl = "https://earthquake.usgs.gov/fdsnws/event/1/";
    private static final String formatGeoJson = "?format=geojson";
    public static final String queryApiUrl = baseApiUrl + "query" + formatGeoJson;
    public static final String countApiUrl = baseApiUrl + "count" + formatGeoJson;
    public static final String dateFormat = "yyyy-mm-dd";
    public static final String magnitudeFormat = "0.0";
}
