package com.flighttracker.flightapi.repository;

import com.flighttracker.flightapi.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassengerRepository  extends JpaRepository<Passenger, Long> {
    @Query("SELECT pa FROM Passenger p JOIN p.aircrafts pa WHERE p.id = :passengerId")
    List<Object[]> findAllByPassengerId(Long passengerId);
}
