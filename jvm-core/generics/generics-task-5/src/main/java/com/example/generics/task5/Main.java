package com.example.generics.task5;

import java.util.ArrayList;
import java.util.List;

/**
 * Небольшой пример запуска без тестов.
 * Тесты находятся в src/test.
 */
public class Main {
    public static void main(String[] args) {
        Repository<User> repo = new UserRepository();

        List<AdminUser> admins = List.of(
                new AdminUser("a-1", "Admin-1", "MODERATOR"),
                new AdminUser("a-2", "Admin-2", "AUDITOR")
        );

        // TODO: после реализации RepositoryUtils.saveAll(...) это должно работать
         RepositoryUtils.saveAll(repo, admins);

        List<Object> sink = new ArrayList<>();
        // TODO: после реализации RepositoryUtils.addDefaultUser(...) это должно работать
         RepositoryUtils.addDefaultUser(sink);

        System.out.println("Done");
    }
}
