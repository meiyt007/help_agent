/**
* 目录关联管理
* @author: chenjm
* @date: 2020-11-05
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
          label-width="108px"
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
          <el-form-item label="事项类型" prop="serviceTypeOid">
            <treeselect
              v-model="queryParams.serviceTypeOid"
              :options="serviceTypeOptions"
              style="width: 240px"
              noOptionsText="暂无数据"
              :defaultExpandLevel="1"
              placeholder="请输入事项类型"
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
            width="110"
            align="center"
            prop="districtName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="所属机构"
            align="center"
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
            prop="implementCode"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="事项类型"
            align="center"
            prop="serviceTypeName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="服务对象类型"
            align="center"
            :show-overflow-tooltip="true"
          >
            <template slot-scope="scope">
              <template
                v-if="
                  scope.row.serviceObject &&
                  scope.row.serviceObject.includes('1')
                "
              >
                自然人
              </template>
              <template
                v-if="
                  scope.row.serviceObject &&
                  scope.row.serviceObject.includes('2')
                "
              >
                企业法人
              </template>
              <template
                v-if="
                  scope.row.serviceObject &&
                  scope.row.serviceObject.includes('3')
                "
              >
                事业法人
              </template>
              <template
                v-if="
                  scope.row.serviceObject &&
                  scope.row.serviceObject.includes('4')
                "
              >
                社会组织法人
              </template>
              <template
                v-if="
                  scope.row.serviceObject &&
                  scope.row.serviceObject.includes('5')
                "
              >
                非法人企业
              </template>
              <template
                v-if="
                  scope.row.serviceObject &&
                  scope.row.serviceObject.includes('6')
                "
              >
                行政机关
              </template>
            </template>
          </el-table-column>
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
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="handleInit(scope.row)"
                >关联</el-button
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
    <!-- 添加或修改信息对话框 -->
    <el-dialog
      v-dialog-drag
      height="600px"
      scrollbar
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openInit"
      v-if="openInit"
      width="1000px"
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <table
          cellspacing="0"
          cellpadding="0"
          border="0"
          width="910px"
          class="zf-zc-table"
        >
          <colgroup>
            <col width="10%" />
            <col width="40%" />
            <col width="10%" />
            <col width="40%" />
          </colgroup>
          <tr>
            <td>
              <b>实施编码：</b>
            </td>
            <td>
              <el-form-item prop="implementCode">
                <el-col :span="24">
                  {{ form.implementCode }}
                </el-col>
              </el-form-item>
            </td>
            <td>
              <b>事项名称：</b>
            </td>
            <td>
              <el-form-item prop="serviceName">
                <el-col :span="24">
                  {{ form.serviceName }}
                </el-col>
              </el-form-item>
            </td>
          </tr>
        </table>
        <br />

        <table
          cellspacing="0"
          cellpadding="0"
          border="0"
          class="zf-zc-table--common zf-zc-table--td-center"
        >
          <colgroup>
            <col width="30" />
            <col width="120" />
            <col width="120" />
            <col width="150" />
            <col width="150" />
          </colgroup>
          <tr>
            <th>序号</th>
            <th>材料名称</th>
            <th>细化材料名称</th>
            <th>目录名称</th>
            <th>证照类型</th>
          </tr>
          <template v-for="(ruleForm, index) in form.refinedMaterials">
            <tr>
              <td>
                <el-form-item prop="xuhao">
                  {{ index + 1 }}
                </el-form-item>
              </td>
              <td>
                <el-form-item>
                  {{ ruleForm.materialName }}
                </el-form-item>
              </td>
              <td>
                <el-form-item>
                  {{ ruleForm.refinedMaterialName }}
                </el-form-item>
              </td>

              <td>
                <el-form-item prop="materialCatalogOid">
                  <el-col :span="24">
                    <treeselect
                      class="themeTreeselect"
                      :defaultExpandLevel="1"
                      noOptionsText="暂无数据"
                      noResultsText="暂无数据"
                      :show-count="true"
                      v-model="ruleForm.materialCatalogOid"
                      :disable-branch-nodes="true"
                      :options="catalogOptions"
                      placeholder="请选择对应目录"
                    />
                    <!--                       <el-select  placeholder="请选择对应目录" v-model="ruleForm.materialCatalogOid" :clearable="true">
                          <el-option v-for="catalog in catalogList" :key="catalog.id" :label="catalog.catalogName" :value="catalog.materialCatalogOid"></el-option>
                        </el-select>-->
                  </el-col>
                </el-form-item>
              </td>

              <td>
                <el-form-item prop="licenceOid">
                  <el-col :span="24">
                    <el-select
                      placeholder="请选择对应证照"
                      v-model="ruleForm.licenceOid"
                      :clearable="true"
                    >
                      <el-option
                        v-for="dict in licenceList"
                        :key="dict.dictOid"
                        :label="dict.name"
                        :value="dict.dictOid"
                      ></el-option>
                    </el-select>
                  </el-col>
                </el-form-item>
              </td>
            </tr>
          </template>
        </table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 信息详细 -->
    <el-dialog
      :title="title"
      :visible.sync="openView"
      width="1000px"
      v-if="openView"
      append-to-body
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
            <b>所属区划：</b>
          </td>
          <td>
            {{ form.districtName }}
          </td>
          <td>
            <b>所属机构：</b>
          </td>
          <td>
            {{ form.organName }}
          </td>
        </tr>
        <tr>
          <td>
            <b>事项名称：</b>
          </td>
          <td colspan="3">
            {{ form.serviceName }}
          </td>
        </tr>
        <tr>
          <td>
            <b>实施编码：</b>
          </td>
          <td>
            {{ form.implementCode }}
          </td>
          <td>
            <b>基本编码：</b>
          </td>
          <td>
            {{ form.basicCode }}
          </td>
        </tr>
        <tr>
          <td>
            <b>事项类型：</b>
          </td>
          <td>
            {{ form.serviceTypeName }}
          </td>
          <td>
            <b>行使层级：</b>
          </td>
          <td>
            {{ form.levelName }}
          </td>
        </tr>
        <tr>
          <td>
            <b>服务对象：</b>
          </td>
          <td>
            {{ form.serviceObjectName }}
          </td>
          <td>
            <b>办件类型：</b>
          </td>
          <td>
            {{ form.caseTypeName }}
          </td>
        </tr>
        <tr>
          <td>
            <b>是否收费：</b>
          </td>
          <td>
            {{ form.chargeFlagName }}
          </td>
          <td>
            <b>办理形式：</b>
          </td>
          <td>
            {{ form.handleFormName }}
          </td>
        </tr>
        <tr>
          <td>
            <b>是否支持在线预约：</b>
          </td>
          <td>
            {{ form.appointmentFlagName }}
          </td>
          <td>
            <b>行政管辖地：</b>
          </td>
          <td>
            {{ form.adminJurisdictionName }}
          </td>
        </tr>
      </table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { pageList, page, getSxServiceOne,
  geSxServiceAndMaterial,
  updateRefinedMaterialList, querySubitemMaterialCatalogList,
  queryCatalogTree } from "@/api/zc/clzs/catalogRelatedManage/catalogRelatedManage.js";
import { updateFaModelRuleValidation }  from "@/api/zc/businessManagement/faModelRuleValidation.js";
import { queryServiceTypeSimpleTree } from "@/api/sxpt/serviceType";
import { validateNumber, validIntNoZero } from "@/utils/validate";
import { getDictList } from "@/api/sys/common";
import Treeselect from '@riophae/vue-treeselect';
import '@riophae/vue-treeselect/dist/vue-treeselect.css';
export default {
  name: "CatalogRelatedManage",
  components: { Treeselect },
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      sxServiceList: [],
      licenceList: [],
      // 事项类型
      serviceTypeOptions: [],
      //目录列表
      catalogList: [],
      catalogOptions: [],
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
        serviceMaterialList: [],
        refinedMaterials: [],
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
    this.getDictList();
    this.getCatalogTree();
  },
  methods: {
    /** 查询列表 */
    getList () {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.sxServiceList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
      //console.log(this.queryParams)
    },
    /** 查询目录列表 */
    getMaterialCatalogList () {
      let _that = this;
      querySubitemMaterialCatalogList().then(res => {
        this.catalogList = res.data;
      });
      /*,querySubitemMaterialCatalogList,queryCatalogTree*/
    },
    getCatalogTree () {
      queryCatalogTree().then(response => {
        this.catalogOptions = response.data;
        console.log(JSON.stringify(this.catalogOptions))
      });

    },
    /** 获取事项类型树 */
    getServiceTypeTree (serviceTypeOid) {
      let _that = this;
      queryServiceTypeSimpleTree(serviceTypeOid).then(response => {
        _that.serviceTypeOptions = response.data;
      });
    },
    /** 获取楼层数据 */
    getDictList () {
      let _that = this;
      // 查询楼层字典数据
      getDictList('LICENCE_TYPE').then(response => {
        _that.licenceList = response.data;
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
    /** 新增和修改按钮操作 */
    handleInit (row) {
      let _that = this;
      _that.reset();
      if (row.serviceOid) {
        geSxServiceAndMaterial(row.serviceOid).then(response => {
          _that.openInit = true;
          _that.form.serviceOid = response.data.sxService.serviceOid;
          _that.form.serviceName = response.data.sxService.serviceName;
          _that.form.implementCode = response.data.sxService.implementCode;
          _that.form.serviceMaterList = response.data.sxServiceMaterials;
          _that.form.serviceMaterialList = response.data.serviceMaterialList;
          _that.form.refinedMaterials = response.data.refinedMaterials;
          //console.log(JSON.stringify(_that.form.refinedMaterials))
        });

      } else {
        _that.openInit = true;
      }
      _that.title = "目录关联配置";

    },
    /** 提交按钮 */
    submitForm: function () {
      let _that = this;
      let querys={serviceOid:_that.form.serviceOid};
      this.form1.serviceOid = _that.form.serviceOid;
      updateRefinedMaterialList(_that.form.refinedMaterials).then(response => {
        _that.msgSuccess("保存成功");
        _that.addDialogShow = false;
        setTimeout(() => {
          _that.getList();
          _that.openInit = false;
        }, 10);
        //修改目录后调整对应的规则
          updateFaModelRuleValidation(querys).then(response => {
          });

      });
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
