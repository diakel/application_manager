package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Represents an application with a name, deadline, category, required documents, progress, and status
// Sources:
//     Parsing String to extract a date - https://stackoverflow.com/a/27580870

public class Application {
    private String name;                                // application's name
    private Date deadline;                              // the deadline of the application
    private String category;                            // the category of the application
    private List<Requirement> requiredDocuments;        // the list of the required documents for the application
    private int progress;                               // tracks the progress on the application (in percentages)
    private boolean status;                             // the status of the application: True is completed, False - not

    // REQUIRES: name can't have been used before
    // EFFECTS: constructs an application with a name, status == false, progress == 0 and no required documents
    public Application(String name) {
        this.name = name;
        status = false;
        progress = 0;
        requiredDocuments = new ArrayList<Requirement>();
    }

    public String getName() {
        return name;
    }

    public Boolean getStatus() {
        return status;
    }

    public int getProgress() {
        return progress;
    }

    // REQUIRES: the date must be entered in the format mm-dd-yyyy hh:mm aa where hh-hours, mm - minutes, aa - AM or PMM
    // MODIFIES: this
    // EFFECTS: sets and returns the deadline for the application
    public void setDeadline(String deadline) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm aa");
        try {
            this.deadline = dateFormat.parse(deadline);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date getDeadline() {
        return deadline;
    }

    // MODIFIES: this
    // EFFECTS: sets and returns the category of the application
    public void setCategory(String categoryName) {
        category = categoryName;
    }

    public String getCategory() {
        return category;
    }

    // MODIFIES: this
    // EFFECTS: adds a new requirement to the list of required documents for the application
    public void addRequirement(Requirement requirement) {
        requiredDocuments.add(requirement);
        trackStatusAndProgress();
    }

    // MODIFIES: this
    // EFFECTS: removes a requirement to the list of required documents for the application
    public void removeRequirement(Requirement requirement) {
        requiredDocuments.remove(requirement);
        trackStatusAndProgress();
    }

    public List<Requirement> getRequiredDocuments() {
        return requiredDocuments;
    }

    // MODIFIES: this
    // EFFECTS: counts the number of completed requirements in the list of requirements and based on this
    // calculates the overall progress on the application, changes the status to true if all the requirements are
    // fulfilled
    public void trackStatusAndProgress() {
        int completedReqs = 0;
        for (Requirement req:requiredDocuments) {
            if (req.getStatus()) {
                completedReqs++;
            }
        }
        if (completedReqs == requiredDocuments.size()) {
            status = true;
            progress = 100;
        } else {
            status = false;
            progress = (completedReqs * 100) / requiredDocuments.size();
        }
    }


}
