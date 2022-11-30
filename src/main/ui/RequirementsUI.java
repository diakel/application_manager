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
import model.Requirement;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// Class for the list of requirements pane (on the right ) for the selected application (on the left)
// Code sources:   - https://www.geeksforgeeks.org/java-swing-jprogressbar/ - progress bar
//                 - https://docs.oracle.com/javase/tutorial/uiswing/components/menu.html#popup - popup menu (+ PopupMenuDemo)
//                 - https://stackoverflow.com/a/6007967 - popup menu for JList
//                 - https://stackoverflow.com/q/58385949, https://stackoverflow.com/a/1665237 - working with custom cell renderers
//                 - https://docs.oracle.com/javase/tutorial/uiswing/components/spinner.html, https://www.tutorialspoint.com/how-to-create-a-date-spinner-in-java -
//                                                                                               JSpinner code
//                 - https://stackoverflow.com/a/18705499 - getting dates from JSpinner
//                 + code sources in the ApplicationListUI class
public class RequirementsUI extends JPanel
                      implements ListSelectionListener {
    private JList list;
    private DefaultListModel listModel;
    private Application selectedApplication;

    JProgressBar progressBar;
    private static final String addString = "Add requirement";
    private static final String removeString = "Remove requirement";
    private JButton removeButton;
    private JTextField requirementName;

    private PopupMenu popup;

    protected Calendar calendar;
    protected JSpinner dateSpinner;

    public RequirementsUI(Application selectedApp) {
        super(new BorderLayout());
        selectedApplication = selectedApp;
        setupRequirements();

        JScrollPane listScrollPane = getJScrollPane();

        JButton addButton = new JButton(addString);
        AddListener addListener = new AddListener(addButton);
        addButton.setActionCommand(addString);
        addButton.addActionListener(addListener);
        addButton.setEnabled(true);

        removeButton = new JButton(removeString);
        removeButton.setActionCommand(removeString);
        removeButton.addActionListener(new RemoveListener());

        requirementName = new JTextField(10);
        requirementName.addActionListener(addListener);
        requirementName.getDocument().addDocumentListener(addListener);

        JPanel informationPanel = getCompletionPanel();

        // Popup Menu
        popup = new PopupMenu();
        popup.createPopupMenu();

        JPanel buttonPane = getButtonPane(addButton);

        add(informationPanel, BorderLayout.PAGE_START);
        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
    }

    // MODIFIES: listModel
    // EFFECTS: adds elements to the requirement list based on the selected application
    private void setupRequirements() {
        if (selectedApplication.getRequiredDocuments().isEmpty()) {
            Requirement sampleRequirement = new Requirement("Sample Requirement");
            selectedApplication.addRequirement(sampleRequirement);
        }

        listModel = new DefaultListModel();
        for (int i = 0; i < selectedApplication.getRequiredDocuments().size(); i++) {
            listModel.addElement(selectedApplication.getRequiredDocuments().get(i));
        }
    }

    // EFFECTS: constructs an upper panel with the progress bar and deadline setter
    private JPanel getCompletionPanel() {
        // Progress bar
        JPanel completionPanel = new JPanel(new GridLayout(0, 1));
        JLabel dateLabel = new JLabel("You can set the deadline for this application (dd/mm/yy hh:mm): ");
        progressBar = new JProgressBar();
        progressBar.setString("Progress on the application");
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
//        dateLabel.setLabelFor(dateSpinner);
        SpinnerDate deadlineSpinner = new SpinnerDate();
        completionPanel.add(progressBar);
        completionPanel.add(dateLabel);
        completionPanel.add(deadlineSpinner.getDateSpinner());
        trackProgress();
        return completionPanel;
    }

    // EFFECTS: constructs and returns button pane
    private JPanel getButtonPane(JButton addButton) {
        //Create a panel that uses BoxLayout. (no)
        JPanel buttonPane = new JPanel();
   //     buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setLayout(new GridLayout(1, 0));
        buttonPane.add(removeButton);
     //   buttonPane.add(Box.createHorizontalStrut(5));
    //    buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
     //   buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(requirementName);
        buttonPane.add(addButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        return buttonPane;
    }

    // EFFECTS: construct the list and puts it on the JScrollPane
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

    // MODIFIES: progressBar
    // EFFECTS: function to increase progress on the progress bar
    public void trackProgress() {
        int numCompleted = 0;
        for (int i = 0; i < list.getModel().getSize(); i++) {
            if (((Requirement) (list.getModel().getElementAt(i))).getStatus()) {
                numCompleted++;
            }
        }
        try {
            if (list.getModel().getSize() != 0) {
                progressBar.setValue((numCompleted * 100) / list.getModel().getSize());
            } else {
                progressBar.setValue(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error with the progress bar");
        }
    }

    public JList getJList() {
        return list;
    }

    // MODIFIES: selectedApplication
    // EFFECTS: changes the selected app
    public void changeSelectedApplication(Application app) {
        selectedApplication = app;
    }

    public void changeSpinnerDate(Date date) {
        dateSpinner.setValue(date);
    }

    public class SpinnerDate extends JPanel
            implements ChangeListener {

        private void createDateSpinner() {
            Date today = new Date();
            SpinnerDateModel dateModel;
            dateModel = new SpinnerDateModel(today, null, null, Calendar.MONTH);
            dateSpinner = new JSpinner(dateModel);
            JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yy HH:mm aa");
            dateSpinner.setEditor(editor);
        }

        public SpinnerDate() {
            calendar = Calendar.getInstance();
            createDateSpinner();

            //Listen for changes on the date spinner.
            dateSpinner.addChangeListener(this);
            dateSpinner.setVisible(true);
        }

        public JSpinner getDateSpinner() {
            return dateSpinner;
        }

        public void stateChanged(ChangeEvent e) {
            SpinnerModel dateModel = dateSpinner.getModel();
            if (dateModel instanceof SpinnerDateModel) {
                setDeadline(((SpinnerDateModel) dateModel).getDate());
            }
        }

        protected void setDeadline(Date date) {
            calendar.setTime(date);
            selectedApplication.setDeadline(date);
        }
    }

    class RemoveListener implements ActionListener {
        // MODIFIES: this
        // EFFECTS: removes selected requirement, selects new index, disables removal if nothing's left
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = list.getSelectedIndex();
            Requirement reqToRemove = (Requirement) list.getSelectedValue();
            listModel = (DefaultListModel) list.getModel();
            listModel.remove(index);
            selectedApplication.removeRequirement(reqToRemove);
            trackProgress();

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

    //This listener is shared by the text field and the hire button.
    class AddListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        public AddListener(JButton button) {
            this.button = button;
        }

        //Required by ActionListener.
        // MODIFIES: this
        // EFFECTS: adds a new requirement in the appropriate place in the list
        public void actionPerformed(ActionEvent e) {
            String name = requirementName.getText();

            //User didn't type in a unique name...
            if (name.equals("") || alreadyInList(name)) {
                Toolkit.getDefaultToolkit().beep();
                requirementName.requestFocusInWindow();
                requirementName.selectAll();
                return;
            }

            int index = list.getSelectedIndex(); //get selected index
            if (index == -1) { //no selection, so insert at beginning
                index = 0;
            } else {           //add after the selected item
                index++;
            }

            Requirement newRequirement = new Requirement(requirementName.getText());
            listModel = (DefaultListModel) list.getModel();
            listModel.insertElementAt(newRequirement, index);
            selectedApplication.addRequirement(newRequirement);
            trackProgress();
            //If we just wanted to add to the end, we'd do this:
            //listModel.addElement(employeeName.getText());

            //Reset the text field.
            requirementName.requestFocusInWindow();
            requirementName.setText("");

            //Select the new item and make it visible.
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }

        // EFFECTS: checks whether a requirement with such name is already in the list
        protected boolean alreadyInList(String name) {
            listModel = (DefaultListModel) list.getModel();
            return listModel.contains(name);
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

    //This method is required by ListSelectionListener.
    // EFFECTS: disables/enables buttons depending on whether there is a selection
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
    }

    class MyListCellRenderer extends DefaultListCellRenderer implements ListCellRenderer<Object> {

        public MyListCellRenderer() {
            setOpaque(true);
        }

        // EFFECTS: highlights completed requirements green
        public Component getListCellRendererComponent(JList paramList, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            setText(value.toString());
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            if (((Requirement) value).getStatus()) {
                setBackground(Color.GREEN);
            }
            return this;
        }
    }

    class PopupMenu implements ActionListener {

        // MODIFIES: this
        // EFFECTS: creates a popup menu for a requirement
        public void createPopupMenu() {
            JMenuItem menuItem;

            //Create the popup menu.
            JPopupMenu popup = new JPopupMenu();
            menuItem = new JMenuItem("Completed");
            menuItem.addActionListener(this);
            popup.add(menuItem);
            menuItem = new JMenuItem("Incomplete");
            menuItem.addActionListener(this);
            popup.add(menuItem);
            //Add listener to the list so the popup menu can come up.
            MouseListener popupListener = new PopupListener(popup);
            list.addMouseListener(popupListener);
        }

        // MODIFIES: this
        // EFFECTS: changes the status to completed/incomplete depending on the button pressed
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Completed")) {
                ((Requirement) list.getSelectedValue()).changeStatus(true);
                selectedApplication.getRequirement((Requirement) list.getSelectedValue()).changeStatus(true);
                selectedApplication.trackStatusAndProgress();
                trackProgress();
            } else {
                ((Requirement) list.getSelectedValue()).changeStatus(false);
                selectedApplication.getRequirement((Requirement) list.getSelectedValue()).changeStatus(false);
                selectedApplication.trackStatusAndProgress();
                trackProgress();
            }
        }

        class PopupListener extends MouseAdapter {
            JPopupMenu popup;

            PopupListener(JPopupMenu popupMenu) {
                popup = popupMenu;
            }

            public void mousePressed(MouseEvent e) {
                showPopup(e);
            }

            public void mouseReleased(MouseEvent e) {
                showPopup(e);
            }

            // EFFECTS: shows the menu upon pressing the right button
            private void showPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    list.setSelectedIndex(list.locationToIndex(e.getPoint())); //select the item
                    popup.show(list, e.getX(), e.getY()); //and show the menu
                }
            }
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
        JComponent newContentPane = new RequirementsUI(new Application("Sample"));
        newContentPane.setOpaque(true); //content panes must be opaque

        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
