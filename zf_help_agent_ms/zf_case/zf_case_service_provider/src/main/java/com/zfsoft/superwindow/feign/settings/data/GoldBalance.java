package com.zfsoft.superwindow.feign.settings.data;

/**
 * @Author ChangSheng
 * @Date 14:48 2022/6/24
 * @Description 公积金余额
 **/
public class GoldBalance {

    //个人账号余额
    private String grzhye;

    //利息
    private String interest;

    //人名
    private String name;

    //证件号
    private String cardNumber;

    //公积金账号
    private String grzh;

    //总提取额
    private String total;

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getGrzh() {
        return grzh;
    }

    public void setGrzh(String grzh) {
        this.grzh = grzh;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getGrzhye() {
        return grzhye;
    }

    public void setGrzhye(String grzhye) {
        this.grzhye = grzhye;
    }
}
