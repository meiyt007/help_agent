<!--author:ningzz-->
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="24" :xs="24">
        <!--已办业务列表-->
        <el-table v-loading="loading" :data="comboCaseIssuedList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="领证人名称" align="center" prop="receiveName" :show-overflow-tooltip="true"/>
          <el-table-column label="领证人手机" align="center" prop="receivePhone" :show-overflow-tooltip="true"/>
          <el-table-column label="领证人身份证" align="center" prop="receiveCardCode" :show-overflow-tooltip="true"/>
          <el-table-column label="领证时间" align="center" prop="createDate" :show-overflow-tooltip="true"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)"
                         v-hasPermi="['sys:sendAndReceive:issued']">查看
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
              <td><b>领证人姓名：</b></td>
              <td>{{form.receiveName}}</td>
              <td><b>领证人证件号码：</b></td>
              <td>
                {{form.receiveCardCode}}
              </td>
            </tr>
            <tr>
              <td><b>联系电话：</b></td>
              <td >
                {{form.receivePhone}}
              </td>
              <td><b>采集时间：</b></td>
              <td >
                {{form.createDate}}
              </td>
            </tr>
            <tr>
              <td><b>办件编号：</b></td>
              <td >
                {{form.comboCase.caseNumber}}
              </td>
              <td><b>影像采集确认信息：</b></td>
              <td >
                {{form.address}}
              </td>
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
import {getComboCaseIssuedBySignOid, getLicenseIssuedComboByCaseOid} from "@/api/onething/fzgl/licenseIssuedCombo";
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
      comboCaseIssuedList: [],
      // 弹出层标题
      title: "",
      // 列表查看
      openView: false,
      // 表单参数
      form: {comboCase:{}},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询办结业务列表 */
    getList() {
      this.loading = true;
      getLicenseIssuedComboByCaseOid(this.comboCaseOid).then(response => {
        this.comboCaseIssuedList = response.data;
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
      getComboCaseIssuedBySignOid(row.signOid).then(response => {
        _that.form = response.data
      });
      _that.openView = true
      //_that.title = '一件事证领证信息'
      _that.title = '领证信息'
    }
  },
};
</script>
