package com.flighttracker.flightapi.controller;

import com.flighttracker.flightapi.model.Airport;
import com.flighttracker.flightapi.service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/airports")
@CrossOrigin(origins = "*") // Adjust the origin as needed
public class AirportController {
    @Autowired
    private AirportService airportService; // Now injecting AirportService

    // GET all airports
    @GetMapping
    public List<Airport> getAllAirports() {
        return airportService.getAllAirports();
    }

    // GET airport by ID
    @GetMapping("/{id}")
    public ResponseEntity<Airport> getAirportById(@PathVariable Long id) {
        Optional<Airport> airport = airportService.getAirportById(id);
        return airport.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST create new airport
    @PostMapping
    public ResponseEntity<Airport> createAirport(@RequestBody Airport airport, @RequestParam Long cityId) {
        try {
            Airport savedAirport = airportService.createAirport(airport, cityId);
            return new ResponseEntity<>(savedAirport, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // PUT update airport by ID
    @PutMapping("/{id}")
    public ResponseEntity<Airport> updateAirport(@PathVariable Long id, @RequestBody Airport airportDetails, @RequestParam(required = false) Long cityId) {
        Airport updatedAirport = airportService.updateAirport(id, airportDetails, cityId);
        if (updatedAirport != null) {
            return ResponseEntity.ok(updatedAirport);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE airport by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirport(@PathVariable Long id) {
        airportService.deleteAirport(id);
        return ResponseEntity.noContent().build();
    }
}
