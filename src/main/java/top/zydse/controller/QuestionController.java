package top.zydse.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import top.zydse.dto.CommentDTO;
import top.zydse.dto.PaginationDTO;
import top.zydse.dto.QuestionDTO;
import top.zydse.enums.CommentTypeEnum;
import top.zydse.service.CommentService;
import top.zydse.service.QuestionService;

import java.util.List;

/**
 * CreateBy: zydse
 * ClassName: QuestionController
 * Description:
 *
 * @Date: 2020/3/10
 */
@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String list(@PathVariable(name = "id")Long questionId,Model model){
        QuestionDTO questionDTO = questionService.findById(questionId);
        List<CommentDTO> commentDTOList = commentService.listByParentId(questionId, CommentTypeEnum.QUESTION);
        questionService.increaseViewCount(questionDTO.getId());
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",commentDTOList);
        return "question";
    }

    @GetMapping("/question/search")
    public String search(@RequestParam(name = "page", defaultValue = "1") Integer page,
                         @RequestParam(name = "size", defaultValue = "5") Integer size,
                         @RequestParam(name = "search", required = false) String search,
                         Model model){
        search = search.trim();
        if(!StringUtils.isNotBlank(search)){
            return "redirect:/";
        }
        PaginationDTO<QuestionDTO> paginationDTO = questionService.findAll(search, page, size);
        model.addAttribute("pagination",paginationDTO);
        model.addAttribute("search", search);
        return "index";
    }
}
