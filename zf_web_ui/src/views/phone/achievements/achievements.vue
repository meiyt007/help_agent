<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-09-23 13:07:46
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-10-13 13:33:31
 * @FilePath: \zf_web_ui\src\views\pc\roundTableAssistant\components\stagingServiceList.vue
 * @Description: 绩效列表
-->
<template>
    <div class="app-container">
        <div class="tab-header">
            <el-form :model="searchParams" ref="searchForm" label-width="110px">
                <el-form-item label="加分项目:">
                    <el-select v-model="searchParams.plusProjectOid" placeholder="请选择加分项目" clearable>
                        <el-option v-for="item in projectOptions" :key="item.name" :label="item.name" :value="item.id">{{item.name}}
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="审核状态:">
                    <el-select v-model="searchParams.auditStatus" placeholder="请选择审核状态" clearable>
                        <el-option v-for="item in statusOptions" :key="item.name" :label="item.name" :value="item.id">{{item.name}}
                        </el-option>
                    </el-select>
                    <!-- <el-input v-model="searchParams.phone"></el-input> -->
                </el-form-item>
                <el-form-item label="帮办人员姓名:">
                     <el-input v-model="searchParams.workUserName"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button @click="getQueryHelpServiceHistoryList" type="primary" icon="el-icon-search"
                        size="medium">搜索</el-button>
                    <el-button @click="addHandle" type="primary" icon="el-icon-plus"
                        size="medium">绩效录入</el-button>
                    <el-button size="medium" @click="resetData" icon="el-icon-refresh-right">重置</el-button>
                </el-form-item>
            </el-form>
        </div>
        <div class="tableArea" ref="tableArea" v-loading="loadingTableData">
            <el-table ref="table" :height="tableHeight" :data="stagingServiceList"
                :cell-style="{ 'text-align': 'center' }" :header-cell-style="{ 'text-align': 'center' }" border>
                <el-table-column type="index" width="50" label="序号">
                </el-table-column>
                <template v-for="(item, index) in columnList">
                    <el-table-column v-if="item.prop === 'operate'" :key="index" :label="item.label" :prop="item.prop"
                        width="250">
                        <template slot-scope="scope">
                            <div class="operateArea">
                                <p @click="lookDetail(scope.row)">查看</p>
                                <p v-if="scope.row.auditStatus != '1' && scope.row.currentHaLoginUserId == scope.row.workUserOid" @click="editDetail(scope.row)">修改</p>
                                <p v-if="scope.row.auditStatus != '1' && scope.row.currentHaLoginUserId == scope.row.workUserOid" @click="cancelDetail(scope.row)">删除</p><!-- v-if="scope.row.groupLeaderId === scope.row.workUserOid"-->
                                <p v-if="scope.row.auditStatus != '1' && scope.row.groupPost === '1'" @click="verifyDetail(scope.row)">审核</p>
                            </div>
                        </template>
                    </el-table-column>

                    <el-table-column v-else-if="item.prop === 'auditDate'" :key="index" :label="item.label" :prop="item.prop" width="160">
                        <template slot-scope="scope">
                            <div v-if="scope.row.auditDate == null" class="operateArea">/</div>
                            <div v-else class="operateArea">{{scope.row.auditDate}}</div>
                        </template>
                    </el-table-column>

                    <el-table-column v-else-if="item.prop === 'createDate'" :key="index" :label="item.label" :prop="item.prop" width="180">
                    </el-table-column>

                    <el-table-column v-else-if=" item.prop === 'statusText'" :key="index" :label="item.label" :prop="item.prop" width="100">
                        <template slot-scope="scope">
                            <div :class="'status'+scope.row.auditStatus">{{scope.row.statusText}}</div>
                        </template>
                    </el-table-column>
                    <el-table-column v-else :key="index" :label="item.label" :prop="item.prop" show-overflow-tooltip width="130">
                    </el-table-column>
                </template>
            </el-table>
        </div>
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pageNum"
            :page-sizes="[10, 20, 30, 50, 100]" :page-size="pageSize" layout="total, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
        <el-dialog title="办件信息" :visible.sync="handlingInformation" width="90%" class="guidelinesForHandlingAffairs"
            v-dialogDrag>
            <handlingInformation v-if="handlingInformation" :caseOid="caseOid" :useInfo="useInfo">
            </handlingInformation>
        </el-dialog>
        <el-dialog :title="statusTitle" :visible.sync="editAchievement" width="90%" class="guidelinesForHandlingAffairs"
            v-dialogDrag>
            <div class="achievement">
                <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="120px" class="demo-ruleForm">
                    <el-form-item label="加分项目" prop="plusProjectOid">
                        <el-select :disabled="statusText == 'look' " v-model="ruleForm.plusProjectOid" placeholder="请选择加分项目">
                            <el-option v-for="item in projectOptions" :key="item.name" :label="item.name" :value="item.id">{{item.name}}
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="加分时长(分钟)" prop="plusDuration">
                        <el-input :disabled="statusText == 'look' " v-model="ruleForm.plusDuration"></el-input>
                    </el-form-item>
                    <el-form-item label="附件上传">
                        <div class="list">
                            <div class="item" v-if="ruleForm.fastdfsNginxUrl">
                                <el-image v-if="ruleForm.preType == 'img'" style="width: 100px; height: 100px" :src="ruleForm.fastdfsNginxUrl" 
                                    :preview-src-list="srcList" @click="handlePictureCardPreview(ruleForm.fastdfsNginxUrl)">
                                </el-image>
                                <el-image v-else style="width: 100px; height: 100px" :src="require('@/assets/images/home/folder.png')">
                                </el-image>
                                <div class="list-btns centerXY">
                                    <el-upload v-if="statusText != 'look'" ref="upload" class="uploadFileList" action :before-upload="beforeUpload" :http-request="uploadFiles">
                                        <i title="重新上传" class="el-icon-upload2"></i>
                                    </el-upload>
                                    <i v-if="ruleForm.preType == 'img'" @click="viewPdf(ruleForm.fastdfsNginxUrl)" title="预览" class="el-icon-view"></i>
                                    <i v-else-if="ruleForm.preType == 'pdf'" @click="viewPdf(ruleForm.fastdfsNginxUrl)" title="预览" class="el-icon-view"></i>
                                    <a v-else :href="ruleForm.fastdfsNginxUrl"><i  title="预览" class="el-icon-view"></i></a>
                                    <i v-if="statusText != 'look'" @click="cancelFile" title="删除" class="el-icon-delete"></i>
                                </div>
                            </div>
                        </div>
                        <el-upload ref="upload" class="uploadFileList" action :before-upload="beforeUpload"
                                :http-request="uploadFiles">
                                <el-button :disabled="statusText == 'look' " size="small" type="primary"><i class="el-icon-upload"></i> 点击上传</el-button>
                            </el-upload>
                    </el-form-item>
                    <el-form-item label="审核组长" prop="groupLeaderId">
                        <el-select :disabled="statusText == 'look' " v-model="ruleForm.groupLeaderId" placeholder="请选择审核组长">
                            <el-option v-for="item in groupOptions" :key="item.id" :label="item.name" :value="item.id">{{item.name}}
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="加分情况说明" v-if="statusText != 'verify'" prop="bonusNotes">
                        <el-input :disabled="statusText == 'look' " type="textarea" maxlength="100" show-word-limit v-model="ruleForm.bonusNotes"></el-input>
                    </el-form-item>
                    <el-form-item label="审核结果" v-if="statusText == 'verify'" prop="auditStatus">
                        <el-radio-group v-model="ruleForm.auditStatus">
                            <el-radio-button :key="item.id" :label="item.id" :value="item.id" v-for="item in  statusOptions">{{ item.name }}</el-radio-button>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="审核原因" v-if="statusText == 'verify'"  prop="auditReason">
                        <el-input type="textarea" maxlength="100" show-word-limit v-model="ruleForm.auditReason"></el-input>
                    </el-form-item>
                    <el-form-item label="创建时间" v-if="statusText == 'look'" prop="createDate">
                        <el-input :disabled="statusText == 'look' " v-model="ruleForm.createDate"></el-input>
                    </el-form-item>
                    <el-form-item label="审核时间" v-if="statusText == 'look'" prop="auditDate">
                        <el-input :disabled="statusText == 'look' " v-model="ruleForm.auditDate"></el-input>
                    </el-form-item>

                    <el-form-item v-if="statusText != 'look'" class="form-btn">
                        <el-button type="primary" @click="submitForm('ruleForm')">确定</el-button>
                        <el-button @click="editAchievement = false">取 消</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </el-dialog>
        <el-dialog v-dialog-drag title="预览" :visible.sync="showPreview" class="assistantNoticeDialog" width="80%">
            <div class="previewArea">
                <iframe v-if="ruleForm.preType == 'pdf'" :src="preViewUrl" frameborder="0"
                    style="height: 70vh; width: 100%"></iframe>
                <img style="max-width:100%;" v-if="ruleForm.preType == 'img'" :src="ruleForm.fastdfsNginxUrl" alt="">
            </div>
        </el-dialog>
    </div>
</template>
<script>
// import { queryHelpServiceHistoryList } from "@/api/modules/workingGroup";
import { 
    getTempServiceList,
    queryGroupLeaderInfoList,
    queryHaPlusProjectList,
    queryHaPerformancePlustimeRecordPageResult,
    saveHaPerformancePlustimeRecord,
    getHaPerformancePlustimeRecordById,
    deleteHaPerformancePlustimeRecordById,
    groupLeaderAuditHaPerformancePlustimeRecord 
} from "@/api/modules/helpAgent";
import {
    outerUploadCaseMaterialFile
} from "@/api/modules/business.js";
export default {
    name: "achievements",
    data() {
        return {
            statusText:'',
            statusTitle:'',  
            preViewUrl:'',
            showPreview:false,
            projectOptions:[],
            groupOptions:[],
            statusOptions:[
                {
                    id:0,
                    name:'待审核'
                },
                {
                    id:1,
                    name:'审核通过'
                },
                {
                    id:2,
                    name:'审核不通过'
                },
            ],
            loadingTableData: false,
            stagingServiceList: [],
            tableHeight: 200,
            searchParams: {
                plusProjectOid: "",
                auditStatus: "",
                workUserName: "",
            },
            columnList: [
                { label: "帮办人员姓名", prop: "workUserName" },
                { label: "加分时长（分钟）", prop: "plusDuration" },
                { label: "审核状态", prop: "statusText" },
                { label: "审核组长", prop: "groupLeaderName" },
                { label: "创建时间", prop: "createDate" },
                { label: "审核时间", prop: "auditDate" },
                { label: "操作", prop: "operate" },
            ],
            pageNum: 1,
            pageSize: 10,
            total: 0,
            handlingInformation: false,
            dateValue: "",
            caseOid: "",
            useInfo: {},
            editAchievement: false,
            ruleForm: {
                id:'',
                plusProjectOid: '',
                plusDuration: '',
                attaOid: '',
                groupLeaderId: '',
                groupLeaderName:'',
                bonusNotes: '',
                fastdfsUploadUrl:'',
                fastdfsNginxUrl:'',
                auditStatus:'',
                auditReason:'',
                preType:'',
                createDate:'',
                auditDate:''
            },
            rules: {
                plusProjectOid: [
                    { required: true, message: '请选择加分项目', trigger: 'change' }
                ],
                plusDuration: [
                    { required: true, message: '请填写加分时长', trigger: 'blur' }
                ],
                groupLeaderId: [
                    { required: true, message: '请选择组长', trigger: 'change' }
                ],
                bonusNotes: [
                    { required: true, message: '请填写加分情况说明', trigger: 'blur' }
                ]
            },
            qlCaseMaterialAttaList: [],
            srcList: [], //预览列表
            url: '',
        };
    },
    computed: {
        staffInformation() {
            return this.$store.state.pageData.staffInformation;
        },
    },
    mounted() {
        this.project();  
        this.group();
        this.getQueryHelpServiceHistoryList();
    },
    methods: {
        viewPdf(url){
            this.showPreview = true;
            this.preViewUrl = url;
        },
        //新增绩效
        addHandle(){
            this.statusText = 'add';
            this.statusTitle = '新增绩效';
            this.ruleForm =  {
                id:'',
                plusProjectOid: '',
                plusDuration: '',
                attaOid: '',
                groupLeaderid: '',
                groupLeaderName:'',
                bonusNotes: '',
                fastdfsUploadUrl:'',
                auditStatus:'',
                auditReason:'',
                fastdfsNginxUrl:'',
                createDate:'',
                auditDate:''
                
            };
            // http://139.9.123.180/group1/M00/01/C5/iwl7tGOcHriAPAMfAAhfu9TA2tk274.png
            this.editAchievement = true;
        },
        //获取全部的加分项目列表
        group(){
            queryGroupLeaderInfoList().then((res)=>{
                // console.log(res)   
                if(res.data.length){
                    this.groupOptions = res.data;
                }
            })
        },
        //获取全部的加分项目列表
        project(){
            queryHaPlusProjectList().then((res)=>{
                // console.log(res)   
                if(res.data.length){
                    this.projectOptions = res.data;
                }
            })
        },
        //删除文件
        cancelImg(index) {
            this.qlCaseMaterialAttaList.splice(index, 1);
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
            const fileSize = file.size;
            if (0 == fileSize) {
                this.msgError("上传文件不能为空！");
                return false;
            }
            if (!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG|pdf|PDF)$/.test(file.name)) {
                this.$message.warning("只允许上传图片和pdf文件");
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
            this.$message.error(resp);
        },
        /** 上传附件 */
        uploadFiles(file) {
            this.loading = this.$loading({
                lock: true,
                text: '文件上传中',
                spinner: 'el-icon-loading',
                background: 'rgba(0, 0, 0, 0.7)'
            });
            const that = this;
            let formData = new FormData();
            formData.append("files", file.file);
            outerUploadCaseMaterialFile(formData)
                .then((res) => {
                    that.loading.close();
                    if (res.code == 200 && res.data.length > 0) {
                        that.ruleForm.attaOid = res.data[0].attaOid;
                        that.ruleForm.fastdfsNginxUrl = res.data[0].fastdfsNginxUrl;
                        that.ruleForm.fastdfsUploadUrl = res.data[0].fastdfsUploadUrl;
                        if (
                            /\.(pdf|PDF)$/.test(that.ruleForm.fastdfsNginxUrl)
                        ) {
                            that.ruleForm['preType'] = 'pdf';
                        } else if (
                            /\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(that.ruleForm.fastdfsNginxUrl)
                        ) {
                            that.ruleForm['preType'] = 'img';
                        } else{
                            that.ruleForm['preType'] = 'download';
                        }
                        that.$forceUpdate();
                    }else{
                        that.$message.warning("上传失败，请重新上传。");
                    }
                })
                .catch((err) => {
                    that.loading.close();
                    console.log(err);
                    that.uploadError();
                });
        },
        //预览图片
        handlePictureCardPreview(url) {
            this.srcList = []
            this.srcList.push(url)
        },
        resetFile() {
            this.$refs.upload.clearFiles();
            this.qlCaseMaterialAttaList = [];
        },
        submitForm(formName) {
            let that = this;
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    if(that.statusText == 'edit' || that.statusText == 'add'){
                        saveHaPerformancePlustimeRecord(that.ruleForm).then((res)=>{
                            if(res.code == 200){
                                that.$message.success("提交成功");
                                that.editAchievement = false;
                                that.getQueryHelpServiceHistoryList();
                                that.$refs['ruleForm'].resetFields();
                            }
                        })
                    }else{
                        groupLeaderAuditHaPerformancePlustimeRecord({
                            id:that.ruleForm.id,
                            auditStatus:that.ruleForm.auditStatus,
                            auditReason:that.ruleForm.auditReason
                        }).then((res)=>{
                            if(res.code == 200){
                                that.$message.success("审核成功");
                                that.editAchievement = false;
                                that.getQueryHelpServiceHistoryList();
                                that.$refs['ruleForm'].resetFields();
                            }
                        })
                    }
                    
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
        },
        //切换时间
        changeDate() {
            this.searchParams.beginTime = this.dateValue[0];
            this.searchParams.endTime = this.dateValue[1];
        },
        //查看暂存办件详情
        seeDetail(row) {
            this.handlingInformation = true;
            this.useInfo = row;
            this.caseOid = row.qlCaseId;
        },
        //删除文件
        cancelFile(){
            let that = this;
            this.$confirm("确定删除该文件?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
                appendToBody: false,
            }) .then(() => {
                that.ruleForm.fastdfsUploadUrl = '';
                that.ruleForm.fastdfsNginxUrl = '';
            })
            .catch((err) => {
                console.log(err);
            });
        },
        //删除办件
        cancelDetail(row){
            let that = this;
            this.$confirm("确定删除?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
                appendToBody: false,
            }) .then(() => {
                deleteHaPerformancePlustimeRecordById({id:row.id}).then((res)=>{
                    if(res.code == 200){
                        that.$message.success("删除成功");
                        that.getQueryHelpServiceHistoryList();
                    }
                })
            })
            .catch((err) => {
                console.log(err);
            });
        },
        //审核绩效
        verifyDetail(row){
            this.ruleForm.auditStatus = '';
            this.ruleForm.auditReason = '';
            this.ruleForm = row;
            this.statusText = 'verify';
            this.statusTitle = '审核绩效';
            this.editAchievement = true;
        },
        lookDetail(row){
            this.ruleForm = row;
            this.statusText = 'look';
            this.statusTitle = '查看绩效';
            this.editAchievement = true;
        },
        editDetail(row) {
            this.ruleForm = row;
            this.statusText = 'edit';
            this.statusTitle = '编辑绩效';
            this.editAchievement = true;
        },
        //继续办件
        proceedStep(row) {
            this.$emit("setProceedStepData", row);
        },

        resetData() {
            this.searchParams = {
                plusProjectOid: "",
                auditStatus: "",
            };
            this.pageNum = 1;
            this.pageSize = 10;
            this.getQueryHelpServiceHistoryList();
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
            const params = {
                plusProjectOid: this.searchParams.plusProjectOid,
                auditStatus: this.searchParams.auditStatus,
                workUserName: this.searchParams.workUserName,
                pageNum: this.pageNum,
                pageSize: this.pageSize,
            };
            this.loadingTableData = true;
            queryHaPerformancePlustimeRecordPageResult(params)
                .then((res) => {
                    this.loadingTableData = false;
                    if (res.code === 200) {
                        this.tableHeight = this.$refs["tableArea"].clientHeight;
                        res.data.data.forEach(function(item){
                            if(item.auditStatus == '0'){
                                item['statusText'] = '待审核';
                            } else if(item.auditStatus == '1'){
                                item['statusText'] = '审核通过';
                            }else{
                                item['statusText'] = '审核不通过';
                            }
                            if (
                                /\.(pdf|PDF)$/.test(item.fastdfsNginxUrl)
                            ) {
                                item['preType'] = 'pdf';
                            } else if (
                                /\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(item.fastdfsNginxUrl)
                            ) {
                                item['preType'] = 'img';
                            } else{
                                item['preType'] = 'download';
                            }
                        })
                        this.stagingServiceList = res.data.data;
                        
                        this.total = res.data.total;
                        
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
    @import './achievement.scss';
</style>
  