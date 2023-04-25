package com.zfsoft.ha.managers;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zfsoft.ha.data.HaBanner;
import com.zfsoft.ha.data.HaWorkGroup;
import com.zfsoft.ha.data.vo.HaFileKnowledgeBase;
import com.zfsoft.ha.dbaccess.dao.DbFileKnowledgeBaseMapper;
import com.zfsoft.ha.dbaccess.dao.DbHaBannerMapper;
import com.zfsoft.ha.dbaccess.data.DbFileKnowledgeBase;
import com.zfsoft.ha.dbaccess.data.DbHaWorkGroup;
import com.zfsoft.ha.dbaccess.data.example.DbFileKnowledgeBaseExample;
import com.zfsoft.ha.dbaccess.data.DbHaBanner;
import com.zfsoft.ha.dbaccess.data.example.DbHaBannerExample;
import com.zfsoft.ha.dbaccess.data.vo.DbHaWorkGroupExample;
import com.zfsoft.ha.util.ReadExcel;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.platform.common.security.data.CurrentLoginUser;
import com.zfsoft.platform.common.security.data.CurrentLoginUserHolder;
import com.zfsoft.platform.utils.validate.ParamValid;
import com.zfsoft.service.dbaccess.dao.sxService.DbSxServiceMapper;
import com.zfsoft.service.dbaccess.data.sxService.DbSxService;
import com.zfsoft.service.dbaccess.data.sxService.DbSxServiceExample;
import com.zfsoft.service.sxService.data.SxService;
import com.zfsoft.superwindow.service.easyquickcase.data.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description //Knowledge表实现
 * @Author: Wangyh
 * @Date: 2022/12/2 16:52
 */
@Service
@Slf4j
public class HaKnowledgeServiceManager {

    /**
     * DbFileKnowledgeBaseMapper数据DB层接口
     */
    @Resource
    private DbFileKnowledgeBaseMapper dbFileKnowledgeBaseMapper;

    @Resource
    private DbSxServiceMapper dbSxServiceMapper;
    /**
     * 查询HaFileKnowledgeBase分页信息列表
     * @return PageResult<HaFileKnowledgeBase>  获取查询HaFileKnowledgeBase分页信息列表详细信息
     * @author: wangyh
     * @Date: 2022年7月26日14:04:40
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public List<HaFileKnowledgeBase> queryKnowledgeServiceList() throws ServiceException {
        DbFileKnowledgeBaseExample dbFileKnowledgeBaseExample = new DbFileKnowledgeBaseExample();
        DbFileKnowledgeBaseExample.Criteria criteria = dbFileKnowledgeBaseExample.createCriteria();
        criteria.andIsDeleteEqualTo((short) 0);
        List<DbFileKnowledgeBase> dbFileKnowledgeBases =  dbFileKnowledgeBaseMapper.selectByExample(dbFileKnowledgeBaseExample);
        List<HaFileKnowledgeBase> fileKnowledgeBases = dbFileKnowledgeBases.stream().map(dbzskServic -> {
            HaFileKnowledgeBase haFileKnowledgeBase1 = new HaFileKnowledgeBase();
            BeanUtils.copyProperties(dbzskServic, haFileKnowledgeBase1);
            return haFileKnowledgeBase1;
        }).collect(Collectors.toList());
        return fileKnowledgeBases;
    }
    /**
     * 查询HaFileKnowledgeBase分页信息列表
     *
     * @param haFileKnowledgeBase
     * @param pageNumber
     * @param pageSize
     * @return PageResult<HaFileKnowledgeBase>  获取查询HaFileKnowledgeBase分页信息列表详细信息
     * @author: wangyh
     * @Date: 2022年7月26日14:04:40
     */
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public PageResult<HaFileKnowledgeBase> queryKnowledgeServiceList(HaFileKnowledgeBase haFileKnowledgeBase, Integer pageNumber, Integer pageSize) throws ServiceException {
        //分页参数
        if (null == pageNumber || pageNumber <= 0) {
            pageNumber = 0;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNumber, pageSize);
        DbFileKnowledgeBaseExample dbFileKnowledgeBaseExample = new DbFileKnowledgeBaseExample();
        DbFileKnowledgeBaseExample.Criteria criteria = dbFileKnowledgeBaseExample.createCriteria();
        if (null != haFileKnowledgeBase) {
            if (StrUtil.isNotEmpty(haFileKnowledgeBase.getFileName())) {
                criteria.andFileNameLike(haFileKnowledgeBase.getFileName());
            }
            if (StrUtil.isNotEmpty(haFileKnowledgeBase.getServiceOid())) {
                criteria.andServiceOidEqualTo(haFileKnowledgeBase.getServiceOid());
            }
            criteria.andIsDeleteEqualTo(haFileKnowledgeBase.getIsDelete());
        } else {
            criteria.andIsDeleteNotEqualTo((short) 0);
        }
        dbFileKnowledgeBaseExample.setOrderByClause("UPLOAD_DATE desc");
        Page<DbFileKnowledgeBase> dbFileKnowledgeBases = (Page<DbFileKnowledgeBase>) dbFileKnowledgeBaseMapper.selectByExample(dbFileKnowledgeBaseExample);
        PageResult<HaFileKnowledgeBase> pageResult = new PageResult<>(dbFileKnowledgeBases.getPageNum(), dbFileKnowledgeBases.getPageSize(), dbFileKnowledgeBases.getTotal());
        //循环遍历,将DbthaBanner对象复制到ThaBanner对象
        List<HaFileKnowledgeBase> fileKnowledgeBases = dbFileKnowledgeBases.stream().map(dbzskServic -> {
            HaFileKnowledgeBase haFileKnowledgeBase1 = new HaFileKnowledgeBase();
            BeanUtils.copyProperties(dbzskServic, haFileKnowledgeBase1);
            return haFileKnowledgeBase1;
        }).collect(Collectors.toList());
        pageResult.setData(fileKnowledgeBases);
        return pageResult;
    }

    /**
     * @param haFileKnowledgeBase 参数配置实体类
     * @description: 参数配置的新增或者修改
     * @author: wangyh
     * @Date: 2022年7月26日14:43:41
     **/
    @Transactional
    @ParamValid
    @ShardingTransactionType(TransactionType.LOCAL)
    public Map<String, Object> saveOrUpdateFileKnowledgeBases(HaFileKnowledgeBase haFileKnowledgeBase) throws ServiceException {
        Map<String, Object> map = new HashMap();
        //获取当前用户登录信息
        CurrentLoginUser loginUser = CurrentLoginUserHolder.getCurrentLoginUser();
        Date day = new Date();
        int index = 0;
        //判断是修改还是新增
        if (haFileKnowledgeBase.getId() != null) {
            //修改
            DbFileKnowledgeBase dbFileKnowledgeBase = dbFileKnowledgeBaseMapper.selectByPrimaryKey(haFileKnowledgeBase.getId());
            BeanUtils.copyProperties(haFileKnowledgeBase, dbFileKnowledgeBase);
            dbFileKnowledgeBase.setUploadDate(day); //获取当前时间
            DbFileKnowledgeBaseExample dbFileKnowledgeBaseExample = new DbFileKnowledgeBaseExample();
            DbFileKnowledgeBaseExample.Criteria criteria = dbFileKnowledgeBaseExample.createCriteria();
            criteria.andIdEqualTo(haFileKnowledgeBase.getId());
            int i = dbFileKnowledgeBaseMapper.updateByExampleSelective(dbFileKnowledgeBase, dbFileKnowledgeBaseExample);
            map.put("index", index);
        } else {
            //新增
            DbFileKnowledgeBase dbFileKnowledgeBase = new DbFileKnowledgeBase();
            BeanUtils.copyProperties(haFileKnowledgeBase, dbFileKnowledgeBase);
            dbFileKnowledgeBase.setIsDelete((short) 0);
            dbFileKnowledgeBase.setUploadDate(day); //获取当前时间
            index = dbFileKnowledgeBaseMapper.insert(dbFileKnowledgeBase);
            map.put("index", index);
        }
        return map;
    }

    /**
     * @param id 主键
     * @description: 根据id查询haFileKnowledgeBase表信息信息
     * @author: wangyh
     * @Date: 2022年7月26日14:08:02
     **/
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public HaFileKnowledgeBase selectByid(Long id) throws ServiceException {
        HaFileKnowledgeBase haFileKnowledgeBase = null;
        DbFileKnowledgeBase dbFileKnowledgeBase = dbFileKnowledgeBaseMapper.selectByPrimaryKey(id);
        if (dbFileKnowledgeBase != null) {
            haFileKnowledgeBase = new HaFileKnowledgeBase();
            BeanUtils.copyProperties(dbFileKnowledgeBase, haFileKnowledgeBase);
        }
        return haFileKnowledgeBase;
    }

    /**
     * @param id 主键
     * @description: 删除haFileKnowledgeBase信息
     * @author: wangyh
     * @Date: 2022年7月26日14:08:02
     **/
    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    public Integer deleteZskBaseId(Long id) throws ServiceException {
        DbFileKnowledgeBase dbFileKnowledgeBase = dbFileKnowledgeBaseMapper.selectByPrimaryKey(id);
        dbFileKnowledgeBase.setIsDelete((short) 1);
        dbFileKnowledgeBase.setUploadDate(new Date());
        DbFileKnowledgeBaseExample dbFileKnowledgeBaseExample = new DbFileKnowledgeBaseExample();
        DbFileKnowledgeBaseExample.Criteria criteria = dbFileKnowledgeBaseExample.createCriteria();
        criteria.andIdEqualTo(id);
        int index = dbFileKnowledgeBaseMapper.updateByExampleSelective(dbFileKnowledgeBase, dbFileKnowledgeBaseExample);
        return index;
    }

    /**
     * @description: 查询事项列表
     * @author: wangyh
     * @Date: 2022/8/2 14:32
     **/
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    public List<SxService> queryServiceList() {
        DbSxServiceExample dbSxServiceExample = new DbSxServiceExample();
        DbSxServiceExample.Criteria criteria = dbSxServiceExample.createCriteria();
        criteria.andDelFlagEqualTo((short)0);
        List<DbSxService>  dbSxServiceLists = dbSxServiceMapper.selectByExample(dbSxServiceExample);
        List<SxService> serviceList = dbSxServiceLists.stream().map(service -> {
            SxService sxService = new SxService();
            BeanUtils.copyProperties(service, sxService);
            return sxService;
        }).collect(Collectors.toList());
        return serviceList;
    }

    /**
     * @description: excel导入
     * @author: wangyh
     * @Date: 2022/8/2 14:32
     **/
    @Transactional
    @ShardingTransactionType(TransactionType.LOCAL)
    public void excelLeading(String fileName, MultipartFile files) throws Exception {
//        File file = new File("C:\\Users\\Admin\\Desktop\\jar\\synonym20230315155812.xlsx");
//        InputStream inputStream = new FileInputStream(file);
        List<List<Object>> list = ReadExcel.getListByExcel(files.getInputStream(),fileName);
        for(int i=1;i<list.size();i++){
                List<Object> list1 = list.get(i);
                DbFileKnowledgeBase dbFileKnowledgeBase = new DbFileKnowledgeBase();
                dbFileKnowledgeBase.setKbType("2");//默认值
                dbFileKnowledgeBase.setFileName((String) list1.get(0));
                dbFileKnowledgeBase.setAnswerPureText((String) list1.get(1));
                dbFileKnowledgeBase.setAnsweRrichText((String) list1.get(2));
                dbFileKnowledgeBase.setIsDelete((short) 0);
                dbFileKnowledgeBase.setUploadDate(new Date()); //获取当前时间
                int index = dbFileKnowledgeBaseMapper.insert(dbFileKnowledgeBase);
                String ass = "sa";
        }
    }
}
