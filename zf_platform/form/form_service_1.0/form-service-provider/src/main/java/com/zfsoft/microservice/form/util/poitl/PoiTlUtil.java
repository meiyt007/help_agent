package com.zfsoft.microservice.form.util.poitl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.PictureType;
import com.deepoove.poi.data.Pictures;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import com.deepoove.poi.template.MetaTemplate;
import com.deepoove.poi.template.PictureTemplate;
import com.deepoove.poi.template.run.RunTemplate;
import com.zfsoft.microservice.form.thread.DelTempFileThred;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName PoiTlUtil
 * @Description: poi-tl对word内容的替换
 * @Author wuxx
 * @Date 2021/11/30
 **/
public class PoiTlUtil {

    /**
     * 线程池 固定大小
     */
    public static final ExecutorService FIXED_THREAD_POOL = Executors.newFixedThreadPool(60);

    /**
     * @description:  初始化配置
     * @author: wuxx
     * @Date: 2021/12/6 13:43
     **/
    public static Configure initConfigure(){
        return Configure.builder()
                .addPlugin('%', new MultiImageRenderPolicy())
                .addPlugin('`',new FloatPicRenderPolicy())
                .addPlugin('$',new CheckBoxRenderPolicy())
                .useSpringEL(false)
                .setRenderDataComputeFactory(model -> new ZfSpELRenderDataCompute(model, false))
                .build();
    }

    /**
     * @description:  word中占位符、表格数据、图片的替换 返回输出流
     * @param docxName docx文件名称 含后缀
     * @param templateIs word模板流
     * @param mappings 替换的key-value map
     * @param isPdf 是否转换pdf
     * @author: wuxx
     * @Date: 2021/12/1 9:36
     **/
    public static void exportDocxAndPDF(Configure config,String docxName, InputStream templateIs, Map<String, Object> mappings,Boolean isPdf){
        XWPFTemplate template = XWPFTemplate.compile(templateIs,config).render(
                mappings);
        String outPath = "";
        OutputStream outputStream = null;
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-type", "application/force-download;charset=UTF-8");
            String displayFileName = StrUtil.isNotEmpty(docxName)?docxName+".docx":"填报数据.docx";
            if(null!=isPdf && isPdf){
                displayFileName = StrUtil.isNotEmpty(docxName)?docxName+".pdf":"填报数据.pdf";
            }
            String header = request.getHeader("User-Agent").toUpperCase();
            if (!header.contains("MSIE") && !header.contains("TRIDENT") && !header.contains("EDGE")) {
                displayFileName = URLEncoder.encode(displayFileName, "utf-8");
            } else {
                displayFileName = URLEncoder.encode(displayFileName, "utf-8");
                displayFileName = displayFileName.replace("+", "%20");
            }
            response.setHeader("Content-Disposition", "attachment;fileName=" + displayFileName);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Access-Control-Allow-Origin", "*");
            //@Cleanup OutputStream outputStream = response.getOutputStream();
            //template.writeAndClose(outputStream);
            if(null!=isPdf && isPdf){
                String pdfUuid = IdUtil.simpleUUID() + ".pdf";
                String windows = "windows";
                String property = "os.name";
                // 获取操作系统名称
                String osName = System.getProperty(property);
                if (osName != null) {
                    if(osName.toLowerCase().contains(windows)){
                        outPath = "C:\\temp\\";
                    }else {
                        outPath = "/temp/";
                    }
                } else {
                    outPath = "C:\\temp\\";
                }
                File dirFile = new File(outPath);
                if(!dirFile.exists()){
                    dirFile.mkdirs();
                }
                outPath = outPath + pdfUuid;
                template.writeToFile(outPath);
                outputStream = response.getOutputStream();
                Word2PdfAsposeUtil.docx2pdf(outputStream,new FileInputStream(outPath));
            }else {
                //下载word
                outputStream = response.getOutputStream();
                template.writeAndClose(outputStream);
            }
            System.out.println("完成转换");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(null!=templateIs)
                    templateIs.close();
                if(null!=outputStream){
                    outputStream.flush();
                    outputStream.close();
                }
            }catch (Exception e){

            }
            //删除temp文件
            FIXED_THREAD_POOL.execute(new DelTempFileThred(outPath));
        }
    }

    /**
     * @description:  word中占位符、表格数据、图片的替换 存在本地并返回本地的路径
     * @param templateIs word模板流
     * @param mappings 替换的key-value map
     **/
    public static String exportDocxInLocalStage(Configure config, InputStream templateIs, Map<String, Object> mappings,Boolean isPdf){
        XWPFTemplate template = XWPFTemplate.compile(templateIs,config).render(mappings);
        String outPath = "";
        String localPath = "";
        try {
            String fileUuid = IdUtil.simpleUUID();
            String wordUuid = fileUuid + ".docx";
            String pdfUuid = fileUuid + ".pdf";
            String windows = "windows";
            String property = "os.name";
            // 获取操作系统名称
            String osName = System.getProperty(property);
            if (osName != null) {
                if(osName.toLowerCase().contains(windows)){
                    localPath = "C:\\temp\\";
                }else {
                    localPath = "/temp/";
                }
            } else {
                localPath = "C:\\temp\\";
            }
            File dirFile = new File(localPath);
            if(!dirFile.exists()){
                dirFile.mkdirs();
            }
            outPath = localPath + pdfUuid;
            localPath = localPath + wordUuid;
            template.writeToFile(localPath);
            if (isPdf){
                Word2PdfAsposeUtil.doc2pdf(localPath,outPath);
            } else {
                outPath = localPath;
            }
//            Word2PdfAsposeUtil.doc2pdf(localPath,outPath);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(null!=templateIs) templateIs.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            if (isPdf){
                //删除temp文件
                FIXED_THREAD_POOL.execute(new DelTempFileThred(localPath));
            }
        }
        return outPath;
    }

    /**
     * @description:  将list、imgae转成替换的key-value map
     * @param mappings 替换的key-value map
     * @param flagList 表格循环的标识符
     * @param imageSeatIsListMap 替换的key：图片占位符  value：fileInputStream:图片流 height:高 width宽
     * @author: wuxx
     * @Date: 2021/12/1 9:36
     **/
    public static void flagListImageToMappings(Configure config ,InputStream inputStreamMeta,Map<String, Object> mappings,
                                               List<String> flagList,
                                               Map<String, Map<String, Object>> imageSeatIsListMap,
                                               Map<String, Map<String, Object>> pictureRenderDataListMap){
        //表格list标识
        if(null!=flagList && flagList.size()>0){
            LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
            for (String flag: flagList){
                config.customPolicy(flag,policy);
            }
        }

        Configure configure = initConfigure();
        //获取图片的标识符
        XWPFTemplate template = XWPFTemplate.compile(inputStreamMeta,configure);
        List<MetaTemplate> elementTemplates = template.getElementTemplates();
        try {
            if(null!=template){
                template.close();
            }
            if(null != inputStreamMeta){
                inputStreamMeta.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //图片
        if(null!=imageSeatIsListMap && imageSeatIsListMap.size()>0){
            //获取图片的标识符
            List<String> imageMetaList = new ArrayList<>();
            for(MetaTemplate metaTemplate : elementTemplates){
                if(metaTemplate instanceof RunTemplate){
                    RunTemplate runTemplate = (RunTemplate) metaTemplate;
                    String tagName = runTemplate.getTagName();
                    imageMetaList.add(tagName);
                }
            }
            for(Map.Entry<String, Map<String, Object>> entry : imageSeatIsListMap.entrySet()) {
                String key = entry.getKey();
                Map<String, Object> value = entry.getValue();
                //文件流
                Object inputStream = value.get("fileInputStream");
                //// 网络图片(注意网络耗时对系统可能的性能影响)
                //put("urlImg", Pictures.ofUrl("http://deepoove.com/images/icecream.png")
                //  .size(100, 100).create());
                Object ofUrl = value.get("ofUrl");
                if (null == inputStream && null == ofUrl) {
                    continue;
                }

                Integer height = (Integer) value.getOrDefault("height", 200);
                Integer width = (Integer) value.getOrDefault("width", 200);

                //从word中获取图片的高宽
                if (null != imageMetaList) {
                    metaOut:
                    for (String meta : imageMetaList) {
                        String[] sMeta = meta.split("_");
                        if (sMeta[0].equals(key)) {
                            //{{@imageUrl_500_200}}
                            //高——宽
                            String[] splitMeta = meta.split("_");
                            if (splitMeta.length > 2) {
                                height = Integer.parseInt(splitMeta[1]);
                                width = Integer.parseInt(splitMeta[2]);
                                key = meta;
                                break metaOut;
                            }
                        }
                    }
                }
                if (null != inputStream && null == ofUrl) {
                    InputStream fileInputStream = (InputStream) inputStream;
                    mappings.put(key, Pictures.ofStream(fileInputStream, PictureType.JPEG)
                            .size(width, height).create());
                }
                if (null == inputStream && null != ofUrl) {
                    {
                        // 网络图片(注意网络耗时对系统可能的性能影响)
                        mappings.put(key, Pictures.ofUrl(ofUrl.toString())
                                .size(width, height).create());
                    }
                }
            }
        }

        /*样本中预置图片 替换  处理 （针对签章）
            --(替换只能一图替换一图，替换的样本中标记名不要加前缀，普通字串就行，如{{qzName}})   tagName.matches("^[a-zA-z].*")
            --word上的图片样式为主，不可传入大小样式
            --替换在电子表单上只能使用上传图片组件且不支持多选文件
          以及
          浮动图片处理 （也是针对签章）
            --
          */
        for(MetaTemplate metaTemplate : elementTemplates){
            if(metaTemplate instanceof PictureTemplate){//样本中预置图片 替换
                boolean isReplaceDefaultPic = true;
                PictureTemplate pictureTemplate = (PictureTemplate) metaTemplate;
                String tagName = pictureTemplate.getTagName();

                if (null!=pictureRenderDataListMap && pictureRenderDataListMap.size()>0
                        && pictureRenderDataListMap.get(tagName) != null){
                    Map<String, Object> value = pictureRenderDataListMap.get(tagName);
                    //文件流
                    Object inputStream = value.get("fileInputStream");
                    Object ofUrl = value.get("ofUrlList");
                    Integer height = (Integer) value.getOrDefault("height", 150);
                    Integer width = (Integer) value.getOrDefault("width", 150);
                    if (null != inputStream) {
                        List<InputStream> fileInputStreamList = (List<InputStream>) inputStream;
                        if (fileInputStreamList.size() == 1){
                            isReplaceDefaultPic = false;
                            mappings.put(tagName, Pictures.ofStream(fileInputStreamList.get(0)).size(height, width).create());
                        }
                    }else if (null != ofUrl) {
                        List<String> ofUrlList = (List<String>) ofUrl;
                        if (ofUrlList.size() == 1){
                            isReplaceDefaultPic = false;
                            mappings.put(tagName, Pictures.ofUrl(ofUrlList.get(0)).size(height, width).create());
                        }
                    }
                }
                if (isReplaceDefaultPic){
                    try {
                        ClassPathResource classPathResource = new ClassPathResource("images/emptycircle.png");
                        InputStream inputStream = classPathResource.getInputStream();
                        mappings.put(tagName, Pictures.ofStream(inputStream).create());

                        //File file = ResourceUtils.getFile("classpath:images/emptycircle.png");
                        //mappings.put(tagName, Pictures.ofLocal(file.getPath()).create());
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
            }
            if(metaTemplate instanceof RunTemplate){
                if (((RunTemplate) metaTemplate).getSign().equals('`')){//浮动图片
                    //{{`qz_150_0_25_0_0}}  图片属性名_水平偏移量_纵正负位_纵向偏移量_宽_高  宽高不设置时传0 必须 5个_格式 纵正负位：0负数
                    String tagName = ((RunTemplate) metaTemplate).getTagName();
                    String[] sMeta = tagName.split("_");
                    if(sMeta.length != 6){
                        throw new RuntimeException("浮动图片设置失败，请注意占位字串格式正确。");
                    }

                    String key = sMeta[0];
                    Map<String, Object> value = pictureRenderDataListMap.get(key);
                    if (null != value){
                        //文件流
                        Object inputStream = value.get("fileInputStream");
                        Object ofUrl = value.get("ofUrlList");
                        if (null == inputStream && null == ofUrl) {
                            continue;
                        }

                        Integer height = (Integer) value.getOrDefault("height", 60);
                        Integer width = (Integer) value.getOrDefault("width", 60);
                        width = Integer.parseInt(sMeta[4]) == 0 ? width : Integer.parseInt(sMeta[4]);
                        height = Integer.parseInt(sMeta[5]) == 0 ? height : Integer.parseInt(sMeta[5]);
                        String zxfh = "0".equals(sMeta[2])? "-" : ""; //纵向偏移量的正负符号

                        if (null != inputStream) {
                            List<InputStream> fileInputStreamList = (List<InputStream>) inputStream;
                            if (fileInputStreamList.size() == 1){
                                mappings.put(tagName,
                                        //Pictures.ofStream(fileInputStreamList.get(0),PictureType.JPEG)
                                        Pictures.ofStream(fileInputStreamList.get(0))
                                                .size(width, height)
                                                .altMeta(sMeta[1]+"_"+zxfh+sMeta[3]+"_"+key)//水平偏移量_纵向偏移量_图片名
                                                .create());
                                //mappings.remove(key);
                            }
                        }else if (null != ofUrl) {
                            List<String> ofUrlList = (List<String>) ofUrl;
                            if (ofUrlList.size() == 1){
                                mappings.put(tagName,
                                        //Pictures.ofUrl(ofUrlList.get(0),PictureType.JPEG)
                                        Pictures.ofUrl(ofUrlList.get(0))
                                                .size(width, height)
                                                .altMeta(sMeta[1]+"_"+zxfh+sMeta[3]+"_"+key)//水平偏移量_纵向偏移量_图片名
                                                .create());
                                //mappings.remove(key);
                            }
                        }
                    }
                }
            }
        }

        //多图片
        if(null!=pictureRenderDataListMap && pictureRenderDataListMap.size()>0){
            List<String> imageMetaList = new ArrayList<>();
            for(MetaTemplate metaTemplate : elementTemplates){
                if(metaTemplate instanceof RunTemplate){
                    RunTemplate runTemplate = (RunTemplate) metaTemplate;
                    String tagName = runTemplate.getTagName();
                    imageMetaList.add(tagName);
                }
            }
            for(Map.Entry<String, Map<String, Object>> entry : pictureRenderDataListMap.entrySet()) {
                String key = entry.getKey();
                Map<String, Object> value = entry.getValue();
                //文件流
                Object inputStream = value.get("fileInputStream");
                //// 网络图片(注意网络耗时对系统可能的性能影响)
                //put("urlImg", Pictures.ofUrl("http://deepoove.com/images/icecream.png")
                //  .size(100, 100).create());
                Object ofUrl = value.get("ofUrlList");
                if (null == inputStream && null == ofUrl) {
                    continue;
                }

                Integer height = (Integer) value.getOrDefault("height", 200);
                Integer width = (Integer) value.getOrDefault("width", 200);

                //从word中获取图片的高宽
                if (null != imageMetaList) {
                    metaOut:
                    for (String meta : imageMetaList) {
                        String[] sMeta = meta.split("_");
                        String checkMeta = "";
                        if(sMeta.length>2){
                            String sMetaSec = sMeta[sMeta.length-2];
                            int lastIndexOf = meta.lastIndexOf(sMetaSec)-1;
                            checkMeta = meta.substring(0,lastIndexOf);
                        }else{
                            checkMeta = sMeta[0];
                        }

                        if (key.equals(checkMeta)) {
                            //{{@imageUrl_500_200}}
                            //高——宽
                            if (sMeta.length > 2) {
                                height = Integer.parseInt(sMeta[sMeta.length-2]);
                                width = Integer.parseInt(sMeta[sMeta.length-1]);
                                key = meta;
                                break metaOut;
                            }
                        }
                    }
                }

                //这里是上面已经做了 样本中预置图片 替换 处理  ，则跳过
                if (null != mappings.get(key) && mappings.get(key) instanceof PictureRenderData){
                    continue;
                }

                List<PictureRenderData> pictureRenderDataList = new ArrayList<>();
                if (null != inputStream && null == ofUrl) {

                    List<InputStream> fileInputStreamList = (List<InputStream>) inputStream;
                    for(InputStream fileInputStream : fileInputStreamList){
                        PictureRenderData pictureRenderData =Pictures.ofStream(fileInputStream, PictureType.JPEG)
                                .size(width, height).create();
                        pictureRenderDataList.add(pictureRenderData);
                    }
                }
                if (null == inputStream && null != ofUrl) {
                    List<String> ofUrlList = (List<String>) ofUrl;
                    for(String ofurl : ofUrlList){
                        PictureRenderData pictureRenderData = Pictures.ofUrl(ofurl).size(height, width).create();
                        pictureRenderDataList.add(pictureRenderData);
                    }
                }
                mappings.put(key,pictureRenderDataList);
            }
        }
    }

    /**
     * @description: 处理设置的模板list标签
     * @param inputStreamMeta 模板流
     * @param flagList 列表集合
     * @author: wuxx
     * @Date: 2021/12/13 16:52
     **/
    public static void dealFlagListMetas(InputStream inputStreamMeta,List<String> flagList){
        //获取列表#开头的标识符
        XWPFTemplate template = XWPFTemplate.compile(inputStreamMeta);
        List<MetaTemplate> elementTemplates = template.getElementTemplates();
        try {
            for(MetaTemplate metaTemplate : elementTemplates){
                if(metaTemplate instanceof RunTemplate){
                    RunTemplate runTemplate= (RunTemplate) metaTemplate;
                    if("#".equals(runTemplate.getSign().toString())){
                        flagList.add(runTemplate.getTagName());
                    }
                }
            }
            if(null!=inputStreamMeta){
                inputStreamMeta.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     * @description: 数组转List
     * @param jsonArray 数组
     * @author: wuxx
     * @Date: 2021/12/3 17:33
     **/
    public static List<HashMap<String, Object>>  jsonArr2List(cn.hutool.json.JSONArray jsonArray){
        List<HashMap<String, Object>> mapList = new ArrayList<>();
        Iterator<Object> iterator = jsonArray.iterator();
        while(iterator.hasNext()){
            HashMap<String, Object> hashMap = new HashMap<>();
            cn.hutool.json.JSONObject optObject = (cn.hutool.json.JSONObject)iterator.next();
            for(String key : optObject.keySet()){
                hashMap.put(key,optObject.get(key));
            }
            mapList.add(hashMap);
        }
        return mapList;
    }
}
