package com.zfsoft.platform.utils.data;


import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @description:  附件信息表
 * @author: wuxx
 * @Date: 2020/9/12 11:51
 **/
@Data
@ToString
public class SysAttaTemp {

    /* 主键 */
    private Long id;

    /* 附件业务主键 */
    private String attaOid;

    /* 附件名称 */
    private String name;

    /* 附件原始名称 */
    private String originName;

    /* 附件地址 */
    private String filePath;

    /* 附件存储路径 */
    private String savePath;

    /* 附件扩展名 */
    private String extensionName;

    /* 上传时间 */
    private Date uploadDate;

    /* 接收人编号 */
    private String userOid;

    /* 删除状态 */
    private Integer isDelete;

    /**
     * 文件大小  （因网站前台用户中心我的自制材料上传需要控制文件大小才添加）
     */
    private String fileSize;

    /**
     * @COLUMN_EXPLAIN : 是否可以交换  修改为Y 时代表当前数据可以交换
     * @TABLE_COLUMN_TYPE : VARCHAR
     */
    private String canChangeFlag;

    /* 数据来源。为空时代表本系统创建，可进行数据交换。1：平台 2：网站 3：其他推送数据 */
    private Integer source;

    /**
     * @COLUMN_EXPLAIN :  FASTDFS上传URL
     * @TABLE_COLUMN_TYPE : VARCHAR(2)
     */
    private String fastdfsUploadUrl;

    /**
     * @COLUMN_EXPLAIN :  Nginx访问URL
     * @TABLE_COLUMN_TYPE : VARCHAR(2)
     */
    private String fastdfsNginxUrl;

}
