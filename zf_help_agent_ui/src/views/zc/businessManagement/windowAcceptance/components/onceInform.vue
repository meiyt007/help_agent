/** * 一次性告知单页面 * @author: wangwg * @date: 2020-11-21 */
<template>
  <div class="once-in-form">
    <div id="print" ref="print">
      <h3
        class="title-detail"
        style="
          display: block;
          font-size: 0.9em;
          margin-block-start: 1em;
          margin-block-end: 1em;
          margin-inline-start: 0px;
          margin-inline-end: 0px;
          font-weight: bold;
          text-align: center;
        "
      >
        《{{ ruleForm.serviceName }}》一次性告知单
      </h3>
      <table cellspacing="0" cellpadding="0" class="zf-zc-table">
        <colgroup>
          <col width="15%" />
          <col width="35%" />
          <col width="15%" />
          <col width="35%" />
        </colgroup>
        <tr>
          <td>
            <b>事项名称：</b>
          </td>
          <td>
            {{ ruleForm.serviceName }}
          </td>
          <td>
            <b>事项类型：</b>
          </td>
          <td>
            {{ ruleForm.serviceTypeName }}
          </td>
        </tr>
        <tr>
          <td>
            <b>实施机构：</b>
          </td>
          <td>
            {{ ruleForm.organName }}
          </td>
          <td>
            <b>咨询电话：</b>
          </td>
          <td>
            {{ ruleForm.zxDhText }}
          </td>
          <!--          <td>
            <b>办理时间：</b>
          </td>
          <td>
            <div
              v-for="(data, index) in ruleForm.sxServiceLocations"
              :label="data.acceptDate"
              :key="index"
            >
              {{ index + 1 }}、{{ data.acceptDate }}
            </div>
          </td>-->
        </tr>
        <!--        <tr>
          <td>
            <b>办理地点：</b>
          </td>
          <td colspan="3">
            <div
              v-for="(data, index) in ruleForm.sxServiceLocations"
              :label="data.locationName"
              :key="index"
            >
              {{ index + 1 }}、{{ data.locationName }}
            </div>
          </td>
        </tr>-->
        <tr>
          <td>
            <b>监督电话：</b>
          </td>
          <td>
            {{ ruleForm.tsDhText }}
          </td>
          <td>
            <b>承诺时限：</b>
          </td>
          <td>
            <template>
              {{ ruleForm.sxServiceExtend.promiseLimit }}
              <span v-if="ruleForm.sxServiceExtend.promiseLimitType == 'W'">
                (工作日)</span
              >
              <span v-if="ruleForm.sxServiceExtend.promiseLimitType == 'N'">
                (自然日)</span
              >
              <span v-if="ruleForm.sxServiceExtend.promiseLimitType == 'H'">
                (小时)</span
              >
            </template>
          </td>
        </tr>
        <tr>
          <td>
            <b>法定时限：</b>
          </td>
          <td>
            <template>
              {{ ruleForm.sxServiceExtend.legalLimit }}
              <span v-if="ruleForm.sxServiceExtend.legalLimitType == 'W'">
                (工作日)</span
              >
              <span v-if="ruleForm.sxServiceExtend.legalLimitType == 'N'">
                (自然日)</span
              >
              <span v-if="ruleForm.sxServiceExtend.legalLimitType == 'H'">
                (小时)</span
              >
            </template>
          </td>
          <td><b>结果样本类型：</b></td>
          <td colspan="3">
            <span v-if="ruleForm.sxServiceExtend.resultSampleType == 0">
              证照
            </span>
            <span v-if="ruleForm.sxServiceExtend.resultSampleType == 1">
              批文
            </span>
            <span v-if="ruleForm.sxServiceExtend.resultSampleType == 2">
              其他
            </span>
          </td>
        </tr>
        <tr>
          <td><b>结果名称：</b></td>
          <td>{{ ruleForm.sxServiceExtend.resultName }}</td>
          <td><b>结果送达方式：</b></td>
          <td>{{ ruleForm.resultDeliveryWayName }}</td>
        </tr>
        <tr>
          <td>
            <b>申请受理条件：</b>
          </td>
          <td colspan="3">
            <div
              v-for="(data, index) in ruleForm.sxAcceptConditions"
              :label="data.conditionText"
              :key="index"
            >
              <template v-if="ruleForm.sxAcceptConditions.length > 1">
                {{ index + 1 }}、{{ data.conditionText }}
              </template>
              <template v-else>
                {{ data.conditionText }}
              </template>
            </div>
          </td>
        </tr>
        <tr>
          <td>
            <b>事项情形：</b>
          </td>
          <td colspan="3">
            {{ currentHotSituationName }}
          </td>
        </tr>
        <tr>
          <td>
            <b>情形选项：</b>
          </td>
          <td colspan="3">
            <div v-for="(data, index) in serviceTitleValList" :key="index">
              {{ data.titleName }}{{ data.valueName }}
            </div>
          </td>
        </tr>
        <tr>
          <td>
            <b>申请材料：</b>
          </td>
          <td colspan="3">
            <div
              v-for="(data, index) in sxServiceMaterialList"
              :label="data.materialName"
              :key="index"
            >
              <span v-if="sxServiceMaterialList.length > 1">
                {{ index + 1 }}、{{ data.materialName }}
              </span>
              <span v-else>
                {{ data.materialName }}
              </span>
            </div>
          </td>
        </tr>
        <tr>
          <td>
            <b>办事流程：</b>
          </td>
          <td colspan="3">
            <div
              v-for="(data, index) in ruleForm.sxServiceLinks"
              :label="data.linkName"
              :key="index"
            >
              <template v-if="ruleForm.sxServiceLinks.length > 1">
                {{ index + 1 }}、{{ data.linkName }}
              </template>
              <template v-else>
                {{ data.linkName }}
              </template>
            </div>
          </td>
        </tr>
        <tr>
          <td>
            <b>常见问题：</b>
          </td>
          <td colspan="3">
            <div
              v-for="(data, index) in ruleForm.sxServiceQuestions"
              :label="data.title"
              :key="index"
            >
              <template>
                （{{index+1}}）问：{{ data.description }} <br />
                              答：{{ data.answer }}
              </template>
            </div>
          </td>
        </tr>
      </table>
    </div>
  </div>
</template>
<script>
import { getSxService } from "@/api/zc/businessManagement/windowAcceptance";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { VueCropper } from "vue-cropper";

export default {
  inheritAttrs: false,
  components: { Treeselect, VueCropper },
  props: {
    serviceOid: {
      type: String,
      default: ""
    },
    currentHotSituationName: {
      type: String,
      default: "暂无"
    },
    sxServiceMaterialList: {
      type: Array,
      default: () => []
    },
    situationCheckList: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      i: 0,
      rightShow: true,
      serviceTitleValList: [],
      // 弹出层标题
      title: "",
      labelPosition: "top",
      ruleForm: {
        serviceName: "",
        serviceTypeName: "",
        organName: "",
        sxServiceLocations: {},
        zxDhText: "",
        tsDhText: "",
        resultDeliveryWayName: "",
        sxServiceExtend: {},
        sxAcceptConditions: {},
        sxServiceLinks: {},
        sxServiceQuestions: {}
      }
    };
  },
  watch: {},
  created() {
    this.initServiceInfo();
  },
  methods: {
    /** 初始化事项 */
    initServiceInfo() {
      this.getServiceDetail();
      this.situationTitleVals();
      setTimeout(() => {
        this.clickPrinting();
      }, 1000);
    },
    situationTitleVals() {
      this.serviceTitleValList = [];
      this.situationCheckList.forEach(data => {
        let valName = "";
        if (data.answers) {
          data.answers.forEach((item, index) => {
            let checkNum = null;
            if (item.isSelected) {
              checkNum++;
              valName += item.name;
              if (checkNum > 1) {
                valName += "、";
              }
            }
          });
        } else {
          if (data.valueName) {
            valName = data.valueName;
          }
        }
        let situation = {};
        if (data.name) {
          situation.titleName = data.name;
        } else {
          if (data.titleName) {
            situation.titleName = data.titleName;
          }
        }
        situation.valueName = valName ? "【" + valName + "】" : "";
        this.serviceTitleValList.push(situation);
        if (this.currentHotSituationName) {
          this.currentHotSituationName =
            this.currentHotSituationName + "、" + situation.titleName;
        } else {
          this.currentHotSituationName = situation.titleName;
        }
      });
    },

    /** 获取事项详细信息 */
    getServiceDetail() {
      getSxService(this.serviceOid).then(response => {
        this.ruleForm.resultDeliveryWayName =
          response.data.sxService.resultDeliveryWayName;
        this.ruleForm.zxDhText = response.data.sxService.zxDhText;
        this.ruleForm.tsDhText = response.data.sxService.tsDhText;
        this.ruleForm.serviceName = response.data.sxService.serviceName;
        this.ruleForm.serviceTypeName = response.data.sxService.serviceTypeName;
        this.ruleForm.organName = response.data.sxService.organName;
        this.ruleForm.sxServiceLocations = response.data.sxServiceLocations;
        this.ruleForm.sxServiceExtend = response.data.sxServiceExtend;
        this.ruleForm.sxAcceptConditions = response.data.sxAcceptConditions;
        this.ruleForm.sxServiceLinks = response.data.sxServiceLinks;
        //this.ruleForm.sxServiceQuestions = response.data.sxServiceQuestions;
        //过滤enabledFlag为0的
        var ssqData = response.data.sxServiceQuestions;
        var ssqArray = [];
        if (ssqData) {
          for (var i = 0; i < ssqData.length; i++) {
            if (ssqData[i].enabledFlag === 1) {
              ssqArray.push(ssqData[i]);
            }
          }
          this.ruleForm.sxServiceQuestions = ssqArray;
        }
      });
    },
    /** 隐藏右侧 */
    hiddenRightSideBar() {
      this.rightShow = false;
    },
    /** 显示右侧 */
    showRightSideBar() {
      this.rightShow = true;
    },

    /** 点击打印按钮 */
    clickPrinting() {
      this.$nextTick(() => {
        $("#trueBtn").click();
      });
    }
  }
};
</script>
<style lang="scss" scoped>
.once-in-form {
  height: 100%;
  .common-dialog-content {
    height: calc(100% - 36px - 25px);
    position: relative;
  }
}

@page {
  margin: 15cm;
}
</style>
