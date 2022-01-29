package com.example.blog.controller;

import com.example.blog.entity.User;
import com.example.blog.service.UserService;
import com.example.blog.util.BlogConstant;
import com.example.blog.util.KaptchaUtil;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
public class LoginController implements BlogConstant {

    private static final Logger logger= LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @GetMapping("/register")
    public String registerPage() {
        return "/front/register";
    }


    @GetMapping("/login")
    public String loginPage() {
//        return "/front/login";
        return("/front/lyear_pages_login_2");
    }

    @PostMapping("/register")
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

    @GetMapping("/activation/{userId}/{code}")
//    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.GET)
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


    @GetMapping("/kaptcha")
    public void getKaptcha(HttpServletResponse response, HttpSession session) {

        int width = 200;
        int height = 69;

        //生成对应宽高的初始图片
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //单独的一个类方法，出于代码复用考虑，进行了封装。功能是生成验证码字符并加上噪点，干扰线，返回值为验证码字符
        String text = KaptchaUtil.drawRandomText(width, height, image);
//        // 生成验证码
//        String text = kaptchaProducer.createText();
//        BufferedImage image = kaptchaProducer.createImage(text);

         //将验证码存入session
         session.setAttribute("kaptcha", text);

//        // 验证码的归属
//        String kaptchaOwner = BlogUtil.generateUUID();
//        Cookie cookie = new Cookie("kaptchaOwner", kaptchaOwner);
//        //失效的时间是10min
//        cookie.setMaxAge(60*10);
//        cookie.setPath(contextPath);
//        response.addCookie(cookie);
//        // 将验证码存入Redis,失效的时间是10min
//        String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
//        redisTemplate.opsForValue().set(redisKey, text, 60*10, TimeUnit.SECONDS);

        // 将图片输出给浏览器
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            logger.error("响应验证码失败:" + e.getMessage());
        }
    }

    @PostMapping("/login")
    public String login(String account, String password, String code, boolean rememberme, Model model,
                        HttpSession session,HttpServletResponse response){

        String kaptcha = (String) session.getAttribute("kaptcha");
        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)){
            model.addAttribute("codeMsg", "验证码不正确!");
            return("/front/lyear_pages_login_2");
        }

        // 检查账号,密码
        int expiredSeconds = rememberme ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(account,password,expiredSeconds);
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            //对项目路径下都有效;设置过期时间，单位是秒
            cookie.setPath("/");
            cookie.setMaxAge(expiredSeconds);
            //登录成功得到cookie，然后将cookie响应给客户端
            response.addCookie(cookie);
            return "redirect:/index";
        } else {
            model.addAttribute("accountMsg", map.get("accountMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return("/front/lyear_pages_login_2");
        }


    }

    @GetMapping("/logout")
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/login";
    }


}
