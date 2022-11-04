package persistence;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.stream.Stream;

import exceptions.AlreadyExistsException;
import exceptions.ApplicationAlreadyExistsException;
import model.Application;
import model.ApplicationList;
import model.Requirement;
import org.json.*;

// Represents a reader that reads Application List from JSON data stored in file
// Based on: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads list of applications from file and returns it;
    // throws IOException if an error occurs reading data from file
    public ApplicationList read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseApplicationList(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses ApplicationList from JSON object and returns it
    private ApplicationList parseApplicationList(JSONObject jsonObject) {
        //String name = jsonObject.getString("name");
        ApplicationList appList = new ApplicationList();
        addApplications(appList, jsonObject);
        return appList;
    }

    // MODIFIES: appList
    // EFFECTS: parses applications from JSON object and adds them to the list
    private void addApplications(ApplicationList appList, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("applications");
        for (Object json : jsonArray) {
            JSONObject nextApp = (JSONObject) json;
            addApplication(appList, nextApp);
        }
    }

    // MODIFIES: appList
    // EFFECTS: parses application from JSON object and adds it to workroom
    private void addApplication(ApplicationList appList, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Boolean status = jsonObject.getBoolean("status");
        int progress = jsonObject.getInt("progress");
        String category = jsonObject.getString("category");
        String deadline = jsonObject.getString("deadline");
        JSONArray jsonArray = jsonObject.getJSONArray("required documents");
        Application application = new Application(name);
        application.setCategory(category);
        try {
            application.setDeadline(deadline);
        } catch (ParseException e) {
            //System.out.println("Problem parsing application deadline");
        }
        application.setStatus(status);
        application.setProgress(progress);
        for (Object json : jsonArray) {
            JSONObject nextReq = (JSONObject) json;
            addRequirement(application, nextReq);
        }
        appList.addApplication(application);
    }

    // MODIFIES: application
    // EFFECTS: parses requirements from JSON object and adds it to the application
    private void addRequirement(Application app, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Boolean status = jsonObject.getBoolean("status");
        String document = jsonObject.getString("uploaded document");
        Requirement requirement = new Requirement(name);
        requirement.changeStatus(status);
        requirement.uploadDocument(document);
        app.addRequirement(requirement);
    }
}