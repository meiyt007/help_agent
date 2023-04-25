package com.zfsoft.cases.data.vo;

import lombok.Data;

/**
 * 办件材料信息
 * @ClassName CaseMaterialVo
 * @Author WangKe
 * @Date 2022/06/10
 **/
@Data
public class CaseMaterialAttaInfoVo {

    /**
     * 主键
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
     * 附件扩展名
     */
    private String extensionName;
}
