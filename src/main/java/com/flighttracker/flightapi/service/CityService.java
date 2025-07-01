package com.flighttracker.flightapi.service;

import com.flighttracker.flightapi.model.Airport;
import com.flighttracker.flightapi.model.City;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CityService {
    List<City> getAllCities();
    Optional<City> getCityById(Long cityId);
    City createCity(City city);
    City updateCity(Long id, City cityDetails);
    void deleteCity(Long id);
    Set<Airport> getAirportsByCity(Long CityId);
}
