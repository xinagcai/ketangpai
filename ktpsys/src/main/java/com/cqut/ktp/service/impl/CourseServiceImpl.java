package com.cqut.ktp.service.impl;

import com.cqut.ktp.mapper.CourseMapper;
import com.cqut.ktp.mapper.CourseMemberMapper;
import com.cqut.ktp.pojo.Course;
import com.cqut.ktp.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author 欧阳建荣
 */
@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseMapper mapper;
    @Autowired
    private CourseMemberMapper courseMemberMapper;

    @Override
    public Boolean addCourse(Course course) {
        try {
            course.setArchiveStatus(0);
            course.setDeleteStatus(0);
            course.setCno(UUID.randomUUID().toString().replace("-", "").substring(0, 5));
            course.setCreateDate(new Date());

            mapper.addCourse(course);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateCourse(String name, String year, String semester, Integer num, Integer archive, Integer deleteStatus, String cno) {
        Object savePoint = null;
        try {
            // 设置回滚点
            savePoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();

            mapper.updateCourse(name, year, semester, num, archive, deleteStatus, cno);

            if (archive != null) {
                courseMemberMapper.changeAllStatus(cno, archive == 1 ? "1" : "0");
            } else if (deleteStatus != null) {
                courseMemberMapper.changeAllStatus(cno, deleteStatus == 1 ? "2" : "0");
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            assert savePoint != null;
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
            return false;
        }
    }

    @Override
    public Course getCourseByCno(String cno) {
        return mapper.getCourseByCno(cno);
    }

    @Override
    public List<Course> getCourses(String uid) {
        return mapper.getCourses(uid);
    }

    @Override
    public Integer getMembers(String cno) {
        return mapper.getMembers(cno);
    }
}
