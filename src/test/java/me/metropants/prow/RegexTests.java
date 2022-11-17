package me.metropants.prow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RegexTests {

    private static final String USERNAME_REGEX = "[a-zA-Z0-9]{3,32}$";

    @Test
    public void testUsername() {
        final List<String> validUsernames = List.of("metropants", "metropants123", "123metropants");
        final List<String> invalidUsernames = List.of("!metropants", "metro_pants_123", "#123metro`__`pants!");

        validUsernames.forEach(username -> assertTrue(username.matches(USERNAME_REGEX)));
        invalidUsernames.forEach(username -> assertFalse(username.matches(USERNAME_REGEX)));
    }

}
