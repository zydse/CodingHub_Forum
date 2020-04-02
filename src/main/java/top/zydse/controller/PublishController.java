package top.zydse.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.zydse.dto.QuestionDTO;
import top.zydse.dto.TagTypeDTO;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.exception.CustomizeException;
import top.zydse.mapper.TagMapper;
import top.zydse.model.Question;
import top.zydse.model.Tag;
import top.zydse.model.TagExample;
import top.zydse.model.User;
import top.zydse.provider.SensitiveWordFilter;
import top.zydse.service.QuestionService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * CreateBy: zydse
 * ClassName: PublishController
 * Description:
 *
 * @Date: 2020/3/4
 */
@Controller
@Slf4j
public class PublishController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private SensitiveWordFilter wordFilter;

    @RequiresPermissions({"question:update"})
    @GetMapping("/publish/{questionId}")
    public String edit(@PathVariable(name = "questionId") Long questionId, Model model) {
        QuestionDTO question = questionService.findById(questionId);
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tags", question.getTags());
        model.addAttribute("id", question.getId());
        return "publish";
    }

    @RequiresAuthentication
    @GetMapping("/publish")
    public String publish(Model model) {
        List<Tag> tags = tagMapper.selectByExample(new TagExample());
        List<String> types = tags.stream().map(Tag::getTagType).distinct().collect(Collectors.toList());
        Map<String, List<Tag>> maps = tags.stream()
                .collect(Collectors.groupingBy(Tag::getTagType));
        List<TagTypeDTO> list = new ArrayList<>();
        for (String type : types) {
            TagTypeDTO dto = new TagTypeDTO();
            dto.setType(type);
            dto.setTags(maps.get(type).stream().map(Tag::getTagName).collect(Collectors.toList()));
            list.add(dto);
        }
        model.addAttribute("typeList", list);
        return "publish";
    }

    @RequiresAuthentication
    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("questionTitle") String title,
            @RequestParam("description") String description,
            @RequestParam("tags") String tag,
            @RequestParam(name = "id", required = false) Long id,
            HttpServletRequest request,
            Model model) {
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tags", tag);
        if (title == null || "".equals(title)) {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (description == null || "".equals(description)) {
            model.addAttribute("error", "问题的详细信息不能为空");
            return "publish";
        }
        if (tag == null || "".equals(tag)) {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }
        Set<String> sensitiveTitle = wordFilter.getSensitiveWord(title);
        Set<String> sensitiveDescription = wordFilter.getSensitiveWord(description);
        if (sensitiveTitle.size() != 0) {
            throw new CustomizeException(CustomizeErrorCode.SENSITIVE_WORD_FOUND_IN_TITLE);
        }
        if (sensitiveDescription.size() != 0) {
            throw new CustomizeException(CustomizeErrorCode.SENSITIVE_WORD_FOUND_IN_DESCRIPTION);
        }
        User user = (User) request.getSession().getAttribute("user");
        Question question = new Question();
        question.setId(id);
        question.setTitle(title);
        question.setDescription(description);
        question.setCreator(user.getId());
        questionService.saveOrUpdate(question, tag);
        return "redirect:/question/" + question.getId();
    }
}
