package broker.websocket.message.entity;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class OrderMessage extends Message{
    private JSONArray orders;
    private JSONArray transactions;

    public OrderMessage(JSONArray orders, JSONArray transactions) {
        this.orders = orders;
        this.transactions = transactions;
    }

    public JSONArray getOrders() {
        return orders;
    }

    public JSONArray getTransactions() {
        return transactions;
    }

    public void setOrders(JSONArray orders) {
        this.orders = orders;
    }

    public void setTransactions(JSONArray transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return "OrderMessage{" +
                "orders=" + orders.toJSONString() +
                ", transactions=" + transactions.toJSONString() +
                '}';
    }
}
