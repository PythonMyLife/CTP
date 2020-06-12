package broker.dao;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import static com.mongodb.client.model.Filters.eq;

@Repository
public class DepthDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    public String findAll(String name) throws ParseException {
        // 1.获取集合对象
        MongoCollection<Document> collection = mongoTemplate.getCollection(name);
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
             array.add(object);
        }
        return array.toJSONString();
    }
}
