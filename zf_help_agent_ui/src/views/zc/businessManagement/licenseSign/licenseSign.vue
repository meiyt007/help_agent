/** * @Author: dxl * @Date: 2020-11-24 * @Description: 证照签发 */
<template>
  <div class="app-container">
    <!--列表数据-->
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="70px">
      <el-form-item label="办件编号" prop="caseNumber">
        <div class="card-item">
          <el-input v-model.trim="queryParams.caseNumber" placeholder="请输入办件编号" clearable size="100"
            @keyup.enter.native="handleQuery">
            <!-- <template slot="append"><el-button
              class="scan-btn"
              type="primary"
              @click="openScan"
            ></el-button></template> -->
          </el-input>
        </div>
      </el-form-item>
      <el-form-item label="证件号" prop="idCard">
        <div class="card-item">
          <el-input v-model.trim="queryParams.idCard" placeholder="请输入证件号" clearable size="100"
            @keyup.enter.native="handleQuery">
            <!-- <template slot="append"
            ><el-button
              class="scan-btn"
              type="primary"
              @click="scanCard"
            ></el-button
            ></template> -->
          </el-input>
        </div>
      </el-form-item>
      <el-form-item label="签收状态" label-width="108px">
        <el-radio-group v-model.trim="queryParams.licenseInStorage">
          <el-radio v-for="(status, key) in statusOptions" :key="key" :label="key">{{ status }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item class="fr mr0">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置
        </el-button>
      </el-form-item>
    </el-form>
    <!-- 列表信息-->
    <el-table :data="caseRegList" v-loading="loading" border :fit="true" height="calc(100% - 120px)">
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="办件编号" align="center" prop="caseNumber" :show-overflow-tooltip="true" />
      <el-table-column label="事项类型" align="center" prop="serviceType" :show-overflow-tooltip="true" />
      <el-table-column label="申请人" align="center" prop="applyUserName" :show-overflow-tooltip="true" />
      <el-table-column label="证件号" align="center" prop="idCard" :show-overflow-tooltip="true" />
      <el-table-column label="手机号" align="center" prop="applyPhone" :show-overflow-tooltip="true" />
      <el-table-column label="登记日期" align="center" prop="caseRegisterDate" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)"
            v-hasPermi="['sys:licenseSign:view']">查看</el-button>
          <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" v-if="scope.row.licenseInStorage != 1"
            @click="handleInit(scope.row)" v-hasPermi="['sys:licenseSign:initlicenseSign']">签收</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination :total="queryParams.total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <el-dialog v-dialog-drag :close-on-click-modal="false" :before-close="cancel" v-if="openInit" :title="dialogTitle" :visible.sync="openInit" append-to-body
      width="900px">
      <el-form ref="form" :rules="rules" :model="form" label-width="0px">
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><i class="require">*</i><b>送达方式：</b></td>
            <td>
              <el-radio-group v-model="form.sendWay" @change="handleRowChange">
                <el-radio :label="1">快递送达</el-radio>
                <el-radio :label="2">人工送达窗口</el-radio>
              </el-radio-group>
            </td>
            <td><i class="require">*</i><b>证件类型：</b></td>
            <td>
              <el-radio-group v-model="form.carType" @change="handleRowChange">
                <el-radio :label="1">证照</el-radio>
                <el-radio :label="2">批文</el-radio>
                <el-radio :label="3">其他</el-radio>
              </el-radio-group>
            </td>
          </tr>
          <tr v-if="form.sendWay == 1">
            <td><i class="require">*</i><b>快递目标：</b></td>
            <td colspan="3">
              <el-radio-group v-model="form.deliverTarget" @change="handleRowChange">
                <el-radio :label="1">窗口</el-radio>
                <el-radio :label="2">申请人</el-radio>
              </el-radio-group>
            </td>
          </tr>
          <tr v-if="form.sendWay == 1">
            <td><i class="require">*</i><b>快递公司：</b></td>
            <td>
              <el-form-item prop="deliverCompany">
                <el-col :span="24">
                  <el-input v-model.trim="form.deliverCompany" placeholder="请输入快递公司" maxlength="50" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>快递单号：</b></td>
            <td>
              <el-form-item prop="deliverNumber">
                <el-col :span="24">
                  <el-input v-model.trim="form.deliverNumber" placeholder="请输入快递单号" maxlength="50" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>送件人姓名：</b></td>
            <td>
              <el-form-item prop="sendPerson">
                <el-col :span="24">
                  <el-input v-model.trim="form.sendPerson" placeholder="请输入送件人姓名" maxlength="20" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>送件人手机号：</b></td>
            <td>
              <el-form-item prop="sendPhone">
                <el-col :span="24">
                  <el-input v-model.trim="form.sendPhone" placeholder="请输入送件人手机号" maxlength="11" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>送件部门：</b></td>
            <td>
              <el-form-item prop="organName">
                <el-col :span="24">
                  <el-input v-model.trim="form.organName" placeholder="请输入送件部门" maxlength="20" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>送件时间：</b></td>
            <td>
              <el-form-item prop="sendTime">
                <el-date-picker v-model="form.sendTime" type="date" :picker-options="pickerOptions" placeholder="送件时间"></el-date-picker>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><b>是否需要短信通知：</b></td>
            <td colspan="3">
              <el-radio-group v-model="form.isCms" @change="handleRowChange">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 查看办件信息-->
    <el-dialog v-dialog-drag :visible.sync="openView" v-if="openView" title="查看办件信息" width="1100px" height="700px"
      scrollbar append-to-body>
      <view-case-info :caseNumber="indexCaseNumber"></view-case-info>
      <div slot="footer" class="zf-text-center">
        <el-button @click="openView = false">
          关闭
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    page,
    saveOrUpdateSign
  } from "@/api/zc/businessManagement/licenseSign.js";
  import viewCaseInfo from "@/views/zc/businessManagement/windowAcceptance/viewCaseInfo";
  import {
    validatePhone
  } from "@/utils/validate";
  import {
    sxServiceOidsListByUserOid
  } from "@/api/zc/businessManagement/sxServiceRegistrar";
  import {
    activeScanningGun,
    openScanningGun
  } from '@/api/zc/businessManagement/charge'
  import {
    findIdCard,
    getIdCardInfo,
    openIdCard
  } from '@/api/sys/hardwareScan'
  export default {
    name: "LicenseSign",
    components: {
      viewCaseInfo
    },
    data() {
      return {

        //时间控制
        pickerOptions:{
          disabledDate:(current)=>{
            return current.getTime()-new Date().getTime()<= -24*3600*1000;
          }
        },
        // 列表数据
        caseRegList: [],
        //证件类型
        certificateTypeList: [],
        rowNum: 1,
        // 弹窗标题
        dialogTitle: "",
        openView: false,
        // 新增/修改显示弹出层
        openInit: false,
        // 表单参数
        form: {
          sendPerson: "",
          sendWay: 1,
          isCms: 1,
          deliverTarget: 1,
          carType: 1
        },
        rules: {
          cardType: [{
            required: true,
            message: "证件类型不能为空",
            trigger: "blur"
          }],
          sendPerson: [{
            required: true,
            message: "送件人不能为空",
            trigger: "blur"
          }],
          sendPhone: [{
              required: true,
              message: "送件人手机号不能为空",
              trigger: "blur"
            },
            {
              validator: validatePhone,
              message: "请输入正确的送件人手机号",
              trigger: "blur"
            }
          ],
          organName: [{
            required: true,
            message: "送件部门不能为空",
            trigger: "blur"
          }],
          sendTime: [{
            required: true,
            message: "送件时间不能为空",
            trigger: "blur"
          }],
          deliverCompany: [{
            required: true,
            message: "快递公司不能为空",
            trigger: "blur"
          }],
          deliverNumber: [{
            required: true,
            message: "快递单号不能为空",
            trigger: "blur"
          }]
        },
        applyInfo: {},
        devWay: "",
        showSevTr: false,

        indexCaseNumber: "",
        // 告知和补正状态
        statusOptions: {
          "0": "待签收",
          "1": "已签收"
        },
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          caseNumber: "",
          licenseInStorage: "0",
          serviceOids: "",
          regOid: "",
          idCard: "",
          total: 0
        },
        loading: false,
      };
    },
    methods: {
      handleRowChange(data) {},
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      // 取消按钮
      cancel() {
        this.openInit = false;
        this.form = {
          sendPerson:""
        };
        this.reset();
      },
      /** 查询列表 */
      getList() {
        this.loading = true;
        page(this.queryParams).then(response => {
          if (response.data) {
            this.caseRegList = response.data.data;
            this.queryParams.total = response.data.total;
          } else {
            this.caseRegList = [];
            this.queryParams.total = 0;
          }
          this.loading = false;
        });
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 表单重置
      reset() {
        Object.assign(this.form, this.$options.data().form);
        this.resetForm("form");
      },
      /** 查看按钮操作 */
      handleView(row) {
        this.indexCaseNumber = row.caseNumber;
        this.openView = true;
        this.dialogTitle = "查看详情";
      },
      /** 送达方式 */
      changeDeliveryWay(val) {},
      viewCaseClose() {
        this.openView = false;
      },
      /** 出库操作 */
      handleInit(row) {
        if (row.regOid) {
          //add
          //this.form = this.deForm;
          this.form.regOid = row.regOid;
          this.showSevTr = true;
          // this.applyInfo.devWay=row.deliverWay;
          // this.form.deliverWay=row.deliverWay;
          // if(row.deliverWay==1){//快递送达
          //   //查询办件送达信息
          //   getOneApplyPerson(this.form.regOid).then(response => {
          //     this.applyInfo = response.data;
          //     this.applyInfo.devWay=row.deliverWay;
          //   })
          // }else{
          //   this.showSevTr=false;
          // }
        }
        this.openInit = true;
        this.dialogTitle = "办件签收";
      },
      /** 提交按钮 */
      submitForm: function () {
        let _that = this;
        _that.$refs["form"].validate(valid => {
          if (valid) {
            saveOrUpdateSign(_that.form).then(response => {
              if (response.code === 200) {
                _that.msgSuccess("签收成功");
                _that.openInit = false;
                //_that.form = {};
                _that.form = {
                  sendPerson: "",
                  sendWay: 1,
                  isCms: 1,
                  deliverTarget: 1,
                  carType: 1
                };
                setTimeout(() => {
                  _that.getList();
                }, 10);
              }
            });
          }
        });
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
      scanCard() {
        openIdCard().then(response => {
          //打开设备
          if (response.state == "sucess") {
            this.getIdcardData(); //重新获取身份证信息
          } else {
            //查找设备
            findIdCard().then(resFind => {
              if ((resFind.state = "sucess" && resFind.StateCode == 0)) {
                this.getIdcardData();
              } else {
                this.$message.error("无法找到设备");
                return false;
              }
            });
          }
        });
      },
      getIdcardData() {
        getIdCardInfo().then(response => {
          let res = response;
          if (!res) {
            this.$message.error("请检查设备或连接是否正常!");
            return false;
          }
          if (res.state == "sucess") {
            this.queryParams.idCard = res.carID;
          } else if (res.StateCode == -1) {
            //0:成功-1:设备没有打开-2:寻卡失败-3选卡失败-4读卡失败
            //判断有没有设备
            //打开身份证
            openIdCard().then(response => {
              if ((response.state = "sucess")) {
                this.getIdcardData(data); //重新获取身份证信息
              }
            });
          } else {
            this.$message.error(res.tips);
            return false;
          }
        });
      },
    },
    watch: {},
    created() {
      this.getRegSxServiceOids();
    }
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

</style>
