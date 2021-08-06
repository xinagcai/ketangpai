package com.cqut.ktp.service.impl;

import com.cqut.ktp.mapper.UserMapper;
import com.cqut.ktp.pojo.User;
import com.cqut.ktp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author 欧阳建荣
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper mapper;

    @Override
    public User userVerify(String account) {
        return mapper.userVerify(account);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<User> selectUsers(String cno) {
        Object savePoint = null;
        try {
            savePoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();

            // 查询该课程有关的用户
            List<User> students = mapper.selectStudents(cno);
            List<User> teachers = mapper.selectTeacherAss(cno);
            List<User> users = new ArrayList<>();
            users.addAll(students);
            users.addAll(teachers);
            users.add(mapper.selectTeach(cno));

            return users;
        } catch (Exception e) {
            assert savePoint != null;
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
            return null;
        }
    }

    @Override
    public User findEntityByUid(String uid) {
        return mapper.findEntityByUid(uid);
    }

    @Override
    public void register(User user) {
        try {
            String pwd = user.getPassword();
            // MD5加密
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
            pwd = base64en.encode(md5.digest(pwd.getBytes("utf-8")));
            String userId = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

            user.setPassword(pwd);
            user.setUserId(userId);

            // 注册前先进行一些的处理
            mapper.register(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUserInfo(String password, Integer pid, String name, Integer school, String identifyNo, String userId) {
        try {
            if (password != null && !"".equals(password)) {
                // MD5加密
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                BASE64Encoder base64en = new BASE64Encoder();
                password = base64en.encode(md5.digest(password.getBytes("utf-8")));
            }

            mapper.updateUserInfo(password, pid, name, school, identifyNo, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
