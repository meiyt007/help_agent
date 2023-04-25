<template>
  <div class="intelligent-tabs-list">
    <div
      class="intelligent-tabs-list--title"
      :style="{
        color: titleFontsColor,
        backgroundColor: titleBgColor,
        borderColor: borderColor,
      }"
    >
      {{ title }}
    </div>
    <el-scrollbar
      :class="[
        'intelligent-tabs-list--container',
        isManual ? 'intelligent-tabs-list--container-is-manual' : '',
      ]"
      v-if="list.length > 0"
    >
      <div
        class="intelligent-tabs-list--container-item"
        v-for="(item, index) in sortList"
        :key="index"
        :style="{
          borderColor: borderColor,
          color: item.isManual ? '#e88c47' : listFontsColor,
        }"
      >
        {{ item.serialNumber }} {{ item.reviewPoints }}
      </div>
    </el-scrollbar>
    <el-empty
      v-else
      :description="resultStatus === '6' ? '附件不符合审核模板' : '暂无数据'"
    />
  </div>
</template>

<script>
import {
  TYPES_TITLE_MAP,
  TYPES_TITLE_BG_COLOR_MAP,
  TYPES_TITLE_FONTS_COLOR_MAP,
  TYPES_LIST_FONTS_COLOR_MAP,
  TYPES_BORDER_COLOR_MAP
} from './constant';
export default {
  name: 'IntelligentTabsList',
  props: {
    resultStatus: String,
    list: {
      type: Array,
      default: () => []
    },
    type: String,
    isManual: Boolean
  },
  computed: {
    /** 标题 */
    title ({ type }) {
      return TYPES_TITLE_MAP[type];
    },
    /** 标题背景色 */
    titleBgColor ({ type }) {
      return TYPES_TITLE_BG_COLOR_MAP[type];
    },
    /** 标题字体色 */
    titleFontsColor ({ type }) {
      return TYPES_TITLE_FONTS_COLOR_MAP[type];
    },
    /** 列表字体色 */
    listFontsColor ({ type }) {
      return TYPES_LIST_FONTS_COLOR_MAP[type];
    },
    /** 边框色 */
    borderColor ({ type }) {
      return TYPES_BORDER_COLOR_MAP[type];
    },
    sortList ({ list }) {
      return list.sort((a, b) => a.serialNumber - b.serialNumber)
    },
  },
}
</script>

<style scoped lang="scss">
.intelligent-tabs-list {
  height: 100%;

  &--title {
    height: 40px;
    font-size: 14px;
    font-weight: bold;
    line-height: 40px;
    padding-left: 10px;
    border: 1px solid;
  }

  &--container {
    height: calc(100% - 40px) !important;

    &-item {
      padding: 12px 10px;
      border: 1px solid;
      border-top: unset;
      display: flex;
      align-items: center;
    }
  }

  &--container-is-manual {
    height: calc(100% - 60px) !important;
  }
}
</style>
