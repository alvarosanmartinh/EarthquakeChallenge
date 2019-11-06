package com.glogic.challenge.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Collection;

@Data
public class FeatureCollection {
    private String type;
    private Metadata metadata;
    private Collection<Feature> features;
    private Collection<BigDecimal> bbox;
}
