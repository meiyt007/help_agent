/** * 窗口受理管理 * @author: wangwg * @date: 2020-10-29 */
<template>
  <div class="app-container">
    <el-row :gutter="20"></el-row>
    <!-- 步骤 -->
    <div class="process-box">
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
          :model="ruleForm"
          :rules="rules"
          ref="ruleForm"
          label-width="0px"
          class="demo-ruleForm"
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
                <div v-if="sxServiceOptionTitles.length > 0">
                  <div v-for="(data,index) in sxServiceOptionTitles" :key='index'>
                    <div class="check-list" v-if="data.isHavingCorrelation==0 || data.isHavingCorrelation==2">
                      <h3>{{ data.titleName }}</h3>
                      <template v-if="data.moreStatus == '0'">
                        <el-radio-group v-model="checkList[index]">
                          <el-radio v-for="item in data.sxServiceOptionVals" :key='item.oid' :disabled="ifDis" :label="item.oid"
                                    @change="changeTitle(data.titleOid,item.oid)">{{ item.name }}</el-radio>
                        </el-radio-group>
                      </template>


                      <template v-if="data.moreStatus == '1'">
                        <el-checkbox-group v-model="checkBoxList">
                          <el-checkbox
                            v-for="item in data.sxServiceOptionVals"
                            :disabled="ifDis"
                            :key="item.oid"
                            @change="changeTitleBox(item, item.oid)"
                            :id="item.oid"
                            :label="item.oid"
                            >{{ item.name }}
                          </el-checkbox>
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
          >下一步
        </el-button>
      </div>
      <!-- 第二步 -->
      <div class="step-content step-second" v-if="show_1">
        <el-form
          :model="serviceForm"
          ref="ruleForm"
          label-width="0px"
          class="demo-ruleForm"
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
                <td><b>事项名称：</b></td>
                <td>{{ serviceForm.serviceName }}</td>
                <td><b>事项类型：</b></td>
                <td>{{ serviceForm.serviceTypeName }}</td>
              </tr>
              <tr>
                <td><b>实施机构：</b></td>
                <td>{{ serviceForm.organName }}</td>
                <td><b>办理地点：</b></td>
                <td>
                  <div
                    v-for="(data, index) in serviceForm.sxServiceLocations"
                    :label="data.locationName"
                    :key="index"
                  >
                    {{ index + 1 }}、{{ data.locationName }}
                  </div>
                </td>
              </tr>
              <tr>
                <td><b>办理时间：</b></td>
                <td>
                  <div
                    v-for="(data, index) in serviceForm.sxServiceLocations"
                    :label="data.acceptDate"
                    :key="index"
                  >
                    {{ index + 1 }}、{{ data.acceptDate }}
                  </div>
                </td>
                <td><b>咨询电话：</b></td>
                <td>{{ serviceForm.zxDhText }}</td>
              </tr>
              <tr>
                <td><b>承诺时限：</b></td>
                <td>
                  <template>
                    {{ serviceForm.sxServiceExtend.promiseLimit }}
                    <span
                      v-if="serviceForm.sxServiceExtend.promiseLimitType == 'W'"
                    >
                      (工作日)</span
                    >
                    <span
                      v-if="serviceForm.sxServiceExtend.promiseLimitType == 'N'"
                    >
                      (自然日)</span
                    >
                    <span
                      v-if="serviceForm.sxServiceExtend.promiseLimitType == 'H'"
                    >
                      (小时)</span
                    >
                  </template>
                </td>
                <td><b>法定时限：</b></td>
                <td>
                  <template>
                    {{ serviceForm.sxServiceExtend.legalLimit }}
                    <span
                      v-if="serviceForm.sxServiceExtend.legalLimitType == 'W'"
                    >
                      (工作日)</span
                    >
                    <span
                      v-if="serviceForm.sxServiceExtend.legalLimitType == 'N'"
                    >
                      (自然日)</span
                    >
                    <span
                      v-if="serviceForm.sxServiceExtend.legalLimitType == 'H'"
                    >
                      (小时)</span
                    >
                  </template>
                </td>
              </tr>
              <tr>
                <td><b>申请受理条件：</b></td>
                <td colspan="3">
                  <div
                    v-for="(data, index) in serviceForm.sxAcceptConditions"
                    :label="data.conditionText"
                    :key="index"
                  >
                    <p v-if="serviceForm.sxAcceptConditions.length > 1">
                      {{ index + 1 }}、 {{ data.conditionText }}
                    </p>
                    <p v-else>
                      {{ data.conditionText }}
                    </p>
                  </div>
                </td>
              </tr>
              <tr>
                <td><b>办事流程：</b></td>
                <td colspan="3">
                  <div
                    v-for="(data, index) in serviceForm.sxServiceLinks"
                    :label="data.linkName"
                    :key="index"
                  >
                    <p v-if="serviceForm.sxServiceLinks.length > 1">
                      {{ index + 1 }}、{{ data.linkName }}
                    </p>
                    <p v-else>
                      {{ data.linkName }}
                    </p>
                  </div>
                </td>
              </tr>
              <tr>
                <td><b>常见问题：</b></td>
                <td colspan="3">
                  <div
                    v-for="(data, index) in serviceForm.sxServiceQuestions"
                    :label="data.title"
                    :key="index"
                  >
                    <p v-if="serviceForm.sxServiceQuestions.length > 1">
                      {{ index + 1 }}、{{ data.title }}
                    </p>
                    <p v-else>
                      {{ data.title }}
                    </p>
                  </div>
                </td>
              </tr>
            </table>
          </div>
          <div class="data-box">
            <h4>材料列表</h4>
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
              <tbody v-if="sxServiceMaterialList.length > 0">
              <tr v-for="(data,index) in sxServiceMaterialList" :key='index' empty-text="暂无我的待办">
                <td>{{ index + 1 }}</td>
                <td>{{ data.materialName }}</td>
                <td>
                  <template>
                    <span v-if="data.materialType=='0'">原件</span>
                    <span v-if="data.materialType=='1'">复印件</span>
                    <span v-if="data.materialType=='2'">原件或者复印件</span>
                  </template>
                </td>
                <td>
                  <template>
                    <span v-if="data.materialFormat=='1'">纸质</span>
                    <span v-if="data.materialFormat=='2'">电子版</span>
                  </template>
                </td>
                <td>{{ data.paperNumber }}</td>
                <td>
                  <template>
                    <span class="bage-necessery" v-if="data.materialMustFlag=='1'">必要</span>
                    <span class="bage-necessery" v-if="data.materialMustFlag=='0'">不必要</span>
                    <span class="bage-necessery" v-if="data.materialMustFlag=='2'">容缺后补</span>
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
          :model="ruleForm"
          :rules="rules"
          ref="ruleForm"
          label-width="0px"
          class="demo-ruleForm"
          :label-position="labelPosition"
        >
          <div class="step-third-box">
            <h3 class="title">
              <i class="el-icon-s-grid"></i>申请人/申请单位信息
              <el-button
                type="primary"
                @click="scanCard('0')"
                style="float: right"
                v-if="cegisterType == 0"
                >✚ 扫描身份证
              </el-button>
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
                        maxlength="25"
                        show-word-limit
                      ></el-input>
                    </el-col>
                  </el-form-item>
                  <el-button
                    type="primary"
                    @click="getUserInfo(ruleForm.credentialNumber)"
                    style="float: right;margin-top: -36px;"
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
                        :popper-append-to-body="false"
                        @change="changeType"
                      >
                        <el-option
                          v-for="certificateType in certificateTypeList"
                          :key="certificateType.dictOid"
                          :label="certificateType.name"
                          :value="certificateType.dictOid"
                          class="category_style"
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
                        v-model.trim="ruleForm.credentialNumber"
                        name="credentialNumber"
                        maxlength="25"
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
                        v-model.trim="ruleForm.applyUserPhone"
                        name="applyUserPhone"
                        maxlength="11"
                        show-word-limit
                      ></el-input>
                    </el-col>
                  </el-form-item>
                </td>
                <td>
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
                        maxlength="15"
                        show-word-limit
                      ></el-input>
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
                        maxlength="150"
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
                        maxlength="25"
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
                        maxlength="250"
                        show-word-limit
                      ></el-input>
                    </el-col>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td><i class="require">*</i><b>申请数量：</b></td>
                <td>
                  <el-form-item prop="applyNumber">
                    <el-col :span="24">
                      <el-input
                        v-model.trim="ruleForm.applyNumber"
                        name="applyNumber"
                        maxlength="3"
                        show-word-limit
                      ></el-input>
                    </el-col>
                  </el-form-item>
                </td>
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
              </tr>
              <tr>
                <td><i class="require">*</i><b>业务管辖地：</b></td>
                <td>
                  <el-form-item prop="bussVenueDistrictOid">
                    <el-col :span="24">
                      <treeselect
                        @input="changeValue"
                        :multiple="true"
                        :options="districtOptions"
                        :flat="true"
                        :default-expand-level="1"
                        placeholder="请选择业务管辖地"
                        v-model="ruleForm.bussVenueDistrictOid"
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
                        maxlength="150"
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
                        maxlength="150"
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
                        maxlength="100"
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
                <td><i class="require">*</i><b>是否为代理人：</b></td>
                <td colspan="3">
                  <el-radio-group
                    v-model.trim="ruleForm.proxyFlag"
                    @change="changeProxyFlag"
                  >
                    <el-radio label="1">是</el-radio>
                    <el-radio label="0">否</el-radio>
                  </el-radio-group>
                </td>
              </tr>
            </table>
          </div>
          <!-- 代理人信息 -->
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
                        maxlength="25"
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
                      >
                      </el-input>
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
                        maxlength="50"
                        show-word-limit
                      ></el-input>
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
                        maxlength="20"
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
          <!-- 送达信息 -->
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
                    style="float:left;margin-bottom:2px;width:75%"
                  >
                    <el-col :span="24">
                      <el-input
                        v-model.trim="ruleForm.addresseeName"
                        name="addresseeName"
                        maxlength="25"
                        show-word-limit
                      ></el-input>
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
                        maxlength="25"
                        show-word-limit
                      ></el-input>
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
                        maxlength="250"
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
                <td>
                  <i class="require" v-if="sxSerForm.isFormFlag == 1">*</i
                  ><b>填写表单：</b>
                </td>
                <td colspan="3">
                  <el-row :gutter="10">
                    <!--                    <el-col :span="12">
                      <div class="form-box-inline">
                        <h4>1、{{sxSerForm.formName}}</h4>
                        <div class="form-btn-group" v-if="caseForm.formDataId">
                          <el-button icon="el-icon-view" class="btn" @click="viewFormFilling">表单预览</el-button>
                          <el-button icon="el-icon-edit-outline" class="btn" @click="formFilling">表单修改</el-button>
                        </div>
                        <el-button v-if="!caseForm.formDataId" type="primary" icon="el-icon-edit-outline"
                          class="btn-write" @click="formFilling">填写表单</el-button>
                        <ul>
                          <li>
                            <div class="icon bdbm-icon">
                              <i class="el-icon-document"></i>
                            </div>
                            <div class="input-text">
                              <h4>表单编码：</h4>
                              <p v-if="sxSerForm.formCode" :title="sxSerForm.formCode">{{sxSerForm.formCode}}</p>
                              <p v-if="!sxSerForm.formCode">暂无</p>
                            </div>
                          </li>
                          <li>
                            <div class="icon bdsm-icon">
                              <i class="el-icon-warning-outline"></i>
                            </div>
                            <div class="input-text">
                              <h4>表单说明：</h4>
                              <p v-if="sxSerForm.formText" :title="sxSerForm.formText">{{sxSerForm.formText}}</p>
                              <p v-if="!sxSerForm.formText">暂无</p>
                            </div>
                          </li>
                          <li v-if="sxSerForm.simpleAtta">
                            <div class="icon bdsm-icon" style="background-color: #868380;">
                              <i class="el-icon-download"></i>
                            </div>
                            <div class="input-text">
                              <el-button icon="el-icon-view" class="btn2" @click="downloadFile(sxSerForm.simpleAtta)">
                                表单样本下载</el-button>
                            </div>
                          </li>
                        </ul>
                      </div>
                    </el-col>-->
                    <el-col :span="12">
                      <div class="form-box-inline">
                        <h4>1、内资公司变更-法定代表人登记表单</h4>
                        <div class="form-btn-group">
                          <el-button icon="el-icon-view" class="btn"
                            >表单预览</el-button
                          >
                          <el-button
                            icon="el-icon-edit-outline"
                            class="btn"
                            @click="getNzgsbgFormInfo"
                            >表单修改</el-button
                          >
                        </div>
                        <ul>
                          <li>
                            <div class="icon bdbm-icon">
                              <i class="el-icon-document"></i>
                            </div>
                            <div class="input-text">
                              <h4>表单编码：</h4>
                              <p>FORM20190429WWHRGSP</p>
                            </div>
                          </li>
                          <li>
                            <div class="icon bdsm-icon">
                              <i class="el-icon-warning-outline"></i>
                            </div>
                            <div class="input-text">
                              <h4>表单说明：</h4>
                              <p>办件必须表单</p>
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
                @click="saveApplyCaseForm('')"
                >暂存</el-button
              >
              <el-button
                type="primary"
                icon="el-icon-circle-check"
                @click="next(3, 1)"
                >下一步
              </el-button>
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
          :model="ruleForm"
          :rules="rules"
          ref="ruleForm"
          label-width="0px"
          class="demo-ruleForm"
          :label-position="labelPosition"
        >
          <table cellspacing="0" cellpadding="0" border="0" class="data-table">
            <colgroup>
              <col width="5%" />
              <col width="25%" />
              <col width="10%" />
              <col width="13%" />
              <col width="47%" />
            </colgroup>
            <tr>
              <th>序号</th>
              <th>材料名称</th>
              <th>参考份数</th>
              <th>材料样本</th>
              <th>操作</th>
            </tr>
            <tbody v-for="(item, index) in sxServiceMaterialAttaList" :key='index'>
              <tr>
                <td>{{ index + 1 }}</td>
                <td>{{ item.materialName }}</td>
                <td>
                  <template>
                    <span v-if="item.sxMaterials.materialType == '0'"
                      >原件</span
                    >
                    <span v-if="item.sxMaterials.materialType == '1'"
                      >复印件</span
                    >
                    <span v-if="item.sxMaterials.materialType == '2'"
                      >原件或者复印件</span
                    >({{ item.sxMaterials.paperNumber }})
                  </template>
                </td>
                <td>
                  <div class="handle-btn">
                    <el-button
                      type="text"
                      icon="el-icon-download"
                      size="small"
                      @click="
                        downLoadMaterialAddr(
                          item.sxMaterials.materialSimpleAddr
                        )
                      "
                      >下载
                    </el-button>
                    <el-button
                      type="text"
                      icon="el-icon-view"
                      size="small"
                      @click="
                        viewMaterialAddr(item.sxMaterials.materialSimpleAddr)
                      "
                      >查看
                    </el-button>
                  </div>
                </td>
                <td>
                  <el-radio
                    label="1"
                    v-model="radio1[item.caseMaterialOid]"
                    @change="chooseCollectionType('1', index, item)"
                    >纸质收取
                  </el-radio>
                  <el-radio
                    label="2"
                    v-model="radio1[item.caseMaterialOid]"
                    @change="chooseCollectionType('2', index, item)"
                    >附件上传
                  </el-radio>
                  <el-radio
                    label="3"
                    v-model="radio1[item.caseMaterialOid]"
                    @change="chooseCollectionType('3', index, item)"
                    >扫描
                  </el-radio>
                  <el-radio
                    label="5"
                    v-model="radio1[item.caseMaterialOid]"
                    @change="chooseCollectionType('5', index, item)"
                    >证照库
                  </el-radio>
                  <el-radio
                    v-if="
                      isSxPersonFlg == false &&
                        item.sxMaterials.materialMustFlag == '2'
                    "
                    label="4"
                    v-model="radio1[item.caseMaterialOid]"
                    @change="chooseCollectionType('4', index, item)"
                    >容缺后补
                  </el-radio>
                </td>
              </tr>
              <tr v-show="show_upload[index]">
                <td colspan="5">
                  <div class="handle-data">
                    <el-row class="right-btn-group">
                      <el-button
                        type="primary"
                        @click="
                          openMaterialComparison(
                            item.materialOid,
                            item.caseMaterialOid
                          )
                        "
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
                            :data="item"
                            :http-request="uploadFiles"
                            :before-upload="beforeUpload"
                            :on-error="uploadError"
                            :show-file-list="showFileList"
                            :file-list="fileList"
                            accept="accept"
                          >
                            <el-button
                              size="mini"
                              type="primary"
                              icon="el-icon-upload"
                              @click="pushMaterialOid(item.caseMaterialOid)"
                              >点击上传</el-button
                            >
                          </el-upload>
                        </el-form-item>
                      </el-form>
                    </el-row>
                    <ul v-for="(data,idx) in item.attaList" :key='idx'>
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
                                v-model="catalogCheckList[data.attaOid]"
                                @change="
                                  chooseCatalog(
                                    item.caseMaterialOid,
                                    data.attaOid,
                                    catalog.materialCatalogOid
                                  )
                                "
                              >
                                {{ catalog.catalogName }}
                              </el-radio>
                            </span>
                          </el-col>
                          <el-col :span="5">
                            <div class="grid-content qdcg-btn">
                              <el-button
                                type="text"
                                icon="el-icon-view"
                                @click="viewFile(data.attaOid)"
                              ></el-button>
                              <el-button
                                type="text"
                                icon="el-icon-delete"
                                @click="deleteAtta(data)"
                              ></el-button>
                            </div>
                          </el-col>
                        </el-row>
                      </li>
                    </ul>
                  </div>
                </td>
              </tr>
              <tr v-show="show_scan[index]">
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
                          @click="scanPicture(item.caseMaterialOid, index)"
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
                              <p :title="data.originName">{{ data.name }}</p>
                            </div>
                          </el-col>
                          <el-col :span="5">
                            <div class="grid-content qdcg-btn">
                              <el-button
                                type="text"
                                icon="el-icon-view"
                                @click="viewFile(data.attaOid)"
                              ></el-button>
                              <el-button
                                type="text"
                                icon="el-icon-delete"
                                @click="deleteAtta(data)"
                              ></el-button>
                            </div>
                          </el-col>
                        </el-row>
                      </li>
                    </ul>
                  </div>
                </td>
              </tr>
              <tr v-show="show_elem[index]">
                <td colspan="5">
                  <div class="handle-data">
                    <el-row class="right-btn-group-two">
                      <el-button
                        size="mini"
                        type="primary"
                        icon="el-icon-upload"
                        @click="
                          getElecLicenInfo(
                            item.materialOid,
                            item.caseMaterialOid,
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
          <h3 class="title" v-if="radioNotice">
            <i class="el-icon-s-grid"></i>补正信息
          </h3>
          <table
            v-if="radioNotice"
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
              <td><i class="require">*</i><b>承诺补正时间：</b></td>
              <td colspan="3">
                <el-date-picker
                  v-model="bqbzCaseForm.dueDate"
                  type="date"
                  value-format="yyyy-MM-dd"
                  start=""
                  :picker-options="optionDate"
                  placeholder="选择承诺补正时间"
                >
                </el-date-picker>
                <el-button
                  type="primary"
                  v-if="bqbzCaseForm.dueDate"
                  @click="printNotice()"
                  >告知承诺书</el-button
                >
              </td>
            </tr>
          </table>

          <div class="btn-wrap">
            <el-checkbox
              v-if="checkFlag == true"
              :disabled="isCheckedDisable"
              v-model="radioNotice"
              label="1"
              @change="checkedBoxNotice"
              >当前事项支持告知承诺方式进行材料提交，是否需要？</el-checkbox
            >
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
          :model="ruleForm"
          :rules="rules"
          ref="ruleForm"
          label-width="0px"
          class="demo-ruleForm"
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
            <tr>
              <td><b>审批部门：</b></td>
              <td colspan="3">{{ loginUser.organName }}</td>
            </tr>
            <tr>
              <td><b>是否受理：</b></td>
              <td colspan="3">
                <el-radio-group v-model="ruleForm.acceptradio">
                  <el-radio label="1">受理通过</el-radio>
                  <el-radio label="2">不予受理</el-radio>
                </el-radio-group>
              </td>
            </tr>
            <tr v-if="isRqslFlag == true">
              <td><i class="require">*</i><b>承诺补正时间：</b></td>
              <td colspan="3">
                <el-date-picker
                  v-model="ruleForm.bqbzDueDate"
                  type="date"
                  value-format="yyyy-MM-dd"
                  :picker-options="optionDate"
                  placeholder="选择承诺补正时间"
                ></el-date-picker>
              </td>
            </tr>
            <tr>
              <td>
                <i class="require" v-if="ruleForm.acceptradio == 2">*</i
                ><b>意见说明：</b>
              </td>
              <td colspan="3">
                <el-form-item label="" prop="desc">
                  <el-col :span="24">
                    <el-input
                      type="textarea"
                      v-model="ruleForm.finalOpinionDesc"
                      maxlength="200"
                      show-word-limit
                    >
                    </el-input>
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
                v-if="isRqslFlag == false"
                @click="pushPjCaseService"
              >
                受理</el-button
              >
              <el-button
                type="primary"
                icon="el-icon-video-pause"
                v-if="isRqslFlag == true"
                @click="pushPjCaseService"
              >
                容缺受理</el-button
              >
              <el-button
                type="primary"
                icon="el-icon-video-pause"
                v-if="flagCc"
                @click="qlCaseCharge"
              >
                收费</el-button
              >
              <el-button
                type="primary"
                icon="el-icon-video-pause"
                v-if="chakanCharge"
                @click="viewCharge"
              >
                查看缴费</el-button
              >
            </div>
          </div>
        </el-form>
      </div>
    </div>
    <!--表单嵌套页面-->
    <el-dialog
      title="电子表单"
      :visible.sync="iframeState"
      v-if="iframeState"
      width="80%"
      height="100%"
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

    <!--引入文件的预览弹出层-->
    <el-dialog
      title="文件预览"
      :visible.sync="showSxFile"
      v-if="showSxFile"
      @close="closeFileView"
      width="60%"
      append-to-body
      :modal-append-to-body="true"
    >
      <material-file-view
        :attaOid="fileSxAttaOid"
        @father-colose="closeFileView"
      ></material-file-view>
    </el-dialog>
    <!--引入办件文件的预览弹出层-->
    <el-dialog
      v-dialog-drag
      title="文件预览"
      :visible.sync="showFile"
      v-if="showFile"
      @close="closeCaseFileView"
      width="60%"
      append-to-body
    >
      <case-file-view
        :attaOid="fileAttaOid"
        @father-click="closeCaseFileView"
      ></case-file-view>
    </el-dialog>
    <!--材料比对-->
    <el-dialog
      v-dialog-drag
      :visible.sync="item.show"
      v-for="item in materialComparisonOptions"
      @close="outerVisible"
      :title="item.title"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
      :key='item.sampleInfoOid'
    >

        <material-comparison
          :sampleInfoOid="item.sampleInfoOid"
          :comboDirectoryOid="item.comboDirectoryOid"
          :materiaOid="item.materiaOid"
          :caseOid="item.caseOid"
          :fileViewurl="item.fileViewurl"
          :attaOids="item.attaOids"
          :title="item.title"
          @father-click="openTempletePic"
        >
        </material-comparison>

    </el-dialog>

    <!-- 材料智审-->
    <el-dialog
      v-dialog-drag
      :visible.sync="item.show"
      v-for="item in intelligentPretrialOptions"
      :key='item.caseFileRecOid'
      @close="outerVisible"
      :title="item.title"
      width="900px"
      scrollbar
      height='600px'
      append-to-body
    >

        <intelligent-pretrial-new
          :caseOid="item.caseOid"
          :caseMaterialOid="item.caseMaterialOid"
          :caseFileRecOid="item.caseFileRecOid"
          :cataOid="item.cataOid"
          :title="item.title"
          @father-click="handleChildView"
        ></intelligent-pretrial-new>

    </el-dialog>

    <!-- 获取申请人信息-->
    <el-dialog
      v-dialog-drag
      :visible.sync="userInfoShow"
      v-if="userInfoShow"
      title="申请人信息列表"
      width="900px"
      height='600px'
      scrollbar
      append-to-body
    >
        <apply-user-list
          :applyCardNum="applyCardNum"
          @selectUserOk="closeUserInfoShow"
        >
        </apply-user-list>

    </el-dialog>

    <!-- 获取收费信息-->
    <el-dialog
      v-dialog-drag
      :visible.sync="chargeShow"
      v-if="chargeShow"
      title="办件收费"
      width="900px"
      append-to-body
    >
      <case-charge @case-close="closeCaseCharge"></case-charge>
    </el-dialog>

    <!-- 获取收费信息-->
    <el-dialog
      v-dialog-drag
      :visible.sync="viewchargeShow"
      v-if="viewchargeShow"
      title="查看收费信息"
      width="900px"
      append-to-body
    >
      <view-charge @case-close="closeView"></view-charge>
    </el-dialog>

    <!-- 人证比对 -->
    <div v-if="dialogVisible">
      <el-dialog
        :visible.sync="dialogVisible"
        width="1100px"
        height='700px'
        scrollbar
        :before-close="handleClose"
        :close-on-click-modal="false"
        append-to-body
      >

          <hardware-scan ref="scanForm"></hardware-scan>

      </el-dialog>
    </div>

    <!-- 内资子公司变更表单 -->
    <div v-if="bgForm">
      <el-dialog
        :visible.sync="bgForm"
        width="1100px"
        height='700px'
        scrollbar
        title="公司登记(备案)申请书"
        :close-on-click-modal="false"
        append-to-body
      >

          <nzgsbg-form
            ref="nzgsbgForm"
            @close-bgForm="closebgForm"
          ></nzgsbg-form>

      </el-dialog>
    </div>
  </div>
</template>
<script>
import {
  getSxService,
  save,
  getQlCaseByOid,
  getCertificateType,
  getloginUser,
  saveCaseAccpet,
  getSituationOpinionList,
  getAllSituationList,
  getSxServiceOptionAllTitleValRelation,
  getSituationMaterialListByOids,
  saveOut,
  changeCredentialType,
  downloadAttaSimple,
  getInformPromiseByServiceOid,
  getDishonestPerson,
  saveOrUpdateGzBqbz
} from "@/api/zc/businessManagement/windowAcceptance";
import IframeUrl from "@/views/iframe/formIndex";
import IframeUrlView from "@/views/iframe/formIndexView";
import { queryDistrictSimpleTree } from "@/api/sys/district";
import {
  validatePhone,
  validateTel,
  validIDCard,
  validatePostCode,
  validateEmails,
  validUnifiedCredit,
  validateNumberNoPonint
} from "@/utils/validate";
import {
  uploadCaseMaterialFile,
  saveQlCaseMaterialAtta,
  getSxServiceMaterialAttaList
} from "@/api/zc/businessManagement/caseMaterialAtta";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { VueCropper } from "vue-cropper";
import { regionData } from "element-china-area-data";
import {
  getElecLicenUrl,
  queryElecLicenseByDirCode
} from "@/api/zc/businessManagement/elemLice";
import {
  sxSerFormByServiceOid,
  saveOrUpdateCaseForm
} from "@/api/zc/businessManagement/formConfig";
import {
  getPjServiceSystem,
  showCallMessage
} from "@/api/zc/businessManagement/doubleScreenInteraction";
import {
  pushPbpjUser,
  pushPbpjConfirmInfo,
  pushPbpjCheckLogin,
  getPbpjConfirmInfo,
  pushPbpjInfo,
  pbpjSaveCaseInfo
} from "@/api/zc/businessManagement/pbpjManage";
import {
  getPjDeviceType,
  getServiceBaseInfo,
  grantWindowUser,
  pushSmartEvaConfirm,
  getSmartPjConfirmFlag,
  savePjMark
} from "@/api/zc/businessManagement/smartEvaManage";
import {
  getCertificateTypeByOid,
  intelligentPretrialmaterialPrePrial,
  queryAllCaseByOid
} from "@/api/zc/businessManagement/temporaryAcceptance";
import materialComparison from "@/views/zc/businessManagement/materialCheckKeyManage/materialComparison";
import intelligentPretrialNew from "@/views/zc/clzs/directoryManagement/intelligentPretrial";
import materialFileView from "@/views/zc/businessManagement/windowAcceptance/materialFileView";
import caseFileView from "@/views/zc/businessManagement/caseBqbz/caseFileView";
import applyUserList from "@/views/zc/businessManagement/temporaryAcceptance/applyUserList";
import caseCharge from "@/views/zc/businessManagement/charge/caseCharge";
import viewCharge from "@/views/zc/businessManagement/charge/viewCharge";
import hardwareScan from "@/views/common/hardwareScan";
import NzgsbgForm from "@/views/zc/businessManagement/windowAcceptance/nzgsbgForm";
export default {
  inheritAttrs: false,
  components: {
    NzgsbgForm,
    Treeselect,
    VueCropper,
    IframeUrl,
    IframeUrlView,
    materialFileView,
    caseFileView,
    materialComparison,
    intelligentPretrialNew,
    applyUserList,
    caseCharge,
    viewCharge,
    hardwareScan
  },
  props: ["cegisterType", "serviceOid"], //登记类型 法人1 自然人0
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
      formConfig_show: false, //标识表单是否显示
      iframeState: false,
      iframVieweState: false,
      showSxFile: false,
      fileSxAttaOid: "",
      iframeUrl: {}, //表单地址
      iframeUrlView: {}, //查看
      sxSerForm: {}, //配置表单的信息
      show_scan: [],
      show_elem: [],
      show_upload: [],
      bqbzCaseForm: {},
      radio: "",
      radio1: [],
      radio2: "1",
      isDisable: false,
      isNextDisable: false,
      ifDis: false,
      //上传按钮、扫描按钮切换
      changeIndex: "",
      changeButton: "1",
      msg_val: {},
      checkFlag: false,
      radioNotice: "",
      // 遮罩层
      loading: true,
      isCheckedDisable: false,
      collectionArray: [],
      isRqslFlag: false, //是否容缺受理
      isSxPersonFlg: false, //是否为失信人员
      // 业务办理事项数据
      serviceList: [],
      // 事项情形与选项关系数据
      situationOptions: [],
      // 事项选项值数据
      situationOptionVals: [],
      //材料比对页面
      materialComparisonOptions: [],
      //材料智审
      intelligentPretrialOptions: [],
      //登录信息
      loginUser: {},
      fileAttaOid: "",
      showFile: false,
      //评价类型
      pjType: "",
      //评价服务系统
      systemType: "",
      //查询办件主键
      id: "",
      //查询办件业务主键
      caseOid: "",
      //查询办件申请信息业务主键
      applyOid: "",
      //查询办件扩展信息业务主键
      extOid: "",
      //办件状态
      caseStatus: "",
      //办件编码
      caseNumber: "",
      createDate: "",
      //事项类型名称
      serviceTypeName: "",
      //材料办件业务主键
      caseMaterialOids: [],
      //环节信息主键
      linkResultOid: "",
      //证件类型
      certificateTypeList: [],
      // 机构
      listOrganOptions: [],
      // 区划
      districtOptions: [],
      // 事项类型
      serviceTypeOptions: [],
      //事项
      sxService: null,
      //办理地点
      sxServiceLocations: [],
      //事项材料
      sxServiceMaterialList: [],
      sxServiceMaterialAttaList: [],
      //情形标题
      sxServiceOptionTitles: [],
      //情形标题--必选
      sxServiceOptionTitlesMust: [],
      //办件与情形标题选项关系
      qlCaseTitleValList: [],
      //事项问题
      sxServiceQuestions: [],
      //事项环节
      sxServiceLinks: [],
      //事项扩展
      sxServiceExtend: null,
      //事项情形
      sxServiceSituations: [],
      //材料上传方式
      materialCollectionTypeList: [],
      situationOid: null,
      situationName: "默认自定情形",
      fileList: [],
      showFileList: false,
      cardType: "",
      chargeFlag: "",
      accept: {
        type: String,
        default: ".jpg,.jpeg,.png,.pdf,.PDF,.doc,.docx,.DOC,.DOCX"
      },
      //附件集合
      attaList: [],
      //材料业务主键
      caseMaterialOid: null,
      indexFlag: null,
      //材料与上传成功的附件集合
      materialAttaList: [],
      //材料与上传成功的附件集合
      materialAttaOidList: [],
      elemLicenseList: [],
      catalogCheckList: [],
      catalogList: [],
      caseForm: {},
      // 表单参数
      ruleForm: {
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
        applyNumber: "1",
        resultDeliveryWay: "1",
        proxyFlag: "0",
        sourceType: 1,
        sourceApp: 1,
        acceptradio: "1",
        credentialNumber: "",
        applyUserName: "",
        credentialType: "",
        contactCredentialNumber: "",
        contactUserName: "",
        bussVenueDistrictOid: []
      },
      serviceForm: {
        serviceName: "",
        serviceTypeName: "",
        organName: "",
        sxServiceLocations: {},
        zxDhText: "",
        sxServiceExtend: {},
        sxAcceptConditions: {},
        sxServiceLinks: {},
        sxServiceQuestions: {}
      },
      formData: {},
      labelPosition: "top",
      checkList: [],
      checkBoxList: [],
      viewchargeShow: false,
      chakanCharge: false,
      flagCc: true,
      stepData: [
        {
          index: "0",
          title: "智能问答"
        },
        {
          index: "1",
          title: "一次告知"
        },
        {
          index: "2",
          title: "信息登记"
        },
        {
          index: "3",
          title: "材料电子化"
        },
        {
          index: "4",
          title: "业务办理"
        }
      ],
      selectData: [],
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
        applyNumber: [
          {
            required: true,
            message: "必填项",
            trigger: "blur"
          },
          {
            validator: validateNumberNoPonint,
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
            trigger: "input"
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
            validator: validateTel,
            message: "请输入正确的申请人/申请单位号码",
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
            message: "请输入正确的固定电话",
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
        ]
      },
      optionDate: {
        disabledDate(time) {
          return time.getTime() < Date.now(); // 选当前时间之后的时间
        }
      },
      materialCatalogAttaList: [],
      userInfoShow: false,
      chargeShow: false,
      applyCardNum: "",
      dialogVisible: false,
      bgForm: false
    };
  },
  created() {
    this.getServiceSituationList();
    this.getServiceDetail();
    this.getDistrictTree();
    this.queryLoginInfo();
    this.queryPjStartType();
    this.queryPjSystem();
    this.getSelectCertificateType();
  },
  destroyed: function() {
    //在离开此页面的时候主动关闭socket
    this.socketApi.webSocketClose();
  },
  methods: {
    closeCaseCharge(obj) {
      if (obj.flag == "1" || obj.flag == "2") {
        this.chargeShow = false;
        this.chakanCharge = true;
        this.flagCc = false;
      }
      if (obj == "") {
        this.chargeShow = false;
      }
    },
    closeView() {
      this.viewchargeShow = false;
      this.chakanCharge = true;
      this.flagCc = false;
    },
    qlCaseCharge() {
      let _that = this;
      console.log(_that.chargeFlag);
      /*  if(_that.chargeFlag =="0"){

          } */
      _that.chargeShow = true;
    },
    viewCharge() {
      let _that = this;
      /*  if(_that.chargeFlag =="0"){

          } */
      _that.viewchargeShow = true;
    },
    changeValue() {
      this.$refs.ruleForm.validateField("bussVenueDistrictOid");
    },
    next(index, count) {
      if (count > 0) {
        if (index == 1) {
          let bixuan = true;
          //判断是否必选
          for (let oneTitle of this.sxServiceOptionTitlesMust) {
            if (bixuan) {
              let chooseFlag = oneTitle.chooseFlag; //是否必选  1是  0否
              let titleShow = oneTitle.titleName; //显示名称
              let isHavingCorrelation = oneTitle.isHavingCorrelation; //是否隐藏
              if (
                chooseFlag == "0" &&
                (isHavingCorrelation == "0" || isHavingCorrelation == "2")
              ) {
                this.msgWarning(titleShow + "必选！");
                bixuan = false;
              }
            }
          }
          if (bixuan) {
            this.getMaterialList();
            this.i = index;
            this.show_0 = false;
            this.show_1 = true;
          }
        }
        if (index == 2) {
          this.getCheckbox(index);
        }
        if (index == 3) {
          this.attaList = [];
          this.show_upload = [];
          this.isNextDisable = true;
          this.checkPbpjInfo(index);
        }
        if (index == 4) {
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
          this.isNextDisable = false;
        }
        if (index == 3) {
          this.show_3 = true;
          this.show_4 = false;
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
    /** 初始化评价服务 */
    queryPjSystem() {
      let _that = this;
      getPjServiceSystem().then(response => {
        //1超级综窗柜台2其他
        _that.systemType = response.data;
      });
    },
    /** 初始化评价类型 */
    queryPjStartType() {
      let _that = this;
      getPjDeviceType().then(response => {
        _that.pjType = response.data;
      });
    },
    handleChange(value) {
      console.log(value);
    },
    /** 选择情形 */
    selectChange(index, item) {
      let _that = this;
      _that.num = index;
      _that.situationName = item.title;
      _that.checkList = [];
      _that.checkBoxList = [];
      _that.sxServiceMaterials = [];
      _that.sxServiceOptionTitles = [];
      _that.sxServiceOptionTitlesMust = [];
      _that.sxServiceMaterialList = [];
      _that.qlCaseTitleValList = [];
      if (item.oid == undefined) {
        _that.getServiceSituationList();
        _that.ifDis = false;
      } else {
        _that.ifDis = true;
        getSituationOpinionList(item.oid).then(response => {
          //情形
          let optionTitle =
            response.data.sxServiceSituationOption.sxServiceOptionTitles;
          const situationOid =
            response.data.sxServiceSituationOption.situationOid;
          if (optionTitle != null && optionTitle != "") {
            optionTitle.forEach(item => {
              //情形选项值
              let titleValues = {};
              titleValues.titleName = item.name;
              titleValues.titleOid = item.oid;
              titleValues.moreStatus = item.moreStatus;
              titleValues.fillFlag = item.fillFlag;
              titleValues.isHavingCorrelation = item.isHavingCorrelation;
              titleValues.sxServiceOptionVals = item.sxServiceOptionVals;
              _that.sxServiceOptionTitles.push(titleValues);
              if (item.fillFlag == "1") {
                titleValues.chooseFlag = "0";
                //this.sxServiceOptionTitlesMust.push(titleValues);
              }
              if (
                item.sxServiceOptionVals != null &&
                item.sxServiceOptionVals != ""
              ) {
                item.sxServiceOptionVals.forEach(data => {
                  if (data.isSelected == "1") {
                    //情形标题选项关系
                    let relations = {};
                    relations.situationOid = situationOid;
                    relations.titleOid = item.oid;
                    relations.valueOid = data.oid;
                    this.qlCaseTitleValList.push(relations);
                    //按钮被选中
                    _that.checkList.push(data.oid);
                    //改变选择的状态为已选择
                    if (item.fillFlag == "1") {
                      titleValues.chooseFlag = "1";
                    }
                  }
                });
              }
              if (item.fillFlag == "1") {
                this.sxServiceOptionTitlesMust.push(titleValues);
              }
            });
          }
        });
      }
    },
    /** 勾选核验材料选项 */
    checkedBox(val) {
      let _that = this;
      _that.radio = val;
    },
    /** 核验材料勾选 */
    getCheckbox(index) {
      let _that = this;
      if (_that.radio) {
        _that.i = index;
        _that.show_1 = false;
        _that.show_2 = true;
        //判断是否需要表单显示
        sxSerFormByServiceOid(_that.serviceOid).then(response => {
          if (response.data) {
            _that.formConfig_show = true;
            _that.sxSerForm = response.data;
            _that.sxSerForm.regOid = _that.caseOid;
          }
        });
      } else {
        _that.msgWarning("请勾选我已核验上述材料并确定齐全！");
        return false;
      }
    },
    /** 获取区划树 */
    getDistrictTree(districtOid) {
      let _that = this;
      queryDistrictSimpleTree(districtOid).then(response => {
        _that.districtOptions = response.data;
      });
    },
    /** 获取证件类型 */
    getSelectCertificateType() {
      let _that = this;
      getCertificateType(_that.cegisterType).then(response => {
        _that.certificateTypeList = response.data;
      });
    },
    /** 改变证件类型 */
    changeType(item) {
      let _that = this;
      changeCredentialType(item).then(response => {
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
      });
    },
    /** 事项情形获取 */
    getServiceSituationList() {
      let _that = this;
      //查询事项情形
      getAllSituationList(_that.serviceOid).then(response => {
        let situations = response.data.sxServiceSituations;
        let optionTitles = response.data.sxServiceOptionTitles;
        //情形
        _that.getSituationOpinion(situations);
        //标题和选项
        _that.getSituationOpinionTitle(optionTitles);
        //材料
        _that.getSxServiceMaterials();
        _that.selectData = [
          {
            index: "0",
            title: "默认自定情形"
          }
        ];
        _that.getSituationOpinion(situations);
      });
    },
    /** 填充选项 */
    getSituationOpinion(situations) {
      let _that = this;
      _that.selectData = [
        {
          index: "0",
          title: "默认自定情形"
        }
      ];
      situations.forEach((item, key) => {
        let situation = {};
        situation.index = key + 1;
        situation.title = item.situationName;
        situation.oid = item.oid;
        _that.selectData.push(situation);
      });
    },
    /** 填充标题和选项值 */
    getSituationOpinionTitle(optionTitles) {
      let _that = this;
      optionTitles.forEach(optionTitle => {
        let titleValues = {};
        titleValues.titleName = optionTitle.name;
        titleValues.titleOid = optionTitle.oid;
        titleValues.moreStatus = optionTitle.moreStatus;
        titleValues.fillFlag = optionTitle.fillFlag;
        titleValues.isHavingCorrelation = optionTitle.isHavingCorrelation;
        titleValues.sxServiceOptionVals = optionTitle.sxServiceOptionVals;
        _that.sxServiceOptionTitles.push(titleValues);
        if (optionTitle.fillFlag == "1") {
          titleValues.chooseFlag = "0";
          this.sxServiceOptionTitlesMust.push(titleValues);
        }
      });
    },
    /** 获取事项 */
    getSxServiceMaterials() {
      let _that = this;
      getSituationMaterialListByOids(_that.serviceOid, "").then(response => {
        if (response.data.length > 0) {
          response.data.forEach(item => {
            this.sxServiceMaterialList.push(item);
          });
        }
      });
    },
    /** 改变选项值 */
    changeTitle(titleOid, optionValOid) {
      for (let oneTitle of this.sxServiceOptionTitlesMust) {
        if (oneTitle.titleOid == titleOid) {
          oneTitle.chooseFlag = "1";
        }
      }
      let _that = this;
      _that.sxServiceMaterialList = [];
      //情形标题选项关系
      let relations = {};
      relations.titleOid = titleOid;
      relations.valueOid = optionValOid;
      _that.getCheckTitleValList(titleOid);
      _that.qlCaseTitleValList.push(relations);
      //点击选项值 展示隐藏对应的标题
      getSxServiceOptionAllTitleValRelation(_that.serviceOid, titleOid).then(
        response => {
          response.data.forEach(data => {
            //当前点击的选项下的有关联的标题设置显示
            if (data.valOid == optionValOid) {
              data.titleList.forEach(title => {
                _that.sxServiceOptionTitles.forEach(optionTitle => {
                  if (optionTitle.titleOid == title.oid) {
                    optionTitle.isHavingCorrelation = 2;
                    _that.checkList.push(optionValOid);
                  }
                });
              });
            } else {
              //设置除了点击的标题选项之外的选项有关联的标题设置隐藏
              data.titleList.forEach(title => {
                _that.sxServiceOptionTitles.forEach(optionTitle => {
                  if (optionTitle.titleOid == title.oid) {
                    optionTitle.isHavingCorrelation = 1;
                    _that.getCheckTitleValList(title.oid);
                    _that.getCheckValList(data.valOid);
                  }
                });
              });
            }
          });
        }
      );
    },
    getCheckTitleValListBox(titleOid, relationsP, hbType) {
      let _that = this;
      //合并
      if (_that.qlCaseTitleValList.length > 0) {
        _that.qlCaseTitleValList.forEach(itemGet => {
          let item = itemGet;
          _that.getCheckTitleValList(itemGet.titleOid);
          if (item.titleOid == titleOid) {
            let relations = {};
            relations.titleOid = item.titleOid;
            if (hbType == 1) {
              relations.valueOid = item.valueOid + "," + relationsP.valueOid;
              this.qlCaseTitleValList.push(relations);
            } else {
              relations.valueOid = item.valueOid.replace(
                "," + relationsP.valueOid,
                ""
              );
              relations.valueOid = relations.valueOid.replace(
                relationsP.valueOid,
                ""
              );
              if (relations.valueOid != "") {
                this.qlCaseTitleValList.push(relations);
              } else {
                _that.getCheckTitleValList(relations.titleOid);
              }
            }
          }
        });
      } else {
        this.qlCaseTitleValList.push(relationsP);
      }
    },
    changeTitleBox(item, val) {
      //每个多选项都单独保存
      if (this.checkBoxList.indexOf(val) > -1) {
        //如果多选里面包含就添加，否则就删除
        let relations = {};
        relations.titleOid = item.titleOid;
        relations.valueOid = val;
        this.qlCaseTitleValList.push(relations);
      } else {
        this.qlCaseTitleValList.forEach((relations, j) => {
          if (relations.valueOid == val) {
            this.qlCaseTitleValList.splice(j, 1);
          }
        });
      }
      //判断是否是必须选择
      for (let oneTitle of this.sxServiceOptionTitlesMust) {
        if (oneTitle.titleOid == item.titleOid) {
          if (oneTitle.chooseFlag.indexOf(item.titleOid + val) != -1) {
            oneTitle.chooseFlag = oneTitle.chooseFlag.replace(
              "," + item.titleOid + val,
              ""
            );
          } else {
            oneTitle.chooseFlag =
              oneTitle.chooseFlag + "," + item.titleOid + val;
          }
        }
      }
      //console.log(JSON.stringify(this.qlCaseTitleValList))
    },
    getCheckTitleValList(titleOid) {
      let _that = this;
      //去除相同标题下的选项
      if (_that.qlCaseTitleValList.length > 0) {
        _that.qlCaseTitleValList.forEach(item => {
          if (item.titleOid == titleOid) {
            _that.qlCaseTitleValList.splice(
              _that.qlCaseTitleValList.indexOf(item),
              1
            );
          }
        });
      }
    },
    getCheckValList(valOid) {
      let _that = this;
      //去除相同标题下的选项
      if (_that.checkList.length > 0) {
        _that.checkList.forEach(oid => {
          if (valOid == oid) {
            _that.checkList.splice(_that.qlCaseTitleValList.indexOf(oid), 1);
          }
        });
      }
    },
    /** 点击选项值 选项有精细化材料展示精细化材料 */
    getMaterialList() {
      let _that = this;
      let allCheckItem = [];
      allCheckItem = this.checkBoxList.concat(this.checkList);
      //根据被选中的选项获取关联材料
      getSituationMaterialListByOids(_that.serviceOid, allCheckItem).then(
        response => {
          if (response.data.length > 0) {
            this.sxServiceMaterialList = response.data;
          }
          this.showGuidence();
        }
      );
    },
    //超级综窗柜台双屏显示办事指南信息
    showGuidence() {
      let _that = this;
      let url = window.location.origin;
      let pushUrl = url + "/serviceHall/guidence.html";
      let pushForm = {};
      let data = {};
      let sxService = {};
      data.userName = _that.loginUser.userName;
      data.organName = _that.loginUser.organName;
      //  pushForm.sxService = _that.sxService;
      data.sxServiceMaterialList = _that.sxServiceMaterialList;
      sxService.caseTypeName = _that.sxService.caseTypeName;
      sxService.serviceTypeName = _that.sxService.serviceTypeName;
      sxService.levelName = _that.sxService.levelName;
      sxService.chargeFlagName = _that.sxService.chargeFlagName;
      sxService.chargeFlag = _that.sxService.chargeFlag;
      sxService.zxDhText = _that.sxService.zxDhText;
      data.sxService = sxService;
      pushForm.pushUrl = pushUrl + "?jsonObject=" + JSON.stringify(data);
      //window.open(pushUrl+"?jsonObject="+JSON.stringify(pushForm))
      showCallMessage(JSON.stringify(pushForm)).then(response => {
        console.log(response.data);
      });
    },
    //材料附件
    getAttaList(caseOid) {
      let _that = this;
      _that.sxServiceMaterialAttaList = [];
      getSxServiceMaterialAttaList(caseOid).then(response => {
        if (response.data != "") {
          _that.sxServiceMaterialAttaList = response.data;
          if (
            _that.sxServiceMaterialList &&
            _that.sxServiceMaterialList.length > 0
          ) {
            _that.sxServiceMaterialList.forEach((item, index) => {
              _that.sxServiceMaterialAttaList.forEach((ite, i) => {
                if (item.materialOid == ite.materialOid) {
                  ite.sxMaterials = item;
                }
              });
            });
          }
        }
      });
    },
    /** 获取事项详细信息 */
    getServiceDetail() {
      let _that = this;
      getSxService(_that.serviceOid).then(response => {
        _that.sxService = response.data.sxService;
        _that.serviceForm.serviceName = response.data.sxService.serviceName;
        _that.serviceForm.serviceTypeName =
          response.data.sxService.serviceTypeName;
        _that.serviceForm.organName = response.data.sxService.organName;
        _that.serviceForm.sxServiceLocations = response.data.sxServiceLocations;
        _that.serviceForm.zxDhText = response.data.sxService.zxDhText;
        _that.serviceForm.sxServiceExtend = response.data.sxServiceExtend;
        _that.serviceForm.sxAcceptConditions = response.data.sxAcceptConditions;
        _that.serviceForm.sxServiceLinks = response.data.sxServiceLinks;
        _that.serviceForm.sxServiceQuestions = response.data.sxServiceQuestions;
        _that.chargeFlag = response.data.sxService.chargeFlag;
        //开启智能评价
        if (_that.pjType == "2") {
          grantWindowUser(_that.loginUser.userOid).then(resp => {
            if (resp.data) {
              _that.pushServiceToSmartEva(response.data);
            } else {
              _that.msgError("登录用户未授权到评价所属窗口！");
              return false;
            }
          });
        }
      });
    },
    //事项信息推送到智能评价系统
    pushServiceToSmartEva(data) {
      let _that = this;
      let service = {};
      service.serviceId = data.sxService.serviceOid;
      service.serviceCode = data.sxService.implementCode;
      service.serviceName = data.sxService.serviceName;
      service.serviceType = data.sxService.serviceTypeName;
      service.objectType = data.sxService.serviceObjectName;
      service.superviceTel = data.sxService.tsDhText;
      service.consultTel = data.sxService.zxDhText;
      service.acceptCondition = data.sxAcceptConditions;
      service.workdaysLimit = data.sxServiceExtend.promiseLimit;
      service.organOid = data.sxService.organOid;
      getServiceBaseInfo(service).then(response => {
        if (response.data.code == "500") {
          _that.msgError(response.data.msg);
          return false;
        }
      });
    },
    //验证表单以及坚持平板评价信息
    checkPbpjInfo(index) {
      let _that = this;
      _that.$refs["ruleForm"].validate(valid => {
        if (valid) {
          //查询是否已保存 返回id用于更新
          if (_that.caseOid != "") {
            getQlCaseByOid(_that.caseOid).then(response => {
              _that.id = response.data.id;
              _that.caseOid = response.data.caseOid;
            });
          }
          //验证表单是否必填
          if (_that.sxSerForm) {
            if (_that.sxSerForm.isFormFlag == 1) {
              //必填
              if (!_that.caseForm.formDataId) {
                _that.$message.error("表单未填写,请填写表单！");
                return false;
              }
            }
          }
          if (_that.systemType == "1") {
            _that.confirmScreenInteraction(index);
          } else {
            //验证人员是否启用评价器
            _that.pbpjConfirmUser(index);
          }
        } else {
          _that.isNextDisable = false;
        }
      });
    },
    /** 办件信息下一步保存 */
    saveApplyCaseForm(index) {
      this.$refs["ruleForm"].validate(valid => {
        if (valid) {
          let _that = this;
          _that.isDisable = true;
          _that.caseStatus = "0";
          setTimeout(() => {
            _that.ruleForm.id = _that.id;
            _that.ruleForm.caseOid = _that.caseOid;
            _that.ruleForm.applyOid = _that.applyOid;
            _that.ruleForm.extOid = _that.extOid;
            _that.ruleForm.caseStatus = _that.caseStatus;
            _that.ruleForm.applyUserType = _that.cegisterType;
            _that.ruleForm.serviceOid = _that.serviceOid;
            _that.ruleForm.caseNumber = _that.caseNumber;
            _that.ruleForm.sxServiceMaterialList = _that.sxServiceMaterialList;
            _that.ruleForm.qlCaseTitleValList = _that.qlCaseTitleValList;
            save(_that.ruleForm).then(response => {
              if (response.data != "") {
                _that.caseOid = response.data.caseOid;
                _that.applyOid = response.data.applyOid;
                _that.extOid = response.data.extOid;
                _that.caseStatus = response.data.caseStatus;
                _that.caseNumber = response.data.caseNumber;
                _that.serviceTypeName = response.data.serviceTypeName;
                _that.caseMaterialOids = response.data.caseMaterialOids;
                _that.createDate = response.data.createDate;
                _that.saveCaseForm();
                _that.msgSuccess("保存办件成功！");
                if (index != "") {
                  _that.i = index;
                  _that.show_2 = false;
                  _that.show_3 = true;
                } else {
                  //关闭页面调用列表页面
                  this.$emit("case-close");
                  return false;
                }
                //验证是否是承诺清单和申请人是否失信
                _that.checkServiceAndUser();
                //加载材料附件
                _that.getAttaList(response.data.caseOid);
              } else {
                _that.msgError("保存办件失败！");
                _that.isDisable = false;
                _that.isNextDisable = false;
                return false;
              }
            });
          }, 500);
        }
      });
    },
    /** 综窗保存材料出库信息 */
    saveMaterialOut(materialOids) {
      let _that = this;
      let outMaterial = {};
      outMaterial.regOid = _that.caseOid;
      outMaterial.caseNumber = _that.caseNumber;
      outMaterial.applyUserName = _that.ruleForm.applyUserName;
      outMaterial.serviceTypeName = _that.serviceTypeName;
      outMaterial.cardNumber = _that.ruleForm.credentialNumber;
      outMaterial.materialOids = materialOids;
      saveOut(outMaterial).then(response => {
        if (response.data == "") {
          _that.msgError("材料出库信息保存失败！");
          return false;
        }
      });
    },
    /** 保存材料附件信息 */
    saveMaterialAtta(index) {
      let _that = this;
      let dataForm = {};
      let flag = false;
      dataForm.materialAttaList = _that.materialAttaList;
      dataForm.collectionTypeList = _that.materialCollectionTypeList;
      dataForm.materialCatalogAttaList = _that.materialCatalogAttaList;
      dataForm.elemLicenseList = _that.elemLicenseList;
      /*_that.materialAttaList.forEach(atta => {
          this.catalogList.push(this.catalogCheckList[atta.attaOid]);
        });
        _that.catalogList.forEach((cata, num) => {
          _that.catalogList.forEach((log, count) => {
            if (cata && cata != null && cata != 'undefined') {
              if (num != count) {
                if (cata == log) {
                  flag = true;
                }
              }
            }
          });
        });*/
      if (
        _that.materialCatalogAttaList &&
        _that.materialCatalogAttaList.length > 0
      ) {
        _that.materialCatalogAttaList.forEach((fir, i) => {
          dataForm.materialCatalogAttaList.forEach(second => {
            //如果办件材料一样附件不一样看选择的智审目录是否相同
            if (
              fir.caseMaterialOid == second.caseMaterialOid &&
              fir.attaOid != second.attaOid
            ) {
              if (fir.materialCatalogOid == second.materialCatalogOid) {
                flag = true;
              }
            }
          });
        });
      }
      if (flag) {
        _that.msgError("上传文件请选择不同的目录材料！");
        return false;
      } else {
        let noticeFlag = false; //标识是否允许缺省材料验证
        //是否存在 必须 材料,未收取
        for (let i = 0; i < _that.sxServiceMaterialAttaList.length; i++) {
          let mustTypeFlag = false;
          //必要材料收取附件
          let mustAttaFlag = false;
          //是否选择上传附件
          let uploadFlag = false;
          let materialName = "";
          const cmMaterial = _that.sxServiceMaterialAttaList[i];
          //必须，看是否有收取
          if (cmMaterial.sxMaterials.materialMustFlag == 1) {
            for (let j = 0; j < _that.materialCollectionTypeList.length; j++) {
              const type = _that.materialCollectionTypeList[j];
              if (type.caseMaterialOid == cmMaterial.caseMaterialOid) {
                if (type.collectionType) {
                  mustTypeFlag = true;
                  //已经点击收取类型，后遍历收取的材料
                  //看是否为纸质，则无材料
                  if (type.collectionType == 1) {
                    //无材料
                    mustAttaFlag = true;
                  } else if (
                    type.collectionType == 2 ||
                    type.collectionType == 3
                  ) {
                    if (_that.materialAttaList.length > 0) {
                      for (let k = 0; k < _that.materialAttaList.length; k++) {
                        const atta = _that.materialAttaList[k];
                        if (
                          atta.caseMaterialOid == cmMaterial.caseMaterialOid
                        ) {
                          mustAttaFlag = true;
                          break;
                        }
                      }
                    }
                  } else if (type.collectionType == 4) {
                    //容缺后补
                    mustAttaFlag = true;
                  } else if (type.collectionType == 5) {
                    //电子证照
                    if (_that.elemLicenseList.length > 0) {
                      for (let k = 0; k < _that.elemLicenseList.length; k++) {
                        const atta = _that.elemLicenseList[k];
                        if (atta.materialOid == cmMaterial.caseMaterialOid) {
                          mustAttaFlag = true;
                          break;
                        }
                      }
                    }
                  }
                } else {
                  mustTypeFlag = false;
                }
              }
            }
          } else {
            mustTypeFlag = true;
            mustAttaFlag = true;
          }
          //选择上传附件 扫描 证照判断是否有附件上传
          if (_that.materialCollectionTypeList.length > 0) {
            for (let k = 0; k < _that.materialCollectionTypeList.length; k++) {
              const coll = _that.materialCollectionTypeList[k];
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
          //如果不为true，给出提示
          if (mustTypeFlag == false) {
            if (!_that.checkFlag) {
              _that.msgError(
                "[" + cmMaterial.materialName + "]必须材料的收取方式未选择！"
              );
              return false;
            } else {
              noticeFlag = true;
            }
          }
          if (mustAttaFlag == false) {
            if (!_that.checkFlag) {
              _that.msgError(
                "[" + cmMaterial.materialName + "]必须材料的未收取！"
              );
              return false;
            } else {
              noticeFlag = true; //允许缺省材料
            }
          }
        }
        //如果有容缺后补材料则不验证承诺告知
        if (_that.isRqslFlag == true) {
          //容缺受理
          _that.saveMaterialInfo(index, dataForm);
        } else {
          if (_that.checkFlag == true && noticeFlag == true) {
            //判断是否勾选承诺告知
            if (_that.radioNotice) {
              //保存材料和补齐补正
              if (!_that.bqbzCaseForm.dueDate) {
                _that.$message.error("请选择承诺补正时间!");
                return false;
              }
              _that.saveMaterialInfo(index, dataForm);
              //_that.saveCaseCorrection();
            } else {
              _that.$message.error("当前支持承诺告知方式提交材料,请勾选");
              return false;
            }
          } else if (_that.radioNotice) {
            //保存材料和补齐补正
            if (!_that.bqbzCaseForm.dueDate) {
              _that.$message.error("请选择承诺补正时间!");
              return false;
            }
            _that.saveMaterialInfo(index, dataForm);
            //_that.saveCaseCorrection();
          } else {
            _that.saveMaterialInfo(index, dataForm);
          }
        }
      }
    },
    /** 办件受理 */
    caseAccpet() {
      let _that = this;
      if (_that.caseOid != "") {
        let formData = new FormData();
        formData.append("caseOid", _that.caseOid);
        formData.append("userOid", _that.loginUser.userOid);
        formData.append("userName", _that.loginUser.userName);
        formData.append("finalOpinion", _that.ruleForm.acceptradio);
        formData.append("finalOpinionDesc", _that.ruleForm.finalOpinionDesc);
        if (_that.ruleForm.acceptradio == 2) {
          if (!_that.ruleForm.finalOpinionDesc) {
            this.$message.error("意见说明不能为空！");
            return false;
          }
        }
        if (_that.isRqslFlag == true) {
          formData.append("rqbzDueDate", _that.ruleForm.bqbzDueDate);
          if (!_that.ruleForm.bqbzDueDate) {
            this.$message.error("承诺补正时间不能为空!");
            return false;
          }
        }
        saveCaseAccpet(formData).then(response => {
          if (response.data != "") {
            _that.linkResultOid = response.data.linkResultOid;
            if (_that.ruleForm.acceptradio == 2) {
              let noticeMes = {};
              noticeMes.applyUserName = _that.ruleForm.applyUserName;
              noticeMes.caseNumber = _that.caseNumber;
              noticeMes.createDate = _that.createDate;
              noticeMes.isRqslFlag = "";
              _that.msgSuccess("办件不予受理保存成功！");
              this.$emit("case-close", noticeMes); //不予受理通知书
            } else {
              _that.msgSuccess("办件进入受理成功！");
              //保存补齐补正信息
              if (_that.bqbzCaseForm.dueDate && _that.radioNotice) {
                _that.saveCaseCorrection();
              }
              if (_that.collectionArray.indexOf("1") > -1) {
                //只有纸质的材料进行出库信息保存
                let materialOids = "";
                _that.materialCollectionTypeList.forEach((ite, i) => {
                  if (ite.collectionType == 1) {
                    materialOids += ite.caseMaterialOid + ";";
                  }
                });
                _that.saveMaterialOut(materialOids);
              }
              if (_that.chargeFlag == "1") {
                _that.chargeShow = true;
              }
              if (_that.isRqslFlag == true) {
                //打印容缺承诺书
                let noticeMes = {};
                noticeMes.applyUserName = _that.ruleForm.applyUserName;
                noticeMes.caseName = _that.ruleForm.projectName;
                noticeMes.createDate = _that.createDate;
                noticeMes.isRqslFlag = "true";
                this.$emit("case-close", noticeMes);
              } else {
                this.$emit("case-close", "");
              }
            }
          }
        });
      } else {
        _that.msgError("办件信息未保存，请先保存！");
        return false;
      }
    },
    saveMaterialInfo(index, dataForm) {
      let _that = this;
      saveQlCaseMaterialAtta(dataForm).then(response => {
        if (response.code == 200) {
          /*_that.msgSuccess("保存材料附件成功！");
            _that.i = index;
            _that.show_3 = false;
            _that.show_4 = true;*/
          var letList = response.data;
          var size = letList.length;
          if (letList && size > 0) {
            /*材料预审start*/
            letList.forEach((item, num) => {
              let caseOid = this.caseOid;
              let caseMaterialOid = item.caseMaterialOid;
              let caseFileRecOid = item.materialAttaOid;
              let cataOid = item.materialCatalogOid;
              intelligentPretrialmaterialPrePrial(
                caseOid,
                caseFileRecOid,
                caseMaterialOid,
                cataOid
              ).then(response => {
                if (num + 1 == letList.length) {
                  _that.msgSuccess("保存材料附件成功！");
                  _that.i = index;
                  _that.loading = false;
                  _that.show_3 = false;
                  _that.show_4 = true;
                }
              });
              /*材料预审end*/
            });
          } else {
            _that.msgSuccess("保存材料附件成功！");
            _that.i = index;
            _that.loading = false;
            _that.show_3 = false;
            _that.show_4 = true;
          }
        } else {
          _that.msgError("保存材料附件失败！");
          return false;
        }
      });
    },
    /** 勾选告知承诺清单 */
    checkedBoxNotice(val) {},
    //验证当前事项是否是告知承诺清单和当前申请人是否在失信名单中
    checkServiceAndUser() {
      let _that = this;
      getInformPromiseByServiceOid(this.serviceOid).then(response => {
        if (response.data) {
          getDishonestPerson(
            _that.ruleForm.applyUserName,
            _that.ruleForm.credentialNumber
          ).then(response => {
            if (response.data) {
              _that.checkFlag = false;
              _that.isSxPersonFlg = true; //失信人
            } else {
              _that.checkFlag = true;
            }
          });
        } else {
          _that.checkFlag = false;
          getDishonestPerson(
            _that.ruleForm.applyUserName,
            _that.ruleForm.credentialNumber
          ).then(response => {
            if (response.data) {
              //失信人
              _that.isSxPersonFlg = true; //失信人
            } else {
              _that.isSxPersonFlg = false;
            }
          });
        }
      });
    },
    //保存补齐补正信息
    saveCaseCorrection() {
      let _that = this;
      _that.bqbzCaseForm.caseOid = _that.caseOid;
      _that.bqbzCaseForm.caseNumber = _that.caseNumber;
      _that.bqbzCaseForm.applyUserName = _that.ruleForm.applyUserName;
      _that.bqbzCaseForm.applyProjectName = _that.ruleForm.projectName;
      _that.bqbzCaseForm.userPhone = _that.ruleForm.applyUserPhone;
      _that.bqbzCaseForm.bzType = "0";
      saveOrUpdateGzBqbz(_that.bqbzCaseForm).then(response => {
        if (response.code == 200) {
        }
      });
    },
    //超级综窗柜台双屏互动信息确认
    confirmScreenInteraction(index) {
      let _that = this;
      let url = window.location.origin;
      let pushForm = {};
      let data = {};
      let content = {};
      let pushUrl = url + "/serviceHall/confirmation.html";
      content.projectName = _that.ruleForm.projectName;
      content.applyUserType = _that.ruleForm.applyUserType;
      if (_that.ruleForm.applyUserType == "0") {
        content.applyUserType = "个人";
      } else {
        content.applyUserType = "法人";
      }
      content.applyUserName = _that.ruleForm.applyUserName;
      content.credentialNumber = _that.ruleForm.credentialNumber;
      content.applyPostCode = _that.ruleForm.applyPostCode;
      content.applyUserAddress = _that.ruleForm.applyUserAddress;
      content.applyUserPhone = _that.ruleForm.applyUserPhone;
      //getCertificateTypeByOid(_that.ruleForm.credentialType).then(response => {
      content.credentialName = "身份证";
      // });
      data.userName = _that.loginUser.userName;
      data.organName = _that.loginUser.organName;
      data.content = content;
      let jsonObject = JSON.stringify(data);
      pushForm.pushUrl = pushUrl + "?jsonObject=" + jsonObject;
      _that
        .$confirm("你确定要进行办件信息确认吗？", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
        .then(function() {
          //window.open(pushUrl+"?jsonObject="+JSON.stringify(pushForm))
          showCallMessage(JSON.stringify(pushForm)).then(response => {
            console.log(response.data);
          });
        })
        .then(function() {
          //保存办件
          _that.saveApplyCaseForm(index);
        })
        .catch(action => {
          if (action === "cancel") {
            //保存办件
            _that.saveApplyCaseForm(index);
          }
        });
    },
    //查询当前登录人员是否开启平板确认或者评价
    pbpjConfirmUser(index) {
      let _that = this;
      pushPbpjUser(_that.loginUser.userOid).then(response => {
        if (response.data != "") {
          if (response.data.confirmFlag == "1") {
            _that.initPushContent(index);
          } else {
            _that.saveApplyCaseForm(index);
          }
        }
      });
    },
    //初始化办件信息
    initPushContent(index) {
      let _that = this;
      let content = {};
      content.projectName = _that.ruleForm.projectName;
      content.applyUserType = _that.ruleForm.applyUserType;
      if (_that.ruleForm.applyUserType == "0") {
        content.applyUserType = "个人";
      } else {
        content.applyUserType = "法人";
      }
      content.applyUserName = _that.ruleForm.applyUserName;
      content.legalPersonName = _that.ruleForm.legalPersonName;
      getCertificateTypeByOid(_that.ruleForm.credentialType).then(response => {
        content.credentialType = response.data.name;
      });
      content.credentialNumber = _that.ruleForm.credentialNumber;
      content.applyPostCode = _that.ruleForm.applyPostCode;
      content.applyUserAddress = _that.ruleForm.applyUserAddress;
      content.applyUserPhone = _that.ruleForm.applyUserPhone;
      content.contactUserName = _that.ruleForm.contactUserName;
      content.contactCredentialNumber = _that.ruleForm.contactCredentialNumber;
      content.contactUserPhone = _that.ruleForm.contactUserPhone;
      content.remark = _that.ruleForm.contactRemark;
      if (_that.pjType == "1") {
        //推送平板评价办件确认信息
        _that.confirmPbpj(index, content, _that.pjType);
      } else {
        //推送智能评价办件确认信息
        _that.confirmSmartPj(index, content, _that.pjType);
      }
    },
    //智能评价确认信息
    confirmSmartPj(index, content, pjType) {
      let _that = this;
      _that
        .$confirm("你确定要进行办件信息确认吗？", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
        .then(function() {
          pushSmartEvaConfirm(JSON.stringify(content)).then(response => {
            let mess = JSON.parse(response.data);
            if (mess.code == 0) {
              _that.msgWarning("正在信息确认...");
              setTimeout(() => {
                _that.getDataCheckValue(1, index, pjType);
              }, 1000);
            } else {
              _that.msgError(response.data.msg);
              return false;
            }
          });
        });
    },
    //平板评价确认信息
    confirmPbpj(index, content, pjType) {
      let _that = this;
      let userOid = _that.loginUser.userOid;
      let url = window.location.origin;
      let pushUrl = url + "/pbpj/pbpjCase.html";
      /*let content={};
        content.projectName= _that.ruleForm.projectName;
        content.applyUserType = _that.ruleForm.applyUserType;
        content.applyUserName = _that.ruleForm.applyUserName;
        content.legalPersonName = _that.ruleForm.legalPersonName;
        content.credentialType = _that.ruleForm.credentialType;
        content.credentialNumber = _that.ruleForm.credentialNumber;
        content.applyPostCode= _that.ruleForm.applyPostCode;
        content.applyUserAddress = _that.ruleForm.applyUserAddress;
        content.applyUserPhone = _that.ruleForm.applyUserPhone;
        content.contactUserName = _that.ruleForm.contactUserName;
        content.contactCredentialNumber = _that.ruleForm.contactCredentialNumber;
        content.contactUserPhone = _that.ruleForm.contactUserPhone;
        content.remark = _that.ruleForm.contactRemark;*/
      let pushForm = {};
      pushForm.userOid = userOid;
      pushForm.type = "3";
      pushForm.pushUrl = pushUrl;
      pushForm.content = content;
      pushPbpjCheckLogin(userOid).then(response => {
        if (response.data) {
          //检查平板评价登录
          _that
            .$confirm("你确定要进行办件信息确认吗？", "警告", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning"
            })
            .then(function() {
              pushPbpjConfirmInfo(JSON.stringify(pushForm)).then(response => {
                if (response.data) {
                  _that.msgWarning("正在信息确认...");
                  setTimeout(() => {
                    _that.getDataCheckValue(1, index, pjType);
                  }, 1000);
                }
              });
            })
            .catch(function() {
              _that.saveApplyCaseForm(index);
            });
        } else {
          _that.msgWarning("平板评价未登录，请登录平板评价器！");
        }
      });
    },
    //平板评价确认
    getDataCheckValue(num, index, pjType) {
      let _that = this;
      let userOid = _that.loginUser.userOid;
      if (pjType == "1") {
        getPbpjConfirmInfo(userOid).then(response => {
          if (response.data == "1") {
            //保存办件
            _that.saveApplyCaseForm(index);
          } else if (response.data == "0") {
            _that.msgError("确认信息有误，请重新修改办件信息！");
            return false;
          } else {
            if (num >= 60) {
              //60s后自动保存
              //保存办件
              _that.saveApplyCaseForm(index);
            } else {
              //每隔一秒钟获取一次评价值，35秒后未获取到值，默认为未确定
              setTimeout(function() {
                _that.getDataCheckValue(num + 1, index, pjType);
              }, 1000);
            }
          }
        });
      } else {
        getSmartPjConfirmFlag(userOid).then(response => {
          if (response.data == "0") {
            //保存办件
            _that.saveApplyCaseForm(index);
          } else if (response.data == "1") {
            _that.msgError("确认信息有误，请重新修改办件信息！");
            return false;
          } else {
            if (num >= 35) {
              //35s后自动保存
              //保存办件
              _that.saveApplyCaseForm(index);
            } else {
              //每隔一秒钟获取一次评价值，35秒后未获取到值，默认为未确定
              setTimeout(function() {
                _that.getDataCheckValue(num + 1, index, pjType);
              }, 1000);
            }
          }
        });
      }
    },
    //办件平板评价
    pushPjCaseService() {
      let _that = this;
      let userOid = _that.loginUser.userOid;
      if (_that.systemType == "1") {
        _that.pushInteractionPj();
      } else {
        pushPbpjUser(userOid).then(response => {
          //推送办件评价
          if (response.data != "") {
            if (response.data.appraiseFlag == "1") {
              if (_that.pjType == "1") {
                //推送平板评价
                _that.pushPbpj();
              } else {
                //推送智能评价
                _that.pushSmartpj();
              }
            } else {
              //没用启用评价 直接受理
              _that.caseAccpet();
            }
          }
        });
      }
    },
    //超级综窗柜台双屏互动办件评价
    pushInteractionPj() {
      let _that = this;
      let url = window.location.origin;
      let pushUrl = url + "/serviceHall/evaluation.html";
      let pushForm = {};
      let data = {};
      data.userName = _that.loginUser.userName;
      data.organName = _that.loginUser.organName;
      data.projectName = _that.ruleForm.projectName;
      data.caseNumber = _that.caseNumber;
      data.createDate = _that.createDate
        ? _that.createDate.substring(0, 10)
        : _that.createDate;
      pushForm.pushUrl = pushUrl + "?jsonObject=" + JSON.stringify(data);
      _that
        .$confirm("你确定要进行办件评价吗？", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
        .then(function() {
          showCallMessage(JSON.stringify(pushForm)).then(response => {
            if (response.data) {
              _that.msgWarning("办件评价中...");
            }
          });
        })
        .then(function() {
          //保存办件
          _that.caseAccpet();
        })
        .catch(action => {
          if (action === "cancel") {
            _that.caseAccpet();
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
              pbpjSaveCaseInfo(_that.caseOid).then(response => {
                if (response.data != "") {
                  let json = JSON.parse(response.data);
                  if (json.state == "0") {
                    pushPbpjInfo(_that.caseNumber).then(response => {
                      if (response.data != null) {
                        _that.caseAccpet();
                      }
                    });
                  }
                } else {
                  _that.msgError("平板评价保存办件失败！");
                  return false;
                }
              });
            })
            .catch(function() {
              _that.caseAccpet();
            });
        } else {
          _that.msgWarning("平板评价未登录，请登录平板评价器！");
          return false;
        }
      });
    },
    //智能评价
    pushSmartpj() {
      let _that = this;
      _that
        .$confirm("你确定要进行办件评价吗？", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
        .then(function() {
          savePjMark(_that.caseOid).then(response => {
            let mess = JSON.parse(response.data);
            if (mess.code == 0) {
              _that.caseAccpet();
            } else {
              _that.msgError("平板评价保存办件失败！");
              return false;
            }
          });
        });
    },
    /** 送达方式 */
    changeDeliveryWay(val) {
      let _that = this;
      _that.address_show = val === "1" ? true : false;
    },
    /** 是否代理人 */
    changeProxyFlag(val) {
      let _that = this;
      _that.proxy_show = val === "1" ? true : false;
    },
    /** 材料形式 */
    chooseCollectionType(val, index, item) {
      let _that = this;
      if (val == "2") {
        _that.show_upload[index] = true;
        _that.show_scan[index] = false;
        _that.show_elem[index] = false;
      } else if (val == "3") {
        _that.show_scan[index] = true;
        _that.show_upload[index] = false;
        _that.show_elem[index] = false;
      } else if (val == "5") {
        _that.show_elem[index] = true;
        _that.show_scan[index] = false;
        _that.show_upload[index] = false;
      } else {
        _that.show_upload[index] = false;
        _that.show_elem[index] = false;
        _that.show_scan[index] = false;
      }
      _that.radio1.push(item.caseMaterialOid);
      _that.collectionArray[index] = val;
      if (_that.collectionArray.indexOf("4") > -1) {
        _that.isCheckedDisable = true;
        _that.isRqslFlag = true;
        _that.radioNotice = "";
      } else {
        _that.isCheckedDisable = false;
        _that.isRqslFlag = false;
      }
      //变量存入材料和上传方式
      let collection = {};
      collection.caseMaterialOid = item.caseMaterialOid;
      collection.collectionType = val;
      collection.collectNumber = item.sxMaterials.paperNumber;
      collection.materialName = item.materialName;
      if (_that.materialCollectionTypeList.length > 0) {
        _that.materialCollectionTypeList.forEach(data => {
          if (data.caseMaterialOid == item.caseMaterialOid) {
            _that.materialCollectionTypeList.splice(
              _that.materialCollectionTypeList.indexOf(data),
              1
            );
          }
        });
      }
      _that.materialCollectionTypeList.push(collection);
      //当收取方式为纸质、附件、扫描、荣却后补的时候清空证照
      if (val == "1" || val == "2" || val == "3" || val == "4") {
        _that.elemLicenseList.forEach((ite, i) => {
          if (ite.materialOid == item.caseMaterialOid) {
            _that.elemLicenseList.splice(i, 1);
          }
        });
        //删除证照
        _that.$set(_that.attaList, index, []);
      }
      //当为纸质、证照、荣却后补的时候清空附件
      //if (val == '1' || val == '4' || val == '5') {
      this.sxServiceMaterialAttaList.forEach((itemAtta, ak) => {
        if (itemAtta.caseMaterialOid == item.caseMaterialOid) {
          itemAtta.attaList = [];
        }
      });
      //删除材料附件关系
      this.materialAttaList.forEach((attItem, j) => {
        if (attItem.caseMaterialOid == item.caseMaterialOid) {
          //删除材料智审关联
          if (_that.materialCatalogAttaList != "") {
            _that.materialCatalogAttaList.forEach((catalog, z) => {
              if (catalog.attaOid == attItem.attaOid) {
                _that.materialCatalogAttaList.splice(z, 1);
              }
            });
          }
          //删除附件关系
          this.materialAttaList.splice(j, 1);
        }
      });
      //}
    },
    /** 附件目录材料 */
    chooseCatalog(caseMaterialOid, attaOid, materialCatalogOid) {
      let _that = this;
      let catalog = {};
      catalog.attaOid = attaOid;
      catalog.materialCatalogOid = materialCatalogOid;
      catalog.caseMaterialOid = caseMaterialOid;
      if (
        _that.materialCatalogAttaList &&
        _that.materialCatalogAttaList.length > 0
      ) {
        _that.materialCatalogAttaList.forEach(catalog => {
          if (catalog.attaOid == attaOid) {
            _that.materialCatalogAttaList.splice(
              _that.materialCatalogAttaList.indexOf(catalog),
              1
            );
          }
        });
      }
      _that.materialCatalogAttaList.push(catalog);
    },
    /** 加载送达信息 */
    getApplyInfo() {
      let _that = this;
      _that.ruleForm.addresseeName = _that.ruleForm.applyUserName;
      _that.ruleForm.addresseePostCode = _that.ruleForm.applyPostCode;
      _that.ruleForm.addresseePhone = _that.ruleForm.applyUserPhone;
      _that.ruleForm.addresseeTel = _that.ruleForm.applyUserTel;
    },
    /** 重置按钮操作 */
    resetQuery() {
      let _that = this;
      _that.resetForm("queryForm");
      _that.handleQuery();
    },
    /** 隐藏右侧 */
    hiddenRightSideBar() {
      let _that = this;
      _that.rightShow = false;
    },
    /** 显示右侧 */
    showRightSideBar() {
      let _that = this;
      _that.rightShow = true;
    },
    /** 表单重置 */
    reset() {
      let _that = this;
      Object.assign(_that.queryForm, _that.$options.data().queryForm);
      _that.resetForm("queryForm");
    },
    /** 取消按钮 */
    cancel() {
      let _that = this;
      _that.reset();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      let _that = this;
      _that.queryParams.pageNum = 1;
      _that.getList();
    },
    pushMaterialOid(caseMaterialOid) {
      let _that = this;
      _that.caseMaterialOid = caseMaterialOid;
    },
    /** 失败后返回 */
    uploadError(resp) {
      let _that = this;
      _that.msgError("文件上传失败");
    },
    /** 上传附件 */
    uploadFiles(file) {
      let _that = this;
      let formData = new FormData();
      formData.append("files", file.file);
      uploadCaseMaterialFile(formData).then(response => {
        if (response.data != "") {
          response.data.forEach(data => {
            let list = {};
            file.data.attaList.push(data);
            list.caseMaterialOid = _that.caseMaterialOid;
            list.attaOid = data.attaOid;
            _that.materialAttaList.push(list);
            if (_that.materialCollectionTypeList.length > 0) {
              _that.materialCollectionTypeList.forEach(collection => {
                if (collection.caseMaterialOid == _that.caseMaterialOid) {
                  collection.attaOid = data.attaOid;
                }
              });
            }
          });
        } else {
          _that.$message.error("不允许上传空文件!");
        }
      });
    },
    /** 上传附件请求操作 */
    beforeUpload(file) {
      let _that = this;
      let isRightSize = file.size / 1024 / 1024 < 100;
      if (!isRightSize) {
        _that.$message.error("文件大小超过 100MB");
      }
      _that.fileList.push(file);
      return isRightSize;
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
    getConfigResult(data, index) {
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
            let materialOid = data.type;
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
                uploadCaseMaterialFile(formD).then(response => {
                  if (response.data != "") {
                    response.data.forEach(attaInfo => {
                      _that.sxServiceMaterialAttaList[index].attaList.push(
                        attaInfo
                      );
                      let list = {};
                      list.caseMaterialOid = materialOid;
                      list.attaOid = attaInfo.attaOid;
                      _that.materialAttaList.push(list);
                      //塞入上传附件
                      if (_that.materialCollectionTypeList.length > 0) {
                        _that.materialCollectionTypeList.forEach(collection => {
                          if (collection.caseMaterialOid == materialOid) {
                            collection.attaOid = attaInfo.attaOid;
                          }
                        });
                      }
                    });
                  } else {
                    _that.$message.error("不允许上传空文件!");
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
    scanPicture(caseMaterialOid, index) {
      //caseMaterialOid--所属材料主键
      //index--所属操作行索引
      //Device：设备类型、
      let info = '{"device":"HighCamera", "type":"' + caseMaterialOid + '"}';
      //建立socket连接
      this.socketApi.initWebSocket(this.socketError);
      this.socketApi.sendSock(info, this.getConfigResult, index);
    },
    getElecLicenInfo(materialOid, caseMaterialOid, index) {
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
        queryElecLicenseByDirCode(materialOid, userName, idCard, "").then(
          response => {
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
                });
              }
              _that.elemLicenseList.push(res);
              //塞入上传附件
              if (_that.materialCollectionTypeList.length > 0) {
                _that.materialCollectionTypeList.forEach(collection => {
                  if (collection.caseMaterialOid == caseMaterialOid) {
                    collection.attaOid = response.data.elecLicenOid;
                  }
                });
              }
            } else {
              _that.$message.error("暂无证照,请检查证照相关配置！");
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
    },
    //材料查看
    viewMaterialAddr(attaOid) {
      if (attaOid) {
        this.fileSxAttaOid = attaOid;
        this.showSxFile = true;
      } else {
        this.$message.warning("暂无材料查看！");
      }
    },
    deleteAtta(list, atta) {
      this.sxServiceMaterialAttaList.forEach(item => {
        if (item.attaList.length > 0) {
          item.attaList.forEach((ite, i) => {
            if (ite.attaOid == list.attaOid) {
              item.attaList.splice(i, 1);
            }
          });
        }
      });
      //删除材料附件关系
      this.materialAttaList.forEach((attItem, j) => {
        if (attItem.attaOid == list.attaOid) {
          this.materialAttaList.splice(j, 1);
        }
      });
      //删除材料智审关联
      if (this.materialCatalogAttaList != "") {
        this.materialCatalogAttaList.forEach((catalog, z) => {
          if (catalog.attaOid == list.attaOid) {
            this.materialCatalogAttaList.splice(z, 1);
          }
        });
      }
    },
    //关闭预览附件
    closeFileView() {
      this.showSxFile = false;
    },
    //关闭预览附件
    closeCaseFileView() {
      this.showFile = false;
    },
    //预览附件
    viewFile(attaOid) {
      this.fileAttaOid = attaOid;
      this.showFile = true;
    },
    downLoadMaterialAddr(attaOid) {
      if (attaOid) {
        downloadAttaSimple(attaOid).then(response => {});
      } else {
        this.$message.warning("暂无材料！");
      }
    },
    //表单填报
    formFilling() {
      let _that = this;
      if (_that.sxSerForm) {
        if (_that.sxSerForm.formType == 0) {
          //自定义表单
          if (_that.sxSerForm.formAddr) {
            window.open(_that.sxSerForm.formAddr, "width=1200px;height=800px;");
          } else {
            _that.$message.error("请配置表单地址！");
          }
        } else if (_that.sxSerForm.formType == 1) {
          //电子表单
          _that.iframeState = true;
          //查询电子表单地址配置
          // "http://172.168.249.2:8081/form//loadInitFormReport.do?authorizeKey=00b98ec063984da0afca427d3637fe2a&formOid=FORM20180927GOXSMMER";
          _that.iframeUrl =
            process.env.VUE_APP_DZBD_TB_ROUTE_PATH +
            "&formOid=" +
            _that.sxSerForm.formCode;
          if (_that.caseForm.formDataId) {
            _that.iframeUrl += "&reportOid=" + _that.caseForm.formDataId;
          }
        }
      }
    },
    //修改表单关闭
    closeIframe(reportOid) {
      if (reportOid) {
        this.caseForm.formDataId = reportOid;
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
      _that.caseForm.regOid = _that.caseOid;
      _that.caseForm.serFormOid = _that.sxSerForm.oid;
      if (_that.caseForm.formDataId) {
        saveOrUpdateCaseForm(this.caseForm).then(response => {
          if (response.data) {
            _that.caseForm = response.data;
          }
        });
      }
    },
    //查看电子表单
    viewFormFilling() {
      let _that = this;
      if (_that.sxSerForm) {
        if (_that.sxSerForm.formType == 0) {
          //自定义表单
          if (_that.sxSerForm.formAddr) {
            window.open(_that.sxSerForm.formAddr, "width=1200px;height=800px;");
          } else {
            _that.$message.error("请配置表单地址！");
          }
        } else if (_that.sxSerForm.formType == 1) {
          //电子表单
          _that.iframVieweState = true;
          _that.iframeUrlView =
            process.env.VUE_APP_DZBD_CK_ROUTE_PATH +
            "&reportOid=" +
            _that.caseForm.formDataId;
        }
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
        text: "材料审核中,请耐心等待...",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });

      let _that = this;
      let dataForm = {};
      let flag = false;
      dataForm.materialAttaList = _that.materialAttaList;
      dataForm.collectionTypeList = _that.materialCollectionTypeList;
      dataForm.materialCatalogAttaList = _that.materialCatalogAttaList;
      dataForm.elemLicenseList = _that.elemLicenseList;

      if (
        !_that.materialCatalogAttaList ||
        _that.materialCatalogAttaList.length <= 0
      ) {
        loadingnew.close();
        _that.msgError("上传文件未关联目录信息不可进行智审！");
        return false;
      } else {
        _that.materialCatalogAttaList.forEach((fir, i) => {
          dataForm.materialCatalogAttaList.forEach(second => {
            //如果办件材料一样附件不一样看选择的智审目录是否相同
            if (
              fir.caseMaterialOid == second.caseMaterialOid &&
              fir.attaOid != second.attaOid
            ) {
              if (fir.materialCatalogOid == second.materialCatalogOid) {
                flag = true;
              }
            }
          });
        });
      }
      if (flag) {
        loadingnew.close();
        _that.msgError("上传文件请选择不同的目录材料！");
        return false;
      } else {
        saveQlCaseMaterialAtta(dataForm).then(response => {
          _that.loading = true;
          if (response.code == 200) {
            _that.catalogList = [];
            var letList = response.data;
            var size = letList.length;
            if (letList && size > 0) {
              console.log("材料智审保存附件返回值：" + JSON.stringify(letList));
              for (let i = 0; i < letList.length; i++) {
                let item = letList[i];
                let caseOid = this.caseOid;
                let caseMaterialOid = item.caseMaterialOid;
                let caseFileRecOid = item.materialAttaOid;
                let cataOid = item.materialCatalogOid;
                intelligentPretrialmaterialPrePrial(
                  caseOid,
                  caseFileRecOid,
                  caseMaterialOid,
                  cataOid
                ).then(response => {
                  let str = "";
                  if (!response.data) {
                    _that.$message.error("智审连接超时!");
                    loadingnew.close();
                    return false;
                  }
                  if (response.data.success == false) {
                    str = response.data.message;
                    _that.$message.error(str);
                    loadingnew.close();
                    return false;
                  }
                  if (i + 1 == letList.length) {
                    loadingnew.close();
                    let item = {
                      caseOid: caseOid,
                      caseMaterialOid: caseMaterialOid,
                      caseFileRecOid: caseFileRecOid,
                      cataOid: cataOid,
                      show: true,
                      title: "智能审核结果"
                    };
                    this.intelligentPretrialOptions.push(item);
                  }
                });
              }
            }
          } else {
            loadingnew.close();
            _that.msgError("智审失败！");
            return false;
          }
        });
      }
    },
    openMaterialComparison(materiaId, caseMaterialOid) {
      let attaOids = "";
      if (this.materialAttaList && this.materialAttaList.length > 0) {
        this.materialAttaList.forEach((ite, i) => {
          if (ite.caseMaterialOid == caseMaterialOid) {
            attaOids += ite.attaOid + ";";
          }
        });
      }
      let sampleInfoOid = this.ruleForm.serviceOid;
      let comboDirectoryOid = "";
      let materiaOid = materiaId;
      let caseOid = this.caseOid;
      let fileViewurls =
        process.env.BASE_URL +
        "picture/prviewList.html?materiaOid=" +
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
        materiaOid: materiaOid,
        fileViewurl: fileViewurls,
        caseOid: caseOid,
        attaOids: attaOids
      };
      this.materialComparisonOptions.push(item);
    },
    printNotice() {
      this.$confirm("是否打印告知承诺书?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.handleGzcnPrint();
        })
        .catch(() => {});
    },
    //调用pageoffice的打印
    handleGzcnPrint() {
      let _that = this;
      let caseName = encodeURIComponent(_that.ruleForm.projectName);
      let applyUserName = encodeURIComponent(_that.ruleForm.applyUserName);
      let sqTime = _that.bqbzCaseForm.dueDate;
      POBrowser.openWindowModeless(
        process.env.VUE_APP_BASE_API_PAGE +
          "/manage/zhuozheng/cngzNoticePrint?caseName=" +
          caseName +
          "&applyUserName=" +
          applyUserName +
          "&sqTime=" +
          sqTime,
        "width=1200px;height=800px;"
      );
    },
    getUserInfo(credentialNumber) {
      if (!credentialNumber) {
        this.$message.error("证件号不能为空!");
        return false;
      }
      this.applyCardNum = credentialNumber;
      this.userInfoShow = true;
    },
    closeUserInfoShow(userCase) {
      this.queryQlCaseInfo(userCase);
      this.userInfoShow = false;
    },
    //查询保存的办件数据 并回填数据
    queryQlCaseInfo(userCase) {
      let _that = this;
      if (userCase != "") {
        queryAllCaseByOid(userCase).then(response => {
          let qlCase = response.data.qlCase;
          let qlCaseApplay = response.data.qlCaseApplay;
          let qlCaseExt = response.data.qlCaseExt;
          _that.ruleForm.projectName = qlCase.projectName;
          _that.ruleForm.specificLocation = qlCaseApplay.specificLocation;
          _that.ruleForm.applyNumber = qlCaseApplay.applyNumber;
          _that.ruleForm.applyUserName = qlCaseApplay.applyUserName;
          _that.ruleForm.applyUserPhone = qlCaseApplay.applyUserPhone;
          _that.ruleForm.applyUserTel = qlCaseApplay.applyUserTel;
          _that.ruleForm.applyPostCode = qlCaseApplay.applyPostCode;
          _that.ruleForm.credentialNumber = qlCaseApplay.credentialNumber;
          _that.ruleForm.legalPersonName = qlCaseApplay.legalPersonName;
          _that.ruleForm.applyUserAddress = qlCaseApplay.applyUserAddress;
          _that.ruleForm.addresseeName = qlCaseApplay.addresseeName;
          _that.ruleForm.addresseePhone = qlCaseApplay.addresseePhone;
          _that.ruleForm.addresseeTel = qlCaseApplay.addresseeTel;
          _that.ruleForm.addresseePostCode = qlCaseApplay.addresseePostCode;
          _that.ruleForm.addresseeDetailAddress =
            qlCaseApplay.addresseeDetailAddress;
          _that.ruleForm.contactUserName = qlCaseApplay.contactUserName;
          _that.ruleForm.contactCredentialNumber =
            qlCaseApplay.contactCredentialNumber;
          _that.ruleForm.contactEmail = qlCaseApplay.contactEmail;
          _that.ruleForm.contactUserPhone = qlCaseApplay.contactUserPhone;
          _that.ruleForm.contactUserTel = qlCaseApplay.contactUserTel;
          _that.ruleForm.contactRemark = qlCaseApplay.contactRemark;
          _that.ruleForm.investProjecName = qlCaseExt.investProjecName;
          _that.ruleForm.investProjectCode = qlCaseExt.investProjectCode;
          _that.ruleForm.projectAbstract = qlCaseExt.projectAbstract;
          _that.ruleForm.resultDeliveryWay = qlCaseExt.resultDeliveryWay;
          _that.ruleForm.proxyFlag = qlCaseExt.proxyFlag;
          _that.changeDeliveryWay(qlCaseExt.resultDeliveryWay);
          _that.changeProxyFlag(qlCaseExt.proxyFlag);
          _that.ruleForm.credentialType = qlCaseApplay.credentialType;
          _that.getDistrictTreeNew(qlCaseApplay.bussVenueDistrictOid);
          _that.getAddressTree(qlCaseApplay.addresseeAddress);
        });
      } else {
        _that.msgError("办件查询失败！");
        return false;
      }
    },
    getAddressTree(addresseeAddress) {
      let _that = this;
      let address = [];
      if (addresseeAddress != "" && addresseeAddress != null) {
        addresseeAddress = addresseeAddress.replace('["', "").replace('"]', "");
        if (addresseeAddress != "") {
          let addressIds =
            addresseeAddress != "" ? addresseeAddress.split('","') : [];
          for (let oid of addressIds) {
            if (oid != "") {
              address.push(oid);
            }
          }
        }
      }
      _that.ruleForm.addresseeAddress = address;
    },
    /** 获取区划 */
    getDistrictTreeNew(bussVenueDistrictOids) {
      let _that = this;
      let oids = [];
      if (bussVenueDistrictOids != "") {
        let districtOids =
          bussVenueDistrictOids != "" ? bussVenueDistrictOids.split(",") : [];
        for (let oid of districtOids) {
          if (oid != "") {
            oids.push(oid);
          }
        }
        _that.ruleForm.bussVenueDistrictOid = oids;
      }
    },
    handleClose() {
      this.$refs.scanForm.getImageRes();
      this.dialogVisible = false;
    },

    //调用摄像头获取图像信息
    getImageCamera() {
      this.dialogVisible = true;
    },
    //内资公司变更法定代表人表单
    getNzgsbgFormInfo() {
      this.bgForm = true;
    },
    closebgForm() {
      this.bgForm = false;
    }
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
  width: 477px;
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
  top: 10px;
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
  top: 11px;
}

.right-btn-group-two {
  position: absolute;
  right: 10px;
  top: 10px;
}

.right-btn-group-two .el-form > .el-button {
  margin-top: 0;
}

.right-btn-group .el-button {
  // padding: 6px 8px;
  font-size: 12px;
  // margin-left: 5px;
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

.category_style {
  height: 45px; // 设置高度，覆盖组件中原option的高度
  overflow: auto; // 内容超出，显示滚动条
  line-height: 45px;
  top: -7px;
}

.btn-download {
  position: absolute;
  right: 150px;
  top: 20px;
  padding: 8px 15px;
  font-size: 12px;
}

.data-btn {
  position: absolute;
  top: 32px;
  right: 52px;
}

.el-radio {
  margin-right: 12px;
}
</style>
