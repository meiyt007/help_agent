<template>
  <el-scrollbar class="form-config-add-tab">
    <template v-if="classifyList.length">
      <div
        class="form-config-add-tab--item"
        v-for="(item, idx) in classifyList"
        :key="idx"
      >
        <div
          class="form-config-add-tab--name"
          :class="{ active: currentId == idx }"
          @click="handleClickClassify(item, idx)"
        >
          <img src="@/assets/image/formConfig/suffix.png" width="15px" alt="" />
          <span>{{ item.labelName }}</span>
        </div>
      </div>
    </template>
    <el-empty v-else description="请先选择分类名称"></el-empty>
  </el-scrollbar>
</template>

<script>
export default {
  name: 'FormConfigAddTab',
  props: {
    classifyList: Array,
    isSingle: Boolean,
    isSubForm: Boolean
  },
  data () {
    return {
      currentId: 0,
    }
  },
  methods: {
    handleClickClassify (item, idx) {
      if (this.currentId === idx) return;
      this.currentId = idx;
      this.$emit('setCurrentFieldTypeOid', item.fieldTypeOid);
    },
  }
}
</script>

<style scoped lang="scss">
.form-config-add-tab {
  flex: 1;
  background: #ffffff;
  border-radius: 5px 5px 0 0;
  height: calc(100% - 10px);
  margin-top: 10px;
  padding: 10px;

  .form-config-add-tab--item {
    margin-bottom: 20px;
    animation: show 1s;

    &:last-child {
      margin-bottom: unset;
    }
  }

  .form-config-add-tab--name {
    height: 60px;
    border-radius: 4px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #486281;
    box-shadow: 0px 3px 7px 0px rgba(72, 98, 129, 0.35);

    &.active {
      background: #007ee8;
      box-shadow: 0px 3px 7px 0px rgba(16, 140, 238, 0.35);
    }

    font-size: 16px;
    font-family: Microsoft YaHei;
    font-weight: 400;
    color: #ffffff;

    > span {
      max-width: 115px;
      @include overflow-rows(2);
    }

    img {
      margin-right: 5px;
      margin-top: 1px;
    }
  }

  .form-config-add-tab--is-main {
    height: 70px;
    background: #f6f7f9;
    border-radius: 0px 0px 4px 4px;
    display: flex;
    align-items: center;
    padding-left: 15px;

    ::v-deep .el-radio__label {
      font-size: 14px;
      font-weight: bold;
      color: #486281;
    }
  }
}
</style>
