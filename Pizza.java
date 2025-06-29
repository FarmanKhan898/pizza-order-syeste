import java.util.ArrayList;
import java.util.List;

public class Pizza {
    private final PizzaSize size;
    private final List<Topping> toppings;
    private final double basePrice;

    public Pizza(PizzaSize size) {
        this.size = size;
        this.toppings = new ArrayList<>();
        this.basePrice = size.getBasePrice();
    }

    public PizzaSize getSize() {
        return size;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public List<Topping> getToppings() {
        return toppings;
    }


    public void addTopping(Topping topping) {
        toppings.add(topping);
    }

    public double calculatePrice() {
        return basePrice + toppings.stream().mapToDouble(Topping::getPrice).sum();
    }

    @Override
    public String toString() {
        return size + " Pizza with " + toppings + " - $" + String.format("%.2f", calculatePrice());
    }
}

