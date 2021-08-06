package com.cqut.ktp.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 欧阳建荣
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class School {
    /**
     * 学校id
     */
    private Integer sclID;

    /**
     * 学校名称
     */
    private String sclName;
}
