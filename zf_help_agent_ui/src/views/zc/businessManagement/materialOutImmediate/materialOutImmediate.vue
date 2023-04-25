/**
* @Author: dxl
* @Date: 2020-11-5
* @Description: 材料出库
*/
<template>
  <div class="app-container">
    <!--区划数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="70px"
    >
      <!--<el-form-item label="所属区划" prop="districtOid">
        <treeselect  class="treeselect" :defaultExpandLevel="1" noOptionsText = "暂无数据" noResultsText="暂无数据" :show-count="true" v-model.trim="queryParams.districtOid" :options="districtOptions" placeholder="请输入所属区划" />
      </el-form-item>-->
      <el-form-item label="申请人" prop="applyUserName">
        <el-input
          v-model.trim="queryParams.applyUserName"
          placeholder="请输入申请人"
          clearable
          size="100"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="扫码出库" prop="caseNumber">
        <div class="card-item">
          <el-input
            v-model.trim="queryParams.caseNumber"
            placeholder="请扫描办件编号二维码"
            clearable
            size="100"
            @keyup.enter.native="handleQuery">
<!--            <template slot="append"><el-button
              class="scan-btn"
              type="primary"
              @click="openScan"
            ></el-button></template>-->
          </el-input>
        </div>
      </el-form-item>
      <el-form-item label="登记日期" label-width="80px">
        <el-date-picker
          v-model.trim="queryParams.startDate"
          type="date"
          value-format="yyyy-MM-dd"
          :picker-options="pickerOptionsStart"
          placeholder="请选择开始日期"
        >
        </el-date-picker>
        -
        <el-date-picker
          v-model.trim="queryParams.endDate"
          type="date"
          value-format="yyyy-MM-dd"
          :picker-options="pickerOptionsEnd"
          placeholder="请选择结束日期"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="出库状态" label-width="108px">
        <el-radio-group v-model="queryParams.outStatus">
          <el-radio
            v-for="(status, key) in statusOptions"
            :key="key"
            :label="key"
            >{{ status }}</el-radio
          >
        </el-radio-group>
      </el-form-item>
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
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleBatch"
          v-hasPermi="['sys:materialOut:init']"
          >批量扫码出库</el-button
        >
      </el-col>
    </el-row>
    <!-- 列表信息-->
    <el-table
      :data="caseRegList"
      v-loading="loading"
      border
      :fit="true"
      :height="calcHeight"
    >
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="办件编号"
        align="center"
        prop="caseNumber"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="申请人"
        align="center"
        prop="applyUserName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="事项类型"
        align="center"
        prop="serviceTypeName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="证件号"
        align="center"
        prop="cardNumber"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="登记日期"
        align="center"
        prop="caseCreateDate"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="当前状态" align="center">
        <template slot-scope="scope">
          <p v-if="scope.row.outStatus == 1">已出库</p>
          <p v-if="scope.row.outStatus == 0">待出库</p>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
        width="260"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['sys:materialOut:view']"
            >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            v-if="scope.row.outStatus == 0"
            @click="handleInit(scope.row)"
            v-hasPermi="['sys:materialOut:out']"
            >出库</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-yanzhengma"
            v-if="scope.row.outStatus == 0"
            @click="printMaterialMenu(scope.row.id)"
            v-hasPermi="['sys:materialOut:out']"
            >打印出库流转单</el-button
          >
          <!-- 该处打印调用pageoffice-->
          <!--<el-button size="mini" type="text" icon="iconfont zfsoft-chakan"  @click="handlePrint(scope.row)" v-hasPermi="['sys:materialOut:view']" >打印</el-button>-->
          <!-- <el-button size="mini" type="text" icon="iconfont zfsoft-chakan"  @click="printMaterialMenu(scope.row)" v-hasPermi="['sys:materialOut:view']" >打印</el-button>-->
        </template>
      </el-table-column>
    </el-table>
    <pagination
      :total="queryParams.total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 材料出库信息详细 -->
    <el-dialog
    v-dialog-drag
      :title="dialogTitle"
      v-if="addDialogShow"
      :visible.sync="addDialogShow"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <el-table v-loading="loading" :data="caseMaterialList">
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="材料名称"
          width="350"
          align="center"
          prop="materialName"
        />
        <el-table-column label="是否已收取" width="150" align="center">
          <template slot-scope="scope">
            <p v-if="scope.row.collectionFlag == 1">已收取</p>
            <p v-if="scope.row.collectionFlag == 0">未收取</p>
          </template>
        </el-table-column>
        <el-table-column
          label="收取方式"
          align="center"

          prop="collectionType"
        >
          <template slot-scope="scope">
            <p v-if="scope.row.collectionType == 1">纸质收取</p>
            <p v-if="scope.row.collectionType == 2">附件上传</p>
            <p v-if="scope.row.collectionType == 3">扫描</p>
            <p v-if="scope.row.collectionType == 4">容缺后补</p>
          </template>
        </el-table-column>

        <el-table-column
          label="收取数量"

          align="center"
          prop="collectionNumber"
        />
      </el-table>
      <div class="zf-zc-table--title">出库信息</div>
      <el-form label-width="0px" :model="form" :rules="rules" ref="form">
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="15%" />
            <col width="35%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><i class="require">*</i><b>出库方式：</b></td>
            <td colspan="3">
              <el-radio-group v-model="outType" @change="handleRowChange">
                <el-radio :label="1">递送员出库</el-radio>
                <el-radio :label="2">快递送达</el-radio>
              </el-radio-group>
            </td>
          </tr>
          <tr v-if="outType == 1">
            <td><i class="require">*</i><b>领取人姓名：</b></td>
            <td>
              <el-form-item prop="receiverName">
                <el-input
                  v-model.trim="form.receiverName"
                  name="receiverName"
                  maxlength="25"
                  show-word-limit
                  placeholder="请输入姓名"
                ></el-input>
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>领取人手机号：</b></td>
            <td>
              <el-form-item prop="receiverPhone">
                <el-input
                  v-model.trim="form.receiverPhone"
                  name="receiverPhone"
                  maxlength="11"
                  show-word-limit
                  placeholder="请输入手机号"
                ></el-input>
              </el-form-item>
            </td>
          </tr>
          <tr v-if="outType == 2">
            <td><i class="require">*</i><b>快递公司：</b></td>
            <td>
              <el-form-item prop="kdCompany">
                <el-input
                  v-model.trim="form.kdCompany"
                  name="kdCompany"
                  maxlength="30"
                  show-word-limit
                  placeholder="请输入快递公司"
                ></el-input>
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>快递编号：</b></td>
            <td>
              <el-form-item prop="kdCode">
                <el-input
                  v-model.trim="form.kdCode"
                  name="kdCode"
                  maxlength="100"
                  show-word-limit
                  placeholder="请输入快递编号"
                ></el-input>
              </el-form-item>
            </td>
          </tr>
          <tr v-if="outType == 2">
            <td><i class="require">*</i><b>寄件人名称：</b></td>
            <td>
              <el-form-item prop="senderUserName">
                <el-input
                  v-model.trim="form.senderUserName"
                  name="senderUserName"
                  maxlength="50"
                  show-word-limit
                  placeholder="请输入寄件人名称"
                ></el-input>
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>寄件人手机：</b></td>
            <td>
              <el-form-item prop="senderUserPhone">
                <el-input
                  v-model.trim="form.senderUserPhone"
                  name="senderUserPhone"
                  maxlength="11"
                  show-word-limit
                  placeholder="请输入寄件人手机"
                ></el-input>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>身份证号码：</b></td>
            <td colspan="3">
              <el-form-item prop="idCard">
                <el-input
                  v-model.trim="form.idCard"
                  name="idCard"
                  maxlength="18"
                  show-word-limit
                  placeholder="请输入身份证号码"
                ></el-input>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitForm">出库</el-button>
        <el-button
          @click="
            () => {
              addDialogShow = false
              reset()
            }
          "
          >关闭</el-button
        >
      </div>
    </el-dialog>

    <!-- 批量材料出库信息详细 -->
    <el-dialog
    v-dialog-drag
      :close-on-click-modal="false"
      :title="dialogTitle"
      v-if="batchView"
      :visible.sync="batchView"
      width="900px"
      height="600px"
      scrollbar
      append-to-body
    >
      <el-row>
        <el-input
          v-model="batch_caseNumber"
          placeholder="请扫描二维码"
          align="center"
          clearable
          size="100"
          @keyup.enter.native="handleBatchQuery"
        />
      </el-row>
      <!-- <h3><i class="el-icon-document"></i></h3> -->
      <div class="zf-zc-table--title">批量出库列表</div>
      <el-table
        v-loading="loading"
        :data="tableData"
        tooltip-effect="dark"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="办件编号"
          width="200"
          align="center"
          prop="caseNumber_pl"
        />
        <el-table-column
          label="申请人名称"

          align="center"
          prop="applyUserName_pl"
        />
        <el-table-column
          label="事项类型"
          align="center"

          prop="serviceTypeName_pl"
        />
        <el-table-column
          label="登记日期"

          align="center"
          prop="caseCreateDate_pl"
        />
      </el-table>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitBatchForm">确定</el-button>
        <el-button
          @click="
            () => {
              batchView = false
              reset()
            }
          "
          >关闭</el-button
        >
      </div>
    </el-dialog>

    <el-dialog
      v-dialog-drag
      :visible.sync="openView"
      v-if="openView"
      title="查看办件信息"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <view-case-info
        :caseNumber="indexCaseNumber"
        @view-case="viewCaseClose"
      ></view-case-info>
      <div slot="footer" class="zf-text-center">
        <el-button @click="openView = false">
          关闭
        </el-button>
      </div>
    </el-dialog>

    <!--利用vue插件进行打印-->
    <el-dialog
    v-dialog-drag
      :close-on-click-modal="false"
      :title="dialogTitle"
      v-if="printShow"
      :visible.sync="printShow"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <div id="print">
        <div
          :model="printCaseInfo"
          v-for="(info, index) in printCaseInfo"
          :key="index"
        >
          <!-- <h3 align="center"><i class="el-icon-document"></i></h3> -->
          <div class="zf-zc-table--title">材料流转通知单</div>

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
                <td style="text-align: center"><b>申请人(单位)名称：</b></td>
                <td colspan="2">{{ info.PO_applyUserName }}</td>
                <td style="text-align: center">
                  <vue-qr :text="info.PO_regNumber" :size="93"></vue-qr>
                  <div>办件编号</div>
                </td>
              </tr>
              <tr>
                <td style="text-align: center"><b>办件编号：</b></td>
                <td colspan="3">{{ info.PO_regNumber }}</td>
              </tr>

              <tr>
                <td style="text-align: center"><b>登记日期：</b></td>
                <td>{{ info.PO_createDate }}</td>
                <td style="text-align: center"><b>通知单编号：</b></td>
                <td>{{ info.PO_noticeNum }}</td>
              </tr>
              <tr v-if="info.PO_outType == '1'">
                <td style="text-align: center"><b>领取人姓名：</b></td>
                <td>
                  {{ info.PO_receiverName }}
                </td>
                <td style="text-align: center"><b>领取人手机号：</b></td>
                <td>
                  {{ info.PO_receiverPhone }}
                </td>
              </tr>
              <tr v-if="info.PO_outType == '2'">
                <td style="text-align: center"><b>快递公司：</b></td>
                <td>
                  {{ info.PO_kdCompany }}
                </td>
                <td style="text-align: center"><b>快递编号：</b></td>
                <td>
                  {{ info.PO_kdCode }}
                </td>
              </tr>
              <tr v-if="info.PO_outType == '2'">
                <td style="text-align: center"><b>寄件人名称：</b></td>
                <td>
                  {{ info.PO_senderUserName }}
                </td>
                <td style="text-align: center"><b>寄件人手机：</b></td>
                <td>
                  {{ info.PO_senderUserPhone }}
                </td>
              </tr>
              <tr>
                <td style="text-align: center"><b>办件材料：</b></td>
                <td colspan="3">{{ info.PO_materialsInfo }}</td>
              </tr>
            </table>

          <!-- <h3 align="center"><i class="el-icon-document"></i></h3> -->
          <div class="zf-zc-table--title">材料流转记录</div>

            <table
              cellspacing="0"
              cellpadding="0"
              border="0"
              class="zf-zc-table"
            >
              <colgroup>
                <col width="30%" />
                <col width="30%" />
                <col width="40%" />
              </colgroup>
              <tr>
                <th style="text-align: center"><b>流转类型</b></th>
                <th style="text-align: center">
                  <b>时间</b>
                </th>
                <th style="text-align: center"><b>签字</b></th>
              </tr>
              <tr>
                <td style="text-align: center; background-color: #fff">
                  {{ info.PO_circulationType }}
                </td>
                <td style="text-align: center; background-color: #fff">
                  {{ info.PO_circulationDate }}
                </td>
                <td style="text-align: center; background-color: #fff">
                  {{ info.PO_signature }}
                </td>
              </tr>
            </table>


            <table
              cellspacing="0"
              cellpadding="0"
              border="0"
              class="zf-zc-table"
            >
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
                <td
                  rowspan="2"
                  style="text-align: center; background-color: #fff"
                >
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
            </table>

        </div>
      </div>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" v-print="printObjLz">打印</el-button>
        <el-button @click="closePrint">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { page, saveOrUpdate, getOneMaterialInfo, getOneByCaseNumber, batchOutMaterial, printOutMaterial } from "@/api/zc/businessManagement/materialOut.js";
import { queryDistrictSimpleTree } from "@/api/sys/district";
import { validatePhone, validIDCard } from "@/utils/validate";
import Treeselect from '@riophae/vue-treeselect';
import '@riophae/vue-treeselect/dist/vue-treeselect.css';
import viewCaseInfo from "@/views/zc/businessManagement/windowAcceptance/viewCaseInfo";
import vueQr from 'vue-qr';
import Resolution from '@/mixins/resolution.js';
import { activeScanningGun, openScanningGun } from '@/api/zc/businessManagement/charge'
export default {
  name: "MaterialOut",
  mixins: [Resolution],
  components: { viewCaseInfo, Treeselect, vueQr },
  data () {
    return {
      printObjLz: {
        id: 'print',
        popTitle: '材料流转通知单',
        extraHead: '<meta http-equiv="Content-Language"content="zh-cn"/>'
      },
      printObj: {
        id: 'print',
        popTitle: '材料出库',
        extraHead: '<meta http-equiv="Content-Language"content="zh-cn"/>'
      },
      // 列表数据
      caseRegList: [],
      tableData: [],
      rowNum: 1,
      // 弹窗标题
      dialogTitle: '',
      addDialogShow: false,
      openView: false,
      batchView: false,
      printShow: false,

      batch_caseNumber: "",
      allSmCaseNum: [],//用于存放所扫描的办件编号
      // 机构
      listOrganOptions: [],
      // 区划树
      districtOptions: [],
      //材料信息
      caseMaterialList: [],
      materialOidsArr: [],
      multipleSelection: [],
      printCaseInfo: [],
      indexCaseNumber: "",
      outType: 1,
      form: { id: "", materialOids: "" },
      rules: {
        receiverName: [
          { required: true, message: "领取人姓名不能为空", trigger: "blur" }
        ],
        receiverPhone: [
          { required: true, message: "领取人手机号不能为空", trigger: "blur" },
          { validator: validatePhone, message: '请输入正确的领取人手机号', trigger: 'blur' }
        ],
        kdCompany: [
          { required: true, message: "快递公司不能为空", trigger: "blur" }
        ],
        kdCode: [
          { required: true, message: "快递编号不能为空", trigger: "blur" }
        ],
        senderUserName: [
          { required: true, message: "寄件人名称不能为空", trigger: "blur" }
        ],
        senderUserPhone: [
          { required: true, message: "寄件人手机不能为空", trigger: "blur" },
          { validator: validatePhone, message: '请输入寄件人手机号', trigger: 'blur' }
        ],
        idCard: [
          { required: true, message: "身份证号不能为空", trigger: "blur" },
          { validator: validIDCard, trigger: 'blur' }
        ]
      },
      // 出库状态
      statusOptions: { '0': '待出库', '1': '已出库' },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        caseNumber: "",
        outStatus: '0',
        startDate: null,
        endDate: new Date(),
        total: 0,
      },
      loading: false,
      pickerOptionsStart: {
        disabledDate: time => {
          const endDateVal = new Date(this.queryParams.endDate).getTime()
          if (endDateVal) {
            return time.getTime() > endDateVal - 0
          }
        }
      },
      pickerOptionsEnd: {
        disabledDate: time => {
          const beginDateVal = new Date().getTime()
          if (beginDateVal) {
            return time.getTime() > beginDateVal - 0
          }
        }
      }
    };
  },
  computed: {
    calcHeight () {
      return (this.resolutionHeight === 1080 && this.resolutionWidth === 1280)
        || (this.resolutionHeight === 1024 && this.resolutionWidth === 1280)
        ? 'calc(100% - 210px)' : 'calc(100% - 170px)';
    }
  },
  methods: {
    initStartTime () {
      let time = new Date(new Date().getTime() - 14 * 24 * 60 * 60 * 1000);
      this.queryParams.startDate = time;
    },
    //复选框的多选
    handleSelectionChange (val) {
      this.multipleSelection = val;
      console.log(JSON.stringify(this.multipleSelection))
    },
    viewCaseClose () {
      this.openView = false;
    },
    districtSel (node, instanceId) {
      this.form.districtName = node.label;
    },
    organSel (node, instanceId) {
      this.form.organName = node.label;
    },
    /** 获取机构数据 */
    getListOrganTree (districtOid, callback) {
      if (districtOid) {
        queryOrganTree(districtOid).then(response => {
          this.listOrganOptions = response.data;
          callback && callback();
        });
      } else {
        this.listOrganOptions = []
        this.queryParams.organOid = null
      }
    },
    handleRowChange (data) {
    },
    /** 获取区划树 */
    getDistrictTree (districtOid) {
      let _that = this;
      queryDistrictSimpleTree(districtOid).then(response => {
        _that.districtOptions = response.data;
      });
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 查询列表 */
    getList () {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.caseRegList = response.data.data;
        this.queryParams.total = response.data.total;
        this.loading = false;
      });
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.queryParams.outStatus = '0';//重置radio无效
      this.handleQuery();
    },
    // 表单重置
    reset () {
      Object.assign(this.form, this.$options.data().form)
      this.resetForm("form");
    },
    /** 查看按钮操作 */
    handleView (row) {
      this.indexCaseNumber = row.caseNumber;
      this.openView = true;
      this.dialogTitle = "查看详情";
    },
    /** 出库操作 */
    handleInit (row) {
      this.form = {};
      if (row.regOid) {
        this.form.id = row.id;
        getOneMaterialInfo(row.id).then(response => {
          this.addDialogShow = true;
          this.caseMaterialList = response.data;
          /*for(let material of this.caseMaterialList){
            this.materialOidsArr.push(material.caseMaterialOid)
          }*/
        });
      } else {
        this.addDialogShow = true;
      }
      this.dialogTitle = "材料出库";
    },
    /** 提交按钮 */
    submitForm: function () {
      let _that = this;
      //_that.form.materialOids=_that.materialOidsArr.join(';');
      this.form.outType = this.outType;
      this.$refs["form"].validate((valid) => {
        if (valid) {
          saveOrUpdate(_that.form).then(response => {
            if (response.code === 200) {
              _that.msgSuccess("保存成功");
              _that.addDialogShow = false;
              setTimeout(() => {
                //询问是否打印材料出库单
                /*this.$confirm('是否打印材料流转单?', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
                }).then(() => {
                  this.$message(this.printMaterialMenu(this.form.id));
                }).catch(() => {
                  _that.getList()
                });*/
                this.$message(this.printMaterialMenu(this.form.id));
                _that.getList()

              }, 10);
            }
          });
        }
      })

    },
    /** 批量出库操作 */
    handleBatch () {
      this.batchView = true;
      this.dialogTitle = "批量受理出库";
      this.batch_caseNumber = '';
      this.tableData = [];
      this.allSmCaseNum = [];
    },
    //批量扫描enter查询
    handleBatchQuery () {
      if (this.batch_caseNumber) {
        if (this.allSmCaseNum) {
          if (this.allSmCaseNum.indexOf(this.batch_caseNumber) != -1) {
            this.$message.error("办件已存在!");
            return false;
          }
        }
        getOneByCaseNumber(this.batch_caseNumber).then(response => {
          if (response.data && response.data.length > 0) {
            response.data.forEach((item, i) => {
              this.addRow(item);
              this.allSmCaseNum.push(this.batch_caseNumber);
            })
          } else {
            this.$message.error("未查询到相关数据!");
          }
          this.batch_caseNumber = '';
        });
      } else {
        this.$message.error("办件编号不能为空!");
      }

    },
    // 增加行
    addRow (caseInfo) {
      var list = {
        rowNum: this.rowNum,
        caseNumber_pl: caseInfo.caseNumber,
        applyUserName_pl: caseInfo.applyUserName,
        serviceTypeName_pl: caseInfo.serviceTypeName,
        caseCreateDate_pl: caseInfo.caseCreateDate,
        id: caseInfo.id
      };
      this.tableData.unshift(list)
      this.rowNum += 1;
    },
    submitBatchForm: function () {
      let _that = this;
      var id = [];
      if (_that.multipleSelection.length <= 0) {
        _that.$message.error("请选择出库数据!")
        return false;
      }
      for (let ss = 0; ss < _that.multipleSelection.length; ss++) {
        id.push(_that.multipleSelection[ss].id);
      }
      var allId = id.join(';');
      batchOutMaterial(allId).then(response => {
        if (response.code === 200) {
          _that.msgSuccess("保存成功");
          _that.batchView = false;
          setTimeout(() => {
            //询问是否打印材料出库单
            /*this.$confirm('是否打印材料流转单?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              this.$message(_that.printMaterialMenu(allId));
            }).catch(() => {
              _that.getList()
            });*/
            this.$message(_that.printMaterialMenu(allId));
            _that.getList()
          }, 10);
        }
      });
    },
    printMaterialMenu (id) {
      this.printShow = true;
      //根据传过来的值获取流转单信息
      printOutMaterial(id).then(response => {
        if (response.code === 200) {
          this.printCaseInfo = response.data;
        }
      });

    },
    //调用pageoffice的打印
    handlePrint (row) {
      const oid = row.regOid;
      const id = row.id;
      window.open(process.env.VUE_APP_BASE_API_PAGE + '/manage/zhuozheng/initTickertape?caseOid=' + oid + '&id=' + id, 'width=1200px;height=800px;');
    },
    closePrint () {
      this.printShow = false;
      this.getList();
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
    'form.districtOid': function (val, oldVal) {
      if (!val) {
        this.form.districtName = null;
      }
      this.form.organOid = null;
      this.form.organName = null;
      //机构加载
      this.getListOrganTree(val)
    },
    'form.organOid': function (val, oldVal) {
      if (!val) {
        this.form.organName = null;
      }
    },
  },
  created () {
    this.initStartTime();
    this.getList();
    this.getDistrictTree();
  },
};
</script>
<style lang="scss" scoped>
.treeselect {
  width: 200px;
}
.treeselect240 {
  width: 240px;
}
// 利用page去掉打印下面的链接
@page {
  margin-bottom: 1mm;
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
  background: #c1d0d9 url(~@/assets/image/scan-icon.png) no-repeat center
  center !important;
  border: none;
  box-shadow: none;
  top: 10px;
  border-radius: 1px;
}
</style>
