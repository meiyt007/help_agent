/**
* @Author: liangss
* @Date: 2020-10-28 10:04:48
* @Last Modified by: liangss
* @Last Modified time: 2020-10-28 11:49:59
* @Description: 咨询登录
*/
<template>
  <div class="app-container">
    <!--咨询数据-->
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="70px">
      <el-form-item label="咨询编号" prop="advisoryCode">
        <el-input v-model.trim="queryParams.advisoryCode" placeholder="请输入咨询编号" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="咨询人" prop="name">
        <el-input v-model.trim="queryParams.name" placeholder="请输入咨询人" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="咨询时间">
        <!--<el-date-picker
              v-model="queryParams.advisoryStartDate"
              type="date" value-format="yyyy-MM-dd"
              placeholder="请选择开始时间">
            </el-date-picker>

            <el-date-picker
              v-model="queryParams.advisoryEndDate"
              type="date" value-format="yyyy-MM-dd"
              placeholder="请选择结束时间">
            </el-date-picker>-->
        <el-date-picker v-model="queryParams.advisoryStartDate" type="date" value-format="yyyy-MM-dd"
          :picker-options="pickerOptionsStart" placeholder="请选择开始日期">
        </el-date-picker>
        -
        <el-date-picker v-model="queryParams.advisoryEndDate" type="date" value-format="yyyy-MM-dd"
          :picker-options="pickerOptionsEnd" placeholder="请选择结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item class="fr mr0">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置
        </el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="default" icon="el-icon-plus" size="mini" @click="handleInit">新增</el-button>
      </el-col>
    </el-row>
    <el-table v-loading="loading" :data="advisoryRegistrationList" border :fit="true" height="calc(100% - 160px)">
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>

      <el-table-column label="咨询人" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="咨询人电话" align="center" prop="telePhone" :show-overflow-tooltip="true" />
      <el-table-column label="咨询编号" align="center" prop="advisoryCode" :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" align="center" prop="createDate" :show-overflow-tooltip="true" />

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" @click="handleInit(scope.row)">修改</el-button>
          <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDelete(scope.row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />
    <!-- 添加或修改咨询登记对话框 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="title" :visible.sync="openInit" v-if="openInit"
      width="1100px" height='700px' scrollbar append-to-body>

      <el-form ref="form" :model="form" :rules="rules" label-width="0px" class="dialog-table">
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><i class="require">*</i><b>咨询人：</b></td>
            <td>
              <el-form-item prop="name">
                <el-col :span="24">
                  <el-input v-model.trim="form.name" placeholder="请输入咨询人" maxlength="50" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
            <td><i class="require">*</i> <b>咨询人电话：</b></td>
            <td>
              <el-form-item prop="telePhone">
                <el-col :span="24">
                  <el-input v-model.trim="form.telePhone" placeholder="请输入咨询人电话" maxlength="15" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>身份证号码：</b></td>
            <td colspan="3">
              <el-form-item prop="cardNumber">
                <el-col :span="24">
                  <el-input v-model.trim="form.cardNumber" placeholder="请输入身份证号码" maxlength="18" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td><i class="require">*</i><b>咨询内容：</b></td>
            <td colspan="3">
              <el-form-item prop="advisoryContent">
                <el-col :span="24">
                  <el-input type="textarea" :autosize="{ minRows: 2 }" placeholder="请输入咨询内容"
                    v-model.trim="form.advisoryContent" maxlength="2000" show-word-limit></el-input>
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i> <b>处理结果：</b></td>
            <td colspan="3">
              <el-form-item prop="result">
                <el-col :span="24">
                  <el-input type="textarea" :autosize="{ minRows: 2 }" placeholder="请输入处理结果" v-model.trim="form.result"
                    maxlength="2000" show-word-limit></el-input>
                </el-col>
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td><b>附件：</b></td>
            <td colspan="3">
              <el-col :span="24">
                <el-form-item label="">
                  <!--   <el-input v-show="false" v-model="form.attaOid" />
                     <el-input v-show="false" v-model="form.attaName" />-->
                  <el-upload class="upload-demo" :action="uploadUrl()" :on-error="uploadError" :file-list="fileList"
                    :before-upload="beforeUpload" :on-success="uploadSuccess">
                    <el-button size="small" type="primary" icon="el-icon-upload">点击上传</el-button>
                  </el-upload>
                  <!--<el-button type="success" size="mini" @click="selectAttas">配置项附件</el-button>-->
                  <!-- <el-button type="danger" size="mini" @click="clearAtta">清理</el-button>-->
                  <div v-show="null != form.attaOid && '' != form.attaOid">
                    <span>{{ form.attaName }}</span>
                    <el-link type="primary" @click="downloadFile(form.attaOid)">下载</el-link>
                    |
                    <el-link type="primary" @click="viewFileNew(form.attaOid)">预览</el-link>
                    |
                    <el-link type="primary" @click="deleteFile(form)">删除</el-link>
                  </div>
                </el-form-item>
              </el-col>
            </td>
          </tr>
        </table>
      </el-form>

      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 咨询登记信息详细 -->
    <el-dialog v-dialog-drag :title="title" :visible.sync="openView" width="1100px" height='700px' scrollbar
      v-if="openView" append-to-body>

      <el-form ref="form" :model="form" label-width="0" size="mini" class="dialog-table">
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b>咨询人：</b>
            </td>
            <td>
              <el-form-item prop="name">
                <el-col :span="24">
                  {{ form.name }}
                </el-col>
              </el-form-item>
            </td>
            <td>
              <b>咨询人电话：</b>
            </td>
            <td>
              <el-form-item prop="telePhone">
                <el-col :span="24">
                  {{ form.telePhone }}
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>身份证号码：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="cardNumber">
                <el-col :span="24">
                  {{ form.cardNumber }}
                </el-col>
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td>
              <b>咨询内容：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="advisoryContent">
                <el-col :span="24">
                  {{ form.advisoryContent }}
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>处理结果：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="result">
                <el-col :span="24">
                  {{ form.result }}
                </el-col>
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td><b>附件：</b></td>
            <td colspan="3">
              <el-col :span="24">
                <el-form-item label="">
                  <div v-show="null != form.attaOid && '' != form.attaOid">
                    <span>{{ form.attaName }}</span>
                    <el-link type="primary" @click="downloadFile(form.attaOid)">下载</el-link>
                    |
                    <el-link type="primary" @click="viewFileNew(form.attaOid)">预览</el-link>
                  </div>
                </el-form-item>
              </el-col>
            </td>
          </tr>
        </table>
      </el-form>

      <div slot="footer" class="zf-text-center">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!--引入文件的预览弹出层-->
    <el-dialog v-dialog-drag title="文件预览" :visible.sync="view.show" v-for="(view, index) in viewDialogOptions"
      :key="index" @close="closeFileView" width="900px" append-to-body>
      <file-view :attaOid="view.attaOid" @father-click="closeFileView"></file-view>
    </el-dialog>
  </div>
</template>

<script>
  import {
    page,
    getOne,
    saveOrUpdate,
    del
  } from "@/api/zc/interactiveConsultation/advisoryRegistration.js";
  import {
    pageFile,
    uploadFile
  } from "@/api/sys/atta";
  import fileView from '@/views/common/fileView';
  import Treeselect from '@riophae/vue-treeselect';
  import '@riophae/vue-treeselect/dist/vue-treeselect.css';
  import {
    validatePhoneOrTel,
    validIDCard
  } from "@/utils/validate";
  export default {
    name: "AdvisoryRegistrationManage",
    components: {
      Treeselect,
      fileView
    },
    data() {
      return {
        selectArr: [],
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 应用表格数据
        advisoryRegistrationList: [],
        //查询区划名称参数
        districtName: "",
        // 弹出层标题
        title: "",
        // 新增/修改显示弹出层
        openInit: false,
        // 查看显示弹出层
        openView: false,
        fileList: [],
        attaList: [],
        viewDialogOptions: [],

        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          advisoryCode: "",
          name: "",
          advisoryStartDate: null,
          advisoryEndDate: null
        },
        pickerOptionsStart: {
          disabledDate: time => {
            const endDateVal = new Date(this.queryParams.advisoryEndDate).getTime()
            if (endDateVal) {
              return time.getTime() > endDateVal - 0
            }
          }
        },
        pickerOptionsEnd: {
          disabledDate: time => {
            const beginDateVal = new Date((new Date(this.queryParams.advisoryStartDate) - 24 * 60 * 60 * 1000))
              .getTime()
            if (beginDateVal) {
              return time.getTime() < beginDateVal - 0
            }
          }
        },
        defaultProps: {
          children: "children",
          label: "label"
        },
        // 表单参数
        form: {
          id: '', //逻辑主键
          name: '', // 咨询人
          telePhone: '', // 咨询人电话
          advisoryCode: '', // 咨询编号
          createDate: '', // 登记时间
          sort: 0, // 排序号
        },
        formPass: {},
        // 表单校验
        rules: {
          name: [{
            required: true,
            message: "咨询人不能为空",
            trigger: "blur"
          }],
          telePhone: [{
              required: true,
              message: "咨询人电话不能为空",
              trigger: "blur"
            },
            {
              validator: validatePhoneOrTel,
              message: '请输入正确的电话号码',
              trigger: 'blur'
            }
          ],
          cardNumber: [{
              required: true,
              message: "身份证号不能为空",
              trigger: "blur"
            },
            {
              validator: validIDCard,
              message: '请输入正确的身份证号码',
              trigger: 'blur'
            }
          ],
          advisoryContent: [{
            required: true,
            message: "咨询内容不能为空",
            trigger: "blur"
          }],
          result: [{
            required: true,
            message: "处理结果不能为空",
            trigger: "blur"
          }]
        }
      };
    },
    created() {
      this.getList();
      /*  this.getDistrictTree();*/
    },
    methods: {
      /** 查询咨询列表 */
      getList() {
        this.loading = true;
        page(this.queryParams).then(response => {
          this.advisoryRegistrationList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        });
      },
      // 取消按钮
      cancel() {
        this.openInit = false;
        this.reset();
      },
      // 表单重置
      reset() {
        Object.assign(this.form, this.$options.data().form)
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.queryParams = {
            pageNum: 1,
            pageSize: 10,
            advisoryCode: "",
            name: "",
            advisoryStartDate: null,
            advisoryEndDate: null
          },
          /*queryParams  queryForm*/
          this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 查看按钮操作 */
      handleView(row) {
        let _that = this;
        _that.reset();
        const oid = row.id;
        getOne(oid).then(response => {
          _that.form = response.data;
          _that.openView = true;
          _that.title = "查看咨询信息";
        });
      },
      /** 新增和修改按钮操作 */
      handleInit(row) {
        let _that = this;
        _that.reset();
        if (row.id) {
          getOne(row.id).then(response => {
            _that.openInit = true;
            _that.form = response.data;
          });
        } else {
          _that.form = {};
          _that.openInit = true;
        }
        _that.title = row.id ? "修改咨询信息" : "新增咨询信息";
      },
      encode(input) {
        let _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        let output = "";
        let chr1, chr2, chr3, enc1, enc2, enc3, enc4;
        let i = 0;
        input = this._utf8_encode(input);
        while (i < input.length) {
          chr1 = input.charCodeAt(i++);
          chr2 = input.charCodeAt(i++);
          chr3 = input.charCodeAt(i++);
          enc1 = chr1 >> 2;
          enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
          enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
          enc4 = chr3 & 63;
          if (isNaN(chr2)) {
            enc3 = enc4 = 64;
          } else if (isNaN(chr3)) {
            enc4 = 64;
          }
          output = output +
            _keyStr.charAt(enc1) + _keyStr.charAt(enc2) +
            _keyStr.charAt(enc3) + _keyStr.charAt(enc4);
        }
        return output;
      },
      _utf8_encode(string) {
        string = string.replace(/\r\n/g, "\n");
        let utftext = "";
        for (let n = 0; n < string.length; n++) {
          let c = string.charCodeAt(n);
          if (c < 128) {
            utftext += String.fromCharCode(c);
          } else if ((c > 127) && (c < 2048)) {
            utftext += String.fromCharCode((c >> 6) | 192);
            utftext += String.fromCharCode((c & 63) | 128);
          } else {
            utftext += String.fromCharCode((c >> 12) | 224);
            utftext += String.fromCharCode(((c >> 6) & 63) | 128);
            utftext += String.fromCharCode((c & 63) | 128);
          }

        }
        return utftext;
      },
      /** 提交按钮 */
      submitForm: function () {
        let _that = this;
        _that.$refs["form"].validate(valid => {
          if (valid) {
            _that.saveFormData();
          }
        });
      },
      saveFormData() {
        let _that = this;
        _that.formPass = _that.form;
        _that.formPass.name = _that.encode(_that.formPass.name);
        _that.formPass.telePhone = _that.encode(_that.formPass.telePhone);
        _that.formPass.cardNumber = _that.encode(_that.formPass.cardNumber);
        _that.formPass.advisoryContent = _that.encode(_that.formPass.advisoryContent);
        _that.formPass.result = _that.encode(_that.formPass.result);
        saveOrUpdate(_that.formPass).then(response => {
          if (response.code === 200) {
            _that.msgSuccess("保存成功");
            _that.openInit = false;
            setTimeout(() => {
              _that.getList();
            }, 10);

          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const oid = row.id;
        this.$confirm("是否确认删除?", "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          })
          .then(function () {
            return del(oid);
          })
          .then(() => {
            this.getList();
            this.msgSuccess("删除成功");
          })
          .catch(function () {});
      },
      //选择配置项附件
      selectAttas() {
        this.getAttaList();
        this.openAttaListView = true;
      },
      //下载附件
      downloadFile(attaOid) {
        this.download(attaOid);
      },
      //预览附件
      viewFileNew(attaOid) {
        let item = {
          show: true,
          attaOid: attaOid
        };
        this.viewDialogOptions.push(item);
      },
      // 删除附件
      deleteFile(form) {
        form.attaName = '';
        form.attaOid = '';
      },
      //关闭预览附件
      closeFileView() {
        this.viewDialogOptions.pop();
      },
      //清空附件
      clearAtta() {
        this.form.attaOid = '';
        this.form.attaName = '';
      },
      //成功后返回
      uploadSuccess(resp) {
        this.fileList = [];
        this.form.attaOid = resp.data.oid;
        this.form.attaName = resp.data.name;
      },
      //失败后返回
      uploadError(resp) {
        this.msgError("文件上传失败");
      },
      uploadUrl() {
        return uploadFile();
      },
      //上传之前
      beforeUpload(file) {
        if (file.name.indexOf("%00") > -1 || file.name.indexOf("./") > -1 || file.name.indexOf(".\\") > -1) {
          this.msgError('上传文件名称非法！');
          return false;
        }
        var type = this.fileByType(file.name);
        if (null != type && type != "jpg" && type != "png" && type != "jpeg" && type != "gif" && type != "bmp" &&
          type != "JPG" && type != "PNG" && type != "JPEG" && type != "GIF" && type != "BMP" &&
          type != "txt" && type != "TXT" && type != "doc" && type != "DOC" && type != "docx" && type != "DOCX" &&
          type != "pdf" && type != "PDF" && type != "xls" && type != "XLS" && type != "xlsx" && type != "XLSX" &&
          type != "ppt" && type != "PPT" && type != "pptx" && type != "PPTX"
        ) {
          this.msgError('请上传正确格式的文件！');
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

      /**
       * 通过文件后缀返回文件
       */
      fileByType(path) {
        return path.substring(path.lastIndexOf(".") + 1, path.length);
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
