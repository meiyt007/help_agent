/**
* 验证规则管理
* @author: liangss
* @date: 2020-11-10
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--一件事目录列表数据-->
      <el-col :span="24" :xs="24">
        <el-form
          :model="queryParams"
          ref="queryForm"
          :inline="true"
          label-width="118px"
          @submit.native.prevent
        >
          <el-form-item label="一件事目录名称" prop="comboDirectoryName">
            <el-input
              v-model.trim="queryParams.comboDirectoryName"
              placeholder="请输入一件事目录名称"
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

        <el-table
          v-loading="loading"
          :data="comboDirectoryList"
          border
          :fit="true"
        >
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="所属主题"
            align="center"
            width="210"
            prop="themeName"
            :show-overflow-tooltip="true"
          />
          <!--<el-table-column label="一件事目录编号" align="center" prop="comboDirectoryCode" />-->
          <el-table-column
            label="一件事目录名称"
            align="center"
            width="210"
            prop="comboDirectoryName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="所属区划"
            width="210"
            align="center"
            prop="districtName"
            :show-overflow-tooltip="true"
          />
          <!--          <el-table-column
            label="主办部门"
            align="center"
            prop="mainOrganName"
            :show-overflow-tooltip="true"
          />-->
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
                @click="
                  openPackageModelRuleManageDetail(
                    scope.row.comboDirectoryOid,
                    scope.row.comboDirectoryCode,
                    scope.row.themeName,
                    scope.row.comboDirectoryName
                  )
                "
                v-hasPermi="['sys:reg:view']"
                >查看</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="openPackageModelRuleManage(scope.row.comboDirectoryOid)"
                v-hasPermi="['sys:reg:update']"
                >规则编制</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="
                  openModelRuleAndPoints(
                    scope.row.comboDirectoryOid,
                    scope.row.comboDirectoryCode,
                    scope.row.themeName,
                    scope.row.comboDirectoryName
                  )
                "
                v-hasPermi="['sys:reg:update']"
                >规则和审查要点配置</el-button
              >

              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="
                  openDZBD(
                    scope.row.comboDirectoryOid,
                    scope.row.comboDirectoryCode,
                    scope.row.comboDirectoryName
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
                    scope.row.comboDirectoryOid,
                    scope.row.comboDirectoryCode,
                    scope.row.comboDirectoryName
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
                    scope.row.comboDirectoryOid,
                    scope.row.comboDirectoryCode,
                    scope.row.comboDirectoryName
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

    <!-- 一件事规则编制start-->
    <el-dialog
      v-dialog-drag
      :visible.sync="item.show"
      v-for="item in packageModelRuleManageOptions"
      :title="item.title"
      ref="ruleMange"
      width="100%"
      height="100%"
      scrollbar
      append-to-body
    >
      <package-model-rule-manage
        :comboDirectoryOid="item.comboDirectoryOid"
        :title="item.title"
        :dialog="$refs.ruleMange"
        @father-click="closeChild"
      >
      </package-model-rule-manage>
    </el-dialog>

    <el-dialog
      v-dialog-drag
      :visible.sync="item.show"
      v-for="item in packageModelRuleManageDetailOptions"
      :title="item.title"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <package-model-rule-manage-detail
        :comboDirectoryOid="item.comboDirectoryOid"
        :comboDirectoryCode="item.comboDirectoryCode"
        :themeName="item.themeName"
        :comboDirectoryName="item.comboDirectoryName"
        :title="item.title"
        @father-click="closeChildDetail"
      >
      </package-model-rule-manage-detail>
    </el-dialog>
    <!-- 一件事规则编制 end-->

    <!--    规则和审查要点配置start-->
    <el-dialog
      v-dialog-drag
      v-if="modelRuleAndPointsVisible"
      :visible.sync="modelRuleAndPointsVisible"
      :title="modelRuleAndPoints.title"
      width="1100px"
      height="600px"
      scrollbar
      append-to-body
    >
      <package-model-rule-and-points-manage
        :comboDirectoryOid="modelRuleAndPoints.comboDirectoryOid"
        :comboDirectoryCode="modelRuleAndPoints.comboDirectoryCode"
        :themeName="modelRuleAndPoints.themeName"
        :comboDirectoryName="modelRuleAndPoints.comboDirectoryName"
        :title="modelRuleAndPoints.title"
        ref="pointsManage"
        @father-click="closemodelRuleAndPointsManage"
      >
      </package-model-rule-and-points-manage>
      <div slot="footer" align="center">
        <el-button
          style="display: inline-block"
          type="primary"
          size="medium"
          @click="
            () => {
              $refs.pointsManage.queding()
            }
          "
          class="save-btn"
        >
          确定
        </el-button>
        <el-button
          size="medium"
          @click="
            () => {
              $refs.pointsManage.guanbi()
            }
          "
          class="close-btn"
        >
          关闭
        </el-button>
      </div>
    </el-dialog>
    <!--    规则和审查要点配置end-->

    <!--表单规则配置   start-->
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
        :comboDirectoryOid="item.comboDirectoryOid"
        :title="item.title"
        ref="fromRuleManage"
        @father-click="closefromRuleManage"
      >
      </from-rule-manage>
    </el-dialog>
    <!--表单规则配置   end-->

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
        :comboDirectoryOid="item.comboDirectoryOid"
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
        :comboDirectoryOid="item.comboDirectoryOid"
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
import { page } from "@/api/onething/clzs/mealCatalogRelatedManage/mealCatalogRelatedManage.js";
import Treeselect from '@riophae/vue-treeselect';
import '@riophae/vue-treeselect/dist/vue-treeselect.css';
import packageModelRuleManage from '@/views/onething/clzs/modelManagement/packageModelRuleManage';
import packageModelRuleManageDetail from '@/views/onething/clzs/modelManagement/packageModelRuleManageDetail';
import packageModelRuleAndPointsManage from '@/views/onething/clzs/modelManagement/packageModelRuleAndPointsManage';
import fromRuleManage from '@/views/onething/clzs/modelManagement/fromRuleManage';
import basicFromRuleManage from '@/views/onething/clzs/modelManagement/basicFromRuleManage';
import electronicLicenseRuleManage from '@/views/onething/clzs/modelManagement/electronicLicenseRuleManage';
export default {
  name: "PackageFaModelRuleValidation",
  components: { Treeselect, packageModelRuleManage, packageModelRuleManageDetail, packageModelRuleAndPointsManage, fromRuleManage, basicFromRuleManage, electronicLicenseRuleManage },
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 一件事目录数据
      comboDirectoryList: [],
      // 事项类型
      serviceTypeOptions: [],
      //目录列表
      catalogList: [],
      //一件事规则验证
      packageModelRuleManageOptions: [],
      //一件事规则验证查询详细
      packageModelRuleManageDetailOptions: [],
      //规则和审查要点配置
      modelRuleAndPointsVisible: false,
      modelRuleAndPoints: {},

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
        comboDirectoryName: ""
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      rules: {},
      form: {},
    };
  },
  created () {
    this.getList();
    /*    this.getServiceTypeTree();
        this.getMaterialCatalogList();*/
  },
  methods: {
    /** 查询列表 */
    getList () {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.comboDirectoryList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel () {
      this.openInit = false;
      this.reset();
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

    //打开一件事验证规则配置
    openPackageModelRuleManage (comboDirectoryOid) {
      let item = { comboDirectoryOid: comboDirectoryOid, show: true, title: '验证规则配置' };
      this.packageModelRuleManageOptions.push(item);
    },
    closeChild (str) {//关闭一件事验证规则页面
      this.packageModelRuleManageOptions = [];
      // let item = { show: false, title: '验证规则配置' };
      // this.packageModelRuleManageOptions.push(item);
    },
    //openPackageModelRuleManageDetail  openPackageModelRuleManage  scope.row.comboDirectoryOid,scope.row.comboDirectoryCode,scope.row.themeName
    //打开一件事验证规则配置详细
    openPackageModelRuleManageDetail (comboDirectoryOid, comboDirectoryCode, themeName, comboDirectoryName) {
      let item = { comboDirectoryOid: comboDirectoryOid, comboDirectoryCode: comboDirectoryCode, themeName: themeName, comboDirectoryName: comboDirectoryName, show: true, title: '一件事验证规则详细信息' };
      this.packageModelRuleManageDetailOptions.push(item);
    },
    closeChildDetail (str) {//关闭一件事验证规则详细页面
      this.packageModelRuleManageDetailOptions = [];
      let item = { show: false, title: '一件事验证规则详细信息' };
      this.packageModelRuleManageDetailOptions.push(item);
    },
    //验证规则和审核要点配置
    openModelRuleAndPoints (comboDirectoryOid, comboDirectoryCode, themeName, comboDirectoryName) {
      this.modelRuleAndPoints = { comboDirectoryOid: comboDirectoryOid, comboDirectoryCode: comboDirectoryCode, themeName: themeName, comboDirectoryName: comboDirectoryName, show: true, title: '验证规则和审核要点配置' };
      this.modelRuleAndPointsVisible = true;
    },

    closemodelRuleAndPointsManage (str) {//验证规则和审核要点配置
      this.modelRuleAndPointsVisible = false;
      this.modelRuleAndPoints = {};
    },

    //打开电子表单规则配置
    openDZBD (comboDirectoryOid, comboDirectoryCode, comboDirectoryName) {
      let item = { comboDirectoryOid: comboDirectoryOid, comboDirectoryCode: comboDirectoryCode, comboDirectoryName: comboDirectoryName, show: true, title: '电子表单规则验证' };
      this.fromRuleManageOptions.push(item);
    },
    //关闭电子表单规则配置
    closefromRuleManage (str) {//验证规则和审核要点配置
      this.fromRuleManageOptions = [];
      let item = { show: false, title: '电子表单规则验证' };
      this.fromRuleManageOptions.push(item);
    },
    openJCBD (comboDirectoryOid, comboDirectoryCode, comboDirectoryName) {
      let item = { comboDirectoryOid: comboDirectoryOid, comboDirectoryCode: comboDirectoryCode, comboDirectoryName: comboDirectoryName, show: true, title: '基础表单规则验证' };
      this.basicFromRuleOptions.push(item);
    },
    closeBasicFromRuleManage (str) {//验证规则和审核要点配置
      this.basicFromRuleOptions = [];
      let item = { show: false, title: '基础表单规则配置' };
      this.basicFromRuleOptions.push(item);
    },

    openDZZZ (comboDirectoryOid, comboDirectoryCode, comboDirectoryName) {
      let item = { comboDirectoryOid: comboDirectoryOid, comboDirectoryCode: comboDirectoryCode, comboDirectoryName: comboDirectoryName, show: true, title: '电子证照规则验证' };
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
</style>
