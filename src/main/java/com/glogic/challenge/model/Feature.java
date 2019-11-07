package com.glogic.challenge.model;

import lombok.Data;

/**
 * The type Feature for the API responses.
 */
@Data
public class Feature {
    private String type;
    private Properties properties;
    private Geometry geometry;
    private String id;
}
