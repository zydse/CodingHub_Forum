package top.zydse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zydse.dto.PaginationDTO;
import top.zydse.dto.ThumbHistoryDTO;
import top.zydse.dto.ViewHistoryDTO;
import top.zydse.mapper.CommonExtensionMapper;
import top.zydse.mapper.ThumbHistoryMapper;
import top.zydse.mapper.ViewHistoryMapper;
import top.zydse.model.ThumbHistoryExample;
import top.zydse.model.ViewHistoryExample;
import top.zydse.service.ProfileService;

import java.util.List;

/**
 * CreateBy: zydse
 * ClassName: ProfileService
 * Description:
 *
 * @Date: 2020/3/29
 */
@Service
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    private CommonExtensionMapper commonExtensionMapper;
    @Autowired
    private ViewHistoryMapper viewHistoryMapper;
    @Autowired
    private ThumbHistoryMapper thumbHistoryMapper;

    public PaginationDTO<ViewHistoryDTO> viewHistory(Integer page, Integer size, Long userId) {
        PaginationDTO<ViewHistoryDTO> paginationDTO = new PaginationDTO<>();
        ViewHistoryExample viewHistoryExample = new ViewHistoryExample();
        viewHistoryExample.createCriteria()
                .andViewerEqualTo(userId);
        viewHistoryExample.setOrderByClause("gmt_modified desc");
        int totalCount = (int) viewHistoryMapper.countByExample(viewHistoryExample);
        if (totalCount == 0) {
            return paginationDTO;
        }
        paginationDTO.setPagination(totalCount, page, size);
        int offset = (paginationDTO.getCurrentPage() - 1) * size;
        List<ViewHistoryDTO> histories = commonExtensionMapper.listViewHistory(userId, offset, size);
        paginationDTO.setPageData(histories);
        return paginationDTO;
    }

    public PaginationDTO<ThumbHistoryDTO> thumbHistory(Integer page, Integer size, Long userId) {
        PaginationDTO<ThumbHistoryDTO> paginationDTO = new PaginationDTO<>();
        ThumbHistoryExample thumbHistoryExample = new ThumbHistoryExample();
        thumbHistoryExample.createCriteria()
                .andThumbUserEqualTo(userId);
        thumbHistoryExample.setOrderByClause("gmt_create desc");
        int totalCount = (int) thumbHistoryMapper.countByExample(thumbHistoryExample);
        if (totalCount == 0) {
            return paginationDTO;
        }
        paginationDTO.setPagination(totalCount, page, size);
        int offset = (paginationDTO.getCurrentPage() - 1) * size;
        List<ThumbHistoryDTO> thumbHistoryDTOS = commonExtensionMapper.listThumbHistory(userId, offset, size);
        paginationDTO.setPageData(thumbHistoryDTOS);
        return paginationDTO;
    }
}
