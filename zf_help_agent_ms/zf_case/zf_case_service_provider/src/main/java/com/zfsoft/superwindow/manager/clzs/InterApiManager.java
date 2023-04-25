package com.zfsoft.superwindow.manager.clzs;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.clzs.InterApi;
import com.zfsoft.superwindow.data.clzs.InterApiResponse;
import com.zfsoft.superwindow.dbaccess.dao.DbInterApiMapper;
import com.zfsoft.superwindow.dbaccess.data.DbInterApi;
import com.zfsoft.superwindow.dbaccess.data.DbInterApiExample;
import com.zfsoft.superwindow.util.BeanUtils;
import com.zfsoft.superwindow.util.SysCode;
import com.zfsoft.superwindow.util.UUIDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.thymeleaf.util.ListUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 卡证目录
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InterApiManager {

    @Resource
    private DbInterApiMapper dbInterApiMapper;

    @Resource
    private InterApiResponseManager interApiResponseManager;

    /**
     * 分页查询列表
     * @param interApi
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public PageResult<InterApi> queryInfoWithPage(InterApi interApi, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        //拼装参数
        DbInterApiExample dbInterApiExample = new DbInterApiExample();
        DbInterApiExample.Criteria criteria=dbInterApiExample.createCriteria();
        if (interApi != null) {
            if(StringUtils.isNotEmpty(interApi.getCode())){
                criteria.andCodeLike("%"+ interApi.getCode().trim() + "%");
            }
            if(StringUtils.isNotEmpty(interApi.getName())){
                criteria.andNameLike("%"+ interApi.getName().trim() + "%");
            }
        }

        criteria.andDeleteFlagEqualTo(SysCode.DELETE_STATUS.NO);
        dbInterApiExample.setOrderByClause(" CREATE_DATE DESC ");
        Page<DbInterApi> dbInterApiPage= (Page<DbInterApi>) dbInterApiMapper.selectByExample(dbInterApiExample);
        PageResult<InterApi> pageResult = new PageResult<>(dbInterApiPage.getPageNum(),dbInterApiPage.getPageSize(),dbInterApiPage.getTotal());

        List<InterApi> apiList= dbInterApiPage.stream().map(dbInterApi -> {
            InterApi api = new InterApi();
            BeanUtils.copyProperties(dbInterApi,api);
            return api;
        }).collect(Collectors.toList());
        pageResult.setData(apiList);
        return pageResult;
    }

    /***
    * @Description:  更新保存方法
    * @Author:liangss
    * @Date:2021/11/4
    * @Param: [cardCatalogue]
    */
    public void saveOrUpdate(InterApi interApi) throws Exception {
        DbInterApi dbInterApi = null;
        if (null != interApi.getId()) {
            dbInterApi=this.dbInterApiMapper.selectByPrimaryKey(interApi.getId());
            Assert.notNull(dbInterApi, MessageFormat.format("更新对象不存在！对象id为{0}", dbInterApi.getId()));
            org.springframework.beans.BeanUtils.copyProperties(interApi, dbInterApi);
            dbInterApi.setModifyDate(new Date());
            this.dbInterApiMapper.updateByPrimaryKeySelective(dbInterApi);
        } else {
            dbInterApi = new DbInterApi();
            org.springframework.beans.BeanUtils.copyProperties(interApi, dbInterApi);
            dbInterApi.setDeleteFlag(SysCode.DELETE_STATUS.NO);
            dbInterApi.setCreateDate(new Date());
            dbInterApi.setModifyDate(new Date());
            dbInterApi.setOid(UUIDUtil.randomUUID());
            this.dbInterApiMapper.insert(dbInterApi);
        }
        Long  interApiInterfaceOid=dbInterApi.getId();
        List<InterApiResponse> subList=interApi.getResponseList();
        if(subList !=null && subList.size()>0){
            for(InterApiResponse interApiElement:subList){
                if(StringUtils.isNotEmpty(interApiElement.getResponseCode())&&StringUtils.isNotEmpty(interApiElement.getResponseName())){
                    interApiElement.setInterApiId(interApiInterfaceOid);
                    interApiResponseManager.saveOrUpdate(interApiElement);
                }
            }
        }
    }

    /**
     *  oid查询
     * @param id 主键oid
     * @return DbCardCatalogue
     */
    public DbInterApi getInterApiByOid(Long id) {
        DbInterApiExample dbInterApiExample = new DbInterApiExample();
        DbInterApiExample.Criteria criteria=dbInterApiExample.createCriteria();
        if(id != null){
            criteria.andIdEqualTo(id);
        }
        criteria.andDeleteFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbInterApi> dbInterApiList = dbInterApiMapper.selectByExample(dbInterApiExample);
        if (dbInterApiList !=null && dbInterApiList.size()>0) {
            return dbInterApiList.get(0);
        } else {
            return null;
        }
    }

    /**
     *  查询接口信息by code
     * @param code
     * @return
     */
    public DbInterApi getInterApiByCode(String code) {
        DbInterApiExample dbInterApiExample = new DbInterApiExample();
        DbInterApiExample.Criteria criteria=dbInterApiExample.createCriteria();
        if(code != null){
            criteria.andCodeEqualTo(code);
        }
        criteria.andDeleteFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbInterApi> dbInterApiList = dbInterApiMapper.selectByExample(dbInterApiExample);
        if (dbInterApiList !=null && dbInterApiList.size()>0) {
            return dbInterApiList.get(0);
        } else {
            return null;
        }
    }

    public InterApi selectByPrimaryKey(Long id){
        InterApi interApi = new InterApi();
        DbInterApi dbInterApi = dbInterApiMapper.selectByPrimaryKey(id);
        BeanUtils.copyProperties(dbInterApi, interApi);
        return interApi;
    }

    public InterApi selectByCode(String code){
        InterApi interApi = new InterApi();
        DbInterApiExample dbInterApiExample = new DbInterApiExample();
        DbInterApiExample.Criteria criteria = dbInterApiExample.createCriteria();
        criteria.andCodeEqualTo(code);
        List<DbInterApi> dbInterApiList = dbInterApiMapper.selectByExample(dbInterApiExample);
        if (ListUtils.isEmpty(dbInterApiList)) {
            return null;
        }
        BeanUtils.copyProperties(dbInterApiList.get(0), interApi);
        return interApi;
    }

    /**
     * 获取所有的内部接口信息
     * @Author:WangKe
     * @Date:2022/20/19
     * @return List<DbInterApi>
     */
    public List<DbInterApi> queryAllInterApi(){
        DbInterApiExample dbInterApiExample = new DbInterApiExample();
        DbInterApiExample.Criteria criteria = dbInterApiExample.createCriteria();
        criteria.andDeleteFlagEqualTo(SysCode.DELETE_STATUS.NO);
        return dbInterApiMapper.selectByExample(dbInterApiExample);
    }

    /**
     * 获取所有的内部接口信息
     * @Author:WangKe
     * @Date:2022/20/19
     * @return List<DbInterApi>
     */
    public List<InterApiResponse> queryInterApiValById(Long id){
        return interApiResponseManager.getInterApiResponseList(id);
    }


    /**
     *  刪除信息
     * @param id
     */
    public void deleteByOid(Long id) {
        dbInterApiMapper.deleteByPrimaryKey(id);
    }

    /**
     *  检测是否已添加证照元素
     * @param interApi
     * @return
     */
    public String checkInterApiIsExist(InterApi interApi) {
        DbInterApiExample dbInterApiExample = new DbInterApiExample();
        DbInterApiExample.Criteria criteria = dbInterApiExample.createCriteria();
        if (interApi !=null) {
            if (StringUtils.isNotEmpty(interApi.getCode())) {
                criteria.andCodeEqualTo(interApi.getCode());
            }
            if (StringUtils.isNotEmpty(interApi.getName())) {
                criteria.andNameEqualTo(interApi.getName());
            }
        }
        criteria.andDeleteFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbInterApi> dbInterApis = dbInterApiMapper.selectByExample(dbInterApiExample);
        if (dbInterApis !=null && dbInterApis.size()>0) {
            return "InterApi";
        }
        return null;
    }

    /**
     *  保存查看证照是否存在
     * @param id
     * @param code
     * @return
     */
    public String checkHasRepeat(Long id ,String code) {
        DbInterApiExample dbInterApiExample = new DbInterApiExample();
        DbInterApiExample.Criteria criteria = dbInterApiExample.createCriteria();
        if (StringUtils.isNotEmpty(code)) {
            criteria.andCodeEqualTo(code);
        }
        criteria.andDeleteFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbInterApi> dbInterApis = dbInterApiMapper.selectByExample(dbInterApiExample);

        //重复判断
        String flag = "false";
        if (id != null) {
            if (dbInterApis !=null && dbInterApis.size()> 0) {
                DbInterApi dbEntityInterApi = dbInterApis.get(0);
                if (dbEntityInterApi !=null) {
                    if (!id.equals(dbEntityInterApi.getId())) {
                        flag = "true";
                    }
                }
            }
        } else {
            if (dbInterApis !=null && dbInterApis.size()>0) {
                flag = "true";
            }
        }
        return flag;
    }

    /**
     * 查询所有的配置的信息
     * @param billOids
     * @return
     * dongxl
     */
    public List<InterApi> queryInterApiListByBillOids(List<String> billOids) {
        DbInterApiExample dbInterApiExample = new DbInterApiExample();
        DbInterApiExample.Criteria criteria = dbInterApiExample.createCriteria();
        if (billOids != null && billOids.size()>0) {
            criteria.andOidIn(billOids);
        }
        criteria.andDeleteFlagEqualTo(SysCode.DELETE_STATUS.NO);
        List<DbInterApi> dbInterApis = dbInterApiMapper.selectByExample(dbInterApiExample);
        if(dbInterApis!=null && dbInterApis.size()>0){
           return dbInterApis.stream().map(license->{
                InterApi eleLince=new InterApi();
                BeanUtils.copyProperties(license,eleLince);
                return eleLince;
            }).collect(Collectors.toList());
        }
        return null;
    }

}
