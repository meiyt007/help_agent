package com.zfsoft.single.controller.yxpz;


import com.alibaba.fastjson.JSON;
import com.zfsoft.cases.service.SysAttaService;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.single.data.yxpz.AhsSamplePicInfo;
import com.zfsoft.single.data.yxpz.ExaminePointCarding;
import com.zfsoft.single.data.yxpz.vo.ExaminePointCardingVo;
import com.zfsoft.single.manager.yxpz.AhsSamplePicInfoManager;
import com.zfsoft.single.manager.yxpz.ExaminePointCardingManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author: liangss
 * @create: 2020-11-16 15:01:29
 * @description: 审查要点控制层
 */
@Slf4j
@RestController
@RequestMapping(value = "/examinePointCarding")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExaminePointCardingController {
    //审查要点
    private final ExaminePointCardingManager examinePointCardingManager;

    //样表
    private final AhsSamplePicInfoManager ahsSamplePicInfoManager;

    private final SysAttaService qlSysAttaFeignService;



    /**
     * 保存/更新样表信息
     *
     * @param ahsSamplePicInfo
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/saveOrUpdateAhsSamplePicInfo")
    public ApiResultSet saveOrUpdateAhsSamplePicInfo(@RequestBody AhsSamplePicInfo ahsSamplePicInfo) throws Exception {
        this.ahsSamplePicInfoManager.saveOrUpdate(ahsSamplePicInfo);
        log.info("保存/更新样表信息成功：{}", JSON.toJSONString(ahsSamplePicInfo));
        return new ApiResultSet(ahsSamplePicInfo);
    }

    /**
     * 根据样表信息查询列表分页
     *
     * @param ahsSamplePicInfo
     * @return
     */
    @PostMapping(value = "/queryAhsSamplePicInfoWithPage")
    public ApiResultSet queryAhsSamplePicInfoWithPage(@RequestBody AhsSamplePicInfo ahsSamplePicInfo, Integer pageNum, Integer pageSize) {
        Map<String, Object> resultMap = new HashMap<>();
        AhsSamplePicInfo ahsSamplePicInfo1 = new AhsSamplePicInfo();
        ahsSamplePicInfo1.setMateriaOid(ahsSamplePicInfo.getMateriaOid());
        log.info("参数数据：{}", JSON.toJSONString(ahsSamplePicInfo));
        if (StringUtils.isNotEmpty(ahsSamplePicInfo.getSampleInfoOid())) {
            ahsSamplePicInfo1.setSampleInfoOid(ahsSamplePicInfo.getSampleInfoOid());
        }
        if (StringUtils.isNotEmpty(ahsSamplePicInfo.getComboDirectoryOid())) {
            ahsSamplePicInfo1.setComboDirectoryOid(ahsSamplePicInfo.getComboDirectoryOid());
        }
        PageResult<AhsSamplePicInfo> pageResult = this.ahsSamplePicInfoManager.queryAhsSamplePicInfoWithPage(ahsSamplePicInfo, pageNum, pageSize);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }


    /**
     * 根据样表信息查询列表
     *
     * @param ahsSamplePicInfo
     * @return
     */
    @PostMapping(value = "/getAhsSamplePicInfoList")
    public ApiResultSet getAhsSamplePicInfoList(@RequestBody AhsSamplePicInfo ahsSamplePicInfo) {
        Map<String, Object> resultMap = new HashMap<>();
        AhsSamplePicInfo ahsSamplePicInfo1 = new AhsSamplePicInfo();
        ahsSamplePicInfo1.setMateriaOid(ahsSamplePicInfo.getMateriaOid());
        log.info("参数数据：{}", JSON.toJSONString(ahsSamplePicInfo));
        if (StringUtils.isNotEmpty(ahsSamplePicInfo.getSampleInfoOid())) {
            ahsSamplePicInfo1.setSampleInfoOid(ahsSamplePicInfo.getSampleInfoOid());
        }
        if (StringUtils.isNotEmpty(ahsSamplePicInfo.getMateriaOid())) {
            ahsSamplePicInfo1.setMateriaOid(ahsSamplePicInfo.getMateriaOid());
        }
        if (StringUtils.isNotEmpty(ahsSamplePicInfo.getComboDirectoryOid())) {
            ahsSamplePicInfo1.setComboDirectoryOid(ahsSamplePicInfo.getComboDirectoryOid());
        }
        List<AhsSamplePicInfo> ahsSamplePicInfoList = this.ahsSamplePicInfoManager.queryAhsSamplePicInfoList(ahsSamplePicInfo1);
        resultMap.put("ahsSamplePicInfoList", ahsSamplePicInfoList);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(resultMap);
        return apiResultSet;
    }


    /**
     * 获取样表信息列表
     *
     * @param ahsSamplePicInfo
     * @param response
     * @return
     */
    @PostMapping(value = "/queryAhsSamplePicInfoList")
    public ApiResultSet queryAhsSamplePicInfoList(AhsSamplePicInfo ahsSamplePicInfo, HttpServletResponse response) {
        Map<String, Object> resultMap = new HashMap<>();
        AhsSamplePicInfo ahsSamplePicInfo1 = new AhsSamplePicInfo();
        ahsSamplePicInfo1.setMateriaOid(ahsSamplePicInfo.getMateriaOid());
        log.info("参数数据：{}", JSON.toJSONString(ahsSamplePicInfo));
        if (StringUtils.isNotEmpty(ahsSamplePicInfo.getSampleInfoOid())) {
            ahsSamplePicInfo1.setSampleInfoOid(ahsSamplePicInfo.getSampleInfoOid());
        }
        if (StringUtils.isNotEmpty(ahsSamplePicInfo.getComboDirectoryOid())) {
            ahsSamplePicInfo1.setComboDirectoryOid(ahsSamplePicInfo.getComboDirectoryOid());
        }
        List<AhsSamplePicInfo> ahsSamplePicInfoList = this.ahsSamplePicInfoManager.queryAhsSamplePicInfoList(ahsSamplePicInfo1);
        for(AhsSamplePicInfo ahsSamplePicInfo2:ahsSamplePicInfoList){
            ExaminePointCarding examinePointCarding= new ExaminePointCarding();
            examinePointCarding.setAhsSamplePicInfoOid(ahsSamplePicInfo2.getAhsSamplePicInfoOid());
            examinePointCarding.setAttaOid(ahsSamplePicInfo2.getAttaOid());
            List<ExaminePointCarding> examinePointCardingList=this.examinePointCardingManager.queryExaminePointCardingList(examinePointCarding);
            ahsSamplePicInfo2.setExaminePointCardingList(examinePointCardingList);
        }
        resultMap.put("ahsSamplePicInfoList", ahsSamplePicInfoList);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(resultMap);
        return apiResultSet;
    }



    /**
     * 删除样表
     * @param id
     * @return
     */
    @PostMapping(value = "/deleteAhsSamplePicInfo")
    public ApiResultSet delete(String id) {
        this.ahsSamplePicInfoManager.del(id);
        log.info("删除成功：{}", id);
        return new ApiResultSet(id);
    }

    /**
     * 获取审查要点列表
     * @param
     * @return
     */
    @GetMapping(value = "/getExaminePointCardingList")
    public ApiResultSet getExaminePointCardingList(String examinePointCardingOid,String ahsSamplePicInfoOid,String attaOid ) {
        Map<String, Object> resultMap = new HashMap<>();
        ExaminePointCarding examinePointCarding= new ExaminePointCarding();
        examinePointCarding.setAhsSamplePicInfoOid(ahsSamplePicInfoOid);
        examinePointCarding.setAttaOid(attaOid);
        examinePointCarding.setExaminePointCardingOid(examinePointCardingOid);
        List<ExaminePointCarding> examinePointCardingList=this.examinePointCardingManager.queryExaminePointCardingList(examinePointCarding);
        resultMap.put("examinePointCardingList",examinePointCardingList);
      /*  ApiResultSet<SysAtta> sysAtta  = sysAttaFeginService.getByAttaOid(attaOid);
        resultMap.put("sysAtta",sysAtta);*/
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(resultMap);
        return apiResultSet;
    }
    /**
     * 保存更新审查要点
     * @param
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/saveOrUpdateExaminePointCardingList")
    public ApiResultSet saveOrUpdateExaminePointCardingList(ExaminePointCardingVo examinePointCardingVo, HttpServletResponse response) throws Exception {
        Map<String, Object> modelMap = new HashMap<String, Object>();
       String message= this.examinePointCardingManager.saveOrUpdateExaminePointCarding(examinePointCardingVo);
        log.info("保存/更新审查要点信息成功：{}", JSON.toJSONString(examinePointCardingVo));
        if(null==message){
            modelMap.put("success", true);
            modelMap.put("message", examinePointCardingVo.getAttaOid());
        }else{
            modelMap.put("success", false);
            modelMap.put("message", examinePointCardingVo.getAttaOid());
        }
        return new ApiResultSet(modelMap);
    }
    /**
     * 删除审查信息
     * @param id
     * @return
     */
    @PostMapping(value = "/deleteExaminePointCarding")
    public ApiResultSet deleteExaminePointCarding(String id) {
        this.examinePointCardingManager.del(id);
        log.info("删除成功：{}", id);
        return new ApiResultSet(id);
    }
    /**
     * 根据id查询审查信息
     * @param id
     * @return
     */
    @PostMapping(value = "/getOneExaminePointCarding")
    public ExaminePointCarding getOneExaminePointCarding(String id) {
        Assert.hasLength(id, "主键不能为空！");
        ExaminePointCarding examinePointCarding=this.examinePointCardingManager.getOne(id);
        return examinePointCarding;
    }


    /**
     * 根据办件id和材料id查询事项附件
     * @param materialOid
     * @param caseOid
     * @return
     */
   @PostMapping(value = "/querySysAttaListByMaterialOid")
   public  ApiResultSet querySysAttaListByMaterialOid(String materialOid,String caseOid){
       ApiResultSet attaResult=qlSysAttaFeignService.querySysAttaListByMaterialOid(materialOid,caseOid);
       return new ApiResultSet(attaResult);
   }



}
