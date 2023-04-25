<!-- 智能审核 -->
<template>
  <div class="done-bussiness-intelligent-audit" style="height: 100%">
    <el-scrollbar
      class="common-dialog-content"
      ref="scrollbar"
      style="height: 100%"
    >
      <div class="zf-zc-table--title">材料列表</div>
      <table border="0" class="zf-zc-table">
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
    </el-scrollbar>

    <!-- 智审详情 -->
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
      onlyShow
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
import IntelligentAuditJs from '@/mixins/onelicence/intelligentAudit';
export default {
  name: "IntelligentAudit",
  mixins: [IntelligentAuditJs],
  mounted () {
    this.getMaterialPrePrialNew();
  },
};
</script>

<style lang="scss">
.done-bussiness-intelligent-audit {
  .operation-area {
    padding: 12px 20px !important;
    line-height: 40px;
    text-align: unset !important;
    img {
      vertical-align: middle;
      margin-right: 10px;
    }
  }

  tr {
    td:first-of-type {
      text-align: center !important;
    }
  }

  tr td {
    background-color: unset !important;
  }

  .el-input__icon {
    line-height: 30px !important;
  }
}

.el-dialog.common-dialog.intelligent-audit-detail.done-bussiness-detail-dialog {
  height: 700px;
  margin: 0 auto;
}
</style>
