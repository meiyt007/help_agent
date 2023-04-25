package com.zfsoft.superwindow.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.zfsoft.cases.feign.SysDictFeignService;
import com.zfsoft.cases.feign.SysDistrictFeignService;
import com.zfsoft.microservice.platform.data.sys.SysDistrict;
import com.zfsoft.microservice.settings.data.SysDict;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.superwindow.controller.clzs.ser.GenDataMaterialCatalogTreeUtil;
import com.zfsoft.superwindow.controller.clzs.ser.MaterialCatalogTreeSelect;
import com.zfsoft.superwindow.data.clzs.MaterialCatalog;
import com.zfsoft.superwindow.manager.clzs.MaterialCatalogManager;
import com.zfsoft.superwindow.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

/**
 * @author: kkfan
 * @create: 2020-10-26 21:32:41
 * @description: 通用
 */
@Slf4j
@RestController
@RequestMapping(value = "/common")
public class CommonController {

    @Resource
    private SysDictFeignService dictFeignService;

    @Resource
    private SysDistrictFeignService sysDistrictFeginService;

    //材料目录
    @Resource
    private MaterialCatalogManager materialCatalogManager;


 /*   @Autowired
    private final ComboThemeFeginService comboThemeFeginService;*/

    @PostMapping(value = "/queryDictListByParentCode")
    public ApiResultSet queryDictListByParentCode(String parentCode) {
        Assert.hasLength(parentCode, "父级编码不能为空！");
        ApiResultSet<SysDict> sysDictByCode = this.dictFeignService.getSysDictByCode(parentCode);
        log.info("字典详情获取成功：{}", JSON.toJSONString(sysDictByCode));
        return sysDictByCode;
    }

    /*
     * @Description: 将指定路径的文件上传至办件服务
     * @Author: liyanqing
     * @Date: 2020-11-26 11:58
     * @param paths:
     * @return: com.zfsoft.platform.common.data.ApiResultSet
     **/
    @PostMapping(value = "/uploadFileByPaths")
    public String uploadFileByPaths(String paths) {
        String data = "";
        try {
            if (StrUtil.isNotEmpty(paths)){
                File file = new File(paths);
                if (file.exists()) {
                    FileInputStream inputFile = new FileInputStream(file);
                    byte[] buffer = new byte[(int)file.length()];
                    inputFile.read(buffer);
                    inputFile.close();
                    String encode = new BASE64Encoder().encode(buffer);
                    data += encode;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("文件上传失败");
        }
        return data;
    }

    /*
     * @Description: 获取父级区划或统计区划主键
     * @Author: liyanqing
     * @Date: 2021-01-04 13:48
     * @param districtOid: 区划主键
     * @param chooseType: 0-直属上级区划主键 、1-获取统计区划主键串
     * @return: java.lang.String
     **/
    @GetMapping(value = "/getParentOidByDistrictOid")
    public String getParentOidByDistrictOid(String districtOid, String chooseType){
        //父级主键
        String parentOid = null;
        //根据主键获取区划
        ApiResultSet<SysDistrict> district = sysDistrictFeginService.getSysDistrictByDistrictOid(districtOid);
        if (district.getData() != null && StringUtils.isNotEmpty(district.getData().getParentOid())) {
            parentOid = district.getData().getParentOid();
        }
        if (StringUtils.isNotEmpty(parentOid)) {
            //0-直属上级区划主键
            if ("0".equals(chooseType)) {
                return parentOid;
            } else {
                //返回的字符串
                String result = "";
                //1-获取统计区划主键串
                ApiResultSet<List<SysDistrict>> listByParentOid = sysDistrictFeginService.querySysDistrictListByParentOid(parentOid);
                if (listByParentOid.getData() != null && listByParentOid.getData().size() > 0) {
                    for (SysDistrict dis : listByParentOid.getData()) {
                        result += dis.getDistrictOid() + ",";
                    }
                }
                return result;
            }
        } else {
            //若是省级本级
            //1-直属上级下级区划主键
            if ("1".equals(chooseType)) {
                return districtOid;
            }
            return parentOid;
        }
    }


    @GetMapping(value = "/queryCatalogTree")
    public ApiResultSet<List<MaterialCatalogTreeSelect>> queryCatalogTree() {
        List<MaterialCatalog> materialCatalogList=materialCatalogManager.queryAllCatalogList();
        List<MaterialCatalogTreeSelect> treeSelects = GenDataMaterialCatalogTreeUtil.buildMaterialCatalogTreeSelect(materialCatalogList);
        return new ApiResultSet(treeSelects);

    }

    //判断系统
    @GetMapping(value = "/isOSWin")
    public ApiResultSet<Boolean> isOSWin() {
        Properties prop = System.getProperties();
        String os = prop.getProperty("os.name");
        if (os != null && os.toLowerCase().startsWith("win")) {
           return new ApiResultSet<>(true);
        } else {
            return new ApiResultSet<>(false);
        }
    }

}




















