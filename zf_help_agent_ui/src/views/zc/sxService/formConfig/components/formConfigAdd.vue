<template>
  <div class="form-config-add">
    <div
      class="form-config-add--item form-config-add--first"
      @click="handleClickSingleForm"
    >
      <span>单表</span>
    </div>
    <div
      class="form-config-add--item form-config-add--second"
      @click="handleClickMasterSlaveForm"
    >
      <span>主从</span>
    </div>
    <div
      class="form-config-add--item form-config-add--third"
      @click="handleClickSubForm"
    >
      <span>子列表</span>
    </div>

    <el-dialog
      v-dialog-drag
      :title="title"
      append-to-body
      v-if="visible"
      :visible.sync="visible"
      bodyBackgroundColor="#E3E8EB"
      width="1100px"
      height="800px"
    >
      <FormConfigAddComponent
        :serviceName="serviceName"
        :serviceOid="serviceOid"
        :formType="formType"
        :columnTypeMap="columnTypeMap"
        :isResetFormConfig="isResetFormConfig"
        :formOid="formOid"
        @close="visible = false"
        @save-close="
          () => {
            visible = false
            $emit('save-close')
          }
        "
      />
    </el-dialog>
  </div>
</template>

<script>
import FormConfigAddComponent from './formConfigAddComponent';
export default {
  name: 'FormConfigAdd',
  mixins: [],
  props: {
    serviceName: String,
    serviceOid: String,
    isResetFormConfig: Boolean,
    formOid: String,
    columnTypeMap: {
      type: Array,
      default: () => []
    },
  },
  components: { FormConfigAddComponent },
  data () {
    return {
      title: '',
      formType: '', // 表单类型
      visible: false,
    }
  },
  methods: {
    handleClickSingleForm () {
      this.formType = 'single';
      this.title = '表单信息新增-单表';
      this.visible = true;
    },

    handleClickMasterSlaveForm () {
      this.formType = 'masterSlave';
      this.title = '表单信息新增-主从';
      this.visible = true;
    },

    handleClickSubForm () {
      this.formType = 'subForm';
      this.title = '表单信息新增-子列表';
      this.visible = true;
    },
  }
}
</script>

<style scoped lang="scss">
.form-config-add {
  background-color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-around;
  padding: 50px 0;
  border-radius: 5px;

  .form-config-add--item {
    width: 230px;
    height: 130px;
    background: url('~@/assets/image/formConfig/first.png') no-repeat center;
    background-size: 100% 100%;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;

    > span {
      font-size: 18px;
      font-family: Microsoft YaHei;
      font-weight: bold;
      color: #ffffff;
      user-select: none;
    }

    &:active,
    &:hover {
      > span {
        color: #ccc;
      }
    }
  }

  .form-config-add--second {
    background: url('~@/assets/image/formConfig/second.png') no-repeat center;
    background-size: 100% 100%;
  }

  .form-config-add--third {
    background: url('~@/assets/image/formConfig/third.png') no-repeat center;
    background-size: 100% 100%;
  }
}
</style>
