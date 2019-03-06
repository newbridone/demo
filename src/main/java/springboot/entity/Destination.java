package springboot.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Destination {
    //ID
    private String destinationId;

    //热门标识
    private String tagsCity;

    //一级分类
    private String tagName;

    //二级分类
    private String floorName;

    //描述
    private String description;

    //图片地址
    private String imgUrl;

    //目的地
    private String destinationCity;

    //创建时间
    private Date createTime;
}
