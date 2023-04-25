package com.zfsoft.single.controller.yxpz;

import com.zfsoft.microservice.platform.data.sys.SysAtta;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.single.data.yxpz.SxSerForm;
import com.zfsoft.single.manager.yxpz.FormConfigurationManager;
import com.zfsoft.single.service.yxpz.FormConfigurationService;
import com.zfsoft.single.util.StringUtils;
import com.zfsoft.superwindow.feign.settings.SysAttaFeginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangns
 * @description 表单配置管理
 * @date 2020/11/5 11:16
 * @copyright 上海卓繁信息技术股份有限公司版权所有
 */
@Slf4j
@RestController
public class FormConfigurationController implements FormConfigurationService {

    @Resource
    private FormConfigurationManager formConfigurationManager;

    @Resource
    private SysAttaFeginService sysAttaFeginService;

    @Value("${zfsoft.electronicForm.url}")
    private String requestUrl;

    @Value("${zfsoft.electronicForm.formAllUrl}")
    private String formUrl;

    @Value("${zfsoft.electronicForm.formPageUrl}")
    private String formPageUrl;

    @Value("${zfsoft.electronicForm.authorizeKey}")
    private String authorizeKey;


    /**
     * 保存/更新
     *
     * @param serForm
     * @return
     */
    @Override
    public ApiResultSet save(SxSerForm serForm) {
        formConfigurationManager.save(serForm);
        return  new ApiResultSet<SxSerForm>(serForm);
    }

    /**
     * 获取详情
     *
     * @param serviceOid
     * @return
     */
    @Override
    public ApiResultSet getSerFormsByServiceOid(String serviceOid, Integer pageNum, Integer pageSize) {
        SxSerForm sxSerForm = new SxSerForm();
        sxSerForm.setServiceOid(serviceOid);
        PageResult<SxSerForm> sxSerForms = formConfigurationManager.getSerFormsByServiceOid(sxSerForm, pageNum, pageSize);
        return new ApiResultSet<PageResult<SxSerForm>>(sxSerForms);
    }

    /**
     * 根据sxSerFormOid查询sxSerForm
     *
     * @param sxSerFormOid
     * @return
     */
    @Override
    public ApiResultSet getSxSerFormByOid( String sxSerFormOid) {
        //根据sxSerFormOid 查询是单条结果
        SxSerForm sxSerForm = formConfigurationManager.getSxSerFormByOid(sxSerFormOid);
        return new ApiResultSet(sxSerForm);
    }

    /**
     * 启禁用
     *
     * @param oid
     * @return
     */
    @Override
    public ApiResultSet sxSerFormAble(String oid) {
        this.formConfigurationManager.ableSxSerFormById(oid);
        return  new ApiResultSet<>();
    }


    /**
     * 单条更新
     *
     * @param oid@return
     */
    @Override
    public ApiResultSet updateSxSerFormByList(String oid) {
        SxSerForm sxSerForm = new SxSerForm();
        sxSerForm.setOid(oid);
        this.formConfigurationManager.updateSxSerForm(sxSerForm);
        return new ApiResultSet<>();
    }

    @Override
    public ApiResultSet selectBySxSerFormByServiceOid(String serviceOid) {
       List<SxSerForm> serForms= this.formConfigurationManager.selectBySxSerFormByServiceOid(serviceOid);
        return new ApiResultSet(serForms);
    }

    @Override
    public ApiResultSet selectBySxSerForm(SxSerForm sxSerForm) {
       List<SxSerForm> serformList= this.formConfigurationManager.selectBySxSerForm(sxSerForm);
       if(serformList !=null){
           for(SxSerForm form:serformList){
               if(StringUtils.isNotEmpty(form.getSimpleAtta())){
                   ApiResultSet<SysAtta> sysAtta= sysAttaFeginService.getSysAttaByAttaOid(form.getSimpleAtta());
                   if(sysAtta!=null && sysAtta.getData()!=null){
                       form.setAttaName(sysAtta.getData().getName());
                   }
               }
           }
       }
        return new ApiResultSet(serformList);
    }

    /**
     * 更新
     *
     * @param sxSerForms
     * @return
     */
    @Override
    public ApiResultSet saveSxSerFormByList(List<SxSerForm> sxSerForms) {
        this.formConfigurationManager.saveSxSerFormByList(sxSerForms);
        return new ApiResultSet<>();

    }

    @Override
    public ApiResultSet getSerFormsByServiceOid(SxSerForm sxSerForm, Integer pageNumber, Integer pageSize) {
        PageResult<SxSerForm> pageResult = formConfigurationManager.getSerFormsByServiceOid(sxSerForm, pageNumber, pageSize);
        return new ApiResultSet(pageResult);
    }

    @Override
    public ApiResultSet getSerFormsByServiceOidStrs(String oidStrs,String comboDirectoryOid, Integer pageNum, Integer pageSize) {
        //获取字符串分割成数组
        List<String> str= Arrays.asList(oidStrs.split(","));
        PageResult<SxSerForm> pageResult = formConfigurationManager.getSerFormsByServiceOidStrs(str,comboDirectoryOid, pageNum, pageSize);
        return new ApiResultSet(pageResult);
    }

    @Override
    public ApiResultSet sxSerFormDel(String oid) {
        formConfigurationManager.delSxSerFormById(oid);
        return  new ApiResultSet<>();
    }

    @Override
    public ApiResultSet queryPublishFormList() {
        //解析办件信息
        String uriString = requestUrl + formUrl + "?authorizeKey=" + authorizeKey;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> resEntity = restTemplate.exchange(uriString.toString(), HttpMethod.GET, requestEntity, String.class);
        String body = resEntity.getBody();
        ApiResultSet<String> apiResultSet = new ApiResultSet<String>();
        apiResultSet.setData(body);
        return apiResultSet;
    }

    @Override
    public ApiResultSet queryElectronicsFormPageList(String queryName, Integer pageNumber, Integer pageSize) {
        //解析办件信息
        if(queryName ==null){
            queryName ="";
        }
         String uriString = requestUrl + formPageUrl + "?authorizeKey=" + authorizeKey +"&formName=" + queryName  +"&formStatus=2&isAble=1"
                +"&pageNum=" + pageNumber +"&pageSize=" + pageSize;
         RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> resEntity = restTemplate.exchange(uriString.toString(), HttpMethod.GET, requestEntity, String.class);
        String body = resEntity.getBody();
        ApiResultSet<String> apiResultSet = new ApiResultSet<String>();
        apiResultSet.setData(body);
        return apiResultSet;
    }


}
