import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Objects;

public class PizzaOrderingAppSwing {
    private final Order order = new Order();
    private final JTextArea orderSummaryArea = new JTextArea(15, 25);
    private PizzaSize selectedSize = PizzaSize.MEDIUM;

    private int headlineX = 0;
    private int headlineDirection = 1;
    private Color headlineColor = new Color(30, 144, 255);

    // Pizza Details UI Components
    private final JPanel pizzaDetailsPanel = new JPanel();
    private final JLabel pizzaIconLabel = new JLabel();
    private final JLabel pizzaNameLabel = new JLabel();
    private final JLabel pizzaSizeLabel = new JLabel();
    private final JLabel pizzaServingLabel = new JLabel();
    private final JLabel pizzaPriceLabel = new JLabel();
    private final JTextArea pizzaDescriptionArea = new JTextArea();

    public PizzaOrderingAppSwing() {
        JFrame frame = new JFrame("🍕 Pizza Ordering System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // === Animated Headline ===
        JPanel headlinePanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, Color.ORANGE, getWidth(), 0, Color.RED);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g.setFont(new Font("Verdana", Font.BOLD, 32));
                g.setColor(Color.WHITE);
                FontMetrics fm = g.getFontMetrics();
                String text = "WELCOME TO FARMAN FAST FOOD ONLINE SHOP";
                int y = (getHeight() + fm.getAscent()) / 2 - 5;
                g.drawString(text, headlineX, y);
            }
        };
        headlinePanel.setPreferredSize(new Dimension(850, 75));

        Timer timer = new Timer(30, e -> {
            headlineX += headlineDirection * 4;
            if (headlineX > headlinePanel.getWidth() - 300) {
                headlineDirection = -1;
            } else if (headlineX < 0) {
                headlineDirection = 1;
            }
            headlinePanel.repaint();
        });
        timer.start();

        // === Left Panel: Ordering ===
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createTitledBorder("🍕 Pizza Customization"));
        leftPanel.setBackground(new Color(245, 255, 250));

        leftPanel.add(createLabel("PLEASE SELECT PIZZA SIZE:"));
        JPanel pizzaSelectionPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        pizzaSelectionPanel.setBackground(new Color(245, 255, 250));
        ButtonGroup sizeGroup = new ButtonGroup();

        for (PizzaSize size : PizzaSize.values()) {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(switch (size) {
                case SMALL -> "small_pizza.png";
                case MEDIUM -> "medium_pizza.png";
                case LARGE -> "large_pizza.png";
            })));
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JRadioButton radio = new JRadioButton(size.name());
            radio.setBackground(Color.WHITE);
            radio.setSelected(size == PizzaSize.MEDIUM);
            radio.addActionListener(e -> {
                selectedSize = size;
                updatePizzaDetails(size);
            });

            sizeGroup.add(radio);
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(Color.WHITE);
            JLabel picLabel = new JLabel(new ImageIcon(img));
            picLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            radio.setAlignmentX(Component.CENTER_ALIGNMENT);

            picLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    radio.setSelected(true);
                    selectedSize = size;
                    updatePizzaDetails(size);
                }
            });

            panel.add(picLabel);
            panel.add(radio);
            pizzaSelectionPanel.add(panel);
        }

        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(pizzaSelectionPanel);
        leftPanel.add(Box.createVerticalStrut(10));

        // === Pizza Details Panel ===
        pizzaDetailsPanel.setLayout(new BorderLayout(10, 10));
        pizzaDetailsPanel.setBorder(BorderFactory.createTitledBorder("Pizza Details"));
        pizzaDetailsPanel.setBackground(new Color(255, 250, 240));
        pizzaDetailsPanel.setPreferredSize(new Dimension(300, 180));

        pizzaIconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pizzaDetailsPanel.add(pizzaIconLabel, BorderLayout.WEST);

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(255, 250, 240));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        pizzaNameLabel.setFont(new Font("Verdana", Font.BOLD, 18));
        pizzaNameLabel.setForeground(new Color(178, 34, 34));
        infoPanel.add(pizzaNameLabel);
        infoPanel.add(pizzaSizeLabel);
        infoPanel.add(pizzaServingLabel);
        infoPanel.add(pizzaPriceLabel);

        pizzaDescriptionArea.setEditable(false);
        pizzaDescriptionArea.setLineWrap(true);
        pizzaDescriptionArea.setWrapStyleWord(true);
        pizzaDescriptionArea.setBackground(new Color(255, 250, 240));
        pizzaDescriptionArea.setFont(new Font("Serif", Font.ITALIC, 13));
        pizzaDescriptionArea.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        infoPanel.add(pizzaDescriptionArea);

        pizzaDetailsPanel.add(infoPanel, BorderLayout.CENTER);
        leftPanel.add(pizzaDetailsPanel);

        // === Toppings ===
        JCheckBox cheese = createStyledCheckbox("Cheese ($1.0)");
        JCheckBox pepperoni = createStyledCheckbox("Pepperoni ($1.5)");
        JCheckBox mushrooms = createStyledCheckbox("Mushrooms ($1.0)");
        JCheckBox olives = createStyledCheckbox("Olives ($1.2)");
        JCheckBox onions = createStyledCheckbox("Onions ($0.8)");
        JCheckBox extraSauce = createStyledCheckbox("Extra Sauce ($0.5)");

        JPanel toppingsPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        toppingsPanel.setBorder(BorderFactory.createTitledBorder("Toppings"));
        toppingsPanel.setBackground(new Color(255, 248, 220));
        toppingsPanel.add(cheese);
        toppingsPanel.add(pepperoni);
        toppingsPanel.add(mushrooms);
        toppingsPanel.add(olives);
        toppingsPanel.add(onions);
        toppingsPanel.add(extraSauce);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(toppingsPanel);

        // === Right Panel: Summary ===
        JPanel rightPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 255, 240), 0, getHeight(), new Color(230, 230, 210));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 2, true),
                        "🧾 Order Summary", 0, 0, new Font("Serif", Font.BOLD, 18), new Color(139, 69, 19))
        ));
        rightPanel.setOpaque(false);

        orderSummaryArea.setEditable(false);
        orderSummaryArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        orderSummaryArea.setBackground(new Color(255, 255, 240));
        orderSummaryArea.setBorder(BorderFactory.createLineBorder(new Color(210, 180, 140), 1));
        JScrollPane scrollPane = new JScrollPane(orderSummaryArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        rightPanel.add(scrollPane, BorderLayout.CENTER);


        // === Buttons ===
        JButton addPizzaBtn = new JButton("➕ Add Pizza");
        JButton printBtn = new JButton("🖨️ Print Receipt");
        styleButton(addPizzaBtn);
        styleButton(printBtn);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(addPizzaBtn);
        buttonPanel.add(printBtn);

        addPizzaBtn.addActionListener((ActionEvent e) -> {
            Pizza pizza = new Pizza(selectedSize);
            if (cheese.isSelected()) pizza.addTopping(new Topping("Cheese", 1.0));
            if (pepperoni.isSelected()) pizza.addTopping(new Topping("Pepperoni", 1.5));
            if (mushrooms.isSelected()) pizza.addTopping(new Topping("Mushrooms", 1.0));
            if (olives.isSelected()) pizza.addTopping(new Topping("Olives", 1.2));
            if (onions.isSelected()) pizza.addTopping(new Topping("Onions", 0.8));
            if (extraSauce.isSelected()) pizza.addTopping(new Topping("Extra Sauce", 0.5));

            order.addPizza(pizza);
            orderSummaryArea.setText(order.generateReceipt());

            cheese.setSelected(false);
            pepperoni.setSelected(false);
            mushrooms.setSelected(false);
            olives.setSelected(false);
            onions.setSelected(false);
            extraSauce.setSelected(false);
        });

        printBtn.addActionListener((ActionEvent e) -> {
            PrinterJob job = PrinterJob.getPrinterJob();
            if (job.printDialog()) {
                try {
                    orderSummaryArea.print();
                } catch (PrinterException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // === Layout ===
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(600);
        splitPane.setResizeWeight(0.7);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);

        mainPanel.add(headlinePanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        updatePizzaDetails(selectedSize);
    }

    private void updatePizzaDetails(PizzaSize size) {
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(switch (size) {
            case SMALL -> "small_pizza.png";
            case MEDIUM -> "medium_pizza.png";
            case LARGE -> "large_pizza.png";
        })));
        Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        pizzaIconLabel.setIcon(new ImageIcon(img));

        switch (size) {
            case SMALL -> {
                pizzaNameLabel.setText("Small Pizza");
                pizzaSizeLabel.setText("Diameter: 8 inches");
                pizzaServingLabel.setText("Serves: 1-2 people");
                pizzaPriceLabel.setText("Base Price: $5.00");
                pizzaDescriptionArea.setText("A perfect personal pizza, great for a quick snack or small appetite.");
            }
            case MEDIUM -> {
                pizzaNameLabel.setText("Medium Pizza");
                pizzaSizeLabel.setText("Diameter: 12 inches");
                pizzaServingLabel.setText("Serves: 2-3 people");
                pizzaPriceLabel.setText("Base Price: $7.50");
                pizzaDescriptionArea.setText("Ideal for small families or groups. Balanced size with good value.");
            }
            case LARGE -> {
                pizzaNameLabel.setText("Large Pizza");
                pizzaSizeLabel.setText("Diameter: 16 inches");
                pizzaServingLabel.setText("Serves: 3-5 people");
                pizzaPriceLabel.setText("Base Price: $10.00");
                pizzaDescriptionArea.setText("Great for parties or sharing. More toppings, more happiness!");
            }
        }
    }

    private JCheckBox createStyledCheckbox(String text) {
        JCheckBox checkbox = new JCheckBox(text);
        checkbox.setFont(new Font("Arial", Font.PLAIN, 13));
        checkbox.setBackground(new Color(255, 248, 220));
        return checkbox;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 15));
        return label;
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(173, 216, 230));
        button.setForeground(Color.DARK_GRAY);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(140, 35));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PizzaOrderingAppSwing::new);
    }
}
