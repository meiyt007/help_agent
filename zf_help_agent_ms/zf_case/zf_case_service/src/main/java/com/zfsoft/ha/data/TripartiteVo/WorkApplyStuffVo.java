package com.zfsoft.ha.data.TripartiteVo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Description //增加材料
 * @Author: Wangyh
 * @Date: 2022/9/16 11:05
 */
@Data
@ToString
public class WorkApplyStuffVo {
    /**
     * 是否是主题事项，0：否；1：是
     */
    private String isScene;
    /**
     * 取值范围：0为首次提交、2为补充材料
     */
    private String stuffStatus;

    List<Stuffs> stuffs;

    @Data
    @ToString
    public static class Stuffs{

        /**
         * 材料所属办件id
         */
        private String applyId;

        /**
         * 情形材料主键
         */
        private String rowguid;
        /**
         * 材料所属环节ID,空时默认最新环节
         */
        private String processID;
        /**
         * 材料id，为空时创建新的
         */
        private String applyStuffId;
        /**
         * 材料的办事指南Id,办事指南里的材料必传
         */
        private String stuffDetailId;
        /**
         * 材料名称
         */
        private String stuffName;
        /**
         * 材料编码，办事指南里的材料必传，市级部门对应材料编码
         */
        private String stuffCode;
        /**
         * 补正材料id（stuffStatus=2时必传）
         */
        private String stuffId;
        /**
         * 电子证照派生编码
         */
        private String sourceLicenseId;
        /**
         * 电子材料id(materialId=libraryId)
         */
        private String sourceStuffId;
        /**
         *  材料收取方式0为上传电子文件
         */
        private String stuffType;
        /**
         * 材料复用方式，取值（通过数据共享和网络核验、通过电子证照库调取证照、未实现复用）
         */
        private String stuffSource;
        /**
         * 材料描述/补正意见
         */
        private String desc;
        /**
         * 附件Id
         */
        private String attachId;
        /**
         * 展示顺序，空时，自增
         */
        private String order;
        /**
         * 原件数量,默认1
         */
        private String nmOriginal;

        /**
         * 复印件数量,默认1
         */
        private String nmCopy;

    }

}
