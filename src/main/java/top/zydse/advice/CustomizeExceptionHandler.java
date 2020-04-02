package top.zydse.advice;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import top.zydse.dto.ResultDTO;
import top.zydse.enums.CustomizeErrorCode;
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
@Slf4j
@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    Object handle(Throwable ex, HttpServletRequest request, HttpServletResponse response) {
        String accept = request.getHeader("Accept").split(",")[0];
        ModelAndView error = new ModelAndView("error");
        if ("application/json".equals(accept)) {
            try {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json;charset=utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(ResultDTO.errorOf(CustomizeErrorCode.SYSTEM_ERROR)));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            error.addObject("message", CustomizeErrorCode.SYSTEM_ERROR.getMessage());
            return error;
        }
    }

    @ExceptionHandler(CustomizeException.class)
    Object customizeHandler(Throwable ex, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView error = new ModelAndView("error");
        String accept = request.getHeader("Accept").split(",")[0];
        if ("application/json".equals(accept)) {
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
            error.addObject("message", ex.getMessage());
            return error;
        }
    }

    @ExceptionHandler(UnauthenticatedException.class)
    void unauthenticated(HttpServletRequest request, HttpServletResponse response) {
        String accept = request.getHeader("Accept").split(",")[0];
        if ("application/json".equals(accept)) {
            try {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json;charset=utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN)));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                response.sendRedirect("/user/toLogin");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ExceptionHandler(UnauthorizedException.class)
    void unauthorized(HttpServletRequest request, HttpServletResponse response){
        String accept = request.getHeader("Accept").split(",")[0];
        if ("application/json".equals(accept)) {
            try {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json;charset=utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(ResultDTO.errorOf(CustomizeErrorCode.AUTHORITY_ERROR)));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                response.sendRedirect("/401");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}