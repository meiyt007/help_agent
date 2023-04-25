package com.zfsoft.superwindow.service.clzs;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.clzs.MaterialCategory;

/**
 * @（#）: MaterialCategoryService
 * @description: 材料类别接口
 * @author: liangss
 * @date: 2020-11-03 10:45:29
 */
public interface MaterialCategoryService {
    ApiResultSet<PageResult<MaterialCategory>> queryMaterialCategoryWithPage(MaterialCategory materialCategory, Integer pageNumber, Integer pageSize);

    ApiResultSet getOne(String id);

    ApiResultSet delete(String id);

    ApiResultSet saveOrUpdate(MaterialCategory materialCategory);

    ApiResultSet checkIsExist(String materialCategoryOid);

    ApiResultSet checkHasRepeat(String materialCategoryOid, String categoryName, String categoryCode);

}
