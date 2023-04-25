package com.zfsoft.ha.xindianResponse;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Description //事项情形列表-新点
 * @Author: Wangyh
 * @Date: 2023/1/28 13:40
 */
public class ServicerGuideVo {
    /**
     * 事项主键
     */
    private String rowguid;
    /**
     * 事项名称
     */
    private String  qlName;

    /**
     * 实施主体
     */
    private String  bljg;

    /**
     * 办件类型
     */
    private Short  bjtype;

    /**
     * 法定时限
     */
    private Long anticipateDay;
    /**
     * 承诺时限
     */
    private Long promiseDay;

    /**
     * 办理时间
     */
    private String ckjssj;

    /**
     * 窗口咨询
     */
    private String  linkCk;

    /**
     *  网上咨询
     */
    private String linkWs;

    /**
     * 信函咨询
     */
    private String linkXh ;

    /**
     * 电话咨询(咨询方式)
     */
    private String linkTel;

    /**
     * 电子邮件咨询
     */
    private String linkYj;

    /**
     * 事项类型
     */
    private String qlkind;

    /**
     *  组织机构主键 部门编码
     */
    private String ouguid;
    /**
     *  部门主键
     */
    private String organOid;
    /**
     *  部门名称
     */
    private String organName;

    /**
     *  业务办理项编码
     */
    private String itemCode;

    /**
     *  事项编码 （12位万达要的）
     */
    private String catalogCode;
    /**
     *  实施主体编码
     */
    private String orgCode;

    /**
     *  事项版本唯一值
     */
    private String itemId;
    /**
     *  排序
     */
    private String ordernum;

    /**
     *  对应国家事项基本编码（12位
     */
    private String codeG;
    /**
     *  对应国家实施编码（33位）
     */
    private String itemcodeG;

    /**
     * 材料列表
     * @return
     */
    private List<MatterVo> matterVoList;

    /**
     * 流程图
     */
    private List<FlowChartVo> flow_chart;

    public List<FlowChartVo> getFlow_chart() {
        return flow_chart;
    }

    public void setFlow_chart(List<FlowChartVo> flow_chart) {
        this.flow_chart = flow_chart;
    }

    public String getCatalogCode() {
        return catalogCode;
    }

    public void setCatalogCode(String catalogCode) {
        this.catalogCode = catalogCode;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public List<MatterVo> getMatterVoList() {
        return matterVoList;
    }

    public void setMatterVoList(List<MatterVo> matterVoList) {
        this.matterVoList = matterVoList;
    }

    public String getCodeG() {
        return codeG;
    }

    public void setCodeG(String codeG) {
        this.codeG = codeG;
    }

    public String getItemcodeG() {
        return itemcodeG;
    }

    public void setItemcodeG(String itemcodeG) {
        this.itemcodeG = itemcodeG;
    }

    public String getRowguid() {
        return rowguid;
    }

    public void setRowguid(String rowguid) {
        this.rowguid = rowguid;
    }

    public String getQlName() {
        return qlName;
    }

    public void setQlName(String qlName) {
        this.qlName = qlName;
    }

    public String getBljg() {
        return bljg;
    }

    public void setBljg(String bljg) {
        this.bljg = bljg;
    }

    public Short getBjtype() {
        return bjtype;
    }

    public void setBjtype(Short bjtype) {
        this.bjtype = bjtype;
    }

    public Long getAnticipateDay() {
        return anticipateDay;
    }

    public void setAnticipateDay(Long anticipateDay) {
        this.anticipateDay = anticipateDay;
    }

    public Long getPromiseDay() {
        return promiseDay;
    }

    public void setPromiseDay(Long promiseDay) {
        this.promiseDay = promiseDay;
    }

    public String getCkjssj() {
        return ckjssj;
    }

    public void setCkjssj(String ckjssj) {
        this.ckjssj = ckjssj;
    }

    public String getLinkCk() {
        return linkCk;
    }

    public void setLinkCk(String linkCk) {
        this.linkCk = linkCk;
    }

    public String getLinkWs() {
        return linkWs;
    }

    public void setLinkWs(String linkWs) {
        this.linkWs = linkWs;
    }

    public String getLinkXh() {
        return linkXh;
    }

    public void setLinkXh(String linkXh) {
        this.linkXh = linkXh;
    }

    public String getLinkTel() {
        return linkTel;
    }

    public void setLinkTel(String linkTel) {
        this.linkTel = linkTel;
    }

    public String getLinkYj() {
        return linkYj;
    }

    public void setLinkYj(String linkYj) {
        this.linkYj = linkYj;
    }

    public String getQlkind() {
        return qlkind;
    }

    public void setQlkind(String qlkind) {
        this.qlkind = qlkind;
    }

    public String getOuguid() {
        return ouguid;
    }

    public void setOuguid(String ouguid) {
        this.ouguid = ouguid;
    }

    public String getOrganOid() {
        return organOid;
    }

    public void setOrganOid(String organOid) {
        this.organOid = organOid;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    @Override
    public String toString() {
        return "ServicerGuideVo{" +
                "rowguid='" + rowguid + '\'' +
                ", qlName='" + qlName + '\'' +
                ", bljg='" + bljg + '\'' +
                ", bjtype=" + bjtype +
                ", anticipateDay=" + anticipateDay +
                ", promiseDay=" + promiseDay +
                ", ckjssj='" + ckjssj + '\'' +
                ", linkCk='" + linkCk + '\'' +
                ", linkWs='" + linkWs + '\'' +
                ", linkXh='" + linkXh + '\'' +
                ", linkTel='" + linkTel + '\'' +
                ", linkYj='" + linkYj + '\'' +
                ", qlkind='" + qlkind + '\'' +
                ", ouguid='" + ouguid + '\'' +
                ", organOid='" + organOid + '\'' +
                ", organName='" + organName + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", itemId='" + itemId + '\'' +
                ", ordernum='" + ordernum + '\'' +
                '}';
    }
}

