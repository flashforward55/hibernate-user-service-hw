package mate.academy;

import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Injector;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");

    public static void main(String[] args) {
        AuthenticationService authService = (AuthenticationService) injector
                .getInstance(AuthenticationService.class);

        System.out.println("=== Testing Authentication Service ===\n");

        testRegistration(authService);
        testLogin(authService);
        testDuplicateRegistration(authService);
        testInvalidLogin(authService);

        System.out.println("\n=== All tests completed! ===");
    }

    private static void testRegistration(AuthenticationService authService) {
        System.out.println("--- Test 1: Register new user ---");
        try {
            User user = authService.register("alice@example.com", "password123");
            System.out.println("✓ User registered successfully: " + user);
        } catch (RegistrationException e) {
            System.err.println("✗ Registration failed: " + e.getMessage());
        }
    }

    private static void testLogin(AuthenticationService authService) {
        System.out.println("\n--- Test 2: Login with correct credentials ---");
        try {
            User user = authService.login("alice@example.com", "password123");
            System.out.println("✓ Login successful: " + user);
        } catch (AuthenticationException e) {
            System.err.println("✗ Login failed: " + e.getMessage());
        }
    }

    private static void testDuplicateRegistration(AuthenticationService authService) {
        System.out.println("\n--- Test 3: Try to register with existing email ---");
        try {
            authService.register("alice@example.com", "anotherPassword");
            System.out.println("✗ Should have thrown RegistrationException!");
        } catch (RegistrationException e) {
            System.out.println("✓ Correctly rejected duplicate email: " + e.getMessage());
        }
    }

    private static void testInvalidLogin(AuthenticationService authService) {
        System.out.println("\n--- Test 4: Login with incorrect password ---");
        try {
            authService.login("alice@example.com", "wrongPassword");
            System.out.println("✗ Should have thrown AuthenticationException!");
        } catch (AuthenticationException e) {
            System.out.println("✓ Correctly rejected wrong password: " + e.getMessage());
        }

        System.out.println("\n--- Test 5: Login with non-existent email ---");
        try {
            authService.login("nonexistent@example.com", "password");
            System.out.println("✗ Should have thrown AuthenticationException!");
        } catch (AuthenticationException e) {
            System.out.println("✓ Correctly rejected non-existent user: "
                    + e.getMessage());
        }
    }
}
