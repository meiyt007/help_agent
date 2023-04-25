package com.zfsoft.superwindow.controller.yxpz;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.yxpz.PbpjManage;
import com.zfsoft.superwindow.manager.yxpz.PbpjManageManager;
import com.zfsoft.superwindow.service.yxpz.PbpjManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName PbpjManageController
 * @Description 参数配置管理的实现类
 * @Author liangxm
 * @Date 2020-09-12 10:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class PbpjManageController implements PbpjManageService {
    @Resource
    private PbpjManageManager pbpjManageManager;



    /**
     * @description:  参数配置的新增或者修改
     * @param pbpjManage 参数配置实体类
     * @author: liangxm
     * @Date: 2020/9/12 10:14
     **/
    @Override
    public ApiResultSet<PbpjManage> savePbpjManage(@RequestBody PbpjManage pbpjManage){
        pbpjManage.setUserCode("123");
        pbpjManage.setUserName("123");
        pbpjManageManager.savePbpjManage(pbpjManage);
        return new ApiResultSet<>(pbpjManage);
    }

    @Override
    public ApiResultSet<PageResult<PbpjManage>> queryPbpjManageWithPage(String name, String runCode, Integer pageNum, Integer pageSize) {
        PbpjManage pbpjManage = new PbpjManage();
        pbpjManage.setName(name);
        pbpjManage.setRunCode(runCode);
//        sysConfig.setParentOid(parentOid);
        PageResult<PbpjManage> pageResult = pbpjManageManager.queryPbpjManageWithPage(pbpjManage,pageNum,pageSize);
        ApiResultSet<PageResult<PbpjManage>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

//    @Override
//    public ApiResultSet initPbpjManage(@PathVariable("id")Long id) {
//        PbpjManage pbpjManage = pbpjManageManager.getPbpjManageById(id);
//        return new ApiResultSet<PbpjManage>(pbpjManage);
//    }

    @Override
    public ApiResultSet<Integer> deletePbpjManageById(String id) {
        pbpjManageManager.deletePbpjManageById(id);
        return  new ApiResultSet<>();
    }

    @Override
    public ApiResultSet<PbpjManage> getPbpjManageById(String id) {
        PbpjManage pbpjManage = pbpjManageManager.getPbpjManageById(id);
        return new ApiResultSet<PbpjManage>(pbpjManage);
    }

    @Override
    public int ablePbpjManage(String id) {
        int rows = pbpjManageManager.ablePbpjManageById(id);
        return  rows;
    }

    @Override
    public ApiResultSet<PbpjManage> getPbpjManageByUserCode(String userCode) {
       PbpjManage pbpj= pbpjManageManager.getPbpjManageByUserCode(userCode);
        return new ApiResultSet<>(pbpj);
    }

    @Override
    public ApiResultSet<PbpjManage> getPbpjManageByRunCode(String runCode) {
        PbpjManage pbpj=pbpjManageManager.getPbpjManageByRunCode(runCode);
        return new ApiResultSet<>(pbpj);
    }

    @Override
    public int updatePbpjManage(PbpjManage pbpjManage) {
        int index=pbpjManageManager.updatePbpjManage(pbpjManage);
        return index;
    }


}
