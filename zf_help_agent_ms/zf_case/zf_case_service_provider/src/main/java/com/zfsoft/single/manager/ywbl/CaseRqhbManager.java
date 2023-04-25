package com.zfsoft.single.manager.ywbl;

import com.zfsoft.single.data.ywbl.CaseRqhb;
import com.zfsoft.single.dbaccess.dao.ywbl.DbCaseRqhbMapper;
import com.zfsoft.single.dbaccess.data.ywbl.DbCaseRqhb;
import com.zfsoft.single.util.BeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/**
 * @author: dongxl
 * @create: 2020-11-17
 * @description: 办件容缺后补
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CaseRqhbManager {

    private final DbCaseRqhbMapper dbCaseRqhbMapper;

    /**
     * 办件补正记录保存
     * dongxl
     * @param caseRqhb
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(CaseRqhb caseRqhb) {
        if(caseRqhb!=null){
            DbCaseRqhb caseAudit=new DbCaseRqhb();
            BeanUtils.copyProperties(caseRqhb,caseAudit);
            this.dbCaseRqhbMapper.insertSelective(caseAudit);
        }
    }
}
