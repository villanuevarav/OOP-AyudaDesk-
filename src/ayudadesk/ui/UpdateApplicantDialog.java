package ayudadesk.ui;

import ayudadesk.model.AppData;
import ayudadesk.model.Applicant;
import ayudadesk.model.PWDApplicant;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

// UPDATE APPLICANT DETAILS (used to correct incomplete applications)
public class UpdateApplicantDialog extends JDialog {
    private AppData data;

    private JComboBox<String> applicantBox;
    private JTextField nameField;
    private JTextField ageField;
    private JTextField addressField;
    private JTextField contactField;
    private JTextField pwdIdField;
    private JTextField disabilityField;
    private JLabel pwdIdLabel;
    private JLabel disabilityLabel;

    private int row = 0; // current row while building the form

    // selectedIndex = which applicant is selected when the window opens
    public UpdateApplicantDialog(Window parent, AppData data, int selectedIndex) {
        super(parent, "Update Applicant Details", Dialog.ModalityType.APPLICATION_MODAL);
        this.data = data;

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        buildUI();
        applicantBox.setSelectedIndex(selectedIndex);
        loadSelected();

        setLocationRelativeTo(parent);
    }

    private void buildUI() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));

        applicantBox = new JComboBox<>();
        for (int i = 0; i < data.getApplicantCount(); i++) {
            applicantBox.addItem(UiUtil.applicantLabel(i, data.getApplicant(i)));
        }

        nameField = new JTextField(20);
        ageField = new JTextField(20);
        addressField = new JTextField(20);
        contactField = new JTextField(20);
        pwdIdField = new JTextField(20);
        disabilityField = new JTextField(20);
        pwdIdLabel = new JLabel("PWD ID Number:");
        disabilityLabel = new JLabel("Disability Type:");

        addRow(form, new JLabel("Applicant:"), applicantBox);
        addRow(form, new JLabel("Name:"), nameField);
        addRow(form, new JLabel("Age:"), ageField);
        addRow(form, new JLabel("Address:"), addressField);
        addRow(form, new JLabel("Contact Number:"), contactField);
        addRow(form, pwdIdLabel, pwdIdField);
        addRow(form, disabilityLabel, disabilityField);

        // Load the details of another applicant when the selection changes
        applicantBox.addActionListener(e -> loadSelected());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        buttons.add(saveButton);
        buttons.add(cancelButton);

        saveButton.addActionListener(e -> save());
        cancelButton.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(saveButton);

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

    // Fills the form with the selected applicant's current details
    private void loadSelected() {
        int index = applicantBox.getSelectedIndex();
        if (index < 0) {
            return;
        }

        Applicant a = data.getApplicant(index);

        nameField.setText(a.getName());
        if (a.getAge() == 0) {
            ageField.setText("");
        } else {
            ageField.setText(String.valueOf(a.getAge()));
        }
        addressField.setText(a.getAddress());
        contactField.setText(a.getContactNumber());

        boolean isPwd = a instanceof PWDApplicant;
        pwdIdLabel.setVisible(isPwd);
        pwdIdField.setVisible(isPwd);
        disabilityLabel.setVisible(isPwd);
        disabilityField.setVisible(isPwd);

        if (isPwd) {
            PWDApplicant p = (PWDApplicant) a;
            pwdIdField.setText(p.getPwdIdNumber());
            disabilityField.setText(p.getDisabilityType());
        }

        pack();
    }

    private void save() {
        int age;
        try {
            age = Integer.parseInt(ageField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a whole number.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Applicant a = data.getApplicant(applicantBox.getSelectedIndex());

        // The setters do the validation (encapsulation)
        a.setName(nameField.getText());
        a.setAge(age);
        a.setAddress(addressField.getText());
        a.setContactNumber(contactField.getText());

        if (a instanceof PWDApplicant) {
            PWDApplicant p = (PWDApplicant) a;
            p.setPwdIdNumber(pwdIdField.getText());
            p.setDisabilityType(disabilityField.getText());
        }

        String missing = a.getMissingDetails();
        if (missing.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Applicant details updated.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Details saved, but these are still missing or invalid:\n" + missing,
                    "Still Incomplete", JOptionPane.WARNING_MESSAGE);
        }
        dispose();
    }
}
