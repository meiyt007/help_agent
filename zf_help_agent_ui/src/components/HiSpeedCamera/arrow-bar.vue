<template>
  <div class="arrow-bar">
    <div class="arrow-bar-left">
      <span>查看示范</span>
    </div>
    <!-- 样表 -->
    <div class="arrow-bar-right">
      <div class="arrow-left" @click="arrowLeft">
        <i class="el-submenu__icon-arrow el-icon-arrow-left"></i>
      </div>
      <!-- 中间滚动区域 -->
      <div class="arrow-list" ref="arrowListParent">
        <div
          ref="arrowList"
          class="arrow-list-content"
          :style="{ width: arrowListWidth }"
        >
          <div
            v-for="(item, idx) in arrowList"
            :key="idx"
            :class="[
              'arrow-list-item',
              item.canPreview ? 'arrow-list-item--preivew' : '',
              item.arrowItemPopover ? 'arrow-list-item--active' : '',
            ]"
          >
            <!-- checked -->
            <div v-if="item.canPreview" class="arrow-list-item--check">
              <i class="el-icon-check" />
            </div>
            <el-popover
              v-model="item.arrowItemPopover"
              placement="bottom-start"
              trigger="click"
              :visible-arrow="false"
              popper-class="arrow-list-item--popover"
            >
              <div class="content">
                <div class="close" @click="item.arrowItemPopover = false">
                  <img
                    src="@/assets/image/close-bg.png"
                    alt=""
                    width="34"
                    height="34"
                  />
                  <i class="el-icon-close"></i>
                </div>
                <div class="preview-content">
                  <div
                    class="preview"
                    v-for="(child, index) in item.srcList"
                    :key="index"
                  >
                    <el-image
                      style="width: 255px; height: 350px"
                      fit="contain"
                      :src="child.src"
                      :preview-src-list="[child.src]"
                      alt="图片加载失败"
                      title="点击预览"
                    />
                    <div class="arrow-list-preview--title">
                      {{ child.imgName }}
                    </div>
                  </div>
                </div>
              </div>
              <el-tooltip
                effect="dark"
                :content="item.title"
                :disabled="item.isShowTooltip"
                :open-delay="500"
                placement="top"
                slot="reference"
                class="arrow-list-item--title"
              >
                <span>
                  {{ item.title }}
                </span>
              </el-tooltip>
            </el-popover>
          </div>
        </div>
      </div>
      <div class="arrow-right" @click="arrowRight">
        <i class="el-submenu__icon-arrow el-icon-arrow-right"></i>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ArrowBar',
  props: {
    arrowList: Array,
  },
  computed: {
    arrowListItemWidth () {
      const dom = document.querySelector('.arrow-list-item');
      return dom ? (dom.offsetWidth + 10) : 130;
    },

    arrowListWidth () {
      return this.arrowList.length * this.arrowListItemWidth + 'px';
    },

  },
  methods: {
    arrowLeft () {
      if (this.$refs.arrowListParent.offsetWidth > this.$refs.arrowList.offsetWidth) return;
      let translateX = this.calcArrowList();
      if (translateX + this.arrowListItemWidth > 0) return;
      // 滚动一屏
      this.$refs.arrowList.style.transform = `translateX(${translateX + this.arrowListItemWidth * 7}px)`;
    },

    arrowRight () {
      if (this.$refs.arrowListParent.offsetWidth > this.$refs.arrowList.offsetWidth) return;
      let translateX = this.calcArrowList();
      if (this.$refs.arrowList.offsetWidth + translateX < this.$refs.arrowListParent.offsetWidth) return;
      // 滚动一屏
      this.$refs.arrowList.style.transform = `translateX(${translateX + (-this.arrowListItemWidth) * 7}px)`;
    },

    calcArrowList () {
      const transform = getComputedStyle(this.$refs.arrowList, null)['transform'];
      let translateX = 0;
      if (transform !== 'none') {
        translateX = parseInt(transform.split(',')[4]);
      }
      return translateX;
    },
  }
}
</script>

<style scoped lang="scss">
@import '@/assets/styles/mixin/index.scss';
// 样表
.arrow-bar {
  width: 100%;
  height: 48px;
  background: #f2f4f8;
  border: 1px dashed #b1b7bb;
  box-shadow: 0px 3px 13px 0px rgba(45, 63, 116, 0.28);
  border-radius: 1px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 30px;

  &-left {
    width: 110px;
    height: 100%;
    background: rgba(219, 226, 237, 0.5);
    border-radius: 5px 0px 0px 5px;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  &-right {
    display: flex;
    justify-content: space-between;
    height: 100%;
    width: calc(100% - 110px);
    .arrow-left,
    .arrow-right {
      width: 25px;
      background: #ced7ea;
      border-radius: 3px 0px 0px 3px;
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;

      .el-submenu__icon-arrow {
        right: unset;
        font-size: 18px;
        color: #fff;
        top: unset;
        margin-top: unset;
        position: unset;

        &:hover {
          color: #3b8ff7;
        }
      }
    }

    .arrow-list {
      flex: 1;
      padding: 0px 10px;
      overflow: hidden;

      &-content {
        display: flex;
        align-items: center;
        height: 100%;
        overflow: hidden;
        transition: transform 0.15s ease 0s; // 如果发现位置不对 要么设置过渡时间减少 要么加入防抖函数
      }

      &-item {
        min-width: 120px;
        height: 36px;
        background: #486281;
        border-radius: 5px;
        font-size: 14px;
        font-weight: 400;
        line-height: 36px;
        text-align: center;
        color: #ffffff;
        margin-right: 10px;
        padding: 0px 15px;
        cursor: pointer;
        position: relative;

        &--check {
          position: absolute;
          right: 0;
          top: 0;
          width: 18px;
          height: 15px;
          background: #19abff;
          border-radius: 0 0 0 5px;

          i {
            position: absolute;
            top: 2px;
            left: 3px;
            font-size: 12px;
          }
        }

        &--active {
          background-color: rgba(34, 119, 222, 0.8) !important;
        }

        &--preivew {
          background-color: rgba(34, 119, 222, 1);
        }

        &--title {
          display: inline-block;
          width: 100%;
          height: 100%;
          @include overflow-ellipsis();
        }
      }
    }
  }
}
</style>
