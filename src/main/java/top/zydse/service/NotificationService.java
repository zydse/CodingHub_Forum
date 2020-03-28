package top.zydse.service;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zydse.dto.NotificationDTO;
import top.zydse.dto.PaginationDTO;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.enums.NotificationType;
import top.zydse.exception.CustomizeException;
import top.zydse.mapper.CommentMapper;
import top.zydse.mapper.NotificationMapper;
import top.zydse.model.Comment;
import top.zydse.model.Notification;
import top.zydse.model.NotificationExample;
import top.zydse.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateBy: zydse
 * ClassName: NotificationService
 * Description:
 *
 * @Date: 2020/3/16
 */
@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private CommentMapper commentMapper;

    public PaginationDTO<NotificationDTO> list(Integer page, Integer size, Long userId) {
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        int totalCount = (int) notificationMapper.countByExample(example);
        paginationDTO.setPagination(totalCount, page, size);
        int offset = (paginationDTO.getCurrentPage() - 1) * size;
        List<Notification> plainNotification = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        List<NotificationDTO> dtoList = new ArrayList<>();
        if(plainNotification.size() == 0){
            return paginationDTO;
        }
        for (Notification notification : plainNotification) {
            NotificationDTO dto = new NotificationDTO();
            BeanUtils.copyProperties(notification, dto);
            dto.setTypeName(NotificationType.nameOf(notification.getType()));
            dtoList.add(dto);
        }
        paginationDTO.setPageData(dtoList);
        return paginationDTO;
    }

    public long unreadCount(Long id) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(id).andStatusEqualTo(0);
        long count = notificationMapper.countByExample(notificationExample);
        return count;
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if(notification == null)
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        if(!notification.getReceiver().equals(user.getId()))
            throw new CustomizeException(CustomizeErrorCode.AUTHORITY_ERROR);
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationType.nameOf(notification.getType()));
        notification.setStatus(1);
        notificationMapper.updateByPrimaryKey(notification);
        return notificationDTO;
    }
}
