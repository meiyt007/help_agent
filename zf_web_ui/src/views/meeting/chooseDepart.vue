<template>
    <div class="depart-container">
        <div class="content">
            <el-radio-group v-if="this.groupType == '1'" v-model="tableradioGroup" class="chooseOption">
                <el-radio
                        :key="data.id" name="group1"  @change="changeService(data)" v-for="data in list" :label="data"
                            >{{ data.name  }}
                </el-radio>
            </el-radio-group>
            <el-radio-group v-else-if="this.groupType == '2'" v-model="tableradioGroup" class="chooseOption">
                <el-radio
                        :key="data.id" name="group1"  @change="changeService(data)" v-for="data in list" :label="data"
                            >{{ data.name  }}
                </el-radio>
            </el-radio-group>
            <el-radio-group v-else class="chooseOption">
                <el-radio
                        :key="data.groupId" name="group2"  @change="unitService(data)" v-for="data in list" :label="data.groupId"
                            >{{ data.groupName  }}
                </el-radio>
            </el-radio-group>
        </div>    
        <div class="dialog-footer center" v-if="operateShow">
            <p @click="cancelDialog">取消</p>
            <p @click="serviceTurn">确定</p>
      </div>
      <el-dialog
        title="选择工作人员"
        :visible.sync="workerShow"
        width="60%"
        class="window-number"
        custom-class="serviceTransfer"
        v-dialogDrag
        >
            <div class="depart-container">
                <div class="content">
                    <el-radio-group  v-model="tableradioGroup" class="chooseOption">
                        <el-radio
                                :key="data.id" name="group3"  v-for="data in userList" :label="data"
                                    >{{ data.name  }}
                        </el-radio>
                    </el-radio-group>
                </div>    
                <div class="dialog-footer center">
                    <p @click="cancelDialog">取消</p>
                    <p @click="serviceTurn">确定</p>
                </div>
            </div>
        </el-dialog>
    </div>
</template>
<script>
    import { getGroupLeaderList,getBureauGroupList,startHelp,callHelp ,getStreetist} from "@/api/modules/helpAgent";
    import videoOperate from "@/utils/video";
    export default{
        name:'chooseDepart',
        data(){
            return{
                list:[],
                tableradioGroup:{},
                workerShow:false,
                userList:[],
                operateShow:false,
                baseUserInfo:JSON.parse(localStorage.getItem('baseUserInfo')),  
            }
        },
        mixins:[videoOperate],
        components:{
        },
        props:{
            type: {
                type: String,
                default: () => "",
            },
            groupType:{
                type: String,
                default: () => "",
            },
            isNeedOpenVideo:{
                type: Boolean,
                default: () => true,
            }
        },
        mounted(){
            if(this.groupType == '1'){
                //获取分组列表
                getStreetist()
                .then((res) => {
                    if (res.code === 200) {
                        this.operateShow = true;
                        this.list = res.data;
                    }
                })
                .catch((err) => {
                });
            }else if(this.groupType == '2'){
                //获取分组列表
                getGroupLeaderList()
                .then((res) => {
                    if (res.code === 200) {
                        let arr = [];
                        res.data.forEach(function(item){
                            if(item.haWorkUsers.length > 0){
                               arr = arr.concat(item.haWorkUsers);
                            }
                        })
                        this.operateShow = true;
                        this.list = arr;
                    }
                })
                .catch((err) => {
                });
            }else{
                this.operateShow = false;
                getBureauGroupList()
                .then((res) => {
                    if (res.code === 200) {
                        this.list = res.data;
                    }
                })
                .catch((err) => {
                });
            }
        },
        methods:{
            unitService(data){
                if(data.haWorkUsers.length > 0){                    
                    this.userList = data.haWorkUsers;
                    this.workerShow = true;
                    this.operateShow = true;
                }else{
                    this.$message.warning("暂无下属工作人员！");
                }
                
            },
            changeService(data){
                // console.log(data);
                // console.log(this.tableradioGroup)
            },  
            cancelDialog(){
                this.$emit('closeDepart');
            },
            closeWorker(){
                this.workerShow = false;
            },
            serviceTurn(){
                let that = this;
                if(!this.tableradioGroup){
                    this.$message.warning("请选择组长或工作人员！");
                    return;
                }
                console.log(this.baseUserInfo);
                if(!this.baseUserInfo){
                    this.$message.warning("请先接待！");
                    return;
                }
                if(this.isNeedOpenVideo){
                    let params = {
                        workUserId:that.tableradioGroup.id,
                        userName:that.tableradioGroup.name,
                        queueId:that.baseUserInfo.id,
                        userType:that.groupType,  //2-帮办组长，3-委办局老师
                    }
                    startHelp(params).then(res=>{
                        if(res.data.sig && res.data.videoNum && res.data.roomNumber && res.data.appId ){
                            res.data['videoAppId'] = res.data.appId;
                            that.cancelDialog();
                            that.$emit('openVideo');
                            that.startOpenVideo(res.data);  //开启视频咨询
                        }else{
                            console.log("视频房间数据不全");
                        }
                    })
                }else{
                    //在视频打开下，呼叫组长或委办局不需要再次打开视频
                    let params = {
                        workUserId:that.tableradioGroup.id,
                        roomOid:localStorage.getItem("roomId"),
                        userName:that.tableradioGroup.name,
                        userType:that.groupType,  //2-帮办组长，3-委办局老师
                    }
                    callHelp(params).then(res=>{
                        if(res.data.accessId){
                            that.$message.warning("正在呼叫中...");
                            that.cancelDialog();
                        }
                    })
                }
                
                
            }
        }
    }
</script>
<style lang="scss" scoped>  
    .depart-container{
        height:100%;
        .content{
            height: calc(100% - 50px);
            overflow: auto;
        }
    }
    .chooseOption {
        width: 100%;
        height: auto;
        display: flex;
        align-items: center;
        justify-content: flex-start;
        flex-wrap: wrap;

        .el-radio {
            width:calc((100% - 3.875rem)/2);
            height: 3.9375rem;
            background: #edf0f4;
            border: 1px solid #c8cdd3;
            box-shadow: 0px 0.5rem 0px 0px rgba(106, 159, 231, 0.31);
            border-radius: 2.2143rem;
            display: flex;
            align-items: center;
            justify-content: flex-start;
            padding: 0 3rem 0 1.3125rem;
            font-size: 1.5rem;
            font-family: Source Han Sans CN;
            font-weight: 500;
            color: #3f3f3f;
            margin-right: 2.875rem;
            margin-bottom: 1.5rem;
            position: relative;
            ::v-deep .el-radio__inner {
                display: inline-block;
                width: 1.625rem;
                height: 1.625rem;
                background: url("@/assets/images/pad/choose.png") no-repeat;
                background-size: 100% 100%;
            }

            // img {
            //   width: 1.625rem;
            //   margin-right: 0.6875rem;
            // }

            span {
                color: #3f3f3f;
            }
            &::after{
                content: '';
                position: absolute;
                top:10px;
                right: 20px;
                width: 17px;
                height: 17px;
                border-radius: 50%;
            }
            &.todo::after{
                background-color: #4effae;
            }
            &.out::after{
                background-color: #b4cae8;
            }
            &.full::after{
                background-color: #ff4f38;
            }
        }
        &>label:nth-child(2n){
            margin-right: 0;
        }
        .is-checked {
        color: #ffffff;
        background: linear-gradient(
            90deg,
            #6d93e8 0%,
            #77b0fe 100%
        ) !important;
        // box-shadow: 0px 0.5rem 0px 0px rgba(106, 159, 231, 0.31);
        box-shadow: 0px 0.4375rem 0px 0px rgb(106 159 231 / 31%),
            inset 0 0.4375rem 0.4375rem 0 rgb(188 212 251),
            inset -0.4375rem 0 0 0 rgb(114 173 249),
            inset 7px 0 0 0 rgb(107 146 230) !important;
        border: none !important;

        ::v-deep .el-radio__label {
            color: #fff;
        }

        ::v-deep .el-checkbox__label {
            color: #fff;
        }

        ::v-deep .el-checkbox__inner {
            background: #fff;

            &:after {
            border-color: #6090e3;
            }
        }

        ::v-deep .el-radio__inner {
            background: url("@/assets/images/pad/chooseActivate.png") no-repeat;
            background-size: 100% 100%;

            &:after {
            display: none;
            }
        }
        }

        .el-checkbox {
        min-width: 13.875rem;
        height: 3.9375rem;
        background: #edf0f4;
        border: 1px solid #c8cdd3;
        box-shadow: 0px 0.5rem 0px 0px rgba(106, 159, 231, 0.31);
        border-radius: 2.2143rem;
        display: flex;
        align-items: center;
        justify-content: flex-start;
        padding: 0 3rem 0 1.3125rem;
        font-size: 1.5rem;
        font-family: Source Han Sans CN;
        font-weight: 500;
        margin-right: 2.875rem;
        margin-bottom: 1.5rem;
        }
    }
     .dialog-footer {
        justify-content: center !important;
        display: flex;
        p {
            &:nth-child(1) {
                cursor: pointer;
                padding: 0 3.75rem;
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
                padding: 0 3.75rem;
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
                color: #ffffff !important;
                background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%) !important;
                box-shadow: 0px 0px 1.5rem 0px rgb(40 107 198 / 31%) !important;
            }
        }
    }
</style>