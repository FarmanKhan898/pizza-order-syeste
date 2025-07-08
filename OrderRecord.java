public class OrderRecord {
    private int orderId;
    private String customerName;
    private String phone;
    private String address;
    private String items;
    private double total;

    public OrderRecord(int orderId, String customerName, String phone, String address, String items, double total) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.phone = phone;
        this.address = address;
        this.items = items;
        this.total = total;
    }

    public int getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getItems() { return items; }
    public double getTotal() { return total; }
}