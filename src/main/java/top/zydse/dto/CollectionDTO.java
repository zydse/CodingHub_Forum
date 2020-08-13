package top.zydse.dto;

import lombok.Data;

/**
 * CreateBy: zydse
 * ClassName: CollectionDTO
 * Description:
 *
 * @Date: 2020/4/19
 */
@Data
public class CollectionDTO {
    Long id;
    Long userId;
    Long questionId;
    String title;
    String name;
    String avatarUrl;
    Integer viewCount;
    Integer commentCount;
    /*收藏信息创建的时间*/
    Long gmtCreate;
    /*问题被修改的时间*/
    Long gmtModified;

}
