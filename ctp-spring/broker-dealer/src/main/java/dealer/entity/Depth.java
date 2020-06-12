package dealer.entity;

import net.minidev.json.JSONObject;

import java.util.Objects;

public class Depth implements Comparable<Depth>{
    private String id;
    private Double price;
    private String action;
    private String time;
    private Integer num;
    private String trader;
    private String broker;
    private String orderId;

    public Depth(JSONObject object) {
        if(object.containsKey("id")) {
            this.id = String.valueOf(object.get("id"));
        }
        this.price = Double.valueOf(String.valueOf(object.get("price")));
        this.action = String.valueOf(object.get("action"));
        this.time = String.valueOf(object.get("time"));
        this.num = Integer.valueOf(String.valueOf(object.get("num")));
        this.trader = String.valueOf(object.get("trader"));
        this.broker = String.valueOf(object.get("broker"));
        this.orderId = String.valueOf(object.get("orderId"));
    }

    public String getId() {
        return id;
    }

    public Double getPrice() {
        return price;
    }

    public String getTime() {
        return time;
    }

    public Integer getNum() {
        return num;
    }

    public String getTrader() {
        return trader;
    }

    public String getBroker() {
        return broker;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public void setTrader(String trader) {
        this.trader = trader;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Depth depth = (Depth) o;
        return id.equals(depth.id) &&
                price.equals(depth.price) &&
                action.equals(depth.action) &&
                time.equals(depth.time) &&
                num.equals(depth.num) &&
                trader.equals(depth.trader) &&
                broker.equals(depth.broker) &&
                orderId.equals(depth.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, action, time, num, trader, broker, orderId);
    }

    @Override
    public int compareTo(Depth t1) {
        if(this.action.equals("buy")) {
            if(this.price < t1.getPrice()) {
                return 1;
            } else if(this.price > t1.getPrice()) {
                return -1;
            } else {
                Long difference = Long.valueOf(this.time) - Long.valueOf(t1.getTime());
                return difference > 0 ? 1 : -1;
            }
        } else {
            if(this.price < t1.getPrice()) {
                return -1;
            } else if(this.price > t1.getPrice()) {
                return 1;
            } else {
                Long difference = Long.valueOf(this.time) - Long.valueOf(t1.getTime());
                return difference > 0 ? 1 : -1;
            }
        }
    }
}
