package com.zfsoft.superwindow.manager.zsgl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.cases.feign.SysUserFeginService;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.superwindow.data.zsgl.Notepad;
import com.zfsoft.superwindow.dbaccess.dao.DbNotepadMapper;
import com.zfsoft.superwindow.dbaccess.data.DbNotepad;
import com.zfsoft.superwindow.dbaccess.data.DbNotepadExample;
import com.zfsoft.superwindow.util.SysCode;
import com.zfsoft.superwindow.util.UUIDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @ClassName NotepadManager
 * @Description: 记事本接口实现类
 * @Author liangss
 * @Date 2020/10/26
 **/
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NotepadManager {
    @Resource
    private DbNotepadMapper dbNotepadMapper;

    @Resource
    private SysUserFeginService sysUserFeginService;




    public PageResult<Notepad> queryNotepadWithPage
            (Notepad notepad, Integer pageNumber, Integer pageSize) {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber,pageSize);
        DbNotepadExample dbNotepadExample = new DbNotepadExample();
        DbNotepadExample.Criteria criteria=dbNotepadExample.createCriteria();
        if(null!=notepad){
            if(null!=notepad.getZslbDictOid()){
                criteria.andZslbDictOidEqualTo(notepad.getZslbDictOid());
            }
            if(null!=notepad.getKnowledgeTitle()){
                criteria.andKnowledgeTitleLike("%"+notepad.getKnowledgeTitle().trim()+"%");
            }
            if(null!=notepad.getKnowledgeContent()){
                criteria.andKnowledgeContentLike("%"+notepad.getKnowledgeContent().trim()+"%");
            }
            if(null!=notepad.getCreateUserOid()){
                criteria.andCreateUserOidEqualTo(notepad.getCreateUserOid());
            }
            if(null!=notepad.getCreateUserName()){
                criteria.andCreateUserNameLike("%"+notepad.getCreateUserName().trim()+"%");
            }
            if(null!=notepad.getShareFlag()){
                criteria.andShareFlagEqualTo(notepad.getShareFlag());
            }
        }
        criteria.andDelFlagEqualTo(SysCode.DELETE_STATUS.NO);
        dbNotepadExample.setOrderByClause("MODIFY_DATE desc");
        Page<DbNotepad> dbNotepads = (Page<DbNotepad>)dbNotepadMapper.selectByExample(dbNotepadExample);
        PageResult<Notepad> pageResult = new PageResult<>(dbNotepads.getPageNum(),dbNotepads.getPageSize(),dbNotepads.getTotal());
        List<Notepad> notepadList = dbNotepads.stream().map(dbNotepad -> {
            Notepad notepad1 = new Notepad();
            BeanUtils.copyProperties(dbNotepad,notepad1);
            return notepad1;
        }).collect(Collectors.toList());
        pageResult.setData(notepadList);
        return pageResult;
    }


    public void saveOrUpdate(Notepad notepad) {
        if (null!=notepad.getId()) {
            DbNotepad dbNotepad=this.dbNotepadMapper.selectByPrimaryKey(notepad.getId());
            Assert.notNull(notepad, MessageFormat.format("更新对象不存在！对象id为{0}", notepad.getId()));
            BeanUtils.copyProperties(notepad, dbNotepad);
            dbNotepad.setModifyDate(new Date());
            if(notepad.getShareFlag()==1){
                dbNotepad.setShareFlag(SysCode.DELETE_STATUS.YES);
                dbNotepad.setShareDate(new Date());
            }
            log.info("获取登录信息结果为：{}", JSON.toJSONString(CurrentLoginUserHolder.getCurrentLoginUser()));
            if(null!=CurrentLoginUserHolder.getCurrentLoginUser()){
                String userOid=CurrentLoginUserHolder.getCurrentLoginUser().getUserOid().toString();
                String userName=CurrentLoginUserHolder.getCurrentLoginUser().getUserName().toString();
                ApiResultSet<SysUser>  resultSet=sysUserFeginService.getSysUserByUserOid(userOid);
                if(null!=resultSet){
                    SysUser sysUser=resultSet.getData();
                    userName=sysUser.getName();
                }
                dbNotepad.setCreateUserOid(userOid);
                dbNotepad.setCreateUserName(userName);
            }

            this.dbNotepadMapper.updateByPrimaryKeySelective(dbNotepad);
        } else {
            DbNotepad dbNotepad= new DbNotepad();
            BeanUtils.copyProperties(notepad, dbNotepad);
            dbNotepad.setModifyDate(new Date());
            dbNotepad.setCreateDate(new Date());
            dbNotepad.setDelFlag(SysCode.DELETE_STATUS.NO);
          /*  if(null!=CurrentLoginUserHolder.getCurrentLoginUser()) {
                dbNotepad.setCreateUserOid(CurrentLoginUserHolder.getCurrentLoginUser().getUserOid().toString());
                dbNotepad.setCreateUserName(CurrentLoginUserHolder.getCurrentLoginUser().getUserName());
            }*/
            if(null!=CurrentLoginUserHolder.getCurrentLoginUser()){
                String userOid=CurrentLoginUserHolder.getCurrentLoginUser().getUserOid().toString();
                String userName=CurrentLoginUserHolder.getCurrentLoginUser().getUserName().toString();
                ApiResultSet<SysUser>  resultSet=sysUserFeginService.getSysUserByUserOid(userOid);
                if(null!=resultSet){
                    SysUser sysUser=resultSet.getData();
                    userName=sysUser.getName();
                }
                dbNotepad.setCreateUserOid(userOid);
                dbNotepad.setCreateUserName(userName);
            }
            if(null!=notepad.getShareFlag()&&notepad.getShareFlag()==1){
                dbNotepad.setShareFlag(SysCode.DELETE_STATUS.YES);
                dbNotepad.setShareDate(new Date());
            }else{
                dbNotepad.setShareFlag(SysCode.DELETE_STATUS.NO);
                dbNotepad.setShareFlag(0);
            }
            dbNotepad.setNotepadOid(UUIDUtil.randomUUID());
            this.dbNotepadMapper.insert(dbNotepad);
        }

    }

    public Notepad getOne(Long id) {
        Assert.hasLength(String.valueOf(id), "主键不能为空！");
        DbNotepad dbNotepad=this.dbNotepadMapper.selectByPrimaryKey(id);
        Notepad notepad = new Notepad();
        BeanUtils.copyProperties(dbNotepad,notepad);
        return notepad;
    }

    public void delNotepad(Long id) {
        Assert.hasLength(String.valueOf(id),  "主键不能为空！");
        DbNotepad dbNotepad=this.dbNotepadMapper.selectByPrimaryKey(id);
        Assert.notNull(dbNotepad, MessageFormat.format("操作对象不存在！对象id为{0}", id));
        dbNotepad.setDelFlag(dbNotepad.getDelFlag().equals(SysCode.DELETE_STATUS.YES) ? SysCode.DELETE_STATUS.NO : SysCode.DELETE_STATUS.YES);
        this.dbNotepadMapper.updateByPrimaryKeySelective(dbNotepad);
    }

    public void shareFlagNotepad(Long id) {
        Assert.hasLength(String.valueOf(id),  "主键不能为空！");
        DbNotepad dbNotepad=this.dbNotepadMapper.selectByPrimaryKey(id);
        Assert.notNull(dbNotepad, MessageFormat.format("操作对象不存在！对象id为{0}", id));
        dbNotepad.setShareFlag(dbNotepad.getShareFlag().equals(SysCode.DELETE_STATUS.YES) ? SysCode.DELETE_STATUS.NO : SysCode.DELETE_STATUS.YES);
        dbNotepad.setShareDate(new Date());
        if(dbNotepad.getShareFlag().equals(SysCode.DELETE_STATUS.YES)){
            if(null!=CurrentLoginUserHolder.getCurrentLoginUser()){
                String userOid=CurrentLoginUserHolder.getCurrentLoginUser().getUserOid().toString();
                String userName=CurrentLoginUserHolder.getCurrentLoginUser().getUserName().toString();
                ApiResultSet<SysUser>  resultSet=sysUserFeginService.getSysUserByUserOid(userOid);
                if(null!=resultSet){
                    SysUser sysUser=resultSet.getData();
                    userName=sysUser.getName();
                }
                dbNotepad.setCreateUserOid(userOid);
                dbNotepad.setCreateUserName(userName);
            }
        }

        this.dbNotepadMapper.updateByPrimaryKeySelective(dbNotepad);
    }

    public JSONArray getOneMouthNotepad(String time) {
        List<DbNotepad> notepads = new ArrayList<>();
        List<String> timeList = new ArrayList<>();
        if (null != CurrentLoginUserHolder.getCurrentLoginUser()) {
            String userOid = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid().toString();
            String userName = CurrentLoginUserHolder.getCurrentLoginUser().getUserName().toString();
            ApiResultSet<SysUser> resultSet = sysUserFeginService.getSysUserByUserOid(userOid);
            if (null != resultSet) {
                SysUser sysUser = resultSet.getData();
                userName = sysUser.getName();
            }
            if (!time.isEmpty() && !userOid.isEmpty()) {
                notepads = this.dbNotepadMapper.selectOneMouthNotepad(time, userOid);
                timeList = this.dbNotepadMapper.countOneMouthNotepad(time, userOid);
            }
        }
        JSONArray jsonArray = new JSONArray();
        if (notepads.size() > 0 && timeList.size() > 0) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            for (String str : timeList) {

                JSONObject jsonObject = new JSONObject();
                List<DbNotepad> list = new ArrayList<>();
                for (DbNotepad notepad : notepads) {
                    if (formatter.format(notepad.getCreateDate()).equals(str)) {
                        list.add(notepad);
                    }
                }
                jsonObject.put("date", str);
                jsonObject.put("list", list);
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }

    public void dateTosaveNotepad(Notepad notepad) {
        if (null!=notepad.getId()) {
            DbNotepad dbNotepad=this.dbNotepadMapper.selectByPrimaryKey(notepad.getId());
            Assert.notNull(notepad, MessageFormat.format("更新对象不存在！对象id为{0}", notepad.getId()));
            BeanUtils.copyProperties(notepad, dbNotepad);
            dbNotepad.setModifyDate(new Date());
            if(notepad.getShareFlag() != null && notepad.getShareFlag()==1){
                dbNotepad.setShareFlag(SysCode.DELETE_STATUS.YES);
                dbNotepad.setShareDate(new Date());
            }
            log.info("获取登录信息结果为：{}", JSON.toJSONString(CurrentLoginUserHolder.getCurrentLoginUser()));
            if(null!=CurrentLoginUserHolder.getCurrentLoginUser()){
                String userOid=CurrentLoginUserHolder.getCurrentLoginUser().getUserOid().toString();
                String userName=CurrentLoginUserHolder.getCurrentLoginUser().getUserName().toString();
                ApiResultSet<SysUser>  resultSet=sysUserFeginService.getSysUserByUserOid(userOid);
                if(null!=resultSet){
                    SysUser sysUser=resultSet.getData();
                    userName=sysUser.getName();
                }
                dbNotepad.setCreateUserOid(userOid);
                dbNotepad.setCreateUserName(userName);
            }
            this.dbNotepadMapper.updateByPrimaryKeySelective(dbNotepad);
        } else {
            DbNotepad dbNotepad= new DbNotepad();
            BeanUtils.copyProperties(notepad, dbNotepad);
            dbNotepad.setModifyDate(new Date());
            dbNotepad.setCreateDate(new Date());
            dbNotepad.setDelFlag(SysCode.DELETE_STATUS.NO);
            if(null!=CurrentLoginUserHolder.getCurrentLoginUser()){
                String userOid=CurrentLoginUserHolder.getCurrentLoginUser().getUserOid().toString();
                String userName=CurrentLoginUserHolder.getCurrentLoginUser().getUserName().toString();
                ApiResultSet<SysUser>  resultSet=sysUserFeginService.getSysUserByUserOid(userOid);
                if(null!=resultSet){
                    SysUser sysUser=resultSet.getData();
                    userName=sysUser.getName();
                }
                dbNotepad.setCreateUserOid(userOid);
                dbNotepad.setCreateUserName(userName);
            }
            if(null!=notepad.getShareFlag()&&notepad.getShareFlag()==1){
                dbNotepad.setShareFlag(SysCode.DELETE_STATUS.YES);
                dbNotepad.setShareDate(new Date());
            }else{
                dbNotepad.setShareFlag(SysCode.DELETE_STATUS.NO);
                dbNotepad.setShareFlag(0);
            }
            dbNotepad.setNotepadOid(UUIDUtil.randomUUID());
            this.dbNotepadMapper.insert(dbNotepad);
        }

    }


}
