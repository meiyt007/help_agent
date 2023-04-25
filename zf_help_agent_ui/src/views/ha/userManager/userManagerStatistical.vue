/** * @Author: wangns */
<!-- 请求条件 -->
<template>
  <div class="app-container">
    <!--用户数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="80px"
    >
      <el-form-item label="姓名" prop="name">
        <el-input
          v-model.trim="queryParams.name"
          placeholder="姓名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input
          v-model.trim="queryParams.phone"
          placeholder="手机号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属区划">
        <treeselect
          v-model="queryParams.districtOidSelect"
          :options="districtOptions"
          style="width: 240px"
          noOptionsText="暂无数据"
          :defaultExpandLevel="1"
          placeholder="请输入所属区划"
        />
      </el-form-item>
      <!--<el-form-item label="人员类型" prop="userType">
        <el-col :span="24">
          <el-select
            v-model.trim="queryParams.userType"
            placeholder="请选择人员类型"
            size="small" >
            <el-option label="导服人员" value="1"></el-option>
            <el-option label="帮代办人员" value="2"></el-option>
          </el-select>
        </el-col>
      </el-form-item>-->
      <el-form-item class="fr mr0">
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
        >搜索</el-button>
        <el-button
          type="warning"
          icon="el-icon-refresh"
          size="mini"
          @click="resetQuery"
          class="btn-reset"
        >重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 表头 -->
    <el-table
      v-loading="loading"
      :data="districtList"
      stripe
      style="width: 100%"
    >
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="所属区划"
        align="center"
        prop="districtOid"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="姓名"
        align="center"
        prop="name"
        :show-overflow-tooltip="true"
      />
      <!--<el-table-column
        label="账号"
        align="center"
        prop="account"
        :show-overflow-tooltip="true"
      />-->
      <el-table-column
        label="手机号"
        align="center"
        prop="phone"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="用户类型"
        align="center"
        prop="userType"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="帮代办类型"
        align="center"
        prop="haType"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="在线状态"
        align="center"
        prop="status"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createDate"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="操作"
        align="center"
        width="340"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleHistoryView(scope.row)"

          >帮办历史查看</el-button>
        <!-- v-hasPermi="['im:visitManager:view']"-->
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-hudongguanli"
            @click="handleMyServiceTransferView(scope.row)"
          >服务转派查看</el-button>

        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      style="padding: 30px 0; text-align: center;"
      layout="total, sizes, prev, pager, next, jumper"
      @pagination="getList"
    />

    <!--帮办历史查看信息-->
    <el-dialog
      title="帮办历史查看"
      :close-on-click-modal="false"
      :visible.sync="openView"
      width="1350px"
      append-to-body
      height="800px"
      scrollbar
    >
      <el-form
        :model="queryQueueParams"
        ref="queryForm"
        :inline="true"
        label-width="100px"
      >
        <el-form-item label="来访日期">
          <el-date-picker
            value-format="yyyy-MM-dd"
            v-model="dateValue"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :append-to-body="false"
            @change="changeDate"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="服务状态" prop="serviceStatus">
          <el-select
            v-model="queryQueueParams.serviceStatus"
            placeholder="请选择"
            size="small"
            style="width: 240px"
          >
            <el-option
              v-for="item in serviceStatusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item class="fr mr0">
          <el-button
            type="primary"
            icon="el-icon-search"
            size="mini"
            @click="handleHistoryQuery"
          >搜索</el-button>
          <!--<el-button
            size="medium"
            @click="resetHistory"
            icon="el-icon-refresh-right"
          >重置</el-button>-->
        </el-form-item>
      </el-form>
      <el-table
        ref="multipleTable"
        :data="helpHistoryQueuelist"
        tooltip-effect="dark"
        style="width: 100%"
      >
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="办事人姓名"
          align="center"
          prop="name"
          show-overflow-tooltip
        />
        <el-table-column
          label="身份证号"
          align="center"
          prop="cardNo"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          label="服务开始时间"
          align="center"
          prop="serviceBeginTime"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          label="服务结束时间"
          align="center"
          prop="serviceEndTime"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          label="服务时长"
          align="center"
          prop="serviceDuration"
          show-overflow-tooltip
        >
        </el-table-column>


        <el-table-column
          label="来访时间"
          align="center"
          prop="createDate"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          label="服务状态"
          align="center"
          prop="serviceStatus"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <p v-if="scope.row.serviceStatus==1">等待服务</p>
            <p v-if="scope.row.serviceStatus==2">服务中</p>
            <p v-if="scope.row.serviceStatus==3">服务完成</p>
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
              @click="handleQueueView(scope.row)"
            >查看</el-button>
          </template>
        </el-table-column>

      </el-table>
      <pagination
        v-show="queueTotal > 0"
        :total="queueTotal"
        :page.sync="queryQueueParams.pageNum"
        :limit.sync="queryQueueParams.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        @pagination="handleHistoryViewList()"
      />

    </el-dialog>

    <!-- 服务记录信息-->
    <el-dialog
      title="服务记录信息查看"
      :close-on-click-modal="false"
      :visible.sync="openQueueView"
      width="1350px"
      append-to-body
      height="800px"
      scrollbar
    >
      <el-table
        ref="multipleTable"
        :data="middleServicelist"
        tooltip-effect="dark"
        style="width: 100%"
      >
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="办事人姓名"
          align="center"
          show-overflow-tooltip
        >
          <template>
            <span>{{ middleUser.name }}</span>
          </template>
        </el-table-column>

        <el-table-column
          label="经办企业信息"
          align="center"
          show-overflow-tooltip
        >
          <template>
            <span>{{ middleUser.companyName }}</span>
          </template>
        </el-table-column>

        <!--<el-table-column
          label="统一社会信用代码"
          align="center"
          prop="companyCode"
          show-overflow-tooltip
        />-->
        <el-table-column
          label="办理事项"
          align="center"
          prop="serviceName"
          show-overflow-tooltip
        />
        <el-table-column
          label="服务内容"
          align="center"
          prop="serviceMemo"
          show-overflow-tooltip
        />
        <el-table-column
          label="服务人员"
          align="center"
          prop="workUserName"
          show-overflow-tooltip
        />
        <el-table-column
          label="服务类型"
          align="center"
          prop="serviceType"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <p v-if="scope.row.serviceType==1">咨询</p>
            <p v-if="scope.row.serviceType==2">材料准备</p>
            <p v-if="scope.row.serviceType==3">收件</p>
            <p v-if="scope.row.serviceType==4">一键推送</p>
          </template>
        </el-table-column>
        <el-table-column
          label="服务状态"
          align="center"
          prop="serviceStatus"
          show-overflow-tooltip
        >
          <!--          3-收件：1-已完成，2-暂存（需要后续提交）；-->
          <template slot-scope="scope">
            <p v-if="scope.row.serviceStatus==1">已完成</p>
            <p v-if="scope.row.serviceStatus==2">暂存</p>
            <p v-if="scope.row.serviceStatus==3">收件</p>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleServiceView(scope.row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="openQueueView = false">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog v-dialog-drag :visible.sync="openServiceView" v-if="openServiceView" title="查看办件信息" width="1100px" height="700px"
               scrollbar append-to-body>
      <view-case-info :caseNumber="indexCaseNumber" @view-case="viewCaseClose"></view-case-info>
      <div slot="footer" style="text-align: center">
        <el-button @click="openServiceView = false"> 关闭 </el-button>
      </div>
    </el-dialog>



    <!--  服务转派列表  -->
    <el-dialog
      title="服务转派查看"
      :close-on-click-modal="false"
      :visible.sync="openMyServiceTransferView"
      width="1350px"
      append-to-body
      height="800px"
      scrollbar
    >
      <el-form
        :model="queryMyServiceTransferParams"
        ref="queryForm"
        :inline="true"
        label-width="100px"
      >
        <el-form-item label="办理日期">
          <el-date-picker
            value-format="yyyy-MM-dd"
            v-model="dateValues"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :append-to-body="false"
            @change="changeMyDate"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="办事人姓名">
          <el-input v-model="queryMyServiceTransferParams.name"></el-input>
        </el-form-item>
        <el-form-item label="办事人手机号">
          <el-input v-model="queryMyServiceTransferParams.phone"></el-input>
        </el-form-item>
        <el-form-item class="fr mr0">
          <el-button
            @click="handleMyServiceTransferQuery"
            type="primary"
            icon="el-icon-search"
            size="medium"
          >搜索</el-button>
          <!--<el-button
            size="medium"
            @click="resetMyServiceTransfer"
            icon="el-icon-refresh-right"
          >重置</el-button>-->
        </el-form-item>
      </el-form>
      <el-table
        ref="multipleTable"
        :data="helpMyServiceTransferList"
        tooltip-effect="dark"
        style="width: 100%"
      >
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="办事人姓名"
          align="center"
          prop="name"
          show-overflow-tooltip
        />
        <el-table-column
          label="办事人手机号"
          align="center"
          prop="phone"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          label="办理日期"
          align="center"
          prop="turnTime"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          label="服务时长"
          align="center"
          prop="serviceDuration"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          label="帮办人员"
          align="center"
          prop="serviceWorkUserName"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          label="服务状态"
          align="center"
          prop="turnStatus"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <p v-if="scope.row.turnStatus==1">未审核</p>
            <p v-if="scope.row.turnStatus==2">审核通过</p>
            <p v-if="scope.row.turnStatus==3">审核不通过</p>
          </template>
        </el-table-column>
        <el-table-column
          label="描述"
          align="center"
          prop="rollbackMemo"
          show-overflow-tooltip
        ></el-table-column>

      </el-table>

      <pagination
        v-show="queueMyServiceTransferTotal > 0"
        :total="queueMyServiceTransferTotal"
        :page.sync="queryMyServiceTransferParams.pageNum"
        :limit.sync="queryMyServiceTransferParams.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        @pagination="handleMyServiceTransferList()"
      />

    </el-dialog>

  </div>
</template>

<script>
  import { page,queryAllWorkQueueListByWorkUserId,queryAllTurnServiceListByWorkUserId } from "@/api/ha/userManagement/userManagerStatistical.js";
  import { queryDistrictSimpleTree } from "@/api/sys/district";
  import {pageQueueVisit} from "@/api/ha/visitManagement/visitManager.js";
  import Treeselect from '@riophae/vue-treeselect'
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import viewCaseInfo from "@/views/zc/businessManagement/windowAcceptance/viewCaseInfo";
  import {second} from "../../../../../zf_web_ui/src/utils";
  export default {
    components: { Treeselect,viewCaseInfo },
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
        // 查看显示弹出层
        openView: false,
        openServiceView: false,
        openQueueView: false,
        openMyServiceTransferView: false,

        indexCaseNumber:"",
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: "",
          account: "",
          phone: "",
          districtOid: "",
          userType: "",
        },
        serviceStatusOptions: [
          { label: "全部", value: "" },
          { label: "等待服务", value: "1" },
          { label: "服务中", value: "2" },
          { label: "服务完成", value: "3" },
        ],
        options: [{
          value: '1',
          label: '导服人员'
        }, {
          value: '2',
          label: '帮代办人员'
        }, {
          value: '3',
          label: '委办局人员'
        }],
        //区划数据
        districtOptions: [],
        dateValue: "",
        queueTotal: 0,
        //帮办历史查看搜索条件
        queryQueueParams: {
          beginTime: null,
          endTime: null,
          serviceStatus: null,
          serviceWorkUserId: null,
          pageNum: 1,
          pageSize: 10,
        },
        helpHistoryQueuelist: [],
        middleServicelist: [],
        middleUser:{},
        //服务转派查询条件
        queryMyServiceTransferParams: {
          beginTime: null,
          endTime: null,
          name: "",
          phone: "",
          serviceWorkUserId: null,
          pageNum: 1,
          pageSize: 10,
        },
        dateValues: "",
        queueMyServiceTransferTotal: 0,
        helpMyServiceTransferList: [],
      };
    },
    watch: {
      'queryParams.districtOidSelect': {
        handler (val, oldVal) {
          let dataId = null != val ? val.substring(val.lastIndexOf('-') + 1, val.length) : '';
          this.queryParams.districtOid = dataId;
        }
      },
      'this.helpHistoryQueuelist':{
      },

    },
    created() {
      this.getList();//查询帮办人信息列表
      this.getDistrictTree();//获取区划树
    },
    computed: {

    },
    methods: {
      //切换帮办历史时间
      changeDate() {
        this.queryQueueParams.beginTime = this.dateValue[0];
        this.queryQueueParams.endTime = this.dateValue[1];
      },
      //切换转派查询时间
      changeMyDate() {
        this.queryMyServiceTransferParams.beginTime = this.dateValues[0];
        this.queryMyServiceTransferParams.endTime = this.dateValues[1];
      },
      /** 获取区划树 */
      getDistrictTree (districtOid) {
        queryDistrictSimpleTree(districtOid).then(response => {
          this.districtOptions = response.data;
        });
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
      //帮办历史查看
      handleHistoryView(row) {
        this.dateValue = "";
        this.resetHistory();//帮办历史重置
        this.queryQueueParams.serviceWorkUserId = row.id;
        this.handleHistoryViewList();
      },
      //帮办历史搜索
      handleHistoryQuery() {
        this.queryQueueParams.pageNum = 1;
        this.queryQueueParams.pageSize = 10;
        this.handleHistoryViewList();
      },
      //帮办历史分页查询
      handleHistoryViewList() {
        this.queryQueueParams.beginTime= this.queryQueueParams.beginTime
          ? this.queryQueueParams.beginTime + " 00:00:00"
          : "";
        this.queryQueueParams.endTime= this.queryQueueParams.endTime
          ? this.queryQueueParams.endTime + " 23:59:59"
          : "";
        queryAllWorkQueueListByWorkUserId(this.queryQueueParams).then(response => {
          this.helpHistoryQueuelist = response.data.data;
          this.helpHistoryQueuelist.forEach((item) => {
            if (item.serviceDuration) {
              item.serviceDuration = second(item.serviceDuration);
            }
            if (item.serviceBeginTime) {
              item.serviceBeginTime = item.serviceBeginTime.split(" ")[1];
            }
            if (item.serviceEndTime) {
              item.serviceEndTime = item.serviceEndTime.split(" ")[1];
            }
            item.companyInfo =
              item.companyName +
              (item.companyCode ? `(${item.companyCode})` : "");
          });
          this.openView = true;
          this.queueTotal = response.data.total;
          this.title = "帮办历史查看";
        })
      },
      // 帮办历史重置
      resetHistory() {
        this.helpHistoryQueuelist= [];
        this.queryQueueParams = {
          serviceStatus: null,
          serviceWorkUserId: null,
          beginTime: null,
          endTime: null,
          pageNum: 1,
          pageSize: 10,
        };
      },
      /** 查看服务记录操作 */
      handleQueueView(row) {
        pageQueueVisit(row.id).then(response => {
          this.middleUser = response.data.baseInfo;
          this.middleServicelist = response.data.serviceHistoryInfo;
          this.openQueueView = true;
        })

      },
      /** 查看服务详情按钮操作 */
      handleServiceView(row) {
        this.indexCaseNumber = row.caseNumber;
        this.openServiceView = true;
        this.dialogTitle = "查看详情";
      },
      viewCaseClose() {
        this.openServiceView = false;
      },
      //我的服务转派列表查看
      handleMyServiceTransferView(row) {
        this.dateValues = "";
        this.resetMyServiceTransfer();
        this.queryMyServiceTransferParams.serviceWorkUserId = row.id;
        this.handleMyServiceTransferList();
      },
      //我的服务转派搜索
      handleMyServiceTransferQuery() {
        this.queryMyServiceTransferParams.pageNum = 1;
        this.queryMyServiceTransferParams.pageSize = 10;
        this.handleMyServiceTransferList();
      },
      //我的服务转派列表分页查询
      handleMyServiceTransferList() {
        this.queryMyServiceTransferParams.beginTime= this.queryMyServiceTransferParams.beginTime
          ? this.queryMyServiceTransferParams.beginTime + " 00:00:00"
          : "";
        this.queryMyServiceTransferParams.endTime= this.queryMyServiceTransferParams.endTime
          ? this.queryMyServiceTransferParams.endTime + " 23:59:59"
          : "";
        queryAllTurnServiceListByWorkUserId(this.queryMyServiceTransferParams).then(response => {
          this.helpMyServiceTransferList = response.data.data;
          this.helpMyServiceTransferList.forEach((item) => {
            if (item.serviceDuration) {
              item.serviceDuration = second(item.serviceDuration);
            }
          });
          this.openMyServiceTransferView = true;
          this.queueMyServiceTransferTotal = response.data.total;
          this.title = "服务转派查看";
        })
      },
      //重置转派服务列表
      resetMyServiceTransfer() {
        this.helpMyServiceTransferList= [];
        this.queryMyServiceTransferParams = {
          beginTime: null,
          endTime: null,
          name: "",
          phone: "",
          serviceWorkUserId: null,
          pageNum: 1,
          pageSize: 10,
        };
      },

    },
  };
</script>

<style lang="scss" scoped>
  //头像样式start
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 100px;
    display: block;
  }
  // end
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
  .zf-zc-table tr td {
    padding: 2px 10px !important;
  }
</style>
