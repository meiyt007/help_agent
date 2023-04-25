<template>
    <div class="depart-container">
        <div class="content" style="min-height:400px;">
            <el-radio-group v-model="selectData" class="chooseOption">
                <el-radio
                        :key="data.id" name="group1"  @change="changeService(data)" v-for="data in listArr" :label="data"
                            >{{ data.name  }}
                </el-radio>
            </el-radio-group>
        </div>    
        <div class="dialog-footer center">
            <p @click="cancelDialog">取消</p>
            <p @click="serviceTurn">确定</p>
      </div>
    </div>
</template>
<script>
    import {
    queryWorkQueueList,
    getResourceInfo,
    attaPush
} from "@/api/modules/resourceInformation";
import {policyPush} from "@/api/modules/policy";

    export default{
        name:'chooseDepart',
        data(){
            return{
                listArr:JSON.parse(this.list),
                workerShow:false,
                selectData:this.tableradioGroup
            }
        },
        components:{

        },
        props:{
            //文件数据
            fileData: {
                type: Object,
                default: () => {},
            },
            //文件数据
            tableradioGroup: {
                type: Object,
                default: () => {},
            },
            //文件数据
            list: {
                type: String,
                default: () => '',
            },
            // 政策库下的分享给办事人flag
            shareFlag:{
                type:Boolean,
                default: () => '',
            }
        },  
        mounted(){
            this.selectData = {};
            // queryWorkQueueList({
            //     serviceStatus:'2',
            //     name:'',
            //     cardNo:'',
            //     companyName:'',
            //     queueStatus:'',
            // }).then(res=>{
            //     if(res.data.length > 0){
            //         this.list = res.data;
            //     }else{
            //         this.$message.warning("暂无办事人员！");
            //         this.$emit("handleClose");
            //     }
            // })
            
        },
        methods:{
            changeService(data){
                // console.log(data);
                // console.log(this.tableradioGroup)
            },  
            cancelDialog(){
                this.selectData = {};
                this.$emit('handleClose');
            },
            serviceTurn(){
                // console.log(this.selectData)
                if(!this.selectData.name){
                    this.$message.warning("请选择办事人员！");
                    return;
                }
                // getResourceInfo({
                //     id:this.fileData.id
                // }).then(res=>{
                    // console.log(this.fileData);
                    
                    if(this.shareFlag){
                        let params = {
                            name:this.selectData.name,
                            cardNo:this.selectData.cardNo,
                            phone:this.selectData.phone,
                            companyName:'',
                            companyCode:'',
                            policyBase:this.fileData
                        }
                        policyPush(params).then((res)=>{
                             if(res.code == 200){
                                this.$message.success("分享成功！");
                                this.$emit('handleClose');
                                this.selectData = {};
                            }
                        }).catch((err) => {
                            // this.$message.warning("分享失败");
                        });
                    }else{
                        let attaName = this.fileData.materialName;
                        let attaUrl = this.fileData.materialUrl;
                        if(!attaUrl){
                            this.$message.warning("分享失败");
                            return;
                        }
                        attaPush({
                            attaName,
                            attaUrl,
                            name:this.selectData.name,
                            cardNo:this.selectData.cardNo,
                            phone:this.selectData.phone,
                            companyName:'',
                            companyCode:''
                        }).then(item=>{
                            if(item.code == 200){
                                this.$message.success("分享成功！");
                                this.$emit('handleClose');
                                this.selectData = {};
                            }
                        }).catch((err) => {
                            // this.$message.warning("分享失败");
                        });
                    }
                // })
                
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