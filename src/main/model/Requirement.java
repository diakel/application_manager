package model;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// Represents a required document
// This class will have a method which would allow the user to upload and read files from their computers.
// That method will be implemented using JFileChooser API but for now there is only a basic functionality
// where the user has to manually type in the full path to a file
// Sources:
//    File I/O documentation: https://docs.oracle.com/javase/tutorial/essential/io/index.html
//    Opening files: https://stackoverflow.com/a/2547004
//                   https://docs.oracle.com/javase/7/docs/api/java/awt/Desktop.html

public class Requirement {
    private String name;             // name of the required document
    private Boolean status;          // status of the requirement: True - fulfilled, False - not
    private File uploadedDocument;   // keeps the document that the user might upload

    // EFFECTS: creates a required document with a given name, status == false, and no uploaded document
    public Requirement(String name) {
        this.name = name;
        status = false;
        uploadedDocument = null;
    }

    public String getName() {
        return name;
    }

    public Boolean getStatus() {
        return status;
    }

    public File getUploadedDocument() {
        return uploadedDocument;
    }

    // MODIFIES: this
    // EFFECTS: change the status of the requirement fulfillment; true - fulfilled, false - not
    public Boolean changeStatus(Boolean newStatus) {
        status = newStatus;
        return status;
    }

    // REQUIRES: the file exists and the path to the file is correct
    // MODIFIES: this
    // EFFECTS: takes the name of the path to a file and, if the file is fine, uploads this file and returns true
    public boolean uploadDocument(String pathName) {
        Path file = Paths.get(pathName);
        if (Files.isRegularFile(file) & Files.isReadable(file)) {
            uploadedDocument = new File(pathName);
            return true;
        } else {
            return false;
        }
    }

    // MODIFIES: this
    // EFFECTS: deletes the file from the app
    public void deleteUploadedDocument() {
        uploadedDocument = null;
    }

    // REQUIRES: there is an uploaded file
    // EFFECTS: opens the uploaded document using the associated application
    public void openUploadedDocument() {
        if (uploadedDocument != null) {
            try {
                Desktop.getDesktop().open(uploadedDocument);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
