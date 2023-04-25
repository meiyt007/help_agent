/**
* @Author: yueng
*/
<template>
  <div class="app-container">
    <!--事项数据-->
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="108px">
      <el-form-item label="事项名称" prop="serviceName">
        <el-input
          v-model.trim="queryParams.serviceName"
          placeholder="请输入事项名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="实施编码" prop="implementCode">
        <el-input
          v-model.trim="queryParams.implementCode"
          placeholder="请输入实施编码"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item class="fr mr0">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="districtList" border :fit="true" height="calc(100% - 120px)">
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="所属区划" align="center" prop="districtName" :show-overflow-tooltip="true"/>
      <el-table-column label="所属机构" align="center" prop="organName" :show-overflow-tooltip="true"/>
      <el-table-column label="事项名称" align="center" prop="serviceName" :show-overflow-tooltip="true"/>
      <el-table-column label="实施编码" align="center" prop="implementCode" :show-overflow-tooltip="true"/>
      <el-table-column label="事项类型" align="center" prop="serviceTypeName" :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['sys:reg:view']"
          >查看
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shouquan"
            @click="handleInit(scope.row)"
            v-hasPermi="['sys:reg:update']"
          >授权
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <!-- 添加或修改信息对话框 -->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openInit"
      v-if="openInit"
      width="900px"
      height="700px"
      scrollbar
      append-to-body
    >
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="15%"/>
          <col width="35%"/>
          <col width="15%"/>
          <col width="35%"/>
        </colgroup>

        <tr>
          <td>
            <b>所属区划：</b>
          </td>
          <td>
            <el-col :span="24">{{ form.districtName }}</el-col>
          </td>
          <td>
            <b>所属机构：</b>
          </td>
          <td>
            <el-col :span="24">{{ form.organName }}</el-col>
          </td>
        </tr>
        <tr>
          <td>
            <b>事项名称：</b>
          </td>
          <td colspan="3">
            <el-col :span="24">{{ form.serviceName }}</el-col>
          </td>
        </tr>
        <tr>
          <td>
            <b>实施编码：</b>
          </td>
          <td>
            <el-col :span="24">{{ form.implementCode }}</el-col>
          </td>
          <td>
            <b>基础编码：</b>
          </td>
          <td>
            <el-col :span="24">{{ form.basicCode }}</el-col>
          </td>
        </tr>

        <tr>
          <td>
            <b>选择用户：</b>
          </td>
          <td colspan="3">
            <el-tree
              :data="treeList"
              show-checkbox
              node-key="id"
              ref="helpWorkUserTree"
              :default-checked-keys="defaultCheckedList"
              highlight-current
              @check="checkHelpWorkUser"
              :props="defaultProps">
            </el-tree>
          </td>
        </tr>
      </table>

      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 信息详细 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="openView"
      v-if="openView"
      title="查看实施清单详情"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <view-sx-service-info :sxServiceOid="sxServiceOid" @view-service="viewServiceClose"/>
      <div slot="footer" class="zf-text-center">
        <el-button @click="openView = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  page,
  getSxServiceOne,
  del
} from "@/api/zc/businessManagement/sxServiceRegistrar.js";
import {
  getAllHelpWorkUserList,
  saveOrUpdateServiceRegistrar
} from "@/api/ha/serviceRegistrar/serviceOrWorkUserRegistrar.js";
import Treeselect from '@riophae/vue-treeselect';
import viewSxServiceInfo from "@/views/zc/businessSupervise/viewSxServiceInfo";
import '@riophae/vue-treeselect/dist/vue-treeselect.css';

export default {
  name: "serviceRegistrar",
  components: {Treeselect, viewSxServiceInfo},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      districtList: [],

      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      sxServiceOid: '',
      expandedKeys: [],
      userExpandedKeys: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        serviceName: "",
        implementCode: "",
        serviceStatus: "3"

      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      sxServiceReg: {
        serviceOid: '',
        userOids: [],
        serviceName: '',
        serviceType: '',
        serviceTypeName: '',
        organOid: '',
        organName: ''
      },
      userOidArr: [],
      authorize: {userOids: []},
      userOids: [],
      helpWorkUserList: [],
      helpWorkUserId: "",
      defaultProps: {
        children: "member",
        label: "name"
      },
      props: {
        label: "name",//这里是树结构中需显示的数据（即接口返回的需展示在页面上的参数）
        children: 'member',
        isLeaf: "leaf"
      },
      treeList: [],
      checkedNodeList: [],
      defaultCheckedList: [],
      allChildNodeList: []
    };
  },
  created() {
    this.getList();
  },
  methods: {

    /**
     * 选择用户
     */
    checkHelpWorkUser() {
      let checkedData = this.$refs.helpWorkUserTree.getCheckedNodes(true, false);
      this.checkedNodeList = checkedData;
    },

    /** 查询列表 */
    getList() {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.districtList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    viewServiceClose() {
      this.openView = false;
    },
    // 取消按钮
    cancel() {
      this.openInit = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.authorize = {userOids: []}
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
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 查看按钮操作 */
    handleView(row) {
      let _that = this;
      _that.sxServiceOid = row.serviceOid;
      _that.openView = true;
      _that.title = "查看事项信息";
    },

    /** 新增和修改按钮操作 */
    handleInit(row) {
      let _that = this;
      _that.reset();
      _that.userOids = [];
      if (row.serviceOid) {
        getSxServiceOne(row.serviceOid).then(response => {
          _that.openInit = true;
          _that.form = response.data.sxService;
          _that.sxServiceReg.serviceOid = row.serviceOid;
          _that.sxServiceReg.serviceName = row.serviceName;
          _that.sxServiceReg.organName = row.organName;
          _that.sxServiceReg.serviceTypeName = row.serviceTypeName;
          _that.sxServiceReg.serviceType = row.serviceTypeOid;
          _that.sxServiceReg.organOid = row.organOid;
          _that.sxServiceReg.serviceObject = row.serviceObject;
        });

        getAllHelpWorkUserList(row.serviceOid).then(response => {
          _that.openInit = true;
          if (response.code == "200") {
            _that.treeList = response.data.treeArray;
            _that.defaultCheckedList = response.data.defaultCheckedArray;
            _that.checkedNodeList = response.data.checkedArray;
            _that.allChildNodeList = response.data.allChildNodeArray;
          }
        });

      } else {
        _that.openInit = true;
      }
      _that.title = "授权";

    },

    /** 提交按钮 */
    submitForm: function () {
      let serviceOid = this.sxServiceReg.serviceOid;
      let checkedNodeList = this.checkedNodeList;
      let requestData = {
        "serviceOid": serviceOid,
        "checkedNodeList": checkedNodeList,
        "allNodeList": this.allChildNodeList
      };
      //保存授权信息
      saveOrUpdateServiceRegistrar(requestData).then(res => {
        if (res.code === 200) {
          this.msgSuccess("保存成功");
          this.openInit = false;
          setTimeout(() => {
            this.getList();
          }, 10);
        } else {
          this.msgError(res.message);
        }
      })
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
