package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// Represents a list of all applications
// Sources:
//     Sorting by dates - https://stackoverflow.com/a/5927408

public class ApplicationList {
    private List<Application> applicationList;  // list of the applications

    // EFFECTS: constructs an empty list of applications
    public ApplicationList() {
        applicationList = new ArrayList<Application>();
    }

    public List<Application> getApplicationList() {
        /*List<String> names = new ArrayList<String>();
        for (Application app : applicationList) {
            names.add(app.getName());
        }
        return names; */
        return applicationList;
    }

    // MODIFIES: this
    // EFFECTS: adds a new application to the list
    public void addApplication(Application app) {
        applicationList.add(app);
    }

    // REQUIRES: non-empty list of applications
    // MODIFIES: this
    // EFFECTS: removes an application from the list
    public void removeApplication(Application app) {
        applicationList.remove(app);
    }


    // REQUIRES: at least two applications with deadlines in the list of applications
    // MODIFIES: this
    // EFFECTS: sorts applications in the list by their deadlines from the earliest date to the latest
    public void sortByDeadlines() {
        Collections.sort(applicationList, (app1, app2) -> {
            if (app1.getDeadline() == null || app2.getDeadline() == null) {
                return 0;
            }
            return app1.getDeadline().compareTo(app2.getDeadline());
        });
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
}
