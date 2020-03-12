package top.zydse.advice;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import top.zydse.dto.ResultDTO;
import top.zydse.exception.CustomizeException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * CreateBy: zydse
 * ClassName: CustomizeExceptionHandler
 * Description:
 *
 * @Date: 2020/3/11
 */
@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(CustomizeException.class)
    Object handle(Throwable ex, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView error = new ModelAndView("error");
        String type = request.getContentType();
        if ("application/json".equals(type)) {
            try {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json;charset=utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(ResultDTO.errorOf((CustomizeException) ex)));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            if (ex instanceof CustomizeException)
                error.addObject("message", ex.getMessage());
            return error;
        }
    }
}