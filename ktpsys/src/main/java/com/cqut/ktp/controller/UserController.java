package com.cqut.ktp.controller;

import com.cqut.ktp.pojo.User;
import com.cqut.ktp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.util.List;

/**
 * @author 欧阳建荣
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/verify")
    public Boolean userVerify(@RequestParam String account, @RequestParam String password, @RequestParam String func) {
        User user = userService.userVerify(account);

        if (user == null) {
            return "register".equalsIgnoreCase(func);
        }
        try {
            // MD5加密
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
            password = base64en.encode(md5.digest(password.getBytes("utf-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password.equals(user.getPassword());
    }

    @GetMapping(value = "/findOneEntity")
    public User findOneEntity(@RequestParam String account) {
        return userService.userVerify(account);
    }

    @GetMapping(value = "/findEntityByUid")
    public User findEntityByUid(@RequestParam String uid) {
        return userService.findEntityByUid(uid);
    }

    @GetMapping(value = "/userList")
    public List<User> selectUsers(@RequestParam String cno) {
        return userService.selectUsers(cno);
    }

    @PostMapping(value = "/register")
    public void register(@RequestBody User user) {
        userService.register(user);
    }

    @PutMapping(value = "/updateUserInfo")
    public void updateUserInfo(@RequestParam(value = "password", required = false) String password,
                               @RequestParam(value = "pid", required = false) Integer pid,
                               @RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "sclID", required = false) Integer sclID,
                               @RequestParam(value = "identifyId", required = false) String identifyNo,
                               @RequestParam String userId) {
        userService.updateUserInfo(password, pid, name, sclID, identifyNo, userId);
    }
}
