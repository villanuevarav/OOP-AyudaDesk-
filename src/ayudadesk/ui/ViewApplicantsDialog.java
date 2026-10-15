package ayudadesk.ui;

import ayudadesk.model.AppData;
import ayudadesk.model.Applicant;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Window;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

// VIEW APPLICANTS AND STATUS (status tracking)
public class ViewApplicantsDialog extends JDialog {

    public ViewApplicantsDialog(Window parent, AppData data) {
        super(parent, "Applicants and Status", Dialog.ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        String[] columns = {"No.", "Type", "Name", "Age", "Address",
            "Contact Number", "Details", "Priority", "Status"};

        // A table model whose cells cannot be edited by the user
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (int i = 0; i < data.getApplicantCount(); i++) {
            Applicant a = data.getApplicant(i);
            model.addRow(new Object[] {
                i + 1,
                a.getApplicantType(),
                a.getName(),
                a.getAge(),
                a.getAddress(),
                a.getContactNumber(),
                a.getExtraInfo(),
                a.getPriorityLevel(),
                a.getStatus()
            });
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);

        int[] widths = {40, 70, 130, 40, 160, 110, 220, 60, 90};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttons.add(closeButton);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        setSize(950, 400);
        setLocationRelativeTo(parent);
    }
}
