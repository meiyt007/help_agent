package com.zfsoft.superwindow.data.front;

import lombok.*;

import java.util.Date;

/**
 * 平均受理时间接口提供机构类
 * @author wb
 */
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AveragePrescription {
    /**
     * 主键
     */
    private Long id;
    /**
     * 工作人员主键
     */
    private String userOid;
    /**
     * 服务时间
     */
    private String processingTime;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 修改时间
     */
    private Date modifyDate;
    /**
     * 逻辑删除状态 0 未删除 1 删除
     */
    private Integer delFlag;
}
