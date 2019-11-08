package com.glogic.challenge

import com.fasterxml.jackson.databind.ObjectMapper
import com.glogic.challenge.controller.EarthquakeController
import com.glogic.challenge.model.FeatureCollection
import com.glogic.challenge.service.EarthquakeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultMatcher
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [EarthquakeController])
class EarthquakeControllerTest extends Specification{

    @Autowired
    protected MockMvc mvc

    @Autowired
    EarthquakeService earthquakeService

    @Autowired
    ObjectMapper objectMapper

    def "should pass dates and return FeatureCollection"() {
        given:
        earthquakeService.getEarthquakesBetweenDates('2019-01-01' as Date, '2019-01-02' as Date) >> new FeatureCollection()

        when:
        def results = mvc.perform(get('/getEarthquakesBetweenDates?startDate=2019-01-01&endDate=2019-01-02'))

        then:
        results.andExpect(status().isOk())

        and:
        results.andExpect(ResultMatcher.isInstance(FeatureCollection.class))
    }

    @TestConfiguration                                          // 6
    static class StubConfig {
        DetachedMockFactory detachedMockFactory = new DetachedMockFactory()

        @Bean
        EarthquakeService earthquakeService() {
            return detachedMockFactory.Stub(EarthquakeService)
        }
    }

}
