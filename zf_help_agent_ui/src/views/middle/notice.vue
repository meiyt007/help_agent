<template>
  <div class="app-container typeApp">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="100px"
      @submit.native.prevent
    >
      <el-form-item label="公告标题" prop="title">
        <el-input
          v-model.trim="queryParams.title"
          placeholder="公告标题"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
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

    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit"
          v-hasPermi="['im:notice:save']"
        >新增</el-button
        >
      </el-col>
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
      :data="noticeList"
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="60" align="center"> </el-table-column>
      <el-table-column label="序号" width="50" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="公告标题"
        align="center"
        prop="title"
        width="600"
        show-overflow-tooltip
      />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createDate"
        width="300"
        show-overflow-tooltip
      />
      <el-table-column
        label="排序"
        align="center"
        prop="sort"
        width="100"
        show-overflow-tooltip
      />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        style="width: 300px"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['im:notice:view']"
          >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            v-hasPermi="['sys:knowleType:update']"
          >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shenhejilu"
            @click="handleNoticeRec(scope.row)"
            v-hasPermi="['im:notice:read']"
          >已读记录</el-button
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
    <!-- 查看应用信息详细 -->
    <el-dialog
      title="查看"
      :close-on-click-modal="false"
      :visible.sync="openView"
      width="900px"
      append-to-body
      height="800px"
      scrollbar
    >
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
          <td><b>公告标题：</b></td>
          <td colspan="3">
            {{ notice.title }}
          </td>
        </tr>
        <tr>
          <td><b>类型：</b></td>
          <td>
            {{ notice.type | typeHandle }}
          </td>
          <td><b>排序：</b></td>
          <td>
            {{ notice.sort }}
          </td>
        </tr>
        <tr>
          <td height="100%"><b>公告内容：</b></td>
          <td colspan="3" height="100%">
            <div class="ql-container ql-snow">
              <div class="ql-editor" v-html="notice.content"></div>
            </div>
          </td>
        </tr>
      </table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 公告已读信息查看列表 -->
    <el-dialog
      title="公告已读信息查看列表"
      :close-on-click-modal="false"
      :visible.sync="openNoticeRec"
      width="70%"
      append-to-body
    >
      <el-form
        :model="queryNoticeRecParams"
        ref="queryForm"
        :inline="true"
        label-width="68px"
        @submit.native.prevent
      >
        <el-form-item label="用户名称" prop="middleUserName">
          <el-input
            v-model.trim="queryNoticeRecParams.middleUserName"
            placeholder="请输入用户名称"
            clearable
            size="small"
            @keyup.enter.native="noticeRecQuery"
          />
        </el-form-item>
        <el-form-item class="fr mr0">
          <el-button
            type="primary"
            icon="el-icon-search"
            size="mini"
            @click="noticeRecQuery"
          >搜索
          </el-button
          >
          <el-button
            type="warning"
            icon="el-icon-refresh"
            size="mini"
            @click="resetNoticeRecQuery"
            class="ml5"
          >重置</el-button
          >
        </el-form-item>
      </el-form>
      <el-table
        ref="multipleTable"
        :data="noticeRecList"
        tooltip-effect="dark"
        style="width: 100%"
      >
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="用户名称"
          align="center"
          prop="middleUserName"
          show-overflow-tooltip
        />
        <el-table-column
          label="企业名称"
          align="center"
          prop="middleUser.companyName"
          show-overflow-tooltip
        />
        <el-table-column
          label="手机号码"
          align="center"
          prop="middleUser.mobile"
          show-overflow-tooltip
        />
        <el-table-column
          label="电子邮件"
          align="center"
          prop="middleUser.email"
          show-overflow-tooltip
        />
        <el-table-column
          label="查看时间"
          align="center"
          prop="createDate"
          show-overflow-tooltip
        />
      </el-table>
      <pagination
        v-show="noticeRecTotal > 0"
        :total="noticeRecTotal"
        :page.sync="queryNoticeRecParams.pageNumber"
        :limit.sync="queryNoticeRecParams.pageSize"
        @pagination="noticeRecQuery"
      />
      <div slot="footer" class="dialog-footer">
        <el-button @click="openNoticeRec = false">关 闭</el-button>
      </div>
    </el-dialog>

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
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><i class="require">*</i><b>公告标题：</b></td>
            <td colspan="3">
              <el-form-item prop="title">
                <el-input
                  v-model.trim="form.title"
                  placeholder="请输入公告标题"
                  maxlength="20"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>类型：</b></td>
            <td>
              <el-form-item prop="type">
                <el-select
                  v-model="form.type"
                  placeholder="请选择类型"
                >
                  <el-option
                    v-for="(value, key) in typeList"
                    :key="key"
                    :label="value"
                    :value="key"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>排序号：</b></td>
            <td colspan="3">
              <el-form-item prop="sort">
                <el-table-column label="排序号" align="center">
                  <el-input-number style="width: 200px" placeholder="请输入排序号" v-model="form.sort" :min="1" :max="9999"/>
                </el-table-column>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>公告内容：</b></td>
            <td colspan="3">
              <el-form-item prop="content" style="height: 300px">
                <quill-editor
                  :value="form.content"
                  v-model="form.content"
                >
                </quill-editor>
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
  </div>
</template>

<script>
import {
  page,
  getOne,
  batchDelete,
  init,
  noticeRec,
  save
} from "@/api/middle/notice";
import quillEditor from "@/views/components/Editor/index.vue";
export default {
  components: {quillEditor},
  name: "Notice",
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 公告已读信息总条数
      noticeRecTotal: 0,
      title: "",
      // 查看显示弹出层
      openView: false,
      // 新增/修改显示弹出层
      openInit: false,
      openNoticeRec: false,
      statusOptions: { "": "全部","Y": "是", "N": "否" },
      typeList: { "0": "全部","1": "服务商", "2": "开发商" },
      // 消息类型
      messageTypeMap: { "0": "停用通知", "1": "服务通知", "2": "申请通知", "3": "升级通知" },
      // 数据表格
      noticeList: [],
      // 公告已读信息查看列表
      noticeRecList: [],
      notice: {},
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
        title: ""
      },
      queryNoticeRecParams: {
        pageNumber: 1,
        pageSize: 10,
        middleUserName: "",
        noticeOid: ""
      },
      form: {
      },
      rules: {
        sort: [
          { required: true, message: "排序不能为空", trigger: ['blur', 'change'] }
        ],
        title: [
          { required: true, message: "公告标题不能为空", trigger: ['blur', 'change'] }
        ],
        type: [
          { required: true, message: "类型不能为空", trigger: ['change']  }
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
        this.noticeList = response.data.datas;
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
        this.notice = response.data;
        this.openView = true;
        this.title = "查看应用信息";
      });
    },
    /** 公告已读信息查看 */
    handleNoticeRec (row) {
      this.reset();
      this.queryNoticeRecParams.noticeOid = row.oid;
      noticeRec(this.queryNoticeRecParams).then(response => {
        this.noticeRecList = response.data.page.datas;
        this.noticeRecTotal = response.data.page.datas.totalRows;
        this.queryNoticeRecParams.noticeOid = response.data.noticeOid;
        this.openNoticeRec = true;
        this.title = "公告已读信息查看";
      });
    },
    //公告已读信息中的搜索
    noticeRecQuery(){
      this.reset();
      this.loading = true
      noticeRec(this.queryNoticeRecParams).then(response => {
        this.noticeRecList = response.data.page.datas;
        this.noticeRecTotal = response.data.page.datas.totalRows;
        this.queryNoticeRecParams.noticeOid = response.data.noticeOid;
        this.openNoticeRec = true;
      });
    },
    // 初始化新增
    handleInit (row) {
      const oid = row.oid;
      if (oid === undefined) {
        this.form = {};
        this.openInit = true;
        this.title = "新增";
      } else {
        init(oid).then(response => {
          this.form = response.data;
          this.openInit = true;
          this.title = "修改";
        });
      }
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
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if(null == this.form.content || this.form.content == '') {
            this.msgError("内容不能为空")
            return;
          }
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
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNumber = 1;
      this.queryParams.pageSize = 10;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.queryParams = {};
      this.handleQuery();
    },
    /** 重置公告已读信息按钮操作  noticeOid不能置空*/
    resetNoticeRecQuery() {
      this.queryNoticeRecParams.middleUserName = "";
      this.queryNoticeRecParams.pageNumber = "";
      this.queryNoticeRecParams.pageSize = "";
      this.noticeRecQuery();
    },
    // 表单重置
    reset () {
      this.form = {};
      this.resetForm("form");
    },
    // 取消按钮
    cancel () {
      this.openView = false;
      this.openNoticeRec = false;
      this.openInit = false;
      this.reset();
    },
  },
  filters:{
    typeHandle(data) {
      if(!data) {
        return '全部'
      }
      const statusMap = {
        0: '全部',
        1: '服务商',
        2: '开发商'
      }
      return statusMap[data]
    }
  }
};
</script>
<style></style>
