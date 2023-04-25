package com.zfsoft.service.sxDirectory.data;


import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @description:  事项类型信息表
 * @author: wangxl
 * @Date: 2020/10/25
 **/
@Data
@ToString
public class SxServiceType {

    /**
     * 新增，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新，验证规则组
     */
    public interface UPDATE_GROUP{};

    /* 主键  */
    private Long id;

    /* 业务主健  */
	private String serviceTypeOid;

    /* 事项类型名称  */
	private String serviceTypeName;

    /* 事项类型编码  */
	private String serviceTypeCode;

    /* 事项性质  */
	private Short serviceCharacter;

    /* 环节字典外建  */
	private String dictOid;

    /* 是否政务服务目录清单(0否、1是)  */
	private Short isGov;

    /* 是否启用 */
	private Short enabledFlag;
	/* 是否删除*/
	private Short delFlag;
	/* 排序号*/
	private Long sort;
	/**
	 * 创建时间
	 */
	private Date createDate;

	/**
	 * 创建人
	 */
	private String createUser;
	/**
	 * 说明
	 */
	private String remark;
}
