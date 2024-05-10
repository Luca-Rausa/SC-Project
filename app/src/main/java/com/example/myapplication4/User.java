package com.example.myapplication4;

public class User {
    private int id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String role;

    // Constructor
    public User(int id, String firstname, String lastname, String email, String password, String role) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}

