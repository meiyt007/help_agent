/**
* 单办事项材料智能分类配置
* @author: liangss
* @date: 2021-06-08
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
                >材料分类器配置</el-button
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
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="0px"
        class="dialog-table"
      >
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
          <tr>
            <td><i class="require">*</i><b>是否开启自动分类状态：</b></td>
            <td colspan="3">
              <el-form-item prop="autoClassificationStatus">
                <el-radio-group
                  v-model="form.autoClassificationStatus"
                  @change="changeProxyFlag"
                >
                  <el-radio label="Y">是</el-radio>
                  <el-radio label="N">否</el-radio>
                </el-radio-group>
              </el-form-item>
            </td>
          </tr>

          <tr v-if="proxy_show">
            <td><i class="require">*</i><b>分类器ID：</b></td>
            <td colspan="3">
              <el-form-item prop="classifierId">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.classifierId"
                    name="classifierId"
                    maxlength="6"
                    show-word-limit
                  ></el-input>
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
          class="tc"
          v-if="proxy_show"
        >
          <colgroup>
            <col width="50" />
            <col width="100" />
            <col width="300" />
          </colgroup>
          <tr>
            <th>序号</th>
            <th>材料名称</th>
            <th>百度模板id</th>
          </tr>
          <template v-for="(ruleForm, index) in form.serviceMaterList">
            <tr>
              <td>
                <el-form-item prop="xuhao">
                  {{ index + 1 }}
                </el-form-item>
              </td>
              <td>
                <el-form-item>
                  <el-input
                    v-model.trim="ruleForm.materialName"
                    readonly
                  ></el-input>
                </el-form-item>
              </td>
              <td>
                <el-form-item>
                  <el-input v-model.trim="ruleForm.baiduTemplateIds"></el-input>
                </el-form-item>
              </td>
            </tr>
          </template>

          <template v-for="(ruleForm, index) in form.serviceMaterialList">
            <tr>
              <td>
                <el-form-item prop="xuhao">
                  {{ index + 1 + form.serviceMaterList.length }}
                </el-form-item>
              </td>
              <td>
                <el-form-item>
                  <el-input
                    v-model.trim="ruleForm.materialName"
                    readonly
                  ></el-input>
                </el-form-item>
              </td>
              <td>
                <el-form-item>
                  <el-input v-model.trim="ruleForm.baiduTemplateIds"></el-input>
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
      v-dialog-drag
      height="600px"
      scrollbar
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
import { pageList, page, getSxServiceOne } from "@/api/zc/clzs/catalogRelatedManage/catalogRelatedManage.js";
import { saveOrUpdateSxServiceMaterialClassifier } from "@/api/zc/businessManagement/sxServiceMaterialClassifier.js";

import { queryServiceTypeSimpleTree } from "@/api/sxpt/serviceType";
import { validateNumber, validIntNoZero } from "@/utils/validate";
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
      // 事项类型
      serviceTypeOptions: [],
      //目录列表
      catalogList: [],
      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      proxy_show: false,
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
        autoClassificationStatus: "N",
        classifierId: "",
        serviceMaterList: [],
        serviceMaterialList: [],
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
    /* alert(JSON.stringify(this.form))*/
    this.getList();
    this.getServiceTypeTree();
    this.getMaterialCatalogList();
  },
  methods: {
    /** 是否启动分类器*/
    changeProxyFlag (val) {
      this.proxy_show = (val === 'Y') ? true : false;
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
    /** 新增和修改按钮操作 */
    handleInit (row) {
      let _that = this;
      _that.reset();
      if (row.serviceOid) {
        getSxServiceOne(row.serviceOid).then(response => {
          _that.openInit = true;
          /*   alert(response.data.sxService.autoClassificationStatus==null);*/
          _that.form.id = response.data.sxService.id;
          _that.form.serviceOid = response.data.sxService.serviceOid;
          _that.form.serviceName = response.data.sxService.serviceName;
          _that.form.implementCode = response.data.sxService.implementCode;
          if (response.data.sxService.autoClassificationStatus == null) {
            _that.form.autoClassificationStatus = "N";
            _that.proxy_show = false;
          } else {
            if (response.data.sxService.autoClassificationStatus == 'Y') {
              _that.proxy_show = true;
            } else {
              _that.proxy_show = false;
            }
            _that.form.autoClassificationStatus = response.data.sxService.autoClassificationStatus;
            _that.form.classifierId = response.data.sxService.classifierId;
          }
          _that.form.serviceMaterList = response.data.sxServiceMaterials;
          _that.form.serviceMaterialList = response.data.serviceMaterialList;


          /* let materList=  _that.form.serviceMaterList;
           for(let mater of materList){
             this.form.materialCatalogOid.push(mater.materialCatalogOid);
           }*/
        });

      } else {
        _that.openInit = true;
      }
      _that.title = "目录关联配置";

    },
    /** 提交按钮 */
    submitForm: function () {
      let _that = this;

      /*  console.log(JSON.stringify(_that.form))*/
      if (this.form.autoClassificationStatus == 'Y') {
        if (!this.form.classifierId) {
          this.$message.error("分类器ID不能为空！");
          return false;
        }
      }
      saveOrUpdateSxServiceMaterialClassifier(_that.form).then(response => {
        if (response.code === 200) {
          _that.msgSuccess("保存成功");
          _that.addDialogShow = false;
          setTimeout(() => {
            _that.getList();
            _that.openInit = false;
          }, 10);
        }
      });
    }
    /*  this.form1.serviceOid=_that.form.serviceOid;
      let materList=  _that.form.serviceMaterList;
      let materialCatalogOidList=  _that.form.materialCatalogOid;
      for(let catalogOid of materialCatalogOidList){
        if(catalogOid==null){
          catalogOid="";
        }
        this.form1.materialCatalogOids.push(catalogOid);
      }
      for(let mater of materList){
        this.form1.materialOids.push(mater.materialOid);
      }*/
    /* saveOrUpdateSxServiceMaterialClassifier(_that.form1).then(response => {
       if (response.code === 200) {
         //如果关联目录发生变化，删除已配置的验证规则 这个功能没有做
         _that.msgSuccess("保存成功");
         _that.addDialogShow = false;
         setTimeout(() => {
           _that.getList();
           _that.openInit = false;
         }, 10);
       }
     });*/


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
