package com.example.generics.task5;

import java.util.Objects;

public final class AdminUser extends User {
    private final String role;

    public AdminUser(String id, String name, String role) {
        super(id, name);
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("role must not be blank");
        }
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return super.toString().replace("}", ", role='" + role + "'}");
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        AdminUser that = (AdminUser) o;
        return role.equals(that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), role);
    }
}
