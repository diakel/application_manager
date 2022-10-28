package persistence;


import model.Application;
import model.ApplicationList;
import model.Requirement;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// tests for JsonReader
// based on: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
class JsonReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            ApplicationList appList = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyApplicationList() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyApplicationList.json");
        try {
            ApplicationList appList = reader.read();
            assertTrue(appList.getApplicationList().isEmpty());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralApplicationList() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralApplicationList.json");
        try {
            ApplicationList appList = reader.read();
            List<Application> applications = appList.getApplicationList();
            assertEquals(2, applications.size());
            assertEquals("SciencePo", applications.get(0).getName());
            assertEquals("Grad", applications.get(0).getCategory());
            assertFalse(applications.get(0).getStatus());
            assertEquals(50, applications.get(0).getProgress());

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, 2022);
            cal.set(Calendar.MONTH, 10);
            cal.set(Calendar.DAY_OF_MONTH, 11);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date testDeadline = cal.getTime();
            assertEquals(testDeadline, applications.get(0).getDeadline());

            List<Requirement> testRequirements = applications.get(0).getRequiredDocuments();
            assertEquals("Transcript", testRequirements.get(0).getName());
            assertEquals("Essay", testRequirements.get(1).getName());
            Path testFile = Paths.get("./data/testFile");
            assertEquals(testFile.toFile(), testRequirements.get(0).getUploadedDocument());
            Path testFile2 = Paths.get("./data/testFile2");
            assertEquals(testFile2.toFile(), testRequirements.get(1).getUploadedDocument());

            assertEquals("EU", applications.get(1).getName());
            assertEquals("Job", applications.get(1).getCategory());
            assertTrue(applications.get(1).getStatus());
            assertEquals(100, applications.get(1).getProgress());
            List<Requirement> testRequirements2 = applications.get(1).getRequiredDocuments();
            assertNull(testRequirements2.get(0).getUploadedDocument());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
