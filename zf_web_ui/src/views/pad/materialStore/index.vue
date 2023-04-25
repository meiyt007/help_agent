<template>
    <div class="material-container" @click.self="pageClick" v-loading="loadingService" element-loading-text="拼命加载中"
        element-loading-background="transparent">
        <div class="header" @click.self="pageClick">
            <div class="back centerXY" @click="$router.back()">
                <img align="center" src="@/assets/images/back.png" alt="">
                <span>返回</span>
            </div>
            <div>我的资源库</div>
        </div>
        <div class="content" @click.self="pageClick">
            <div class="title centerBetween">
                <div class="search centerY">
                    <span class="text">资源名称：</span>
                    <el-input placeholder="输入关键词搜索" v-model="search" clearable>
                        <i slot="prefix" class="el-input__icon el-icon-search"></i>
                    </el-input>
                </div>
                <div class="centerXY">
                    <div class="add" @click="addDocument('1')">添加文件夹</div>
                    <div class="add" @click="addDocument('2')">添加资料</div>
                </div>

            </div>
            <div class="file-wrap" :style="{ transition: `all .${number}s`, top: `${translateY}px` }"
                @touchend="touchend" @touchmove="touchmove" @touchstart="touchstart">
                <div class="loadingBox" v-if="touchstartTitleShow">释放可刷新...</div>
                <div class="loadingBox" v-if="touchEndTitleShow">加载中...</div>
                <div class="scroll-container" ref="scroll">
                    <div></div>
                    <div class="files">
                        <div class="file" @click="reback" v-if="documentIdList.length > 1">
                            <div class="img">
                                <img :src="require('@/assets/images/home/folder.png')" alt="" />
                            </div>
                            <div class="detail centerY">
                                <div class="name">返回上一级</div>
                                <div class="status"></div>
                            </div>
                        </div>
                        <div class="file" :class="{ 'select': current == index + 1 }" :key="data.id"
                            v-for="(data, index) in fileList">
                            <div class="img" @click="toDetail(data)">
                                <img :src="data.type === '1' ? require('@/assets/images/home/folder.png') : require('@/assets/images/home/document.png')"
                                    alt="" />
                            </div>
                            <div class="detail centerY" @click="toSelect(data, index + 1)">
                                <div class="name">{{ data.name }}</div>
                                <div class="status" >
                                    <img class="noSelect" src="@/assets/images/noSelect.png" alt="">
                                    <img class="selected" src="@/assets/images/selected.png" alt=""></img>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="loadingBox" v-if="loading">加载中...</div>
                <div class="loadingBox" v-if="!loading && !hasNext">已加载全部内容</div>
            </div>

        </div>
        <!-- 点击弹窗 -->
        <div class="share-coupon centerXY" v-show="shareCoupon">
            <div class="item" @click="toHelp">
                <img src="@/assets/images/share.png" alt="">
                <div class="name">分享给帮办人员</div>
            </div>
            <div class="item" @click="toBanshi">
                <img src="@/assets/images/send.png" alt="">
                <div class="name">发送给办事人</div>
            </div>
            <div class="item" @click="deleteDoc">
                <img src="@/assets/images/cancel.png" alt="">
                <div class="name">删除</div>
            </div>
            <div class="item" @click="editDoc">
                <img src="@/assets/images/edit.png" alt="">
                <div class="name">编辑</div>
            </div>
            <div class="item" @click="closeShare">
                <img src="@/assets/images/close.png" alt="">
                <div class="name">关闭</div>
            </div>
        </div>
        <!-- 添加资料 -->
        <!-- <el-dialog title="添加资料" top="200px　!important" :visible.sync="showMaterialAdd" width="80%" center
            class="pageDialog" append-to-body v-dialogDrag>
            <materialAdd @close="closeAdd" />
        </el-dialog> -->
        <!-- 添加资料、文件夹 -->
        <el-dialog :title="titleValue" :visible.sync="showAddDocument" width="80%" center class="addDocumentDialog"
            append-to-body v-dialogDrag>
            <el-form v-if="showAddDocument" :model="documentInfo" :rules="documentRules" ref="documentInfo"
                label-width="10rem">
                <el-form-item label="资源名称" prop="name">
                    <el-input v-model="documentInfo.name" placeholder="请输入资源名称"></el-input>
                </el-form-item>
                <el-form-item label="资源类型" prop="type">
                    <el-select v-model="documentInfo.type" placeholder="请选择资源类型" :disabled="isEdit">
                        <el-option label="文件夹" value="1"></el-option>
                        <el-option label="文件" value="2"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="附件上传" prop="upload" v-if="documentInfo.type === '2'">
                    <el-upload class="upload-demo" action :http-request="uploadFiles" :before-upload="beforeUpload"
                        :on-error="uploadError" :limit="1" :file-list="upLoadFileList">
                        <img width="100%" :src="require('@/assets/images/home/downLoadIcon.png')" alt="" />
                    </el-upload>
                </el-form-item>
                <!-- <el-form-item label="备注" prop="resourceInfo">
          <el-input type="textarea" v-model="documentInfo.resourceInfo"></el-input>
        </el-form-item> -->
            </el-form>
            <div slot="footer" class="dialog-footer">
                <p @click="showAddDocument = false">取消</p>
                <p @click="compliteNext">确定</p>
            </div>
        </el-dialog>
        <!-- 分享 -->
        <el-dialog :show-close="false" title="分享给帮办人员" top="100px　!important" :visible.sync="showShare" width="800px"
            center class="pageDialog" append-to-body v-dialogDrag>
            <shareHelp @shareClose="shareClose" @shareConfirm="shareData" :chooseAssistant="chooseAssistant" />
        </el-dialog>
        <!-- 分享办事人 -->
        <el-dialog :show-close="false" title="分享给办事人员" top="100px　!important" :visible.sync="showHandle" width="800px"
            center class="pageDialog" append-to-body v-dialogDrag>
            <shareHandle @handleClose="handleClose" v-if="showHandle" :tableradioGroup="tableradioGroup" :fileData="fileData"/>
        </el-dialog>
        <el-dialog v-dialog-drag title="预览" :visible.sync="showPreview" class="preview-dialog" width="80%">
            <div class="previewArea">
                <iframe v-if="preType == 'pdf'" ref="iframe" id="pdf-box" :src="preViewUrl" frameborder="0"
                    style="height: 800px; width: 100%; overflow: hidden"></iframe>
                <img v-if="preType == 'img'" :src="preViewUrl" alt="img" />
            </div>
            <!-- <div class="foot">
                <p @click="shareAssistant">分享帮办人员</p>
                <p>发送给办事人</p>
            </div> -->
        </el-dialog>
    </div>
</template>
<script>
import {
    listResource,
    getResourceInfo,
    uploadFile,
    saveResourceInfo,
    deleteResource,
    shareResource,
} from "@/api/modules/resourceInformation";
import materialAdd from '../materialAdd/index.vue';
import shareHelp from './shareHelp';
import shareHandle from './shareHandle';
export default {
    name: 'materialStore',
    components: { materialAdd, shareHelp,shareHandle },
    data() {
        return {
            search: '',
            current: '',
            shareCoupon: false,
            showMaterialAdd: false,
            showShare: false,
            loadingService: false,
            touchEndTitleShow: false, //控制手指离开屏幕的title显示
            touchstartTitleShow: false, //控制手指按下屏幕的title显示
            number: 0, //列表回弹动画时间
            translateY: 0, //列表随手指下拉而偏移的量
            startY: 0, //手指按住的位置的y坐标，也就是起始坐标
            hasNext: true, //是否还有下一页
            loading: false, //loading显示
            fileInfo: {
                parentId: "",
                name: "",
                pageNum: 1,
                pageSize: 16,
            },
            fileList: [],
            chooseAssistant: [],
            documentIdList: [""], //存放上一级ids
            shareDocumentId: "",
            preViewUrl: "", //预览
            preType:'', //预览文件类型
            showPreview: false, //打开预览
            titleValue: "",
            showAddDocument: false,
            documentInfo: {
                name: "",
                type: "",
                fileName: "",
                upload: "",
            },
            documentRules: {
                name: [{ required: true, message: "请输入资源名称", trigger: "blur" }],
                type: [
                    { required: true, message: "请选择资源类型", trigger: "change" },
                ],
                upload: [{ required: true, message: "请上传文件", trigger: "change" }],
            },
            upLoadFileList: [],
            isEdit: false,
            editData: {},
            dataPrepare: {},
            upLoadFileList: [],
            resourceInfo: '',
            fileType:'',  // 1文件夹   2文件  
            showHandle:false, 
            fileData:{},
            tableradioGroup:{}
        }
    },
    mounted() {
        this.getListResource();
    },
    computed: {
        basicUserInfo() {
            return this.$store.state.user.basicUserInfo;
        },
    },
    watch: {
        showAddDocument: {
            handler(val) {
                if (!val) {
                    this.documentInfo = {
                        name: "",
                        type: "",
                        fileName: "",
                        upload: "",
                    };
                    this.upLoadFileList = [];
                }
            },
        },
        search(val){
            if(!val){
                this.getListResource()
            }else{
                let arr =  this.fileList.filter(function(file){
                    if(file.name.indexOf(val) > -1){
                        return file;
                    }  
                })
                this.fileList = arr;
            }
            //清空选中
            this.current = '';
            this.shareCoupon = false;
        }
    },
    methods: {
        pageClick(){
            this.shareCoupon = false;
            this.current = '';
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
                        this.pageNum += 1;
                        this.getListResource();
                    }
                }
            };
        },
        //获取列表
        getListResource() {
            this.loadingService = true;
            listResource(this.fileInfo)
                .then((res) => {
                    this.loadingService = false;
                    if (res.code === 200) {
                        this.fileList = res.data;
                    }
                })
                .catch((err) => {
                    console.log(err);
                    this.loadingService = false;
                });
        },
        //返回上一级
        reback() {
            this.documentIdList.splice(this.documentIdList.length - 1, 1);
            this.fileInfo.parentId =
                this.documentIdList[this.documentIdList.length - 1];
            this.getListResource();
        },
        handleClose(){
            this.showHandle = false;
        },  
        closeShare() {
            this.shareCoupon = false;
            this.current = '';
        },
        //点击文件夹详情
        toDetail(data) {
            this.current = '';
            this.shareCoupon = false;
            if (data.type === "1") {
                this.documentIdList.push(data.id);
                this.fileInfo.parentId = data.id;
                this.getListResource();
            } else {
                this.getResourceInfo(data.id);
                this.shareDocumentId = data.id;
            }
        },
        toBanshi(){
            if(this.dataPrepare.type == '1'){
                this.$message.warning("正在开发中...");
            }else{
                this.tableradioGroup = {};
                this.showHandle = true;

            }
        },
        //获取资源详情
        getResourceInfo(id) {
            getResourceInfo({ id: id })
                .then((res) => {
                    if (res.code === 200) {
                        this.preViewUrl = res.data.fastdfsUploadUrl;
                        if (
                            /\.(pdf|PDF)$/.test(this.preViewUrl)
                        ) {
                            // this.showPreview = true;
                            // this.preType = 'pdf';
                            android.openPdf(this.preViewUrl);
                        } else if(/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(this.preViewUrl)){
                            this.preType = 'img';
                            this.showPreview = true;
                        }else {
                            window.open(this.preViewUrl);
                        }
                    }
                })
                .catch((err) => {
                    this.$message.warning("资源详情获取失败");
                });
        },
        //打开分享帮办弹窗
        shareAssistant() {
            this.showShare = true;
        },
        //添加资料、文件夹
        addDocument(type) {
            this.current = '';
            this.shareCoupon = false;
            this.showAddDocument = true;
            this.documentInfo.type = type;
        },
        compliteNext() {
            this.$refs["documentInfo"].validate((valid) => {
                if (valid) {
                    const params = {
                        id: this.isEdit ? this.editData.id : "",
                        type: this.documentInfo.type,
                        name: this.documentInfo.name,
                        parentId: this.fileInfo.parentId,
                        fileName: this.documentInfo.fileName,
                        resourceInfo:
                            this.documentInfo.type === "2" ? this.resourceInfo : "",
                    };
                    saveResourceInfo(params).then((res) => {
                        if (res.code === 200) {
                            this.showAddDocument = false;
                            // if (this.fileInfo.parentId) {
                            //   this.fileInfo.name = this.documentInfo.name;
                            // }
                            this.getListResource();
                        }
                    });
                }
            });
        },
        toSelect(data, index) {
            console.log(data);
            this.fileData = data;
            this.fileType = data.type;
            if (index != this.current) {
                this.current = index;
                this.dataPrepare = data;
                this.shareCoupon = true;
            } else {
                this.current = '';
                this.dataPrepare = {};
                this.shareCoupon = false;
            }
        },
        /** 上传附件 */
        uploadFiles(file) {
            const loading = this.$loading({
                lock: true,
                text: "正在上传文件",
                spinner: "el-icon-loading",
                background: "rgba(0, 0, 0, 0.7)",
            });
            let formData = new FormData();
            formData.append("file", file.file);
            return uploadFile(formData)
                .then((res) => {
                    if (res.code === 200) {
                        this.resourceInfo = res.data.filePath;
                        this.documentInfo.upload = res.data.filePath;
                        this.documentInfo.fileName = file.file.name;
                        loading.close();
                    }
                })
                .catch((err) => {
                    loading.close();
                    this.uploadError();
                });
        },
        //上传之前
        beforeUpload(file) {
            if (
                file.name.indexOf("%00") > -1 ||
                file.name.indexOf("./") > -1 ||
                file.name.indexOf(".\\") > -1
            ) {
                this.msgError("上传文件名称非法！");
                return false;
            }
            if (
                !/\.(gif|jpg|jpeg|png|bmp|GIF|JPG|PNG|swf|flv|mp3|wav|wma|wmv|mid|avi|mpg|asf|rm|fmvb|mp4|mp5|doc|docx|xls|xlsx|ppt|txt|zip|rar|gz|bz2|pdf|PDF)$/.test(
                    file.name
                )
            ) {
                this.$message.warning("上传文件格式错误");
                return false;
            }
            const fileSize = file.size;
            if (0 == fileSize) {
                this.msgError("上传文件不能为空！");
                return false;
            }
            const isLt2M = file.size / 1024 / 1024 < 30;
            if (!isLt2M) {
                this.msgError("上传文件大小不能超过 30MB！");
            }
            return isLt2M;
        },
        //失败后返回
        uploadError(resp) {
            // this.$message.error("文件上传失败");
        },
        //修改资料
        editDoc() {
            let data = this.dataPrepare;
            this.isEdit = true;
            this.editData = data;
            console.log(data);
            this.upLoadFileList = [];
            this.showAddDocument = true;
            this.documentInfo = {
                name: data.name,
                type: data.type,
                upload: data.resourceInfo,
            };
            const obj = { name: data.fileName, url: data.resourceInfo };
            this.upLoadFileList.push(obj);
        },
        deleteDoc() {
            let data = this.dataPrepare;
            this.$confirm("确定删除该文件?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            })
                .then(() => {
                    this.deleteResource(data);
                })
                .catch((err) => {
                    console.log(err);
                    this.dataPrepare = [];
                    this.current = '';
                });
        },
        deleteResource(data) {
            deleteResource({ ids: data.id }).then((res) => {
                if (res.code === 200) {
                    this.$message.success("已成功删除");
                    this.getListResource();
                }
            });
        },
        //分享帮办人员
        shareData(val) {
            const params = {
                id: this.dataPrepare.id,
                workUserIds: "",
            };
            val.forEach((item, index) => {
                if (index === val.length - 1) {
                    params.workUserIds += item.id;
                } else {
                    params.workUserIds += item.id + ",";
                }
            });
            if (!params.workUserIds) {
                this.$message.warning("请选择需要分享的帮办人员");
                return;
            }
            shareResource(params)
                .then((res) => {
                    if (res.code === 200) {
                        this.$message.success("资料分享成功");
                        this.showShare = false;
                    }
                })
                .catch((err) => {
                    console.log(err);
                });
        },
        // 打开详情
        toFileDetail() {
            this.$router.push({
                name: 'detail'
            })
        },
        // 添加资料
        addMaterial() {
            this.showMaterialAdd = true;
        },

        // 打开帮办
        toHelp() {
            this.showShare = true;
            this.chooseAssistant = [];  //清空帮办人员选中
        },
        shareClose() {
            this.showShare = false;
            this.chooseAssistant = [];  //清空帮办人员选中
        },
        // 关闭添加资料弹窗
        closeAdd() {
            this.showMaterialAdd = false;

        }
    }
}
</script>
<style lang="scss" scoped>
@import './index.scss';
.previewArea{
    img{
        max-width: 100%;
    }
}
.addDocumentDialog {
    ::v-deep .el-dialog {
        height: 75vh;

        .el-dialog__body {
            height: calc(100% - 13.75rem);
        }
    }
}
</style>