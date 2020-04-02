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
import top.zydse.model.User;
import top.zydse.service.CommentService;
import top.zydse.service.QuestionService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public String list(@PathVariable(name = "id") Long questionId,
                       HttpServletRequest request,
                       HttpServletResponse response) {
        User user = (User) request.getSession().getAttribute("user");
        boolean viewed = false;
        if (user == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    //游客用户重复浏览
                    if (cookie.getName().equals("viewed")) {
                        viewed = true;
                        break;
                    }
                }
            }
            Cookie isView = new Cookie("viewed", UUID.randomUUID().toString());
            isView.setMaxAge(60 * 60 * 24 * 30);
            response.addCookie(isView);
        }
        QuestionDTO questionDTO = questionService.viewQuestion(questionId, user, viewed);
        List<CommentDTO> commentDTOList = commentService.listComment(questionId, user);
        request.setAttribute("question", questionDTO);
        request.setAttribute("comments", commentDTOList);
        return "question";
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
