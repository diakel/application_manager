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

package ui;

import model.Application;
import model.ApplicationList;
import model.Requirement;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

// Code sources: - https://docs.oracle.com/javase/tutorial/uiswing/components/list.html#mutable - how to work with lists + ListDemoProject
//               - https://stackoverflow.com/a/43533541 - adding elements to list model
//               - https://stackoverflow.com/a/23584594 - changing elements in another list according to actions in this one
//               - https://stackoverflow.com/a/44419681 - getting all elements of the list
// represents the list of all applications (on the right)
public class ApplicationListUI extends JPanel
                      implements ListSelectionListener {
    private JList list;
    private DefaultListModel listModel;
    private ApplicationList applicationList;
    private RequirementsUI requirementsList;

    private static final String addString = "Add application";
    private static final String removeString = "Remove application";
    private JButton removeButton;
    private JTextField applicationName;

    private JButton searchButton;
    private JMenu sortMenu;
    private JTextField searchName;
    JMenuBar sortMenuBar;

    public ApplicationListUI() {
        super(new BorderLayout());
        applicationList = new ApplicationList();
        Application sampleApp = new Application("Sample Application");
        applicationList.addApplication(sampleApp);

        listModel = new DefaultListModel();
        listModel.addElement(applicationList.getApplicationList().get(0));

        JScrollPane listScrollPane = getJScrollPane();

        requirementsList = new RequirementsUI(getSelectedApplication());

        JButton addButton = new JButton(addString);
        AddListener addListener = new AddListener(addButton);
        addButton.setActionCommand(addString);
        addButton.addActionListener(addListener);
        addButton.setEnabled(true);

        createRemoveButton();

        applicationName = new JTextField(10);
        applicationName.addActionListener(addListener);
        applicationName.getDocument().addDocumentListener(addListener);

        JPanel toolPanel = createToolPanel();

        JPanel buttonPane = getButtonPane(addButton);

        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
        add(toolPanel, BorderLayout.PAGE_START);
    }

    public void setApplicationList(ApplicationList appList) {
        applicationList = appList;
    }

    private void createRemoveButton() {
        removeButton = new JButton(removeString);
        removeButton.setActionCommand(removeString);
        removeButton.addActionListener(new RemoveListener());
    }

    // EFFECTS: constructs an upper panel with the search and sort functions
    private JPanel createToolPanel() {
        JPanel toolPanel = new JPanel(new GridLayout(1, 0));
        searchButton = new JButton("Search");
        SearchListener searchListener = new SearchListener(searchButton);
        searchButton.setActionCommand("Search");
        searchButton.addActionListener(searchListener);

        searchName = new JTextField(0);
        searchName.addActionListener(searchListener);
        searchName.getDocument().addDocumentListener(searchListener);

        sortMenuBar = new JMenuBar();
        sortMenu = createSortMenu();
        sortMenuBar.add(sortMenu);

        toolPanel.add(searchName);
        toolPanel.add(searchButton);
        toolPanel.add(sortMenuBar);

        return toolPanel;
    }

    private JMenu createSortMenu() {
        JMenuItem menuItem;
        SortListener sortListener = new SortListener();
        JMenu menu = new JMenu("Sort by");
        menuItem = new JMenuItem("deadlines");
        menuItem.addActionListener(sortListener);
        menu.add(menuItem);

        menuItem = new JMenuItem("original order");
        menuItem.addActionListener(sortListener);
        menu.add(menuItem);

        return menu;
    }

    // MODIFIES: this
    // EFFECTS: creates and returns button pane in the bottom of the pane
    private JPanel getButtonPane(JButton addButton) {
        //Create a panel that uses BoxLayout. (no)
        JPanel buttonPane = new JPanel();
  //      buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setLayout(new GridLayout(1, 0));
        buttonPane.add(removeButton);
 //       buttonPane.add(Box.createHorizontalStrut(5));
//        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
//        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(applicationName);
        buttonPane.add(addButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        return buttonPane;
    }

    private JScrollPane getJScrollPane() {
        //Create the list and put it in a scroll pane.
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        list.setCellRenderer(new MyListCellRenderer());
        JScrollPane listScrollPane = new JScrollPane(list);
        return listScrollPane;
    }

    public Application getSelectedApplication() {
        return (Application) list.getSelectedValue();
    }

    public RequirementsUI getRequirementsList() {
        return requirementsList;
    }

    public JList getJList() {
        return list;
    }

    public ApplicationList getApplicationList() {
        ApplicationList appList = new ApplicationList();
        for (int i = 0; i < list.getModel().getSize(); i++) {
            appList.addApplication((Application) list.getModel().getElementAt(i));
        }
        return appList;
    }

    class RemoveListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS: removes selected application, sets the index, disables remove button if there is nothing left
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = list.getSelectedIndex();
            Application appToRemove = getSelectedApplication();
            listModel = (DefaultListModel) list.getModel();
            listModel.remove(index);
            applicationList.removeApplication(appToRemove);

            int size = listModel.getSize();

            if (size == 0) { //Nobody's left, disable firing.
                removeButton.setEnabled(false);

            } else { //Select an index.
                if (index == listModel.getSize()) {
                    //removed item in last position
                    index--;
                }

                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }
    }

    //This listener is shared by the text field and the add button.
    class AddListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        public AddListener(JButton button) {
            this.button = button;
        }

        //Required by ActionListener.
        // MODIFIES: this
        // EFFECTS: adds a new application in the appropriate place and to the application list
        public void actionPerformed(ActionEvent e) {
            String name = applicationName.getText();

            //User didn't type in a unique name...
            if (name.equals("") || alreadyInList(name)) {
                Toolkit.getDefaultToolkit().beep();
                applicationName.requestFocusInWindow();
                applicationName.selectAll();
                return;
            }

            int index = list.getSelectedIndex(); //get selected index
            if (index == -1) { //no selection, so insert at beginning
                index = 0;
            } else {           //add after the selected item
                index++;
            }

            Application newApp = new Application(applicationName.getText());
            listModel = (DefaultListModel) list.getModel();
            listModel.insertElementAt(newApp, index);
            //If we just wanted to add to the end, we'd do this:
            //listModel.addElement(employeeName.getText());
            applicationList.addApplication(newApp);

            //Reset the text field.
            applicationName.requestFocusInWindow();
            applicationName.setText("");

            //Select the new item and make it visible.
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }

        // EFFECTS: tests for string equality
        protected boolean alreadyInList(String name) {
            return ((DefaultListModel) list.getModel()).contains(name);
        }

        //Required by DocumentListener.
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        //Required by DocumentListener.
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        //Required by DocumentListener.
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }

    //This listener is shared by the text field and the add button.
    class SearchListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        public SearchListener(JButton button) {
            this.button = button;
        }

        //Required by ActionListener.
        // MODIFIES: this
        // EFFECTS: searches for the application with the given name or category
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Search")) {
                String name = searchName.getText();

                //User didn't type in anything
                if (name.equals("")) {
                    Toolkit.getDefaultToolkit().beep();
                    searchName.requestFocusInWindow();
                    searchName.selectAll();
                    return;
                }

                listModel = (DefaultListModel) list.getModel();
                filterApplicationList(name);
                searchButton.setText("Go back");
                searchButton.setActionCommand("Return");
            } else {
                list.setModel(listModel);
                searchButton.setText("Search");
                searchButton.setActionCommand("Search");
            }

            setIndex();
        }

        private void setIndex() {
            int index = list.getSelectedIndex(); //get selected index
            if (index == -1) { //no selection, so insert at beginning
                index = 0;
            } else {           //add after the selected item
                index++;
            }

            //Reset the text field.
            searchName.requestFocusInWindow();
            searchName.setText("");

            //Select the new item and make it visible.
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }

        private void filterApplicationList(String name) {
            DefaultListModel filteredList = new DefaultListModel();
            for (Application app : applicationList.getApplicationList()) {
                if (app.getName().contains(name) || app.getCategory().contains(name)) {
                    filteredList.addElement(app);
                }
            }

            list.setModel(filteredList);

        }

        //Required by DocumentListener.
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        //Required by DocumentListener.
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        //Required by DocumentListener.
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0 && button.getActionCommand().equals("Search")) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }

    class SortListener implements ActionListener {
        // MODIFIES: this
        // EFFECTS: sorts applications by deadlines or returns original order
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("deadlines")) {
                listModel = (DefaultListModel) list.getModel();
                sortByDeadlines();
            } else {
                list.setModel(listModel);
            }

            int index = list.getSelectedIndex();
            int size = listModel.getSize();
            if (size == 0) { //Nobody's left, disable firing.
                removeButton.setEnabled(false);

            } else { //Select an index.
                if (index == listModel.getSize()) {
                    //removed item in last position
                    index--;
                }

                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }

        // EFFECTS: orders applications by their deadlines
        private void sortByDeadlines() {
            DefaultListModel sortedModel = new DefaultListModel();
            for (Application app : applicationList.sortByDeadlines()) {
                sortedModel.addElement(app);
            }
            list.setModel(sortedModel);
        }
    }

    //This method is required by ListSelectionListener.
    // EFFECTS: sets the appropriate requirements for the selected application, enables/disables remove button
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            if (list.getSelectedIndex() == -1) {
            //No selection, disable fire button.
                removeButton.setEnabled(false);

            } else {
            //Selection, enable the fire button.
                removeButton.setEnabled(true);
            }
        }
        DefaultListModel appropriateReqModel = new DefaultListModel<>();
        if (getSelectedApplication() != null) {
            requirementsList.changeSelectedApplication(getSelectedApplication());
            if (!getSelectedApplication().getRequiredDocuments().isEmpty()) {
                for (int i = 0; i < getSelectedApplication().getRequiredDocuments().size(); i++) {
                    appropriateReqModel.addElement(getSelectedApplication().getRequiredDocuments().get(i));
                }
            }
            if (getSelectedApplication().getDeadline() != null) {
                requirementsList.changeSpinnerDate(getSelectedApplication().getDeadline());
            }
        }
        requirementsList.getJList().setModel(appropriateReqModel);
        requirementsList.trackProgress();
    }

    class MyListCellRenderer extends DefaultListCellRenderer implements ListCellRenderer<Object> {

        public MyListCellRenderer() {
            setOpaque(true);
        }

        // EFFECTS: highlights completed requirements green
        public Component getListCellRendererComponent(JList paramList, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            if (!((Application) value).getStrDeadline().isEmpty()) {
                setText(value + " " + ((Application) value).getStrDeadline());
            } else {
                setText(value.toString());
            }
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            return this;
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Application Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new ApplicationListUI();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
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
