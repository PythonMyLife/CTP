package dealer.controller;

import dealer.dao.DepthDao;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/broker/test")
public class DepthController {
    @Autowired
    private DepthDao depthDao;

    @GetMapping("/insert")
    public String insert() {
        try {
            JSONObject object = new JSONObject();
            return depthDao.insertOne("test", object);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @GetMapping("/update")
    public String update(String _id, Integer num) {
        try {
            JSONObject object = new JSONObject();
            object.put("_id", _id);
            object.put("num", num);
            depthDao.updateOne("test", object);
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @GetMapping("/delete")
    public String delete(String _id) {
        try {
            JSONObject object = new JSONObject();
            object.put("_id", _id);
            depthDao.deleteOne("test", object);
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @GetMapping("/all")
    public String all() {
        try {
            return depthDao.findAll("test").toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @GetMapping("/find")
    public String find(String id) {
        try {
            JSONObject object = new JSONObject();
            object.put("_id", id);
            return depthDao.findOne("test", object);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
