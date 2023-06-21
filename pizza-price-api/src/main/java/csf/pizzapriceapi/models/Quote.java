package csf.pizzapriceapi.models;

public class Quote {
    private String orderId;
    private long date;
    private Float total;

    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public long getDate() {
        return date;
    }
    public void setDate(long date) {
        this.date = date;
    }
    public Float getTotal() {
        return total;
    }
    public void setTotal(Float total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Quote [orderId=" + orderId + ", date=" + date + ", total=" + total + "]";
    }
}
