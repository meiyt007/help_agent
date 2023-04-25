/**
* 验证规则管理
* @author: liangss
* @date: 2020-11-10
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--事项数据-->
      <el-col :span="24" :xs="24">
        <el-form
          :model="queryParams"
          ref="queryForm"
          :inline="true"
          label-width="70px"
        >
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

        <el-table v-loading="loading" :data="sxServiceList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="所属区划"
            align="center"
            width="110"
            prop="districtName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="所属机构"
            align="center"
            width="110"
            prop="organName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="事项名称"
            align="center"
            prop="serviceName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="实施编码"
            align="center"
            width="220"
            prop="implementCode"
            :show-overflow-tooltip="true"
          />
          <!--          <el-table-column
            label="事项类型"
            align="center"
            prop="serviceTypeName"
            :show-overflow-tooltip="true"
          />-->

          <el-table-column
            label="操作"
            align="center"
            width="750"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-chakan"
                @click="
                  openModelRuleManageDetail(
                    scope.row.serviceOid,
                    scope.row.implementCode,
                    scope.row.serviceName
                  )
                "
                >查看</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="openModelRuleManage(scope.row.serviceOid)"
                >规则编制</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="
                  openModelRuleAndPoints(
                    scope.row.serviceOid,
                    scope.row.implementCode,
                    scope.row.serviceName
                  )
                "
                >规则和审查要点配置</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="
                  openDZBD(
                    scope.row.serviceOid,
                    scope.row.implementCode,
                    scope.row.serviceName
                  )
                "
                >电子表单规则配置</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="
                  openJCBD(
                    scope.row.serviceOid,
                    scope.row.implementCode,
                    scope.row.serviceName
                  )
                "
                >基础表单规则配置</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="
                  openDZZZ(
                    scope.row.serviceOid,
                    scope.row.implementCode,
                    scope.row.serviceName
                  )
                "
                >电子证照规则配置</el-button
              >
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
      </el-col>
    </el-row>

    <!-- 事项规则编制start-->
    <el-dialog
      v-dialog-drag
      :visible.sync="item.show"
      v-for="item in modelRuleManageOptions"
      :title="item.title"
      width="100%"
      height="100%"
      append-to-body
      center
      custom-class="fa-model-rule-manage"
    >
      <fa-model-rule-manage
        :serviceOid="item.serviceOid"
        :title="item.title"
        ref="faModelRuleManage"
        @father-click="closeChild"
      >
      </fa-model-rule-manage>
      <div slot="footer">
        <el-button type="primary" @click="handleConfirm"> 确定 </el-button>
        <el-button @click="closeChild('N')"> 关闭 </el-button>
      </div>
    </el-dialog>

    <el-dialog
      v-dialog-drag
      :visible.sync="item.show"
      v-for="item in modelRuleManageDetailOptions"
      :title="item.title"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <fa-model-rule-manage-detail
        :serviceOid="item.serviceOid"
        :implementCode="item.implementCode"
        :serviceName="item.serviceName"
        :title="item.title"
        @father-click="closeChildDetail"
      >
      </fa-model-rule-manage-detail>
    </el-dialog>
    <!-- 事项规则编制 end-->

    <el-dialog
      v-dialog-drag
      :visible.sync="item.show"
      v-for="item in modelRuleAndPointsManageOptions"
      :title="item.title"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <model-rule-and-points-manage
        :serviceOid="item.serviceOid"
        :implementCode="item.implementCode"
        :serviceName="item.serviceName"
        :title="item.title"
        @father-click="closemodelRuleAndPointsManage"
      >
      </model-rule-and-points-manage>
    </el-dialog>

    <!--    custom-class="fa-model-rule-manage"-->

    <!--电子表单规则配置   start-->
    <el-dialog
      v-dialog-drag
      :visible.sync="item.show"
      v-for="item in fromRuleManageOptions"
      :title="item.title"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
      center
    >
      <from-rule-manage
        :serviceOid="item.serviceOid"
        :title="item.title"
        ref="fromRuleManage"
        @father-click="closefromRuleManage"
      >
      </from-rule-manage>
      <!--      <div slot="footer">
        <el-button type="primary" @click="handleConfirm"> 确定 </el-button>
        <el-button @click="closeChild('N')"> 关闭 </el-button>
      </div>-->
    </el-dialog>
    <!--电子表单规则配置   end-->

    <!--基础表单规则配置   start-->
    <el-dialog
      v-dialog-drag
      :visible.sync="item.show"
      v-for="item in basicFromRuleOptions"
      :title="item.title"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
      center
    >
      <basic-from-rule-manage
        :serviceOid="item.serviceOid"
        :title="item.title"
        ref="basicfromRuleManage"
        @father-click="closeBasicFromRuleManage"
      >
      </basic-from-rule-manage>
    </el-dialog>
    <!--基础规则配置   end-->

    <!--电子证照规则配置   start-->
    <el-dialog
      v-dialog-drag
      :visible.sync="item.show"
      v-for="item in electronicLicenseRuleOptions"
      :title="item.title"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
      center
    >
      <electronic-license-rule-manage
        :serviceOid="item.serviceOid"
        :title="item.title"
        ref="electronicLicenseRuleManage"
        @father-click="closeElectronicLicenseRuleManage"
      >
      </electronic-license-rule-manage>
    </el-dialog>
    <!--电子证照规则配置   end-->
  </div>
</template>

<script>
import { pageList, page, getSxServiceOne } from "@/api/zc/businessManagement/faModelRuleValidation.js";
import { queryServiceTypeSimpleTree } from "@/api/sxpt/serviceType";
import Treeselect from '@riophae/vue-treeselect';
import '@riophae/vue-treeselect/dist/vue-treeselect.css';
import faModelRuleManage from '@/views/zc/businessManagement/modelRule/faModelRuleManage';
import faModelRuleManageDetail from '@/views/zc/businessManagement/modelRule/faModelRuleManageDetail';
import modelRuleAndPointsManage from '@/views/zc/businessManagement/modelRule/modelRuleAndPointsManage';
import fromRuleManage from '@/views/zc/businessManagement/modelRule/fromRuleManage';
import basicFromRuleManage from '@/views/zc/businessManagement/modelRule/basicFromRuleManage';
import electronicLicenseRuleManage from '@/views/zc/businessManagement/modelRule/electronicLicenseRuleManage';

export default {
  name: "FaModelRuleValidation",
  components: { Treeselect, faModelRuleManage, faModelRuleManageDetail, modelRuleAndPointsManage, fromRuleManage, basicFromRuleManage, electronicLicenseRuleManage },
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      sxServiceList: [],
      // 事项类型
      serviceTypeOptions: [],
      //目录列表
      catalogList: [],
      //规则验证
      modelRuleManageOptions: [],
      //规则验证查询详细
      modelRuleManageDetailOptions: [],
      modelRuleAndPointsManageOptions: [],
      //电子表单规则验证
      fromRuleManageOptions: [],
      //基础表单规则验证
      basicFromRuleOptions: [],
      //电子证照规则验证
      electronicLicenseRuleOptions: [],

      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
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
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单参数
      form: {
        id: '', //逻辑主键
        serviceOid: "",
        implementCode: "",
        serviceName: "",
        serviceMaterList: [],
        materialCatalogOid: [],
        catalogName: []

      },
      form1: {
        serviceOid: "",
        materialOids: [],
        materialCatalogOids: []

      },
      // 表单校验
      rules: {},
    };
  },
  created () {
    this.getList();
    this.getServiceTypeTree();
    this.getMaterialCatalogList();
  },
  methods: {
    handleConfirm () {
      this.$refs.faModelRuleManage[0].queding();
    },
    /** 查询列表 */
    getList () {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.sxServiceList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
      console.log(this.queryParams)
    },
    /** 获取事项类型树 */
    getServiceTypeTree (serviceTypeOid) {
      let _that = this;
      queryServiceTypeSimpleTree(serviceTypeOid).then(response => {
        _that.serviceTypeOptions = response.data;
      });
    },
    /** 查询目录列表 */
    getMaterialCatalogList () {
      let _that = this;
      pageList().then(res => {
        this.catalogList = res.data;
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
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 查看按钮操作 */
    handleView (row) {
      let _that = this;
      _that.reset();
      const oid = row.serviceOid;
      getSxServiceOne(oid).then(response => {
        _that.form = response.data.sxService;
        _that.openView = true;
        _that.title = "查看事项信息";
      });
    },
    //打开验证规则配置
    openModelRuleManage (serviceOid) {
      let item = { serviceOid: serviceOid, show: true, title: '验证规则配置' };
      //let item = {show:false,title:'验证规则配置'};
      this.modelRuleManageOptions.push(item);
    },
    closeChild (str) {//关闭验证规则页面
      this.modelRuleManageOptions = [];
    },
    //打开验证规则配置详细    serviceName
    openModelRuleManageDetail (serviceOid, implementCode, serviceName) {
      let item = { serviceOid: serviceOid, implementCode: implementCode, serviceName: serviceName, show: true, title: '验证规则详细信息' };
      this.modelRuleManageDetailOptions.push(item);
    },
    closeChildDetail (str) {//关闭验证规则详细页面
      this.modelRuleManageDetailOptions = [];
      let item = { implementCode: implementCode, serviceName: serviceName, show: false, title: '验证规则详细信息' };
      // let item = {serviceOid:serviceOid,implementCode:implementCode,serviceName:serviceName,show:false,title:'验证规则详细信息'};
      this.modelRuleManageDetailOptions.push(item);
    },

    openModelRuleAndPoints (serviceOid, implementCode, serviceName) {
      let item = { serviceOid: serviceOid, implementCode: implementCode, serviceName: serviceName, show: true, title: '验证规则和审核要点配置' };
      this.modelRuleAndPointsManageOptions.push(item);
    },

    closemodelRuleAndPointsManage (str) {//验证规则和审核要点配置
      this.modelRuleAndPointsManageOptions = [];
      let item = { show: false, title: '验证规则配置' };
      this.modelRuleAndPointsManageOptions.push(item);
    },

    openDZBD (serviceOid, implementCode, serviceName) {
      let item = { serviceOid: serviceOid, implementCode: implementCode, serviceName: serviceName, show: true, title: '电子表单规则配置' };
      this.fromRuleManageOptions.push(item);
    },
    closefromRuleManage (str) {//验证规则和审核要点配置
      this.fromRuleManageOptions = [];
      let item = { show: false, title: '电子表单规则配置' };
      this.fromRuleManageOptions.push(item);
    },

    openJCBD (serviceOid, implementCode, serviceName) {
      let item = { serviceOid: serviceOid, implementCode: implementCode, serviceName: serviceName, show: true, title: '基础表单规则配置' };
      this.basicFromRuleOptions.push(item);
    },
    closeBasicFromRuleManage (str) {//验证规则和审核要点配置
      this.basicFromRuleOptions = [];
      let item = { show: false, title: '基础表单规则配置' };
      this.basicFromRuleOptions.push(item);
    },

    openDZZZ (serviceOid, implementCode, serviceName) {
      let item = { serviceOid: serviceOid, implementCode: implementCode, serviceName: serviceName, show: true, title: '电子证照规则配置' };
      this.electronicLicenseRuleOptions.push(item);
    },
    closeElectronicLicenseRuleManage (str) {//验证规则和审核要点配置
      this.electronicLicenseRuleOptions = [];
      let item = { show: false, title: '电子证照规则配置' };
      this.electronicLicenseRuleOptions.push(item);
    },

  },
};
</script>
<style lang="scss" scoped>
.primary-table h3 {
  font-size: 14px;
  font-weight: normal;
  color: #333;
  margin: 0px 0px 10px 0px;
}
.primary-table {
  padding: 20px;
  box-sizing: border-box;
}
.primary-table .handle-btn {
  border: none;
  background: none;
  padding: 0;
  font-size: 16px;
}
.primary-table .el-table,
.primary-table .el-table th {
  font-size: 13px;
}
.primary-table .el-form-item:first-child {
  margin-left: 8px;
}
.primary-table .el-form-item {
  margin-bottom: 0;
  width: 130px;
  margin-right: 0;
  margin-left: 14px;
  font-size: 13px;
}
.primary-table .el-form-item__content {
  font-size: 13px;
}
.primary-table .inputTable .el-form-item:first-child {
  margin-left: 0;
}
.primary-table .inputTable .el-form-item {
  width: 100px;
}
.el-select {
  width: 90%;
}

::v-deep .el-dialog.fa-model-rule-manage {
  .el-dialog__body {
    padding-bottom: 0;
  }
}
</style>
