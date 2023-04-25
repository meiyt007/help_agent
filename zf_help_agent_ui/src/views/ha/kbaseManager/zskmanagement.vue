/** * @Author: wangns */
<!-- 请求条件 -->
<template>
  <div class="app-container">
    <!--用户数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="80px"
    >
      <el-form-item label="文件名称" prop="fileName">
        <el-input
          v-model.trim="queryParams.fileName"
          placeholder="请输入标题"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="isDelete">
                <el-col :span="24">
                  <el-select
                    v-model.trim="queryParams.isDelete"
                    placeholder="请选择状态类型"
                    size="small" >
                    <el-option label="未删除" value="0"></el-option>
                    <el-option label="已删除" value="1"></el-option>
                  </el-select>
                </el-col>
      </el-form-item>
      <el-form-item class="fr mr0">
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button
          type="warning"
          icon="el-icon-refresh"
          size="mini"
          @click="resetQuery"
          class="btn-reset"
          >重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 工具条 -->
    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit"
          v-hasPermi="['im:banner:save']"
        >新增</el-button
        >
      </el-col>
    </el-row>
     <!-- 表头 -->
    <el-table
      v-loading="loading"
      :data="zskList"
      stripe
      style="width: 100%"
    >
      <el-table-column type="selection" width="55"/>
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="文件名称"
        align="center"
        prop="fileName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="所属事项"
        align="center"
        prop="serviceOid"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="附件地址"
        align="center"
        prop="fastdfsNginxUrl"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="附件扩展名"
        align="center"
        prop="extensionName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="创建时间"
        align="center"
        prop="uploadDate"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="操作"
        align="center"
        width="340"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['im:banner:view']"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            v-hasPermi="['im:banner:update']"
          >修改</el-button>
          <el-button
             size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
             @click="removeZSKById(scope.row.id)"
             v-hasPermi="['im:banner:delete']">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNumber"
      :limit.sync="queryParams.pageSize"
      style="padding: 30px 0; text-align: center;"
      layout="total, sizes, prev, pager, next, jumper"
      @pagination="getList"
    />

<!-- 信息详细 -->
     <!-- 添加或修改应用信息对话框 -->
     <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openInit"
      width="900px"
      append-to-body
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="0px"
        class="demo-ruleForm"
      >
        <el-input v-show="false" />
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><i class="require">*</i><b>名称：</b></td>
            <td>
              <el-form-item prop="fileName">
                <el-input
                  v-model.trim="form.fileName"
                  placeholder="请输入名称"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>知识库类型：</b></td>
            <td>
              <el-form-item prop="kbType">
                <el-col :span="24">
                  <el-select
                    v-model.trim="form.kbType"
                    placeholder="请选知识库类型"
                    size="small">
                    <el-option label="文件" value="1"></el-option>
                    <el-option label="文字" value="2"></el-option>
                  </el-select>
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr v-if="form.kbType!=='2'">
            <td><i class="require">*</i><b>附件扩展名：</b></td>
            <td>
              <el-form-item prop="extensionName">
                <el-input
                  v-model.trim="form.extensionName"
                  placeholder="请输入跳转地址"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>所属事项：</b></td>
            <td>
            <template>
              <el-select v-model.trim="form.serviceOid" filterable placeholder="请选择">
                <el-option
                  v-for="item in serviceIdOptions"
                  :key="item.serviceOid"
                  :label="item.serviceName"
                  :value="item.serviceOid">
                </el-option>
              </el-select>
            </template>
            </td>
          </tr>
          <tr v-if="form.kbType!=='2'">
            <td><i class="require">*</i><b>文件上传：</b></td>
            <td>
              <el-form-item prop="fastdfsNginxUrl">
                  <el-upload
                    class="upload-demo"
                    action
                    :multiple="false"
                    :http-request="uploadFiles"
                    :before-upload="beforeUpload"
                    :on-error="uploadError"
                    :file-list="fileList"
                    :on-change="handleChange"
                    :show-file-list="false"
                    accept="accept"
                  >
                    <el-button size="mini" type="primary" icon="el-icon-upload">点击上传</el-button>
                  </el-upload>
                  <div v-if="form.fastdfsNginxUrl">
                    <span
                      v-if="
                        form.fastdfsNginxUrl != null &&
                        form.fastdfsNginxUrl != ''
                      "
                    >
                      {{
                        form.fileName
                      }}
                    </span>
                    <el-link
                      type="primary"
                      @click="
                        downLoadFileUrl(form.fastdfsNginxUrl)
                      "
                    >下载</el-link>|
                    <el-link
                      type="primary"
                      @click="
                        delresultFile(form.fastdfsNginxUrl)
                      "
                    >删除</el-link>
                  </div>
                </el-form-item>
            </td>
          </tr>
          <tr v-if="form.kbType ==='2'">
            <td><i class="require">*</i><b>答案纯文本：</b></td>
            <td colspan="3">
              <el-form-item prop="answerPureText">
                <el-input
                  v-model.trim="form.answerPureText"
                  placeholder="请输入文本"
                  maxlength="5000"
                  show-word-limit
                  type="textarea"
                />
              </el-form-item>
            </td>
          </tr>
          <tr v-if="form.kbType ==='2'">
            <td><i class="require">*</i><b>答案富文本：</b></td>
            <td colspan="3">
              <el-form-item prop="answeRrichText">
                <el-input
                  v-model.trim="form.answeRrichText"
                  placeholder="请输入文本"
                  maxlength="5000"
                  show-word-limit
                  type="textarea"
                />
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" align="center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

     <!-- 查看ZSK信息 -->
     <el-dialog
      title="查看"
      :close-on-click-modal="false"
      :visible.sync="openView"
      width="900px"
      append-to-body
      height="800px"
      scrollbar
      >
      <div class="zf-zc-table--title">知识库信息</div>
      <table
        cellspacing="0"
        cellpadding="0"
        border="0"
        class="zf-zc-table"
      >
          <colgroup>
            <col width="20%" />
            <col width="20%" />
            <col width="20%" />
            <col width="20%" />
            <col width="20%" />
          </colgroup>
          <tr>
            <td><b>文件名称：</b></td>
            <td>
              {{ middleZSK.fileName }}
            </td>
            <td><b>所属事项主键：</b></td>
            <td>
              {{ middleZSK.serviceOid }}
            </td>
          </tr>
          <tr>
            <td><b>附件地址：</b></td>
            <td>
              {{ middleZSK.fastdfsNginxUrl }}
            </td>
            <td><b>附件扩展名：</b></td>
            <td>
              {{ middleZSK.extensionName}}
            </td>
          </tr>
        </table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { page ,deleteZSKById,initZsk,saveOrUpdateZsk,uploadFile,queryServiceList  } from "@/api/ha/zskmanagement/zskmanagement.js";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {

  data() {
    // 验证帮代办类型的函数
    // let validateName = (rule, value, callback) => {
    //   // 当帮代办类型为空值且为必填时，抛出错误，反之通过校验
    //   if (this.form.kbType === undefined || this.form.kbType === '' && this.userTypeFla) {
    //     callback(new Error('必填项'))
    //   } else {
    //     callback()
    //   }
    // }
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      zskList: [],
      serviceIdOptions: [], //事项集合
      // 新增或修改显示弹出层
      title: "",
      openInit: false,
      // 查看显示弹出层
      openView: false,
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
        fileName: "",
        serviceOid: "",
        isDelete: "",
      },
      // 表单参数
      form: {
        oid: "",
        fastdfsNginxUrl: "",
        fileName: "",
      },

      // 表单校验
      rules: {
        fileName: [
          { required: true, message: "必填项", trigger: "blur" }
        ],
        // serviceOid: [
        //   { required: true, message: '必填项', trigger: 'blur' },
        //   // { validator:validatePhone, trigger: 'blur' }
        // ],
        extensionName: [
          {required:true,message:'必填项',trigger:"blur"}
        ],
        kbType: [{required:true,message:'必填项',trigger:"blur"}],
        answerPureText: [
          {required:true,message:'必填项',trigger:"blur"}
        ],
        answeRrichText: [
          {required:true,message:'必填项',trigger:"blur"}
        ],
        
      },
      //查看
      middleZSK:{},
      fileList: [],
      uploadFileUrl: process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/knowledge/uploadFile',
      // uploadFileUrl: 'http://127.0.0.1:9101/work/knowledge/uploadFile',
    };
  },
  created() {
    this.getList();
  },
  // computed: {
  //   userTypeFla: function () {
  //     return this.form.kbType !== `1`;
  //   }
  // },
  methods: {
    /** 获取事项下拉数据 */
    getServiceList (id) {
      queryServiceList(id).then(response => {
        debugger
        this.serviceIdOptions = response.data;
      });
    },
    delresultFile (val) {
        this.form.fastdfsNginxUrl = "";
        this.form.fileName = "";
      },
    downLoadFileUrl(url){
        window.open(url);
      },
    //文件改变时
    handleChange (file, fileList) {
        this.fileList = fileList.slice(-1);
      },
    /** 失败后返回 */
    uploadError (resp) {
        let _that = this;
        _that.msgError("文件上传失败");
      },
    /** 上传附件请求操作 */
    beforeUpload (file) {
        let _that = this;
        let isRightSize = file.size / 1024 / 1024 < 100;
        if (!isRightSize) {
          _that.$message.error("文件大小超过 100MB");
        }
        return isRightSize;
      },

    /** 上传附件 */
    uploadFiles (file) {
        let _that = this;
        let formData = new FormData();
        formData.append("file", file.file);
        uploadFile(formData).then(respon => {
          if (respon.data) {
            _that.form.fastdfsNginxUrl = respon.data.fastdfsNginxUrl;
            _that.form.oid = respon.data.oid;
            _that.form.fileName = respon.data.fileName;
            _that.$forceUpdate();
          } else {
            _that.$message.error("上传文件失败！");
          }
        });
      },

    /** 查看按钮操作 */
    handleView (row) {
      this.reset();
      const id = row.id;
      initZsk(id).then(response => {
        console.log(response.data)
        this.middleZSK = response.data;
        this.openView = true;
        this.title = "查看应用信息";
      });
    },
     /** 提交按钮 */
     submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          saveOrUpdateZsk(this.form).then(response => {
            if (response.code === 200) {
              this.msgSuccess("保存成功");
              this.openInit = false;
              this.getList();
            }
          });
        } else {
          return false;
        }
      });
    },
    //删除
    removeZSKById(id){
        this.$confirm('此操作将该用户数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {//确定执行then方法
          //调用接口方法
           deleteZSKById(id)
           .then(response => {
               //提示信息
                this.$message({
                type: 'success',
                message: '删除成功!'
              })
               //刷新页面
               this.getList()
           })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
    },
       /** 初始化新增或修改 */
      handleInit (row) {
      const id = row.id;
      if (id === undefined) {
        this.getServiceList();
        this.form = {};
        this.openInit = true;
        this.title = "新增";
      } else {
        initZsk(id).then(response => {
          this.getServiceList();
          this.form = response.data;
          this.openInit = true;
          this.title = "修改";
        });
      }
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      debugger
      page(this.queryParams).then(response => {
        this.zskList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.openInit = false;
      // this.reset();
    },
    // 表单重置
    reset() {
      Object.assign(this.form, this.$options.data().form);
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNumber = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
  },
  
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
