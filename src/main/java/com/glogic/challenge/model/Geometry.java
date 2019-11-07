package com.glogic.challenge.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * The type Geometry for the API responses.
 */
@Data
public class Geometry {
    private String type;
    private Collection<BigDecimal> coordinates;
}
