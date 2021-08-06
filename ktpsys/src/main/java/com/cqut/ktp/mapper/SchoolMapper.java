package com.cqut.ktp.mapper;

import com.cqut.ktp.pojo.School;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 欧阳建荣
 */
@Mapper
public interface SchoolMapper {
    /**
     * 查询所有学校的信息
     *
     * @return
     */
    List<School> selectSchool();
}
