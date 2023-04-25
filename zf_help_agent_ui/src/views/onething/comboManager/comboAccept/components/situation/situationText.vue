<template>
  <div class="situation-item-text">
    <div
      style="display: flex; align-items: center"
      :class="{ 'situation-item-text--first': situation.ifFrist }"
    >
      <span
        class="situation-item-title situation-item-text--title"
        style="font-size: 14px"
        v-if="situation.titleShowStyle === 1"
      >
        {{ situation.situationName }}
      </span>
      <span
        v-if="situation.mustStatus == 1"
        class="situation-item-must"
        style="margin-left: 5px"
      >
        [必填]
      </span>
      <SituationRemark v-if="situation.tips" :content="situation.tips" />
      <el-input
        :value="inputTextVal"
        @input="(value) => $emit('input', value)"
        ref="inputText"
        :placeholder="'请输入' + situation.situationName"
        style="width: 360px; margin-left: 20px"
        @change="checkContent"
      />
    </div>
    <span v-if="null != inputVal">
      <el-radio-group
        v-model="inputVal.optionCode"
        class="el-radio-group--text"
        :class="{ 'el-radio-group--text-first': situation.ifFrist }"
      >
        <el-radio :label="inputVal.optionCode">
          {{ inputVal.name }}
        </el-radio>
        <SituationRemark v-if="inputVal.remark" :content="inputVal.remark" />
      </el-radio-group>
    </span>
  </div>
</template>

<script>
import SituationRemark from './situationRemark';
import { getOptionValBySituOid } from "@/api/onething/comboManager/comboAccept/situation";
export default {
  name: 'SituationText',
  model: {
    prop: 'inputTextVal',
    event: 'input'
  },
  props: {
    situation: {
      type: Object,
      default: {},
    },
    inputTextVal: {
      type: String,
      default: ''
    },
    inputVal: {
      type: Object,
      default: () => ({})
    }
  },
  components: { SituationRemark },
  methods: {
    //验证文本框内容
    checkContent () {
      const FloatRegex = /^[0-9]+\.?[0-9]*$/;
      if (!FloatRegex.test(this.inputTextVal)) {
        this.msgWarning("输入框内容必须为数值");
        return false;
      }
      if (parseFloat(this.inputTextVal) >= 100000000) {
        this.msgWarning("输入框内容数值过大");
        return false;
      }
      getOptionValBySituOid({
        situationOid: this.situation.situationOid,
        textVal: this.inputTextVal
      }).then(response => {
        if (response.data) {
          let obj = {
            valOid: response.data.valOid,
            name: response.data.name,
            remark: response.data.remark,
            optionCode: response.data.optionCode
          };
          //   this.inputVal = obj;
          this.$emit('setInputVal', obj);
        }
      });
    },
  }
}
</script>

<style scoped lang='scss'>
.situation-item-text--first {
  font-size: 14px;
  font-weight: bold;
  color: #2a344c;
  position: relative;
  padding-left: 10px;

  &::before {
    content: '';
    position: absolute;
    left: 0;
    top: 50%;
    transform: translateY(-50%);
    width: 3px;
    height: 16px;
    background: #2e7dff;
    border-radius: 2px;
  }

  ::v-deep.situation-item-popover--tips {
    margin-top: 3px;
  }
}

.el-radio-group--text-first {
  margin-left: 30px;
}
</style>