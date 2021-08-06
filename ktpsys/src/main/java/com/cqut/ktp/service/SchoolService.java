package com.cqut.ktp.service;

import com.cqut.ktp.pojo.School;

import java.util.List;

/**
 * @author 欧阳建荣
 */
public interface SchoolService {
    /**
     * 查询所有学校的信息
     *
     * @return
     */
    List<School> selectSchool();
}
