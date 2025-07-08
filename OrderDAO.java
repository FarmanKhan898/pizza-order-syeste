import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public static void saveOrder(Order order, String customerName, String phone, String address) {
        try (Connection conn = DBConnection.getConnection()) {
            String insertOrder = "INSERT INTO orders (customer_name, phone, address) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, customerName);
            ps.setString(2, phone);
            ps.setString(3, address);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int orderId = rs.getInt(1);

                for (Pizza pizza : order.getPizzas()) {
                    String insertPizza = "INSERT INTO pizzas (order_id, size, price) VALUES (?, ?, ?)";
                    PreparedStatement psPizza = conn.prepareStatement(insertPizza, Statement.RETURN_GENERATED_KEYS);
                    psPizza.setInt(1, orderId);
                    psPizza.setString(2, pizza.getSize().name());
                    psPizza.setDouble(3, pizza.calculatePrice());
                    psPizza.executeUpdate();

                    ResultSet rsPizza = psPizza.getGeneratedKeys();
                    if (rsPizza.next()) {
                        int pizzaId = rsPizza.getInt(1);
                        for (Topping topping : pizza.getToppings()) {
                            String insertTopping = "INSERT INTO toppings (pizza_id, name, price) VALUES (?, ?, ?)";
                            PreparedStatement psTopping = conn.prepareStatement(insertTopping);
                            psTopping.setInt(1, pizzaId);
                            psTopping.setString(2, topping.getName());
                            psTopping.setDouble(3, topping.getPrice());
                            psTopping.executeUpdate();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<OrderRecord> fetchAllOrders() {
        List<OrderRecord> orders = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT o.order_id, o.customer_name, o.phone, o.address, " +
                    "GROUP_CONCAT(CONCAT(p.size, ' Pizza with ', IFNULL(toppings, 'No Toppings')) SEPARATOR '\n') as items, " +
                    "SUM(p.price + IFNULL(t.total_toppings, 0)) as total " +
                    "FROM orders o " +
                    "JOIN pizzas p ON o.order_id = p.order_id " +
                    "LEFT JOIN (" +
                    "    SELECT pizza_id, GROUP_CONCAT(name SEPARATOR ', ') as toppings, SUM(price) as total_toppings " +
                    "    FROM toppings GROUP BY pizza_id" +
                    ") t ON p.pizza_id = t.pizza_id " +
                    "GROUP BY o.order_id";

            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                orders.add(new OrderRecord(
                        rs.getInt("order_id"),
                        rs.getString("customer_name"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("items"),
                        rs.getDouble("total")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }
}