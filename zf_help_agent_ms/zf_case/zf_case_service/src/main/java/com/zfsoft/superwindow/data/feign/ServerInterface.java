package com.zfsoft.superwindow.data.feign;

import io.swagger.annotations.ApiModel;

/**
 * 服务接口信息表
 *
 * @author : cbc
 * @date : 2020-07-09 15:08:04
 */
@lombok.Data
@ApiModel(description = "服务接口信息表")
public class ServerInterface {
	/**
	 * @COLUMN_EXPLAIN : 主键
	 * @TABLE_COLUMN_TYPE : varchar
	 */
	private String oid;

	/**
	 * @COLUMN_EXPLAIN : 删除标识
	 * @TABLE_COLUMN_TYPE : varchar
	 */
	private String deleteStatus;

	/**
	 * @COLUMN_EXPLAIN : 服务主键
	 * @TABLE_COLUMN_TYPE : varchar
	 */
	private String serverOid ;
	/**
	 * @COLUMN_EXPLAIN : 接口名称
	 * @TABLE_COLUMN_TYPE : varchar
	 */
	private String interfaceName ;
	/**
	 * @COLUMN_EXPLAIN : 接口简介
	 * @TABLE_COLUMN_TYPE : varchar
	 */
	private String interfaceMemo ;
	/**
	 * @COLUMN_EXPLAIN : 请求方式
	 * @TABLE_COLUMN_TYPE : varchar
	 */
	private String requestMethod ;
	/**
	 * @COLUMN_EXPLAIN : 请求地址
	 * @TABLE_COLUMN_TYPE : varchar
	 */
	private String requestAddr ;
	/**
	 * @COLUMN_EXPLAIN : 返回示例
	 * @TABLE_COLUMN_TYPE : varchar
	 */
	private String backExample ;
	/**
	 * @COLUMN_EXPLAIN : 使用说明
	 * @TABLE_COLUMN_TYPE : varchar
	 */
	private String instructions ;
	/**
	 * @COLUMN_EXPLAIN : 是否需要网关加解密处理
	 * @TABLE_COLUMN_TYPE : varchar
	 */
	private String isEncryption ;
	/**
	 * @COLUMN_EXPLAIN : 理论并发量
	 * @TABLE_COLUMN_TYPE : varchar
	 */
	private String concurrentNum ;
	/**
	 * @COLUMN_EXPLAIN :
	 * @TABLE_COLUMN_TYPE : varchar
	 */
	private String interfaceStatus ;
	/**
	 * @COLUMN_EXPLAIN : 启禁用标识
	 * @TABLE_COLUMN_TYPE : varchar
	 */
	private String ableStatus ;

	/**
	 * @COLUMN_EXPLAIN : 是否限流
	 * @TABLE_COLUMN_TYPE : varchar
	 */
	private String limitStatus ;

	/**
	 * @COLUMN_EXPLAIN : 限流策略
	 * @TABLE_COLUMN_TYPE : varchar
	 */
	private String limitStrategy ;

	/**
	 * @COLUMN_EXPLAIN : 流量单位
	 * @TABLE_COLUMN_TYPE : varchar
	 */
	private String flowUnit ;

	/**
	 * @COLUMN_EXPLAIN : 流量次数
	 * @TABLE_COLUMN_TYPE : varchar
	 */
	private Integer flowNum ;

	/**
	 * @COLUMN_EXPLAIN : JSON格式的body参数
	 * @TABLE_COLUMN_TYPE : varchar
	 */
	private String paramsJson ;

	/**
	 * @COLUMN_EXPLAIN : 接口参数类型
	 * @TABLE_COLUMN_TYPE : varchar
	 */
	private String interfaceType;

}
