package com.cqut.ktp.service.impl;

import com.cqut.ktp.mapper.UploadMapper;
import com.cqut.ktp.mapper.UserMapper;
import com.cqut.ktp.pojo.User;
import com.cqut.ktp.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author 欧阳建荣
 */
@Service
public class UploadServiceImpl implements UploadService {
    @Autowired
    UploadMapper mapper;
    @Autowired
    UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = IOException.class)
    public String upload(MultipartFile file, String uid) {
        Object savePoint = null;
        try {
            String fileName = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
            // 设置回滚点
            savePoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
            // 修改数据库
            mapper.upload(fileName + file.getOriginalFilename(), uid);
            file.transferTo(new File("D:\\learn\\KTPsystem\\ktp\\src\\assets\\avatar\\" + fileName + file.getOriginalFilename()));
            return userMapper.findEntityByUid(uid).getAvatar();
        } catch (SQLException e) {
            return "false";
        } catch (IOException throwables) {
            // 保存失败的时候回滚事件
            assert savePoint != null;
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
            return "false";
        }
    }
}
