package top.zydse.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import top.zydse.exception.CustomizeException;

import javax.servlet.http.HttpServletRequest;

/**
 * CreateBy: zydse
 * ClassName: CustomizeExceptionHandler
 * Description:
 *
 * @Date: 2020/3/11
 */
@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable ex) {
        ModelAndView error = new ModelAndView("error");
        if (ex instanceof CustomizeException) {
            error.addObject("message", ex.getMessage());
        } else {
            error.addObject("message", "服务器要爆炸了，程序员正在抢修");
        }
        return error;
    }
}
