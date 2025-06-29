
import java.util.ArrayList;
import java.util.List;

public class Order {
    private final List<Pizza> pizzas = new ArrayList<>();
    public void addPizza(Pizza pizza) { pizzas.add(pizza); }
    public double getTotal() {
        return pizzas.stream().mapToDouble(Pizza::calculatePrice).sum();
    }
    public List<Pizza> getPizzas() { return pizzas; }
    public String generateReceipt() {
        StringBuilder sb = new StringBuilder("====== RECEIPT ======\n");
        for (Pizza pizza : pizzas) sb.append(pizza.toString()).append("\n");
        sb.append("---------------------\n");
        sb.append("Total: $").append(String.format("%.2f", getTotal()));
        return sb.toString();
    }
}

