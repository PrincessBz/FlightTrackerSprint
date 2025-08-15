package com.flighttracker.flightapi.service;

import com.flighttracker.flightapi.model.Airport;
import com.flighttracker.flightapi.model.City;
import com.flighttracker.flightapi.repository.CityRepository;
import com.flighttracker.flightapi.service.impl.CityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CityServiceImplTest {

    private CityRepository cityRepository;
    private CityServiceImpl cityService;

    private City city1;
    private City city2;

    @BeforeEach
    void setUp() {
        cityRepository = Mockito.mock(CityRepository.class);
        cityService = new CityServiceImpl(cityRepository);

        // --- THIS IS THE CORRECTED SECTION ---

        city1 = new City();
        city1.setId(1L);
        city1.setName("New York");
        city1.setState("NY");
        city1.setPopulation(8000000);


        city2 = new City();
        city2.setId(2L);
        city2.setName("Los Angeles");
        city2.setState("CA");
        city2.setPopulation(4000000);
    }

    @Test
    void testGetAllCities() {
        when(cityRepository.findAll()).thenReturn(Arrays.asList(city1, city2));
        List<City> cities = cityService.getAllCities();
        assertNotNull(cities);
        assertEquals(2, cities.size());
        verify(cityRepository, times(1)).findAll();
    }

    @Test
    void testGetCityById_Found() {
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city1));
        Optional<City> foundCity = cityService.getCityById(1L);
        assertTrue(foundCity.isPresent());
        assertEquals("New York", foundCity.get().getName());
        verify(cityRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateCity() {
        // --- THIS IS THE CORRECTED SECTION ---
        City newCity = new City();
        newCity.setName("Seattle");
        newCity.setState("WA");
        newCity.setPopulation(750000);

        when(cityRepository.save(any(City.class))).thenReturn(newCity);
        City createdCity = cityService.createCity(newCity);
        assertNotNull(createdCity);
        assertEquals("Seattle", createdCity.getName());
        verify(cityRepository, times(1)).save(newCity);
    }
}