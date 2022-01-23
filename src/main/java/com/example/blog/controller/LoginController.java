package com.example.blog.controller;

import com.example.blog.entity.User;
import com.example.blog.service.UserService;
import com.example.blog.util.BlogConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
public class LoginController implements BlogConstant {

    private static final Logger logger= LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String registerPage() {
        return "/front/register";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginPage() {
        return "/front/login";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    //这里跳转的是templates下的内容,这里用return来获取。input的name属性和bean的变量名对应才能成功取到值
    //springmvc user封装到model里，页面直接可以用。confirmPassword从request里可以取到，用param即可
    public String register(Model model, User registerUser, String confirmPassword) {
        Map<String, Object> map = userService.register(registerUser,confirmPassword);
        if (map == null || map.isEmpty()) {
            model.addAttribute("msg", "注册成功,我们已经向您的邮箱发送了一封激活邮件,请尽快激活!");
            model.addAttribute("target","/index");
            model.addAttribute("destination","首页");
            return "/front/operate-result";
        } else {
            model.addAttribute("accountMsg", map.get("accountMsg"));
            model.addAttribute("nameMsg", map.get("nameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("confirmPasswordMsg", map.get("confirmPasswordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/front/register";
        }
    }

    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code) {
        int result = userService.activation(userId, code);
        if (result == ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "激活成功,您的账号已经可以正常使用了!");
            model.addAttribute("target", "/login");
            model.addAttribute("destination","登录页");
        } else if (result == ACTIVATION_REPEAT) {
            model.addAttribute("msg", "无效操作,该账号已经激活过了!");
            model.addAttribute("target", "/login");
            model.addAttribute("destination","登录页");
        } else {
            model.addAttribute("msg", "激活失败,您提供的激活码不正确!");
            model.addAttribute("target", "/index");
            model.addAttribute("destination","首页");
        }
        return "/front/operate-result";
    }


}
