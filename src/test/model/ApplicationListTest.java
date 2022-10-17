package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    }

    @Test
    void testConstructor() {
        assertEquals(0, testApplicationList.getApplicationList().size());
    }

    @Test
    void testAddApplication() {
        testApplicationList.addApplication(testApplication1);
        assertTrue(testApplicationList.getApplicationList().contains(testApplication1));
    }

    @Test
    void testAddMultipleApplications() {
        testApplicationList.addApplication(testApplication2);
        testApplicationList.addApplication(testApplication3);
        assertTrue(testApplicationList.getApplicationList().contains(testApplication2));
        assertTrue(testApplicationList.getApplicationList().contains(testApplication3));
    }

    @Test
    void testRemoveApplication() {
        testApplicationList.removeApplication(testApplication1);
        assertEquals(0, testApplicationList.getApplicationList().size());
        testApplicationList.addApplication(testApplication2);
        testApplicationList.addApplication(testApplication3);
        testApplicationList.removeApplication(testApplication2);
        assertFalse(testApplicationList.getApplicationList().contains(testApplication2));
        testApplicationList.removeApplication(testApplication3);
        assertEquals(0, testApplicationList.getApplicationList().size());
    }

    @Test
    void testSortByDeadlines(){
        testApplicationList.sortByDeadlines();
        assertTrue(testApplicationList.getApplicationList().isEmpty());

        testApplicationList.addApplication(testApplication1);
        testApplicationList.addApplication(testApplication2);
        testApplicationList.addApplication(testApplication3);
        testApplicationList.sortByDeadlines();
        assertEquals(testApplication1, testApplicationList.getApplicationList().get(0));
        assertEquals(testApplication2, testApplicationList.getApplicationList().get(1));
        assertEquals(testApplication3, testApplicationList.getApplicationList().get(2));

        testApplication1.setDeadline("04-20-2023 11:59 PM");
        testApplication2.setDeadline("07-25-2023 00:00 AM");
        testApplication3.setDeadline("06-01-2023 12:23 PM");
        testApplicationList.sortByDeadlines();
        assertEquals(testApplication1, testApplicationList.getApplicationList().get(0));
        assertEquals(testApplication3, testApplicationList.getApplicationList().get(1));
        assertEquals(testApplication2, testApplicationList.getApplicationList().get(2));
    }

    @Test
    void testFilterByCategory() {
        assertTrue(testApplicationList.filterByCategory("school").isEmpty());
        testApplication1.setCategory("school");
        testApplication2.setCategory("work");
        testApplication3.setCategory("school");
        List testFilteredList = new ArrayList<Application>();
        testFilteredList.add(testApplication1);
        testFilteredList.add(testApplication3);
        testApplicationList.addApplication(testApplication1);
        testApplicationList.addApplication(testApplication2);
        testApplicationList.addApplication(testApplication3);
        assertEquals(testFilteredList, testApplicationList.filterByCategory("school"));
        assertTrue(testApplicationList.filterByCategory("job").isEmpty());
    }

    @Test
    void testSearchByName() {
        assertEquals(null, testApplicationList.searchByName("Test Application 1"));

        testApplicationList.addApplication(testApplication1);
        testApplicationList.addApplication(testApplication2);
        assertEquals(testApplication1, testApplicationList.searchByName("Test Application 1"));
        assertEquals(testApplication2, testApplicationList.searchByName("Test Application 2"));

        assertEquals(null, testApplicationList.searchByName("Test Application 3"));
    }
}
