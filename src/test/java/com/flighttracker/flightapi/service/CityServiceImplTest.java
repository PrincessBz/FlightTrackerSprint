package com.flighttracker.flightapi.service;

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

        city1 = new City("New York", "NY", 8000000);
        city1.setId(1L);
        city2 = new City("Los Angeles", "CA", 4000000);
        city2.setId(2L);

    }

    @Test
    void testGetAllCities() {
        when(cityRepository.findAll()).thenReturn(Arrays.asList(city1, city2));

        List<City> cities = cityService.getAllCities();

        assertNotNull(cities);
        assertEquals(2, cities.size());
        assertEquals("New York", cities.get(0).getName());
        assertEquals("Los Angeles", cities.get(1).getName());

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
        City newCity = new City("Seattle", "WA", 750000);
        when(cityRepository.save(any(City.class))).thenReturn(newCity);
        City createdCity = cityService.createCity(newCity);
        assertNotNull(createdCity);
        assertEquals("Seattle", createdCity.getName());
        verify(cityRepository, times(1)).save(newCity);
    }
}
