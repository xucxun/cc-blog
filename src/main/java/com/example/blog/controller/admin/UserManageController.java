package com.example.blog.controller.admin;

import com.example.blog.entity.Page;
import com.example.blog.entity.User;
import com.example.blog.service.ArticleService;
import com.example.blog.service.CategoryService;
import com.example.blog.service.CommentService;
import com.example.blog.service.UserService;
import com.example.blog.util.BlogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class UserManageController {

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/index")
    public String adminPage(Model model) {
        int userCount = userService.countUser();
        model.addAttribute("userCount", userCount);

        int articleCount = articleService.findArticlesRows(0);
        model.addAttribute("articleCount", articleCount);

        int categoryCount = categoryService.countAllCategory();
        model.addAttribute("categoryCount",categoryCount);

        int commentCount = commentService.countAll();
        model.addAttribute("commentCount",commentCount);
        return "admin/index";
    }

    @GetMapping("/userManage")
    public String userManage(Model model, Page page) {
        // 设置分页信息
        page.setLimit(5);
        page.setRows(userService.countUser());
        page.setPath("/admin/userManage");
        // 查询用户列表
        List<User> users = userService.findUserList(page.getOffset(), page.getLimit());
        List<Map<String, Object>> list = new ArrayList<>();
        if (users != null) {
            for (User user : users) {
                Map<String, Object> map = new HashMap<>();
                map.put("user", user);
                list.add(map);
            }
            model.addAttribute("users", list);
        }
        model.addAttribute("userCount",userService.countUser());
//        model.addAttribute("searchUser",new User());
       return"admin/userManage";
    }

    /**
     * 根据用户名、邮箱、角色、状态查询用户列表
     * @return
     */
    @GetMapping("/userManage/search")
    public String searchUser(//**String nickName, String email, Integer role, Integer status,**/
                             User user,
                             Model model,
                             Page page) {
//        model.addAttribute("searchUser",user);
        // 设置分页信息
        page.setLimit(5);
//        page.setRows(userService.countSearchUser(nickName,email,role,status));
//        page.setPath("/admin/userManage/search?nickName="+ nickName + "&email="+email+"&role="+role+"&status="+status);

        page.setRows(userService.countSearchUser(user.getNickName(),user.getEmail(),user.getRole(),user.getStatus()));
        page.setPath("/admin/userManage/search?nickName="+ user.getNickName() + "&email="+user.getEmail()+"&role="+user.getRole()+"&status="+user.getStatus());
        // 查询搜索的用户列表
//        List<User> users = userService.searchUserList(nickName,email,role,status, page.getOffset(), page.getLimit());
        List<User> users = userService.searchUserList(user.getNickName(),user.getEmail(),user.getRole(),user.getStatus(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> list = new ArrayList<>();
        if (users != null) {
            for (User searchUser : users) {
                Map<String, Object> map = new HashMap<>();
                map.put("user", searchUser);
                list.add(map);
            }
            model.addAttribute("users", list);
        }
        model.addAttribute("nickName",user.getNickName());
        model.addAttribute("email",user.getEmail());
        model.addAttribute("role",user.getRole());
        model.addAttribute("status",user.getStatus());
        model.addAttribute("userCount",userService.countSearchUser(user.getNickName(),user.getEmail(),user.getRole(),user.getStatus()));
        return "admin/userManage";
    }



    // 禁用
    @PostMapping("/userManage/disabled")
    @ResponseBody
    public String setDisabled(int id) {
        userService.updateStatus(id,2);
        userService.clearCache(id);
        return BlogUtil.getJsonResult(0);
    }

    // 取消禁用
    @PostMapping("/userManage/unDisabled")
    @ResponseBody
    public String setUnDelete(int id) {
        userService.updateStatus(id, 1);
        userService.clearCache(id);
        return BlogUtil.getJsonResult(0);
    }

    // 激活
    @PostMapping("/userManage/activate")
    @ResponseBody
    public String activate(int id) {
        userService.updateStatus(id, 1);
        userService.clearCache(id);
        return BlogUtil.getJsonResult(0);
    }


    // 设为普通用户
    @RequestMapping( "/userManage/setUser")
    @ResponseBody
    public String setUser(int id) {
        userService.updateRole(id, 0);
        userService.clearCache(id);
        return BlogUtil.getJsonResult(0);
    }

    // 设为管理员
    @PostMapping("/userManage/setAdmin")
    @ResponseBody
    public String setAdmin(int id) {
        userService.updateRole(id, 1);
        userService.clearCache(id);
        return BlogUtil.getJsonResult(0);
    }

    // 设为超级管理员
    @PostMapping("/userManage/setSuperAdmin")
    @ResponseBody
    public String setSuperAdmin(int id) {
        userService.updateRole(id, 2);
        userService.clearCache(id);
        return BlogUtil.getJsonResult(0);
    }


}
