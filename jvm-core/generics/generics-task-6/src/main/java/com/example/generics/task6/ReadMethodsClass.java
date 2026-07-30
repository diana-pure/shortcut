package com.example.generics.task6;

import com.example.generics.task6.hierarchy.AdminUser;
import com.example.generics.task6.hierarchy.SuperAdmin;
import com.example.generics.task6.hierarchy.User;

import java.io.PrintStream;
import java.util.List;


public class ReadMethodsClass {
    private static final PrintStream PS = System.out;

    // param can be List<Object>, List<User>, List<AdminUser>, List<SuperAdmin>
    public static void printObject(List<? extends Object> list) {
        Object obj = list.get(0); //reading the bound type or higher up to Object
        PS.println(obj.toString());
    }

    // param can be List<User>, List<AdminUser>, List<SuperAdmin>
    public static void printUser(List<? extends User> list) {
        User user = list.get(0); //reading the bound type or higher up to Object
        PS.println(user.getName());

        Object obj = list.get(0);
        PS.println(obj.toString());
    }

    // param can be  List<AdminUser>, List<SuperAdmin>
    public static void printAdmin(List<? extends AdminUser> list) {
        AdminUser admin = list.get(0); //reading the bound type or higher up to Object
        PS.println(admin.getName());
        PS.println(admin.getRole());

        Object obj = list.get(0);
        obj.toString();

        User user = list.get(0);
        user.getName();
    }

    // param can be List<SuperAdmin>
    public static void printSuperAdmin(List<? extends SuperAdmin> list) {
        Object obj = list.get(0);
        PS.println(obj.toString());

        User user = list.get(0);
        PS.println(user.getName());

        AdminUser admin = list.get(0); //reading the bound type or higher up to Object
        PS.println(admin.getName());
        PS.println(admin.getRole());

        SuperAdmin superAdmin = list.get(0);
        PS.println(superAdmin.getName());
        PS.println(superAdmin.getRole());
        PS.println(superAdmin.whenExpired());
    }
}
