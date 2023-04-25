package com.zfsoft.service.sxService.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 前置事项核验信息
 * @author chenyq
 * @date 20220324
 */
@Data
@ToString
@ApiModel(value="sxServicePrecheck", description="一件事前置核验对象")
public class SxServicePrecheck {
    /**
     *id 主键
     */
    private Long id;

    /**
     * 业务主键
     */
    @ApiModelProperty(value="业务主键id",name="precheckOid",example="3ec828931d294a8faf7d3daa76866d26")
    private String precheckOid;

    /**
     * 事项主键
     */
    @ApiModelProperty(value="事项主键id",name="serviceOid",example="3ec828931d294a8faf7d3daa76866d26")
    private String serviceOid;

    /**
     * 前置核验名称
     */
    @ApiModelProperty(value="前置核验名称",name="precheckName",example="百度网站")
    private String precheckName;

    /**
     * 前置核验地址
     */
    @ApiModelProperty(value="前置核验地址",name="precheckAddress",example="https://www.baidu.com")
    private String precheckAddress;

    /**
     * 排序
     */
    @ApiModelProperty(value="排序",name="sort",example="1")
    private Integer sort;

    /**
     * 前置核验附件地址
     */
    @ApiModelProperty(value="前置核验附件地址",name="attaOid",example="3ec828931d294a8faf7d3daa76866d26")
    private String attaOid;

    /**
     * 前置核验创建时间
     */
    private Date createDate;

    /**
     * 删除标识
     */
    private Integer delFlag;

    public SxServicePrecheck(){

    }
    public SxServicePrecheck(String precheckOid,int delFlag){
        this.precheckOid = precheckOid;
        this.delFlag = delFlag;
    }

    /**
     * 附件名称
     */
    private String attaName;


    /**
     * 附件地址
     */
    private String attaUrl;
}
