package broker.websocket.message.entity;

public class OrderJoinMessage extends Message{
    private String username;

    public OrderJoinMessage(String username) {
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "OrderJoinMessage{" +
                "username='" + username + '\'' +
                '}';
    }
}
