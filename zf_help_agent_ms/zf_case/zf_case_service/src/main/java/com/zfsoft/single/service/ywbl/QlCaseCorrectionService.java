package com.zfsoft.single.service.ywbl;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.single.data.ywbl.QlCaseCorrection;
import com.zfsoft.single.data.ywbl.vo.CaseMaterialVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author: dongxl
 * @create: 2020-11-17
 * @description: 办件补齐补正
 */
@RequestMapping(value = "/qlCaseCorrection")
public interface QlCaseCorrectionService {

    /**
     * 查询办件补齐补正列表
     *dongxl
     */
    @PostMapping(value = "/pageList")
    ApiResultSet queryPageList(QlCaseCorrection qlCaseCorrection, @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNumber, @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize);

    /**
     * 保存或者更新材料信息
     * dongxl
     * @param
     * @return
     */
    @PostMapping(value = "/saveOrUpdate")
    ApiResultSet saveOrUpdate(@RequestBody CaseMaterialVo vo);

    /**
     * 根据办件编号查询信息
     * dongxl
     * @param caseOid
     * @return
     */
    @PostMapping(value = "/getListByCaseOid")
    ApiResultSet getListByCaseOid(@RequestParam(value = "caseOid") String caseOid);

    @PostMapping(value = "/getOneById")
    ApiResultSet getOneById(Long id);

    @PostMapping(value = "/saveOrUpdateGzBqbz")
    ApiResultSet saveOrUpdateGzBqbz(@RequestBody QlCaseCorrection correction);

    /**
     * 保存终止补正操作
     * @param id
     * @return
     */
    @PostMapping(value = "/saveStopCorrection")
    ApiResultSet saveStopCorrection(Long id);

    @PostMapping(value = "/getOneMaterialInfo")
    ApiResultSet getOneMaterialInfo(@RequestParam(value = "correctionOid", required = false) String correctionOid, @RequestParam(value = "caseOid", required = false) String caseOid);

    @PostMapping(value = "/saveOrUpdateRqbz")
    ApiResultSet saveOrUpdateRqbz(@RequestBody CaseMaterialVo vo);

    @PostMapping(value = "/saveOrUpdateNotice")
    ApiResultSet saveOrUpdateNotice(@RequestBody @Validated QlCaseCorrection qlCaseCorrection);

    @RequestMapping(value = "/getQlCorrectByCaseOid",method = RequestMethod.GET)
    ApiResultSet<QlCaseCorrection> getQlCorrectByCaseOid(String caseOid);

}
