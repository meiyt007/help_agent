/** * @Author: dxl * @Date: 2020-11-24 * @Description: 证照签发 */
<template>
  <div class="app-container">
    <!--区划数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="70px"
    >
      <el-form-item label="办件编号" prop="caseNumber">
        <div class="card-item">
          <el-input
            v-model.trim="queryParams.caseNumber"
            placeholder="请输入办件编号"
            clearable
            size="100"
            @keyup.enter.native="handleQuery"
          >
            <!-- <template slot="append"
              ><el-button
                class="scan-btn"
                type="primary"
                @click="openScan(0)"
              ></el-button
            ></template> -->
          </el-input>
        </div>
      </el-form-item>
      <el-form-item label="证件号" prop="idCard">
        <div class="card-item">
          <el-input
            v-model.trim="queryParams.idCard"
            placeholder="请输入证件号"
            clearable
            size="100"
            @keyup.enter.native="handleQuery"
          >
            <!-- <template slot="append"
              ><el-button
                class="scan-btn"
                type="primary"
                @click="scanCard(1)"
              ></el-button
            ></template> -->
          </el-input>
        </div>
      </el-form-item>
      <el-form-item label="发证状态" label-width="108px">
        <el-radio-group v-model="queryParams.licenseInStorage">
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
    <el-row :gutter="10" class="mb8" style="width: 200px">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleBatch"
          v-hasPermi="['sys:licenseIssued:init']"
          >扫描领证</el-button
        >
      </el-col>
    </el-row>
    <!-- 列表信息-->
    <el-table
      :data="caseRegList"
      v-loading="loading"
      border
      :fit="true"
      height="calc(100% - 160px)"
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
        label="事项类型"
        align="center"
        prop="serviceType"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="申请人"
        align="center"
        prop="applyUserName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="证件号"
        align="center"
        prop="idCard"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="登记日期"
        align="center"
        prop="caseRegisterDate"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="送达方式" align="center">
        <template slot-scope="scope">
          <p v-if="scope.row.deliverWay == 1">快递送达</p>
          <p v-if="scope.row.deliverWay == 2">自行取件</p>
          <p v-if="scope.row.deliverWay == 3">其他</p>
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
            @click="handleView(scope.row)"
            v-hasPermi="['sys:licenseIssued:view']"
            >证照预览</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            v-if="
              scope.row.licenseInStorage == 1 &&
                scope.row.licenseOutStorage != 1
            "
            @click="handleInit(scope.row)"
            v-hasPermi="['sys:licenseIssued:out']"
            >发证</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <pagination
      :total="queryParams.total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 签发信息详细 -->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :title="dialogTitle"
      v-if="addDialogShow"
      :visible.sync="addDialogShow"
      width="900px"
      append-to-body
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="0px"
        class="demo-ruleForm"
      >
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><b>申请人：</b></td>
            <td>
              {{ form.caseIssuedList.applyUserName }}
            </td>
            <td><b>证件号码：</b></td>
            <td>
              {{ form.caseIssuedList.idCard }}
            </td>
          </tr>
          <tr>
            <td><b>申请人手机：</b></td>
            <td>
              {{ form.caseIssuedList.applyPhone }}
            </td>
            <td><b>申请人住址：</b></td>
            <td>
              {{ form.caseIssuedList.applyAddress }}
            </td>
          </tr>
          <tr>
            <td><b>送达方式：</b></td>
            <td colspan="3">
              <span v-if="form.caseIssuedList.deliverWay == 1">快递送达</span>
              <span v-if="form.caseIssuedList.deliverWay == 2">自行取件</span>
              <span v-if="form.caseIssuedList.deliverWay == 3">其他</span>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>取件人证件类型：</b></td>
            <td colspan="3">
              <el-form-item prop="pickerCardType">
                <el-col :span="24">
                  <el-select
                    v-model="form.pickerCardType"
                    placeholder="请选择证件类型"
                  >
                    <el-option
                      v-for="certificateType in certificateTypeList"
                      :key="certificateType.dictOid"
                      :label="certificateType.name"
                      :value="certificateType.dictOid"
                    >
                    </el-option>
                  </el-select>
                  <span v-if="varify == 1" style="color: green"
                    >人证比对通过</span
                  >
                  <span v-if="varify == 2" style="color: red"
                    >人证比对不通过</span
                  >
                  <el-button
                    type="primary"
                    @click="scanCard(0)"
                    style="float: right"
                    >✚ 扫描身份证</el-button
                  >
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>取件人证件号码：</b></td>
            <td colspan="3">
              <el-form-item prop="receiveCardCode">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.receiveCardCode"
                    placeholder="请输入取件人证件号码"
                    maxlength="50"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>取件人姓名：</b></td>
            <td>
              <el-form-item prop="receiveName">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.receiveName"
                    placeholder="请输入取件人姓名"
                    maxlength="50"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>取件人手机号：</b></td>
            <td>
              <el-form-item prop="receivePhone">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.receivePhone"
                    placeholder="请输入取件人手机号"
                    maxlength="11"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="getImageCamera">人证比对</el-button>
        <el-button type="primary" @click="viewReceiveInfo">
          预览领证回执
        </el-button>
        <el-button type="primary" @click="outOfStock">发证</el-button>
        <el-button
          @click="
            () => {
              addDialogShow = false;
              reset();
            }
          "
          >关闭</el-button
        >
      </div>
    </el-dialog>

    <!-- 签发信息页面 -->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :title="dialogTitle"
      :visible.sync="qfShow"
      width="900px"
      append-to-body
    >
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="20%" />
          <col width="30%" />
          <col width="20%" />
          <col width="30%" />
        </colgroup>
        <tr>
          <td><i class="require">*</i><b>扫码确认：</b></td>
          <td colspan="3">
            <el-col :span="24">
              <div class="card-item">
                <el-input
                  v-model.trim="barCode"
                  placeholder="请扫描条形码"
                  maxlength="50"
                  show-word-limit
                >
                  <!--                  <template slot="append"
                    ><el-button
                      class="scan-btn"
                      type="primary"
                      @click="openScan(2)"
                    ></el-button
                  ></template>-->
                </el-input>
              </div>
            </el-col>
          </td>
        </tr>
        <tr>
          <td><b>使用说明：</b></td>
          <td colspan="3">
            请将档案袋上的条形码放在扫描枪下进行扫描条码，扫描后，系统会自动将该档案袋进行匹配签收
          </td>
        </tr>
      </table>

      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="outOfStockSave">确定</el-button>
        <el-button
          @click="
            () => {
              this.qfShow = false;
            }
          "
          >关闭</el-button
        >
      </div>
    </el-dialog>

    <!-- 快递签发签发信息详细 -->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :title="dialogTitle"
      v-if="issuedKdShow"
      :visible.sync="issuedKdShow"
      width="900px"
      append-to-body
    >
      <el-form
        :model="kdform"
        :rules="kdRules"
        ref="kdform"
        label-width="0px"
        class="demo-ruleForm"
      >
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><b>送达方式：</b></td>
            <td colspan="3">快递送达</td>
          </tr>
          <tr>
            <td><b>收件人：</b></td>
            <td>
              {{ applyInfo.addresseeName }}
            </td>
            <td><b>收件人邮编：</b></td>
            <td>
              {{ applyInfo.addresseePostCode }}
            </td>
          </tr>
          <tr>
            <td><b>收件人电话：</b></td>
            <td>
              {{ applyInfo.addresseeTel }}
            </td>
            <td><b>收件人手机：</b></td>
            <td>
              {{ applyInfo.addresseePhone }}
            </td>
          </tr>
          <tr>
            <td><b>收件人所在地址：</b></td>
            <td colspan="3">
              {{ applyInfo.addresseeAddress }}
            </td>
          </tr>
          <tr>
            <td><b>收件人详细地址：</b></td>
            <td colspan="3">
              {{ applyInfo.addresseeDetailAddress }}
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>寄件人：</b></td>
            <td>
              <el-form-item prop="sendePerson">
                <el-input
                  v-model.trim="kdform.sendePerson"
                  placeholder="请输入寄件人"
                  maxlength="10"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>寄件人邮编：</b></td>
            <td>
              <el-form-item prop="senderMailCode">
                <el-input
                  v-model.trim="kdform.senderMailCode"
                  placeholder="请输入寄件人邮编"
                  maxlength="6"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td><i class="require">*</i><b>寄件人电话：</b></td>
            <td>
              <el-form-item prop="senderCall">
                <el-input
                  v-model.trim="kdform.senderCall"
                  placeholder="请输入寄件人电话"
                  maxlength="20"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>寄件人手机：</b></td>
            <td>
              <el-form-item prop="senderPhone">
                <el-input
                  v-model.trim="kdform.senderPhone"
                  placeholder="请输入寄件人手机"
                  maxlength="11"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>寄件人地址：</b></td>
            <td>
              <el-form-item prop="senderAddress">
                <el-cascader
                  size="large"
                  placeholder="请输入寄件人地址"
                  :options="provinceCityOptions"
                  v-model.trim="kdform.senderAddress"
                  @blur="handleChangeAddress"
                  @change="handleChangeAddress"
                  style="width: 100%"
                >
                </el-cascader>
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>寄件人详细地址：</b></td>
            <td>
              <el-form-item prop="senderDetailAddress">
                <el-col :span="24">
                  <el-input
                    v-model.trim="kdform.senderDetailAddress"
                    placeholder="请输入寄件人详细地址"
                    maxlength="250"
                    show-word-limit
                  ></el-input>
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <!--          <tr>
            <td><i class="require">*</i><b>快递编号：</b></td>
            <td colspan="3">
              <el-form-item prop="kdCode">
                <el-col :span="24">
                  <el-input
                    v-model.trim="kdform.kdCode"
                    placeholder="请输入快递编号"
                    maxlength="50"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>-->
        </table>
      </el-form>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitKdForm">发证</el-button>
        <el-button
          @click="
            () => {
              issuedKdShow = false;
              reset();
            }
          "
          >关闭</el-button
        >
      </div>
    </el-dialog>

    <!-- 预览领证回执单信息详细 -->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :title="dialogTitle"
      v-if="issuedReceiveShow"
      :visible.sync="issuedReceiveShow"
      width="900px"
      append-to-body
    >
      <el-form :model="caseReceiveInfo" label-width="0px" class="demo-ruleForm">
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><b>领证人姓名：</b></td>
            <td colspan="2">{{ caseReceiveInfo.receiveName }}</td>
            <td rowspan="5">
              <img :src="verifyImg" class="user-avatar" />
            </td>
          </tr>
          <tr>
            <td><b>领证人证件号码：</b></td>
            <td colspan="2">
              {{ caseReceiveInfo.receiveCardCode }}
            </td>
          </tr>
          <tr>
            <td><b>联系电话：</b></td>
            <td colspan="2">
              {{ caseReceiveInfo.receivePhone }}
            </td>
          </tr>
          <tr>
            <td><b>采集时间：</b></td>
            <td colspan="2">
              {{ caseReceiveInfo.createDate }}
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="zf-text-center">
        <el-button
          @click="
            () => {
              issuedReceiveShow = false;
            }
          "
          >关闭</el-button
        >
      </div>
    </el-dialog>

    <!-- 扫码领证信息详细 -->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :title="dialogTitle"
      v-if="batchView"
      :visible.sync="batchView"
      width="900px"
      append-to-body
    >
      <el-row>
        <div class="card-item">
          <el-input
            v-model.trim="batch_caseNumber"
            placeholder="请输入办件编号"
            align="center"
            clearable
            size="100"
            @keyup.enter.native="handleBatchQuery"
          >
            <!-- <template slot="append">
              <el-button class="scan-btn" type="primary" @click="openScan(1)">
              </el-button>
            </template> -->
          </el-input>
        </div>
      </el-row>
      <div class="zf-zc-table--title">办件信息列表</div>
      <el-table v-loading="loading" :data="tableData" border>
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="办件编号"
          width="150"
          align="center"
          prop="caseNumber"
        />
        <el-table-column
          label="申请人名称"
          width="150"
          align="center"
          prop="applyUserName"
        />
        <el-table-column
          label="事项类型"
          align="center"
          width="150"
          prop="serviceType"
        />
        <el-table-column
          label="登记日期"
          width="150"
          align="center"
          prop="caseRegisterDate"
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
              icon="iconfont zfsoft-xiugai"
              @click="handleInit(scope.row)"
              v-hasPermi="['sys:licenseIssued:out']"
              >领证</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="zf-text-center">
        <el-button
          @click="
            () => {
              batchView = false;
            }
          "
          >关闭</el-button
        >
      </div>
    </el-dialog>

    <!-- <el-dialog v-dialog-drag :visible.sync="openView"  title="查看办件信息" width="70%" append-to-body>
      <el-scrollbar style="height:500px;">
        <view-case-info :msg-val="indexCaseNumber"></view-case-info>
      </el-scrollbar>
    </el-dialog>-->

    <!--利用vue插件进行打印办件证照发证凭条-->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :title="dialogTitle"
      v-if="printShow"
      :visible.sync="printShow"
      width="900px"
      append-to-body
    >
      <div id="print">
        <div
          class="el-table__header-wrapper dialog-table"
          :model="printCaseInfo"
        >
          <!-- <h3 align="center"><i class="el-icon-document"></i></h3> -->
          <div class="zf-zc-table--title">签收单</div>
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="20%" />
              <col width="80%" />
            </colgroup>
            <tr>
              <td style="text-align: center"><b>办件编号</b></td>
              <td>{{ printCaseInfo.caseNumber }}</td>
            </tr>

            <tr>
              <td style="text-align: center"><b>登记日期</b></td>
              <td>{{ printCaseInfo.caseRegisterDate }}</td>
            </tr>
            <tr>
              <td style="text-align: center"><b>注意</b></td>
              <td>
                办件编号为{{ printCaseInfo.caseNumber }}的证照，在{{
                  printCaseInfo.caseFzDate
                }}通过
                <span v-if="printCaseInfo.deliverWay == 1">快递送达</span>
                <span v-if="printCaseInfo.deliverWay == 2">自行取件</span>
                <span v-if="printCaseInfo.deliverWay == 3">其他</span>已发证。
              </td>
            </tr>
          </table>
        </div>
      </div>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" v-print="'#print'">打印</el-button>
        <el-button @click="closePrint">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 人证比对 -->
    <!-- <div v-if="dialogVisible">
      <el-dialog
        v-dialog-drag
        :visible.sync="dialogVisible"
        width="1100px"
        height="700px"
        scrollbar
        :before-close="handleClose"
        :close-on-click-modal="false"
        append-to-body
      >
        <hardware-scan ref="scanForm"></hardware-scan>
      </el-dialog>
    </div> -->
    <!-- 人证比对 -->
    <div v-if="dialogVisible">
      <el-dialog
        v-dialog-drag
        :visible.sync="dialogVisible"
        @close="handleClose"
        width="1158px"
        top="10vh"
        :close-on-click-modal="false"
        :close-on-press-escape="false"
        append-to-body
        custom-class="hardware-scan"
        title="人证比对"
      >
        <HardWareScan
          ref="scanForm"
          :isShowSmall="true"
          :caseOid="form.regOid"
          :loginUser="loginUser"
          @close="handleClose"
        />
      </el-dialog>
    </div>
  </div>
</template>

<script>
import {
  page,
  getOneByCaseOid,
  saveOrUpdateKd,
  saveOrUpdateIssued,
  getLicenseIssuedByCaseOid,
  getCertificateType,
  getLicenseIssuedByOid,
  saveOrUpOutOfStock,
  getOneByCaseNumber
} from "@/api/zc/businessManagement/licenseIssued.js";
import { getElecLicenUrl } from "@/api/zc/businessManagement/elemLice";
import viewCaseInfo from "@/views/zc/businessManagement/windowAcceptance/viewCaseInfo";
import { getOneApplyPerson } from "@/api/zc/businessManagement/viewCaseInfo";
import { validatePhone, validateYB, validateTel } from "@/utils/validate";
import {
  uploadFileByPaths,
  downFileByoid
} from "@/api/zc/businessManagement/fileUpload";
import { uploadCaseMaterialFile } from "@/api/zc/businessManagement/caseMaterialAtta";
import { CodeToText, regionData } from "element-china-area-data";
import HardWareScan from "@/views/common/hardwareScan";
import { sxServiceOidsListByUserOid } from "@/api/zc/businessManagement/sxServiceRegistrar";

import { getloginUser } from "@/api/zc/businessManagement/windowAcceptance";

import { getIdCardInfo, openIdCard, findIdCard } from "@/api/sys/hardwareScan";

import GPYDrive from "@/api/handwareDrive.js";
import DEVEICETYPE,{
    ID_CARD_V1,
    ID_CARD_V2,
    ID_CARD_V3
  } from '@/components/HiSpeedCamera/config.js'
import {
  openIdcardv3,
  closeIdcardv3,
  getdataIdcardv3
} from "@/api/handwarev3.js";
export default {
  name: "LicenseIssued",
  components: { viewCaseInfo, HardWareScan },
  data() {
    return {
      provinceCityOptions: regionData,
      // 列表数据
      caseRegList: [],
      tableData: [],
      rowNum: 1,
      // 弹窗标题
      dialogTitle: "",
      addDialogShow: false,
      issuedKdShow: false,
      openView: false,
      batchView: false,
      printShow: false,
      issuedReceiveShow: false,
      //是否已启动硬件设备，默认 启用
      isDevice: true,
      qfShow: false,
      case_Number: "", //办件编号，用于预览回执单
      barCode: "", //条形码
      recordInfo: {}, //证照管理信息
      caseReceiveInfo: {}, //证照领证信息
      batch_caseNumber: "",
      allSmCaseNum: [], //所有已经扫描的办件号
      currStep: "",
      clerkAble: "N", //是否启用签名
      varify: "",
      //签发信息
      applyInfo: {},
      certificateTypeList: [], //证件类型
      printCaseInfo: {}, //用于打印证照领证信息
      indexCaseNumber: "",
      verifyImg: "",
      form: {
        regOid: "",
        pickerCardType: "",
        receiveCardCode: "",
        caseIssuedList: {},
        oid: "",
        signInfo: "",
        photoOid: "",
        currStep: "",
        receiveName: "",
        receivePhone: ""
      },
      rules: {
        receiveCardCode: [
          { required: true, message: "取件人证件号码不能为空", trigger: "blur" }
        ],
        receiveName: [
          { required: true, message: "取件人姓名不能为空", trigger: "blur" }
        ],
        receivePhone: [
          { required: true, message: "取件人手机号不能为空", trigger: "blur" },
          {
            validator: validatePhone,
            message: "请输入正确的取件人手机号",
            trigger: "blur"
          }
        ]
      },
      kdRules: {
        senderMailCode: [
          { required: true, message: "邮编不能为空", trigger: "blur" },
          {
            validator: validateYB,
            message: "请输入正确的邮编",
            trigger: "blur"
          }
        ],
        senderCall: [
          { required: true, message: "寄件人电话不能为空！", trigger: "blur" },
          {
            validator: validateTel,
            message: "请输入正确的寄件人电话",
            trigger: "blur"
          }
        ],
        senderPhone: [
          {
            required: true,
            message: "寄件人手机号不能为空！",
            trigger: "blur"
          },
          {
            validator: validatePhone,
            message: "请输入正确的取件人手机号",
            trigger: "blur"
          }
        ],
        sendePerson: [
          { required: true, message: "寄件人不能为空！", trigger: "blur" }
        ],
        senderAddress: [
          { required: true, message: "寄件人地址不能为空！", trigger: "blur" }
        ],
        senderDetailAddress: [
          {
            required: true,
            message: "寄件人详细地址不能为空！",
            trigger: "blur"
          }
        ]
      },
      //快递送达验证
      kdform: {
        kdCode: "",
        oid: "",
        addresseeName: "",
        addresseePhone: "",
        sendePerson: "",
        senderMailCode: "",
        senderCall: "",
        senderPhone: "",
        senderAddress: "",
        senderDetailAddress: ""
      },
      // 签发状态
      statusOptions: { "0": "未发证", "1": "已发证" },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        caseNumber: "",
        licenseInStorage: "0",
        idCard: "",
        serviceOids: "",
        total: 0
      },
      dialogVisible: false,
      loading: true,
      loginUser: {}
    };
  },
  watch: {
    addDialogShow: {
      immediate: true,
      handler(val) {
        localStorage.removeItem("idCardInfo");
      }
    }
  },
  created() {
    this.initQuery();
    this.getRegSxServiceOids();
    this.queryLoginInfo();
  },
  methods: {
    handleChangeAddress() {
      this.$refs.kdform.validateField("senderAddress");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    initQuery() {
      this.queryParams.regOid = this.$route.query.regOid;
      this.queryParams.caseNumber = this.$route.query.caseNumber;
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
      this.queryParams.regOid = "";
      this.queryParams.caseNumber = "";
      this.handleQuery();
    },
    // 表单重置
    reset() {
      Object.assign(this.form, this.$options.data().form);
      this.resetForm("form");
    },
    /** 查看按钮操作 */
    handleView(row) {
      if (row.elecLicenOid) {
        getElecLicenUrl(row.elecLicenOid).then(response => {
          let urlElem = [];
          if (response.data) {
            urlElem = response.data;
            if (urlElem[0].viewOfdUrl) {
              window.open(urlElem[0].viewOfdUrl, "width=1200px;height=800px;");
            }
          }
        });
      } else {
        this.$message.error("暂无证照，无法预览!");
      }
    },
    /** 签发操作 */
    handleInit(row) {
      this.kdform = {};
      this.varify = "";
      this.reset();
      if (row.regOid) {
        this.form.regOid = row.regOid;
        this.kdform.oid = row.oid;
        this.case_Number = row.caseNumber;
        this.printCaseInfo = row; //赋值给打印，以便用户打印证照领取信息
        if (row.deliverWay == 1) {
          //快递送达
          getOneApplyPerson(row.regOid).then(response => {
            this.issuedKdShow = true;
            this.applyInfo = response.data;
            if (
              this.applyInfo.addresseeAddress.indexOf("[") != -1 &&
              this.applyInfo.addresseeAddress.indexOf("]") != -1
            ) {
              this.handleChange();
            }
            this.kdform.addresseeName = response?.data?.addresseeName;
            this.kdform.addresseePhone = response?.data?.addresseePhone;
            this.kdform.addresseeDetailAddress =
              response?.data?.addresseeDetailAddress;
            this.kdform.addresseeAddress = response?.data?.addresseeAddress;
            this.kdform.addresseePostCode = response?.data?.addresseePostCode;
            this.kdform.addresseeTel = response?.data?.addresseeTel;
          });
        } else {
          //查询证照管理信息
          getOneByCaseOid(row.regOid).then(response => {
            this.addDialogShow = true;
            if (response.data) {
              this.recordInfo = response.data;
              this.form.regOid = row.regOid;
              this.form.caseIssuedList = this.recordInfo;
              this.getSelectCertificateType(response.data.applyUserType);
              //查询签发记录信息
              getLicenseIssuedByCaseOid(row.regOid).then(response => {
                if (response.data) {
                  // this.form= {...this.form,...response?.data};
                  this.form.pickerCardType = response.data.pickerCardType;
                  this.form.receiveCardCode = response.data.receiveCardCode;
                  this.form.receiveName = response.data.receiveName;
                  this.form.receivePhone = response.data.receivePhone;
                  this.form.photoOid = response.data.photoOid;
                  this.form.oid = response.data.oid;
                  this.currStep = response.data.currStep; //防止修改重新进来必须人证比对
                  // this.$forceUpdate();
                }
              });
            }
          });
        }
      } else {
        if (row.deliverWay == 1) {
          this.issuedKdShow = true;
        } else {
          this.addDialogShow = true;
        }
      }
      this.dialogTitle = "领证信息";
    },
    // 编辑格式化地址
    handleChange() {
      let str = this.applyInfo.addresseeAddress;
      str = str.substring(1, str.length - 1);
      let self = str.toString().replace(/"/g, "");
      var selfArr = self.split(",");
      if (null != selfArr) {
        var provinceCode = "";
        var zhixia = "";
        var city = "";
        if (null != selfArr[0]) {
          provinceCode = CodeToText[selfArr[0]];
        }
        if (null != selfArr[1]) {
          zhixia = "/" + CodeToText[selfArr[1]];
        }
        if (null != selfArr[2]) {
          city = "/" + CodeToText[selfArr[2]];
        }
        this.applyInfo.addresseeAddress = provinceCode + zhixia + city;
      }
    },
    /** 获取证件类型 */
    getSelectCertificateType(cegisterType) {
      /** 写死获取个人的证件类型信息 */
      getCertificateType("0").then(response => {
        this.certificateTypeList = response.data;
      });
    },
    /** 提交按钮 */
    submitKdForm() {
      if (!this.kdform.sendePerson) {
        this.$message.error("寄件人不能为空！");
        return false;
      }
      if (!this.kdform.senderMailCode) {
        this.$message.error("寄件人邮编不能为空！");
        return false;
      }
      if (!this.kdform.senderPhone) {
        this.$message.error("寄件人手机号不能为空！");
        return false;
      }
      if (!this.kdform.senderAddress) {
        this.$message.error("寄件人地址不能为空！");
        return false;
      }
      if (!this.kdform.senderDetailAddress) {
        this.$message.error("寄件人详细地址不能为空！");
        return false;
      }

      console.log(JSON.stringify(this.kdform));
      this.$refs.kdform.validate(valid => {
        if (valid) {
          //寄件人地址处理
          var codeArray = this.kdform.senderAddress;
          if (codeArray) {
            var province = "";
            var zhixia = "";
            var city = "";
            if (null != codeArray[0]) {
              province = CodeToText[codeArray[0]];
            }
            if (null != codeArray[1]) {
              zhixia = "/" + CodeToText[codeArray[1]];
            }
            if (null != codeArray[2]) {
              city = "/" + CodeToText[codeArray[2]];
            }
            this.kdform.senderAddress = province + zhixia + city;
          }

          saveOrUpdateKd(this.kdform).then(response => {
            console.log("====================", JSON.stringify(response));
            if (response.code === 200) {
              this.printCaseInfo.caseFzDate = response.data;
              this.msgSuccess("保存成功");
              this.issuedKdShow = false;
              setTimeout(() => {
                //询问是否打印材料出库单
                this.$confirm("是否打印证照发证凭条?", "提示", {
                  confirmButtonText: "确定",
                  cancelButtonText: "取消",
                  type: "warning"
                })
                  .then(() => {
                    this.handlePrint();
                  })
                  .catch(() => {
                    //判断是否有扫描领证的
                    if (this.tableData && this.tableData.length > 0) {
                      this.tableData.forEach((item, i) => {
                        if (item.regOid == this.printCaseInfo.regOid) {
                          this.tableData.splice(i, 1); //删除数组
                          this.$forceUpdate(); //强制刷新列表
                        }
                      });
                    }
                    this.getList();
                  });
              }, 10);
            }
          });
        }
      });
      /*if (!this.kdform.kdCode) {
        this.$message.error("快递单号不能为空！");
      } else {

      }*/
    },
    /** 拍照保存记录提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          saveOrUpdateIssued(this.form).then(response => {
            if (response.code === 200) {
              this.currStep = response.data.currStep;
              this.form.oid = response.data.oid;
              this.msgSuccess("保存成功");
              //this.form={};
            }
          });
        }
      });
    },
    /** 批量出库操作 */
    handleBatch() {
      this.batchView = true;
      this.dialogTitle = "领证";
      this.batch_caseNumber = "";
      this.tableData = [];
      this.allSmCaseNum = [];
    },
    //批量扫描enter查询
    handleBatchQuery() {
      if (this.batch_caseNumber) {
        if (this.allSmCaseNum) {
          if (this.allSmCaseNum.indexOf(this.batch_caseNumber) != -1) {
            this.$message.error("办件已存在!");
            return false;
          }
        }
        getOneByCaseNumber(this.batch_caseNumber).then(response => {
          if (response.data) {
            if (
              response.data.licenseOutStorage &&
              response.data.licenseOutStorage == "1"
            ) {
              this.$message.error("该办件已签发！");
            } else {
              this.addRow(response.data);
              this.allSmCaseNum.push(this.batch_caseNumber);
            }
          } else {
            this.$message.error("未查询到相关数据!");
          }
          this.batch_caseNumber = "";
        });
      } else {
        this.$message.error("办件编号不能为空!");
      }
    },
    // 增加行
    addRow(caseInfo) {
      this.tableData.unshift(caseInfo); //追加数据
      this.rowNum += 1;
    },
    handlePrint() {
      this.printShow = true;
    },
    closePrint() {
      this.printShow = false;
      setTimeout(() => {
        this.getList(); //返回列表页面
        //判断是否有扫描领证的
        if (this.tableData && this.tableData.length > 0) {
          this.tableData.forEach((item, i) => {
            if (item.regOid == this.printCaseInfo.regOid) {
              this.tableData.splice(i, 1); //删除数组
              this.$forceUpdate(); //强制刷新列表
            }
          });
        }
      }, 10);
    },
    //预览领证回执
    viewReceiveInfo() {
      if (!this.form.photoOid) {
        this.$message.error("请先进行人证比对！");
        return;
      }
      /*if (this.varify != 1) {
        this.$message.error("请先进行人证比对！");
        return;
      }*/
      //预览回执单信息
      getLicenseIssuedByOid(this.form.oid).then(response => {
        if (response.data) {
          //回执单
          this.caseReceiveInfo = response.data;
          //this.caseReceiveInfo.caseNumber=this.case_Number;
          //图片预加载
          downFileByoid(this.caseReceiveInfo.photoOid).then(response => {
            this.verifyImg =
              response.data + "?attaOid=" + this.caseReceiveInfo.photoOid;
          });
          this.issuedReceiveShow = true;
        }
      });
    },
    //签发扫描条形码
    outOfStock() {
      if (!this.form.photoOid) {
        this.$message.error("请先进行人证比对！");
        return;
      }
      if (this.varify == 2) {
        this.$message.error("人证比对不通过，请重新比对！");
        return;
      }
      if (!this.varify) {
        this.$message.error("请先进行人证比对！");
        return;
      }
      //进行签发保存操作
      this.qfShow = true;
    },
    //签发扫描出件
    outOfStockSave() {
      if (!this.barCode) {
        this.$message.error("请扫描档案袋上的条形码！");
      } else {
        //调用保存信息
        //this.addDialogShow = false;
        saveOrUpOutOfStock(this.recordInfo.oid, this.barCode).then(response => {
          this.barCode = "";
          if (response.code == 200 && response.data.outDate) {
            this.qfShow = false;
            this.form = {};
            this.addDialogShow = false;
            this.verifyImg = "";
            this.printCaseInfo.caseFzDate = response.data.outDate;
            setTimeout(() => {
              //询问是否打印材料出库单
              this.$confirm("是否打印签收单?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
              })
                .then(() => {
                  this.handlePrint();
                })
                .catch(() => {
                  //判断是否有扫描领证的,如果有刷新去掉列表中出证的
                  if (this.tableData && this.tableData.length > 0) {
                    this.tableData.forEach((item, i) => {
                      if (item.regOid == this.printCaseInfo.regOid) {
                        this.tableData.splice(i, 1); //删除数组
                        this.$forceUpdate(); //强制刷新列表
                      }
                    });
                  }
                  this.getList();
                });
            }, 10);
          } else {
            this.$message.error("发证失败，请输入正确的办件编号");
            return;
          }
        });
      }
    },
    base64ToFile(urlData, fileName) {
      let arr = urlData.split(",");
      // let mime = arr[0].match(/:(.*?);/)[1];
      let bytes = atob(arr[0]); // 解码base64
      let n = bytes.length;
      let ia = new Uint8Array(n);
      while (n--) {
        ia[n] = bytes.charCodeAt(n);
      }
      return new File([ia], fileName, { type: "image/jpeg" });
    },
    // 接收socket回调函数返回数据的方法
    getConfigResult(data) {
      if (data.status == 0) {
        //读卡
        if (data.device == "IdCard") {
          this.form.receiveCardCode = data.content.CardNum;
          this.form.receiveName = data.content.CardBelongName;
          this.form.pickerCardType = "身份证";
        }
        if (data.device == "PersonVerify") {
          if (data.content.verifyFlag && data.content.verifyFlag == 0) {
            this.varify = 1;
            this.currStep = 1;
            //将用户选择的图片进行保存处理
            //预览领证回执设置图片
            this.verifyImg =
              "data:image/jpeg;base64," + data.content.verifyBase64;
            let file = this.base64ToFile(
              data.content.verifyBase64,
              "verifyPicture.jpg"
            );
            let formD = new FormData();
            formD.append("files", file);
            uploadCaseMaterialFile(formD).then(response => {
              if (response.data != "") {
                response.data.forEach(data => {
                  //取证人信息保存
                  console.log(data.attaOid);
                  this.form.photoOid = data.attaOid; //人证比对返回的附件主键
                  this.submitForm();
                  downFileByoid(data.attaOid).then(response => {
                    this.verifyImg =
                      response.data + "?attaOid=" + this.form.photoOid;
                  });
                });
              }
            });
          } else {
            this.varify = 2;
            this.currStep = "";
            if (data.content.verifyMessage) {
              this.$message.error(data.content.verifyMessage);
            }
          }
        }
      }
      if (data.status == 1) {
        this.currStep = "";
        this.$message.error(data.message);
      }
    },
    //初始化socket发生错误回调
    socketError() {
      this.$message.error("请检查设备或连接是否正常");
    },
    scanCard(data) {
      if (DEVEICETYPE.IDCARD_CONFIG === ID_CARD_V2) {
        this.getIdcardData(data);
      }
      if (DEVEICETYPE.IDCARD_CONFIG === ID_CARD_V3 || DEVEICETYPE.IDCARD_CONFIG === ID_CARD_V1) {
        this.getIdcardDatav3(data);
      }
    },
    /** --------------------------------------- 综窗api高拍仪 版本3 --------------------------------------- */
    async getIdcardDatav3(data) {
      this.form.receiveName = "";
      openIdcardv3().then(res => {
        if (res.StateCode == 0 || res.StateCode == -1) {
          return this.getIdcardDataByv3(data); //获取身份证信息
        }else{
          return this.$message.warning("请确认设备连接是否正常");
        }
      });
    },
    async getIdcardDataByv3(scantype) {
      getdataIdcardv3().then(res => {
        if (res.state == "sucess" && res.StateCode == 0) {
          let resInfo = JSON.parse(res.data);
          let cardInfo = {
            CNName: resInfo.name,
            sex: resInfo.sex,
            carID: resInfo.number,
            nation: resInfo.nation,
            address: resInfo.department,
            bron: resInfo.birthday,
            imgData: "data:image/jpeg;base64," + resInfo.Image
          };
          if (scantype == 0) {
            this.handleIdCardInfo(cardInfo);
          } else if (scantype == 1) {
            this.queryParams.idCard = cardInfo.carID;
          }
        } else {
          this.$message.error("读取身份证信息失败");
          return false;
        }
      });
    },
    /** --------------------------------------- 综窗api高拍仪结尾 版本3 --------------------------------------- */

    async getIdcardData(data) {
      this.form.receiveName = "";
      const resData = await GPYDrive.readCardInfo();
      if (!resData || resData.code !== 200) {
        this.$message.error("读取身份证信息失败");
        return false;
      }
      let res = {
        CNName: resData.data.name,
        sex: resData.data.sex,
        carID: resData.data.number,
        nation: resData.data.nation,
        address: resData.data.address,
        bron: resData.data.birthday,
        imgData: "data:image/jpeg;base64," + resData.data.image
      };
      if (data == 0) {
        this.handleIdCardInfo(res);
      } else if (data == 1) {
        this.queryParams.idCard = res.carID;
      }
    },
    handleIdCardInfo(res) {
      this.$set(this.form, "receiveName", res.CNName.trim());
      this.$set(this.form, "receiveCardCode", res.carID);
      const dictOid = this.certificateTypeList?.find?.(
        item => item.code === "SFZ"
      )?.dictOid;
      this.$set(this.form, "pickerCardType", dictOid);
      localStorage.setItem("idCardInfo", JSON.stringify(res));
    },

    /** 登录信息 */
    queryLoginInfo() {
      getloginUser().then(response => {
        this.loginUser = response.data;
      });
    },

    handleClose(rzbdResult) {
      //this.openWelcome();
      this.varify = this.$refs?.scanForm.rzbdResult || rzbdResult;
      this.$refs.scanForm.getImageRes();
      let res = this.$refs.scanForm.returnInfo();
      this.saveAndUploadFile(res);
      this.dialogVisible = false;
    },

    //调用摄像头获取图像信息
    getImageCamera() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.dialogVisible = true;
        }
      });
    },

    //保存并上传图片信息
    saveAndUploadFile(info) {
      if (info && info.faceImg) {
        this.currStep = 1;
        //将用户选择的图片进行保存处理
        //预览领证回执设置图片
        this.verifyImg = "data:image/jpeg;base64," + info.faceImg;
        let file = this.base64ToFile(info.faceImg, "verifyPicture.jpg");
        let formD = new FormData();
        formD.append("files", file);
        uploadCaseMaterialFile(formD).then(response => {
          if (response.data != "") {
            response.data.forEach(data => {
              //取证人信息保存
              this.form.photoOid = data.attaOid; //人证比对返回的附件主键
              this.submitForm();
              downFileByoid(data.attaOid).then(response => {
                this.verifyImg =
                  response.data + "?attaOid=" + this.form.photoOid;
              });
            });
          }
        });
      }
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
