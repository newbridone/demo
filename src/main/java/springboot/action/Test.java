package springboot.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import springboot.entity.Destination;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.service.DestinationService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class Test {
    @Autowired
    private DestinationService destinationDao;

    //导数据
    @RequestMapping(value = "/json")
    public List<JSONObject> getDFDesCity(@RequestBody JSONObject jsons) throws Exception {
        List<JSONObject> list = new ArrayList<JSONObject>();
        try {
            JSONObject data = jsons.getJSONObject("data");
            JSONArray tags = data.getJSONArray("tags");
            for (Object object : tags) {
                Destination destination = new Destination();
                JSONObject obj = (JSONObject) object;
                String tagName = obj.getString("tagName");
                if ("热门".equals(tagName)) {
                    destination.setTagsCity("1");
                }
                destination.setTagName(tagName);
                JSONObject obj1 = getDestination(obj, tagName, destination);
                list.add(obj1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public JSONObject getDestination(JSONObject obj, String title, Destination destination) {
        JSONObject json = new JSONObject();
        try {
            if (title.equals(obj.getString("tagName"))) {
                JSONArray modules = obj.getJSONArray("modules");
                for (Object object2 : modules) {
                    JSONObject obj2 = (JSONObject) object2;
                    if ("2".equals(obj2.getString("moduleId"))) {
                        json = getCity("2", obj2, obj, destination);
                    }
                    if ("3".equals(obj2.getString("moduleId"))) {
                        json = getCity("3", obj2, obj, destination);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public JSONObject getCity(String type, JSONObject json, JSONObject jsons, Destination destination) {
        JSONObject jsonObj = new JSONObject();
        try {
            if (type.equals(json.getString("moduleId"))) {
                if (StringUtils.isNotBlank(String.valueOf(json.get("floors")))) {
                    JSONObject json1 = new JSONObject();
                    if (!"null".equals(String.valueOf(json.get("floors")))) {
                        JSONArray floors = json.getJSONArray("floors");
                        String tagName = jsons.getString("tagName");
                        List<String> list = new ArrayList<String>();
                        for (Object object3 : floors) {
                            JSONObject obj3 = (JSONObject) object3;
                            String floorName = obj3.getString("floorName");
                            destination.setFloorName(floorName);
                            JSONArray element = obj3.getJSONArray("element");
                            String eleName = "";
                            String description = "";
                            String imgUrl = "";
                            for (Object object4 : element) {
                                JSONObject obj4 = (JSONObject) object4;
                                description = obj4.getString("description");
                                imgUrl = obj4.getString("imgUrl");
                                eleName = obj4.getString("eleName");
                                destination.setDestinationId(String.valueOf(UUID.randomUUID()));
                                destination.setDescription(description);
                                destination.setImgUrl(imgUrl);
                                destination.setDestinationCity(eleName);
                                destinationDao.insertDestination(destination);
                                list.add(eleName);
                            }
                            json1.put(floorName, list);
                        }
                        jsonObj.put(tagName, json1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }
}
