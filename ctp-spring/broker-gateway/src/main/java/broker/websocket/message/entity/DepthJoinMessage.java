package broker.websocket.message.entity;

public class DepthJoinMessage extends Message{
    private String productId;

    private String username;

    public DepthJoinMessage(String productId, String username) {
        this.productId = productId;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "DepthJoinMessage{" +
                "productId='" + productId + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
