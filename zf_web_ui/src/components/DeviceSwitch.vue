<template>
  <div class="cmp-device-switch" v-outside="closeList">
    <div class="device-slider">
      <slot name="slider"></slot>
    </div>

    <!-- <el-tooltip class="item" effect="light" :content="label" placement="top"> -->
      <div class='switch-icon' @click='clickFn'>
        <span :class="`bottom-icon ${status ? 'open' : 'close'} ${className}`" :data-disable="disable ? 1 : 0"></span>
      </div>
    <!-- </el-tooltip> -->
    <div v-if="open" class="device-list">
      <!-- <div class="select-tip">选择{{selectLabel}}</div> -->
      <div v-for="(item, i) in list" :key="item.deviceId" class="list-item" :class="i === index ? 'active' : ''" @click="change(i)">
        <span v-if="i === index" class="item-select"></span>
        {{item.name}}
      </div>
    </div>
    <span v-if="list.length" class="trigger-icon" :data-open="open ? 1 : 0" @click="toggleOpen"></span>
    <!-- <span class="icon-text">{{label}}</span> -->
  </div>
</template>

<script>

export default {
  name: 'DeviceSwitch',
  data () {
    return {
      open: false
    }
  },
  props: {
    status: Boolean,
    label: String,
    className: String,
    selectLabel: String,
    list: {
      type: Array,
      default: function () {
        return []
      }
    },
    index: {
      type: Number,
      default: 0
    },
    disable: Boolean
  },
  methods: {
    clickFn () {
      // console.log('clickFn')
      if (this.disable) {
        return false
      }
      this.$emit('btnclick')
    },
    toggleOpen () {
      this.open = !this.open
    },
    closeList () {
      this.open = false
    },
    change (index) {
      this.$emit('change', index)
    }
  },
  mounted () {

  },
  components: {

  }
}
</script>
<style scoped lang="less">
    .cmp-device-switch{
      position: relative;
      width: 40px;
      height: 40px;
      cursor: pointer;
      margin-right: 15px;

      .device-slider {
        position: absolute;
        bottom: 60px;
        margin-left: 4px;
      }

      .switch-icon{
        width: 40px;
        height: 40px;
        .bottom-icon {
          display: inline-block;
          width: 40px;
          height: 40px;
          background: url(../assets/sprites/bottom-icon-sprites@2x.png) no-repeat;
          background-position: 0 0;
          background-size: 500px 80px;
          cursor: pointer;
          &.mic-switch {
            background-position: 0 0;
            &:hover {
              background-position-y: -40px;
            }
            &.close {
              background-position-x: -40px;
            }
          }
          &.video-switch {
            background-position: -80px 0;
            &:hover {
              background-position-y: -40px;
            }
            &.close {
              background-position-x: -120px;
            }
          }
          &.audio-switch {
            background-position: -160px 0;
            &:hover {
              background-position-y: -40px;
            }
            &.close {
              background-position-x: -200px;
            }
          }
          &.white-broad-switch {
            background-position: -240px 0;
            &.open {
              background-position: -240px 0;
            }
            &.close {
              background-position: -460px 0;
            }
            &:hover {
              background-position-y: -40px;
            }
            &.new-message-tip::after {
              content: '';
              display: inline-block;
              width: 8px;
              height: 8px;
              border-radius: 50%;
              background-color: red;
              position: absolute;
              top: 6px;
              right: 18px;
            }
          }
          &.screen-share-switch {
            background-position: -280px 0;
            &:hover {
              background-position-y: -40px;
            }
          }
          &.chat, &.user-list {
            background: url(../assets/sprites/icon-user-chat.png) no-repeat;
            background-image: -webkit-image-set(url(../assets/sprites/icon-user-chat@2x.png) 2x);
            background-position: 0px 0;
            &:hover {
              background-position-y: -40px;
            }
          }
          &.new-message {
            background: url(../assets/sprites/icon-new-message.png) no-repeat;
            background-image: -webkit-image-set(url(../assets/sprites/icon-new-message@2x.png) 2x);
            background-position: 0 0;
          }
          &.user-list {
            background-position: -40px 0;
          }
          &.top-gallery {
            background: url(../assets/sprites/icon-switch-layout.png) no-repeat;
            background-image: -webkit-image-set(url(../assets/sprites/icon-switch-layout@2x.png) 2x);
            background-position: -40px 0;
            &:hover {
              background-position-y: -40px;
            }
            &.close {
              background-position: 0 0;
              &:hover {
                background-position-y: -40px;
              }
            }
          }
          &.setting {
            background-position: -400px 0;
            &:hover {
              background-position-y: -40px;
            }
          }
          &[data-disable="1"] {
            opacity: 0.5;
            cursor: not-allowed;
            &:hover {
              background-position-y: 0px;
            }
          }
        }
      }
      .switch-label{
        color: #fff;
        font-size: 12px;
      }

      .device-list {
        position: absolute;
        bottom: 54px;
        color: #000;
        width: 198px;
        left: 36px;
        z-index: 1;
        background: rgba(255,255,255,0.8);
        border-radius: 8px;
        box-shadow: 0px 5px 10px 0px rgba(5,6,53,0.1);

        .select-tip {
          font-size: 12px;
          padding: 6px 14px;
          text-align: left;
        }

        .list-item {
          font-size: 12px;
          padding: 7px 7px 7px 22px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          text-align: left;
          position: relative;

          &:hover {
            background: #eeeeee;
          }

          .item-select {
            display: inline-block;
            width: 4px;
            height: 4px;
            border-radius: 50%;
            background: #1795ff;
            position: absolute;
            top: 12px;
            left: 10px;
          }
        }
      }
      
      .trigger-icon {
        display: inline-block;
        width: 20px;
        height: 20px;
        position: absolute;
        left: 40px;
        top: 10px;
        background: url(../assets/sprites/bottom-icon-sprites@2x.png) no-repeat;
        background-size: 500px 80px;
        background-position:  -440px 0;
        &[data-open="1"] {
          transform: rotateX(180deg);
        }
        &:hover {
          background-position-y: -20px;
        }
      }

      .icon-text {
        display: inline-block;
        width: 48px;
        position: absolute;
        left: -4px;
        bottom: 4px;
        text-align: center;
        font-size: 12px;
        color: #fff;
      }
    }
</style>

<style lang="less">
.cmp-device-switch {
  .device-slider {
    .el-slider.is-vertical {
      width: 32px;
      height: 130px;
      padding-top: 18px;
      background: rgba(255,255,255,0.8);
      border-radius: 8px;
      box-shadow: 0px 5px 10px 0px rgba(5,6,53,0.1);
      -webkit-animation: fadelogIn .2s;
      animation: fadelogIn .2s;
      .el-slider__runway {
        margin: 0 13px;
      }
    }
  }
}
@keyframes fadelogIn {
  0% {
    opacity: 0;
    transform: scale(1);
  }
  50% {
    opacity: 0.5;
    transform: scale(1.1);
  }
  100% {
    opacity: 1;
    transform: scale(1);
  }
}

@-webkit-keyframes fadelogIn {
  0% {
    opacity: 0;
    transform: scale(1);
  }
  50% {
    opacity: 0.5;
    transform: scale(1.1);
  }
  100% {
    opacity: 1;
    transform: scale(1);
  }
}
</style>
