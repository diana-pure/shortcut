package com.example.generics.task5;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты намеренно падают, пока студент не реализует TODO в RepositoryUtils.
 */
class RepositoryUtilsTest {

    @Test
    void saveAll_shouldAcceptListOfAdminUser_andStoreAsUser() {
        Repository<User> repo = new UserRepository();

        List<AdminUser> admins = List.of(
                new AdminUser("a-1", "Admin-1", "MODERATOR"),
                new AdminUser("a-2", "Admin-2", "AUDITOR")
        );

        RepositoryUtils.saveAll(repo, admins);

        assertNotNull(repo.findById("a-1"));
        assertEquals("Admin-1", repo.findById("a-1").getName());
        assertEquals(AdminUser.class, repo.findById("a-2").getClass());
    }

    @Test
    void getFirstUser_shouldWorkForListOfUserAndListOfAdminUser() {
        User u1 = new User("u-1", "Alex");
        AdminUser a1 = new AdminUser("a-1", "Veronica", "SUPER_ADMIN");

        assertEquals(u1, RepositoryUtils.getFirstUser(List.of(u1)));
        assertEquals(a1, RepositoryUtils.getFirstUser(List.of(a1)));
    }

    @Test
    void addDefaultUser_shouldAcceptListOfUser_andListOfObject() {
        List<User> usersSink = new ArrayList<>();
        RepositoryUtils.addDefaultUser(usersSink);
        assertEquals(1, usersSink.size());
        assertEquals("default", usersSink.get(0).getId());

        List<Object> objectsSink = new ArrayList<>();
        RepositoryUtils.addDefaultUser(objectsSink);
        assertEquals(1, objectsSink.size());
        assertTrue(objectsSink.get(0) instanceof User);
    }

    @Test
    void invariance_example_explainedByCompilation() {
        Repository<User> repo = new UserRepository();

        // Инвариантность: эти типы НЕ связаны наследованием, присваивание запрещено:
        // Repository<AdminUser> adminRepo = repo; // compilation error

        assertNotNull(repo);
    }
}
