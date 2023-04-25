/**
 * @Author: liangxm
 */
<template>
  <div class="app-container">
    <!--事项数据-->
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
      :data="districtList"
      border
      :fit="true"
      height="calc(100% - 120px)"
    >
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
      />
      <el-table-column
        label="所属机构"
        align="center"
        width="110"
        prop="organName"
      />
      <el-table-column label="事项名称" align="center" prop="serviceName" />
      <el-table-column label="实施编码" align="center" prop="implementCode" />
      <el-table-column label="事项类型" align="center" prop="serviceTypeName" />
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
            v-hasPermi="['sys:reg:view']"
            >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            v-hasPermi="['sys:bill:update']"
            >材料目录配置</el-button
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
    <!-- 材料列表对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openInit"
      v-if="openInit"
      width="900px"
      append-to-body
    >
      <el-table v-loading="loading" :data="materialList">
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="材料名称"
          align="center"
          width="150"
          prop="materialName"
        />
        <el-table-column
          label="参考份数"
          align="center"
          width="150"
          prop="paperNumber"
        />
        <el-table-column
          label="材料类型名称"
          width="300"
          align="center"
          prop="materialTypeName"
        />

        <el-table-column
          label="操作"
          align="center"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <!--<el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)" v-hasPermi="['sys:reg:view']" >查看</el-button>-->
            <el-button
              size="mini"
              type="text"
              icon="iconfont zfsoft-xiugai"
              @click="handleInitMater(scope.row)"
              v-hasPermi="['sys:bill:update']"
              >配置</el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer" class="dialog-footer">
        <!--<el-button type="primary" @click="submitForm">确 定</el-button>-->
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改信息对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openInitMater"
      v-if="openInitMater"
      width="900px"
      append-to-body
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="0px"
        class="dialog-table"
      >
        <el-input v-show="false" v-model="form.id" />
        <el-input v-show="false" v-model="form.materialOid" />
        <table
          cellspacing="0"
          cellpadding="0"
          border="0"
          class="el-table__body"
        >
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>

          <!--<tr>
            <td>
              <b>材料名称：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="serviceName">
                <el-col :span="24">
                  {{ form.serviceName }}
                </el-col>
              </el-form-item>
            </td>
          </tr>
-->

          <tr>
            <td>
              <b>选择证照目录：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="billOid">
                <el-select
                  v-model="form.billOid"
                  filterable
                  clearable
                  style="width: 50%"
                >
                  <el-option
                    v-for="dict in billList"
                    :key="dict.billOid"
                    :label="dict.directoryName"
                    :value="dict.billOid"
                  ></el-option>
                </el-select>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancelMater">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 信息详细 -->
    <el-dialog
      :title="title"
      :visible.sync="openView"
      width="800px"
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
        <table
          cellspacing="0"
          cellpadding="0"
          border="0"
          class="el-table__body"
        >
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
              <el-form-item prop="districtName">
                <el-col :span="24">
                  {{ form.districtName }}
                </el-col>
              </el-form-item>
            </td>
            <td>
              <b>所属机构：</b>
            </td>
            <td>
              <el-form-item prop="organName">
                <el-col :span="24">
                  {{ form.organName }}
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>事项名称：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="serviceName">
                <el-col :span="24">
                  {{ form.serviceName }}
                </el-col>
              </el-form-item>
            </td>
          </tr>
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
              <b>基本编码：</b>
            </td>
            <td>
              <el-form-item prop="basicCode">
                <el-col :span="24">
                  {{ form.basicCode }}
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>事项类型：</b>
            </td>
            <td>
              <el-form-item prop="serviceTypeOid">
                <el-col :span="24">
                  {{ form.serviceTypeName }}
                </el-col>
              </el-form-item>
            </td>
            <td>
              <b>行使层级：</b>
            </td>
            <td>
              <el-form-item prop="levelName">
                <el-col :span="24">
                  {{ form.levelName }}
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>服务对象：</b>
            </td>
            <td>
              <el-form-item prop="serviceObject">
                <el-col :span="24">
                  {{ form.serviceObjectName }}
                </el-col>
              </el-form-item>
            </td>
            <td>
              <b>办件类型：</b>
            </td>
            <td>
              <el-form-item prop="caseType">
                <el-col :span="24">
                  {{ form.caseTypeName }}
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>是否收费：</b>
            </td>
            <td>
              <el-form-item prop="chargeFlag">
                <el-col :span="24">
                  {{ form.chargeFlagName }}
                </el-col>
              </el-form-item>
            </td>
            <td>
              <b>办理形式：</b>
            </td>
            <td>
              <el-form-item prop="handleForm">
                <el-col :span="24">
                  {{ form.handleFormName }}
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>是否支持在线预约：</b>
            </td>
            <td>
              <el-form-item prop="appointmentFlag">
                <el-col :span="24">
                  {{ form.appointmentFlagName }}
                </el-col>
              </el-form-item>
            </td>
            <td>
              <b>行政管辖地：</b>
            </td>
            <td>
              <el-form-item prop="adminJurisdiction">
                <el-col :span="24">
                  {{ form.adminJurisdictionName }}
                </el-col>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { page, handleInitMater, pageMaterial, getSxServiceOne, saveOrUpdate, queryMaterBill } from "@/api/onething/sxpz/sxSerMaterBill";
import { validateNumber, validIntNoZero } from "@/utils/validate";
// import the component
import Treeselect from '@riophae/vue-treeselect';
// import the styles
import '@riophae/vue-treeselect/dist/vue-treeselect.css';
export default {
  name: "sxServiceRegistrar",
  components: { Treeselect },
  data () {
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
      openInitMater: false,
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
        billOid: ''//主题

      },
      materialList: [],
      billList: [],

      // 表单校验
      rules: {
        billOid: [
          { required: true, message: "证照目录不能为空", trigger: "blur" }
        ]
      },
      sxServiceReg: { serviceOid: '', userOids: '', serviceName: '', serviceType: '', serviceTypeName: '', organOid: '', organName: '' },
      userOidArr: [],
      props: {
        label: "label",//这里是树结构中需显示的数据（即接口返回的需展示在页面上的参数）
        children: 'children',
        isLeaf: "leaf"
      },

    };
  },
  created () {
    this.getList();



  },
  methods: {

    /** 查询列表 */
    getList () {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.districtList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel () {
      this.openInit = false;


      this.reset();
    },
    cancelMater () {

      this.openInitMater = false;
      pageMaterial(this.form.configOid).then(response => {
        this.materialList = response.data;
        this.openInit = true;
        //this.total = response.data.total;
        this.loading = false;

      });

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
      _that.loading = true;
      if (row.serviceOid) {
        pageMaterial(row.serviceOid).then(response => {
          _that.materialList = response.data;
          _that.openInit = true;
          //this.total = response.data.total;
          _that.loading = false;

        });
        _that.title = "材料列表";
      }
    },
    getBillList () {
      let _that = this;
      // 查询文书模板数据
      queryMaterBill().then(response => {
        _that.billList = response.data;
      });
    },
    /** 新增和修改按钮操作 */
    handleInitMater (row) {
      let _that = this;
      _that.loading = true;
      if (row.materialOid) {
        handleInitMater(row.materialOid, row.serviceOid).then(response => {
          _that.openInitMater = true;
          _that.form = response.data;


        });
        _that.getBillList();
        _that.title = "证照目录关联";
      }
    },
    /** 提交按钮 */
    submitForm: function () {
      let _that = this;
      _that.$refs["form"].validate(valid => {
        if (valid) {

          saveOrUpdate(_that.form).then(response => {
            if (response.code == 200) {
              _that.msgSuccess("保存成功");
              _that.openInitMater = false;

              pageMaterial(_that.form.configOid).then(response => {
                _that.materialList = response.data;
                _that.openInit = true;
                //this.total = response.data.total;
                _that.loading = false;

              });
              _that.title = "材料列表";
            }
          });
        }
      });
    }
  }
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
