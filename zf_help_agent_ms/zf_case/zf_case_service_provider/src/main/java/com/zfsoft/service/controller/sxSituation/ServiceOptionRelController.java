package com.zfsoft.service.controller.sxSituation;

import cn.hutool.core.collection.CollUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.service.manager.sxSituation.ServiceOptionRelManager;
import com.zfsoft.service.manager.sxSituation.SxServiceOptionTitleManager;
import com.zfsoft.service.sxSituation.data.ServiceOptionRel;
import com.zfsoft.service.sxSituation.data.SxServiceOptionTitle;
import com.zfsoft.service.sxSituation.data.SxServiceOptionVal;
import com.zfsoft.service.sxSituation.data.vo.ServiceOptionRelDto;
import com.zfsoft.service.sxSituation.service.ServiceOptionRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangns
 * @description 综窗选项关联信息
 * @date 2020/11/3 10:49
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@RestController
@Slf4j
public class ServiceOptionRelController implements ServiceOptionRelService {

    @Resource
    private ServiceOptionRelManager serviceOptionRelManager;
    @Resource
    private SxServiceOptionTitleManager sxServiceOptionTitleManager;

    /**
     * @param oid 业务主键
     * @description: 根据大综窗选项关联业务主健获取大综窗选项关联消息
     * @author: wangns
     * @Date: 2020/11/3 10:00
     **/
    @Override
    public ApiResultSet<ServiceOptionRel> getServiceOptionRelByOid(String oid) {

        ServiceOptionRel serviceOptionRel = serviceOptionRelManager.getServiceOptionRelByOid(oid);

        ApiResultSet<ServiceOptionRel> apiResultSet = new ApiResultSet<>();

        apiResultSet.setData(serviceOptionRel);

        return apiResultSet;
    }

    /**
     * 根据这两 oid 查询和该选项值关联的选项标题和选项值
     * @param serviceOid
     * @param optionValOid
     * @param optionTitleOid
     * @return
     */
    @Override
    public ApiResultSet getOptionRelationDataById(String serviceOid, String optionValOid, String optionTitleOid) {
        Map<String, Object> resultMap = this.serviceOptionRelManager.getOptionRelationDataById(serviceOid,optionValOid,optionTitleOid);
        ApiResultSet apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(resultMap);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<PageResult<ServiceOptionRelDto>> relationList(String serviceOid, Integer pageNum, Integer pageSize) {
        PageResult<ServiceOptionRel> page=serviceOptionRelManager.queryServiceOptionRelDto(serviceOid,pageNum,pageSize);
        List<ServiceOptionRelDto> serviceOptionRelDtoList = new ArrayList<>();
        if(null == page.getData() || page.getData().size() == 0) {
            return new ApiResultSet<>();
        }
        List<ServiceOptionRel> serviceOptionRelList=page.getData();
        //获取当前事项下所有的选项信息
        List<SxServiceOptionTitle> serviceOptionTitleList = sxServiceOptionTitleManager.getListTitleAndOption(serviceOid);
        // 通过标题id转map
        Map<String, SxServiceOptionTitle> serviceOptionTitleMap = new HashMap<>();
        if(null != serviceOptionTitleList && serviceOptionTitleList.size()>0){
            for(SxServiceOptionTitle title : serviceOptionTitleList){
                serviceOptionTitleMap.put(title.getOid(), title);
            }
        }
        //所有的选项标题信息
        List<SxServiceOptionVal> serviceOptionValList = new ArrayList<>();
        for (SxServiceOptionTitle serviceOptionTitle : serviceOptionTitleList) {
            if(CollUtil.isEmpty(serviceOptionTitle.getSxServiceOptionVals())){
                continue;
            }
            serviceOptionValList.addAll(serviceOptionTitle.getSxServiceOptionVals());
        }
        //通过选项值id转map
        Map<String, SxServiceOptionVal> serviceOptionValMap = new HashMap<>();
        if(null != serviceOptionValList && serviceOptionValList.size()>0){
            for(SxServiceOptionVal val : serviceOptionValList){
                //获取标题名称用于塞值用
                val.setTitleName(serviceOptionTitleMap.get(val.getTitleOid()).getName());
                serviceOptionValMap.put(val.getOid(), val);
            }
        }

        for (ServiceOptionRel optionRel : serviceOptionRelList) {
            ServiceOptionRelDto serviceOptionRelDto = new ServiceOptionRelDto();
            //情形结果list 格式 ： 选项标题【选项值】
            List<String>  optionValueList = new ArrayList<>();
            //情形标题list 格式 ： 选项标题
            List<String> optionTitleList = new ArrayList<>();
            serviceOptionRelDto.setOid(optionRel.getOid());
            serviceOptionRelDto.setServiceOptionRel(optionRel);
            serviceOptionRelDto.setOptionValueList(optionValueList);
            serviceOptionRelDto.setOptionTitleList(optionTitleList);

            serviceOptionRelDtoList.add(serviceOptionRelDto);

            //设置标题选项值等
            String valueOids = optionRel.getValueOids();
            String titleOids = optionRel.getTitleOids();

            String[] arrValueOid = valueOids.split(",");
            for (String valueOid : arrValueOid) {
                optionValueList.add(serviceOptionValMap.get(valueOid).getTitleName() + "【"+serviceOptionValMap.get(valueOid).getName()+"】");
            }

            String[] arrTitleOid = titleOids.split(",");
            for (String titleOid : arrTitleOid) {
                if(serviceOptionTitleMap.get(titleOid) !=null){
                    optionTitleList.add(serviceOptionTitleMap.get(titleOid).getName());
                }
            }
        }
        PageResult<ServiceOptionRelDto> pageDto=new PageResult<>(page.getPageNum(),page.getPageSize(),page.getTotal());
        pageDto.setData(serviceOptionRelDtoList);
        return new ApiResultSet<>(pageDto);
    }

    @Override
    public ApiResultSet<String> saveOrUpdateServiceOptionRel(ServiceOptionRel serviceOptionRel) {
       String res= serviceOptionRelManager.saveOrUpdateServiceOptionRel(serviceOptionRel);
        return new ApiResultSet<>(res);
    }

    @Override
    public ApiResultSet<String> delBatchRel(String relOids) {
        serviceOptionRelManager.delBatchRel(relOids);
        return new ApiResultSet<>();
    }
}
