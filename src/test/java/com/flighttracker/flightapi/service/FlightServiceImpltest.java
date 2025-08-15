package com.flighttracker.flightapi.service;

import com.flighttracker.flightapi.model.Airport;
import com.flighttracker.flightapi.model.Flight;
import com.flighttracker.flightapi.repository.FlightRepository;
import com.flighttracker.flightapi.service.impl.FlightServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FlightServiceImpltest {
    private FlightRepository flightRepository;
    private FlightServiceImpl flightService;

    private Airport jfk;
    private Airport lax;
    private Flight departure1;
    private Flight arrival1;

    @BeforeEach
    void setUp() {
        // Manually create mock instance
        flightRepository = Mockito.mock(FlightRepository.class);

        // Manually inject mock into the service constructor
        flightService = new FlightServiceImpl(flightRepository);

        // Create sample data for tests
        jfk = new Airport();
        jfk.setId(1L);
        jfk.setCode("JFK");
        jfk.setName("John F. Kennedy International Airport");

        lax = new Airport();
        lax.setId(2L);
        lax.setCode("LAX");
        lax.setName("Los Angeles International Airport");

        departure1 = new Flight();
        departure1.setFlightNumber("UA123");
        departure1.setAirlineName("United Airlines");
        departure1.setOriginAirport(jfk);
        departure1.setDestinationAirport(lax);
        departure1.setDepartureTime(LocalDateTime.now());
        departure1.setArrivalTime(LocalDateTime.now().plusHours(5));
        departure1.setGateNumber("A5");
        departure1.setStatus("ON TIME");

        arrival1 = new Flight();
        arrival1.setFlightNumber("DL456");
        arrival1.setAirlineName("Delta Airlines");
        arrival1.setOriginAirport(lax);
        arrival1.setDestinationAirport(jfk);
        arrival1.setDepartureTime(LocalDateTime.now());
        arrival1.setArrivalTime(LocalDateTime.now().plusHours(5));
        arrival1.setGateNumber("B12");
        arrival1.setStatus("LANDED");
    }

    @Test
    void testGetDeparturesByAirport_Found() {
        // Arrange: Mock the repository to return a list with one departure
        when(flightRepository.findByOriginAirportId(1L)).thenReturn(List.of(departure1));

        // Act: Call the service method
        List<Flight> result = flightService.getDeparturesByAirport(1L);

        // Assert: Check that the result is correct
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("UA123", result.get(0).getFlightNumber());
        assertEquals("JFK", result.get(0).getOriginAirport().getCode());

        // Verify that the repository method was called exactly once
        verify(flightRepository, times(1)).findByOriginAirportId(1L);
    }

    @Test
    void testGetDeparturesByAirport_NotFound() {
        // Arrange: Mock the repository to return an empty list
        when(flightRepository.findByOriginAirportId(99L)).thenReturn(Collections.emptyList());

        // Act: Call the service method
        List<Flight> result = flightService.getDeparturesByAirport(99L);

        // Assert: Check that the result is an empty list
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify that the repository method was called
        verify(flightRepository, times(1)).findByOriginAirportId(99L);
    }

    @Test
    void testGetArrivalsByAirport_Found() {
        // Arrange: Mock the repository to return a list with one arrival
        when(flightRepository.findByDestinationAirportId(1L)).thenReturn(List.of(arrival1));

        // Act: Call the service method
        List<Flight> result = flightService.getArrivalsByAirport(1L);

        // Assert: Check that the result is correct
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("DL456", result.get(0).getFlightNumber());
        assertEquals("JFK", result.get(0).getDestinationAirport().getCode());

        // Verify that the repository method was called
        verify(flightRepository, times(1)).findByDestinationAirportId(1L);
    }
}
