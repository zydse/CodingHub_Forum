package top.zydse.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.zydse.dto.FileResultDTO;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.exception.CustomizeException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * CreateBy: zydse
 * ClassName: FileController
 * Description:
 *
 * @Date: 2020/3/22
 */
@Controller
@Slf4j
public class FileController {

    @RequestMapping("/file/upload")
    @ResponseBody
    public Object upload(@RequestPart("editormd-image-file") MultipartFile file,
                         String guid,
                         HttpServletRequest request) {
        if(file == null){
            throw new CustomizeException(CustomizeErrorCode.BAD_REQUEST);
        }
        String path = request.getServletContext().getRealPath("/posts/");
        String fileName = file.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        File directory = new File(path);
        if(!directory.exists()){
            directory.mkdir();
        }
        try {
            file.transferTo(new File(directory, guid + fileType));
            FileResultDTO fileResultDTO = new FileResultDTO();
            fileResultDTO.setSuccess(1);
            fileResultDTO.setMessage("服务器已成功收到图片...");
            fileResultDTO.setUrl("/posts/" + guid + fileType);
            return fileResultDTO;
        } catch (IOException e) {
            log.info("There is an user encounter some problem while uploads file.");
            FileResultDTO fileResultDTO = new FileResultDTO();
            fileResultDTO.setSuccess(0);
            fileResultDTO.setMessage("上传过程中遇到了一些问题,上传失败了,请稍后重试...");
            return fileResultDTO;
        }
    }
}
