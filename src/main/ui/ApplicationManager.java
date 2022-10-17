package ui;

import model.Application;
import model.ApplicationList;
import model.Requirement;

import java.util.Scanner;

// Applications manager program
public class ApplicationManager {
    private Application gradApp1;
    private Application gradApp2;
    private Application jobApp;
    private Scanner input;

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
            displayMenu();
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
    private void processCommand(String command) {
        if (command.equals("a")) {
            addRequirement();
        } else if (command.equals("r")) {
            removeRequirement();
        } else if (command.equals("d")) {
            setDeadline();
        } else if (command.equals("c")) {
            setCategory();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes applications
    private void init() {
        gradApp1 = new Application("SciencesPo");
        gradApp2 = new Application("UBC");
        jobApp = new Application("EU Internship");
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> Add requirement");
        System.out.println("\tr -> Remove requirement");
        System.out.println("\td -> Set the deadline");
        System.out.println("\tc -> Set the category");
        System.out.println("\tq -> quit");
    }

    // MODIFIES: this
    // EFFECTS: adds a requirement to the application
    private void addRequirement() {
        Application selected = selectApplication();
        System.out.print("Enter the name of the new requirement: ");
        String name = input.next();
        Requirement newRequirement = new Requirement(name);
        selected.addRequirement(newRequirement);
        printAllRequirements(selected);
    }

    // MODIFIES: this
    // EFFECTS: removes a requirement from the application
    private void removeRequirement() {
        Application selected = selectApplication();
        System.out.print("Enter the name of the requirement to be removed: ");
        String name = input.next();

        for (Requirement req : selected.getRequiredDocuments()) {
            if (name.equals(req.getName())) {
                selected.removeRequirement(req);
                System.out.println("Requirement removed successfully");
                break;
            }
        }
        printAllRequirements(selected);
    }

    // MODIFIES: this
    // EFFECTS: set the deadline for the application
    private void setDeadline() {
        Application selected = selectApplication();
        System.out.print("Enter the deadline in the format mm-dd-yyyy h:m aa where h-hours, m-minutes, aa-AM or PM: ");
        String deadline = input.next();
        selected.setDeadline(deadline);
        System.out.println("The deadline set: ");
        selected.getDeadline();
    }

    // MODIFIES: this
    // EFFECTS: set the category for the application
    private void setCategory() {
        Application selected = selectApplication();
        System.out.print("Enter the category for the application: ");
        String category = input.next();
        selected.setCategory(category);
        System.out.println("The category set: ");
        selected.getCategory();
    }

    // EFFECTS: prompts user to select application and returns it
    private Application selectApplication() {
        String selection = "";  // force entry into loop

        while (!(selection.equals("s") || selection.equals("u") || selection.equals("e"))) {
            System.out.println("s for SciencePo application");
            System.out.println("u for UBC application");
            System.out.println("e for EU application");
            selection = input.next();
            selection = selection.toLowerCase();
        }

        if (selection.equals("s")) {
            return gradApp1;
        } else if (selection.equals("u")) {
            return gradApp2;
        } else {
            return jobApp;
        }
    }

    // EFFECTS: prints all requirements for an application to the screen
    private void printAllRequirements(Application selected) {
        for (Requirement req : selected.getRequiredDocuments()) {
            System.out.println(req.getName());
        }
    }
}
