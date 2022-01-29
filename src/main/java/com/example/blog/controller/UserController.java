package com.example.blog.controller;

import com.example.blog.annotation.LoginRequired;
import com.example.blog.entity.User;
import com.example.blog.service.UserService;
import com.example.blog.util.BlogUtil;
import com.example.blog.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger= LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Value("${blog.path.domain}")
    private String domain;

    @Value("${blog.path.upload}")
    private String uploadPath;

    @LoginRequired
    @GetMapping("/setting")
    public String settingPage(Model model) {
        return "/front/setting";
    }

    @LoginRequired
    @PostMapping("/upload")
    public String uploadAvatar(MultipartFile avatar, Model model) {
        if (avatar == null) {
            model.addAttribute("error", "您还没有选择图片!");
            return "/front/setting";
        }

        String fileName = avatar.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error", "图片的格式不正确!");
            return "/front/setting";
        }

        // 生成随机文件名
        fileName = BlogUtil.generateUUID() + suffix;
        // 确定文件存放的路径
        File dest = new File(uploadPath + "/" + fileName);
        try {
            // 存储文件
            avatar.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败: " + e.getMessage());
            throw new RuntimeException("上传文件失败,服务器发生异常!", e);
        }
        String avatarUrl = domain + "/user/avatar/" + fileName;
        User user = hostHolder.getUser();
        userService.updateAvatar(user.getId(), avatarUrl);

        return "redirect:/index";
    }

    @GetMapping("/avatar/{fileName}")
    public void getavatar(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 服务器存放路径
        fileName = uploadPath + "/" + fileName;
        // 文件后缀，得到png
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应图片
        response.setContentType("image/" + suffix);
        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败: " + e.getMessage());
        }
    }


}
