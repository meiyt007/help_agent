package com.zfsoft.superwindow.service.easyquickcase.data;


import java.util.List;
import java.util.Map;

/**
 * @Author ChangSheng
 * @Date 11:02 2022/6/20
 * @Description 新增签名人配置实体类
 **/
public class MaterialSignPersonDto {

    //签章人信息集合
    private List<MaterialSignPerson> signList;

    //签署文件集合
    private List<Map<String, String>> fileList;

    // 重定向地址
    private String redirectUrl;

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public List<MaterialSignPerson> getSignList() {
        return signList;
    }

    public void setSignList(List<MaterialSignPerson> signList) {
        this.signList = signList;
    }

    public List<Map<String, String>> getFileList() {
        return fileList;
    }

    public void setFileList(List<Map<String, String>> fileList) {
        this.fileList = fileList;
    }
}
