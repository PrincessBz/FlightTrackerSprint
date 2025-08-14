package com.flighttracker.flightapi.service;

import com.flighttracker.flightapi.model.Aircraft;
import com.flighttracker.flightapi.model.Airport;
import com.flighttracker.flightapi.model.Passenger;
import com.flighttracker.flightapi.repository.AircraftRepository;
import com.flighttracker.flightapi.repository.AirportRepository;
import com.flighttracker.flightapi.repository.PassengerRepository;
import com.flighttracker.flightapi.service.impl.AircraftServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AircraftServiceImplTest {

    private AircraftRepository aircraftRepository;
    private AirportRepository airportRepository;
    private PassengerRepository passengerRepository;
    private AircraftServiceImpl aircraftService;

    private Aircraft aircraft1;
    private Aircraft aircraft2;
    private Airport airport1;
    private Airport airport2;
    private Passenger passenger1;
    private Passenger passenger2;

    @BeforeEach
    void setUp() {
        aircraftRepository = Mockito.mock(AircraftRepository.class);
        airportRepository = Mockito.mock(AirportRepository.class);
        passengerRepository = Mockito.mock(PassengerRepository.class);
        aircraftService = new AircraftServiceImpl(aircraftRepository, airportRepository, passengerRepository);

        aircraft1 = new Aircraft();
        aircraft1.setId(1L);
        aircraft1.setType("Boeing 747");
        aircraft1.setAirlineName("United");
        aircraft1.setNumberOfPassengers(400);

        aircraft2 = new Aircraft();
        aircraft2.setId(2L);
        aircraft2.setType("Airbus A320");
        aircraft2.setAirlineName("Delta");
        aircraft2.setNumberOfPassengers(150);

        airport1 = new Airport();
        airport1.setId(100L);
        airport1.setCode("JFK");
        airport1.setName("JFK Airport");

        airport2 = new Airport();
        airport2.setId(101L);
        airport2.setCode("LAX");
        airport2.setName("LAX Airport");

        passenger1 = new Passenger();
        passenger1.setId(1000L);
        passenger1.setFirstName("Alice");
        passenger1.setLastName("Smith");

        passenger2 = new Passenger();
        passenger2.setId(1001L);
        passenger2.setFirstName("Bob");
        passenger2.setLastName("Johnson");

        aircraft1.getAirports().add(airport1);
        aircraft1.getAirports().add(airport2);
        aircraft1.getPassengers().add(passenger1);
        aircraft1.getPassengers().add(passenger2);
    }

    @Test
    void testGetAllAircraft() {
        when(aircraftRepository.findAll()).thenReturn(Arrays.asList(aircraft1, aircraft2));
        List<Aircraft> aircrafts = aircraftService.getAllAircraft();
        assertNotNull(aircrafts);
        assertEquals(2, aircrafts.size());
        verify(aircraftRepository, times(1)).findAll();
    }

    @Test
    void testGetAircraftById_Found() {
        when(aircraftRepository.findById(1L)).thenReturn(Optional.of(aircraft1));
        Optional<Aircraft> foundAircraft = aircraftService.getAircraftById(1L);
        assertTrue(foundAircraft.isPresent());
        assertEquals("Boeing 747", foundAircraft.get().getType());
        verify(aircraftRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAircraftById_NotFound() {
        when(aircraftRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Aircraft> foundAircraft = aircraftService.getAircraftById(99L);
        assertFalse(foundAircraft.isPresent());
        verify(aircraftRepository, times(1)).findById(99L);
    }

    @Test
    void testCreateAircraft() {
        Aircraft newAircraft = new Aircraft();
        newAircraft.setType("Cessna 172");
        newAircraft.setAirlineName("Private");
        newAircraft.setNumberOfPassengers(3);

        when(aircraftRepository.save(any(Aircraft.class))).thenReturn(newAircraft);
        Aircraft createdAircraft = aircraftService.createAircraft(newAircraft);
        assertNotNull(createdAircraft);
        assertEquals("Cessna 172", createdAircraft.getType());
        verify(aircraftRepository, times(1)).save(newAircraft);
    }

    @Test
    void testUpdateAircraft_Found() {
        Aircraft updatedDetails = new Aircraft();
        updatedDetails.setType("Boeing 747-8");
        updatedDetails.setAirlineName("Updated Airline");
        updatedDetails.setNumberOfPassengers(450);

        when(aircraftRepository.findById(1L)).thenReturn(Optional.of(aircraft1));
        when(aircraftRepository.save(any(Aircraft.class))).thenReturn(updatedDetails);
        Aircraft result = aircraftService.updateAircraft(1L, updatedDetails);
        assertNotNull(result);
        assertEquals("Boeing 747-8", result.getType());
        assertEquals(450, result.getNumberOfPassengers());
        verify(aircraftRepository, times(1)).findById(1L);
        verify(aircraftRepository, times(1)).save(aircraft1);
    }

    @Test
    void testUpdateAircraft_NotFound() {
        Aircraft updatedDetails = new Aircraft();
        updatedDetails.setType("NonExistent");
        updatedDetails.setAirlineName("None");
        updatedDetails.setNumberOfPassengers(1);

        when(aircraftRepository.findById(99L)).thenReturn(Optional.empty());
        Aircraft result = aircraftService.updateAircraft(99L, updatedDetails);
        assertNull(result);
        verify(aircraftRepository, times(1)).findById(99L);
        verify(aircraftRepository, never()).save(any(Aircraft.class));
    }

    @Test
    void testDeleteAircraft_Found() {
        when(aircraftRepository.existsById(1L)).thenReturn(true);
        aircraftService.deleteAircraft(1L);
        verify(aircraftRepository, times(1)).existsById(1L);
        verify(aircraftRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteAircraft_NotFound() {
        when(aircraftRepository.existsById(99L)).thenReturn(false);
        aircraftService.deleteAircraft(99L);
        verify(aircraftRepository, times(1)).existsById(99L);
        verify(aircraftRepository, never()).deleteById(anyLong());
    }

    @Test
    void testAddAirportToAircraft_Success() {
        when(aircraftRepository.findById(1L)).thenReturn(Optional.of(aircraft1));
        when(airportRepository.findById(100L)).thenReturn(Optional.of(airport1));
        when(aircraftRepository.save(any(Aircraft.class))).thenReturn(aircraft1);
        when(airportRepository.save(any(Airport.class))).thenReturn(airport1);

        Aircraft updatedAircraft = aircraftService.addAirportToAircraft(1L, 100L);
        assertNotNull(updatedAircraft);
        assertTrue(updatedAircraft.getAirports().contains(airport1));
        assertTrue(airport1.getAircraft().contains(aircraft1));
        verify(aircraftRepository, times(1)).findById(1L);
        verify(airportRepository, times(1)).findById(100L);
        verify(aircraftRepository, times(1)).save(aircraft1);
        verify(airportRepository, times(1)).save(airport1);
    }

    @Test
    void testAddAirportToAircraft_AircraftNotFound() {
        when(aircraftRepository.findById(99L)).thenReturn(Optional.empty());
        when(airportRepository.findById(100L)).thenReturn(Optional.of(airport1));
        assertThrows(RuntimeException.class, () -> {
            aircraftService.addAirportToAircraft(99L, 100L);
        });
        verify(aircraftRepository, never()).save(any(Aircraft.class));
        verify(airportRepository, never()).save(any(Airport.class));
    }

    @Test
    void testAddAirportToAircraft_AirportNotFound() {
        when(aircraftRepository.findById(1L)).thenReturn(Optional.of(aircraft1));
        when(airportRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            aircraftService.addAirportToAircraft(1L, 99L);
        });
        verify(aircraftRepository, never()).save(any(Aircraft.class));
        verify(airportRepository, never()).save(any(Airport.class));
    }

    @Test
    void testGetAirportsUsedByAircraft_Found() {
        when(aircraftRepository.findById(1L)).thenReturn(Optional.of(aircraft1));
        Set<Airport> airports = aircraftService.getAirportsUsedByAircraft(1L);
        assertNotNull(airports);
        assertEquals(2, airports.size());
        verify(aircraftRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAirportsUsedByAircraft_NotFound() {
        when(aircraftRepository.findById(99L)).thenReturn(Optional.empty());
        Set<Airport> airports = aircraftService.getAirportsUsedByAircraft(99L);
        assertNull(airports);
        verify(aircraftRepository, times(1)).findById(99L);
    }

    @Test
    void testGetPassengersOnAircraft_Found() {
        when(aircraftRepository.findById(1L)).thenReturn(Optional.of(aircraft1));
        Set<Passenger> passengers = aircraftService.getPassengersOnAircraft(1L);
        assertNotNull(passengers);
        assertEquals(2, passengers.size());
        verify(aircraftRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPassengersOnAircraft_NotFound() {
        when(aircraftRepository.findById(99L)).thenReturn(Optional.empty());
        Set<Passenger> passengers = aircraftService.getPassengersOnAircraft(99L);
        assertNull(passengers);
        verify(aircraftRepository, times(1)).findById(99L);
    }
}