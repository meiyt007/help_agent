package com.zfsoft.superwindow.manager.front;

import com.zfsoft.cases.feign.SysUserFeginService;
import com.zfsoft.microservice.platform.data.sys.SysUser;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.superwindow.data.front.WorkerHonor;
import com.zfsoft.superwindow.dbaccess.dao.DbWorkerHonorMapper;
import com.zfsoft.superwindow.dbaccess.data.DbWorkerHonor;
import com.zfsoft.superwindow.util.StringUtils;
import com.zfsoft.superwindow.util.SysCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class WorkerHonorManager {

    @Resource
    private DbWorkerHonorMapper workerHonorMapper;

    @Resource
    private SysUserFeginService sysUserFeginService;

    public DbWorkerHonor selectByUserOid() {
        DbWorkerHonor workerHonor = null;
        if (null != CurrentLoginUserHolder.getCurrentLoginUser()) {
            String userOid = CurrentLoginUserHolder.getCurrentLoginUser().getUserOid().toString();
            workerHonor = this.workerHonorMapper.selectByUserOid(userOid);
        }
        return workerHonor;
    }

    public DbWorkerHonor queryByUserOid(String userOid) {
        DbWorkerHonor workerHonor = null;
        workerHonor = this.workerHonorMapper.selectByUserOid(userOid);
        return workerHonor;
    }

    public List<DbWorkerHonor> queryAll() {
        List<DbWorkerHonor> workerHonorList = this.workerHonorMapper.selectALLHonors();
        return workerHonorList;
    }

    public void saveWorkerHonor(WorkerHonor workerHonor) {
        int count =0;
        if (null != workerHonor.getUserOid()) {
            DbWorkerHonor dbWorkerHonor = this.workerHonorMapper.selectByUserOid(workerHonor.getUserOid());
            if (dbWorkerHonor != null) {
                Assert.notNull(workerHonor, MessageFormat.format("更新对象不存在！对象id为{0}", workerHonor.getId()));
                dbWorkerHonor.setModifyDate(new Date());
                if(StringUtils.isEmpty(workerHonor.getHonorOid())){
                    dbWorkerHonor.setHonorOid(null);
                }else{
                    dbWorkerHonor.setHonorOid(workerHonor.getHonorOid());
                }
                 count = this.workerHonorMapper.updateByPrimaryKeySelective(dbWorkerHonor);
            } else {
                DbWorkerHonor dbHonor = new DbWorkerHonor();
                ApiResultSet<SysUser> resultSet = sysUserFeginService.getSysUserByUserOid(workerHonor.getUserOid());
                if (null != resultSet) {
                    BeanUtils.copyProperties(workerHonor, dbHonor);
                    dbHonor.setModifyDate(new Date());
                    dbHonor.setCreateDate(new Date());
                    dbHonor.setDelFlag(SysCode.DELETE_STATUS.NO);
                }
                 count = this.workerHonorMapper.insert(dbHonor);
            }
            if (count == 0) {
                Assert.hasLength(String.valueOf(count),  "保存失败！");
            }
        }
    }

    public void deleteWorkerHonor(Long id) {
        Assert.hasLength(String.valueOf(id),  "主键不能为空！");
        DbWorkerHonor dbWorkerHonor=this.workerHonorMapper.selectByPrimaryKey(id);
        Assert.notNull(dbWorkerHonor, MessageFormat.format("操作对象不存在！对象id为{0}", id));
        dbWorkerHonor.setDelFlag(dbWorkerHonor.getDelFlag().equals(SysCode.DELETE_STATUS.YES) ? SysCode.DELETE_STATUS.NO : SysCode.DELETE_STATUS.YES);
        this.workerHonorMapper.updateByPrimaryKeySelective(dbWorkerHonor);
    }
}
