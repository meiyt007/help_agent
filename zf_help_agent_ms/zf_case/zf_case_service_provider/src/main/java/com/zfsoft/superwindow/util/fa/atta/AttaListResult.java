package com.zfsoft.superwindow.util.fa.atta;

import com.zfsoft.cases.data.QlSysAtta;

import java.util.List;

/**
 * 获取附件信息请求结果类，多个附件信息
 *
 * @author gaolh
 * @date 2016-11-21 13:50:03
 */
public class AttaListResult extends AttaResult {
    private static final long serialVersionUID = -877998052475468314L;

    /**
     * 请求结果，附件集合
     */
    private List<QlSysAtta> result;

    public List<QlSysAtta> getResult() {
        return result;
    }

    public void setResult(List<QlSysAtta> result) {
        this.result = result;
    }
}
