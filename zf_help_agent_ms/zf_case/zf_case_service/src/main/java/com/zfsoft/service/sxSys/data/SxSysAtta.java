package com.zfsoft.service.sxSys.data;

import lombok.Data;

import java.util.Date;

/**事项附件
 * @author wangxialing
 * @Description:com.zfsoft.sys.data
 * @date 2020/10/22
 */
@Data
public class SxSysAtta {
    /**
     * 主健
     */
    private Long id;
    /**
     * 业务主健
     */
    private String oid;
    /**
     * 附件名称
     */
    private String name;
    /**
     * 附件原始名称
     */
    private String originName;
    /**
     * 附件地址
     */
    private String filePath;
    /**
     * 附件扩展名
     */
    private String extensionName;
    /**
     * 上传时间
     */
    private Date uploadDate;
    /**
     * 接收人编号
     */
    private String userOid;
    /**
     * 删除状态(0否、1是)
     */
    private Integer isDelete;
}
