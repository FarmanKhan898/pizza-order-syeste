import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::showLoginUI);
    }

    private static void showLoginUI() {
        JFrame frame = new JFrame("🍕 Pizza Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 330);
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(true); // modern flat look

        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0,
                        new Color(255, 230, 204), 0, getHeight(),
                        new Color(255, 153, 102));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBorder(BorderFactory.createLineBorder(new Color(255, 102, 51), 3));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel icon = new JLabel("🍕", SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 42));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(icon, gbc);

        JLabel title = new JLabel("Pizza Ordering System", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(new Color(102, 51, 0));
        gbc.gridy++;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        panel.add(new JLabel("Username:"), gbc);

        JTextField nameField = new JTextField();
        nameField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Password:"), gbc);

        JPasswordField passField = new JPasswordField();
        passField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        gbc.gridx = 1;
        panel.add(passField, gbc);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(255, 102, 51));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(loginBtn, gbc);

        // 💡 Forgot Password button
        JButton forgotBtn = new JButton("Forgot Password?");
        forgotBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        forgotBtn.setForeground(Color.BLUE);
        forgotBtn.setContentAreaFilled(false);
        forgotBtn.setBorderPainted(false);
        forgotBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotBtn.setFocusPainted(false);

        gbc.gridy++;
        panel.add(forgotBtn, gbc);

        JButton exitBtn = new JButton("Exit");
        exitBtn.setBackground(Color.LIGHT_GRAY);
        exitBtn.setForeground(Color.BLACK);
        exitBtn.setFocusPainted(false);
        exitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy++;
        panel.add(exitBtn, gbc);

        // Event handlers
        loginBtn.addActionListener(e -> {
            String username = nameField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (username.equalsIgnoreCase("muhammad farman") && password.equals("farman12345")) {
                fadeOut(frame);
                PizzaOrderingAppSwing.launchWithUser(username);
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        forgotBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame,
                    "your passowrd is farman12345",
                    "Password Recovery",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        exitBtn.addActionListener(e -> System.exit(0));

        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    private static void fadeOut(JFrame frame) {
        Timer timer = new Timer(15, null);
        timer.addActionListener(new ActionListener() {
            float opacity = 1f;

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity -= 0.05f;
                if (opacity <= 0) {
                    timer.stop();
                    frame.dispose();
                } else {
                    frame.setOpacity(opacity);
                }
            }
        });
        timer.start();
    }
}
