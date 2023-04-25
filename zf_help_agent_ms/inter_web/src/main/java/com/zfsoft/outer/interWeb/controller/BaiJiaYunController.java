package com.zfsoft.outer.interWeb.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.outer.interWeb.util.FastDFSUtil;
import com.zfsoft.outer.interWeb.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


/**
 * @author hut
 * @date 2023/3/21
 * 百家云操作控制类
 */
@Slf4j
@RestController
@RequestMapping(value = "/web/baijiayun")
public class BaiJiaYunController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${fdfs.fastDFSNginxUrl}")
    private String fastDFSNginxUrl;

    @Value("${baijiayun.appId}")
    private  String appId;
    @Value("${baijiayun.appSecret}")
    private  String appSecret;
    @Value("${baijiayun.customerId}")
    private  String customerId;
    @Value("${baijiayun.customerSecret}")
    private  String customerSecret;
    @Value("${baijiayun.streamMixUrl}")
    private  String streamMixUrl;
    @Value("${baijiayun.streamMixStatusUrl}")
    private  String streamMixStatusUrl;
    @Value("${baijiayun.streamMixStopUrl}")
    private  String streamMixStopUrl;
    @Value("${baijiayun.streamMixUpdateUrl}")
    private  String streamMixUpdateUrl;
    @Value("${baijiayun.streamQueryUrl}")
    private  String streamQueryUrl;

    /**
     * 获取混流录制录制资源
     * <a href="https://docs.baijiayun.com/crs/api/mix/resource.html">...</a>
     * @param roomId 必传 录制房间id
     * @param userId 必传 录制用户id
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/streamMix")
    public String streamMix(String roomId, String userId){
        log.info("获取混流录制录制资源：roomId->{}，userId->{}", roomId, userId);
        JSONObject jsonObjectResult = new JSONObject();
        try {
            // 组装参数
            JSONObject requestObj = new JSONObject();
            requestObj.put("app_id", appId);
            requestObj.put("room_id", roomId);
            JSONObject inputParamsObj = new JSONObject();
            JSONObject mixConfigObj = new JSONObject();
            // 布局方式(暂时只支持 1) 1: 自定义布局 2: 画廊布局(默认) 3:演讲布局 4:悬浮布局
            mixConfigObj.put("mix_mode", 1);
            JSONArray inputList = new JSONArray();
            JSONObject inputObj = new JSONObject();
            inputObj.put("user_id", userId);
            inputObj.put("x", 10);
            inputObj.put("y", 10);
            inputObj.put("width", 600);
            inputObj.put("height", 320);
            inputObj.put("z_order", 1);
            inputList.add(inputObj);
            JSONObject videoObj = new JSONObject();
            videoObj.put("size", "1280x720");
            videoObj.put("fps", 15);
            videoObj.put("bit_rate", 1000);
            videoObj.put("background_color", "#FFFFFF");
            mixConfigObj.put("input_list", inputList);
            mixConfigObj.put("video", videoObj);
            inputParamsObj.put("mix_config", mixConfigObj);
            requestObj.put("input_params", inputParamsObj);
            // 调用接口
            String result = HttpRequest.post(streamMixUrl).header("Authorization", getAuthorization())
                    .body(requestObj.toJSONString()).execute().body();
            log.info("获取混流录制录制资源结果：result->{}", result);
            return result;
        } catch (Exception ex) {
            jsonObjectResult.put("code", 500);
            jsonObjectResult.put("msg", "获取混流录制录制资源发生错误");
        }
        return  jsonObjectResult.toJSONString();
    }

    /**
     * 开始混流录制
     * <a href="https://docs.baijiayun.com/crs/api/mix/start.html">...</a>
     * @param roomId 必传 录制房间id
     * @param taskId 必传 录制任务id
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/streamMixStart")
    public String streamMixStart(String roomId, String taskId){
        log.info("开始混流录制：roomId->{}, taskId->{}", roomId, taskId);
        JSONObject jsonObjectResult = new JSONObject();
        try {
            // 组装参数
            JSONObject requestObj = new JSONObject();
            requestObj.put("app_id", appId);
            requestObj.put("room_id", roomId);
            requestObj.put("task_id", taskId);
            JSONObject recordOutputParamsObj = new JSONObject();
            // 输出文件格式，默认为 flv , 可选 flv、mp4
            recordOutputParamsObj.put("output_file_format", "mp4");
            requestObj.put("record_output_params", recordOutputParamsObj);
            // 调用接口
            String result = HttpRequest.post(streamMixStatusUrl).header("Authorization", getAuthorization())
                    .body(requestObj.toJSONString()).execute().body();
            log.info("开始混流录制结果：result->{}", result);
            return result;
        } catch (Exception ex) {
            jsonObjectResult.put("code", 500);
            jsonObjectResult.put("msg", "开始混流录制发生错误");
        }
        return  jsonObjectResult.toJSONString();
    }

    /**
     * 结束混流录制
     * <a href="https://docs.baijiayun.com/crs/api/mix/stop.html">...</a>
     * @param roomId  录制房间id
     * @param taskId  录制任务id
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/streamMixStop")
    public String streamMixStop(String roomId, String taskId){
        log.info("结束混流录制：roomId->{}, taskId->{}", roomId, taskId);
        JSONObject jsonObjectResult = new JSONObject();
        try {
            // 组装参数
            JSONObject requestObj = new JSONObject();
            requestObj.put("app_id", appId);
            requestObj.put("room_id", roomId);
            requestObj.put("task_id", taskId);
            // 停止的类型 1: 全部 2: 云端录制 3: 旁路推流
            requestObj.put("type", 1);
            // 调用接口
            String result = HttpRequest.post(streamMixStopUrl).header("Authorization", getAuthorization())
                    .body(requestObj.toJSONString()).execute().body();
            log.info("结束混流录制结果：result->{}", result);
            return result;
        } catch (Exception ex) {
            jsonObjectResult.put("code", 500);
            jsonObjectResult.put("msg", "结束混流录制发生错误");
        }
        return  jsonObjectResult.toJSONString();
    }

    /**
     * 更新混流录制资源
     * @param roomId  录制房间id
     * @param userId  录制用户id
     * @param taskId  录制任务id
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/streamMixUpdate")
    public String streamMixUpdate(String roomId, String userId, String taskId){
        log.info("更新混流录制资源：roomId->{}，userId->{}，taskId->{}", roomId, userId, taskId);
        JSONObject jsonObjectResult = new JSONObject();
        try {
            // 组装参数
            JSONObject requestObj = new JSONObject();
            requestObj.put("app_id", appId);
            requestObj.put("room_id", roomId);
            requestObj.put("task_id", taskId);
            JSONObject inputParamsObj = new JSONObject();
            JSONObject mixConfigObj = new JSONObject();
            // 布局方式(暂时只支持 1) 1: 自定义布局 2: 画廊布局(默认) 3:演讲布局 4:悬浮布局
            mixConfigObj.put("mix_mode", 1);
            JSONArray inputList = new JSONArray();
            JSONObject inputObj = new JSONObject();
            inputObj.put("user_id", userId);
            inputObj.put("x", 10);
            inputObj.put("y", 10);
            inputObj.put("width", 600);
            inputObj.put("height", 320);
            inputObj.put("z_order", 1);
            inputList.add(inputObj);
            JSONObject videoObj = new JSONObject();
            videoObj.put("size", "1280x720");
            videoObj.put("fps", 15);
            videoObj.put("bit_rate", 1000);
            videoObj.put("background_color", "#FFFFFF");
            mixConfigObj.put("input_list", inputList);
            mixConfigObj.put("video", videoObj);
            inputParamsObj.put("mix_config", mixConfigObj);
            requestObj.put("input_params", inputParamsObj);
            // 调用接口
            String result = HttpRequest.post(streamMixUrl).header("Authorization", getAuthorization())
                    .body(requestObj.toJSONString()).execute().body();
            log.info("获取混流录制录制资源结果：result->{}", result);
            return result;
        } catch (Exception ex) {
            jsonObjectResult.put("code", 500);
            jsonObjectResult.put("msg", "获取混流录制录制资源发生错误");
        }
        return  jsonObjectResult.toJSONString();
    }

    /**
     * 获取录制文件
     * <a href="https://docs.baijiayun.com/crs/api/serch_files/file.html">...</a>
     * @param roomId    录制房间id
     * @param start     查询范围开始时间, 如 2021-07-28
     * @param end       查询范围结束时间, 如 2021-08-01
     * @param page      页数, 录制文件数量过多时, 可以分多页来获取, 每页取 page_size 条, 默认值为 1
     * @param pageSize  每页获取的条数, 最大值不能超过 1000, 默认值 100
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/streamQuery")
    public String streamQuery(String roomId, String start, String end, int page, int pageSize){
        log.info("获取录制文件：roomId->{}, start->{}, end->{}, page->{}, pageSize->{}"
                , roomId, start, end, page, pageSize);
        JSONObject jsonObjectResult = new JSONObject();
        try {
            // 组装参数
            streamQueryUrl += "?app_id=" + appId +
                    "&room_id=" + roomId +
                    "&start_ts=" + start +
                    "&end_ts=" + end;
            if (page != 0) {
                streamQueryUrl += "&page=" + page;
            }
            if (pageSize != 0) {
                streamQueryUrl += "&pageSize=" + pageSize;
            }
            // 调用接口
            String result = HttpRequest.get(streamQueryUrl).header("Authorization", getAuthorization()).execute().body();
            log.info("获取录制文件结果：result->{}", result);
            return result;
        } catch (Exception ex) {
            jsonObjectResult.put("code", 500);
            jsonObjectResult.put("msg", "获取录制文件发生错误");
        }
        return  jsonObjectResult.toJSONString();
    }

    /**
     * 定时上传视频录制文件
     * 每天00:30开始下载上传操作
     */
    @Scheduled(cron = "0 30 0 * * ? ")
    // @GetMapping(value = "/testUpload")
    public void uploadVideoRecord(){
        try {
            log.info("定时上传视频录制文件开始！");
            // 查询未下载且结束日期不为空的视频录制数据
            String querySql = "select ID, ROOM_NUMBER, START_DATE, STOP_DATE from t_ha_video_cloud_record where DOWNLOAD_STATUS = 0 and STOP_DATE is not null;";
            List<Map<String, Object>> maps = jdbcTemplate.queryForList(querySql);
            for (Map<String, Object> map : maps) {
                String id = map.get("ID").toString();
                String roomNumber = map.get("ROOM_NUMBER").toString();
                String startDate = map.get("START_DATE").toString().substring(0, 10);
                String stopDate = map.get("STOP_DATE").toString().substring(0, 10);
                String result = streamQuery(roomNumber, startDate, stopDate, 1, 1000);
                JSONObject res = JSON.parseObject(result);
                if ((Integer)res.get("code") != 200) {
                    continue;
                }
                JSONObject data = JSON.parseObject(res.get("data").toString());
                JSONArray list = JSON.parseArray(data.get("list").toString());
                // 遍历同一视频编号的所有视频链接进行上传到本地fast系统
                JSONArray resList = new JSONArray();
                for (int i = 0; i < list.size(); i++) {
                    JSONObject o = list.getJSONObject(i);
                    String url = o.get("oss_path").toString();
                    String name = o.get("name").toString();
                    String size = o.get("total_size").toString();
                    String date = o.get("created_at").toString();
                    if (StrUtil.isNotBlank(url)) {
                        MultipartFile file = FileUtils.tranFile(url);
                        String path = FastDFSUtil.uploadFile(file.getInputStream(), "mp4");
                        path = fastDFSNginxUrl + "/" + path;
                        JSONObject jObj = new JSONObject();
                        jObj.put("url", path);
                        jObj.put("name", name);
                        jObj.put("size", size);
                        jObj.put("date", date);
                        resList.add(jObj);
                    }
                }
                // 视频信息不为空的数据进行更新操作
                if (CollUtil.isNotEmpty(resList)) {
                    String updateSql = "update t_ha_video_cloud_record set DOWNLOAD_STATUS = 1, DOWNLOAD_URL = ? where ID = ?;";
                    jdbcTemplate.update(updateSql, resList.toJSONString(), id);
                    log.info("定时上传视频录制文件，更新视频编号：id->{}", id);
                }
            }
        } catch (Exception ex) {
            log.error("定时上传视频录制文件错误：" + ex);
        }
    }

    private String getAuthorization() {
        // 填入客户 ID（customerId）和客户密钥（customerSecret）,计算 plainCredentials。
        String plainCredentials = customerId + ":" + customerSecret;
        // 填入 plainCredentials,计算 base64Credentials（使用 Base64 算法编码）。
        return "Basic " + new String(Base64.encodeBase64(plainCredentials.getBytes()));
    }
}
