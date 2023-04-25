package com.zfsoft.single.controller.sxpz;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.single.data.yxpz.SxMaterialBill;
import com.zfsoft.single.data.yxpz.vo.SxMaterialBillVo;
import com.zfsoft.single.manager.sxpz.SxMaterialBillManager;
import com.zfsoft.single.service.sxpz.SxMaterialBillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName PbpjManageController
 * @Description 证照目录的实现类
 * @Author liangxm
 * @Date 2020-11-06 10:30
 * @Version V1.0
 **/
@RestController
@Slf4j
public class SxMaterialBillController implements SxMaterialBillService {
    @Resource
    private SxMaterialBillManager sxMaterialBillManager;

    @Override
    public ApiResultSet<List<SxMaterialBillVo>> querySxMaterialBill() {

        List<SxMaterialBillVo> sysConfigList = sxMaterialBillManager.querySxMaterialBill();
        return new ApiResultSet(sysConfigList);
    }

    @Override
    public ApiResultSet<SxMaterialBill> getSxMaterialElmsConfigByoId(String oid) {
        SxMaterialBill sxMaterialBill = sxMaterialBillManager.getSxMaterialBillByMaterOid(oid);
        return new ApiResultSet(sxMaterialBill);
    }

    @Override
    public ApiResultSet<SxMaterialBill> getSxMaterialBillByMaterOid(String billOid) {
        SxMaterialBill sxMaterialBill = sxMaterialBillManager.getSxMaterialBillByMaterOid(billOid);
        return new ApiResultSet(sxMaterialBill);
    }
}
