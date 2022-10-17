package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

// tests for the Application class
// Sources used:
//  Calendar class documentation - https://docs.oracle.com/javase/7/docs/api/java/util/Calendar.html
public class ApplicationTest {
    private Application testApplication;
    private Requirement testRequirement1 = new Requirement("Test Requirement 1");
    private Requirement testRequirement2 = new Requirement("Test Requirement 2");
    private Requirement testRequirement3 = new Requirement("Test Requirement 3");

    @BeforeEach
    void runBefore() {
        testApplication = new Application("Test1");
    }

    @Test
    void testConstructor() {
        assertEquals("Test1", testApplication.getName());
        assertFalse(testApplication.getStatus());
        assertEquals(0, testApplication.getProgress());
        assertEquals(0, testApplication.getRequiredDocuments().size());
    }

    @Test
    void testSetDeadline() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2022);
        cal.set(Calendar.MONTH, 10);
        cal.set(Calendar.DAY_OF_MONTH, 9);
        cal.set(Calendar.HOUR_OF_DAY, 11);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date testDate = cal.getTime();
        testApplication.setDeadline("11-09-2022 11:59 AM");
        assertEquals(testDate, testApplication.getDeadline());
    }

    @Test
    void testSetCategory() {
        testApplication.setCategory("work");
        assertEquals("work", testApplication.getCategory());
    }

    @Test
    void testAddRequirement() {
        testApplication.addRequirement(testRequirement1);
        assertTrue(testApplication.getRequiredDocuments().contains(testRequirement1));
    }

    @Test
    void testAddMultipleRequirements() {
        testApplication.addRequirement(testRequirement2);
        testApplication.addRequirement(testRequirement1);
        testApplication.addRequirement(testRequirement3);
        assertTrue(testApplication.getRequiredDocuments().contains(testRequirement2));
        assertTrue(testApplication.getRequiredDocuments().contains(testRequirement3));
        assertTrue(testApplication.getRequiredDocuments().contains(testRequirement1));
    }

    @Test
    void testRemoveRequirement() {
        testApplication.addRequirement(testRequirement1);
        testApplication.addRequirement(testRequirement2);
        testApplication.removeRequirement(testRequirement1);
        assertFalse(testApplication.getRequiredDocuments().contains(testRequirement1));
        assertTrue(testApplication.getRequiredDocuments().contains(testRequirement2));
        testApplication.removeRequirement(testRequirement2);
        assertFalse(testApplication.getRequiredDocuments().contains(testRequirement2));
    }

    @Test
    void testTrackStatusAndProgress() {
        testRequirement1.changeStatus(true);
        testApplication.addRequirement(testRequirement1);
        testApplication.trackStatusAndProgress();
        assertTrue(testApplication.getStatus());
        assertEquals(100, testApplication.getProgress());

        testApplication.addRequirement(testRequirement2);
        testApplication.trackStatusAndProgress();
        assertFalse(testApplication.getStatus());
        assertEquals(50, testApplication.getProgress());

        testApplication.addRequirement(testRequirement3);
        testApplication.trackStatusAndProgress();
        assertFalse(testApplication.getStatus());
        assertEquals(33, testApplication.getProgress());
    }

}
