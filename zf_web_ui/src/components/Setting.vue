
<template>
  <div class="setting-container">
    <div class="setting-header">
      {{ subMenuOpend ? (subMenuKey === 'debug' ? '参数设置' : '版本号') : '设置'}}
      <span class="setting-close" @click="close">×</span>
      <span v-show="subMenuOpend" class="setting-pre" @click="preMenu"></span>
    </div>
    <div class="setting-body">
      <div class="setting-options menu" v-show="!subMenuOpend">
        <div class="setting-option">
          <Checkbox class="setting-checkbox" v-model="debug" >调试模式</Checkbox>
        </div>
        <div class="setting-option setting-option-debug" @click="subMenuNext('debug')">
          调试模式参数设置
          <span class="setting-next"></span>
        </div>
        <div class="setting-option setting-option-debug" @click="subMenuNext('version')">
          版本号
          <span class="setting-next"></span>
        </div>
      </div>
      <div class="setting-options submenu-debug" v-show="subMenuOpend && subMenuKey === 'debug'">
        <div class="setting-option">
          <div class="left">
            <span class="title">videoResolution</span>
            <span class="sub-title">视频分辨率</span>
          </div>
          <el-switch
            v-model="debugSetting.videoResolution"
            active-color="#6297FC"
            inactive-color="#DCDFE6">
          </el-switch>
        </div>
        <!-- <div class="setting-option">
          <div class="left">
            <span class="title">FPS</span>
            <span class="sub-title">视频帧率（fps）</span>
          </div>
          <el-switch
            v-model="debugSetting.fps"
            active-color="#6297FC"
            inactive-color="#DCDFE6">
          </el-switch>
        </div> -->
        <div class="setting-option">
          <div class="left">
            <span class="title">upLoss</span>
            <span class="sub-title">上行丢包率</span>
          </div>
          <el-switch
            v-model="debugSetting.upLoss"
            active-color="#6297FC"
            inactive-color="#DCDFE6">
          </el-switch>
        </div>
        <div class="setting-option">
          <div class="left">
            <span class="title">downLoss</span>
            <span class="sub-title">下行丢包率</span>
          </div>
          <el-switch
            v-model="debugSetting.downLoss"
            active-color="#6297FC"
            inactive-color="#DCDFE6">
          </el-switch>
        </div>
        <div class="setting-option">
          <div class="left">
            <span class="title">rtt</span>
            <span class="sub-title">延时（ms）</span>
          </div>
          <el-switch
            v-model="debugSetting.rtt"
            active-color="#6297FC"
            inactive-color="#DCDFE6">
          </el-switch>
        </div>
        <div class="setting-option">
          <div class="left">
            <span class="title">videoBitrate</span>
            <span class="sub-title">视频发送码率（kbps）</span>
          </div>
          <el-switch
            v-model="debugSetting.videoBitrate"
            active-color="#6297FC"
            inactive-color="#DCDFE6">
          </el-switch>
        </div>
        <div class="setting-option">
          <div class="left">
            <span class="title">audioBitrate</span>
            <span class="sub-title">音频发送码率（kbps）</span>
          </div>
          <el-switch
            v-model="debugSetting.audioBitrate"
            active-color="#6297FC"
            inactive-color="#DCDFE6">
          </el-switch>
        </div>
        <div class="setting-option">
          <div class="left">
            <span class="title">receivedBytes</span>
            <span class="sub-title">总接收字节数</span>
          </div>
          <el-switch
            v-model="debugSetting.receivedBytes"
            active-color="#6297FC"
            inactive-color="#DCDFE6">
          </el-switch>
        </div>
        <div class="setting-option">
          <div class="left">
            <span class="title">sendBytes</span>
            <span class="sub-title">总发送字节数</span>
          </div>
          <el-switch
            v-model="debugSetting.sendBytes"
            active-color="#6297FC"
            inactive-color="#DCDFE6">
          </el-switch>
        </div>
      </div>
      <div class="setting-options submenu-version" v-show="subMenuOpend && subMenuKey === 'version'">
        <div class="icon">
          <span class="icon-brtc"></span>
        </div>
        <div class="setting-option">
          <span class="title">BRTC SDK版本</span>
          <span class="version">{{ brtcVersion }}</span>
        </div>
        <div class="setting-option">
          <span class="title">BRTM SDK版本</span>
          <span class="version">{{ brtmVersion }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import { Checkbox } from 'element-ui'
export default {
  name: 'Setting',
  data () {
    return {
      subMenuOpend: false,
      subMenuKey: '',
      brtcVersion: BRTC.version,
      brtmVersion: BRTM.version
    }
  },
  computed: {
    ...mapState({
      debugSetting: state => state.debugSetting
    }),
    debug: {
      get: function () {
        return this.$store.state.debug
      },
      set: function (value) {
        this.$store.commit('debug', {
          debug: value
        })
      }
    },
    debugSetting: {
      get: function () {
        return this.$store.state.debugSetting
      },
      set: function (value) {
        this.$store.commit('debugSetting', {
          debug: value
        })
      }
    }
  },
  methods: {
    close: function () {
      this.$emit('close')
    },
    preMenu: function () {
      this.subMenuOpend = false
    },
    subMenuNext: function (key) {
      this.subMenuOpend = true
      this.subMenuKey = key
    }
  },
  mounted () {
  },
  components: {
    Checkbox
  }
}
</script>
<style scoped lang="less">
  .setting-container {
    position: relative;
    height: 100%;
    .setting-header {
      position: relative;
      height: 48px;
      font-size: 16px;
      font-weight: 500;
      line-height: 48px;
      border-bottom: 1px solid #dcdfe6;
      padding-left: 40px;

      .setting-close {
        position: absolute;
        right: 17px;
        font-size: 24px;
        cursor: pointer;
      }
      .setting-pre {
        position: absolute;
        display: inline-block;
        width: 28px;
        height: 24px;
        left: 8px;
        top: 12px;
        background: url('../assets/components/setting/setting-back.png');
        background-image: -webkit-image-set(
          url('../assets/components/setting/setting-back@2x.png') 2x
        );
        cursor: pointer;
      }
    }

    .setting-body {
      overflow: auto;
      position: absolute;
      top: 48px;
      bottom: 0;
      width: 100%;
      .setting-option {
        position: relative;
        height: 56px;
        line-height: 56px;
        padding-left: 18px;
        text-align: left;
        border-bottom: 1px solid #dcdfe6;
        color: #333333;
        font-weight: 400;

        .setting-next {
          position: absolute;
          display: inline-block;
          width: 9px;
          height: 16px;
          right: 17px;
          top: 20px;
          background: url('../assets/components/setting/setting-next.png');
          background-image: -webkit-image-set(
            url('../assets/components/setting/setting-next@2x.png') 2x
          );
          cursor: pointer;
        }
        .left {
          display: inline-block;
          .sub-title,
          .title {
            display: block;
            line-height: 22px;
          }
          .title {
            margin-top: 6px;
          }
        }
        .el-switch {
          position: absolute;
          right: 17px;
          top: 18px;
        }
      }
      .submenu-version {
        .icon {
          text-align: center;
          margin: 10px 0 -10px 0;
          .icon-brtc {
            display: inline-block;
            width: 80px;
            height: 83px;
            background: url('../assets/sprites/icon-brtc.png');
            background-image: -webkit-image-set(
              url('../assets/sprites/icon-brtc@2x.png') 2x
            );
          }
        }
        .setting-option {
          margin: 0 20px;
          overflow: hidden;
          border-bottom: none;
          & + .setting-option {
            border-top: 1px solid #eee;
          }
          .title {
            font-size: 16px;
            font-family: PingFangSC, PingFangSC-Regular;
            font-weight: 400;
            float: left;
            color: #301d1d;
            margin-left: -16px;
          }
          .version {
            font-size: 14px;
            font-family: PingFangSC, PingFangSC-Regular;
            font-weight: 400;
            float: right;
            color: #666666;
          }
        }
      }
      .setting-option-debug {
        cursor: pointer;
      }
    }
  }
</style>
