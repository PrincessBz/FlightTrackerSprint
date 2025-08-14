package com.flighttracker.flightapi.util;

import com.flighttracker.flightapi.model.*;
import com.flighttracker.flightapi.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final CityRepository cityRepository;
    private final AirportRepository airportRepository;
    private final PassengerRepository passengerRepository;
    private final AircraftRepository aircraftRepository;
    private final FlightRepository flightRepository;

    public DataLoader(CityRepository cityRepository, AirportRepository airportRepository,
                      PassengerRepository passengerRepository, AircraftRepository aircraftRepository,
                      FlightRepository flightRepository) {
        this.cityRepository = cityRepository;
        this.airportRepository = airportRepository;
        this.passengerRepository = passengerRepository;
        this.aircraftRepository = aircraftRepository;
        this.flightRepository = flightRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Clear existing data
        flightRepository.deleteAllInBatch();
        passengerRepository.deleteAllInBatch();
        aircraftRepository.deleteAllInBatch();
        airportRepository.deleteAllInBatch();
        cityRepository.deleteAllInBatch();

        // 1. Create Cities using setters
        City nyc = new City();
        nyc.setName("New York");
        nyc.setState("NY");
        nyc.setPopulation(8400000);
        nyc = cityRepository.save(nyc);

        City la = new City();
        la.setName("Los Angeles");
        la.setState("CA");
        la.setPopulation(3900000);
        la = cityRepository.save(la);

        City chi = new City();
        chi.setName("Chicago");
        chi.setState("IL");
        chi.setPopulation(2700000);
        chi = cityRepository.save(chi);

        City mia = new City();
        mia.setName("Miami");
        mia.setState("FL");
        mia.setPopulation(470000);
        mia = cityRepository.save(mia);

        // 2. Create Airports using setters
        Airport jfk = new Airport();
        jfk.setName("John F. Kennedy International Airport");
        jfk.setCode("JFK");
        jfk.setCity(nyc);
        jfk = airportRepository.save(jfk);

        Airport lga = new Airport();
        lga.setName("LaGuardia Airport");
        lga.setCode("LGA");
        lga.setCity(nyc);
        lga = airportRepository.save(lga);

        Airport lax = new Airport();
        lax.setName("Los Angeles International Airport");
        lax.setCode("LAX");
        lax.setCity(la);
        lax = airportRepository.save(lax);

        Airport ord = new Airport();
        ord.setName("O'Hare International Airport");
        ord.setCode("ORD");
        ord.setCity(chi);
        ord = airportRepository.save(ord);

        Airport miamiAir = new Airport();
        miamiAir.setName("Miami International Airport");
        miamiAir.setCode("MIA");
        miamiAir.setCity(mia);
        miamiAir = airportRepository.save(miamiAir);

        Airport ftl = new Airport();
        ftl.setName("Fort Lauderdale-Hollywood International Airport");
        ftl.setCode("FLL");
        ftl.setCity(mia);
        ftl = airportRepository.save(ftl);


        // 3. Create Aircraft using setters
        Aircraft boeing747 = new Aircraft();
        boeing747.setType("Boeing 747");
        boeing747.setAirlineName("United Airlines");
        boeing747.setNumberOfPassengers(416);
        boeing747 = aircraftRepository.save(boeing747);

        Aircraft airbusA320 = new Aircraft();
        airbusA320.setType("Airbus A320");
        airbusA320.setAirlineName("Delta Airlines");
        airbusA320.setNumberOfPassengers(150);
        airbusA320 = aircraftRepository.save(airbusA320);

        // 4. Create Passengers using setters
        Passenger alice = new Passenger();
        alice.setFirstName("Alice");
        alice.setLastName("Smith");
        alice.setPhoneNumber("555-111-2222");
        alice.setCity(nyc);
        alice = passengerRepository.save(alice);

        Passenger bob = new Passenger();
        bob.setFirstName("Bob");
        bob.setLastName("Johnson");
        bob.setPhoneNumber("555-333-4444");
        bob.setCity(la);
        bob = passengerRepository.save(bob);

        // 5. Create Flights using setters
        System.out.println("Creating sample flights...");

        Flight flight1 = new Flight();
        flight1.setFlightNumber("UA123");
        flight1.setAirlineName("United Airlines");
        flight1.setOriginAirport(jfk);
        flight1.setDestinationAirport(lax);
        flight1.setDepartureTime(LocalDateTime.now().plusHours(1));
        flight1.setArrivalTime(LocalDateTime.now().plusHours(6));
        flight1.setGateNumber("A5");
        flight1.setStatus("ON TIME");
        flightRepository.save(flight1);

        Flight flight2 = new Flight();
        flight2.setFlightNumber("DL456");
        flight2.setAirlineName("Delta Airlines");
        flight2.setOriginAirport(jfk);
        flight2.setDestinationAirport(ord);
        flight2.setDepartureTime(LocalDateTime.now().plusHours(2));
        flight2.setArrivalTime(LocalDateTime.now().plusHours(4));
        flight2.setGateNumber("B12");
        flight2.setStatus("DELAYED");
        flightRepository.save(flight2);

        Flight flight3 = new Flight();
        flight3.setFlightNumber("AA987");
        flight3.setAirlineName("American Airlines");
        flight3.setOriginAirport(ord);
        flight3.setDestinationAirport(jfk);
        flight3.setDepartureTime(LocalDateTime.now().minusHours(3));
        flight3.setArrivalTime(LocalDateTime.now().minusMinutes(5));
        flight3.setGateNumber("C3");
        flight3.setStatus("LANDED");
        flightRepository.save(flight3);

        System.out.println("Data loaded successfully!");
        System.out.println("Cities: " + cityRepository.count());
        System.out.println("Airports: " + airportRepository.count());
        System.out.println("Passengers: " + passengerRepository.count());
        System.out.println("Aircrafts: " + aircraftRepository.count());
        System.out.println("Flights created: " + flightRepository.count());
    }
}