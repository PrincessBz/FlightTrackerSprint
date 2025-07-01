package com.flighttracker.flightapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "aircrafts")
public class Aircraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String airlineName;
    private int numberOfPassengers;

    @ManyToMany(mappedBy = "aircrafts", fetch = FetchType.LAZY)
    @JsonBackReference("passenger-aircraft")
    private Set<Passenger> passengers = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "airport_aircraft",
        joinColumns = @JoinColumn(name = "aircraft_id"),
        inverseJoinColumns = @JoinColumn(name = "airport_id")
    )
    @JsonBackReference("airport-aircraft")
    private Set<Airport> airports = new HashSet<>();

    public Aircraft() {}

    public Aircraft(String type, String airlineName, int numberofPassengers) {
        this.type = type;
        this.airlineName = airlineName;
        this.numberOfPassengers = numberofPassengers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public void setNumberOfPassengers(int numberofPassengers) {
        this.numberOfPassengers = numberofPassengers;
    }

    public Set<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<Passenger> passengers) {
        this.passengers = passengers;
    }

    public Set<Airport> getAirports() {
        return airports;
    }

    public void setAirports(Set<Airport> airports) {
        this.airports = airports;
    }

    @Override
    public String toString() {
        return "Aircraft{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", airlineName='" + airlineName + '\'' +
                ", numberOfPassengers=" + numberOfPassengers +
                ", passengers=" + (passengers != null ? passengers.size() : 0) +
                ", airports=" + (airports != null ? airports.size() : 0) +
                '}';
    }


}
