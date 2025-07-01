package com.flighttracker.flightapi.repository;

import com.flighttracker.flightapi.model.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AircraftRepository  extends JpaRepository<Aircraft, Long> {
    // Additional query methods can be defined here if needed
    @Query("SELECT ap FROM Aircraft a JOIN a.airports ap WHERE a.id = :aircraftId")
    List<Object[]>findAirportsByAircraftId(Long aircraftId);
}
