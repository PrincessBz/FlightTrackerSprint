package com.flighttracker.flightapi.controller;

import com.flighttracker.flightapi.model.Aircraft;
import com.flighttracker.flightapi.model.Airport;
import com.flighttracker.flightapi.model.Passenger;
import com.flighttracker.flightapi.service.AircraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/aircrafts")
public class AircraftController {
    @Autowired
    private AircraftService aircraftService;

    // GET all aircraft
    @GetMapping
    public List<Aircraft> getAllAircraft() {
        return aircraftService.getAllAircraft();
    }

    // GET aircraft by ID
    @GetMapping("/{id}")
    public ResponseEntity<Aircraft> getAircraftById(@PathVariable Long id) {
        Optional<Aircraft> aircraft = aircraftService.getAircraftById(id);
        return aircraft.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST create new aircraft
    @PostMapping
    public ResponseEntity<Aircraft> createAircraft(@RequestBody Aircraft aircraft) {
        Aircraft savedAircraft = aircraftService.createAircraft(aircraft);
        return new ResponseEntity<>(savedAircraft, HttpStatus.CREATED);
    }

    // PUT update aircraft by ID
    @PutMapping("/{id}")
    public ResponseEntity<Aircraft> updateAircraft(@PathVariable Long id, @RequestBody Aircraft aircraftDetails) {
        Aircraft updatedAircraft = aircraftService.updateAircraft(id, aircraftDetails);
        if (updatedAircraft != null) {
            return ResponseEntity.ok(updatedAircraft);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE aircraft by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAircraft(@PathVariable Long id) {
        aircraftService.deleteAircraft(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint to associate aircraft with airport (takeoff/landing)
    @PutMapping("/{aircraftId}/addAirport/{airportId}")
    public ResponseEntity<Aircraft> addAirportToAircraft(@PathVariable Long aircraftId, @PathVariable Long airportId) {
        try {
            Aircraft updatedAircraft = aircraftService.addAirportToAircraft(aircraftId, airportId);
            return ResponseEntity.ok(updatedAircraft);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Question 3: What airports do aircraft take off from and land at?
    @GetMapping("/{aircraftId}/airports")
    public ResponseEntity<Set<Airport>> getAirportsUsedByAircraft(@PathVariable Long aircraftId) {
        Set<Airport> airports = aircraftService.getAirportsUsedByAircraft(aircraftId);
        if (airports != null) {
            return ResponseEntity.ok(airports);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // GET passengers on a specific aircraft
    @GetMapping("/{aircraftId}/passengers")
    public ResponseEntity<Set<Passenger>> getPassengersOnAircraft(@PathVariable Long aircraftId) {
        Set<Passenger> passengers = aircraftService.getPassengersOnAircraft(aircraftId);
        if (passengers != null) {
            return ResponseEntity.ok(passengers);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
