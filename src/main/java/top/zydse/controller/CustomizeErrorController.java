package top.zydse.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import top.zydse.enums.CustomizeErrorCode;

import javax.servlet.http.HttpServletRequest;

/**
 * CreateBy: zydse
 * ClassName: CustomizeErrorController
 * Description:
 *
 * @Date: 2020/3/11
 */
@Controller
@RequestMapping({"${server.error.path:${error.path:/error}}"})
@Slf4j
public class CustomizeErrorController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "error";
    }

    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletRequest request) {
        log.info("Now you are in errorController");
        HttpStatus status = getStatus(request);
        ModelAndView error = new ModelAndView("error");
        if (status.is4xxClientError()) {
            error.addObject("message", CustomizeErrorCode.BAD_REQUEST.getMessage());
        }
        error.addObject("message", CustomizeErrorCode.SYSTEM_ERROR.getMessage());
        return error;
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
