/**
* @Author: xldong
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
<!--      <el-form-item label="办理类型">
        <el-select
          v-model="queryParams.handleType"
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="(value, key) in handleTypeMap"
            :key="key"
            :label="value"
            :value="key"
          />
        </el-select>
      </el-form-item>-->
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
    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit"
          v-hasPermi="['service:mag:init']"
          >新增</el-button
        >
      </el-col>
      <el-button type="default" size="mini" icon="el-icon-refresh" @click="getDataSynchronization()">同步万达事项</el-button>
      <el-button type="default" size="mini" icon="el-icon-refresh" @click="initKnowledge()">初始化知识库</el-button>
    </el-row>
    <el-table
      v-loading="loading"
      :data="sxServiceList"
      border
      :fit="true"
      height="calc(100% - 160px)"
    >
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
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
        width="650"
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
            v-hasPermi="['service:mag:init']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDel(scope.row)"
            v-hasPermi="['service:mag:del']"
            >删除</el-button
          >
          <el-button
            size="mini"
            type="text"
            @click="handleMa(scope.row)"
            v-hasPermi="['service:mag:material']"
            >材料</el-button
          >
          <el-button
            size="mini"
            type="text"
            @click="handlePlace(scope.row)"
            v-hasPermi="['service:mag:place']"
            >办事地点</el-button
          >
          <el-button
            size="mini"
            type="text"
            @click="handleQuestion(scope.row)"
            v-hasPermi="['service:mag:problem']"
            >常见问题</el-button
          >
          <el-button
            size="mini"
            type="text"
            @click="handleCondition(scope.row)"
            v-hasPermi="['service:mag:condition']"
            >受理条件</el-button
          >
          <el-button
            size="mini"
            type="text"
            @click="handleLink(scope.row)"
            v-hasPermi="['service:mag:linking']"
            >办理环节</el-button
          >
           <el-button
            size="mini"
            type="text"
            @click="handleCopy(scope.row)"
            v-hasPermi="['service:mag:copying']"
            >一键复制</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 信息详细 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="infoViewMa"
      v-if="infoViewMa"
      title="材料详情"
      width="1200px"
      height="700px"
      scrollbar
      append-to-body
    >
      <material-details
        :sxServiceOid="serviceOid"
        @view-service="materialClose"
      ></material-details>
      <div slot="footer" class="zf-text-center">
        <el-button @click="infoViewMa = false">
          关闭
        </el-button>
      </div>

    </el-dialog>

    <!-- 信息详细 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="infoView"
      v-if="infoView"
      title="查看实施清单详情"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <view-sx-service-info
        :sxServiceOid="serviceOid"
        @view-service="viewServiceClose"
      ></view-sx-service-info>
      <div slot="footer" class="zf-text-center">
        <el-button @click="infoView = false">
          关闭
        </el-button>
      </div>
    </el-dialog>

    <!-- 信息新增修改 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="openInit"
      v-if="openInit"
      title="新增/修改实施清单"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
      :close-on-click-modal="false"
    >
      <init-sx-service
        ref="initSxService"
        :serviceOid="serviceOid"
        @initServiceClose="initServiceClose"
      />
      <div slot="footer" class="zf-text-center">
        <el-button
          type="primary"
          @click="
            () => {
              $refs.initSxService.submitForm()
            }
          "
          >确 定</el-button
        >
        <el-button
          @click="
            () => {
              $refs.initSxService.cancel()
            }
          "
          >取 消</el-button
        >
      </div>
    </el-dialog>

    <!-- 办事地点 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="placeFlag"
      v-if="placeFlag"
      title="办事地点"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <business-place
        :serviceOid="serviceOid"
        @businessPlaceClose="businessPlaceClose"
      ></business-place>
      <div slot="footer" class="zf-text-center">
        <el-button @click="placeFlag = false">
          关闭
        </el-button>
      </div>

    </el-dialog>

    <!-- 常见问题 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="questionFlag"
      v-if="questionFlag"
      title="常见问题"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <common-question
        :createUser="createUser"
        :serviceOid="serviceOid"
        @questionClose="questionClose"
      ></common-question>
      <div slot="footer" class="zf-text-center">
        <el-button @click="questionFlag = false">
          关闭
        </el-button>
      </div>
    </el-dialog>

    <!-- 受理条件 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="conditionFlag"
      v-if="conditionFlag"
      title="受理条件"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <acceptance-condition
        :organOid="organOid"
        :createUser="createUser"
        :serviceOid="serviceOid"
        @conditionClose="conditionClose"
      ></acceptance-condition>
      <div slot="footer" class="zf-text-center">
        <el-button @click="conditionFlag = false">
          关闭
        </el-button>
      </div>
    </el-dialog>

    <!-- 办理环节 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="linkFlag"
      v-if="linkFlag"
      title="办理环节"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <handle-link
        :districtOid="districtOid"
        :organOid="organOid"
        :createUser="createUser"
        :serviceOid="serviceOid"
        @linkClose="linkClose"
      ></handle-link>
      <div slot="footer" class="zf-text-center">
        <el-button @click="linkFlag = false">
          关闭
        </el-button>
      </div>
    </el-dialog>
    <!-- 一键复制 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="copyFlag"
      v-if="copyFlag"
      title="一键复制"
      width="700px"
      height="320px"
      scrollbar
      append-to-body
    >
      <handle-copy
        :serviceOid="serviceOid"
        @copyClose="copyClose"
      ></handle-copy>

    </el-dialog>
  </div>
</template>

<script>
import { page, delSxServiceByOid, getServiceRegistrarByServiceOid,dataSynchronization,initKnowledge} from '@/api/zc/qdgl/sxService.js'
import viewSxServiceInfo from "@/views/zc/businessSupervise/viewSxServiceInfo";
import materialDetails from "@/views/zc/qdgl/materialDetails";
import initSxService from "@/views/zc/qdgl/initSxService.vue";
import businessPlace from '@/views/zc/qdgl/businessPlace'
import commonQuestion from '@/views/zc/qdgl/commonQuestion'
import HandleLink from '@/views/zc/qdgl/handleLink'
import HandleCopy from '@/views/zc/qdgl/handleCopy'
import acceptanceCondition from '@/views/zc/qdgl/acceptanceCondition'
import { getOneByUserOid, queryServiceTree } from '@/api/zc/businessManagement/sxServiceRegistrar'
export default {
  name: "SxServiceMag",
  components: { acceptanceCondition, HandleLink, HandleCopy, viewSxServiceInfo, initSxService, businessPlace, materialDetails, commonQuestion },
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      sxServiceList: [],

      // 弹出层标题
      title: "",
      // 查看显示弹出层
      infoView: false,
      infoViewMa: false,
      openInit: false,
      placeFlag: false,
      questionFlag: false,
      linkFlag: false,
      conditionFlag: false,
      copyFlag:false,//一键复制标识
      //知识库初始化状态 true表示正在初始化
      knowSta: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        serviceName: "",
        implementCode: "",
        serviceStatus: "3",
        handleType: ""

      },
      handleTypeMap: { "1": "好办快办", "2": "秒批秒办", "3": "标准" },
      // 表单参数
      form: {},

      //事项oid
      serviceOid: "",
      createUser: "",
      organOid: "",
      districtOid: "",
    };
  },
  created () {
    this.getList();
  },
  methods: {
    /** 同步万达事项库操作 */
    getDataSynchronization () {
      debugger
      this.$confirm('是否确认同步万达事项库信息?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return true;
      }).then(() => {
        dataSynchronization().then(res => {
          //提示信息
          this.$message({
            type: 'success',
            message: '同步成功'
          })
          this.getList();
        });
      }).catch(function () { });
    },
    initKnowledge () {
      const _this = this;
      if(_this.knowSta) {
        //提示信息
        this.$message({
          message: "正在初始化知识库中，不要连续点击",
          type: "error",
          duration: 5000
        })
      }else{
        this.$confirm('是否初始化知识库，初始化会删除之前的知识库数据?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return true;
        }).then(() => {
          _this.knowSta = true

          initKnowledge().then(res => {
            _this.knowSta = false
            //提示信息
            this.$message({
              type: 'success',
              message: '初始化知识成功'
            })
          }).catch(function () {
            _this.knowSta = false
          });

        }).catch(function () {
          _this.knowSta = false
        });
      }

    },

    /** 查看按钮操作 */
    handleMa (row) {
      this.serviceOid = row.serviceOid;
      this.infoViewMa = true;
    },
    /** 查询列表 */
    getList () {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.sxServiceList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel () {
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
      this.serviceOid = row.serviceOid;
      this.infoView = true;
    },
    viewServiceClose () {
      this.infoView = false;
    },
    materialClose () {
      this.infoViewMa = false;
    },
    //删除
    handleDel (row) {
      const oid = row.serviceOid;
      this.$confirm('是否确认删除?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return true;
      }).then(() => {
        getServiceRegistrarByServiceOid(oid).then(res => {
          if (res.data) {
            delSxServiceByOid(oid).then(res => {
              this.msgSuccess("删除成功");
              this.getList();
            });
          } else {
            this.$message.warning("已授权事项无法删除");
            this.getList();
          }
        });
      }).catch(function () { });
    },
    handleInit (row) {
      this.serviceOid = row.serviceOid;
      this.openInit = true;
    },
    //关闭新增页面
    initServiceClose () { this.openInit = false; this.getList() },
    handlePlace (row) {
      this.serviceOid = row.serviceOid;
      this.placeFlag = true;
    },
    //关闭办事地点页面
    businessPlaceClose () { this.placeFlag = false; },
    handleQuestion (row) {
      this.createUser = row.createUser;
      this.serviceOid = row.serviceOid;
      this.questionFlag = true;
    },
    //关闭常见问题页面
    questionClose () { this.questionFlag = false; },
    handleCondition (row) {
      this.organOid = row.organOid;
      this.createUser = row.createUser;
      this.serviceOid = row.serviceOid;
      this.conditionFlag = true;
    },
    //关闭受理条件页面
    conditionClose () { this.conditionFlag = false; },
    handleLink (row) {
      this.districtOid = row.districtOid;
      this.organOid = row.organOid;
      this.createUser = row.createUser;
      this.serviceOid = row.serviceOid;
      this.linkFlag = true;
    },
    //关闭办理环节页面
    linkClose () { this.linkFlag = false; },
    handleCopy (row) {
      this.serviceOid = row.serviceOid;
      this.copyFlag = true;
    },
    //关闭一键复制页面
    copyClose () { this.copyFlag = false; this.getList();},
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
