package top.zydse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.zydse.dto.QuestionDTO;
import top.zydse.model.Question;
import top.zydse.model.User;
import top.zydse.service.QuestionService;

import javax.servlet.http.HttpServletRequest;

/**
 * CreateBy: zydse
 * ClassName: PublishController
 * Description:
 *
 * @Date: 2020/3/4
 */
@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish/{questionId}")
    public String edit(@PathVariable(name = "questionId")Long questionId,Model model) {
        QuestionDTO question = questionService.findById(questionId);
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tags", question.getTags());
        model.addAttribute("id", question.getId());
        return "publish";
    }

    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

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
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "请登录，登录后才可以发布问题");
            return "publish";
        }
        Question question = new Question();
        question.setId(id);
        question.setTitle(title);
        question.setDescription(description);
        question.setCreator(user.getId());
        String[] tags = tag.split(",");
        for (String s : tags) {
            System.out.println(s);
        }
        questionService.saveOrUpdate(question);
        return "redirect:/";
    }
}
