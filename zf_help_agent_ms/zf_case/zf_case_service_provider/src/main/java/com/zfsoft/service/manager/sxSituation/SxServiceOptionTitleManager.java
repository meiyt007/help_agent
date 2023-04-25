package com.zfsoft.service.manager.sxSituation;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.service.dbaccess.dao.sxSituation.DbSxServiceOptionTitleMapper;
import com.zfsoft.service.dbaccess.data.sxSituation.DbSxServiceOptionTitle;
import com.zfsoft.service.dbaccess.data.sxSituation.DbSxServiceOptionTitleExample;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.service.common.SxptBaseStaticParameter;
import com.zfsoft.service.manager.sxService.SxServiceMaterialManager;
import com.zfsoft.service.util.JsonUtil;
import com.zfsoft.service.sxService.data.SxServiceMaterial;
import com.zfsoft.service.sxSituation.data.ServiceOptionRel;
import com.zfsoft.service.sxSituation.data.SxServiceMateOptRel;
import com.zfsoft.service.sxSituation.data.SxServiceOptionTitle;
import com.zfsoft.service.sxSituation.data.SxServiceOptionVal;
import com.zfsoft.service.sxSituation.data.vo.ServiceOptionLineDto;
import com.zfsoft.service.sxSituation.data.vo.ServiceOptionPictureDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangns
 * @description 选项标题表 实现类
 * @date 2020/11/3 11:12
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@Service
@Slf4j
public class SxServiceOptionTitleManager {

    @Resource
    private DbSxServiceOptionTitleMapper dbSxServiceOptionTitleMapper;

    @Resource
    private SxServiceOptionValManager sxServiceOptionValManager;
    @Resource
    private SxServiceMateOptRelManager sxServiceMateOptRelManager;

    @Resource
    private SxServiceMaterialManager sxServiceMaterialManager;

    @Resource
    private ServiceOptionRelManager serviceOptionRelManager;


    public SxServiceOptionTitle getServiceOptionTitleByOid(String oid){
        DbSxServiceOptionTitle dbSxServiceOptionTitle = dbSxServiceOptionTitleMapper.selectByOid(oid);
        SxServiceOptionTitle sxServiceOptionTitle = null ;
        if (null == dbSxServiceOptionTitle) {
            //没查出数据，不抛异常
//            throw  new ResultInfoException("根据业务主键OID=【"+oid+"】，查询选项标题表单为空！");
        }else{
            sxServiceOptionTitle = new SxServiceOptionTitle();
            BeanUtils.copyProperties(dbSxServiceOptionTitle, sxServiceOptionTitle);
        }
        return sxServiceOptionTitle;
    }

    public List<SxServiceOptionTitle> getServiceOptionTitlesByServiceOid(String serviceOid) {
        DbSxServiceOptionTitleExample dbSxServiceOptionTitleExample = new DbSxServiceOptionTitleExample();
        DbSxServiceOptionTitleExample.Criteria criteria = dbSxServiceOptionTitleExample.createCriteria();
        if(StrUtil.isNotEmpty(serviceOid)){
            criteria.andServiceOidEqualTo(serviceOid);
        }
        criteria.andDeleteStatusEqualTo((short)0);
        dbSxServiceOptionTitleExample.setOrderByClause("  sort");
        List<DbSxServiceOptionTitle> dbSxServiceOptionTitle = dbSxServiceOptionTitleMapper.selectByExample(dbSxServiceOptionTitleExample);
        List<SxServiceOptionTitle> sxServiceOptionTitles = dbSxServiceOptionTitle.stream().map(sxServiceOptionTitle -> {
            SxServiceOptionTitle serviceOptionTitle = new SxServiceOptionTitle();
            BeanUtils.copyProperties(sxServiceOptionTitle,serviceOptionTitle);
            return serviceOptionTitle;
        }).collect(Collectors.toList());
        return sxServiceOptionTitles;
    }

    public List<SxServiceOptionTitle> getListTitleAndOption(String serviceOid) {
        DbSxServiceOptionTitleExample dbSxServiceOptionTitleExample = new DbSxServiceOptionTitleExample();
        DbSxServiceOptionTitleExample.Criteria criteria = dbSxServiceOptionTitleExample.createCriteria();
        if(StrUtil.isNotEmpty(serviceOid)){
            criteria.andServiceOidEqualTo(serviceOid);
        }
        criteria.andDeleteStatusEqualTo((short)0);
        dbSxServiceOptionTitleExample.setOrderByClause("  sort");
        List<DbSxServiceOptionTitle> dbSxServiceOptionTitle = dbSxServiceOptionTitleMapper.selectByExample(dbSxServiceOptionTitleExample);
        List<SxServiceOptionTitle> sxServiceOptionTitles = dbSxServiceOptionTitle.stream().map(sxServiceOptionTitle -> {
            SxServiceOptionTitle serviceOptionTitle = new SxServiceOptionTitle();
            BeanUtils.copyProperties(sxServiceOptionTitle,serviceOptionTitle);
            //查询标题下面所有的选项
           List<SxServiceOptionVal> listVal= sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(sxServiceOptionTitle.getOid());
            serviceOptionTitle.setSxServiceOptionVals(listVal);
            return serviceOptionTitle;
        }).collect(Collectors.toList());
        return sxServiceOptionTitles;
    }

   public List<SxServiceOptionTitle> getListTitleAndOptionMaterial(String serviceOid){
      List<SxServiceOptionTitle> list=this.getListTitleAndOption(serviceOid);
      if(list!=null && list.size()>0){
          for(SxServiceOptionTitle title:list){
              if(title.getSxServiceOptionVals()!=null){
                  for(SxServiceOptionVal val:title.getSxServiceOptionVals()){
                      //根据选项查询选项关联材料信息
                     List<SxServiceMateOptRel> listrel= sxServiceMateOptRelManager.getSxServiceMateOptRelsByOptionValOid(val.getOid());
                     if(listrel!=null && listrel.size()>0){
                         List<SxServiceMaterial> clList=new ArrayList();
                         //查询关联的所有材料
                         for(SxServiceMateOptRel material:listrel){
                             //材料主键查询材料
                             if(StringUtil.isNotEmpty(material.getSxMaterialOid())){
                               SxServiceMaterial sxMaterial=  sxServiceMaterialManager.getSxServiceMaterialByOid(material.getSxMaterialOid());
                                 clList.add(sxMaterial);
                             }
                         }
                         val.setSxServiceMaterials(clList);
                     }
                  }
              }
          }
      }
       return list;
    }

    /**
     * 通过事项id获取选项关系数据 json格式
     * @param
     * @return
     */
    public String[] getRelationJsonForPicture(String serviceOid,String oldTitleOid,String oldValOid){

        //获取当前事项下所有的选项
        List<SxServiceOptionTitle> serviceOptionTitleList=  this.getListTitleAndOption(serviceOid);
        Map<String, SxServiceOptionTitle> serviceOptionTitleMap = new HashMap<>();
        if(null != serviceOptionTitleList && serviceOptionTitleList.size()>0){
            for(SxServiceOptionTitle title : serviceOptionTitleList){
                serviceOptionTitleMap.put(title.getOid(), title);
            }
        }
        if(StringUtil.isNotEmpty(oldTitleOid)) {
            SxServiceOptionTitle serviceOptionTitle = serviceOptionTitleMap.get(oldTitleOid);
            List<SxServiceOptionVal> serviceOptionValList = serviceOptionTitle.getSxServiceOptionVals();
            if(null != serviceOptionValList && serviceOptionValList.size()>0){
                String strOid = "";
                for(SxServiceOptionVal val : serviceOptionValList){
                    strOid = val.getOid()+",";
                }
                if(StringUtil.isNotEmpty(strOid)){
                    oldValOid = strOid.substring(0, strOid.length()-1);
                }
            }
        }
        //获取已配置的全部前后置关系
        List<ServiceOptionRel> serviceOptionRelList= serviceOptionRelManager.getServiceOptionRelByServiceOid(serviceOid);
        //转map key- valueOids value- ServiceOptionRel
        Map<String, ServiceOptionRel> serviceOptionRelMap = new HashMap<>();
        if(null != serviceOptionRelList && serviceOptionRelList.size()>0){
            for(ServiceOptionRel rel : serviceOptionRelList){
                serviceOptionRelMap.put(rel.getValueOids(), rel);
            }
        }
        //全部选项tree end
        //坐标map key title或value主键 value 数组坐标
        Map<String,double[]> xyMap = new HashMap<>();
        //标题接收颜色线条map 数量
        Map<String,Integer> titleLineColorMap = new HashMap<>();
        //color 数组
        String[] arrColor = new String[]{"#0fff17","#FF4500","#F8F8FF","#FCCE10","#E87C25","#27727B","#FE8463","#9BCA63","#FAD860","#F3A43B","#60C0DD","#D7504B","#C6E579","#F4E001","#F0805A","#F0805A"};
        //遍历 转dto
        // 起点坐标
        double x = 100;
        double y = 100;
        List<ServiceOptionPictureDto> serviceOptionPictureDtoList = new ArrayList<>();
        for (SxServiceOptionTitle serviceOptionTitle : serviceOptionTitleList) {
            //标题信息
            ServiceOptionPictureDto serviceOptionPictureDto = new ServiceOptionPictureDto();
            serviceOptionPictureDtoList.add(serviceOptionPictureDto);
            serviceOptionPictureDto.setOid(serviceOptionTitle.getOid());
            serviceOptionPictureDto.setName(serviceOptionTitle.getName());
            serviceOptionPictureDto.setX(x);
            serviceOptionPictureDto.setY(y);
            serviceOptionPictureDto.setType("0");
            serviceOptionPictureDto.setUniqueness(serviceOptionTitle.getOid());
            xyMap.put(serviceOptionPictureDto.getOid(),new double[]{serviceOptionPictureDto.getX(),serviceOptionPictureDto.getY()});
            List<SxServiceOptionVal> serviceOptionValList = serviceOptionTitle.getSxServiceOptionVals();
            double valX = x + 150;
            double valY = y;
            for (SxServiceOptionVal serviceOptionVal : serviceOptionValList) {
                ServiceOptionPictureDto serviceOptionPictureDtoVal = new ServiceOptionPictureDto();
                serviceOptionPictureDtoList.add(serviceOptionPictureDtoVal);
                serviceOptionPictureDtoVal.setOid(serviceOptionVal.getOid());
                serviceOptionPictureDtoVal.setName(serviceOptionVal.getName());
                serviceOptionPictureDtoVal.setX(valX);
                serviceOptionPictureDtoVal.setY(valY);
                serviceOptionPictureDtoVal.setType("1");
                serviceOptionPictureDtoVal.setUniqueness(serviceOptionVal.getOid());
                xyMap.put(serviceOptionPictureDtoVal.getOid(),new double[]{serviceOptionPictureDtoVal.getX(),serviceOptionPictureDtoVal.getY()});
                valX +=150;
            }
            y +=100;
        }
        List<ServiceOptionLineDto> serviceOptionLineDtoList = new ArrayList<>();
        for (ServiceOptionRel serviceOptionRel : serviceOptionRelList) {
            String[] valueOids = serviceOptionRel.getValueOids().split(",");

            String[] titleOids = serviceOptionRel.getTitleOids().split(",");

            for (String valueOid : valueOids) {
//                if(StrUtil.isNotBlank(oldValOid) && !oldValOid.contains(valueOid))
//                    continue;
                for (String titleOid : titleOids) {
                    if(StringUtil.isNotEmpty(oldTitleOid)) {
                        if((StringUtil.isNotEmpty(oldValOid) && !serviceOptionRel.getValueOids().contains(oldValOid))) {
                            continue;
                        }
                    }
                    if((StringUtil.isNotEmpty(oldTitleOid) && !oldTitleOid.equals(titleOid)) ) {
                        if((StringUtil.isNotEmpty(oldValOid) && !oldValOid.contains(valueOid))) {
                            continue;
                        }
                    }

                    if(titleLineColorMap.get(titleOid) == null) {
                        titleLineColorMap.put(titleOid,0);
                    }
                    ServiceOptionLineDto serviceOptionLineDto = new ServiceOptionLineDto();
                    serviceOptionLineDto.setFrom("");
                    serviceOptionLineDto.setTo("");
                    if(xyMap.get(valueOid) !=null){
                        serviceOptionLineDto.setFromX(xyMap.get(valueOid)[0]);
                        serviceOptionLineDto.setFromY(xyMap.get(valueOid)[1]);
                    }
                    if(xyMap.get(titleOid) !=null){
                        serviceOptionLineDto.setToX(xyMap.get(titleOid)[0]);
                        serviceOptionLineDto.setToY(xyMap.get(titleOid)[1]);
                    }
                    serviceOptionLineDto.setColor(arrColor[titleLineColorMap.get(titleOid)]);
                    serviceOptionLineDtoList.add(serviceOptionLineDto);
                }
            }
            for (String titleOid : titleOids) {
                if(titleLineColorMap.get(titleOid) != null){
                    titleLineColorMap.put(titleOid,titleLineColorMap.get(titleOid) +1);
                }
            }
        }
        return new String[]{JsonUtil.toJSONString(serviceOptionPictureDtoList),JsonUtil.toJSONString(serviceOptionLineDtoList)};
    }

    /**
     * 分页查询选项标题信息
     * @param name 标题名称
     * @param serviceOid  事项oid
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ApiResultSet<PageResult<SxServiceOptionTitle>> querySxServiceOptionTitlePag(String name, String serviceOid, Integer pageNum, Integer pageSize){
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum,pageSize);
        DbSxServiceOptionTitleExample dbSxServiceOptionTitleExample = new DbSxServiceOptionTitleExample();
        DbSxServiceOptionTitleExample.Criteria criteria = dbSxServiceOptionTitleExample.createCriteria();
        if(StrUtil.isNotEmpty(serviceOid)){
            criteria.andServiceOidEqualTo(serviceOid);
        }
        if(StrUtil.isNotEmpty(name)){
            criteria.andNameLike("%"+name+"%");
        }
        criteria.andDeleteStatusEqualTo((short)0);
        dbSxServiceOptionTitleExample.setOrderByClause(" sort asc");
        Page<DbSxServiceOptionTitle> dbSxServiceOptionTitle = (Page<DbSxServiceOptionTitle>) dbSxServiceOptionTitleMapper.selectByExample(dbSxServiceOptionTitleExample);
        PageResult pageResult=new PageResult(dbSxServiceOptionTitle.getPageNum(),dbSxServiceOptionTitle.getPageSize(),dbSxServiceOptionTitle.getTotal());
        List<SxServiceOptionTitle> sxServiceOptionTitleList = dbSxServiceOptionTitle.stream().map(sxServiceOptionTitle -> {
            SxServiceOptionTitle optionTitle = new SxServiceOptionTitle();
            BeanUtils.copyProperties(sxServiceOptionTitle,optionTitle);
            return optionTitle;
        }).collect(Collectors.toList());
        pageResult.setData(sxServiceOptionTitleList);
        return new ApiResultSet<>(pageResult);
    }


    /**
     * 初始化
     * @param oid
     * @return
     */
    public SxServiceOptionTitle initSxServiceOptionTitle(String oid){
        DbSxServiceOptionTitle dbSxServiceOptionTitle = dbSxServiceOptionTitleMapper.selectByOid(oid);
        SxServiceOptionTitle sxServiceOptionTitle = null ;
        if (null == dbSxServiceOptionTitle) {
            //没查出数据，不抛异常
//            throw  new ResultInfoException("根据业务主键OID=【"+oid+"】，查询选项标题表单为空！");
        }else{
            sxServiceOptionTitle = new SxServiceOptionTitle();
            BeanUtils.copyProperties(dbSxServiceOptionTitle, sxServiceOptionTitle);
        }
        return sxServiceOptionTitle;
    }

    /**
     * 保存选项标题和选项值信息
     * @param sxServiceOptionTitle
     * @return
     */
    public int saveSxServiceOptionTitle(SxServiceOptionTitle sxServiceOptionTitle) {
        DbSxServiceOptionTitle record = null;
        if(null==sxServiceOptionTitle.getOid()){
            record = new  DbSxServiceOptionTitle();
            BeanUtils.copyProperties(sxServiceOptionTitle,record);
            record.setOid(UUID.randomUUID().toString().replaceAll("-",""));
            record.setDeleteStatus((short)0);
            record.setCreateDate(new Date());
            record.setModifyDate(new Date());
            dbSxServiceOptionTitleMapper.insertSelective(record);
        }else{
            record = new  DbSxServiceOptionTitle();
            BeanUtils.copyProperties(sxServiceOptionTitle,record);
            record.setModifyDate(new Date());
            dbSxServiceOptionTitleMapper.updateByPrimaryKeySelective(record);
        }
        //选项值List
        if(null != sxServiceOptionTitle.getSxServiceOptionVals() && sxServiceOptionTitle.getSxServiceOptionVals().size()>0){
            Map<String, SxServiceOptionVal> optionMap = new HashMap<>();
            if(null != sxServiceOptionTitle.getOid()) {//修改
                List<SxServiceOptionVal> optionValList = this.sxServiceOptionValManager.getSxServiceOptionValsByTitleOid(sxServiceOptionTitle.getOid());
                for(SxServiceOptionVal optionVal : optionValList){
                    optionMap.put(optionVal.getOid(),optionVal);
                }
            }
            SxServiceOptionVal serviceOptionVal = null;
            for(SxServiceOptionVal val : sxServiceOptionTitle.getSxServiceOptionVals()){
                if(null == optionMap.get(val.getOid())) {//添加
                    serviceOptionVal = new SxServiceOptionVal();
                    serviceOptionVal.setTitleOid(record.getOid());
                    serviceOptionVal.setName(val.getName());
                    serviceOptionVal.setDefaultFlag(val.getDefaultFlag());
                    serviceOptionVal.setSort(val.getSort());
                    serviceOptionVal.setDeleteStatus(SxptBaseStaticParameter.ZERO);
                    this.sxServiceOptionValManager.saveOrUpdate(serviceOptionVal);
                }else{//修改
                    serviceOptionVal = this.sxServiceOptionValManager.getSxServiceOptionValByOid(val.getOid());
                    BeanUtils.copyProperties(val,serviceOptionVal);
                    this.sxServiceOptionValManager.saveOrUpdate(serviceOptionVal);
                    optionMap.remove(serviceOptionVal.getOid());
                }
            }
            if(optionMap.size()>0){
                for(Map.Entry<String, SxServiceOptionVal> entry : optionMap.entrySet()){
                    entry.getValue().setDeleteStatus(SxptBaseStaticParameter.ONE);
                    sxServiceOptionValManager.saveOrUpdate(entry.getValue());
                }
            }
        }
        sxServiceOptionTitle.setId(record.getId());
        return 0;
    }

    public int delSxServiceOptionTitleByOid(String oid) {
        DbSxServiceOptionTitle dbSxServiceOptionTitle = dbSxServiceOptionTitleMapper.selectByOid(oid);
        dbSxServiceOptionTitle.setDeleteStatus((short)1);
        return dbSxServiceOptionTitleMapper.updateByPrimaryKeySelective(dbSxServiceOptionTitle);
    }

}
