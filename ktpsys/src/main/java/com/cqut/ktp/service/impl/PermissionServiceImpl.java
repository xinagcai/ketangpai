package com.cqut.ktp.service.impl;

import com.cqut.ktp.mapper.PermissionMapper;
import com.cqut.ktp.pojo.Permission;
import com.cqut.ktp.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 欧阳建荣
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionMapper mapper;

    @Override
    public List<Permission> selectPermission() {
        return mapper.selectPermission();
    }
}
