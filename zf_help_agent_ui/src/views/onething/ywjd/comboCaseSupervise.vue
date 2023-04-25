<!--author:ningzz-->
<template>
  <div class="app-container">
    <el-row :gutter="20">

      <!--首页-->
      <el-col :span="24" :xs="24">
        <!--提交表单-->
        <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="108px">
          <el-form-item label="登记人" prop="createUserOid">
            <treeselect
              v-model="queryParams.createUserOid"
              :options="userOptions"
              style="width: 240px"
              noOptionsText="暂无数据"
              :defaultExpandLevel="1"
              placeholder="请输入登记人"/>
          </el-form-item>
          <el-form-item label="登记日期">
            <el-date-picker
              v-model="queryParams.caseStartDate"
              type="date" value-format="yyyy-MM-dd"
              :picker-options="pickerOptionsStart"
              placeholder="请选择开始日期">
            </el-date-picker>-
            <el-date-picker
              v-model="queryParams.caseEndDate"
              type="date" value-format="yyyy-MM-dd"
              :picker-options="pickerOptionsEnd"
              placeholder="请选择结束日期">
            </el-date-picker>
          </el-form-item>
          <br>
          <el-form-item label="申请人" prop="name">
            <el-input v-model.trim="queryParams.applyUserName" placeholder="请输入申请人名称" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="办件编号" prop="caseNumber">
            <div class="card-item">
              <el-input
                v-model.trim="queryParams.caseNumber"
                placeholder="请输入办件编号"
                clearable
                size="small"
                @keyup.enter.native="handleQuery"
              >
                <!-- <template slot="append"
                ><el-button
                  class="scan-btn"
                  type="primary"
                  @click="openScan"
                ></el-button
                ></template> -->
              </el-input>
            </div>
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置</el-button>
          </el-form-item>
        </el-form>

        <!--列表-->
        <el-table v-loading="loading" :data="comboCaseList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>

          <el-table-column label="一件事目录名称" align="center" prop="comboDirectoryName" :show-overflow-tooltip="true"/>
          <el-table-column label="办件编号" align="center" prop="caseNumber" :show-overflow-tooltip="true"/>
          <el-table-column label="项目名称" align="center" prop="projectName" :show-overflow-tooltip="true"/>
          <el-table-column label="申请人" align="center" prop="applyUserName" :show-overflow-tooltip="true"/>
          <el-table-column label="登记日期" align="center" prop="createDate" :show-overflow-tooltip="true"/>
          <el-table-column label="办件状态" align="center" prop="caseStatus">
            <template slot-scope="scope">
              <p  v-if="scope.row.caseStatus==0">暂存</p>
              <p  v-if="scope.row.caseStatus==1">待预审</p>
              <p  v-if="scope.row.caseStatus==9">预审不通过</p>
              <p v-if="scope.row.caseStatus==2">办理中</p>
              <p  v-if="scope.row.caseStatus == 3 && scope.row.ifAccept == 1 ">办结</p>
              <p  v-if="scope.row.caseStatus == 3 && scope.row.ifAccept == 2 ">不予受理</p>
              <p  v-if="scope.row.caseStatus==-1">已作废</p>
              <p  v-if="scope.row.caseStatus == 5">异常办结</p>
　　　　　　　</template>
          </el-table-column>
          <el-table-column label="应办结时间" align="center" prop="ybjTime" :show-overflow-tooltip="true"/>
          <el-table-column label="是否超期" align="center" prop="expireFlag" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <p  v-if="scope.row.expireFlag==0">未超期</p>
              <p  v-if="scope.row.expireFlag==1">已超期</p>
　　　　　　　　</template>
          </el-table-column>

          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)" v-hasPermi="['sys:yjsSupervise:view']" >查看</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
      </el-col>
    </el-row>

    <!-- 一件事办件查看开始 -->
    <el-dialog v-dialog-drag :visible.sync="caseOpenView" v-if="caseOpenView" @close="closeOpenView" :title="title" width="1100px" height='700px' scrollbar append-to-body>

        <view-combo-case-new :caseOid="indexCaseOid" @view-case="closeOpenView"></view-combo-case-new>
        <div slot="footer" class="zf-text-center">
        <el-button @click="closeOpenView">关 闭</el-button>
      </div>

    </el-dialog>
    <!--一件事办件查看结束-->

  </div>
</template>

<script>
import {page,queryUserSimpleTree} from "@/api/onething/ywjd/comboCaseSupervise";
import Treeselect from '@riophae/vue-treeselect';
import TemporaryAcceptance from "@/views/zc/businessManagement/temporaryAcceptance/temporaryAcceptance";
import ViewComboCaseNew from "@/views/onething/comboManager/comboAccept/viewComboCaseNew";
import {activeScanningGun, openScanningGun} from "@/api/zc/businessManagement/charge";
export default {
  name: "ComboCaseSupervise",
  components: {ViewComboCaseNew, TemporaryAcceptance, Treeselect},
  data() {
    return {
      userOptions: [],
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      comboCaseList: [],
      // 弹出层标题
      title: "",
      // 办件查看
      caseOpenView: false,
      indexCaseOid: '',
      // 预审状态
      statusOptions: {'0':'未超时','1':'已超时'},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        caseStartDate:'',
        caseEndDate:''
      },
      pickerOptionsStart: {
        disabledDate: time => {
          const endDateVal = new Date(this.queryParams.caseEndDate).getTime()
          if (endDateVal) {
            return time.getTime() > endDateVal - 0
          }
        }
      },
      pickerOptionsEnd: {
        disabledDate: time => {
          const beginDateVal =new Date( (new Date(this.queryParams.caseStartDate)-24*60*60*1000)).getTime()
          if (beginDateVal) {
            return time.getTime() < beginDateVal-0
          }
        }
      },
      // 列表查看弹窗参数
      form: { info: {}, qlCases: [] },
    };
  },
  created() {
    this.getUserTree();
    this.getList();
  },
  methods: {
    /** 获取用户树 采用延迟异步加载方式*/
    getUserTree(organOid) {
      queryUserSimpleTree(organOid).then(response => {
        this.userOptions = response.data;
      });
    },
    getList() {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.comboCaseList = response.data.data;
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
      this.queryParams.applyUserName='';
      this.queryParams.caseNumber='';
      this.queryParams.caseEndDate=null;
      this.queryParams.caseStartDate=null;
      this.queryParams.createUserOid=null;
      this.handleQuery()
    },
    /** 办件查看按钮操作 */
    handleView(row) {
      this.indexCaseOid = row.caseOid;
      this.caseOpenView = true;
      this.title = "一件事办件信息";
    },
    /** 办件查看关闭按钮操作 */
    closeOpenView() {
      this.caseOpenView = false;
    },
    // 点击清空按钮时触发
    delValue() {
      console.log(11)
    },
    //打开扫码枪
    openScan() {
      openScanningGun().then(res => {
        if (res.StateCode == 0) {
          //成功后激活扫码枪
          activeScanningGun()
            .then(response => {
              if (response.StateCode == 0) {
                this.$message.success("扫描成功");
               // this.queryParams.caseNumber = response.data;
                this.$set(this.queryParams,'caseNumber', response.data || '')
              } else if (response.StateCode == -1) {
                this.$message.error("扫码枪扫描超时");
              } else if (response.StateCode == -2) {
                this.$message.error("扫码枪没有打开");
              }
            })
            .catch(err => {
              console.log(err);
            });
        } else if (res.StateCode == -1) {
          this.$message.error(res.tips);
        } else if (res.StateCode == -4) {
          //扫码枪已打开
          activeScanningGun()
            .then(response => {
              if (response.StateCode == 0) {
                this.$message.success("扫描成功");
               // this.queryParams.caseNumber = response.data;
                this.$set(this.queryParams,'caseNumber', response.data || '')
              } else if (response.StateCode == -1) {
                this.$message.error("扫码枪扫描超时");
              } else if (response.StateCode == -2) {
                this.$message.error("扫码枪没有打开");
              }
            })
            .catch(err => {
              console.log(err);
            });
        } else {
          this.$message.error("加载配置失败");
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.card-item {
  .el-form-item {
    position: relative;
  }
  .el-input {
    margin-bottom: 0px;
    border-radius: 3px;
    &:last-child {
      margin-bottom: 0;
    }
  }
}
.scan-btn {
  position: absolute;
  height: 30px;
  background: #c1d0d9 url(~@/assets/image/scan-icon.png) no-repeat center center !important;
  border: none;
  box-shadow: none;
  top: 10px;
  border-radius: 1px;
}
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

<style lang="scss" scoped>
.treeselect{
  width: 200px;
}
.treeselect240{
  width: 240px;
}
</style>
