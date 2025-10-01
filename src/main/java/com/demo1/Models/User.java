package com.demo1.Models;

import java.util.UUID;

public class User {
    private UUID Id;
    private String Name;
    private String Email;
    private String  Password;
    private Role role;

    public enum Role{
        TELLER, MANAGER, ADMIN, AUDITOR
    }



    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + Id +
                ", name='" + Name + '\'' +
                ", email='" + Email + '\'' +
                ", role=" + role +
                '}';
    }
}
