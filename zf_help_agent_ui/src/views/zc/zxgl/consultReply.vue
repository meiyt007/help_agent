<!--author:liyanqing-->
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--首页-->
      <el-col :span="24" :xs="24">

        <!--提交表单-->
        <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="108px" @submit.native.prevent>
          <el-form-item label="征询办件名称" prop="caseName">
            <el-input v-model.trim="queryParams.caseName" placeholder="请输入征询办件名称" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="回复状态">
            <el-radio-group v-model="queryParams.replyStatus">
              <el-radio v-for="(status,key) in statusOptions" :key="key" :label="key">{{status}}</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置</el-button>
          </el-form-item>
        </el-form>

        <!--列表-->
        <el-table v-loading="loading" :data="consultManageList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>

          <el-table-column label="发起部门" align="center" prop="organName"/>
          <el-table-column label="发起人" align="center" prop="launchUserName"/>
          <el-table-column label="征询办件" align="center" prop="caseName"/>
          <el-table-column label="发起时间" align="center" prop="createDate"/>
          <el-table-column label="希望回复时间" align="center" prop="hopeDate" :formatter="dateFormatHope"/>

          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)" v-hasPermi="['sys:consultReply:view']" >查看</el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai"  @click="handleCancel(scope.row)" v-if="scope.row.replyStatus == 0" v-hasPermi="['sys:consultReply:reply']">回复</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
      </el-col>
    </el-row>

    <!-- 详细 -->
    <el-dialog :title="dialogTitle" :visible.sync="detailDialogShow" v-if="detailDialogShow" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="0px" class="dialog-table">
        <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <div v-if="form.replyStatus == 1" class="option-item"><i class="el-icon-s-grid"></i>征询信息</div>
          <tr>
            <td>
              <b>一件事办件：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="name">
                <el-col :span="24">
                  {{form.caseName}}
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>涉及部门：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="cardNumber">
                <el-col :span="24">
                  {{form.organName}}
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>希望回复时间：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="phone">
                <el-col :span="24">
                  {{form.hopeDate}}
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>征询内容：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="dishonestReason">
                <el-col :span="24">
                  {{form.consultContent}}
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <div v-if="form.replyStatus == 1" class="option-item"><i class="el-icon-s-grid"></i>回复信息</div>
          <tr v-if="form.replyStatus == 1">
            <td>
              <b>回复内容：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="replyContent">
                <el-col :span="24">
                  {{form.replyContent}}
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr v-if="form.replyStatus == 1">
            <td> <b>附件信息：</b></td>
            <td colspan="3">
              <el-col :span="24">
                <el-form-item label="" >
                  <div v-show="null!=form.attaOid && ''!=form.attaOid">
                    <span>{{form.attaName}}</span>
                    <el-link type="primary" @click="downloadFile(form.attaOid)">下载</el-link> |
                    <el-link type="primary" @click="viewFileNew(form.attaOid)">预览</el-link>
                  </div>
                </el-form-item>
              </el-col>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogShow = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 回复操作 -->
    <el-dialog :close-on-click-modal="false" :title="title" v-if="cancelDialogShow" :visible.sync="cancelDialogShow" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="0px" class="dialog-table">
        <table cellspacing="0" cellpadding="0" border="0">
          <colgroup>
            <col width="13%" />
            <col width="37%" />
            <col width="13%" />
            <col width="37%" />
          </colgroup>
          <tr>
            <td><i class="require">*</i>
              <b>回复内容：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="replyContent">
                <el-col :span="24">
                  <el-input type="textarea" v-model.trim="form.replyContent" placeholder="请输入回复内容" maxlength="500" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td> <b>附件：</b></td>
            <td>
              <el-col :span="24">
                <el-form-item label="" >
                  <el-upload
                    class="upload-demo"
                    :action="uploadUrl()"
                    :on-error="uploadError"
                    :before-upload="beforeUpload"
                    :file-list="fileList"
                    :on-success="uploadSuccess">
                    <el-button size="small" type="primary">点击上传<i class="iconfont zfsoft-wenjianshangchuan"></i></el-button>
                  </el-upload>
                  <div v-show="null!=form.attaOid && ''!=form.attaOid">
                    <span>{{form.attaName}}</span>
                    <el-link type="primary" @click="downloadFile(form.attaOid)">下载</el-link> |
                    <el-link type="primary" @click="viewFileNew(form.attaOid)">预览</el-link>
                  </div>
                </el-form-item>
              </el-col>

            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="reply">确 定</el-button>
        <el-button @click="() => {cancelDialogShow = false; reset()}">取 消</el-button>
      </div>
    </el-dialog>

    <!--引入文件的预览弹出层-->
    <el-dialog v-dialog-drag title="文件预览" :visible.sync="view.show" v-for="view in viewDialogOptions"
               @close="closeFileView" width="60%"  append-to-body>
      <file-view :attaOid="view.attaOid" @father-click="closeFileView"></file-view>
    </el-dialog>
  </div>
</template>

<script>
import {paged,get, reply} from "@/api/zc/zxgl/consultManage";
import {deepClone} from "@/utils";
import {uploadFile } from "@/api/sys/atta";
import fileView from '@/views/common/fileView';
var moment = require('moment');
export default {
  name: "ConsultReply",
  components: {fileView},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      consultManageList: [],
      fileList: [],
      //查看信息弹框标识
      detailDialogShow: false,
      addDialogShow: false,
      cancelDialogShow: false,
      viewDialogOptions:[],
      // 弹出层标题
      title: "",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        replyStatus: '0',
        caseName: ''
      },
      // 失信状态
      statusOptions: {'0':'未回复','1':'已回复'},
      // 表单参数
      form: {
      },
      // 表单校验
      rules: {
        replyContent: [
          { required: true, message: "请输入回复内容", trigger: "blur" }
        ]
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      paged(this.queryParams).then(response => {
        this.consultManageList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 表单重置
    reset() {
      this.form =  {
      },
      this.resetForm("form");
    },
    /** 回复按钮操作 */
    handleCancel(row) {
      let id = row.id;
      let _that = this;
      _that.reset();
      if(row.id) {
        get(id).then(response => {
          _that.cancelDialogShow = true;
          _that.form = deepClone(response.data);
        });
      }
    },
    /** 回复信息保存 */
    reply: function() {
      let _that = this;
      _that.$refs["form"].validate(valid => {
        if (valid) {
          reply(_that.form).then(response => {
            if (response.code === 200) {
              _that.msgSuccess("回复成功");
              _that.cancelDialogShow = false;
              setTimeout(() => {
                _that.getList();
              }, 10);
              _that.reset();
            }
          });
        }
      });
    },
    /** 查看按钮操作 */
    handleView(row) {
      let _that = this;
      _that.reset();
      const id = row.id;
      get(id).then(response => {
        _that.form = deepClone(response.data);
        if(_that.form.hopeDate){
          _that.form.hopeDate=_that.dateFormat(_that.form.hopeDate);
        }
        _that.detailDialogShow = true;
        _that.dialogTitle = "查看详情";
      });
    },
    //下载附件
    downloadFile(attaOid){
      this.download(attaOid);
    },
    //预览附件
    viewFileNew(attaOid){
      let item = {show:true,attaOid:attaOid};
      this.viewDialogOptions.push(item);
    },
    //关闭预览附件
    closeFileView(){
      this.viewDialogOptions.pop();
    },
    //清空附件
    clearAtta(){
      this.form.attaOid = '';
      this.form.attaName = '';
    },
    //成功后返回
    uploadSuccess(resp){
      this.form.attaOid = resp.data.oid;
      this.form.attaName = resp.data.name;
      this.fileList=[];
    },
    //上传之前
    beforeUpload(file) {
      if(file.name.indexOf("%00") > -1 || file.name.indexOf("./") > -1 || file.name.indexOf(".\\") > -1 ) {
        this.msgError('上传文件名称非法！');
        return false;
      }
      const fileSize = file.size;
      if (0 == fileSize) {
        this.msgError('上传文件不能为空！');
        return false;
      }
      const isLt2M = file.size / 1024 / 1024 < 100;
      if (!isLt2M) {
        this.msgError('上传文件大小不能超过 100MB！');
      }
      return isLt2M;
    },
    //失败后返回
    uploadError(resp){
      this.msgError("文件上传失败");
    },
    uploadUrl(){
      return uploadFile();
    },
    dateFormat(cellValue) {
      return cellValue ? moment(cellValue).format("YYYY-MM-DD"):"";
    },
    dateFormatHope(cellValue) {
      return cellValue ? moment(cellValue.hopeDate).format("YYYY-MM-DD"):"";
    },
  }
};
</script>
<style lang="scss" scoped>
.dialog-table {
  padding: 20px;
  box-sizing: border-box;
}
.dialog-table h3 {
  font-size: 14px;
  font-weight: normal;
  color: #333;
  margin: 0px 0px 10px 0px;
}
.dialog-table table {
  width: 100%;
}
.dialog-table table {
  border: 1px solid #dfe6ec;
  border-collapse: collapse;
}

.dialog-table table tr td {
  border: 1px solid #dfe6ec;
  padding: 17px 8px;
  font-size: 12px;
  color: #515a6e;
  text-align: right;
}
.dialog-table table tr td:nth-of-type(2n) {
  color: #606266;
  text-align: left;
}
.dialog-table .el-form-item {
  margin-bottom: 0;
}
.el-form-item--small .el-form-item_content {
  margin-left: 0px !important;
}
.line {
  text-align: center;
}
</style>
<style lang="scss" scoped>
.treeselect{
  width: 200px;
}
.treeselect240{
  width: 240px;
}
</style>
