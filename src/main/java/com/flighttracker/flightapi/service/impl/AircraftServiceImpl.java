package com.flighttracker.flightapi.service.impl;

import com.flighttracker.flightapi.model.Aircraft;
import com.flighttracker.flightapi.model.Airport;
import com.flighttracker.flightapi.model.Passenger;
import com.flighttracker.flightapi.repository.AircraftRepository;
import com.flighttracker.flightapi.repository.AirportRepository;
import com.flighttracker.flightapi.repository.PassengerRepository;
import com.flighttracker.flightapi.service.AircraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AircraftServiceImpl implements AircraftService {


    private AircraftRepository aircraftRepository;
    private AirportRepository airportRepository;
    private PassengerRepository passengerRepository;

    public AircraftServiceImpl(
            AircraftRepository aircraftRepository,
            AirportRepository airportRepository,
            PassengerRepository passengerRepository) {
        this.aircraftRepository = aircraftRepository;
        this.airportRepository = airportRepository;
        this.passengerRepository = passengerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Aircraft> getAllAircraft() {
        return aircraftRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Aircraft> getAircraftById(Long id) {
        return aircraftRepository.findById(id);
    }

    @Override
    @Transactional
    public Aircraft createAircraft(Aircraft aircraft) {
        return aircraftRepository.save(aircraft);
    }

    @Override
    @Transactional
    public Aircraft updateAircraft(Long id, Aircraft aircraftDetails) {
        Optional<Aircraft> aircraft = aircraftRepository.findById(id);
        if (aircraft.isPresent()) {
            Aircraft existingAircraft = aircraft.get();
            existingAircraft.setType(aircraftDetails.getType());
            existingAircraft.setAirlineName(aircraftDetails.getAirlineName());
            existingAircraft.setNumberOfPassengers(aircraftDetails.getNumberOfPassengers());
            return aircraftRepository.save(existingAircraft);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteAircraft(Long id) {
        if (aircraftRepository.existsById(id)) {
            aircraftRepository.deleteById(id);
        } else {
            System.out.println("Aircraft with ID " + id + " not found for deletion.");
        }
    }

    @Override
    @Transactional
    public Aircraft addAirportToAircraft(Long aircraftId, Long airportId) {
        Optional<Aircraft> aircraftOptional = aircraftRepository.findById(aircraftId);
        Optional<Airport> airportOptional = airportRepository.findById(airportId);

        if (aircraftOptional.isPresent() && airportOptional.isPresent()) {
            Aircraft aircraft = aircraftOptional.get();
            Airport airport = airportOptional.get();

            aircraft.getAirports().add(airport);
            airport.getAircraft().add(aircraft);

            aircraftRepository.save(aircraft);
            airportRepository.save(airport); // Important: persist airport changes too

            return aircraft;
        } else {
            throw new RuntimeException("Aircraft or Airport not found.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Airport> getAirportsUsedByAircraft(Long aircraftId) {
        Optional<Aircraft> aircraft = aircraftRepository.findById(aircraftId);

        return aircraft.map(Aircraft::getAirports).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Passenger> getPassengersOnAircraft(Long aircraftId) {
        Optional<Aircraft> aircraft = aircraftRepository.findById(aircraftId);
        return aircraft.map(Aircraft::getPassengers).orElse(null);
    }

}
