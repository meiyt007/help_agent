package com.zfsoft.single.controller.ywbl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.single.data.ywbl.vo.CaseMaterialVo;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.single.data.ywbl.QlCaseCorrection;
import com.zfsoft.single.manager.ywbl.QlCaseCorrectionManager;
import com.zfsoft.single.manager.ywbl.QlCorrectionMaterialManager;
import com.zfsoft.single.manager.yxpz.SxServiceRegistrarManager;
import com.zfsoft.single.service.ywbl.QlCaseCorrectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * @author: dongxl
 * @create: 2020-11-17
 * @description: 办件补齐补正
 */
@Slf4j
@RestController
public class QlCaseCorrectionController implements QlCaseCorrectionService {

    @Resource
    private QlCaseCorrectionManager qlCaseCorrectionManager;
    @Resource
    private QlCorrectionMaterialManager qlCorrectionMaterialManager;
    @Resource
    private SxServiceRegistrarManager sxServiceRegistrarManager;


    /**
     * 办件补正列表
     * dongxl
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ApiResultSet queryPageList(QlCaseCorrection qlCaseCorrection, Integer pageNum, Integer pageSize) {

      /* List list= sxServiceRegistrarManager.sxServiceOidsListByUserOid(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid());
       qlCaseCorrection.setServiceOids(list);*/
        PageHelper.startPage(pageNum, pageSize);
        List<QlCaseCorrection> caseCorrectionList = this.qlCaseCorrectionManager.queryPage(qlCaseCorrection);
        if(caseCorrectionList!=null && caseCorrectionList.size()>0){
            PageResult<QlCaseCorrection> pageResult = new PageResult<>(((Page) caseCorrectionList).getPageNum(), ((Page) caseCorrectionList).getPageSize(), ((Page) caseCorrectionList).getTotal());
            pageResult.setData(caseCorrectionList);
            log.info("获取办件补正列表调用成功结果为：{}", JSON.toJSONString(pageResult));
            return new ApiResultSet(pageResult);
        }
        return new ApiResultSet();
    }

    /**
     * 保存办件补正信息
     * dongxl
     * @param vo
     * @return
     */
    @Override
    public ApiResultSet saveOrUpdate( CaseMaterialVo vo ) {
        log.info("办件补正信息新增/更新成功：{}", JSON.toJSONString(vo));
        this.qlCaseCorrectionManager.saveOrUpdate(vo);
        return new ApiResultSet(vo);
    }

    /**
     * 保存办件告知信息
     * dongxl
     * @param qlCaseCorrection
     * @return
     */

    @Override
    public ApiResultSet saveOrUpdateNotice( QlCaseCorrection qlCaseCorrection) {
        log.info("办件告知信息新增/更新成功：{}", JSON.toJSONString(qlCaseCorrection));
        this.qlCaseCorrectionManager.saveOrUpdateNotice(qlCaseCorrection);
        return new ApiResultSet(qlCaseCorrection);
    }

    @Override
    public ApiResultSet<QlCaseCorrection> getQlCorrectByCaseOid(String caseOid) {
        QlCaseCorrection correction=qlCaseCorrectionManager.getQlCorrectByCaseOid(caseOid);
        return new ApiResultSet(correction);
    }

    /**
     * 根据办件编号获取信息
     * dongxl
     * @param caseOid
     * @return
     */

    @Override
    public ApiResultSet getListByCaseOid(String caseOid) {
        List<QlCaseCorrection> outOfStock =  this.qlCaseCorrectionManager.getListByCaseOid(caseOid);
        log.info("根据办件编号获取信息：{}", outOfStock);
        return new ApiResultSet(outOfStock);
    }


    @Override
    public ApiResultSet saveOrUpdateGzBqbz( QlCaseCorrection correction) {
        this.qlCaseCorrectionManager.saveOrUpdateGzBqbz(correction);
        return new ApiResultSet(correction);
    }

    @Override
    public ApiResultSet saveStopCorrection(Long id) {
        Map<String, String> cardNumber=this.qlCaseCorrectionManager.saveStopCorrection(id);
        return new ApiResultSet(cardNumber);
    }

    /**
     * 根据主键获取信息
     * dongxl
     * @return
     */
    @Override
    public ApiResultSet getOneById(Long id) {
        QlCaseCorrection outOfStock = this.qlCaseCorrectionManager.getOneByid(id);
        log.info("根据主键获取信息：{}", outOfStock);
        return new ApiResultSet(outOfStock);
    }

    /**
     * 查询要补正的材料信息
     * @param caseOid
     * @return
     */
    @Override
    public ApiResultSet getOneMaterialInfo(String correctionOid,String caseOid) {
       ApiResultSet materials= qlCorrectionMaterialManager.getMaterialByCorrectionOid(correctionOid,caseOid);
        return materials;
    }

    /**
     * 保存容缺补正信息
     * dongxl
     * @return
     */

    @Override
    public ApiResultSet saveOrUpdateRqbz( CaseMaterialVo vo) {
        this.qlCaseCorrectionManager.saveOrUpdateRqbz(vo);
        return new ApiResultSet(vo);
    }

}
