<template>
    <div class="search-container">
        <div class="none" style="margin-top: 20px;margin-bottom: 20px;" @click="jsCallApp">测试3</div>
        <div class="search">
            <el-input type="text" placeholder="请输入关键词" v-model="query.keyword" clearable></el-input>
            <div class="btn centerXY" @click="queryData">
                <img src="@/assets/images/pad/query.png" alt="">
                <span>搜索</span>
            </div>
        </div>
        <input type="file" id="fileInputValue" style="display: none"></input>
        <template v-for="(data,index) in list">
            <div class="item centerY" :class="{'item-pdf':data.materialUrl.indexOf('pdf') !== -1}" :key="index" v-if="data.materialName">
                <span class="name">{{ data.materialName }}</span>
                <!-- <div class="look lookPdf" @click="toShare(data)" v-show="data.materialUrl.indexOf('pdf') !== -1" >分享</div> -->
                <template v-if="data.materialName.indexOf('doc') !== -1 || data.materialName.indexOf('docx') !== -1">
                    <div class="look look_share" @click="toShare(data)">分享</div>
                    <div class="look" @click="detail(data)">
                        <a :href="data.materialUrl">查看</a>
                    </div>
                </template>
                 <div class="look" v-else @click="detail(data)">查看</div>
            </div>
        </template>
        <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="query.pageNumber"
        :page-sizes="[10, 20, 30, 50, 100]"
        :page-size="query.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        >
        </el-pagination>
        <el-dialog v-dialog-drag title="预览" :visible.sync="showPreview" class="preview-dialog" width="80%" append-to-body custom-class="serviceTransfer">
            <div class="previewArea">
                <iframe v-if="preType == 'pdf'" ref="iframe" id="pdf-box" :src="preViewUrl" frameborder="0"
                    style="height: 800px; width: 100%; overflow: hidden"></iframe>
                <img v-if="preType == 'img'" :src="preViewUrl" alt="img" />
            </div>
            <div class="shareBtn" @click="toBanshi">分享给办事人员</div>
        </el-dialog>
        <!-- 分享办事人 -->
        <el-dialog :show-close="false" title="分享给办事人员"  :visible.sync="showHandle" width="80%"
            center class="pageDialog" custom-class="handleTransfer" append-to-body v-dialogDrag>
            <shareHandle @handleClose="handleClose" :list="listData" :tableradioGroup="tableradioGroup"
                :fileData="fileData" />
        </el-dialog>
    </div>
</template>
<script>
import { queryMaterialList } from "@/api/modules/helpAgent";
import {
    queryWorkQueueList,
    getResourceInfo,
    attaPush
} from "@/api/modules/resourceInformation";
import shareHandle from './shareHandle';
export default {
    name: 'search',
    data() {
        return {
            query: {
                keyword: '',
                serviceOid: '',
                isDelete: '',
                pageNumber: 1,
                pageSize: 10
            },
            touchEndTitleShow: false, //控制手指离开屏幕的title显示
            touchstartTitleShow: false, //控制手指按下屏幕的title显示
            number: 0, //列表回弹动画时间
            translateY: 50, //列表随手指下拉而偏移的量
            startY: 0, //手指按住的位置的y坐标，也就是起始坐标
            hasNext: true, //是否还有下一页
            loading: false, //loading显示
            list: [],
            total: 0,
            showPreview: false,
            preType: '',
            preViewUrl: '',
            showHandle: false,
            tableradioGroup: {},
            fileData: {},
            keyValue: 0,
            listData: ''
        }
    },
    components: { shareHandle },
    mounted() {
        this.getData();
    },
    methods: {
        test() {
            android.setWebUrl('https://zwdt.sh.gov.cn/govPortals/bsfw/item/4df7e909-5471-49cf-adf6-3cfc7f4604bd')
        },
        jsCallApp() {
            var json = {
                "cmdType": "login",
                "roomID": "102",
                "account": "tc01",
                "password": "123456",
                "ep": ""
            };
            window.android.jsCallAndroidData(JSON.stringify(json));
        },
        // 预览页面里的分享给办事人
        toBanshi() {
            this.tableradioGroup = {};
            let that = this;
            queryWorkQueueList({
                serviceStatus: '2',
                name: '',
                cardNo: '',
                companyName: '',
                queueStatus: '',
            }).then(res => {
                if (res.data.length > 0) {
                    that.listData = JSON.stringify(res.data);
                    that.showHandle = true;
                } else {
                    that.$message.warning("暂无办事人员！");
                    that.showHandle = false;
                }
            })
        },
        // 列表里的分享给办事人
        toShare(data){
            this.fileData = data;
            this.toBanshi();
        },
        handleClose() {
            this.showHandle = false;
            this.tableradioGroup = {};
            this.keyValue = Math.random();
        },
        handleSizeChange(pageSize) {
            this.query.pageSize = pageSize;
            this.getData();
        },
        handleCurrentChange(pageNumber) {
            this.query.pageNumber = pageNumber;
            this.getData();
        },
        queryData() {
            this.query.pageNumber = 1;
            this.hasNext = true;
            this.list = [];
            this.getData();
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
                    // console.log("到底啦");
                    if (!this.loading && this.hasNext) {
                        this.query.pageNumber += 1;
                        if (this.query.pageNumber <= Math.ceil(this.total / this.query.pageSize)) {
                            this.getData();
                            this.hasNext = true;
                        } else {
                            this.hasNext = false;
                        }

                    }
                }
            };
        },
        getData() {
            // if(!this.hasNext){
            //     return;
            // }
            let that = this;
            queryMaterialList(this.query).then(res => {
                if (res.data.data.length > 0) {
                    
                    that.total = res.data.total;
                    //判断是否有下一页  关闭加载提示
                    // if(that.query.pageNumber < that.total/that.query.pageSize){
                    //     that.hasNext = true
                    // }else{
                    //     that.hasNext = false;
                    // }
                    res.data.data.forEach(function(item){
                        if(!item.materialUrl){
                            item.materialUrl = '';
                        }
                    })
                    that.list = res.data.data;
                } else {
                    // that.list = [];
                    // that.total = 0;
                    // that.$message.warning("暂无数据");
                }
            })
        },
        detail(data) {
            this.preViewUrl = data.materialUrl;
            this.fileData = data;
            let url = data.materialUrl;
            if (!url) {
                this.$message.warning("没有文件");
                return;
            }
            if(/\.(pdf|PDF)$/.test(url)){
                this.preType = 'pdf';
                this.showPreview = true;
                // android.openPdf(url);
            }else
            if (/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(url)) {
                this.preType = 'img';
                this.showPreview = true;
            } else {
                // this.$message.warning("该文件类型无法查看");
            }
        },
        fileUpload: function () {
            document.querySelector('input[type=file]').click()
            const that = this
            const file = document.querySelector('input[type=file]')
            file.onchange = () => {
                var fileData = file.files[0]
                console.log(fileData)
            }
        }
    }
}
</script>
<style lang="scss" scoped>
    @import './index.scss';    
</style>
<style lang="scss">
    .handleTransfer  {
        ::v-deep .el-dialog__body{
                height: calc(100% - 3.75rem);
            }
    }
</style>