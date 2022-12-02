package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// tests for the Requirement class
// no tests for the openUploadedDocument method in the Requirement class
// WARNING: there is a test for opening files using a Desktop application which will open the testFile.txt
public class RequirementTest {
    private Requirement testRequirement;

    @BeforeEach
    void runBefore() {
        testRequirement = new Requirement("Test");
        EventLog.getInstance().clear();
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

        testEventLog(1, "Changed status of Test to completed");
        testEventLog(2, "Changed status of Test to incomplete");
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

        testEventLog(1, "Uploaded file testFile for Test");
        testEventLog(2, "Uploaded file testFile2 for Test");
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
            testRequirement.openUploadedDocument(); // opens the file
        } catch (IOException e) {
            fail();
        }
        testEventLog(1, "Opened file testFile for Test");
    }

    @Test
    void testNullOpenUploadedDocument() {
        try {
            testRequirement.openUploadedDocument();
        } catch (IOException e) {
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    void testDeleteUploadedDocument() {
        Path testFile = Paths.get("./data/testFile");
        testRequirement.uploadDocument(testFile.toString());
        assertEquals(testFile.toFile(), testRequirement.getUploadedDocument());
        testRequirement.deleteUploadedDocument();
        assertNull(testRequirement.getUploadedDocument());
        testEventLog(2, "Deleted file testFile for Test");
    }

    private boolean testEventLog(int index, String logDescription) {
        List<Event> l = new ArrayList<Event>();
        EventLog el = EventLog.getInstance();
        for (Event next : el) {
            l.add(next);
        }
        return l.get(index).getDescription().equals(logDescription);
    }
}
