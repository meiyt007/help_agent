package com.zfsoft.single.data.cpic;

import lombok.*;

import java.util.List;

/**
 * @author sky
 * @version 1.0
 * @description: TODO
 * @date 2021/7/22 13:05
 */
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PicCompressRequest {

    private List<String> serviceIds ;//事项id集合

    private String   publishExplain;//打包说明
}
