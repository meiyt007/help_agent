<template>
  <div class="cmp-side-mask-top">
    <div class="top-left">
      <span :class="'network-status-icon ' + netWorkStatus"></span>
      <TimeLabel></TimeLabel>
    </div>
    <!-- <div class="top-center">
      <span class="room-number">房间号 {{$store.state.roomId}}</span>
    </div> -->

    <!-- <div class="top-right">
      <div class="top-gallery" :data-gallery="isGallery ? 1 : 0" @click="toggleGallery"></div>
      <div class="top-fullscreen" :data-fullscreen="fullScreen ? 1 : 0" @click="toggleFullscreen"></div>
    </div> -->
  </div>
</template>

<script>
import TimeLabel from '@/components/TimeLabel.vue'
export default {
  name: 'SideMaskTop',

  props: {
    isGallery: {
      type: Boolean,
      require: true
    }
  },

  data () {
    return {
      fullScreen: !!(document.isFullScreen || document.webkitIsFullScreen || document.mozIsFullScreen),
      videoBitrateVolume: this.$store.state.videoBitrateVolume
    }
  },
  computed: {
    netWorkStatus() {
      let videoBitrate = +this.$store.state.videoBitrateVolume;
      let netWorkStatus = 'network-status-0';
      if (videoBitrate == 0) {
        netWorkStatus = 'network-status-0';
      }
      else if (videoBitrate <= 30) {
        netWorkStatus = 'network-status-1';
      }
      else if (videoBitrate <= 70) {
        netWorkStatus = 'network-status-2';
      }
      else if (videoBitrate <= 150) {
        netWorkStatus = 'network-status-3';
      }
      else {
        netWorkStatus = 'network-status-4';
      }
      return netWorkStatus;
    }
  },
  methods: {
    fullscreenFn () {
      console.log('fullscreenFn')
      const el = document.documentElement
      const rfs = el.requestFullScreen || el.webkitRequestFullScreen || el.mozRequestFullScreen || el.msRequestFullScreen
      if (rfs) {
        rfs.call(el)
        this.fullScreen = true
      }
    },
    exitFullscreenFn () {
      const el = document
      const cfs = el.cancelFullScreen || el.webkitCancelFullScreen || el.mozCancelFullScreen || el.exitFullScreen
      if (cfs) {
        cfs.call(el)
        this.fullScreen = false
      }
    },

    toggleFullscreen () {
      if (this.fullScreen) {
        this.exitFullscreenFn()
      }
      else {
        this.fullscreenFn()
      }
    },

    toggleGallery () {
      this.$emit('toggleGallery')
    }
  },
  mounted () {
  },
  components: {
    TimeLabel
  }
}
</script>
<style scoped lang="less">
    .cmp-side-mask-top {
      background: #fff;
      position: absolute;
      width: 300px;
      height: 28px;
      top: 0;
      right: 0;
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      padding: 0 12px 0 25px;
      font-size: 14px;
      font-family: PingFangSC, PingFangSC-Regular;
      font-weight: 400;
      color: #000000;
      align-items: center;

      .top-left {
        display: flex;
        flex-direction: row;
        align-items: center;
        color: #000;
        width: 300px;
        align-items: center;
        .network-status-icon {
          display: inline-block;
          width: 14px;
          height: 14px;
          margin-right: 10px;
          background: url('../assets/sprites/top-icon-sprites@2x.png') no-repeat;
          background-size: 170px 32px;
          background-position: -80px 0;
          margin-top: -2px;
          &.network-status-0 {
            background-position-x: -80px;
          }
          &.network-status-1 {
            background-position-x: -98px;
          }
          &.network-status-2 {
            background-position-x: -118px;
          }
          &.network-status-3 {
            background-position-x: -136px;
          }
          &.network-status-4 {
            background-position-x: -154px;
          }
        }
      }

      .top-center {
        display: flex;
        flex-direction: row;
        align-items: center;
        color: #000;
        width: 300px;
      }

      .top-fullscreen {
        cursor: pointer;
        display: inline-block;
        width: 16px;
        height: 16px;
        background: url(../assets/sprites/top-icon-sprites@2x.png);
        background-size: 170px 32px;
        background-position: 0 0;
        &:hover {
          background-position-y: -16px;
        }
        &[data-fullscreen="1"] {
          background-position: -16px 0;
          &:hover {
            background-position-y: -16px;
          }
        }

      }

      .top-gallery {
        width: 40px;
        height: 40px;
        margin-right: 20px;
        cursor: pointer;
        display: inline-block;
        background: url(../assets/components/view_icon_gallery.png);
        background-image: -webkit-image-set(
          url(../assets/components/view_icon_gallery@2x.png) 2x
        );
        &[data-gallery="1"] {
          background: url(../assets/components/view_icon_list.png);
          background-image: -webkit-image-set(
            url(../assets/components/view_icon_list@2x.png) 2x
          );
        }
      }
    }
</style>
