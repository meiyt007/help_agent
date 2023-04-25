package com.zfsoft.superwindow.dbaccess.dao;

import com.zfsoft.superwindow.dbaccess.data.DbPbpjHistoryManage;
import org.apache.ibatis.annotations.Mapper;

/**
 * 综窗平板设备历史信息表(PbpjHistoryManage)表服务实现类
 *
 * @author: wangwg
 * @date: 2020/12/02
 * @version: 1.0
 * @Copyright: 上海卓繁信息技术股份有限公司版权所有
 */
@Mapper
public interface DbPbpjHistoryManageMapper {

    /**
     * 新增
     *
      * @param dbPbpjHistoryManage
     * @return
     */
    int insert(DbPbpjHistoryManage dbPbpjHistoryManage);

    /**
     * 修改
     *
     * @param dbPbpjHistoryManage
     * @return
     */
    int update(DbPbpjHistoryManage dbPbpjHistoryManage);
}