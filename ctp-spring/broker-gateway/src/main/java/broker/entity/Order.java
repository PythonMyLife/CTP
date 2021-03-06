package broker.entity;

import net.minidev.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Objects;

@Document(collection = "order")
public class Order implements Serializable {
    private static final long serialVersionUID = 3L;

    @Id
    private String id;
    private String type;
    private String time;
    private JSONObject object;
    /* status = 0 pending, status = 1 handled */
    private Integer status;

    public Order() {}

    public Order(String type,String time, JSONObject object, Integer status) {
        this.type = type;
        this.time = time;
        this.object = object;
        this.status = status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public JSONObject getObject() {
        return object;
    }

    public void setObject(JSONObject object) {
        this.object = object;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id.equals(order.id) &&
                type.equals(order.type) &&
                time.equals(order.time) &&
                object.equals(order.object) &&
                status.equals(order.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, time, object, status);
    }
}
