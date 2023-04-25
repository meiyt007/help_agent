<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-26 10:31:12
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-11-10 16:42:11
 * @FilePath: \zf_web_ui\src\views\pc\operationFlow\components\visitingRecords.vue
 * @Description: 来访记录
-->
<template>
    <div class="visitingRecords">
        <div class="tab-header">
            <el-form :model="searchParams">
                <el-form-item label="主题">
                    <el-select v-model="searchParams.title" placeholder="请选择" clearable :popper-append-to-body="false">
                        <el-option label="资金扶持" value="资金扶持"></el-option>
                        <el-option label="就业创业" value="就业创业"></el-option>
                        <el-option label="行业发展" value="行业发展"></el-option>
                        <el-option label="中小企业" value="中小企业"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="部门">
                    <el-select v-model="searchParams.organId" placeholder="请选择" clearable :popper-append-to-body="false">
                        <el-option v-for="item in listOrganOptions" :key="item.id" :label="item.label"
                            :value="item.id.split('-')[1]">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-input clearable placeholder="请输入关键词" suffix-icon="el-icon-microphone" v-model="searchParams.name">
                    </el-input>
                </el-form-item>
                <el-form-item>
                    <el-button @click="query" type="primary" icon="el-icon-search">搜索</el-button>
                </el-form-item>
            </el-form>
        </div>
        <div class="table-header">
            <div class="th01">序号</div>
            <div class="th02">政策名称</div>
            <div class="th03">主题</div>
            <div class="th03">政策体检</div>
            <div class="th03">线上申报</div>
            <div class="th03">政策推送</div>
        </div>
        <div class="result" :style="{ transition: `all .${number}s`, top: `${translateY}px` }" @touchend="touchend"
            @touchmove="touchmove" @touchstart="touchstart">
            <div class="loadingBox" v-if="touchstartTitleShow">释放可刷新...</div>
            <div class="loadingBox" v-if="touchEndTitleShow">加载中...</div>
            <div id="box" ref="scroll" >
                <div class="tableArea" ref="tableArea" v-loading="loadingTableData">
                    <el-table ref="table" :show-header="false" :data="visitingRecordList"
                        :cell-style="{ 'text-align': 'center' }" :header-cell-style="{ 'text-align': 'center' }" border>
                        <el-table-column type="index" width="50" label="序号">
                        </el-table-column>
                        <template v-for="(item, index) in columnList">
                            <el-table-column :key="index" :label="item.label" :prop="item.prop" v-if="item.prop === 'operate'"
                                show-overflow-tooltip :width="item.width">
                                <template slot-scope="scope">
                                    <p v-if="scope.row.experienceLink" class="exam"  @click="toOuter(scope.row.experienceName,scope.row.experienceLink)">
                                        <span>我要体检</span>
                                    </p>
                                    <p v-if="!scope.row.experienceLink" class="nolink"  @click="toOuter(scope.row.experienceName,scope.row.experienceLink)">
                                      <span>我要体检</span>
                                    </p>
                                </template>
                            </el-table-column>
                            <el-table-column :key="index" :label="item.label" :prop="item.prop" v-else-if="item.prop === 'operate2'"
                                show-overflow-tooltip :width="item.width">
                                <template slot-scope="scope">
                                    <p v-if="scope.row.declareLink" class="seeOp" @click="toOuter(scope.row.serviceName,scope.row.declareLink)">
                                        <span>我要申报</span>
                                    </p>
                                    <p v-if="!scope.row.declareLink" class="nolink" @click="toOuter(scope.row.serviceName,scope.row.declareLink)">
                                      <span>我要申报</span>
                                    </p>
                                </template>
                            </el-table-column>
                            <el-table-column :key="index" :label="item.label" :prop="item.prop" v-else-if="item.prop === 'operate3'"
                                show-overflow-tooltip :width="item.width">
                                <template slot-scope="scope">
                                    <p class="shareLink" @click="shareLink(scope.row)">
                                        <span>分享办事</span>
                                    </p>
                                </template>
                            </el-table-column>
                            <el-table-column :key="index" :label="item.label" :prop="item.prop" v-else-if="item.prop === 'name'" show-overflow-tooltip
                                :width="item.width">
                                <template slot-scope="scope">
                                     <span @click="toOuter2(scope.row.name,scope.row.policyLink)" class="tableName">{{scope.row.name}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column :key="index" :label="item.label" :prop="item.prop" v-else show-overflow-tooltip
                                :width="item.width">
                            </el-table-column>
                        </template>
                    </el-table>
                </div>
            </div>
            <div class="loadingBox" v-if="loading">加载中...</div>
            <div class="loadingBox" v-if="!loading && !hasNext">已加载全部内容</div>
        </div>
        <div class="footBtn" v-if="hasNext">
            <p>
                <img :src="require('@/assets/images/pad/upup.png')" alt="" />
                向上滑动加载更多
            </p>
        </div>
      <el-dialog
          title="政策库"
          :visible.sync="showOuter"
          v-if="showOuter"
          width="80%"
          class="pageDialog"
          center
          append-to-body
          v-dialogDrag
      >
        <PolicyNext :listUrl="listUrl" :listName="listName" />
      </el-dialog>
      <!-- 分享办事人 -->
        <el-dialog :show-close="false" title="分享给办事人员" top="100px　!important" :visible.sync="showHandle" width="800px"
            center class="pageDialog" append-to-body v-dialogDrag>
            <shareHandle @handleClose="handleClose" :list="listData" :tableradioGroup="tableradioGroup"
                :fileData="fileData" :shareFlag = "shareFlag"/>
        </el-dialog>
    </div>
</template>
<script>
import { querySysOrganWithPage, queryPolicyBaseWithPage } from "@/api/modules/helpAgent";
import PolicyNext from "@/views/pad/assistant/components/policyNext";
import {queryWorkQueueList} from "@/api/modules/resourceInformation";
import shareHandle from './search/shareHandle';
export default {
    name: "VisitingRecords",
  components: {PolicyNext,shareHandle},
  props: {
        // baseUserInfo: {
        //   type: Object,
        //   default: () => {},
        // },
    },
    data() {
        return {
            tableHeight: 300,
            visitingRecordList: [],
            loadingTableData: false,
            searchParams: {
                title: '',
                name: '',
                organId: '',
                pageNumber: 1,
                pageSize: 15,
            },
            columnList: [
                { label: "政策名称", prop: "name", width: "" },
                { label: "主题", prop: "title", width: "130" },
                { label: "政策体检", prop: "operate", width: "130" },
                { label: "线上申报", prop: "operate2", width: "130" },
                { label: "政策推送", prop: "operate3", width: "130" },
            ],
            pageNum: 1,
            pageSize: 15,
            total: 0,
            serviceData: {},
            serviceStatusOptions: [
                { label: "全部", value: "" },
                { label: "等待服务", value: "1" },
                { label: "服务中", value: "2" },
                { label: "服务完成", value: "3" },
            ],
            listOrganOptions: [],
            touchEndTitleShow: false, //控制手指离开屏幕的title显示
            touchstartTitleShow: false, //控制手指按下屏幕的title显示
            number: 0, //列表回弹动画时间
            translateY: 50, //列表随手指下拉而偏移的量
            startY: 0, //手指按住的位置的y坐标，也就是起始坐标
            hasNext: true, //是否还有下一页
            loading: false, //loading显示
            showOuter:false,
            listName:'',
            listUrl:'',
            showHandle:false,
            listData:'',
            tableradioGroup:{},
            shareFlag:true,
            fileData:''
        };
    },
    mounted() {
        let districtOid = '4028545d665734290166b02711c20073';
        querySysOrganWithPage({ districtOid: districtOid }).then(response => {
            this.listOrganOptions = response.data;
            // console.log("-----"+JSON.stringify(this.listOrganOptions))
        });
        this.getQueryAllWorkQueueList();
    },
    methods: {
        toOuter(name,url){
            if(name){
                if(name.indexOf(';') > -1){
                  this.listName = name;
                  this.listUrl = url;
                  this.showOuter = true
                }else{
                  console.log("Url"+url)
                  android.setWebUrl(url);
                }

            }else{
                this.$message.warning("暂无链接");
            }
        },
        handleClose() {
            this.showHandle = false;
            this.tableradioGroup = {};
            this.keyValue = Math.random();
        },
        shareLink(data){
             this.$message.warning("正在开发中...");

            // this.tableradioGroup = {};
            // let that = this;
            // queryWorkQueueList({
            //     serviceStatus: '2',
            //     name: '',
            //     cardNo: '',
            //     companyName: '',
            //     queueStatus: '',
            // }).then(res => {
            //     if (res.data.length > 0) {
            //         that.listData = JSON.stringify(res.data);
            //         that.showHandle = true;
            //     } else {
            //         that.$message.warning("暂无办事人员！");
            //         that.showHandle = false;
            //     }
            // })
            // let policyBase = {
            //     title:data.title,
            //     name:data.name,
            //     linkName:data.linkName,
            //     policyLink:data.policyLink,
            //     serviceName:data.serviceName,
            //     declareLink:data.declareLink,
            //     experienceName:data.experienceName,
            //     experienceLink:data.experienceLink,
            // };
            // // 根据organID得到organName
            // this.listOrganOptions.forEach(element => {
            //     if(element.id == data.id){
            //         policyBase.organName = element.label;
            //     }
            // });
            // // 对linkName字符做拼接
            // if(policyBase.policyLink.indexOf(';') > -1){
            //     var urlArr = policyBase.policyLink.split(";");
            //     var len = urlArr.length;
            //     var jg = null;
            //     var jg2 = "";
            //     for(var i = 1; i < len+1 ;i++){
            //         jg = jg2 + policyBase.name+"("+i+")";
            //         jg2 = jg + ";";
            //     }
            //     policyBase.linkName = jg;
            // }else{
            //     policyBase.linkName = data.name;
            // }
            // this.fileData = policyBase;
        },
        //根据链接数量拼接政策链接名称，有多少个政策链接就拼接多少给政策名称
        //例如link：http://***.com;http://***.com
        //拼接后的名称jg：**政策1;**政策2
        toOuter2(name,url){
          if(name){
            if(url.indexOf(';') > -1){
              var urlArr = url.split(";");
              var len = urlArr.length;
              var jg = null;
              var jg2 = "";
              for(var i = 1; i < len+1 ;i++){
                jg = jg2 + name+"("+i+")";
                jg2 = jg + ";";
              }

              this.listName = jg;
              this.listUrl = url;
              this.showOuter = true
            }else{
              console.log("Url"+url)
              android.setWebUrl(url);
            }

          }else{
            this.$message.warning("暂无链接");
          }
        },
        query() {
            this.searchParams.pageNumber = 1;
            this.visitingRecordList = [];
            this.getQueryAllWorkQueueList();
        },
        //查看服务记录
        toServiceRecord(data) {
            this.serviceData = data;
        },
        //手指触碰到屏幕
        touchstart(e) {
            this.number = 0;
            let y = e.targetTouches[0].pageY;
            this.startY = y;
        },
        //手指开始滑动
        touchmove(e) {
            let y = e.targetTouches[0].pageY;
            if (y > this.startY && this.$refs.scroll.scrollTop == 0) {
                this.touchstartTitleShow = true;
                //如果当前移动距离大于初始点击坐标，则视为是下拉。并且要处于顶部才刷新，不能影响正常的列表滑动。
                this.translateY = (y - this.startY) / 2;
            } else {
                this.initScrollChange();
            }
        },
        //手指松开
        touchend(e) {
            let y = e.changedTouches[0].pageY;
            if (y > this.startY) {
                this.number = 4;
                this.translateY = 0;
                this.touchstartTitleShow = false;
                this.touchEndTitleShow = true;
                setTimeout(() => {
                    this.touchEndTitleShow = false;
                }, 1000);
                this.startY = 0;
            }
        },
        initScrollChange() {
            this.$refs.scroll.onscroll = (e) => {
                const offsetHeight = this.$refs.scroll.offsetHeight; //可视区域的高度
                const scrollHeight = this.$refs.scroll.scrollHeight; //元素全部高度
                const scrollTop = this.$refs.scroll.scrollTop; //滚动条滚动距离
                //可视区域高度加上滚动条滚动距离大于等于元素全部高度则表示滚动到底
                if (offsetHeight + scrollTop - scrollHeight >= -1) {
                    console.log("到底啦");
                    if (!this.loading && this.hasNext) {
                        this.searchParams.pageNumber += 1;
                        if (this.searchParams.pageNumber <= Math.ceil(this.total / this.searchParams.pageSize)) {
                            this.getQueryAllWorkQueueList();
                            this.hasNext = true;
                        } else {
                            this.hasNext = false;
                        }

                    }
                }
            };
        },
        getQueryAllWorkQueueList() {
            this.loadingTableData = true;
            let that = this;
            queryPolicyBaseWithPage(this.searchParams)
                .then((res) => {
                    if (res.data.data.length > 0) {
                        that.total = res.data.total;
                        //判断是否有下一页  关闭加载提示
                        if(that.searchParams.pageNumber < that.total/that.searchParams.pageSize){
                            that.hasNext = true;
                        }else{
                            that.hasNext = false;
                        }
                        that.loadingTableData = false;
                        that.visitingRecordList =that.visitingRecordList.concat(res.data.data);                        
                        
                    }
                })
                .catch((err) => {
                    console.log(err);
                    this.loadingTableData = false;
                });
        },
    },
    watch: {},
};
</script>
<style lang="scss" scoped>   
    .tableName{
        padding-left: 10px;
        display: inline-block;
        text-align: left;
        width: 100%;
    }
    .result{
        // margin-top: 2.9412rem;
        // max-height: 450px;
        height: 450px;
        height: calc(100% - 15rem);
        // padding-right: 10px;
        #box{
            width: calc(100% + 3px);
            height: 100%;
            overflow-y: auto;
            &::-webkit-scrollbar {
                width: 0.4375rem;
                background-color: #fff;
            }
            
            &::-webkit-scrollbar-thumb {
                width: 0.4375rem;
                height: 0.625rem !important;
                // background: linear-gradient(270deg, #bf9e63 0%, #dfca98 100%);
                background: linear-gradient(270deg, #2473ff 0%, #56b1fd 100%);
                border-radius: 4px;
            }
        }

        
    }
    .loadingBox{
        text-align: center;
        font-size: 12px;
        margin-top: 5px;
    }
    .footBtn {
    width: 100%;
    height: 3.125rem;
    margin-top: 0.6875rem;
    display: flex;
    align-items: center;
    justify-content: center;

    p {
      height: 3.125rem;
      width: auto;
      padding: 0 1.75rem;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      font-size: 1.25rem;
      background-color: rgba(59, 166, 255, 0.1);
      border-radius: 1.5625rem;
      font-family: Microsoft YaHei;
      font-weight: 400;
      color: #3ba6ff;

      img {
        width: 0.8125rem;
        margin-right: 0.5rem;
      }
    }
}
 .table-header {
        display: flex;
        align-items: center;
        height: 50px;
        justify-content: center;
        background-color: #edf0f4;
        border-top: solid 1px #c8cdd3;
        border-left: solid 1px #c8cdd3;
        font-size: 14px;
        &>div {
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .th01 {
            width: 50px;
            text-align: center;
            border-right: solid 1px #c8cdd3;
        }

        .th02 {
            width: calc(100% - 570px);
            text-align: left;
            padding-left: 20px;
            border-right: solid 1px #c8cdd3;
            justify-content: flex-start;
        }

        .th03 {
            width: 130px;
            text-align: center;
            border-right: solid 1px #c8cdd3;
        }
    }

    .visitingRecords {
        height: 100%;
        padding-top: 4.0588rem;
        text-align: left;
        width: calc(100% - 50px);
        margin: 0 auto;

        .tab-header {
            width: 100%;
            height: 5.7143rem;
            padding: 0.7143rem 0;

            .el-form {
                width: 100%;
                height: 100%;
                display: flex;
                align-items: center;
                justify-content: space-between;

                .el-form-item {
                    height: 100%;
                    display: flex;
                    align-items: center;
                    justify-content: space-between;

                    &:nth-child(1) {
                        width: 29%;
                    }

                    &:nth-child(2) {
                        width: 29%;
                    }

                    &:nth-child(3) {
                        width: 29%;
                    }

                    &:nth-child(4) {
                        width: 13%;
                    }

                    ::v-deep .el-form-item__label {
                        width: 8rem;
                        height: 100%;
                    }

                    ::v-deep .el-form-item__content {
                        flex: 1;

                        .el-select {
                            width: 90%;
                        }

                        .el-input {
                            width: 90%;
                        }

                        .el-date-editor {
                            width: 80%;
                        }
                    }
                }
            }
        }

        .tableArea {
            width: 100%;
            // height: calc(100% - 10rem);
            // overflow-y: auto;
        }
    }

    .el-pagination {
        text-align: right;
        margin-top: 1rem;
        padding-right: 0;
    }

    .seeOp {
        padding: 0;
        margin: 0;
        width: 100%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;

        span {
            margin: 0;
            cursor: pointer;
            width: 5.7857rem;
            height: 2.5rem;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 1.25rem;
            background-color: #d3e6f5;
            font-size: 1.2857rem;
            font-family: Microsoft YaHei;
            font-weight: 400;
            color: #3ba6ff;
            width: 7.2353rem;
            height: 2.6471rem;
            background-color: #ffb045;
            border-radius: 1.3529rem;
            color: #fff;
            font-size: 1.2941rem;
        }
    }
    .shareLink {
        padding: 0;
        margin: 0;
        width: 100%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;

        span {
            margin: 0;
            cursor: pointer;
            width: 5.7857rem;
            height: 2.5rem;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 1.25rem;
            background-color: #d3e6f5;
            font-size: 1.2857rem;
            font-family: Microsoft YaHei;
            font-weight: 400;
            color: #3ba6ff;
            width: 7.2353rem;
            height: 2.6471rem;
            background-color: #06c4ba94;
            border-radius: 1.3529rem;
            color: #fff;
            font-size: 1.2941rem;
        }
    }

    .exam {
        padding: 0;
        margin: 0;
        width: 100%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;

        span {
            margin: 0;
            cursor: pointer;
            width: 5.7857rem;
            height: 2.5rem;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 1.25rem;
            background-color: #d3e6f5;
            font-size: 1.2857rem;
            font-family: Microsoft YaHei;
            font-weight: 400;
            color: #3ba6ff;
            width: 7.2353rem;
            height: 2.6471rem;
            background-color: #659fef;
            border-radius: 1.3529rem;
            color: #fff;
            font-size: 1.2941rem;
        }
    }

    .nolink {
      padding: 0;
      margin: 0;
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;

      span {
        margin: 0;
        cursor: pointer;
        width: 5.7857rem;
        height: 2.5rem;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 1.25rem;
        background-color: #d3e6f5;
        font-size: 1.2857rem;
        font-family: Microsoft YaHei;
        font-weight: 400;
        color: #C7C7C7;
        width: 7.2353rem;
        height: 2.6471rem;
        background-color: #C7C7C7;
        border-radius: 1.3529rem;
        color: #ffffff;
        font-size: 1.2941rem;
      }
    }

    ::v-deep .el-dialog {
        height: 80vh !important;

        .el-dialog__body {
            height: calc(100% - 3.75rem);
            max-height: 100%;
        }

    }

    ::v-deep .el-date-editor .el-range-separator {
        min-width: 25px;
    }
</style>
  