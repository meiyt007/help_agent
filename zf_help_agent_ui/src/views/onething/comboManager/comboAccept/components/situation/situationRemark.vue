<template>
  <el-popover
    placement="right-start"
    width="300"
    :open-delay="500"
    popper-class="situation-item-popover"
    trigger="hover"
    :content="content"
  >
    <i class="el-icon-info situation-item-popover--tips" slot="reference" />
  </el-popover>
</template>

<script>
export default {
  name: 'SituationRemark',
  props: {
    content: {
      type: String,
      default: '',
    },
  }
}
</script>

<style lang='scss'>
.el-popover.situation-item-popover {
  background: #eaf4fd;
  border: 1px solid #237ee9;
  box-shadow: 0px 3px 7px 0px rgba(83, 113, 200, 0.35);
  color: #237ee9;
  padding: 10px;

  &[x-placement^='right'] .popper__arrow {
    border-right-color: #237ee9;

    &::after {
      border-right-color: #eaf4fd;
    }
  }
}

.situation-item-popover--tips {
  color: #3689eb;
  vertical-align: bottom;
  margin: 0 5px;
  font-size: 20px;
}
</style>