<template>
  <div>
    <!--暂存受理查询数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="108px"
    >
      <el-form-item label="申请项目名称" prop="projectName">
        <el-input
          v-model.trim="queryParams.projectName"
          placeholder="请输入申请项目名称"
          clearable
          size="100"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="事项名称" prop="caseNumber">
        <el-input
          v-model.trim="queryParams.caseNumber"
          placeholder="请输入事项名称"
          clearable
          size="100"
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
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="qlCaseList" border :fit="true">
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="申请项目名称"
        align="center"
        prop="projectName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="办件编号"
        align="center"
        prop="caseNumber"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="事项名称"
        align="center"
        prop="serviceName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="申请人"
        align="center"
        prop="applyUserName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="证件号"
        align="center"
        prop="credentialNumber"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="登记日期"
        align="center"
        prop="createDate"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="操作"
        width="185"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-dengji"
            @click="acceptanceCaseSelect(scope.row)"
            v-hasPermi="['business:temporary:select']"
            >选择
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="acceptanceCaseView(scope.row)"
            v-hasPermi="['business:temporary:view']"
            >查看
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

    <!-- 办件查看 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="openView"
      v-if="openView"
      title="查看办件信息"
      width="1158px"
      height="600px"
      scrollbar
      append-to-body
      custom-class="case-view"
    >
      <view-case-info
        :caseNumber="caseNumber"
        @view-case="viewCaseClose"
      ></view-case-info>
    </el-dialog>
  </div>
</template>
<script>
/*import {queryServiceTypeSimpleTree} from "@/api/sxpt/serviceType";*/
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import viewCaseInfo from "@/views/zc/businessManagement/windowAcceptance/viewCaseInfo";
import { getUserInfoList } from "@/api/zc/businessManagement/applyUserList";
export default {
  inheritAttrs: false,
  components: { Treeselect, viewCaseInfo },
  name: "ApplyUserList",
  props: ['applyCardNum', 'cegisterType'],
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 办件数据
      qlCaseList: [],

      caseNumber: '',

      // 弹出层标题
      title: "",
      // 显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      // 查询参数
      queryParams: {
        projectName:'',
        caseNumber:'',
        credentialNumber: this.applyCardNum,
        applyUserType: this.cegisterType,
        pageNum: 1,
        pageSize: 10,
        sourceApp: 1
      },
    };
  },
  created () {
    this.getList();
  },
  methods: {
    viewCaseClose () {
      this.openView = false;
    },
    /* /!** 获取事项类型树 *!/
     getServiceTypeTree(serviceType) {
       let _that = this;
       queryServiceTypeSimpleTree(serviceType).then(response => {
         _that.serviceTypeOptions=response.data;
       });
     },*/

    getList () {
      let _that = this;
      _that.loading = true;
      getUserInfoList(this.queryParams).then(response => {
        _that.qlCaseList = response.data.data;
        _that.total = response.data.total;
        _that.loading = false;
      });
    },
    /** 重置按钮操作 */
    resetQuery () {
      let _that = this;
      Object.assign(this.queryParams, this.$options.data().queryParams);
      _that.resetForm("queryForm");
      _that.handleQuery();
    },
    /** 表单重置 */
    reset () {
      let _that = this;
      Object.assign(_that.queryForm, _that.$options.data().queryForm)
      _that.resetForm("queryForm");
    },
    /** 取消按钮 */
    cancel () {
      let _that = this;
      _that.openInit = false;
      _that.reset();
    },
    /** 搜索按钮操作 */
    handleQuery () {
      let _that = this;
      _that.queryParams.pageNum = 1;
      _that.queryParams.credentialNumber = this.applyCardNum,
        _that.queryParams.applyUserType = this.cegisterType,
        _that.getList();
    },
    /** 办件查看按钮操作 */
    acceptanceCaseView (row) {
      this.caseNumber = row.caseNumber;
      this.openView = true;
      this.dialogTitle = "查看办件详情";
    },

    // 办件修改关闭按钮
    acceptanceCaseSelect (row) {
      this.$emit("selectUserOk", row.caseOid);
    },

  }
};
</script>
