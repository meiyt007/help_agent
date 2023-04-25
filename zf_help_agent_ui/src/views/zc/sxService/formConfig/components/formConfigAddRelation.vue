<template>
  <div class="form-config-add-relation">
    <div class="form-config-add-relation--title">关联对象信息</div>
    <el-form
      :model="ruleForm"
      :rules="rules"
      ref="ruleForm"
      label-width="120px"
    >
      <div class="form-config-add-relation--item">
        <el-form-item prop="variableName" label="对象属性名称：">
          <el-input
            v-model.trim="ruleForm.variableName"
            type="input"
            placeholder="对象属性名称"
            maxlength="50"
            show-word-limit
          />
        </el-form-item>
        <el-form-item prop="type" label="类型：" label-width="80px">
          <el-select v-model="ruleForm.type">
            <el-option
              v-for="data in objectTypeMap"
              :key="data.id"
              :label="data.value"
              :value="data.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="foreignKey" label="关联外键：" label-width="100px">
          <el-select
            v-model="ruleForm.foreignKey"
            placeholder="请选择"
            filterable
          >
            <el-option
              v-for="item in columnKeys"
              :key="item.fieldOid"
              :label="item.fieldCode"
              :value="item.fieldCode"
            />
          </el-select>
        </el-form-item>
      </div>
      <div class="form-config-add-relation--item" style="margin-top: 7px">
        <el-form-item label="备注：">
          <el-input
            v-model.trim="ruleForm.demo"
            type="textarea"
            placeholder="备注"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script>
import { validateLegalStrNoNumber, validateRepeat, validataVariableName } from './validate';
export default {
  name: 'FormConfigAddRelation',
  props: {
    columnKeys: {
      type: Array,
      default: () => []
    },
    formObjectExtand: {
      type: Object,
      default: () => ({})
    }
  },
  data () {
    return {
      ruleForm: {
        variableName: '',
        type: 2,
        demo: '',
        foreignKey: ''
      },

      objectTypeMap: [{ id: 1, value: '数组' }, { id: 2, value: '对象' }],

      rules: {
        variableName: [
          { required: true, message: '对象属性名称不能为空', trigger: 'blur' },
          { required: true, validator: validateLegalStrNoNumber, trigger: 'blur' },
          // { required: true, validator: validateRepeat, trigger: 'blur' },
          // { required: true, validator: validataVariableName, trigger: 'blur' }
        ],
        type: [
          { required: true, message: '属性类型不能为空', trigger: 'change' }
        ],
        foreignKey: [
          { required: true, message: '关联外键不能为空', trigger: 'change' }
        ]
      }
    }
  },
  created () {
    if (Object.keys(this.formObjectExtand).length > 0) {
      this.ruleForm = { ...this.formObjectExtand };
    }
  },
}
</script>

<style scoped lang="scss">
.form-config-add-relation {
  background: #f8f9fa;
  border-radius: 3px;
  padding: 40px 20px 20px;
  border: 1px dashed #eaeef3;
  position: relative;
  animation: show 1s;

  .form-config-add-relation--title {
    width: 110px;
    height: 26px;
    background: #e0e5f3;
    border-radius: 3px 3px 0px 0px;
    font-size: 14px;
    font-weight: 400;
    color: #3d5fb5;
    text-align: center;
    line-height: 26px;
    position: absolute;
    top: -10px;
    left: 0;

    &::after {
      content: '';
      position: absolute;
      top: -7px;
      right: -7px;
      width: 0;
      height: 0;
      border: 8px solid;
      border-left-width: 0;
      border-color: transparent transparent #3d5fb5;
    }
  }

  .form-config-add-relation--item {
    display: flex;
    align-items: center;

    ::v-deep .el-form-item {
      flex: 1;
    }
  }
}
</style>
