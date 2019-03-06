package springboot.serviceImp;

import springboot.entity.Destination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class DestinationServiceImp {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public int insertDestination(Destination destination) {
        String sql = "INSERT INTO tbl_route_destination ( destination_id, tag_name, floor_name, description, img_url, destination_city, tags_city, create_time)" +
                " VALUES ( :destinationId ,:tagName ,:floorName ,:description ,:imgUrl ,:destinationCity ,:tagsCity ,:createTime)";
        Map<String, Object> param = new HashMap<>();
        param.put("destinationId", destination.getDestinationId());
        param.put("tagName", destination.getTagName());
        param.put("floorName", destination.getFloorName());
        param.put("description", destination.getDescription());
        param.put("imgUrl", destination.getImgUrl());
        param.put("destinationCity", destination.getDestinationCity());
        param.put("tagsCity", destination.getTagsCity());
        param.put("createTime", new Date());
        return jdbcTemplate.update(sql, param);
    }
}
