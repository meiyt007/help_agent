/**
* @Author: liangss
* @Date: 2020-10-28 10:04:48
* @Last Modified by: liangss
* @Last Modified time: 2020-10-28 11:49:59
* @Description: 咨询记录
*/
<template>
  <div class="app-container">
    <!--咨询数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="70px"
    >
      <el-form-item label="咨询编号" prop="advisoryCode">
        <el-input
          v-model.trim="queryParams.advisoryCode"
          placeholder="请输入咨询编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="咨询人" prop="name">
        <el-input
          v-model.trim="queryParams.name"
          placeholder="请输入咨询人"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="咨询时间">
        <!-- <el-date-picker
              v-model="queryParams.advisoryStartDate"
              type="date" value-format="yyyy-MM-dd"
              placeholder="请选择开始时间">
            </el-date-picker>

            <el-date-picker
              v-model="queryParams.advisoryEndDate"
              type="date" value-format="yyyy-MM-dd"
              placeholder="请选择结束时间">
            </el-date-picker>-->
        <el-date-picker
          v-model="queryParams.advisoryStartDate"
          type="date"
          value-format="yyyy-MM-dd"
          :picker-options="pickerOptionsStart"
          placeholder="请选择开始日期"
        >
        </el-date-picker>
        -
        <el-date-picker
          v-model="queryParams.advisoryEndDate"
          type="date"
          value-format="yyyy-MM-dd"
          :picker-options="pickerOptionsEnd"
          placeholder="请选择结束日期"
        >
        </el-date-picker>
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
          >重置</el-button
        >
      </el-form-item>
    </el-form>
    <!--  <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="default" icon="el-icon-plus" size="mini" @click="handleInit" v-hasPermi="['sys:AreaSite:init']">新增</el-button>
          </el-col>
        </el-row>-->

    <el-table
      v-loading="loading"
      :data="advisoryRegistrationList"
      border
      :fit="true"
      :height="calcHeight3"
    >
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="咨询人"
        align="center"
        prop="name"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="咨询人电话"
        align="center"
        prop="telePhone"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="咨询编号"
        align="center"
        prop="advisoryCode"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createDate"
        :show-overflow-tooltip="true"
      />

      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            >查看</el-button
          >
          <!-- <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai"  @click="handleInit(scope.row)" v-hasPermi="['sys:district:update']">修改</el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDelete(scope.row)" v-hasPermi="['sys:district:delete']">删除</el-button>-->
        </template>
      </el-table-column>
    </el-table>
    <pagination
      :total="queryParams.total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <!-- 添加或修改咨询登记对话框 -->
    <el-dialog
    v-dialog-drag
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openInit"
      v-if="openInit"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="0px"
        class="dialog-table"
      >
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
                  <el-input
                    v-model.trim="form.name"
                    placeholder="请输入咨询人"
                    maxlength="50"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
            <td>
              <b>咨询人电话：</b>
            </td>
            <td>
              <el-form-item prop="telePhone">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.telePhone"
                    placeholder="请输入咨询人电话"
                    maxlength="11"
                    show-word-limit
                  />
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
                  <el-input
                    v-model.trim="form.cardNumber"
                    placeholder="请输入身份证号码"
                    maxlength="18"
                    show-word-limit
                  />
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
                  <el-input
                    type="textarea"
                    placeholder="请选择咨询内容"
                    v-model.trim="form.advisoryContent"
                    maxlength="2000"
                    show-word-limit
                  ></el-input>
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
                  <el-input
                    type="textarea"
                    placeholder="请选择处理结果"
                    v-model.trim="form.result"
                    maxlength="2000"
                    show-word-limit
                  ></el-input>
                </el-col>
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td><b>附件：</b></td>
            <td>
              <el-col :span="24">
                <el-form-item label="">
                  <!--   <el-input v-show="false" v-model="form.attaOid" />
                  <el-input v-show="false" v-model="form.attaName" />-->
                  <el-upload
                    class="upload-demo"
                    :action="uploadUrl()"
                    :on-error="uploadError"
                    :file-list="fileList"
                    :on-success="uploadSuccess"
                  >
                    <el-button size="small" type="primary"
                      >点击上传<i class="iconfont zfsoft-wenjianshangchuan"></i
                    ></el-button>
                  </el-upload>
                  <!--<el-button type="success" size="mini" @click="selectAttas">配置项附件</el-button>-->
                  <!-- <el-button type="danger" size="mini" @click="clearAtta">清理</el-button>-->
                  <div v-show="null != form.attaOid && '' != form.attaOid">
                    <span>{{ form.attaName }}</span>
                    <el-link type="primary" @click="downloadFile(form.attaOid)"
                      >下载</el-link
                    >
                    |
                    <el-link type="primary" @click="viewFile(form.attaOid)"
                      >预览</el-link
                    >
                  </div>
                </el-form-item>
              </el-col>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 咨询登记信息详细 -->
    <el-dialog
    v-dialog-drag
      :title="title"
      :visible.sync="openView"
      width="1100px"
      height="700px"
      scrollbar
      v-if="openView"
      append-to-body
    >
      <el-form
        ref="form"
        :model="form"
        label-width="0"
        size="mini"
        class="dialog-table"
      >
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
                    <el-link type="primary" @click="downloadFile(form.attaOid)"
                      >下载</el-link
                    >
                    |
                    <el-link type="primary" @click="viewFileNew(form.attaOid)"
                      >预览</el-link
                    >
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
    <el-dialog
      v-dialog-drag
      title="文件预览"
      :visible.sync="view.show"
      v-for="(view, index) in viewDialogOptions"
      :key="index"
      @close="closeFileView"
      width="900px"
      append-to-body
    >
      <file-view :attaOid="view.attaOid" @father-click="closeFileView" />
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
// import { getOne } from "@/api/zc/sysRunConfiguration/sysAreaSite.js";
import {
  queryDistrictSimpleTree
} from "@/api/sys/district";
import {
  validateNumber,
  validIntNoZero
} from "@/utils/validate";
// import the component
import Treeselect from '@riophae/vue-treeselect';
// import the styles
import '@riophae/vue-treeselect/dist/vue-treeselect.css';
import Resolution from '@/mixins/resolution.js';
export default {
  name: "AdvisoryRegistration",
  mixins: [Resolution],
  components: {
    Treeselect,
    fileView
  },
  data () {
    return {
      selectArr: [],
      // 遮罩层
      loading: true,
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
        advisoryEndDate: null,
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
          const beginDateVal = new Date((new Date(this.queryParams.advisoryStartDate) - 24 * 60 * 60 * 1000)).getTime()
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
        }],
        cardNumber: [{
          required: true,
          message: "身份证号不能为空",
          trigger: "blur"
        }],
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
  created () {
    this.getList();
    /*  this.getDistrictTree();*/
  },
  methods: {
    /** 查询咨询列表 */
    getList () {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.advisoryRegistrationList = response.data.data;
        this.queryParams.total = response.data.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel () {
      this.openInit = false;
      this.reset();
    },
    // 表单重置
    reset () {
      Object.assign(this.form, this.$options.data().form)
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        advisoryCode: "",
        name: "",
        advisoryStartDate: null,
        advisoryEndDate: null
      },
        this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 查看按钮操作 */
    handleView (row) {
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
    handleInit (row) {
      let _that = this;
      _that.reset();
      if (row.id) {
        getOne(row.id).then(response => {
          _that.openInit = true;
          _that.form = response.data;
          /* alert(JSON.stringify(response.data));*/
        });
      } else {
        _that.openInit = true;
      }
      _that.title = row.id ? "修改咨询信息" : "新增咨询信息";
    },
    /** 提交按钮 */
    submitForm: function () {
      let _that = this;
      _that.$refs["form"].validate(valid => {
        if (valid) {
          saveOrUpdate(_that.form).then(response => {
            if (response.code === 200) {
              _that.msgSuccess("保存成功");
              _that.openInit = false;
              setTimeout(() => {
                _that.getList();
              }, 10);

            }
          });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete (row) {
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
        .catch(function () { });
    },
    //选择配置项附件
    selectAttas () {
      this.getAttaList();
      this.openAttaListView = true;
    },
    //下载附件
    downloadFile (attaOid) {
      this.download(attaOid);
    },
    //预览附件
    viewFileNew (attaOid) {
      let item = {
        show: true,
        attaOid: attaOid
      };
      this.viewDialogOptions.push(item);
    },
    //关闭预览附件
    closeFileView () {
      this.viewDialogOptions.pop();
    },
    //清空附件
    clearAtta () {
      this.form.attaOid = '';
      this.form.attaName = '';
    },
    //成功后返回
    uploadSuccess (resp) {
      this.form.attaOid = resp.data.oid;
      this.form.attaName = resp.data.name;
      this.fileList = [];
    },
    //失败后返回
    uploadError (resp) {
      this.msgError("文件上传失败");
    },
    uploadUrl () {
      return uploadFile();
    }

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
