package com.glogic.challenge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * The type Feature count for the API responses.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeatureCount {
    private BigDecimal count;
    private BigDecimal maxAllowed;
}
