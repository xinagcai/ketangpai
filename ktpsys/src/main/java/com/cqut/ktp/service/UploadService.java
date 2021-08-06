package com.cqut.ktp.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author 欧阳建荣
 */
public interface UploadService {
    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    String upload(MultipartFile file, String uid);
}
