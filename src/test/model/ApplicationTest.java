package model;

import exceptions.AlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
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
        try {
            testApplication.setDeadline("11-09-2022 11:59 AM");
        } catch (ParseException e) {
            fail();
        }
        assertEquals(testDate, testApplication.getDeadline());
    }

    @Test
    void testSetDeadlineWrong() {
        try {
            testApplication.setDeadline("11-09-2022");
            fail();
        } catch (ParseException e) {
            // To be expected
        }
    }

    @Test
    void testSetCategory() {
        testApplication.setCategory("work");
        assertEquals("work", testApplication.getCategory());
    }

    @Test
    void testAddRequirement() {
        try {
            testApplication.addRequirement(testRequirement1);
        } catch (AlreadyExistsException e){
            fail();
        }
        assertTrue(testApplication.getRequiredDocuments().contains(testRequirement1));
    }

    @Test
    void testAddMultipleRequirements() {
        try {
            testApplication.addRequirement(testRequirement2);
            testApplication.addRequirement(testRequirement1);
            testApplication.addRequirement(testRequirement3);
        } catch (AlreadyExistsException e) {
            fail();
        }
        assertTrue(testApplication.getRequiredDocuments().contains(testRequirement2));
        assertTrue(testApplication.getRequiredDocuments().contains(testRequirement3));
        assertTrue(testApplication.getRequiredDocuments().contains(testRequirement1));
    }

    @Test
    void testAddRequirementAlreadyExists() {
        try {
            testApplication.addRequirement(testRequirement1);
            testApplication.addRequirement(testRequirement1);
            fail();
        } catch (AlreadyExistsException e) {
            // Works as expected
        }

        try {
            testApplication.addRequirement(testRequirement1);
            testApplication.addRequirement(testRequirement2);
            testApplication.addRequirement(testRequirement3);
            testApplication.addRequirement(testRequirement1);
            fail();
        } catch (AlreadyExistsException e) {
            // Works as expected
        }

    }

    @Test
    void testRemoveRequirement() {
        try {
            testApplication.addRequirement(testRequirement1);
            testApplication.addRequirement(testRequirement2);
        } catch (AlreadyExistsException e) {
            fail();
        }
        testApplication.removeRequirement(testRequirement1);
        assertFalse(testApplication.getRequiredDocuments().contains(testRequirement1));
        assertTrue(testApplication.getRequiredDocuments().contains(testRequirement2));
        testApplication.removeRequirement(testRequirement2);
        assertFalse(testApplication.getRequiredDocuments().contains(testRequirement2));
    }

    @Test
    void testTrackStatusAndProgress() {
        testRequirement1.changeStatus(true);
        try {
            testApplication.addRequirement(testRequirement1);
        } catch (AlreadyExistsException e) {
            fail();
        }
        testApplication.trackStatusAndProgress();
        assertTrue(testApplication.getStatus());
        assertEquals(100, testApplication.getProgress());

        try {
            testApplication.addRequirement(testRequirement2);
        } catch (AlreadyExistsException e) {
            fail();
        }
        testApplication.trackStatusAndProgress();
        assertFalse(testApplication.getStatus());
        assertEquals(50, testApplication.getProgress());

        try {
            testApplication.addRequirement(testRequirement3);
        } catch (AlreadyExistsException e) {
            fail();
        }
        testApplication.trackStatusAndProgress();
        assertFalse(testApplication.getStatus());
        assertEquals(33, testApplication.getProgress());
    }

}
