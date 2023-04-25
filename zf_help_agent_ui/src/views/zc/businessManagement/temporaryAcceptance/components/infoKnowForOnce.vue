<!-- 一次告知 -->
<template>
  <div class="infoKnow-for-once">
    <el-scrollbar class="common-dialog-content">
      <div class="data-box">
        <div class="common-dialog--title">事项基本信息</div>
        <table class="common-dalog-table">
          <colgroup>
            <col width="15%" />
            <col width="35%" />
            <col width="15%" />
            <col width="35%" />
          </colgroup>
          <tbody>
            <tr>
              <td><b>事项名称：</b></td>
              <td>{{ serviceForm.serviceName }}</td>
              <td><b>事项类型：</b></td>
              <td>{{ serviceForm.serviceTypeName }}</td>
            </tr>
            <tr>
              <td><b>实施机构：</b></td>
              <td>{{ serviceForm.organName }}</td>
              <td><b>是否收费：</b></td>
              <td>{{ sxService.chargeFlagName }}</td>
            </tr>
            <tr v-if="sxService.chargeFlag == 1">
              <td><b>收费依据：</b></td>
              <td>{{ serviceForm.sxServiceExtend.chargeAccord }}</td>
              <td><b>收费标准：</b></td>
              <td>{{ serviceForm.sxServiceExtend.chargeStandard }}</td>
            </tr>
            <tr>
              <td><b>办理方式：</b></td>
              <td>{{ sxService.handleFormName }}</td>
              <td><b>领取方式：</b></td>
              <td>{{ sxService.resultDeliveryWayName }}</td>
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
              <td><b>办理时间：</b></td>
              <td>
                <div
                  v-for="(data, index) in serviceForm.sxServiceLocations"
                  :label="data.acceptDate"
                  :key="index"
                >
                  {{ data.acceptDate }}
                </div>
              </td>
              <td><b>办理地点：</b></td>
              <td>
                <div
                  v-for="(data, index) in serviceForm.sxServiceLocations"
                  :label="data.locationName"
                  :key="index"
                >
                  {{ data.locationName }}
                </div>
              </td>
            </tr>
            <tr>
              <td><b>监督电话：</b></td>
              <td>{{ serviceForm.tsDhText }}</td>
              <td><b>咨询电话：</b></td>
              <td>{{ serviceForm.zxDhText }}</td>
            </tr>

            <tr>
              <td><b>结果名称：</b></td>
              <td>{{ serviceForm.sxServiceExtend.resultName }}</td>
              <td><b>结果送达方式：</b></td>
              <td>{{ serviceForm.resultDeliveryWayName }}</td>
            </tr>

            <tr>
              <td><b>是否有特殊程序：</b></td>
              <td>{{ serviceForm.isSpecialName }}</td>
              <td><b>结果样本类型：</b></td>
              <td>
                <span v-if="serviceForm.sxServiceExtend.resultSampleType == 0">
                  证照
                </span>
                <span v-if="serviceForm.sxServiceExtend.resultSampleType == 1">
                  批文
                </span>
                <span v-if="serviceForm.sxServiceExtend.resultSampleType == 2">
                  其他
                </span>
              </td>
            </tr>

            <tr v-if="serviceForm.unionOrganFlag == 1">
              <td><b>是否联合办理：</b></td>
              <td>{{ serviceForm.unionOrganFlagName }}</td>
              <td><b>联合办理机构：</b></td>
              <td>{{ serviceForm.unionOrgan }}</td>
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
              <td><b>设定依据：</b></td>
              <td colspan="3">
                {{ serviceForm.sxServiceExtend.setAccord }}
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
          </tbody>
        </table>
      </div>
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
                  <span v-if="data.materialType == '2'"> 原件或者复印件 </span>
                </template>
              </td>
              <td>
                <template>
                  <span v-if="data.materialFormat == '1'">纸质</span>
                  <span v-if="data.materialFormat == '2'">电子版</span>
                  <span v-if="data.materialFormat == '3'">证照</span>
                  <span v-if="data.materialFormat == '4'">容缺补正</span>
                  <span v-if="data.materialFormat == '7'">告知承诺</span>
                </template>
              </td>
              <td>{{ data.paperNumber }}</td>
              <td>
                <template>
                  <span
                    class="bage-necessery"
                    v-if="data.materialMustFlag == '1'"
                  >
                    必要
                  </span>
                  <span v-if="data.materialMustFlag == '0'">不必要</span>
                  <span v-if="data.materialMustFlag == '2'">容缺后补</span>
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
    </el-scrollbar>
    <div class="common-dialog-footer">
      <el-button
        style="background: #3d5fb5; color: #fff"
        @click="openNoticeForm"
      >
        一次性告知单
      </el-button>
      <el-button type="primary" @click="$emit('lastStep', 1)">上一步</el-button>
      <el-button
        v-if="serviceForm.showRzhs == '1'"
        style="background: #3d5fb5; color: #fff"
        @click="getImageCamera"
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
            :serviceOid="serviceOid"
            :sxServiceMaterialList="sxServiceMaterialList"
            :situationCheckList="situationCheckList"
            :currentHotSituationName="currentHotSituationName"
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
        @close="closeHard"
        :close-on-click-modal="false"
        :close-on-press-escape="false"
        append-to-body
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
import { getSxService } from "@/api/zc/businessManagement/windowAcceptance";
import { showCallMessage } from "@/api/zc/businessManagement/doubleScreenInteraction";
import OnceInform from "@/views/zc/businessManagement/windowAcceptance/components/onceInform";
// 人证比对
import HardWare from '@/mixins/hardware';
export default {
  name: "InfoKnowForOnce",
  inheritAttrs: false,
  mixins: [HardWare],
  props: {
    loginUser: {
      type: Object,
      default: () => ({})
    },
    serviceOid: {
      type: String,
      default: ""
    },
    currentHotSituationName: {
      type: String,
      default: ""
    },
    sxServiceMaterialList: {
      type: Array,
      default: () => []
    },
    situationCheckList: {
      type: Array,
      default: () => []
    },
    caseOid: {
      type: String,
      default: ""
    },
  },
  components: { OnceInform, },
  data () {
    return {
      printObj: {
        id: "print",
        popTitle: "一次性告知单",
        extraHead: '<meta http-equiv="Content-Language"content="zh-cn"/>'
      },
      serviceForm: {
        serviceName: "",
        serviceTypeName: "",
        organName: "",
        sxServiceLocations: {},
        zxDhText: "",
        sjStatus:1,
        sxServiceExtend: {},
        sxAcceptConditions: {},
        sxServiceLinks: {},
        sxServiceQuestions: {},
        situationCheckList: []
      },
      //事项
      sxService: {},
      radio: "1",
      firstEnter: true,
      dialogVisible: false
    };
  },
  created () {
    this.getServiceDetail();
  },
  methods: {
    /** 获取事项详细信息 */
    getServiceDetail () {
      getSxService(this.serviceOid).then(({ code, data, message }) => {
        if (code !== 200)
          return this.$message.warning(message || "接口请求失败");
        this.sxService = data.sxService;
        var reg = new RegExp("null ", "g"); //创建正则RegExp对象
        this.serviceForm = {
          ...this.serviceForm,
          serviceName: data.sxService.serviceName,
          serviceTypeName: data.sxService.serviceTypeName,
          organName: data.sxService.organName,
          sxServiceLocations: data.sxServiceLocations,
          zxDhText: data.sxService.zxDhText,
          tsDhText: data.sxService.tsDhText,
          sxServiceExtend: data?.sxServiceExtend ?? {},
          sxAcceptConditions: data.sxAcceptConditions,
          sxServiceLinks: data.sxServiceLinks,
          sxServiceQuestions: data.sxServiceQuestions,
          showRzhs: data.sxService.showRzhs,
          sjStatus: data.sxService.sjStatus,
          resultDeliveryWayName: data.sxService.resultDeliveryWayName.replace(reg, "")
        };
        this.$emit("setServiceFormData", {
          serviceName: this.serviceForm.serviceName,
          zxDhText: this.serviceForm.zxDhText,
          serviceTypeName: this.serviceForm.serviceTypeName,
          showRzhs: this.serviceForm.showRzhs,
          organName: this.serviceForm.organName,
          sjStatus:this.serviceForm.sjStatus
        });
        this.showGuidence();
        this.firstEnter = false;
      });
    },

    //超级综窗柜台双屏显示办事指南信息
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
      sxService.serviceName = this.sxService.serviceName;
      sxService.chargeFlagName = this.sxService.chargeFlagName;
      sxService.chargeFlag = this.sxService.chargeFlag;
      sxService.zxDhText = this.sxService.zxDhText;
      sxService.tsDhText = this.sxService.tsDhText;
      sxService.promiseLimit = this.serviceForm.sxServiceExtend.promiseLimit;
      sxService.legalLimit = this.serviceForm.sxServiceExtend.legalLimit;
      sxService.handleFormName = this.sxService.handleFormName;
      data.sxService = sxService;
      pushUrl = pushUrl + "?jsonObject=" + JSON.stringify(data);
      await showCallMessage(pushUrl).then(response => {
        console.log(response);
      });
    },

    nextStep () {
      this.$emit("nextStep", 3);
    },

    openNoticeForm () {
      this.dialogVisible = true;
    },

    handleClose () {
      this.dialogVisible = false;
    }
  }
};
</script>

<style scoped lang="scss">
.bage-necessery {
  width: 75px;
  height: 28px;
  background: #fbe1cb;
  border-radius: 14px;
  display: inline-block;
  line-height: 28px;
  color: rgba(255, 60, 0, 0.8);
}
>>> .common-dialog.once-in-form {
  .el-dialog__body,
  .el-dialog__footer {
    background-color: #fff;
  }
}
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
</style>
