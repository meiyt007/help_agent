package com.zfsoft.microservice.form.data.vo;

import com.zfsoft.microservice.form.data.FormMain;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @ClassName FormDataVo
 * @Description: 表单数据的vo
 * @Author wuxx
 * @Date 2021/4/22
 **/
@Data
@ToString
public class FormDataVo {

    /**
     * 新增信息验证规则组
     */
    public interface INSERT_GROUP{};

    /**
     * 授权KEY
     */
    @NotNull(message = "授权KEY不能为空",groups = {FormMain.INSERT_GROUP.class})
    private String authorizeKey;

    /**
     * 表单主表的业务主键
     */
    private String formMainOid;

    /**
     * 表单设计的业务主键
     */
    @NotNull(message = "表单设计主键designOid不能为空",groups = {FormMain.INSERT_GROUP.class})
    private String designOid;

    /**
     * 表单设计保存的数据
     */
    @NotNull(message = "表单设计保存的数据formData不能为空",groups = {FormMain.INSERT_GROUP.class})
    private String formData;

    /**
     * 暂存标识  1 -- 暂存
     */
    private Integer isZc;

    /**
     * 表单设计保存的api数据
     */
    private String formApiData;

    /**
     * 表单设计的配置信息
     */
    private String formConfig;

    /**
     * 保存后返回的ID
     */
    private String reportOid;

    /**
     * 数据库存储类型  0本地  1API  2数据库
     */
    private String saveDataType;

    /**
     *  业务关联的key
     */
    private String businessKey;

    /**
     *  创建时间
     */
    private Date createDate;
}
