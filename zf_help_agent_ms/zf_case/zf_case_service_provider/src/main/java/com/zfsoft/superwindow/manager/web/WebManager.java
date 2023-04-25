package com.zfsoft.superwindow.manager.web;

import cn.hutool.core.util.StrUtil;
import com.zfsoft.platform.common.data.BaseStaticParameter;
import com.zfsoft.platform.utils.validate.ResultInfoException;
import com.zfsoft.superwindow.util.FileUtils;
import com.zfsoft.superwindow.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * @author hut
 * @date 2022/5/31
 */
@Service
@Slf4j
public class WebManager {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public String login() {
        // 登录成功
        String token = JwtUtil.sign("341222233334444", 0);
        redisTemplate.opsForValue().set("browser:jwt:"+token, "", JwtUtil.EXPIRE_TIME, TimeUnit.MINUTES);
        return token;
    }

    public void previewImageAndPdf (String fastdfsNginxUrl) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        try {
            // 预览只支持图片和pdf预览
            if (StrUtil.isNotEmpty(fastdfsNginxUrl)) {
                String nginxUrl = BaseStaticParameter.FASTDFS_NGINX_URL + "/";
                String filePath = fastdfsNginxUrl.replace(nginxUrl, "");
                String substring = filePath.substring(0, filePath.indexOf("."));
                String formatName = filePath.substring(substring.length() + 1);
                if (BaseStaticParameter.ATTA_IMAGE_EXT_SET.contains(formatName.toLowerCase())) {
                    BufferedImage image = ImageIO.read(new URL(fastdfsNginxUrl));
                    // 将内存中的图片通过流动形式输出到客户端
                    ImageIO.write(image, formatName, response.getOutputStream());
                } else {
                    if ("pdf".equals(formatName.toLowerCase())) {
                        File file = null;
                        if (fastdfsNginxUrl.startsWith("http://")) {
                            file = FileUtils.getHttpFile(fastdfsNginxUrl);
                        } else if (fastdfsNginxUrl.startsWith("https://")) {
                            file = FileUtils.getSSLFile(fastdfsNginxUrl);
                        }
                        InputStream is = new FileInputStream(file);
                        response.setContentType("application/pdf");
                        OutputStream out = response.getOutputStream();
                        byte[] b = new byte[512];
                        while ((is.read(b)) != -1) {
                            out.write(b);
                        }
                        out.flush();
                        is.close();
                        out.close();
                    }
                }
            }
        } catch (Exception e) {
            throw new ResultInfoException(e.getMessage());
        }
    }

    /**
     * 跟进fast文件地址下载文件
     * @param fastdfsNginxUrl
     */
    public void downloadFileByUrl (String fastdfsNginxUrl) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        try {
           FileUtils.downloadFromUrl(fastdfsNginxUrl, response);
        } catch (Exception e) {
            throw new ResultInfoException(e.getMessage());
        }
    }

}
