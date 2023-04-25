/** * @Author: dxl * @Date: 2020-11-4 * @Description: 已办业务 */
<template>
  <div class="app-container">
    <!--区划数据-->
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="70px">
      <!--<el-form-item label="所属区划" prop="districtOid">
          <treeselect  class="treeselect" :defaultExpandLevel="1" noOptionsText = "暂无数据" noResultsText="暂无数据" :show-count="true" v-model="queryParams.districtOid" :options="districtOptions" placeholder="请输入所属区划" />
        </el-form-item>-->
      <el-form-item label="办件编号" prop="caseNumber">
        <div class="card-item">
          <el-input v-model.trim="queryParams.caseNumber" placeholder="请输入办件编号" clearable size="100"
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
      <el-form-item label="申请人" prop="applyUserName">
        <el-input v-model.trim="queryParams.applyUserName" placeholder="请输入申请人" clearable size="100"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="登记日期">
        <el-date-picker v-model.trim="queryParams.startDate" type="date" value-format="yyyy-MM-dd"
          :picker-options="pickerOptionsStart" placeholder="请选择开始日期">
        </el-date-picker>
        -
        <el-date-picker v-model.trim="queryParams.endDate" type="date" value-format="yyyy-MM-dd"
          :picker-options="pickerOptionsEnd" placeholder="请选择结束日期">
        </el-date-picker>
      </el-form-item>

      <el-form-item class="fr mr0">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="caseRegList" border :fit="true" :height="calcHeight">
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="申请项目名称" align="center" prop="projectName" :show-overflow-tooltip="true" />
      <el-table-column label="办件编号" align="center" prop="caseNumber" :show-overflow-tooltip="true" />
      <el-table-column label="事项类型" align="center" prop="serviceTypeName" :show-overflow-tooltip="true" />
      <el-table-column label="申请人" align="center" prop="applay.applyUserName" :show-overflow-tooltip="true" />
      <el-table-column label="证件号" align="center" prop="applay.credentialNumber" :show-overflow-tooltip="true" />
      <el-table-column label="登记日期" align="center" prop="createDate" :show-overflow-tooltip="true" />
      <el-table-column label="办件状态" align="center" prop="caseStatus" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <p v-if="scope.row.caseStatus == 0">暂存</p>
          <p v-if="scope.row.caseStatus == 1">待预审</p>
          <p v-if="scope.row.caseStatus == 9">预审不通过</p>
          <p v-if="scope.row.caseStatus == 2">办理中</p>
          <p v-if="scope.row.caseStatus == 3">办结</p>
          <p v-if="scope.row.caseStatus == 5 || scope.row.caseStatus == -1">
            异常办结
          </p>
          <p v-if="scope.row.caseStatus == 4">已作废</p>
          <p v-if="scope.row.caseStatus == 7">不予受理</p>
          <p v-if="scope.row.caseStatus == 10">收件</p>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)"
            v-hasPermi="['sys:done:view']">查看</el-button>
          <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" @click="handlePrint(scope.row)"
            v-hasPermi="['sys:done:print']">补打收件回执</el-button>
          <el-button size="mini" v-if="clzsFlag" type="text" icon="iconfont zfsoft-chakan" @click="handlePreviewIntelligent(scope.row)">
            查看智审</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination :total="queryParams.total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <el-dialog v-dialog-drag :visible.sync="openView" v-if="openView" title="查看办件信息" width="1100px" height="700px"
      scrollbar append-to-body>
      <view-case-info :caseNumber="indexCaseNumber" @view-case="viewCaseClose"></view-case-info>
      <div slot="footer" style="text-align: center">
        <el-button @click="openView = false"> 关闭 </el-button>
      </div>
    </el-dialog>

    <!-- 打印回执单 -->
    <el-dialog :close-on-click-modal="false" :title="dialogTitle" v-if="addDialogShow" :visible.sync="addDialogShow"
      width="1100px" height="700px" scrollbar append-to-body v-dialog-drag>
      <div id="print">
        <div class="zf-zc-table--title">业务单（存档）</div>

        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><b>事项名称：</b></td>
            <td colspan="3">
              {{ printCaseInfo.qlCase.serviceName }}
            </td>
          </tr>
          <tr>
            <td><b>业务部门：</b></td>
            <td colspan="3">
              {{ printCaseInfo.qlCase.organOid }}
            </td>
          </tr>
          <tr>
            <td><b>办件编号：</b></td>
            <td>
              {{ printCaseInfo.qlCase.caseNumber }}
            </td>
            <td><b>受理时间：</b></td>
            <td>
              {{ printCaseInfo.qlCase.acceptanceDate }}
            </td>
          </tr>
          <tr>
            <td>
              <b>
                {{
                  printCaseInfo.applyUserType == '0'
                    ? '申请人名称：'
                    : '申请单位名称：'
                }}
              </b>
            </td>
            <td>{{ printCaseInfo.applyUserName }}</td>
            <td>
              <b>
                {{
                  printCaseInfo.applyUserType == '0'
                    ? '申请人手机：'
                    : '申请单位手机：'
                }}
              </b>
            </td>
            <td>
              {{ printCaseInfo.applyUserPhone }}
            </td>
          </tr>
          <tr>
            <td><b>领取方式：</b></td>
            <td colspan="3">
              <span v-if="printCaseInfo.deliveryWay == 1">快递送达</span>
              <span v-if="printCaseInfo.deliveryWay == 2">自行取件</span>
              <span v-if="printCaseInfo.deliveryWay == 3">其他</span>
            </td>
          </tr>
          <tr>
            <td><b>受理具体地点：</b></td>
            <td colspan="3">{{ printCaseInfo.specificLocation }}</td>
          </tr>
          <tr>
            <td><b>材料名称：</b></td>
            <td colspan="3">{{ printCaseInfo.materialNames }}</td>
          </tr>
          <tr>
            <td style="text-align: center">
              <vue-qr :text="printCaseInfo.caseNumber" :size="93"></vue-qr>
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
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><b>事项名称：</b></td>
            <td colspan="3">{{ printCaseInfo.qlCase.serviceName }}</td>
          </tr>
          <tr>
            <td><b>办件编号：</b></td>
            <td>{{ printCaseInfo.qlCase.caseNumber }}</td>
            <td><b>申请时间：</b></td>
            <td>{{ printCaseInfo.qlCase.createDate }}</td>
          </tr>

          <tr>
            <td v-if="printCaseInfo.applyUserType == '0'">
              <b>申请人名称：</b>
            </td>
            <td v-else><b>申请单位名称：</b></td>
            <td>{{ printCaseInfo.applyUserName }}</td>
            <td v-if="printCaseInfo.applyUserType == '0'">
              <b>申请人手机：</b>
            </td>
            <td v-else><b>申请单位手机：</b></td>
            <td>{{ printCaseInfo.applyUserPhone }}</td>
          </tr>
          <tr>
            <td><b>受理人：</b></td>
            <td>{{ printCaseInfo.slPerson }}</td>
            <td><b>受理人电话：</b></td>
            <td>{{ printCaseInfo.slInternet }}</td>
          </tr>
          <tr>
            <td><b>材料清单：</b></td>
            <td colspan="3">{{ printCaseInfo.materialNames }}</td>
          </tr>
        </table>

        <div class="zf-zc-table--title">材料证照流转签字表</div>

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
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" v-print="printObj">打印</el-button>
        <el-button @click="addDialogShow = false">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog v-dialog-drag :close-on-click-modal="false" title="智审列表" v-if="intelligentVisible"
      :visible.sync="intelligentVisible" width="1000px" height="700px" scrollbar append-to-body
      custom-class="done-buiness-dialog">
      <IntelligentAudit :caseOid="caseOid" :serviceOid="serviceOid" />
      <div slot="footer" class="zf-text-center">
        <el-button @click="intelligentVisible = false"> 关闭 </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    page,
    getOneByCaseOidForYbyw,
    querySignImgPath
  } from "@/api/zc/businessManagement/doneBusiness.js";
  import {
    queryDistrictSimpleTree
  } from "@/api/sys/district";
  import viewCaseInfo from "@/views/zc/businessManagement/windowAcceptance/viewCaseInfo";
  import {
    getOne
  } from "@/api/sys/organ";
  import vueQr from "vue-qr";

  // import the component
  import Treeselect from "@riophae/vue-treeselect";
  // import the styles
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import {
    sxServiceOidsListByUserOid
  } from "@/api/zc/businessManagement/sxServiceRegistrar";
  import Resolution from "@/mixins/resolution.js";
  import IntelligentAudit from "./intelligentAudit.vue";
  import {
    activeScanningGun,
    openScanningGun
  } from '@/api/zc/businessManagement/charge'
  import { mapGetters } from "vuex";
  export default {
    name: "DoneBusiness",
    mixins: [Resolution],
    components: {
      Treeselect,
      viewCaseInfo,
      vueQr,
      IntelligentAudit
    },
    data() {
      return {
        printObj: {
          id: "print",
          popTitle: "业务单",
          extraHead: '<meta http-equiv="Content-Language"content="zh-cn"/>'
        },
        // 列表数据
        caseRegList: [],
        // 弹窗标题
        dialogTitle: "",
        addDialogShow: false,
        detailDialogShow: false,
        loading: false,
        indexCaseNumber: "",
        openView: false,

        // 机构
        listOrganOptions: [],
        // 区划树
        districtOptions: [],
        printCaseInfo: {},
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          caseNumber: "",
          applyUserName: "",
          startDate: null,
          endDate: new Date(),
          sourceApp: 1,
          serviceOids: "",
          total: 0,
          credentialNumber: ""
        },
        pickerOptionsStart: {
          disabledDate: time => {
            const endDateVal = new Date(this.queryParams.endDate).getTime();
            if (endDateVal) {
              return time.getTime() > endDateVal - 0;
            }
          }
        },
        pickerOptionsEnd: {
          disabledDate: time => {
            const beginDateVal = new Date().getTime();
            if (beginDateVal) {
              return time.getTime() > beginDateVal - 0;
            }
          }
        },
        caseOid: "",
        serviceOid: "",
        intelligentVisible: false,
        signImage: ""
      };
    },
    computed: {
       ...mapGetters([
         "clzsFlag"
      ]),

      calcHeight() {
        return (this.resolutionHeight === 1080 &&
            this.resolutionWidth === 1280) ||
          (this.resolutionHeight === 1024 && this.resolutionWidth === 1280) ?
          "calc(100% - 160px)" :
          "calc(100% - 112px)";
      }
    },
    methods: {
      initStartTime() {
        let time = new Date(new Date().getTime() - 14 * 24 * 60 * 60 * 1000);
        this.queryParams.startDate = time;
      },
      districtSel(node, instanceId) {
        this.form.districtName = node.label;
      },
      organSel(node, instanceId) {
        this.form.organName = node.label;
      },
      viewCaseClose() {
        this.openView = false;
      },
      /** 获取机构数据 */
      getListOrganTree(districtOid, callback) {
        if (districtOid) {
          queryOrganTree(districtOid).then(response => {
            this.listOrganOptions = response.data;
            callback && callback();
          });
        } else {
          this.listOrganOptions = [];
          this.queryParams.organOid = null;
        }
      },
      /** 获取区划树 */
      getDistrictTree(districtOid) {
        let _that = this;
        queryDistrictSimpleTree(districtOid).then(response => {
          _that.districtOptions = response.data;
        });
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 查询列表 */
      getList() {
        this.loading = true;
        page(this.queryParams).then(response => {
          if (response.data) {
            this.caseRegList = response.data.data;
            this.queryParams.total = response.data.total;
          }
          this.loading = false;
        });
      },
      /** 重置按钮操作 */
      resetQuery() {
        let _that = this;
        _that.resetForm("queryForm");
        _that.queryParams.applyUserName = '';
        _that.queryParams.caseNumber = '';
        _that.handleQuery();
      },
      // 表单重置
      reset() {
        let _that = this;
        _that.resetForm("form");
      },
      /** 查看按钮操作 */
      handleView(row) {
        this.indexCaseNumber = row.caseNumber;
        this.openView = true;
        this.dialogTitle = "查看详情";
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
      //查询所有授权事项
      getRegSxServiceOids() {
        sxServiceOidsListByUserOid().then(respon => {
          if (respon.code === 200) {
            if (respon.data) {
              this.queryParams.serviceOids = respon.data.join(",");
            }
            this.getList();
          }
        });
      },

      handlePreviewIntelligent(row) {
        this.caseOid = row.caseOid;
        this.serviceOid = row.serviceOid;
        this.intelligentVisible = true;
      },

      changeCaseCrediten() {
        this.queryParams.caseNumber = this.$route.query.caseNumber;
        this.queryParams.credentialNumber = this.$route.query.credentialNumber;
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
                  this.queryParams.caseNumber = response.data;
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
                  this.queryParams.caseNumber = response.data;
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
    watch: {
      "form.districtOid": function (val, oldVal) {
        if (!val) {
          this.form.districtName = null;
        }
        this.form.organOid = null;
        this.form.organName = null;
        //机构加载
        this.getListOrganTree(val);
      },
      "form.organOid": function (val, oldVal) {
        if (!val) {
          this.form.organName = null;
        }
      }
    },
    created() {
      this.initStartTime();
      this.getRegSxServiceOids();
      //this.getList();
      this.getDistrictTree();
    },
    activated() {
      this.changeCaseCrediten();
      if (
        this.queryParams.caseNumber != "" ||
        this.queryParams.credentialNumber != ""
      ) {
        this.getRegSxServiceOids();
      }
    }
  };

</script>
<style lang="scss" scoped>
  .treeselect {
    width: 200px;
  }

  .treeselect240 {
    width: 240px;
  }

  >>>.done-buiness-dialog {
    height: 700px;
  }

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
