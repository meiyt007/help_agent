package com.zfsoft.superwindow.data.wgpj;

import lombok.Data;

/**
 * @author sky
 * @version 1.0
 * @description: TODO
 * @date 2021/8/12 16:48
 */
@Data
public class ImageVo {
    private String base64Img;
    private String currentNum;
    //add
    private String  virtualBusinessOid;//虚拟业务记录表主键
}
