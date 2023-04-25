/**
* 窗口受理管理
* @author: wangxl
* @date: 2020-11-23
*/
<template>
  <div>
    <!-- 步骤 -->
    <div class="process-box" id="print">
      <div class="step-title">
        <span
          v-for="(item, index) in stepData"
          :key="item.index"
          :class="{ active: index == i }"
        >
          {{ item.title }}
        </span>
      </div>
      <!-- 第一步 -->
      <div class="step-content step-first" v-if="show_0">
        <div class="situation-box">
          <h3 class="title"><i class="el-icon-s-grid"></i>情形选择</h3>
          <div class="select-list">
            <span
              v-for="(item, index) in selectData"
              :key="item.index"
              :class="{ current: index == num }"
              @click="selectChange(index, item)"
              >{{ item.title }}</span
            >
          </div>
        </div>
        <div class="option-box">
          <div class="option-title">
            <div class="option-item">
              <i class="el-icon-s-grid"></i>选项信息
            </div>
            <div class="option-item">
              <i class="el-icon-s-grid"></i>
              <div class="chose-item">
                [<span>{{ situationName }}</span
                >]
              </div>
              情景选项信息
            </div>
          </div>
        </div>
        <el-form
          :model="optionForm"
          :rules="rules"
          ref="optionForm"
          label-width="0px"
          class="demo-optionForm"
          :label-position="labelPosition"
        >
          <table
            cellspacing="0"
            cellpadding="0"
            border="0"
            class="el-table__body mt20"
          >
            <colgroup>
              <col width="15%" />
            </colgroup>
            <tr>
              <td><b>选项名称：</b></td>
              <td colspan="3">
                <div v-if="direSituationOptionTitles.length > 0">
                  <div v-for="(data, index) in direSituationOptionTitles" :key='index'>
                    <div class="check-list">
                      <h3>{{ data.titleName }}</h3>
                      <template v-if="data.moreStatus == '0'">
                        <el-radio-group v-model="checkList[index]">
                          <el-radio
                            v-for="item in data.comboOptionVals"
                            :key='item.valOid'
                            :disabled="ifDis"
                            :label="item.valOid"
                            @change="changeTitle(data.titleOid, item.valOid)"
                            >{{ item.name }}</el-radio
                          >
                        </el-radio-group>
                      </template>
                      <template v-if="data.moreStatus == '1'">
                        <el-checkbox-group v-model="checkBoxList">
                          <el-checkbox
                            v-for="item in data.comboOptionVals"
                            :key="item.valOid"
                            :disabled="ifDis"
                            @change="changeTitleBox(item, item.valOid)"
                            :id="item.valOid"
                            :label="item.valOid"
                          >
                            {{ item.name }}</el-checkbox
                          >
                        </el-checkbox-group>
                      </template>
                    </div>
                  </div>
                </div>
                <div v-else>暂无数据</div>
              </td>
            </tr>
          </table>
        </el-form>
        <el-button
          type="primary"
          icon="el-icon-circle-check"
          class="next-btn"
          @click="next(1, 1)"
          >下一步</el-button
        >
      </div>
      <!-- 第二步 -->
      <div class="step-content step-second" v-if="show_1">
        <el-form
          :model="comboDireForm"
          ref="comboDireForm"
          label-width="0px"
          class="demo-comboDireForm"
          :label-position="labelPosition"
        >
          <h3 class="title"><i class="el-icon-s-grid"></i>材料核验列表</h3>
          <div class="data-box">
            <h4>事项基本信息</h4>
            <table
              cellspacing="0"
              cellpadding="0"
              border="0"
              class="el-table__body"
            >
              <colgroup>
                <col width="15%" />
                <col width="35%" />
                <col width="15%" />
                <col width="35%" />
              </colgroup>
              <tr>
                <td><b>一件事目录名称：</b></td>
                <td colspan="3">{{ comboDireForm.comboDirectoryName }}</td>
              </tr>
              <tr>
                <td><b>所属一件事分类：</b></td>
                <td>{{ comboDireForm.themeName }}</td>
                <td><b>所属区划：</b></td>
                <td>{{ comboDireForm.districtName }}</td>
              </tr>
              <tr>
                <td><b>服务对象：</b></td>
                <td>{{ comboDireForm.comboServiceObjectName }}</td>
                <td><b>主办部门：</b></td>
                <td>{{ comboDireForm.mainOrganName }}</td>
              </tr>
              <tr>
                <td><b>协办部门：</b></td>
                <td colspan="3">{{ comboDireForm.assistOrganName }}</td>
              </tr>
              <tr>
                <td><b>线下跑动次数：</b></td>
                <td>{{ comboDireForm.countToScence }}</td>
                <td><b>是否收费：</b></td>
                <td>
                  <span v-if="comboDireForm.ifCharge == 0">否</span>
                  <span v-if="comboDireForm.ifCharge == 1">是</span>
                </td>
              </tr>
              <tr>
                <td><b>承诺时限(工作日)：</b></td>
                <td>{{ comboDireForm.promiseLimit }}</td>
                <td><b>法定时限(工作日)：</b></td>
                <td>{{ comboDireForm.legalLimit }}</td>
              </tr>
            </table>
          </div>
          <!--<div v-for="item in comboDireMaterials">-->
          <div class="data-box">
            <!--<h4>{{ item.situationName }}</h4>-->
            <table
              cellspacing="0"
              cellpadding="0"
              border="0"
              class="data-table"
            >
              <colgroup>
                <col width="10%" />
                <col width="30%" />
                <col width="15%" />
                <col width="15%" />
                <col width="15%" />
              </colgroup>
              <tr>
                <th>序号</th>
                <th>材料名称</th>
                <th>材料类型</th>
                <th>材料形式</th>
                <th>份数</th>
                <th>必要性</th>
              </tr>
              <tbody
                v-if="
                  null != comboDireMaterials && comboDireMaterials.length > 0
                "
              >
                <tr v-for="(data, index) in comboDireMaterials" :key='index'>
                  <td>{{ index + 1 }}</td>
                  <td>{{ data.materialName }}</td>
                  <td>
                    <template>
                      <span v-if="data.materialType == 0">原件</span>
                      <span v-if="data.materialType == 1">复印件</span>
                      <span v-if="data.materialType == 2">原件或者复印件</span>
                    </template>
                  </td>
                  <td>
                    <template>
                      <span v-if="data.materialFormat == 1">纸质</span>
                      <span v-if="data.materialFormat == 2">电子版</span>
                    </template>
                  </td>
                  <td>{{ data.paperNumber }}</td>
                  <td>
                    <template>
                      <span v-if="data.mustFlag == 0">非必须</span>
                      <span v-if="data.mustFlag == 1"
                        ><span class="bage-necessery">必须</span></span
                      >
                      <span v-if="data.mustFlag == 2">容缺后补</span>
                    </template>
                  </td>
                </tr>
              </tbody>
              <tbody v-else>
                <tr>
                  <td colspan="6">暂无数据</td>
                </tr>
              </tbody>
            </table>
          </div>
          <!--</div>-->
          <div class="btn-wrap">
            <el-checkbox v-model="radio" label="1" @change="checkedBox"
              >我已核验上述材料并确定齐全</el-checkbox
            >
            <div class="btn-list mt10">
              <el-button type="primary" icon="el-icon-back" @click="next(0, -1)"
                >上一步</el-button
              >
              <el-button
                type="primary"
                icon="el-icon-circle-check"
                @click="next(2, 1)"
                >下一步</el-button
              >
            </div>
          </div>
        </el-form>
      </div>
      <!-- 第三步 -->
      <div class="step-content step-third" v-if="show_2">
        <el-form
          ref="ruleForm"
          :model="ruleForm"
          :rules="rules"
          label-width="0px"
          class="demo-ruleForm"
          :label-position="labelPosition"
        >
          <div class="step-third-box">
            <h3 class="title">
              <i class="el-icon-s-grid"></i>申请人/申请单位信息
            </h3>
            <el-button
              type="primary"
              @click="scanCard('0')"
              style="float: right"
              v-if="cegisterType == 0"
              >✚ 扫描身份证
            </el-button>
            <table
              cellspacing="0"
              cellpadding="0"
              border="0"
              class="el-table__body"
            >
              <colgroup>
                <col width="15%" />
                <col width="35%" />
                <col width="15%" />
                <col width="35%" />
              </colgroup>
              <el-input v-show="false" v-model="ruleForm.id" />
              <el-input v-show="false" v-model="ruleForm.caseOid" />
              <el-input v-show="false" v-model="ruleForm.caseNumber" />
              <el-input v-show="false" v-model="ruleForm.delFlag" />
              <el-input v-show="false" v-model="ruleForm.createUserOid" />
              <el-input v-show="false" v-model="ruleForm.applyUserType" />
              <el-input v-show="false" v-model="ruleForm.caseStatus" />
              <el-input v-show="false" v-model="ruleForm.comboDireOid" />
              <el-input v-show="false" v-model="ruleForm.comboDireMaterials" />
              <el-input v-show="false" v-model="ruleForm.situationOid" />
              <el-input v-show="false" v-model="ruleForm.valList" />
              <tr>
                <td>
                  <i class="require">*</i>
                  <span v-if="cegisterType == 0">
                    <b>申请人姓名：</b>
                  </span>
                  <span v-else>
                    <b>申请单位名称：</b>
                  </span>
                </td>
                <td colspan="3">
                  <el-form-item prop="applyUserName" style="width: 70%">
                    <el-col :span="24">
                      <el-input
                        v-model.trim="ruleForm.applyUserName"
                        name="applyUserName"
                        maxlength="50"
                        show-word-limit
                      ></el-input>
                    </el-col>
                  </el-form-item>
                  <el-button
                    type="primary"
                    @click="getUserInfo(ruleForm.credentialNumber)"
                    style="float: right; margin-top: -36px"
                    >✚ 提取申请人信息</el-button
                  >
                </td>
              </tr>
              <tr>
                <td><i class="require">*</i><b>证件类型：</b></td>
                <td>
                  <el-form-item prop="credentialType">
                    <el-col :span="24">
                      <el-select
                        v-model="ruleForm.credentialType"
                        placeholder="请选择证件类型"
                        @change="changeType"
                      >
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
                <td><i class="require">*</i><b>证件号码：</b></td>
                <td>
                  <el-form-item prop="credentialNumber">
                    <el-col :span="24">
                      <el-input
                        v-model="ruleForm.credentialNumber"
                        name="credentialNumber"
                        maxlength="20"
                        show-word-limit
                      ></el-input>
                    </el-col>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td>
                  <i class="require">*</i>
                  <span v-if="cegisterType == 0">
                    <b>申请人手机：</b>
                  </span>
                  <span v-else>
                    <b>申请单位手机：</b>
                  </span>
                </td>
                <td>
                  <el-form-item prop="applyUserPhone">
                    <el-col :span="24">
                      <el-input
                        v-model="ruleForm.applyUserPhone"
                        name="applyUserPhone"
                        maxlength="11"
                        show-word-limit
                      >
                      </el-input>
                    </el-col>
                  </el-form-item>
                </td>
                <td>
                  <i class="require">*</i>
                  <span v-if="cegisterType == 0">
                    <b>申请人电话：</b>
                  </span>
                  <span v-else>
                    <b>申请单位电话：</b>
                  </span>
                  <br />(格式：xxxx-xxxxxxxx)
                </td>
                <td>
                  <el-form-item prop="applyUserTel">
                    <el-col :span="24">
                      <el-input
                        v-model.trim="ruleForm.applyUserTel"
                        name="applyUserTel"
                        maxlength="13"
                        show-word-limit
                      >
                      </el-input>
                    </el-col>
                  </el-form-item>
                </td>
              </tr>
              <tr v-if="cegisterType == 0">
                <td><b>通讯地址：</b></td>
                <td colspan="3">
                  <el-form-item prop="applyUserAddress">
                    <el-col :span="24">
                      <el-input
                        v-model.trim="ruleForm.applyUserAddress"
                        name="applyUserAddress"
                        maxlength="100"
                        show-word-limit
                      ></el-input>
                    </el-col>
                  </el-form-item>
                </td>
              </tr>
              <tr v-else>
                <td><i class="require">*</i><b>法定代表人：</b></td>
                <td colspan="3">
                  <el-form-item prop="legalPersonName">
                    <el-col :span="24">
                      <el-input
                        v-model.trim="ruleForm.legalPersonName"
                        name="legalPersonName"
                        maxlength="50"
                        show-word-limit
                      ></el-input>
                    </el-col>
                  </el-form-item>
                </td>
              </tr>
            </table>
          </div>
          <div class="step-third-box">
            <h3 class="title"><i class="el-icon-s-grid"></i>办件信息</h3>
            <table
              cellspacing="0"
              cellpadding="0"
              border="0"
              class="el-table__body"
            >
              <colgroup>
                <col width="15%" />
                <col width="35%" />
                <col width="15%" />
                <col width="35%" />
              </colgroup>
              <tr>
                <td><i class="require">*</i><b>申请项目名称：</b></td>
                <td colspan="3">
                  <el-form-item prop="projectName">
                    <el-col :span="24">
                      <el-input
                        v-model.trim="ruleForm.projectName"
                        name="projectName"
                        maxlength="100"
                        show-word-limit
                      >
                      </el-input>
                    </el-col>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td><i class="require">*</i><b>业务管辖地：</b></td>
                <td>
                  <el-form-item prop="bussVenueDistrictOidChoose">
                    <el-col :span="24">
                      <treeselect
                        :multiple="true"
                        :options="districtOptions"
                        :flat="true"
                        :default-expand-level="1"
                        placeholder="请选择业务管辖地"
                        v-model="ruleForm.bussVenueDistrictOidChoose"
                        append-to-body
                      />
                    </el-col>
                  </el-form-item>
                </td>
                <td><i class="require">*</i><b>受理具体地点：</b></td>
                <td>
                  <el-form-item prop="specificLocation">
                    <el-col :span="24">
                      <el-input
                        v-model.trim="ruleForm.specificLocation"
                        name="specificLocation"
                        maxlength="200"
                        show-word-limit
                      ></el-input>
                    </el-col>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td><b>投资项目名称：</b></td>
                <td>
                  <el-form-item prop="investProjecName">
                    <el-col :span="24">
                      <el-input
                        v-model.trim="ruleForm.investProjecName"
                        name="investProjecName"
                        maxlength="200"
                        show-word-limit
                      ></el-input>
                    </el-col>
                  </el-form-item>
                </td>
                <td><b>投资项目编号：</b></td>
                <td>
                  <el-form-item prop="investProjectCode">
                    <el-col :span="24">
                      <el-input
                        v-model.trim="ruleForm.investProjectCode"
                        name="investProjectCode"
                        maxlength="50"
                        show-word-limit
                      ></el-input>
                    </el-col>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td><b>摘要内容：</b></td>
                <td colspan="3">
                  <el-form-item prop="projectAbstract">
                    <el-col :span="24">
                      <el-input
                        type="textarea"
                        v-model.trim="ruleForm.projectAbstract"
                        name="projectAbstract"
                        maxlength="500"
                        show-word-limit
                      ></el-input>
                    </el-col>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td><i class="require">*</i><b>邮政编码：</b></td>
                <td>
                  <el-form-item prop="applyPostCode">
                    <el-col :span="24">
                      <el-input
                        v-model.trim="ruleForm.applyPostCode"
                        name="applyPostCode"
                        maxlength="6"
                        show-word-limit
                      ></el-input>
                    </el-col>
                  </el-form-item>
                </td>
                <td><i class="require">*</i><b>是否为代理人：</b></td>
                <td>
                  <el-radio-group
                    v-model="ruleForm.proxyFlag"
                    @change="changeProxyFlag"
                  >
                    <el-radio :label="1">是</el-radio>
                    <el-radio :label="0">否</el-radio>
                  </el-radio-group>
                </td>
              </tr>
            </table>
          </div>

          <!--&lt;!&ndash; 代理人信息 &ndash;&gt;-->
          <div class="step-third-box" v-if="proxy_show">
            <h3 class="title">
              <i class="el-icon-s-grid"></i>联系人/代理人相关信息
            </h3>
            <el-button
              type="primary"
              @click="scanCard('1')"
              style="float: right"
              >✚ 扫描身份证</el-button
            >
            <table
              cellspacing="0"
              cellpadding="0"
              border="0"
              class="el-table__body"
            >
              <colgroup>
                <col width="15%" />
                <col width="35%" />
                <col width="15%" />
                <col width="35%" />
              </colgroup>
              <tr>
                <td><i class="require">*</i><b>联系人/代理人姓名：</b></td>
                <td colspan="3">
                  <el-form-item prop="contactUserName">
                    <el-col :span="24">
                      <el-input
                        v-model.trim="ruleForm.contactUserName"
                        name="contactUserName"
                        maxlength="50"
                        show-word-limit
                      ></el-input>
                    </el-col>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td>
                  <i class="require">*</i><b>联系人/代理人身份证号码：</b>
                </td>
                <td>
                  <el-form-item prop="contactCredentialNumber">
                    <el-col :span="24">
                      <el-input
                        v-model.trim="ruleForm.contactCredentialNumber"
                        name="contactCredentialNumber"
                        maxlength="18"
                        show-word-limit
                      ></el-input>
                    </el-col>
                  </el-form-item>
                </td>
                <td><b>电子邮箱：</b></td>
                <td>
                  <el-form-item prop="contactEmail">
                    <el-col :span="24">
                      <el-input
                        v-model.trim="ruleForm.contactEmail"
                        name="contactEmail"
                        maxlength="25"
                        show-word-limit
                      >
                      </el-input>
                    </el-col>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td><i class="require">*</i><b>手机号：</b></td>
                <td>
                  <el-form-item prop="contactUserPhone">
                    <el-col :span="24">
                      <el-input
                        v-model.trim="ruleForm.contactUserPhone"
                        name="contactUserPhone"
                        maxlength="11"
                        show-word-limit
                      ></el-input>
                    </el-col>
                  </el-form-item>
                </td>
                <td><b>固定电话：</b></td>
                <td>
                  <el-form-item prop="contactUserTel">
                    <el-col :span="24">
                      <el-input
                        v-model.trim="ruleForm.contactUserTel"
                        name="contactUserTel"
                        maxlength="13"
                        show-word-limit
                      ></el-input>
                    </el-col>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td><b>备注：</b></td>
                <td colspan="3">
                  <el-form-item prop="contactRemark">
                    <el-col :span="24">
                      <el-input
                        type="textarea"
                        v-model.trim="ruleForm.contactRemark"
                        name="contactRemark"
                        maxlength="500"
                        show-word-limit
                      ></el-input>
                    </el-col>
                  </el-form-item>
                </td>
              </tr>
            </table>
          </div>
          <!-- &lt;!&ndash; 送达信息 &ndash;&gt;-->
          <div class="step-third-box">
            <h3 class="title"><i class="el-icon-s-grid"></i>送达信息</h3>
            <table
              cellspacing="0"
              cellpadding="0"
              border="0"
              class="el-table__body"
            >
              <colgroup>
                <col width="15%" />
                <col width="35%" />
                <col width="15%" />
                <col width="35%" />
              </colgroup>
              <tr>
                <td><i class="require">*</i><b>送达方式：</b></td>
                <td colspan="3">
                  <el-radio-group
                    v-model="ruleForm.resultDeliveryWay"
                    @change="changeDeliveryWay"
                  >
                    <el-radio label="1">快递送达</el-radio>
                    <el-radio label="2">自行取件</el-radio>
                    <el-radio label="3">其他</el-radio>
                  </el-radio-group>
                </td>
              </tr>
              <tr v-if="address_show">
                <td><i class="require">*</i><b>收件人名称：</b></td>
                <td>
                  <el-form-item
                    prop="addresseeName"
                    style="float: left; margin-bottom: 2px; width: 76%"
                  >
                    <el-col :span="24">
                      <el-input
                        v-model.trim="ruleForm.addresseeName"
                        name="addresseeName"
                        maxlength="50"
                        show-word-limit
                      ></el-input>
                    </el-col>
                  </el-form-item>
                  <el-button
                    type="primary"
                    @click="getApplyInfo"
                    style="margin-left: 10px"
                    >提取</el-button
                  >
                </td>
                <td><i class="require">*</i><b>收件人邮政编码：</b></td>
                <td>
                  <el-form-item prop="addresseePostCode">
                    <el-col :span="24">
                      <el-input
                        v-model.trim="ruleForm.addresseePostCode"
                        name="addresseePostCode"
                        maxlength="6"
                        show-word-limit
                      ></el-input>
                    </el-col>
                  </el-form-item>
                </td>
              </tr>
              <tr v-if="address_show">
                <td><i class="require">*</i><b>收件人手机：</b></td>
                <td>
                  <el-form-item prop="addresseePhone">
                    <el-col :span="24">
                      <el-input
                        v-model.trim="ruleForm.addresseePhone"
                        name="addresseePhone"
                        maxlength="11"
                        show-word-limit
                      ></el-input>
                    </el-col>
                  </el-form-item>
                </td>
                <td><b>收件人电话：</b></td>
                <td>
                  <el-form-item prop="addresseeTel">
                    <el-col :span="24">
                      <el-input
                        v-model.trim="ruleForm.addresseeTel"
                        name="addresseeTel"
                        maxlength="13"
                        show-word-limit
                      >
                      </el-input>
                    </el-col>
                  </el-form-item>
                </td>
              </tr>
              <tr v-if="address_show">
                <td><i class="require">*</i><b>收件人地址：</b></td>
                <td>
                  <el-form-item prop="addresseeAddress">
                    <el-col :span="24">
                      <el-cascader
                        size="large"
                        :options="provinceCityOptions"
                        v-model="ruleForm.addresseeAddress"
                        @change="handleChange"
                      >
                      </el-cascader>
                    </el-col>
                  </el-form-item>
                </td>
                <td><i class="require">*</i><b>收件人详细地址：</b></td>
                <td>
                  <el-form-item prop="addresseeDetailAddress">
                    <el-col :span="24">
                      <el-input
                        v-model.trim="ruleForm.addresseeDetailAddress"
                        name="addresseeDetailAddress"
                        maxlength="500"
                        show-word-limit
                      ></el-input>
                    </el-col>
                  </el-form-item>
                </td>
              </tr>
            </table>
          </div>
          <div class="step-third-box" v-if="formConfig_show">
            <h3 class="title"><i class="el-icon-s-grid"></i>表单信息</h3>
            <table
              cellspacing="0"
              cellpadding="0"
              border="0"
              class="el-table__body"
            >
              <colgroup>
                <col width="15%" />
              </colgroup>
              <tr>
                <td><b>填写表单：</b></td>
                <td colspan="3">
                  <el-row :gutter="10">
                    <el-col
                      :span="12"
                      class="mb10"
                      v-for="(dataForm, i) in sxSerFormList"
                      :key='i'
                    >
                      <div class="form-box-inline">
                        <h4>1、{{ dataForm.formName }}</h4>
                        <div class="form-btn-group" v-if="tempFormDataId[i]">
                          <el-button
                            icon="el-icon-view"
                            class="btn"
                            @click="viewFormFilling(tempFormDataId[i], i)"
                            >表单预览
                          </el-button>
                          <el-button
                            icon="el-icon-edit-outline"
                            class="btn"
                            @click="formFilling(dataForm, i)"
                            >表单修改
                          </el-button>
                        </div>
                        <el-button
                          type="primary"
                          icon="el-icon-edit-outline"
                          v-if="!tempFormDataId[i]"
                          class="btn-write"
                          @click="formFilling(dataForm, i)"
                          >填写表单</el-button
                        >
                        <ul>
                          <li>
                            <div class="icon bdbm-icon">
                              <i class="el-icon-document"></i>
                            </div>
                            <div class="input-text">
                              <h4>表单编码：</h4>
                              <p
                                v-if="dataForm.formCode"
                                :title="dataForm.formCode"
                              >
                                {{ dataForm.formCode }}
                              </p>
                              <p v-if="!dataForm.formCode">暂无</p>
                            </div>
                          </li>
                          <li>
                            <div class="icon bdsm-icon">
                              <i class="el-icon-warning-outline"></i>
                            </div>
                            <div class="input-text">
                              <h4>表单说明：</h4>
                              <p
                                v-if="dataForm.formText"
                                :title="dataForm.formText"
                              >
                                {{ dataForm.formText }}
                              </p>
                              <p v-if="!dataForm.formText">暂无</p>
                            </div>
                          </li>
                          <li>
                            <div
                              class="icon bdsm-icon"
                              style="background-color: #868380"
                            >
                              <i class="el-icon-download"></i>
                            </div>
                            <div class="input-text">
                              <h4>表单样本：</h4>
                              <p
                                v-if="dataForm.simpleAtta"
                                :title="dataForm.attaName"
                              >
                                {{ dataForm.attaName
                                }}<span
                                  style="cursor: pointer"
                                  @click="downloadFile(dataForm.simpleAtta)"
                                  >下载
                                </span>
                              </p>
                              <p v-if="!dataForm.simpleAtta">暂无</p>
                            </div>
                          </li>
                        </ul>
                      </div>
                    </el-col>
                  </el-row>
                </td>
              </tr>
            </table>
          </div>
          <div class="btn-wrap">
            <div class="btn-list mt10">
              <el-button type="primary" @click="getImageCamera"
                >人证比对</el-button
              >
              <el-button type="primary" icon="el-icon-back" @click="next(1, -1)"
                >上一步</el-button
              >
              <el-button
                type="info"
                icon="el-icon-video-pause"
                :disabled="isDisable"
                class="print-btn"
                @click="saveComboCaseForm('')"
                >暂存</el-button
              >
              <el-button
                type="primary"
                icon="el-icon-circle-check"
                @click="next(3, 1)"
                >下一步</el-button
              >
            </div>
          </div>
        </el-form>
      </div>
      <!-- 第四步 -->
      <div class="step-content step-fourth" v-if="show_3">
        <el-button
          type="primary"
          :loading="false"
          @click="clzs(4, 1)"
          class="data-btn"
          >材料智审</el-button
        >
        <h3 class="title"><i class="el-icon-s-grid"></i>申请者相关信息</h3>
        <el-form
          :model="materialForm"
          :rules="rules"
          ref="materialForm"
          label-width="0px"
          class="demo-materialForm"
          :label-position="labelPosition"
        >
          <table cellspacing="0" cellpadding="0" border="0" class="data-table">
            <colgroup>
              <col width="5%" />
              <col width="31%" />
              <col width="15%" />
              <col width="5%" />
              <col width="44%" />
            </colgroup>
            <tr>
              <th>序号</th>
              <th>材料名称</th>
              <th>材料类型</th>
              <th>份数</th>
              <!--<th>材料样本</th>-->
              <th>操作</th>
            </tr>
            <tbody v-for="(item, index) in comboCaseMaterials" :key='index'>
              <tr>
                <td>{{ index + 1 }}</td>
                <td>{{ item.materialName }}</td>
                <td>
                  <template>
                    <span v-if="item.materialType == '0'">原件</span>
                    <span v-if="item.materialType == '1'">复印件</span>
                    <span v-if="item.materialType == '2'">原件或者复印件</span>
                  </template>
                </td>
                <td>{{ item.paperNumber }}</td>
                <td>
                  <el-radio
                    label="1"
                    v-model="radio1[item.caseMaterialOid]"
                    @change="
                      chooseCollectionType('1', item, item.caseMaterialOid)
                    "
                    >纸质收取</el-radio
                  >
                  <el-radio
                    label="2"
                    v-model="radio1[item.caseMaterialOid]"
                    @change="
                      chooseCollectionType('2', item, item.caseMaterialOid)
                    "
                    >附件上传</el-radio
                  >
                  <el-radio
                    label="3"
                    v-model="radio1[item.caseMaterialOid]"
                    @change="
                      chooseCollectionType('3', item, item.caseMaterialOid)
                    "
                    >扫描</el-radio
                  >
                  <el-radio
                    label="5"
                    v-model="radio1[item.caseMaterialOid]"
                    @change="
                      chooseCollectionType('5', item, item.caseMaterialOid)
                    "
                    >证照库</el-radio
                  >
                  <el-radio
                    label="4"
                    v-if="dishonestFlag && item.comboDireMaterial.mustFlag == 2"
                    v-model="radio1[item.caseMaterialOid]"
                    @change="
                      chooseCollectionType('4', item, item.caseMaterialOid)
                    "
                    >容缺后补</el-radio
                  >
                </td>
              </tr>
              <tr v-show="show_upload[item.caseMaterialOid]">
                <td colspan="5">
                  <div class="handle-data">
                    <el-row class="right-btn-group">
                      <el-button
                        type="primary"
                        icon="el-icon-document"
                        @click="openMaterialComparison(item)"
                        size="mini"
                        >材料比对</el-button
                      >
                    </el-row>
                    <el-row class="right-btn-group-two">
                      <el-form
                        ref="formForm"
                        :model="formData"
                        :rules="rules"
                        size="medium"
                        label-width="100px"
                      >
                        <el-form-item>
                          <el-upload
                            class="upload-demo"
                            action=""
                            multiple
                            :limit="5"
                            :data="item"
                            :http-request="uploadFiles"
                            :before-upload="beforeUpload"
                            :on-error="uploadError"
                            :show-file-list="showFileList"
                            accept="accept"
                          >
                            <el-button
                              size="mini"
                              type="primary"
                              icon="el-icon-upload"
                              >点击上传</el-button
                            >
                          </el-upload>
                        </el-form-item>
                      </el-form>
                    </el-row>
                    <ul v-for="(data,index) in item.attaList" :key='index'>
                      <li>
                        <el-row :gutter="24">
                          <el-col :span="9">
                            <div class="grid-content qdcg-text">
                              <img src="../../../../assets/image/icons05.png" />
                              <p :title="data.originName">
                                {{ data.originName }}
                              </p>
                            </div>
                          </el-col>
                          <el-col
                            :span="5"
                            v-if="
                              data.extensionName &&
                              (data.extensionName.indexOf('png') > -1 ||
                                data.extensionName.indexOf('jpg') > -1)
                            "
                          >
                            <span v-for="catalog in item.materialCatalogList" :key='catalog.materialCatalogOid'>
                              <el-radio
                                :label="catalog.materialCatalogOid"
                                v-model="data.materialCatalogOid"
                                @change="
                                  chooseCatalog(
                                    item.caseMaterialOid,
                                    data.oid,
                                    catalog.materialCatalogOid
                                  )
                                "
                                >{{ catalog.catalogName }}
                              </el-radio>
                            </span>
                          </el-col>
                          <el-col :span="5">
                            <div class="grid-content qdcg-btn">
                              <el-button
                                type="text"
                                icon="el-icon-view"
                                @click="viewFile(data.oid)"
                              ></el-button>
                              <el-button
                                type="text"
                                icon="el-icon-delete"
                                @click="deleteFile(item, data)"
                              ></el-button>
                            </div>
                          </el-col>
                        </el-row>
                      </li>
                    </ul>
                  </div>
                </td>
              </tr>
              <tr v-show="show_scan[item.caseMaterialOid]">
                <td colspan="5">
                  <div class="handle-data">
                    <el-row class="right-btn-group-two">
                      <el-form
                        ref="formForm"
                        :model="formData"
                        :rules="rules"
                        size="medium"
                        label-width="100px"
                      >
                        <el-button
                          size="mini"
                          type="primary"
                          icon="el-icon-upload"
                          @click="
                            scanPicture(item.caseMaterialOid, item.oid, item)
                          "
                          >点击扫描</el-button
                        >
                      </el-form>
                    </el-row>
                    <ul v-for="(data,index) in item.attaList" :key='index'>
                      <li>
                        <el-row :gutter="24">
                          <el-col :span="9">
                            <div class="grid-content qdcg-text">
                              <img src="../../../../assets/image/icons05.png" />
                              <p :title="data.originName">
                                {{ data.originName }}
                              </p>
                            </div>
                          </el-col>
                          <el-col :span="5">
                            <div class="grid-content qdcg-btn">
                              <el-button
                                type="text"
                                icon="el-icon-view"
                                @click="viewFile(data.oid)"
                              ></el-button>
                              <el-button
                                type="text"
                                icon="el-icon-delete"
                                @click="deleteFile(item, data)"
                              ></el-button>
                            </div>
                          </el-col>
                        </el-row>
                      </li>
                    </ul>
                  </div>
                </td>
              </tr>
              <tr v-show="show_elem[item.caseMaterialOid]">
                <td colspan="5">
                  <div class="handle-data">
                    <el-row class="right-btn-group-two">
                      <el-button
                        v-if="item.sourceType == 1"
                        size="mini"
                        type="primary"
                        icon="el-icon-upload"
                        @click="
                          getElecLicenInfo(
                            item.caseMaterialOid,
                            item.materialOid,
                            '',
                            index
                          )
                        "
                        >获取证照</el-button
                      >
                      <el-button
                        v-if="item.sourceType == 0"
                        size="mini"
                        type="primary"
                        icon="el-icon-upload"
                        @click="
                          getElecLicenInfo(
                            item.caseMaterialOid,
                            item.materialOid,
                            item.comboDireMaterial.elecBillOid,
                            index
                          )
                        "
                        >获取证照</el-button
                      >
                    </el-row>
                    <ul v-for="data in attaList[index]" :key='data.attaOid'>
                      <li>
                        <el-row :gutter="24">
                          <el-col :span="9">
                            <div class="grid-content qdcg-text">
                              <p :title="data.originName">
                                {{ data.originName }}
                              </p>
                            </div>
                          </el-col>
                          <el-col :span="5">
                            <div class="grid-content qdcg-btn">
                              <el-button
                                type="text"
                                icon="el-icon-view"
                                @click="viewElemsInfo(data.attaOid)"
                              >
                              </el-button>
                            </div>
                          </el-col>
                        </el-row>
                      </li>
                    </ul>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
          <div class="step-third-box" v-if="rqhb_show">
            <h3 class="title"><i class="el-icon-s-grid"></i>容缺补正</h3>
            <table
              cellspacing="0"
              cellpadding="0"
              border="0"
              class="el-table__body"
            >
              <colgroup>
                <col width="15%" />
                <col width="35%" />
                <col width="15%" />
                <col width="35%" />
              </colgroup>
              <tr>
                <td><i class="require">*</i><b>容缺补正到期时间：</b></td>
                <td colspan="3">
                  <el-form-item prop="rqhbTime">
                    <el-date-picker
                      v-model="materialForm.rqhbTime"
                      :picker-options="optionDate"
                      type="date"
                      value-format="yyyy-MM-dd"
                      placeholder="选择到期时间"
                    >
                    </el-date-picker>
                  </el-form-item>
                </td>
              </tr>
            </table>
          </div>
          <div class="btn-wrap">
            <div class="btn-list mt10">
              <el-button type="primary" icon="el-icon-back" @click="next(2, -1)"
                >上一步</el-button
              >
              <el-button type="primary" icon="el-icon-right" @click="next(4, 1)"
                >下一步</el-button
              >
            </div>
          </div>
        </el-form>
      </div>
      <!-- 第五步 -->
      <div class="step-content step-last" v-if="show_4">
        <h3 class="title"><i class="el-icon-s-grid"></i>申请者相关信息</h3>
        <el-form
          :model="acceptForm"
          :rules="rules"
          ref="acceptForm"
          label-width="0px"
          class="demo-acceptForm"
          :label-position="labelPosition"
        >
          <table
            cellspacing="0"
            cellpadding="0"
            border="0"
            class="el-table__body"
          >
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <el-input v-show="false" v-model="acceptForm.caseOid" />
            <el-input v-show="false" v-model="acceptForm.valList" />
            <!--<tr>
              <td><b>审批部门：</b></td>
              <td colspan="3">{{loginUser.organName}}</td>
            </tr>-->
            <tr>
              <td><b>是否受理：</b></td>
              <td colspan="3">
                <el-form-item prop="ifAccept">
                  <el-radio-group v-model="acceptForm.ifAccept">
                    <el-radio :label="1">受理通过</el-radio>
                    <el-radio :label="2">不予受理</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <i class="require" v-if="acceptForm.ifAccept == 2">*</i
                ><b>意见说明：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="acceptOpinionDesc">
                  <el-col :span="24">
                    <el-input
                      type="textarea"
                      v-model.trim="acceptForm.acceptOpinionDesc"
                      name="acceptOpinionDesc"
                      maxlength="200"
                      show-word-limit
                    ></el-input>
                  </el-col>
                </el-form-item>
              </td>
            </tr>
          </table>
          <div class="btn-wrap">
            <div class="btn-list mt10">
              <el-button
                type="primary"
                icon="el-icon-circle-check"
                @click="next(3, -1)"
                >上一步</el-button
              >
              <el-button
                type="primary"
                icon="el-icon-video-pause"
                @click="pushPjCaseService"
                >受理</el-button
              >
            </div>
          </div>
        </el-form>
      </div>
    </div>
    <!--</el-dialog>-->

    <!--表单嵌套页面-->
    <el-dialog
      title="电子表单"
      :visible.sync="iframeState"
      width="80%"
      height="100%"
      v-if="iframeState"
      :modal-append-to-body="true"
      append-to-body
    >
      <iframe-url :outLink="iframeUrl" @closeIframe="closeIframe"></iframe-url>
    </el-dialog>

    <el-dialog
      title="电子表单预览"
      :visible.sync="iframVieweState"
      v-if="iframVieweState"
      width="80%"
      height="100%"
      :modal-append-to-body="true"
      append-to-body
    >
      <iframe-url-view
        :outLink="iframeUrlView"
        @closeIframeView="closeIframeView"
      ></iframe-url-view>
    </el-dialog>

    <el-dialog
      v-dialog-drag
      :visible.sync="item.show"
      v-for="(item,idx) in materialComparisonOptions"
      :key='idx'
      @close="outerVisible"
      :title="item.title"
      width="80%"
      height="700px"
      append-to-body
    >
      <!--  <iframe :src=fileViewurlList  rameborder="0" width="100%" height="600px"  @father-click="closeFileView"  ></iframe>-->
      <el-scrollbar style="height: 650px">
        <package-material-comparison
          :sampleInfoOid="item.sampleInfoOid"
          :comboDirectoryOid="item.comboDirectoryOid"
          :materiaOid="item.materiaOid"
          :caseOid="item.caseOid"
          :caseMaterialOid="item.caseMaterialOid"
          :attaOids="item.attaOids"
          :attaLists="item.attaLists"
          :fileViewurl="item.fileViewurl"
          :title="item.title"
          @father-click="openTempletePic"
        >
        </package-material-comparison>
      </el-scrollbar>
    </el-dialog>
    <!--引入文件的预览弹出层-->
    <el-dialog
      v-dialog-drag
      title="文件预览"
      :visible.sync="view.show"
      v-for="(view,index) in viewDialogOptions"
      :key='index'
      @close="closeFileView"
      width="60%"
      append-to-body
    >
      <combo-case-file-view
        :attaOid="view.attaOid"
        @father-click="closeFileView"
      ></combo-case-file-view>
    </el-dialog>

    <!--套餐智审结果-->
    <el-dialog
      v-dialog-drag
      :visible.sync="item.show"
      v-for="(item,idx) in comboIntelligentPretrialOptions"
      :key='idx'
      @close="outerVisible"
      :title="item.title"
      width="50%"
      append-to-body
    >
      <el-scrollbar style="height: 500px">
        <combo-intelligent-pretrial
          :caseOid="item.caseOid"
          :caseMaterialOid="item.caseMaterialOid"
          :caseFileRecOid="item.caseFileRecOid"
          :cataOid="item.cataOid"
          :title="item.title"
          @father-click="handleChildView"
        ></combo-intelligent-pretrial>
      </el-scrollbar>
    </el-dialog>

    <!-- 获取申请人信息-->
    <el-dialog
      v-dialog-drag
      :visible.sync="userInfoShow"
      v-if="userInfoShow"
      title="申请人信息列表"
      width="60%"
      append-to-body
    >
      <el-scrollbar style="height: 500px">
        <apply-user-onething-list
          :applyCardNum="applyCardNum"
          @selectUserOk="closeUserInfoShow"
        >
        </apply-user-onething-list>
      </el-scrollbar>
    </el-dialog>

    <!-- 人证比对 -->
    <div v-if="dialogVisible">
      <el-dialog
        :visible.sync="dialogVisible"
        width="80%"
        :before-close="handleClose"
        :close-on-click-modal="false"
        append-to-body
      >
        <el-scrollbar>
          <hardware-scan ref="scanForm"></hardware-scan>
        </el-scrollbar>
      </el-dialog>
    </div>
  </div>
</template>
<script>
import IframeUrl from "@/views/iframe/formIndex";
import IframeUrlView from "@/views/iframe/formIndexView";
import {
  getSituationList,
  getSituationOpinionList,
  queryComboOptionTitleByValOid,
  getComboDireMaterialList,
  getCertificateType,
  saveComboCase,
  uploadCaseMaterialFile,
  saveCaseMaterialAtta,
  getComboCaseByOid,
  saveCaseAccpet,
  changeCredentialType,
  pushPbpjCheckLogin,
  regComboCaseInfo,
  pushPbpjUser,
  comboCaseCallBack,
  blockSituationOptionsInfo,
  getloginUser, pbpjSaveCaseInfo, pushPbpjInfo,
} from "@/api/onething/comboManager/comboAccept/initComboCase";
import {
  getOne
} from "@/api/onething/sxpz/comboDirectory";
import {
  delFile
} from "@/api/onething/comboManager/comboAccept/comboCaseAtta";
import {
  queryDistrictSimpleTree
} from "@/api/sys/district";
import {
  validatePhone,
  validateTel,
  validIDCard,
  validatePostCode,
  validateEmails,
  validUnifiedCredit
} from '@/utils/validate';
import comboCaseFileView from "@/views/onething/comboManager/comboAccept/comboCaseFileView";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {
  VueCropper
} from "vue-cropper";
import packageMaterialComparison from '@/views/onething/clzs/materialCheckKeyManage/packageMaterialComparison';
import {
  selectBySxSerForm,
  saveOrUpdateComboCaseForm
} from "@/api/zc/businessManagement/formConfig";
import {
  getElecLicenUrl,
  queryElecLicenseByDirCode
} from "@/api/zc/businessManagement/elemLice";
import {
  regionData
} from 'element-china-area-data'
import {
  checkApplyUserInDishonest
} from "@/api/onething/comboManager/comboAccept/combocaseRqbz";
import comboIntelligentPretrial from '@/views/onething/clzs/directoryManagement/comboIntelligentPretrial';
import {
  intelligentPretrial,
  intelligentPretrialmaterialPrePrial,
  viewResult
} from "@/api/onething/comboManager/comboAccept/comboIntelligentPretrial";
import applyUserOnethingList from '@/views/onething/comboManager/comboAccept/applyUserOnethingList';
import { importScript } from '../../../../api/sys/util'
import hardwareScan from "@/views/common/hardwareScan";
export default {
  inheritAttrs: false,
  components: {
    Treeselect,
    VueCropper,
    packageMaterialComparison,
    IframeUrl,
    IframeUrlView,
    comboCaseFileView,
    comboIntelligentPretrial, applyUserOnethingList, hardwareScan
  },
  name: "comboCaseInfo",
  //定义获取父类传过来值的格式
  props: ["comboDireOid", "pCegisterType"],
  data () {
    return {
      provinceCityOptions: regionData,
      i: 0,
      num: 0,
      num1: 1,
      num2: 2,
      show_0: true,
      show_1: false,
      show_2: false,
      show_3: false,
      show_4: false,
      rightShow: true,
      isDisable: false,
      warnFlag: true, //硬件设备提示标识
      address_show: true,
      proxy_show: false,
      compare_show: false,
      ifDis: false,
      rqhb_show: false,
      show_upload: [],
      show_scan: [],
      scan_item: {},
      viewDialogOptions: [],
      show_elem: [],
      radio: "",
      radio1: [],
      radio2: "1",
      //材料比对页面
      materialComparisonOptions: [],
      //材料智审
      comboIntelligentPretrialOptions: [],
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 事项情形数据
      situationList: [],
      // 事项情形与选项关系数据
      direSituationOptionTitles: [],
      // 事项情形与选项关系数据--必选集合
      direSituationOptionTitlesMust: [],
      // 事项选项值数据
      situationOptionVals: [],
      optionValOids: [],
      //已选的单选项和多选项值集合
      allValOids: [],
      //查询办件主键
      id: '',
      //查询办件业务主键
      caseOid: '',
      caseNumber: '',
      createDate: '',
      //办件状态
      caseStatus: '',
      //材料办件业务主键
      caseMaterialOids: [],
      //登记类型 法人1 自然人0
      cegisterType: null,
      //证件类型
      certificateTypeList: [],
      // 机构
      listOrganOptions: [],
      // 区划
      districtOptions: [],
      formConfig_show: false, //标识表单是否显示
      iframeState: false,
      iframVieweState: false,
      iframeUrl: {}, //表单地址
      iframeUrlView: {}, //查看
      sxSerForm: {}, //配置表单的信息
      sxSerFormList: [], //多个表单信息
      caseForm: [],
      tempFormDataId: [], //临时存放表单填报的返回值
      indexForm: 0,
      //事项材料
      comboDireMaterials: [],
      //办件材料
      comboCaseMaterials: [],
      mateOptRels: [],
      materialOptList: [],
      //事项情形
      comboDireSituationOid: "",
      bussVenueDistrictOids: "",
      situationOid: null,
      situationName: '默认自定情形',
      fileList: [],
      catalogCheckList: [],
      catalogList: [],
      materialCatalogAttaList: [],
      showFileList: false,
      accept: {
        type: String,
        default: '.jpg,.jpeg,.png,.pdf,.PDF,.doc,.docx,.DOC,.DOCX'
      },
      //附件集合
      attaList: [],
      pbpjCaseOkUrl: '',
      //材料业务主键
      materialOid: null,
      //材料与上传成功的附件集合
      materialAttaList: [],
      //材料与上传成功的附件集合
      materialAttaOidList: [],
      //收取材料方式
      collectionTypeList: [],
      //容缺补正材料
      rqhbMaterialList: [],
      elemLicenseList: [],
      // 弹出层标题
      title: "办件添加",
      // 显示弹出层
      openInit: true,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      //登录信息
      loginUser: {},
      queryForm: {},
      // 表单参数
      ruleForm: {
        applyNumber: "1",
        resultDeliveryWay: "1",
        deliverFlag: 1,
        proxyFlag: 0,
        sourceApp: 1,
        credentialNumber: "",
        applyUserName: "",
        credentialType: "",
        contactCredentialNumber: "",
        contactUserName: "",
        applyUserPhone: "",
        applyUserTel: "",
        applyUserAddress: "",
        legalPersonName: "",
        projectName: "",
        applyPostCode: "",
        specificLocation: "",
        investProjecName: "",
        investProjectCode: "",
        projectAbstract: "",
        contactEmail: "",
        contactUserPhone: "",
        contactUserTel: "",
        contactRemark: "",
        addresseeName: "",
        addresseePostCode: "",
        addresseePhone: "",
        addresseeTel: "",
        addresseeAddress: "",
        addresseeDetailAddress: "",
        finalOpinionDesc: "",
        bussVenueDistrictOidChoose: []
      },
      comboDireForm: {},
      optionForm: {},
      formData: {},
      materialForm: {},
      acceptForm: {
        ifAccept: 1
      },
      labelPosition: "top",
      checkList: [],
      checkBoxList: [],
      allCheckBoxList: [],
      stepData: [{
        index: "0",
        title: "情形选择",
      }, {
        index: "1",
        title: "材料核验",
      }, {
        index: "2",
        title: "信息登记",
      },
      {
        index: "3",
        title: "收取材料",
      }, {
        index: "4",
        title: "进入受理",
      },
      ],
      selectData: [],
      //失信人标志
      dishonestFlag: true,
      // 表单校验
      rules: {
        projectName: [{
          required: true,
          message: "必填项",
          trigger: "blur"
        },
        {
          min: 3,
          max: 100,
          message: "长度在 3 到 100 个字符",
          trigger: "blur"
        },
        ],
        applyNumber: [{
          required: true,
          message: "必填项",
          trigger: "blur"
        },],
        applyPostCode: [{
          required: true,
          message: "必填项",
          trigger: "blur"
        },
        {
          validator: validatePostCode,
          trigger: 'blur'
        }
        ],
        bussVenueDistrictOidChoose: [{
          required: true,
          message: "请选择业务管辖地",
          trigger: "blur"
        },],
        specificLocation: [{
          required: true,
          message: "必填项",
          trigger: "blur"
        },],
        applyUserName: [{
          required: true,
          message: "必填项",
          trigger: "blur"
        },],
        applyUserPhone: [{
          required: true,
          message: "必填项",
          trigger: "blur"
        },
        {
          validator: validatePhone,
          trigger: 'blur'
        }
        ],
        applyUserTel: [{
          required: true,
          message: "必填项",
          trigger: "blur"
        },
        {
          validator: validateTel,
          message: "请输入正确的申请人/单位电话",
          trigger: 'blur'
        }
        ],
        credentialType: [{
          required: true,
          message: "请选择证件类型",
          trigger: "change"
        },],
        credentialNumber: [{
          required: true,
          message: "必填项",
          trigger: "blur"
        },],
        legalPersonName: [{
          required: true,
          message: "必填项",
          trigger: "blur"
        },],
        contactUserName: [{
          required: true,
          message: "必填项",
          trigger: "blur"
        },
        {
          min: 3,
          max: 100,
          message: "长度在 3 到 100 个字符",
          trigger: "blur"
        },
        ],
        contactCredentialNumber: [{
          required: true,
          message: "必填项",
          trigger: "blur"
        },
        {
          validator: validIDCard,
          trigger: 'blur'
        }
        ],
        contactUserPhone: [{
          required: true,
          message: "必填项",
          trigger: "blur"
        },
        {
          validator: validatePhone,
          trigger: 'blur'
        }
        ],
        contactUserTel: [{
          validator: validateTel,
          message: "请输入正确的固定电话",
          trigger: 'blur'
        }],
        contactEmail: [{
          validator: validateEmails,
          trigger: 'blur'
        }],
        addresseeName: [{
          required: true,
          message: "必填项",
          trigger: "blur"
        },],
        addresseePostCode: [{
          required: true,
          message: "必填项",
          trigger: "blur"
        },
        {
          validator: validatePostCode,
          trigger: 'blur'
        }
        ],
        addresseePhone: [{
          required: true,
          message: "必填项",
          trigger: "blur"
        },
        {
          validator: validatePhone,
          trigger: 'blur'
        }
        ],
        addresseeTel: [{
          validator: validateTel,
          message: "请输入正确的收件人电话",
          trigger: 'blur'
        }],
        addresseeAddress: [{
          required: true,
          message: "请选择收件人地址",
          trigger: "change"
        },],
        addresseeDetailAddress: [{
          required: true,
          message: "必填项",
          trigger: "blur"
        },],
        rqhbTime: [{
          required: true,
          message: "请选择容缺补正到期时间",
          trigger: "blur"
        },],
      },
      optionDate: {
        disabledDate (time) {
          return time.getTime() < Date.now() // 选当前时间之后的时间
        }
      },
      userInfoShow: false,
      applyCardNum: "",
      dialogVisible: false
    };
  },
  watch: {

  },
  created () {
    this.cegisterType = this.pCegisterType;
    this.queryLoginInfo();
    this.getDistrictTree();
    //查看右侧
    //this.showRightSideBar();
    //一件事情形获取
    this.getComboDireSituationList();
    //获取证照类型
    this.getSelectCertificateType();
  },
  destroyed: function () {
    //在离开此页面的时候主动关闭socket
    this.socketApi.webSocketClose();
  },
  methods: {
    next (index, count) {
      let _that = this;
      if (count > 0) {
        if (index == 1) {
          let bixuan = true;
          //判断是否必选
          for (let oneTitle of _that.direSituationOptionTitlesMust) {
            if (bixuan) {
              let tuichu = true;
              let chooseFlag = oneTitle.chooseFlag; //是否必选  1是  0否
              let titleShow = oneTitle.titleName; //显示名称
              let comboOptionVals = oneTitle.comboOptionVals;
              if (chooseFlag == '0') {
                for (let opt of comboOptionVals) {
                  if (opt.ifCheck == true) {
                    tuichu = false;
                    break;
                  }
                }
                if (tuichu) {
                  _that.msgWarning(titleShow + "必选！");
                  bixuan = false;
                }
              }
            }
          }
          if (bixuan) {
            let options = "";
            if (_that.checkBoxList.length > 0) {
              _that.checkBoxList.forEach(box => {
                _that.allCheckBoxList.push(box);
                options += box + ",";
              });
            }
            if (_that.checkList.length > 0) {
              _that.checkList.forEach(check => {
                _that.allCheckBoxList.push(check);
                options += check + ",";
              });
            }
            blockSituationOptionsInfo(options, this.comboDireOid).then(response => {
              if (response.data == null) {
                //获取目录认息及材料信息
                _that.getComboDireDetail();
                _that.getComboDireMaterialList();
                _that.i = index;
                _that.show_0 = false;
                _that.show_1 = true;
              } else {
                _that.msgError("情形选项选择存在阻塞情形，请重新选择！");
                return false;
              }
            });
          }
        }
        if (index == 2) {
          _that.i = index;
          _that.getCheckbox(index);
        }
        if (index == 3) {
          //保存办件信息
          _that.checkPbpjInfo(index);
        }
        if (index == 4) {
          _that.catalogList = [];
          _that.saveMaterialAtta(index);
        }
      } else {
        _that.i = index;
        if (index == 0) {
          _that.allCheckBoxList = [];
          _that.show_0 = true;
          _that.show_1 = false;
        }
        if (index == 1) {
          _that.show_1 = true;
          _that.show_2 = false;
        }
        if (index == 2) {
          _that.show_2 = true;
          _that.show_3 = false;
          _that.catalogList = [];
          _that.getDistrictTree("", _that.bussVenueDistrictOids);
          _that.getAddressTree(_that.ruleForm.addresseeAddress);
        }
        if (index == 3) {
          _that.show_3 = true;
          _that.show_4 = false;
          //清空材料比对list
          _that.catalogList = [];
        }
      }
    },
    /** 登录信息 */
    queryLoginInfo () {
      let _that = this;
      getloginUser().then(response => {
        _that.loginUser = response.data;
      });
    },
    /** 获取区划树 */
    getDistrictTree (districtOid, bussVenueDistrictOids) {
      let _that = this;
      let oids = [];
      if (bussVenueDistrictOids) {
        let districtOids = bussVenueDistrictOids != "" ? bussVenueDistrictOids.split(',') : [];
        for (let oid of districtOids) {
          if (oid != "") {
            oids.push(oid)
          }
        }
        _that.ruleForm.bussVenueDistrictOidChoose = oids;
      }
      queryDistrictSimpleTree(districtOid).then(response => {
        _that.districtOptions = response.data;
      });
    },
    /** 选择情形 */
    selectChange (index, item) {
      this.num = index;
      this.situationName = item.title;
      this.checkList = [];
      this.checkBoxList = [];
      this.comboDireMaterials = [];
      this.direSituationOptionTitles = [];
      this.direSituationOptionTitlesMust = [];
      this.comboDireSituationOid = item.oid;
      if (item.oid == undefined) {
        this.getComboDireSituationList();
        this.ifDis = false;
      } else {
        this.ifDis = true;
        getSituationOpinionList(this.comboDireOid, item.oid).then(response => {
          //情形
          let titles = response.data;
          if (titles) {
            //情形选项值
            let titleValues;
            this.direSituationOptionTitles = [];
            titles.forEach(ti => {
              titleValues = {};
              titleValues.titleName = ti.name;
              titleValues.titleOid = ti.titleOid;
              titleValues.comboOptionVals = ti.comboOptionVals;
              titleValues.moreStatus = ti.moreStatus;
              titleValues.fillFlag = ti.fillFlag;
              if (ti.moreStatus === 0) {
                this.addRadioValTitle(ti.comboOptionVals);
              } else {
                this.addBoxValTitle(ti.comboOptionVals);
              }
              this.direSituationOptionTitles.push(titleValues);
              if (ti.fillFlag == '1') {
                titleValues.chooseFlag = '0';
                let comboOptionVals = ti.comboOptionVals;
                for (let opt of comboOptionVals) {
                  if (opt.ifCheck == true) {
                    titleValues.chooseFlag = titleValues.chooseFlag + "," + ti.titleOid + opt.valOid;
                  }
                }
                this.direSituationOptionTitlesMust.push(titleValues);
              }
            });
          }
        });
      }
    },
    addRadioValTitle (comboOptionVals) {
      if (comboOptionVals) {
        comboOptionVals.forEach(va => {
          if (va.ifCheck) {
            this.checkList.push(va.valOid);
          }
        });
      }
    },
    addBoxValTitle (comboOptionVals) {
      if (comboOptionVals) {
        comboOptionVals.forEach(va => {
          if (va.ifCheck) {
            this.checkBoxList.push(va.valOid);
          }
        });
      }
    },
    /** 勾选核验材料选项 */
    checkedBox (val) {
      this.radio = val;
    },
    /** 核验材料勾选 */
    getCheckbox (index) {
      if (this.radio) {
        this.i = index;
        this.show_1 = false;
        this.show_2 = true;

        //判断是否需要表单显示
        selectBySxSerForm(this.comboDireOid).then(response => {
          if (response.data) {
            this.formConfig_show = true;
            this.sxSerFormList = response.data;
            //this.sxSerForm.regOid=this.caseOid;
          }
        });
      } else {
        this.msgWarning("请勾选我已核验上述材料并确定齐全！");
        return false;
      }
    },
    /** 获取证件类型 */
    getSelectCertificateType () {
      getCertificateType(this.cegisterType).then(response => {
        this.certificateTypeList = response.data;
      });
    },
    /** 已有地址加载 */
    getAddressTree (addresseeAddress) {
      let _that = this;
      let address = [];
      if (addresseeAddress != "" && addresseeAddress != null) {
        let addressIds = addresseeAddress != "" ? addresseeAddress.replace("[\"", "").replace("\"]", "") : "";
        if (addressIds != "") {
          address = addressIds.split("\",\"");
        }
      }
      _that.ruleForm.addresseeAddress = address;
    },
    /** 改变证件类型 */
    changeType (item) {
      let _that = this;
      changeCredentialType(item).then(response => {
        _that.cardType = response.data.code;
        let type = {};
        _that.rules.credentialNumber.forEach((item, index) => {
          if (_that.cardType == "SFZ") {
            type.validator = validIDCard;
            type.trigger = 'blur';
            _that.rules.credentialNumber.push(type)
          } else if (_that.cardType == "XYDMZ") {
            type.validator = validUnifiedCredit;
            type.trigger = 'blur';
            _that.rules.credentialNumber.push(type)
          } else {
            if (index == 1) {
              _that.rules.credentialNumber.splice(_that.rules.credentialNumber.indexOf(item), 1)
            }
          }
        })
      });
    },
    /** 一件事情形获取 */
    getComboDireSituationList () {
      //查询事项情形
      getSituationList(this.comboDireOid).then(response => {
        this.situationName = "默认自定情形";
        let situations = response.data.comboSituations;
        let optionTitles = response.data.comboOptionTitleList;
        this.getSituationOpinion(situations);
        this.getSituationOpinionTitle(optionTitles);
      });
    },
    /** 填充选项 */
    getSituationOpinion (situations) {
      this.selectData = [{
        index: "0",
        title: "默认自定情形",
      },];
      if (situations) {
        situations.forEach((item, key) => {
          let situation = {};
          situation.index = key + 1;
          situation.title = item.situationName;
          situation.oid = item.situationOid;
          this.selectData.push(situation);
        });
      }
    },
    /** 填充标题和选项值 */
    getSituationOpinionTitle (optionTitles) {
      if (optionTitles) {
        optionTitles.forEach((optionTitle, index) => {
          let titleValues = {};
          titleValues.titleName = optionTitle.name;
          titleValues.titleOid = optionTitle.titleOid;
          titleValues.moreStatus = optionTitle.moreStatus;
          titleValues.fillFlag = optionTitle.fillFlag;
          titleValues.comboOptionVals = optionTitle.comboOptionVals;
          this.direSituationOptionTitles.push(titleValues);
          let comboOptionVals = optionTitle.comboOptionVals;
          if (optionTitle.fillFlag == '1') {
            titleValues.chooseFlag = '0';
            for (let opt of comboOptionVals) {
              if (opt.ifCheck == true) {
                titleValues.chooseFlag = titleValues.chooseFlag + "," + optionTitle.titleOid + opt.valOid;
              }
            }
            this.direSituationOptionTitlesMust.push(titleValues);
          }
        });
        optionTitles.forEach((optionTitle, index) => {
          let comboOptionVals = optionTitle.comboOptionVals;
          //检查选项值是否默认选中
          for (let opt of comboOptionVals) {
            //单选默认
            if (optionTitle.moreStatus == 0 && opt.defaultFlag == 1) {
              this.checkList[index] = opt.valOid;
              this.changeTitle(optionTitle.titleOid, opt.valOid);
            }
            //多选默认
            if (optionTitle.moreStatus == 1 && opt.defaultFlag == 1) {
              this.checkBoxList.push(opt.valOid);
              this.changeTitleBox(opt, opt.valOid);
            }
          }
        });
      }
    },
    /**获取一件事目录详细**/
    getComboDireDetail () {
      let _that = this
      getOne(_that.comboDireOid).then(response => {
        _that.comboDireForm = response.data;
      });
    },
    /** 查询不到情形获取一件事目录材料数据信息 */
    getComboDireMaterialList () {
      //将单选、多选的选项值拼接
      this.allValOids = [];
      if (this.checkBoxList.length > 0) {
        this.checkBoxList.forEach(checkBox => {
          this.allValOids.push(checkBox);
        })
      }
      if (this.checkList.length > 0) {
        this.checkList.forEach(checkBox => {
          this.allValOids.push(checkBox);
        })
      }

      getComboDireMaterialList(this.comboDireOid, this.allValOids).then(response => {
        this.comboDireMaterials = response.data;
      });
    },
    //验证表单以及坚持平板评价信息
    checkPbpjInfo (index) {
      let _that = this;
      _that.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          //验证表单是否必填
          if (_that.sxSerFormList) {
            let flagMust = false;
            _that.sxSerFormList.forEach((ite, i) => {
              if (ite.isFormFlag == 1) {//必填
                if (!_that.tempFormDataId[i]) {
                  flagMust = true;
                }
              }
            })
            if (flagMust) {
              _that.$message.error("表单未填写,请填写表单！");
              return false;
            }
          }
          //验证人员是否启用评价器
          if (undefined != _that.ruleForm.bussVenueDistrictOidChoose && _that.ruleForm.bussVenueDistrictOidChoose
            .length > 0) {
            let oids = "";
            for (let i = 0; i < _that.ruleForm.bussVenueDistrictOidChoose.length; i++) {
              oids += _that.ruleForm.bussVenueDistrictOidChoose[i] + ",";
            }
            _that.ruleForm.bussVenueDistrictOid = oids;
          }
          //收件人地址
          if (typeof (this.ruleForm.addresseeAddress) != "string") {
            let address = "[";
            this.ruleForm.addresseeAddress.forEach(val => {
              address += '"' + val + '",';
            })
            if (this.ruleForm.addresseeAddress.length > 0) {
              address = address.substring(0, address.lastIndexOf(","));
            }
            address += "]";
            this.ruleForm.addresseeAddress = address;
          }
          _that.pbpjConfirmUser(index, _that.ruleForm);
        }
      });

    },
    //查询当前登录人员是否开启平板确认或者评价
    pbpjConfirmUser (index, ruleForm) {
      let _that = this;
      pushPbpjUser(_that.loginUser.userOid).then(response => {
        if (response.data != "") {
          if (response.data.confirmFlag == "1") {
            //推送平板评价办件确认信息
            _that.confirmPbpj(index, ruleForm);
          } else { //没用启用评价 直接保存办件
            _that.saveComboCaseForm(index);
          }
        }
      });
    },

    //平板评价确认信息
    confirmPbpj (index, ruleForm) {
      let _that = this;
      let url = window.location.origin;
      _that.pbpjCaseOkUrl = url;
      pushPbpjCheckLogin().then(response => {
        if (response.data) {
          //检查平板评价登录
          _that.$confirm('你确定要进行办件信息确认吗？', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function () {
            regComboCaseInfo(ruleForm, _that.pbpjCaseOkUrl).then(response => {
              if (response.data) {
                _that.msgWarning("正在信息确认...");
                _that.loading = true;
                if (_that.getDataCheckValue(1, index)) {
                  _that.loading = false;
                };
              }
            });
          }).catch(action => {
            if (action === 'cancel') {
              _that.saveComboCaseForm(index);
            }
          });
        } else {
          _that.msgWarning("平板评价未登录，请登录平板评价器！");
        }
      });
    },
    //平板评价确认
    getDataCheckValue (num, index) {
      let _that = this;
      comboCaseCallBack().then(response => {
        if (response.data == "1") {
          //保存办件
          this.saveComboCaseForm(index);
        } else if (response.data == "0") {
          _that.msgError("确认信息有误，请重新修改办件信息！");
          return false;
        } else {
          if (num >= 60) { //60s后自动保存
            //保存办件
            _that.saveComboCaseForm(index);
          } else {
            //每隔一秒钟获取一次评价值，35秒后未获取到值，默认为未确定
            setTimeout(function () {
              _that.getDataCheckValue((num + 1), index);
            }, 1000);
          }
        }
      })
    },

    /** 办件信息下一步保存 */
    saveComboCaseForm (index) {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          //查询是否已保存 返回id用于更新
          if (this.caseOid != "") {
            getComboCaseByOid(this.caseOid).then(response => {
              this.id = response.data.id;
              this.caseOid = response.data.caseOid;
            });
          }
          if (index == "") { //暂存
            this.isDisable = true;
            this.caseStatus = 0;
          }
          this.ruleForm.id = this.id;
          this.ruleForm.caseOid = this.caseOid;
          //this.ruleForm.caseStatus = this.caseStatus;
          this.ruleForm.applyUserType = this.cegisterType;
          this.ruleForm.comboDireOid = this.comboDireOid;
          this.ruleForm.comboDireMaterials = this.comboDireMaterials;
          //情形
          this.ruleForm.situationOid = this.comboDireSituationOid;
          //选项值:将单选、多选的选项值拼接
          this.ruleForm.valList = this.allCheckBoxList;
          //收件人地址
          if (typeof (this.ruleForm.addresseeAddress) != "string") {
            let address = "[";
            this.ruleForm.addresseeAddress.forEach(val => {
              address += '"' + val + '",';
            })
            if (this.ruleForm.addresseeAddress.length > 0) {
              address = address.substring(0, address.lastIndexOf(","));
            }
            address += "]";
            this.ruleForm.addresseeAddress = address;
          }
          //业务管辖
          if (this.ruleForm.bussVenueDistrictOidChoose) {
            let districtOid = [];
            let oids = "";
            districtOid = this.ruleForm.bussVenueDistrictOidChoose;
            districtOid.forEach(oid => {
              oids += oid + ",";
            })
            this.ruleForm.bussVenueDistrictOid = oids;
          }
          saveComboCase(this.ruleForm).then(response => {
            if (response.data != "") {
              this.caseOid = response.data.caseOid;
              this.id = response.data.id;
              this.caseNumber = response.data.caseNumber;
              this.createDate = response.data.createDate;
              this.comboCaseMaterials = JSON.parse(response?.data?.comboCaseMaterials ?? "[]")
              if (this.comboDireMaterials && this.comboDireMaterials.length > 0 &&
                this.comboCaseMaterials && this.comboCaseMaterials.length > 0) {
                this.comboCaseMaterials.forEach((item, index) => {
                  this.comboDireMaterials.forEach((ite, i) => {
                    if (item.materialOid == ite.materialOid) {
                      item.comboDireMaterial = ite;
                    }
                  })
                })
              }
              // this.comboDireSituationOid = response.data.comboDireSituationOid;
              // this.checkList = response.data.valList;
              this.getDistrictTree("", response.data.bussVenueDistrictOid);
              this.getAddressTree(this.ruleForm.addresseeAddress);
              this.saveCaseForm(); //保存办件表单信息
              this.msgSuccess("保存办件成功！");
              //获取失信人信息
              this.checkInDishonestUser();
              if (index != "") {
                this.i = index;
                this.show_2 = false;
                this.show_3 = true;
              } else {
                this.$emit('case-close');
                return false;
              }
            } else {
              this.msgError("保存办件失败！");
              this.isDisable = false;
              return false;
            }
          });
          //   }, 1000);
        }
      });

    },
    /** 附件目录材料 */
    chooseCatalog (caseMaterialOid, attaOid, materialCatalogOid) {
      let _that = this;
      let catalog = {};
      catalog.attaOid = attaOid;
      catalog.materialCatalogOid = materialCatalogOid;
      catalog.caseMaterialOid = caseMaterialOid;
      if (_that.materialCatalogAttaList != "") {
        _that.materialCatalogAttaList.forEach(catalog => {
          if (catalog) {
            if (catalog.attaOid == attaOid) {
              _that.materialCatalogAttaList.splice(_that.materialCatalogAttaList.indexOf(catalog), 1, {})
            }
          }

        })
      }
      _that.materialCatalogAttaList.push(catalog);
    },
    /** 检查失信人 */
    checkInDishonestUser (index) {
      let _that = this;
      checkApplyUserInDishonest(_that.ruleForm.applyUserName, _that.ruleForm.credentialNumber).then(response => {
        if (response.data != null) {
          _that.dishonestFlag = false;
        }
      })
    },
    /** 保存材料附件信息 */
    saveMaterialAtta (index) {
      let _that = this;
      let flag = false
      if (_that.collectionTypeList) {
        let findnum = _that.collectionTypeList.findIndex(coll => {
          if (coll.collectionType == '2') {
            return true;
          }
        });
        if (findnum > 0 && _that.fileList.length == 0 && _that.materialAttaList.length == 0) {
          _that.msgError("请至少上传一个附件");
          return false;
        } else {
          //若存在办件材料，再验证是否含有必须收取
          if (_that.comboCaseMaterials.length > 0) {
            //是否存在 必须 材料,未收取
            for (let i = 0; i < _that.comboCaseMaterials.length; i++) {
              const cmMaterial = _that.comboCaseMaterials[i];
              //单个必要材料收取类型开关
              let mustTypeFlagFegin = false;
              //单个必要材料收取附件开关
              let mustAttaFlagFegin = false;
              //是否选择上传附件
              let uploadFlag = false;
              let materialName = "";
              //必须，看是否有收取
              if (cmMaterial.comboDireMaterial.mustFlag == '1') {
                //遍历收取类型
                if (_that.collectionTypeList && _that.collectionTypeList.length > 0) {
                  for (let j = 0; j < _that.collectionTypeList.length; j++) {
                    const type = _that.collectionTypeList[j];
                    if (type.caseMaterialOid == cmMaterial.caseMaterialOid && type.collectionType) {
                      mustTypeFlagFegin = true;
                      //已经点击收取类型，后遍历收取的材料
                      //看是否为纸质，则无材料
                      if (type.collectionType == 1) {
                        //无材料
                        mustAttaFlagFegin = true;
                      } else if (type.collectionType == 2 || type.collectionType == 3) {
                        //附件上传、扫描、
                        if (_that.materialAttaList.length > 0) {
                          for (let k = 0; k < _that.materialAttaList.length; k++) {
                            const atta = _that.materialAttaList[k];
                            if (atta.caseMaterialOid == cmMaterial.caseMaterialOid) {
                              mustAttaFlagFegin = true;
                            }
                          }
                        }
                      } else if (type.collectionType == 5) {
                        //证照
                        if (_that.elemLicenseList.length > 0) {
                          for (let k = 0; k < _that.elemLicenseList.length; k++) {
                            const atta = _that.elemLicenseList[k];
                            if (atta.materialOid == cmMaterial.caseMaterialOid) {
                              mustAttaFlagFegin = true;
                            }
                          }
                        }
                      } else if (type.collectionType == 4) {
                        //容缺后补
                        mustAttaFlagFegin = true;
                      }
                    }
                  }
                }
              } else {
                //非必要
                mustTypeFlagFegin = true;
                mustAttaFlagFegin = true;
              }
              //选择上传附件 扫描 证照判断是否有附件上传
              if (_that.collectionTypeList.length > 0) {
                for (let k = 0; k < _that.collectionTypeList.length; k++) {
                  const coll = _that.collectionTypeList[k];
                  if (coll.collectionType != '1' && coll.collectionType != '4' && coll.collectionType != null) {
                    if (coll.attaOid == null || coll.attaOid == '') {
                      uploadFlag = true;
                      materialName = coll.materialName;
                      break;
                    }
                  }
                }
              }
              //如果为true，给出提示
              if (uploadFlag) {
                _that.msgError("[" + materialName + "]必须上传材料！");
                return false;
              }
              //必须材料，未选取方式
              if (mustTypeFlagFegin == false) {
                _that.msgError("材料 " + cmMaterial.materialName + " 的收取方式未选择！");
                return false;
              }
              //必须材料，未选取方式
              if (mustAttaFlagFegin == false) {
                _that.msgError("必须材料 " + cmMaterial.materialName + " 的附件未上传或证照未选取！");
                return false;
              }
            }

            //必须材料，未选取方式
            /*if (mustTypeFlag == false) {
              _that.msgError("材料 " + mustType + " 的收取方式未选择！");
              return false;
            }
            //必须材料附件，未收取
            if (mustAttaFlag == false) {
              _that.msgError("必须材料 " + mustAtta + " 的附件未上传或证照未选取！");
              return false;
            }*/
          }
          let dataForm = {};
          dataForm.caseOid = _that.caseOid;
          dataForm.caseMaterialOid = _that.materialOid;
          dataForm.attList = _that.materialAttaList;
          dataForm.collectionTypeVos = _that.collectionTypeList;
          dataForm.elemLicense = _that.elemLicenseList;
          //dataForm.materialCatalogAttaList = this.materialCatalogAttaList;
          dataForm.rqhbTime = _that.materialForm.rqhbTime;
          dataForm.sqrName = _that.ruleForm.applyUserName;
          dataForm.caseName = _that.ruleForm.projectName;
          let tempMaterialCatalogAtta = [];
          _that.materialCatalogAttaList.forEach((fir, i) => {
            if (fir) {
              tempMaterialCatalogAtta.push(fir);
              _that.materialCatalogAttaList.forEach(second => {
                //如果办件材料一样附件不一样看选择的智审目录是否相同
                if (second) {
                  if (fir.caseMaterialOid == second.caseMaterialOid && fir.attaOid != second.attaOid) {
                    if (fir.materialCatalogOid == second.materialCatalogOid) {
                      flag = true;
                    }
                  }
                }
              })
            }

          })
          dataForm.materialCatalogAttaList = tempMaterialCatalogAtta;
          if (flag) {
            _that.msgError("上传文件请选择不同的目录材料！");
            return false;
          } else {
            if (_that.rqhbMaterialList.length > 0) {
              _that.$refs["materialForm"].validate((valid) => {
                if (valid) {
                  _that.$confirm('你确定要打印容缺补正承诺书吗？', "警告", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                  }).then(function () {
                    POBrowser.openWindowModeless(process.env.VUE_APP_BASE_API_PAGE + '/manage/zhuozheng/printComboCaseRqhbNotice?rqhbTime=' + dataForm.rqhbTime + '&sqrName=' +
                      encodeURIComponent(dataForm.sqrName) + '&caseName=' + encodeURIComponent(dataForm.caseName), 'width=1200px;height=800px;')
                  }).then(function () {
                    saveCaseMaterialAtta(dataForm).then(response => {
                      if (response.code === 200) {
                        _that.msgSuccess("保存材料附件成功！");
                        _that.i = index;
                        _that.loading = false;
                        _that.show_3 = false;
                        _that.show_4 = true;
                      }
                    });
                  }).catch(action => {
                    if (action === 'cancel') {
                      saveCaseMaterialAtta(dataForm).then(response => {
                        if (response.code === 200) {
                          _that.msgSuccess("保存材料附件成功！");
                          _that.i = index;
                          _that.loading = false;
                          _that.show_3 = false;
                          _that.show_4 = true;
                        }
                      });
                    }
                  })
                }
              });

            } else {
              saveCaseMaterialAtta(dataForm).then(response => {
                if (response.code === 200) {
                  _that.msgSuccess("保存材料附件成功！");
                  _that.i = index;
                  _that.loading = false;
                  _that.show_3 = false;
                  _that.show_4 = true;
                }
              });
            }
          }
        }
      } else {
        _that.msgError("请至少选择一个收取方式");
        return false;
      }
    },
    //办件平板评价
    pushPjCaseService () {
      let _that = this;
      let userOid = _that.loginUser.userOid;
      pushPbpjUser(userOid).then(response => {
        //推送办件评价
        if (response.data != "") {
          if (response.data.appraiseFlag == "1") {
            //推送平板评价
            _that.caseAccpet(0);
          } else { //没用启用评价 直接受理
            _that.caseAccpet(1);
          }
        }
      });
    },
    //平板评价
    pushPbpj () {
      let _that = this;
      pushPbpjCheckLogin().then(response => {
        if (response.data) {
          //检查平板评价登录
          _that.$confirm('你确定要进行办件评价吗？', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function () {
            pbpjSaveCaseInfo(_that.caseOid).then(response => {
              if (response.data != null) {
                let caseNumber = response.data
                pushPbpjInfo(caseNumber).then(response => {
                  if (response.data != null) {
                    _that.$emit('closeUpdate');
                  }
                });
              } else {
                _that.msgError("办件评价失败！");
                return false;
              }
            });
          }).catch(action => {
            if (action === 'cancel') {
              _that.$emit('closeUpdate');
            }
          });
        } else {
          _that.msgWarning("平板评价未登录，请登录平板评价器！");
          return false;
        }
      });
    },
    /** 办件受理 */
    caseAccpet (flag) {
      let _that = this;
      if (_that.caseOid != "") {
        _that.acceptForm.caseOid = _that.caseOid;
        _that.acceptForm.valList = _that.allCheckBoxList;
        _that.acceptForm.caseNumber = _that.ruleForm.caseNumber;
        _that.acceptForm.sqrName = _that.ruleForm.applyUserName;
        _that.acceptForm.sqTime = _that.createDate ? _that.createDate.substring(0, 10) : _that.createDate;
        _that.acceptForm.rqhbTime = _that.materialForm.rqhbTime;
        _that.acceptForm.projectName = _that.ruleForm.projectName;
        if (_that.acceptForm.ifAccept == 2) {
          if (!_that.acceptForm.acceptOpinionDesc) {
            _that.$message.error("意见说明不能为空！");
            return false;
          }
        }
        if (_that.acceptForm.ifAccept == 1) {
          saveCaseAccpet(_that.acceptForm).then(response => {
            if (response.data != "") {
              _that.msgSuccess("办件进入受理成功！");
              if (flag == 1) {
                _that.$emit('closeUpdate');
              } else {
                //推送平板评价
                _that.pushPbpj();
              }
            }
          });
        } else {
          _that.$confirm('你确定要打印不予受理通知书吗？', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function () {
            POBrowser.openWindowModeless(process.env.VUE_APP_BASE_API_PAGE +
              '/manage/zhuozheng/printComboCaseNotAcceptNotice?caseNumber=' + _that.caseNumber +
              '&sqrName=' + encodeURIComponent(_that.acceptForm.sqrName) + '&sqTime=' + _that.acceptForm.sqTime,
              'width=1200px;height=800px;');
          }).then(function () {
            saveCaseAccpet(_that.acceptForm).then(response => {
              if (response.data != "") {
                _that.msgSuccess("办件已不予受理！");
                if (flag == 1) {
                  _that.$emit('closeUpdate');
                } else {
                  //推送平板评价
                  _that.pushPbpj();
                }
              }
            });
          }).catch(action => {
            if (action === 'cancel') {
              saveCaseAccpet(_that.acceptForm).then(response => {
                if (response.data != "") {
                  _that.msgSuccess("办件已不予受理！");
                  if (flag == 1) {
                    _that.$emit('closeUpdate');
                  }
                }
              });
            }
          })
        }
      } else {
        _that.msgError("办件信息未保存，请先保存！");
        return false;
      }
    },
    /** 送达方式 */
    changeDeliveryWay (val) {
      this.address_show = (val === '1') ? true : false;
    },
    /** 是否代理人 */
    changeProxyFlag (val) {
      this.proxy_show = (val === 1) ? true : false;
    },
    /** 材料形式 */
    chooseCollectionType (val, item, index) {
      let bb = new Object();
      if (val == "2") {
        this.show_upload[index] = true;
        this.show_scan[index] = false;
        this.show_elem[index] = false;
      } else if (val == "3") {
        this.show_scan[index] = true;
        this.show_upload[index] = false;
        this.show_elem[index] = false;
      } else if (val == "5") {
        this.show_elem[index] = true;
        this.show_scan[index] = false;
        this.show_upload[index] = false;
      } else {
        this.show_upload[index] = false;
        this.show_scan[index] = false;
        this.show_elem[index] = false;
      }
      bb.collectionType = val;
      bb.caseMaterialOid = item.caseMaterialOid;
      bb.collectionNumber = item.collectionNumber;
      bb.materialName = item.materialName;
      if (item.elemLicenseOid == null) {
        bb.attaOid = item.attaList;
      } else {
        bb.attaOid = item.elemLicenseOid;
      }
      if (this.collectionTypeList && this.collectionTypeList.length > 0) {
        let count = this.collectionTypeList.findIndex(da => {
          if (da.caseMaterialOid == item.caseMaterialOid) {
            da.collectionType = val;
            return true;
          }
        });
        if (count < 0) {
          this.collectionTypeList.push(bb);
        }
      } else {
        this.collectionTypeList.push(bb);
      }
      //除了附件上传方式或扫描，别的方式需要移除附件OID
      if (this.materialAttaList.length > 0) {
        var index = this.materialAttaList.findIndex(atta => {
          if (item.caseMaterialOid == atta.caseMaterialOid) {
            return true;
          }
        })
        if (index != -1) {
          this.materialAttaList.splice(index, 1);

          if (this.comboCaseMaterials && this.comboCaseMaterials.length > 0) {
            this.comboCaseMaterials.forEach((caseMat, z) => {
              if (caseMat.caseMaterialOid == item.caseMaterialOid) {
                caseMat.attaList = [];
              }
            })
          }
          //删除材料和智审目录关联
          if (this.materialCatalogAttaList && this.materialCatalogAttaList.length > 0) {
            this.materialCatalogAttaList.forEach((cata, z) => {
              if (cata) {
                if (cata.caseMaterialOid == item.caseMaterialOid) {
                  this.materialCatalogAttaList.splice(z, 1, {});
                }
              }

            })
          }
        }
      }
      //清空电子证照信息
      if (this.elemLicenseList.length > 0) {
        this.elemLicenseList.forEach((ite, i) => {
          if (ite.materialOid == item.caseMaterialOid) {
            this.elemLicenseList.splice(i, 1);
          }
        });
        //删除证照
        this.$set(this.attaList, index, []);
      }
      //选择材料容缺后补
      if (this.rqhbMaterialList) {
        this.rqhbMaterialList.forEach(rqhb => {
          if (rqhb == item.caseMaterialOid) {
            this.rqhbMaterialList.splice(this.rqhbMaterialList.indexOf(rqhb), 1);
          }
        });
      }
      if (val == "4") {
        this.rqhbMaterialList.push(item.caseMaterialOid);
      }
      //容缺后补为空隐藏选择时间
      if (this.rqhbMaterialList.length <= 0) {
        this.rqhb_show = false;
      } else {
        this.rqhb_show = true;
      }
      this.radio1.push(index);
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 选择地址 */
    handleChange (values) {
    },
    /** 加载送达信息 */
    getApplyInfo () {
      let _that = this;
      _that.ruleForm.addresseeName = _that.ruleForm.applyUserName;
      _that.ruleForm.addresseePostCode = _that.ruleForm.applyPostCode;
      _that.ruleForm.addresseePhone = _that.ruleForm.applyUserPhone;
      _that.ruleForm.addresseeTel = _that.ruleForm.applyUserTel;
    },
    /*  /!** 隐藏右侧 *!/
      hiddenRightSideBar() {
        this.rightShow = false;
      },
      /!** 显示右侧 *!/
      showRightSideBar() {
        this.rightShow = true;
      },*/
    /** 表单重置 */
    reset () {
      Object.assign(this.queryForm, this.$options.data().queryForm)
      this.resetForm("queryForm");
    },
    /** 取消按钮 */
    cancel () {
      this.openInit = false;
      this.reset();
    },

    /** 关闭方法 */
    closeDialog () {
      this.comboDireMaterials = [];
      this.situationOptionVals = [];
      this.checkList = [];
      this.situationList = [];
      this.materialOptList = [];
      this.mateOptRels = [];
      this.radio1 = [];
    },
    /** 失败后返回 */
    uploadError (resp) {
      this.msgError("文件上传失败");
    },

    /** 上传附件 */
    uploadFiles (file) {
      let formData = new FormData();
      formData.append("files", file.file);
      uploadCaseMaterialFile(formData).then(response => {
        if (response.data != "") {
          response.data.forEach(data => {
            let atta = {};
            atta.index = data;
            file.data.attaList.push(data);
            let at = new Object();
            at.caseMaterialOid = file.data.caseMaterialOid;
            at.attaOid = data.oid;
            this.materialAttaList.push(at);
            if (this.collectionTypeList.length > 0) {
              this.collectionTypeList.forEach(collection => {
                if (collection.caseMaterialOid == file.data.caseMaterialOid) {
                  collection.attaOid = data.oid;
                }
              });
            }
          });
        } else {
          this.$message.error('上传文件不能为空！')
        }
      })
    },
    /** 上传附件请求操作 */
    beforeUpload (file) {
      let isRightSize = file.size / 1024 / 1024 < 10
      if (!isRightSize) {
        this.$message.error('文件大小超过 10MB')
      }
      this.fileList.push(file);
      return isRightSize
    },
    //下载附件
    downloadFile (attaOid) {
      this.download(attaOid);
    },
    //材料智审功能
    clzs (index, count) {
      const loadingnew = this.$loading({
        lock: true,
        text: '材料审核中...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });

      let _that = this;
      let flag = false
      if (this.collectionTypeList) {
        var index = this.collectionTypeList.findIndex(coll => {
          if (coll.collectionType == '2') {
            return true;
          }
        });
        if (index > 0 && this.fileList.length == 0 && this.materialAttaList.length == 0) {
          loadingnew.close();
          this.msgError("请至少上传一个附件");
          return false;
        } else {
          let dataForm = {};
          dataForm.caseOid = this.caseOid;
          // dataForm. caseMaterialOid= this.materialOid;
          dataForm.attList = this.materialAttaList;
          dataForm.collectionTypeVos = this.collectionTypeList;
          dataForm.elemLicense = this.elemLicenseList;

          let tempMaterialCatalogAtta = [];
          if (!_that.materialCatalogAttaList || _that.materialCatalogAttaList.length <= 0) {
            loadingnew.close();
            _that.msgError("上传文件未关联目录信息不可进行智审！");
            return false;
          }
          _that.materialCatalogAttaList.forEach((fir, i) => {
            if (fir) {
              tempMaterialCatalogAtta.push(fir);
              _that.materialCatalogAttaList.forEach(second => {
                //如果办件材料一样附件不一样看选择的智审目录是否相同
                if (second) {
                  if (fir.caseMaterialOid == second.caseMaterialOid && fir.attaOid != second.attaOid) {
                    if (fir.materialCatalogOid == second.materialCatalogOid) {
                      flag = true;
                    }
                  }
                }
              })
            }

          })
          dataForm.materialCatalogAttaList = tempMaterialCatalogAtta;
          if (flag) {
            loadingnew.close();
            _that.msgError("上传文件请选择不同的目录材料！");
            return false;
          } else {
            saveCaseMaterialAtta(dataForm).then(response => {
              if (response.code === 200) {
                _that.catalogList = [];
                var letList = response.data;
                var size = letList.length;
                if (letList && size > 0) {
                  /*材料预审start*/
                  letList.forEach((item, num) => {
                    let caseOid = this.caseOid;
                    let caseMaterialOid = item.caseMaterialOid;
                    let caseFileRecOid = item.materialAttaOid;
                    let cataOid = item.materialCatalogOid;
                    if (item.materialCatalogOid != null && item.materialCatalogOid != "") {
                      intelligentPretrialmaterialPrePrial(caseOid, caseFileRecOid, caseMaterialOid, cataOid).then(response => {
                        if (response.data.success == true) {
                          if (num + 1 == letList.length) {
                            loadingnew.close();
                            let item = {
                              caseOid: caseOid,
                              caseMaterialOid: caseMaterialOid,
                              caseFileRecOid: caseFileRecOid,
                              cataOid: cataOid,
                              show: true,
                              title: '智能审核结果'
                            };
                            _that.comboIntelligentPretrialOptions.push(item);
                          }
                        } else {
                          _that.$loading().close();
                          _that.msgError(response.data.message);
                          return false;
                        }
                      })
                    }
                  })
                }
              }
            });
          }
        }
      } else {
        loadingnew.close();
        this.msgError("请至少选择一个收取方式");
        return false;
      }
    },
    /** 改变选项值 */
    changeTitle (titleChooseOid, optionValOid) {
      for (let oneTitle of this.direSituationOptionTitlesMust) {
        if (oneTitle.titleOid == titleChooseOid) {
          oneTitle.chooseFlag = '1';
        }
      }
      let _that = this;
      if (_that.optionValOids && _that.optionValOids.length > 0) {
        _that.optionValOids.forEach(ov => {
          var ind = _that.checkList.findIndex(ck => {
            if (ov.valOid == ck) {
              return true;
            }
          });
          if (ind < 0) {
            if (ov.titles.titleOid) {
              //清除选项值下的标题
              var index = _that.direSituationOptionTitles.findIndex(tti => {
                if (ov.titles.titleOid == tti.titleOid) {
                  return true;
                }
              });
              if (index > 0) {
                _that.direSituationOptionTitles.forEach(tit => {
                  if (ov.titles.titleOid == tit.titleOid) {
                    tit.comboOptionVals.forEach(tti => {
                      var indd = _that.checkList.findIndex(cck => {
                        if (tti.valOid == cck) {
                          return true;
                        }
                      });
                      if (indd > 0) {
                        _that.checkList.splice(indd, 1);
                      }

                    });
                  }
                });

                _that.direSituationOptionTitles.splice(index, 1);
              }
              //清除选项
              var ind2 = _that.optionValOids.findIndex(ovd => {
                if (ov.valOid == ovd.valOid) {
                  return true;
                }
              });
              if (ind2 > 0) {
                _that.optionValOids.splice(ind2, 1);
              }
            }
          }
        });
        // _that.optionValOids = [];
      }
      queryComboOptionTitleByValOid(this.comboDireOid, optionValOid).then(response => {
        //情形
        let titles = response.data;
        if (titles && titles.length > 0) {
          //情形选项值
          let titleValues;
          let valObj;
          titles.forEach(ti => {
            titleValues = {};
            titleValues.titleName = ti.name;
            titleValues.titleOid = ti.titleOid;
            titleValues.moreStatus = ti.moreStatus;
            titleValues.fillFlag = ti.fillFlag;
            titleValues.comboOptionVals = ti.comboOptionVals;
            ti.comboOptionVals.forEach(comboVal => {
              if (comboVal.defaultFlag == 1) {
                _that.checkBoxList.push(comboVal.valOid);
              }
            })
            valObj = {};
            valObj.valOid = optionValOid;
            valObj.titles = titleValues;
            _that.optionValOids.push(valObj);
            _that.direSituationOptionTitles.push(titleValues);
          });
        } else {
          let valObj = {};
          valObj.valOid = optionValOid;
          valObj.titles = {};
          _that.optionValOids.push(valObj);
        }
      });
    },
    changeTitleBox (item, val) {
      let _that = this;
      for (let oneTitle of this.direSituationOptionTitlesMust) {
        if (oneTitle.titleOid == item.titleOid) {
          if (oneTitle.chooseFlag.indexOf(item.titleOid + val) != -1) {
            oneTitle.chooseFlag = oneTitle.chooseFlag.replace("," + item.titleOid + val, '');
          } else {
            oneTitle.chooseFlag = oneTitle.chooseFlag + "," + item.titleOid + val;
          }
        }
      }
      //多选框勾选或者取消
      _that.checkBoxList.forEach(box => {
        //多选框勾选显示与勾选的选项值的选项
        if (val == box) {
          queryComboOptionTitleByValOid(this.comboDireOid, box).then(response => {
            if (response.data != null) {
              //情形
              let titles = response.data;
              if (titles && titles.length > 0) {
                //情形选项值
                let titleValues;
                titles.forEach(ti => {
                  titleValues = {};
                  titleValues.titleName = ti.name;
                  titleValues.titleOid = ti.titleOid;
                  titleValues.moreStatus = ti.moreStatus;
                  titleValues.fillFlag = ti.fillFlag;
                  titleValues.comboOptionVals = ti.comboOptionVals;
                  _that.direSituationOptionTitles.push(titleValues);
                });
              }
            }
          });
        } else {
          //多选框取消隐藏与勾选的选项值的选项
          queryComboOptionTitleByValOid(this.comboDireOid, val).then(response => {
            //情形
            let titles = response.data;
            if (titles && titles.length > 0) {
              titles.forEach(ti => {
                _that.direSituationOptionTitles.forEach((tti, index) => {
                  if (tti.titleOid === ti.titleOid) {
                    _that.direSituationOptionTitles.splice(index, 1);
                  }
                });
              });
            }
          });
        }
      })
      //多选框取消时最后一个多选框点击
      if (_that.checkBoxList.length == 0) {
        queryComboOptionTitleByValOid(this.comboDireOid, val).then(response => {
          //情形
          let titles = response.data;
          if (titles && titles.length > 0) {
            titles.forEach(ti => {
              _that.direSituationOptionTitles.forEach((tti, index) => {
                if (tti.titleOid === ti.titleOid) {
                  _that.direSituationOptionTitles.splice(index, 1);
                }
              });
            });
          }
        });
      }
    },
    base64ToFile (urlData, fileName) {
      let arr = urlData.split(',');
      // let mime = arr[0].match(/:(.*?);/)[1];
      let bytes = atob(arr[0]); // 解码base64
      let n = bytes.length
      let ia = new Uint8Array(n);
      while (n--) {
        ia[n] = bytes.charCodeAt(n);
      }
      return new File([ia], fileName, {
        type: 'image/jpg'
      });
    },
    // 接收socket回调函数返回数据的方法
    getConfigResult (data) {
      let _that = this;
      if (data.status == 0) {
        //读卡
        if (data.device == "IdCard") {
          //申请人模块--0
          //联系人模块--1
          if (data.type == "0") {
            _that.ruleForm.credentialNumber = data.content.CardNum;
            _that.ruleForm.applyUserName = data.content.CardBelongName;
            _that.ruleForm.credentialType = "身份证";
          }
          if (data.type == "1") {
            _that.ruleForm.contactCredentialNumber = data.content.CardNum;
            _that.ruleForm.contactUserName = data.content.CardBelongName;
          }
        }

        //扫描
        if (data.device == "HighCamera") {
          if (data.type) {
            const caseMaterialOid = data.type;
            if (data.content.Cameras64) {
              //切割路径
              let base64s = data.content.Cameras64.split(",");
              for (let i = 0; i < base64s.length; i++) {
                if (!base64s[i]) {
                  continue;
                }
                //将用户选择的图片进行保存处理
                let file = _that.base64ToFile(base64s[i], "scanPicture" + i + ".jpg");
                let formD = new FormData();
                formD.append("files", file);
                _that.fileList.push(file);
                uploadCaseMaterialFile(formD).then(response => {
                  if (response.data != "") {
                    response.data.forEach(data => {
                      _that.scan_item.attaList.push(data);
                      let at = new Object();
                      at.caseMaterialOid = caseMaterialOid;
                      at.attaOid = data.oid;
                      _that.materialAttaList.push(at);
                      //塞入上传附件
                      if (_that.collectionTypeList.length > 0) {
                        _that.collectionTypeList.forEach(collection => {
                          if (collection.caseMaterialOid == caseMaterialOid) {
                            collection.attaOid = data.oid;
                          }
                        });
                      }
                    });
                  }
                })
              }
            }
          }
        }
      }
      if (data.status == 1) {
        _that.$message.error(data.message)
      }
    },
    //初始化socket发生错误回调
    socketError () {
      this.$message.error("请检查设备或连接是否正常")
    },
    scanCard (scanType) {
      //申请人模块--0
      //联系人模块--1
      //Device：设备类型、
      let info = '{"device":"IdCard", "type":"' + scanType + '"}';
      //建立socket连接
      this.socketApi.initWebSocket(this.socketError);
      this.socketApi.sendSock(info, this.getConfigResult);
    },
    scanPicture (caseMaterialOid, index, item) {
      //caseMaterialOid--所属材料主键
      //index--所属操作行索引
      //item--所属操作行数据
      //Device：设备类型、
      this.scan_item = item;
      let info = '{"device":"HighCamera", "type":"' + caseMaterialOid + '"}';
      //建立socket连接
      this.socketApi.initWebSocket();
      this.socketApi.sendSock(info, this.getConfigResult);
    },
    openMaterialComparison (materiaitem) {
      /*   alert(JSON.stringify(this.materiaitem.attaList))*/
      let attaOids = "";
      let fastdfsNginxUrls = "";
      let maattaList = materiaitem.attaList;
      let attaLists = JSON.stringify(materiaitem.attaList)
      let sampleInfoOid = "";
      let comboDirectoryOid = "";
      if (materiaitem.comboDireMaterial.typeFlage == 0) {
        comboDirectoryOid = this.ruleForm.comboDireOid;
      }
      let materiaOid = materiaitem.materialOid;
      let caseMaterialOid = materiaitem.caseMaterialOid;
      let caseOid = "";
      let fileViewurls = process.env.BASE_URL + 'picture/comboPrviewList.html?materiaOid=' + materiaOid +
        '&sampleInfoOid=' + sampleInfoOid + '&comboDirectoryOid=' + comboDirectoryOid;
      let item = {
        show: true,
        title: '材料比对',
        sampleInfoOid: sampleInfoOid,
        comboDirectoryOid: comboDirectoryOid,
        caseMaterialOid: caseMaterialOid,
        materiaOid: materiaOid,
        fileViewurl: fileViewurls,
        caseOid: caseOid,
        attaOids: attaOids,
        attaLists: attaLists
      };
      this.materialComparisonOptions.push(item);
    },
    //表单填报
    formFilling (dataForm, index) {
      let _that = this;
      _that.indexForm = index;
      if (dataForm) {
        if (dataForm.formType == 0) { //自定义表单
          if (dataForm.formAddr) {
            window.open(dataForm.formAddr, 'width=1200px;height=800px;');
          } else {
            _that.$message.error("请配置表单地址！");
          }
        } else if (dataForm.formType == 1) { //电子表
          _that.iframeState = true;
          //查询电子表单地址配置
          _that.iframeUrl = process.env.VUE_APP_DZBD_TB_ROUTE_PATH + "&formOid=" + dataForm.formCode;
          if (_that.tempFormDataId[index]) {
            _that.iframeUrl += "&reportOid=" + _that.tempFormDataId[index];
          }
        }
      }
    },
    //修改表单关闭
    closeIframe (reportOid) {
      if (reportOid) {
        this.tempFormDataId[this.indexForm] = reportOid;
        reportOid = ""; //防止点击取消的关闭
      }
      this.iframeState = false;
    },
    //关闭表单预览
    closeIframeView () {
      this.iframVieweState = false;
    },
    //保存电子表单
    saveCaseForm () {
      let _that = this;
      if (_that.sxSerFormList) {
        let formData = [];
        let comboForm = {};
        _that.sxSerFormList.forEach((items, i) => {
          let caseForm = {};
          caseForm.regOid = _that.caseOid;
          caseForm.serFormOid = items.oid;
          caseForm.comboDireOid = _that.comboDireOid;
          caseForm.formDataId = _that.tempFormDataId[i];
          formData.push(caseForm)
        })
        comboForm.comboCaseFormList = formData;
        saveOrUpdateComboCaseForm(comboForm).then(response => {
          if (response.data) {
            let arr = response.data;
            arr.forEach((ite) => {
              _that.sxSerFormList.forEach((items, z) => {
                if (ite.serFormOid == items.oid) {
                  _that.$set(_that.caseForm, z, ite);
                }
              })
            })
          }
        })
      }
    },
    //查看电子表单
    viewFormFilling (items, index) {
      let _that = this;
      if (_that.sxSerFormList[index]) {
        if (_that.sxSerFormList[index].formType == 0) { //自定义表单
          if (_that.sxSerFormList[index].formAddr) {
            window.open(_that.sxSerFormList[index].formAddr, 'width=1200px;height=800px;');
          } else {
            _that.$message.error("请配置表单地址！");
          }
        } else if (_that.sxSerFormList[index].formType == 1) { //电子表单
          if (_that.tempFormDataId[index] == null) {
            _that.$message.error("未查询到表单！");
          } else {
            _that.iframVieweState = true;
            _that.iframeUrlView = process.env.VUE_APP_DZBD_CK_ROUTE_PATH + "&reportOid=" + _that.tempFormDataId[
              index];
          }
        }
      }
    },
    //预览附件
    viewFile (attaOid) {
      let item = {
        show: true,
        attaOid: attaOid
      };
      this.viewDialogOptions.push(item);
    },

    //关闭预览附件
    closeFileView () {
      this.viewDialogOptions.pop();
    },

    deleteFile (bean, atta) {
      delFile(bean.caseMaterialOid, atta.oid).then(response => {
        if (response.data != "") {
          var index = this.materialAttaList.findIndex(item => {
            if (item.attaOid == atta.oid) {
              return true;
            }
          })
          this.materialAttaList.splice(index, 1)
          var index2 = bean.attaList.findIndex(item2 => {
            if (item2.oid == atta.oid) {
              return true;
            }
          })
          bean.attaList.splice(index2, 1);
          //删除附件和智审目录关联
          if (this.materialCatalogAttaList != "") {
            this.materialCatalogAttaList.forEach((catalog, z) => {
              if (catalog) {
                if (catalog.attaOid == atta.oid) {
                  this.materialCatalogAttaList.splice(z, 1, {});
                }
              }

            })
          }
        }
      })
    },
    getElecLicenInfo (caseMaterialOid, materialOid, billOid, index) {
      let _that = this;
      let userName = "";
      let idCard = "";
      idCard = _that.ruleForm.credentialNumber;
      userName = _that.ruleForm.applyUserName;
      /* if (_that.cegisterType == "0"){
         idCard=_that.ruleForm.credentialNumber;
         userName= _that.ruleForm.applyUserName ;
       }
       if (_that.cegisterType == "1"){
         idCard=_that.ruleForm.contactCredentialNumber
         userName=_that.ruleForm.contactUserName
       }*/
      if (userName && idCard) {
        queryElecLicenseByDirCode(materialOid, userName, idCard, billOid).then(response => {
          if (response.data) {
            let res = {};
            let eleArr = [];
            res.originName = response.data.licenseNumber;
            res.attaOid = response.data.elecLicenOid;
            res.materialOid = caseMaterialOid;
            eleArr[0] = res;
            _that.$set(_that.attaList, index, eleArr);
            //判断elemLicenseList是否已经存在办件的证照信息
            if (_that.elemLicenseList) {
              _that.elemLicenseList.forEach((ite, i) => {
                if (ite.materialOid == caseMaterialOid) {
                  //移除原来的重新赋值
                  _that.elemLicenseList.splice(i, 1);
                }
              })
            }
            _that.elemLicenseList.push(res);
            //塞入上传附件
            if (_that.collectionTypeList.length > 0) {
              _that.collectionTypeList.forEach(collection => {
                if (collection.caseMaterialOid == caseMaterialOid) {
                  collection.attaOid = response.data.elecLicenOid;
                }
              });
            }
            //_that.$forceUpdate();
          } else {
            _that.$message.error("请检查证照相关配置！");
            return;
          }
        });
      } else {
        _that.$message.error("申请人/申请单位和证件号不能为空！");
        return;
      }
    },
    //电子证照预览
    viewElemsInfo (eleLincenseOid) {
      getElecLicenUrl(eleLincenseOid).then(response => {
        let urlElem = [];
        if (response.data) {
          urlElem = response.data;
          if (urlElem[0].viewOfdUrl) {
            window.open(urlElem[0].viewOfdUrl, 'width=1200px;height=800px;');
          } else {
            this.$message.error("暂时无法查看证照信息！");
          }
        }
      });
    },
    getUserInfo (credentialNumber) {
      if (!credentialNumber) {
        this.$message.error("证件号不能为空!")
        return false;
      }
      this.applyCardNum = credentialNumber;
      this.userInfoShow = true;
    },
    closeUserInfoShow (userCase) {
      this.ruleForm = userCase;
      this.ruleForm.caseOid = "";
      this.ruleForm.id = "";
      this.ruleForm.caseNumber = "";
      this.ruleForm.applyUserType = this.cegisterType;
      this.ruleForm.comboDireOid = this.comboDireOid;
      this.ruleForm.createUserOid = "";
      this.ruleForm.acceptanceDate = "";
      this.ruleForm.concludeDate = "";
      this.ruleForm.rqhbTime = "";
      this.ruleForm.sourceFlag = "";
      this.changeDeliveryWay(this.ruleForm.resultDeliveryWay);
      this.changeProxyFlag(this.ruleForm.proxyFlag);
      this.getDistrictTree("", this.ruleForm.bussVenueDistrictOid);
      this.getAddressTree(this.ruleForm.addresseeAddress);
      this.userInfoShow = false;
    },
    handleClose () {
      this.$refs.scanForm.getImageRes();
      this.dialogVisible = false;
    },

    //调用摄像头获取图像信息
    getImageCamera () {
      this.dialogVisible = true;
    },

  }
};
</script>
<style lang="scss" scoped>
.el-tree > .el-tree-node {
  min-width: 100%;
  display: inline-block;
}

.process-box {
  padding: 15px;
  box-sizing: border-box;
  text-align: left;
  position: relative;
}

.process-box .step-title {
  font-size: 14px;
  color: #333;
}

.process-box .step-title span {
  display: inline-block;
  vertical-align: middle;
  background: url(../../../../assets/image/step-lastbg.png) no-repeat center;
  width: 100px;
  height: 30px;
  line-height: 30px;
  text-align: center;
  cursor: pointer;
}

.process-box .step-title span:first-child {
  background: url(../../../../assets/image/step-firstbg.png) no-repeat center;
  width: 100px;
  height: 30px;
}

.process-box .step-title span.active {
  background: url(../../../../assets/image/step-lastbg-active.png) no-repeat
    center;
  width: 100px;
  height: 30px;
  color: #fff;
}

.process-box .step-title span:first-child.active {
  background: url(../../../../assets/image/step-firstbg-active.png) no-repeat
    center;
  width: 100px;
  height: 30px;
  color: #fff;
}

.process-box .title {
  font-size: 12px;
  color: #333;
  font-weight: normal;
  margin-top: 30px;
  text-align: left;
}

.select-list span {
  display: inline-block;
  vertical-align: middle;
  color: #47657d;
  font-size: 14px;
  background-color: #f1f5f9;
  height: 40px;
  line-height: 40px;
  margin: 0px 10px 10px 0px;
  padding: 0px 35px 0px 20px;
  border-radius: 5px;
  cursor: pointer;
}

.select-list span:first-child {
  background-color: #fff6f1;
  color: #2d506b;
}

.select-list span.current {
  background-color: #4d87b5;
  color: #fff;
  position: relative;
}

.select-list span.current:after {
  content: '';
  position: absolute;
  width: 19px;
  height: 13px;
  right: 10px;
  top: 13px;
  background: url(../../../../assets/image/check-icon.png) no-repeat center;
  background-size: cover;
}

.option-box .option-title {
  margin-top: 20px;
  font-size: 12px;
  color: #333;
}

.option-box .option-item {
  margin-right: 10px;
}

.option-box .option-item,
.option-box .chose-item {
  display: inline-block;
  vertical-align: top;
}

.option-box .chose-item {
  color: #4d87b5;
}

.process-box table {
  width: 100%;
}

.process-box table {
  border: 1px solid #dfe6ec;
  border-collapse: collapse;
}

.process-box table tr td,
.process-box table tr th {
  border: 1px solid #dfe6ec;
  padding: 17px 10px;
  font-size: 12px;
  color: #515a6e;
  text-align: right;
}

.process-box table tr td:nth-of-type(2n + 1) {
  background-color: #f8f8f9;
}

.process-box table tr td:nth-of-type(2n) {
  color: #606266;
  text-align: left;
}

.process-box table.data-table tr td,
.process-box table.data-table tr th {
  text-align: center;
  padding: 12px 10px;
}

.process-box table.data-table tr th {
  background-color: #f8f9fb;
}

.process-box table.data-table tr td:nth-of-type(2n + 1) {
  background: none;
}

.process-box table.data-table .bage-necessery {
  color: #ff3000;
  background-color: #fff2f2;
  height: 30px;
  line-height: 30px;
  padding: 0px 20px;
  border-radius: 20px;
  display: inline-block;
}

.process-box .check-list {
  border-bottom: 1px solid #e5e5e5;
  margin: 0px 20px;
  padding-left: 10px;
  box-sizing: border-box;
  padding-bottom: 20px;
}

.process-box .check-list h3 {
  font-size: 12px;
  color: #333;
}

.process-box .el-form-item {
  margin-bottom: 0;
}

.require {
  color: #ff0000;
  font-style: normal;
  font-size: 14px;
  display: inline-block;
  vertical-align: middle;
  margin-right: 5px;
}

.process-box .next-btn {
  display: block;
  margin: 30px auto;
  font-size: 14px;
}

.process-box .data-box h4 {
  font-size: 12px;
  color: #333;
  margin: 20px 0px 10px 0px;
}

.process-box .btn-wrap {
  text-align: center;
  margin: 30px 0;
}

.process-box .form-box-inline {
  display: inline-block;
  vertical-align: middle;
  border: 1px solid #2d506b;
  width: 100%;
  padding: 30px;
  box-sizing: border-box;
}

.process-box .el-button--info {
  background-color: #3b5f7b;
}

.process-box .el-button--info:hover {
  background-color: #426886;
}

.process-box .form-box-inline {
  position: relative;
}

.process-box .form-box-inline .btn-write {
  position: absolute;
  right: 20px;
  top: 20px;
  font-size: 12px;
  padding: 8px 15px;
}

.process-box .form-box-inline ul {
  margin: 0;
  padding: 0;
}

.process-box .form-box-inline ul li {
  padding: 10px 20px;
  background-color: #f3f6f9;
  border-radius: 10px;
  margin-bottom: 10px;
  list-style: none;
}

.process-box .form-box-inline ul li > .icon {
  display: inline-block;
  vertical-align: middle;
  width: 36px;
  height: 36px;
  line-height: 36px;
  text-align: center;
  border-radius: 100%;
  color: #fff;
  font-size: 20px;
}

.process-box .form-box-inline ul li > .bdbm-icon {
  background-color: #a8cfec;
}

.process-box .form-box-inline ul li > .bdsm-icon {
  background-color: #e6b893;
}

.process-box .form-box-inline .input-text {
  display: inline-block;
  vertical-align: middle;
  padding-left: 10px;
  font-size: 12px;
  color: #333;
}

.process-box .form-box-inline > h4 {
  font-weight: normal;
  color: #003259;
  font-size: 14px;
}

.process-box .form-box-inline .input-text > h4 {
  margin: 0px 0px 5px 0px;
  color: #3b5f7b;
  font-weight: normal;
}

.process-box .form-box-inline .input-text > p {
  margin: 0px;
  width: 476px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
@media screen and (max-width: 1600px) {
  .process-box .form-box-inline .input-text > p {
    width: 275px;
  }
}

.process-box .form-btn-group {
  position: absolute;
  right: 20px;
  top: 20px;
}

.process-box .form-btn-group .btn {
  border: 1px solid #097dd6;
  color: #097dd6;
  font-size: 12px;
  padding: 8px 15px;
}

.handle-data {
  padding: 25px 40px;
  background-color: #f8f9fb;
  position: relative;
}

.handle-data ul {
  padding: 0px;
  margin: 0;
}

.handle-data ul li {
  list-style: none;
  text-align: left;
  margin-top: 10px;
}

.handle-data .el-col-9 {
  margin-top: -6px;
}

.right-btn-group {
  position: absolute;
  right: 130px;
  top: 10px;
}

.right-btn-group-two {
  position: absolute;
  right: 10px;
  top: 10px;
}

.right-btn-group .el-button {
  padding: 7px 15px;
  font-size: 12px;
  margin-left: 5px;
  background-color: #0ba2b8;
  border: 1px solid #0ba2b8;
}

.right-btn-group .el-button:last-child {
  background-color: #47657d;
  border: 1px solid #47657d;
}

.right-btn-group .el-button:last-child:hover {
  background-color: #4d708b;
  border: 1px solid #4d708b;
}

.right-btn-group-two .el-form > .el-button {
  margin-top: 0 !important;
}

.qdcg-success {
  font-size: 12px;
  color: #00b45e;
  margin-top: 5px;
  text-decoration: underline;
}

.qdcg-fail {
  font-size: 12px;
  color: #ff0000;
  margin-top: 5px;
  text-decoration: underline;
}

.qdcg-success > img,
.qdcg-fail > img {
  display: inline-block;
  vertical-align: middle;
  margin: -2px 5px 0 0;
}

.qdcg-text img {
  margin-right: 5px;
  margin-top: -4px;
  float: left;
}

.qdcg-text p {
  width: 86%;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}

.qdcg-text p > span {
  font-size: 12px;
  color: #999;
  padding-left: 20px;
}

.qdcg-btn .el-button {
  color: #333;
}

.qdcg-btn {
  margin-top: -5px;
}

.input-number {
  text-align: left;
  margin-top: 20px;
  margin-bottom: 10px;
}

.input-number-label {
  display: inline-block;
  vertical-align: middle;
  margin-right: 20px;
}

.rightSideBar {
  position: fixed;
  right: 0px;
  bottom: 100px;
  box-shadow: -1px 1px 10px #ddd;
  border-top-left-radius: 5px;
  border-bottom-left-radius: 5px;
}

.rightSideBar .sideItem {
  width: 42px;
  height: 42px;
  line-height: 42px;
  text-align: center;
  color: #0b76c7;
  background-color: #fff;
  border-top-left-radius: 5px;
  cursor: pointer;
  font-size: 20px;
  position: relative;
  -webkit-transition: all 0.3s ease-in-out 0.1s;
  transition: all 0.3s ease-in-out 0.1s;
  z-index: 2;
}

.rightSideBar .sideItem:last-child {
  border-top-left-radius: 0px;
  border-bottom-left-radius: 5px;
}

.rightSideBar .sideItem:hover {
  background-color: #0b76c7;
  color: #fff;
  border-top-left-radius: 0px;
  border-bottom-left-radius: 0px;
}

.rightSideBar .sideItem .sideText {
  position: absolute;
  left: 42px;
  top: 0px;
  height: 42px;
  line-height: 42px;
  padding: 0px 10px 0px 20px;
  border-top-left-radius: 30px;
  border-bottom-left-radius: 30px;
  color: #fff;
  background-color: #0b76c7;
  font-size: 14px;
  -webkit-transition: left 0.3s ease-in-out 0.1s;
  transition: left 0.3s ease-in-out 0.1s;
  z-index: 1;
}

.rightSideBar .sideItem:hover .sideText {
  left: -86px;
}

.title-detail {
  text-align: center;
}
.data-btn {
  position: absolute;
  right: 20px;
  top: 20px;
  font-size: 14px;
}

table {
  border-collapse: collapse;
}
.el-radio {
  margin-right: 12px;
}
</style>
