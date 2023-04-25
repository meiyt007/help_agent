package com.zfsoft.cases.controller;

import com.zfsoft.cases.data.QlCaseSituationTitleValRelation;
import com.zfsoft.cases.manager.QlCaseTitleValueRelationManager;
import com.zfsoft.cases.service.QlCaseSituationTitleValRelationService;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.service.sxSituation.data.SxServiceOptionTitle;
import com.zfsoft.service.sxSituation.data.SxServiceOptionVal;
import com.zfsoft.service.sxSituation.data.SxServiceSituation;
import com.zfsoft.service.sxSituation.service.SxServiceOptionTitleService;
import com.zfsoft.service.sxSituation.service.SxServiceOptionValService;
import com.zfsoft.service.sxSituation.service.SxServiceSituationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**TQlCaseTitleValueRelation
 * (TQlCaseTitleValueRelation)表控制层
 *
 * @author makejava
 * @since 2020-11-30 10:22:45
 */
@RestController
@Slf4j
public class QlCaseTitleValueRelationController implements QlCaseSituationTitleValRelationService {
    /**
     * 服务对象
     */
    @Resource
    private QlCaseTitleValueRelationManager qlCaseTitleValueRelationManager;

    @Resource
    private SxServiceOptionTitleService sxServiceOptionTitleFeginService;

    @Resource
    private SxServiceOptionValService sxServiceOptionValFeginService;

    @Resource
    private SxServiceSituationService sxServiceSituationFeginService;


    @Override
    public ApiResultSet<List<Map<String, String>>> getCaseTitleValueList(String caseOid) {
        Map<String, String> map = null;
        List<Map<String, String>> list= new ArrayList<Map<String, String>>();
        List<QlCaseSituationTitleValRelation> relations  =qlCaseTitleValueRelationManager.queryTitleValueRelationByCaseOid(caseOid);
        if( list != null){
            for (QlCaseSituationTitleValRelation relation : relations) {
                map = new HashMap<String, String>();
                if(!StringUtil.isEmpty(relation.getSituationOid())){
                    ApiResultSet<SxServiceSituation> resultSituation = sxServiceSituationFeginService.getSxServiceSituationByOid(relation.getSituationOid());
                    SxServiceSituation situation = resultSituation.getData();
                    if(situation !=null){
                        map.put("situationName",situation.getSituationName());
                    }else{
                        map.put("situationName","");
                    }
                }else{
                    map.put("situationName","");
                }
                ApiResultSet<SxServiceOptionTitle> resultTitle = sxServiceOptionTitleFeginService.getSxServiceOptionTitleByOid(relation.getTitleOid());
                SxServiceOptionTitle title = resultTitle.getData();
                if(title !=null){
                    map.put("titleName",title.getName());
                    map.put("titleOid",title.getOid());
                    map.put("noticeFormFlag",title.getNoticeFormFlag().toString());
                }
                ApiResultSet<SxServiceOptionVal> resultVal = sxServiceOptionValFeginService.getSxServiceOptionValByOid(relation.getValueOid());
                SxServiceOptionVal val = resultVal.getData();
                if(val !=null){
                    map.put("valueOid",val.getOid());
                    map.put("valueName",val.getName());
                }
                list.add(map);
            }
        }
        ApiResultSet<List<Map<String, String>>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(list);
        return apiResultSet;
    }

}