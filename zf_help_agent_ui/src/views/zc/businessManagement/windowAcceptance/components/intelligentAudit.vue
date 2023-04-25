<!-- 智能审核 -->
<template>
  <div class="intelligent-audit">
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
      <!-- <div class="common-dialog--title" v-if="radioNotice">
        补正信息
      </div>
      <table
        v-if="radioNotice"
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
              v-model.trim="dueDate"
              type="date"
              value-format="yyyy-MM-dd"
              start
              :picker-options="optionDate"
              placeholder="选择承诺补正时间"
            ></el-date-picker>
          </td>
        </tr>
      </table> -->

      <!-- <div
        style="
          text-align: center;
          position: absolute;
          bottom: 10px;
          width: 100%;
        "
      >
        <el-checkbox
          v-if="checkFlag && !isSxPersonFlg"
          v-model.trim="radioNotice"
          label="1"
          @change="chooseNotice"
        >
          当前事项支持告知承诺方式进行材料提交，是否需要？
        </el-checkbox>
      </div> -->
    </el-scrollbar>

    <div class="common-dialog-footer">
      <el-button type="primary" @click="$emit('lastStep', 4)">上一步</el-button>
<!--      <el-button style="background: #3d5fb5; color: #fff" @click="closeAndInit">
        签名
      </el-button>-->
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
      :serviceName="serviceName"
      :caseOid="caseOid"
      :serviceOid="serviceOid"
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
import IntelligentAuditJs from "@/mixins/intelligentAudit";

/** 签名控件websocket */
import jSign from '@/mixins/jSign';

/** 签名控件 ajax */
// import Sign from '@/mixins/sign';
export default {
  name: "IntelligentAudit",
  mixins: [IntelligentAuditJs, jSign],
  props: ["sxServiceMaterialList", "loginUser", "ruleForm"],
  data () {
    return {
      radioNotice: "",
      // dueDate: "",
      // optionDate: {
      //   disabledDate (time) {
      //     return time.getTime() < Date.now(); // 选当前时间之后的时间
      //   }
      // },
      checkFlag: false, // 告知单
      isSxPersonFlg: false, // 失信人
      isKeepAlive: true, // 是否是缓存组件
      intervalCount: null,
    };
  },
  created () {
    this.checkServiceAndUser();
  },
  activated () {
    this.getMaterialPrePrialNew();
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

      // if (this.radioNotice && !this.dueDate)
      //   return this.$message.warning("请选择补正时间");

      // this.$emit("setDueData", this.dueDate);
      this.$emit("nextStep", 6);
    },

    chooseNotice (value) {
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
      getInformPromiseByServiceOid(this.serviceOid).then(response => {
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
.intelligent-audit {
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

.dialog-container {
  width: 100%;
  height: 100%;
  display: flex;
  &-left {
    flex: 1;
    padding: 0 30px;
    align-items: flex-start;
    display: flex;
    justify-content: space-around;
    padding-top: 70px;
    position: relative;

    img {
      object-fit: cover;
    }

    .title {
      height: 34px;
      font-weight: bold;
      font-size: 16px;
      background: #bfcfd9;
      border-radius: 10px 10px 0px 0px;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .content {
      height: 485px;
      border-radius: 0px 0px 12px 12px;
      background: #fff;
    }

    .sample-table {
      width: 45%;
    }

    .scanning-materials {
      width: 45%;

      .title {
        background: #f7d599;
      }
    }
  }

  &-right {
    width: 308px;
    background: #ffffff;
    border-radius: 5px;
    text-align: center;
    padding: 24px 10px;

    &--title {
      display: flex;
      justify-content: space-between;

      .page {
        color: #798998;
      }

      i {
        color: #2e7dff;
        font-style: inherit;
      }
    }

    &--list {
      height: 90%;
      padding-bottom: 20px;

      .list-item {
        background: #edf0f5;
        border: 1px solid #e0e6f0;
        border-bottom: unset;
        display: flex;
        flex-direction: column;
        text-align: left;
        margin-bottom: 30px;

        &--child {
          border-bottom: 1px solid #e0e6f0;
          width: 100%;
          background-color: #fff;
          line-height: normal;
          padding: 10px 10px 10px 20px;
        }

        &--title {
          padding-left: 10px;
        }
      }
    }
  }

  .common-dialog--title {
    font-size: 14px;
    font-weight: bold;
    color: #2a344c;
    position: relative;
    padding-left: 10px;
    margin-bottom: 25px;
    text-align: left;

    &::before {
      content: '';
      position: absolute;
      left: 0;
      top: 0;
      width: 3px;
      height: 16px;
      background: #2e7dff;
      border-radius: 2px;
    }
  }
}
</style>
