package com.zfsoft.ha.data.TripartiteVo;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //类名称
 * @Author: Wangyh
 * @Date: 2022/9/16 11:18
 */
@Data
@ToString
public class WorkAttachmentVo {
    /**
     * 消息头，accessToken，验证是否是已登陆账号
     */
    private String Authorization;
    /**
     * 办件材料Id（主键）
     */
    private String stuffId;
    /**
     * 材料收取方式0为上传电子文件；1为证照库引用；2为快送收取纸质材料；3为窗口收取纸质材料；4为网上申报信息；5为数据共享
     */
    private String stuffType;
    /**
     * 当stuffType为0时必需有
     */
    private String  file;
    /**
     * 材料复用方式，取值（通过数据共享和网络核验、通过电子证照库调取证照、未实现复用）
     */
    private String stuffSource;
    /**
     * 复用材料标识符-电子证照派生编码
     */
    private String sourceLicenseId;
    /**
     * 复用材料标识符-电子材料id(materialId=libraryId)
     */
    private String sourceStuffId;
    /**
     * 附件id(材料表的）
     */
    private String attachmentId;
    /**
     * 材料目录编码（libraryType=catalogCode）
     */
    private String catalogCode;
    /**
     * 电子材料库-附件唯一id
     */
    private String stuffFileId;
    /**
     * 电子材料库-持有人姓名/企业名称
     */
    private String stuffUser;
    /**
     * 电子材料库-持有人证件号/统一信用代码
     */
    private String stuffUserCard;
    /**
     * 电子材料库-持有人证件类型[个人1(身份证)，法人2（统一社会信用代码）]
     */
    private String stuffUserType;
    /**
     * 电子材料库-失效日期yyyy-MM-dd
     */
    private String stuffEndTime;
}
