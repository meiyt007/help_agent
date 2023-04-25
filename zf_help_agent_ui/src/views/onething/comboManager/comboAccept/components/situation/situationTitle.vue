<template>
  <p
    :class="[
      'situation-item-title',
      situation.ifFrist ? 'common-dialog--title' : '',
      situation.linkStatus === 1 && situation.linkUrl && !situation.ifFrist
        ? 'situation-item-title--link'
        : '',
      situation.ifFrist && situation.linkStatus === 1
        ? 'situation-item-title-first--link'
        : '',
      situation.ifFrist && situation.columnType == 3 && !situation.linkStatus
        ? 'situation-item-title-first--input'
        : '',
    ]"
  >
    <span
      class="situation-item-title--link-span"
      @click="$emit('openWindowlink')"
    >
      {{ situation.situationName }}
    </span>
    <span v-if="situation.mustStatus == 1 && situation.linkStatus !== 1" class="situation-item-must">
      {{ situation.columnType == 3 ? '[必填]' : '[必选]' }}
    </span>
    <SituationRemark v-if="situation.tips" :content="situation.tips" />
    <slot />
  </p>
</template>

<script>
import SituationRemark from './situationRemark';
export default {
  name: 'SituationTitle',
  props: {
    situation: {
      type: Object,
      default: {},
    },
  },
  components: { SituationRemark },
}
</script>

<style lang='scss' scoped>
.situation-item-title {
  font-weight: bold;
  color: #2a344c;
  font-size: 14px;
}
.situation-item-title-first--link,
.situation-item-title--link {
  text-decoration: underline;
  cursor: pointer;
  margin: unset;
}

.situation-item-title-first--input {
  margin-bottom: unset !important;
}

.situation-item-title--link-span {
  margin-right: 4px;
}
</style>