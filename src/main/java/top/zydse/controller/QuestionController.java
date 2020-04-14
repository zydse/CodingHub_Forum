package top.zydse.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import top.zydse.dto.*;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.exception.CustomizeException;
import top.zydse.model.Question;
import top.zydse.model.User;
import top.zydse.provider.SensitiveWordFilter;
import top.zydse.service.CommentService;
import top.zydse.service.QuestionService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * CreateBy: zydse
 * ClassName: QuestionController
 * Description:
 *
 * @Date: 2020/3/10
 */
@Controller
@Slf4j
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private SensitiveWordFilter wordFilter;

    @PostMapping("/question/quality/{id}")
    public String quality(@PathVariable(name = "id") Long questionId){
        int result = questionService.quality(questionId);
        return "redirect:/";
    }

    @PostMapping("/question/top/{id}")
    public String top(@PathVariable(name = "id") Long questionId){
        int result = questionService.top(questionId);
        return "redirect:/";
    }

    @RequiresPermissions({"question:delete"})
    @DeleteMapping("/question/{id}")
    public String delete(@PathVariable(name = "id") Long questionId) {
        int result = questionService.deleteById(questionId);
        return "redirect:/";
    }

    @GetMapping("/question/{id}")
    public String get(@PathVariable(name = "id") Long questionId,
                      HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        QuestionDTO questionDTO = questionService.viewQuestion(questionId, user);
        List<CommentDTO> commentDTOList = commentService.listComment(questionId, user);
        List<Question> relatedQuestion = questionService.relatedQuestion(questionDTO);
        request.setAttribute("question", questionDTO);
        request.setAttribute("comments", commentDTOList);
        request.setAttribute("related", relatedQuestion);
        return "question";
    }

    @RequiresPermissions({"question:update"})
    @PutMapping("/question/{id}")
    public String update(@PathVariable(name = "id") Long questionId, Model model) {
        QuestionDTO question = questionService.findById(questionId);
        List<TagTypeDTO> list = questionService.getAllTags();
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tags", question.getTags());
        model.addAttribute("id", question.getId());
        model.addAttribute("typeList", list);
        return "publish";
    }

    @RequiresPermissions("question:create")
    @PostMapping("/question/publish")
    @ResponseBody
    public ResultDTO saveQuestion(@RequestBody QuestionCreateDTO createDTO,
                                  HttpServletRequest request) {
        if (createDTO.getTitle() == null || "".equals(createDTO.getTitle())) {
            return ResultDTO.errorOf(CustomizeErrorCode.QUESTION_INFO_ERROR);
        }
        if (createDTO.getDescription() == null || "".equals(createDTO.getDescription())) {
            return ResultDTO.errorOf(CustomizeErrorCode.QUESTION_INFO_ERROR);
        }
        if (createDTO.getTags() == null || "".equals(createDTO.getTags())) {
            return ResultDTO.errorOf(CustomizeErrorCode.QUESTION_INFO_ERROR);
        }
        Set<String> sensitiveTitle = wordFilter.getSensitiveWord(createDTO.getTitle());
        if (sensitiveTitle.size() != 0) {
            throw new CustomizeException(CustomizeErrorCode.SENSITIVE_WORD_FOUND_IN_TITLE);
        }
        Set<String> sensitiveDescription = wordFilter.getSensitiveWord(createDTO.getDescription());
        if (sensitiveDescription.size() != 0) {
            throw new CustomizeException(CustomizeErrorCode.SENSITIVE_WORD_FOUND_IN_DESCRIPTION);
        }
        User user = (User) request.getSession().getAttribute("user");
        Question question = new Question();
        question.setId(createDTO.getId());
        question.setTitle(createDTO.getTitle());
        question.setDescription(createDTO.getDescription());
        question.setCreator(user.getId());
        questionService.saveOrUpdate(question, createDTO.getTags(), user.getAvatarUrl());
        return ResultDTO.successOf("/question/" + question.getId());
    }

    @RequiresPermissions("question:create")
    @GetMapping("/question/publish")
    public String getPublishPage(Model model) {
        List<TagTypeDTO> list = questionService.getAllTags();
        model.addAttribute("typeList", list);
        return "publish";
    }

    @GetMapping("/question/search")
    public String search(@RequestParam(name = "page", defaultValue = "1") Integer page,
                         @RequestParam(name = "size", defaultValue = "5") Integer size,
                         @RequestParam(name = "search", required = false) String search,
                         Model model) {
        search = search.trim();
        if (!StringUtils.isNotBlank(search)) {
            return "redirect:/";
        }
        PaginationDTO<QuestionDTO> paginationDTO = questionService.findAll(search, page, size);
        List<Long> idList = paginationDTO.getPageData()
                .stream()
                .map(QuestionDTO::getId)
                .collect(Collectors.toList());
        String ids = idList.toString().replace(" ", "");
        model.addAttribute("pagination", paginationDTO);
        model.addAttribute("search", search);
        model.addAttribute("ids", ids.substring(1, ids.length() - 1));
        return "index";
    }

}
