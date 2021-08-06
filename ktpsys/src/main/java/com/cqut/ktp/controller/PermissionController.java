package com.cqut.ktp.controller;

import com.cqut.ktp.pojo.Permission;
import com.cqut.ktp.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 欧阳建荣
 */
@RestController
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    public List<Permission> selectPermission() {
        return permissionService.selectPermission();
    }
}
