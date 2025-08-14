package com.flighttracker.flightapi.repository;

import com.flighttracker.flightapi.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByOriginAirportId(Long airportId);
    List<Flight> findByDestinationAirportId(Long airportId);
}
