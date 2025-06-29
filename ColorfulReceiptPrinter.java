import java.awt.*;
import java.awt.print.*;
import java.util.List;

public class ColorfulReceiptPrinter implements Printable {
    private final List<Pizza> pizzas;
    private final double total;

    public ColorfulReceiptPrinter(List<Pizza> pizzas, double total) {
        this.pizzas = pizzas;
        this.total = total;
    }

    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
        if (pageIndex > 0) return NO_SUCH_PAGE;

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        int y = 20;
        g2d.setFont(new Font("Monospaced", Font.BOLD, 16));
        g2d.setColor(new Color(30, 144, 255)); // Blue
        g2d.drawString("==== PIZZA RECEIPT ====", 100, y);

        g2d.setFont(new Font("Monospaced", Font.PLAIN, 12));
        y += 30;
        int count = 1;

        for (Pizza pizza : pizzas) {
            g2d.setColor(Color.BLACK);
            g2d.drawString("#" + count++ + " " + pizza.getSize() + " Pizza - Base: $" + String.format("%.2f", pizza.getBasePrice()), 50, y);
            y += 15;

            for (Topping topping : pizza.getToppings()) {
                g2d.setColor(new Color(34, 139, 34)); // Green
                g2d.drawString("   + " + topping + " $" + String.format("%.2f", topping.getPrice()), 70, y);
                y += 15;
            }

            g2d.setColor(Color.GRAY);
            g2d.drawString("Subtotal: $" + String.format("%.2f", pizza.calculatePrice()), 50, y);
            y += 25;
        }

        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 14));
        g2d.drawString("TOTAL: $" + String.format("%.2f", total), 50, y);

        return PAGE_EXISTS;
    }
}
