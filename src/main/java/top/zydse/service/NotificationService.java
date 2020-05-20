package top.zydse.service;

import top.zydse.dto.NotificationDTO;
import top.zydse.dto.PaginationDTO;
import top.zydse.model.User;

/**
 * CreateBy: zydse
 * ClassName: NotificationService
 * Description:
 *
 * @Date: 2020/3/31
 */
public interface NotificationService {
    PaginationDTO<NotificationDTO> list(Integer page, Integer size, Long userId);

    long unreadCount(Long id);

    NotificationDTO read(Long id, User user);

    int readAll(User user);

    int deleteNotificationByUserId(Long userId);
}
