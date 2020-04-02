package top.zydse.elasticsearch.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * CreateBy: zydse
 * ClassName: Publish
 * Description:
 *
 * @Date: 2020/4/2
 */
@Data
@Document(indexName = "index_article", type = "publish")
public class Publish {

    @Id
    @Field(type = FieldType.Long, index = false, store = true)
    private Long id;

    @Field(type = FieldType.Text, store = true, analyzer = "ik_smart")
    private String title;

    @Field(type = FieldType.Text, store = true, analyzer = "ik_smart")
    private String content;

    @Field(type = FieldType.Text, store = true, index = false)
    private String avatarUrl;

    @Field(type = FieldType.Long, store = true, index = false)
    private Long creator;

    @Field(type = FieldType.Integer, store = true, index = false)
    private Integer commentCount;

    @Field(type = FieldType.Integer, store = true, index = false)
    private Integer viewCount;

    @Field(type = FieldType.Long, store = true, index = false)
    private Long gmtModified;
}
