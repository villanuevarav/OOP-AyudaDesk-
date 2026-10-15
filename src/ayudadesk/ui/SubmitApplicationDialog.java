package ayudadesk.ui;

import ayudadesk.model.AidApplication;
import ayudadesk.model.AppData;
import ayudadesk.model.Applicant;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

// FEATURE 3: EMERGENCY AID SUBMISSION WITH COMPLETENESS CHECK (GUI)
// Follows the flowchart: fill form -> check completeness ->
// (incomplete: return to staff for correction) -> check applicant type -> pending
public class SubmitApplicationDialog extends JDialog {
    private AppData data;

    private JComboBox<String> applicantBox;
    private JLabel infoLabel;
    private JTextField amountField;

    private int row = 0; // current row while building the form

    public SubmitApplicationDialog(Window parent, AppData data) {
        super(parent, "Submit Emergency Aid Application", Dialog.ModalityType.APPLICATION_MODAL);
        this.data = data;

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        buildUI();
        fillApplicants(0);

        pack();
        setLocationRelativeTo(parent);
    }

    private void buildUI() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));

        applicantBox = new JComboBox<>();
        infoLabel = new JLabel(" ");
        amountField = new JTextField(20);

        addRow(form, new JLabel("Applicant:"), applicantBox);
        addRow(form, new JLabel("Applicant info:"), infoLabel);
        addRow(form, new JLabel("Amount requested (PHP):"), amountField);

        applicantBox.addActionListener(e -> updateInfo());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");
        buttons.add(submitButton);
        buttons.add(cancelButton);

        submitButton.addActionListener(e -> submit());
        cancelButton.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(submitButton);

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    private void addRow(JPanel panel, JLabel label, JComponent field) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridy = row;

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(field, gbc);

        row++;
    }

    // (Re)fills the applicant list, so names and statuses are up to date
    private void fillApplicants(int selectedIndex) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (int i = 0; i < data.getApplicantCount(); i++) {
            model.addElement(UiUtil.applicantLabel(i, data.getApplicant(i)));
        }
        applicantBox.setModel(model);
        applicantBox.setSelectedIndex(selectedIndex);
        updateInfo();
    }

    // Shows the applicant type, priority and status (the "check applicant type" step)
    private void updateInfo() {
        int index = applicantBox.getSelectedIndex();
        if (index < 0) {
            return;
        }
        Applicant a = data.getApplicant(index);
        infoLabel.setText("Type: " + a.getApplicantType()
                + "  |  Priority: " + a.getPriorityLevel()
                + "  |  Status: " + a.getStatus());
    }

    private void submit() {
        int index = applicantBox.getSelectedIndex();
        Applicant a = data.getApplicant(index);

        if (a.getStatus().equals("Pending")) {
            JOptionPane.showMessageDialog(this,
                    "This applicant already has a pending application.",
                    "Already Pending", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (!data.canAddApplication()) {
            JOptionPane.showMessageDialog(this, "The application list is full.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Amount must be a number.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        AidApplication app = new AidApplication(a, amount);

        // The system checks the application for completeness
        if (app.submitApplication()) {
            data.addApplication(app);
            JOptionPane.showMessageDialog(this,
                    "Application submitted!\n\nApplicant type: " + a.getApplicantType()
                    + " (priority " + a.getPriorityLevel() + ")\nStatus: Pending",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            return;
        }

        // Incomplete: return to staff for correction
        if (a.hasCompleteDetails()) {
            // Only the amount is wrong
            JOptionPane.showMessageDialog(this,
                    "The application is INCOMPLETE and was returned to staff for correction."
                    + "\n\nMissing or invalid: " + app.getMissingDetails()
                    + "\n\nPlease enter a valid amount and submit again.",
                    "Incomplete Application", JOptionPane.WARNING_MESSAGE);
            updateInfo();
            return;
        }

        Object[] options = {"Correct details now", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this,
                "The application is INCOMPLETE and was returned to staff for correction."
                + "\n\nMissing or invalid:\n" + app.getMissingDetails(),
                "Incomplete Application", JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            new UpdateApplicantDialog(this, data, index).setVisible(true);
        }
        fillApplicants(index); // refresh names and statuses
    }
}
