package ui;

/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import model.Application;
import model.ApplicationList;
import model.Event;
import model.EventLog;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.*;

// Code sources: - https://docs.oracle.com/javase/tutorial/uiswing/components/menu.html - menus + MenuDemo app
//               - https://stackoverflow.com/a/6578266 - event handling
//               - https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html - Dialog windows
//               - https://docs.oracle.com/javase/tutorial/uiswing/components/splitpane.html - split panes
//               - https://stackoverflow.com/a/60516801 - printing to console before closing the application
//                             + SplitPaneDemo2Project, SplitPaneDividerDemoProject
public class ApplicationManagerUI extends JPanel {
    private JSplitPane splitPane;
    JComponent applicationListPane;
    JComponent requirementsPane;
    MenuUI menuBar;
    private static final String JSON_STORE = "./data/applicationlist.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    public ApplicationManagerUI() {

        //Create a split pane with the two scroll panes in it.
        applicationListPane = new ApplicationListUI();
        requirementsPane = ((ApplicationListUI) applicationListPane).getRequirementsList();
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                applicationListPane, requirementsPane);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(500);
        splitPane.setResizeWeight(0.5D);


        //Provide minimum sizes for the two components in the split pane.
        Dimension minimumSize = new Dimension(100, 50);
        applicationListPane.setMinimumSize(minimumSize);
        requirementsPane.setMinimumSize(minimumSize);

        //Provide a preferred size for the split pane.
        splitPane.setPreferredSize(new Dimension(700, 200));
        // updateLabel(imageNames[list.getSelectedIndex()]);

        menuBar = new MenuUI();

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    public JSplitPane getSplitPane() {
        return splitPane;
    }

    public JMenuBar getMenuBar() {
        return menuBar.createMenuBar();
    }

    private class MenuUI implements ActionListener {

        // MODIFIES: this
        // EFFECTS: creates a menu bar
        public JMenuBar createMenuBar() {
            JMenuBar menuBar;
            JMenu menu;
            JMenuItem menuItem;

            //Create the menu bar.
            menuBar = new JMenuBar();

            //Build the menu.
            menu = new JMenu("Menu");
            menuBar.add(menu);

            //a group of JMenuItems
            menuItem = new JMenuItem("Save");
            menuItem.setMnemonic(KeyEvent.VK_S);
            menu.getAccessibleContext().setAccessibleDescription("Save current state of the application manager");
            menuItem.addActionListener(this);
            menu.add(menuItem);

            menuItem = new JMenuItem("Load");
            menuItem.setMnemonic(KeyEvent.VK_L);
            menu.getAccessibleContext().setAccessibleDescription("Load last saved session");
            menuItem.addActionListener(this);
            menu.add(menuItem);

            return menuBar;
        }

        // MODIFIES: this
        // EFFECTS: calls appropriate method depending on the button pressed
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Save")) {
                saveApplications();
            } else {
                loadApplications();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: loads application from the json file
    private void loadApplications() {
        try {
            ApplicationList appList = jsonReader.read();
            ((ApplicationListUI) applicationListPane).setApplicationList(appList);
            DefaultListModel loadedApplicationList = new DefaultListModel<>();
            if (!appList.getApplicationList().isEmpty()) {
                for (int i = 0; i < appList.getApplicationList().size(); i++) {
                    loadedApplicationList.addElement(appList.getApplicationList().get(i));
                }
            }
            ((ApplicationListUI) applicationListPane).getJList().setModel(loadedApplicationList);
            JOptionPane.showMessageDialog(splitPane, "Loaded the application list" + " from " + JSON_STORE);
        } catch (IOException fe) {
            JOptionPane.showMessageDialog(splitPane, "Unable to read from file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: writes the state of the application to the json file
    private void saveApplications() {
        try {
            jsonWriter.open();
            jsonWriter.write(((ApplicationListUI) applicationListPane).getApplicationList());
            jsonWriter.close();
            JOptionPane.showMessageDialog(splitPane, "Saved current application list" + " to " + JSON_STORE);
        } catch (FileNotFoundException fe) {
            JOptionPane.showMessageDialog(splitPane, "Unable to write to file: " + JSON_STORE);
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Application Manager");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        // EFFECTS: when the user closes the window, print event log to console and then closes the application
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                for (Event next : EventLog.getInstance()) {
                    System.out.println(next.toString() + "\n");
                }
                System.exit(0);
            }
        });
        ApplicationManagerUI applicationManagerUI = new ApplicationManagerUI();
        frame.getContentPane().add(applicationManagerUI.getSplitPane());

        //Add a menu to the frame
        frame.setJMenuBar(applicationManagerUI.getMenuBar());

        //Display the window.
        frame.setPreferredSize(new Dimension(1000, 700));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
