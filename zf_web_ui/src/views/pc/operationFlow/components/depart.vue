<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-09-23 13:07:46
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-10-13 13:33:31
 * @FilePath: \zf_web_ui\src\views\pc\roundTableAssistant\components\stagingServiceList.vue
 * @Description: 一桌联办服务列表
-->
<template>
    <div class="app-container">
        <div class="depart-content">
            <div style="padding-top:20px">请确认联办部门：</div>
            <el-checkbox-group v-model.trim="groupIds" class="chooseOption">
                <el-checkbox 
                v-for="item in list" 
                :key="item.groupId" 
                :id="item.groupId" 
                :label="item.groupId" 
                :checked="item.status == '1'"
                disabled
                @change="handleQA()">{{ item.groupName }}
                </el-checkbox>
            </el-checkbox-group>
        </div>
        <div class="foot">
            <div @click="toNext">确认</div>
        </div>
    </div>
</template>
<script>
import { getDeskGroup,startDesk } from "@/api/modules/helpAgent";
export default {
    name: "depart",
    components: {
    },
    data() {
        return {
            pageNum: 1,
            pageSize: 10,
            total: 0,
            list: [],
            appDate:'',
            query:{
                deskQueueId:this.deskQueueId,
                deskId:this.deskId,

            },
            groupIds:[]
        };
    },
    props:{
        deskId: {
            type: String,
            default: () => "",
        },
        deskQueueId: {
            type: String,
            default: () => "",
        },
    },
    computed: {

    },
    mounted() {
        this.getQueryHelpServiceHistoryList();
    },
    methods: {
        toNext(){
            console.log(this.query)
            let that = this;
            startDesk(this.query).then(res=>{
                if(res.code == 200){
                    that.$message.success("联办发起成功！");
                    that.$emit('closeDepart');
                }
            }).catch(err=>{
                console.log(err)
            })
        },
        handleQA() {

        },
        handleSizeChange(pageSize) {
            this.pageSize = pageSize;
            this.getQueryHelpServiceHistoryList();
        },
        handleCurrentChange(pageNum) {
            this.pageNum = pageNum;
            this.getQueryHelpServiceHistoryList();
        },
        //获取暂存办件记录
        getQueryHelpServiceHistoryList() {
            let that = this;
            getDeskGroup({deskId:this.deskId})
                .then((res) => {
                    if (res.code === 200) {
                        that.list = res.data;
                    }
                })
                .catch((err) => {
                    this.loadingTableData = false;
                });
        },
    },
};
</script>
<style lang="scss" scoped>
    .chooseOption {
        width: 100%;
        height: auto;
        display: flex;
        align-items: center;
        justify-content: flex-start;
        flex-wrap: wrap;
        padding: 1.5rem 0 1.5rem 1.8125rem;

        .el-radio {
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

        .is-checked {
            color: #ffffff;
            background: linear-gradient(90deg,
                    #6d93e8 0%,
                    #77b0fe 100%) !important;
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
    .depart-content{
        height: calc(100% - 8rem);
        overflow-y: auto;
        text-align: left;
    }
    .foot{
        width: 100%;
        height: 3.0625rem;
        margin-top: 1.3rem;
        display: flex;
        justify-content: center;
        &>div{
            width: 17.1429rem;
            height: 4.8571rem;
            display: flex;
            align-items: center;
            justify-content: center;
            background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
            box-shadow: 0px 0px 2.0714rem 0px rgb(204 177 121 / 31%);
            border-radius: 0.7143rem;
            font-size: 1.7143rem;
            font-family: Microsoft YaHei;
            font-weight: 400;
            color: #ffffff;
            text-shadow: 0px 0px 0.3571rem rgb(0 56 156 / 63%);
        }
    }
</style>
