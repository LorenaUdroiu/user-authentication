package com.lu.user.authentication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Date;

@Entity
@Table(name = "USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @JsonIgnore
    private Long id;

    //user credentials
    //username in the email address format
    @Column(name = "USERNAME")
    @NotNull
    @Email(message = "Username should be a valid email")
    @JsonProperty(required = true)
    private String username;

    @Column(name = "PASSWORD")
    @NotNull
    @JsonProperty(required = true)
    private String password;

    //user details
    @Column(name = "FIRSTNAME")
    @NotNull
    @JsonProperty(required = true)
    private String firstName;

    @Column(name = "LASTNAME")
    @NotNull
    @JsonProperty(required = true)
    private String lastName;

    @Column(name = "BIRTHDATE")
    @JsonProperty
    private Date birthDate;

    @OneToOne
    @JoinColumn(name = "ADDRESS")
    @JsonProperty(required = true)
    private Address address;

    @Column(name = "CREATEDATE")
    @JsonProperty
    private OffsetDateTime createDate;

    @Column(name = "UPDATEDATE")
    @JsonProperty
    private OffsetDateTime updateDate;

    //user authentication details
    @Column(name = "FAILEDLOGINS")
    @JsonProperty
    private int failedLogins;

    @Column(name = "LASTLOGIN")
    @JsonProperty
    private OffsetDateTime lastLogin;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATE")
    @JsonProperty
    private UserState state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
    }

    public OffsetDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(OffsetDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public int getFailedLogins() {
        return failedLogins;
    }

    public void setFailedLogins(int failedLogins) {
        this.failedLogins = failedLogins;
    }

    public OffsetDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(OffsetDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }
}
