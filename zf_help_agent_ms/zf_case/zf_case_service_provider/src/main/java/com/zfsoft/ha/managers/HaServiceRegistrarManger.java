package com.zfsoft.ha.managers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.feign.SysOrganFeginService;
import com.zfsoft.cases.util.StringUtil;
import com.zfsoft.ha.data.HaWorkGroup;
import com.zfsoft.ha.data.vo.HaServiceVo;
import com.zfsoft.ha.data.vo.HaWorkUserRegistrarVo;
import com.zfsoft.ha.data.HaUserService;
import com.zfsoft.ha.data.vo.HaServiceRegistrarVo;
import com.zfsoft.ha.dbaccess.dao.DbHaUserServiceMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkGroupMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaWorkUserMapper;
import com.zfsoft.ha.dbaccess.data.DbHaUserService;
import com.zfsoft.ha.dbaccess.data.DbHaWorkGroup;
import com.zfsoft.ha.dbaccess.data.vo.DbHaWorkUserRegistrarVo;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.bean.BeanUtils;
import com.zfsoft.service.sxService.data.SxService;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 事项授权或帮代办人员授权manger
 *
 * @author yupeng
 * @version 1.0
 * @date 2022年08月03日 14:47:32
 */
@Service
public class HaServiceRegistrarManger {

    /**
     * 帮代办人员mapper
     */
    @Resource
    private DbHaWorkUserMapper dbHaWorkUserMapper;

    /**
     * 事项授权或帮代办人员授权mapper
     */
    @Resource
    private DbHaUserServiceMapper userServiceMapper;

    /**
     * 帮代办人员分组mapper
     */
    @Resource
    private DbHaWorkGroupMapper workGroupMapper;

    /**
     * 部门信息外部模块调用接口
     */
    @Resource
    private SysOrganFeginService sysOrganFeginService;

    /**
     * 查询所有的帮代办人员
     *
     * @return 帮代办人员集合
     * @author yupeng
     * @date 2022年08月01 16:25:46
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public ApiResultSet getAllHelpWorkUserList(String serviceOid) {

        HaServiceRegistrarVo serviceRegistrarVo = new HaServiceRegistrarVo();
        //这个map是将帮代办用户按照组名，分组职务进行归类
        Map<String, Map<String, List<JSONObject>>> groupMap = new HashMap<>();
        //查询所有帮代办人员信息
        List<DbHaWorkUserRegistrarVo> dbHaQWorkUserVoList = dbHaWorkUserMapper.queryAllHelpWorkUserList(null, null, null);

        //根据事项id查询已授权集合
        List<DbHaUserService> userServiceList = userServiceMapper.queryUserServiceByServiceId(serviceOid);

        Map<Long, JSONObject> registrarMap = new HashMap<>();

        //授权集合信息封装到map中
        userServiceList.forEach(dbHaUserService -> {
            Long workUserId = dbHaUserService.getWorkUserId();
            String serviceType = dbHaUserService.getServiceType();
            if (registrarMap.containsKey(workUserId)) {
                JSONObject helpWorkUserObject = registrarMap.get(workUserId);
                if (Objects.equals(serviceType, "SJ")) {
                    helpWorkUserObject.put("SJUserServiceOid", dbHaUserService.getId());
                }
                if (Objects.equals(serviceType, "CLZB")) {
                    helpWorkUserObject.put("CLZBUserServiceOid", dbHaUserService.getId());
                }
                helpWorkUserObject.put(dbHaUserService.getServiceType(), dbHaUserService.getServiceStatus());
            } else {
                JSONObject helpWorkUserObject = new JSONObject();
                helpWorkUserObject.put(dbHaUserService.getServiceType(), dbHaUserService.getServiceStatus());
                if (Objects.equals(serviceType, "SJ")) {
                    helpWorkUserObject.put("SJUserServiceOid", dbHaUserService.getId());
                }
                if (Objects.equals(serviceType, "CLZB")) {
                    helpWorkUserObject.put("CLZBUserServiceOid", dbHaUserService.getId());
                }
                helpWorkUserObject.put("userServiceOid", dbHaUserService.getId());
                registrarMap.put(workUserId, helpWorkUserObject);
            }
        });

        //封装tree
        JSONArray treeArray = new JSONArray();
        //封装所有的子节点信息
        JSONArray allChildNodeArray = new JSONArray();
        //封装选中的子节点id
        JSONArray defaultCheckedArray = new JSONArray();
        //封装已选中的子节点所有信息
        JSONArray checkedArray = new JSONArray();
        dbHaQWorkUserVoList.forEach(dbHaWorkUserVo -> {
            //组名
            String groupName = dbHaWorkUserVo.getGroupName();
            //分组职务
            String groupPost = dbHaWorkUserVo.getGroupPost();

            //将帮代办员工的主键id及账号封装到JSONObject
            JSONObject helpWorkUserObject = new JSONObject();
            Long workUserId = dbHaWorkUserVo.getId();
            helpWorkUserObject.put("name", dbHaWorkUserVo.getWorkUserName());

            JSONObject registrarObject = null;
            if (registrarMap.containsKey(workUserId)) {
                registrarObject = registrarMap.get(workUserId);
            }

            //为每个帮代办人员添加默认服务类型
            JSONArray memberArray = new JSONArray();

            JSONObject serviceTypeObject1 = new JSONObject();
            serviceTypeObject1.put("name", "材料准备");
            serviceTypeObject1.put("helpWorkUserId", workUserId);

            JSONObject serviceTypeObject2 = new JSONObject();
            serviceTypeObject2.put("name", "收件");
            serviceTypeObject2.put("helpWorkUserId", dbHaWorkUserVo.getId());

            if (registrarObject != null) {
                if (registrarObject.containsKey("CLZB")) {
                    String serviceStatus = registrarObject.getString("CLZB");
                    if (Objects.equals(serviceStatus, "1")) {
                        serviceTypeObject1.put("id", registrarObject.getString("CLZBUserServiceOid"));
                        defaultCheckedArray.add(registrarObject.getString("CLZBUserServiceOid"));
                        checkedArray.add(serviceTypeObject1);
                    } else {
                        serviceTypeObject1.put("id", "");
                    }
                }
                if (registrarObject.containsKey("SJ")) {
                    String serviceStatus = registrarObject.getString("SJ");
                    if (Objects.equals(serviceStatus, "1")) {
                        serviceTypeObject2.put("id", registrarObject.getString("SJUserServiceOid"));
                        defaultCheckedArray.add(registrarObject.getString("SJUserServiceOid"));
                        checkedArray.add(serviceTypeObject2);
                    } else {
                        serviceTypeObject2.put("id", "");
                    }
                }
            }

            allChildNodeArray.add(serviceTypeObject1);
            allChildNodeArray.add(serviceTypeObject2);
            memberArray.add(serviceTypeObject1);
            memberArray.add(serviceTypeObject2);
            helpWorkUserObject.put("member", memberArray);

            //判断组名，将相同的组名的分配到一起
            if (groupMap.containsKey(groupName)) {
                Map<String, List<JSONObject>> groupPostMap = groupMap.get(groupName);
                //判断分组职务，将相同的划分到一起
                if (groupPostMap.containsKey(groupPost)) {
                    List<JSONObject> helpWorkUserList = groupPostMap.get(groupPost);
                    helpWorkUserList.add(helpWorkUserObject);
                } else {
                    List<JSONObject> helpWorkUserList = new ArrayList<>();
                    helpWorkUserList.add(helpWorkUserObject);
                    groupPostMap.put(groupPost, helpWorkUserList);
                }
            } else {
                Map<String, List<JSONObject>> groupPostMap = new HashMap<>();
                List<JSONObject> helpWorkUserList = new ArrayList<>();
                helpWorkUserList.add(helpWorkUserObject);
                groupPostMap.put(groupPost, helpWorkUserList);
                groupMap.put(groupName, groupPostMap);
            }
        });

        //遍历分组后的数据进行封装，返回给页面直接使用
        for (String groupName : groupMap.keySet()) {
            JSONObject groupObject = new JSONObject();
            groupObject.put("name", groupName);
            Map<String, List<JSONObject>> groupPostMap = groupMap.get(groupName);
            JSONArray groupPostArray = new JSONArray();
            for (String groupPost : groupPostMap.keySet()) {
                JSONObject groupPostObject = new JSONObject();
                //将list转json
                JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(groupPostMap.get(groupPost)));
                groupPostObject.put("name", groupPost);
                groupPostObject.put("member", jsonArray);
                groupPostArray.add(groupPostObject);
            }
            //内层分组职务进行排序
            if (groupPostArray.size() > 0) {
                groupPostArray.sort(Comparator.comparing(groupPostObject -> ((JSONObject) groupPostObject).getString("name")));
            }
            groupObject.put("member", groupPostArray);
            treeArray.add(groupObject);
        }

        //外层分组排序
        if (treeArray.size() > 0) {
            treeArray.sort(Comparator.comparing(groupObject -> ((JSONObject) groupObject).getString("name")));
        }

        serviceRegistrarVo.setTreeArray(treeArray);
        serviceRegistrarVo.setAllChildNodeArray(allChildNodeArray);
        serviceRegistrarVo.setCheckedArray(checkedArray);
        serviceRegistrarVo.setDefaultCheckedArray(defaultCheckedArray);
        return ApiResultSet.ok(serviceRegistrarVo);
    }

    /**
     * 保存事项授权信息
     *
     * @return ApiResultSet 保存结果
     * @author yupeng
     * *@date 2022年08月03 16:45:36
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public String saveOrUpdateServiceRegistrar(JSONObject jsonObject) {

        //事项id
        String serviceOid = jsonObject.getString("serviceOid");
        //所有的
        JSONArray allNodeArray = jsonObject.getJSONArray("allNodeList");
        //选中的
        JSONArray checkedArray = jsonObject.getJSONArray("checkedNodeList");
        //未选中的
        List<Object> unCheckedList = allNodeArray.stream().filter(node -> !checkedArray.contains(node)).collect(Collectors.toList());

        //处理选中的
        for (Object object : checkedArray) {
            JSONObject checkedObject = JSON.parseObject(JSON.toJSONString(object));
            //员工id
            Long helpWorkUserId = Long.valueOf(checkedObject.getString("helpWorkUserId"));
            //服务类型
            String serviceType = checkedObject.getString("name");
            Integer handleResult = saveOrUpdateUserService(helpWorkUserId, serviceOid, serviceType, "1");
            if (handleResult < 0) {
                return "保存失败！";
            }
        }

        //处理未选中的
        for (Object object : unCheckedList) {
            JSONObject checkedObject = JSON.parseObject(JSON.toJSONString(object));
            //员工id
            Long helpWorkUserId = Long.valueOf(checkedObject.getString("helpWorkUserId"));
            //服务类型
            String serviceType = checkedObject.getString("name");
            Integer handleResult = saveOrUpdateUserService(helpWorkUserId, serviceOid, serviceType, "2");
            if (handleResult < 0) {
                return "保存失败！";
            }
        }

        return null;
    }

    /**
     * 查询所有的分组信息
     *
     * @return 分组信息集合
     * @author yupeng
     * @date 2022年08月05 09:34:17
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public List<HaWorkGroup> getAllGroup() {
        //查询所有的分组
        List<DbHaWorkGroup> dbHaWorkGroupList = workGroupMapper.selectByExample(null);
        return dbHaWorkGroupList.stream().map(dbHaWorkGroup -> {
            HaWorkGroup workGroup = new HaWorkGroup();
            BeanUtils.copyProperties(dbHaWorkGroup, workGroup);
            return workGroup;
        }).collect(Collectors.toList());
    }

    /**
     * 查询授权帮代办人员列表
     *
     * @param groupName    分组名
     * @param postName     分组职务
     * @param workUserName 帮代办人员姓名
     * @param pageNum      分页参数
     * @param pageSize     分页参数
     * @return 分页列表
     * @author yupeng
     * @date 2022年08月05 10:21:19
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public PageResult<HaWorkUserRegistrarVo> queryWorkUserRegistrarPage(String groupName, String postName, String workUserName, Integer pageNum, Integer pageSize) {
        //分页参数
        if (null == pageNum || pageNum <= 0) {
            pageNum = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);

        //查询帮代办人员信息
        Page<DbHaWorkUserRegistrarVo> dbHaQWorkUserVoPage = (Page<DbHaWorkUserRegistrarVo>) dbHaWorkUserMapper.queryAllHelpWorkUserList(groupName, postName, workUserName);

        PageResult<HaWorkUserRegistrarVo> pageResult = new PageResult<>(dbHaQWorkUserVoPage.getPageNum(), dbHaQWorkUserVoPage.getPageSize(), dbHaQWorkUserVoPage.getTotal());

        List<HaWorkUserRegistrarVo> haWorkUserRegistrarVoList = dbHaQWorkUserVoPage.stream().map(dbHaWorkUserRegistrarVo -> {
            HaWorkUserRegistrarVo haWorkUserRegistrarVo = new HaWorkUserRegistrarVo();
            BeanUtils.copyProperties(dbHaWorkUserRegistrarVo, haWorkUserRegistrarVo);
            return haWorkUserRegistrarVo;
        }).collect(Collectors.toList());

        pageResult.setData(haWorkUserRegistrarVoList);

        return pageResult;
    }


    /**
     * 保存或修改帮代办人员授权
     *
     * @param jsonArray 授权信息
     * @return 保存结果信息
     * @author yupeng
     * @date 2022年08月05 17:24:19
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public Integer saveOrUpdateWorkUserRegistrar(JSONArray jsonArray) {
        for (Object object : jsonArray) {
            //object转JSONObject
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(object));
            //员工id
            Long helpWorkUserId = Long.valueOf(jsonObject.getString("helpWorkUserId"));
            //事项oid
            String serviceOid = jsonObject.getString("serviceOid");
            //服务类型
            JSONArray typeArray = jsonObject.getJSONArray("serviceType");
            for (Object o : typeArray) {
                JSONObject typeObject = JSON.parseObject(JSON.toJSONString(o));
                String typeName = typeObject.getString("typeName");
                String status = typeObject.getString("status");
                Integer handleResult = saveOrUpdateUserService(helpWorkUserId, serviceOid, typeName, status);
                if (handleResult < 0) {
                    return handleResult;
                }
            }
        }
        return null;
    }

    /**
     * 获取用户的事项权限
     *
     * @param serviceIds 事项ids 逗号隔开
     * @param userId     用户id
     * @author zhaobf
     * @date 2022年08月05 11:06
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public List<HaUserService> queryUserServiceByServiceIdsAndUserId(String serviceIds, Long userId) {
        List<DbHaUserService> dbHaUserServices = userServiceMapper.queryUserServiceByServiceIdsAndUserId( Arrays.asList(serviceIds.split(",")), userId);
        return dbHaUserServices.stream().map(dbHaUserResource -> {
            HaUserService haUserService = new HaUserService();
            BeanUtils.copyProperties(dbHaUserResource, haUserService);
            return haUserService;
        }).collect(Collectors.toList());
    }

    /**
     * 查询帮代办授权中的事项列表
     *
     * @param sxServicePage 事项分页数据
     * @param workUserId    帮代办表主键id
     * @return 事项结果集
     * @author yupeng
     * *@date 2022年08月08 10:11:54
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public PageResult<HaServiceVo> queryServiceInfoPage(PageResult<SxService> sxServicePage, String workUserId) {

        PageResult<HaServiceVo> resultPage = new PageResult<>();

        List<HaServiceVo> haServiceVoList = new ArrayList<>();

        //查询到的事项
        List<SxService> sxServiceList = sxServicePage.getData();

        resultPage.setPageNum(sxServicePage.getPageNum());
        resultPage.setPageSize(sxServicePage.getPageSize());
        resultPage.setTotal(sxServicePage.getTotal());

        Long helpWorkUserId = Long.valueOf(workUserId);
        //查询到事项后，再根据事项信息查询用户授权信息
        sxServiceList.forEach(sxService -> {
            HaServiceVo serviceVo = new HaServiceVo();
            BeanUtils.copyProperties(sxService, serviceVo);
            String serviceOid = sxService.getServiceOid();
            //查询授权记录
            List<DbHaUserService> dbUserServiceList = userServiceMapper.queryUserServiceByServiceIdsAndUserId( Arrays.asList(serviceOid.split(",")), helpWorkUserId);
            dbUserServiceList.forEach(dbHaUserService -> {
                if (Objects.equals("CLZB", dbHaUserService.getServiceType())) {
                    serviceVo.setPrepareMaterialStatus(dbHaUserService.getServiceStatus());
                }
                if (Objects.equals("SJ", dbHaUserService.getServiceType())) {
                    serviceVo.setReceiveMaterialStatus(dbHaUserService.getServiceStatus());
                }
            });
            haServiceVoList.add(serviceVo);
        });

        resultPage.setData(haServiceVoList);
        return resultPage;
    }

    /**
     * 查询授权事项tree
     *
     * @param workUserId    帮代办用户id
     * @param sxServiceList 事项集合
     * @return 授权事项tree
     * @author yupeng
     * @date 2022年08月10 14:36:14
     */
    public HaServiceRegistrarVo queryServiceTree(List<SxService> sxServiceList, String workUserId) {
        //封装下拉tree所有信息
        HaServiceRegistrarVo haServiceRegistrarVo = new HaServiceRegistrarVo();
        //用来封装相同机构的事项
        Map<String, List<SxService>> serviceMap = new HashMap<>();
        //机构相同的事项封装到一起
        sxServiceList.forEach(sxService -> {
            String organOid = sxService.getOrganOid();
            if (StringUtil.isNotEmpty(organOid)) {
                //查询机构名称
                String organName = sysOrganFeginService.queryOrganNameByOid(organOid);
                if (serviceMap.containsKey(organName)) {
                    List<SxService> sxServiceCopyList = serviceMap.get(organName);
                    sxServiceCopyList.add(sxService);
                } else {
                    List<SxService> sxServiceCopyList = new ArrayList<>();
                    sxServiceCopyList.add(sxService);
                    serviceMap.put(organName, sxServiceCopyList);
                }
            } else {
                //没有绑定机构的事项封装到一起
                if (serviceMap.containsKey(null)) {
                    List<SxService> sxServiceCopyList = serviceMap.get(null);
                    sxServiceCopyList.add(sxService);
                } else {
                    List<SxService> sxServiceCopyList = new ArrayList<>();
                    sxServiceCopyList.add(sxService);
                    serviceMap.put(null, sxServiceCopyList);
                }
            }
        });

        //封装tree
        JSONArray treeArray = new JSONArray();
        //封装所有的子节点信息
        JSONArray allChildNodeArray = new JSONArray();
        //封装选中的子节点id
        JSONArray defaultCheckedArray = new JSONArray();
        //封装已选中的子节点所有信息
        JSONArray checkedArray = new JSONArray();

        //封装下拉树
        serviceMap.forEach((organName, sxServiceCopyList) -> {
            //封装机构复选框
            JSONObject organObject = new JSONObject();
            organObject.put("name", organName);
            //封装机构下拉下面事项
            JSONArray serviceArray = new JSONArray();
            sxServiceCopyList.forEach(sxService -> {
                //封装事项复选框
                JSONObject serviceObject = new JSONObject();
                String serviceOid = sxService.getServiceOid();
                serviceObject.put("name", sxService.getServiceName());
                //封装事项下拉下面的服务类型
                JSONArray serviceTypeArray = new JSONArray();

                //材料准备复选框
                JSONObject cLZBObject = new JSONObject();
                cLZBObject.put("id", System.currentTimeMillis());
                cLZBObject.put("name", "材料准备");
                cLZBObject.put("serviceOid", serviceOid);

                //收件复选框
                JSONObject sJObject = new JSONObject();
                //保持id的唯一性，不然前端页面报错
                sJObject.put("id", System.currentTimeMillis() + 1);
                sJObject.put("name", "收件");
                sJObject.put("serviceOid", serviceOid);

                //查询当前事项授权记录
                List<DbHaUserService> dbHaUserServiceList = userServiceMapper.queryUserServiceByServiceIdsAndUserId(Arrays.asList(serviceOid.split(",")), Long.valueOf(workUserId));
                dbHaUserServiceList.forEach(dbHaUserService -> {
                    //服务类型
                    String serviceType = dbHaUserService.getServiceType();
                    //授权状态：1：授权，2：未授权
                    String serviceStatus = dbHaUserService.getServiceStatus();
                    //此处是为了让页面默认选中
                    if (Objects.equals(serviceType, "CLZB")) {
                        if (Objects.equals(serviceStatus, "1")) {
                            cLZBObject.put("id", dbHaUserService.getId());
                            //封装选中的id
                            defaultCheckedArray.add(dbHaUserService.getId());
                            //封装选中的子节点
                            checkedArray.add(cLZBObject);
                        }
                    }
                    if (Objects.equals(serviceType, "SJ")) {
                        if (Objects.equals(serviceStatus, "1")) {
                            sJObject.put("id", dbHaUserService.getId());
                            //封装选中的id
                            defaultCheckedArray.add(dbHaUserService.getId());
                            //封装选中的子节点
                            checkedArray.add(sJObject);
                        }
                    }
                });
                serviceTypeArray.add(cLZBObject);
                serviceTypeArray.add(sJObject);
                serviceObject.put("member", serviceTypeArray);
                serviceArray.add(serviceObject);
                //封装所有的子节点信息
                allChildNodeArray.add(cLZBObject);
                allChildNodeArray.add(sJObject);
            });
            organObject.put("member", serviceArray);
            treeArray.add(organObject);
        });

        haServiceRegistrarVo.setTreeArray(treeArray);
        haServiceRegistrarVo.setAllChildNodeArray(allChildNodeArray);
        haServiceRegistrarVo.setCheckedArray(checkedArray);
        haServiceRegistrarVo.setDefaultCheckedArray(defaultCheckedArray);
        return haServiceRegistrarVo;
    }


    /**
     * 批量保存或修改帮代办授权
     *
     * @param jsonObject 授权信息
     * @return 保存结果
     * @author yupeng
     * @date 2022年08月11 15:07:01
     */
    public Integer bathSaveOrUpdateWorkUserRegistrar(JSONObject jsonObject) {
        //帮代办用户id
        Long workUserId = Long.valueOf(jsonObject.getString("workUserId"));
        //所有的
        JSONArray allNodeArray = jsonObject.getJSONArray("allNodeList");
        //选中的
        JSONArray checkedArray = jsonObject.getJSONArray("checkedNodeList");
        //未选中的
        List<Object> unCheckedList = allNodeArray.stream().filter(node -> !checkedArray.contains(node)).collect(Collectors.toList());
        for (Object object : checkedArray) {
            //object转JSONObject
            JSONObject checkedObject = JSON.parseObject(JSON.toJSONString(object));
            //服务类型
            String serviceType = checkedObject.getString("name");
            //获取事项id
            String serviceOid = checkedObject.getString("serviceOid");
            Integer result = saveOrUpdateUserService(workUserId, serviceOid, serviceType, "1");
            if (result < 0) {
                return result;
            }
        }
        for (Object object : unCheckedList) {
            //object转JSONObject
            JSONObject checkedObject = JSON.parseObject(JSON.toJSONString(object));
            //服务类型
            String serviceType = checkedObject.getString("name");
            //获取事项id
            String serviceOid = checkedObject.getString("serviceOid");
            //获取事项id
            Integer result = saveOrUpdateUserService(workUserId, serviceOid, serviceType, "2");
            if (result < 0) {
                return result;
            }
        }
        return null;
    }

    /**
     * 帮代办授权保存
     *
     * @param workUserId  帮代办用户id
     * @param serviceOid  事项id
     * @param serviceType 服务类型
     * @param status      授权状态，1：授权，2：未授权
     * @return 处理结果
     * @author yupeng
     * @date 2022年08月11 15:58:38
     */
    public Integer saveOrUpdateUserService(Long workUserId, String serviceOid, String serviceType, String status) {
        //获取当前登录用户
        CurrentLoginUser currentLoginUser = CurrentLoginUserHolder.getCurrentLoginUser();

        if (Objects.equals(serviceType, "收件")) {
            serviceType = "SJ";
        }
        if (Objects.equals(serviceType, "材料准备")) {
            serviceType = "CLZB";
        }

        //查询是否有过授权记录
        DbHaUserService dbHaUserService = userServiceMapper.queryUserServiceByServiceIdAndServiceTypeAndWorkUserId(serviceOid, serviceType, workUserId);

        if (dbHaUserService == null) {
            if (Objects.equals(status, "1")) {
                dbHaUserService = new DbHaUserService();
                dbHaUserService.setServiceId(serviceOid);
                dbHaUserService.setServiceType(serviceType);
                dbHaUserService.setWorkUserId(workUserId);
                //服务状态，1：授权，2：未授权
                dbHaUserService.setServiceStatus(status);
                //创建人
                dbHaUserService.setCreateBy(currentLoginUser.getUserName());
                //创建时间
                dbHaUserService.setCreateDate(new Date());
                //保存
                return userServiceMapper.saveUserService(dbHaUserService);
            }
        } else {
            //授权状态改变则进行更新
            if (!Objects.equals(status, dbHaUserService.getServiceStatus())) {
                dbHaUserService.setUpdateBy(currentLoginUser.getUserName());
                dbHaUserService.setUpdateDate(new Date());
                //服务状态，1：授权，2：未授权
                dbHaUserService.setServiceStatus(status);
                //修改
                return userServiceMapper.updateUserService(dbHaUserService);
            }
        }
        return 1;
    }
}
