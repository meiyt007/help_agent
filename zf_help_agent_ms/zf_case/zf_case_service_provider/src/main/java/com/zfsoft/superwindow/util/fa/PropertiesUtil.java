package com.zfsoft.superwindow.util.fa;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.superwindow.util.FaStaticParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * properties配置文件的读写
 *
 * @author gaolh
 * @date 2016-3-24
 *
 */
public class PropertiesUtil {
    /** 日志记录 */
    public Logger log = LoggerFactory.getLogger(PropertiesUtil.class);

    /** 将日志记录到文件中，日志文件路径在log4j.xml中配置 */
    public Logger fileLog = LoggerFactory.getLogger("FILE_LOGGER");

    private String properiesName = "";
    private InputStream is = null;
    private Properties p;

    /**
     * 初始化帮助类
     *
     * @param fileName
     *            配置文件路径
     */
    public PropertiesUtil(String fileName) {
        log.debug("初始化读取{}的内容", fileName);
        this.properiesName = fileName;
        try {
            is = PropertiesUtil.class.getClassLoader().getResourceAsStream(properiesName);
            p = new Properties();
            BufferedReader bf = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            p.load(bf);
            //p.load(is);
        } catch (Exception e) {
            fileLog.error(fileName + "文件初始化失败", e.getMessage());
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                fileLog.error(fileName + "文件流关闭失败", e.getMessage());
            }
        }
    }

    /**
     * 根据key，读取配置的值
     *
     * @author gaolh
     * @date 2016-3-24
     *
     * @param key
     *            配置文件的key值
     * @return
     */
    public String readProperty(String key) {
        String propertyValue = p.getProperty(key);
        //判断是否加密 如果加密了 按照加密后的读取
        if (StrUtil.isBlank(propertyValue)){
            return propertyValue;
        }

        if (propertyValue.startsWith("ENC(") && propertyValue.endsWith(")")) {
            propertyValue = propertyValue.substring(4, propertyValue.length()-1);
            try {
                return AesUtil.aesDncode(FaStaticParam.PROPERTIES_SALT,propertyValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return propertyValue;
//        return p.getProperty(key);
    }

    public Properties getProperties() {
        return p;
    }

    /**
     * 将key、value写入配置文件中
     *
     * @author gaolh
     * @date 2016-3-24
     * @param key
     *            需要写入的key
     * @param value
     *            需要写入的值
     */
    public void writeProperty(String key, String value) {
        log.debug("{}文件内容写入， key={}, value={}", properiesName, key, value);
        OutputStream os = null;
        try {
            os = new FileOutputStream(PropertiesUtil.class.getClassLoader().getResource(properiesName).getFile());

            p.setProperty(key, value);
            p.store(os, key);
            os.flush();
            os.close();
        } catch (Exception e) {
            fileLog.error(properiesName + "文件写入失败", e.getMessage());
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
                if (null != os) {
                    os.close();
                }
            } catch (Exception e) {
                fileLog.error(properiesName + "文件流关闭失败", e.getMessage());
            }
        }
    }
}
