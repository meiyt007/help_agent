package com.zfsoft.ha.data.TripartiteVo;

import lombok.Data;
import lombok.ToString;

/**
 * @Description //附件材料上传响应实体类
 * @Author: Wangyh
 * @Date: 2022/9/22 17:56
 */
@Data
@ToString
public class ResponseaStuffAttaVo {
    /**
     * 材料uuid
     */
    private String stApplyStuffId;
    /**
     * 材料名称
     */
    private String filename;
    /**
     * 附件uuid
     */
    private String stAttachId;
}
