package top.zydse.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * CreateBy: zydse
 * ClassName: PaginationDTO
 * Description:
 *
 * @Date: 2020/3/9
 */
@Data
public class PaginationDTO<T> implements Serializable {
    private List<T> pageData;
    private Integer currentPage;
    private Integer pageCount;
    private List<Integer> pages = new ArrayList<>();

    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;

    public void setPagination(int totalCount, int page, int size) {
        int pCount = totalCount % size == 0 ? totalCount / size : totalCount / size + 1;
        pageCount = pCount > 0 ? pCount : 1;
        currentPage = page < 1 ? 1 : page;
        currentPage = page > pageCount ? pageCount : page;
        showPrevious = currentPage != 1;
        showNext = !currentPage.equals(pageCount);
        pages.add(currentPage);
        for (int i = 1; i <= 3; i++) {
            if (currentPage - i > 0) {
                pages.add(0, currentPage - i);
            }
            if (currentPage + i <= pageCount) {
                pages.add(currentPage + i);
            }
        }
        showFirstPage = !pages.contains(1);
        showEndPage = !pages.contains(pageCount);
    }
}
