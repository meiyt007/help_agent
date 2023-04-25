package com.zfsoft.superwindow.dbaccess.dao;

import com.zfsoft.superwindow.dbaccess.data.DbEmotionRecognitionRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 情绪分析记录数据库访问层
 *
 * @author wangwg
 * @since 2021-06-17
 */
@Mapper
public interface DbEmotionRecognitionRecordMapper {

    /**
     * 插入
     *
     * @param dbEmotionRecognitionRecord
     * @return
     */
    int insert(DbEmotionRecognitionRecord dbEmotionRecognitionRecord);

    /**
     * 查询列表
     *
     * @param caseNumber
     * @return
     */
    List<DbEmotionRecognitionRecord> queryEmotionRecognitionList(String caseNumber);



    int update(DbEmotionRecognitionRecord dbEmotionRecognitionRecord);

    List<DbEmotionRecognitionRecord> queryEmotionRecognitionListByVirtualBusinessOidAndCaseId(@Param("virtualBusinessOid") String virtualBusinessOid, @Param("caseOid") String caseOid);

    List<DbEmotionRecognitionRecord> queryEmotionRecognitionListByVirtualBusinessOidAndCaseOidIsNull(@Param("virtualBusinessOid") String virtualBusinessOid);

    List<DbEmotionRecognitionRecord> queryAll(DbEmotionRecognitionRecord dbEmotionRecognitionRecord);

    DbEmotionRecognitionRecord queryById(Long id);
}
