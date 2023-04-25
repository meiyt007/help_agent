package com.zfsoft.platform.utils.dict;

import com.google.common.collect.Maps;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.utils.data.SysDict;
import com.zfsoft.platform.utils.data.SysDistrict;
import com.zfsoft.platform.utils.dict.enums.TypeEnums;
import com.zfsoft.platform.utils.feign.SysDictFeignService;
import com.zfsoft.platform.utils.feign.SysDistrictFeignService;
import com.zfsoft.platform.utils.spring.SpringContextHelper;
import com.zfsoft.platform.utils.util.Assert;
import com.zfsoft.platform.utils.util.OptionalUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * @author: kkfan
 * @create: 2022-04-27 13:59:20
 * @description: 取值帮助类
 */
public class CTTUtil {

    /**
     * 对象缓存
     */
    private static Map<TypeEnums, Object> dealObjMap = Maps.newHashMap();

    /**
     * 数据处理规则
     * @author kkfan
     * @return 数据处理规则
     */
    public static Map<TypeEnums, BiFunction<DictCTT, String, String>> dealMap = new HashMap<TypeEnums, BiFunction<DictCTT, String, String>>() {
        {
            put(TypeEnums.DICT, (dictCTT, code) -> dealDictCTT(dictCTT, code));
            put(TypeEnums.DISTRICT, (dictCTT, code) -> dealDistrictCTT(dictCTT, code));
        }
    };

    /**
     * 获取字典项配置文本
     * @param dictCTT 字典标识
     * @param codeOrOid 字典code或者oid
     * @return  字典文本
     */
    private static String dealDictCTT(DictCTT dictCTT, String codeOrOid) {
        SysDictFeignService sysDictService = initDealService(SysDictFeignService.class, dictCTT.typeEnum());
        return dictCTT.isDictOid() ? getDictTextByDictOid(sysDictService, codeOrOid) : getDictTextByCode(sysDictService, codeOrOid);
    }

    /**
     * 通过字典code获取字典文本
     * @param sysDictService    字典接口
     * @param code  字典编码
     * @return  字典文本
     */
    private static String getDictTextByCode(SysDictFeignService sysDictService, String code) {
        ApiResultSet<SysDict> apiResultSet = sysDictService.getSysDictByCode(code);
        Assert.isTrue(apiResultSet.getCode() == 200, "调用字典接口失败！");
        return OptionalUtils.resolve(() -> apiResultSet.getData().getName()).orElseGet(() -> null);
    }

    /**
     * 通过字典oid获取字典文本
     * @param sysDictService    字典接口
     * @param oid  字典oid
     * @return  字典文本
     */
    private static String getDictTextByDictOid(SysDictFeignService sysDictService, String oid) {
        ApiResultSet<SysDict> apiResultSet = sysDictService.getSysDictByDictOid(oid);
        Assert.isTrue(apiResultSet.getCode() == 200, "调用字典接口失败！");
        return OptionalUtils.resolve(() -> apiResultSet.getData().getName()).orElseGet(() -> null);
    }

    /**
     * 获取字典项配置文本
     * @param dictCTT 字典标识
     * @param districtOid 区划oid
     * @return  字典文本
     */
    private static String dealDistrictCTT(DictCTT dictCTT, String districtOid) {
        SysDistrictFeignService sysDistrictFeignService = initDealService(SysDistrictFeignService.class, dictCTT.typeEnum());
        ApiResultSet<SysDistrict> apiResultSet = sysDistrictFeignService.getSysDistrictByDistrictOid(districtOid);
        Assert.isTrue(apiResultSet.getCode() == 200, "调用区划接口失败！");
        return OptionalUtils.resolve(() -> apiResultSet.getData().getName()).orElseGet(() -> null);
    }

    /**
     * 初始化所需服务层
     * @author kkfan
     * @param clz   服务层
     * @param typeEnums 类型枚举 {@link TypeEnums}
     * @param <T>   clz
     * @return  T
     */
    private static <T> T initDealService(Class<T> clz, TypeEnums typeEnums) {
        return (T) OptionalUtils.resolve(() -> dealObjMap.get(typeEnums)).orElseGet(() -> {
            synchronized (CTTUtil.class) {
                OptionalUtils.resolve(() -> dealObjMap.get(typeEnums)).orElseGet(() -> {
                    T t = SpringContextHelper.getBean(clz);
                    dealObjMap.put(typeEnums, t);
                    return t;
                });
            }
            return Optional.ofNullable(dealObjMap.get(typeEnums)).orElseThrow(() -> new ClassCastException(clz.getName() + "初始化异常!"));
        });
    }

}
