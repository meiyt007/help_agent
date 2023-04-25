package com.zfsoft.superwindow.service.zsgl;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.superwindow.data.zsgl.KnowledgeBase;

/**
 * @author: kkfan
 * @create: 2020-10-29 15:23:23
 * @description:    知识库接口
 */
public interface KnowledgeBaseService {
    /**
     * 分页获取知识库列表信息
     * @param knowledgeBase
     * @param pageNum
     * @param pageSize
     * @return
     */
    ApiResultSet queryPageList(KnowledgeBase knowledgeBase,
                               Integer pageNum,
                               Integer pageSize);

    /**
     * 保存/更新知识库信息
     * @param knowledgeBase
     * @return
     */
    ApiResultSet saveOrUpdate(KnowledgeBase knowledgeBase);

    /**
     * 递归删除知识库信息
     * @param ids
     * @return
     */
    ApiResultSet delete(String ids);

    /**
     * 获取知识库信息详情
     * @param id
     * @return
     */
    ApiResultSet getOne(String id);
    /**
     * 检测是否有关联
     * @param zskDictOid
     * @return
     */
    ApiResultSet checkIsExist(String zskDictOid);
}
