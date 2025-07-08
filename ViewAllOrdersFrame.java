import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ViewAllOrdersFrame extends JFrame {
    public ViewAllOrdersFrame(List<OrderRecord> orders) {
        setTitle("📋 All Orders");
        setSize(600, 400);
        setLocationRelativeTo(null);

        JTextArea area = new JTextArea();
        area.setEditable(false);

        for (OrderRecord record : orders) {
            area.append(record.toString());
            area.append("\n----------------------\n");
        }

        JScrollPane scroll = new JScrollPane(area);
        add(scroll, BorderLayout.CENTER);
    }
}
