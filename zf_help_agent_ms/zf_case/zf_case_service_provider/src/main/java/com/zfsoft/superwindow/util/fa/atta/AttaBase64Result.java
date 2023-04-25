package com.zfsoft.superwindow.util.fa.atta;


/**
 * 获取附件信息请求结果类，单个附件信息
 *
 * @author gaolh
 * @date 2016-11-21 13:50:03
 */
public class AttaBase64Result extends AttaResult {
    private static final long serialVersionUID = -877998052475468314L;

    /**
     * 文件的Base64字符串
     */
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
