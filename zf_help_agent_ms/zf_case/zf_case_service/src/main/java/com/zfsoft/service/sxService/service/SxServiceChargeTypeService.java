package com.zfsoft.service.sxService.service;


import com.zfsoft.service.sxService.data.SxServiceChargeType;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @（#）: SxServiceChargeTypeService
 * @description: 事项收费类型
 * @author: wangwg
 * @date: 2021/06/10
 * @version: 1.1
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@RequestMapping("/affair/sxServiceType")
public interface SxServiceChargeTypeService {

    /**
     * 通过ID查询单条数据
     *
     * @param chargeTypeOid 主键
     * @return 实例对象
     */
    SxServiceChargeType queryById(String chargeTypeOid);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<SxServiceChargeType> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param tSxServiceChargeType 实例对象
     * @return 实例对象
     */
    SxServiceChargeType insert(SxServiceChargeType tSxServiceChargeType);

    /**
     * 修改数据
     *
     * @param tSxServiceChargeType 实例对象
     * @return 实例对象
     */
    SxServiceChargeType update(SxServiceChargeType tSxServiceChargeType);

    /**
     * 通过主键删除数据
     *
     * @param chargeTypeOid 主键
     * @return 是否成功
     */
    boolean deleteById(String chargeTypeOid);

}
