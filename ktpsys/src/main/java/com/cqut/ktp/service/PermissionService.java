package com.cqut.ktp.service;

import com.cqut.ktp.pojo.Permission;

import java.util.List;

/**
 * @author 欧阳建荣
 */
public interface PermissionService {
    /**
     * 查找权限
     *
     * @return
     */
    List<Permission> selectPermission();
}
