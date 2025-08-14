package com.flighttracker.flightapi.service.impl;

import com.flighttracker.flightapi.model.Flight;
import com.flighttracker.flightapi.repository.FlightRepository;
import com.flighttracker.flightapi.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;

    @Autowired
    public FlightServiceImpl(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public List<Flight> getDeparturesByAirport(Long airportId) {
        return flightRepository.findByOriginAirportId(airportId);
    }

    @Override
    public List<Flight> getArrivalsByAirport(Long airportId) {
        return flightRepository.findByDestinationAirportId(airportId);
    }
}
