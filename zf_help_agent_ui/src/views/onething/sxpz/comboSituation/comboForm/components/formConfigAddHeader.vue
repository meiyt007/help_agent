<template>
  <div class="form-config-add-header">
    <div class="form-config-add-header--left">
      <el-tooltip
        effect="dark"
        :content="`事项名称：${comboDirectoryName}`"
        placement="top-start"
      >
        <span>目录名称：{{ comboDirectoryName }}</span>
      </el-tooltip>
    </div>
    <div class="form-config-add-header--right">
      <span>选择分类</span>
      <el-select
        style="margin-left: 10px"
        size="mini"
        placeholder="请选择"
        filterable
        v-model="fieldTypeOid"
        @change="selectFieldType"
      >
        <el-option
          v-for="dept in fieldTypeList"
          :key="dept.fieldTypeOid"
          :label="dept.fieldTypeName"
          :value="dept.fieldTypeOid"
        />
      </el-select>
      <el-button
        type="warning"
        icon="el-icon-refresh"
        size="mini"
        @click="handleReset"
        class="ml5"
      >
        重置
      </el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FormConfigAddHeader',
  props: {
    comboDirectoryName: String,
    formType: String,
    isSingle: Boolean,
    fieldTypeList: Array
  },
  data () {
    return {
      fieldTypeOid: ''
    }
  },
  methods: {
    selectFieldType (val) {
      const value = this.fieldTypeList.find(item => item.fieldTypeOid === val);
      value && this.$emit('setClassify', value);
    },

    handleReset () {
      this.fieldTypeOid = '';
      this.handleFieldTypeClear();
    },

    handleFieldTypeClear () {
      this.$emit('setClassify', null);
    }
  }
}
</script>

<style scoped lang="scss">
.form-config-add-header {
  height: 50px;
  background: #ffffff;
  border-radius: 5px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 10px;

  .form-config-add-header--left {
    font-size: 14px;
    font-family: Microsoft YaHei;
    font-weight: bold;
    color: #2a344c;
    max-width: 700px;
    @include overflow-ellipsis();
  }

  .form-config-add-header--right {
    font-size: 14px;
    font-family: Microsoft YaHei;
    font-weight: bold;
    color: #666666;
    display: flex;
    align-items: center;

    ::v-deep .el-select .el-tag {
      max-width: 74%;
    }
  }
}
</style>
