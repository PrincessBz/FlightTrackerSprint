package com.flighttracker.flightapi.service.impl;

import com.flighttracker.flightapi.model.Airport;
import com.flighttracker.flightapi.model.City;
import com.flighttracker.flightapi.repository.CityRepository;
import com.flighttracker.flightapi.service.CityService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CityServiceImpl implements CityService {

    private CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<City> getCityById(Long id) {
        return cityRepository.findById(id);
    }

    @Override
    @Transactional
    public City createCity(City city) {
        return cityRepository.save(city);
    }

    @Override
    @Transactional
    public City updateCity(Long id, City cityDetails) {
        Optional<City> city = cityRepository.findById(id);
        if (city.isPresent()) {
            City existingCity = city.get();
            existingCity.setName(cityDetails.getName());
            existingCity.setState(cityDetails.getState());
            existingCity.setPopulation(cityDetails.getPopulation());
            return cityRepository.save(existingCity);
        } else {
            System.out.println("City with ID " + id + " not found for update.");
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteCity(Long id) {
        if (cityRepository.existsById(id)) {
            cityRepository.deleteById(id);
        } else {

            System.out.println("City with ID " + id + " not found for deletion.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Airport> getAirportsByCity(Long cityId) {
        Optional<City> city = cityRepository.findById(cityId);
        return city.map(City::getAirports).orElse(null);
    }


}
