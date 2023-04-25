package com.zfsoft.ha.managers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.ha.data.HaTakeCatalogue;
import com.zfsoft.ha.data.requestData.CataloguePageRequestData;
import com.zfsoft.ha.data.requestData.HaTakeNumberRequestData;
import com.zfsoft.ha.data.vo.HaTakeCatalogueVo;
import com.zfsoft.ha.data.vo.HaTakeNumberData;
import com.zfsoft.ha.dbaccess.dao.DbHaTakeCatalogueMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkQueueMapper;
import com.zfsoft.ha.dbaccess.data.DbHaTakeCatalogue;
import com.zfsoft.ha.dbaccess.data.DbHaWorkQueue;
import com.zfsoft.ha.util.ClientServer;
import com.zfsoft.ocr.util.CommonRestUtil;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.utils.bean.BeanUtils;
import com.zfsoft.superwindow.service.easyquickcase.data.ServiceException;
import com.zfsoft.superwindow.util.SysCode;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/10/31 9:12
 */
@Service
@Slf4j
public class HaTakeCatalogueManager {
    @Value("${zfsoft.inter.url}")
    private String interUrl;

    @Value("${zfsoft.inter.getMachineCategoryTreeById}")
    private String getMachineCategoryTreeById;


    @Value("${zfsoft.inter.takeNumber}")
    private String takeNumber;

    @Value("${zfsoft.inter.takePriorityNumber}")
    private String takePriorityNumber;

    @Value("${zfsoft.inter.winNumbPush}")
    private String winNumbPush;


    @Resource
    DbHaTakeCatalogueMapper dbHaTakeCatalogueMapper;

    @Resource
    DbHaWorkQueueMapper dbHaWorkQueueMapper;

    /**
     * 同步、更新万达事项到本地数据库
     * @return
     * @throws Exception
     */

    @Transactional(rollbackFor = Exception.class)
    public boolean getAndLoadWandaCatalogue() throws Exception {
        //拼接inter地址
        String url = interUrl + getMachineCategoryTreeById;
        Map<String,Object> map = new HashMap<>();

        try{
            String mapResult1 = CommonRestUtil.sendPostString(url,map);
            JSONObject jsonObject = JSON.parseObject(mapResult1);
            if(200 == (Integer) jsonObject.get("code")){
                //获取对象数组
                List<HaTakeCatalogueVo> haTakeCatalogueVoList = JSON.parseArray(jsonObject.getString("data"), HaTakeCatalogueVo.class);
                if(haTakeCatalogueVoList==null || haTakeCatalogueVoList.size()==0) return true;
                int count = 0;
                //遍历，判断更新还是插入目录
                for (HaTakeCatalogueVo takeCatalogueVo : haTakeCatalogueVoList) {
//                    System.out.println(takeCatalogueVo);
                    DbHaTakeCatalogue haTakeCatalogue = new DbHaTakeCatalogue();
                    BeanUtils.copyProperties(takeCatalogueVo,haTakeCatalogue);
                    DbHaTakeCatalogue dbHaTakeCatalogue = dbHaTakeCatalogueMapper.selectByCategoryId(takeCatalogueVo.getCateGoryId());

                    if(dbHaTakeCatalogue==null){
                        //插入
                        haTakeCatalogue.setCreateDate(new Date());
                        haTakeCatalogue.setModifyDate(new Date());
                        haTakeCatalogue.setShowFlag((short) 0);
                        haTakeCatalogue.setDeleteFlag(String.valueOf(SysCode.DELETE_STATUS.NO));
                        count += dbHaTakeCatalogueMapper.insertSelective(haTakeCatalogue);
                    }else{
                        //更新
                        haTakeCatalogue.setModifyDate(new Date());
                        haTakeCatalogue.setId(dbHaTakeCatalogue.getId());
                        count += dbHaTakeCatalogueMapper.updateByPrimaryKeySelective(haTakeCatalogue);
                    }

                }
            }else{
                return false;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        }
        return true;
    }

    /**
     * 获取万达取号目录
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public List<HaTakeCatalogueVo> getWandaCatalogue() throws Exception {
        //拼接inter地址
        String url = interUrl + getMachineCategoryTreeById;
        Map<String,Object> map = new HashMap<>();
        List<HaTakeCatalogueVo> collect = new ArrayList<>();
        try{
            String mapResult1 = CommonRestUtil.sendPostString(url,map);
            JSONObject jsonObject = JSON.parseObject(mapResult1);
            if(200 == (Integer) jsonObject.get("code")){
                //获取对象数组
                List<HaTakeCatalogueVo> haTakeCatalogueVoList = JSON.parseArray(jsonObject.getString("data"), HaTakeCatalogueVo.class);
                if(haTakeCatalogueVoList==null || haTakeCatalogueVoList.size()==0) return collect;
                collect = haTakeCatalogueVoList.stream().filter(e -> "2楼取号机".equals(e.getCateGoryName())).collect(Collectors.toList());
                collect = collect.stream().peek(
                        e -> {
                            List<HaTakeCatalogueVo> collect1 = e.getChildMachineCategory().stream().filter(
                                    s -> (!"测试窗口".equals(s.getCateGoryName()) && (!"咨询帮办".equals(s.getCateGoryName())))
                            ).sorted(Comparator.comparing(HaTakeCatalogueVo::getCateGoryCode)).collect(Collectors.toList());
                            e.setChildMachineCategory(collect1);
                        }
                ).collect(Collectors.toList());

            }else{
                return collect;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        }
        return collect;
    }

    /**
     * 获取指定取号目录
     * @return
     * @throws Exception
     */
    @Transactional
    public List<HaTakeCatalogueVo> getAppointCatalogue() throws Exception {
        List<HaTakeCatalogueVo> collect = new ArrayList<>();
        try{
            DbHaTakeCatalogue dbHaTakeCatalogue = new DbHaTakeCatalogue();
            dbHaTakeCatalogue.setShowFlag((short) 1);
            List<DbHaTakeCatalogue> dbHaTakeCatalogues = dbHaTakeCatalogueMapper.queryCatalogueList(dbHaTakeCatalogue);
            List<HaTakeCatalogueVo> haTakeCatalogueVoList = BeanUtils.copyListProperties(dbHaTakeCatalogues, HaTakeCatalogueVo::new);

            collect = haTakeCatalogueVoList.stream().filter(item ->
                    item.getParentId() == null
            ).map((e) -> {
                e.setChildMachineCategory(haTakeCatalogueVoList.stream().filter(item ->
                        e.getId().equals(item.getParentId())
                ).collect(Collectors.toList()));
                return e;
            }).collect(Collectors.toList());

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        }
        return collect;
    }

    /**
     * 窗口取号
     * @param haTakeNumberRequestData
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject takeNumber(HaTakeNumberRequestData haTakeNumberRequestData) {

        try {
            String url = interUrl + takeNumber;
            HaTakeNumberData haTakeNumberData = new HaTakeNumberData();
            BeanUtils.copyProperties(haTakeNumberRequestData,haTakeNumberData);
            //窗口取号测试，先只调用测试窗口
//            haTakeNumberRequestData.setGroupId("861afddf-4511-444c-af8e-7de8e168007d");
//            haTakeNumberRequestData.setCateGoryId("82074911-349d-4333-8478-d8eb8452f39d");

            String s = JSON.toJSONString(haTakeNumberRequestData);
            String body = ClientServer.send(url,s,"utf-8");

            JSONObject jsonObject = JSON.parseObject(body);
            if(200 == (Integer) jsonObject.get("code")){
                JSONObject data = (JSONObject) jsonObject.get("data");
                JSONObject tickerMsg = (JSONObject) data.get("tickerMsg");
                String stNumber = tickerMsg.getString("stNumber");
                DbHaWorkQueue dbHaWorkQueue = new DbHaWorkQueue();
                dbHaWorkQueue.setId(Long.valueOf(haTakeNumberRequestData.getQueueId()));
                dbHaWorkQueue.setAgentTakeNumber(stNumber);
                dbHaWorkQueueMapper.updateByPrimaryKeySelective(dbHaWorkQueue);
                return data;
            }else{
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public PageResult<HaTakeCatalogue> queryCataloguePage(CataloguePageRequestData req) {
        //分页参数
        if (null == req.getPageNum() || req.getPageNum() <= 0) {
            req.setPageNum(1)  ;
        }
        if (null == req.getPageSize() || req.getPageSize() <= 0) {
            req.setPageSize(10);
        }
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        DbHaTakeCatalogue dbHaTakeCatalogue = new DbHaTakeCatalogue();
        dbHaTakeCatalogue.setCateGoryName(req.getCateGoryName());
        dbHaTakeCatalogue.setShowFlag(req.getShowFlag());
        dbHaTakeCatalogue.setParentId(req.getParentId());
        Page<DbHaTakeCatalogue> dbHaTakeCataloguePage = (Page<DbHaTakeCatalogue>) dbHaTakeCatalogueMapper.queryCatalogueList(dbHaTakeCatalogue);
        PageResult<HaTakeCatalogue> HaTakeCataloguePage = new PageResult<>(dbHaTakeCataloguePage.getPageNum(), dbHaTakeCataloguePage.getPageSize(), dbHaTakeCataloguePage.getTotal());

        List<HaTakeCatalogue> haTakeCatalogues = dbHaTakeCataloguePage.stream().map(dbHaTakeCatalogue1 -> {
            HaTakeCatalogue haTakeCatalogue = new HaTakeCatalogue();
            BeanUtils.copyProperties(dbHaTakeCatalogue1, haTakeCatalogue);
            return haTakeCatalogue;
        }).collect(Collectors.toList());

        HaTakeCataloguePage.setData(haTakeCatalogues);

        return HaTakeCataloguePage;
    }

    /**
     * 保存或更新用户资源信息
     *
     * @param haTakeCatalogue 取号目录数据
     * @return
     * @throws ServiceException
     */
    @Transactional
    public int saveOrUpdate(HaTakeCatalogue haTakeCatalogue) throws ServiceException {
        int index = 0;
        if (haTakeCatalogue.getId() != null) {
            //修改
            DbHaTakeCatalogue dbHaTakeCatalogue = new DbHaTakeCatalogue();
            org.springframework.beans.BeanUtils.copyProperties(haTakeCatalogue, dbHaTakeCatalogue);
            index = dbHaTakeCatalogueMapper.update(dbHaTakeCatalogue);
        } else {
            //新增
            DbHaTakeCatalogue dbHaTakeCatalogue = new DbHaTakeCatalogue();
//            haUserResource.setId(getId());
            org.springframework.beans.BeanUtils.copyProperties(haTakeCatalogue, dbHaTakeCatalogue);
            index = dbHaTakeCatalogueMapper.insertSelective(dbHaTakeCatalogue);
        }
        return index;
    }

    /**
     * 根据id删除用户你资源
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    @Transactional(rollbackFor = Exception.class)
    public HaTakeCatalogue deleteUserid(String id) throws ServiceException {
        DbHaTakeCatalogue dbHaTakeCatalogue = dbHaTakeCatalogueMapper.selectByPrimaryKey(Long.valueOf(id));
        dbHaTakeCatalogue.setDeleteFlag("1");

        dbHaTakeCatalogueMapper.update(dbHaTakeCatalogue);
        HaTakeCatalogue haTakeCatalogue = new HaTakeCatalogue();
        org.springframework.beans.BeanUtils.copyProperties(dbHaTakeCatalogue, haTakeCatalogue);
        return haTakeCatalogue;
    }


    public HaTakeCatalogue selectByPrimaryKey(String id) throws ServiceException {
        DbHaTakeCatalogue dbHaTakeCatalogue = dbHaTakeCatalogueMapper.selectByPrimaryKey(Long.valueOf(id));
        HaTakeCatalogue haTakeCatalogue = new HaTakeCatalogue();
        BeanUtils.copyProperties(dbHaTakeCatalogue, haTakeCatalogue);
        return haTakeCatalogue;
    }
}
