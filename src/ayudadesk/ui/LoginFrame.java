package ayudadesk.ui;

import ayudadesk.model.AppData;
import ayudadesk.model.Staff;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

// FEATURE 1: STAFF LOGIN (GUI)
public class LoginFrame extends JFrame {
    private static final int MAX_ATTEMPTS = 3;

    private AppData data;
    private int attempts = 0;

    private JTextField idField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    public LoginFrame(AppData data) {
        this.data = data;

        setTitle("AyudaDesk - Staff Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buildUI();

        pack();
        setResizable(false);
        setLocationRelativeTo(null); // center on screen
    }

    private void buildUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("AyudaDesk", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        JLabel subtitle = new JLabel("Financial Aid Application and Processing System",
                SwingConstants.CENTER);
        gbc.gridy = 1;
        panel.add(subtitle, gbc);

        // Staff ID
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Staff ID:"), gbc);

        idField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(idField, gbc);

        // Password
        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Login button
        loginButton = new JButton("Login");
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        // Message (errors show here)
        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);
        gbc.gridy = 5;
        panel.add(messageLabel, gbc);

        add(panel);

        // Pressing Enter also clicks Login
        getRootPane().setDefaultButton(loginButton);

        // What happens when the button is clicked
        loginButton.addActionListener(e -> attemptLogin());
    }

    private void attemptLogin() {
        String id = idField.getText().trim();
        String pass = new String(passwordField.getPassword());

        Staff staff = data.login(id, pass);

        if (staff != null) {
            new MainFrame(data, staff).setVisible(true);
            dispose(); // close the login window
            return;
        }

        attempts++;
        passwordField.setText("");

        if (attempts >= MAX_ATTEMPTS) {
            messageLabel.setText("Too many failed attempts. Closing...");
            loginButton.setEnabled(false);
            idField.setEnabled(false);
            passwordField.setEnabled(false);

            // Close the program after 2 seconds
            javax.swing.Timer timer = new javax.swing.Timer(2000, ev -> System.exit(0));
            timer.setRepeats(false);
            timer.start();
        } else {
            messageLabel.setText("Invalid ID or password. Attempts left: "
                    + (MAX_ATTEMPTS - attempts));
        }
    }
}
