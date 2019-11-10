package com.glogic.challenge

import com.glogic.challenge.controller.EarthquakeController
import com.glogic.challenge.service.EarthquakeService
import com.glogic.challenge.util.Constants
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import java.text.SimpleDateFormat

import static org.springframework.http.HttpStatus.OK
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class EarthquakeControllerTest extends Specification{

    def earthquakeService = Mock(EarthquakeService)
    def earthquakeController = new EarthquakeController(earthquakeService)

    MockMvc mockMvc = standaloneSetup(earthquakeController).build()

    def "getEarthquakesBetweenDates test hits the URL with correctly formated values and return HTTP status 200"() {
        def dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT)
        when: 'rest url is hit'
        def response = mockMvc.perform(get('/getEarthquakesBetweenDates')
                .param("startDate", dateFormat.format(new Date()))
                .param("endDate", dateFormat.format(new Date())))
                .andReturn().response

        then: 'earthquakeService correctly returns status 200'
        response.status == OK.value()
    }


    def "getEarthquakesBetweenMagnitudes test hits the URL with correctly formated values and return HTTP status 200"() {
        when: 'rest url is hit'
        def response = mockMvc.perform(get('/getEarthquakesBetweenMagnitudes')
                .param("minMagnitude", "6.0")
                .param("maxMagnitude", "6.0"))
                .andReturn().response

        then: 'earthquakeService correctly returns status 200'
        response.status == OK.value()
    }

    def "getEarthquakesByCountriesBetweenDates test hits the URL with correctly formated values and return HTTP status 200"() {
        def dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT)
        when: 'rest url is hit'
        def response = mockMvc.perform(get('/getEarthquakesByCountriesBetweenDates')
                .param("countryCode", "cl")
                .param("anotherCountryCode", "us")
                .param("startDate", dateFormat.format(new Date()))
                .param("endDate", dateFormat.format(new Date())))
                .andReturn().response

        then: 'earthquakeService correctly returns status 200'
        response.status == OK.value()
    }

    def "getEarthquakesByCountry test hits the URL with correctly formated values and return HTTP status 200"() {
        def dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT)
        when: 'rest url is hit'
        def response = mockMvc.perform(get('/getEarthquakesByCountry')
                .param("countryCode", "cl"))
                .andReturn().response

        then: 'earthquakeService correctly returns status 200'
        response.status == OK.value()
    }


    def "getEarthquakesBetweenTwoRangesOfDates test hits the URL with correctly formated values and return HTTP status 200"() {
        def dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT)
        when: 'rest url is hit'
        def response = mockMvc.perform(get('/getEarthquakesBetweenTwoRangesOfDates')
                .param("firstStartDate", dateFormat.format(new Date()))
                .param("firstEndDate", dateFormat.format(new Date()))
                .param("secondStartDate", dateFormat.format(new Date()))
                .param("secondEndDate", dateFormat.format(new Date())))
                .andReturn().response

        then: 'earthquakeService correctly returns status 200'
        response.status == OK.value()
    }
}
