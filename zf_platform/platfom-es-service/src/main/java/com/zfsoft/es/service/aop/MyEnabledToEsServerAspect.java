package com.zfsoft.es.service.aop;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zfsoft.es.service.annotation.EnabledToEsServer;
import com.zfsoft.es.service.dto.SendESObjDto;
import com.zfsoft.es.service.enums.OperationTypeEnum;
import com.zfsoft.es.service.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.*;


/**
 * 将自定义的注解，使用AOP的方式，来增强它的功能
 * @author: kkfan
 * @create: 2021-1-18 18:35
 */
@Aspect
@Component
public class MyEnabledToEsServerAspect {

    private static final Logger logger = LoggerFactory.getLogger(MyEnabledToEsServerAspect.class);

    @Autowired(required = false)
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 标记着@enabledToEsServer注解的方法，不直接调用，而是在该方法前后增加代码，增强该方法功能
     * @param proceedingJoinPoint
     * @param enabledToEsServer 增强点的定义
     * @throws Throwable
     */
    @Around("@annotation(enabledToEsServer)")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint, EnabledToEsServer enabledToEsServer) throws Throwable {
        try {
            // 执行方法前增强（根据主键id查询） 判断是否为从es查询操作 若从es中根据方法参数主键获取到数据则直接返回 若查询无数据则继续从库中查询
            if (enabledToEsServer.enabledToEs() && StringUtils.equals(enabledToEsServer.operationType().name(), OperationTypeEnum.QUERY.name())) {
                // 通过反射根据接口参数名 + 入参值 + 返回值类型构造查询语句  局限性较大 仅用于单表简单查询
                // Object object = EsDataUtil.queryByReflection(proceedingJoinPoint, enabledToEsServer);
                // 利用原生sql转化为es查询语句
                Object object = EsDataUtil.queryBySql(proceedingJoinPoint, sqlSessionFactory, elasticsearchTemplate);
                // es中有数据直接返回 没有则执行切入方法 从库中查找数据
                if ((null != object) && (!(object instanceof List) || (object instanceof List && !CollectionUtils.isEmpty((List)object)))) {
                    logger.debug(MessageFormat.format("从es中获取数据，数据详情为：{0}", JSONObject.toJSONString(object)));
                    return object;
                } else {
                    logger.debug("从es中未查询到相关数据");
                }
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
            throw new Exception(e);
        }
        // 调用实际的方法
        Object object = proceedingJoinPoint.proceed();
        try {
            // 方法执行后增强
            if(enabledToEsServer.enabledToEs()) {
                // 数据新增 / 更新操作
                if(StringUtils.equals(enabledToEsServer.operationType().name(), OperationTypeEnum.SAVE_OR_UPDATE.name())) {
                    // 获取入参列表
                    List<Object> arsObjList = Arrays.asList(proceedingJoinPoint.getArgs());
                    Optional.ofNullable(arsObjList)
                            .orElseGet(Lists::newArrayList)
                            .stream()
                            .forEach(obj -> {
                                try {
                                    // 封装传入es数据
                                    SendESObjDto sendESObjDto = EsDataUtil.getSendESObj(obj, enabledToEsServer);
                                    // 调es服务接口 数据传入es
                                    Map<String, Object> objectMap = ElasticSearchUtil.universalSave(sendESObjDto);
                                    System.out.println(JSONObject.toJSONString(objectMap));
                                } catch (Exception e) {
                                    logger.error(e.toString(), e);
                                    throw new RuntimeException(e);
                                }
                            });
                } else if(StringUtils.equals(enabledToEsServer.operationType().name(), OperationTypeEnum.DELETE.name())) {
                    // 逻辑删除暂未实现
                }
            }
            return object;
        } catch (Exception e) {
            logger.error(e.toString(), e);
            throw new Exception(e);
        }
    }

}
