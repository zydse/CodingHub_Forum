package top.zydse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.zydse.dto.PaginationDTO;
import top.zydse.dto.QuestionDTO;
import top.zydse.service.QuestionService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CreateBy: zydse
 * ClassName: IndexController
 * Description:
 *
 * @Date: 2020/3/27
 */
@Controller
public class IndexController {
    @Autowired
    private QuestionService questionService;

    @RequestMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "5") Integer size) {
        PaginationDTO<QuestionDTO> pagination = questionService.findAll(page, size);
        List<QuestionDTO> pageData = pagination.getPageData();
        model.addAttribute("pagination", pagination);
        return "index";
    }

}
