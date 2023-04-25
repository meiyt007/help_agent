package com.zfsoft.ocr.data.pojo.face;

import com.zfsoft.ocr.data.pojo.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 创建用户人脸组接口，请求信息
 *
 * @Auther cbc
 * @Date 2019/6/22 13:17
 * @Copyright 版权由上海卓繁信息技术股份有限公司拥有
 */
@SuppressWarnings("serial")
@ApiModel(description= "创建用户人脸组接口请求参数")
public class FaceGroupAddRequest extends BaseRequest {

    @ApiModelProperty(value = "用户组ID",required=true)
    private String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    
}
