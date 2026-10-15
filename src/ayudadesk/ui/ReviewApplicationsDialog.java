package ayudadesk.ui;

import ayudadesk.model.AidApplication;
import ayudadesk.model.AppData;
import ayudadesk.model.Applicant;
import ayudadesk.model.Staff;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

// FEATURE 4: STAFF DASHBOARD (GUI)
// Pending applications are listed with PWD applicants first.
public class ReviewApplicationsDialog extends JDialog {
    private AppData data;
    private Staff staff;

    private AidApplication[] pending; // same order as the table rows
    private DefaultTableModel model;
    private JTable table;
    private JLabel messageLabel;

    public ReviewApplicationsDialog(Window parent, AppData data, Staff staff) {
        super(parent, "Review Pending Applications", Dialog.ModalityType.APPLICATION_MODAL);
        this.data = data;
        this.staff = staff;

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        buildUI();
        loadPending();
        if (pending.length == 0) {
            messageLabel.setText("No pending applications.");
        }

        setSize(850, 400);
        setLocationRelativeTo(parent);
    }

    private void buildUI() {
        JLabel heading = new JLabel("Pending applications (PWD applicants first)");
        heading.setFont(heading.getFont().deriveFont(Font.BOLD, 15f));
        heading.setBorder(BorderFactory.createEmptyBorder(12, 15, 5, 15));

        String[] columns = {"Applicant", "Type", "Priority", "Amount",
            "Date Submitted", "Details"};

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton approveButton = new JButton("Approve");
        JButton rejectButton = new JButton("Reject");
        JButton closeButton = new JButton("Close");

        approveButton.addActionListener(e -> decide(true));
        rejectButton.addActionListener(e -> decide(false));
        closeButton.addActionListener(e -> dispose());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttons.add(approveButton);
        buttons.add(rejectButton);
        buttons.add(closeButton);

        messageLabel = new JLabel(" ");
        messageLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        JPanel south = new JPanel(new BorderLayout());
        south.add(messageLabel, BorderLayout.WEST);
        south.add(buttons, BorderLayout.EAST);

        add(heading, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);
    }

    // Reloads the table from the data (already sorted by priority)
    private void loadPending() {
        pending = data.getPendingApplications();
        model.setRowCount(0);

        for (int i = 0; i < pending.length; i++) {
            Applicant a = pending[i].getApplicant();
            model.addRow(new Object[] {
                a.getName(),
                a.getApplicantType(),
                a.getPriorityLevel(),
                UiUtil.money(pending[i].getAmount()),
                pending[i].getDateSubmitted(),
                a.getExtraInfo()
            });
        }
    }

    // true = approve, false = reject
    private void decide(boolean approve) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an application in the table first.",
                    "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        AidApplication app = pending[row];
        String name = app.getApplicant().getName();
        String action;
        if (approve) {
            action = "approve";
        } else {
            action = "reject";
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to " + action + " the application of " + name + "?",
                "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        staff.reviewApplication(app, approve);
        loadPending();

        String result;
        if (approve) {
            result = name + "'s application was APPROVED.";
        } else {
            result = name + "'s application was REJECTED.";
        }
        if (pending.length == 0) {
            result += "  No more pending applications.";
        }
        messageLabel.setText(result);
    }
}
