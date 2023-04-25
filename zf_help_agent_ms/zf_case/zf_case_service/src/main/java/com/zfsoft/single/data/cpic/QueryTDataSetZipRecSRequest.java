package com.zfsoft.single.data.cpic;

import lombok.*;

/**
 * @author sky
 * @version 1.0
 * @description: TODO
 * @date 2021/7/23 9:43
 */
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryTDataSetZipRecSRequest {
    public Integer  pageNum;
    public  Integer pageSize;
}
