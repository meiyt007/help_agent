package com.zfsoft.outer.inter.pojo;

import lombok.Data;

/**
 * @author zhaobf
 * @version 1.0
 * @date 2022/9/22 9:20
 */
@Data
public class ScanParam {
    private String qrcode;
    private String sn;
    private String pos;
    private String use;

    public String getQrcode() {
        return qrcode;
    }

    /**
     * 为扫码枪扫描二维码得到的数据
     *
     * @param qrcode
     */
    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getSn() {
        return sn;
    }

    /**
     * 流水号
     *
     * @param sn
     */
    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getPos() {
        return pos;
    }

    /**
     * 亮证地址
     *
     * @return
     */
    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getUse() {
        return use;
    }

    /**
     * 亮证事项
     *
     * @param use
     */
    public void setUse(String use) {
        this.use = use;
    }
}
