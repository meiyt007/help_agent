<template>
  <div class="cmp-side-mask-bottom">
    <div class='bottom-left'>
      <DeviceSwitch :status="audioOn" :className="'mic-switch'" label="麦克风"
        @btnclick="toggleMic" :list="micList" @change="micChange" :index="micIndex" selectLabel="麦克风"
      />
      <DeviceSwitch :status="videoOn" :className="'video-switch'" label="摄像头"
        @btnclick="toggleCamera" :list="cameraList" @change="cameraChange" :index="cameraIndex" selectLabel="摄像头"
      />
      <DeviceSwitch :status="volume > 0" :className="'audio-switch'" label='扬声器'
        @btnclick='toggleSpeaker' v-outside="closeSpeakerSlider" :list="speakerList" @change="speakerChange" :index="speakerIndex" selectLabel="扬声器"
      >
        <template v-if="showVolume" slot="slider">
          <el-slider
            v-model="volume"
            vertical
            tooltip-class="is-light"
            height="100px">
          </el-slider>
        </template>
      </DeviceSwitch>
      <DeviceSwitch :status="whitebroadOpened" :disable="!isGalleryOpen"  :className="'white-broad-switch' + (whiteboardNewMessageTip ? ' new-message-tip' : '')" :label="whitebroadOpened ? '关闭白板' : '打开白板'"
        @btnclick='toggleWhitebroad'
      />
      <DeviceSwitch v-show="hasScreenShare" :status="screenShared" :className="'screen-share-switch'" :label="screenShared ? '停止共享' : '共享屏幕'"
        @btnclick='toggleScreenShare'
      />
      <span class="screen-shared-open" v-if="screenShared"></span>
      <DeviceSwitch :status="isGalleryOpen" :disable="whitebroadOpened" :className="'top-gallery'" label="切换布局" @btnclick='toggleGallery'
      />
      <DeviceSwitch :className="'setting'" label="设置"
        @btnclick='toggleSetting'
      />
    </div>
    <div v-if="joined" class='bottom-right'>
      <DeviceSwitch :className="(!showUserListChatBox || userChatBoxType !== 1) && hasNewMessage ? 'new-message' : 'chat'" label="聊天"
        @btnclick='toggleChat'
      />
      <DeviceSwitch :className="'user-list'" label="用户列表"
        @btnclick='toggleUserList'
      />
      <DeviceSwitch :className="'setting'" label="设置"
        @btnclick='toggleSetting'
      />
      <Button :type="joined ? 'danger' : 'primary'"
        @click='dialogVisible = true'
      >{{joined ? '结束' : '加入'}}</Button>
    </div>
    <el-dialog
      title="提示"
      :visible.sync="dialogVisible"
      width="30%"
      center>
      <span>确认退出吗？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="leave">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="提示"
      :visible.sync="whiteboarddialogVisible"
      width="30%"
      center>
      <span>确认关闭白板吗？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="whiteboarddialogVisible = false">取消</el-button>
        <el-button @click="toggleWhitebroad(true, true)">关闭白板</el-button>
        <el-button type="primary" @click="toggleWhitebroad(true, false)">禁用白板</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
// import * as BRTC from '../lib/BRTC'
import DeviceSwitch from '@/components/DeviceSwitch.vue'
import { Button } from 'element-ui'
import { mapState } from 'vuex'
export default {
  name: 'SideMaskBottom',
  props: {
    joined: {
      type: Boolean,
      default: false
    },
    screenShared: {
      type: Boolean,
      default: false
    },
    isGallery: {
      type: Boolean,
      require: true
    },
    whitebroadOpened: {
      type: Boolean,
      default: false
    },
    showUserListChatBox: {
      type: Boolean,
      default: false
    }
  },
  data () {
    return {
      dialogVisible: false,
      showVolume: false,
      volume: this.$store.state.speakerVolume,
      hasScreenShare: BRTC.supportScreenSharing(),
      fullScreen: !!(document.isFullScreen || document.webkitIsFullScreen || document.mozIsFullScreen),
      isGalleryOpen: true,
      whiteboarddialogVisible: false
    }
  },

  watch: {
    volume: function (value) {
      this.$store.commit('speakerVolume', {
        speakerVolume: value
      })
    }
  },

  computed: {
    ...mapState({
      videoOn: state => state.videoOn,
      audioOn: state => state.audioOn,
      cameraList: state => state.cameraList,
      micList: state => state.micList,
      speakerList: state => state.speakerList,
      cameraIndex: state => state.cameraIndex,
      micIndex: state => state.micIndex,
      speakerIndex: state => state.speakerIndex,
      hasNewMessage: state => state.hasNewMessage,
      userChatBoxType: state => state.userChatBoxType,
      whiteboardNewMessageTip: state => state.whiteboardNewMessageTip
    })
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
    // 摄像头操作
    toggleCamera () {
      if (this.joined) {
        this.$store.commit('videoOn', {
          videoOn: !this.videoOn
        })
      }
      else {
        this.$message({
          type: 'error',
          message: '未加入频道'
        })
      }
    },
    // 扬声器操作
    toggleSpeaker () {
      this.showVolume = !this.showVolume
    },

    toggleWhitebroad(force, local) {
      if (this.whitebroadOpened && !force) {
        this.whiteboarddialogVisible = true
      }
      else {
        console.log(local)
        this.$emit('toggleWhitebroad', local)
        this.whiteboarddialogVisible = false
      }
    },

    closeSpeakerSlider () {
      this.showVolume = false
    },

    toggleScreenShare () {
      this.$emit('toggleScreenShare');
    },

    toggleChat () {
      this.$emit('toggleChat');
    },

    toggleUserList () {
      this.$emit('toggleUserList');
    },

    toggleSetting () {
      this.$emit('toggleSetting');
    },

    leave () {
      // this.$router.push({path:'join'})
      alert("关闭");
    },

    getCurrentCamera () {
      return this.cameraList[this.cameraIndex]
    },

    getCurrentMic () {
      return this.micList[this.micIndex]
    },

    getCurrenSpeaker () {
      return this.speakerList[this.speakerIndex]
    },

    micChange (index) {
      this.$store.commit('micIndex', {
        micIndex: index
      })
      this.$emit('micChange', this.micList[index])
    },

    cameraChange (index) {
      this.$store.commit('cameraIndex', {
        cameraIndex: index
      })
      this.$emit('cameraChange', this.cameraList[index])
    },

    speakerChange (index) {
      this.$store.commit('speakerIndex', {
        speakerIndex: index
      })
      this.$emit('speakerChange', this.speakerList[index])
    },

    enumAudioInputDevices() {
      // TODO index 记住之前的设置
      BRTC.enumAudioInputDevices().then(
        micList => {
          this.$store.commit('micList', {
            micList: micList
          })
          for (let i = 0; i < micList.length; i++) {
            if (micList.default) {
              this.$store.commit('micIndex', {
                micIndex: i
              })
            }
          }
        },
        error => {
          this.$message({
            type: 'error',
            message: error.message
          })
        }
      )
    },

    enumVideoDevices() {
      // TODO index 记住之前的设置
      BRTC.enumVideoDevices().then(
        cameraList => {
          this.$store.commit('cameraList', {
            cameraList: cameraList
          })
          for (let i = 0; i < cameraList.length; i++) {
            if (cameraList.default) {
              this.$store.commit('cameraIndex', {
                cameraIndex: i
              })
            }
          }
        },
        error => {
          this.$message({
            type: 'error',
            message: error.message
          })
        }
      )
    },

    enumAudioOutputDevices() {
      // TODO index 记住之前的设置
      BRTC.enumAudioOutputDevices().then(
        speakerList => {
          this.$store.commit('speakerList', {
            speakerList: speakerList
          })
          for (let i = 0; i < speakerList.length; i++) {
            if (speakerList.default) {
              this.$store.commit('speakerIndex', {
                speakerIndex: i
              })
            }
          }
        },
        error => {
          this.$message({
            type: 'error',
            message: error.message
          })
        }
      )
    },
    toggleGallery () {
      this.$emit('toggleGallery')
      this.isGalleryOpen = !this.isGalleryOpen;
    }
  },
  mounted () {
    this.enumVideoDevices()
    this.enumAudioInputDevices()
    this.enumAudioOutputDevices()

    navigator.mediaDevices.ondevicechange = (event) => {
      this.enumVideoDevices()
      this.enumAudioInputDevices()
      this.enumAudioOutputDevices()
    }
  },
  components: {
    DeviceSwitch,
    Button
  }
}
</script>
<style scoped lang="less">
    .cmp-side-mask-bottom {
      background: #fff;
      position: absolute;
      width: 100%;
      height: 56px;
      bottom: 0;
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      align-items: center;
      padding: 8px 20px 0px;
      .bottom-left {
        height: 100%;
        display: flex;
        flex-direction: row;
        .screen-shared-open {
          display: inline-block;
          position: absolute;
          top: 24px;
          left: 349px;
          width: 6px;
          height: 6px;
          border-radius: 50%;
          background: #F00;
        }
      }
      .bottom-right {
        margin-top: -10px;
        width: 320px;
        display: flex;
        align-items: center;
        .cmp-device-switch {
          display: inline-block;
          height: 40px;
        }
        .el-button {
          padding: 8px 20px;
        }
      }
    }
    
</style>
