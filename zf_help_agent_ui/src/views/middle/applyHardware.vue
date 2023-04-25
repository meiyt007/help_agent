<template>
  <div class="app-container typeApp">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="115px"
    >
      <el-form-item label="申请开发商名称" prop="companyName">
        <el-input
          v-model.trim="queryParams.companyName"
          placeholder="请输入申请开发商名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="申请用户名称" prop="middleUserName">
        <el-input
          v-model.trim="queryParams.middleUserName"
          placeholder="请输入申请用户名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="硬件名称" prop="hardwareName">
        <el-input
          v-model.trim="queryParams.hardwareName"
          placeholder="请输入硬件名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="状态">
        <el-select
          v-model="queryParams.status"
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="(value, key) in statusMap"
            :key="key"
            :label="value"
            :value="key"
          />
        </el-select>
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
          class="ml5"
        >重置</el-button
        >
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-delete"
          size="mini"
          @click="submitBatchForm"
        >删除</el-button
        >
      </el-col>
    </el-row>

    <el-table
      ref="multipleTable"
      :data="applyHardwareList"
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="60" align="center"> </el-table-column>
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="申请开发商名称"
        align="center"
        prop="companyName"
        show-overflow-tooltip
      />
      <el-table-column
        label="申请用户名称"
        align="center"
        prop="middleUserName"
        show-overflow-tooltip
      />
      <el-table-column
        label="硬件名称"
        align="center"
        prop="hardwareName"
        show-overflow-tooltip
      />
      <el-table-column
        label="型号"
        align="center"
        prop="model"
        show-overflow-tooltip
      />
      <el-table-column
        label="状态"
        align="center"
        prop="status"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <p v-if="scope.row.status==0">未提交</p>
          <p v-if="scope.row.status==1">待审核</p>
          <p v-if="scope.row.status==2">已受理</p>
          <p v-if="scope.row.status==3">未通过</p>
          <p v-if="scope.row.status==4">已上传</p>
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        prop="createDate"
        show-overflow-tooltip
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
            v-hasPermi="['im:applyHardware:view']"
          >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shenhe"
            v-if="isShowAuth(scope.row)"
            @click="handleInit(scope.row)"
            v-hasPermi="['im:applyHardware:audit']"
          >审核</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-ziliaoshangchuan"
            v-if="isShowTerminal(scope.row)"
            @click="handleTerminal(scope.row)"
            v-hasPermi="['im:applyHardware:upload']"
          >上传终端包</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNumber"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

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
        <el-input v-show="false" v-model="form.hardwareOid" />
        <div class=".zf-zc-table--title">硬件申请基本信息</div>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><b>用户名称：</b></td>
            <td>
              {{ applyHardware.middleUserName }}
            </td>
            <td><b>开发商名称：</b></td>
            <td>
              {{ applyHardware.companyName }}
            </td>
          </tr>
          <tr>
            <td><b>硬件名称：</b></td>
            <td>
              {{ applyHardware.hardwareName }}
            </td>
            <td><b>品牌：</b></td>
            <td>
              {{ applyHardware.brand }}
            </td>
          </tr>
          <tr>
            <td><b>型号：</b></td>
            <td>
              {{ applyHardware.model }}
            </td>
            <td><b>硬件开发包：</b></td>
            <td>
              <div v-for="(attaJSONObject, index) in attaJsonArray" :key="index">
                <div v-show="null != attaJSONObject.oid && '' != attaJSONObject.oid">
                  <span>{{ attaJSONObject.fileName }}</span>
                  <el-link type="primary" @click="downloadFile(attaJSONObject.oid)">下载</el-link>
                </div>
              </div>
            </td>
          </tr>
          <tr>
            <td><b>状态：</b></td>
            <td>
              {{ applyHardware.status | statusHandle }}
            </td>
            <td><b>终端应用程序包：</b></td>
            <td>
              <div v-show="null != applyHardware.terminalAttaOid && '' != applyHardware.terminalAttaOid">
                <span>{{ applyHardware.attaName }}</span>
                <el-link type="primary" @click="downloadFile(applyHardware.terminalAttaOid)">下载</el-link>
              </div>
            </td>
          </tr>
          <tr>
            <td><b>备注：</b></td>
            <td colspan="3">
              {{ applyHardware.memo }}
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>审核状态：</b></td>
            <td colspan="3">
              <el-radio-group v-model="form.status">
                <el-radio :label="'2'">通过</el-radio>
                <el-radio :label="'3'">未通过</el-radio>
              </el-radio-group>
            </td>
          </tr>
          <tr>
            <td><b>备注：</b></td>
            <td colspan="3">
              <el-form-item>
                <el-input
                  v-model.trim="form.memo"
                  type="textarea"
                  placeholder="请输入备注"
                  maxlength="32"
                  show-word-limit
                ></el-input>
              </el-form-item>
            </td>
          </tr>
        </table>
        <div class=".zf-zc-table--title">历史审核记录列表</div>
        <el-table v-loading="loading" :data="hardwareAuthRecList" border>
          <el-table-column label="序号" width="50" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="审核人"
            align="center"
            width="200"
            prop="middleUserName"
          >
            <template slot-scope="scope">
              <p>{{ scope.row.userName == null || scope.row.userName == '' ? scope.row.middleUserName : scope.row.userName}}</p>
            </template>
          </el-table-column>
          <el-table-column
            label="状态"
            align="center"
            width="100"
            prop="serverStatus"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <p v-if="scope.row.status==0">未提交</p>
              <p v-if="scope.row.status==1">待审核</p>
              <p v-if="scope.row.status==2">已受理</p>
              <p v-if="scope.row.status==3">未通过</p>
              <p v-if="scope.row.status==4">已上传</p>
            </template>
          </el-table-column>
          <el-table-column
            label="审核日期"
            width="200"
            align="center"
            prop="createDate"
          />
          <el-table-column
            label="原因"
            width="300"
            align="center"
            prop="memo"
          />
        </el-table>
      </el-form>
      <div slot="footer" align="center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改终端包上传 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openInitTerminal"
      width="900px"
      append-to-body
    >
      <el-form
        ref="formTerminal"
        :model="formTerminal"
        :rules="rules"
        label-width="0px"
        class="demo-ruleForm"
      >
        <el-input v-show="false" v-model="formTerminal.hardwareOid" />
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><b>用户名称：</b></td>
            <td>
              {{ formTerminal.middleUserName }}
            </td>
            <td><b>开发商名称：</b></td>
            <td>
              {{ formTerminal.companyName }}
            </td>
          </tr>
          <tr>
            <td><b>硬件名称：</b></td>
            <td>
              {{ formTerminal.hardwareName }}
            </td>
            <td><b>品牌：</b></td>
            <td>
              {{ formTerminal.brand }}
            </td>
          </tr>
          <tr>
            <td><b>型号：</b></td>
            <td>
              {{ formTerminal.model }}
            </td>
            <td><b>硬件开发包：</b></td>
            <td>
              <div v-for="(attaJSONObject, index) in attaJsonArray" :key="index">
                <div v-show="null != attaJSONObject.oid && '' != attaJSONObject.oid">
                  <span>{{ attaJSONObject.fileName }}</span>
                  <el-link type="primary" @click="downloadFile(attaJSONObject.oid)">下载</el-link>
                </div>
              </div>
            </td>
          </tr>
          <tr>
            <td><b>状态：</b></td>
            <td>
              {{ formTerminal.status | statusHandle }}
            </td>
            <td><b>终端应用程序包：</b></td>
            <td>
              <el-upload class="upload-demo" :action="uploadUrl()" :on-error="uploadError" :file-list="fileList"
                         :before-upload="beforeUpload" :on-success="uploadSuccess">
                <el-button size="small" type="primary" icon="el-icon-upload">点击上传</el-button>
              </el-upload>
              <div v-show="null != formTerminal.terminalAttaOid && '' != formTerminal.terminalAttaOid">
                <span>{{ formTerminal.attaName }}</span>
                <el-link type="primary" @click="downloadFile(formTerminal.terminalAttaOid)">下载</el-link>
                |
                <el-link type="primary" @click="viewFileNew(formTerminal.terminalAttaOid)">预览</el-link>
                |
                <el-link type="primary" @click="deleteFile(formTerminal)">删除</el-link>
              </div>
            </td>
          </tr>
          <tr>
            <td><b>备注：</b></td>
            <td colspan="3">
              {{ formTerminal.memo }}
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" align="center">
        <el-button type="primary" @click="submitTerminalForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看应用信息详细 -->
    <el-dialog
      title="查看"
      :close-on-click-modal="false"
      :visible.sync="openView"
      width="900px"
      append-to-body
    >
      <div class="zf-zc-table--title">硬件申请基本信息</div>
      <!--<h3>事项标题</h3>-->
      <table
        cellspacing="0"
        cellpadding="0"
        border="0"
        class="zf-zc-table"
      >
        <colgroup>
          <col width="20%" />
          <col width="30%" />
          <col width="20%" />
          <col width="30%" />
        </colgroup>
        <tr>
          <td><b>用户名称：</b></td>
          <td>
            {{ applyHardware.middleUserName }}
          </td>
          <td><b>开发商名称：</b></td>
          <td>
            {{ applyHardware.companyName }}
          </td>
        </tr>
        <tr>
          <td><b>硬件名称：</b></td>
          <td>
            {{ applyHardware.hardwareName }}
          </td>
          <td><b>品牌：</b></td>
          <td>
            {{ applyHardware.brand }}
          </td>
        </tr>
        <tr>
          <td><b>型号：</b></td>
          <td>
            {{ applyHardware.model }}
          </td>
          <td><b>硬件开发包：</b></td>
          <td>
            <div v-for="(attaJSONObject, index) in attaJsonArray" :key="index">
              <div v-show="null != attaJSONObject.oid && '' != attaJSONObject.oid">
                <span>{{ attaJSONObject.fileName }}</span>
                <el-link type="primary" @click="downloadFile(attaJSONObject.oid)">下载</el-link>
              </div>
            </div>
          </td>
        </tr>
        <tr>
          <td><b>状态：</b></td>
          <td>
            {{ applyHardware.status | statusHandle }}
          </td>
          <td><b>终端应用程序包：</b></td>
          <td>
            <div v-show="null != applyHardware.terminalAttaOid && '' != applyHardware.terminalAttaOid">
              <span>{{ applyHardware.attaName }}</span>
              <el-link type="primary" @click="downloadFile(applyHardware.terminalAttaOid)">下载</el-link>
            </div>
          </td>
        </tr>
        <tr>
          <td><b>备注：</b></td>
          <td colspan="3">
            {{ applyHardware.memo }}
          </td>
        </tr>
      </table>
      <div class="zf-zc-table--title">历史审核记录列表</div>
      <el-table v-loading="loading" :data="hardwareAuthRecList" border>
        <el-table-column label="序号" width="50" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="审核员用户名称"
          width="200"
          align="center"
          prop="userName"
        />
        <el-table-column
          label="状态"
          align="center"
          width="100"
          prop="serverStatus"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <p v-if="scope.row.status==0">未提交</p>
            <p v-if="scope.row.status==1">待审核</p>
            <p v-if="scope.row.status==2">已受理</p>
            <p v-if="scope.row.status==3">未通过</p>
            <p v-if="scope.row.status==4">已上传</p>
          </template>
        </el-table-column>
        <el-table-column
          label="审核日期"
          width="200"
          align="center"
          prop="createDate"
        />
        <el-table-column
          label="原因"
          width="300"
          align="center"
          prop="memo"
        />
      </el-table>
      <div slot="footer" class="dialog-footer">
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
  init,
  initTerminal,
  getOne,
  save,
  batchDelete,
  saveTerminal
} from "@/api/middle/applyHardware";
import {
  uploadFile
} from "@/api/sys/atta";
import fileView from '@/views/common/fileView';
export default {
  components: {fileView},
  name: "ApplyHardware",
  data () {
    return {
      // 遮罩层
      loading: true,
      statusMap: { "0": "未提交", "1": "待审核", "2": "已受理", "3": "未通过", "4": "已上传" },
      // 总条数
      total: 0,
      title: "",
      // 数据表格
      applyHardwareList: [],
      // 新增/修改显示弹出层
      openInit: false,
      openInitTerminal: false,
      // 查看显示弹出层
      openView: false,
      // 启用状态
      ableMap: { "1": "启用", "0": "禁用" },
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
        companyName: "",
        middleUserName: "",
        hardwareName: "",
        status: ""
      },
      hardwareAuthRecList: [],
      applyHardware: {},
      attaJsonArray: {},
      form: {
        status: "2",
        memo: "",
        hardwareOid: ""
      },
      formTerminal: {

      },
      viewDialogOptions: [],
      fileList: [],
      rules: {
        serverStatus: [
          { required: true, message: "审核状态不能为空", trigger: "blur" }
        ]
      }
    };
  },
  watch: {},
  created () {
    this.getList();
  },
  methods: {
    /** 查询业务层级列表 */
    getList () {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.applyHardwareList = response.data.datas;
        this.total = response.data.totalRows;
        this.loading = false;
      });
    },
    handleSelectionChange(val){
      this.multipleSelection = val;
    },
    /** 查看按钮操作 */
    handleView (row) {
      this.reset();
      const oid = row.oid;
      getOne(oid).then(response => {
        this.applyHardware = response.data.applyHardware;
        this.hardwareAuthRecList = response.data.hardwareAuthRecList;
        this.attaJsonArray = response.data.attaJsonArray;
        this.openView = true;
        this.title = "查看应用信息";
      });
    },
    // 是否展示个人登记按钮
    isShowAuth (row) {
      return row.status && row.status.indexOf("1") != -1;
    },
    isShowTerminal (row) {
      return row.status && row.status.indexOf("2") != -1;
    },
    // 初始化新增
    handleInit (row) {
      const oid = row.oid;
      init(oid).then(response => {
        this.applyHardware = response.data.applyHardware;
        this.hardwareAuthRecList = response.data.hardwareAuthRecList;
        this.form.hardwareOid = response.data.applyHardware.oid;
        this.attaJsonArray = response.data.attaJsonArray;
        this.openInit = true;
        this.title = "审核";
      });
    },
    handleTerminal (row) {
      const oid = row.oid;
      initTerminal(oid).then(response => {
        this.formTerminal = response.data.applyHardware;
        this.attaJsonArray = response.data.attaJsonArray;
        this.openInitTerminal = true;
        this.title = "终端包上传";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          save(this.form).then(response => {
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
    // 终端包保存
    submitTerminalForm: function () {
      this.$refs["formTerminal"].validate(valid => {
        if (valid) {
          saveTerminal(this.formTerminal).then(response => {
            if (response.code === 200) {
              this.msgSuccess("保存成功");
              this.openInitTerminal = false;
              this.getList();
            }
          });
        } else {
          return false;
        }
      });
    },
    submitBatchForm: function() {
      let _that = this;
      var oid = [];
      if (_that.multipleSelection.length <= 0) {
        _that.$message.error("请选择删除!");
        return false;
      }
      for (let ss = 0; ss < _that.multipleSelection.length; ss++) {
        oid.push(_that.multipleSelection[ss].oid);
      }
      var oids = oid.join(",");
      batchDelete(oids).then(response => {
        if (response.code === 200) {
          _that.msgSuccess("删除成功");
          this.getList();
        }
      });
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNumber = 1;
      this.queryParams.pageSize = 10;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.queryParams = {};
      this.handleQuery();
    },
    // 表单重置
    reset () {
      this.form = {};
      this.resetForm("form");
    },
    // 取消按钮
    cancel () {
      this.openInit = false;
      this.openInitTerminal = false;
      this.openView = false;
      this.reset();
    },
    //下载附件
    downloadFile(attaOid) {
      this.download(attaOid);
    },
    //预览附件
    viewFileNew(terminalAttaOid) {
      let item = {
        show: true,
        attaOid: terminalAttaOid
      };
      this.viewDialogOptions.push(item);
    },

    //关闭预览附件
    closeFileView() {
      this.viewDialogOptions.pop();
    },
    // 删除附件
    deleteFile(formTerminal) {
      formTerminal.attaName = '';
      formTerminal.terminalAttaOid = '';
    },

    //成功后返回
    uploadSuccess(resp) {
      this.fileList = [];
      this.formTerminal.terminalAttaOid = resp.data.oid;
      this.formTerminal.attaName = resp.data.name;
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
      if (null != type && type != "jpg" && type != "png" && type != "jpeg" && type != "bmp" &&
        type != "JPG" && type != "PNG" && type != "JPEG" && type != "BMP") {
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
  filters:{
    statusHandle(data) {
      if(!data) {
        return '未提交'
      }
      const statusMap = {
        0: '未提交',
        1: '待审核',
        2: '已受理',
        3: '未通过',
        4: '已上传'
      }
      return statusMap[data]
    }
  }
};
</script>
<style></style>
