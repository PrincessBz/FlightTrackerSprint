package com.flighttracker.flightapi.service.impl;

import com.flighttracker.flightapi.model.Aircraft;
import com.flighttracker.flightapi.model.Airport;
import com.flighttracker.flightapi.model.City;
import com.flighttracker.flightapi.model.Passenger;
import com.flighttracker.flightapi.repository.AircraftRepository;
import com.flighttracker.flightapi.repository.CityRepository;
import com.flighttracker.flightapi.repository.PassengerRepository;
import com.flighttracker.flightapi.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PassengerServiceImpl implements PassengerService {

    private PassengerRepository passengerRepository;
    private CityRepository cityRepository;
    private AircraftRepository aircraftRepository;

    public PassengerServiceImpl(
            PassengerRepository passengerRepository,
            CityRepository cityRepository,
            AircraftRepository aircraftRepository) {
        this.passengerRepository = passengerRepository;
        this.cityRepository = cityRepository;
        this.aircraftRepository = aircraftRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Passenger> getPassengerById(Long id) {
        return passengerRepository.findById(id);
    }

    @Override
    @Transactional
    public Passenger createPassenger(Passenger passenger, Long cityId) {
        Optional<City> cityOptional = cityRepository.findById(cityId);
        if (cityOptional.isPresent()) {
            passenger.setCity(cityOptional.get());
            return passengerRepository.save(passenger);
        } else {
            throw new RuntimeException("City with ID " + cityId + " not found.");
        }
    }

    @Override
    @Transactional
    public Passenger updatePassenger(Long id, Passenger passengerDetails, Long cityId) {
        Optional<Passenger> passenger = passengerRepository.findById(id);
        if (passenger.isPresent()) {
            Passenger existingPassenger = passenger.get();
            existingPassenger.setFirstName(passengerDetails.getFirstName());
            existingPassenger.setLastName(passengerDetails.getLastName());
            existingPassenger.setPhoneNumber(passengerDetails.getPhoneNumber());

            if (cityId != null) {
                Optional<City> cityOptional = cityRepository.findById(cityId);
                cityOptional.ifPresent(existingPassenger::setCity);
            }

            return passengerRepository.save(existingPassenger);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public void deletePassenger(Long id) {
        if (passengerRepository.existsById(id)) {
            passengerRepository.deleteById(id);
        } else {
            System.out.println("Passenger with ID " + id + " not found for deletion.");
        }
    }

    @Override
    @Transactional
    public Passenger addAircraftToPassenger(Long passengerId, Long aircraftId) {
        Optional<Passenger> passengerOptional = passengerRepository.findById(passengerId);
        Optional<Aircraft> aircraftOptional = aircraftRepository.findById(aircraftId);

        if (passengerOptional.isPresent() && aircraftOptional.isPresent()) {
            Passenger passenger = passengerOptional.get();
            Aircraft aircraft = aircraftOptional.get();


            passenger.getAircrafts().add(aircraft);
            aircraft.getPassengers().add(passenger);


            passengerRepository.save(passenger);
            aircraftRepository.save(aircraft);

            return passenger;
        } else {
            throw new RuntimeException("Passenger or Aircraft not found.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Aircraft> getAircraftsFlownByPassenger(Long passengerId) {
        Optional<Passenger> passenger = passengerRepository.findById(passengerId);
        return passenger.map(Passenger::getAircrafts).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Airport> getAirportsUsedByPassenger(Long passengerId) {
        Optional<Passenger> passengerOptional = passengerRepository.findById(passengerId);
        if (passengerOptional.isPresent()) {
            Passenger passenger = passengerOptional.get();
            return passenger.getAircrafts().stream()
                    .flatMap(aircraft -> aircraft.getAirports().stream())
                    .collect(Collectors.toSet());
        } else {
            return null; // Or throw NotFoundException
        }
    }
}
