package com.zfsoft.service.manager.sxService;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.cases.feign.SysDictFeignService;
import com.zfsoft.service.dbaccess.dao.sxService.DbRefinedMaterialMapper;
import com.zfsoft.service.dbaccess.dao.sxService.DbReviewPointsMapper;
import com.zfsoft.service.dbaccess.data.sxService.DbRefinedMaterial;
import com.zfsoft.service.dbaccess.data.sxService.DbRefinedMaterialExample;
import com.zfsoft.service.dbaccess.data.sxService.DbReviewPoints;
import com.zfsoft.microservice.settings.data.SysDict;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.service.manager.sxSys.SxSysAttaManager;
import com.zfsoft.service.sxService.data.RefinedMaterial;
import com.zfsoft.service.sxService.data.ReviewPoints;
import com.zfsoft.service.sxService.data.SxServiceMaterial;
import com.zfsoft.service.sxSys.data.SxSysAtta;
import com.zfsoft.service.util.PdfUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName reviewPointsManager
 * @Description: 细化材料实现类
 * @Author liangss
 * @Date 2021/07/12
 **/
@Service
@Slf4j
public class RefinedMaterialManager {

    @Resource
    private DbRefinedMaterialMapper dbRefinedMaterialMapper;

    @Resource
    private ReviewPointsManager reviewPointsManager;
    @Resource
    private SysDictFeignService sysDictFeignService;

    @Resource
    private DbReviewPointsMapper dbReviewPointsMapper;

    @Resource
    private SxSysAttaManager sxSysAttaManager;

    @Resource
    private SxServiceMaterialManager sxServiceMaterialManager;



    /**
     * 根据材料oid查询细化材料
     * @param materialOid
     * @return
     */
    public List<RefinedMaterial> getRefinedMaterialListByMaterialOid(String materialOid) {
        DbRefinedMaterialExample dbRefinedMaterialExample=new DbRefinedMaterialExample();
        DbRefinedMaterialExample.Criteria criteria=dbRefinedMaterialExample.createCriteria();
        if(StringUtils.isNotEmpty(materialOid)){
            criteria.andMaterialOidEqualTo(materialOid);
        }
        criteria.andDeleteStatusEqualTo((short) 0);
        dbRefinedMaterialExample.setOrderByClause(" SERIAL_NUMBER");
        List<DbRefinedMaterial> dbRefinedMaterials=dbRefinedMaterialMapper.selectByExample(dbRefinedMaterialExample);
        List<RefinedMaterial> refinedMaterialList=dbRefinedMaterials.stream().map(dbRefinedMaterial -> {
            RefinedMaterial refinedMaterial = new RefinedMaterial();
            BeanUtils.copyProperties(dbRefinedMaterial, refinedMaterial);
            return refinedMaterial;
        }).collect(Collectors.toList());
        return refinedMaterialList;
    }

    /**
     * 根据主键查询细化材料
     * @param id
     * @return
     */
    public RefinedMaterial getOne(String id) {
        Assert.hasLength(id, "主键不能为空！");
        DbRefinedMaterial dbRefinedMaterial=this.dbRefinedMaterialMapper.selectByPrimaryKey(Long.valueOf(id));
        RefinedMaterial refinedMaterial = new RefinedMaterial();
        BeanUtils.copyProperties(dbRefinedMaterial, refinedMaterial);
        return refinedMaterial;
    }


    /**
     * 根据业务主键查询oid
     * @param oid
     * @return
     */
    public RefinedMaterial getRefinedMaterialByOid(String oid) {
        Assert.hasLength(oid, "业务主键不能为空！");
        DbRefinedMaterial dbRefinedMaterial=this.dbRefinedMaterialMapper.getRefinedMaterialByOid(oid);
        RefinedMaterial refinedMaterial = new RefinedMaterial();
        List<ReviewPoints> reviewPointsList= reviewPointsManager.getReviewPointsListByRefinedMaterialOid(dbRefinedMaterial.getOid());
        refinedMaterial.setReviewPointsList(reviewPointsList);
        if(StrUtil.isNotEmpty(dbRefinedMaterial.getMaterialSampleOid())){
            SxSysAtta sxSysAtta = sxSysAttaManager.getSxSysAttaByOid(dbRefinedMaterial.getMaterialSampleOid());
            if(null!=sxSysAtta){
                refinedMaterial.setMaterialSampleOriginName(sxSysAtta.getOriginName());
            }
        }
        BeanUtils.copyProperties(dbRefinedMaterial, refinedMaterial);
        return refinedMaterial;
    }


    /**
     * 更新保存细化材料
     * @param refinedMaterial
     * @throws Exception
     */
    public void saveOrUpdateRefinedMaterial(RefinedMaterial refinedMaterial) throws Exception {
        DbRefinedMaterial dbRefinedMaterial = null;
        if (null != refinedMaterial.getId()) {
             dbRefinedMaterial=this.dbRefinedMaterialMapper.selectByPrimaryKey(refinedMaterial.getId());
             Assert.notNull(dbRefinedMaterial, MessageFormat.format("更新对象不存在！对象id为{0}", dbRefinedMaterial.getId()));
             BeanUtils.copyProperties(refinedMaterial, dbRefinedMaterial);
             dbRefinedMaterial.setModifyDate(new Date());
            int result = this.dbRefinedMaterialMapper.updateByPrimaryKeySelective(dbRefinedMaterial);
            if (result > 0 && refinedMaterial.getReviewPointsList().size() > 0) {
                List<ReviewPoints> reviewPointsList= reviewPointsManager.getReviewPointsListByRefinedMaterialOid(dbRefinedMaterial.getOid());
                List<String> list = new ArrayList<>();
                for (ReviewPoints reviewPointUi : refinedMaterial.getReviewPointsList()) {
                    if (StringUtils.isEmpty(reviewPointUi.getOid())) {
                        DbReviewPoints dbReviewPoints = new DbReviewPoints();
                        dbReviewPoints.setRefinedMaterialOid(dbRefinedMaterial.getOid());
                        dbReviewPoints.setReviewPoints(reviewPointUi.getReviewPoints());
                        dbReviewPoints.setSerialNumber(reviewPointUi.getSerialNumber());
                        dbReviewPoints.setDeleteStatus((short)0);
                        dbReviewPoints.setCreateDate(new Date());
                        dbReviewPoints.setModifyDate(new Date());
                        dbReviewPoints.setOid(UUID.randomUUID().toString().replaceAll("-", ""));
                        this.dbReviewPointsMapper.insert(dbReviewPoints);
                    } else {
                        for (ReviewPoints reviewPointDb : reviewPointsList) {
                            if (reviewPointUi.getOid().equals(reviewPointDb.getOid())) {
                                DbReviewPoints dbReviewPoints = this.dbReviewPointsMapper.selectByPrimaryKey(reviewPointUi.getId());
                                BeanUtils.copyProperties(reviewPointUi, dbReviewPoints);
                                dbReviewPoints.setModifyDate(new Date());
                                this.dbReviewPointsMapper.updateByPrimaryKeySelective(dbReviewPoints);
                            }
                        }
                        list.add(reviewPointUi.getOid());
                    }
                }
                if (list.size() > 0) {
                    for (ReviewPoints reviewPointDb : reviewPointsList) {
                        if (!list.contains(reviewPointDb.getOid())) {
                            DbReviewPoints dbReviewPoints = this.dbReviewPointsMapper.selectByPrimaryKey(reviewPointDb.getId());
                            dbReviewPoints.setModifyDate(new Date());
                            dbReviewPoints.setDeleteStatus((short)1);
                            this.dbReviewPointsMapper.updateByPrimaryKeySelective(dbReviewPoints);
                        }
                    }
                }
            }
        } else {
            dbRefinedMaterial = new DbRefinedMaterial();
            BeanUtils.copyProperties(refinedMaterial, dbRefinedMaterial);
            dbRefinedMaterial.setDeleteStatus((short)0);
            dbRefinedMaterial.setCreateDate(new Date());
            dbRefinedMaterial.setModifyDate(new Date());
            String num = UUID.randomUUID().toString().replaceAll("-", "");
            dbRefinedMaterial.setOid(num);
            int result = this.dbRefinedMaterialMapper.insert(dbRefinedMaterial);
            if (result > 0) {
                for (ReviewPoints reviewPoints : refinedMaterial.getReviewPointsList()) {
                    DbReviewPoints dbReviewPoints = new DbReviewPoints();
                    dbReviewPoints.setRefinedMaterialOid(num);
                    dbReviewPoints.setReviewPoints(reviewPoints.getReviewPoints());
                    dbReviewPoints.setSerialNumber(reviewPoints.getSerialNumber());
                    dbReviewPoints.setDeleteStatus((short)0);
                    dbReviewPoints.setCreateDate(new Date());
                    dbReviewPoints.setModifyDate(new Date());
                    dbReviewPoints.setOid(UUID.randomUUID().toString().replaceAll("-", ""));
                    this.dbReviewPointsMapper.insert(dbReviewPoints);
                }
            }
        }
    }

    /**
     * 更新精细化材料列表
     * @param refinedMaterialList
     * @throws Exception
     */
    public String  updateRefinedMaterialList(List<RefinedMaterial> refinedMaterialList) throws Exception {
           String materialCatalogOids="";
           for(RefinedMaterial refinedMaterial:refinedMaterialList){
               String materialCatalogOid=refinedMaterial.getMaterialCatalogOid();
               if (StringUtils.isNotEmpty(materialCatalogOid)) {
                   if (materialCatalogOids.indexOf(materialCatalogOid) != -1) {
                       materialCatalogOids = materialCatalogOids + materialCatalogOid + ";";
                   }
               }
               String licenceOid=refinedMaterial.getLicenceOid();
               if(StringUtils.isNotEmpty(licenceOid)){
                   ApiResultSet<SysDict>   sysDictApiResultSet= sysDictFeignService.getSysDictByDictOid(licenceOid);
                   if(null!=sysDictApiResultSet){
                       SysDict sysDict= sysDictApiResultSet.getData();
                       refinedMaterial.setLicenceName(sysDict.getName());
                   }
               }
               DbRefinedMaterial  dbRefinedMaterial=this.dbRefinedMaterialMapper.selectByPrimaryKey(refinedMaterial.getId());
               BeanUtils.copyProperties(refinedMaterial, dbRefinedMaterial);
               dbRefinedMaterial.setModifyDate(new Date());
               this.dbRefinedMaterialMapper.updateByPrimaryKey(dbRefinedMaterial);
           }
           return  materialCatalogOids;
    }

    /**
     * 通过事项查询细化材料
     * @param serviceOid
     * @return
     */
    public List<RefinedMaterial> getRefinedMaterialListByServiceOid(String serviceOid) {
        DbRefinedMaterialExample dbRefinedMaterialExample=new DbRefinedMaterialExample();
        DbRefinedMaterialExample.Criteria criteria=dbRefinedMaterialExample.createCriteria();
        if(StringUtils.isNotEmpty(serviceOid)){
            //criteria.andMaterialOidEqualTo(serviceOid);
            criteria.andServiceOidEqualTo(serviceOid);
        }
        criteria.andDeleteStatusEqualTo((short) 0);
        List<DbRefinedMaterial> dbRefinedMaterials=dbRefinedMaterialMapper.selectByExample(dbRefinedMaterialExample);
        List<RefinedMaterial> refinedMaterialList=dbRefinedMaterials.stream().map(dbRefinedMaterial -> {
            RefinedMaterial refinedMaterial = new RefinedMaterial();
            BeanUtils.copyProperties(dbRefinedMaterial, refinedMaterial);
            String  materialOid=refinedMaterial.getMaterialOid();
            //审查要点列表
            List<ReviewPoints>   reviewPointsList= reviewPointsManager.getReviewPointsListByRefinedMaterialOid(dbRefinedMaterial.getOid());
            refinedMaterial.setReviewPointsList(reviewPointsList);

            SxServiceMaterial sxServiceMaterial = sxServiceMaterialManager.getSxServiceMaterialByOid(dbRefinedMaterial.getMaterialOid());
            if(sxServiceMaterial!=null){
                refinedMaterial.setMaterialName(sxServiceMaterial.getMaterialName());
            }

            return refinedMaterial;
        }).collect(Collectors.toList());
        return refinedMaterialList;
    }


    @Transactional(rollbackFor = Exception.class)
    public void delete(String Oid) {
        Assert.hasLength(Oid, "删除主键不能为空！");
        DbRefinedMaterial dbRefinedMaterial = this.dbRefinedMaterialMapper.selectByPrimaryKey(Long.valueOf(Oid));
        Assert.notNull(dbRefinedMaterial, MessageFormat.format("删除对象不存在！对象id为{0}", Oid));
        dbRefinedMaterial.setDeleteStatus((short)1);
        dbRefinedMaterial.setModifyDate(new Date());
        this.dbRefinedMaterialMapper.updateByPrimaryKeySelective(dbRefinedMaterial);
        List<ReviewPoints> reviewPointsList = reviewPointsManager.getReviewPointsListByRefinedMaterialOid(dbRefinedMaterial.getOid());
        if (reviewPointsList.size() > 0) {
            for (ReviewPoints reviewPoints : reviewPointsList) {
                DbReviewPoints dbReviewPoints = new DbReviewPoints();
                BeanUtils.copyProperties(reviewPoints, dbReviewPoints);
                dbReviewPoints.setDeleteStatus((short)1);
                dbReviewPoints.setModifyDate(new Date());
                this.dbReviewPointsMapper.updateByPrimaryKeySelective(dbReviewPoints);
            }
        }
    }




    /**
     * 多张样本图转pdf文件
     * @param dzUrl
     * @param pdfPath
     * @return
     * @throws Exception
     */
    public Map<String, String> getPdf(List<String>  dzUrl, String pdfPath) throws Exception {
        Map<String, String> map=new HashMap<>();
        String materialSampleOid="";
        String materialSampleAddr = "";

        String url= PdfUtil.toPdf(dzUrl,pdfPath);
        String picBase642=ImageToBase64(url);

        //上传pdf
        SxSysAtta sxSysAtta=sxSysAttaManager.uploadBase64PdfToSX(picBase642);

       if (null!=sxSysAtta) {
            materialSampleOid=sxSysAtta.getOid();
            materialSampleAddr=sxSysAtta.getFilePath();

        }
        map.put("materialSampleOid",materialSampleOid);
        map.put("materialSampleAddr",materialSampleAddr);

        //保存后清除本地的附件
        File myFilePath = new File(pdfPath);
        myFilePath.delete();
        return map;

    }
    public  final static String DEFAULT_CHARACTER_SET = "UTF-8";
    /**
     * 本地地址图片转base64；
     * @param imgPath
     * @return
     * @throws
     */
    private  String ImageToBase64(String imgPath) throws Exception {
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        // log.info("本地图片转换Base64:" + encoder.encode(Objects.requireNonNull(data)));

        String base64= encoder.encode(Objects.requireNonNull(data));
        if(null!=base64){
            base64 = base64.replace("\n", "").replace("\t", "").replace("\r", "");
            base64 = URLDecoder.decode(base64, DEFAULT_CHARACTER_SET);
            String strOne = " ";
            String strTwo = "+";
            base64 = base64.replaceAll(strOne, strTwo);
        }
        return  base64;
    }

    /**
     * 获取细化材料信息
     * @param serviceOid
     * @return
     */
    public List<RefinedMaterial> getOnlyRefinedMaterialByServiceOid(String serviceOid) {
        DbRefinedMaterialExample dbRefinedMaterialExample=new DbRefinedMaterialExample();
        DbRefinedMaterialExample.Criteria criteria=dbRefinedMaterialExample.createCriteria();
        if(StringUtils.isNotEmpty(serviceOid)){
            //criteria.andMaterialOidEqualTo(serviceOid);
            criteria.andServiceOidEqualTo(serviceOid);
        }
        criteria.andDeleteStatusEqualTo((short) 0);
        List<DbRefinedMaterial> dbRefinedMaterials=dbRefinedMaterialMapper.selectByExample(dbRefinedMaterialExample);
        List<RefinedMaterial> refinedMaterialList=dbRefinedMaterials.stream().map(dbRefinedMaterial -> {
            RefinedMaterial refinedMaterial = new RefinedMaterial();
            BeanUtils.copyProperties(dbRefinedMaterial, refinedMaterial);
            return refinedMaterial;
        }).collect(Collectors.toList());
        return refinedMaterialList;
    }

    /**
     * 获取一件事目录细化材料信息
     * @param comboDirectoryOid
     * @return
     */
    public List<RefinedMaterial> getOnlyRefinedMaterialByComboDirectoryOid(String comboDirectoryOid) {
        DbRefinedMaterialExample dbRefinedMaterialExample=new DbRefinedMaterialExample();
        DbRefinedMaterialExample.Criteria criteria=dbRefinedMaterialExample.createCriteria();
        if(StringUtils.isNotEmpty(comboDirectoryOid)){
            criteria.andComboDirectoryOidEqualTo(comboDirectoryOid);
        }
        criteria.andDeleteStatusEqualTo((short) 0);
        List<DbRefinedMaterial> dbRefinedMaterials=dbRefinedMaterialMapper.selectByExample(dbRefinedMaterialExample);
        List<RefinedMaterial> refinedMaterialList=dbRefinedMaterials.stream().map(dbRefinedMaterial -> {
            RefinedMaterial refinedMaterial = new RefinedMaterial();
            BeanUtils.copyProperties(dbRefinedMaterial, refinedMaterial);
            return refinedMaterial;
        }).collect(Collectors.toList());
        return refinedMaterialList;
    }
}
