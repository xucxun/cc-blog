package com.example.blog.controller;

import com.example.blog.annotation.LoginRequired;
import com.example.blog.entity.User;
import com.example.blog.service.FollowService;
import com.example.blog.service.LikeService;
import com.example.blog.service.UserService;
import com.example.blog.util.BlogConstant;
import com.example.blog.util.ResultUtil;
import com.example.blog.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController implements BlogConstant {

    private static final Logger logger= LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

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
        fileName = ResultUtil.generateUUID() + suffix;
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
    public void getAvatar(@PathVariable("fileName") String fileName, HttpServletResponse response) {
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

    @GetMapping("/get")
     public String getByNickName(Model model,String nickName){
        User searchUser = userService.findUserByNickName(nickName);
        if(Objects.isNull(searchUser)){
            return ResultUtil.getJsonResult(1,"用户不存在");
        }
        String nickName1 = searchUser.getNickName();
        String avatar = searchUser.getAvatar();
        model.addAttribute("nickName1",nickName1);
        model.addAttribute("avatar",avatar);
        return  "test/test_chat_v4 :: user";
    }

    /**
     * 个人主页
     */
    @GetMapping( "/{userId}")
    public String getProfilePage(@PathVariable("userId") int userId, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        model.addAttribute("user", user);
        //点赞数量
        int likeCount = likeService.userLikeCount(userId);
        model.addAttribute("likeCount", likeCount);
        // 关注数量
        long followingCount = followService.countFollowing(userId, ENTITY_TYPE_USER);
        model.addAttribute("followingCount", followingCount);
        // 粉丝数量
        long followerCount = followService.countFollower(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        // 是否已关注
        boolean isFollowed = false;
        if (hostHolder.getUser() != null) {
            isFollowed = followService.isFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("isFollowed", isFollowed);
        return "/front/profile";
    }


}
