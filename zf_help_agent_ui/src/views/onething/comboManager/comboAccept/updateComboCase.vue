/** * 窗口受理管理 * @author: wangxl * @date: 2020-12-1 */
<template>
  <div>
    <!-- 步骤 -->
    <!--    <el-dialog :close-on-click-modal="false" @close="closeDialog" :title="title" :visible.sync="openInit" v-if="openInit" width="1300px"
                   append-to-body>-->
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
          <!-- <h3 class="title"><i class="el-icon-s-grid"></i></h3> -->
          <div class="zf-zc-table--title">情形选择</div>
          <div class="select-list">
            <span>{{ situationName }}</span>
          </div>
        </div>
        <div class="option-box">
          <div class="option-title">
            <!-- <div class="option-item"><i class="el-icon-s-grid"></i></div> -->
            <div class="zf-zc-table--title">选项信息</div>

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
                <div
                  v-if="
                    direSituationOptionTitles &&
                      direSituationOptionTitles.length > 0
                  "
                >
                  <div
                    v-for="(data, index) in direSituationOptionTitles"
                    :key="index"
                  >
                    <div class="check-list">
                      <h3>{{ data.titleName }}</h3>
                      <div
                        v-for="(item, idx) in data.comboOptionVals"
                        :key="idx"
                      >
                        {{ item.name }}
                      </div>
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
          <!-- <h3 class="title"><i class="el-icon-s-grid"></i></h3> -->
          <div class="zf-zc-table--title">材料核验列表</div>

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
                <td><b>服务对像：</b></td>
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
          <div class="data-box">
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
              <tbody v-if="comboDireMaterials && comboDireMaterials.length > 0">
                <tr v-for="(data, index) in comboDireMaterials" :key="index">
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
            <!-- <h3 class="title"><i class="el-icon-s-grid"></i>申请人/申请单位信息</h3> -->
            <div class="zf-zc-table--title">申请人/申请单位信息</div>
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
              <!--<el-input v-show="false" v-model="ruleForm.id" />
              <el-input v-show="false" v-model="ruleForm.caseOid" />
              <el-input v-show="false" v-model="ruleForm.caseNumber" />
              <el-input v-show="false" v-model="ruleForm.delFlag" />
              <el-input v-show="false" v-model="ruleForm.createUserOid" />
              <el-input v-show="false" v-model="ruleForm.applyUserType" />
              <el-input v-show="false" v-model="ruleForm.caseStatus" />
              <el-input v-show="false" v-model="ruleForm.comboDireOid" />
              <el-input v-show="false" v-model="ruleForm.comboDireMaterials" />
              <el-input v-show="false" v-model="ruleForm.valList" />
              <el-input v-show="false" v-model="ruleForm.updateFlag" />-->
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
                  <el-form-item prop="applyUserName">
                    <el-col :span="24">
                      <el-input
                        v-model="ruleForm.applyUserName"
                        name="applyUserName"
                        maxlength="50"
                        show-word-limit
                      >
                      </el-input>
                    </el-col>
                  </el-form-item>
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
                        v-model="ruleForm.applyUserTel"
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
                        v-model="ruleForm.applyUserAddress"
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
                        v-model="ruleForm.legalPersonName"
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
            <!-- <h3 class="title"><i class="el-icon-s-grid"></i></h3> -->
            <div class="zf-zc-table--title">办件信息</div>
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
                        v-model="ruleForm.projectName"
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
                  <el-form-item prop="bussVenueDistrictOid">
                    <el-col :span="24">
                      <treeselect
                        :multiple="true"
                        :options="districtOptions"
                        :flat="true"
                        :default-expand-level="1"
                        placeholder="请选择业务管辖地"
                        v-model="bussVenueDistrictOidChoose"
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
                        v-model="ruleForm.specificLocation"
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
                        v-model="ruleForm.investProjecName"
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
                        v-model="ruleForm.investProjectCode"
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
                        v-model="ruleForm.projectAbstract"
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
                        v-model="ruleForm.applyPostCode"
                        name="applyPostCode"
                        maxlength="6"
                        show-word-limit
                      >
                      </el-input>
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
                        v-model="ruleForm.contactUserName"
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
                        v-model="ruleForm.contactCredentialNumber"
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
                        v-model="ruleForm.contactEmail"
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
                        v-model="ruleForm.contactUserPhone"
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
                        v-model="ruleForm.contactUserTel"
                        name="contactUserTel"
                        maxlength="13"
                        show-word-limit
                      >
                      </el-input>
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
                        v-model="ruleForm.contactRemark"
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
            <!-- <h3 class="title"><i class="el-icon-s-grid"></i></h3> -->
            <div class="zf-zc-table--title">送达信息</div>

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
                    style="float:left;margin-bottom:2px;width:80%"
                  >
                    <el-col :span="24">
                      <el-input
                        v-model="ruleForm.addresseeName"
                        name="addresseeName"
                        maxlength="50"
                        show-word-limit
                      >
                      </el-input>
                    </el-col>
                  </el-form-item>
                  <el-button type="primary" @click="getApplyInfo"
                    >提取</el-button
                  >
                </td>
                <td><i class="require">*</i><b>收件人邮政编码：</b></td>
                <td>
                  <el-form-item prop="addresseePostCode">
                    <el-col :span="24">
                      <el-input
                        v-model="ruleForm.addresseePostCode"
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
                        v-model="ruleForm.addresseePhone"
                        name="addresseePhone"
                        maxlength="11"
                        show-word-limit
                      >
                      </el-input>
                    </el-col>
                  </el-form-item>
                </td>
                <td><b>收件人电话：</b></td>
                <td>
                  <el-form-item prop="addresseeTel">
                    <el-col :span="24">
                      <el-input
                        v-model="ruleForm.addresseeTel"
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
                      ></el-input>
                    </el-col>
                  </el-form-item>
                </td>
              </tr>
            </table>
          </div>
          <div class="step-third-box" v-if="formConfig_show">
            <!-- <h3 class="title"><i class="el-icon-s-grid"></i></h3> -->
            <div class="zf-zc-table--title">表单信息</div>
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
                      :key="i"
                    >
                      <div class="form-box-inline">
                        <h4>1、{{ dataForm.formName }}</h4>
                        <div class="form-btn-group" v-if="tempFormDataId[i]">
                          <el-button
                            icon="el-icon-view"
                            class="btn"
                            @click="viewFormFilling(caseForm[i], i)"
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
                              style="background-color: #868380;"
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
                                  style="cursor: pointer;"
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
              <el-button type="primary" icon="el-icon-back" @click="next(1, -1)"
                >上一步</el-button
              >
              <el-button
                type="info"
                icon="el-icon-video-pause"
                class="print-btn"
                @click="checkPbpjInfo('')"
                >暂存
              </el-button>
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
        <!-- <h3 class="title"><i class="el-icon-s-grid"></i></h3> -->
        <div class="zf-zc-table--title">申请者相关信息</div>
        <el-form
          ref="materialForm"
          :model="materialForm"
          :rules="rules"
          label-width="0px"
          class="demo-materialForm"
          :label-position="labelPosition"
        >
          <table
            cellspacing="0"
            cellpadding="0"
            border="0"
            class="data-table"
            ref="materialTable"
          >
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
              <th>操作</th>
            </tr>
            <tbody v-for="(item, index) in comboCaseMaterials" :key="index">
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
                        @click="openMaterialComparison(item)"
                        icon="el-icon-document"
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
                    <ul v-for="(data, i) in item.attaList" :key="i">
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
                            <span
                              v-for="(catalog,
                              index) in item.materialCatalogList"
                              :key="index"
                            >
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
                    <ul v-for="(data, i) in item.attaList" :key="i">
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
                    <ul v-for="(data, idx) in attaList[index]" :key="idx">
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
            <!-- <h3 class="title"><i class="el-icon-s-grid"></i></h3> -->
            <div class="zf-zc-table--title">容缺补正</div>
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
                      type="date"
                      value-format="yyyy-MM-dd"
                      :picker-options="pickerrqhbTime"
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
        <!-- <h3 class="title"><i class="el-icon-s-grid"></i></h3> -->
        <div class="zf-zc-table--title">申请者相关信息</div>
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
                      v-model="acceptForm.acceptOpinionDesc"
                      name="acceptOpinionDesc"
                      maxlength="500"
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
      <!--表单嵌套页面-->
      <el-dialog
        v-dialog-drag
        title="电子表单"
        :visible.sync="iframeState"
        width="80%"
        height="100%"
        v-if="iframeState"
        :modal-append-to-body="true"
        append-to-body
      >
        <iframe-url
          :outLink="iframeUrl"
          @closeIframe="closeIframe"
        ></iframe-url>
      </el-dialog>

      <el-dialog
        v-dialog-drag
        title="电子表单预览"
        :visible.sync="iframVieweState"
        width="80%"
        v-if="iframVieweState"
        :modal-append-to-body="true"
        append-to-body
        height="100%"
      >
        <iframe-url-view
          :outLink="iframeUrlView"
          @closeIframeView="closeIframeView"
        ></iframe-url-view>
      </el-dialog>
    </div>
    <!--</el-dialog>-->
    <!--引入文件的预览弹出层-->
    <el-dialog
      v-dialog-drag
      title="文件预览"
      :visible.sync="view.show"
      v-for="view in viewDialogOptions"
      :key="view.attaOid"
      width="900px"
      append-to-body
    >
      <combo-case-file-view
        :attaOid="view.attaOid"
        @father-click="closeFileView"
      ></combo-case-file-view>
    </el-dialog>

    <el-dialog
      v-dialog-drag
      :visible.sync="item.show"
      v-for="item in materialComparisonOptions"
      :key="item.caseMaterialOid"
      @close="outerVisible"
      :title="item.title"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <!--  <iframe :src=fileViewurlList  rameborder="0" width="100%" height="600px"  @father-click="closeFileView"  ></iframe>-->

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
    </el-dialog>

    <!--套餐智审结果-->
    <el-dialog
      v-dialog-drag
      :visible.sync="item.show"
      v-for="item in comboIntelligentPretrialOptions"
      :key="item.caseOid"
      @close="outerVisible"
      :title="item.title"
      width="900px"
      height="600px"
      scrollbar
      append-to-body
    >
      <combo-intelligent-pretrial
        :caseOid="item.caseOid"
        :caseMaterialOid="item.caseMaterialOid"
        :caseFileRecOid="item.caseFileRecOid"
        :cataOid="item.cataOid"
        :title="item.title"
        @father-click="handleChildView"
      ></combo-intelligent-pretrial>
    </el-dialog>
  </div>
</template>
<script>
import {
  getCaseSituationList,
  getCertificateType,
  saveComboCase,
  uploadCaseMaterialFile,
  saveCaseMaterialAtta,
  saveCaseAccpet,
  pushPbpjCheckLogin,
  regComboCaseInfo,
  pushPbpjUser,
  comboCaseCallBack,
  changeCredentialType
} from "@/api/onething/comboManager/comboAccept/updateComboCase";
import IframeUrl from "@/views/iframe/formIndex";
import IframeUrlView from "@/views/iframe/formIndexView";
import { queryDistrictSimpleTree } from "@/api/sys/district";
import { delFile } from "@/api/onething/comboManager/comboAccept/comboCaseAtta";
import {
  validatePhone,
  validateTel,
  validIDCard,
  validatePostCode,
  validateEmails,
  validUnifiedCredit
} from "@/utils/validate";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { VueCropper } from "vue-cropper";
import packageMaterialComparison from "@/views/onething/clzs/materialCheckKeyManage/packageMaterialComparison";
import {
  saveOrUpdateCaseForm,
  selectBySxSerForm,
  caseFormByCase,
  saveOrUpdateComboCaseForm
} from "@/api/zc/businessManagement/formConfig";
import comboCaseFileView from "@/views/onething/comboManager/comboAccept/comboCaseFileView";
import {
  getElecLicenUrl,
  queryElecLicenseByDirCode
} from "@/api/zc/businessManagement/elemLice";
import {
  intelligentPretrial,
  intelligentPretrialmaterialPrePrial,
  viewResult
} from "@/api/onething/comboManager/comboAccept/comboIntelligentPretrial";
import comboIntelligentPretrial from "@/views/onething/clzs/directoryManagement/comboIntelligentPretrial";
/*  import {regionDataPlus} from "element-china-area-data";*/
import { regionData } from "element-china-area-data";
import { checkApplyUserInDishonest } from "@/api/onething/comboManager/comboAccept/combocaseRqbz";
import {
  getloginUser,
  pbpjSaveCaseInfo,
  pushPbpjInfo
} from "@/api/onething/comboManager/comboAccept/initComboCase";
import { importScript } from "../../../../api/sys/util";

export default {
  inheritAttrs: false,
  components: {
    Treeselect,
    VueCropper,
    comboCaseFileView,
    packageMaterialComparison,
    IframeUrl,
    IframeUrlView,
    comboIntelligentPretrial
  },
  name: "updateCaseInfo",
  //定义获取父类传过来值的格式
  props: ["comboCaseOid", "comboDireOid"],
  data() {
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
      address_show: true,
      proxy_show: false,
      compare_show: false,
      rqhb_show: false,
      show_upload: [],
      show_scan: [],
      scan_item: {},
      show_elem: [],
      //材料比对页面
      materialComparisonOptions: [],
      //材料智审
      comboIntelligentPretrialOptions: [],
      radio1: [],
      radio2: "1",
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 事项情形数据
      situationList: [],
      // 事项情形与选项关系数据
      direSituationOptionTitles: [],
      // 事项选项值数据
      situationOptionVals: [],
      viewDialogOptions: [],
      //查询办件主键
      id: "",
      //查询办件业务主键
      caseOid: "",
      //办件状态
      caseStatus: "",
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
      //事项材料
      comboDireMaterials: [],
      //办件材料
      comboCaseMaterials: [],
      elemLicenseList: [],
      mateOptRels: [],
      materialOptList: [],
      formConfig_show: false, //标识表单是否显示
      iframeState: false,
      iframVieweState: false,
      iframeUrl: {}, //表单地址
      iframeUrlView: {}, //查看
      sxSerFormList: [], //多个表单信息
      caseForm: [],
      tempFormDataId: [], //临时存放表单填报的返回值
      indexForm: 0,

      situationOid: null,
      situationName: "默认自定情形",
      fileList: [],
      catalogCheckList: [],
      catalogList: [],
      materialCatalogAttaList: [],
      showFileList: false,
      accept: {
        type: String,
        default: ".jpg,.jpeg,.png,.pdf,.PDF,.doc,.docx,.DOC,.DOCX"
      },
      //附件集合
      attaList: [],
      materialCatalogList: [],
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
      bussVenueDistrictOids: "",
      addresseeAddressOids: "",
      // 弹出层标题
      title: "办件修改",
      // 显示弹出层
      openInit: true,
      // 查看显示弹出层
      //openView: false,
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
        resultDeliveryWay: "1",
        //deliverFlag: 1,
        proxyFlag: 0,
        valList: [],
        comboDireMaterials: [],
        addresseeAddress: ""
      },
      bussVenueDistrictOidChoose: [],
      addresseeAddressChoose: "",
      pbpjCaseOkUrl: "",
      comboDireForm: {},
      optionForm: {},
      formData: {},
      materialForm: {
        rqhbTime: ""
      },
      acceptForm: {
        ifAccept: 1
      },
      labelPosition: "top",
      comboDireSituationOid: "",
      checkList: [],
      stepData: [
        {
          index: "0",
          title: "情形选择"
        },
        {
          index: "1",
          title: "材料核验"
        },
        {
          index: "2",
          title: "信息登记"
        },
        {
          index: "3",
          title: "收取材料"
        },
        {
          index: "4",
          title: "进入受理"
        }
      ],
      selectData: [],
      //失信人标志
      dishonestFlag: true,
      // 表单校验
      rules: {
        projectName: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          },
          {
            min: 3,
            max: 100,
            message: "长度在 3 到 100 个字符",
            trigger: "blur"
          }
        ],
        applyPostCode: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          },
          {
            validator: validatePostCode,
            trigger: "blur"
          }
        ],
        bussVenueDistrictOid: [
          {
            required: true,
            message: "请选择业务管辖地",
            trigger: "blur"
          }
        ],
        specificLocation: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          }
        ],
        applyUserName: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          }
        ],
        applyUserPhone: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          },
          {
            validator: validatePhone,
            trigger: "blur"
          }
        ],
        applyUserTel: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          },
          {
            validator: validateTel,
            message: "请输入正确的申请人/单位电话",
            trigger: "blur"
          }
        ],
        credentialType: [
          {
            required: true,
            message: "请选择证件类型",
            trigger: "change"
          }
        ],
        credentialNumber: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          }
        ],
        legalPersonName: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          }
        ],
        contactUserName: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          },
          {
            min: 3,
            max: 100,
            message: "长度在 3 到 100 个字符",
            trigger: "blur"
          }
        ],
        contactCredentialNumber: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          },
          {
            validator: validIDCard,
            trigger: "blur"
          }
        ],
        contactUserPhone: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          },
          {
            validator: validatePhone,
            trigger: "blur"
          }
        ],
        contactUserTel: [
          {
            validator: validateTel,
            trigger: "blur"
          }
        ],
        contactEmail: [
          {
            validator: validateEmails,
            trigger: "blur"
          }
        ],
        addresseeName: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          }
        ],
        addresseePostCode: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          },
          {
            validator: validatePostCode,
            trigger: "blur"
          }
        ],
        addresseePhone: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          },
          {
            validator: validatePhone,
            trigger: "blur"
          }
        ],
        addresseeTel: [
          {
            validator: validateTel,
            message: "请输入正确的收件人电话",
            trigger: "blur"
          }
        ],
        addresseeAddress: [
          {
            required: true,
            message: "请选择收件人地址",
            trigger: "change"
          }
        ],
        addresseeDetailAddress: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          }
        ],
        rqhbTime: [
          {
            required: true,
            message: "请选择容缺补正到期时间",
            trigger: "blur"
          }
        ]
      },
      pickerrqhbTime: {
        disabledDate: time => {
          const beginDateVal = new Date(
            new Date() - 24 * 60 * 60 * 1000
          ).getTime();
          if (beginDateVal) {
            return time.getTime() < beginDateVal - 0;
          }
        }
      }
    };
  },
  watch: {},
  computed: {},
  created() {
    this.queryLoginInfo();
    //一件事情形获取
    this.getComboCaseSituationList();
    //this.queryLoginInfo();
    this.next(1, 1);
    this.next(2, 1);
  },
  methods: {
    next(index, count) {
      if (count > 0) {
        if (index == 1) {
          this.i = index;
          this.show_0 = false;
          this.show_1 = true;
        }
        if (index == 2) {
          this.getCheckbox(index);
        }
        if (index == 3) {
          // this.show_2=false;
          // this.show_3=true;
          // this.show_upload=[];
          // this.show_scan=[];
          //保存办件信息
          //this.saveComboCaseForm(index);
          this.checkPbpjInfo(index);
        }
        if (index == 4) {
          //this.i = index;
          // this.show_3 = false;
          // this.show_4 = true;
          this.catalogList = [];
          this.saveMaterialAtta(index);
        }
      } else {
        this.i = index;
        if (index == 0) {
          this.show_0 = true;
          this.show_1 = false;
        }
        if (index == 1) {
          this.show_1 = true;
          this.show_2 = false;
        }
        if (index == 2) {
          this.attaList = [];
          this.catalogList = [];
          this.show_2 = true;
          this.show_3 = false;
          this.getDistrictTree("", this.bussVenueDistrictOids);
          this.getAddressTree(this.ruleForm.addresseeAddress);
        }
        if (index == 3) {
          this.show_3 = true;
          this.show_4 = false;
          //清空材料比对list
          this.catalogList = [];
        }
      }
    },
    /** 登录信息 */
    queryLoginInfo() {
      let _that = this;
      getloginUser().then(response => {
        _that.loginUser = response.data;
      });
    },
    /** 获取区划树 */
    getDistrictTree(districtOid, bussVenueDistrictOids) {
      let _that = this;
      let oids = [];
      if (
        bussVenueDistrictOids != "" &&
        bussVenueDistrictOids != null &&
        bussVenueDistrictOids != "null"
      ) {
        let districtOids =
          bussVenueDistrictOids != "" ? bussVenueDistrictOids.split(",") : [];
        for (let oid of districtOids) {
          if (oid != "") {
            if (!oid.indexOf("DISTRICT-") > -1) {
              oid = "DISTRICT-" + oid;
            }
            oids.push(oid);
          }
        }
        _that.bussVenueDistrictOidChoose = oids;
      }
      queryDistrictSimpleTree(districtOid).then(response => {
        _that.districtOptions = response.data;
      });
    },
    /** 核验材料勾选 */
    getCheckbox(index) {
      this.i = index;
      this.show_1 = false;
      this.show_2 = true;
      let tempForm = [];
      selectBySxSerForm(this.comboDireOid).then(response => {
        if (response.data) {
          this.formConfig_show = true;
          this.sxSerFormList = response.data;
          //this.sxSerForm.regOid=this.caseOid;
          //查询办件表单是否存在
          caseFormByCase(this.comboDireOid, this.comboCaseOid).then(
            response => {
              if (response.data) {
                tempForm = response.data;
              }
              if (tempForm) {
                //循环事项表单配置添加表单关联信息
                this.sxSerFormList.forEach((items, i) => {
                  tempForm.forEach((childItem, j) => {
                    if (childItem.serFormOid == items.oid) {
                      this.caseForm[i] = childItem;
                      this.tempFormDataId[i] = childItem.formDataId;
                    }
                  });
                });
                this.$forceUpdate();
              }
            }
          );
        }
      });
    },
    /** 获取证件类型 */
    getSelectCertificateType() {
      getCertificateType(this.cegisterType).then(response => {
        this.certificateTypeList = response.data;
      });
    },
    /** 已有地址加载 */
    getAddressTree(addresseeAddress) {
      let _that = this;
      let address = [];
      if (addresseeAddress != "" && addresseeAddress != null) {
        let addressIds =
          addresseeAddress != ""
            ? addresseeAddress.replace('["', "").replace('"]', "")
            : "";
        if (addressIds != "") {
          address = addressIds.split('","');
        }
      }
      _that.ruleForm.addresseeAddress = address;
    },
    /** 改变证件类型 */
    changeType(item) {
      let _that = this;
      changeCredentialType(item).then(response => {
        if (response.data) {
          _that.cardType = response.data.code;
          let type = {};
          _that.rules.credentialNumber.forEach((item, index) => {
            if (_that.cardType == "SFZ") {
              type.validator = validIDCard;
              type.trigger = "blur";
              _that.rules.credentialNumber.push(type);
            } else if (_that.cardType == "XYDMZ") {
              type.validator = validUnifiedCredit;
              type.trigger = "blur";
              _that.rules.credentialNumber.push(type);
            } else {
              if (index == 1) {
                _that.rules.credentialNumber.splice(
                  _that.rules.credentialNumber.indexOf(item),
                  1
                );
              }
            }
          });
        }
      });
    },
    /** 事项情形获取 */
    getComboCaseSituationList() {
      let _that = this;
      //查询事项情形
      getCaseSituationList(_that.comboCaseOid).then(response => {
        this.situationName = response.data.comboSituations
          ? response.data.comboSituations
          : this.situationName;
        let optionTitles = response.data.comboOptionTitleList;
        if (optionTitles) {
          optionTitles.forEach(title => {
            if (title.comboOptionVals) {
              title.comboOptionVals.forEach(val => {
                _that.checkList.push(val.valOid);
              });
            }
          });
        }

        _that.getSituationOpinionTitle(optionTitles);
        //给办件相关赋值 data.comboCase
        _that.comboDireOid = response.data.comboCase.comboDireOid;
        _that.cegisterType = response.data.comboCase.applyUserType;
        //获取证照类型
        _that.getSelectCertificateType();
        //获取一件事目录信息
        _that.comboDireForm = response.data.comboDirectory;
        _that.ruleForm = response.data.comboCase;
        //初始化证件号码是否验证
        _that.changeType(_that.ruleForm.credentialType);
        //获取一件事材料
        _that.comboDireMaterials = response.data.directoryMaterials;
        //办件信息
        _that.id = response.data.comboCase.id;
        _that.comboCaseOid = response.data.comboCase.caseOid;
        _that.caseNumber = response.data.comboCase.caseNumber;
        _that.caseStatus = response.data.comboCase.caseStatus;
        _that.materialForm.rqhbTime = response.data.comboCase.rqhbTime;
        //办件材料信息
        _that.comboCaseMaterials = response.data.comboCase.comboCaseMaterials;
        if (_that.comboCaseMaterials) {
          _that.$nextTick(() => {
            _that.materialAttaList = [];
            _that.comboCaseMaterials.forEach(mat => {
              let bb = new Object();
              bb.collectionType = mat.collectionType;
              bb.caseMaterialOid = mat.caseMaterialOid;
              bb.collectionNumber = mat.collectionNumber;
              bb.attaOid = mat.attaList;
              _that.collectionTypeList.push(bb);
              mat.attaList.forEach(att => {
                let at = new Object();
                at.caseMaterialOid = mat.caseMaterialOid;
                at.attaOid = att.oid;
                _that.materialAttaList.push(at);
                if (att.materialCatalogOid) {
                  //保存材料与智审目录关系
                  this.chooseCatalog(
                    mat.caseMaterialOid,
                    att.oid,
                    att.materialCatalogOid
                  );
                }
              });
              _that.radio1[mat.caseMaterialOid] = mat.collectionType;
              if (mat.collectionType == "4") {
                _that.rqhb_show = true;
                _that.rqhbMaterialList.push(mat.caseMaterialOid);
              }
            });
          });
        }
        _that.addresseeAddressOids = response.data.comboCase.addresseeAddress;
        _that.bussVenueDistrictOids =
          response.data.comboCase.bussVenueDistrictOid;
        _that.changeDeliveryWay(response.data.comboCase.resultDeliveryWay);
        _that.changeProxyFlag(response.data.comboCase.proxyFlag);
        _that.getDistrictTree("", response.data.comboCase.bussVenueDistrictOid);
        _that.getAddressTree(response.data.comboCase.addresseeAddress);
      });
    },
    /** 填充选项 */
    getSituationOpinion(situation) {
      if (situation) {
        // situations.forEach((item, key) => {
        // let situation = {};
        // situation.index = key + 1;
        //situation.title = item.situationName;
        //situation.oid = item.situationOid;
        this.situationName = situation.situationName;
        this.selectData.push(situation);
        // });
      }
    },
    /** 填充标题和选项值 */
    getSituationOpinionTitle(optionTitles) {
      if (optionTitles) {
        optionTitles.forEach(optionTitle => {
          let titleValues = {};
          titleValues.titleName = optionTitle.name;
          titleValues.titleOid = optionTitle.oid;
          titleValues.comboOptionVals = optionTitle.comboOptionVals;
          this.direSituationOptionTitles.push(titleValues);
        });
      }
    },
    //验证表单以及坚持平板评价信息
    checkPbpjInfo(index) {
      let _that = this;
      _that.$refs["ruleForm"].validate(valid => {
        if (valid) {
          //验证表单是否必填
          if (_that.sxSerFormList) {
            let flagMust = false;
            _that.sxSerFormList.forEach((ite, i) => {
              if (ite.isFormFlag == 1) {
                //必填
                if (!_that.tempFormDataId[i]) {
                  flagMust = true;
                }
              }
            });
            if (flagMust) {
              _that.$message.error("表单未填写,请填写表单！");
              return false;
            }
          }
          //验证人员是否启用评价器
          _that.pbpjConfirmUser(index, _that.ruleForm);
        }
      });
    },
    //查询当前登录人员是否开启平板确认或者评价
    pbpjConfirmUser(index, ruleForm) {
      let _that = this;
      if (!index) {
        //暂存
        _that.saveComboCaseForm(index);
      } else {
        pushPbpjUser(_that.loginUser.userOid).then(response => {
          if (response.data != "") {
            if (response.data.confirmFlag == "1") {
              //推送平板评价办件确认信息
              _that.confirmPbpj(index, ruleForm);
            } else {
              //没用启用评价 直接保存办件
              _that.saveComboCaseForm(index);
            }
          }
        });
      }
    },

    //平板评价确认信息
    confirmPbpj(index, ruleForm) {
      let _that = this;
      let url = window.location.origin;
      _that.pbpjCaseOkUrl = url;
      pushPbpjCheckLogin().then(response => {
        if (response.data) {
          if (this.ruleForm.addresseeAddress) {
            if (typeof this.ruleForm.addresseeAddress != "string") {
              let address = "[";
              this.ruleForm.addresseeAddress.forEach(val => {
                address += '"' + val + '",';
              });
              if (this.ruleForm.addresseeAddress.length > 0) {
                address = address.substring(0, address.lastIndexOf(","));
              }
              address += "]";
              this.ruleForm.addresseeAddress = address;
            }
          }
          //检查平板评价登录
          _that
            .$confirm("你确定要进行办件信息确认吗？", "警告", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning"
            })
            .then(function() {
              regComboCaseInfo(ruleForm, _that.pbpjCaseOkUrl).then(response => {
                if (response.data) {
                  _that.msgWarning("正在信息确认...");
                  _that.loading = true;
                  if (_that.getDataCheckValue(1, index)) {
                    _that.loading = false;
                  }
                }
              });
            })
            .catch(action => {
              if (action === "cancel") {
                _that.saveComboCaseForm(index);
              }
            });
        } else {
          _that.msgWarning("平板评价未登录，请登录平板评价器！");
        }
      });
    },
    //平板评价确认
    getDataCheckValue(num, index) {
      let _that = this;
      comboCaseCallBack().then(response => {
        if (response.data == "1") {
          //保存办件
          this.saveComboCaseForm(index);
        } else if (response.data == "0") {
          _that.msgError("确认信息有误，请重新修改办件信息！");
          return false;
        } else {
          if (num >= 60) {
            //60s后自动保存
            //保存办件
            _that.saveComboCaseForm(index);
          } else {
            //每隔一秒钟获取一次评价值，35秒后未获取到值，默认为未确定
            setTimeout(function() {
              _that.getDataCheckValue(num + 1, index);
            }, 1000);
          }
        }
      });
    },
    /** 办件信息下一步保存 */
    saveComboCaseForm(index) {
      this.$refs["ruleForm"].validate(valid => {
        if (valid) {
          this.ruleForm.id = this.id;
          this.ruleForm.caseOid = this.comboCaseOid;
          this.ruleForm.caseNumber = this.caseNumber;
          this.ruleForm.caseStatus = this.caseStatus;
          this.ruleForm.applyUserType = this.cegisterType;
          this.ruleForm.comboDireOid = this.comboDireOid;
          this.ruleForm.comboDireMaterials = this.comboDireMaterials;
          this.ruleForm.updateFlag = "Y";
          //收件人地址
          if (this.ruleForm.addresseeAddress) {
            if (typeof this.ruleForm.addresseeAddress != "string") {
              let address = "[";
              this.ruleForm.addresseeAddress.forEach(val => {
                address += '"' + val + '",';
              });
              if (this.ruleForm.addresseeAddress.length > 0) {
                address = address.substring(0, address.lastIndexOf(","));
              }
              address += "]";
              this.ruleForm.addresseeAddress = address;
            }
          }

          //业务管辖地
          if (this.bussVenueDistrictOidChoose) {
            let districtOid = [];
            let oids = "";
            districtOid = this.bussVenueDistrictOidChoose;

            districtOid.forEach(oid => {
              oids += oid + ",";
            });
            this.ruleForm.bussVenueDistrictOid = oids;
          }
          saveComboCase(this.ruleForm).then(response => {
            if (response.data != "") {
              if (!index) {
                this.$emit("closeUpdate");
                return false;
              }
              this.comboCaseOid = response.data.caseOid;
              this.id = response.data.id;
              this.caseNumber = response.data.caseNumber;
              this.comboCaseMaterials = response.data.comboCaseMaterials;
              if (
                this.comboDireMaterials &&
                this.comboDireMaterials.length > 0 &&
                this.comboCaseMaterials &&
                this.comboCaseMaterials.length > 0
              ) {
                this.comboCaseMaterials.forEach((item, index) => {
                  this.comboDireMaterials.forEach((ite, i) => {
                    if (item.materialOid == ite.materialOid) {
                      item.comboDireMaterial = ite;
                    }
                  });
                });
              }
              if (this.comboCaseMaterials) {
                this.$nextTick(() => {
                  this.comboCaseMaterials.forEach((ma, j) => {
                    if (ma.collectionType == "2") {
                      this.show_upload[ma.caseMaterialOid] = true;
                    } else if (ma.collectionType == "3") {
                      this.show_scan[ma.caseMaterialOid] = true;
                    } else if (ma.collectionType == "5") {
                      this.show_elem[ma.caseMaterialOid] = true;
                      if (ma.elemLicenseOid) {
                        let res = {};
                        let eleArr = [];
                        res.originName = ma.elemNumber;
                        res.attaOid = ma.elemLicenseOid;
                        res.materialOid = ma.caseMaterialOid;
                        eleArr[0] = res;
                        this.attaList[j] = eleArr;
                        //判断elemLicenseList是否已经存在办件的证照信息
                        if (this.elemLicenseList) {
                          this.elemLicenseList.forEach((ite, i) => {
                            if (ite.materialOid == ma.caseMaterialOid) {
                              //移除原来的重新赋值
                              this.elemLicenseList.splice(i, 1);
                            }
                          });
                        }
                        this.elemLicenseList.push(res);
                      }
                    } else {
                      this.show_upload[ma.caseMaterialOid] = false;
                      this.show_scan[ma.caseMaterialOid] = false;
                      this.show_elem[ma.caseMaterialOid] = false;
                    }
                  });
                });
              }
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
                // this.show_upload=[];
                // this.show_scan=[];
              }
            } else {
              this.msgError("保存办件失败！");
              return false;
            }
          });
        }
      });
    },
    /** 附件目录材料 */
    chooseCatalog(caseMaterialOid, attaOid, materialCatalogOid) {
      let _that = this;
      let catalog = {};
      catalog.attaOid = attaOid;
      catalog.materialCatalogOid = materialCatalogOid;
      catalog.caseMaterialOid = caseMaterialOid;
      if (_that.materialCatalogAttaList != "") {
        _that.materialCatalogAttaList.forEach(catalog => {
          if (catalog) {
            if (catalog.attaOid == attaOid) {
              _that.materialCatalogAttaList.splice(
                _that.materialCatalogAttaList.indexOf(catalog),
                1,
                {}
              );
            }
          }
        });
      }
      _that.materialCatalogAttaList.push(catalog);
    },
    /** 检查失信人 */
    checkInDishonestUser(index) {
      let _that = this;
      checkApplyUserInDishonest(
        _that.ruleForm.applyUserName,
        _that.ruleForm.credentialNumber
      ).then(response => {
        if (response.data != null) {
          _that.dishonestFlag = false;
        }
      });
    },
    /** 保存材料附件信息 */
    saveMaterialAtta(index) {
      let _that = this;
      let flag = false;
      if (_that.collectionTypeList) {
        let findnum = _that.collectionTypeList.findIndex(coll => {
          if (coll.collectionType == "2") {
            return true;
          }
        });
        if (
          findnum > 0 &&
          _that.fileList.length == 0 &&
          _that.materialAttaList.length == 0
        ) {
          _that.msgError("请至少上传一个附件");
          return false;
        } else {
          //若存在办件材料，再验证是否含有必须收取
          if (_that.comboCaseMaterials.length > 0) {
            //必要材料收取类型(总开关)
            let mustTypeFlag = true;
            //必要材料收取附件(总开关)
            let mustAttaFlag = true;
            //单个必要材料收取类型未选择
            let mustType = "";
            //单个必要材料收取附件/证照未上传
            let mustAtta = "";
            //是否选择上传附件
            let uploadFlag = false;
            let materialName = "";
            //是否存在 必须 材料,未收取
            for (let i = 0; i < _that.comboCaseMaterials.length; i++) {
              const cmMaterial = _that.comboCaseMaterials[i];
              //单个必要材料收取类型开关
              let mustTypeFlagFegin = false;
              //单个必要材料收取附件开关
              let mustAttaFlagFegin = false;
              //必须，看是否有收取
              if (cmMaterial.comboDireMaterial.mustFlag == "1") {
                //遍历收取类型
                if (
                  _that.collectionTypeList &&
                  _that.collectionTypeList.length > 0
                ) {
                  for (let j = 0; j < _that.collectionTypeList.length; j++) {
                    const type = _that.collectionTypeList[j];
                    if (
                      type.caseMaterialOid == cmMaterial.caseMaterialOid &&
                      type.collectionType
                    ) {
                      mustTypeFlagFegin = true;
                      //已经点击收取类型，后遍历收取的材料
                      //看是否为纸质，则无材料
                      if (type.collectionType == 1) {
                        //无材料
                        mustAttaFlagFegin = true;
                      } else if (
                        type.collectionType == 2 ||
                        type.collectionType == 3
                      ) {
                        //附件上传、扫描、
                        if (_that.materialAttaList.length > 0) {
                          for (
                            let k = 0;
                            k < _that.materialAttaList.length;
                            k++
                          ) {
                            const atta = _that.materialAttaList[k];
                            if (
                              atta.caseMaterialOid == cmMaterial.caseMaterialOid
                            ) {
                              mustAttaFlagFegin = true;
                            }
                          }
                        }
                      } else if (type.collectionType == 5) {
                        //证照
                        if (_that.elemLicenseList.length > 0) {
                          for (
                            let k = 0;
                            k < _that.elemLicenseList.length;
                            k++
                          ) {
                            const atta = _that.elemLicenseList[k];
                            if (
                              atta.materialOid == cmMaterial.caseMaterialOid
                            ) {
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
                  if (
                    coll.collectionType != "1" &&
                    coll.collectionType != "4" &&
                    coll.collectionType != null
                  ) {
                    if (coll.attaOid == null || coll.attaOid == "") {
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
                mustTypeFlag = false;
                mustType = cmMaterial.materialName;
                break;
              }
              //必须材料，未选取方式
              if (mustAttaFlagFegin == false) {
                mustAttaFlag = false;
                mustAtta = cmMaterial.materialName;
                break;
              }
            }

            //必须材料，未选取方式
            if (mustTypeFlag == false) {
              _that.msgError("材料 " + mustType + " 的收取方式未选择！");
              return false;
            }
            //必须材料附件，未收取
            if (mustAttaFlag == false) {
              _that.msgError(
                "必须材料 " + mustAtta + " 的附件未上传或证照未选取！"
              );
              return false;
            }
          }

          /********************************************/
          let dataForm = {};
          dataForm.caseOid = _that.comboCaseOid;
          // dataForm. caseMaterialOid= this.materialOid;
          dataForm.attList = _that.materialAttaList;
          dataForm.collectionTypeVos = _that.collectionTypeList;
          dataForm.elemLicense = _that.elemLicenseList;
          //dataForm.materialCatalogAttaList = this.materialCatalogAttaList;
          dataForm.rqhbTime = _that.materialForm.rqhbTime;
          dataForm.sqrName = _that.ruleForm.applyUserName;
          dataForm.caseName = _that.ruleForm.projectName;
          let tempMaterialCatalogAtta = [];
          /*       if (!_that.materialCatalogAttaList||_that.materialCatalogAttaList.length <= 0) {
              _that.msgError("上传文件未关联目录信息不可进行智审！");
              return false;
            }*/
          _that.materialCatalogAttaList.forEach((fir, i) => {
            if (fir) {
              tempMaterialCatalogAtta.push(fir);
              _that.materialCatalogAttaList.forEach(second => {
                //如果办件材料一样附件不一样看选择的智审目录是否相同
                if (second) {
                  if (
                    fir.caseMaterialOid == second.caseMaterialOid &&
                    fir.attaOid != second.attaOid
                  ) {
                    if (fir.materialCatalogOid == second.materialCatalogOid) {
                      flag = true;
                    }
                  }
                }
              });
            }
          });
          dataForm.materialCatalogAttaList = tempMaterialCatalogAtta;
          if (flag) {
            _that.msgError("上传文件请选择不同的目录材料！");
            return false;
          } else {
            if (_that.rqhbMaterialList.length > 0) {
              _that.$refs["materialForm"].validate(valid => {
                if (valid) {
                  _that
                    .$confirm("你确定要打印容缺补正承诺书吗？", "警告", {
                      confirmButtonText: "确定",
                      cancelButtonText: "取消",
                      type: "warning"
                    })
                    .then(function() {
                      POBrowser.openWindowModeless(
                        process.env.VUE_APP_BASE_API_PAGE +
                          "/manage/zhuozheng/printComboCaseRqhbNotice?rqhbTime=" +
                          dataForm.rqhbTime +
                          "&sqrName=" +
                          encodeURIComponent(dataForm.sqrName) +
                          "&caseName=" +
                          encodeURIComponent(dataForm.caseName),
                        "width=1200px;height=800px;"
                      );
                    })
                    .then(function() {
                      saveCaseMaterialAtta(dataForm).then(response => {
                        if (response.code === 200) {
                          _that.msgSuccess("保存材料附件成功！");
                          _that.i = index;
                          _that.loading = false;
                          _that.show_3 = false;
                          _that.show_4 = true;
                        }
                      });
                    })
                    .catch(action => {
                      if (action === "cancel") {
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
                    });
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
    //下载附件
    downloadFile(attaOid) {
      this.download(attaOid);
    },
    //材料智审功能
    clzs(index, count) {
      const loadingnew = this.$loading({
        lock: true,
        text: "材料审核中...",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });

      let _that = this;
      let flag = false;
      if (this.collectionTypeList) {
        var index = this.collectionTypeList.findIndex(coll => {
          if (coll.collectionType == "2") {
            return true;
          }
        });
        if (
          index > 0 &&
          this.fileList.length == 0 &&
          this.materialAttaList.length == 0
        ) {
          loadingnew.close();
          this.msgError("请至少上传一个附件");
          return false;
        } else {
          let dataForm = {};
          dataForm.caseOid = this.comboCaseOid;
          // dataForm. caseMaterialOid= this.materialOid;
          dataForm.attList = this.materialAttaList;
          dataForm.collectionTypeVos = this.collectionTypeList;
          dataForm.elemLicense = this.elemLicenseList;
          let tempMaterialCatalogAtta = [];
          if (
            !_that.materialCatalogAttaList ||
            _that.materialCatalogAttaList.length <= 0
          ) {
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
                  if (
                    fir.caseMaterialOid == second.caseMaterialOid &&
                    fir.attaOid != second.attaOid
                  ) {
                    if (fir.materialCatalogOid == second.materialCatalogOid) {
                      flag = true;
                    }
                  }
                }
              });
            }
          });
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
                    let caseOid = this.comboCaseOid;
                    let caseMaterialOid = item.caseMaterialOid;
                    let caseFileRecOid = item.materialAttaOid;
                    let cataOid = item.materialCatalogOid;
                    if (
                      item.materialCatalogOid != null &&
                      item.materialCatalogOid != ""
                    ) {
                      intelligentPretrialmaterialPrePrial(
                        caseOid,
                        caseFileRecOid,
                        caseMaterialOid,
                        cataOid
                      ).then(response => {
                        if (!response.data) {
                          _that.$message.error("智审连接超时!");
                          loadingnew.close();
                          return false;
                        }
                        if (response.data.success == true) {
                          if (num + 1 == letList.length) {
                            loadingnew.close();
                            let item = {
                              caseOid: caseOid,
                              caseMaterialOid: caseMaterialOid,
                              caseFileRecOid: caseFileRecOid,
                              cataOid: cataOid,
                              show: true,
                              title: "智能审核结果"
                            };
                            _that.comboIntelligentPretrialOptions.push(item);
                          }
                        } else {
                          _that.$loading().close();
                          _that.msgError(response.data.message);
                          return false;
                        }
                      });
                    }
                  });
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
    //办件平板评价
    pushPjCaseService() {
      let _that = this;
      let userOid = _that.loginUser.userOid;
      pushPbpjUser(userOid).then(response => {
        //推送办件评价
        if (response.data != "") {
          if (response.data.appraiseFlag == "1") {
            //推送平板评价
            _that.caseAccpet(0);
          } else {
            //没用启用评价 直接受理
            _that.caseAccpet(1);
          }
        }
      });
    },
    //平板评价
    pushPbpj() {
      let _that = this;
      pushPbpjCheckLogin().then(response => {
        if (response.data) {
          //检查平板评价登录
          _that
            .$confirm("你确定要进行办件评价吗？", "警告", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning"
            })
            .then(function() {
              pbpjSaveCaseInfo(_that.comboCaseOid).then(response => {
                if (response.data != null) {
                  let caseNumber = response.data;
                  pushPbpjInfo(caseNumber).then(response => {
                    if (response.data != null) {
                      _that.$emit("closeUpdate");
                    }
                  });
                } else {
                  _that.msgError("办件评价失败！");
                  return false;
                }
              });
            })
            .catch(action => {
              if (action === "cancel") {
                _that.$emit("closeUpdate");
              }
            });
        } else {
          _that.msgWarning("平板评价未登录，请登录平板评价器！");
          return false;
        }
      });
    },
    /** 办件受理 */
    caseAccpet(flag) {
      let _that = this;
      if (_that.comboCaseOid != "") {
        _that.acceptForm.caseOid = _that.comboCaseOid;
        _that.acceptForm.caseNumber = _that.ruleForm.caseNumber;
        _that.acceptForm.sqrName = _that.ruleForm.applyUserName;
        _that.acceptForm.sqTime = _that.ruleForm.createDate
          ? _that.ruleForm.createDate.substring(0, 10)
          : _that.ruleForm.createDate;
        _that.acceptForm.rqhbTime = _that.materialForm.rqhbTime;
        _that.acceptForm.projectName = _that.ruleForm.projectName;
        //添加权限选项值
        _that.acceptForm.valList = _that.checkList;
        if (_that.acceptForm.ifAccept == 1) {
          saveCaseAccpet(_that.acceptForm).then(response => {
            if (response.data != "") {
              _that.msgSuccess("办件进入受理成功！");
              if (flag == 1) {
                _that.$emit("closeUpdate");
              } else {
                //推送平板评价
                _that.pushPbpj();
              }
            }
          });
        } else {
          if (!_that.acceptForm.acceptOpinionDesc) {
            _that.$message.error("意见说明不能为空！");
            return false;
          }
          _that
            .$confirm("您是否要打印不予受理通知书？", "警告", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning"
            })
            .then(function() {
              POBrowser.openWindowModeless(
                process.env.VUE_APP_BASE_API_PAGE +
                  "/manage/zhuozheng/printComboCaseNotAcceptNotice?caseNumber=" +
                  _that.acceptForm.caseNumber +
                  "&sqrName=" +
                  encodeURIComponent(_that.acceptForm.sqrName) +
                  "&sqTime=" +
                  _that.acceptForm.sqTime,
                "width=1200px;height=800px;"
              );
            })
            .then(function() {
              saveCaseAccpet(_that.acceptForm).then(response => {
                if (response.data != "") {
                  _that.msgSuccess("办件已不予受理！");
                  if (flag == 1) {
                    _that.$emit("closeUpdate");
                  } else {
                    //推送平板评价
                    _that.pushPbpj();
                  }
                }
              });
            })
            .catch(action => {
              if (action === "cancel") {
                saveCaseAccpet(_that.acceptForm).then(response => {
                  if (response.data != "") {
                    _that.msgSuccess("办件不予受理成功！");
                    if (flag == 1) {
                      _that.$emit("closeUpdate");
                    }
                  }
                });
              }
            });
        }
      } else {
        _that.msgError("办件信息未保存，请先保存！");
        return false;
      }
    },
    /** 送达方式 */
    changeDeliveryWay(val) {
      this.address_show = val === "1" ? true : false;
    },
    /** 是否代理人 */
    changeProxyFlag(val) {
      this.proxy_show = val === 1 ? true : false;
    },
    /** 材料形式 */
    chooseCollectionType(val, item, index) {
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
      if (this.collectionTypeList) {
        this.collectionTypeList.forEach(da => {
          if (bb.caseMaterialOid === da.caseMaterialOid) {
            this.collectionTypeList.splice(
              this.collectionTypeList.indexOf(da),
              1
            );
            this.collectionTypeList.push(bb);
          }
        });
      } else {
        this.collectionTypeList.push(bb);
      }
      //切换附件收取方式，需要移除附件信息
      if (this.materialAttaList.length > 0) {
        let index = this.materialAttaList.findIndex(atta => {
          if (item.caseMaterialOid == atta.caseMaterialOid) {
            return true;
          }
        });
        if (index > -1) {
          this.materialAttaList.splice(index, 1);
          if (this.comboCaseMaterials && this.comboCaseMaterials.length > 0) {
            this.comboCaseMaterials.forEach((caseMat, z) => {
              if (caseMat.caseMaterialOid == item.caseMaterialOid) {
                caseMat.attaList = [];
              }
            });
          }
          //删除材料和智审目录关联
          if (
            this.materialCatalogAttaList &&
            this.materialCatalogAttaList.length > 0
          ) {
            this.materialCatalogAttaList.forEach((cata, z) => {
              if (cata) {
                if (cata.caseMaterialOid == item.caseMaterialOid) {
                  this.materialCatalogAttaList.splice(z, 1, {});
                }
              }
            });
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
            this.rqhbMaterialList.splice(
              this.rqhbMaterialList.indexOf(rqhb),
              1
            );
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
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 选择地址 */
    handleChange(values) {},
    /** 已有地址加载 */
    /*getAddressTree(addresseeAddress) {
        let _that = this;
        let address = [];
        if (addresseeAddress != "" && addresseeAddress != null) {
          let addressIds = addresseeAddress != "" ? addresseeAddress.replace("[\"","").replace("\"]",""):"";
          if(addressIds !=""){
            address = addressIds.split("\",\"");
          }
        }
        _that.ruleForm.addresseeAddress = address;
      },*/
    /** 加载送达信息 */
    getApplyInfo() {
      let _that = this;
      _that.ruleForm.addresseeName = _that.ruleForm.applyUserName;
      _that.ruleForm.addresseePostCode = _that.ruleForm.applyPostCode;
      _that.ruleForm.addresseePhone = _that.ruleForm.applyUserPhone;
      _that.ruleForm.addresseeTel = _that.ruleForm.applyUserTel;
    },
    /*/!** 隐藏右侧 *!/
      hiddenRightSideBar() {
        this.rightShow = false;
      },
      /!** 显示右侧 *!/
      showRightSideBar() {
        this.rightShow = true;
      },*/
    /** 表单重置 */
    reset() {
      Object.assign(this.queryForm, this.$options.data().queryForm);
      this.resetForm("queryForm");
    },
    /** 取消按钮 */
    cancel() {
      this.openInit = false;
      this.reset();
    },

    /** 关闭方法 */
    closeDialog() {
      this.comboDireMaterials = [];
      this.situationOptionVals = [];
      this.checkList = [];
      this.situationList = [];
      this.materialOptList = [];
      this.mateOptRels = [];
      this.radio1 = [];
    },
    pushMaterialOid(materialOid, rawMaterialOid) {
      if (materialOid != undefined) {
        this.materialOid = materialOid;
      }
      if (rawMaterialOid != undefined) {
        this.materialOid = rawMaterialOid;
      }
    },
    /** 失败后返回 */
    uploadError(resp) {
      this.msgError("文件上传失败");
    },

    /** 上传附件 */
    uploadFiles(file) {
      let formData = new FormData();
      formData.append("files", file.file);
      uploadCaseMaterialFile(formData).then(response => {
        if (response.data != "") {
          response.data.forEach(data => {
            let atta = {};
            //let list={};
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
            this.$forceUpdate();
          });
        } else {
          this.$message.error("上传文件不能为空！");
        }
      });
    },
    /** 上传附件请求操作 */
    beforeUpload(file) {
      let isRightSize = file.size / 1024 / 1024 < 10;
      if (!isRightSize) {
        this.$message.error("文件大小超过 10MB");
      }
      if (file.size == 0) {
        this.$message.error("不允许上传空文件");
        return false;
      }
      this.fileList.push(file);
      return isRightSize;
    },
    //预览附件
    viewFile(attaOid) {
      let item = {
        show: true,
        attaOid: attaOid
      };
      this.viewDialogOptions.push(item);
    },
    //关闭预览附件
    closeFileView() {
      this.viewDialogOptions.pop();
    },
    deleteFile(bean, atta) {
      delFile(bean.caseMaterialOid, atta.oid).then(response => {
        if (response.data != "") {
          if (this.materialAttaList) {
            var index = this.materialAttaList.findIndex(item => {
              if (item.attaOid == atta.oid) {
                return true;
              }
            });
            this.materialAttaList.splice(index, 1);
          }
          if (this.attaList) {
            var index2 = bean.attaList.findIndex(item2 => {
              if (item2.oid == atta.oid) {
                return true;
              }
            });
            bean.attaList.splice(index2, 1);
          }
          //删除附件和智审目录关联
          if (this.materialCatalogAttaList != "") {
            this.materialCatalogAttaList.forEach((catalog, z) => {
              if (catalog) {
                if (catalog.attaOid == atta.oid) {
                  this.materialCatalogAttaList.splice(z, 1, {});
                }
              }
            });
          }
        }
      });
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
      return new File([ia], fileName, {
        type: "image/jpeg"
      });
    },
    // 接收socket回调函数返回数据的方法
    getConfigResult(data) {
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
              let base64s = data.content.Cameras64.split(",");
              for (let i = 0; i < base64s.length; i++) {
                if (!base64s[i]) {
                  continue;
                }
                let file = _that.base64ToFile(
                  base64s[i],
                  "scanPicture" + i + ".jpg"
                );
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
                });
              }
            }
          }
        }
      }
      if (data.status == 1) {
        _that.$message.error(data.message);
      }
    },
    //初始化socket发生错误回调
    socketError() {
      this.$message.error("请检查设备或连接是否正常");
    },
    scanCard(scanType) {
      //申请人模块--0
      //联系人模块--1
      //Device：设备类型、
      let info = '{"device":"IdCard", "type":"' + scanType + '"}';
      //建立socket连接
      this.socketApi.initWebSocket(this.socketError);
      this.socketApi.sendSock(info, this.getConfigResult);
    },
    scanPicture(caseMaterialOid, index, item) {
      //caseMaterialOid--所属材料主键
      //index--所属操作行索引
      //item--所属操作行数据
      //Device：设备类型、
      this.scan_item = item;
      let info = '{"device":"HighCamera", "type":"' + caseMaterialOid + '"}';
      //建立socket连接
      this.socketApi.initWebSocket(this.socketError);
      this.socketApi.sendSock(info, this.getConfigResult);
    },

    openMaterialComparison(materiaitem) {
      console.log(materiaitem);
      /*   alert(JSON.stringify(this.materiaitem.attaList))*/
      let attaOids = "";
      let fastdfsNginxUrls = "";
      let maattaList = materiaitem.attaList;
      let attaLists = JSON.stringify(materiaitem.attaList);
      /* if(maattaList && maattaList.length>0){
           maattaList.forEach((ite,i)=>{
             attaOids+=ite.oid+";";
             fastdfsNginxUrls+=ite.fastdfsNginxUrl+";";
           })
         }*/
      let sampleInfoOid = "";
      let comboDirectoryOid = "";
      if (materiaitem.comboDireMaterial.typeFlage == 0) {
        comboDirectoryOid = this.ruleForm.comboDireOid;
      }
      let materiaOid = materiaitem.materialOid;
      let caseMaterialOid = materiaitem.caseMaterialOid;
      let caseOid = this.comboCaseOid;
      let fileViewurls =
        process.env.BASE_URL +
        "picture/comboPrviewList.html?materiaOid=" +
        materiaOid +
        "&sampleInfoOid=" +
        sampleInfoOid +
        "&comboDirectoryOid=" +
        comboDirectoryOid;
      let item = {
        show: true,
        title: "材料比对",
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
    formFilling(dataForm, index) {
      let _that = this;
      _that.indexForm = index;
      if (dataForm) {
        if (dataForm.formType == 0) {
          //自定义表单
          if (dataForm.formAddr) {
            window.open(dataForm.formAddr, "width=1200px;height=800px;");
          } else {
            _that.$message.error("请配置表单地址！");
          }
        } else if (dataForm.formType == 1) {
          //电子表单
          _that.iframeState = true;
          //查询电子表单地址配置
          // "http://172.168.249.2:8081/form//loadInitFormReport.do?authorizeKey=00b98ec063984da0afca427d3637fe2a&formOid=FORM20180927GOXSMMER";
          _that.iframeUrl =
            process.env.VUE_APP_DZBD_TB_ROUTE_PATH +
            "&formOid=" +
            dataForm.formCode;
          if (_that.caseForm[index].formDataId) {
            _that.iframeUrl += "&reportOid=" + _that.caseForm[index].formDataId;
          }
        }
      }
    },
    //修改表单关闭
    closeIframe(reportOid) {
      if (reportOid) {
        this.tempFormDataId[this.indexForm] = reportOid;
        reportOid = ""; //防止点击取消的关闭
      }
      this.iframeState = false;
    },
    //关闭表单预览
    closeIframeView() {
      this.iframVieweState = false;
    },
    //保存电子表单
    saveCaseForm() {
      let _that = this;
      if (_that.sxSerFormList) {
        let comboForm = {};
        let comboCaseFormList = [];
        _that.sxSerFormList.forEach((items, i) => {
          let formData = {};
          let formCaseTemp = _that.caseForm[i];
          formData.regOid = _that.comboCaseOid;
          formData.serFormOid = items.oid;
          formData.comboDireOid = _that.comboDireOid;
          formData.formDataId = _that.tempFormDataId[i];
          if (_that.caseForm[i]) {
            formData.id = formCaseTemp.id;
            formData.oid = formCaseTemp.oid;
          }
          comboCaseFormList.push(formData);
        });
        comboForm.comboCaseFormList = comboCaseFormList;
        saveOrUpdateComboCaseForm(comboForm).then(response => {
          if (response.data) {
            let arr = response.data;
            arr.forEach(ite => {
              _that.sxSerFormList.forEach((items, z) => {
                if (ite.serFormOid == items.oid) {
                  _that.$set(_that.caseForm, z, ite);
                }
              });
            });
          }
        });
      }
    },
    //查看电子表单
    viewFormFilling(items, index) {
      let _that = this;
      if (_that.sxSerFormList[index]) {
        if (_that.sxSerFormList[index].formType == 0) {
          //自定义表单
          if (_that.sxSerFormList[index].formAddr) {
            window.open(
              _that.sxSerFormList[index].formAddr,
              "width=1200px;height=800px;"
            );
          } else {
            _that.$message.error("请配置表单地址！");
          }
        } else if (_that.sxSerFormList[index].formType == 1) {
          //电子表单
          if (items == null) {
            _that.$message.error("未查询到表单！");
          } else {
            _that.iframVieweState = true;
            _that.iframeUrlView =
              process.env.VUE_APP_DZBD_CK_ROUTE_PATH +
              "&reportOid=" +
              items.formDataId;
          }
        }
      }
    },
    getElecLicenInfo(caseMaterialOid, materialOid, billOid, index) {
      let _that = this;
      let userName = "";
      let idCard = "";
      idCard = _that.ruleForm.credentialNumber;
      userName = _that.ruleForm.applyUserName;
      /*if (_that.cegisterType == "0"){
          idCard=_that.ruleForm.credentialNumber;
          userName= _that.ruleForm.applyUserName ;
        }
        if (_that.cegisterType == "1"){
          idCard=_that.ruleForm.contactCredentialNumber
          userName=_that.ruleForm.contactUserName
        }*/
      if (userName && idCard) {
        queryElecLicenseByDirCode(materialOid, userName, idCard, billOid).then(
          response => {
            if (response.data) {
              let res = {};
              let eleArr = [];
              res.originName = response.data.licenseNumber;
              res.attaOid = response.data.elecLicenOid;
              res.materialOid = caseMaterialOid;
              eleArr[0] = res;
              _that.$set(_that.attaList, index, eleArr);
              // _that.attaList[index] = eleArr;
              //判断elemLicenseList是否已经存在办件的证照信息
              if (_that.elemLicenseList) {
                _that.elemLicenseList.forEach((ite, i) => {
                  if (ite.materialOid == caseMaterialOid) {
                    //移除原来的重新赋值
                    _that.elemLicenseList.splice(i, 1);
                  }
                });
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
          }
        );
      } else {
        _that.$message.error("申请人/申请单位和证件号不能为空！");
        return;
      }
    },
    //电子证照预览
    viewElemsInfo(eleLincenseOid) {
      getElecLicenUrl(eleLincenseOid).then(response => {
        let urlElem = [];
        if (response.data) {
          urlElem = response.data;
          if (urlElem[0].viewOfdUrl) {
            window.open(urlElem[0].viewOfdUrl, "width=1200px;height=800px;");
          } else {
            this.$message.error("暂时无法查看证照信息！");
          }
        }
      });
    }
  },
  destroyed: function() {
    //在离开此页面的时候主动关闭socket
    this.socketApi.webSocketClose();
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
  content: "";
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
.el-radio {
  margin-right: 12px;
}
</style>
