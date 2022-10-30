package model;

import exceptions.AlreadyExistsException;
import exceptions.RequirementAlreadyExistsException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Represents an application with a name, deadline, category, required documents, progress, and status
// Sources:
//     Parsing String to extract a date - https://stackoverflow.com/a/27580870

public class Application implements Writable {
    private String name;                                // application's name
    private Date deadline;                              // the deadline of the application
    private String category;                            // the category of the application
    private List<Requirement> requiredDocuments;        // the list of the required documents for the application
    private int progress;                               // tracks the progress on the application (in percentages)
    private boolean status;                             // the status of the application: True is completed, False - not
    private String strDeadline = "";

    // REQUIRES: name can't have been used before
    // EFFECTS: constructs an application with a name, status == false, progress == 0 and no required documents
    public Application(String name) {
        this.name = name;
        status = false;
        progress = 0;
        requiredDocuments = new ArrayList<Requirement>();
        category = "";
    }

    // REQUIRES: the date must be entered in the format mm-dd-yyyy hh:mm aa where hh-hours, mm - minutes, aa - AM or PMM
    // MODIFIES: this
    // EFFECTS: sets and returns the deadline for the application
    public void setDeadline(String deadline) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm aa");
        if (!(deadline.isEmpty())) {
            this.deadline = dateFormat.parse(deadline);
        }
        strDeadline = deadline;
    }

    // MODIFIES: this
    // EFFECTS: sets and returns the category of the application
    public void setCategory(String categoryName) {
        category = categoryName;
    }

    // MODIFIES: this
    // EFFECTS: adds a new requirement to the list of required documents for the application
    public void addRequirement(Requirement requirement) throws AlreadyExistsException {
        for (Requirement req : requiredDocuments) {
            if (req.getName() == requirement.getName()) {
                throw new RequirementAlreadyExistsException();
            }
        }
        requiredDocuments.add(requirement);
        trackStatusAndProgress();
    }

    // MODIFIES: this
    // EFFECTS: removes a requirement to the list of required documents for the application
    public void removeRequirement(Requirement requirement) {
        requiredDocuments.remove(requirement);
        trackStatusAndProgress();
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

    // MODIFIES: this
    // EFFECTS: sets the status of the application
    public void setStatus(Boolean newStatus) {
        this.status = newStatus;
    }

    // MODIFIES: this
    // EFFECTS: sets the progress of the application
    public void setProgress(int progress) {
        this.progress = progress;
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

    public Date getDeadline() {
        return deadline;
    }

    public String getCategory() {
        return category;
    }

    public List<Requirement> getRequiredDocuments() {
        return requiredDocuments;
    }

    public String getStrDeadline() {
        return strDeadline;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("category", category);
        json.put("status", status);
        json.put("progress", progress);
        json.put("deadline", strDeadline);
        json.put("required documents", requirementsToJson());
        return json;
    }

    // EFFECTS: returns requirements in this list as a JSON array
    private JSONArray requirementsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Requirement req : requiredDocuments) {
            jsonArray.put(req.toJson());
        }

        return jsonArray;
    }
}
