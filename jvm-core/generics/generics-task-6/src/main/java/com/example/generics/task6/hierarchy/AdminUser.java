package com.example.generics.task6.hierarchy;

import java.util.Objects;

public class AdminUser extends User {

    public AdminUser(String name) {
        super(name);
    }

    public String getRole() {
        return "admin";
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }
}
