package com.example.generics.task6;

import com.example.generics.task6.hierarchy.AdminUser;
import com.example.generics.task6.hierarchy.SuperAdmin;
import com.example.generics.task6.hierarchy.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Демо PECS
 */
public class Main {
    private final static List<Object> objects = new ArrayList<>();
    private final static List<User> users = new ArrayList<>();
    private final static List<AdminUser> admins = new ArrayList<>();
    private final static List<SuperAdmin> supers = new ArrayList<>();

    public static void main(String[] args) {
        objects.add(new Object());
        users.add(new User(""));
        admins.add(new AdminUser(""));
        supers.add(new SuperAdmin(""));

        readPECS();
        writePECS();
    }

    private static void readPECS() {
        //print any that extends Object
        ReadMethodsClass.printObject(objects);
        ReadMethodsClass.printObject(users);
        ReadMethodsClass.printObject(admins);
        ReadMethodsClass.printObject(supers);

        //print any that extends User
//        ReadMethodsClass.printUser(objects); can not pass object
        ReadMethodsClass.printUser(users);
        ReadMethodsClass.printUser(admins);
        ReadMethodsClass.printUser(supers);

        //print any that extends AdminUser
//        ReadMethodsClass.printAdmin(objects); can not pass object
//        ReadMethodsClass.printAdmin(users);   can not pass user
        ReadMethodsClass.printAdmin(admins);
        ReadMethodsClass.printAdmin(supers);

        //print any that extends AdminUser
//        ReadMethodsClass.printAdmin(objects); can not pass object
//        ReadMethodsClass.printAdmin(users);   can not pass user
//        ReadMethodsClass.printSuperAdmin(admins); can not pass admin
        ReadMethodsClass.printSuperAdmin(supers);
    }

    private static void writePECS() {
        WriteMethodsClass.addObject(objects); //add object to objects
//        WriteMethodsClass.addObject(users); can not add Object to list with users
//        WriteMethodsClass.addObject(admins); can not add Object to list with admins
//        WriteMethodsClass.addObject(supers); can not add Object to list with super admin

        WriteMethodsClass.addUser(objects); // add User to objects
        WriteMethodsClass.addUser(users);   // add User to users
//        WriteMethodsClass.addUser(admins); can not add User to list with admins
//        WriteMethodsClass.addUser(supers); can not add User to list with super admins

        WriteMethodsClass.addAdmin(objects); // add AdminUser to objects
        WriteMethodsClass.addAdmin(users);   // add AdminUser to users
        WriteMethodsClass.addAdmin(admins);  // add AdminUser to admins
//        WriteMethodsClass.addAdmin(supers); can not add AdminUser to list with super admins

        WriteMethodsClass.addSuperAdmin(objects); // add SuperAdmin to objects
        WriteMethodsClass.addSuperAdmin(users);   // add SuperAdmin to users
        WriteMethodsClass.addSuperAdmin(admins);  // add SuperAdmin to admins
        WriteMethodsClass.addSuperAdmin(supers);  // add SuperAdmin to supers
    }
}
