public enum PizzaSize {
    SMALL(5.0), MEDIUM(8.0), LARGE(10.0);

    private final double basePrice;

    PizzaSize(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getBasePrice() {
        return basePrice;
    }
}


