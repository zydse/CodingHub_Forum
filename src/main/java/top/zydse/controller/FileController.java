package top.zydse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.zydse.dto.FileResultDTO;

/**
 * CreateBy: zydse
 * ClassName: FileController
 * Description:
 *
 * @Date: 2020/3/22
 */
@Controller
public class FileController {

    @RequestMapping("/file/upload")
    @ResponseBody
    public Object upload() {
        FileResultDTO fileResultDTO = new FileResultDTO();
        fileResultDTO.setSuccess(1);
        fileResultDTO.setUrl("/favicon.ico");
        return fileResultDTO;
    }
}
