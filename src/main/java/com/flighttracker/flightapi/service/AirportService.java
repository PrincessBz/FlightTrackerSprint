package com.flighttracker.flightapi.service;

import com.flighttracker.flightapi.model.Airport;

import java.util.List;
import java.util.Optional;

public interface AirportService {
    List<Airport> getAllAirports();
    Optional<Airport> getAirportById(Long id);
    Airport createAirport(Airport airport, Long cityId);
    Airport updateAirport(Long id, Airport airportDetails, Long cityId);
    void deleteAirport(Long id);
}

