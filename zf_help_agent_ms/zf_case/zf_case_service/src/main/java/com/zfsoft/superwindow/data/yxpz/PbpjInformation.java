package com.zfsoft.superwindow.data.yxpz;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author wangwg
 *
 * 评价展示信息
 */
@Data
@ToString
public class PbpjInformation {
	
	/**主键*/
	private String oid;
	/**内容*/
	private String information;
	/**时间*/
	private Date dateTime;
	/**状态*/
	private String status;
	/**标题*/
	private String title;
	/**类型*/
	private String type;


}
