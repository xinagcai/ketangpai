package com.cqut.ktp.mapper;

import com.cqut.ktp.pojo.Permission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 欧阳建荣
 */
@Mapper
public interface PermissionMapper {
    /**
     * 查找权限
     *
     * @return
     */
    List<Permission> selectPermission();
}
