package model;

import exceptions.AlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationListTest {
    private ApplicationList testApplicationList;
    private Application testApplication1 = new Application("Test Application 1");
    private Application testApplication2 = new Application("Test Application 2");
    private Application testApplication3 = new Application("Test Application 3");

    @BeforeEach
    void runBefore() {
        testApplicationList = new ApplicationList();
        EventLog.getInstance().clear();
    }

    @Test
    void testConstructor() {
        assertEquals(0, testApplicationList.getApplicationList().size());
    }

    @Test
    void testAddApplication() {
        try {
            testApplicationList.addApplication(testApplication1);
        } catch (AlreadyExistsException e) {
            fail();
        }
        assertTrue(testApplicationList.getApplicationList().contains(testApplication1));
        List<Event> l = new ArrayList<Event>();
        EventLog el = EventLog.getInstance();
        for (Event next : el) {
            l.add(next);
        }
        assertTrue(l.get(1).getDescription().equals("Added application: Test Application 1"));
    }

    @Test
    void testAddMultipleApplications() {
        try {
            testApplicationList.addApplication(testApplication2);
            testApplicationList.addApplication(testApplication3);
        } catch (AlreadyExistsException e) {
            fail();
        }
        assertTrue(testApplicationList.getApplicationList().contains(testApplication2));
        assertTrue(testApplicationList.getApplicationList().contains(testApplication3));
        // Testing EventLog:
        List<Event> l = new ArrayList<Event>();
        for (Event next : EventLog.getInstance()) {
            l.add(next);
        }
        assertTrue(l.get(1).getDescription().equals("Added application: Test Application 2"));
        assertTrue(l.get(2).getDescription().equals("Added application: Test Application 3"));
    }

    @Test
    void testAddApplicationAlreadyExists() {
        try {
            testApplicationList.addApplication(testApplication2);
            testApplicationList.addApplication(testApplication2);
            fail();
        } catch (AlreadyExistsException e) {
            // To be expected
        }

        try {
            testApplicationList.addApplication(testApplication1);
            testApplicationList.addApplication(testApplication2);
            testApplicationList.addApplication(testApplication3);
            testApplicationList.addApplication(testApplication2);
            fail();
        } catch (AlreadyExistsException e) {
            // To be expected
        }
    }

    @Test
    void testRemoveApplication() {
        testApplicationList.removeApplication(testApplication1);
        assertEquals(0, testApplicationList.getApplicationList().size());
        try {
            testApplicationList.addApplication(testApplication2);
            testApplicationList.addApplication(testApplication3);
        } catch (AlreadyExistsException e) {
            fail();
        }
        testApplicationList.removeApplication(testApplication2);
        assertFalse(testApplicationList.getApplicationList().contains(testApplication2));
        testApplicationList.removeApplication(testApplication3);
        assertTrue(testApplicationList.getApplicationList().isEmpty());
        // Testing EventLog:
        List<Event> l = new ArrayList<Event>();
        for (Event next : EventLog.getInstance()) {
            l.add(next);
        }
        assertTrue(l.get(1).getDescription().equals("Removed application: Test Application 1"));
        assertTrue(l.get(2).getDescription().equals("Added application: Test Application 2"));
        assertTrue(l.get(3).getDescription().equals("Added application: Test Application 3"));
        assertTrue(l.get(4).getDescription().equals("Removed application: Test Application 2"));
        assertTrue(l.get(5).getDescription().equals("Removed application: Test Application 3"));
    }

    @Test
    void testSortByDeadlines(){
        List<Application> testSortedList = testApplicationList.sortByDeadlines();
        assertTrue(testSortedList.isEmpty());

        // No deadlines set yet
        try {
            testApplicationList.addApplication(testApplication1);
            testApplicationList.addApplication(testApplication2);
            testApplicationList.addApplication(testApplication3);
        } catch (AlreadyExistsException e) {
            fail();
        }
        testSortedList = testApplicationList.sortByDeadlines();
        assertEquals(testApplication1, testSortedList.get(0));
        assertEquals(testApplication2, testSortedList.get(1));
        assertEquals(testApplication3, testSortedList.get(2));

        // Not all applications have deadlines
        try {
            testApplication1.setDeadline("20-04-23 11:59 PM");
            testApplication3.setDeadline("01-06-23 12:23 PM");
        } catch (ParseException e) {
            fail();
        }
        testSortedList = testApplicationList.sortByDeadlines();
        assertEquals(testApplication1, testSortedList.get(0));
        assertEquals(testApplication3, testSortedList.get(1));
        assertEquals(testApplication2, testSortedList.get(2));

        // All applications have deadlines to be sorted by
        try {
            testApplication2.setDeadline("25-07-23 00:00 AM");
        } catch (ParseException e) {
            fail();
        }
        testSortedList = testApplicationList.sortByDeadlines();
        assertEquals(testApplication1, testSortedList.get(0));
        assertEquals(testApplication3, testSortedList.get(1));
        assertEquals(testApplication2, testSortedList.get(2));
    }

    @Test
    void testFilterByCategory() {
        assertTrue(testApplicationList.filterByCategory("school").isEmpty());
        testApplication1.setCategory("school");
        testApplication2.setCategory("work");
        testApplication3.setCategory("school");
        List testFilteredList = new ArrayList<Application>();
        try {
            testFilteredList.add(testApplication1);
            testFilteredList.add(testApplication3);
            testApplicationList.addApplication(testApplication1);
            testApplicationList.addApplication(testApplication2);
            testApplicationList.addApplication(testApplication3);
        } catch (AlreadyExistsException e) {
            fail();
        }
        assertEquals(testFilteredList, testApplicationList.filterByCategory("school"));
        assertEquals(testApplication2, testApplicationList.filterByCategory("work").get(0));
        assertTrue(testApplicationList.filterByCategory("job").isEmpty());
    }

    @Test
    void testSearchByName() {
        assertEquals(null, testApplicationList.searchByName("Test Application 1"));

        try {
            testApplicationList.addApplication(testApplication1);
            testApplicationList.addApplication(testApplication2);
        } catch (AlreadyExistsException e) {
            fail();
        }
        assertEquals(testApplication1, testApplicationList.searchByName("Test Application 1"));
        assertEquals(testApplication2, testApplicationList.searchByName("Test Application 2"));

        assertEquals(null, testApplicationList.searchByName("Test Application 3"));
    }
}
