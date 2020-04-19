package top.zydse.service;

import top.zydse.dto.*;

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

    UserProfileDTO findUserById(Long id);

    PaginationDTO<CollectionDTO> collection(Long id, int page, int size);
}
