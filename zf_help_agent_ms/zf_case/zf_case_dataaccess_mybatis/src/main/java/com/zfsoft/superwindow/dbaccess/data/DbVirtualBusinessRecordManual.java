package com.zfsoft.superwindow.dbaccess.data;

public class DbVirtualBusinessRecordManual extends DbVirtualBusinessRecord {
    /**
     * 业务主键
     */
    private String maOid;
    /**
     * 服务评价
     */
    private String serviceEvaluation;
    /**
     * 是否推送
     */
    private Integer pushFlag;

    public String getMaOid() {
        return maOid;
    }

    public void setMaOid(String maOid) {
        this.maOid = maOid;
    }

    public String getServiceEvaluation() {
        return serviceEvaluation;
    }

    public void setServiceEvaluation(String serviceEvaluation) {
        this.serviceEvaluation = serviceEvaluation;
    }

    public Integer getPushFlag() {
        return pushFlag;
    }

    public void setPushFlag(Integer pushFlag) {
        this.pushFlag = pushFlag;
    }
}
