/**
* @Author: wangxl
*/
<template>
  <div>
    <div>
      <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <table cellspacing="0" cellpadding="0" border="0" class='zf-zc-table'>
          <el-input v-show="false" v-model="form.id" />
          <el-input v-show="false" v-model="form.comboDirectoryOid" />
          <el-input v-show="false" v-model="form.results" />
          <el-input v-show="false" v-model="form.status" />
          <el-input v-show="false" v-model="form.delFlag" />
          <el-input v-show="false" v-model="form.createUser" />
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b><i class="require">*</i>证照名称：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="resultName">
                <el-input v-model.trim="form.resultName" placeholder="请输入证照名称" maxlength="50" show-word-limit/>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>证照样表：</b>
            </td>
            <td colspan="3">
              <el-form-item >
                <el-input v-show="false" v-model="form.resultSampleAddr" />
                <el-input v-show="false" v-model="form.resultSampleName" />
                <el-button type="success" size="mini" @click="selectAttas">证照样本附件</el-button>
                <el-button type="danger" size="mini" @click="clearAtta">清理</el-button>
                <div v-show="null!=form.resultSampleAddr && ''!=form.resultSampleAddr">
                  <span>{{form.resultSampleName}}</span>
                  <el-link type="primary" @click="downloadFile(form.resultSampleAddr)">下载</el-link> |
                  <el-link type="primary" @click="viewFile(form.resultSampleAddr)">预览</el-link>
                </div>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
    </div>
    <div slot="footer" class="zf-text-center">
      <el-button type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
    <!-- 选择配置项附件view-->
    <el-dialog v-dialog-drag title="选择证照样本附件" :visible.sync="openAttaListView" width="1100px" height='700px' scrollbar append-to-body>
      <div>
        <!-- <el-row :gutter="10" class="mb8">
          <el-col :span="1.5"> -->
            <el-upload
              class="upload-demo"
              :action="uploadUrl()"
              :on-error="uploadError"
              :file-list="fileList"
              :on-success="uploadSuccess">
              <el-button size="small" type="primary">点击上传<i class="iconfont zfsoft-wenjianshangchuan"></i></el-button>
            </el-upload>
          <!-- </el-col>
        </el-row> -->

        <el-table v-loading="loading" :data="attaList">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column label="原始文件名" align="center" prop="originName"  :show-overflow-tooltip="true"/>
          <el-table-column label="文件名" align="center" prop="name"  :show-overflow-tooltip="true"/>
          <el-table-column label="上传时间" align="center" prop="uploadDate" width="180" :show-overflow-tooltip="true"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-link type="primary" @click="selectFile(scope.row.attaOid,scope.row.originName)">选择</el-link> |
              <el-link type="primary" @click="downloadFile(scope.row.attaOid)">下载</el-link> |
              <el-link type="primary" @click="viewFile(scope.row.attaOid)">预览</el-link>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="attatotal>0"
          :total="attatotal"
          :page.sync="attaQueryParams.pageNum"
          :limit.sync="attaQueryParams.pageSize"
          @pagination="getAttaList"
        />
      </div>
      <div slot="footer" class="zf-text-center">
        <el-button @click="openAttaListView = false">关 闭</el-button>
      </div>
    </el-dialog>
    <!--引入文件的预览弹出层-->
    <el-dialog v-dialog-drag title="文件预览" :visible.sync="fileShow"  width="1100px" height='700px' scrollbar   @close="closeFileView" append-to-body>
      <combo-dire-file-view :attaOid="attaOid"  ></combo-dire-file-view>
      <div slot="footer" class="zf-text-center">
        <el-button @click="fileShow = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {saveOrupdateResult,getDireResultsByOid} from "@/api/onething/sxpz/serviceResult";
  import { pageFile, uploadFile,downloadFile } from "@/api/onething/sxpz/comboAtta";
  import comboDireFileView from "@/views/onething/sxpz/comboDirectory/comboDireFileView";
  export default {
    components: {comboDireFileView},
    name: "ServiceResult",
    //定义获取父类传过来值的格式
    props:["comboDireOid","resultOid"],
    data() {
      return {
        // 遮罩层
        loading: true,
        attatotal:0,
        fileShow:false,
        comboDirectoryOid:"",
        multipleSelection: [],
        // 表单参数
        form: {resultName:''},

        rules: {
          resultName: [
            {required: true, message: "必填项", trigger: "blur"},
            {min: 3, max: 50, message: "长度在 3 到 50 个字符", trigger: "blur"},
          ]
        },
        checkList:[],
        fileList:[],
        openAttaListView:false,
        //附件参数
        attaQueryParams: {
          pageNum: 1,
          pageSize: 10
        },
        //附件列表
        attaList:[],
        attaOid:""
      };
    },
    watch:{
    },
    created() {
      //统一证照详细信息
      this.initDirectoryResult();
    },
    methods: {
      // 取消按钮
      cancel() {
        this.reset();
        this.$emit('dialog-close');
      },
      // 表单重置
      reset() {
        this.resetForm("form")
      },
      //获取统一证照详细
      initDirectoryResult(){
        this.reset();
        if(this.resultOid){
          getDireResultsByOid(this.resultOid).then(response => {
            if(response.data.comboDirectoryResult != undefined){
              this.form = response.data.comboDirectoryResult;
            }
            this.dialogTitle = this.resultOid ? "修改" : "新增";
          });
        }
      },
      /** 提交按钮 */
      submitForm: function() {
        this.form.comboDirectoryOid = this.comboDireOid;
        this.$refs["form"].validate(valid => {
          if (valid) {
            saveOrupdateResult(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("保存成功");
                this.reset();
                this.$emit('dialog-close');
              }
            });
          }
        });
      },

      //选择配置项附件
      selectAttas(){
        this.getAttaList();
        this.openAttaListView = true ;
      },
      /** 查询附件列表 */
      getAttaList() {
        pageFile(this.attaQueryParams).then(response => {
          this.attaList = response.data.data;
          this.attatotal = response.data.total;
          this.loading = false;
        });
      },
      //选中附件
      selectFile(attaOid,attaName){
        this.form.resultSampleAddr = attaOid;
        this.form.resultSampleName = attaName;
        this.openAttaListView = false ;
      },
      //下载附件
      downloadFile(attaOid){
        this.loading = true;
        downloadFile(attaOid);
        this.loading = false;
      },
      //预览附件
      viewFile(attaOid){
        this.attaOid = attaOid;
        this.fileShow = true;
      },
      //关闭预览附件
      closeFileView(){
        this.fileShow=false;
      },
      //清空附件
      clearAtta(){
        this.form.resultSampleAddr = '';
        this.form.resultSampleName = '';
        this.$forceUpdate();
      },
      //成功后返回
      uploadSuccess(resp){
        this.fileList=[];
        this.getAttaList();
      },
      //失败后返回
      uploadError(resp){
        this.msgError("文件上传失败");
      },
      uploadUrl(){
        return uploadFile();
      }
    }
  };
</script>
