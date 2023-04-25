package com.zfsoft.single.manager.clzs;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.rest.annotation.CataWordBind;
import com.zfsoft.single.data.clzs.dto.HouseholdRegisterInfo;
import com.zfsoft.single.util.FaStaticParam;
import com.zfsoft.superwindow.data.clzs.MaterialCatalog;
import com.zfsoft.superwindow.data.clzs.MaterialCatalogElement;
import com.zfsoft.superwindow.service.clzs.MaterialCatalogElementService;
import com.zfsoft.superwindow.util.fa.AiTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @ClassName HouseholdRegisterInfoManager
 * @Description: 户口本识别
 * @Author liangss
 * @Date 2020-12-14 15:46:29
 **/
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HouseholdRegisterInfoManager {
    @Resource
    private AiTokenUtil aiTokenUtil;
//    @Resource
//    private IOcrCertificateRestService ocrCertificateRestService;
    @Resource
    private MaterialCatalogElementService materialCatalogElementFeginService;


    public HouseholdRegisterInfo getHouseholdRegister(String picBase64)  {
        HouseholdRegisterInfo householdRegisterInfo = null;
        picBase64 = picBase64.replace(" ", "+");

//        HouseholdRegisterRequest householdRegisterRequest = aiTokenUtil.getTokenRequest(HouseholdRegisterRequest.class);
//        householdRegisterRequest.setImgBase64(picBase64);
//        HouseholdRegisterResponse householdRegisterResponse = ocrCertificateRestService.householdRegister(householdRegisterRequest);
//
//        // 调用成功
//        String code = String.valueOf(householdRegisterResponse.getCode());
//        if (FaStaticParam.HTTP_REQUEST_CODE_SUCCESS.equals(code)) {
//            householdRegisterInfo = new HouseholdRegisterInfo();
//            householdRegisterInfo.setSex(householdRegisterResponse.getSex());
//            householdRegisterInfo.setName(householdRegisterResponse.getName());
//            householdRegisterInfo.setBirthaddress(householdRegisterResponse.getBirthaddress());
//            householdRegisterInfo.setCardno(householdRegisterResponse.getCardno());
//            householdRegisterInfo.setNation(householdRegisterResponse.getNation());
//            householdRegisterInfo.setRelationship(householdRegisterResponse.getRelationship());
//            householdRegisterInfo.setBirthday(householdRegisterResponse.getBirthday());
//        }
        return householdRegisterInfo;
    }

    public Map<String, Object> discernHouseholdRegister(HouseholdRegisterInfo householdRegisterInfo,
                                                        MaterialCatalog cata) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", BaseStaticParameter.N);
        // 获取到目录元素信息
        MaterialCatalogElement materialCatalogElement=new MaterialCatalogElement();
        materialCatalogElement.setMaterialCatalogOid(cata.getMaterialCatalogOid());
        ApiResultSet<List<MaterialCatalogElement>> list=materialCatalogElementFeginService.queryList(materialCatalogElement);
        List<MaterialCatalogElement> faMaterialCatalogMetadataList=null;
        if(list!=null&&list.getData()!=null){
            faMaterialCatalogMetadataList=list.getData();
        }
        resultMap.put("code", FaStaticParam.HTTP_REQUEST_CODE_SUCCESS);
        List<Map<String, String>> resultList = handHouseholdRegisterInfo(householdRegisterInfo, faMaterialCatalogMetadataList);
        resultMap.put("result", resultList);
        return resultMap;
    }

    /**
     * 处理户口本识别结果
     *
     * @author chenbw
     * @date 2019年6月17日
     * @param householdRegisterInfo
     * @param faMaterialCatalogMetadataList
     *            区块列表
     * @return
     * @throws Exception
     */
    private List<Map<String, String>> handHouseholdRegisterInfo(HouseholdRegisterInfo householdRegisterInfo,
                                                                List<MaterialCatalogElement> faMaterialCatalogMetadataList) {
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
        try {
            // 得到类的属性列表
            Field[] fields = householdRegisterInfo.getClass().getDeclaredFields();
            // key-目录元素 value-值
            Map<String, String> businessLiceInfoMap = new HashMap<>();
            for (Field field : fields) {
                // 这个是检查类中属性是否含有注解
                if (field.isAnnotationPresent(CataWordBind.class)) {
                    // 获取注解
                    CataWordBind annotation = field.getAnnotation(CataWordBind.class);
                    String word = annotation.word();
                    if (StrUtil.isBlank(word)){
                        continue;
                    }
                    field.setAccessible(true); // 设置些属性是可以访问的
                    String val = field.get(householdRegisterInfo) == null ? "" : field.get(householdRegisterInfo).toString();
                    businessLiceInfoMap.put(word, val);
                }
            }
            for (MaterialCatalogElement faMaterialCatalogMetadata : faMaterialCatalogMetadataList) {
                String catalogCode = faMaterialCatalogMetadata.getElementCode();
                String catalogName = faMaterialCatalogMetadata.getElementName();
                Map<String, String> cataMap = new HashMap<String, String>();
                cataMap.put("code", catalogCode);
                cataMap.put("name", catalogName);
                cataMap.put("words",
                        businessLiceInfoMap.get(catalogName) == null ? "" : businessLiceInfoMap.get(catalogName));
                resultList.add(cataMap);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultList;
    }

}
