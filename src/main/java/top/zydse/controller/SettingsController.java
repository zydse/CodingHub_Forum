package top.zydse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * CreateBy: zydse
 * ClassName: SettingsController
 * Description:
 *
 * @Date: 2020/4/19
 */
@Controller
@RequestMapping("/settings")
public class SettingsController {

    @GetMapping("/{type}")
    public String profile(@PathVariable("type")String type,
                          Model model){
        model.addAttribute("type", type);
        return "settings";
    }

//    @GetMapping("/phone")
//    public String phone(Model model){
//        return "settings";
//    }
//
//    @GetMapping("/avatar")
//    public String avatar(Model model){
//        return "settings";
//    }
//
//    @GetMapping("/password")
//    public String password(Model model){
//        return "settings";
//    }
}
