package com.zfsoft.microservice.platform.gateway.utils;

import com.zfsoft.microservice.platform.gateway.config.ZhuofanConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName CheckHostUrl
 * @Description:
 * @Author wuxx
 * @Date 2021/5/27
 **/
@Component
public class CheckHostUtil {

    @Autowired
    private ZhuofanConfig zhuofanConfig;

    //允许通过的站点
    private  String[] ALLOW_SITES=null;

    public boolean check(String refererPath,String host) {
        boolean refererResult=false;
        if(zhuofanConfig.isRefererCheck() && refererPath != null){
            if(null!=zhuofanConfig.getAllowSites()){
                ALLOW_SITES=zhuofanConfig.getAllowSites().split(",");
            }
            if(refererPath.contains(host)){
                refererResult=true;
            }else{
                if(null != ALLOW_SITES){
                    for (String allowSite : ALLOW_SITES) {
                        if(refererPath.contains(allowSite)){
                            refererResult=true;
                            break;
                        }
                    }
                }else {
                    //没有设置默认通过
                    refererResult=true;
                }
            }
        }else{
            refererResult=true;
        }
        return refererResult;
    }
}
