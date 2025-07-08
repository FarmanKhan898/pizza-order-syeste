import java.util.ArrayList;
import java.util.List;

public class Order {
    private final List<Pizza> pizzas = new ArrayList<>();

    public void addPizza(Pizza pizza) {
        pizzas.add(pizza);
    }

    public List<Pizza> getPizzas() {
        return pizzas;
    }

    public double calculateTotal() {
        return pizzas.stream().mapToDouble(Pizza::calculatePrice).sum();
    }

    public String generateReceipt() {
        StringBuilder sb = new StringBuilder("==== Order Receipt ====");
        int count = 1;
        for (Pizza pizza : pizzas) {
            sb.append("#").append(count++)
                    .append(" ").append(pizza.getSize())
                    .append(" - Base: $").append(String.format("%.2f", pizza.getBasePrice())).append("\n");
            for (Topping topping : pizza.getToppings()) {
                sb.append("   + ").append(topping.getName())
                        .append(" $").append(String.format("%.2f", topping.getPrice())).append("\n");
            }
            sb.append("Subtotal: $").append(String.format("%.2f", pizza.calculatePrice())).append("\n\n");
        }
        sb.append("TOTAL: $").append(String.format("%.2f", calculateTotal()));
        return sb.toString();
    }
}
