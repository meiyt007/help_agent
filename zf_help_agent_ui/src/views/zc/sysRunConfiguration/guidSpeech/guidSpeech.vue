/**
* @Author: dxl
*/
<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="108px">
      <el-row>
        <el-form-item label="引导语名称" prop="guideSpeechName">
          <el-input v-model.trim="queryParams.guideSpeechName" placeholder="请输入引导语名称" clearable size="100" @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="引导语编码" prop="guideSpeechCode">
          <el-input v-model.trim="queryParams.guideSpeechCode" placeholder="请输入引导语编码" clearable size="100" @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item class="fr mr0">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置</el-button>
        </el-form-item>
      </el-row>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="default" icon="el-icon-plus" size="mini" @click="handleInit" v-hasPermi="['sys:guideSpeech:init']">新增</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="guideSpeechList" border :fit="true">
    <el-table-column label="序号" width="55" type="index" align="center">
      <template slot-scope="scope">
        <span>{{ scope.$index + 1 }}</span>
      </template>
    </el-table-column>
    <el-table-column label="引导语名称"  align="center" prop="guideSpeechName" :show-overflow-tooltip="true"/>
    <el-table-column label="引导语编码"  align="center" prop="guideSpeechCode" :show-overflow-tooltip="true"/>
    <el-table-column label="语音生成方式" align="center"  prop="buildType" :show-overflow-tooltip="true">
      <template slot-scope="scope">
        <span v-if="scope.row.buildType==0">接口生成</span>
        <span v-if="scope.row.buildType==1">上传文件</span>
      </template>
    </el-table-column>
    <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)" v-hasPermi="['sys:guideSpeech:view']" >查看</el-button>
          <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai"  @click="handleInit(scope.row)" v-hasPermi="['sys:guideSpeech:init']">修改</el-button>
          <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDelete(scope.row)" v-hasPermi="['sys:guideSpeech:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="queryParams.total > 0" :total="queryParams.total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
    <!-- 添加或修改对话框 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="dialogTitle" v-if="addDialogShow" :visible.sync="addDialogShow" width="900px" append-to-body>
      <div>
      <el-form ref="form" :model="form" :rules="rules" label-width="0px" >
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><i class="require">*</i>
              <b>引导语名称：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="guideSpeechName">
                  <el-input v-model.trim="form.guideSpeechName" placeholder="请输入引导语名称" maxlength="100" show-word-limit />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i>
              <b>引导语编码：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="guideSpeechCode">
                  <el-input v-model.trim="form.guideSpeechCode" placeholder="请输入引导语名称" maxlength="100" show-word-limit />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i>
              <b>排序号：</b>
            </td>
            <td>
              <el-form-item prop="sort">
                  <el-input-number v-model="form.sort" :min="1" :max="9999" />
              </el-form-item>
            </td>
            <td><i class="require">*</i>
              <b>语音生成方式：</b>
            </td>
            <td>
              <el-form-item prop="buildType">
                  <el-radio-group v-model="form.buildType">
                    <el-radio :label="'0'" >接口生成</el-radio>
                    <el-radio :label="'1'" >上传文件</el-radio>
                  </el-radio-group>
              </el-form-item>
            </td>
          </tr>
          <tr v-if="form.buildType==0">
            <td><i class="require">*</i>
              <b>引导语内容：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="guideSpeechContent">
                <el-input type="textarea" placeholder="请输入引导语内容" v-model.trim="form.guideSpeechContent" maxlength="300" show-word-limit></el-input>
              </el-form-item>
            </td>
          </tr>
          <tr v-if="form.buildType==0">
            <td>
              <b>引导语语音地址：</b>
            </td>
            <td colspan="3" v-if="form.attaOid==null || form.attaOid==''">保存后生成</td>
            <td colspan="3" v-if="form.attaOid!=null && form.attaOid!=''">
              <el-link type="primary" @click="doPaly(form.attaOid)">点我试听</el-link>
            </td>
          </tr>
          <tr v-if="form.buildType==1">
            <td><i class="require">*</i>
              <b>语音文件：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="attaOid">
                <el-upload
                  class="upload-demo"
                  :action="uploadUrl()"
                  :on-error="uploadError"
                  :file-list="fileList"
                  accept="accept"
                  :multiple="false"
                  :before-upload="beforeUpload"
                  :on-success="uploadSuccess">
                  <el-button size="small" type="primary">点击上传<i class="iconfont zfsoft-wenjianshangchuan"></i></el-button>
                </el-upload>
                <div v-show="null!=form.attaOid && ''!=form.attaOid">
                  <span>{{form.attaName}}</span>
                  <el-link type="primary" @click="downloadFile(form.attaOid)">下载</el-link>
                </div>
              </el-form-item>(请上传mp3格式文件)</td>
          </tr>
          <tr>
            <td><b>备注：</b></td>
            <td colspan="3">
              <el-form-item prop="guideSpeechMemo">
                <el-input type="textarea" placeholder="请输入备注" v-model.trim="form.guideSpeechMemo" maxlength="500" show-word-limit></el-input>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="() => {addDialogShow = false; reset()}">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 信息详细 -->
    <el-dialog v-dialog-drag :title="dialogTitle" :visible.sync="detailDialogShow" v-if="detailDialogShow" width="800px" append-to-body>
      <div>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b>引导语名称：</b>
            </td>
            <td colspan="3">
              {{ form.guideSpeechName }}
            </td>
          </tr>
          <tr>
            <td>
              <b>引导语编码：</b>
            </td>
            <td colspan="3">
              {{ form.guideSpeechCode }}
            </td>
          </tr>
          <tr>
            <td>
              <b>排序：</b>
            </td>
            <td>
              {{ form.sort }}
            </td>
            <td>
              <b>语音生成方式：</b>
            </td>
            <td>
             <span v-if="form.buildType=='0'">接口生成</span>
              <span v-if="form.buildType=='1'">上传文件</span>
            </td>
          </tr>
          <tr v-if="form.buildType==0">
            <td>
              <b>引导语内容：</b>
            </td>
            <td colspan="3">
              {{ form.guideSpeechContent }}
            </td>
          </tr>
          <tr v-if="form.buildType==1">
            <td>
              <b>语音文件：</b>
            </td>
            <td colspan="3">
              <el-link v-if="form.attaOid" type="primary" @click="downloadFile(form.attaOid)">下载</el-link>
              <span v-else>暂无</span>
            </td>
          </tr>
          <tr>
            <td>
              <b>备注：</b>
            </td>
            <td colspan="3">
              {{ form.guideSpeechMemo }}
            </td>
          </tr>
        </table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogShow = false">关 闭</el-button>
      </div>
    </el-dialog>
    <div style="display:none;">
      <audio id="aud" controls autoplay></audio>
    </div>
  </div>
</template>

<script>
  import { page, deletes, saveOrUpdate, getOne } from "@/api/zc/sysRunConfiguration/guidSpeech";
  import {uploadFile} from "@/api/sys/atta";
  export default {
    name: "GuidSpeech",
    data() {
      return {
        // 列表数据
        guideSpeechList: [],
        fileList:[],
        // 弹窗标题
        dialogTitle: '',
        addDialogShow: false,
        detailDialogShow: false,

        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          total: 0, // 分页参数
          guideSpeechName: "",
          guideSpeechCode:""
        },

        // 表单参数
        form: {
          oid: '', //逻辑主键
          guideSpeechName: '', // 名称
          guideSpeechCode: '',  // 编号
          buildType: '0',
          guideSpeechContent: '',
          guideSpeechMemo: '',
          convertStatus: '',
          sort:0,
          attaOid: '',
        },
        // 表单校验
        rules: {
          guideSpeechName: [
            { required: true, message: "引导语名称不能为空", trigger: "blur" }
          ],
          guideSpeechCode: [
            { required: true, message: "引导语编码不能为空", trigger: "blur" }
          ],
          buildType: [
            { required: true, message: "语音生成方式不能为空", trigger: "blur" }
          ],
          guideSpeechContent: [
            { required: true, message: "引导语内容不能为空", trigger: "blur" }
          ],
          sort: [
            { required: true, message: "排序不能为空", trigger: "blur" }
          ],
        }
      };
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
        page(this.queryParams).then(response => {
          this.guideSpeechList = response.data.data;
        this.queryParams.total = response.data.total;
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
        Object.assign(this.form, this.$options.data().form)
        this.resetForm("form");
      },
      uploadUrl() {
        return uploadFile();
      },
      //失败后返回
      uploadError(resp){
        this.msgError("文件上传失败");
      },
      //成功后返回
      uploadSuccess(resp){
        this.form.attaOid = resp.data.oid;
        this.form.attaName = resp.data.name;
        this.fileList=[];
      },
      /** 上传附件请求操作 */
      beforeUpload(file) {
        console.log(file.type)
        let _that = this;
        let isRightSize = file.size / 1024 / 1024 < 100;
        if (!isRightSize) {
          _that.$message.error("文件大小超过 100MB");
          return isRightSize;
        }
        const isMp3 = file.type === 'audio/mpeg';
        if(!isMp3){
          _that.$message.error("文件格式不允许!");
          return isMp3;
        }
      },
      //下载附件
      downloadFile(attaOid){
        this.download(attaOid);
      },
      /** 查看按钮操作 */
      handleView(row) {
        let _that = this;
        _that.reset();
        const oid = row.oid;
        getOne(oid).then(response => {
          _that.form=response.data;
        _that.detailDialogShow = true;
        _that.dialogTitle = "查看详情";
      });
      },
      /** 新增和修改按钮操作 */
      handleInit(row) {
        let _that = this;
        _that.reset();
        if(row.oid) {
          getOne(row.oid).then(response => {
            _that.form=response.data;
        });
        } else{
          _that.form.dataType='0';
        }
        _that.addDialogShow = true;
        _that.dialogTitle = row.oid ? "修改" : "新增";
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        let id = row.oid;
        this.$confirm("是否确认删除?", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(function() {
            return deletes(id);
          })
          .then(() => {
          this.getList();
        this.msgSuccess("删除成功");
      })
      .catch(function() {});
      },
      /** 提交按钮 */
      submitForm: function() {
        let _that = this;
        if(_that.form.buildType=='1'){
          if(!_that.form.attaOid){
            this.$message.error("请上传文件！");
            return false;
          }
        }
        _that.$refs["form"].validate(valid => {
          if (valid) {
            saveOrUpdate(_that.form).then(response => {
              if (response.code === 200) {
              _that.msgSuccess("保存成功");
              _that.addDialogShow = false;
              setTimeout(() => {
                _that.getList();
            }, 10);
            }
          });
          }
        });
      },
      doPaly: function (attaOid){
        debugger
        var audio = document.getElementById('aud'); //用于播放合成语音
        //停止播放
        audio.pause();
        audio.currentTime = 0;
        audio.src = "/dev-api/platform/security/atta/downloadFile/"+attaOid;
        audio.autoplay = true;//播放
      },

    },
    watch: {
    },
    created() {
      this.getList();
    },
  };
</script>

