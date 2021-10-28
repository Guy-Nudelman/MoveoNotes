package com.example.moveonotes.Model;

public class User {

    //Variables
    private String email;
    private String firstName;
    private String lastName;
    private int pin;

    //Constructors
    public User() {
    }

    public User(String email, String firstName, String lastName, int pin) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pin = pin;
    }


    //Class Methods
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
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

}
