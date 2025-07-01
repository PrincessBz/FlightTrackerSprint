package com.flighttracker.flightapi.service;

import com.flighttracker.flightapi.model.Aircraft;
import com.flighttracker.flightapi.model.Airport;
import com.flighttracker.flightapi.model.Passenger;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PassengerService {
    List<Passenger> getAllPassengers();
    Optional<Passenger> getPassengerById(Long id);
    Passenger createPassenger(Passenger passenger, Long cityId);
    Passenger updatePassenger(Long id, Passenger passengerDetails, Long cityId);
    void deletePassenger(Long id);
    Passenger addAircraftToPassenger(Long passengerId, Long aircraftId);
    Set<Aircraft> getAircraftsFlownByPassenger(Long passengerId); // Question 2
    Set<Airport> getAirportsUsedByPassenger(Long passengerId); // Question 4

}
