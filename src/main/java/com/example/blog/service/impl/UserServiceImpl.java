package com.example.blog.service.impl;

import com.example.blog.dao.UserMapper;
import com.example.blog.entity.LoginTicket;
import com.example.blog.entity.User;
import com.example.blog.service.UserService;
import com.example.blog.common.Constant;
import com.example.blog.util.MailClient;
import com.example.blog.util.RedisKeyUtil;
import com.example.blog.util.BlogUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${blog.path.domain}")
    private String domain;


    @Override
    public int countUser() {
        return userMapper.selectUserCount();
    }

    @Override
    public List<User> findUserList(int offset, int limit) {
        return userMapper.findUsers(offset,limit);
    }

    @Override
    public List<User> searchUserList(String nickName,String email,Integer role,Integer status, int offset, int limit) {
        return userMapper.searchUserList(nickName,email,role,status,offset,limit);
    }

    @Override
    public int countSearchUser(String nickName, String email, Integer role, Integer status) {
        return userMapper.searchUserCount(nickName,email,role,status);
    }

    @Override
    public User findUserById(int id) {
        User user = getCache(id);
        if (user == null) {
            user = initCache(id);
        }
        return user;
    }

    @Override
    public List<User> findUserByIds(List<Integer> ids) {
        return userMapper.selectByIds(ids);
    }

    @Override
    public User findUserByNickName(String nickName) {
        return userMapper.selectByNickName(nickName);
    }

    @Override
    public Map<String, Object> register(User registerUser,String confirmPassword) {
        Map<String, Object> map = new HashMap<>();
        if (registerUser == null) {
            throw new IllegalArgumentException("?????????????????????");
        }
        if (StringUtils.isBlank(registerUser.getAccount())) {
            map.put("accountMsg", "?????????????????????");
            return map;
        }
        User registeredUser = userMapper.selectByAccount(registerUser.getAccount());
        if (registeredUser != null) {
            map.put("accountMsg", "?????????????????????");
            return map;
        }
        if (StringUtils.isBlank(registerUser.getNickName())) {
            map.put("nameMsg", "?????????????????????");
            return map;
        }
        if(registeredUser != null && registerUser.getNickName().equals(registeredUser.getNickName())){
            map.put("nameMsg", "???????????????");
            return map;
        }
        if (StringUtils.isBlank(registerUser.getPassword())) {
            map.put("passwordMsg", "?????????????????????");
            return map;
        }
        if (registerUser.getPassword().length() < 6) {
            map.put("passwordMsg", "??????????????????6??????");
            return map;
        }
        if(!registerUser.getPassword().equals(confirmPassword)){
            map.put("confirmPasswordMsg","?????????????????????????????????");
            return map;
        }
        if (StringUtils.isBlank(registerUser.getEmail())) {
            map.put("emailMsg", "?????????????????????");
            return map;
        }
        registeredUser = userMapper.selectByEmail(registerUser.getEmail());
        if (registeredUser != null) {
            map.put("emailMsg", "????????????????????????");
            return map;
        }
        // ????????????
        registerUser.setSalt(BlogUtil.generateUUID().substring(0, 5));  //??????5?????????
        registerUser.setPassword(BlogUtil.md5(registerUser.getPassword() + registerUser.getSalt()));
        registerUser.setRole(0);    //0-????????????;
        registerUser.setStatus(0);  //0-?????????;
        registerUser.setActivationCode(BlogUtil.generateUUID());   //?????????????????????
        //???????????????????????????????????????????????????
        registerUser.setAvatar(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        registerUser.setCreateTime(new Date());
        userMapper.insertUser(registerUser);

        // ????????????,??????thymeleaf???????????????????????????
        Context context = new Context();
        context.setVariable("email",registerUser.getEmail());
        //??????????????????,????????????????????????
        String url = domain  + "/activation/" + registerUser.getId() + "/" +registerUser.getActivationCode();
        context.setVariable("url", url);
        //???????????????????????????????????????????????????
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(registerUser.getEmail(), "????????????", content);

        return map;
    }

    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1) {
            return Constant.ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1,new Date());
            clearCache(userId);
            return Constant.ACTIVATION_SUCCESS;
        } else {
            return Constant.ACTIVATION_FAILURE;
        }
    }

    @Override
    public Map<String, Object> login(String account, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        if (StringUtils.isBlank(account)){
            map.put("accountMsg", "?????????????????????");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "?????????????????????");
            return map;
        }

        User user = userMapper.selectByAccount(account);
        if (user == null) {
            map.put("accountMsg", "?????????????????????");
            return map;
        }

        // ????????????
        if (user.getStatus() == 0) {
            map.put("accountMsg", "?????????????????????");
            return map;
        }

        if (user.getStatus() == 2) {
            map.put("accountMsg", "??????????????????????????????????????????xucxun@qq.com???");
            return map;
        }
        // ????????????
        password = BlogUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "??????????????????");
            return map;
        }

        // ????????????????????????cookie
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(BlogUtil.generateUUID());
        loginTicket.setStatus(0);  //0-??????
        //ms????????????????????????1000L??????????????????long?????????????????????????????????
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000L));
        //loginTicketMapper.insertLoginTicket(loginTicket);

        //???ticket??????redis???
        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey, loginTicket);

        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    @Override
    public void logout(String ticket) {
        //???redis??????????????????????????????????????????
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(redisKey, loginTicket);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateAvatar(int userId, String avatar) {
        int rows = userMapper.updateAvatar(userId, avatar);
        clearCache(userId);
        return rows;
    }

    public LoginTicket findLoginTicket(String ticket) {
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);

    }

    public Collection<? extends GrantedAuthority> getAuthorities(int userId){
        User user = this.findUserById(userId);

        List<GrantedAuthority> list = new ArrayList<>();
        list.add((GrantedAuthority) () -> {
            switch (user.getRole()) {
                case 0:
                    return Constant.AUTHORITY_USER;
                case 1:
                    return Constant.AUTHORITY_ADMIN;
                default:
                    return Constant.AUTHORITY_SUPER_ADMIN;
            }
        });
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(int id, Integer status) {
        userMapper.updateStatus(id,status,new Date());
        clearCache(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(int id, Integer role) {
        userMapper.updateRole(id,role,new Date());
        clearCache(id);
    }

    @Override
    public Map<String, Object> changePassword(User user, String oldPassword, String newPassword, String confirmPassword) {
        Map<String, Object> map = new HashMap<>();
        // ???????????????????????????
        oldPassword = BlogUtil.md5(oldPassword + user.getSalt());
        if (StringUtils.isBlank(oldPassword)) {
            map.put("oldPasswordMsg", "????????????????????????");
            return map;
        }
        if (!user.getPassword().equals(oldPassword)) {
            map.put("oldPasswordMsg", "?????????????????????");
            return map;
        }
        if (StringUtils.isBlank(newPassword)) {
            map.put("newPasswordMsg", "????????????????????????");
            return map;
        }
        if (newPassword.length()<6) {
            map.put("newPasswordMsg", "?????????????????????6??????");
            return map;
        }
        if(!newPassword.equals(confirmPassword)){
            map.put("confirmPasswordMsg", "?????????????????????????????????");
            return map;
        }
        int id=user.getId();
        newPassword = BlogUtil.md5(newPassword + user.getSalt());
        if(oldPassword.equals(newPassword)){
            map.put("newPasswordMsg", "??????????????????????????????");
            return map;
        }
        userMapper.updatePassword(id,newPassword);
        clearCache(user.getId());
        return map;
    }

    // 1.????????????????????????
    @Override
    public User getCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }

    // 2.?????????????????????????????????
    @Override
    public User initCache(int userId) {
        User user = userMapper.selectById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        // ?????????????????????Redis,??????????????????60min
        redisTemplate.opsForValue().set(redisKey, user, 3600, TimeUnit.SECONDS);
        return user;
    }

    // 3.?????????????????????????????????
    @Override
    public void clearCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }
}
