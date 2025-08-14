package com.flighttracker.flightapi.service;

import com.flighttracker.flightapi.model.Flight;

import java.util.List;

public interface FlightService {
    List<Flight> getDeparturesByAirport(Long airportId);
    List<Flight> getArrivalsByAirport(Long airportId);
}
