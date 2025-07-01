package com.flighttracker.flightapi.service;

import com.flighttracker.flightapi.model.Aircraft;
import com.flighttracker.flightapi.model.Airport;
import com.flighttracker.flightapi.model.Passenger;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AircraftService {
    List<Aircraft> getAllAircraft();
    Optional<Aircraft> getAircraftById(Long id);
    Aircraft createAircraft(Aircraft aircraft);
    Aircraft updateAircraft(Long id, Aircraft aircraftDetails);
    void deleteAircraft(Long id);
    Aircraft addAirportToAircraft(Long aircraftId, Long airportId);
    Set<Airport> getAirportsUsedByAircraft(Long aircraftId); // Question 3
    Set<Passenger> getPassengersOnAircraft(Long aircraftId);
}
