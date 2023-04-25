<!-- 智能填表 -->
<template>
  <div class="onething-intelligent-form-filling">
    <el-scrollbar class="common-dialog-content">
      <el-tabs v-model="activeName" @tab-click="handleTabClick">
        <el-tab-pane label="办件申请基础信息" name="1">
          <BaseForm
            :pCegisterType="pCegisterType"
            ref="ruleForm"
            :isTempComponent="isTempComponent"
            :caseOid="caseOid"
            :rqbzDueDate="rqbzDueDate"
            :comboDireOid="comboDirectoryOid"
            :lastComponentId="$attrs.lastComponentId"
            @setFormField="handleFormField"
          />
        </el-tab-pane>
        <el-tab-pane v-for="item in reportFormList" :key="item.id" :label="item.formName" :name="item.name">
          <div class="el-table__header-wrapper">
            <FormReport v-if="item.reportForm.designOid" :ref="`reportForm_${item.designOid}`" :key="item.designOid"
                        :reportForm="item.reportForm" :authorizeKey="item.authorizeKey" :formNames="item.formNames"
                        :isShowDefaultVal="!isTempComponent" :isZc="isZc" :isOneLevelBindFormData="true"
                        @sendRes="item.sendRes" />
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-scrollbar>
    <div class="common-dialog-footer">
      <el-button type="primary" @click="$emit('lastStep', 3)">上一步</el-button>
      <el-button style="background: rgb(207 147 12); color: #fff" @click="saveApplyCaseForm(true)">
        暂存
      </el-button>
      <el-button type="primary" @click="nextStep">下一步</el-button>
    </div>

    <ZfLoading v-if="zfloading" :text="zftext" />
  </div>
</template>

<script>
import IntelligentFormFilling from "@/mixins/onelicence/intelligentFormFilling";
import { queryFormInfoList } from "@/api/onething/comboForm/formManage.js";

import {
  getCaseSituationResultByCaseOid,
  queryMaterialListByDireOidAndValOids
} from "@/api/onething/comboManager/comboAccept/situation";
 import {
  industrygetCaseSituationResultByCaseOid
} from '@/api/onelicence/industryManager/industryCaseAccept/situation';
import { handleArr2String } from "./util";

import { getComboDirectoryByDirectoryOid } from "@/api/onething/comboManager/comboAccept/initComboCase";

import {getIndustryCaseMaterialList} from '@/api/onelicence/industryManager/industryCaseAccept/materialClassifiler'

// 表单组件
import BaseForm from "./form/base-form.vue";
export default {
  mixins: [IntelligentFormFilling],
  components: { BaseForm },
  props: ["serviceForm", "situation", "isQlCaseChanged"],
  data () {
    return {
      storageSituation: {}
    };
  },
  created () {
    if (this.isTempComponent) {
      this.getCaseSituationResultByCaseOid();
      this.getServiceDetail();
      this.getAttaList();
    }
  },
  activated () {
    if (!this.isTempComponent && !this.isQlCaseChanged) {
      this.reportFormList = [];
      this.selectBySxSerFormByServiceOid();
    }
  },
  methods: {
    // 初始获取表单信息
    async selectBySxSerFormByServiceOid () {
      this.getFormState = true;
      try {
        let _situation = {};
        if (JSON.stringify(this.situation) == "{}") {
          _situation = this.storageSituation;
        } else {
          _situation = this.situation;
        }
        const { data, code, message } = await queryFormInfoList(
          this.caseOid,
          this.comboDirectoryOid,
          _situation.valOids
        );

        if (code !== 200) {
          this.getFormState = false;
          return message && this.$message.warning(message)
        };
        this.reportFormList =
          data?.map?.((item, idx) => {
            return {
              ...item,
              childFormName: (item?.childFormName?.split(",") ?? []).filter(
                item => item
              ),
              name: String(idx + 2),
              elecFormTimer: null,
              reportForm: {
                designOid: item.designOid,
                authorizeKey: item.authorizeKey,
                reportOid: this.caseOid,
                formData: this.isTempComponent ? JSON.stringify({}) : JSON.stringify(item.formVals)
              },
              // 保存表单后返回的接口数据
              sendRes: ret => {
                return JSON.parse(ret);
              }
            };
          }) ?? [];
        this.getFormState = false;
      } catch (error) {
        this.getFormState = false;
      }
    },

    /** -------------------------------------暂存受理提前请求接口-------------------------------------- */
    async getCaseSituationResultByCaseOid () {
      await industrygetCaseSituationResultByCaseOid({ caseOid: this.caseOid }).then(
        ({ code, data }) => {
          if (code !== 200) return this.$message.warning("查询情形信息失败");
          // 过滤掉标题不显示的
          const comboSituationList = (data || []).map(item => {
            return {
              ...item,
              situationName: item.pacSituationName,
              situationValueName: item.caseSituatResultList
                ?.map?.(i => i.optionName)
                ?.join(";")
            };
          });

          this.$emit("setSituationCheckList", comboSituationList);

          // 获取文本内容, 情形oids, 选项值, 是否必选集合
          const situation = comboSituationList.reduce(
            (prev, cur) => {
              prev.selectValOids.push(
                cur.caseSituatResultList?.map?.(i => i.pacOptionOid)
              );
              prev.situationArr.push(cur.pacSituationOid);
              return prev;
            },
            { situationArr: [], selectValOids: [] }
          );
          // 转字符串
          const situationArr = handleArr2String(situation.situationArr);
          const selectValOids = handleArr2String(situation.selectValOids);

          // 缓存情形信息
          this.$emit("setSituation", {
            situationOids: situationArr,
            valOids: selectValOids
          });
          this.storageSituation.situationOids = situationArr;
          this.storageSituation.valOids = selectValOids;
          this.getMaterialInfo(selectValOids);
          this.selectBySxSerFormByServiceOid();
        }
      );
    },

    /** 查询材料信息 */
    getMaterialInfo (valOidS) {
      queryMaterialListByDireOidAndValOids({
        direOid: this.comboDirectoryOid,
        valOidS
      }).then(({ code, data }) => {
        if (code !== 200) return;
        this.$emit("setSxServiceMaterialList", data || []);
      });
    },

    /** 获取事项详细信息 */
    getServiceDetail () {
      getComboDirectoryByDirectoryOid(this.comboDirectoryOid).then(
        ({ code, data, message }) => {
          if (code !== 200)
            return this.$message.warning(message || "接口请求失败");
          const comboDireForm = {
            serviceName: data.comboDirectoryName,
            zxDhText: data.zixunType,
            tsDhText: data.jianduType,
            promiseLimit: data.promiseLimit,
            legalLimit: data.legalLimit,
            showRzhs: data.showRzhs
          };
          this.$emit("setServiceFormData", {
            serviceName: comboDireForm.serviceName,
            zxDhText: comboDireForm.zxDhText,
            showRzhs: comboDireForm.showRzhs
          });
        }
      );
    },

    //材料附件
    getAttaList () {
      getIndustryCaseMaterialList({ caseOid: this.caseOid }).then(({ code, data }) => {
        if (code !== 200 || !data)
          return this.$message.warning("查询材料附件失败");
        // 缓存所有的caseMaterialOid
        const caseMaterialOids = data.reduce((prev, cur) => {
          if (prev[cur.collectionType]) {
            prev[cur.collectionType] = `${prev[cur.collectionType]};${cur.caseMaterialOid
            }`;
          } else {
            prev[cur.collectionType] = cur.caseMaterialOid;
          }
          return prev;
        }, {});
        this.$emit("caseMaterialOids", caseMaterialOids);

        // 获取容缺补正材料名称
        const rqbzMaterialNames = data
          .filter(item => item.collectionType == "4")
          .map(item => item.materialName)
          .join(",");

        this.$emit("setRqbzMaterialNames", rqbzMaterialNames);

        const isExitRqbzTime =
          data.filter(item => item.collectionType === "4").length > 0;

        this.$emit("setIsRqbzFlag", isExitRqbzTime);
      });
    }
  }
};
</script>

<style scoped lang="scss">
@import './style/elec-form.scss';
.onething-intelligent-form-filling {
  .custom-table td {
    padding: 0 10px !important;
  }

  >>>.el-dialog.hardware-scan {
    height: 714px;

    .el-dialog__body {
      height: calc(100% - 56px);
      padding: 26px 24px 0px 24px;
    }
  }

  >>>.el-form-item {
    padding: 20px 0;
  }

  >>>.el-select {
    width: 100%;
  }

  // 自定义表单
  >>>.el-table {
    &__header {
      thead {
        tr th {
          padding: 0;
          background: #edf0f5;
          color: #121b2f;
          font-size: 14px;
          font-weight: 700;

          &:not(:last-child) {
            border-right: 1px solid #e0e6f0;
          }
        }
      }
    }

    &__row {
      td {
        background-color: #fff !important;
        text-align: center !important;

        .el-form-item {
          padding: unset;
        }

        &:first-child {
          border-left: unset;
        }
      }
    }
  }
}
</style>
