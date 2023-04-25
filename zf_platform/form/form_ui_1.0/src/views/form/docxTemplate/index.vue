<template>
  <div class="app-container" id="app-container-tep">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="模板名称"  prop="docxTemplateName">
        <el-input
          v-model="queryParams.docxTemplateName"
          placeholder="请输入模板名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="存储对象名称" label-width="120px">
         <el-input
           v-model="formObject.objectName"
           size="small"
           readonly
         />
       </el-form-item>
      <el-form-item class="fr mr0">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="ml5">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit"
        >新增</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="templateList" border>
      <!--<el-table-column type="selection" width="55" align="center"  />-->
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="模板名称" align="center" prop="docxTemplateName" :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" align="center" prop="createDate" :show-overflow-tooltip="true"/>
      <el-table-column label="状态" align="center"  width="120" prop="isAble" >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isAble"
            :active-value="1"
            :inactive-value="0"
            @change="handleAble(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />


    <!-- 添加或修改信息对话框 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="openInit" width="70%" append-to-body>
      <div class="el-table__header-wrapper dialog-table">
        <!--<h3>事项标题</h3>-->
        <el-form ref="form" :model="form" :rules="rules" label-width="0px" class="demo-ruleForm">
          <el-input v-show="false" v-model="form.id" />
          <el-input v-show="false" v-model="form.docxTemplateOid" />
          <el-input v-show="false" v-model="form.isAble" />
          <el-input v-show="false" v-model="form.isDelete" />
          <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><i class="require">*</i><b>模板名称：</b></td>
              <td colspan="3">
                <el-form-item prop="docxTemplateName">
                  <el-input v-model.trim="form.docxTemplateName" placeholder="请输入模板名称" maxlength="50" show-word-limit/>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>模板样本：</b></td>
              <td colspan="3">
                <el-form-item prop="docxAttaOid">
                  <el-input v-show="false" v-model="form.docxAttaOid" />
                  <el-upload
                    class="upload-demo"
                    accept=".docx"
                    :headers="getToken()"
                    :action="uploadUrl()"
                    :on-error="uploadError"
                    :before-upload="beforeUpload"
                    :file-list="fileList"
                    :on-success="uploadSuccess">
                    <el-button type="success" size="mini">上传模板word<i class="el-icon-upload el-icon--right"></i></el-button>
                  </el-upload>
                  <!--<el-button v-if="undefined != form.templateConfig" type="success" size="mini" @click="getTemplateImage">生成样本图片<i class="el-icon-sold-out el-icon&#45;&#45;right"></i></el-button>-->
                  <div v-show="null!=form.docxAttaOid && ''!=form.docxAttaOid">
                    <span>{{form.attaName}}</span>&nbsp;&nbsp;&nbsp;
                    <el-link type="success"  @click="downloadFile(form.docxAttaOid)">下载</el-link> |
                    <el-link type="danger" @click="clearAtta">删除</el-link>
                  </div>
                  <!-- <div style="color: red">备注：样本图片，可在设计完成后，点击生成！</div>-->
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><b>关联存储对象：</b></td>
              <td  colspan="3" class="object_td">
                <el-row :gutter="10" class="mb8">
                  <el-col :span="1.5">
                    <el-button
                      type="primary"
                      icon="el-icon-plus"
                      size="mini"
                      @click="addAppendObject"
                    >增加
                    </el-button>
                    <div style="margin-top: 8px">当前存储对象： <font color="red">{{formObject.objectName}}</font></div>
                  </el-col>
                </el-row>
                <el-table :data="form.appendObjectList">
                  <el-table-column label="序号" width="55" align="center">
                    <template slot-scope="scope">
                      <span>{{scope.$index + 1}}</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="存储对象key" width="320" >
                    <template slot-scope="scope">
                      <el-form-item :prop="'appendObjectList.' + scope.$index + '.key'"
                                    :rules="rules.appendObjectList.rules.key" style="height: 53px;">
                        <el-input placeholder="存储对象key" v-model.trim="scope.row.key" maxlength="50"
                                  show-word-limit/>
                      </el-form-item>
                    </template>
                  </el-table-column>
                  <el-table-column label="关联存储对象">
                    <template slot-scope="scope">
                      <el-form-item :prop="'appendObjectList.' + scope.$index + '.objectOid'"
                                    :rules="rules.appendObjectList.rules.objectOid" style="height: 53px;">
                        <el-select  style="width: 100%" v-model="scope.row.objectOid"
                                    clearable
                                    filterable
                                    placeholder="请选择">
                          <el-option
                            v-for="append in appendObjectOptions"
                            :key="append.objectOid"
                            :label="append.objectName"
                            :value="append.objectOid"
                          >
                          </el-option>
                        </el-select>
                      </el-form-item>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="80" align="center">
                    <template slot-scope="scope">
                      <el-form-item align="center">
                        <el-button
                          size="mini"
                          type="text"
                          icon="iconfont zfsoft-shanchu"
                          @click="deleteAppendObject(scope.$index)"
                        >删除
                        </el-button>
                      </el-form-item>
                    </template>
                  </el-table-column>
                </el-table>
              </td>
            </tr>
            <tr>
              <td><b>模板说明：</b></td>
              <td class="tipTd" colspan="3" v-pre>
                <span style="color: red" >1.模板是docx格式的Word文档，你可以使用Microsoft office、WPS Office、Pages等软件制作模板。</span>
                <br/>
                <span style="color: red" >2.所有的标签都是以{{开头,以}}结尾，标签可以出现在任何位置。</span>
                <br/>
                <span style="color: red" >3.图片标签以%开始：{{%var}}，并支持{{%var_height_width}}设置高和宽，支持多图片。如:{{%var_400_300}}</span>
                <br/>
                <span style="color: red" >4.例如 {{#goods}} 是个标准的标签，将 {{#goods}} 置于循环行的上一行，
                  循环行设置要循环的标签和内容，注意此时的标签应该使用 [] 。</span>
                <br/>
                <span style="color: red">5.区块对由前后两个标签组成，开始标签以?标识，结束标签以/标识：{{?sections}}{{/sections}}。</span>
                <br/>
                <span style="color: red">6.模板使用关联存储对象，需要使用{{key.属性}}获取对应得值。</span>
                <br/>
                <span style="color: red">7.表单图片替换模板中图片(主要用于签章)，图片格式和布局由模板指定，图片使用引用标签替换即可。使用{{属性}}不加任何前置符号。</span>

                <br/>
              </td>
            </tr>
            <tr>
              <td><b>模板示例下载：</b></td>
              <td colspan="3" >
                <el-link title="点击下载" :href="url" download="模板部分标签示例.docx" >
                  模板示例下载
                </el-link>
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>


    <!-- 查看信息详细 -->
    <el-dialog :title="title" :visible.sync="openView" width="70%" append-to-body>
      <div class="el-table__header-wrapper dialog-table">
        <!--<h3>事项标题</h3>-->
        <el-form :model="form" label-width="0px" class="demo-ruleForm">
          <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>模板名称：</b></td>
              <td colspan="3">
                {{ form.docxTemplateName }}
              </td>
            </tr>
            <tr>
            </tr>
            <tr>
              <td><b>模板样本：</b></td>
              <td colspan="3">
                <el-form-item>
                  <div v-show="null!=form.docxAttaOid && ''!=form.docxAttaOid">
                    <span>{{form.attaName}}</span>&nbsp;&nbsp;&nbsp;
                    <el-link type="success"  @click="downloadFile(form.docxAttaOid)">下载</el-link>
                  </div>
                </el-form-item>
              </td>
            </tr>
            <tr id="object_view">
              <td><b>关联存储对象：</b></td>
              <td colspan="3">
                <el-table :data="form.appendObjectList">
                  <el-table-column label="序号" width="55" align="center">
                    <template slot-scope="scope">
                      <span>{{scope.$index + 1}}</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="存储对象key" width="320"  prop="key" >
                    <!--<template slot-scope="scope">
                      <el-form-item :prop="'appendObjectList.' + scope.$index + '.key'"
                                    :rules="rules.appendObjectList.rules.key" style="height: 53px;">
                        <el-input placeholder="存储对象key" v-model.trim="scope.row.key" maxlength="50"
                                  show-word-limit/>
                      </el-form-item>
                    </template>-->
                  </el-table-column>
                  <el-table-column label="关联存储对象" align="left">
                    <template slot-scope="scope">
                      <span v-for="append in appendObjectOptions" v-if="scope.row.objectOid == append.objectOid">
                        {{append.objectName}}
                      </span>
                    </template>
                  </el-table-column>
                </el-table>
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import {save, del ,getOne, page, able,getAppendObjects,getObjectColumns } from "@/api/form/docxTemplate";
  import { uploadImage,download } from "@/api/form/atta";
  import {validateLegalStrNoNumber} from '@/utils/validate';
  import { getToken} from '@/utils/auth';
  export default {
    name: "DocxTemplate",
    props: {
      formObject: {
        type: Object,
        default: {}
      }
    },
    data() {
      let validataVariableName = (rule, value, callback) => {
        if (value != '') {
          let columnList = this.formColumnStrList.filter(d => d == value);
          if (columnList.length > 0) {
            callback(new Error('当前存储对象名称已存在，请重新输入'));
          } else {
            callback();
          }
        } else {
          callback();
        }
      };
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        //授权id
        // 表格数据
        templateList: [],
        //附件列表
        fileList:[],
        viewDialogOptions:[],
        formColumnStrList:[],
        // 弹出层标题
        title: "",
        // 新增/修改显示弹出层
        openInit: false,
        // 查看显示弹出层
        openView: false,
        // 启用状态
        ableMap:{'1':'启用','0':'禁用'},
        url:process.env.BASE_URL+'docxTempalte.docx',
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10
        },
        appendObjectOptions:[],
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          docxTemplateName: [
            { required: true, message: "模板名称不能为空", trigger: "blur" },
          ],
          docxAttaOid: [
            { required: true, message: "请上传模板word", trigger: "blur" },
          ],
          appendObjectList: {
            rules: {
              key: [
                {required: true, message: '存储对象key不能为空', trigger: 'blur'},
                {required: true, validator: validateLegalStrNoNumber, trigger: 'blur'},
                {required: true, validator: validataVariableName, trigger: 'blur'},
              ],
              objectOid: [
                {required: true, message: '请选择关联存储对象', trigger: 'change'},
              ]
            }
          }
        }
      };
    },
    created() {
      this.getList();
      this.getAppendObjectOptions();
      this.getColumns();
    },
    methods: {
      /** 查询列表 */
      getList() {
        this.loading = true;
        let that = this;
        this.queryParams.objectOid = this.formObject.objectOid;
        this.queryParams.authorizeKey = this.formObject.authorizeKey;
        page(this.queryParams).then(response => {
          this.templateList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        }).catch(function () {
          that.loading = false;
        });
      },
      // 启用状态
      statusFormat(row) {
        return this.selectMapLabel(this.ableMap, row.isAble);
      },
      getAppendObjectOptions(){
        let that = this;
         getAppendObjects(this.formObject.authorizeKey).then(response => {
           //过滤掉当前得
           this.appendObjectOptions = response.data.filter(obj => obj.objectOid != that.formObject.objectOid);
         });
       },
      getColumns(){
        let that = this;
        getObjectColumns(this.formObject.objectOid).then(response => {
          //过滤掉当前得
          this.formColumnStrList = response.data;
        });
      },
      // 取消按钮
      cancel() {
        this.openInit = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {};
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.queryParams = {};
        this.queryParams.pageNum = 1;
        this.queryParams.pageSize = 10;
        this.handleQuery();
      },
      /** 查看按钮操作 */
      handleView(row) {
        this.reset();
        const oid = row.id;
        getOne(oid).then(response => {
          this.form = response.data;
          this.openView = true;
          this.title = "查看";
        });
      },
      /** 新增和修改按钮操作 */
      handleInit(row) {
        this.reset();
        const oid = row.id;
        if(oid == undefined){
          this.form = {};
          this.openInit = true;
          this.title = "新增模板信息";
        }else {
          getOne(oid).then(response => {
            this.form = response.data;
            this.openInit = true;
            this.title = "修改模板信息";
          });
        }
      },
      /** 提交按钮 */
      submitForm: function() {
        this.form.appendObjectOids=null;
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.form.objectOid = this.formObject.objectOid;
            this.form.authorizeKey = this.formObject.authorizeKey;
            save(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("保存成功");
                this.openInit = false;
                this.getList();
              }
            });
          }else {
            return false;
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const oid = row.id;
        this.$confirm('是否确认删除吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return del(oid);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 启禁用按钮操作 */
      handleAble(row) {
        const oid = row.id;
        let ableMessage = row.isAble === 1 ? '启用' : '禁用'
        this.$confirm('你确定要' + ableMessage + '吗？', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return able(oid);
        }).then(() => {
          this.msgSuccess(ableMessage+"成功");
        }).catch(function() {
          row.isAble = row.isAble === 0 ? 1 : 0
        });
      },
      getToken() {
        return {Authorization: 'Bearer ' + getToken()}
      },

      //下载附件
      downloadFile(attaOid){
        download(attaOid);
      },
      //预览附件
      viewFile(attaOid){
        let downloadFileUrl = process.env.VUE_APP_BASE_API + "/form/manager/downloadFile/";
        let item = {show:true,attaOid:attaOid,name:this.form.attaName
          ,downloadFileUrl:downloadFileUrl};
        this.viewDialogOptions.push(item);
      },
      //关闭预览附件
      closeFileView(){
        this.viewDialogOptions.pop();
      },
      //成功后返回
      async uploadSuccess(resp){
        this.fileList=[];
        if(200 !== resp.code){
          return this.msgError(resp.message);
        }
        this.form.docxAttaOid = resp.data.attaOid;
        this.form.attaName = resp.data.name;
        //this.$refs['form'].clearValidate();
        this.$set(this.form, "docxAttaOid", resp.data.attaOid);
      },
      //失败后返回
      uploadError(resp){
        this.msgError("文件上传失败");
      },
      uploadUrl(){
        return uploadImage();
      },
      //上传之前
      beforeUpload(file) {
        if(file.name.indexOf("%00") > -1 || file.name.indexOf("./") > -1 || file.name.indexOf(".\\") > -1 ) {
          this.msgError('上传文件名称非法！');
          return false;
        }
        var type = this.fileByType(file.name);
        if(null != type && type !="docx" ) {
          this.msgError('请上传docx格式的文件！');
          return false;
        }
        const fileSize = file.size;
        if (0 == fileSize) {
          this.msgError('上传文件不能为空！');
          return false;
        }
        const isLt2M = file.size / 1024 / 1024 < 50;
        if (!isLt2M) {
          this.msgError('上传文件大小不能超过 50MB！');
        }
        return isLt2M;
      },
      /**
       * 通过文件后缀返回文件
       */
      fileByType(path) {
        return path.substring(path.lastIndexOf(".") + 1, path.length);
      },
      //清空附件
      clearAtta(){
        //delFile(this.form.docxAttaOid);
        this.form.docxAttaOid = '';
        this.form.attaName = '';
        this.$forceUpdate();
      },
      deleteAppendObject(index) {
        this.form.appendObjectList.splice(index, 1)
        this.$refs['form'].clearValidate();
      },
      addAppendObject() {
        let column = {'key': ''}
        let list = []
        if (this.form.appendObjectList) {
          list = this.form.appendObjectList;
        }
        list.push(column)
        this.$set(this.form, 'appendObjectList', list)
      },
    }
  };
</script>
<style>
  #app-container-tep{
    padding-top: 0px !important;
  }
  #object_view .cell{
    text-align: center !important;
  }
  .tipTd{
    line-height: 22px;
    font-size: 13px;
  }
</style>
