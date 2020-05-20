package top.zydse.service.impl;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zydse.dto.NotificationDTO;
import top.zydse.dto.PaginationDTO;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.enums.NotificationType;
import top.zydse.exception.CustomizeException;
import top.zydse.mapper.CommonExtensionMapper;
import top.zydse.mapper.NotificationMapper;
import top.zydse.model.Notification;
import top.zydse.model.NotificationExample;
import top.zydse.model.User;
import top.zydse.service.NotificationService;

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
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private CommonExtensionMapper extensionMapper;

    public PaginationDTO<NotificationDTO> list(Integer page, Integer size, Long userId) {
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        int totalCount = (int) notificationMapper.countByExample(example);
        if(totalCount == 0){
            return paginationDTO;
        }
        paginationDTO.setPagination(totalCount, page, size);
        int offset = (paginationDTO.getCurrentPage() - 1) * size;
        List<Notification> plainNotification = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        List<NotificationDTO> dtoList = new ArrayList<>();
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
        return notificationMapper.countByExample(notificationExample);
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

    @Transactional
    @Override
    public int readAll(User user) {
        return extensionMapper.readAllNotification(user.getId());
    }

    @Override
    public int deleteNotificationByUserId(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andNotifierEqualTo(userId);
        notificationMapper.deleteByExample(notificationExample);
        notificationExample.clear();
        notificationExample.createCriteria().andReceiverEqualTo(userId);
        notificationMapper.deleteByExample(notificationExample);
        return 0;
    }
}
