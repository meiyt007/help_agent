package com.zfsoft.ha.data.requestData;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/7/28 11:17
 */
public class HaUserResourceRequestData {
    /**
     * 资源id
     */
    private Long id;
    /**
     * 资源名称;文件夹名称或文件名称
     */
    private String name;
    /**
     * 父级主键;为null时，为父级
     */
    private Long parentId;
    /**
     * 文件
     */
    private MultipartFile file;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
