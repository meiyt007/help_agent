package com.zfsoft.superwindow.service.zsgl;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.superwindow.data.zsgl.ZskDict;

/**
 * @author: kkfan
 * @create: 2020-10-28 14:10:30
 * @description:    知识库分类接口
 */
public interface KnowledgeBaseClassificationService {

    /**
     * 获取知识库分类树
     * @return
     */
    ApiResultSet queryKnowledgeBaseClassificationTree(String parentOid);

    /**
     * 分页获取分类信息
     * @param zskDict
     * @param pageNum
     * @param pageSize
     * @return
     */
    ApiResultSet pageList(ZskDict zskDict,
                          Integer pageNum,
                          Integer pageSize);

    /**
     * 根据parentOid查询分类列表
     * @param parendOid
     * @return
     */
    ApiResultSet queryDictListByParentOid(String parendOid);

    /**
     * 保存/更新分类信息
     * @param zskDict
     * @return
     */
    ApiResultSet saveOrUpdate(ZskDict zskDict);

    ApiResultSet validateRepeat(String zskDictOid, String code, String name, String parentOid);

    /**
     * 递归删除分类信息
     * @param ids
     * @return
     */
    ApiResultSet delete(String ids);

    /**
     * 获取分享详情
     * @param id
     * @return
     */
    ApiResultSet getOne(String id);
}
