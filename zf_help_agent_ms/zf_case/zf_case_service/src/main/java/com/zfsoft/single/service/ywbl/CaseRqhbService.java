
package com.zfsoft.single.service.ywbl;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.single.data.ywbl.CaseRqhb;

/**
 * @author: dongxl
 * @create: 2020-11-17
 * @description: 办件容缺补正
 */
public interface CaseRqhbService {

    /**
     * 保存或者更新
     * dongxl
     * @param caseRqhb
     * @return
     */
    ApiResultSet saveOrUpdate(CaseRqhb caseRqhb);

}
