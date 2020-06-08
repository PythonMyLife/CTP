package broker.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Objects;

@Document(collection = "transaction")
public class Transaction implements Serializable {
    private static final long serialVersionUID = 4L;

    @Id
    private String id;
    private String product;
    private String time;
    private String price;
    private Integer num;
    private String tradersell;
    private String traderbuy;
    private String brokersell;
    private String brokerbuy;

    public Transaction() {}

    public Transaction(String time, String product, String price, Integer num, String tradersell, String traderbuy, String brokersell, String brokerbuy) {
        this.time = time;
        this.price = price;
        this.product = product;
        this.num = num;
        this.tradersell = tradersell;
        this.traderbuy = traderbuy;
        this.brokersell = brokersell;
        this.brokerbuy = brokerbuy;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTraderbuy() {
        return traderbuy;
    }

    public void setTraderbuy(String traderbuy) {
        this.traderbuy = traderbuy;
    }

    public String getTradersell() {
        return tradersell;
    }

    public void setTradersell(String tradersell) {
        this.tradersell = tradersell;
    }

    public String getBrokerbuy() {
        return brokerbuy;
    }

    public void setBrokerbuy(String brokerbuy) {
        this.brokerbuy = brokerbuy;
    }

    public String getBrokersell() {
        return brokersell;
    }

    public void setBrokersell(String brokersell) {
        this.brokersell = brokersell;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id.equals(that.id) &&
                product.equals(that.product) &&
                time.equals(that.time) &&
                price.equals(that.price) &&
                num.equals(that.num) &&
                tradersell.equals(that.tradersell) &&
                traderbuy.equals(that.traderbuy) &&
                brokersell.equals(that.brokersell) &&
                brokerbuy.equals(that.brokerbuy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product, time, price, num, tradersell, traderbuy, brokersell, brokerbuy);
    }
}
