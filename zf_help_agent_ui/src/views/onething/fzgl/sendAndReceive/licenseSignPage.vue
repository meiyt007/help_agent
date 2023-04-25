<!--author:ningzz-->
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="24" :xs="24">
        <!--已办业务列表-->
        <el-table v-loading="loading" :data="comboCaseSignList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="证照名称" align="center" prop="licenseName" :show-overflow-tooltip="true"/>
          <el-table-column label="办件编号" align="center" prop="comboCaseNumber" :show-overflow-tooltip="true"/>
          <el-table-column label="创建时间" align="center" prop="createDate" :show-overflow-tooltip="true"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)"
                         v-hasPermi="['sys:sendAndReceive:sign']">查看
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>

    <!-- 已办业务详细信息 -->
    <el-dialog :title="title" :visible.sync="openView" :close-on-click-modal="false" width="800px" height="400px" v-dialog-drag append-to-body>
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
                <span v-if="form.sendWay=='1'">快递送达</span>
                <span v-if="form.sendWay=='2'">人工送达窗口</span>
              </td>
              <td><b>证件类型：</b></td>
              <td>
                <span v-if="form.carType=='1'">证照</span>
                <span v-if="form.carType=='2'">批文</span>
                <span v-if="form.carType=='3'">其他</span>
              </td>
            </tr>
            <tr v-if="form.sendWay == 1 ">
              <td><b>快递目标：</b></td>
              <td colspan="3" v-if="form.sendWay == 1">
                窗口
              </td>
              <td colspan="3" v-if="form.sendWay == 2">
                申请人
              </td>
            </tr>
            <tr v-if="form.sendWay == 1">
              <td><b>快递公司：</b></td>
              <td>{{form.deliverCompany}}</td>
              <td><b>快递单号：</b></td>
              <td>{{form.deliverNumber}}</td>
            </tr>
            <tr>
              <td><b>送件人姓名：</b></td>
              <td>{{form.sendPerson}}</td>
              <td><b>送件人手机号：</b></td>
              <td>{{form.sendPhone}}</td>
            </tr>
            <tr>
              <td><b>送件部门：</b></td>
              <td>{{form.organName}}</td>
              <td><b>送件时间：</b></td>
              <td>{{form.sendTime}}</td>
            </tr>
          </table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {queryComboCaseSignByCaseOid,queryComboCaseSignByOid} from "@/api/onething/fzgl/licenseSignCombo";
export default {
  name: "sendAndReceive",
  components: {},
  props:["comboCaseOid"],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      comboCaseSignList: [],
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
      queryComboCaseSignByCaseOid(this.comboCaseOid).then(response => {
        this.comboCaseSignList = response.data;
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
      queryComboCaseSignByOid(row.oid).then(response => {
        _that.form = response.data
      });
      _that.openView = true
      //_that.title = '一业一证签收信息'
      _that.title = '签收信息'
    }
  },
};
</script>
