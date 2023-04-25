<!--author:ningzz-->
<template>
  <div>

        <!--已办业务列表-->
        <el-table v-loading="loading" :data="industrySignList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="证照名称" align="center" prop="licenseName" :show-overflow-tooltip="true"/>
          <el-table-column label="办件编号" align="center" prop="caseNumber" :show-overflow-tooltip="true"/>
          <el-table-column label="证照来源" align="center" prop="caseStatus" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <p v-if="scope.row.recordType==0">签收</p>
              <p v-if="scope.row.recordType==1">打印</p>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" align="center" prop="createDate" :show-overflow-tooltip="true"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)"
                         v-hasPermi="['sys:sendAndReceive:sign']">查看
              </el-button>
            </template>
          </el-table-column>
        </el-table>


    <!-- 已办业务详细信息 -->
    <el-dialog v-dialog-drag :title="title" :visible.sync="openView" :close-on-click-modal="false" width="1100px" append-to-body>
      <div>
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="20%"/>
              <col width="30%"/>
              <col width="20%"/>
              <col width="30%"/>
            </colgroup>
            <tr>
              <td><b>送达方式：</b></td>
              <td >
                <span v-if="form.recordType=='1'">窗口打印</span>
                <span v-if="form.sendWay=='1'">快递送达</span>
                <span v-if="form.sendWay=='2'">人工送达窗口</span>
              </td>
              <td><b>证件类型：</b></td>
              <td>
                <span v-if="form.recordType=='1'">打印证照</span>
                <span v-if="form.cardType=='1'">证照</span>
                <span v-if="form.cardType=='2'">批文</span>
                <span v-if="form.cardType=='3'">其他</span>
              </td>
            </tr>
            <tr v-if="form.recordType=='1'">
              <td><b>打印时间：</b></td>
              <td colspan="3">
                {{form.createDate}}
              </td>
            </tr>
            <tr v-if="form.sendWay == 1 && form.recordType=='0'">
              <td><b>快递目标：</b></td>
              <td colspan="3" v-if="form.sendWay == 1">
                窗口
              </td>
              <td colspan="3" v-if="form.sendWay == 2">
                申请人
              </td>
            </tr>
            <tr v-if="form.sendWay == 1 && form.recordType=='0'">
              <td><b>快递公司：</b></td>
              <td>{{form.deliverCompany}}</td>
              <td><b>快递单号：</b></td>
              <td>{{form.deliverNumber}}</td>
            </tr>
            <tr v-if="form.sendWay == 1 && form.recordType=='0'">
              <td><b>送件人姓名：</b></td>
              <td>{{form.sendPerson}}</td>
              <td><b>送件人手机号：</b></td>
              <td>{{form.sendPhone}}</td>
            </tr>
            <tr v-if="form.sendWay == 1 && form.recordType=='0'">
              <td><b>送件部门：</b></td>
              <td>{{form.organName}}</td>
              <td><b>送件时间：</b></td>
              <td>{{form.sendTime}}</td>
            </tr>
          </table>
      </div>
      <div slot="footer" class="zf-text-center">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {queryIndustrySignByCaseOid,queryIndustrySignByOid} from "@/api/onelicence/fzgl/licenseSignIndustry";
export default {
  name: "sendAndReceive",
  components: {},
  props:["industryCaseOid"],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      industrySignList: [],
      // 弹出层标题
      title: "",
      // 列表查看
      openView: false,
      form:{},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询办结业务列表 */
    getList() {
      this.loading = true;
      queryIndustrySignByCaseOid(this.industryCaseOid).then(response => {
        this.industrySignList = response.data;
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
      this.queryParams.applyUserName = '';
      this.queryParams.caseNumber = '';
      this.queryParams.caseEndDate = null;
      this.queryParams.caseStartDate = null;
      this.handleQuery()
    },
    /** 列表查看按钮操作 */
    handleView(row) {
      let _that = this;
      queryIndustrySignByOid(row.oid).then(response => {
        _that.form = response.data
      });
      _that.openView = true
      _that.title = '一业一证签收信息'
    }
  },
};
</script>
