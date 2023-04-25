<!-- 一次告知 -->
<template>
  <div class="onething-infoKnow-for-once" v-loading="loading">
    <el-scrollbar class="common-dialog-content">
      <div class="data-box">
        <div class="data-box--title">
          <div class="common-dialog--title">材料列表</div>
          <div class="data-box--tip">
            <div class="data-box--tip-item">
              <img
                :src="require('@/assets/image/intelligent/gzcn.png')"
                width="10px"
                alt=""
              />
              <span>告知承诺</span>
            </div>
            <div class="data-box--tip-item">
              <img
                :src="require('@/assets/image/intelligent/rqsl.png')"
                width="10px"
                alt=""
              />
              <span>容缺受理</span>
            </div>
          </div>
        </div>
        <table border="0" class="zf-zc-table--common zf-zc-table--td-center">
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
            v-if="sxServiceMaterialList.length > 0"
            style="background: #fff"
          >
            <tr
              v-for="(data, index) in sxServiceMaterialList"
              :key="index"
              empty-text="暂无我的待办"
            >
              <td>{{ index + 1 }}</td>
              <td
                style="text-align: left !important"
                :class="[
                  data.materialFormat == '4' ? 'rqbz--tip' : '',
                  data.materialFormat == '7' ? 'inform-promise--tip' : '',
                ]"
              >
                {{ data.materialName }}
              </td>
              <td>
                <template>
                  <span v-if="data.materialType == '0'">原件</span>
                  <span v-if="data.materialType == '1'">复印件</span>
                  <span v-if="data.materialType == '2'">原件和复印件</span>
                  <span v-if="data.materialType == '3'">原件或复印件</span>
                </template>
              </td>
              <td>
                <template>
                  <span v-if="data.materialFormat == '1'">纸质</span>
                  <span v-if="data.materialFormat == '2'">电子版</span>
                  <span v-if="data.materialFormat == '3'">证照</span>
                  <span v-if="data.materialFormat == '4'">容缺补正</span>
                  <span v-if="data.materialFormat == '5'">纸质、电子版</span>
                  <span v-if="data.materialFormat == '7'"> 告知承诺 </span>
                </template>
              </td>
              <td>{{ data.paperNumber }}</td>
              <td>
                <template>
                  <span class="bage-necessery" v-if="data.mustFlag == '0'">
                    必要
                  </span>
                  <span v-if="data.mustFlag == '1'">不必要</span>
                  <span v-if="data.mustFlag == '2'">容缺后补</span>
                  <span v-if="data.mustFlag == '3'">信息免交、信用后补</span>
                </template>
              </td>
            </tr>
          </tbody>
          <tbody v-else>
            <tr>
              <td colspan="6" style="text-align: center; background: #fff">
                暂无数据
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="data-box" v-if="informLabel.length > 0">
        <div class="common-dialog--title">事项基本信息</div>
        <div class="infoKnow-for-once--title">
          {{ comboDirectoryName }}告知单
        </div>
        <el-collapse v-model="active">
          <el-collapse-item
            v-for="(item, index) in informLabel"
            :key="item.id"
            :title="item.informTitle"
            :name="index"
          >
            <!-- <div class="ql-container ql-snow"> -->
            <div class="ql-editor" v-html="item.informContent || ''"></div>
            <!-- </div> -->
          </el-collapse-item>
        </el-collapse>
      </div>
      <!-- <div style="height: 5px"></div>
      <div
        v-if="!isTempComponent"
        style="
          text-align: center;
          position: absolute;
          bottom: 10px;
          width: 100%;
        "
      >
        <el-checkbox checked v-model="radio" label="1">
          已核验上述材料并确定齐全
        </el-checkbox>
      </div> -->
    </el-scrollbar>
    <div class="common-dialog-footer">
      <el-button
        style="background: #3d5fb5; color: #fff"
        @click="openNoticeForm"
      >
        打印告知单
      </el-button>
      <el-button type="primary" @click="$emit('lastStep', 1)">上一步</el-button>
      <el-button
        v-if="comboDireForm.showRzhs == '1'"
        style="background: #3d5fb5; color: #fff"
        @click="beforeGetImageCamera"
      >
        人证对比
      </el-button>
      <el-button type="primary" @click="nextStep">下一步并确认齐全</el-button>
      <div v-if="dialogVisible">
        <el-dialog
          v-dialog-drag
          :visible.sync="dialogVisible"
          width="1158px"
          height="800px"
          scrollbar
          :before-close="handleClose"
          :close-on-click-modal="false"
          :close-on-press-escape="false"
          append-to-body
          custom-class="common-dialog once-in-form"
          title="一次性告知单"
        >
          <OnceInform
            ref="onceNoticeForm"
            :comboDirectoryOid="comboDirectoryOid"
            :comboDirectoryName="comboDirectoryName"
            :informLabel="informLabel"
          />
          <div style="text-align: center" slot="footer">
            <el-button type="primary" id="trueBtn" v-print="printObj">
              打印
            </el-button>
            <el-button @click="handleClose">关闭</el-button>
          </div>
        </el-dialog>
      </div>
    </div>

    <!-- 人证比对 -->
   <div v-if="hardWareVisible">
      <el-dialog
        v-dialog-drag
        :visible.sync="hardWareVisible"
        width="1158px"
        top="10vh"
        :close-on-click-modal="false"
        :close-on-press-escape="false"
        append-to-body
        @close="closeHard"
        custom-class="hardware-scan"
        title="人证比对"
      >
        <HardWareScan
          ref="scanForm"
          :caseOid="caseOid"
          :loginUser="loginUser"
          @close="closeHard"
        />
      </el-dialog>
    </div>
  </div>
</template>

<script>
//import { showCallMessage } from "@/api/zc/businessManagement/doubleScreenInteraction";
import OnceInform from "./onceInform";
import {
  queryMaterialListByDireOidAndValOids,
  getAllInformLableByVals,
  saveCaseSituation
} from '@/api/onething/comboManager/comboAccept/situation';
import {
  industrysaveCaseSituation
} from '@/api/onelicence/industryManager/industryCaseAccept/situation';

import { saveOrupdateCase } from '@/api/onelicence/industryManager/industryCaseAccept/initIndustryCase';
import { saveComboCase, getComboDirectoryByDirectoryOid } from "@/api/onething/comboManager/comboAccept/initComboCase";
// 人证比对
import HardWare from '@/mixins/hardware';
export default {
  name: "InfoKnowForOnce",
  inheritAttrs: false,
  mixins: [HardWare],
  components: { OnceInform },
  props: {
    loginUser: {
      type: Object,
      default: () => ({})
    },
    comboDirectoryOid: {
      type: String,
      default: ""
    },
    situationCheckList: {
      type: Array,
      default: () => []
    },
    isQlCaseChanged: Boolean,
    situation: {
      type: Object,
      default: () => ({})
    },
    comboDirectoryName: String,
    isTempComponent: Boolean,
    caseOid: String,
    caseId: String
  },
  data () {
    return {
      // radio: "1",
      firstEnter: true,
      dialogVisible: false,
      sxServiceMaterialList: [],
      loading: true,
      informLabel: [], // 告知单
      active: [0],
      printObj: {
        id: 'print',
        popTitle: '《' + this.comboDirectoryName + '》' + '一次性告知单',
        extraHead: '<meta http-equiv="Content-Language"content="zh-cn"/>'
      },
      comboDireForm: {},
      sxService: {},
    };
  },
  created () {
    if (this.isTempComponent) {
      this.getMaterialInfo();
      this.getAllInformLableByVals();
    }
  },
  activated () {
    if (this.isQlCaseChanged && !this.isTempComponent) {
      this.getMaterialInfo();
      this.getAllInformLableByVals();
    }
  },
  methods: {
    openNoticeForm () {
      this.dialogVisible = true;
    },
    handleClose () {
      this.dialogVisible = false;
    },

    /** 查询材料信息 */
    getMaterialInfo () {
      this.loading = true;
      queryMaterialListByDireOidAndValOids({ direOid: this.comboDirectoryOid, valOidS: this.situation?.valOids })
        .then(({ code, data }) => {
          this.loading = false;
          if (code !== 200) return;
          this.sxServiceMaterialList = data || [];
          this.$emit('setSxServiceMaterialList', this.sxServiceMaterialList);

          this.getServiceDetail();
        })
        .catch(() => {
          this.loading = false;
        })
    },

    /** 获取标签信息 */
    getAllInformLableByVals () {
      getAllInformLableByVals({ comboDirectoryOid: this.comboDirectoryOid, selectValOids: this.situation?.valOids })
        .then(({ code, data }) => {
          if (code !== 200) return;
          this.informLabel = (data || []).map((item) => {
            return {
              ...item,
              informContent: item.informContent?.replace?.(/&lt;/g, '<')?.replace(/&gt;/g, '>')
            }
          });
        })
    },

    /** 获取事项详细信息 */
    getServiceDetail () {
      getComboDirectoryByDirectoryOid(this.comboDirectoryOid).then(({ code, data, message }) => {
        if (code !== 200)
          return this.$message.warning(message || "接口请求失败");
        this.sxService = data;
        this.comboDireForm = {
          ...this.comboDireForm,
          serviceName: data.comboDirectoryName,
          organName: data.mainOrganName,
          zxDhText: data.zixunType,
          tsDhText: data.jianduType,
          promiseLimit: data.promiseLimit,
          legalLimit: data.legalLimit,
          showRzhs: data.showRzhs,
          // sxServiceExtend: data?.sxServiceExtend ?? {},
          // sxAcceptConditions: data.sxAcceptConditions,
          // sxServiceLinks: data.sxServiceLinks,
          // sxServiceQuestions: data.sxServiceQuestions
        };
        this.$emit("setServiceFormData", {
          serviceName: this.comboDireForm.serviceName,
          zxDhText: this.comboDireForm.zxDhText,
          showRzhs: this.comboDireForm.showRzhs,
          organName: this.comboDireForm.organName,
        });
       // this.showGuidence();
        this.firstEnter = false;
      });
    },

    /*//超级综窗柜台双屏显示办事指南信息
    async showGuidence () {
      let url = window.location.origin;
      let pushUrl = url + "/serviceHall/guidence.html";
      let data = {};
      let sxService = {};
      let materialList = [];
      if (this.sxServiceMaterialList.length > 0) {
        this.sxServiceMaterialList.forEach(material => {
          let ma = {};
          ma.materialName = material.materialName;
          ma.paperNumber = material.paperNumber;
          materialList.push(ma);
        });
      }
      data.userName = this.loginUser.userName;
      data.organName = this.loginUser.organName;
      data.sxServiceMaterialList = materialList;
      sxService.serviceName = this.sxService.comboDirectoryName;
      sxService.chargeFlagName = this.sxService.ifCharge == 1 ? "是" : "否";
      sxService.chargeFlag = this.sxService.ifCharge;
      sxService.zxDhText = this.sxService.zixunType;
      sxService.tsDhText = this.sxService.jianduType;
      sxService.promiseLimit = this.comboDireForm.promiseLimit;
      sxService.legalLimit = this.comboDireForm.legalLimit;
      if (this.sxService.handleForm == 0) {
        sxService.handleFormName = "窗口办理";
      } else if (this.sxService.handleForm == 1) {
        sxService.handleFormName = "网上办理";
      } else if (this.sxService.handleForm == 2) {
        sxService.handleFormName = "一体化办理";
      }

      data.sxService = sxService;
      pushUrl = pushUrl + "?jsonObject=" + JSON.stringify(data);
      await showCallMessage(pushUrl).then(response => {
        console.log(response);
      });
    },*/

    nextStep () {
      // 暂存受理直接过
      if (this?.comboDireForm?.showRzhs == '1' && !this.isTempComponent) {
        if (!this.rzbdResult) return this.$message.warning('请先完成人证比对!');
        if (this.rzbdResult == '2') return this.$message.warning('人证比对不通过, 请重新比对');
      }

      if (this.isTempComponent) {
        this.$emit("nextStep", 3);
      } else {
        this.getCheckbox();
      }
    },

    /** 核验材料勾选 */
    getCheckbox () {
      if (this.isQlCaseChanged) {
        this.initCase();
      } else {
        this.$emit("nextStep", 3);
      }
    },

    /** 办件信息下一步保存 */
    initCase (isRzhy = false) {
      this.loading = true;
      return industrysaveCaseSituation({
        caseOid: this.caseOid,
        caseSituationList: this.situation.situationOids?.split(','),
        caseOptionValList: this.situation.valOids?.split(','),
        caseSituTextList: this.situation.textVals,
      })
        .then(response => {
          if (response.code === 200 && response.data) {
            return this.saveComboCase(response.data.caseOid, isRzhy);
          } else {
            this.$message.warning("保存情形选项信息失败！");
            this.loading = false;
          }
        })
        .catch(() => {
          this.loading = false;
        });
    },

    /** 保存材料信息 */
    saveComboCase (caseOid, isRzhy) {
      return saveOrupdateCase({
        id: this.caseId,
        caseOid: caseOid,
        comboDireOid: this.comboDirectoryOid,
        comboDireMaterials: this.sxServiceMaterialList,
      })
        .then(({ code, data }) => {
          this.loading = false;
          if (code === 200 && data) {
            this.$emit("setData", {
              caseOid: data.caseOid,
              caseNumber: data.caseNumber,
              caseId: data.id,
            });

            if (!this.isTempComponent) {
              this.$store.commit("SET_CASE_OID", data.caseOid);
            }

            if (isRzhy) {
              return;
            }

            this.$emit("nextStep", 3);
          } else {
            this.$message.warning("保存办件失败！");
            this.loading = false;
            return false;
          }
        })
        .catch(() => {
          this.loading = false;
        });
    },

    beforeGetImageCamera () {
     if (this.caseOid) {
        return this.getImageCamera();
      }

      this.initCase(true).then(() => {
        this.$nextTick(() => {
          this.getImageCamera();
        })
      })

    }
  }
};
</script>

<style scoped lang="scss">
.onething-infoKnow-for-once {
  .data-box {
    .data-box--title {
      display: flex;
      align-items: center;
      justify-content: space-between;

      .data-box--tip {
        display: flex;
        align-items: center;
        color: #cbcccf;

        .data-box--tip-item:nth-child(1) {
          margin-right: 20px;
        }
      }
    }
  }

  .rqbz--tip {
    position: relative;

    &::before {
      content: '';
      position: absolute;
      right: 0px;
      top: 0px;
      background: url(~@/assets/image/intelligent/rqsl.png) center no-repeat;
      width: 10px;
      height: 10px;
      background-size: 100%;
    }
  }

  .inform-promise--tip {
    position: relative;

    &::before {
      content: '';
      position: absolute;
      right: 0px;
      top: 0px;
      background: url(~@/assets/image/intelligent/gzcn.png) center no-repeat;
      width: 10px;
      height: 10px;
      background-size: 100%;
    }
  }

  .bage-necessery {
    width: 75px;
    height: 28px;
    background: #fbe1cb;
    border-radius: 14px;
    display: inline-block;
    line-height: 28px;
    color: rgba(255, 60, 0, 0.8);
  }

  .infoKnow-for-once--title {
    font-size: 27px;
    margin-top: 15px;
    margin-bottom: 15px;
    text-align: center;
    font-weight: bold;
    font-family: '仿宋';
  }

  ::v-deep .el-collapse {
    border: unset;

    .el-collapse-item {
      margin-bottom: 15px;

      &.is-active {
        margin-bottom: unset;
      }

      .el-collapse-item__wrap {
        border: unset;
        padding: 0px 15px;

        .el-collapse-item__content {
          font-size: 16px;
          font-family: '仿宋';
          font-weight: 400;
          color: #2a344c;
          padding-bottom: unset;
          // text-indent: 1em;
        }
      }

      .el-collapse-item__header {
        background: #f5f5f5;
        border-radius: 5px;
        padding-left: 30px;
        border: unset;
        font-size: 16px;
        font-family: '仿宋';
        font-weight: bold;
        color: #2a344c;

        i {
          color: #237ee9;
        }
      }
    }
  }
}

::v-deep .common-dialog.once-in-form {
  .el-dialog__body,
  .el-dialog__footer {
    background-color: #fff;
  }
}
</style>
