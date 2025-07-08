import java.util.ArrayList;
import java.util.List;

public class Pizza {
    public enum Size { SMALL, MEDIUM, LARGE }

    private final Size size;
    private final double basePrice;
    private final List<Topping> toppings;

    public Pizza(Size size, double basePrice) {
        this.size = size;
        this.basePrice = basePrice;
        this.toppings = new ArrayList<>();
    }

    public void addTopping(Topping topping) {
        toppings.add(topping);
    }

    public Size getSize() { return size; }
    public double getBasePrice() { return basePrice; }
    public List<Topping> getToppings() { return toppings; }

    public double calculatePrice() {
        return basePrice + toppings.stream().mapToDouble(Topping::getPrice).sum();
    }
}