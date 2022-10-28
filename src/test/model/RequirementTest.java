package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

// tests for the Requirement class
// no tests for the openUploadedDocument method in the Requirement class
// WARNING: there is a test for opening files using a Desktop application which will open the testFile.txt
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

    @Test
    void testUploadDocument() {
        assertNull(testRequirement.getUploadedDocument());

        Path testFile = Paths.get("./data/testFile");
        testRequirement.uploadDocument(testFile.toString());
        assertEquals(testFile.toFile(), testRequirement.getUploadedDocument());

        Path testFile2 = Paths.get("./data/testFile2");
        testRequirement.uploadDocument(testFile2.toString());
        assertEquals(testFile2.toFile(), testRequirement.getUploadedDocument());
    }

    @Test
    void testFailedUploadDocument() {
        Path invalidFile = Paths.get("");
        assertFalse(testRequirement.uploadDocument(invalidFile.toString()));
    }

    @Test
    void testOpenUploadedDocument() {
        Path testFile = Paths.get("./data/testFile");
        testRequirement.uploadDocument(testFile.toString());
        try {
            assertTrue(testRequirement.openUploadedDocument());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void testNullOpenUploadedDocument() {
        try {
            assertFalse(testRequirement.openUploadedDocument());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void testDeleteUploadedDocument() {
        Path testFile = Paths.get("./data/testFile");
        testRequirement.uploadDocument(testFile.toString());
        assertEquals(testFile.toFile(), testRequirement.getUploadedDocument());
        testRequirement.deleteUploadedDocument();
        assertNull(testRequirement.getUploadedDocument());
    }
}
