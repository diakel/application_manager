package ui;

import exceptions.AlreadyExistsException;
import exceptions.ApplicationAlreadyExistsException;
import model.Application;
import model.ApplicationList;
import model.Requirement;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Applications manager console program
public class ApplicationManager {
    private static final String JSON_STORE = "./data/applicationlist.json";
    private ApplicationList appList;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the application
    public ApplicationManager() {
        runManager();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    void runManager() {
        boolean keepGoing = true;
        String command = null;

        init();

        while (keepGoing) {
            displayMainMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    @SuppressWarnings("methodlength")
    private void processCommand(String command) {
        switch (command) {
            case "a": addApplication();
                break;
            case "r": removeApplication();
                break;
            case "ar": addRequirement();
                break;
            case "rr": removeRequirement();
                break;
            case "ad": addDocument();
                break;
            case "o": openDocument();
                break;
            case "d": setDeadline();
                break;
            case "c": setCategory();
                break;
            case "s": sortByDeadlines();
                break;
            case "f": filterByCategory();
                break;
            case "save": saveApplicationList();
                break;
            case "load": loadApplicationList();
                break;
            default: System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes applications
    private void init() {
        appList = new ApplicationList();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // EFFECTS: displays main menu of options to user
    private void displayMainMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> Add application");
        System.out.println("\tr -> Remove application");
        System.out.println("\tar -> Add requirement for an application");
        System.out.println("\trr -> Remove requirement for an application");
        System.out.println("\tad -> Add a document to an application requirement");
        System.out.println("\to -> Open a document for a requirement");
        System.out.println("\td -> Set a deadline for an application");
        System.out.println("\tc -> Set a category for an application");
        System.out.println("\ts -> Sort applications by the deadlines");
        System.out.println("\tf -> Filter applications by category");
        System.out.println("\tsave -> save application list to file");
        System.out.println("\tload -> load application list from file");
        System.out.println("\tq -> quit");
    }

    // MODIFIES: this
    // EFFECTS: adds a new application to the list of applications
    private void addApplication() {
        System.out.print("Enter the name of the new application: ");
        String name = input.next();
        Application newApp = new Application(name);
        try {
            appList.addApplication(newApp);
        } catch (ApplicationAlreadyExistsException e) {
            System.out.println("Application with such name already exists");
            return;
        }
        printAllApplications();
    }

    // MODIFIES: this
    // EFFECTS: removes an application from the list of applications
    private void removeApplication() {
        System.out.print("Enter the name of the application to be removed: ");
        String name = input.next();
        for (Application app : appList.getApplicationList()) {
            if (name.equals(app.getName())) {
                appList.removeApplication(app);
                System.out.println("Application removed successfully");
                break;
            }
        }
        printAllApplications();
    }

    // MODIFIES: this
    // EFFECTS: adds a requirement to the chosen application
    private void addRequirement() {
        Application selected = null;
        selected = selectApplication();
        System.out.print("Enter the name of the new requirement: ");
        String name = input.next();
        Requirement newRequirement = new Requirement(name);
        try {
            selected.addRequirement(newRequirement);
        } catch (AlreadyExistsException e) {
            System.out.println("Requirement with such name already exists");
            return;
        }
        printAllRequirements(selected);
    }

    // MODIFIES: this
    // EFFECTS: removes a requirement from the chosen application
    private void removeRequirement() {
        Application selected = null;
        selected = selectApplication();
        System.out.print("Enter the name of the requirement to be removed: ");
        String name = input.next();

        for (Requirement req : selected.getRequiredDocuments()) {
            if (name.equals(req.getName())) {
                selected.removeRequirement(req);
                System.out.println("Requirement removed successfully");
                break;
            }
        }
        System.out.println("No such requirement exists for the chosen application");
        printAllRequirements(selected);
    }

    // MODIFIES: this
    // EFFECTS: uploads a document to the requirement
    private void addDocument() {
        Application selected = null;
        selected = selectApplication();
        Requirement selectedReq = null;
        selectedReq = selectRequirement(selected);
        System.out.println("Type in the path to the document you want to upload");
        String path = input.next();
        if (selectedReq.uploadDocument(path)) {
            System.out.println("The document was added successfully");
        } else {
            System.out.println("There was a problem uploading the document. Check the path and the type of the file");
        }
    }

    // EFFECTS: opens a document for the requirement
    private void openDocument() {
        Application selected = null;
        selected = selectApplication();
        Requirement selectedReq = null;
        selectedReq = selectRequirement(selected);
        try {
            selectedReq.openUploadedDocument();
        } catch (IOException e) {
            System.out.println("Problem opening the document");
        }
    }

    // MODIFIES: this
    // EFFECTS: "deletes" the document for a requirement
    private void removeDocument() {
        Application selected = null;
        selected = selectApplication();
        Requirement selectedReq = null;
        selectedReq = selectRequirement(selected);
        selectedReq.deleteUploadedDocument();
        System.out.println("The document removed successfully");
    }

    // MODIFIES: this
    // EFFECTS: set the deadline for the application
    private void setDeadline() {
        String strDate;
        Application selected = null;
        selected = selectApplication();
        System.out.print("Enter the deadline in the format mm-dd-yyyy h:m aa where h-hours, m-minutes, aa-AM or PM: ");
        String deadline = input.next();
        try {
            selected.setDeadline(deadline);
        } catch (ParseException e) {
            System.out.println("Wrong format");
            return;
        }
        strDate = selected.getDeadline().toString();
        System.out.println("The deadline set: " + strDate);
    }

    // MODIFIES: this
    // EFFECTS: set the category for the application
    private void setCategory() {
        Application selected = null;
        selected = selectApplication();
        System.out.print("Enter the category for the application: ");
        String category = input.next();
        selected.setCategory(category);
        System.out.println("The category set: " + selected.getCategory());
    }

    // MODIFIES: this
    // EFFECTS: sorts applications by their deadlines
    private void sortByDeadlines() {
        appList.sortByDeadlines();
    }

    // EFFECTS: returns all applications in the given category
    private void filterByCategory() {
        System.out.print("Enter the category to filter by: ");
        String category = input.next();
        List<Application> filteredList;
        filteredList = appList.filterByCategory(category);
        if (filteredList.isEmpty()) {
            System.out.println("No applications in the " + category + " category");
        } else {
            filteredList.forEach(a -> {
                System.out.println(a.getName());
            });
        }
    }

    // EFFECTS: prompts user to type in the name of the application and returns it
    private Application selectApplication() {
        if (appList.getApplicationList().isEmpty()) {
            System.out.println("No applications have been added yet");
            return null;
        } else {
            String selection = "";  // force entry into loop

            while (appList.searchByName(selection) == null) {
                System.out.println("Type in the name of one of the applications in the list");
                printAllApplications();
                selection = input.next();
            }

            return appList.searchByName(selection);
        }
    }

    // EFFECTS: prompts user to type in the name of the requirement and returns it
    private Requirement selectRequirement(Application app) {
        if (app.getRequiredDocuments().isEmpty()) {
            System.out.println("No requirements have been added yet");
            return null;
        } else {
            List<String> requirementNames = new ArrayList<String>();
            app.getRequiredDocuments().forEach(req -> {
                requirementNames.add(req.getName());
            });

            String selection = "";  // force entry into loop
            while (!(requirementNames.contains(selection))) {
                System.out.println("Type in the name of one of the application requirements in the list");
                printAllRequirements(app);
                selection = input.next();
            }

            return getRequirementByName(app, selection);
        }
    }

    private Requirement getRequirementByName(Application app, String name) {
        for (Requirement req:app.getRequiredDocuments()) {
            if (req.getName().equals(name)) {
                return req;
            }
        }
        return null;
    }

    // EFFECTS: prints all requirements for an application to the screen
    private void printAllRequirements(Application selected) {
        for (Requirement req : selected.getRequiredDocuments()) {
            System.out.println(req.getName());
        }
    }

    // EFFECTS: prints the names of all the applications
    private void printAllApplications() {
        for (Application app : appList.getApplicationList()) {
            System.out.println(app.getName());
        }
    }

    // EFFECTS: saves the application list to file
    private void saveApplicationList() {
        try {
            jsonWriter.open();
            jsonWriter.write(appList);
            jsonWriter.close();
            System.out.println("Saved current application list" + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads application list from file
    private void loadApplicationList() {
        try {
            appList = jsonReader.read();
            System.out.println("Loaded the application list" + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}