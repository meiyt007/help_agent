package com.zfsoft.ha.managers;

import com.github.pagehelper.Page;
import com.zfsoft.ha.constant.Constants;
import com.zfsoft.ha.data.HaLoginUserInfo;
import com.zfsoft.ha.data.HaUserResource;
import com.zfsoft.ha.data.responseData.HaUserResourceResponseVoData;
import com.zfsoft.ha.data.vo.HaUserResourceVo;
import com.zfsoft.ha.dbaccess.dao.DbHaUserResourceMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkUserMapper;
import com.zfsoft.ha.dbaccess.data.DbHaUserResource;
import com.zfsoft.ha.dbaccess.data.DbHaWorkUser;
import com.zfsoft.ha.dbaccess.data.vo.DbHaUserResourceVo;
import com.zfsoft.ha.util.HaLoginUserHolder;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.service.easyquickcase.data.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HaUserResourceManager {
    @Resource
    private DbHaUserResourceMapper dbHaUserResourceMapper;

    @Resource
    private DbHaWorkUserMapper dbHaWorkUserMapper;

    /**
     * 保存用户资源信息
     *
     * @param haUserResource 资源信息
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public HaUserResource saveHaUserResource(HaUserResource haUserResource) {
        if (null == haUserResource.getId()) {
            haUserResource.setId(null);
            haUserResource.setCreateDate(new Date());
        }
//        if(null == haUserResource.getAttaOid()){
//            haUserResource.setAttaOid(UUID.randomUUID().toString().replaceAll("-", ""));
//        }
        haUserResource.setUpdateDate(new Date());
        haUserResource.setDeleteStatus("1");
        DbHaUserResource DbHaUserResource = new DbHaUserResource();
        BeanUtils.copyProperties(haUserResource, DbHaUserResource);
        int index = 0;
        if (null == haUserResource.getId()) {
            index = dbHaUserResourceMapper.insert(DbHaUserResource);
        } else {
            index = dbHaUserResourceMapper.update(DbHaUserResource);
        }
        return haUserResource;
    }

    /**
     * 根据名称获取用户资源信息
     *
     * @param name
     * @return
     */
    public PageResult<HaUserResource> queryHaUserResourceByName(String name) {
//        if (StringUtil.isEmpty(name)) {
//            throw new ResultInfoException("附件业务主键为空！");
//        }
        List<HaUserResource> haUserResources = null;
        Page<DbHaUserResource> dbHaUserResources = (Page<DbHaUserResource>) dbHaUserResourceMapper.queryHaUserResourceByName(name);
        PageResult<HaUserResource> pageResult = new PageResult<>(dbHaUserResources.getPageNum(), dbHaUserResources.getPageSize(), dbHaUserResources.getTotal());
        List<HaUserResource> haUserResourceList = dbHaUserResources.stream().map(dbHaUserResource -> {
            HaUserResource haUserResource = new HaUserResource();
            BeanUtils.copyProperties(dbHaUserResource, haUserResource);
            return haUserResource;
        }).collect(Collectors.toList());
        pageResult.setData(haUserResourceList);
        return pageResult;
//        List<DbHaUserResource> dbHaUserResources = dbHaUserResourceMapper.queryHaUserResourceByName(name);
//        if(dbHaUserResources !=null){
//            haUserResources = dbHaUserResources.stream().map(dbQlCase -> {
//                HaUserResource haUserResource = new HaUserResource();
//                BeanUtils.copyProperties(dbHaUserResources, haUserResource);
//                return haUserResource;
//            }).collect(Collectors.toList());
//        }
//        return haUserResources;
    }

    /**
     * 根据资源名称，工作用户名称获取用户资源信息
     *
     * @param name         资源名称
     * @param wokeUserName 工作用户名称
     * @return
     */
    public PageResult<HaUserResourceVo> queryHaUserResourceByNameAndWorkUserName(String name, String wokeUserName) {
        Page<DbHaUserResourceVo> dbHaUserResourcePage = (Page<DbHaUserResourceVo>) dbHaUserResourceMapper.queryHaUserResourceByNameAndWorkUserName(name, wokeUserName);
        PageResult<HaUserResourceVo> pageResult = new PageResult<>(dbHaUserResourcePage.getPageNum(), dbHaUserResourcePage.getPageSize(), dbHaUserResourcePage.getTotal());
        List<HaUserResourceVo> haUserResourceList = dbHaUserResourcePage.stream().map(dbHaUserResource -> {
            HaUserResourceVo haUserResource = new HaUserResourceVo();
            BeanUtils.copyProperties(dbHaUserResource, haUserResource);
            return haUserResource;
        }).collect(Collectors.toList());
        pageResult.setData(haUserResourceList);
        return pageResult;
    }

    /**
     * 根据资源名称，资源父id获取用户资源信息
     *
     * @param name     资源名称
     * @param parentId 资源父id
     * @return
     * @throws ServiceException
     */
    public List<HaUserResource> queryHaUserResourceByNameAndParentId(String name, String parentId,String workUserId) throws ServiceException {
        List<DbHaUserResource> dbHaUserResources = dbHaUserResourceMapper.queryHaUserResourceByNameAndParentId(name, parentId,workUserId);
        List<HaUserResource> haUserResourceList = dbHaUserResources.stream().map(dbHaUserResource -> {
            HaUserResource haUserResource = new HaUserResource();
            BeanUtils.copyProperties(dbHaUserResource, haUserResource);
            return haUserResource;
        }).collect(Collectors.toList());
        return haUserResourceList;
    }

    public List<HaUserResource> queryHaUserResourceByType(String type) throws ServiceException {
        List<DbHaUserResource> dbHaUserResources = dbHaUserResourceMapper.queryHaUserResourceByType(type);
        List<HaUserResource> haUserResourceList = dbHaUserResources.stream().map(dbHaUserResource -> {
            HaUserResource haUserResource = new HaUserResource();
            BeanUtils.copyProperties(dbHaUserResource, haUserResource);
            return haUserResource;
        }).collect(Collectors.toList());
        return haUserResourceList;
    }

    public List<HaUserResource> queryHaUserResourceByIdAndType(String id, String type) throws ServiceException {
        List<DbHaUserResource> dbHaUserResources = dbHaUserResourceMapper.queryHaUserResourceByIdAndType(id, type);
        List<HaUserResource> haUserResourceList = dbHaUserResources.stream().map(dbHaUserResource -> {
            HaUserResource haUserResource = new HaUserResource();
            BeanUtils.copyProperties(dbHaUserResource, haUserResource);
            return haUserResource;
        }).collect(Collectors.toList());
        return haUserResourceList;
    }

    /**
     * 生成Long 类型唯一ID
     */
//    public synchronized static Long getId() {
//        //如果需要更长 或者更大冗余空间, 只需要 time * 10^n   即可
//        //当前可保证1毫秒 生成 10000条不重复
//        Long time = Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()))*10000;
//        if (id == null) {
//            id = new AtomicLong(time);
//            return id.get();
//        }
//        if (time <= id.get()) {
//            id.addAndGet(1);
//        } else {
//            id = new AtomicLong(time);
//        }
//        return id.get();
//    }

    /**
     * 保存或更新用户资源信息
     *
     * @param haUserResource 用户资源信息
     * @return
     * @throws ServiceException
     */
    @Transactional
    public int saveOrUpdateHaUserResource(HaUserResource haUserResource) throws ServiceException {
        int index = 0;
        if (haUserResource.getId() != null) {
            //修改
            DbHaUserResource dbHaUserResource = new DbHaUserResource();
            BeanUtils.copyProperties(haUserResource, dbHaUserResource);
            index = dbHaUserResourceMapper.update(dbHaUserResource);
        } else {
            //新增
            DbHaUserResource dbHaUserResource = new DbHaUserResource();
//            haUserResource.setId(getId());
            BeanUtils.copyProperties(haUserResource, dbHaUserResource);
            index = dbHaUserResourceMapper.insertSelective(dbHaUserResource);
            haUserResource.setId(dbHaUserResource.getId());
        }
        return index;
    }

    public HaUserResourceVo getHaUserResourceById(String id) {
        DbHaUserResourceVo dbHaUserResource = dbHaUserResourceMapper.queryById(Long.valueOf(id));
        HaUserResourceVo haUserResource = new HaUserResourceVo();
        BeanUtils.copyProperties(dbHaUserResource, haUserResource);
        return haUserResource;
    }

    /**
     * 根据id删除用户你资源
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    @Transactional(rollbackFor = Exception.class)
    public HaUserResource deleteUserid(String id) throws ServiceException {
        HaLoginUserInfo loginUser = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        DbHaUserResourceVo dbHaUserResourceVo = dbHaUserResourceMapper.queryById(Long.valueOf(id));
        DbHaUserResource dbHaUserResource = new DbHaUserResource();
        BeanUtils.copyProperties(dbHaUserResourceVo, dbHaUserResource);
        dbHaUserResource.setDeleteStatus("2");
        dbHaUserResource.setUpdateBy(loginUser.getName());
        dbHaUserResource.setUpdateDate(new Date());

        dbHaUserResourceMapper.update(dbHaUserResource);
        HaUserResource haUserResource = new HaUserResource();
        BeanUtils.copyProperties(dbHaUserResource, haUserResource);
        return haUserResource;
    }

    @Transactional(rollbackFor = Exception.class)
    public int shareResource(String id, String workUserIds) {
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        DbHaUserResourceVo dbHaUserResourceVo = dbHaUserResourceMapper.queryById(Long.valueOf(id));
        if(dbHaUserResourceVo==null){
            return -110;
        }

//        if("1".equals(dbHaUserResourceVo.getType())){
//            return -100;
//        }

        DbHaWorkUser dbHaWorkUser = dbHaWorkUserMapper.queryById(dbHaUserResourceVo.getWorkUserId());
        String[] split = workUserIds.split(",");
        if(split.length == 0) return -101;
        int index=0;
        for (String workUserId : split) {
            //查询需要分享的目标帮办人，是否有分享文件夹
            List<DbHaUserResource> shareFolders = dbHaUserResourceMapper.queryHaUserResourceByNameAndParentId("分享文件夹", null,workUserId);
            //如果不存在分享文件夹，则创建
            if(shareFolders==null || shareFolders.size()==0){
                HaUserResource haUserResource = new HaUserResource();
                haUserResource.setWorkUserId(Long.valueOf(workUserId));
                haUserResource.setType("1");
                haUserResource.setName("分享文件夹");
                haUserResource.setDeleteStatus(Constants.DELETE_STATUS_NO);
                haUserResource.setCreateBy(currentHaLoginUserInfo.getName());
                haUserResource.setCreateDate(new Date());
                int i = saveOrUpdateHaUserResource(haUserResource);
                if(i == 0){
                    return -102;
                }
            }
            //如果分享文件夹里面没有分享发起人的文件夹，则新建
            List<DbHaUserResource> shareFolders2 = dbHaUserResourceMapper.queryHaUserResourceByNameAndParentId("分享文件夹", null,workUserId);
            DbHaUserResource shareFolder = shareFolders2.get(0);
            List<DbHaUserResource> workNameFolders = dbHaUserResourceMapper.queryHaUserResourceByNameAndParentId(dbHaWorkUser.getName(), String.valueOf(shareFolder.getId()),workUserId);
            if(workNameFolders==null || workNameFolders.size()==0){
                HaUserResource haUserResource = new HaUserResource();
                haUserResource.setWorkUserId(Long.valueOf(workUserId));
                haUserResource.setType("1");
                haUserResource.setName(dbHaWorkUser.getName());
                haUserResource.setParentId(shareFolder.getId());
                haUserResource.setDeleteStatus(Constants.DELETE_STATUS_NO);
                haUserResource.setCreateBy(currentHaLoginUserInfo.getName());
                haUserResource.setCreateDate(new Date());
                int i = saveOrUpdateHaUserResource(haUserResource);
                if(i == 0){
                    return -103;
                }
            }
            //分享文件
            List<DbHaUserResource> workNameFolders2 = dbHaUserResourceMapper.queryHaUserResourceByNameAndParentId(dbHaUserResourceVo.getWorkUserName(), String.valueOf(shareFolder.getId()),workUserId);

            HaUserResource haUserResource = new HaUserResource();
            BeanUtils.copyProperties(dbHaUserResourceVo,haUserResource);
            haUserResource.setId(null);
            haUserResource.setParentId(workNameFolders2.get(0).getId());
            haUserResource.setWorkUserId(Long.valueOf(workUserId));
            haUserResource.setCreateDate(new Date());
            haUserResource.setCreateBy(currentHaLoginUserInfo.getName());
            int i = saveOrUpdateHaUserResource(haUserResource);
            if(i == 0){
                return -104;
            }
            index+=i;
            //如果是文件夹且有子节点
            if("1".equals(dbHaUserResourceVo.getType())){

                if(haUserResource.getId() == null){
                    continue;
                }
                List<HaUserResource> haUserResources = this.queryHaUserResourceByNameAndParentId("", id, String.valueOf(currentHaLoginUserInfo.getId()));
                if(haUserResources!=null){
                    List<Integer> collect = haUserResources.stream().map(e -> ((HaUserResourceManager) AopContext.currentProxy()).shareResource(String.valueOf(e.getId()), workUserId, String.valueOf(haUserResource.getId()))).collect(Collectors.toList());
                    log.info("分享文件夹{}给{}成功,分享的结果:{}",dbHaUserResourceVo.getName(),workUserId,collect);
                }
            }

        }

        return index;

    }

    @Transactional(rollbackFor = Exception.class)
    public int shareResource(String id, String workUserId,String parentId) {
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        DbHaUserResourceVo dbHaUserResourceVo = dbHaUserResourceMapper.queryById(Long.valueOf(id));
        if(dbHaUserResourceVo==null){
            return -110;
        }
        int index=0;
        if(parentId == null){
//            ((HaUserResourceManager) AopContext.currentProxy()).shareResource(id,workUserId);
            return index;
        }else{

            HaUserResource haUserResource2 = new HaUserResource();
            BeanUtils.copyProperties(dbHaUserResourceVo,haUserResource2);
            haUserResource2.setId(null);
            haUserResource2.setParentId(Long.valueOf(parentId));
            haUserResource2.setWorkUserId(Long.valueOf(workUserId));
            haUserResource2.setCreateDate(new Date());
            haUserResource2.setCreateBy(currentHaLoginUserInfo.getName());
            int i2 = saveOrUpdateHaUserResource(haUserResource2);
            if(i2 == 0){
                return -104;
            }
            index+=i2;
            //如果是文件夹且有子节点
            if("1".equals(dbHaUserResourceVo.getType())){
                List<HaUserResource> haUserResources = this.queryHaUserResourceByNameAndParentId("", id, String.valueOf(currentHaLoginUserInfo.getId()));
                if(haUserResources!=null){
                    haUserResources.stream().map(e->((HaUserResourceManager) AopContext.currentProxy()).shareResource(String.valueOf(e.getId()), workUserId, String.valueOf(haUserResource2.getId())));
                }
            }

        }

        return index;

    }

    public HaUserResourceResponseVoData getHaUserResourceVo(Long resourceId) {
        HaLoginUserInfo currentHaLoginUserInfo = HaLoginUserHolder.getCurrentHaLoginUserInfo();
        HaUserResourceResponseVoData result = new HaUserResourceResponseVoData();
        DbHaUserResourceVo dbHaUserResourceVo = dbHaUserResourceMapper.queryById(resourceId);
        BeanUtils.copyProperties(dbHaUserResourceVo,result);
        List<HaUserResource> haUserResources =((HaUserResourceManager) AopContext.currentProxy()).queryHaUserResourceByNameAndParentId("", String.valueOf(resourceId), String.valueOf(currentHaLoginUserInfo.getId()));
        List<HaUserResourceResponseVoData> setVo = new ArrayList<>();
        for(HaUserResource ha:haUserResources){
            HaUserResourceResponseVoData haUserResourceVo = ((HaUserResourceManager) AopContext.currentProxy()).getHaUserResourceVo(ha.getId());
            setVo.add(haUserResourceVo);
        }
        result.setHaUserResourceResponseVoData(setVo);
        return result;
    }
}
