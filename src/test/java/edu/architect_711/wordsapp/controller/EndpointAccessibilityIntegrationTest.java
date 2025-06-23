package edu.architect_711.wordsapp.controller;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class EndpointAccessibilityIntegrationTest {
    private static final Logger log = LoggerFactory.getLogger(EndpointAccessibilityIntegrationTest.class);

    @Autowired
    private MockMvc mockMvc;

    public record Entrypoint(HttpMethod httpMethod, String endpoint, boolean isSecure) {}

    public final List<Entrypoint> entrypoints = new ArrayList<>();

    private void putEntrypoint(HttpMethod httpMethod, String endpoint, boolean isSecure) {
        entrypoints.add(new Entrypoint(httpMethod, endpoint, isSecure));
    }

    @BeforeAll
    public void setup() {
        putEntrypoint(GET,  "/api/languages", false);

        putEntrypoint(POST, "/api/login64", true);
        putEntrypoint(POST, "/api/accounts", false);
        putEntrypoint(GET,  "/api/accounts", true);
        putEntrypoint(PUT,  "/api/accounts", true);
        putEntrypoint(DELETE,   "/api/accounts", true);

        putEntrypoint(GET,  "/api/groups", true);
        putEntrypoint(POST, "/api/groups", true);
    }

    @Test
    public void accessibility_test() throws Exception {
        for (Entrypoint entrypoint : entrypoints)
            makeRequest(entrypoint);
    }

    private void makeRequest(Entrypoint entrypoint) throws Exception {
        MockHttpServletRequestBuilder requestMatcher = determineMethod(entrypoint);

        mockMvc
                .perform(requestMatcher)
                .andExpect(status().is( matcher(entrypoint) ));
    }

    private Matcher<Integer> matcher(Entrypoint entrypoint) {
        return new BaseMatcher<>() {
            @Override
            public void describeTo(Description description) {}

            @Override
            public boolean matches(Object actual) {
                if (!(actual instanceof Integer)) return false;

                if (entrypoint.isSecure) {
                    log.debug("Secured endpoint {} with method {} status must be 401 or 403", entrypoint.endpoint, entrypoint.httpMethod);
                    return actual.equals(HttpStatus.UNAUTHORIZED.value()) || actual.equals(HttpStatus.FORBIDDEN.value());
                }
                log.debug("Endpoint {} with method {} isn't secure", entrypoint.endpoint, entrypoint.httpMethod);
                return true;
            }
        };
    }

    private MockHttpServletRequestBuilder determineMethod(Entrypoint entrypoint) {
        var httpMethod = entrypoint.httpMethod;

        if      (httpMethod == GET)     return get(entrypoint.endpoint);
        else if (httpMethod == POST)    return post(entrypoint.endpoint);
        else if (httpMethod == PUT)     return put(entrypoint.endpoint);
        else if (httpMethod == DELETE)  return delete(entrypoint.endpoint);

        else throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod);
    }

}
