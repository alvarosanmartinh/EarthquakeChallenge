package com.glogic.challenge

import com.glogic.challenge.controller.EarthquakeController
import com.glogic.challenge.service.EarthquakeService
import com.glogic.challenge.service.impl.EarthquakeServiceImpl
import com.glogic.challenge.util.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.client.RestTemplate
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import java.text.DecimalFormat
import java.text.SimpleDateFormat

import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class EarthquakeServiceTest extends Specification {

    @Autowired
    EarthquakeService earthquakeService = new EarthquakeServiceImpl(new RestTemplateBuilder())

    def "getEarthquakesBetweenDates returns ResponseEntity with FeatureCollection inside"() {
        when: 'method is hit'
        def response = earthquakeService.getEarthquakesBetweenDates(new Date(), new Date())

        then: 'earthquakeService returns ResponseEntity with FeatureCollection inside'
        response.hasBody()
    }

    def "getEarthquakesBetweenDates is called with null dates and returns Bad Request status"() {
        when: 'method is hit'
        def response = earthquakeService.getEarthquakesBetweenDates(null, null)

        then: 'earthquakeService returns ResponseEntity with Bad Request status'
        response.statusCode == BAD_REQUEST
    }

    def "getEarthquakesBetweenMagnitudes returns ResponseEntity with FeatureCollection inside"() {
        when: 'method is hit'
        def response = earthquakeService.getEarthquakesBetweenMagnitudes(
                new BigDecimal(6.1),
                new BigDecimal(6.2))

        then: 'earthquakeService returns ResponseEntity with FeatureCollection inside'
        response.hasBody()
    }

    def "getEarthquakesBetweenMagnitudes is called with null magnitudes and returns Bad Request status"() {
        when: 'method is hit'
        def response = earthquakeService.getEarthquakesBetweenMagnitudes(null, null)

        then: 'earthquakeService returns ResponseEntity with Bad Request status'
        response.statusCode == BAD_REQUEST
    }

    def "getEarthquakesByCountriesBetweenDates returns ResponseEntity with FeatureCollection inside"() {
        when: 'method is hit'
        def response = earthquakeService.getEarthquakesByCountriesBetweenDates(
                "cl",
                "us",
                new Date(),
                new Date())

        then: 'earthquakeService returns ResponseEntity with FeatureCollection inside'
        response.hasBody()
    }

    def "getEarthquakesByCountriesBetweenDates is called with wrong country codes and null dates and returns Bad Request status"() {
        when: 'method is hit'
        def response = earthquakeService.getEarthquakesByCountriesBetweenDates(
                "hello",
                "hello",
                null,
                null)

        then: 'earthquakeService returns ResponseEntity with Bad Request status'
        response.statusCode == BAD_REQUEST
    }

    def "getEarthquakesByCountry returns ResponseEntity with FeatureCollection inside"() {
        when: 'method is hit'
        def response = earthquakeService.getEarthquakesByCountry("cl")

        then: 'earthquakeService returns ResponseEntity with FeatureCollection inside'
        response.hasBody()
    }

    def "getEarthquakesByCountry with a wrong country code as parameter returns ResponseEntity with FeatureCollection inside"() {
        when: 'method is hit'
        def response = earthquakeService.getEarthquakesByCountry("hello")

        then: 'earthquakeService returns ResponseEntity with FeatureCollection inside'
        response.hasBody()
    }

    def "getEarthquakesByCountry with a null country code as parameter returns ResponseEntity with FeatureCollection inside"() {
        when: 'method is hit'
        def response = earthquakeService.getEarthquakesByCountry(null)

        then: 'earthquakeService returns ResponseEntity with FeatureCollection inside'
        response.hasBody()
    }

    def "getEarthquakesBetweenTwoRangesOfDates returns ResponseEntity with FeatureCollection inside"() {
        when: 'method is hit'
        def response = earthquakeService.getEarthquakesBetweenTwoRangesOfDates(new Date(), new Date(), new Date(), new Date())

        then: 'earthquakeService returns ResponseEntity with FeatureCollection inside'
        response.hasBody()
    }

    def "getEarthquakesBetweenTwoRangesOfDates with null parameters returns bad request status"() {
        when: 'method is hit'
        def response = earthquakeService.getEarthquakesBetweenTwoRangesOfDates(
                null,
                null,
                null,
                null)

        then: 'earthquakeService returns bad request'
        response.hasBody()
    }

    def "getEarthquakesBetweenTwoRangesOfDates with null parameters at second range of dates returns bad request status"() {
        when: 'method is hit'
        def response = earthquakeService.getEarthquakesBetweenTwoRangesOfDates(
                new Date(),
                new Date(),
                null,
                null)

        then: 'earthquakeService returns bad request'
        response.hasBody()
    }
}
