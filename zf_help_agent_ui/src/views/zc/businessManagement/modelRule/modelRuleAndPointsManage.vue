/**
* 验证规则详细信息
* @author: liangss
* @date: 2020-11-12
*/

<template>
  <div>
    <table
      cellspacing="0"
      cellpadding="0"
      border="0"
      style="margin-bottom: 20px"
      class="zf-zc-table"
    >
      <colgroup>
        <col width="20%" />
        <col width="30%" />
        <col width="20%" />
        <col width="30%" />
      </colgroup>
      <tr>
        <td>
          <b>事项编码：</b>
        </td>
        <td>
          {{ implementCode }}
        </td>
        <td>
          <b>事项名称：</b>
        </td>
        <td>
          {{ serviceName }}
        </td>
      </tr>
    </table>

    <div class="zf-zc-table--title">单项验证规则</div>
    <table
      cellspacing="0"
      cellpadding="0"
      border="0"
      class="zf-zc-table--common zf-zc-table--td-center"
    >
      <colgroup>
        <col width="5%" />
        <col width="11%" />
        <col width="11%" />
        <col width="11%" />
        <col width="15%" />
        <col width="30%" />
        <col width="30%" />
      </colgroup>
      <tr>
        <th>序号</th>
        <th>材料名称</th>
        <th>细化材料名称</th>
        <th>目录</th>
        <th>规则</th>
        <th>审核要点</th>
        <th>审核可信度</th>
      </tr>

      <template v-for="(rule, index) in faModelRuleValidationList">
        <tr>
          <td>
            {{ index + 1 }}
          </td>
          <td> {{rule.sxMaterialName}}</td>
          <td :title="rule.sxMaterialName">
            <!--           {{rule.sxMaterialName}}--->

            {{ rule.refinedMaterialName }}
          </td>
          <td>
            {{ rule.templateMetadataName }}
          </td>

          <td>
            <template v-if="rule.validateType === VALIDATION_TYPE_NOTNULL">
              不能为空
            </template>
            <template v-if="rule.validateType === VALIDATION_TYPE_NULL">
              必须为空
            </template>
            <template v-if="rule.validateType === VALIDATION_TYPE_CONTENT">
              文本验证
            </template>
            <template v-if="rule.validateType === VALIDATION_TYPE_STR_CONTAIN">
              文本包含
            </template>
            <template v-if="rule.validateType === VALIDATION_TYPE_RANGE">
              数字范围
            </template>
            <template v-if="rule.validateType === VALIDATION_TYPE_DATE_COMPARE">
              日期比较
            </template>
            <template v-if="rule.validateType === VALIDATION_TYPE_HAND_SIGN">
              手写验证
            </template>
            <template v-if="rule.validateType === VALIDATION_TYPE_SEAL">
              盖章验证
            </template>
            <template v-if="rule.validateType === VALIDATION_TYPE_MANUAL_AUDIT">
              人工审核
            </template>
            <template v-if="rule.validateType === VALIDATION_TYPE_CONTENT_THAN">
              关联对比
               <template v-if="rule.ruleType === 'DZBD'">
                （电子表单）
              </template>
              <template v-if="rule.ruleType === 'JCBD'">
                （基础表单）
              </template>
              <template v-if="rule.ruleType === 'DZZZ'">
                （电子证照）
              </template>
            </template>
            <template v-if="rule.validateType === VALIDATION_TYPE_SIMILARITY">
              相识度
            </template>
          </td>

          <td>
            <el-col :span="24">
              <template v-if="rule.reviewPointsList != ''">
                <el-select
                  placeholder="请选择对应目录"
                  v-model="rule.reviewPointOid"
                  :clearable="true"
                  popper-class="package-model-points-manage--select"
                >
                  <el-option
                    v-for="catalog in rule.reviewPointsList"
                    :key="catalog.id"
                    :label="catalog.reviewPoints"
                    :value="catalog.oid"
                  >
                    <el-tooltip
                      v-if="!isShowTooltip(catalog.reviewPoints, 400)"
                      effect="dark"
                      :content="catalog.reviewPoints"
                      placement="top"
                    >
                      <span>{{ catalog.reviewPoints }}</span>
                    </el-tooltip>

                    <span v-else>{{ catalog.reviewPoints }}</span>
                  </el-option>
                </el-select>
              </template>
            </el-col>
          </td>
          <td>
            <el-select
              placeholder="请选择对应审核状态"
              v-model="rule.auditPointStatus"
              :clearable="true"
            >
              <el-option
                v-for="auditPointS in auditPointStatusList"
                :key="auditPointS.id"
                :label="auditPointS.name"
                :value="auditPointS.id"
              ></el-option>
            </el-select>
          </td>
        </tr>
      </template>
    </table>

    <div slot="footer" class="dialog-footer">
      <el-button
        style="display: inline-block"
        type="primary"
        size="medium"
        @click="queding()"
        class="save-btn"
        >确定</el-button
      >
      <el-button size="medium" @click="guanbi()" class="close-btn"
        >关闭</el-button
      >
    </div>
  </div>
</template>

<script>
import { initFaModelRule, initAll, initFaModelRuleValidation, saveOrUpdate } from "@/api/zc/businessManagement/faModelRuleValidation.js";
import { isShowTooltip } from '@/utils/utils.js';
export default {
  name: "ModelRuleAndPointsManage",
  props: ['serviceOid', 'implementCode', 'serviceName'],
  data () {
    return {
      num: 0,
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 弹出层标题
      title: "",
      //人工审核
      auditPointStatusList: [
        {
          id: '1',
          name: '审核可信度高',
        },
        {
          id: '2',
          name: '审核可信度低',
        },
        {
          id: '3',
          name: '需人工审核',
        },
      ],
      //验证规则列表
      faModelRuleValidationList: [],
      VALIDATION_TYPE_NOTNULL: '',
      VALIDATION_TYPE_NULL: '',
      VALIDATION_TYPE_CONTENT: '',
      VALIDATION_TYPE_STR_CONTAIN: '',
      VALIDATION_TYPE_RANGE: '',
      VALIDATION_TYPE_DATE_COMPARE: '',
      VALIDATION_TYPE_HAND_SIGN: '',
      VALIDATION_TYPE_SEAL: '',
      VALIDATION_TYPE_CONTENT_THAN: '',
      VALIDATION_TYPE_SIMILARITY: '',
      VALIDATION_TYPE_MANUAL_AUDIT: '',
      // 查询参数
      queryParams: {
        serviceOid: this.serviceOid,
      },
      defaultProps: {
        children: "children",
        label: "label"
      },

    };
  },
  created () {
    //初始化数据
    this.initInfo();
    /** 初始化数据 */
    initAll().then(response => {
      this.VALIDATION_TYPE = response.data.VALIDATION_TYPE;
      this.VALIDATION_TYPE_NOTNULL = response.data.VALIDATION_TYPE_NOTNULL;
      this.VALIDATION_TYPE_NULL = response.data.VALIDATION_TYPE_NULL;
      this.VALIDATION_TYPE_CONTENT = response.data.VALIDATION_TYPE_CONTENT;
      this.VALIDATION_TYPE_STR_CONTAIN = response.data.VALIDATION_TYPE_STR_CONTAIN;
      this.VALIDATION_TYPE_RANGE = response.data.VALIDATION_TYPE_RANGE;
      this.VALIDATION_TYPE_DATE_COMPARE = response.data.VALIDATION_TYPE_DATE_COMPARE;
      this.VALIDATION_TYPE_HAND_SIGN = response.data.VALIDATION_TYPE_HAND_SIGN;
      this.VALIDATION_TYPE_SEAL = response.data.VALIDATION_TYPE_SEAL;
      this.VALIDATION_TYPE_CONTENT_THAN = response.data.VALIDATION_TYPE_CONTENT_THAN;
      this.VALIDATION_TYPE_SIMILARITY = response.data.VALIDATION_TYPE_SIMILARITY;
      this.VALIDATION_TYPE_MANUAL_AUDIT = response.data.VALIDATION_TYPE_MANUAL_AUDIT;
    });
  },
  methods: {
    isShowTooltip,
    guanbi () {
      this.$emit('father-click', 'N');//调用父页面关闭子页面的功能
    },

    initInfo () {
      this.loading = true;
      initFaModelRuleValidation(this.queryParams).then(response => {
        this.faModelRuleValidationList = response.data;
        console.log(JSON.stringify(this.faModelRuleValidationList))
        this.loading = false;
      });
    },
    queding () {
      this.loading = true;
      console.log("saveOrUpdate");
      console.log(this.faModelRuleValidationList);
      /* return;*/
      saveOrUpdate(this.faModelRuleValidationList).then(response => {
        if (response.code === 200) {
          this.msgSuccess("保存成功");
          this.loading = false;
          setTimeout(() => {
            this.$emit('father-click', 'N');//调用父页面关闭子页面的功能
          }, 10);

        }
      });
    },



  },
};
</script>

<style lang="scss" scoped>
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
// .select-list span:first-child {
//   background-color: #fff6f1;
//   color: #2d506b;
// }
.select-list span.current {
  background-color: #4d87b5;
  color: #fff;
  position: relative;
}
.select-list span.current:after {
  content: '';
  position: absolute;
  width: 19px;
  height: 13px;
  right: 10px;
  top: 13px;
  background: url(../../../../assets/image/check-icon.png) no-repeat center;
  background-size: cover;
}
.require-box {
  font-size: 12px;
  color: #ff0000;
  margin: 0px 0px 10px 0px;
  height: 30px;
  line-height: 30px;
  text-align: center;
  background-color: #fff7f7;
}
.require-table table tr th {
  background-color: #f8f9fb;
}
.require-table table tr td {
  text-align: center;
  vertical-align: top;
}
.require-table table tr td:nth-of-type(2n + 1) {
  background-color: #fff;
}
.add-box ul {
  margin: 0;
  padding: 0;
}
.add-box ul li {
  list-style: none;
  text-align: left;
  height: 30px;
  line-height: 30px;
  background-color: #f8f9fb;
  border: 1px solid #f8f9fb;
  margin-top: 5px;
  text-indent: 5px;
  cursor: pointer;
  color: #0b61a3;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  position: relative;
}
.btn-add {
  display: inline-block;
  vertical-align: middle;
  border: none;
  padding: 0;
  position: absolute;
  right: 8px;
  top: 8px;
  cursor: pointer;
  display: none;
}
/*.add-box ul li:hover {
  border: 1px solid #0b61a3;
  background-color: #fff;
}
.add-box ul li:hover .btn-add {
  color: #0b61a3;
  display: block;
}*/
.require-item {
  vertical-align: top;
  padding: 20px 10px;
  box-sizing: border-box;
  background-color: #f5f5f5;
  border: 1px solid #f5f5f5;
  position: relative;
  cursor: pointer;
  margin-bottom: 5px;
}
/*.require-item:hover {
  background-color: #ffffff;
  border: 1px solid #0b61a3;
}*/
.btn-close {
  position: absolute;
  right: 3px;
  bottom: 3px;
  padding: 0;
  border: none;
  color: #ff0000;
}
.btn-close-position {
  position: inherit;
}
.set-table table tr td {
  vertical-align: middle;
}
.save-btn,
.close-btn {
  margin: 30px auto 0px auto;
}
</style>
<style lang="scss">
.el-select-dropdown.el-popper.package-model-points-manage--select {
  max-width: 400px;
}
</style>
