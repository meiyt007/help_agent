package com.zfsoft.single.util.fa.atta;

import com.alibaba.fastjson.JSON;
import com.zfsoft.cases.data.QlSysAtta;

/**
 * 获取附件信息请求结果类，单个附件信息
 *
 * @author gaolh
 * @date 2016-11-21 13:50:03
 */
public class AttaInfoResult extends AttaResult {
    private static final long serialVersionUID = -877998052475468314L;

    /**
     * 请求结果
     */
    private QlSysAtta result;

    public QlSysAtta getResult() {
        return result;
    }

    /**
     * 返回附件结果的json字符串
     *
     * @return 附件结果的json字符串
     */
    public String getResultJsonString() {
        return JSON.toJSONString(result);
    }

    public void setResult(QlSysAtta result) {
        this.result = result;
    }
}
