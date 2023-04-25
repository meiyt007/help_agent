<!--author:ningzz-->
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="24" :xs="24">
        <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="70px">
          <el-form-item label="办件编号" prop="caseNumber">
            <div class="card-item">
              <el-input v-model.trim="queryParams.caseNumber" placeholder="请输入办件编号" clearable size="small"
                @keyup.enter.native="handleQuery">
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
          <el-form-item label="申请人" prop="name">
            <el-input v-model.trim="queryParams.applyUserName" placeholder="请输入申请人名称" clearable size="small"
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="登记日期">
            <!-- <el-date-picker
              v-model="queryParams.caseStartDate"
              type="date" value-format="yyyy-MM-dd"
              placeholder="请选择开始时间">
            </el-date-picker>
            -
            <el-date-picker
              v-model="queryParams.caseEndDate"
              type="date" value-format="yyyy-MM-dd"
              placeholder="请选择结束时间">
            </el-date-picker>-->
            <el-date-picker v-model="queryParams.caseStartDate" type="date" value-format="yyyy-MM-dd"
              :picker-options="pickerOptionsStart" placeholder="请选择开始日期">
            </el-date-picker>
            -
            <el-date-picker v-model="queryParams.caseEndDate" type="date" value-format="yyyy-MM-dd"
              :picker-options="pickerOptionsEnd" placeholder="请选择结束日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置
            </el-button>
          </el-form-item>
        </el-form>

        <!--已办业务列表-->
        <el-table v-loading="loading" :data="comboCaseList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>

          <el-table-column label="一件事目录名称" align="center" prop="comboDirectoryName" :show-overflow-tooltip="true" />
          <el-table-column label="办件编号" align="center" prop="caseNumber" :show-overflow-tooltip="true" />
          <el-table-column label="项目名称" align="center" prop="projectName" :show-overflow-tooltip="true" />
          <el-table-column label="申请人" align="center" prop="applyUserName" :show-overflow-tooltip="true" />
          <el-table-column label="证件号" align="center" prop="credentialNumber" :show-overflow-tooltip="true" />
          <el-table-column label="登记日期" align="center" prop="createDate" :show-overflow-tooltip="true" />
          <el-table-column label="办件状态" align="center" prop="caseStatus" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <p v-if="scope.row.caseStatus == 1">待预审</p>
              <p v-if="scope.row.caseStatus == 9">预审不通过</p>
              <p v-if="scope.row.caseStatus == 2">办理中</p>
              <p v-if="scope.row.caseStatus == 3 && scope.row.ifAccept == 1">
                办结
              </p>
              <p v-if="scope.row.caseStatus == 3 && scope.row.ifAccept == 2">
                不予受理
              </p>
              <p v-if="scope.row.caseStatus == -1">已作废</p>
              <p v-if="scope.row.caseStatus == 5">异常办结</p>
              　　　　　　　　
            </template>
          </el-table-column>

          <el-table-column label="操作" width="400" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)"
                v-hasPermi="['sys:comboDone:view']">
                查看
              </el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" @click="handlePrint(scope.row)"
                v-hasPermi="['sys:comboDone:view']">
                补打收件回执
              </el-button>
              <el-button v-if="clzsFlag" size="mini" type="text" icon="iconfont zfsoft-chakan"
                @click="handlePreviewIntelligent(scope.row)">
                查看智审
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize" @pagination="getList" />
      </el-col>
    </el-row>

    <!-- 关联办件详细信息 -->
    <el-dialog v-dialog-drag :visible.sync="caseOpenView" v-if="caseOpenView" @close="closeOpenView" :title="title"
      width="1100px" scrollbar height="700px" append-to-body>
      <view-combo-case-new :caseOid="indexCaseOid"></view-combo-case-new>
      <div style="text-align: center" slot="footer">
        <el-button type="primary" @click="closeOpenView">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 打印回执单 -->
    <el-dialog v-dialog-drag title="打印回执单" v-if="addDialogShow" :visible.sync="addDialogShow" width="1100px" scrollbar
      height="700px" append-to-body center>
      <div id="print">
        <div class="zf-zc-table--title">业务单（存档）</div>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="15%" />
            <col width="35%" />
            <col width="15%" />
            <col width="35%" />
          </colgroup>
          <tr>
            <td><b>目录名称：</b></td>
            <td colspan="3">
              {{ printCaseInfo.comboDirectory.comboDirectoryName }}
            </td>
          </tr>
          <tr>
            <td><b>所属区划：</b></td>
            <td colspan="3">{{ printCaseInfo.comboDirectory.districtName }}</td>
          </tr>
          <tr>
            <td><b>办件编号：</b></td>
            <td>{{ printCaseInfo.comboCase.caseNumber }}</td>
            <td><b>受理时间：</b></td>
            <td>{{ printCaseInfo.comboCase.acceptanceDate }}</td>
          </tr>
          <tr>
            <td v-if="printCaseInfo.applyUserType == '0'">
              <b>申请人名称：</b>
            </td>
            <td v-else><b>申请单位名称：</b></td>
            <td>{{ printCaseInfo.comboCase.applyUserName }}</td>
            <td v-if="printCaseInfo.applyUserType == '0'">
              <b>申请人手机：</b>
            </td>
            <td v-else><b>申请单位手机：</b></td>
            <td>{{ printCaseInfo.comboCase.applyUserPhone }}</td>
          </tr>
          <tr>
            <td><b>领取方式：</b></td>
            <td colspan="3">
              <span v-if="printCaseInfo.comboCase.resultDeliveryWay == 1">
                快递送达
              </span>
              <span v-if="printCaseInfo.comboCase.resultDeliveryWay == 2">
                自行取件
              </span>
              <span v-if="printCaseInfo.comboCase.resultDeliveryWay == 3">
                其他
              </span>
            </td>
          </tr>
          <tr>
            <td><b>受理具体地点：</b></td>
            <td colspan="3">{{ printCaseInfo.comboCase.specificLocation }}</td>
          </tr>
          <tr>
            <td><b>材料名称：</b></td>
            <td colspan="3">{{ printCaseInfo.materialNames }}</td>
          </tr>
          <tr>
            <td style="text-align: center">
              <vue-qr :text="printCaseInfo.comboCase.caseNumber" :size="93" />
              <div>办件编号</div>
            </td>
            <td colspan="3">
              签名：
              <div v-if="signImage != '' && signImage != null">
                <img style="width: 160px; height: 110px" :src="signImage" />
              </div>
            </td>
          </tr>
        </table>
        <div class="zf-zc-table--title">材料证照流转单</div>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="15%" />
            <col width="35%" />
            <col width="15%" />
            <col width="35%" />
          </colgroup>
          <tr>
            <td><b>目录名称：</b></td>
            <td colspan="3">
              {{ printCaseInfo.comboDirectory.comboDirectoryName }}
            </td>
          </tr>
          <tr>
            <td><b>办件编号：</b></td>
            <td>{{ printCaseInfo.comboCase.caseNumber }}</td>
            <td><b>申请时间：</b></td>
            <td>{{ printCaseInfo.comboCase.createDate }}</td>
          </tr>

          <tr>
            <td v-if="printCaseInfo.applyUserType == '0'">
              <b>申请人名称：</b>
            </td>
            <td v-else><b>申请单位名称：</b></td>
            <td>{{ printCaseInfo.comboCase.applyUserName }}</td>
            <td v-if="printCaseInfo.applyUserType == '0'">
              <b>申请人手机：</b>
            </td>
            <td v-else><b>申请单位手机：</b></td>
            <td>{{ printCaseInfo.comboCase.applyUserPhone }}</td>
          </tr>
          <tr>
            <td><b>材料清单：</b></td>
            <td colspan="3">{{ printCaseInfo.materialNames }}</td>
          </tr>
        </table>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="15%" />
            <col width="20%" />
            <col width="15%" />
            <col width="30%" />
            <col width="20%" />
          </colgroup>
          <tr>
            <td colspan="5" style="text-align: center">
              <b>材料证照流转签字表</b>
            </td>
          </tr>
          <tr>
            <td style="text-align: center; background-color: #fff">
              <b>类型</b>
            </td>
            <td style="text-align: center"><b>环节</b></td>
            <td style="text-align: center; background-color: #fff">
              <b>签字单位/人</b>
            </td>
            <td style="text-align: center"><b>签字</b></td>
            <td style="text-align: center; background-color: #fff">
              <b>时间</b>
            </td>
          </tr>

          <tr>
            <td rowspan="2" style="text-align: center; background-color: #fff">
              <b>材料</b>
            </td>
            <td style="text-align: center; background-color: #fff">
              <b>窗口出库</b>
            </td>
            <td style="text-align: center; background-color: #fff">
              <b>收件（受理）人</b>
            </td>
            <td style="text-align: center; background-color: #fff">
              <b></b>
            </td>
            <td style="text-align: center; background-color: #fff">
              <b></b>
            </td>
          </tr>
          <tr>
            <td style="text-align: center; background-color: #fff">
              <b>部门签收</b>
            </td>
            <td style="text-align: center; background-color: #fff">
              <b>业务部门</b>
            </td>
            <td style="text-align: center; background-color: #fff">
              <b></b>
            </td>
            <td style="text-align: center; background-color: #fff">
              <b></b>
            </td>
          </tr>
          <tr>
            <td rowspan="2" style="text-align: center; background-color: #fff">
              <b>证照</b>
            </td>
            <td style="text-align: center; background-color: #fff">
              <b>部门签收</b>
            </td>
            <td style="text-align: center; background-color: #fff">
              <b>业务部门</b>
            </td>
            <td style="text-align: center; background-color: #fff">
              <b></b>
            </td>
            <td style="text-align: center; background-color: #fff">
              <b></b>
            </td>
          </tr>
          <tr>
            <td style="text-align: center; background-color: #fff">
              <b>窗口签收</b>
            </td>
            <td style="text-align: center; background-color: #fff">
              <b>出件人</b>
            </td>
            <td style="text-align: center; background-color: #fff">
              <b></b>
            </td>
            <td style="text-align: center; background-color: #fff">
              <b></b>
            </td>
          </tr>
        </table>
      </div>
      <div slot="footer">
        <el-button type="primary" v-print="printObj">打印</el-button>
        <el-button @click="addDialogShow = false">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog v-dialog-drag :close-on-click-modal="false" title="智审列表" v-if="intelligentVisible"
      :visible.sync="intelligentVisible" width="1000px" height="700px" scrollbar append-to-body
      custom-class="done-buiness-dialog">
      <IntelligentAudit :caseOid="caseOid" :comboDirectoryOid="comboDirectoryOid" />
      <div slot="footer" class="zf-text-center">
        <el-button @click="intelligentVisible = false"> 关闭 </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    page,
    get,
    getOneByCaseOidForYbyw,
    querySignImgPath
  } from "@/api/onething/comboManager/comboCaseDone/comboCaseDone";
  import ViewComboCaseNew from "@/views/onething/comboManager/comboAccept/viewComboCaseNew";
  import vueQr from 'vue-qr';
  import IntelligentAudit from "./intelligentAudit.vue";
  import { mapGetters } from "vuex";
  import {
    activeScanningGun,
    openScanningGun
  } from "@/api/zc/businessManagement/charge";
  export default {
    name: "ComboCasedone",
    components: {
      ViewComboCaseNew,
      vueQr,
      IntelligentAudit
    },
    data() {
      return {
        printObj: {
          id: 'print',
          popTitle: '业务单',
          extraHead: '<meta http-equiv="Content-Language"content="zh-cn"/>'
        },
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
        indexCaseOid: "",
        addDialogShow: false,
        printCaseInfo: {},
        // 查询参数默认值
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          caseStartDate: '',
          caseEndDate: '',
          caseNumber: '',
          applyUserName: '',
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
            const beginDateVal = new Date((new Date(this.queryParams.caseStartDate) - 24 * 60 * 60 * 1000)).getTime()
            if (beginDateVal) {
              return time.getTime() < beginDateVal - 0
            }
          }
        },

        caseOid: "",
        comboDirectoryOid: "",
        intelligentVisible: false,
        signImage: ""
      };
    },
    created() {
      this.initQuery();
      this.getList();
    },
    computed:{
      ...mapGetters([
         "clzsFlag"
      ]),
    },
    activated() {
      this.changeCaseCrediten();
      if (
        this.queryParams.caseNumber != "" ||
        this.queryParams.credentialNumber != ""
      ) {
        this.getList();
      }
    },
    methods: {
      changeCaseCrediten() {
        this.queryParams.caseNumber = this.$route.query.caseNumber;
        this.queryParams.credentialNumber = this.$route.query.credentialNumber;
      },
      initQuery() {
        if (undefined != this.$route.query.caseNumber) {
          this.queryParams.caseNumber = this.$route.query.caseNumber;
        }
        if (undefined != this.$route.query.credentialNumber) {
          this.queryParams.credentialNumber = this.$route.query.credentialNumber;
        }
      },
      handlePreviewIntelligent(row) {
        this.caseOid = row.caseOid;
        this.comboDirectoryOid = row.comboDireOid;
        this.intelligentVisible = true;
      },

      /** 查询已办业务列表 */
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
        // this.resetForm("queryForm");
        this.queryParams.applyUserName = '';
        this.queryParams.caseNumber = '';
        this.queryParams.caseEndDate = null;
        this.queryParams.caseStartDate = null;
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
      handlePrint(row) {
        this.addDialogShow = true;
        this.signImage = "";
        const oid = row.caseOid;
        getOneByCaseOidForYbyw(oid).then(response => {
          this.printCaseInfo = response.data;
        });
        querySignImgPath(oid).then(res => {
          if (res.code = 200) {
            if (res.data != "" && res.data != null) {
              this.signImage = res.data;
              this.$forceUpdate(); //强制刷新页面
            }
          }
        })
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
                  this.$set(this.queryParams, 'caseNumber', response.data || '')
                  //this.queryParams.caseNumber = response.data;
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
                  this.$set(this.queryParams, 'caseNumber', response.data || '')
                  //this.queryParams.caseNumber = response.data;
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

  @media print {
    tr td {
      font-size: 12px !important;
    }
  }

</style>
