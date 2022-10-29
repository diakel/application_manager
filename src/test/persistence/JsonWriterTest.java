package persistence;

import exceptions.AlreadyExistsException;
import model.Application;
import model.ApplicationList;
import model.Requirement;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest {

    @Test
    void testWriterInvalidFile() {
        try {
            ApplicationList appList = new ApplicationList();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyApplicationList() {
        try {
            ApplicationList appList = new ApplicationList();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyApplicationList.json");
            writer.open();
            writer.write(appList);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyApplicationList.json");
            appList = reader.read();
            assertTrue(appList.getApplicationList().isEmpty());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralApplicationList() {
        try {
            ApplicationList appList = new ApplicationList();
            Application testApp1 = new Application("BCIT");
            testApp1.setCategory("College");
            testApp1.setStatus(true);
            try {
                testApp1.setDeadline("10-01-2023 11:00 AM");
            } catch (ParseException e) {
                System.out.println("This shouldn't be happening");
            }
            Requirement testReq1 = new Requirement("Some document");
            testReq1.changeStatus(true);
            testReq1.uploadDocument("./data/testFile");
            Requirement testReq2 = new Requirement("Another document");
            testReq2.changeStatus(true);
            testApp1.addRequirement(testReq1);
            testApp1.addRequirement(testReq2);
            Application testApp2 = new Application("UBC");
            testApp2.setStatus(false);

            appList.addApplication(testApp1);
            appList.addApplication(testApp2);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralApplicationList.json");
            writer.open();
            writer.write(appList);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralApplicationList.json");
            appList = reader.read();
            List<Application> testAppList = appList.getApplicationList();
            assertEquals(2, testAppList.size());
            assertEquals("BCIT", testAppList.get(0).getName());
            assertEquals(2, testAppList.get(0).getRequiredDocuments().size());
            assertEquals("UBC", testAppList.get(1).getName());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}