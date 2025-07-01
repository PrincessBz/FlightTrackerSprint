package com.flighttracker.flightapi.service.impl;

import com.flighttracker.flightapi.model.Airport;
import com.flighttracker.flightapi.model.City;
import com.flighttracker.flightapi.repository.AirportRepository;
import com.flighttracker.flightapi.repository.CityRepository;
import com.flighttracker.flightapi.service.AirportService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AirportServiceImpl implements AirportService {

    private AirportRepository airportRepository;
    private CityRepository cityRepository;

    public  AirportServiceImpl(AirportRepository airportRepository, CityRepository cityRepository) {
        this.airportRepository = airportRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Airport> getAllAirports() {
        return airportRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Airport> getAirportById(Long id) {
        return airportRepository.findById(id);
    }

    @Override
    @Transactional
    public Airport createAirport(Airport airport, Long cityId) {
        Optional<City> cityOptional = cityRepository.findById(cityId);
        if (cityOptional.isPresent()) {
            airport.setCity(cityOptional.get());
            return airportRepository.save(airport);
        } else {
            // Throw custom exception or handle error (e.g., CityNotFoundException)
            throw new RuntimeException("City with ID " + cityId + " not found.");
        }
    }

    @Override
    @Transactional
    public Airport updateAirport(Long id, Airport airportDetails, Long cityId) {
        Optional<Airport> airport = airportRepository.findById(id);
        if (airport.isPresent()) {
            Airport existingAirport = airport.get();
            existingAirport.setName(airportDetails.getName());
            existingAirport.setCode(airportDetails.getCode());

            if (cityId != null) {
                Optional<City> cityOptional = cityRepository.findById(cityId);
                cityOptional.ifPresent(existingAirport::setCity);
            }

            return airportRepository.save(existingAirport);
        } else {
            return null; // Or throw NotFoundException
        }
    }

    @Override
    @Transactional
    public void deleteAirport(Long id) {
        if (airportRepository.existsById(id)) {
            airportRepository.deleteById(id);
        } else {
            System.out.println("Airport with ID " + id + " not found for deletion.");
        }
    }
}
