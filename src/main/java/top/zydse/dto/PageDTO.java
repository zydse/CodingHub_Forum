package top.zydse.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * CreateBy: zydse
 * ClassName: PageDTO
 * Description:
 *
 * @Date: 2020/3/9
 */
@Data
public class PageDTO implements Serializable {
    private List<QuestionDTO> questions;
    private Integer currentPage;
    private Integer pageCount;
    private List<Integer> pages = new ArrayList<>();

    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;

    public void setPagination(int pageCount, Integer page, Integer size) {
        this.pageCount = pageCount;
        currentPage = page;
        showPrevious = page != 1;
        showNext = page != pageCount;
        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);
            }
            if (page + i <= pageCount) {
                pages.add(page + i);
            }
        }
        showFirstPage = !pages.contains(1);
        showEndPage = !pages.contains(pageCount);
        System.out.println(pages);
    }
}
