<template>
  <el-form
    :model="ruleForm"
    :rules="rules"
    ref="ruleForm"
    label-width="0px"
    class="base-rule-form"
    label-position="top"
  >
    <div class="step-third-box">
      <div class="common-dialog--title">
        申请人/申请单位信息
        <el-button
          type="primary"
          @click="handleScanCard('0')"
          style="float: right"
          v-if="cegisterType == 0"
          >✚ 扫描身份证
        </el-button>
      </div>
      <table
        cellspacing="0"
        cellpadding="0"
        border="0"
        class="common-dalog-table"
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
            <div class="button-in-form">
              <el-form-item prop="applyUserName" style="width: 70%">
                <el-input
                  v-model.trim="ruleForm.applyUserName"
                  name="applyUserName"
                  maxlength="25"
                  show-word-limit
                />
              </el-form-item>
              <el-button
                type="primary"
                @click="getUserInfo(ruleForm.credentialNumber)"
              >
                提取申请人信息
              </el-button>
            </div>
          </td>
        </tr>
        <tr>
          <td><i class="require">*</i><b>证件类型：</b></td>
          <td>
            <el-form-item prop="credentialType">
              <el-select
                v-model.trim="ruleForm.credentialType"
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
                />
              </el-select>
            </el-form-item>
          </td>
          <td><i class="require">*</i><b>证件号码：</b></td>
          <td>
            <el-form-item prop="credentialNumber">
              <el-input
                v-model.trim="ruleForm.credentialNumber"
                name="credentialNumber"
                maxlength="25"
                show-word-limit
                @blur="
                  () => {
                    handleBlur()
                  }
                "
              />
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
              <el-input
                v-model.trim="ruleForm.applyUserPhone"
                name="applyUserPhone"
                maxlength="11"
                show-word-limit
              />
            </el-form-item>
          </td>
          <td>
            <span v-if="cegisterType == 0">
              <b>申请人电话：</b>
            </span>
            <span v-else>
              <b>申请单位电话：</b>
            </span>
          </td>
          <td>
            <el-form-item prop="applyUserTel">
              <el-input
                v-model.trim="ruleForm.applyUserTel"
                name="applyUserTel"
                maxlength="15"
                show-word-limit
              />
            </el-form-item>
          </td>
        </tr>
        <tr v-if="cegisterType == 1">
          <td><b>通讯地址：</b></td>
          <td colspan="3">
            <el-form-item prop="applyUserAddress">
              <el-input
                type="textarea"
                v-model.trim="ruleForm.applyUserAddress"
                name="applyUserAddress"
                maxlength="150"
                show-word-limit
              />
            </el-form-item>
          </td>
        </tr>
        <tr v-else>
          <td><i class="require">*</i><b>法定代表人：</b></td>
          <td colspan="3">
            <el-form-item prop="legalPersonName">
              <el-input
                v-model.trim="ruleForm.legalPersonName"
                name="legalPersonName"
                maxlength="25"
                show-word-limit
              />
            </el-form-item>
          </td>
        </tr>
      </table>
    </div>
    <div class="step-third-box">
      <div class="common-dialog--title">办件信息</div>
      <table
        cellspacing="0"
        cellpadding="0"
        border="0"
        class="common-dalog-table"
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
              <el-input
                v-model.trim="ruleForm.projectName"
                name="projectName"
                maxlength="200"
                show-word-limit
              />
            </el-form-item>
          </td>
        </tr>
        <tr>
          <td><i class="require">*</i><b>申请数量：</b></td>
          <td>
            <el-form-item prop="applyNumber">
              <el-input-number :min="1" :max="999"
                v-model.trim="ruleForm.applyNumber"
                name="applyNumber"
                maxlength="3"
                show-word-limit
              />
            </el-form-item>
          </td>
          <td><i class="require">*</i><b>邮政编码：</b></td>
          <td>
            <el-form-item prop="applyPostCode">
              <el-input
                v-model.trim="ruleForm.applyPostCode"
                name="applyPostCode"
                maxlength="6"
                show-word-limit
              />
            </el-form-item>
          </td>
        </tr>
        <tr>
          <td><i class="require">*</i><b>业务管辖地：</b></td>
          <td>
            <el-form-item prop="bussVenueDistrictOid">
              <treeselect
                @input="changeValue"
                :multiple="true"
                :options="districtOptions"
                :flat="true"
                :default-expand-level="1"
                placeholder="请选择业务管辖地"
                v-model.trim="ruleForm.bussVenueDistrictOid"
              />
            </el-form-item>
          </td>
          <td><i class="require">*</i><b>受理具体地点：</b></td>
          <td>
            <el-form-item prop="specificLocation">
              <el-input
                v-model.trim="ruleForm.specificLocation"
                name="specificLocation"
                maxlength="150"
                show-word-limit
              />
            </el-form-item>
          </td>
        </tr>
        <tr>
          <td><b>投资项目名称：</b></td>
          <td>
            <el-form-item prop="investProjecName">
              <el-input
                v-model.trim="ruleForm.investProjecName"
                name="investProjecName"
                maxlength="150"
                show-word-limit
              />
            </el-form-item>
          </td>
          <td><b>投资项目编号：</b></td>
          <td>
            <el-form-item prop="investProjectCode">
              <el-input
                v-model.trim="ruleForm.investProjectCode"
                name="investProjectCode"
                maxlength="100"
                show-word-limit
              />
            </el-form-item>
          </td>
        </tr>
        <tr>
          <td><b>摘要内容：</b></td>
          <td colspan="3">
            <el-form-item prop="projectAbstract">
              <el-input
                type="textarea"
                v-model.trim="ruleForm.projectAbstract"
                name="projectAbstract"
                maxlength="500"
                show-word-limit
              />
            </el-form-item>
          </td>
        </tr>
        <tr>
          <td><i class="require">*</i><b>是否为代理人：</b></td>
          <td colspan="3">
            <el-form-item prop="proxyFlag">
              <el-radio-group
                v-model.trim="ruleForm.proxyFlag"
                @change="changeProxyFlag"
              >
                <el-radio label="1">是</el-radio>
                <el-radio label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </td>
        </tr>
      </table>
    </div>
    <!-- 代理人信息 -->
    <div class="step-third-box" v-if="isProxyPerson">
      <div class="common-dialog--title">
        联系人/代理人相关信息
        <el-button
          type="primary"
          @click="handleScanCard('1')"
          style="float: right"
          >✚ 扫描身份证</el-button
        >
      </div>
      <table
        cellspacing="0"
        cellpadding="0"
        border="0"
        class="common-dalog-table"
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
              <el-input
                v-model.trim="ruleForm.contactUserName"
                name="contactUserName"
                maxlength="25"
                show-word-limit
              />
            </el-form-item>
          </td>
        </tr>
        <tr>
          <td><i class="require">*</i><b>联系人/代理人身份证号码：</b></td>
          <td>
            <el-form-item prop="contactCredentialNumber">
              <el-input
                v-model.trim="ruleForm.contactCredentialNumber"
                name="contactCredentialNumber"
                maxlength="18"
                show-word-limit
              />
            </el-form-item>
          </td>
          <td><b>电子邮箱：</b></td>
          <td>
            <el-form-item prop="contactEmail">
              <el-input
                v-model.trim="ruleForm.contactEmail"
                name="contactEmail"
                maxlength="50"
                show-word-limit
              />
            </el-form-item>
          </td>
        </tr>
        <tr>
          <td><i class="require">*</i><b>手机号：</b></td>
          <td>
            <el-form-item prop="contactUserPhone">
              <el-input
                v-model.trim="ruleForm.contactUserPhone"
                name="contactUserPhone"
                maxlength="11"
                show-word-limit
              />
            </el-form-item>
          </td>
          <td><b>固定电话：</b></td>
          <td>
            <el-form-item prop="contactUserTel">
              <el-input
                v-model.trim="ruleForm.contactUserTel"
                name="contactUserTel"
                maxlength="20"
                show-word-limit
              />
            </el-form-item>
          </td>
        </tr>
        <tr>
          <td><b>备注：</b></td>
          <td colspan="3">
            <el-form-item prop="contactRemark">
              <el-input
                type="textarea"
                v-model.trim="ruleForm.contactRemark"
                name="contactRemark"
                maxlength="500"
                show-word-limit
              />
            </el-form-item>
          </td>
        </tr>
      </table>
    </div>
    <!-- 送达信息 -->
    <div class="step-third-box">
      <div class="common-dialog--title">送达信息</div>
      <table
        cellspacing="0"
        cellpadding="0"
        border="0"
        class="common-dalog-table"
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
            <el-form-item prop="resultDeliveryWay">
              <el-radio-group
                v-model.trim="ruleForm.resultDeliveryWay"
                @change="changeDeliveryWay"
              >
                <el-radio label="1">快递送达</el-radio>
                <el-radio label="2">自行取件</el-radio>
                <el-radio label="3">其他</el-radio>
              </el-radio-group>
            </el-form-item>
          </td>
        </tr>
        <tr v-if="isShowAddress">
          <td><i class="require">*</i><b>收件人名称：</b></td>
          <td>
            <div class="button-in-form">
              <el-form-item prop="addresseeName" style="width: 75%">
                <el-input
                  v-model.trim="ruleForm.addresseeName"
                  name="addresseeName"
                  maxlength="25"
                  show-word-limit
                />
              </el-form-item>
              <el-button type="primary" @click="getDeliveryInfo">
                提取
              </el-button>
            </div>
          </td>
          <td><i class="require">*</i><b>收件人邮政编码：</b></td>
          <td>
            <el-form-item prop="addresseePostCode">
              <el-input
                v-model.trim="ruleForm.addresseePostCode"
                name="addresseePostCode"
                maxlength="6"
                show-word-limit
              />
            </el-form-item>
          </td>
        </tr>
        <tr v-if="isShowAddress">
          <td><i class="require">*</i><b>收件人手机：</b></td>
          <td>
            <el-form-item prop="addresseePhone">
              <el-input
                v-model.trim="ruleForm.addresseePhone"
                name="addresseePhone"
                maxlength="11"
                show-word-limit
              />
            </el-form-item>
          </td>
          <td><b>收件人电话：</b></td>
          <td>
            <el-form-item prop="addresseeTel">
              <el-input
                v-model.trim="ruleForm.addresseeTel"
                name="addresseeTel"
                maxlength="25"
                show-word-limit
              />
            </el-form-item>
          </td>
        </tr>
        <tr v-if="isShowAddress">
          <td><i class="require">*</i><b>收件人地址：</b></td>
          <td>
            <el-form-item prop="addresseeAddress">
              <el-cascader
                style="width: 100%"
                size="large"
                :options="provinceCityOptions"
                v-model.trim="ruleForm.addresseeAddress"
                @change="handleChange"
              />
            </el-form-item>
          </td>
          <td><i class="require">*</i><b>收件人详细地址：</b></td>
          <td>
            <el-form-item prop="addresseeDetailAddress">
              <el-input
                v-model.trim="ruleForm.addresseeDetailAddress"
                name="addresseeDetailAddress"
                maxlength="100"
                show-word-limit
              />
            </el-form-item>
          </td>
        </tr>
      </table>
    </div>

    <!-- 获取申请人信息-->
    <template v-if="userInfoShow">
      <el-dialog
        v-dialog-drag
        :visible.sync="userInfoShow"
        title="历史申请人信息快速提取"
        width="1000px"
        height="600px"
        scrollbar
        append-to-body
        :close-on-press-escape="false"
        :close-on-click-modal="false"
        custom-class="apply-user-list"
      >
        <apply-user-list
          :applyCardNum="ruleForm.credentialNumber"
          :cegisterType="cegisterType"
          @selectUserOk="closeUserInfoShow"
        />
      </el-dialog>
    </template>
  </el-form>
</template>

<script>
import BaseFormJs from "@/mixins/baseForm.js";
import { queryAllCaseByOid } from "@/api/zc/businessManagement/temporaryAcceptance";
export default {
  mixins: [BaseFormJs],
  props:{
rqbzDueDate:String,
    fillUserInfo:{
      type:Object,
      default:()=>({})
    },
  },
  created () {
    this.getDistrictTree();

    this.getregUserInfo();
  },
  activated(){
      this.getSelectCertificateType();
    if(JSON.stringify(this.fillUserInfo)!="{}"){
      this.ruleForm.credentialType = this.fillUserInfo.credentialType
      this.ruleForm.applyUserName = this.fillUserInfo.applyUserName
      this.ruleForm.credentialNumber = this.fillUserInfo.credentialNumber
    }
  },
  methods: {
    //查询保存的办件数据 并回填数据
    queryQlCaseInfo (userCase) {
      if (!!userCase) {
        queryAllCaseByOid(userCase).then(response => {
          const qlCase = response.data.qlCase;
          const qlCaseApplay = response.data.qlCaseApplay;
          const qlCaseExt = response.data.qlCaseExt;
          this.ruleForm = {
            ...this.ruleForm,
            rqbzDueDate: this.rqbzDueDate ? this.rqbzDueDate : "",
            projectName: qlCase.projectName,
            specificLocation: qlCaseApplay.specificLocation,
            applyNumber: qlCaseApplay.applyNumber,
            applyUserName: qlCaseApplay.applyUserName,
            applyUserPhone: qlCaseApplay.applyUserPhone,
            applyUserTel: qlCaseApplay.applyUserTel,
            applyPostCode: qlCaseApplay.applyPostCode,
            credentialNumber: qlCaseApplay.credentialNumber,
            legalPersonName: qlCaseApplay.legalPersonName,
            applyUserAddress: qlCaseApplay.applyUserAddress,
            addresseeName: qlCaseApplay.addresseeName,
            addresseePhone: qlCaseApplay.addresseePhone,
            addresseeTel: qlCaseApplay.addresseeTel,
            addresseePostCode: qlCaseApplay.addresseePostCode,
            addresseeDetailAddress: qlCaseApplay.addresseeDetailAddress,
            contactUserName: qlCaseApplay.contactUserName,
            contactCredentialNumber: qlCaseApplay.contactCredentialNumber,
            contactEmail: qlCaseApplay.contactEmail,
            contactUserPhone: qlCaseApplay.contactUserPhone,
            contactUserTel: qlCaseApplay.contactUserTel,
            contactRemark: qlCaseApplay.contactRemark,
            investProjecName: qlCaseExt.investProjecName,
            investProjectCode: qlCaseExt.investProjectCode,
            projectAbstract: qlCaseExt.projectAbstract,
            resultDeliveryWay: qlCaseExt.resultDeliveryWay,
            proxyFlag: qlCaseExt.proxyFlag
            // bussVenueDistrictOid: qlCaseApplay.bussVenueDistrictOid.split(',').filter(item => item)
          };
          this.changeDeliveryWay(qlCaseExt.resultDeliveryWay);
          this.changeProxyFlag(qlCaseExt.proxyFlag);
          this.ruleForm.credentialType = qlCaseApplay.credentialType;
          this.getAddressTree(qlCaseApplay.addresseeAddress);
          this.changeType(this.ruleForm.credentialType, true, false);
          this.$nextTick(() => {
            this.$refs.ruleForm.validate();
          })
        });
      } else {
        this.msgError("办件查询失败！");
        return false;
      }
    }
  }
};
</script>

<style scoped lang="scss">
.base-rule-form {
  .button-in-form {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  & ::v-deep .el-autocomplete-suggestion {
    width: auto !important;
  }

  >>> .el-form-item {
    margin-bottom: unset;
  }
}
</style>
