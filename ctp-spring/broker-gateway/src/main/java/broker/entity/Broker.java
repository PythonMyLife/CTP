package broker.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Objects;

@Document(collection = "broker")
public class Broker implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String username;
    private String password;

    public Broker() {}

    public Broker(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Broker broker = (Broker) o;
        return id.equals(broker.id) &&
                username.equals(broker.username) &&
                password.equals(broker.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }
}
