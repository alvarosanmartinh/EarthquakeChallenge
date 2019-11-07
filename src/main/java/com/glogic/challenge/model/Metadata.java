package com.glogic.challenge.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * The type Metadata for the API responses.
 */
@Data
public class Metadata {
    private BigDecimal generated;
    private String url;
    private String title;
    private BigDecimal status;
    private String api;
    private BigDecimal count;
}
