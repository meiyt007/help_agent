<template>
  <div class="app-container typeApp">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="100px"
    >
      <el-form-item label="名称" prop="name">
        <el-input
          v-model.trim="queryParams.name"
          placeholder="请输入名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="类型" prop="visitType">
        <el-select
          v-model="queryParams.visitType"
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="(value, key) in iconTypeMap"
            :key="key"
            :label="value"
            :value="key"
          />
        </el-select>
      </el-form-item>
      <el-form-item class="fr mr0">
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
        >搜索</el-button
        >
        <!--        <el-button-->
        <!--          type="warning"-->
        <!--          icon="el-icon-refresh"-->
        <!--          size="mini"-->
        <!--          @click="resetQuery"-->
        <!--          class="ml5"-->
        <!--        >重置</el-button-->
        <!--        >-->
      </el-form-item>
    </el-form>


    <el-table
      ref="multipleTable"
      :data="middleUserList"
      tooltip-effect="dark"
      style="width: 100%"
    >
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="名称"
        align="center"
        prop="name"
        show-overflow-tooltip
      />
      <el-table-column
        label="证件号码"
        align="center"
        prop="cardNo"
        show-overflow-tooltip
      />
      <el-table-column
        label="类型"
        align="center"
        prop="visitType"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <p v-if="scope.row.visitType==1">个人</p>
          <p v-if="scope.row.visitType==2">法人</p>
        </template>
      </el-table-column>
      <el-table-column
        label="来访时间"
        align="center"
        prop="createDate"
        show-overflow-tooltip
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
            v-hasPermi="['im:visitManager:view']"
          >查看</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNumber"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <!--查看来访信息-->
    <el-dialog
      title="来访详细查看"
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
        <el-form-item label="名称" prop="name">
          <el-input
            v-model.trim="queryQueueParams.name"
            placeholder="请输入名称"
            clearable
            size="small"
          />
        </el-form-item>
<!--        <el-form-item label="所属工作人员" prop="serviceWorkUserId">-->
<!--          <el-input-->
<!--            v-model.trim="queryQueueParams.serviceWorkUserId"-->
<!--            placeholder="请选择所属工作人员"-->
<!--            clearable-->
<!--            size="medium"-->
<!--          />-->
<!--        </el-form-item>-->
        <el-form-item prop="serviceWorkUserId">
          <el-select
            v-model="queryQueueParams.serviceWorkUserId"
            placeholder="请选择所属工作人员"
            clearable
            filterable
            style="width: 170px"
          >
            <el-option
              v-for="item in userList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="服务状态" prop="serviceStatus">
          <el-select
            v-model="queryQueueParams.serviceStatus"
            size="small"
            style="width: 240px"
          >
            <el-option
              v-for="(value, key) in serviceStatusMap"
              :key="key"
              :label="value"
              :value="key"
            />
          </el-select>
        </el-form-item>
        <el-form-item class="fr mr0">
          <el-button
            type="primary"
            icon="el-icon-search"
            size="mini"
            @click="handleQueueQuery"
          >搜索</el-button
          >
          <!--        <el-button-->
          <!--          type="warning"-->
          <!--          icon="el-icon-refresh"-->
          <!--          size="mini"-->
          <!--          @click="resetQuery"-->
          <!--          class="ml5"-->
          <!--        >重置</el-button-->
          <!--        >-->
        </el-form-item>
      </el-form>
      <el-table
        ref="multipleTable"
        :data="middleQueuelist"
        tooltip-effect="dark"
        style="width: 100%"
      >
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="来访人姓名"
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
          label="办理企业名称"
          align="center"
          prop="companyName"
          show-overflow-tooltip
        />
        <el-table-column
          label="企业统一社会信用代码"
          align="center"
          prop="companyCode"
          show-overflow-tooltip
        />
        <el-table-column
          label="来访时间"
          align="center"
          prop="createDate"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          label="帮代办工作人员"
          align="center"
          prop="serviceWorkUserName"
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
              v-hasPermi="['im:visitManager:Queueview']"
            >查看</el-button
            >
          </template>
        </el-table-column>

      </el-table>
      <pagination
        v-show="queueTotal > 0"
        :total="queueTotal"
        :page.sync="queryQueueParams.pageNumber"
        :limit.sync="queryQueueParams.pageSize"
        @pagination="handleViewList()"
      />

    </el-dialog>

    <!-- 来访详细信息-->
    <el-dialog
      title="来访人员办事详情"
      :close-on-click-modal="false"
      :visible.sync="openQueueView"
      width="1350px"
      append-to-body
      height="800px"
      scrollbar
    >
      <div class="zf-zc-table--title">来访人员信息</div>
      <table
        cellspacing="0"
        cellpadding="0"
        border="0"
        class="zf-zc-table"
      >
        <colgroup>
          <col width="20%" />
          <col width="30%" />
          <col width="20%" />
          <col width="30%" />
        </colgroup>
        <tr>
          <td><b>来访人姓名：</b></td>
          <td>
            {{ middleUser.name }}
          </td>
          <td><b>证件类型：</b></td>
          <td>
            身份证
          </td>
        </tr>
        <tr>
          <td><b>证件号码：</b></td>
          <td>
            {{ middleUser.cardNo }}
          </td>
          <td><b>手机号：</b></td>
          <td>
            {{ middleUser.phone }}
          </td>
        </tr>
        <tr>
          <td><b>核酸报告：</b></td>
          <td>
            {{ middleUser.nucleicStatus  | nucleicInfoStatusHandle }}
          </td>

          <td><b>是否预约：</b></td>
          <td>
            {{ middleUser.isAppointment }}
          </td>
        </tr>
        <tr>
          <td><b>来访次数：</b></td>
          <td>
            <!--            <td colspan="3">-->
            {{ middleUser.visitNumb }}
          </td>

          <td><b>最近来访时间：</b></td>
          <td>
            {{ middleUser.visitTime }}
          </td>
        </tr>
        <tr>
          <td><b>企业名称：</b></td>
          <td>
            <!--            <td colspan="3">-->
            {{ middleUser.companyName }}
          </td>

          <td><b>统一社会信用代码：</b></td>
          <td>
            {{ middleUser.companyCode }}
          </td>
        </tr>
        <tr>
          <td><b>最近服务状态：</b></td>
          <td>
            {{ middleUser.serviceType  | distributeStatusHandle }}
          </td>
        </tr>

      </table>
      <div class="zf-zc-table--title">办理企业和事项信息</div>
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
<!--        <el-table-column-->
<!--          label="企业名称"-->
<!--          align="center"-->
<!--          prop="companyName"-->
<!--          show-overflow-tooltip-->
<!--        />-->
<!--        <el-table-column-->
<!--          label="统一社会信用代码"-->
<!--          align="center"-->
<!--          prop="companyCode"-->
<!--          show-overflow-tooltip-->
<!--        />-->
        <el-table-column
          label="办理事项"
          align="center"
          prop="serviceName"
          show-overflow-tooltip
        />
        <el-table-column
          label="服务人员"
          align="center"
          prop="workUserName"
          show-overflow-tooltip
        />
        <el-table-column
          label="帮办业务类型"
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
          label="帮办服务状态"
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
            <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleServiceView(scope.row)"
                       v-hasPermi="['im:visitManager:servicceView']">查看</el-button>
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

  </div>
</template>
<script>
import {
  pageVisit,
  pageQueueVisit
} from "@/api/ha/visitManagement/visitManager.js";
import {
  pageUser,
  initUserList,
  initUser
} from "@/api/ha/QueueManagement/workQueueManager.js";
import viewCaseInfo from "@/views/zc/businessManagement/windowAcceptance/viewCaseInfo";
export default {
  components: {viewCaseInfo},
  name: "visitManager",
  data () {
    return {
      fileElectronicFormAddrList: [],
      // 遮罩层
      loading: true,
      userStatus: { "N": "禁用", "Y": "启用" },
      distributeStatusMap: { "1": "咨询", "2": "材料准备", "3": "收件" , "4": "一键推送"},
      nucleicInfoStatusMap: { "1": "阴性", "2": "阳性", "3": "其他" },
      iconTypeMap: { "1": "个人", "2": "法人" },
      serviceStatusMap: { "":"无","1": "等待服务", "2": "服务中" ,"3":"服务完成"},
      // 总条数
      total: 0,
      queueTotal: 0,
      title: "",
      indexCaseNumber:"",
      // 数据表格
      middleUserList: [],
      middleQueuelist: [],
      middleQueueVisitType:"",
      middleQueueCompanyCode:"",
      middleQueueCardNo:"",
      middleServicelist: [],
      //userList
      userList: [],
      resourceParList: [],
      //根据文件夹文件判断是否需要上传
      attaListStatic: false,
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      openServiceView: false,
      openQueueView: false,
      // 审核
      openCheck: false,
      // 启用状态
      ableMap: { "1": "启用", "0": "禁用" },
      // 查询参数
      queryQueueParams: {
        pageNumber: 1,
        pageSize: 10,
        name: "",
        serviceWorkUserId: "",
        serviceStatus:"",
        cardNo:"",
        companyCode:""
      },
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
        name: "",
        visitType: ""
      },

      middleUser:{},
      userAuthRecList:[],
      resourceOptions: [],
      form: {
      },
      formCheck: {
        userStatus: "2",
        memo: "",
        middleUserOid: ""
      },
      attaList: "",

      //预览
      viewDialogOptions:[],
    };
  },
  watch: {
    'this.middleQueuelist':{
    }
  },
  created () {
    this.getList();
    initUserList().then(response1 => {
      // console.log(response.data.data)
      this.userList = response1.data.data;
    });
  },
  methods: {
    /** 查询业务层级列表 */
    getList() {
      this.loading = true;
      pageVisit(this.queryParams).then(response => {
        this.middleUserList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    viewCaseClose() {
      this.openServiceView = false;
    },

    /** 查看按钮操作 */
    handleView(row) {
      this.middleQueueVisitType = row.visitType;
      this.reset();
      this.middleQueueCardNo="";
      this.middleQueueCompanyCode="";
      if(row.visitType==="1"){
        this.queryQueueParams.cardNo = row.cardNo;
        this.middleQueueCardNo = row.cardNo;
      }else if(row.visitType==="2"){
        this.queryQueueParams.companyCode = row.cardNo;
        this.middleQueueCompanyCode = row.cardNo;
      }
      pageUser(this.queryQueueParams).then(response => {
        this.middleQueuelist = response.data.data;
        this.openView = true;
        this.queueTotal = response.data.total;
        this.title = "来访详细查询";
      })
    },
    handleViewList(){
      this.reset();
      if(this.middleQueueVisitType==="1"){
        this.queryQueueParams.cardNo = this.middleQueueCardNo;
      }else if(this.middleQueueVisitType==="2"){
        this.queryQueueParams.companyCode = this.middleQueueCompanyCode;
      }
      pageUser(this.queryQueueParams).then(response => {
        this.middleQueuelist = response.data.data;
        this.openView = true;
        this.queueTotal = response.data.total;
        this.title = "来访详细查询";
      })
    },
    /** 查看服务详情按钮操作 */
    handleServiceView(row) {
      // console.log("row:",row);
      this.indexCaseNumber = row.caseNumber;
      this.openServiceView = true;
      this.dialogTitle = "查看详情";
    },
    reset () {
      this.middleQueuelist= [];
      this.queryQueueParams.cardNo="";
      this.queryQueueParams.companyCode="";
    },
    /** 查看按钮操作 */
    handleQueueView(row) {

      pageQueueVisit(row.id).then(response => {
        this.middleUser = response.data.baseInfo;
        this.middleServicelist = response.data.serviceHistoryInfo;
        this.openQueueView = true;
      })

    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNumber = 1;
      this.queryParams.pageSize = 10;
      this.getList();
    },
    handleQueueQuery(){
      this.queryQueueParams.pageNumber = 1;
      this.queryQueueParams.pageSize = 10;
      this.handleViewList();
    }
  },

  filters:{
    distributeStatusHandle(data) {
      if(!data) {
        return ''
      }
      const distributeStatusMap = {
        1: '咨询',
        2: '材料准备',
        3: '收件',
        4: '一键推送'
      }
      return distributeStatusMap[data]
    },
    nucleicInfoStatusHandle(data) {
      if(!data) {
        return ''
      }
      const nucleicInfoStatusMap = {
        1: '阴性',
        2: '阳性',
        3: '其他'
      }
      return nucleicInfoStatusMap[data]
    }
  }
};
</script>
<style ></style>
