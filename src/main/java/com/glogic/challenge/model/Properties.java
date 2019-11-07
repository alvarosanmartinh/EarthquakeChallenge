package com.glogic.challenge.model;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * The type Properties for the API responses.
 */
@Data
public class Properties {
    private BigDecimal mag;
    private String place;
    private BigDecimal time;
    private BigDecimal updated;
    private BigDecimal tz;
    private String url;
    private String detail;
    private BigDecimal felt;
    private BigDecimal cdi;
    private BigDecimal mmi;
    private String alert;
    private String status;
    private BigDecimal tsunami;
    private BigDecimal sig;
    private String net;
    private String code;
    private String ids;
    private String sources;
    private String types;
    private BigDecimal nst;
    private BigDecimal dmin;
    private BigDecimal rms;
    private BigDecimal gap;
    private String magType;
    private String type;
    private String title;
}
