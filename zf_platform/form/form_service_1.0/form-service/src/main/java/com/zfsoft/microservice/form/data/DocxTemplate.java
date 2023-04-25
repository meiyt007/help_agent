package com.zfsoft.microservice.form.data;


import cn.hutool.core.util.StrUtil;
import com.zfsoft.microservice.form.data.vo.AppendObjectVo;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:  docx模板表
 * @author: wuxx
 * @Date: 2021/12/2 11:51
 **/
@Data
@ToString
public class DocxTemplate {

    /**
     * 新增区划，验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 更新区划，验证规则组
     */
    public interface UPDATE_GROUP{};

    /* 主键 */
    @NotNull(message = "id不能为空",groups = {UPDATE_GROUP.class})
    private Long id;

    /* 业务主键 */
    @NotNull(message = "业务主键不能为空",groups = { UPDATE_GROUP.class})
    private String docxTemplateOid;

    /* 模板名称 */
    @NotNull(message = "模板名称不能为空",groups = { UPDATE_GROUP.class})
    private String docxTemplateName;

    /* 附件 */
    @NotNull(message = "附件不能为空",groups = {INSERT_GROUP.class})
    private String docxAttaOid;

    /**
     * 附件名称
     */
    private String attaName;

    /* 存储对象 */
    @NotNull(message = "存储对象不能为空",groups = {INSERT_GROUP.class})
    private String objectOid;

    /* 存储对象名称 */
    private String objectName;

    /**
     * @COLUMN_EXPLAIN : 授权authorizeKey
     * @TABLE_COLUMN_TYPE : VARCHAR (50)
     */
    @NotNull(message = "授权KEY不能为空",groups = {UPDATE_GROUP.class})
    private java.lang.String authorizeKey;

    /**
     * @COLUMN_EXPLAIN : 删除状态
     * @TABLE_COLUMN_TYPE : VARCHAR (2)
     */
    private Integer isDelete;

    /* 启禁用状态 */
    private Integer isAble;


    private Integer isPdf;


    /**
     * @COLUMN_EXPLAIN : 创建时间
     * @TABLE_COLUMN_TYPE : DATETIME
     */
    private java.util.Date createDate;

    /* 关联存储对象list */
    private List<AppendObjectVo> appendObjectList;

    /**
     *  可能是多个存储对象
     */
    private String appendObjectOids;

    public List<AppendObjectVo> getAppendObjectList() {
        if(StrUtil.isNotEmpty(this.appendObjectOids) && null ==this.appendObjectList){
            String[] appendObjectOidList = appendObjectOids.split(";");
            List<AppendObjectVo> appendObjectList = new ArrayList<>();
            for(String objects : appendObjectOidList){
                String[] keyObject = objects.split("~");
                AppendObjectVo vo = new AppendObjectVo();
                vo.setKey(keyObject[0]);
                vo.setObjectOid(keyObject[1]);
                appendObjectList.add(vo);
            }
            return appendObjectList;
        }
        return appendObjectList;
    }

}
