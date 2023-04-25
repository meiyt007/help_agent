<template>
  <div class="document-tools">
    <el-tooltip class="item" effect="light" content="第一页" placement="top">
      <div class="item first-page" :data-disable="currentPage === 1 ? 1 : 0" @click="changePage(0)"></div>
    </el-tooltip>
    <el-tooltip class="item" effect="light" content="上一步" placement="top">
      <div class="item prev" :data-disable="canPrev ? 0 : 1" @click="prev()"></div>
    </el-tooltip>
    <div class="item page-state">
      <span class="current-page">{{currentPage}}</span>
      <span> / </span>
      <span class="total-page">{{totalPage}}</span>
    </div>
    <el-tooltip class="item" effect="light" content="下一步" placement="top">
      <div class="item next" :data-disable="canNext ? 0 : 1" @click="next()"></div>
    </el-tooltip>
    <el-tooltip class="item" effect="light" content="最后一页" placement="top">
      <div class="item last-page" :data-disable="currentPage === totalPage ? 1 : 0" @click="changePage(totalPage - 1)"></div>
    </el-tooltip>
    <el-tooltip class="item" effect="light" content="操作文档" placement="top">
      <div class="item operate-ppt" @click="toggleOperatePPT" v-show="canOperatePPT" :class="operatePPT ? 'active' : ''"></div>
    </el-tooltip>
    <el-tooltip class="item" effect="light" content="添加白板" placement="top">
      <div class="item page-add" :class="{'hide-page-operate': isShowOperatePage}" :data-disable="canAddPage ? 0 : 1" @click="addPage()"></div>
    </el-tooltip>
    <el-tooltip class="item" effect="light" content="删除当前白板" placement="top">
      <div class="item page-delete" :class="{'hide-page-operate': isShowOperatePage}" :data-disable="canDeletePage ? 0 : 1" @click="deletePage()"></div>
    </el-tooltip>
  </div>
</template>

<script>

import { mapState } from 'vuex'
import debounce from '../util/debounce'

export default {
  name: 'DocumentTools',
  data () {
    return {
      operatePPT: false
    }
  },
  props: {
    currentPage: Number,
    totalPage: Number,
    canPrev: Boolean,
    canNext: Boolean,
    canAddPage: Boolean,
    isShowOperatePage: Boolean,
    canDeletePage: Boolean,
    canOperatePPT: Boolean
  },
  computed: {
    
  },
  watch: {
    
  },
  methods: {
    prev() {
      this.$emit('prev')
    },
    next() {
      this.$emit('next')
    },
    changePage(page) {
      this.$emit('changePage', page)
    },
    addPage: debounce(function () {
      if (this.canAddPage) {
        this.$emit('addPage')
      }
    }, 1000, true),
    deletePage() {
      if (this.canDeletePage) {
        this.$emit('deletePage', this.currentPage - 1)
      }
    },
    toggleOperatePPT() {
      this.operatePPT = !this.operatePPT
      this.$emit('operatePPT', this.operatePPT)
    }
  },
  mounted () {
    
  },
  components: {
  },

  boforeDestroy () {
    
  }
}
</script>
<style scoped lang="less">
  .document-tools {
    height: 56px;
    padding: 10px 30px;
    background: rgba(255,255,255,0.6);
    border-radius: 8px;
    box-shadow: 0px 5px 10px 0px rgba(5,6,53,0.1); 
    user-select: none;
    transform: scale(0.7);
    .item {
      display: inline-block;
      width: 36px;
      height: 36px;

      background: url(../assets/tools/doc-tool.png);
      background-image: -webkit-image-set(
        url(../assets/tools/doc-tool@2x.png) 2x
      );
      cursor: pointer;

      &[data-disable="1"] {
        opacity: 0.5;
        cursor: not-allowed;
      }

      &.item {
        margin-left: 20px;
      }

      &.first-page {
        background-position: 0 0;
        margin-left: 0px;
        &:hover {
          background-position: 0 -36px;
        }
      }
      &.prev {
        background-position: -36px 0;
        &:hover {
          background-position: -36px -36px;
        }
      }
      &.next {
        background-position: -72px 0;
        &:hover {
          background-position: -72px -36px;
        }
      }
      &.last-page {
        background-position: -108px 0;
        &:hover {
          background-position: -108px -36px;
        }
      }
      &.page-add {
        background-position: -144px 0;
        &:hover {
          background-position: -144px -36px;
        }
      }
      &.page-delete {
        background-position: -180px 0;
        &:hover {
          background-position: -180px -36px;
        }
      }
      &.hide-page-operate {
        display: none;
      }
      &.page-state {
        background: none;
        vertical-align: top;
        line-height: 36px;
        width: 52px;
        text-align: center;
      }

      &.operate-ppt {
        background: url(../assets/tools/operate-ppt.png);
        background-image: -webkit-image-set(
          url(../assets/tools/operate-ppt@2x.png) 2x
        );
        background-position: 0 0;
        &.active {
          background-position: 0 -36px;
        }
      }
    }
  }
</style>
