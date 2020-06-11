package dealer.dao;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import static com.mongodb.client.model.Filters.eq;

@Repository
public class DepthDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    public String insertOne(String productId, JSONObject object) {
        // 1.获取集合对象
        MongoCollection<Document> collection = mongoTemplate.getCollection(productId);
        // 2.构造Document
        Document document = new Document();
        document.append("price", object.get("price"));
        document.append("action", object.get("action"));
        document.append("time", object.get("time"));
        document.append("num", object.get("num"));
        document.append("trader", object.get("trader"));
        document.append("broker", object.get("broker"));
        document.append("orderId", object.get("orderId"));
        // 3.向集合中存储 Document
        collection.insertOne(document);
        // 4.返回_id
        System.out.println(document.toJson());
        return document.get("_id").toString();
    }

    public void updateOne(String productId, JSONObject object) {
        // 1.获取集合对象
        MongoCollection<Document> collection = mongoTemplate.getCollection(productId);
        // 2.创建用于查询和修改的BSON对象
        Bson bson = eq("_id", new ObjectId(object.get("_id").toString()));
        Document document = new Document("$set", new Document("num", object.get("num")));
        // 3.修改对应的数据
        collection.updateOne(bson, document);
    }

    public String findOne(String productId, JSONObject object) throws ParseException {
        // 1.获取集合对象
        MongoCollection<Document> collection = mongoTemplate.getCollection(productId);
        // 2.创建用于查询和修改的BSON对象
        Bson bson = eq("_id", new ObjectId(object.get("_id").toString()));
        //3.利用bson条件查询结果
        FindIterable<Document> documents = collection.find(bson);
        //4.将结果拼接成JSONArray
        JSONArray array = new JSONArray();
        JSONParser jp = new JSONParser();
        for(Document document : documents) {
            JSONObject jsonObject = (JSONObject) jp.parse(document.toJson());
            array.add(jsonObject);
        }
        return array.toJSONString();
    }

    public void deleteOne(String productId, JSONObject object) {
        // 1.获取集合对象
        MongoCollection<Document> collection = mongoTemplate.getCollection(productId);
        // 2.创建用于查询的BSON对象
        Bson bson = eq("_id", new ObjectId(object.get("_id").toString()));
        // 3.修改对应的数据
        collection.deleteOne(bson);
    }

    public JSONArray findAll(String productId) throws ParseException {
        // 1.获取集合对象
        MongoCollection<Document> collection = mongoTemplate.getCollection(productId);
//        // 2.创建用于查询的BSON对象
//        Bson bson = eq("field", "value");
        //3.利用bson条件查询结果
        FindIterable<Document> documents = collection.find();
        //4.将结果拼接成JSONArray
        JSONArray array = new JSONArray();
        JSONParser jp = new JSONParser();
        for(Document document : documents) {
            JSONObject object = (JSONObject) jp.parse(document.toJson());
            System.out.println(object.get("_id"));
            object.put("id", ((JSONObject)object.get("_id")).get("$oid"));
            array.add(object);
        }
        return array;
    }
}
