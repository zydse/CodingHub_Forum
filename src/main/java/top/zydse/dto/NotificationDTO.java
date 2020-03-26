package top.zydse.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * CreateBy: zydse
 * ClassName: NotificationDTO
 * Description:
 *
 * @Date: 2020/3/16
 */
@Data
public class NotificationDTO implements Serializable {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private Long notifier;
    private Long outerId;
    private String outerTitle;
    private String notifierName;
    private Integer type;
    private String typeName;
}
