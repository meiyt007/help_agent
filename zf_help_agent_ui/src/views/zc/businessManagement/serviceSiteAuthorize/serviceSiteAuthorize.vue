/**
 * @Author: dongxl
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
            v-hasPermi="['sys:site:view']"
            >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shouquan"
            @click="handleInit(scope.row)"
            v-hasPermi="['sys:site:update']"
            >授权</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-quxiao"
            @click="deleteSQ(scope.row)"
            v-hasPermi="['sys:site:update']"
            >取消</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
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
      width="900px"
      append-to-body
    >
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="19%" />
          <col width="33%" />
          <col width="19%" />
          <col width="29%" />
        </colgroup>

        <tr>
          <td>
            <b>所属区划：</b>
          </td>
          <td>
            <el-col :span="24">
              {{ form.districtName }}
            </el-col>
          </td>
          <td>
            <b>所属机构：</b>
          </td>
          <td>
            <el-col :span="24">
              {{ form.organName }}
            </el-col>
          </td>
        </tr>
        <tr>
          <td>
            <b>事项名称：</b>
          </td>
          <td colspan="3">
            <el-col :span="24">
              {{ form.serviceName }}
            </el-col>
          </td>
        </tr>
        <tr>
          <td>
            <b>实施编码：</b>
          </td>
          <td>
            <el-col :span="24">
              {{ form.implementCode }}
            </el-col>
          </td>
          <td>
            <b>基础编码：</b>
          </td>
          <td>
            <el-col :span="24">
              {{ form.basicCode }}
            </el-col>
          </td>
        </tr>

        <tr>
          <td>
            <b>选择辖区：</b>
          </td>
          <td colspan="3">
            <treeselect
              :multiple="true"
              :disable-branch-nodes="true"
              ref="userTree"
              :defaultExpandLevel="1"
              noOptionsText="暂无数据"
              noResultsText="暂无数据"
              :show-count="true"
              v-model="form.siteOids"
              :options="appSiteOptions"
              placeholder="请选择辖区"
              append-to-body
            />
          </td>
        </tr>
      </table>

      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitRole">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 信息详细 -->
    <!--实施清单详细-->
    <el-dialog
      v-dialog-drag
      :visible.sync="sxServiceInfo.show"
      v-for="(sxServiceInfo, index) in serviceDialogOptions"
      :key="index"
      title="查看实施清单详情"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <view-sx-service-info
        :sxServiceOid="sxServiceInfo.sxServiceOid"
        @view-service="viewServiceClose"
      />
      <div slot="footer" class="zf-text-center">
        <el-button @click="sxServiceInfo.show = false"> 关闭 </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { page, getSxServiceByOid } from "@/api/zc/businessManagement/sxServiceRegistrar";
import { saveOrUpdate, querySiteAuthorizeTree, getSiteOidsByServiceOid, del } from "@/api/zc/businessManagement/serviceSiteAuthorize";
// import the styles
import '@riophae/vue-treeselect/dist/vue-treeselect.css';
import Treeselect from '@riophae/vue-treeselect';
import viewSxServiceInfo from "@/views/zc/businessSupervise/viewSxServiceInfo";
export default {
  name: "ServiceSiteAuthorize",
  components: { Treeselect, viewSxServiceInfo },
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
      // 查看显示弹出层
      openView: false,
      appSiteOptions: [],
      serviceDialogOptions: [],
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
      form: {},
      // 表单校验
      rules: {
        siteOids: [
          { type: 'array', required: true, message: '所选辖区不能为空', trigger: 'change' }
        ]
      },
      sxServiceReg: { serviceOid: '', siteOid: '' },
      siteOidArr: [],
      props: {
        label: "label",//这里是树结构中需显示的数据（即接口返回的需展示在页面上的参数）
        children: 'children',
        isLeaf: "leaf"
      },
      watch: {
        'form.siteOids': function (val) {
          if (this.form.siteOids && this.form.siteOids > 0) {
            this.form.siteOids = this.form.siteOids[0];
          }
        }
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
      /*let _that = this;
     // _that.reset();
      const oid = row.serviceOid;
      getSxServiceOne(oid).then(response => {
        _that.form = response.data;
        _that.openView = true;
        _that.title = "查看事项信息";
      });*/
      let item = {
        show: true,
        sxServiceOid: row.serviceOid
      };
      this.serviceDialogOptions.push(item)
    },

    viewServiceClose () {
      this.serviceDialogOptions.pop();
    },
    deleteSQ (row) {//删除授授
      const oid = row.serviceOid;
      this.$confirm('确认取消授权吗?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return del(oid);
      }).then(() => {
        this.msgSuccess("取消成功");
        this.getList();
      }).catch(function () { });
    },
    /** 新增和修改按钮操作 */
    handleInit (row) {
      let _that = this;
      // _that.reset();
      if (row.serviceOid) {
        this.sxServiceReg.serviceOid = row.serviceOid;
        getSxServiceByOid(row.serviceOid).then(response => {
          this.openInit = true;
          _that.form = response.data;
        });
        let treeQuery = {
          appOid: '',
          disable: this.disable
        }
        getSiteOidsByServiceOid(row.serviceOid).then(response => {
          _that.openInit = true;
          _that.siteOidArr = response.data ? response.data.split(';') : [];
          //辖区树
          querySiteAuthorizeTree(treeQuery).then(res => {
            _that.appSiteOptions = res.data || [];
            _that.openInit = true;
            _that.form.siteOids = _that.siteOidArr;
          })
        });
      } else {
        _that.openInit = true;
      }

      _that.title = "辖区授权";

    },
    submitRole () {
      if (this.form.siteOids && this.form.siteOids.length > 0) {
        this.sxServiceReg.siteOid = this.form.siteOids.join(';');
        this.sxServiceReg.serviceName = this.form.serviceName;
        this.sxServiceReg.serviceType = this.form.serviceTypeOid;
        saveOrUpdate(this.sxServiceReg).then(res => {
          if (res.code === 200) {
            this.msgSuccess('保存成功')
            this.openInit = false
            this.getList()
          }
        })
      } else {
        this.$refs['form'].validate(valid => {
        })
      }
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
  text-align: left !important;
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
