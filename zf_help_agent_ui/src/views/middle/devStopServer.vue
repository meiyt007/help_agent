<template>
  <div class="app-container typeApp">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="应用名称" prop="appliName">
        <el-input
          v-model.trim="queryParams.appliName"
          placeholder="请输入应用名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="服务名称" prop="serverName">
        <el-input
          v-model.trim="queryParams.serverName"
          placeholder="请输入服务名称"
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

    <el-table
      ref="multipleTable"
      :data="devStopServerList"
      tooltip-effect="dark"
      style="width: 100%"
    >
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="开发商名称"
        align="center"
        prop="middleUser.companyName"
        show-overflow-tooltip
      />
      <el-table-column
        label="申请人姓名"
        align="center"
        prop="middleUser.userName"
        show-overflow-tooltip
      />
      <el-table-column
        label="应用名称"
        align="center"
        prop="appliName"
        show-overflow-tooltip
      />
      <el-table-column
        label="服务名称"
        align="center"
        prop="serverName"
        show-overflow-tooltip
      />
      <el-table-column
        label="状态"
        align="center"
        prop="status"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <p v-if="scope.row.status==4">停用</p>
          <p v-if="scope.row.status==10">停用待审核</p>
          <p v-if="scope.row.status==11">停用审核不通过</p>
        </template>
      </el-table-column>
      <el-table-column
        label="申请理由"
        align="center"
        prop="memo"
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
            v-hasPermi="['im:devStopServer:view']"
          >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shenhe"
            v-if="isShowAuth(scope.row)"
            @click="handleInit(scope.row)"
            v-hasPermi="['im:devStopServer:audit']"
          >审核</el-button
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
      height="800px"
      scrollbar
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="0px"
        class="demo-ruleForm"
      >
        <el-input v-show="false" v-model="form.appliOid" />
        <div class="zf-zc-table--title">服务申请停用基本信息</div>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><b>开发商名称：</b></td>
            <td>
              {{ middleUser.companyName }}
            </td>
            <td><b>申请人姓名：</b></td>
            <td>
              {{ middleUser.userName }}
            </td>
          </tr>
          <tr>
            <td><b>应用名称：</b></td>
            <td>
              {{ devStopServer.appliName }}
            </td>
            <td><b>服务名称：</b></td>
            <td>
              {{ devStopServer.serverName }}
            </td>
          </tr>
          <tr>
            <td><b>状态：</b></td>
            <td colspan="3">
              {{ devStopServer.status | statusHandle }}
            </td>
          </tr>
          <tr>
            <td><b>申请理由：</b></td>
            <td colspan="3">
              {{ devStopServer.memo }}
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>审核状态：</b></td>
            <td colspan="3">
              <el-radio-group v-model="form.serverStatus">
                <el-radio :label="'11'">停用审核不通过</el-radio>
                <el-radio :label="'4'">停用</el-radio>
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
                  maxlength="500"
                  show-word-limit
                ></el-input>
              </el-form-item>
            </td>
          </tr>
        </table>
        <div class="zf-zc-table--title">开发商申请服务停用审核记录列表</div>
        <el-table v-loading="loading" :data="devStopAuthRecList" border>
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
              <p v-if="scope.row.serverStatus==4">停用</p>
              <p v-if="scope.row.serverStatus==10">停用待审核</p>
              <p v-if="scope.row.serverStatus==11">停用审核不通过</p>
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

    <!-- 查看应用信息详细 -->
    <el-dialog
      title="查看"
      :close-on-click-modal="false"
      :visible.sync="openView"
      width="1000px"
      append-to-body
      height="800px"
      scrollbar
    >
      <div class="zf-zc-table--title" >服务申请停用基本信息</div>
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
          <td><b>开发商名称：</b></td>
          <td>
            {{ middleUser.companyName }}
          </td>
          <td><b>申请人姓名：</b></td>
          <td>
            {{ middleUser.userName }}
          </td>
        </tr>
        <tr>
          <td><b>应用名称：</b></td>
          <td>
            {{ devStopServer.appliName }}
          </td>
          <td><b>服务名称：</b></td>
          <td>
            {{ devStopServer.serverName }}
          </td>
        </tr>
        <tr>
          <td><b>状态：</b></td>
          <td colspan="3">
            {{ devStopServer.status | statusHandle }}
          </td>
        </tr>
        <tr>
          <td><b>申请理由：</b></td>
          <td colspan="3">
            {{ devStopServer.memo }}
          </td>
        </tr>
      </table>
      <div class="zf-zc-table--title">开发商申请服务停用审核记录列表</div>
      <el-table v-loading="loading" :data="devStopAuthRecList" border>
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
          width="200"
          prop="serverStatus"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <p v-if="scope.row.serverStatus==0">未提交</p>
            <p v-if="scope.row.serverStatus==1">待审核</p>
            <p v-if="scope.row.serverStatus==2">审核通过</p>
            <p v-if="scope.row.serverStatus==3">审核不通过</p>
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
  </div>
</template>

<script>
import {
  page,
  init,
  getOne,
  save,
} from "@/api/middle/devStopServer";
export default {
  components: {},
  name: "DevStopServer",
  data () {
    return {
      // 遮罩层
      loading: true,
      statusMap: { "4": "停用", "10": "停用待审核", "11": "停用审核不通过" },
      // 总条数
      total: 0,
      title: "",
      // 数据表格
      devStopServerList: [],
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      // 启用状态
      ableMap: { "1": "启用", "0": "禁用" },
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
        appliName: "",
        serverName: "",
        status: ""
      },
      devStopAuthRecList: [],
      devStopServer: {},
      middleUser: {},
      form: {
        serverStatus: "4",
        memo: "",
        stopOid: ""
      },
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
        this.devStopServerList = response.data.datas;
        this.total = response.data.totalRows;
        this.loading = false;
      });
    },
    /** 查看按钮操作 */
    handleView (row) {
      this.reset();
      const oid = row.oid;
      getOne(oid).then(response => {
        this.devStopServer = response.data.devStopServer;
        this.middleUser = response.data.devStopServer.middleUser;
        this.devStopAuthRecList = response.data.devStopAuthRecList;
        this.openView = true;
        this.title = "查看应用信息";
      });
    },
    // 是否展示个人登记按钮
    isShowAuth (row) {
      return row.status && row.status.indexOf("10") != -1;
    },
    // 初始化新增
    handleInit (row) {
      const oid = row.oid;
      init(oid).then(response => {
        this.devStopServer = response.data.devStopServer;
        this.middleUser = response.data.devStopServer.middleUser;
        this.devStopAuthRecList = response.data.devStopAuthRecList;
        this.form.stopOid = response.data.devStopServer.oid;
        this.openInit = true;
        this.title = "审核";
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
      this.form = {
        serverStatus: "4",
          memo: "",
          stopOid: ""
      },
      this.resetForm("form");
    },
    // 取消按钮
    cancel () {
      this.openInit = false;
      this.openView = false;
      this.reset();
    },
  },
  filters:{
    statusHandle(data) {
      if(!data) {
        return '停用'
      }
      const statusMap = {
        4: '停用',
        10: '停用待审核',
        11: '停用审核不通过'
      }
      return statusMap[data]
    }
  }
};
</script>
<style></style>
