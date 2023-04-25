package com.zfsoft.superwindow.controller.clzs;


import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.superwindow.data.clzs.CardCatalogueElement;
import com.zfsoft.superwindow.manager.clzs.CardCatalogueElementManager;
import com.zfsoft.superwindow.manager.clzs.CardCatalogueManager;
import com.zfsoft.superwindow.service.clzs.CardCatalogueElementService;
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
public class CardCatalogueElementController implements CardCatalogueElementService {

    @Resource
    private CardCatalogueManager cardCatalogueManager;
    @Resource
    private CardCatalogueElementManager cardCatalogueElementManager;


    @Override
    public ApiResultSet getCardCatalogueElementList(String cardCatalogueOid) {
        ApiResultSet apiResultSet = new ApiResultSet();
        List<CardCatalogueElement> cardCatalogueElementList=cardCatalogueElementManager.getCardCatalogueElementList(cardCatalogueOid);
        apiResultSet.setData(cardCatalogueElementList);
        return apiResultSet;
    }
}



