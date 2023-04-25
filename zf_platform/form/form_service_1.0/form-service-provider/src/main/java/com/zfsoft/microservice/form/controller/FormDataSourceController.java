package com.zfsoft.microservice.form.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.zfsoft.microservice.form.data.FormDataSource;
import com.zfsoft.microservice.form.feign.SysDictFeignService;
import com.zfsoft.microservice.form.manager.FormDataSourceManager;
import com.zfsoft.microservice.form.service.FormDataSourceService;
import com.zfsoft.microservice.settings.data.SysDict;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.common.data.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName FormDataSourceController1
 * @Description 数据源配置的实现类
 * @Author wuxx
 * @Date 2021-03-11 10:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class FormDataSourceController implements FormDataSourceService {

    @Resource
    private FormDataSourceManager formDataSourceManager;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SysDictFeignService sysDictFeignService;

    //加密的key
    private final String encrypt_key = "form:dataSource:encrypt:";

    /**
     * @description:  查询数据源配置的信息列表
     * @param authorizeKeyApi 授权key
     * @param connectionName 数据源连接名称
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    public ApiResultSet<PageResult<FormDataSource>> queryFormDataSourceWithPage(String authorizeKeyApi, String connectionName,String moduleOid, Integer pageNum,
                                                                                      Integer pageSize){
        //String authorizeKey = AuthorizeKeyUtil.checkAuthorizeKeyApi(authorizeKeyApi);
        FormDataSource formDataSource = new FormDataSource();
        formDataSource.setConnectionName(connectionName);
        formDataSource.setAuthorizeKey(authorizeKeyApi);
        formDataSource.setModuleOid(moduleOid);
        PageResult<FormDataSource> pageResult = formDataSourceManager.queryFormDataSourceWithPage(formDataSource,pageNum,pageSize);
        return new ApiResultSet<>(pageResult);
    }

    /**
     * @description: 初始化数据源配置的信息
     * @param id 数据源配置实体类主键
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    @Override
    public ApiResultSet initFormDataSource(Long id){
        //设置加密的内容
        Map<String, Object> dataSourceMap = this.getDataSourcePublicKey();
        if(null!=id){
            FormDataSource formDataSource = formDataSourceManager.getFormDataSourceById(id);
            if(StrUtil.isNotBlank(formDataSource.getPassword())){
                byte[] bytes = formDataSource.getPassword().getBytes();
                //Base64 加密
                //String encodedPassword = Base64.getEncoder().encodeToString(bytes);
                //encodedPassword = Base64.getEncoder().encodeToString(encodedPassword.getBytes());
                String encodedPassword = this.byteToHex(bytes);
                formDataSource.setPassword(encodedPassword);
            }
            dataSourceMap.put("formDataSource",formDataSource);
        }
        return new ApiResultSet<>(dataSourceMap);
    }

    /**
     * hex转byte数组
     * @param hex
     * @return
     */
    public static byte[] hexToByte(String hex){
        int m = 0, n = 0;
        int byteLen = hex.length() / 2; // 每两个字符描述一个字节
        byte[] ret = new byte[byteLen];
        for (int i = 0; i < byteLen; i++) {
            m = i * 2 + 1;
            n = m + 1;
            int intVal = Integer.decode("0x" + hex.substring(i * 2, m) + hex.substring(m, n));
            ret[i] = Byte.valueOf((byte)intVal);
        }
        return ret;
    }

    /**
     * byte数组转hex
     * @param bytes
     * @return
     */
    public static String byteToHex(byte[] bytes){
        String strHex = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < bytes.length; n++) {
            strHex = Integer.toHexString(bytes[n] & 0xFF);
            sb.append((strHex.length() == 1) ? "0" + strHex : strHex); // 每个字节由两个字符表示，位数不够，高位补0
        }
        return sb.toString().trim();
    }

    /**
     * @description:  数据源配置的新增或者修改
     * @param formDataSource 数据源配置实体类
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    public ApiResultSet<FormDataSource> saveFormDataSource(@RequestBody FormDataSource formDataSource){
        formDataSource = this.getDataSourcePublicKey(formDataSource);
        formDataSourceManager.saveFormDataSource(formDataSource);
        return  new ApiResultSet<>(formDataSource);
    }

    /**
     * @description:  数据源配置的信息的删除
     * @param id 数据源配置实体类主键
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    public ApiResultSet<Integer>  deleteFormDataSourceById(Long id){
        formDataSourceManager.deleteFormDataSourceById(id);
        return new ApiResultSet<>();
    }

    /**
     * @description:  获取数据源配置的信息
     * @param id 数据源配置实体类主键
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    public ApiResultSet<FormDataSource> getFormDataSourceById(Long id){
        FormDataSource formDataSource = formDataSourceManager.getFormDataSourceById(id);
        formDataSource.setPassword(null);
        return new ApiResultSet<>(formDataSource);
    }

    /**
     * @description:  获取数据源配置的信息
     * @param dataSourceOid 授权key
     * @author: wuxx
     * @Date: 2020/10/29 11:40
     **/
    public ApiResultSet<FormDataSource>  getFormDataSourceByDataSourceOid(@PathVariable("dataSourceOid")String dataSourceOid){
        FormDataSource formDataSource = formDataSourceManager.getFormDataSourceByDataSourceOid(dataSourceOid);
        formDataSource.setPassword(null);
        return new ApiResultSet<>(formDataSource);
    }

    /**
     * @description:  数据源配置的信息的启禁用
     * @param id 数据源配置实体类主键
     * @author: wuxx
     * @Date: 2020/9/12 10:14
     **/
    public ApiResultSet<Integer> ableFormDataSourceById(@PathVariable("id")Long id){
        int rows = formDataSourceManager.ableFormDataSourceById(id);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(rows);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<List<FormDataSource>> queryFormDataSourceList(String authorizeKey,String moduleOid) {
        FormDataSource formDataSource = new FormDataSource();
        formDataSource.setAuthorizeKey(authorizeKey);
        formDataSource.setModuleOid(moduleOid);
        formDataSource.setIsAble(BaseStaticParameter.Y);
        formDataSource.setIsDelete(BaseStaticParameter.N);
        List<FormDataSource> formDataSourceList = formDataSourceManager.queryFormDataSourceList(formDataSource);
        return new ApiResultSet<>(formDataSourceList);
    }

    @Override
    public ApiResultSet getDataSourceDictList(String code) {
        List<SysDict> sysDictList = sysDictFeignService.querySysDictListByParentCode(code).getData();
        return new ApiResultSet<>(sysDictList);
    }

    /**
     * @description:  检查数据源配置是否正常连接
     * @param formDataSource 数据源配置实体类
     * @author: wuxx
     * @Date: 2021/4/1 16:40
     **/
    @RequestMapping( value = "/checkConnection",method = {RequestMethod.POST})
    public ApiResultSet checkFormDataSourceConnection(@RequestBody FormDataSource formDataSource){
        formDataSource = this.getDataSourcePublicKey(formDataSource);
        boolean connection = formDataSourceManager.checkFormDataSourceConnection(formDataSource);
        return new ApiResultSet<>(connection);
    }

    /**
     * @description: 获取数据库公钥
     * @author: wuxx
     * @Date: 2021/4/13 16:09
     **/
    public Map<String,Object> getDataSourcePublicKey(){
        Map<String,Object> map=new HashMap<>();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        RSA rsa = SecureUtil.rsa();
        String privateKeyBase64 = rsa.getPrivateKeyBase64();
        String publicKeyBase64 = rsa.getPublicKeyBase64();
        redisTemplate.opsForValue().set(encrypt_key + request.getSession().getId(),privateKeyBase64);
        redisTemplate.expire(encrypt_key + request.getSession().getId(),10, TimeUnit.MINUTES);
        map.put(BaseStaticParameter.PUBLIC_KEY, publicKeyBase64);
        return map;
    }

    /**
     * @description: 获取数据对象的解密
     * @author: wuxx
     * @Date: 2021/4/13 16:09
     **/
    public FormDataSource getDataSourcePublicKey(FormDataSource formDataSource){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String privateKeyBase64 = (String)redisTemplate.opsForValue().get(encrypt_key + request.getSession().getId());
        RSA rsa = SecureUtil.rsa(privateKeyBase64, null);
        if(null!=formDataSource){
            if(StrUtil.isNotBlank(formDataSource.getEncryptPassword())){
                formDataSource.setPassword(rsa.decryptStr(formDataSource.getEncryptPassword(), KeyType.PrivateKey));
            }
        }
        return formDataSource;
    }
}
