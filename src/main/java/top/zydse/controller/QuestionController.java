package top.zydse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.zydse.dto.QuestionDTO;
import top.zydse.service.QuestionService;

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
    QuestionService questionService;
    @GetMapping("/question/{id}")
    public String list(@PathVariable(name = "id")Long questionId,
                       Model model){
        QuestionDTO questionDTO = questionService.findById(questionId);
        questionService.increaseViewCount(questionDTO.getId());
        model.addAttribute("question",questionDTO);
        return "question";
    }
}
