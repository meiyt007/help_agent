package com.zfsoft.superwindow.controller.zzzd;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.platform.common.data.PageResult;
import com.zfsoft.superwindow.data.zzzd.PointInfo;
import com.zfsoft.superwindow.manager.zzzd.PointManager;
import com.zfsoft.superwindow.service.zzzd.PointService;
import com.zfsoft.superwindow.util.HttpReqUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @Author ChangSheng
 * @Date 9:13 2022/5/28
 * @Description 点位信息管理
 **/
@RestController
@Slf4j
public class PointController implements PointService {

    @Resource
    private PointManager pointManager;

    public static String key = "40022eb66e75fb0a5c075a0411d5553f";//个人申请
    public static String mapUrl = "https://restapi.amap.com/v3/geocode/geo";

    /**
     * 分页查询点位数据
     * @param pointInfo
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ApiResultSet<PageResult<PointInfo>> queryPointInfoPage(PointInfo pointInfo, Integer pageNum, Integer pageSize) {
        log.info("查询点位分页数据，参数：{}",pointInfo);
        PageResult<PointInfo> pageResult = pointManager.queryPointInfoWithPage(pointInfo,pageNum,pageSize);
        ApiResultSet<PageResult<PointInfo>> apiResultSet = new ApiResultSet<>();
        apiResultSet.setData(pageResult);
        return apiResultSet;
    }

    /**
     * 根据id查询点位数据
     * @param id
     * @return
     */
    @Override
    public ApiResultSet<PointInfo> getPointInfoById(String id) {
        log.info("根据id查询点位数据：{}",id);
        PointInfo pointInfo = pointManager.getPointInfoById(id);
        return new ApiResultSet<PointInfo>(pointInfo);
    }

    /**
     * 保存或修改点位信息
     * @param pointInfo
     * @return
     */
    @Override
    public ApiResultSet<PointInfo> savePointInfo(PointInfo pointInfo) {
        log.info("修改或保存点位信息");
        ApiResultSet<PointInfo> apiResultSet = new ApiResultSet<>();
        int index = pointManager.savePointInfo(pointInfo);
        if(index != 0){
            //说明有新增或修改
            apiResultSet.setCode(200);
        }else{
            apiResultSet.setCode(201);
        }
        return apiResultSet;
    }

    /**
     * 逻辑删除点位信息
     * @param id
     * @return
     */
    @Override
    public ApiResultSet<Integer> deletePointInfoById(String id) {
        log.info("删除点位信息：{}",id);
        ApiResultSet<Integer> apiResultSet = new ApiResultSet<>();
        int index = pointManager.deletePointInfoById(id);
        if(index != 0){
            //说明有新增或修改
            apiResultSet.setCode(200);
        }else{
            apiResultSet.setCode(201);
        }
        return apiResultSet;
    }

    /**
     * 根据地址获取坐标
     * @param address
     * @return
     */
    @Override
    public ApiResultSet<String> getCoordinate(String address) {
        log.info("根据地址查询坐标：{}",address);
        ApiResultSet<String> apiResultSet = new ApiResultSet<>();
        String data = "0,0";
        try {
            //拼接url
            String url = mapUrl+"?address="+address+"&key="+key;
            //请求接口
            String dataStr = HttpReqUtil.sendGet(url, "UTF-8");
            //解析返回值
            if(Strings.isNotBlank(dataStr)){
                JSONObject dataJson = JSONObject.parseObject(dataStr);
                if("1".equals(dataJson.getString("status"))){
                    //1为接口返回成功，解析获取坐标
                    JSONArray temp = dataJson.getJSONArray("geocodes");
                    data = temp.getJSONObject(0).getString("location");
                }else{
                    log.warn("接口返回失败：{}",dataJson.getString("info"));
                }
            }
        }catch (Exception e){
            log.warn("根据地址获取坐标报错：{}",e.getMessage());
        }
        apiResultSet.setData(data);
        return apiResultSet;
    }

    @Override
    public ApiResultSet<JSONArray> getCoordinateMap() {
        ApiResultSet<JSONArray> apiResultSet = new ApiResultSet<>();
        JSONArray dataList = new JSONArray();
        HashMap dataMap;
        List<PointInfo> pointList = pointManager.getAllPointInfo();
        for(PointInfo temp : pointList){
            dataMap = new HashMap();
            dataMap.put("label",temp.getId());
            dataMap.put("name",temp.getName());
            dataList.add(dataMap);
        }
        apiResultSet.setData(dataList);
        return apiResultSet;
    }
}
