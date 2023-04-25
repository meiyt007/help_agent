package com.zfsoft.single.service.insideapi;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.single.data.ywbl.CaseMaterialOutOfStock;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/appService")
public interface IAppService {



    @RequestMapping(value = "/getOneMaterialTypeInfo", method = {RequestMethod.POST})
    ApiResultSet getOneMaterialTypeInfo(@RequestParam(value = "id", required = false) String id);

    /***
    * @Description:  根据id获取出库信息
    * @Author:liangss
    * @Date:2022/1/7
    * @Param: [id]
    */
    @RequestMapping(value = "/getOneMaterialInfo", method = {RequestMethod.POST})
    ApiResultSet getOneMaterialInfo(@RequestParam(value = "id", required = false) String id);


    /***
    * @Description: 根据id查询流转记录
    * @Author:liangss
    * @Date:2022/1/7
    * @Param: [id]
    */
    @RequestMapping(value = "/getCaseMaterialOutOfStockRecordById", method = {RequestMethod.POST})
    ApiResultSet getCaseMaterialOutOfStockRecordById(@RequestParam(value = "id", required = false) String id);

    /***
    * @Description:  保存更新出库信息
    * @Author:liangss
    * @Date:2022/1/7
    * @Param: [caseMaterialOutOfStock]
    */
    @RequestMapping(value = "/saveMaterialOutInfo", method = {RequestMethod.POST})
    ApiResultSet saveMaterialOutInfo(@RequestBody CaseMaterialOutOfStock caseMaterialOutOfStock);

    /***
    * @Description:  出库签收
    * @Author:liangss
    * @Date:2022/1/7
    * @Param: [caseMaterialOutOfStock]
    */
    @RequestMapping(value = "/signMaterialOutInfo", method = {RequestMethod.POST})
    ApiResultSet signMaterialOutInfo(@RequestBody CaseMaterialOutOfStock caseMaterialOutOfStock);



   /* @ProcessFeignCalledResult
    @RequestMapping(value = {"/uploadAppFile"}, method = {RequestMethod.POST} , consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ApiResultSet uploadAppFile(MultipartFile[] files);*/


    @RequestMapping(value = {"/uploadAppFile"}, method = {RequestMethod.POST})
    ApiResultSet uploadAppFile(@RequestParam("file") MultipartFile[] files);


}
