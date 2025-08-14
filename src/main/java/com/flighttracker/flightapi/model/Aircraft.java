package com.flighttracker.flightapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "aircrafts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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


}