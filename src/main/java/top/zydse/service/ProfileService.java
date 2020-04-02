package top.zydse.service;

import top.zydse.dto.PaginationDTO;
import top.zydse.dto.ThumbHistoryDTO;
import top.zydse.dto.ViewHistoryDTO;

/**
 * CreateBy: zydse
 * ClassName: ProfileService
 * Description:
 *
 * @Date: 2020/3/31
 */
public interface ProfileService {
    PaginationDTO<ViewHistoryDTO> viewHistory(Integer page, Integer size, Long userId);

    PaginationDTO<ThumbHistoryDTO> thumbHistory(Integer page, Integer size, Long userId);
}
