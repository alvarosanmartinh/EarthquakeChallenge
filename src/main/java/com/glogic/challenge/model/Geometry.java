package com.glogic.challenge.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Collection;

@Data
public class Geometry {
    private String type;
    private Collection<BigDecimal> coordinates;
}
