package com.flighttracker.flightapi.service;

import com.flighttracker.flightapi.model.Airport;
import com.flighttracker.flightapi.model.City;
import com.flighttracker.flightapi.repository.AirportRepository;
import com.flighttracker.flightapi.repository.CityRepository;
import com.flighttracker.flightapi.service.impl.AirportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AirportServiceImplTest {

    private AirportRepository airportRepository;
    private CityRepository cityRepository;
    private AirportServiceImpl airportService;

    private City city;
    private Airport airport1;
    private Airport airport2;

    @BeforeEach
    void setUp() {
        airportRepository = Mockito.mock(AirportRepository.class);
        cityRepository = Mockito.mock(CityRepository.class);
        airportService = new AirportServiceImpl(airportRepository, cityRepository);

        city = new City();
        city.setId(1L);
        city.setName("New York");
        city.setState("NY");
        city.setPopulation(8000000);

        airport1 = new Airport();
        airport1.setId(101L);
        airport1.setName("JFK Airport");
        airport1.setCode("JFK");
        airport1.setCity(city);

        airport2 = new Airport();
        airport2.setId(102L);
        airport2.setName("LaGuardia Airport");
        airport2.setCode("LGA");
        airport2.setCity(city);
    }

    @Test
    void testGetAllAirports() {
        when(airportRepository.findAll()).thenReturn(Arrays.asList(airport1, airport2));
        List<Airport> airports = airportService.getAllAirports();
        assertNotNull(airports);
        assertEquals(2, airports.size());
        verify(airportRepository, times(1)).findAll();
    }

    @Test
    void testGetAirportById_Found() {
        when(airportRepository.findById(101L)).thenReturn(Optional.of(airport1));
        Optional<Airport> foundAirport = airportService.getAirportById(101L);
        assertTrue(foundAirport.isPresent());
        assertEquals("JFK", foundAirport.get().getCode());
        verify(airportRepository, times(1)).findById(101L);
    }

    @Test
    void testGetAirportById_NotFound() {
        when(airportRepository.findById(999L)).thenReturn(Optional.empty());
        Optional<Airport> foundAirport = airportService.getAirportById(999L);
        assertFalse(foundAirport.isPresent());
        verify(airportRepository, times(1)).findById(999L);
    }

    @Test
    void testCreateAirport_Success() {
        Airport newAirport = new Airport();
        newAirport.setName("Newark");
        newAirport.setCode("EWR");

        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        when(airportRepository.save(any(Airport.class))).thenReturn(newAirport);

        Airport createdAirport = airportService.createAirport(newAirport, 1L);
        assertNotNull(createdAirport);
        assertEquals("Newark", createdAirport.getName());
        verify(cityRepository, times(1)).findById(1L);
        verify(airportRepository, times(1)).save(newAirport);
    }

    @Test
    void testCreateAirport_CityNotFound() {
        Airport newAirport = new Airport();
        newAirport.setName("Newark");
        newAirport.setCode("EWR");

        when(cityRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            airportService.createAirport(newAirport, 99L);
        });

        assertEquals("City with ID 99 not found.", thrown.getMessage());
        verify(cityRepository, times(1)).findById(99L);
        verify(airportRepository, never()).save(any(Airport.class));
    }

    @Test
    void testUpdateAirport_Found_WithCityChange() {
        City newCity = new City();
        newCity.setId(2L);
        newCity.setName("Los Angeles");

        Airport updatedDetails = new Airport();
        updatedDetails.setName("JFK Updated");

        Airport savedAirport = new Airport();
        savedAirport.setId(101L);
        savedAirport.setName("JFK Updated");
        savedAirport.setCity(newCity);

        when(airportRepository.findById(101L)).thenReturn(Optional.of(airport1));
        when(cityRepository.findById(2L)).thenReturn(Optional.of(newCity));
        when(airportRepository.save(any(Airport.class))).thenReturn(savedAirport);

        Airport result = airportService.updateAirport(101L, updatedDetails, 2L);
        assertNotNull(result);
        assertEquals("JFK Updated", result.getName());
        assertEquals(newCity, result.getCity());
        verify(airportRepository, times(1)).findById(101L);
        verify(cityRepository, times(1)).findById(2L);
        verify(airportRepository, times(1)).save(airport1);
    }

    @Test
    void testUpdateAirport_Found_NoCityChange() {
        Airport updatedDetails = new Airport();
        updatedDetails.setName("JFK Updated");

        Airport savedAirport = new Airport();
        savedAirport.setId(101L);
        savedAirport.setName("JFK Updated");
        savedAirport.setCity(city);

        when(airportRepository.findById(101L)).thenReturn(Optional.of(airport1));
        when(airportRepository.save(any(Airport.class))).thenReturn(savedAirport);

        Airport result = airportService.updateAirport(101L, updatedDetails, null);
        assertNotNull(result);
        assertEquals(city, result.getCity());
        verify(airportRepository, times(1)).findById(101L);
        verify(cityRepository, never()).findById(anyLong());
        verify(airportRepository, times(1)).save(airport1);
    }

    @Test
    void testUpdateAirport_NotFound() {
        Airport updatedDetails = new Airport();
        updatedDetails.setName("NonExistent");

        when(airportRepository.findById(999L)).thenReturn(Optional.empty());

        Airport result = airportService.updateAirport(999L, updatedDetails, 1L);
        assertNull(result);
        verify(airportRepository, times(1)).findById(999L);
        verify(airportRepository, never()).save(any(Airport.class));
    }

    @Test
    void testDeleteAirport_Found() {
        when(airportRepository.existsById(101L)).thenReturn(true);
        airportService.deleteAirport(101L);
        verify(airportRepository, times(1)).existsById(101L);
        verify(airportRepository, times(1)).deleteById(101L);
    }

    @Test
    void testDeleteAirport_NotFound() {
        when(airportRepository.existsById(999L)).thenReturn(false);
        airportService.deleteAirport(999L);
        verify(airportRepository, times(1)).existsById(999L);
        verify(airportRepository, never()).deleteById(anyLong());
    }
}