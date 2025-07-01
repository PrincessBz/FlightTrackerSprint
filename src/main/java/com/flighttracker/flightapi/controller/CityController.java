package com.flighttracker.flightapi.controller;

import com.flighttracker.flightapi.model.Airport;
import com.flighttracker.flightapi.model.City;
import com.flighttracker.flightapi.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    @Autowired
    private CityService cityService; // Now injecting CityService

    // GET all cities
    @GetMapping
    public List<City> getAllCities() {
        return cityService.getAllCities();
    }

    // GET city by ID
    @GetMapping("/{id}")
    public ResponseEntity<City> getCityById(@PathVariable Long id) {
        Optional<City> city = cityService.getCityById(id);
        return city.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST create new city
    @PostMapping
    public ResponseEntity<City> createCity(@RequestBody City city) {
        City savedCity = cityService.createCity(city);
        return new ResponseEntity<>(savedCity, HttpStatus.CREATED);
    }

    // PUT update city by ID
    @PutMapping("/{id}")
    public ResponseEntity<City> updateCity(@PathVariable Long id, @RequestBody City cityDetails) {
        City updatedCity = cityService.updateCity(id, cityDetails);
        if (updatedCity != null) {
            return ResponseEntity.ok(updatedCity);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE city by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id); // Service handles existence check
        return ResponseEntity.noContent().build();
    }

    // Question 1: What airports are there in each city?
    // GET airports by city ID
    @GetMapping("/{cityId}/airports")
    public ResponseEntity<Set<Airport>> getAirportsByCity(@PathVariable Long cityId) {
        Set<Airport> airports = cityService.getAirportsByCity(cityId);
        if (airports != null) {
            return ResponseEntity.ok(airports);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
