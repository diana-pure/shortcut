package com.example.generics.task6.hierarchy;

import java.time.LocalDate;

public class SuperAdmin extends AdminUser {
    public SuperAdmin(String name) {
        super(name);
    }

    @Override
    public String getRole() {
        return "super admin";
    }

    public LocalDate whenExpired() {
        int year = LocalDate.now().getYear();
        return LocalDate.ofYearDay(year, 365);
    }
}
