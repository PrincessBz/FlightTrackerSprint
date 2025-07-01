package com.flighttracker.flightapi.util;

import com.flighttracker.flightapi.model.Aircraft;
import com.flighttracker.flightapi.model.Airport;
import com.flighttracker.flightapi.model.City;
import com.flighttracker.flightapi.model.Passenger;
import com.flighttracker.flightapi.repository.AircraftRepository;
import com.flighttracker.flightapi.repository.AirportRepository;
import com.flighttracker.flightapi.repository.CityRepository;
import com.flighttracker.flightapi.repository.PassengerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataLoader implements CommandLineRunner {

        private final CityRepository cityRepository;
        private final AirportRepository airportRepository;
        private final PassengerRepository passengerRepository;
        private final AircraftRepository aircraftRepository;

        public DataLoader(CityRepository cityRepository, AirportRepository airportRepository,
                          PassengerRepository passengerRepository, AircraftRepository aircraftRepository) {
            this.cityRepository = cityRepository;
            this.airportRepository = airportRepository;
            this.passengerRepository = passengerRepository;
            this.aircraftRepository = aircraftRepository;
        }

        @Override
        @Transactional // Ensure all operations are part of a single transaction
        public void run(String... args) throws Exception {
            // Clear existing data (for development/testing purposes)
            airportRepository.deleteAllInBatch();
            passengerRepository.deleteAllInBatch();
            aircraftRepository.deleteAllInBatch();
            cityRepository.deleteAllInBatch();

            // 1. Create Cities
            City nyc = new City("New York", "NY", 8400000);
            City la = new City("Los Angeles", "CA", 3900000);
            City chi = new City("Chicago", "IL", 2700000);
            City mia = new City("Miami", "FL", 470000);

            nyc = cityRepository.save(nyc);
            la = cityRepository.save(la);
            chi = cityRepository.save(chi);
            mia = cityRepository.save(mia);

            // 2. Create Airports and associate with Cities (A City has many Airports, An Airport belongs to one City)
            Airport jfk = new Airport("John F. Kennedy International Airport", "JFK", nyc);
            Airport lga = new Airport("LaGuardia Airport", "LGA", nyc);
            Airport lax = new Airport("Los Angeles International Airport", "LAX", la);
            Airport ord = new Airport("O'Hare International Airport", "ORD", chi);
            Airport miami = new Airport("Miami International Airport", "MIA", mia);
            Airport ftl = new Airport("Fort Lauderdale-Hollywood International Airport", "FLL", mia); // Another airport in Miami area

            jfk = airportRepository.save(jfk);
            lga = airportRepository.save(lga);
            lax = airportRepository.save(lax);
            ord = airportRepository.save(ord);
            miami = airportRepository.save(miami);
            ftl = airportRepository.save(ftl);

            // Update city's airport list (optional, but good for bidirectional consistency)
            nyc.getAirports().add(jfk);
            nyc.getAirports().add(lga);
            la.getAirports().add(lax);
            chi.getAirports().add(ord);
            mia.getAirports().add(miami);
            mia.getAirports().add(ftl);
            cityRepository.save(nyc);
            cityRepository.save(la);
            cityRepository.save(chi);
            cityRepository.save(mia);


            // 3. Create Aircraft
            Aircraft boeing747 = new Aircraft("Boeing 747", "United Airlines", 416);
            Aircraft airbusA320 = new Aircraft("Airbus A320", "Delta Airlines", 150);
            Aircraft boeing737 = new Aircraft("Boeing 737", "Southwest Airlines", 143);
            Aircraft embraer175 = new Aircraft("Embraer 175", "American Eagle", 76);

            boeing747 = aircraftRepository.save(boeing747);
            airbusA320 = aircraftRepository.save(airbusA320);
            boeing737 = aircraftRepository.save(boeing737);
            embraer175 = aircraftRepository.save(embraer175);

            // Associate Aircraft with Airports (An Aircraft can use many Airports)
            boeing747.getAirports().add(jfk);
            boeing747.getAirports().add(lax);
            jfk.getAircraft().add(boeing747); lax.getAircraft().add(boeing747); // Bidirectional
            aircraftRepository.save(boeing747); airportRepository.save(jfk); airportRepository.save(lax);

            airbusA320.getAirports().add(lga);
            airbusA320.getAirports().add(ord);
            lga.getAircraft().add(airbusA320); ord.getAircraft().add(airbusA320); // Bidirectional
            aircraftRepository.save(airbusA320); airportRepository.save(lga); airportRepository.save(ord);

            boeing737.getAirports().add(lax);
            boeing737.getAirports().add(miami);
            lax.getAircraft().add(boeing737); miami.getAircraft().add(boeing737); // Bidirectional
            aircraftRepository.save(boeing737); airportRepository.save(lax); airportRepository.save(miami);

            embraer175.getAirports().add(ftl);
            embraer175.getAirports().add(jfk);
            ftl.getAircraft().add(embraer175); jfk.getAircraft().add(embraer175); // Bidirectional
            aircraftRepository.save(embraer175); airportRepository.save(ftl); airportRepository.save(jfk);

            // 4. Create Passengers and associate with City (A Passenger lives in one City)
            Passenger alice = new Passenger("Alice", "Smith", "555-111-2222", nyc);
            Passenger bob = new Passenger("Bob", "Johnson", "555-333-4444", la);
            Passenger charlie = new Passenger("Charlie", "Brown", "555-555-6666", chi);
            Passenger diana = new Passenger("Diana", "Prince", "555-777-8888", mia);

            alice = passengerRepository.save(alice);
            bob = passengerRepository.save(bob);
            charlie = passengerRepository.save(charlie);
            diana = passengerRepository.save(diana);

            // Update city's passenger list (optional, for bidirectional consistency, though not explicitly mapped in City entity)
            // nyc.getPassengers().add(alice); // If Passenger relationship were managed by City
            // cityRepository.save(nyc);

            // Associate Passengers with Aircraft (A Passenger can fly on many Aircraft)
            alice.getAircrafts().add(boeing747);
            boeing747.getPassengers().add(alice); // Bidirectional
            passengerRepository.save(alice); aircraftRepository.save(boeing747);

            alice.getAircrafts().add(airbusA320);
            airbusA320.getPassengers().add(alice); // Bidirectional
            passengerRepository.save(alice); aircraftRepository.save(airbusA320);

            bob.getAircrafts().add(boeing747);
            boeing747.getPassengers().add(bob); // Bidirectional
            passengerRepository.save(bob); aircraftRepository.save(boeing747);

            charlie.getAircrafts().add(airbusA320);
            airbusA320.getPassengers().add(charlie); // Bidirectional
            passengerRepository.save(charlie); aircraftRepository.save(airbusA320);

            diana.getAircrafts().add(boeing737);
            boeing737.getPassengers().add(diana); // Bidirectional
            passengerRepository.save(diana); aircraftRepository.save(boeing737);

            diana.getAircrafts().add(embraer175);
            embraer175.getPassengers().add(diana); // Bidirectional
            passengerRepository.save(diana); aircraftRepository.save(embraer175);


            System.out.println("Data loaded successfully!");
            System.out.println("Cities: " + cityRepository.findAll().size());
            System.out.println("Airports: " + airportRepository.findAll().size());
            System.out.println("Passengers: " + passengerRepository.findAll().size());
            System.out.println("Aircrafts: " + aircraftRepository.findAll().size());
        }
}
