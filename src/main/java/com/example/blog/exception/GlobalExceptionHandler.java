//package com.example.blog.exception;
//
//import com.example.blog.util.ResultUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//
///**
// * 统一异常处理
// */
//@ControllerAdvice(annotations = Controller.class)
//public class GlobalExceptionHandler {
//
//    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
//
//    /**
//     * 自定义异常处理器,
//     */
//    @ExceptionHandler({Exception.class})   // 标识该方法是用来做异常处理的，处理的异常级别为Exception
//    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
//
//        logger.error("请求 URL : {} , 异常信息 : {}",request.getRequestURL(),e.getMessage());
//
//        // 逐条记录错误日志
//        for (StackTraceElement element : e.getStackTrace()) {
//            logger.error(element.toString());
//        }
//
//        String xRequestedWith = request.getHeader("x-requested-with");
//        if ("XMLHttpRequest".equals(xRequestedWith)) {
//            response.setContentType("application/plain;charset=utf-8");
//            PrintWriter writer = response.getWriter();
//            writer.write(ResultUtil.getJsonResult(1, "服务器异常!"));
//        } else {
//            response.sendRedirect(request.getContextPath() + "/error");
//        }
//    }
//
//}
