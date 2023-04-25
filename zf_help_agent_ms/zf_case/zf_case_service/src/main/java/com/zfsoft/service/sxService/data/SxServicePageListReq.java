package com.zfsoft.service.sxService.data;

import lombok.Data;
import lombok.ToString;

/**
 * @author sky
 * @version 1.0
 * @description: TODO
 * @date 2021/7/27 14:45
 */
@Data
@ToString
public class SxServicePageListReq {
    private int pageNum;//第几页
    private int pageSize;//每页几条数据
    private String materialSampleName;//材料样本名称
}
