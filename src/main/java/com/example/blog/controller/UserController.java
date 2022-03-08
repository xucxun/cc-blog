package com.example.blog.controller;

import com.example.blog.annotation.LoginRequired;
import com.example.blog.entity.*;
import com.example.blog.service.*;
import com.example.blog.common.Constant;
import com.example.blog.util.BlogUtil;
import com.example.blog.util.HtmlToPlainTextUtil;
import com.example.blog.util.LoginUser;
import com.example.blog.util.MarkdownUtils;
import com.example.blog.vo.ArticleVO;
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
public class UserController{

    private static final Logger logger= LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private FollowService followService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private LoginUser loginUser;

    @Value("${blog.path.domain}")
    private String domain;

    @Value("${blog.path.upload}")
    private String uploadPath;

    @GetMapping("/setting")
    public String settingPage(Model model) {
        return "/front/setting";
    }

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
        User user = loginUser.getUser();
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

    //修改个人密码
    @RequestMapping(path = "/changePassword", method = {RequestMethod.GET,RequestMethod.POST})
    public String changePassword(String oldPassword,String newPassword,String confirmPassword, Model model) {
        User user = loginUser.getUser();
        Map<String, Object> map = userService.changePassword(user,oldPassword, newPassword, confirmPassword);
        if(map == null || map.isEmpty()){
            return "redirect:/login";
        }else {
            model.addAttribute("oldPasswordMsg", map.get("oldPasswordMsg"));
            model.addAttribute("newPasswordMsg", map.get("newPasswordMsg"));
            model.addAttribute("confirmPasswordMsg", map.get("confirmPasswordMsg"));
            return "/front/setting";
        }
    }

    @GetMapping("/get")
     public String getByNickName(Model model,String nickName){
        User searchUser = userService.findUserByNickName(nickName);
        if(Objects.isNull(searchUser)){
            return BlogUtil.getJsonResult(1,"用户不存在");
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
    public String getProfilePage(@PathVariable("userId") int userId, Model model, @RequestParam(name = "tab",
                                 defaultValue = "0") int tab, Page page) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        model.addAttribute("user", user);
        //点赞数量
        int likeCount = likeService.userLikeCount(userId);
        model.addAttribute("likeCount", likeCount);
        // 关注数量
        long followingCount = followService.countFollowing(userId, Constant.ENTITY_TYPE_USER);
        model.addAttribute("followingCount", followingCount);
        // 粉丝数量
        long followerCount = followService.countFollower(Constant.ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        // 是否已关注
        boolean isFollowed = false;
        if (loginUser.getUser() != null) {
            isFollowed = followService.isFollowed(loginUser.getUser().getId(), Constant.ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("isFollowed", isFollowed);
        model.addAttribute("tab", tab);

        if(tab == 0) {
            List<ArticleVO> list = articleService.findArticleVOs(userId, page.getOffset(), page.getLimit(), 0);
            List<Map<String, Object>> articleLists = new ArrayList<>();
            if (list != null) {
                for (ArticleVO articleVO : list) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("articleVO", articleVO);
                    articleLists.add(map);
                }
            }
            model.addAttribute("articleLists", articleLists);

            // 设置分页信息
            page.setLimit(10);
            page.setRows(articleService.findArticlesRows(user.getId()));
            page.setPath("/user/" + userId + "?tab=" + tab);
        }else{
            // 设置分页信息
            page.setLimit(10);
            page.setRows(commentService.findCommentCountById(user.getId()));
            page.setPath("/user/" + userId + "?tab=" + tab);

            // 获取用户所有评论不包括回复
            List<Comment> comments = commentService.findCommentsByUserId(user.getId(),page.getOffset(), page.getLimit());
            List<Map<String, Object>> list = new ArrayList<>();
            if (comments != null) {
                for (Comment comment : comments) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("comment", comment);

                    // 根据实体 id 查询对应的博客标题
                    Article article = articleService.getById(comment.getEntityId());
                    map.put("article", article);

                    list.add(map);
                }
                model.addAttribute("comments", list);
            }
            model.addAttribute("tab", tab);

        }
        // 文章数量
        int articleCount = articleService.findArticlesRows(user.getId());
        model.addAttribute("articleCount", articleCount);
        // 评论的数量
        int commentCount = commentService.findCommentCountById(user.getId());
        model.addAttribute("commentCount", commentCount);
        return "/front/profile";
    }

}
