// 窗口取号
<template>
  <div class="intelligentFormFilling-container window-container">
        <div v-loading="loadingData" style="width: 100%;height: calc(100% - 10rem);" >
            <div class="tabHeader">
            <p
                class="tabLeft"
                @click="changeTab('1')"
                :class="tabType === '1' ? 'active' : ''"
            >
                标准取号
            </p>
            <p
                class="tabRight"
                @click="changeTab('2')"
                :class="tabType === '2' ? 'active' : ''"
            >
                指定取号
            </p>
            </div>
            <div class="body-content" style="overflow-y: scroll;">
            <div class="tab" ref="tab1" v-show="tabType === '1'">
                <div class="table-title">请选择业务分区：</div>
                <div class="table-btn label3" >
                    <el-radio  :class="list1Group.cateGoryId == data.cateGoryId?'is-focus':''" @change="list1Change(data)" v-model="list1Group" :key="data.cateGoryId" v-for="data in list1" :label="data"
                    > {{ data.cateGoryName }}</el-radio >
                </div>
                <div class="label-box" v-if="list2.length>0">
                    <div class="table-btn label1">
                            <el-radio 
                            :class="list2Group.cateGoryId == data.cateGoryId?'is-focus':''"  
                            @change="list2Change(data)" v-model="list2Group" 
                            :key="data.cateGoryId" v-for="data in list2" 
                            :label="data.cateGoryName"></el-radio>
                    </div>
                    <div class="table-btn label2" v-if="list3.length>0">
                        <el-radio 
                        :class="list3Group.cateGoryId == data.cateGoryId?'is-focus':''"
                            @change="list3Change(data)" v-model="list3Group" 
                            :key="data.cateGoryId" v-for="data in list3" 
                            :label="data.cateGoryName"></el-radio>
                    </div>
                    <div class="table-btn label2" v-if="list4.length>0">
                        <el-radio 
                        :class="list4Group.cateGoryId == data.cateGoryId?'is-focus':''"
                            @change="list4Change(data)" v-model="list4Group" 
                            :key="data.cateGoryId" v-for="data in list4" 
                            :label="data.cateGoryName"></el-radio>
                    </div>
                    <div class="table-btn label2" v-if="list5.length>0">
                        <el-radio 
                        :class="list5Group.cateGoryId == data.cateGoryId?'is-focus':''"
                            @change="list5Change(data)" v-model="list5Group" 
                            :key="data.cateGoryId" v-for="data in list5" 
                            :label="data.cateGoryName"></el-radio>
                    </div>
                    <div class="table-btn label2" v-if="list6.length>0">
                        <el-radio 
                        :class="list6Group.cateGoryId == data.cateGoryId?'is-focus':''"
                            @change="list6Change(data)" v-model="list6Group" 
                            :key="data.cateGoryId" v-for="data in list6" 
                            :label="data.cateGoryName"></el-radio>
                    </div>
                </div>
            </div>
            <div class="tab" ref="tab2" v-show="tabType === '2'">
                <div class="table-title">请选择业务分区：</div>
                <div class="table-btn label3" >
                    <el-radio  :class="list1Group.cateGoryId == data.cateGoryId?'is-focus':''" @change="list1Change(data)" v-model="list1Group" :key="data.cateGoryId" v-for="data in list1" :label="data"
                    > {{ data.cateGoryName }}</el-radio >
                </div>
                <div class="label-box" v-if="list2.length>0">
                    <div class="table-btn label1">
                            <el-radio 
                            :class="list2Group.cateGoryId == data.cateGoryId?'is-focus':''"  
                            @change="list2Change(data)" v-model="list2Group" 
                            :key="data.cateGoryId" v-for="data in list2" 
                            :label="data.cateGoryName"></el-radio>
                    </div>
                    <div class="table-btn label2" v-if="list3.length>0">
                        <el-radio 
                        :class="list3Group.cateGoryId == data.cateGoryId?'is-focus':''"
                            @change="list3Change(data)" v-model="list3Group" 
                            :key="data.cateGoryId" v-for="data in list3" 
                            :label="data.cateGoryName"></el-radio>
                    </div>
                    <div class="table-btn label2" v-if="list4.length>0">
                        <el-radio 
                        :class="list4Group.cateGoryId == data.cateGoryId?'is-focus':''"
                            @change="list4Change(data)" v-model="list4Group" 
                            :key="data.cateGoryId" v-for="data in list4" 
                            :label="data.cateGoryName"></el-radio>
                    </div>
                    <div class="table-btn label2" v-if="list5.length>0">
                        <el-radio 
                        :class="list5Group.cateGoryId == data.cateGoryId?'is-focus':''"
                            @change="list5Change(data)" v-model="list5Group" 
                            :key="data.cateGoryId" v-for="data in list5" 
                            :label="data.cateGoryName"></el-radio>
                    </div>
                     <div class="table-btn label2" v-if="list6.length>0">
                        <el-radio 
                        :class="list6Group.cateGoryId == data.cateGoryId?'is-focus':''"
                            @change="list6Change(data)" v-model="list6Group" 
                            :key="data.cateGoryId" v-for="data in list6" 
                            :label="data.cateGoryName"></el-radio>
                    </div>
                </div>
            </div>
            </div>
        </div>
        <div class="dialog-footer center">
            <p @click="cancelDialog">取消</p>
            <p @click="serviceTurn">普通号</p>
            <p @click="takeBooking">预约号</p>
        </div>
  </div>
</template>
<script>
import {getWandaCatalogue,takeNumber,getAppointCatalogue } from "@/api/modules/windowGetNum";
import {takePriorityNumber,winNumbPush} from "@/api/modules/helpAgent";
import { sendHPSms } from "@/api/modules/business.js";
export default {
    name: "QueueCondition",
    props: {
        baseUserInfo: {
          type: Object,
          default: () => {},
        },
        // currentServiceIndex: {
        //   type: Number,
        //   default: () => 0,
        // },
        // showServiceTransfer: {
        //   type: Boolean,
        //   default: () => false,
        // },
    },
    data() {
        return {
            loadingData: false,
            tabType: "1",
            list1: [],
            list1Group:{
                cateGoryId:'',
                cateGoryName:''
            },
            list2: [],
            list2Group:{
                cateGoryId:'',
                cateGoryName:''
            },
            list3: [],
            list3Group:{
                cateGoryId:'',
                cateGoryName:''
            },
            list4: [],
            list4Group:{
                cateGoryId:'',
                cateGoryName:''
            },
            list5: [],
            list5Group:{
                cateGoryId:'',
                cateGoryName:''
            },
            list6: [],
            list6Group:{
                cateGoryId:'',
                cateGoryName:''
            },
            cateGoryId:'',
        };
    },
    computed: {
    },
    mounted() {
        this.getNormalList();
    },
    methods: {
        list1Change(val){
            this.clearChange();
            this.list1Group ={
                cateGoryId: val.cateGoryId,
                cateGoryName:val.cateGoryName
            }
            this.cateGoryId = val.cateGoryId;
            if(val.childMachineCategory !== null){
                this.list2 = val.childMachineCategory;
            }
        },
        list2Change(val){
            this.list3 = []
            this.list3Group = [{
                cateGoryId:'',
                cateGoryName:''
            }]; 
            this.list4 = []
            this.list4Group = [{
                cateGoryId:'',
                cateGoryName:''
            }]; 
            this.list5 = []
            this.list5Group = [{
                cateGoryId:'',
                cateGoryName:''
            }]; 
            this.list6 = []
            this.list6Group = [{
                cateGoryId:'',
                cateGoryName:''
            }]; 
            this.list2Group ={
                cateGoryId: val.cateGoryId,
                cateGoryName:val.cateGoryName
            }
            this.cateGoryId = val.cateGoryId;
            if(val.childMachineCategory !== null){
                this.list3 = val.childMachineCategory;
            }
        },
        list3Change(val){
            this.list4 = []
            this.list4Group = [{
                cateGoryId:'',
                cateGoryName:''
            }]; 
            this.list5 = []
            this.list5Group = [{
                cateGoryId:'',
                cateGoryName:''
            }]; 
             this.list6 = []
            this.list6Group = [{
                cateGoryId:'',
                cateGoryName:''
            }]; 
             this.list3Group ={
                cateGoryId: val.cateGoryId,
                cateGoryName:val.cateGoryName
            }
            this.cateGoryId = val.cateGoryId;
             if(val.childMachineCategory !== null){
                this.list4 = val.childMachineCategory;
            }
        },
        list4Change(val){
            this.list5 = []
            this.list5Group = [{
                cateGoryId:'',
                cateGoryName:''
            }]; 
             this.list6 = []
            this.list6Group = [{
                cateGoryId:'',
                cateGoryName:''
            }]; 
             this.list4Group ={
                cateGoryId: val.cateGoryId,
                cateGoryName:val.cateGoryName
            }
            this.cateGoryId = val.cateGoryId;
             if(val.childMachineCategory !== null){
                this.list5 = val.childMachineCategory;
            }
        },
        list5Change(val){
             this.list6 = []
            this.list6Group = [{
                cateGoryId:'',
                cateGoryName:''
            }]; 
             this.list5Group ={
                cateGoryId: val.cateGoryId,
                cateGoryName:val.cateGoryName
            }
            this.cateGoryId = val.cateGoryId;
            if(val.childMachineCategory !== null){
                this.list6 = val.childMachineCategory;
            }
        },
        list6Change(val){
             this.list6Group ={
                cateGoryId: val.cateGoryId,
                cateGoryName:val.cateGoryName
            }
            this.cateGoryId = val.cateGoryId;
        },
        clearChange(){
            this.list1Group = [{
                cateGoryId:'',
                cateGoryName:''
            }]; 
                this.list2 = []
                this.list2Group = [{
                cateGoryId:'',
                cateGoryName:''
            }];  
                this.list3 = []
                this.list3Group = [{
                cateGoryId:'',
                cateGoryName:''
            }]; 
                this.list4 = []
                this.list4Group = [{
                cateGoryId:'',
                cateGoryName:''
            }]; 
                this.list5 = []
                this.list5Group = [{
                cateGoryId:'',
                cateGoryName:''
            }]; 
                this.list6 = []
                this.list6Group = [{
                cateGoryId:'',
                cateGoryName:''
            }]; 
        },

        // 获取标准分区
        getNormalList() {
            this.loadingData = true;
            getWandaCatalogue().then((res) => {
                this.loadingData = false;
                if (res.code === 200) {
                    this.list1 = res.data[0].childMachineCategory;
                }
            })
            .catch((err) => {
                console.log(err);
                this.loadingData = false;
            });
        },
        // 获取指定分区
        getSpecificList(){
            this.loadingData = true;
            getAppointCatalogue()
                .then((res) => {
                    this.loadingData = false;
                    if (res.code === 200) {
                        this.list1 = res.data;
                    }
            }).catch((err) => {
                console.log(err);
                this.loadingData = false;
            });
        },
        // 切换取号模式
        changeTab(type) {
            this.clearChange();
            this.tabType = type;
            if(type == '1'){
                this.getNormalList();
                return
            }
            if(type == '2'){
                this.getSpecificList();
                return
            }
        },
        // 关闭取号弹窗
        cancelDialog() {
            this.clearChange();
            this.list1 = [];
            this.tabType = '1'
            this.$emit("closeWindow");
        },
        // 检查是否没有选择标签
        checkChang(){
            let that = this;
            if(!that.list1Group.cateGoryId){
                that.$message.warning("请选择分区");
                return false;
            }else{
                if( that.list2.length>0 && !that.list2Group.cateGoryId){
                    that.$message.warning("请选择子分区1");
                    return false;
                }else{
                    if( that.list3.length>0 && !that.list3Group.cateGoryId){
                        that.$message.warning("请选择子分区2");
                        return false;
                    }else{
                        if( that.list4.length>0 && !that.list4Group.cateGoryId){
                            that.$message.warning("请选择子分区3");
                            return false;
                        }else{
                            if( that.list5.length>0 && !that.list5Group.cateGoryId){
                                that.$message.warning("请选择子分区4");
                                return false;
                            }else{
                                 if( that.list6.length>0 && !that.list6Group.cateGoryId){
                                    that.$message.warning("请选择子分区5");
                                    return false;
                                }else{
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        },
        // 预约取号
        takeBooking(){
            if(this.checkChang()){
                let params = {
                    queueId: this.baseUserInfo.id ?? this.baseUserInfo.queueId,
                    username:this.baseUserInfo.name,
                    mobile:this.baseUserInfo.phone,
                    certno:this.baseUserInfo.cardNo ?? this.baseUserInfo,
                    hallCode:'',
                    clintId:'',
                    groupId:'',
                    cateGoryId:this.cateGoryId,
                }
                this.loadingData = true;
                console.log(params,"0000")
                takePriorityNumber(params).then((res)=>{
                    if (res.code === 200) {
                        this.loadingData = false;
                        let dataObj = res.data.tickerMsg;
                        dataObj['name'] = that.baseUserInfo.name;
                        dataObj['cardNo'] = that.baseUserInfo.cardNo;
                        dataObj['companyCode'] = '';
                        dataObj['companyName'] = '';
                        that.$alert('<div>取号成功,您的窗口号票为：'+ res.data.tickerMsg.stNumber +'</div><div>备注：'+res.data.tickerMsg.stDesc+'</div>', '', {
                            dangerouslyUseHTMLString: true,
                            center: true
                        });
                        //this.sendHPSms("N0001","二楼c01");
                        that.sendHPSms(res.data.tickerMsg.stNumber,res.data.tickerMsg.stDesc);
                        // 号票推送
                        winNumbPush(dataObj).then(list =>{})
                        that.$message.success("取号成功！");
                        this.cancelDialog();
                    }
                })
                .catch((err) => {
                    console.log(err);
                });
            }
        },
        //普通取号
        serviceTurn() {
            if(this.checkChang()){
                let that = this;
                let params  = {
                    queueId: this.baseUserInfo.id ?? this.baseUserInfo.queueId,
                    username:this.baseUserInfo.name,
                    mobile:this.baseUserInfo.phone,
                    certno:this.baseUserInfo.cardNo,
                    hallCode:'',
                    clintId:'',
                    groupId:'',
                    cateGoryId:this.cateGoryId,
                }
                this.loadingData = true;
                takeNumber(JSON.stringify(params)).then(res=>{
                    if(res.code == 200){
                        this.loadingData = false;
                        let dataObj = res.data.tickerMsg;
                        dataObj['name'] = that.baseUserInfo.name;
                        dataObj['cardNo'] = that.baseUserInfo.cardNo;
                        dataObj['companyCode'] = '';
                        dataObj['companyName'] = '';
                        that.$alert('<div>取号成功,您的窗口号票为：'+ res.data.tickerMsg.stNumber +'</div><div>备注：'+res.data.tickerMsg.stDesc+'</div>', '', {
                            dangerouslyUseHTMLString: true,
                            center: true
                        });
                        //this.sendHPSms("N0001","二楼c01");
                        that.sendHPSms(res.data.tickerMsg.stNumber,res.data.tickerMsg.stDesc);
                        // 号票推送
                        winNumbPush(dataObj).then(list =>{})
                        that.$message.success("取号成功！");
                        this.cancelDialog();
                    }else{
                        that.$message.warning("服务器故障！");
                    }
                }).catch((err) => {});
            }
        },
        // 发送短信
        sendHPSms(stNumber,stDesc) {
            const data = {
                title: "窗口取号提醒",
                phone: this.baseUserInfo.phone,
                message: this.baseUserInfo.name+"您好，您的窗口号票为"+stNumber+"，备注："+stDesc+"。",
                workUserId: this.basicUserInfo.id,
                workUserName: this.basicUserInfo.name,
            };
            sendHPSms(data);
        },
    },
 
};
</script>
<style lang="scss" scoped>
    @import './css/serviceTransfer.scss';
    .window-container .dialog-footer {
        justify-content: center !important;
        p {
            &:nth-child(1) {
                cursor: pointer;
                padding: 0 4.75rem;
                width: auto;
                height: 5.1667rem;
                background: #ffffff;
                border: 1px solid #4988f2;
                box-shadow: 0px 0px 1.5rem 0px rgb(40 107 198 / 31%);
                border-radius: 2.5833rem;
                font-size: 1.8333rem;
                font-family: Microsoft YaHei;
                font-weight: 400;
                color: #2473ff;
                display: flex;
                align-items: center;
                justify-content: center;
                margin-right: 2.1875rem;
            }
            &:nth-child(2) {
                cursor: pointer;
                padding: 0 4.75rem;
                width: auto;
                height: 5.1667rem;
                background: #ffffff;
                box-shadow: 0px 0px 1.5rem 0px rgb(40 107 198 / 31%);
                border-radius: 2.5833rem;
                font-size: 1.8333rem;
                font-family: Microsoft YaHei;
                font-weight: 400;
                color: #2473ff;
                display: flex;
                align-items: center;
                justify-content: center;
                margin-right: 2.1875rem;
                color: #ffffff !important;
                background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%) !important;
                box-shadow: 0px 0px 1.5rem 0px rgb(40 107 198 / 31%) !important;
            }
            &:nth-child(3) {
                cursor: pointer;
                padding: 0 4.75rem;
                width: auto;
                height: 5.1667rem;
                background: #ffffff;
                box-shadow: 0px 0px 1.5rem 0px rgb(40 107 198 / 31%);
                border-radius: 2.5833rem;
                font-size: 1.8333rem;
                font-family: Microsoft YaHei;
                font-weight: 400;
                color: #2473ff;
                display: flex;
                align-items: center;
                justify-content: center;
                color: #ffffff !important;
                background: linear-gradient(90deg, #56b1fd 0%, #56fda9 100%) !important;
                box-shadow: 0px 0px 1.5rem 0px rgb(40 107 198 / 31%) !important;
            }
        }
    }
    .table-btn{
        display: flex;
        flex-wrap: wrap;
    }
    .el-radio {
        width:calc((100% - 7.875rem)/5);
        height: 3.9375rem ;
        border: 1px solid #afafaf;
        display: flex;
        align-items: center;
        justify-content: center;
        background-color: rgba(214, 214, 214, 0.521) !important;
        font-size: 1.5rem;
        font-family: Source Han Sans CN;
        font-weight: 500;
        color: rgb(56, 49, 49);
        margin-right: 2.875rem;
        margin-bottom: 1.5rem;
        border-radius: 20px !important;
        box-shadow: -5px 5px 3px -1px #9faebd;
        ::v-deep .el-radio__inner {
            display: inline-block;
            width: 1.625rem;
            height: 1.625rem;
            margin-right: 5px;
            background: url("@/assets/images/pad/choose.png") no-repeat;
            background-size: 100% 100%;
        }
        span {
            color: #3f3f3f;
        }
    }
     ::v-deep .is-focus .el-radio__inner {
        background: url("@/assets/images/pad/chooseActivate.png") no-repeat;
        background-size: 100% 100%;

        &:after {
        display: none;
        }
    }

    .label-box{
        padding: 10px 20px 0 20px;
        background-color:#f5f9fd ;
    }
    .label3{
        ::v-deep .table-btn .el-radio.is-checked,.is-focus{
            border: 1px solid rgb(170, 170, 170);
            background: linear-gradient(90deg, #7f5ef8 0%, #3aa0f3 100%) !important;
            color: #fff !important;
            span{
                color: #fff !important;
            }
        }
    }
    .label1,.label2{
        .el-radio {
            width:calc((100% - 6rem)/4);
            height: 3.9375rem;
            border: 1px solid #c8cdd3;
            display: flex;
            align-items: center;
            justify-content: center;
            background-color: transparent !important;
            font-size: 1.5rem;
            font-family: Source Han Sans CN;
            font-weight: 500;
            color: rgb(56, 49, 49);
            margin-right: 1rem;
            margin-bottom: 1.5rem;
            box-shadow: none;
            
            span {
                color: #3f3f3f;
            }
        }
        ::v-deep .el-radio-button__inner{
            width: 100% !important;
        }

         ::v-deep .table-btn .el-radio.is-checked,.is-focus{
            color: #fff;
            // border: 1px solid #2473ff;
             background: linear-gradient(90deg, #7f5ef8 0%, #3aa0f3 100%) !important;
        }
    }
           
    .label1{
        margin-bottom: 10px;
    }
    .label2{
        padding:20px 70px;
        border-top: 1px rgb(201, 201, 201) dashed;
        .el-radio-button {
            width:calc((100% - 6rem)/4);
        }
    }
   .tabHeader{
       height: 4.8rem !important;
       background-color: white !important;
        box-shadow: 0 0px 7px 1px rgba($color: #93b7cf, $alpha: 0.5) inset; 
        padding:5px;
        .active{
            background: linear-gradient(90deg, #7755f1 0%, #3aa0f3 100%) !important;
        }
   }

</style>
  