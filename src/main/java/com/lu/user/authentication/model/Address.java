package com.lu.user.authentication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "USER_ADDRESS")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @JsonIgnore
    private Long id;

    @Column(name = "CITY")
    @JsonProperty(required = true)
    private String city;

    @Column(name = "ZIPCODE")
    @JsonProperty(required = true)
    private String zipCode;

    @Column(name = "STATE")
    @JsonProperty(required = true)
    private String state;

    @Column(name = "COUNTRY")
    @JsonProperty(required = true)
    private String country;

    @Column(name = "STREET")
    @JsonProperty(required = true)
    private String street;

    @Column(name = "HOUSENUMBER")
    @JsonProperty(required = true)
    private String houseNumber;

    public String getCity() {
        return city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }
}
