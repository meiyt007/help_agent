package com.zfsoft.superwindow.data.feign;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 服务信息表
 *
 * @author : cbc
 * @date : 2020-07-09 15:08:04
 */
@lombok.Data
@ApiModel(description = "服务信息表")
public class Server {
	/**
	 * @COLUMN_EXPLAIN : 主键
	 * @TABLE_COLUMN_TYPE : varchar
	 */
	private String oid;

	/**
	 * @COLUMN_EXPLAIN : 服务名称
	 * @TABLE_COLUMN_TYPE : varchar
	 */
	private String serverName ;

}
