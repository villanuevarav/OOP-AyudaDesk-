package ayudadesk.ui;

import ayudadesk.model.AppData;
import ayudadesk.model.Staff;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

// MAIN WINDOW (staff menu)
public class MainFrame extends JFrame {
    private AppData data;
    private Staff staff;

    private JLabel countLabel;

    public MainFrame(AppData data, Staff staff) {
        this.data = data;
        this.staff = staff;

        setTitle("AyudaDesk - Staff Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buildUI();

        setSize(440, 430);
        setLocationRelativeTo(null);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(10, 15));
        root.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Top: welcome message and counts
        JPanel top = new JPanel(new GridLayout(2, 1, 0, 4));
        JLabel welcome = new JLabel("Welcome, " + staff.getName() + "!");
        welcome.setFont(welcome.getFont().deriveFont(Font.BOLD, 18f));
        countLabel = new JLabel();
        updateCount();
        top.add(welcome);
        top.add(countLabel);
        root.add(top, BorderLayout.NORTH);

        // Center: menu buttons
        JPanel buttons = new JPanel(new GridLayout(0, 1, 0, 8));

        JButton registerButton = new JButton("Register Applicant");
        registerButton.addActionListener(e -> openRegisterDialog());
        buttons.add(registerButton);

        JButton updateButton = new JButton("Update Applicant Details");
        updateButton.addActionListener(e -> openUpdateDialog());
        buttons.add(updateButton);

        JButton submitButton = new JButton("Submit Emergency Aid Application");
        submitButton.addActionListener(e -> openSubmitDialog());
        buttons.add(submitButton);

        JButton reviewButton = new JButton("Review Pending Applications");
        reviewButton.addActionListener(e -> openReviewDialog());
        buttons.add(reviewButton);

        JButton viewButton = new JButton("View Applicants and Status");
        viewButton.addActionListener(e -> openViewDialog());
        buttons.add(viewButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        buttons.add(logoutButton);

        root.add(buttons, BorderLayout.CENTER);
        add(root);
    }

    private void updateCount() {
        countLabel.setText("Registered applicants: " + data.getApplicantCount()
                + "   |   Pending applications: " + data.getPendingApplications().length);
    }

    private boolean hasApplicants() {
        if (data.getApplicantCount() == 0) {
            JOptionPane.showMessageDialog(this, "No applicants registered yet.",
                    "No Applicants", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
    }

    // FEATURE 2: APPLICANT REGISTRATION
    private void openRegisterDialog() {
        new RegisterApplicantDialog(this, data, staff).setVisible(true);
        updateCount();
    }

    private void openUpdateDialog() {
        if (!hasApplicants()) {
            return;
        }
        new UpdateApplicantDialog(this, data, 0).setVisible(true);
        updateCount();
    }

    // FEATURE 3: AID SUBMISSION
    private void openSubmitDialog() {
        if (!hasApplicants()) {
            return;
        }
        new SubmitApplicationDialog(this, data).setVisible(true);
        updateCount();
    }

    // FEATURE 4: DASHBOARD
    private void openReviewDialog() {
        if (data.getPendingApplications().length == 0) {
            JOptionPane.showMessageDialog(this, "No pending applications.",
                    "Nothing to Review", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        new ReviewApplicationsDialog(this, data, staff).setVisible(true);
        updateCount();
    }

    private void openViewDialog() {
        if (!hasApplicants()) {
            return;
        }
        new ViewApplicantsDialog(this, data).setVisible(true);
    }

    private void logout() {
        new LoginFrame(data).setVisible(true);
        dispose();
    }
}
