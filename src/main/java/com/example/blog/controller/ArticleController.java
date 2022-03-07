package com.example.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.blog.common.EventProducer;
import com.example.blog.entity.*;
import com.example.blog.service.*;
import com.example.blog.common.Constant;
import com.example.blog.util.LoginUser;
import com.example.blog.util.RedisKeyUtil;
import com.example.blog.util.BlogUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/article")
@CrossOrigin
public class ArticleController{

    @Autowired
    private ArticleService articleService;

    @Autowired
    private LoginUser loginUser;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${blog.path.upload}")
    private String uploadPath;

    /**
     * 新增文章页面
     */
    @GetMapping("/input")
    public String toAddBlog(Model model){
        User user = loginUser.getUser();
        if(user == null){
           return "redirect:/login";
        }
        //返回一个article对象给前端th:object
        model.addAttribute("article", new Article());

        List<Category> categories = categoryService.getArticleCategory();
        model.addAttribute("categories",categories);
        return "/front/article-input";
    }

    /**
     * 处理文章图片上传
     *
     * @param file
     * @param request
     * @return
     * @throws IOException
     */
    //博客图片上传问题
    @RequestMapping("/file/upload")
    @ResponseBody
    public JSONObject fileUpload(@RequestParam(value = "editormd-image-file", required = true) MultipartFile file, HttpServletRequest request) throws IOException {
        //上传路径保存设置
        File realPath = new File(uploadPath);
        if (!realPath.exists()) {
            realPath.mkdirs();
        }

        //上传文件地址
        System.out.println("上传文件保存地址：" + realPath);

        //解决文件名字问题：使用uuid;
        String filename =  BlogUtil.generateUUID() + ".jpg";
        File newfile = new File(realPath + "/" + filename);
        //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
        file.transferTo(newfile);

        //给editormd进行回调
        JSONObject res = new JSONObject();
        res.put("url", "/upload/" + filename);
        res.put("success", 1);
        res.put("message", "图片上传成功!");

        return res;
    }

    /**
     * 编辑文章页面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/input/{id}")
    public String toEditBlog(@PathVariable int id,Model model){
        Article article = articleService.getById(id);
        model.addAttribute("article", article);

        List<Category> categories = categoryService.getArticleCategory();
        model.addAttribute("categories",categories);

        return "/front/article-input";
    }



//    @PostMapping("/save")
//    @ResponseBody
//    public String save(Article article){
//        User user = loginUser.getUser();
//        if (user == null) {
//            return BlogUtil.getJsonResult(403, "您还没有登录哦!");
//        }
//        if(StringUtils.isBlank(article.getTitle()) || StringUtils.isBlank(article.getContent())){
//            return BlogUtil.getJsonResult(1, "标题或内容不能为空!");
//        }
//        article.setCategoryId(categoryService.getCategory(article.getCategoryId()).getId());
//        if(article.getId()==0){
//            articleService.add(article);
//            // 新增博客计算博客初始分数
//            String redisKey = RedisKeyUtil.getArticleScoreKey();
//            redisTemplate.opsForSet().add(redisKey, article.getId());
//        }else {
//            articleService.update(article);
//        }
//        // 触发发博客事件，将文章保存到es服务器里
//        Event event = new Event()
//                .setTopic(Constant.TOPIC_PUBLISH)
//                .setUserId(user.getId())
//                .setEntityType(Constant.ENTITY_TYPE_ARTICLE)
//                .setEntityId(article.getId());
//        eventProducer.emitEvent(event);
//
//        return BlogUtil.getJsonResult(0,"发布成功");
//    }


    @PostMapping("/save")
    @ResponseBody
    public String save(String title,Integer categoryId,String content){
        User user = loginUser.getUser();
        if (user == null) {
            return BlogUtil.getJsonResult(403, "您还没有登录哦!");
        }
        if(StringUtils.isBlank(title) || StringUtils.isBlank(content)){
            return BlogUtil.getJsonResult(1, "标题或内容不能为空!");
        }
        if(categoryId==0){
            return BlogUtil.getJsonResult(1, "请选择一个类别!");
        }
        Article article = new Article();
        article.setTitle(title);
        article.setCategoryId(categoryId);
        article.setContent(content);
        if(article.getId()==0){
            articleService.add(article);
            // 新增博客计算博客初始分数
            String redisKey = RedisKeyUtil.getArticleScoreKey();
            redisTemplate.opsForSet().add(redisKey, article.getId());
        }else {
            articleService.update(article);
        }
        // 触发发博客事件，将文章保存到es服务器里
        Event event = new Event()
                .setTopic(Constant.TOPIC_PUBLISH)
                .setUserId(user.getId())
                .setEntityType(Constant.ENTITY_TYPE_ARTICLE)
                .setEntityId(article.getId());
        eventProducer.emitEvent(event);

        return BlogUtil.getJsonResult(0,"发布成功");
    }
    /**
     * 文章详情页
     */
    @GetMapping("/{id}")
    public String articleDetail(@PathVariable("id") int id, Model model, Page page) {
        //文章详情
        Article article = articleService.getById(id);
        model.addAttribute("article",article);

        //文章类别
        Category category = categoryService.getCategory(article.getCategoryId());
        model.addAttribute("category",category);

        User user = userService.findUserById(article.getUserId());
        model.addAttribute("user",user);
        //评论列表
        List<Map<String, Object>> commentVOList = commentService.listComments(id, page.getOffset() ,page.getLimit());
        model.addAttribute("comments", commentVOList);
        //文章点赞数量
        Long likeCount = likeService.countLike(Constant.ENTITY_TYPE_ARTICLE,id);
        model.addAttribute("likeCount",likeCount);
        //点赞状态
        int likeStatus = loginUser.getUser() == null ? 0 : likeService.likeStatus(loginUser.getUser().getId(), Constant.ENTITY_TYPE_ARTICLE,id);
        model.addAttribute("likeStatus",likeStatus);
        // 评论分页
        page.setLimit(5);
        page.setPath("/article/" + id);
        page.setRows(article.getCommentCount());

        // 是否已关注
        boolean isFollowed = false;
        if (loginUser.getUser() != null) {
            isFollowed = followService.isFollowed(loginUser.getUser().getId(), Constant.ENTITY_TYPE_USER, user.getId());
        }
        model.addAttribute("isFollowed", isFollowed);

        // 关注数量
        long followingCount = followService.countFollowing(user.getId(), Constant.ENTITY_TYPE_USER);
        model.addAttribute("followingCount", followingCount);
        // 粉丝数量
        long followerCount = followService.countFollower(Constant.ENTITY_TYPE_USER, user.getId());
        model.addAttribute("followerCount", followerCount);

        return "front/article-detail";
    }

    @PostMapping("/top")
    @ResponseBody
    public String setTop(int id){
        articleService.setTop(id,1);
        Event event = new Event()
                .setTopic(Constant.TOPIC_PUBLISH)
                .setUserId(loginUser.getUser().getId())
                .setEntityType(Constant.ENTITY_TYPE_ARTICLE)
                .setEntityId(id);
        eventProducer.emitEvent(event);

        // 计算博客分数
        String redisKey = RedisKeyUtil.getArticleScoreKey();
        redisTemplate.opsForSet().add(redisKey, id);

        return BlogUtil.getJsonResult(0);
    }

    @PostMapping("/untop")
    @ResponseBody
    public String setunTop(int id){
        articleService.setTop(id,0);
        Event event = new Event()
                .setTopic(Constant.TOPIC_PUBLISH)
                .setUserId(loginUser.getUser().getId())
                .setEntityType(Constant.ENTITY_TYPE_ARTICLE)
                .setEntityId(id);
        eventProducer.emitEvent(event);
        return BlogUtil.getJsonResult(0);
    }

    @PostMapping("/marrow")
    @ResponseBody
    public String setMarrow(int id){
        articleService.setMarrow(id,1);
        Event event = new Event()
                .setTopic(Constant.TOPIC_PUBLISH)
                .setUserId(loginUser.getUser().getId())
                .setEntityType(Constant.ENTITY_TYPE_ARTICLE)
                .setEntityId(id);
        eventProducer.emitEvent(event);
        return BlogUtil.getJsonResult(0);
    }

    @PostMapping("/unmarrow")
    @ResponseBody
    public String setunMarrow(int id){
        articleService.setMarrow(id,0);
        Event event = new Event()
                .setTopic(Constant.TOPIC_PUBLISH)
                .setUserId(loginUser.getUser().getId())
                .setEntityType(Constant.ENTITY_TYPE_ARTICLE)
                .setEntityId(id);
        eventProducer.emitEvent(event);
        return BlogUtil.getJsonResult(0);
    }

    @PostMapping("/delete")
    @ResponseBody
    public String delete(int id){
        articleService.deleteById(id);
        //删除文章下的评论和回复
//        commentService.updateCommentByArticleId(id,1);
//        commentService.updateReplyByArticleId(id,1);
//        // 触发删博客事件
//        Event event = new Event()
//                .setTopic(Constant.TOPIC_DELETE)
//                .setUserId(loginUser.getUser().getId())
//                .setEntityType(Constant.ENTITY_TYPE_ARTICLE)
//                .setEntityId(id);
//        eventProducer.emitEvent(event);
        return BlogUtil.getJsonResult(0,"删除成功!");
    }



}
