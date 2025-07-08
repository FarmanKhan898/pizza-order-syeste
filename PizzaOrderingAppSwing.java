import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.List;
import java.util.Objects;

public class PizzaOrderingAppSwing {
    private final Order order = new Order();
    private final JTextArea orderSummaryArea = new JTextArea(15, 25);
    private Pizza.Size selectedSize = Pizza.Size.MEDIUM;

    private final JLabel pizzaIconLabel = new JLabel();
    private final JLabel pizzaNameLabel = new JLabel();
    private final JLabel pizzaSizeLabel = new JLabel();
    private final JLabel pizzaServingLabel = new JLabel();
    private final JLabel pizzaPriceLabel = new JLabel();
    private final JTextArea pizzaDescriptionArea = new JTextArea();

    private final JTextField customerNameField = new JTextField(20);
    private final JTextField phoneField = new JTextField(15);
    private final JTextArea addressField = new JTextArea(3, 20);

    private final String loggedInUser;

    public static void launchWithUser(String userName) {
        SwingUtilities.invokeLater(() -> new PizzaOrderingAppSwing(userName));
    }

    public PizzaOrderingAppSwing(String userName) {
        this.loggedInUser = userName;

        JFrame frame = new JFrame("\uD83C\uDF55 Pizza Ordering System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setUndecorated(true);

        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 240, 200), 0, getHeight(), new Color(255, 180, 120));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        backgroundPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 102, 51), 3));

        JLabel welcomeLabel = new JLabel("\uD83D\uDC64 Logged in as: " + loggedInUser);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        welcomeLabel.setForeground(new Color(80, 40, 0));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));
        backgroundPanel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(255, 245, 230));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Select Pizza Size"));
        ButtonGroup sizeGroup = new ButtonGroup();
        JPanel pizzaSizePanel = new JPanel(new GridLayout(1, 3));

        for (Pizza.Size size : Pizza.Size.values()) {
            String imagePath = "images/" + size.name().toLowerCase() + "_pizza.png";
            ImageIcon icon = loadImageIcon(imagePath);
            Image scaled = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

            JRadioButton radio = new JRadioButton(size.name());
            radio.setSelected(size == Pizza.Size.MEDIUM);
            radio.setBackground(Color.WHITE);
            radio.addActionListener(e -> {
                selectedSize = size;
                updatePizzaDetails(size);
            });

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(Color.WHITE);
            JLabel imageLabel = new JLabel(new ImageIcon(scaled));
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            radio.setAlignmentX(Component.CENTER_ALIGNMENT);

            panel.add(imageLabel);
            panel.add(radio);
            pizzaSizePanel.add(panel);
            sizeGroup.add(radio);
        }

        leftPanel.add(pizzaSizePanel);

        JPanel detailPanel = new JPanel(new BorderLayout());
        detailPanel.setBackground(new Color(255, 245, 230));
        detailPanel.setBorder(BorderFactory.createTitledBorder("Pizza Details"));
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(new Color(255, 245, 230));
        textPanel.add(pizzaNameLabel);
        textPanel.add(pizzaSizeLabel);
        textPanel.add(pizzaServingLabel);
        textPanel.add(pizzaPriceLabel);

        pizzaDescriptionArea.setEditable(false);
        pizzaDescriptionArea.setLineWrap(true);
        pizzaDescriptionArea.setWrapStyleWord(true);
        pizzaDescriptionArea.setBackground(new Color(255, 245, 230));

        detailPanel.add(pizzaIconLabel, BorderLayout.WEST);
        detailPanel.add(textPanel, BorderLayout.CENTER);
        detailPanel.add(pizzaDescriptionArea, BorderLayout.SOUTH);

        JCheckBox cheese = new JCheckBox("Cheese ($1.0)");
        JCheckBox olives = new JCheckBox("Olives ($1.2)");
        JCheckBox mushrooms = new JCheckBox("Mushrooms ($1.0)");
        JCheckBox pepperoni = new JCheckBox("Pepperoni ($1.5)");

        JPanel toppingsPanel = new JPanel(new GridLayout(2, 2));
        toppingsPanel.setBackground(new Color(255, 245, 230));
        toppingsPanel.setBorder(BorderFactory.createTitledBorder("Toppings"));
        toppingsPanel.add(cheese);
        toppingsPanel.add(olives);
        toppingsPanel.add(mushrooms);
        toppingsPanel.add(pepperoni);

        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBackground(new Color(255, 245, 230));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Order Summary"));
        orderSummaryArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(orderSummaryArea);
        summaryPanel.add(scroll, BorderLayout.CENTER);

        JPanel customerPanel = new JPanel(new GridLayout(3, 2));
        customerPanel.setBackground(new Color(255, 245, 230));
        customerPanel.setBorder(BorderFactory.createTitledBorder("Customer Info"));
        customerPanel.add(new JLabel("Name:"));
        customerPanel.add(customerNameField);
        customerPanel.add(new JLabel("Phone:"));
        customerPanel.add(phoneField);
        customerPanel.add(new JLabel("Address:"));
        customerPanel.add(new JScrollPane(addressField));
        summaryPanel.add(customerPanel, BorderLayout.NORTH);

        JButton addBtn = new JButton("Add Pizza");
        JButton printBtn = new JButton("Print Receipt");
        JButton saveBtn = new JButton("Place Order");
        JButton viewOrdersBtn = new JButton("View All Orders");

        JButton[] buttons = { addBtn, printBtn, saveBtn, viewOrdersBtn };
        for (JButton btn : buttons) {
            btn.setBackground(new Color(255, 102, 51));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
        }

        addBtn.addActionListener((ActionEvent e) -> {
            Pizza pizza = new Pizza(selectedSize, getBasePrice(selectedSize));
            if (cheese.isSelected()) pizza.addTopping(new Topping("Cheese", 1.0));
            if (olives.isSelected()) pizza.addTopping(new Topping("Olives", 1.2));
            if (mushrooms.isSelected()) pizza.addTopping(new Topping("Mushrooms", 1.0));
            if (pepperoni.isSelected()) pizza.addTopping(new Topping("Pepperoni", 1.5));

            order.addPizza(pizza);
            orderSummaryArea.setText(order.generateReceipt());

            cheese.setSelected(false);
            olives.setSelected(false);
            mushrooms.setSelected(false);
            pepperoni.setSelected(false);
        });

        saveBtn.addActionListener(e -> {
            String name = customerNameField.getText().trim();
            String phone = phoneField.getText().trim();
            String addr = addressField.getText().trim();

            if (name.isEmpty() || phone.isEmpty() || addr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill all customer fields.");
                return;
            }

            // ✅ Name should only contain letters and spaces
            if (!name.matches("[a-zA-Z ]+")) {
                JOptionPane.showMessageDialog(null, "Name must contain only letters.");
                return;
            }

            // ✅ Phone should be only digits and 10 to 13 characters
            if (!phone.matches("\\d{10,13}")) {
                JOptionPane.showMessageDialog(null, "Phone must contain only digits (10 to 13 digits).");
                return;
            }

            if (order.getPizzas().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Add at least one pizza before placing order.");
                return;
            }

            OrderDAO.saveOrder(order, name, phone, addr);
            JOptionPane.showMessageDialog(null, "Order placed successfully!");
        });

        printBtn.addActionListener(e -> {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable(new ColorfulReceiptPrinter(order.getPizzas(), order.calculateTotal()));
            if (job.printDialog()) {
                try {
                    job.print();
                } catch (PrinterException ex) {
                    ex.printStackTrace();
                }
            }
        });

        viewOrdersBtn.addActionListener(e -> {
            List<OrderRecord> records = OrderDAO.fetchAllOrders();
            if (records.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No orders found.");
            } else {
                StringBuilder result = new StringBuilder();
                for (OrderRecord record : records) {
                    result.append("Order #").append(record.getOrderId()).append("\n")
                            .append("Customer: ").append(record.getCustomerName()).append("\n")
                            .append("Phone: ").append(record.getPhone()).append("\n")
                            .append("Address: ").append(record.getAddress()).append("\n")
                            .append("Items: \n").append(record.getItems()).append("\n")
                            .append("Total: $").append(String.format("%.2f", record.getTotal())).append("\n\n");
                }
                JTextArea area = new JTextArea(result.toString());
                area.setEditable(false);
                JScrollPane pane = new JScrollPane(area);
                pane.setPreferredSize(new Dimension(500, 400));
                JOptionPane.showMessageDialog(null, pane, "All Orders", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 245, 230));
        for (JButton btn : buttons) buttonPanel.add(btn);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(255, 245, 230));
        centerPanel.add(detailPanel);
        centerPanel.add(toppingsPanel);
        centerPanel.add(buttonPanel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, summaryPanel);
        splitPane.setDividerLocation(600);

        backgroundPanel.add(splitPane, BorderLayout.CENTER);
        backgroundPanel.add(centerPanel, BorderLayout.SOUTH);

        updatePizzaDetails(selectedSize);
        frame.setContentPane(backgroundPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private double getBasePrice(Pizza.Size size) {
        return switch (size) {
            case SMALL -> 5.0;
            case MEDIUM -> 7.5;
            case LARGE -> 10.0;
        };
    }

    private void updatePizzaDetails(Pizza.Size size) {
        pizzaNameLabel.setText(size.name() + " Pizza");
        pizzaSizeLabel.setText("Size: " + (size == Pizza.Size.SMALL ? "8\"" : size == Pizza.Size.MEDIUM ? "12\"" : "16\""));
        pizzaServingLabel.setText("Serves: " + (size == Pizza.Size.SMALL ? "1-2" : size == Pizza.Size.MEDIUM ? "2-3" : "3-5"));
        pizzaPriceLabel.setText("Base Price: $" + getBasePrice(size));

        pizzaDescriptionArea.setText(switch (size) {
            case SMALL -> "Perfect for individuals.";
            case MEDIUM -> "Ideal for couples or small families.";
            case LARGE -> "Great for groups and parties!";
        });

        String path = "images/" + size.name().toLowerCase() + "_pizza.png";
        ImageIcon icon = loadImageIcon(path);
        Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        pizzaIconLabel.setIcon(new ImageIcon(img));
    }

    private ImageIcon loadImageIcon(String path) {
        try {
            return new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource(path)));
        } catch (Exception e) {
            System.err.println("⚠️ Image not found: " + path);
            return new ImageIcon();
        }
    }

    public static void main(String[] args) {
        launchWithUser("TestUser");
    }
}
