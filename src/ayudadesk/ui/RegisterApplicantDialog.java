package ayudadesk.ui;

import ayudadesk.model.AppData;
import ayudadesk.model.Applicant;
import ayudadesk.model.PWDApplicant;
import ayudadesk.model.RegularApplicant;
import ayudadesk.model.Staff;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

// FEATURE 2: APPLICANT REGISTRATION (GUI)
public class RegisterApplicantDialog extends JDialog {
    private AppData data;
    private Staff staff;

    private JComboBox<String> typeBox;
    private JTextField nameField;
    private JTextField ageField;
    private JTextField addressField;
    private JTextField contactField;
    private JTextField pwdIdField;
    private JTextField disabilityField;
    private JLabel pwdIdLabel;
    private JLabel disabilityLabel;

    private int row = 0; // current row while building the form

    public RegisterApplicantDialog(JFrame parent, AppData data, Staff staff) {
        super(parent, "Register Applicant", true); // true = modal
        this.data = data;
        this.staff = staff;

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        buildUI();
        updatePwdFields();

        setLocationRelativeTo(parent);
    }

    private void buildUI() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));

        typeBox = new JComboBox<>(new String[] {"Regular", "PWD"});
        nameField = new JTextField(20);
        ageField = new JTextField(20);
        addressField = new JTextField(20);
        contactField = new JTextField(20);
        pwdIdField = new JTextField(20);
        disabilityField = new JTextField(20);
        pwdIdLabel = new JLabel("PWD ID Number:");
        disabilityLabel = new JLabel("Disability Type:");

        addRow(form, new JLabel("Applicant Type:"), typeBox);
        addRow(form, new JLabel("Name:"), nameField);
        addRow(form, new JLabel("Age:"), ageField);
        addRow(form, new JLabel("Address:"), addressField);
        addRow(form, new JLabel("Contact Number:"), contactField);
        addRow(form, pwdIdLabel, pwdIdField);
        addRow(form, disabilityLabel, disabilityField);

        // Show or hide the PWD fields when the type changes
        typeBox.addActionListener(e -> updatePwdFields());

        // Buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton registerButton = new JButton("Register");
        JButton cancelButton = new JButton("Cancel");
        buttons.add(registerButton);
        buttons.add(cancelButton);

        registerButton.addActionListener(e -> registerApplicant());
        cancelButton.addActionListener(e -> dispose());

        getRootPane().setDefaultButton(registerButton);

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    // Adds one "label + field" row to the form
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

    // PWD fields are only visible when "PWD" is selected
    private void updatePwdFields() {
        boolean isPwd = typeBox.getSelectedIndex() == 1;
        pwdIdLabel.setVisible(isPwd);
        pwdIdField.setVisible(isPwd);
        disabilityLabel.setVisible(isPwd);
        disabilityField.setVisible(isPwd);
        pack(); // resize the dialog to fit
    }

    private void registerApplicant() {
        // Age must be a number
        int age;
        try {
            age = Integer.parseInt(ageField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a whole number.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = nameField.getText();
        String address = addressField.getText();
        String contactNumber = contactField.getText();

        // Create the right kind of applicant (inheritance + polymorphism)
        Applicant applicant;
        if (typeBox.getSelectedIndex() == 0) {
            applicant = new RegularApplicant(name, age, address, contactNumber);
        } else {
            applicant = new PWDApplicant(name, age, address, contactNumber,
                    pwdIdField.getText(), disabilityField.getText());
        }

        // Warn about missing details; staff may still save and fix them later
        String missing = applicant.getMissingDetails();
        if (!missing.isEmpty()) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "These details are missing or invalid:\n" + missing
                    + "\n\nSave this applicant anyway?\n"
                    + "(An aid application cannot be submitted until this is fixed.)",
                    "Incomplete Details", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }
        }

        if (staff.registerApplicant(applicant, data)) {
            JOptionPane.showMessageDialog(this, "Applicant registered successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "The applicant list is full.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
