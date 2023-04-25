<template>
  <div class="bjy-video-container" @click="setMain" v-loading="loading && !showButton" element-loading-text="正在加载画面...">
    <div ref="video" class="bjy-video"></div>
    <div class="video-cover" v-if="!videoOn || showButton">
      <el-button v-if="showButton" type="primary" size="mini" @click.stop="replay">播放</el-button>
      <div v-else class="camera-off">
        <span class="icon-camera-off"></span>
        <div class="tip">未开摄像头</div>
      </div>
    </div>
    <label class="bjy-user-id" :class="(videoOn && !showButton && isMain) ? 'has-video' : ''">
      <img align="center" @click="toggleCamera" class="video" :class="{'video_off':!videoOn}" :src=" videoOn?require('@/assets/video/2.png'):require('@/assets/video/video-off.png')" alt="">
      <span class="user-id">{{(isSelf || local) ? '自己' : (currentUserList[stream.getUserId()] ? currentUserList[stream.getUserId()].name : '')}}</span>
      <span @click="toggleMic" :class="'mic-status-icon ' + micStatusIcon" :data-screen="isScreen ? 1 : 0" :data-mic="audioOn ? 1 : 0"></span>
    </label>
    <!-- <label class="bjy-volume">{{volume}}</label> -->
    <!-- <Loading v-if="loading && !showButton" /> -->
    <div class="debug-pannel" v-show="stream && debug">
      <div class="item" v-show="debugSetting.videoResolution">videoResolution: {{stats.width || 0}}*{{stats.height || 0}}</div>
      <!-- <div class="item" v-show="debugSetting.fps">fps: {{stats.fps || 0}}</div> -->
      <div class="item" v-show="isSelf ? debugSetting.upLoss : debugSetting.downLoss">{{isSelf ? 'upLoss' : 'downLoss'}}: {{packetLoss.getPacketLossRate() || 0.00}} %</div>
      <div class="item" v-show="debugSetting.rtt">rtt: {{stats.rtt || 0}} ms</div>
      <div class="item" v-show="debugSetting.videoBitrate">videoBitrate: {{videoBitrate || 0}} kbps</div>
      <div class="item" v-show="debugSetting.audioBitrate">audioBitrate: {{audioBitrate || 0}} kbps</div>
      <div class="item" v-show="isSelf ? debugSetting.sendBytes : debugSetting.receivedBytes" >{{isSelf ? 'sendBytes' : 'receivedBytes'}}: {{(isSelf ? ((stats.videoBytesSent || 0) + (stats.audioBytesSent || 0)) : ((stats.videoBytesReceived || 0) + (stats.audioBytesReceived || 0))) || 0}} B</div>
    </div>
  </div>
</template>

<script>
import Loading from './Loading'
import PacketLost from '../util/PacketLoss'
import { mapState } from 'vuex'
export default {
  name: 'Video',

  props: {
    stream: {
      type: Object
    },
    display: {
      type: String,
      default: 'block'
    },
    local: {
      type: Boolean,
      default: false
    },
    isMain: {
      type: Boolean
    },
    joined: {
      type: Boolean,
      default: false
    },
  },

  data () {
    return {
      volume: 0,
      videoOn: this.stream && this.stream.getVideoOn(),
      audioOn: this.stream && this.stream.getAudioOn(),
      isSelf: this.stream && this.stream.isLocalStream() || !this.stream,
      showButton: false,
      loading: !this.local,
      isScreen: false,
      stats: {},
      videoBitrate: 0,
      audioBitrate: 0,
      packetLoss: new PacketLost()
    }
  },

  filters: {
    filterUserId: function (value) {
      if (value === '自己') {
        return value
      }
      value += ''
      return (value + '').substr(0, value.length - 1)
    }
  },

  computed: {
    ...mapState({
      speakerList: state => state.speakerList,
      speakerIndex: state => state.speakerIndex,
      speakerVolume: state => state.speakerVolume,
      videoBitrateVolume: state => state.videoBitrateVolume,
      debug: state => state.debug,
      debugSetting: state => state.debugSetting,
      currentUserList: state => state.currentUserList
    }),
    micStatusIcon() {
      let volume = this.volume;
      let micStatusIcon = 'mic-status-0';
      if (volume == 0) {
        micStatusIcon = 'mic-status-0';
      }
      else if (volume <= 20) {
        micStatusIcon = 'mic-status-1';
      }
      else if (volume <= 40) {
        micStatusIcon = 'mic-status-2';
      }
      else if (volume <= 60) {
        micStatusIcon = 'mic-status-3';
      }
      else {
        micStatusIcon = 'mic-status-4';
      }
      return micStatusIcon;
    }
  },

  watch: {
    stream: {
      handler: function (stream, oldStream) {
        if (oldStream) {
          oldStream.stop()
          this.onPlayerStateChange && oldStream.off('player-state-changed', this.onPlayerStateChange)
        }
        if (stream) {
          this.$nextTick(() => {
            
            this.onPlayerStateChange = (state) => {
              // 播放失败了需要添加一个按钮
              if (state.type === 'error' && state.reason && state.reason.name === 'NotAllowedError') {
                this.showButton = true
                  this.$message({
                  type: 'error',
                  message: '播放失败，请点击播放按钮手动播放'
                })
              }
              if (state.state === 'PLAYING') {
                this.loading = false
              }
            }

            stream.on('player-state-changed', this.onPlayerStateChange)

            if (!stream.isLocalStream() && this.speakerList[this.speakerIndex]) {
              stream.setAudioOutput(this.speakerList[this.speakerIndex].deviceId)
            }
            if (!stream.isLocalStream()) {
              stream.setAudioVolume(this.speakerVolume / 100)
            }
            stream.play(this.$refs.video).then(() => {
              console.log(`stream ${stream.getUserId()} ${stream.getStreamId()} play success`)
              if (this.isSelf && this.stream.isScreenStream()) {
                this.$emit('selfScreenFrame', this.stream.getVideoFrame())
              }
              this.loading = false
              this.isScreen = stream.isScreenStream() || /1$/.test(stream.getUserId() + '')
            })

          })
          this.videoOn = this.stream.getVideoOn()
          this.audioOn = this.stream.getAudioOn()
          this.isSelf = this.stream.isLocalStream()
        }
      },
      immediate: true
    }
  },

  methods: {
    // 麦克风操作
    toggleMic () {
      if (this.joined) {
        this.$store.commit('audioOn', {
          audioOn: !this.audioOn
        });
      }
      else {
        this.$message({
          type: 'error',
          message: '未加入频道'
        })
      }
    },
    toggleCamera: function () {
      if (this.videoOn) {
        this.stream.muteVideo()
        this.videoOn = false
      } else {
        this.stream.unmuteVideo()
        this.videoOn = true
      }
    },
    toggleMic: function () {
      if (this.audioOn) {
        this.stream.muteAudio()
        this.audioOn = false
      } else {
        this.stream.unmuteAudio()
        this.audioOn = true
      }
    },
    updateMediaStatus: function () {
      if (this.stream) {
        this.videoOn = this.stream.getVideoOn()
        this.audioOn = this.stream.getAudioOn()
      }
    },
    replay: function () {
      if (this.stream) {
        this.stream.resume()
        if (this.isSelf && this.stream.isScreenStream()) {
          this.$emit('selfScreenFrame', this.stream.getVideoFrame())
        }
        this.showButton = false
        this.loading = false
      }
    },

    setMain: function () {
      this.$emit('setMain')
    }
  },

  mounted: function () {
    this.timer = setInterval(() => {
      if (this.stream) {
        this.volume = Math.floor(this.stream.getAudioLevel() * 100)
      }
    }, 1000)
    this.debugTimer = setInterval(() => {
      if (this.stream) {
        this.stream.getStats().then((stats) => {
          if (this.stream.isLocalStream()) {
            stats.width = stats.width || this.stats.width
            stats.height = stats.height || this.stats.height
            this.videoBitrate = ((stats.videoBytesSent - this.stats.videoBytesSent || 0) * 4 / 1000).toFixed(2)
            this.audioBitrate = ((stats.audioBytesSent - this.stats.audioBytesSent || 0) * 4 / 1000).toFixed(2)
            this.packetLoss.addData(stats.videoPacketsSent, stats.videoPacketsLost)
          }
          else {
            this.videoBitrate = ((stats.videoBytesReceived - this.stats.videoBytesReceived || 0) * 4 / 1000).toFixed(2)
            this.audioBitrate = ((stats.audioBytesReceived - this.stats.audioBytesReceived || 0) * 4 / 1000).toFixed(2)
            this.packetLoss.addData(stats.videoPacketsReceived, stats.videoPacketsLost)
          }
          this.stats = stats
          this.$store.commit('videoBitrateVolume', {
            videoBitrateVolume: this.videoBitrate
          })
        })
      }
    }, 2000)
  },

  beforeDestroy: function () {
    if (this.timer) {
      clearInterval(this.timer)
      this.timer = null
    }
    if (this.debugTimer) {
      clearInterval(this.debugTimer)
      this.debugTimer = null
    }
    if (this.stream) {
      this.stream.stop()
      this.onPlayerStateChange && this.stream.off('player-state-changed', this.onPlayerStateChange)
    }
  },

  components: {
    Loading
  }
}
</script>

<style lang="less">
  .bjy-video-container {
    width: 100%;
    height: 100%;
    background-color: #000;
    display: inline-block;
    position: relative;

    & /deep/ .el-loading-mask {
      z-index: 0;
    }
  }
  .video-cover {
    position: absolute;
    top: 0;
    width: 100%;
    height: 100%;
    background-color: #f1f3f9;
    font-size: 20px;
    color: rgba(184,198,204,0.8);
    text-align: center;
    line-height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 8px;

    .camera-off {
      text-align: center;
      .icon-camera-off {
        display: inline-block;
        width: 192px;
        height: 192px;
        background: url(../assets/sprites/camera-off.png);
        background-image: -webkit-image-set(
          url(../assets/sprites/camera-off@2x.png) 2x
        );
        background-size: contain;
      }
      .tip {
        text-align: center;
      }
    }
  }
  .bjy-switch {
    position: absolute;
    top: 10px;
    font-size: 12px;

    .bjy-device-mic,
    .bjy-device-camera {
      text-align: center;
      display: block;
      color: #fff;
      cursor: pointer;
      background-color: rgba(70,70,70,0.8);
      border-radius: 5px;
      padding: 3px 6px;
      font-size: 12px;
    }
    .bjy-device-mic {
      margin-top: 10px;
    }
  }
  .bjy-video {
    width: 100%;
    height: 100%;

    video {
      left: 0;
    }
  }
  .bjy-user-id {
    position: absolute;
    left: 1px;
    bottom: 0;
    width: 100%;
    height: 24px;
    color: #fff;
    padding: 3px 13px 3px 0px;
    background-color: #202020;
    opacity: 0.5;
    border-radius: 0 0px 8px 8px;
    font-size: 14px;
    .video{
      float: left;
      margin-left: 10px;
      margin-top: 5px;
      // width: 20px;
    }
    .video_off{
      width: 19px;
      margin-top: -2px;
    }
    .mic-status-icon {
      position: absolute;
      right: 0;
      bottom: 1px;
      width: 24px;
      height: 24px;
      vertical-align: middle;
      background: url(../assets/sprites/audio-icon-sprites@2x.png) no-repeat;
      background-size: 144px 24px;
      background-position: -24px 0;
      &.mic-status-0 {
        background-position-x: -24px;
      }
      &.mic-status-1 {
        background-position-x: -48px;
        background-position: unset;
        background: url('@/assets/video/3.png');
        background-size: 100%  100%;
        width: 11px;
        height: 17px;
        right: 5px;
        bottom: 5px;
      }
      &.mic-status-2 {
        background-position-x: -72px;
      }
      &.mic-status-3 {
        background-position-x: -96px;
      }
      &.mic-status-4 {
        background-position-x: -120px;
      }
      &[data-mic="0"] {
        background-position-x: 0px;
      }
      &[data-screen="1"] {
        background-position-x: 0px;
      }
    }
    .user-id {
      vertical-align: middle;
      float: left;
      margin-left: 10px;
    }
  }
  .has-video {
      width: 100%;
      background: #fff;
      color: #000;
      background: rgb(0 0 0 / 50%);
      color: #fff;
      margin-bottom: 4px;
      border-radius: 0px 0px 8px 8px;
      width: 100%;
      background: rgba(0,0,0,0.9);
      .mic-status-icon {
        background: url(../assets/sprites/mic-white-sprites@2x.png) no-repeat;
        background-size: 144px 24px;
        background-position: -24px 0;
      }
    }
  .bjy-volume {
    position: absolute;
    right: 0;
    bottom: 0;
    padding: 7px 13px;
    color: #fff;
    background-color: rgba(70,70,70,0.8);
    border-radius: 5px 0 0 5px;
    font-size: 12px;
  }
  .debug-pannel {
    position: absolute;
    top: 0;
    opacity: 0.5;
    background: #000;
    width: 194px;
    color: #fff;
    max-height: 85px;
    overflow: auto;
    padding: 10px;
    text-align: left;
    font-size: 12px;
    padding-top: 4px;
    left: 2px;
    border-radius: 8px 8px 0px 0px;
  }
</style>
