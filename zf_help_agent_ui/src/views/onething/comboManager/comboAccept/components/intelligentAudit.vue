<!-- 智能审核 -->
<template>
  <div class="onething-intelligent-audit">
    <el-scrollbar class="common-dialog-content" ref="scrollbar">
      <div class="common-dialog--title">材料列表</div>
      <table border="0" class="data-table common-dalog-table">
        <colgroup>
          <col width="10%" />
          <col width="55%" />
          <col width="35%" />
        </colgroup>
        <tr>
          <th>序号</th>
          <th>材料名称</th>
          <th>材料情况</th>
        </tr>
        <tbody v-if="intelligentList.length > 0" style="background: #fff">
          <tr v-for="(data, index) in intelligentList" :key="index">
            <td>{{ index + 1 }}</td>
            <td style="text-align: left !important">{{ data.materialName }}</td>
            <td class="operation-area" style="text-align: unset !important">
              <!-- 没有点击确认材料无误 -->
              <template v-if="data.confirmStatus !== 'Y'">
                <span
                  v-if="data.resultStatus === RESULT_STATUS_NOT_PASS"
                  style="color: #ff3600"
                >
                  <img
                    src="@/assets/image/intelligent/common-0.png"
                    alt
                    height="20"
                    width="20"
                  />
                  机器预审不通过
                </span>
                <span
                  v-if="data.resultStatus === RESULT_STATUS_PASS"
                  style="color: #0cb959"
                >
                  <img
                    src="@/assets/image/intelligent/common-2.png"
                    alt
                    height="20"
                    width="20"
                  />
                  机器预审通过
                </span>
                <span
                  v-if="
                    data.resultStatus === RESULT_STATUS_PASS_AND_MUST_ARTIFICIAL
                  "
                  style="color: #0cb959"
                >
                  <img
                    src="@/assets/image/intelligent/common-1.png"
                    alt
                    height="20"
                    width="20"
                  />
                  机器预审通过（部分需人工审核）
                </span>
                <span
                  v-if="data.resultStatus === RESULT_STATUS_NOT_AUDIT"
                  style="color: #0cb959"
                >
                  <img
                    src="@/assets/image/intelligent/common-2.png"
                    alt
                    height="20"
                    width="20"
                  />
                  无需智审
                </span>
                <span
                  v-if="data.resultStatus === RESULT_STATUS_ARTIFICIAL_AUDIT"
                  style="color: #2998ff"
                >
                  <img
                    src="@/assets/image/intelligent/common-1.png"
                    alt
                    height="20"
                    width="20"
                  />
                  需人工核验
                </span>
              </template>
              <!-- 已经点击材料无误 -->
              <span v-else style="color: #0cb959">
                <img
                  src="@/assets/image/intelligent/common-2.png"
                  alt
                  height="20"
                  width="20"
                />
                已通过人工核验
              </span>
              <el-button
                :disabled="data.resultStatus === RESULT_STATUS_NOT_AUDIT"
                style="float: right"
                type="primary"
                @click="previewDetail(data)"
              >
                查看
              </el-button>
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
      <!-- <div class="common-dialog--title" v-if="radioNotice && !isRqslFlag">
        补正信息
      </div>
      <table
        v-if="radioNotice && !isRqslFlag"
        cellspacing="0"
        cellpadding="0"
        border="0"
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
            <b>承诺补正时间：</b>
          </td>
          <td colspan="3">
            <el-date-picker
              v-model="dueDate"
              type="date"
              value-format="yyyy-MM-dd"
              start
              :picker-options="optionDate"
              placeholder="选择承诺补正时间"
            ></el-date-picker>
          </td>
        </tr>
      </table>

      <div
        v-if="!isRqslFlag"
        style="
          text-align: center;
          position: absolute;
          bottom: 10px;
          width: 100%;
        "
      >
        <el-checkbox
          v-if="checkFlag && !isSxPersonFlg"
          v-model="radioNotice"
          label="1"
          @change="chooseNotice"
        >
          当前事项支持告知承诺方式进行材料提交，是否需要？
        </el-checkbox>
      </div> -->
    </el-scrollbar>

    <div class="common-dialog-footer">
      <el-button type="primary" @click="$emit('lastStep', 4)">上一步</el-button>
      <!-- <el-button style="background: #3d5fb5; color: #fff" @click="closeAndInit">
        签名
      </el-button> -->
      <el-button type="primary" @click="nextStep">下一步</el-button>
    </div>

    <!-- 智审详情 -->
    <!-- caseOid="f82e6791012143058d59c94256683426"
    serviceOid="2c287b8b79f39489017a190d7f8c13ff"-->
    <IntelligentAuditDetail
      v-if="visibleDetail"
      :visibleDetail.sync="visibleDetail"
      :intelligentDetailList="intelligentDetailList"
      :total="total"
      :isManualVerification="isManualVerification"
      :serviceName="comboDirectoryName"
      :caseOid="caseOid"
      :serviceOid="comboDirectoryOid"
      :qlCaseMaterial="qlCaseMaterial"
      :onlyShow="onlyShow"
      @setAll="setAll"
      @setNoPass="setNoPass"
      @refreshIntelligentList="
        () => {
          visibleDetail = false
          getMaterialPrePrialNew()
        }
      "
    />
  </div>
</template>

<script>
import {
  getInformPromiseByServiceOid,
  getDishonestPerson
} from "@/api/zc/businessManagement/windowAcceptance";
import IntelligentAuditJs from "@/mixins/onething/intelligentAudit";

/** 签名控件websocket */
import jSign from '@/mixins/jSign';
/** 签名混入js */
// import SignMixinJs from '@/mixins/sign';
export default {
  name: "IntelligentAudit",
  mixins: [IntelligentAuditJs, jSign],
  data () {
    return {
      // radioNotice: "",
      // dueDate: "",
      // optionDate: {
      //   disabledDate (time) {
      //     return time.getTime() < Date.now(); // 选当前时间之后的时间
      //   }
      // },
      checkFlag: false, // 告知单
      isSxPersonFlg: false, // 失信人
    };
  },
  created () {
    this.checkServiceAndUser();
  },
  activated () {
    !this.isTempComponent && this.getMaterialPrePrialNew();
  },
  methods: {
    nextStep () {
      // 判断是否都以确认材料无误
      const noConfirm = this.intelligentList
        .filter(item => item.resultStatus === this.RESULT_STATUS_NOT_PASS)
        .find(item => item.confirmStatus !== "Y");
      if (noConfirm) {
        return this.$message.warning(
          `请确认材料【${noConfirm.materialName}】无误`
        );
      }

      // if (this.isRqslFlag) {
      //   this.dueDate = '';
      //   this.radioNotice = false;
      // }

      // if (this.radioNotice && !this.dueDate)
      //   return this.$message.warning("请选择补正时间");
      // this.$emit("setDueData", this.dueDate);
      this.$emit("nextStep", 6);
    },

    chooseNotice () {
      if (!value) {
        this.dueDate = '';
      };
      // 自动滚动到底部
      this.$nextTick(() => {
        if (this.radioNotice) {
          this.$refs.scrollbar.$refs.wrap.scrollTop = this.$refs.scrollbar.$refs.wrap.scrollHeight;
        }
      });
    },

    checkServiceAndUser () {
      // 是否是承诺告知单
      getInformPromiseByServiceOid(this.comboDirectoryOid).then(response => {
        if (response.data) {
          this.checkFlag = true;
          // 判断是否是失信人
          getDishonestPerson(
            this.ruleForm.applyUserName,
            this.ruleForm.credentialNumber
          ).then(_response => {
            if (_response.data) {
              this.isSxPersonFlg = true; //失信人
            } else {
              this.isSxPersonFlg = false;
            }
          });
        } else {
          this.checkFlag = false;
        }
      });
    }
  }
};
</script>

<style lang="scss">
.onething-intelligent-audit {
  .operation-area {
    padding: 12px 20px !important;
    line-height: 40px;
    img {
      vertical-align: middle;
      margin-right: 10px;
    }
  }

  .el-input__icon {
    line-height: 30px !important;
  }
}
</style>
