/**
* @Author: liangss
* @Date: 2021-1-5
* @Description: 办件退件
*/
<template>
  <div class="app-container">
    <!--查询条件start -->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="70px"
    >
      <el-form-item label="申请人" prop="applyUserName">
        <el-input
          v-model.trim="queryParams.applyUserName"
          placeholder="请输入申请人"
          clearable
          size="100"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
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
      <el-form-item label="退件状态" label-width="108px">
        <el-radio-group v-model="queryParams.returnStatus">
          <el-radio
            v-for="(status, key) in returnStatusOptions"
            :key="key"
            :label="key"
            >{{ status }}</el-radio
          >
        </el-radio-group>
      </el-form-item>
      <el-form-item label="告知状态" label-width="108px">
        <el-radio-group v-model="queryParams.informStatus">
          <el-radio
            v-for="(status, key) in informStatusOptions"
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
    <!--查询条件end -->
    <el-row :gutter="10" class="mb8" style="width: 120px">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleBatch"
          >扫码退件</el-button
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
        label="事项名称"
        align="center"
        prop="serviceName"
        :show-overflow-tooltip="true"
      />
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
        label="证件号"
        align="center"
        prop="credentialNumber"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="登记日期"
        align="center"
        prop="createDate"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            v-show="tjgzStatus == '1'"
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInitInfrom(scope.row)"
            >退件告知</el-button
          >
          <el-button
            v-show="tjStatus == '1'"
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            >退件</el-button
          >
          <el-button
            v-show="tjStatus != '1'"
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            >查看</el-button
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

    <!-- 办件退件 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogTitle"
      v-if="addDialogShow"
      :visible.sync="addDialogShow"
      width="1000px"
      height="600px"
      scrollbar
      append-to-body
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="0"
        size="mini"
      >
        <table cellpadding="0" cellspacing="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b>申请人：</b>
            </td>
            <td>
              {{ caseInfo.applyUserName }}
            </td>
            <td>
              <b>证件号码：</b>
            </td>
            <td>
              {{ caseInfo.credentialNumber }}
            </td>
          </tr>
          <tr>
            <td>
              <b>申请人手机：</b>
            </td>
            <td>
              {{ caseInfo.applyUserPhone }}
            </td>
            <td>
              <b>申请人住址：</b>
            </td>
            <td>
              {{ caseInfo.addresseeDetailAddress }}
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>退件人证件类型：</b></td>
            <td colspan="3">
              <el-form-item prop="pickerCardType">
                <el-col :span="24">
                  <el-select
                    v-model="form.pickerCardType"
                    placeholder="请选择退件人证件类型"
                  >
                    <el-option
                      v-for="certificateType in certificateTypeList"
                      :key="certificateType.dictOid"
                      :label="certificateType.name"
                      :value="certificateType.dictOid"
                    >
                    </el-option>
                  </el-select>
                  <!--  <span v-if="varify==1" style="color: green">人证比对通过</span>
                  <span v-if="varify==2" style="color: red">人证比对不通过</span>-->
                  <!-- <el-button type="primary" @click="scanCard('0');" style="float: right">✚ 扫描身份证</el-button>-->
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b><i class="require">*</i>退件人证件号码：</b>
            </td>
            <td>
              <el-form-item prop="receiveCardCode">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.receiveCardCode"
                    placeholder="请输入退件人证件号码"
                    maxlength="50"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>

            <td>
              <b><i class="require">*</i>退件人名称：</b>
            </td>
            <td>
              <el-form-item prop="receiveName">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.receiveName"
                    placeholder="请输入退件人名称"
                    maxlength="50"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b><i class="require">*</i>退件人手机号：</b>
            </td>
            <td>
              <el-form-item prop="receivePhone">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.receivePhone"
                    placeholder="请输入退件人手机号"
                    maxlength="11"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>

            <td>
              <b><i class="require">*</i>是否短信通知：</b>
            </td>
            <td>
              <el-form-item prop="isCms">
                <el-form-item>
                  <el-radio v-model="form.isCms" label="Y">是</el-radio>
                  <el-radio v-model="form.isCms" label="N">否</el-radio>
                </el-form-item>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b><i class="require">*</i>退件材料明细：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="materialInfo">
                <el-col :span="24">
                  <el-input
                    type="textarea"
                    placeholder="请输入退件材料明细"
                    v-model.trim="form.materialInfo"
                    maxlength="2000"
                    show-word-limit
                  ></el-input>
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b><i class="require">*</i>退件说明：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="returnRemark">
                <el-col :span="24">
                  <el-input
                    type="textarea"
                    placeholder="请输入退件说明"
                    v-model.trim="form.returnRemark"
                    maxlength="2000"
                    show-word-limit
                  ></el-input>
                </el-col>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div class="el-table-box">
        <el-table v-loading="loading" :data="caseMaterialList" border>
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="材料名称"
            show-overflow-tooltip
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
            width="200"
            prop="collectionType"
          >
            <template slot-scope="scope">
              <p v-if="scope.row.collectionType == 1">纸质收取</p>
              <p v-if="scope.row.collectionType == 2">附件上传</p>
              <p v-if="scope.row.collectionType == 3">扫描</p>
              <p v-if="scope.row.collectionType == 4">容缺后补</p>
              <p v-if="scope.row.collectionType == 5">证照库</p>
            </template>
          </el-table-column>

          <el-table-column
            label="收取数量"
            width="100"
            align="center"
            prop="collectionNumber"
          />
        </el-table>
      </div>
      <div slot="footer" class="dialog-footer" style="text-align: center">
        <el-button type="primary" @click="getReceivePhoto">拍照</el-button>
        <!--        <el-button type="primary" @click="signConfirm">签名</el-button>-->
        <el-button type="primary" @click="viewReceiveInfo"
          >预览退件告知单</el-button
        >
        <el-button type="primary" @click="submitForm">退件</el-button>
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

    <!--退件详细信息-->
    <el-dialog
    v-dialog-drag
      :close-on-click-modal="false"
      :title="dialogTitle"
      v-if="viewDialogShow"
      :visible.sync="viewDialogShow"
      width="1000px"
      height="600px"
      scrollbar
      append-to-body
    >
      <el-form ref="form" :model="form" label-width="0" size="mini">
        <table cellpadding="0" cellspacing="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b>申请人：</b>
            </td>
            <td>
              {{ caseInfo.applyUserName }}
            </td>
            <td>
              <b>证件号码：</b>
            </td>
            <td>
              {{ caseInfo.credentialNumber }}
            </td>
          </tr>
          <tr>
            <td>
              <b>申请人手机：</b>
            </td>
            <td>
              {{ caseInfo.applyUserPhone }}
            </td>
            <td>
              <b>申请人住址：</b>
            </td>
            <td>
              {{ caseInfo.addresseeDetailAddress }}
            </td>
          </tr>
          <tr>
            <td>
              <b><i class="require">*</i>退件人证件号码：</b>
            </td>
            <td>
              {{ form.receiveCardCode }}
            </td>

            <td>
              <b><i class="require">*</i>退件人名称：</b>
            </td>
            <td>
              {{ form.receiveName }}
            </td>
          </tr>
          <tr>
            <td>
              <b><i class="require">*</i>退件人手机号：</b>
            </td>
            <td>
              {{ form.receivePhone }}
            </td>

            <td>
              <b><i class="require">*</i>是否短信通知：</b>
            </td>
            <td>
              <template v-if="form.isCms == 'Y'">是</template>
              <template v-if="form.isCms == 'N'">否</template>
            </td>
          </tr>
          <tr>
            <td>
              <b>退件材料明细：</b>
            </td>
            <td colspan="3">
              {{ form.materialInfo }}
            </td>
          </tr>
          <tr>
            <td>
              <b>退件说明：</b>
            </td>
            <td colspan="3">
              {{ form.returnRemark }}
            </td>
          </tr>
        </table>
        <el-table
          v-loading="loading"
          :data="caseMaterialList"
          border
          class="mt20 tx-detail"
        >
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="材料名称"
            show-overflow-tooltip
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
            width="200"
            prop="collectionType"
          >
            <template slot-scope="scope">
              <p v-if="scope.row.collectionType == 1">纸质收取</p>
              <p v-if="scope.row.collectionType == 2">附件上传</p>
              <p v-if="scope.row.collectionType == 3">扫描</p>
              <p v-if="scope.row.collectionType == 4">容缺后补</p>
              <p v-if="scope.row.collectionType == 5">证照库</p>
            </template>
          </el-table-column>

          <el-table-column
            label="收取数量"
            width="100"
            align="center"
            prop="collectionNumber"
          />
        </el-table>
      </el-form>

      <div slot="footer" class="dialog-footer" style="text-align: center">
        <el-button
          @click="
            () => {
              viewDialogShow = false
              reset()
            }
          "
          >关闭</el-button
        >
      </div>
    </el-dialog>

    <!-- 办件退件告知 -->
    <el-dialog
    v-dialog-drag
      :close-on-click-modal="false"
      :title="dialogTitle"
      v-if="addInfromDialogShow"
      :visible.sync="addInfromDialogShow"
      width="1000px"
      height="600px"
      scrollbar
      append-to-body
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rulesInfrom"
        label-width="0"
        size="mini"
      >
        <table cellpadding="0" cellspacing="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b>申请人：</b>
            </td>
            <td>
              {{ caseInfo.applyUserName }}
            </td>
            <td>
              <b>证件号码：</b>
            </td>
            <td>
              {{ caseInfo.credentialNumber }}
            </td>
          </tr>
          <tr>
            <td>
              <b>申请人手机：</b>
            </td>
            <td>
              {{ caseInfo.applyUserPhone }}
            </td>
            <td>
              <b>申请人住址：</b>
            </td>
            <td>
              {{ caseInfo.addresseeDetailAddress }}
            </td>
          </tr>
          <tr>
            <td>
              <b><i class="require">*</i>退件人证件号码：</b>
            </td>
            <td>
              <el-form-item prop="receiveCardCode">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.receiveCardCode"
                    placeholder="请输入退件人证件号码"
                    maxlength="50"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>

            <td>
              <b><i class="require">*</i>退件人名称：</b>
            </td>
            <td>
              <el-form-item prop="receiveName">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.receiveName"
                    placeholder="请输入退件人名称"
                    maxlength="50"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b><i class="require">*</i>退件人手机号：</b>
            </td>
            <td>
              <el-form-item prop="receivePhone">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.receivePhone"
                    placeholder="请输入退件人手机号"
                    maxlength="11"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>

            <td>
              <b><i class="require">*</i>是否短信通知：</b>
            </td>
            <td>
              <el-form-item prop="informCms">
                <el-form-item>
                  <el-radio v-model="form.informCms" label="Y">是</el-radio>
                  <el-radio v-model="form.informCms" label="N">否</el-radio>
                </el-form-item>
              </el-form-item>
            </td>
          </tr>
          <tr v-if="form.informCms == 'Y'">
            <td>
              <b><i class="require">*</i>短信告知内容：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="informRemark">
                <el-col :span="24">
                  <el-input
                    type="textarea"
                    placeholder="请输入短信告知内容"
                    v-model.trim="form.informRemark"
                    maxlength="2000"
                    show-word-limit
                  ></el-input>
                </el-col>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div class="el-table-box">
        <el-table v-loading="loading" :data="caseMaterialList" border>
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="材料名称"
            show-overflow-tooltip
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
            width="200"
            prop="collectionType"
          >
            <template slot-scope="scope">
              <p v-if="scope.row.collectionType == 1">纸质收取</p>
              <p v-if="scope.row.collectionType == 2">附件上传</p>
              <p v-if="scope.row.collectionType == 3">扫描</p>
              <p v-if="scope.row.collectionType == 4">容缺后补</p>
              <p v-if="scope.row.collectionType == 5">证照库</p>
            </template>
          </el-table-column>
          <el-table-column
            label="收取数量"
            width="100"
            align="center"
            prop="collectionNumber"
          />
        </el-table>
      </div>
      <div slot="footer" style='text-align:center'>
        <el-button type="primary" @click="submitInForm">退件告知</el-button>
        <el-button
          @click="
            () => {
              addInfromDialogShow = false
              reset()
            }
          "
          >关闭</el-button
        >
      </div>
    </el-dialog>

    <!-- 预览办件退件告知单 -->
    <el-dialog
      :close-on-click-modal="false"
      title="预览退件告知单"
      v-if="issuedReceiveShow"
      :visible.sync="issuedReceiveShow"
      width="1000px"
      append-to-body
    >
      <el-form :model="formYl" label-width="0" size="mini">
        <table class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b>申请人：</b>
            </td>
            <td>
              {{ caseInfo.applyUserName }}
            </td>
            <td>
              <b>证件号码：</b>
            </td>
            <td>
              {{ caseInfo.credentialNumber }}
            </td>
          </tr>
          <tr>
            <td>
              <b>申请人手机：</b>
            </td>
            <td>
              {{ caseInfo.applyUserPhone }}
            </td>
            <td>
              <b>申请人住址：</b>
            </td>
            <td>
              {{ caseInfo.addresseeDetailAddress }}
            </td>
          </tr>
          <tr>
            <td>
              <b>文书模板类型：</b>
            </td>
            <td>
              <el-form-item prop="pickerCardType">
                <el-col :span="24">
                  <el-select v-model="formYl.pickerCardType" :disabled="true">
                    <el-option
                      v-for="certificateType in certificateTypeList"
                      :key="certificateType.dictOid"
                      :label="certificateType.name"
                      :value="certificateType.dictOid"
                    >
                    </el-option>
                  </el-select>
                </el-col>
              </el-form-item>
            </td>
            <td>
              <b>退件人证件号码：</b>
            </td>
            <td>
              {{ formYl.receiveCardCode }}
            </td>
          </tr>
          <tr>
            <td>
              <b>退件人名称：</b>
            </td>
            <td>
              {{ formYl.receiveName }}
            </td>
            <td colspan="2" style="background: #fff; text-align: center">
              <img :src="verifyImg" width="400px" height="200px" />
            </td>
          </tr>
          <tr>
            <td>
              <b>退件人手机号：</b>
            </td>
            <td>
              {{ formYl.receivePhone }}
            </td>

            <td>
              <b>是否短信通知：</b>
            </td>
            <td>
              <template v-if="formYl.isCms == 'Y'">是</template>
              <template v-if="formYl.isCms == 'N'">否</template>
            </td>
          </tr>
          <tr>
            <td>
              <b>退件材料明细：</b>
            </td>
            <td colspan="3">
              {{ formYl.materialInfo }}
            </td>
          </tr>
          <tr>
            <td>
              <b>退件说明：</b>
            </td>
            <td colspan="3">
              {{ formYl.returnRemark }}
            </td>
          </tr>
        </table>
      </el-form>

      <div slot="footer" class="dialog-footer" style="text-align: center">
        <el-button
          @click="
            () => {
              issuedReceiveShow = false
            }
          "
          >关闭</el-button
        >
      </div>
    </el-dialog>

    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :title="dialogTitle"
      v-if="batchView"
      :visible.sync="batchView"
      width="800px"
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
              <el-button
                class="scan-btn"
                type="primary"
                @click="openScan(1)">
              </el-button>
            </template> -->
          </el-input>
        </div>
      </el-row>
      <div class="zf-zc-table--title">办件退件列表</div>
      <el-table :data="tableData" v-loading="loading" border :fit="true">
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="事项名称"
          align="center"
          prop="serviceName"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="办件编号"
          align="center"
          prop="caseNumber"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="申请人"
          align="center"
          prop="applay.applyUserName"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="证件号"
          align="center"
          prop="applay.credentialNumber"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="登记日期"
          align="center"
          prop="createDate"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="操作"
          align="center"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button
              v-show="queryParams.returnStatus == '1'"
              size="mini"
              type="text"
              icon="iconfont zfsoft-xiugai"
              @click="handleInit(scope.row)"
              >退件</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="zf-text-center">
        <el-button
          @click="
            () => {
              batchView = false
            }
          "
          >关闭</el-button
        >
      </div>
    </el-dialog>

    <TakePhoto
      :visibleDialog.sync="visibleDialog"
      @getPhotoInfo="getPhotoInfo"
    />
  </div>
</template>

<script>
import {
  page,
  outReturnFile,
  saveCaseReturn,
  saveOrUpdateInfrom,
  queryCaseReturnByCaseOid,
  getCertificateType,
  getOneRetuenCaseByCaseNumber
} from "@/api/zc/businessManagement/caseBjtj.js";
import {
  getOneMaterialInfo
} from "@/api/zc/businessManagement/viewCaseInfo.js";
import {
  queryDistrictSimpleTree
} from "@/api/sys/district";
import Treeselect from '@riophae/vue-treeselect';
import '@riophae/vue-treeselect/dist/vue-treeselect.css';
import viewCaseInfo from "@/views/zc/businessManagement/windowAcceptance/viewCaseInfo";
import vueQr from 'vue-qr';
import { validatePhone } from "@/utils/validate";
import {
  downFileByoid,
  uploadFileByPaths,
  querySysAttaByOid
} from "@/api/zc/businessManagement/fileUpload";
import {
  uploadCaseMaterialFile
} from "@/api/zc/businessManagement/caseMaterialAtta";
import { sxServiceOidsListByUserOid } from "@/api/zc/businessManagement/sxServiceRegistrar";
/* import {getOneByCaseNumber} from "@/api/zc/businessManagement/licenseIssued";*/
import Resolution from '@/mixins/resolution.js';
import { activeScanningGun, openScanningGun } from '@/api/zc/businessManagement/charge'

/** 拍照 */
import TakePhoto from '@/components/TakePhoto/dialog.vue';
export default {
  name: "CaseBjtj",
  mixins: [Resolution],
  components: {
    viewCaseInfo,
    Treeselect,
    vueQr,
    TakePhoto
  },
  data () {
    return {
      tjgzStatus: "",
      tjStatus: "",
      // 列表数据
      caseRegList: [],
      tableData: [],
      rowNum: 1,
      attaList: [],
      materialAttaList: [],
      // 弹窗标题
      dialogTitle: '',
      addDialogShow: false,
      viewDialogShow: false,
      addInfromDialogShow: false,
      openView: false,
      batchView: false,
      printShow: false,
      issuedReceiveShow: false,
      formYl: {},

      batch_caseNumber: "",
      // 机构
      listOrganOptions: [],
      // 区划树
      districtOptions: [],
      //材料信息
      caseMaterialList: [],
      //证件类型
      certificateTypeList: [],
      materialOidsArr: [],
      multipleSelection: [],
      form: {},
      caseReturn: {},
      caseOid: '',
      currStep: "", //1 已拍照
      clerkAble: "N", //是否启用签名
      varify: '',
      //签发信息
      applyInfo: {},
      attaInfo: {},
      //是否已启动硬件设备，默认 启用
      isDevice: true,
      printCaseInfo: {}, //用于打印证照领证信息
      indexCaseNumber: "",
      verifyImg: "",
      allSmCaseNum: [],

      //退件状态'2': '取消退件',
      returnStatusOptions: {
        '1': '未退件',
        '3': '已退件'
      },
      //告知状态
      informStatusOptions: {
        '1': '未告知',
        '3': '已告知'
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        caseNumber: "",
        returnStatus: '1',
        informStatus: '1',
        sourceApp: 1,
        startDate: null,
        endDate: new Date(),
        serviceOids: "",
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
      },
      // 表单校验
      rules: {
        receiveCardCode: [{
          required: true,
          message: "退件人证件号码不能为空",
          trigger: "blur"
        }],
        receiveName: [{
          required: true,
          message: "退件人名称不能为空",
          trigger: "blur"
        }],
        receivePhone: [{
          required: true,
          message: "退件人手机号不能为空",
          trigger: "blur"
        },
        {
          validator: validatePhone,
          message: '请输入正确的退件人手机号',
          trigger: 'blur'
        }
        ],
        isCms: [{
          required: true,
          message: "是否短信通知不能为空",
          trigger: "blur"
        }],
        materialInfo: [{
          required: true,
          message: "退件材料明细不能为空",
          trigger: "blur"
        }],
        returnRemark: [{
          required: true,
          message: "退件说明不能为空",
          trigger: "blur"
        }],
      },
      rulesInfrom: {
        receiveCardCode: [{
          required: true,
          message: "退件人证件号码不能为空",
          trigger: "blur"
        }],
        receiveName: [{
          required: true,
          message: "退件人名称不能为空",
          trigger: "blur"
        }],
        receivePhone: [{
          required: true,
          message: "退件人手机号不能为空",
          trigger: "blur"
        },
        {
          validator: validatePhone,
          message: '请输入正确的退件人手机号',
          trigger: 'blur'
        }
        ],
        informCms: [{
          required: true,
          message: "是否短信通知不能为空",
          trigger: "blur"
        }],
        informRemark: [{
          required: true,
          message: "短信告知内容不能为空",
          trigger: "blur"
        }]
      },
      caseInfo: {},

      visibleDialog: false,
    };
  },
  computed: {
    calcHeight () {
      return (this.resolutionHeight === 1080 && this.resolutionWidth === 1280)
        || (this.resolutionHeight === 1024 && this.resolutionWidth === 1280)
        ? 'calc(100% - 260px)' : 'calc(100% - 210px)';
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
    districtSel (node, instanceId) {
      this.form.districtName = node.label;
    },
    organSel (node, instanceId) {
      this.form.organName = node.label;
    },
    /** 获取证件类型 */
    getSelectCertificateType (cegisterType) {
      //根据质控需求，改为查询个人的证件类型
      cegisterType=0;
      getCertificateType(cegisterType).then(response => {
        this.certificateTypeList = response.data;
      });
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
    /** 获取区划树 */
    getDistrictTree (districtOid) {

      queryDistrictSimpleTree(districtOid).then(response => {
        this.districtOptions = response.data;
      });
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.caseRegList = [];
      this.getList();
    },
    /** 查询列表 */
    getList () {
      this.loading = true;
      page(this.queryParams).then(response => {
        if (response.data) {
          this.caseRegList = response.data.data;
          this.queryParams.total = response.data.total;
          //按钮状态修改
          this.tjgzStatus = this.queryParams.informStatus;
          this.tjStatus = this.queryParams.returnStatus;
        }
        this.loading = false;
      });
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 表单重置
    reset () {
      Object.assign(this.form, this.$options.data().form)
      this.resetForm("form");
    },
    /** 查看按钮操作 */
    handleViewold (row) {
      this.indexCaseNumber = row.caseNumber;
      this.openView = true;
      this.dialogTitle = "查看详情";
    },
    handleView (row) {
      this.caseInfo = row;
      if (row.caseOid) {
        this.form.caseOid = row.caseOid;
        getOneMaterialInfo(row.caseOid).then(response => {
          this.viewDialogShow = true;
          this.caseMaterialList = response.data;
          for (let material of this.caseMaterialList) {
            this.materialOidsArr.push(material.caseMaterialOid)
          }
        });
        queryCaseReturnByCaseOid(row.caseOid).then(response => {
          this.form = response.data;
        });
      } else {
        this.viewDialogShow = true;
      }
      this.dialogTitle = "办件退件详细查看";
    },
    /** 退件操作 */
    handleInit (row) {
      this.caseInfo = row;
      if(row.applay != "" && row.applay != null) {
        this.caseInfo.applyUserName =  row.applay.applyUserName;
        this.caseInfo.credentialNumber = row.applay.credentialNumber;
        this.caseInfo.applyUserPhone = row.applay.applyUserPhone;
        this.caseInfo.addresseeDetailAddress = row.applay.addresseeDetailAddress;
      }
      if (row.caseOid) {
        this.form.caseOid = row.caseOid;
        this.caseOid = row.caseOid;
        getOneMaterialInfo(row.caseOid).then(response => {
          this.addDialogShow = true;
          this.caseMaterialList = response.data;
          for (let material of this.caseMaterialList) {
            this.materialOidsArr.push(material.caseMaterialOid)
          }
        });
        queryCaseReturnByCaseOid(row.caseOid).then(response => {
          if (response.data) {
            this.form = response.data;
            if (this.form.imgInfo != '') {
              this.currStep = '1';
            }
          }

        });
        if (row.applay != "" && row.applay != null && row.applay != undefined && row.applay.applyUserType != "" && row.applay.applyUserType != null && row.applay.applyUserType != undefined) {
          this.getSelectCertificateType(row.applay.applyUserType);
        } else {
          this.getSelectCertificateType(row.applyUserType);
        }
      } else {
        this.addDialogShow = true;
      }
      this.dialogTitle = "办件退件";
    },
    /** 提交按钮 */
    submitForm() {
      this.form.returnStatus = '3';
      this.form.caseOid = this.caseOid;
      this.$refs["form"].validate(valid => {
        if (valid) {
          outReturnFile(this.form).then(response => {
            if (response.code === 200) {
              this.msgSuccess("保存成功");
              this.addDialogShow = false;
              this.form = {};
              this.verifyImg = "";
              this.getList();
              //判断是否是扫描退件
              if (this.tableData && this.tableData.length > 0) {
                this.tableData.forEach((item, i) => {
                  if (item.caseOid == this.form.caseOid) {
                    this.tableData.splice(i, 1);//删除数组
                    this.$forceUpdate();//强制刷新列表
                  }
                })

              }
            }
          });
        }
      })
    },
    //退件告知
    handleInitInfrom (row) {
      this.caseInfo = row;
      if (row.caseOid) {
        this.caseOid = row.caseOid;
        this.form.caseOid = row.caseOid;
        getOneMaterialInfo(row.caseOid).then(response => {
          this.addInfromDialogShow = true;
          this.caseMaterialList = response.data;
          for (let material of this.caseMaterialList) {
            this.materialOidsArr.push(material.caseMaterialOid)
          }
        });
        queryCaseReturnByCaseOid(row.caseOid).then(response => {
          this.form = response.data;
        });
        if (row.applay.applyUserType != "" && row.applay.applyUserType != null && row.applay.applyUserType != undefined) {
          this.getSelectCertificateType(row.applay.applyUserType);
        } else {
          this.getSelectCertificateType(row.applyUserType);
        }
       /* this.getSelectCertificateType(row.applyUserType);*/
      } else {
        this.addInfromDialogShow = true;
      }
      this.dialogTitle = "办件退件告知";
    },
    submitInForm() {

      //this.form.returnStatus = '3';
      this.form.informStatus = '3';
      this.form.caseOid = this.caseOid;
      this.$refs["form"].validate(valid => {
        if (valid) {
          saveOrUpdateInfrom(this.form).then(response => {
            if (response.code === 200) {
              this.msgSuccess("保存成功");
              this.addInfromDialogShow = false;
              this.getList();
            }
          });
        }
      })
    },
    //保存拍照id到退件记录表中
    saveCaseReturn: () => {

      this.form.caseOid = this.caseOid;
      saveCaseReturn(this.form).then(response => {
        if (response.code === 200) {
          let returnOid = response.data.returnOid;
          this.form.returnOid = returnOid;
          this.currStep = '1';
          this.msgSuccess("保存拍照成功");
        }
      });
    },
    closePrint () {
      this.printShow = false;
      this.getList();
    },
    //签名
    signConfirm () {

      if (!this.currStep) {
        this.$message.error("请先进行拍照！");
      } else {
        //签字对接，需要保存

        //保存签字信息主键
        this.form.signInfo = ""; //签名返回的附件主键

      }
    },
    //预览领证回执
    viewReceiveInfo () {
      if (!this.currStep) {
        this.$message.error("请先进行拍照！");
        return;
      }
      // queryCaseReturnByCaseOid(this.caseOid).then(response => {
      // if (response.data) {
      // this.form = response.data;
      // }
      if (this.form.imgInfo) {
        //图片预加载
        querySysAttaByOid(this.form.imgInfo).then(response => {
          this.verifyImg = response.data.fastdfsNginxUrl;
        });
      }
      this.formYl = this.form;
      this.issuedReceiveShow = true;
      // });

    },
    /** 上传附件请求操作 */
    beforeUpload (file) {

      let isRightSize = file.size / 1024 / 1024 < 100
      if (!isRightSize) {
        this.$message.error('文件大小超过 100MB')
      }
      this.fileList.push(file);
      return isRightSize
    },
    base64ToFile (urlData, fileName) {
      let arr = urlData.split(',');
      let bytes = atob(arr[0]); // 解码base64
      let n = bytes.length
      let ia = new Uint8Array(n);
      while (n--) {
        ia[n] = bytes.charCodeAt(n);
      }
      return new File([ia], fileName, {
        type: 'image/jpeg'
      });
    },
    // 接收socket回调函数返回数据的方法
    getConfigResult (data) {

      if (data.status == 0) {
        //扫描
        if (data.device == "HighCamera") {
          if (data.content.Cameras64) {
            let base64s = data.content.Cameras64.split(",");
            for (let i = 0; i < base64s.length; i++) {
              if (!base64s[i]) {
                continue;
              }
              let file = this.base64ToFile(base64s[i], "scanPicture" + i + ".jpg");
              let formD = new FormData();
              formD.append("files", file);
              uploadCaseMaterialFile(formD).then(response => {
                if (response.data != "") {
                  response.data.forEach(data => {
                    this.form.imgInfo = data.attaOid;
                    this.verifyImg = data.fastdfsNginxUrl;
                    this.saveCaseReturn();
                  });
                }
              })
            }
          }
        }
      }
      if (data.status == 1) {
        this.$message.error(data.message)
      }
    },
    //初始化socket发生错误回调
    socketError () {
      this.$message.error("请检查设备或连接是否正常")
    },
    //拍照
    getReceivePhoto () {
      this.visibleDialog = true;
    },

    getPhotoInfo (data) {
      let file = this.base64ToFile(data.returnBase64, Date.now() + ".jpg");
      let formData = new FormData();
      formData.append("files", file);
      uploadCaseMaterialFile(formData).then(response => {
        if (response.data != "") {
          response.data.forEach(data => {
            this.form.imgInfo = data.attaOid;
            this.verifyImg = data.fastdfsNginxUrl;
            this.saveCaseReturn();
          });
        }
      });
    },

    handleBatch () {
      this.batchView = true;
      this.dialogTitle = "退件";
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
        getOneRetuenCaseByCaseNumber(this.batch_caseNumber).then(response => {
          if (response.data.applay != null && response.data.applay != "") {
            this.addRow(response.data);
            this.allSmCaseNum.push(this.batch_caseNumber);
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
      this.tableData.unshift(caseInfo) //追加数据
      this.rowNum += 1;
    },
    //查询所有授权事项
    getRegSxServiceOids () {
      sxServiceOidsListByUserOid().then(respon => {
        if (respon.code === 200) {
          if (respon.data) {
            this.queryParams.serviceOids = respon.data.join(",");
          }
          this.getList();
        }
      })
    },
    //打开扫码枪
    openScan (data) {
      openScanningGun().then(res => {
        if (res.StateCode == 0) {
          //成功后激活扫码枪
          activeScanningGun()
            .then(response => {
              if (response.StateCode == 0) {
                this.$message.success("扫描成功");
                if (data == 0) {
                  this.queryParams.caseNumber = response.data;
                } else if (data == 1) {
                  this.batch_caseNumber = response.data;
                }
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
                if (data == 0) {
                  this.queryParams.caseNumber = response.data;
                } else if (data == 1) {
                  this.batch_caseNumber = response.data;
                }
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
    this.getRegSxServiceOids();
    this.getDistrictTree();
  },
  destroyed: () => {
    //在离开此页面的时候主动关闭socket
   // this.socketApi.webSocketClose();
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
  margin-bottom: 0 !important;
}

.el-form-item--small .el-form-item_content {
  margin-left: 0px !important;
}

.line {
  text-align: center;
}
.el-table-box {
  padding: 20px;
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
