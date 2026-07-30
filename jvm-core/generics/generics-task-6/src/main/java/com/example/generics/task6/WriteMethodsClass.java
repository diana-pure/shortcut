package com.example.generics.task6;

import com.example.generics.task6.hierarchy.AdminUser;
import com.example.generics.task6.hierarchy.SuperAdmin;
import com.example.generics.task6.hierarchy.User;

import java.io.PrintStream;
import java.util.List;


public class WriteMethodsClass {
    private static final PrintStream PS = System.out;

    // param can be List<Object>
    public static void addObject(List<? super Object> list) {
        list.add(new Object()); //can add lower bound Object or lower types, ancestors
        list.add(new User(""));
        list.add(new AdminUser(""));
        list.add(new SuperAdmin(""));
    }

    // param can be List<User>, List<Object>
    public static void addUser(List<? super User> list) {
//        list.add(new Object()); //can not add higher type
        list.add(new User("")); //can add lower bound User or lower types, ancestors
        list.add(new AdminUser(""));
        list.add(new SuperAdmin(""));
    }

    // param can be List<AdminUser>, List<User>, List<Object>
    public static void addAdmin(List<? super AdminUser> list) {
//        list.add(new Object()); //can add lower bound or lower types, ancestors
//        list.add(new User(""));
        list.add(new AdminUser("")); //can add lower bound AdminUser or lower types, ancestors
        list.add(new SuperAdmin(""));
    }

    // param can be List<SuperAdmin>, List<AdminUser>, List<User>, List<Object>
    public static void addSuperAdmin(List<? super SuperAdmin> list) {
//        list.add(new Object()); //can not add higher type
//        list.add(new User(""));
//        list.add(new AdminUser(""));
        list.add(new SuperAdmin("")); //can add lower bound SuperAdmin or lower types, ancestors
    }
}
