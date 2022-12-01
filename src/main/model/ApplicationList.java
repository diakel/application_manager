package model;

import exceptions.ApplicationAlreadyExistsException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// Represents a list of all applications
// Sources:
//     Sorting by dates - https://stackoverflow.com/a/5927408

public class ApplicationList implements Writable {
    private List<Application> applicationList;  // list of the applications

    // EFFECTS: constructs an empty list of applications
    public ApplicationList() {
        applicationList = new ArrayList<Application>();
    }

    // MODIFIES: this
    // EFFECTS: adds a new application to the list, throws exception if an app with the same name is already in the list
    public void addApplication(Application application) throws ApplicationAlreadyExistsException {
        for (Application app : applicationList) {
            if (app.getName() == application.getName()) {
                throw new ApplicationAlreadyExistsException();
            }
        }
        applicationList.add(application);
    }

    // MODIFIES: this
    // EFFECTS: removes an application from the list
    public void removeApplication(Application app) {
        applicationList.remove(app);
    }

    // REQUIRES: all applications in the list must have deadlines
    // EFFECTS: returns a sorted by their deadlines list of applications from the earliest date to the latest
    public List<Application> sortByDeadlines() {
        List<Application> listToSort = new ArrayList<>();
        List<Application> appsWithoutDeadlines = new ArrayList<>();
        for (Application app : getApplicationList()) {
            if (app.getDeadline() != null) {
                listToSort.add(app);
            } else {
                appsWithoutDeadlines.add(app);
            }
        }
        Collections.sort(listToSort, Comparator.comparing(Application::getDeadline));
        listToSort.addAll(appsWithoutDeadlines);
        return listToSort;
    }

    // REQUIRES: non-empty list of applications
    // EFFECTS: returns a list of applications with the given category
    public List<Application> filterByCategory(String category) {
        List<Application> filteredList;
        filteredList = new ArrayList<Application>();
        for (Application app:applicationList) {
            if (app.getCategory().equals(category)) {
                filteredList.add(app);
            }
        }
        return filteredList;
    }

    // REQUIRES: non-empty list of applications
    // EFFECTS: returns an application with the given name or nothing if there is no such application
    public Application searchByName(String name) {
        for (Application app:applicationList) {
            if (app.getName().equals(name)) {
                return app;
            }
        }
        return null;
    }

    public List<Application> getApplicationList() {
        return applicationList;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("applications", applicationsToJson());
        return json;
    }

    // EFFECTS: returns applications in this list as a JSON array
    private JSONArray applicationsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Application app : applicationList) {
            jsonArray.put(app.toJson());
        }

        return jsonArray;
    }
}
