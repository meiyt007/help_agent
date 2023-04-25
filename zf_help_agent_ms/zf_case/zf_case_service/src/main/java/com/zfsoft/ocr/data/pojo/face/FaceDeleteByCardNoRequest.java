package com.zfsoft.ocr.data.pojo.face;

import com.zfsoft.ocr.data.pojo.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @description 
 * @author lmz 
 * @date 2019-07-17 16:06:43
 * @copyright 上海卓繁信息技术股份有限公司版权所有 . 
 */
@SuppressWarnings("serial")
@ApiModel(description= "根据用户id删除人脸接口请求参数")
public class FaceDeleteByCardNoRequest extends BaseRequest {

    @ApiModelProperty(value = "用户id",required=true)
    private String userId;
    
    @ApiModelProperty(value = "用户组id",required=true)
    private String groupId;

    public String getUserId() {
        return userId;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getGroupId() {
        return groupId;
    }


    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    
    
}

