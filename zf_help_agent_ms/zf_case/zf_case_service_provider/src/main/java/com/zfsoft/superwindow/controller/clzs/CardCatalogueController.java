package com.zfsoft.superwindow.controller.clzs;


import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.clzs.CardCatalogue;
import com.zfsoft.superwindow.data.clzs.CardCatalogueElement;
import com.zfsoft.superwindow.dbaccess.data.DbCardCatalogue;
import com.zfsoft.superwindow.manager.clzs.CardCatalogueElementManager;
import com.zfsoft.superwindow.manager.clzs.CardCatalogueManager;
import com.zfsoft.superwindow.service.clzs.CardCatalogueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author: liangss
 * @create: 2020-11-04 10:49:29
 * @description: 材料目录控制层
 */
@Slf4j
@RestController
public class CardCatalogueController implements CardCatalogueService {

    @Resource
    private CardCatalogueManager cardCatalogueManager;
    @Resource
    private CardCatalogueElementManager cardCatalogueElementManager;

    @Override
    public ApiResultSet<PageResult> queryCardCatalogueWithPage(String catalogName, Integer pageNum, Integer pageSize) {
        PageResult<CardCatalogue> dbCardCataloguePageResult = cardCatalogueManager.queryCardCatalogueWithPage(catalogName, pageNum, pageSize);
        ApiResultSet apiResultSet  = new ApiResultSet();
        apiResultSet.setData(dbCardCataloguePageResult);
        return apiResultSet;
    }

    @Override
    public ApiResultSet saveOrUpdateCatalogue(CardCatalogue cardCatalogue) throws Exception {
        ApiResultSet apiResultSet = new ApiResultSet();
        cardCatalogueManager.saveOrUpdate(cardCatalogue);
        apiResultSet.setCode(200);
        return apiResultSet;
    }

    @Override
    public ApiResultSet getCardCatalogueByOid(String oid) {
        ApiResultSet apiResultSet = new ApiResultSet();
        CardCatalogue cardCatalogue=new CardCatalogue();
        DbCardCatalogue dbCardCatalogue = cardCatalogueManager.getCardCatalogueByOid(oid);
        List<CardCatalogueElement> subList=cardCatalogueElementManager.getCardCatalogueElementList(oid);
        org.springframework.beans.BeanUtils.copyProperties(dbCardCatalogue,cardCatalogue);
        cardCatalogue.setSubList(subList);
        apiResultSet.setData(cardCatalogue);
        return apiResultSet;
    }

    @Override
    public ApiResultSet deleteByOid(String oid) {
        ApiResultSet apiResultSet = new ApiResultSet();
        cardCatalogueManager.deleteByOid(oid);
        apiResultSet.setCode(200);
        return apiResultSet;
    }

    @Override
    public ApiResultSet getAllCardCatalogueList() {
        ApiResultSet apiResultSet = new ApiResultSet();
        List<CardCatalogue>   cardCatalogueList= cardCatalogueManager.getAllCardCatalogueList();
        apiResultSet.setData(cardCatalogueList);
        apiResultSet.setCode(200);
        return apiResultSet;
    }


}



