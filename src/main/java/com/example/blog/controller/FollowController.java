package com.example.blog.controller;

import com.example.blog.entity.Page;
import com.example.blog.entity.User;
import com.example.blog.service.FollowService;
import com.example.blog.service.UserService;
import com.example.blog.util.BlogConstant;
import com.example.blog.util.HostHolder;
import com.example.blog.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class FollowController implements BlogConstant {

    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @PostMapping ( "/follow")
    @ResponseBody
    public String follow(int entityType, int entityId) {
        User user = hostHolder.getUser();
        followService.follow(user.getId(), entityType, entityId);

        return ResultUtil.getJsonResult(0, "关注用户成功!");
    }

    @PostMapping("/unfollow")
    @ResponseBody
    public String unfollow(int entityType, int entityId) {
        User user = hostHolder.getUser();
        followService.unfollow(user.getId(), entityType, entityId);

        return ResultUtil.getJsonResult(0, "取消关注用户成功!");
    }

    @GetMapping("/user/{userId}/following/")
    public String getFollowings(@PathVariable("userId") int userId, Page page, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        model.addAttribute("user", user);

        page.setLimit(5);
        page.setPath("/user/" + userId +"/following/");
        page.setRows((int) followService.countFollowing(userId, ENTITY_TYPE_USER));

        List<Map<String, Object>> userList = followService.listFollowings(userId, page.getOffset(), page.getLimit());
        if (userList != null) {
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("followingUser");
                map.put("isFollowed", isFollowed(u.getId()));
            }
        }
        model.addAttribute("followingUsers", userList);

        return "/front/following";

    }

    @GetMapping("/user/{userId}/followers/")
    public String getFollowers(@PathVariable("userId") int userId, Page page, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        model.addAttribute("user", user);

        page.setLimit(5);
        page.setPath("/user/"+ userId +"/followers/");
        page.setRows((int) followService.countFollower(ENTITY_TYPE_USER,userId));

        List<Map<String, Object>> userList = followService.listFollowers(userId, page.getOffset(), page.getLimit());
        if (userList != null) {
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("followerUser");
                map.put("isFollowed", isFollowed(u.getId()));
            }
        }
        model.addAttribute("followerUsers", userList);

        return "/front/follower";

    }

    private boolean isFollowed(int userId) {
        if (hostHolder.getUser() == null) {
            return false;
        }
        return followService.isFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
    }


}
