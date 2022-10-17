package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// tests for the Requirement class
// no tests for the methods that work with the filing system yet
public class RequirementTest {
    private Requirement testRequirement;

    @BeforeEach
    void runBefore() {
        testRequirement = new Requirement("Test");
    }

    @Test
    void testConstructor() {
        assertEquals("Test", testRequirement.getName());
        assertFalse(testRequirement.getStatus());
        assertEquals(null, testRequirement.getUploadedDocument());
    }

    @Test
    void testChangeStatus() {
        testRequirement.changeStatus(true);
        assertTrue(testRequirement.getStatus());
        testRequirement.changeStatus(false);
        assertFalse(testRequirement.getStatus());
    }
}
