package com.flighttracker.flightapi.controller;

import com.flighttracker.flightapi.model.Flight;
import com.flighttracker.flightapi.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Adjust the origin as
public class FlightController {
    @Autowired
    private FlightService flightService;

    @GetMapping("/departures")
    public ResponseEntity<List<Flight>> getDepartures(@RequestParam Long airportId) {
        List<Flight> departures = flightService.getDeparturesByAirport(airportId);
        return ResponseEntity.ok(departures);
    }

    // Optional: Add an endpoint for arrivals if you have time
    @GetMapping("/arrivals")
    public ResponseEntity<List<Flight>> getArrivals(@RequestParam Long airportId) {
        List<Flight> arrivals = flightService.getArrivalsByAirport(airportId);
        return ResponseEntity.ok(arrivals);
    }
}
