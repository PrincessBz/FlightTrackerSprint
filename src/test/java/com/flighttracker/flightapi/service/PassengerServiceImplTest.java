package com.flighttracker.flightapi.service;

import com.flighttracker.flightapi.model.Aircraft;
import com.flighttracker.flightapi.model.Airport;
import com.flighttracker.flightapi.model.City;
import com.flighttracker.flightapi.model.Passenger;
import com.flighttracker.flightapi.repository.AircraftRepository;
import com.flighttracker.flightapi.repository.CityRepository;
import com.flighttracker.flightapi.repository.PassengerRepository;
import com.flighttracker.flightapi.service.impl.PassengerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito; // Import Mockito

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PassengerServiceImplTest {
    private PassengerRepository passengerRepository;
    private CityRepository cityRepository;
    private AircraftRepository aircraftRepository;

    private PassengerServiceImpl passengerService; // Declare service without @InjectMocks

    private City city;
    private Passenger passenger1;
    private Passenger passenger2;
    private Aircraft aircraft1;
    private Aircraft aircraft2;
    private Airport airport1;
    private Airport airport2;

    @BeforeEach
    void setUp() {
        // Manually create mock instances
        passengerRepository = Mockito.mock(PassengerRepository.class);
        cityRepository = Mockito.mock(CityRepository.class);
        aircraftRepository = Mockito.mock(AircraftRepository.class);

        // Manually inject mocks into the service constructor
        passengerService = new PassengerServiceImpl(passengerRepository, cityRepository, aircraftRepository);

        city = new City("New York", "NY", 8000000);
        city.setId(1L);

        passenger1 = new Passenger("Alice", "Smith", "555-111-2222", city);
        passenger1.setId(1L);
        passenger2 = new Passenger("Bob", "Johnson", "555-333-4444", city);
        passenger2.setId(2L);

        aircraft1 = new Aircraft("Boeing 747", "United", 400);
        aircraft1.setId(10L);
        aircraft2 = new Aircraft("Airbus A320", "Delta", 150);
        aircraft2.setId(11L);

        airport1 = new Airport("JFK", "JFK", city);
        airport1.setId(100L);
        airport2 = new Airport("LAX", "LAX", city);
        airport2.setId(101L);

        // Setup relationships for testing
        passenger1.getAircrafts().add(aircraft1);
        passenger1.getAircrafts().add(aircraft2);

        aircraft1.getAirports().add(airport1);
        aircraft2.getAirports().add(airport2);
    }

    @Test
    void testGetAllPassengers() {
        when(passengerRepository.findAll()).thenReturn(Arrays.asList(passenger1, passenger2));
        List<Passenger> passengers = passengerService.getAllPassengers();
        assertNotNull(passengers);
        assertEquals(2, passengers.size());
        assertEquals("Alice", passengers.get(0).getFirstName());
        verify(passengerRepository, times(1)).findAll();
    }

    @Test
    void testGetPassengerById_Found() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger1));
        Optional<Passenger> foundPassenger = passengerService.getPassengerById(1L);
        assertTrue(foundPassenger.isPresent());
        assertEquals("Alice", foundPassenger.get().getFirstName());
        verify(passengerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPassengerById_NotFound() {
        when(passengerRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Passenger> foundPassenger = passengerService.getPassengerById(99L);
        assertFalse(foundPassenger.isPresent());
        verify(passengerRepository, times(1)).findById(99L);
    }

    @Test
    void testCreatePassenger_Success() {
        Passenger newPassenger = new Passenger("Charlie", "Brown", "555-555-6666", null);
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        when(passengerRepository.save(any(Passenger.class))).thenReturn(newPassenger);
        Passenger createdPassenger = passengerService.createPassenger(newPassenger, 1L);
        assertNotNull(createdPassenger);
        assertEquals("Charlie", createdPassenger.getFirstName());
        assertEquals(city, createdPassenger.getCity());
        verify(cityRepository, times(1)).findById(1L);
        verify(passengerRepository, times(1)).save(newPassenger);
    }

    @Test
    void testCreatePassenger_CityNotFound() {
        Passenger newPassenger = new Passenger("Charlie", "Brown", "555-555-6666", null);
        when(cityRepository.findById(99L)).thenReturn(Optional.empty());
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            passengerService.createPassenger(newPassenger, 99L);
        });
        assertEquals("City with ID 99 not found.", thrown.getMessage());
        verify(cityRepository, times(1)).findById(99L);
        verify(passengerRepository, never()).save(any(Passenger.class));
    }

    @Test
    void testUpdatePassenger_Found_WithCityChange() {
        City newCity = new City("Los Angeles", "CA", 4000000);
        newCity.setId(2L);
        Passenger updatedDetails = new Passenger("Alice Updated", "Smith", "555-111-2222", null);
        updatedDetails.setId(1L);

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger1));
        when(cityRepository.findById(2L)).thenReturn(Optional.of(newCity));
        // Simulate the saved passenger with the new city
        Passenger savedPassenger = new Passenger("Alice Updated", "Smith", "555-111-2222", newCity);
        savedPassenger.setId(1L);
        when(passengerRepository.save(any(Passenger.class))).thenReturn(savedPassenger);

        Passenger result = passengerService.updatePassenger(1L, updatedDetails, 2L);

        assertNotNull(result);
        assertEquals("Alice Updated", result.getFirstName());
        assertEquals(newCity, result.getCity());

        verify(passengerRepository, times(1)).findById(1L);
        verify(cityRepository, times(1)).findById(2L);
        verify(passengerRepository, times(1)).save(passenger1);
    }

    @Test
    void testUpdatePassenger_Found_NoCityChange() {
        Passenger updatedDetails = new Passenger("Alice Updated", "Smith", "555-111-2222", null);
        updatedDetails.setId(1L);

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger1));
        // Simulate the saved passenger with the same city
        Passenger savedPassenger = new Passenger("Alice Updated", "Smith", "555-111-2222", city);
        savedPassenger.setId(1L);
        when(passengerRepository.save(any(Passenger.class))).thenReturn(savedPassenger);

        Passenger result = passengerService.updatePassenger(1L, updatedDetails, null);

        assertNotNull(result);
        assertEquals("Alice Updated", result.getFirstName());
        assertEquals(city, result.getCity());

        verify(passengerRepository, times(1)).findById(1L);
        verify(cityRepository, never()).findById(anyLong());
        verify(passengerRepository, times(1)).save(passenger1);
    }

    @Test
    void testUpdatePassenger_NotFound() {
        Passenger updatedDetails = new Passenger("NonExistent", "User", "555-000-0000", null);
        when(passengerRepository.findById(99L)).thenReturn(Optional.empty());
        Passenger result = passengerService.updatePassenger(99L, updatedDetails, 1L);
        assertNull(result);
        verify(passengerRepository, times(1)).findById(99L);
        verify(passengerRepository, never()).save(any(Passenger.class));
    }

    @Test
    void testDeletePassenger_Found() {
        when(passengerRepository.existsById(1L)).thenReturn(true);
        passengerService.deletePassenger(1L);
        verify(passengerRepository, times(1)).existsById(1L);
        verify(passengerRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePassenger_NotFound() {
        when(passengerRepository.existsById(99L)).thenReturn(false);
        passengerService.deletePassenger(99L);
        verify(passengerRepository, times(1)).existsById(99L);
        verify(passengerRepository, never()).deleteById(anyLong());
    }

    @Test
    void testAddAircraftToPassenger_Success() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger1));
        when(aircraftRepository.findById(10L)).thenReturn(Optional.of(aircraft1));
        when(passengerRepository.save(any(Passenger.class))).thenReturn(passenger1);
        when(aircraftRepository.save(any(Aircraft.class))).thenReturn(aircraft1);

        Passenger updatedPassenger = passengerService.addAircraftToPassenger(1L, 10L);

        assertNotNull(updatedPassenger);
        assertTrue(updatedPassenger.getAircrafts().contains(aircraft1));
        assertTrue(aircraft1.getPassengers().contains(passenger1));

        verify(passengerRepository, times(1)).findById(1L);
        verify(aircraftRepository, times(1)).findById(10L);
        verify(passengerRepository, times(1)).save(passenger1);
        verify(aircraftRepository, times(1)).save(aircraft1);
    }

    @Test
    void testAddAircraftToPassenger_PassengerNotFound() {
        when(passengerRepository.findById(99L)).thenReturn(Optional.empty());
        when(aircraftRepository.findById(10L)).thenReturn(Optional.of(aircraft1));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            passengerService.addAircraftToPassenger(99L, 10L);
        });
        assertEquals("Passenger or Aircraft not found.", thrown.getMessage());

        verify(passengerRepository, times(1)).findById(99L);
        verify(aircraftRepository, times(1)).findById(10L);
        verify(passengerRepository, never()).save(any(Passenger.class));
        verify(aircraftRepository, never()).save(any(Aircraft.class));
    }

    @Test
    void testAddAircraftToPassenger_AircraftNotFound() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger1));
        when(aircraftRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            passengerService.addAircraftToPassenger(1L, 99L);
        });
        assertEquals("Passenger or Aircraft not found.", thrown.getMessage());

        verify(passengerRepository, times(1)).findById(1L);
        verify(aircraftRepository, times(1)).findById(99L);
        verify(passengerRepository, never()).save(any(Passenger.class));
        verify(aircraftRepository, never()).save(any(Aircraft.class));
    }

    @Test
    void testGetAircraftsFlownByPassenger_Found() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger1));
        Set<Aircraft> aircrafts = passengerService.getAircraftsFlownByPassenger(1L);
        assertNotNull(aircrafts);
        assertEquals(2, aircrafts.size());
        assertTrue(aircrafts.contains(aircraft1));
        assertTrue(aircrafts.contains(aircraft2));
        verify(passengerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAircraftsFlownByPassenger_NotFound() {
        when(passengerRepository.findById(99L)).thenReturn(Optional.empty());
        Set<Aircraft> aircrafts = passengerService.getAircraftsFlownByPassenger(99L);
        assertNull(aircrafts);
        verify(passengerRepository, times(1)).findById(99L);
    }

    @Test
    void testGetAirportsUsedByPassenger_Found() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger1));
        Set<Airport> airports = passengerService.getAirportsUsedByPassenger(1L);
        assertNotNull(airports);
        assertEquals(2, airports.size());
        assertTrue(airports.contains(airport1));
        assertTrue(airports.contains(airport2));
        verify(passengerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAirportsUsedByPassenger_NotFound() {
        when(passengerRepository.findById(99L)).thenReturn(Optional.empty());
        Set<Airport> airports = passengerService.getAirportsUsedByPassenger(99L);
        assertNull(airports);
        verify(passengerRepository, times(1)).findById(99L);
    }
}


