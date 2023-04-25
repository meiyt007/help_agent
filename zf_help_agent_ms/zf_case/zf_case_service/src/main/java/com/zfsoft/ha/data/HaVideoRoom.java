package com.zfsoft.ha.data;

import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

import java.util.Date;

@ToString
public class HaVideoRoom {
    /** 主键 */
    @ApiModelProperty(name = "主键",notes = "")
    private Long id ;
    /** 队列主键 */
    @ApiModelProperty(name = "队列主键",notes = "")
    private Long queueOid ;
    /** 房间类型;1-办里人发起，2-帮办人员发起的 */
    @ApiModelProperty(name = "房间类型",notes = "1-办里人发起，2-帮办人员发起的")
    private String roomType ;
    /** 发起人姓名 */
    @ApiModelProperty(name = "发起人姓名",notes = "")
    private String sponsorName ;
    /** 发起主键;存的是房间出入记录表的主键 */
    @ApiModelProperty(name = "发起主键",notes = "存的是房间出入记录表的主键")
    private String sponsorId ;
    /** 发起时间 */
    @ApiModelProperty(name = "发起时间",notes = "")
    private String sponsorDate ;
    /** 房间号;生成十位唯一随机数 */
    @ApiModelProperty(name = "房间号",notes = "生成十位唯一随机数")
    private String roomNumber ;
    /** 房间名称;获取当前发起房间号的人员ID，作为房间名称 */
    @ApiModelProperty(name = "房间名称",notes = "获取当前发起房间号的人员ID，作为房间名称")
    private Long roomName ;
    /** 房间开启时间 */
    @ApiModelProperty(name = "房间开启时间",notes = "")
    private Date openDate ;
    /** 房间关闭时间 */
    @ApiModelProperty(name = "房间关闭时间",notes = "")
    private Date closDate ;
    /** 房间状态;1-开启状态、2-关闭状态 3-等待中（申请人在创建队列时是等待中)） */
    @ApiModelProperty(name = "房间状态;1-开启状态、2-关闭状态 3-等待中（申请人在创建队列时是等待中)）",notes = "")
    private String roomStatus ;
    /** 房间最高峰人数;只做递增，不做递减 */
    @ApiModelProperty(name = "房间最高峰人数",notes = "只做递增，不做递减")
    private String peakNum ;
    /** 当前房间人数，房间状态为关闭状态时，值为0 */
    @ApiModelProperty(name = "当前房间人数，房间状态为关闭状态时，值为0",notes = "")
    private String roomNum ;
    /** 创建人 */
    @ApiModelProperty(name = "创建人",notes = "")
    private String createBy ;
    /** 创建时间 */
    @ApiModelProperty(name = "创建时间",notes = "")
    private Date createDate ;
    /** 更新人 */
    @ApiModelProperty(name = "更新人",notes = "")
    private String updateBy ;
    /** 更新时间 */
    @ApiModelProperty(name = "更新时间",notes = "")
    private Date updateDate ;

    /** 主键 */
    public Long getId(){
        return this.id;
    }
    /** 主键 */
    public void setId(Long id){
        this.id=id;
    }
    /** 队列主键 */
    public Long getQueueOid(){
        return this.queueOid;
    }
    /** 队列主键 */
    public void setQueueOid(Long queueOid){
        this.queueOid=queueOid;
    }
    /** 房间类型;1-办里人发起，2-帮办人员发起的 */
    public String getRoomType(){
        return this.roomType;
    }
    /** 房间类型;1-办里人发起，2-帮办人员发起的 */
    public void setRoomType(String roomType){
        this.roomType=roomType;
    }
    /** 发起人姓名 */
    public String getSponsorName(){
        return this.sponsorName;
    }
    /** 发起人姓名 */
    public void setSponsorName(String sponsorName){
        this.sponsorName=sponsorName;
    }
    /** 发起主键;存的是房间出入记录表的主键 */
    public String getSponsorId(){
        return this.sponsorId;
    }
    /** 发起主键;存的是房间出入记录表的主键 */
    public void setSponsorId(String sponsorId){
        this.sponsorId=sponsorId;
    }
    /** 发起时间 */
    public String getSponsorDate(){
        return this.sponsorDate;
    }
    /** 发起时间 */
    public void setSponsorDate(String sponsorDate){
        this.sponsorDate=sponsorDate;
    }
    /** 房间号;生成十位唯一随机数 */
    public String getRoomNumber(){
        return this.roomNumber;
    }
    /** 房间号;生成十位唯一随机数 */
    public void setRoomNumber(String roomNumber){
        this.roomNumber=roomNumber;
    }
    /** 房间名称;获取当前发起房间号的人员ID，作为房间名称 */
    public Long getRoomName(){
        return this.roomName;
    }
    /** 房间名称;获取当前发起房间号的人员ID，作为房间名称 */
    public void setRoomName(Long roomName){
        this.roomName=roomName;
    }
    /** 房间开启时间 */
    public Date getOpenDate(){
        return this.openDate;
    }
    /** 房间开启时间 */
    public void setOpenDate(Date openDate){
        this.openDate=openDate;
    }
    /** 房间关闭时间 */
    public Date getClosDate(){
        return this.closDate;
    }
    /** 房间关闭时间 */
    public void setClosDate(Date closDate){
        this.closDate=closDate;
    }
    /** 房间状态;1-开启状态、2-关闭状态 3-等待中（申请人在创建队列时是等待中)） */
    public String getRoomStatus(){
        return this.roomStatus;
    }
    /** 房间状态;1-开启状态、2-关闭状态 3-等待中（申请人在创建队列时是等待中)） */
    public void setRoomStatus(String roomStatus){
        this.roomStatus=roomStatus;
    }
    /** 房间最高峰人数;只做递增，不做递减 */
    public String getPeakNum(){
        return this.peakNum;
    }
    /** 房间最高峰人数;只做递增，不做递减 */
    public void setPeakNum(String peakNum){
        this.peakNum=peakNum;
    }
    /** 当前房间人数，房间状态为关闭状态时，值为0 */
    public String getRoomNum(){
        return this.roomNum;
    }
    /** 当前房间人数，房间状态为关闭状态时，值为0 */
    public void setRoomNum(String roomNum){
        this.roomNum=roomNum;
    }
    /** 创建人 */
    public String getCreateBy(){
        return this.createBy;
    }
    /** 创建人 */
    public void setCreateBy(String createBy){
        this.createBy=createBy;
    }
    /** 创建时间 */
    public Date getCreateDate(){
        return this.createDate;
    }
    /** 创建时间 */
    public void setCreateDate(Date createDate){
        this.createDate=createDate;
    }
    /** 更新人 */
    public String getUpdateBy(){
        return this.updateBy;
    }
    /** 更新人 */
    public void setUpdateBy(String updateBy){
        this.updateBy=updateBy;
    }
    /** 更新时间 */
    public Date getUpdateDate(){
        return this.updateDate;
    }
    /** 更新时间 */
    public void setUpdateDate(Date updateDate){
        this.updateDate=updateDate;
    }
}
