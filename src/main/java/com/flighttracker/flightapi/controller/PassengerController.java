package com.flighttracker.flightapi.controller;

import com.flighttracker.flightapi.model.Aircraft;
import com.flighttracker.flightapi.model.Airport;
import com.flighttracker.flightapi.model.Passenger;
import com.flighttracker.flightapi.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/passengers")
@CrossOrigin(origins ="*" ) // Adjust the origin as needed
public class PassengerController {
    @Autowired
    private PassengerService passengerService;

    // GET all passengers
    @GetMapping
    public List<Passenger> getAllPassengers() {
        return passengerService.getAllPassengers();
    }

    // GET passenger by ID
    @GetMapping("/{id}")
    public ResponseEntity<Passenger> getPassengerById(@PathVariable Long id) {
        Optional<Passenger> passenger = passengerService.getPassengerById(id);
        return passenger.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST create new passenger
    @PostMapping
    public ResponseEntity<Passenger> createPassenger(@RequestBody Passenger passenger, @RequestParam Long cityId) {
        try {
            Passenger savedPassenger = passengerService.createPassenger(passenger, cityId);
            return new ResponseEntity<>(savedPassenger, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // PUT update passenger by ID
    @PutMapping("/{id}")
    public ResponseEntity<Passenger> updatePassenger(@PathVariable Long id, @RequestBody Passenger passengerDetails, @RequestParam(required = false) Long cityId) {
        Passenger updatedPassenger = passengerService.updatePassenger(id, passengerDetails, cityId);
        if (updatedPassenger != null) {
            return ResponseEntity.ok(updatedPassenger);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE passenger by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        passengerService.deletePassenger(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint to associate passenger with aircraft
    @PutMapping("/{passengerId}/addAircraft/{aircraftId}")
    public ResponseEntity<Passenger> addAircraftToPassenger(@PathVariable Long passengerId, @PathVariable Long aircraftId) {
        try {
            Passenger updatedPassenger = passengerService.addAircraftToPassenger(passengerId, aircraftId);
            return ResponseEntity.ok(updatedPassenger);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Question 2: What aircraft has each passenger flown on?
    @GetMapping("/{passengerId}/aircrafts")
    public ResponseEntity<Set<Aircraft>> getAircraftsFlownByPassenger(@PathVariable Long passengerId) {
        Set<Aircraft> aircrafts = passengerService.getAircraftsFlownByPassenger(passengerId);
        if (aircrafts != null) {
            return ResponseEntity.ok(aircrafts);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Question 4: What airports have passengers used?
    @GetMapping("/{passengerId}/airportsUsed")
    public ResponseEntity<Set<Airport>> getAirportsUsedByPassenger(@PathVariable Long passengerId) {
        Set<Airport> airportsUsed = passengerService.getAirportsUsedByPassenger(passengerId);
        if (airportsUsed != null) {
            return ResponseEntity.ok(airportsUsed);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
