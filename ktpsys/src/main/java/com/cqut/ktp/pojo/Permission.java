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
public class Permission {
    /**
     * 权限
     */
    private Integer pid;
    private String pname;
}
