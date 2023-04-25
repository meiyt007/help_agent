<template>
  <div v-if="isOpenSuccess" class="router-item router-meeting" v-loading="!joined && !wasKicked" element-loading-text="正在进入房间...">
    <div class="boardOpen" :class="{'borderLeft':videoMin}">
      <div class="centerXY" @click="toggleWhitebroad">
        <img src="@/assets/video/1.png" alt="">
      </div>
      <div class="video-time centerXY flexY" v-if="videoMin" @click="videoToMin">
        <img src="@/assets/video/9.png" alt="">
        <!-- <div class="time">12:30:30</div> -->
      </div>
      <div class="centerXY" v-if="!showUserListChatBox" @click="toggleChat">
        <img src="@/assets/video/10.png" alt="">
      </div>
    </div>
    <!-- <div class="maxVideo" v-if="maxVideo">
      <Video :ref="maxStream.getStreamId()" :key="maxStream.getStreamId()+'33'"  :stream="maxStream"></Video>
      <img @click="minVideo" src="@/assets/video/4.png" alt="">
    </div> -->
    <div class="video-container" :class="{'video-board':whitebroadOpened}" ref="videoContainer" :style="{opacity: joined ? 1 : 0}">
      <DocumentPlayer :screenShared="!!publishScreenStream" @toggleScreenShare="toggleScreenShare" @toggleWhitebroad="toggleWhitebroad" :whitebroadOpened="whitebroadOpened" v-if="whitebroadOpened && enableDocument" ref="docPlayer" :config="painterConfig" :client="rtmClient" :docToolsOptions="docToolsOptions" :docId="openDocId" :data-operat-ppt="docToolsOptions.canOperatePPT && operatePPT ? 1 : 0"></DocumentPlayer>
      <VideoGallery ref="videoGallery" v-if="isGallery" :streamList="galleryList" :width="videoGalleryWidth" :height="videoGalleryHeight"  />
      <template v-else>
        <div class="video-content" v-if="!videoMin">
          <img @click="videoToMin" class="close" src="@/assets/video/4.png" alt="">
          <div class="main-video" v-if="mainStream" :class="{'maxVideo':maxVideoId == mainStream.getStreamId()}">
            <Video :joined="joined" v-if="mainStream" :isMain="true" :key="mainStream.getStreamId()" :ref="mainStream.getStreamId()" :stream="mainStream" @selfScreenFrame="selfScreenFrame" @setMain="setActive(mainStream)" />
            <Video v-else :local="true" :isMain="true" />
            <img class="amplify" @click="amplifyVideo(mainStream.getStreamId())" src="@/assets/video/amplify.png" alt="">
          </div>
          <div class="sub-video-list" :data-open="subListOpen ? 1 : 0" @mouseenter="showPannelButton" @mouseleave="hidePannelButton">
            <div class="video-list" :style="{'max-height': videoGalleryHeight - 40 + 'px'}">
              <div class="sub-video" v-for="stream in renderStreamList" :key="stream.getStreamId()"  :class="{'maxVideo':maxVideoId == stream.getStreamId()}">
                <Video :ref="stream.getStreamId()" :stream="stream" @selfScreenFrame="selfScreenFrame" @setMain="setActive(stream)" />
                <img class="amplify" @click="amplifyVideo(stream.getStreamId())" src="@/assets/video/amplify.png" alt="">
              </div>
              <div class="invite toInvite centerXY" :class="statusText[people.existStatus]" v-show="people.existStatus != '2'" :key="people.inDate" v-for="people in callList">
                <img src="@/assets/video/toVideo.png" alt="">
              </div>
              <div class="invite centerXY" @click="getPeople">
                <img src="@/assets/video/5.png" alt="">
              </div>
            </div>
            <div class="toggle" @click="togglePannel" :data-show="(showToggleButtom || !subListOpen) ? 1 : 0">
              <span class="icon-triangle"></span>
            </div>
          </div>
        </div>
        <!-- <div v-if="screenFrame" class="screen-frame">
          <span class="screen-icon"></span>
          <span class="screen-title">您正在分享屏幕</span>
          <img class="screen-img" :src="screenFrame" />
        </div> -->
      </template>
      <DocumentManage v-if="rtmJoined && enableDocument" v-show="whitebroadOpened && !isGallery" :client="rtmClient" @openDoc="openDoc" />
      <PainterTools ref="painterTools" v-show="whitebroadOpened && !isGallery && enableDocument" :config="painterConfig" @clear="clearAllShape" @download="download" />
      <DocumentTools v-show="whitebroadOpened && !isGallery && enableDocument" :currentPage="docToolsOptions.currentPage" :totalPage="docToolsOptions.totalPage" :canPrev="docToolsOptions.canPrev"
        :canNext="docToolsOptions.canNext" :canAddPage="docToolsOptions.canAddPage" :isShowOperatePage="docToolsOptions.isShowOperatePage" :canDeletePage="docToolsOptions.canDeletePage" :canOperatePPT="docToolsOptions.canOperatePPT"
        @prev="prev" @next="next" @addPage="addPage" @deletePage="deletePage" @changePage="changePage" @operatePPT="toggleOperatePPT"/>
    </div>
    <!-- 视频时间 -->
    <!-- <SideMaskTop @toggleGallery="toggleGallery" :isGallery="isGallery"></SideMaskTop> -->
    <SideMaskBottom class="none" ref="bottom"  :joined="joined" :screenShared="!!publishScreenStream"
      @toggleJoined="toggleJoined" @toggleScreenShare="toggleScreenShare"
      @micChange="micChange" @cameraChange="cameraChange" @speakerChange="speakerChange"
      @toggleSetting="toggleSetting" @toggleWhitebroad="toggleWhitebroad" :whitebroadOpened="whitebroadOpened"
      @toggleGallery="toggleGallery" :isGallery="isGallery"
      @toggleChat="toggleChat" @toggleUserList="toggleUserList"
      :showUserListChatBox="showUserListChatBox"
    >
    </SideMaskBottom>

    <el-dialog class="setting-dialog" :visible.sync="dialogSettingVisible" width="720px">
      <Setting @close="dialogSettingVisible = false" />
    </el-dialog>
    <UserListChatBox :class="{'chatNovideo':videoMin}" v-if="showUserListChatBox" ref="userListChatBox" :messageList="messageList" :currentUserList="currentUserList"
      @closeUserListChatBox="closeUserListChatBox" @sendMessage="sendMessage" 
      @audioSwitch="audioSwitch" @videoSwitch="videoSwitch" @kickOut="kickOut"
      @closeVideo="closeVideo"
    />
    <el-dialog
      title="提示"
      :visible.sync="showUserKickedDialog"
      width="30%" :modal="false"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :show-close="false"
      center>
      <span>您已被踢出房间</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="blue" @click="confirmKickOut">确认</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import Video from '@/components/Video.vue'
import SideMaskTop from '@/components/SideMaskTop.vue'
import SideMaskBottom from '@/components/SideMaskBottom.vue'
import VideoGallery from '@/components/VideoGallery.vue'
import Setting from '@/components/Setting.vue'
import { mapState } from 'vuex'
import debounce from '../../util/debounce'
import PainterTools from '@/components/PainterTools.vue'
import DocumentTools from '@/components/DocumentTools.vue'
import DocumentManage from '@/components/DocumentManage.vue'
import DocumentPlayer from '@/components/DocumentPlayer.vue'
import UserListChatBox from '@/components/UserListChatBox.vue'
import { callWait } from "@/api/modules/helpAgent";
export default {
  name: 'Meeting',
  data: function () {
    return {
      publishStream: null,
      publishScreenStream: null,
      remoteStreamList: [],
      activeStream: null,
      screenFrame: '',
      isGallery: false,
      videoGalleryWidth: 100,
      videoGalleryHeight: 100,
      dialogSettingVisible: false,
      whitebroadOpened: false,
      enableDocument: false,
      painterConfig: BRTM.createPainterConfig(),
      docToolsOptions: {
        currentPage: 1,
        totalPage: 1,
        canPrev: false,
        canNext: false,
        canAddPage: false,
        isShowOperatePage: false,
        canDeletePage: false,
        canOperatePPT: false
      },
      rtmClient: null,
      openDocId: null,
      operatePPT: false,
      subListOpen: true,
      showToggleButtom: false,
      rtmJoined: false,
      showUserListChatBox: true,
      messageList: [],
      currentUserList: [],
      showUserKickedDialog: false,
      wasKicked: false,
      isRtmError: true,
      roomId:localStorage.getItem('roomId'), 
      roomNumber: localStorage.getItem('roomNumber'), 
      userId: parseInt(localStorage.getItem('userId')),
      userName: localStorage.getItem('userName'),
      screenVideoNum:parseInt(localStorage.getItem('screenVideoNum')),
      cameraOn: true,
      micOn: true,
      videoMin:false,
      chatMin:false,
      isOpenSuccess:true,
      callList:[],  //呼叫列表
      timer:null,
      statusText:{  
        '1':'videoing',  //呼叫中   2已接通
        '3':'videone',  //已拒绝
        '4':'videoOut',  //已退出
        '5':'videoWait',  //已超时
      },
      maxVideoId:null,  //最大化视频流
    }
  },
  
  computed: {
    ...mapState({
      videoOn: state => state.videoOn,
      audioOn: state => state.audioOn,
      joined: state => state.joined,
      speakerVolume: state => state.speakerVolume
    }),

    /**
     * 媒体状态
     */
    mediaOn: function () {
      return this.videoOn || this.audioOn
    },

    /**
     * 大屏流
     */
    mainStream: function () {
      let activeStream = this.activeStream
      let publishStream = this.publishStream
      let publishScreenStream = this.publishScreenStream
      if (activeStream) {
        return activeStream
      }
      // else if (publishScreenStream) {
      //   return publishScreenStream
      // }
      return publishStream
    },
    /**
     * 小屏流列表
     */
    renderStreamList: function () {
      let publishStream = this.publishStream
      let publishScreenStream = this.publishScreenStream
      let remoteStreamList = this.remoteStreamList
      let mainStream = this.mainStream
      let list = remoteStreamList.slice()
      // if (publishScreenStream) {
      //   list.unshift(publishScreenStream)
      // }
      if (publishStream) {
        list.unshift(publishStream)
      }
      console.log(list);
      // if (this.whitebroadOpened) {
      //   return list
      // }
      // else {
          return list.filter((stream) => {
          if (mainStream && mainStream.getStreamId() === stream.getStreamId()) {
            return false
          }
          return true
        })
      // }
    },
    /**
     * 画廊列表
     */
    galleryList: function () {
      let list = []
      if (this.mainStream && !this.whitebroadOpened) {
        list.push(this.mainStream)
      }
      return list.concat(this.renderStreamList)
    }
  },

  watch: {
    mediaOn: function (newValue, oldValue) {
      // 第一次打开设备推流
      if (newValue && !this.publishStream) {
        this.publishCameraStream()
      }
    },

    videoOn: function (newValue, oldValue) {
      // 对摄像头进行开关操作
      if (this.publishStream) {
        if (newValue) {
          this.publishStream.unmuteVideo()
        }
        else {
          this.publishStream.muteVideo()
        }
        let video = this.getVideoInstance(this.publishStream.getStreamId())
        if (video) {
          video.videoOn = newValue
        }
      }
    },

    audioOn: function (newValue, oldValue) {
      // 对麦克风进行开关操作
      if (this.publishStream) {
        if (newValue) {
          this.publishStream.unmuteAudio()
        }
        else {
          this.publishStream.muteAudio()
        }
        let video = this.getVideoInstance(this.publishStream.getStreamId())
        if (video) {
          video.audioOn = newValue
        }
      }
    },

    speakerVolume: function (value) {
      // 设置播放音量
      for (let i = 0; i < this.remoteStreamList.length; i++) {
        this.remoteStreamList[i].setAudioVolume(value / 100)
      }
    }
  },
  beforeMount(){
    this.joinMeetFn();
  },
  mounted () {
    // console.log(this.userId)
    let that = this;
    setTimeout(function(){
      that.beforeVideo();
    },2000)

    that.timer = setInterval(function(){
        that.getCallList(); //获取房间人员呼叫状态
    },1000)

  },
  methods: {
    //视频最大化
    amplifyVideo(id){
      if(this.maxVideoId != id){
        this.maxVideoId = id;
      }else{
        this.maxVideoId = '';
      }
      
      
    },
    ////视频最大化  缩小
    minVideo(){
      // this.maxVideo = false;
      // this.maxStream = null;
    },
    //获取房间人员呼叫状态
    getCallList(){
      let that = this;
      callWait({
        roomId:localStorage.getItem('roomId')
      }).then(res=>{
        // console.log(res);
        let arr = res.data.accessList;
        that.callList = arr;
        // 房间状态;1-开启状态、2-关闭状态 3-等待中（申请人在创建队列时是等待中)）
        if(res.data.roomStatus == '2'){
          that.closeVideo();
          // that.$message.warning("房间已关闭！");
        }
        localStorage.setItem('callUserId',res.data.callUserId);
        
      })
    },
    closeVideo(){
      clearInterval(this.timer);
      this.timer = null;
      this.$emit('finishVideo');
    },
    getPeople(){
      this.$emit("openBusinessType",'',true);  //打开组长或委办局
    },
    //视频最小化
    videoToMin(){
      this.videoMin = !this.videoMin;
    },
    //聊天最小化
    chatToMin(){
      this.chatMin = !this.chatMin;
    },
    beforeVideo() {
      // let params = {
      //   // accessId:localStorage.getItem("accessId"),
      //   accessId:'315860399964786688',
      //   roomId:'',
      //   // roomId:localStorage.getItem("roomId"),
      //   roomNum:'',
      //   roomName:'',
      //   workUserId:'',
      //   userName:'',
      //   userType:'',
      // }
      // joinRoom(params).then(res=>{

      // })
      /**
       * 配置自己的 sig 需要客户根据自己的业务从后端返回
       * 
       * mainSig 用于推摄像头的流
       * assistSig 用于推屏幕分享
       */
      
      // this.mainSig = 'eJw8zk9rgzAcxvH38rtuuOhG1MAuZmwwrYVG7dmaKDH*Cam12tL3XrC1x*cDD3yvkETMEpOWRgDx8BdC7wtJLrpBllIYIGAjeKjp*3bZaJUjV7nWkgOBpMyCbaUY-QnjOKJ-p4YWNe3C1GAd5Or1kBUQCN9iv-jPWUbVfGEfZ5Gm3G14z3dTN1a4KTa-zn726*Cgvp-HQbYCiI1dz-l0-TV0XAodC8HtHgAA---fdz1E'
      
      this.assistSig = localStorage.getItem("screenVideoSig");
      this.appId = localStorage.getItem("appId");
      this.mainSig = localStorage.getItem("mainSig");
      /**
       * 没有 roomId 和 userId 直接退出到登录页
       */
      if (!this.$store.state.roomId || !this.$store.state.userId) {
        // this.$router.push({ path: 'join' })
        return
      }

      // 创建主 client
      let client = this.client = BRTC.createClient({
        codec: 'h264',
        appId: this.appId,
        comments: 'brtc-js-demo-web'
      })
      let rtmClient = this.rtmClient = BRTM.createClient({
        appId: this.appId
      })
      // 自定义白板背景图
      rtmClient.document.setWhiteboardImageConfig({
        url: 'https://test-img.baijiayun.com/0baijiatools/20d369c13ccb17e85d375673c5f80fe7/矩形备份42x.png',
        width: 1920,
        height: 1080
      })

    
      this.join()


      /**
       * 监听事件
       */
      client
        /**
         * 远端推流
         */
        .on('stream-added', (data) => {
          let remoteStream = data.stream
          let userId = remoteStream.getUserId() + ''
          console.log(userId);
          console.log(this.$store.state.userId);
          // if (/1$/.test(userId) && +userId.substr(0, userId.length - 1) === this.$store.state.userId) {
          if (/1$/.test(userId) && +userId.substr(0, userId.length - 1) * 10 === this.$store.state.userId) {
            // 是自己的屏幕分享，不处理
            return
          }
          this.client.subscribe(remoteStream).then(
            () => {
              this.remoteStreamList.push(remoteStream)
              console.log('订阅流成功')
            },
            (error) => {
              console.log('订阅流失败', error)
            }
          )
          remoteStream.on('connect-error', () => {
            console.log('远程流中断')
            remoteStream.resubscribeStreamTimer = setTimeout(() => {
              this.resubscribeStream(remoteStream)
            }, 2000)
          })
        })
        /**
         * 订阅流成功
         */
        .on('stream-subscribed', (data) => {
          let remoteStream = data.stream
          let videoOn = remoteStream.getVideoOn()
          let audioOn = remoteStream.getAudioOn()

          let video = this.getVideoInstance(remoteStream.getStreamId())
          if (video) {
            video.videoOn = videoOn
            video.audioOn = audioOn
          }
          let currentUserList = this.$store.state.currentUserList
          if (currentUserList[remoteStream.uid]) {
            currentUserList[remoteStream.uid]['videoOn'] = videoOn
            currentUserList[remoteStream.uid]['audioOn'] = audioOn
          }
          this.$store.commit('initUserList', currentUserList)
          this.currentUserList = this.objectToArray(currentUserList)
        })
        /**
         * 远端结束推流
         */
        .on('stream-removed', (data) => {
          let remoteStream = data.stream
          if (this.activeStream === remoteStream) {
            this.activeStream = null
          }
          remoteStream.destroy()
          for (let i = 0; i < this.remoteStreamList.length; i++) {
            if (this.remoteStreamList[i].getUserId() === remoteStream.getUserId()) {
              this.remoteStreamList.splice(i, 1)
              break
            }
          }
        })
        /**
         * 远端关掉摄像头
         */
        .on('mute-video', (data) => {
          let remoteStream = data.stream
          let video = this.getVideoInstance(remoteStream.getStreamId())
          if (video) {
            video.videoOn = false
          }
          let currentUserList = this.$store.state.currentUserList
          if (currentUserList[remoteStream.uid]) {
            currentUserList[remoteStream.uid]['videoOn'] = false
          }
          this.$store.commit('initUserList', currentUserList)
          this.currentUserList = this.objectToArray(currentUserList)
        })
        /**
         * 远端打开摄像头
         */
        .on('unmute-video', (data) => {
          let remoteStream = data.stream
          let video = this.getVideoInstance(remoteStream.getStreamId())
          if (video) {
            video.videoOn = true
          }
          let currentUserList = this.$store.state.currentUserList
          if (currentUserList[remoteStream.uid]) {
            currentUserList[remoteStream.uid]['videoOn'] = true
          }
          else {
            currentUserList[remoteStream.uid] = {
              videoOn: true
            }
          }
          this.$store.commit('initUserList', currentUserList)
          this.currentUserList = this.objectToArray(currentUserList)
        })
        /**
         * 远端关掉麦克风
         */
        .on('mute-audio', (data) => {
          let remoteStream = data.stream
          let video = this.getVideoInstance(remoteStream.getStreamId())
          if (video) {
            video.audioOn = false
          }
          let currentUserList = this.$store.state.currentUserList
          if (currentUserList[remoteStream.uid]) {
            currentUserList[remoteStream.uid]['audioOn'] = false
          }
          this.$store.commit('initUserList', currentUserList)
          this.currentUserList = this.objectToArray(currentUserList)
        })
        /**
         * 远端打开麦克风
         */
        .on('unmute-audio', (data) => {
          let remoteStream = data.stream
          let video = this.getVideoInstance(remoteStream.getStreamId())
          if (video) {
            video.audioOn = true
          }
          let currentUserList = this.$store.state.currentUserList
          currentUserList[remoteStream.uid]['audioOn'] = true
          this.$store.commit('initUserList', currentUserList)
          this.currentUserList = this.objectToArray(currentUserList)
        })
        /**
         * 无法处理发错误
         */
        .on('error', (error) => {
          console.log(error)
          if (this.republishStreamTimer) {
            clearTimeout(this.republishStreamTimer)
          }
          for (let i = 0; i < this.remoteStreamList.length; i++) {
            this.remoteStreamList[i].destroy()
            if (this.remoteStreamList[i].resubscribeStreamTimer) {
              clearTimeout(this.remoteStreamList[i].resubscribeStreamTimer)
            }
          }
          this.remoteStreamList = []
          
          if (this.rejoining) {
            return
          }

          this.rejoining = true

          this.$message('网络断开，正在重连')
          
          if (this.assistClient) {
            this.assistClient.leave()
            if (this.publishScreenStream) {
              this.publishScreenStream.destroy()
            }
          }

          // 重新进入房间
          this.rejoin()
        })
        .on('client-banned', () => {
          this.$message({
            type: 'error',
            message: '您已在其他端登录'
          })
          // this.$router.push({
          //   path: 'join'
          // })
        })
        .on('user-kicked', () => {
          this.$message({
            type: 'error',
            message: '您已被踢出房间'
          })
          // this.$router.push({
          //   path: 'join'
          // })
        })
        .on('peer-joined', data => {
          console.log('user: ' + data.userId + ' 进入房间')
        })
        .on('peer-leaved', data => {
          console.log('user: ' + data.userId + ' 离开房间')
        })
        .on('token-expire', data => {
          this.$message({
            type: 'error',
            message: `token 已过期`
          })
          // this.$router.push({ path: 'join' })
        })
        .on('room-closed', data => {
          this.$message({
            type: 'error',
            message: `房间已关闭`
          })
          // this.$router.push({ path: 'join' })
        })

      this.rtmClient
        .on(
          BRTM.Events.Room.USER_KICKED, fromId => {
            console.log(`已被用户 ${fromId} 踢出房间`)
            // this.$router.push({
            //   path: 'join'
            // })
          }
        )
        .on(
          BRTM.Events.Room.CLIENT_BANNED, endType => {
            console.log(`你已在终端 ${endType} 上登录`)
            // this.$router.push({
            //   path: 'join'
            // })
          }
        )
        .on(
          BRTM.Events.Room.MESSAGE_CHANNEL_OPENED, () => {
            console.log('brtm message channel opened')
            this.getMessageList()
          }
        )
        .on(
          BRTM.Events.Room.ERROR, error => {
            console.log('brtm error')
          }
        )
        /**
         * 监听收到广播消息
         */
        .on(BRTM.Events.Room.BROADCAST_MESSAGE_RECEIVED, (data) => {
          console.log('收到广播消息：', data)
        })
        /**
         * 监听收到广播消息
         */
        .on(BRTM.Events.Room.MESSAGE_RECEIVED, (data) => {
          console.log('收到单播消息：', data)
          if (data.messageType === 'logout') {
            this.leaveRoom()
          }
        })
        /**
         * 
         */
        .on(BRTM.Events.Room.BRTM_ABILITIES_CHANGED, (data) => {
          this.enableDocument = data.document
          this.whitebroadOpened = data.document
        })

      this.rtmClient.document
        // 文档翻页
        .on('page-changed', (data) => {
          this.whitebroadTip()
        })
        // 添加文档
        .on('doc-added', (data) => {
          this.whitebroadTip()
        })
        // 文档更新
        .on('doc-updated', (data) => {
          this.whitebroadTip()
        })
        // 文档移除
        .on('doc-removed', (data) => {
          this.whitebroadTip()
        })
        // 添加白板
        .on('page-added', (data) => {
          this.whitebroadTip()
        })
        // 删除白板
        .on('page-removed', (data) => {
          this.whitebroadTip()
        })
        // 画笔相关
        .on('shape-added', (data) => {
          this.whitebroadTip()
        })
        .on('shape-appended', (data) => {
          this.whitebroadTip()
        })
        .on('shape-updated', (data) => {
          this.whitebroadTip()
        })
        .on('shape-removed', (data) => {
          this.whitebroadTip()
        })
        .on('laser-received', (data) => {
          this.whitebroadTip()
        })

      this.rtmClient.user
        .on(
          BRTM.Events.User.USER_IN, user => {
            setTimeout(() => {
              this.getUserList()
            }, 1500)
            console.log('brtm user in', user)
          }
        )
        .on(
          BRTM.Events.User.USER_OUT, user => {
            this.getUserList()
            console.log('brtm user out', user)
          }
        )
      this.rtmClient.chat
        .on(
          BRTM.Events.Chat.MESSAGE_RECEIVED, message => {
            this.receviedNewMessage(message)
            console.log('brtm message received', message)
          }
        )
        .on(
          BRTM.Events.Chat.MESSAGE_WHISPER_REVEIVED, message => {
            console.log('brtm message whisper received', message)
          }
        )
        .on(
          BRTM.Events.Chat.MESSAGE_REVOKED, message => {
            console.log('brtm message revoked', message)
          }
        )

      this.videoGalleryWidth = this.$refs['videoContainer'].offsetWidth
      this.videoGalleryHeight = this.$refs['videoContainer'].offsetHeight

      window.onresize = debounce(() => {
        this.$nextTick(() => {
          this.videoGalleryWidth = this.$refs['videoContainer'].offsetWidth
          this.videoGalleryHeight = this.$refs['videoContainer'].offsetHeight
          if (this.$refs.docPlayer) {
            this.$refs.docPlayer.reload()
          }
          if (this.$refs.painterTools && this.$refs.docPlayer) {
            this.$refs.painterTools.resetFontSize()
          }
        })
      }, 200)

      window.onbeforeunload = () => {
        // 直接关闭页面退出房间
        this.close()
      }
    },
    joinMeetFn(){
      if (!this.userId) {
        this.$message({
          type: 'error',
          message: '用户id为空'
        })
        return
      }
      if (!this.roomNumber) {
        this.$message({
          type: 'error',
          message: '房间号为空'
        })
        return
      }

      const userId = this.userId
      const userName = this.userName + ''

      if (userId == 0) {
        this.$message({
          type: 'error',
          message: 'user id 不能为 0'
        })
        return
      }

      if (userName.length > 16) {
        this.$message({
          type: 'error',
          message: 'user name 不能超过16个字符'
        })
        return
      }

      this.$store.commit('roomId', {
        roomId: this.roomNumber
      })
      this.$store.commit('userId', {
        userId: userId
      })
      this.$store.commit('userName', {
        userName: userName
      })
      this.$store.commit('videoOn', {
        videoOn: this.cameraOn
      })
      this.$store.commit('audioOn', {
        audioOn: this.micOn
      })
      // this.$router.push({path:'meeting'})
    },
    join() {
      let that = this;
      console.log(this.mainSig)
      return new Promise((resolve, reject) => {
        this.rtmClient.join(this.$store.state.roomId, this.$store.state.userId, this.mainSig, {
          userInfo: {
            name: this.$store.state.userName || this.$store.state.userId + '',
            avatar: 'https://test-img.baijiayun.com/0bjcloud/live/avatar/v2/teacherv3.png'
          }
        }).then(() => {
          // 设置当前是否可以使用文档功能
          this.enableDocument = this.whitebroadOpened = this.rtmClient.getAbilities().document
          this.rtmJoined = true
          this.getUserList()
        }).catch((error) => {
          console.log(error)
          // this.isOpenSuccess = false;
          this.$message({
            type: 'error',
            message: 'rtm 加入房间失败'
          })
        })
        Promise.all([
          this.client.join(this.$store.state.roomId, this.$store.state.userId, this.mainSig)
        ]).then(
          () => {
            // this.isOpenSuccess = true;
            this.$message.success('加入房间成功')
            // that.$emit('openRoom');
            this.$store.commit('joined', {
              joined: true
            })

            this.$nextTick(() => {
              this.videoGalleryWidth = this.$refs['videoContainer'].offsetWidth
              this.videoGalleryHeight = this.$refs['videoContainer'].offsetHeight
            })

            if (this.$store.state.videoOn || this.$store.state.audioOn) {
              /**
               * 本地流已经创建，直接 publish
               */
              if (this.publishStream) {
                this.client.publish(this.publishStream)
              }
              else {
                // 先创建，在 publish
                this.publishCameraStream()
              }
            }
            resolve()
          },
          (error) => {
            console.log(error)
            // this.isOpenSuccess = false;
            this.$message({
              type: 'error',
              message: '加入房间失败'
            })
            // this.$router.push({ path: 'join' })
          }
        )
      })
    },

    rejoin() {
      console.log('rejoin')
      this.join().then(
        () => {
          // this.isOpenSuccess = true;
          this.$message.success('重新加入房间成功')
          // this.$emit('openRoom');
          this.rejoining = false
        },
        () => {
          setTimeout(() => {
            this.rejoin()
          }, 2000)
        }
      )
    },

    toggleJoined() {
      if (this.joined) {
        Promise.all([
          this.publishStream && this.client.unpublish(this.publishStream),
          this.publishScreenStream && this.assistClient && this.assistClient.unpublish(this.publishScreenStream)
        ]).then(() => {
          // 把流销毁掉
          if (this.publishStream) {
            this.publishStream.destroy()
            this.publishStream = null
          }
          if (this.publishScreenStream) {
            this.publishScreenStream.destroy()
            this.publishScreenStream = null
          }
          // 退出房间
          this.client.leave().then(() => {
            // this.isOpenSuccess = false;
            this.$message('退出房间成功')
            this.$store.commit('audioOn', {
              audioOn: false
            })
            this.$store.commit('videoOn', {
              videoOn: false
            })
            this.$nextTick(() => {
              this.$store.commit('joined', {
                joined: false
              })
            })
          })
          if (this.assistClient) {
            this.assistClient.leave()
          }
          if (this.rtmClient) {
            this.rtmClient.leave()
          }
        })
      }
      else {
        this.join()
      }
    },
    publishCameraStream() {
      let that = this;
      let camera = this.$refs['bottom'].getCurrentCamera()
      let mic = this.$refs['bottom'].getCurrentMic()

      if (camera || mic) {
          let options = {
            // video: this.$store.state.videoOn,
            // audio: this.$store.state.audioOn
            video: !!camera,
            audio: !!mic
          }
          if (camera) {
            options.cameraId = camera.deviceId
          }
          else {
            this.$store.commit('videoOn', {
              videoOn: false
            })
            // options.videoOn = false
          }
          if (mic) {
            options.microphoneId = mic.deviceId
          }
          else {
            this.$store.commit('audioOn', {
              audioOn: false
            })
            // options.audioOn = false
          }
          
          // 浏览器禁用设备报错处理
          let publishStream;
          try {
            publishStream = BRTC.createStream(options);
            publishStream.setVideoProfile('1080p');
          }
          catch (error) {
            let code = error && error.getCode();
            let name = '';
            switch (code) {
              case '0x1003': name = "DevicesNotFoundError"; break;
            }
            reject({
              name: name,
              error: error
            });
            return;
          }

          // 设置自定义视频配置
          publishStream.setVideoEncoderConfiguration({
            width: 196,
            height: 196,
            frameRate: 15,
            bitrate: 200
          })
          publishStream.init()
            .then(() => {
              this.publishStream = publishStream
              this.client.publish(this.publishStream)
                .then(() => {
                  console.log('发布成功')
                  // that.$emit('openRoom');
                  if (!this.videoOn) {
                    this.publishStream.muteVideo()
                  }
                  else if (!this.audioOn) {
                    this.publishStream.muteAudio()
                  }
                  let instance = this.getVideoInstance(this.publishStream.getStreamId());
                  instance.updateMediaStatus()
                })
                .catch((error) => {
                  console.log('发布失败', error)
                })
                // 处理推流中断
                this.publishStream.on('connect-error', () => {
                  console.log('推流中断')
                  this.republishStreamTimer = setTimeout(() => {
                    this.republishCameraStream()
                  }, 2000)
                })
            })
            .catch((error) => {
              that.beforeVideo();
              console.log('创建流失败', error)
              if (err && err.code_ === 4099) {
                reject(Object.assign({}, err, { name: 'DevicesNotFoundError' }));
                return;
              }
              reject(err);
            })
            // 处理播放器状态改变
            publishStream.on('player-state-changed', (data) => {
              console.log(`${publishStream.getUserId()} player status: [${data.type}] ${data.state} ${data.reason}`)
            })
        }
        else {
          // this.$message({
          //   message: '未找到摄像头和麦克风',
          //   type: 'error'
          // })
        }
    },

    resubscribeStream(stream) {
      if (this.rejoining) {
        return
      }
      this.client.subscribe(stream).then(
        () => {
          console.log('重新拉流成功')
          stream.replay()
        },
        () => {
          stream.resubscribeStreamTimer = setTimeout(() => {
            this.resubscribeStream(stream)
          }, 2000)
        }
      )
    },

    republishCameraStream() {
      let that = this;
      if (this.rejoining) {
        return
      }
      this.client.publish(this.publishStream).then(
        () => {
          console.log('重新发布成功')
          // that.$emit('openRoom');
        },
        () => {
          this.republishStreamTimer = setTimeout(() => {
            this.republishCameraStream()
          }, 2000)
        }
      )
    },

    unpublishCameraStream() {
      if (this.publishStream && this.joined) {
        this.client.unpublish(this.publishStream)
        this.publishStream.destroy()
        this.publishStream = null
      }
    },

    toggleScreenShare () {
      let that = this;
      if (this.publishScreenStream) {
        this.assistClient.unpublish(this.publishScreenStream).then(() => {
          console.log('屏幕共享取消发布成功')
          this.publishScreenStream.destroy()
          if (this.activeStream === this.publishScreenStream) {
            this.activeStream = this.publishStream
          }
          this.publishScreenStream = null
          this.screenFrame = ''
          this.assistClient.leave().then(() => {
            console.log('屏幕共享离开房间成功')
          })
        })
      }
      else {
        let clientPromise
        if (this.assistClient) {
          clientPromise = new Promise((reslove, reject) => {
            this.assistClient.join(this.$store.state.roomId, this.screenVideoNum, this.assistSig).then(
              () => {
                console.log('屏幕共享加入房间成功')
                // that.$emit('openRoom');
                reslove()
              },
              (err) => {
                console.log(err)
                this.$message('屏幕共享加入房间失败')
                reject()
              }
            )
          })
        }
        else {
          clientPromise = new Promise((reslove, reject) => {
            this.assistClient = BRTC.createClient({
              codec: 'h264',
              appId: this.appId
            })
            // 设置 client 只用来推流
            this.assistClient.setPublishOnly(true)
            this.assistClient.join(this.$store.state.roomId, this.screenVideoNum, this.assistSig).then(
              () => {
                console.log('屏幕共享加入房间成功')
                // that.$emit('openRoom');
                reslove()
              },
              (err) => {
                console.log(err)
                this.$message('屏幕共享加入房间失败')
                reject()
              }
            )
          })
        }
        Promise.all([
          clientPromise,
          new Promise((reslove, reject) => {
            let publishScreenStream = BRTC.createStream({
              screen: true,
              audio: true
            })
            publishScreenStream.setVideoProfile('1080p');
            publishScreenStream.init().then(
              () => {
                this.publishScreenStream = publishScreenStream
                this.publishScreenStream.on('video-track-ended', () => {
                  // 屏幕共享结束，停止推流
                  this.toggleScreenShare()
                })
                reslove()
              },
              (error) => {
                reject(error)
              },
            )
          })
        ]).then(
          () => {
            this.assistClient.publish(this.publishScreenStream).then(() => {
              console.log('屏幕共享成功')
              // that.$emit('openRoom');
            }, () => {
              this.$message('屏幕共享发布失败')
            })

            this.publishScreenStream.on('connect-error', () => {
              console.log('屏幕分享推流中断')
              setTimeout(() => {
                this.republishShareStream()
              }, 2000)
            })

          },
          (error) => {
            if (!error || error.message !== 'Permission denied') {
                this.$message({
                type: 'error',
                message: '屏幕共享失败'
              })
            }
            // 失败了离开房间
            if (this.assistClient) {
              this.assistClient.leave()
            }
          }
        )
      }
    },

    republishShareStream() {
      let that = this;
      if (this.rejoining) {
        return
      }
      if (this.assistClient) {
        this.assistClient.publish(this.publishScreenStream).then(
          () => {
            console.log('屏幕分享重新发布成功')
            // that.$emit('openRoom');
          },
          () => {
            setTimeout(() => {
              this.republishShareStream()
            }, 2000)
          }
        )
      }
    },

    selfScreenFrame: function (img) {
      this.screenFrame = img
    },

    /**
     * 手动将某个流设置为主屏
     */
    setActive: function (stream) {
      if (this.activeStream !== stream) {
        this.activeStream = stream
      }
    },

    /**
     * 获取 Video Vue 实例
     */
    getVideoInstance: function (id) {
      if (this.isGallery) {
        return this.$refs['videoGallery'].getInstance(id);
      }
      else {
        let item = this.$refs[id]
        if (Array.isArray(item)) {
          return item[0]
        }
        return item
      }
    },

    /**
     * 切换麦克风
     */
    micChange: function (device) {
      if (this.publishStream) {
        // 切换麦克风设备
        this.publishStream.switchDevice('audio', device.deviceId)
      }
    },

    /**
     * 切换摄像头
     */
    cameraChange: function (device) {
      if (this.publishStream) {
        // 切换摄像头设备
        this.publishStream.switchDevice('video', device.deviceId)
      }
    },

    /**
     * 切换扬声器
     */
    speakerChange: function (device) {
      for (let i = 0; i < this.remoteStreamList.length; i++) {
        this.remoteStreamList[i].setAudioOutput(device.deviceId)
      }
    },

    /**
     * 切换布局
     */
    toggleGallery: function () {
      this.isGallery = !this.isGallery
    },

    /**
     * 打开聊天框
     */
    toggleChat: function () {
      new Promise((resolve, reject) => {
        if (this.isRtmError) {
          reject()
        } else {
          resolve()
        }
      }).then(res => {
        if (!this.showUserListChatBox) {
          this.$store.commit('setShowUserList', {
            userListStatus: false,
            type: 1
          })
        }
        this.showUserListChatBox = true
        this.$store.commit('setNewMessageStatus', false)
        this.$store.commit('setChatBox', {
          chatStatus: true,
          type: 1
        })
        this.$store.commit('setUserChatBoxType', 1)
        if (this.$
        ['userListChatBox']) {
          this.$refs['userListChatBox'].type = 1
          setTimeout(() => {
            this.$refs['userListChatBox'].chatHeight()
          }, 50)
        }
      })
      .catch(err => {
        // this.isOpenSuccess =false;
        this.$message.error('RTM 连接异常，请退出房间重试')
      })
    },

    /**
     * 关闭聊天用户列表窗口
     */
    closeUserListChatBox: function () {
      this.showUserListChatBox = false
      if (this.$store.state.userChatBoxType === 1) {
        this.$store.commit('setNewMessageStatus', false)
      }
      this.$store.commit('closeBox')
    },

    /**
     * 获取聊天信息
     */
    getMessageList: function () {
      this.rtmClient.chat.pull(this.$store.state.roomId + '').then(data => {
        this.isRtmError = false
        data.messageList.reverse()
        if (this.messageList.length < data.messageList.length) {
          this.$store.commit('setNewMessageStatus', true)
        }
        this.messageList = data.messageList
        if (this.showUserListChatBox) {
          setTimeout(() => {
            this.$refs['userListChatBox'].chatHeight()
          }, 50)
        }
      })
      .catch(err => {
        // this.isOpenSuccess = false;
        this.$message.error('拉取消息失败，请重试')
      })
    },

    /**
     * 接收到新的消息
     */
    receviedNewMessage: function (message) {
      this.messageList.push(message)
      this.$store.commit('setNewMessageStatus', true)
      setTimeout(() => {
        if (this.$refs['userListChatBox']) {
          this.$refs['userListChatBox'].chatHeight()
        }
      }, 50)
    },
    
    /**
     * 发送聊天信息
     */
    sendMessage: function (message) {
      this.rtmClient.chat.send({
        content: message.message + '',
        channel: this.$store.state.roomId + ''
      }).then(data => {
        if (!message.isEmoji) {
          this.$refs['userListChatBox'].messageContent = ''
        }
        this.receviedNewMessage({
          content: message.message + '',
          channel: this.$store.state.roomId + '',
          time: parseInt(new Date().getTime() / 1000),
          from: {
            id: this.$store.state.userId,
            name: this.$store.state.userName
          }
        })
      })
      .catch(err => {
        this.$message.error('消息发送失败，请退出房间重试')
        setTimeout(() => {
          this.$refs['userListChatBox'].chatHeight()
        }, 50)
      })
    },

    /**
     * 打开用户列表
     */
    toggleUserList: function () {
      new Promise((resolve, reject) => {
        this.getUserList()
        resolve()
      }).then(res => {
        if (!this.showUserListChatBox) {
          this.$store.commit('setChatBox', {
            chatStatus: false,
            type: 2
          })
        }
        this.showUserListChatBox = true
        this.$store.commit('setShowUserList', {
          userListStatus: true,
          type: 2
        })
        this.$store.commit('setUserChatBoxType', 2)
        if (this.$refs['userListChatBox']) {
          this.$refs['userListChatBox'].type = 2
        }
      }).catch(err => {
        // this.isOpenSuccess = false;
        this.$message.error('RTM 连接异常，请退出房间重试')
      })
    },

    /**
     * 获取用户列表
     */
    getUserList: function () {
      let userStatusList = {};
      this.remoteStreamList.forEach(item => {
        userStatusList[item.uid] = {
          videoOn: item.videoOn,
          audioOn: item.audioOn
        }
      })
      let userList = this.rtmClient.user.all()
      let currentUserList = this.$store.state.currentUserList
      let userListIds = []
      userList.forEach(item => {
        userListIds.push(item.id + '')
        if (!currentUserList[item.id]) {
          currentUserList[item.id] = {
            ...item,
            ...userStatusList[item.id + '']
          }
        }
        else if (item.id / 10 != this.$store.state.userId) {
          currentUserList[item.id].name = item.name
          currentUserList[item.id].audioOn = userStatusList[item.id] ? userStatusList[item.id].audioOn : false
          currentUserList[item.id].videoOn = userStatusList[item.id] ? userStatusList[item.id].videoOn : false
        }
      })
      for (let key in currentUserList) {
        if (userListIds.indexOf(key) === -1) {
          delete currentUserList[key]
        }
      }
      this.$store.commit('initUserList', currentUserList)
      this.currentUserList = this.objectToArray(currentUserList)
    },

    /**
     * 对象转数组
     */
    objectToArray: function (userListObj) {
      let userList = [];
      let self = this.$store.state.userId
      for (let key in userListObj) {
        if (key / 10 == self) {
          userList.unshift(userListObj[key])
        }
        else {
          userList.push(userListObj[key])
        }
      }
      return userList
    },

    /**
     * 麦克风开关
     */
    audioSwitch: function (data) {
      let self = this.$store.state.userId
      if (self != data.id / 10) {
        let remoteStreamList = this.remoteStreamList
        remoteStreamList.forEach(item => {
          if (item.uid == data.id) {
            let videoDom = this.getVideoInstance(item.sid)
            videoDom.audioOn = data.status
            videoDom.toggleMic()
          }
        })
      }
      let currentUserList = this.$store.state.currentUserList
      currentUserList[data.id]['isMuteAudio'] = data.status
      this.$store.commit('initUserList', currentUserList)
      this.currentUserList = this.objectToArray(currentUserList)
    },

    /**
     * 摄像头开关
     */
    videoSwitch: function (data) {
      let self = this.$store.state.userId
      if (self != data.id / 10) {
        let remoteStreamList = this.remoteStreamList
        remoteStreamList.forEach(item => {
          if (item.uid == data.id) {
            let videoDom = this.getVideoInstance(item.sid)
            videoDom.videoOn = data.status
            videoDom.toggleCamera()
          }
        })
      }
      let currentUserList = this.$store.state.currentUserList
      currentUserList[data.id]['isMuteVideo'] = data.status
      this.$store.commit('initUserList', currentUserList)
      this.currentUserList = this.objectToArray(currentUserList)
    },

    /**
     * 收到消息自己退出房间
     */
    leaveRoom: function () {
      Promise.all([
        this.publishStream && this.client.unpublish(this.publishStream),
        this.publishScreenStream && this.assistClient && this.assistClient.unpublish(this.publishScreenStream)
      ]).then(() => {
        // 把流销毁掉
        if (this.publishStream) {
          this.publishStream.destroy()
          this.publishStream = null
        }
        if (this.publishScreenStream) {
          this.publishScreenStream.destroy()
          this.publishScreenStream = null
        }
        // 退出房间
        this.client.leave().then(() => {
          this.$store.commit('audioOn', {
            audioOn: false
          })
          this.$store.commit('videoOn', {
            videoOn: false
          })
          this.$nextTick(() => {
            this.$store.commit('joined', {
              joined: false
            })
          })
        })
        if (this.assistClient) {
          this.assistClient.leave()
        }
        if (this.rtmClient) {
          this.rtmClient.leave()
        }
        this.wasKicked = true
        this.showUserKickedDialog = true
      })
    },

    /**
     * 踢人
     */
    kickOut: function (data) {
      this.rtmClient.sendMessage(data.id, 'logout').then(data => {
        this.getUserList()
        this.$refs['userListChatBox'].showKickOutDialog = false
      })
    },

    /**
     * 确认被踢出rtm
     */
    confirmKickOut: function () {
      this.showUserKickedDialog = false
      // this.$router.push({
      //   path: 'join'
      // })
    },

    /**
     * 打开设置
     */
    toggleSetting: function () {
      this.dialogSettingVisible = true
    },

    toggleWhitebroad: function (local) {
      if (!this.rtmJoined) {
        this.$commit.error('rtm 未加入成功')
        return
      }
      if (this.whitebroadOpened) {
        if (local) {
          this.whitebroadOpened = false
          this.$store.commit('whiteboardNewMessageTip', false)
        }
        else {
          this.rtmClient.disableDocumentAbility().then(() => {
            this.whitebroadOpened = false
            this.enableDocument = false
            this.$store.commit('whiteboardNewMessageTip', false)
          })
        }
      }
      else {
        if (this.enableDocument) {
          this.whitebroadOpened = true
          this.$store.commit('whiteboardNewMessageTip', false)
        }
        else {
          this.rtmClient.enableDocumentAbility().then(() => {
            this.whitebroadOpened = true
            this.enableDocument = true
            this.$store.commit('whiteboardNewMessageTip', false)
          })
          .catch(error => {
            this.$commit.error('开启白板失败')
          })
        }
      }
    },

    prev() {
      if (this.$refs.docPlayer) {
        this.$refs.docPlayer.prev()
      }
    },
    next() {
      if (this.$refs.docPlayer) {
        this.$refs.docPlayer.next()
      }
    },
    changePage(page) {
      if (this.$refs.docPlayer) {
        this.$refs.docPlayer.changePage(page)
      }
    },
    addPage() {
      if (this.$refs.docPlayer) {
        this.$refs.docPlayer.addPage()
      }
    },
    deletePage(page) {
      if (this.$refs.docPlayer) {
        this.$refs.docPlayer.deletePage(page)
      }
    },

    openDoc(docId) {
      this.openDocId = docId
    },

    clearAllShape(select) {
      if (this.$refs.docPlayer) {
        this.$refs.docPlayer.clear(select)
      }
    },

    download() {
      if (this.$refs.docPlayer) {
        this.$refs.docPlayer.download()
      }
    },

    toggleOperatePPT() {
      this.operatePPT = !this.operatePPT
    },

    togglePannel() {
      this.subListOpen = !this.subListOpen
    },

    togglePannelButton(event) {
      const clientX = event.clientX
      const clientY = event.clientY
      if (clientY > 28 && clientY < this.videoGalleryHeight + 28 && this.videoGalleryWidth - clientX < 250) {
        this.showToggleButtom = true
        if (this.hideToggleButtomTimer) {
          clearTimeout(this.hideToggleButtomTimer)
        }
        this.hideToggleButtomTimer = setTimeout(() => {
          this.showToggleButtom = false
          this.hideToggleButtomTimer = null
        }, 300)
      }
    },

    showPannelButton() {
      if (this.hideToggleButtomTimer) {
        clearTimeout(this.hideToggleButtomTimer)
      }
      this.showToggleButtom = true
    },

    hidePannelButton() {
      if (this.hideToggleButtomTimer) {
        clearTimeout(this.hideToggleButtomTimer)
      }
      this.hideToggleButtomTimer = setTimeout(() => {
        this.showToggleButtom = false
        this.hideToggleButtomTimer = null
      }, 150)
    },

    close: async function (force) {
      // 离开销毁创建出来的实例
      try {
        if (this.rtmClient) {
          this.rtmClient.destroy()
        }
        if (this.client) {
          await this.client.destroy(force)
        }
        if (this.assistClient) {
          await this.assistClient.destroy(force)
        }
        if (this.publishStream) {
          this.publishStream.destroy()
        }
        if (this.publishScreenStream) {
          this.publishScreenStream.destroy()
        }
      }
      catch (error) {

      }

      this.$store.commit('joined', {
        joined: false
      })
    },
    whitebroadTip: function () {
      let whiteboardNewMessageTip = this.$store.state.whiteboardNewMessageTip;
      if (!whiteboardNewMessageTip && !this.whitebroadOpened && this.enableDocument) {
        this.$store.commit('whiteboardNewMessageTip', true)
      }
    }
  },
  components: {
    SideMaskTop,
    SideMaskBottom,
    Video,
    VideoGallery,
    Setting,
    PainterTools,
    DocumentTools,
    DocumentManage,
    DocumentPlayer,
    UserListChatBox,
  },

  beforeDestroy: function () {
    this.close();
    window.onresize = null;
    this.closeVideo();
  }
}
</script>
<style lang='less'>
  .router-meeting {
    top: 0;
    width: 300px;
    right: 0;
    position: initial;
    .video-container {
      position: absolute;
      width: 300px;
      right:0;
      // height: 200px;
      top: 40px;
      // bottom: 56px;
      background: #f1f3f9;

      .document-manage {
        position: absolute;
        top: 25px;
        right: 460px;
        text-align: left;
        // z-index: 1;
        user-select: none;
      }

      .main-video {
        width: 201px;
        height: 210px;
        background: #FEFEFE;
        border: 2px solid #FFF;
        border-radius: 8px;
        box-shadow: 0px 5px 10px 0px rgba(0,0,0,0.1);
        position: absolute;
        width: 300px;
        height: 200px;
        top: 25px;
        left: 10px;
        .bjy-video-container {
          background: #fff;
          border-radius: 8px;
          .bjy-user-id {
            border-radius: 0px 0px 8px 8px;
            margin-bottom: 0;
            width: 100%;
          }
        }
        .bjy-video {
          background: #fff;
          border-radius: 8px;
          >div {
            background: #FEFEFE !important;
            border-radius: 8px;
            >video {
              border-radius: 8px;
            }
          }
        }
      }

      .sub-video-list {
        position: absolute;
        right: 30px;
        top: 25px;
        transition: right 0.5s;
        height: 200px;
        overflow-y: auto;
        .video-list {
          // overflow: auto;
          max-height: 100% !important;
        }

        .sub-video {
          pointer-events: all;
          width: 50px;
          height: 50px;
          box-shadow: 0px 5px 10px 0px rgba(0,0,0,0.1);
          background: #FEFEFE;
          border: 2px solid #FFF;
          border-radius: 8px;
          margin-bottom: 5px;
          & + .sub-video {
            margin-top: 10px;
          }
          .bjy-video {
            background: #fff;
            border-radius: 8px;
            >div {
              background: #FEFEFE !important;
              border-radius: 8px;
              >video {
                border-radius: 8px;
              }
            }
          }
          .video-cover {
            font-size: 14px;
            .camera-off {
              margin-top: -30px;

              .icon-camera-off {
                width: 64px;
                height: 64px;
              }
            }
          }
          .bjy-video-container {
            width: 100%;
            height: 100%;
            display: block;
            cursor: pointer;
            border: 1px solid #fff;
            background: #fff;
            border-radius: 2px;
            .bjy-user-id{
              display:none;
            }
          }
          .debug-pannel {
            width: 300px;
            max-height: 84px;
            font-size: 13px;
          }
        }

        .toggle {
          position: absolute;
          top: 50%;
          left: -36px;
          transform: translateY(-50px);
          width: 28px;
          height: 100px;
          background: rgba(255,255,255,0.6);
          border-radius: 8px;
          box-shadow: 0px 5px 10px 0px rgba(5,6,53,0.1);
          cursor: pointer;
          opacity: 1;
          transition: opacity 0.5s;

          .icon-triangle {
            display: inline-block;
            width: 16px;
            height: 16px;
            margin-top: 42px;
            background: url(../../assets/sprites/pannel-toggle.png);
            background-image: -webkit-image-set(
              url(../../assets/sprites/pannel-toggle@2x.png) 2x
            );
            background-position: 0 0;
          }

          &:hover {
            background: #1795ff;

            .icon-triangle {
              background-position: -16px 0;
            }
          }

          &[data-show="0"] {
            opacity: 0;
            pointer-events: none;
          }
        }
        &[data-open="0"] {
          right: -202px;

          .toggle {
            .icon-triangle {
              background-position: 0 -16px;
            }
            &:hover {
              .icon-triangle {
                background-position: -16px -16px;
              }
            }
          }
        }
      }

      .screen-frame {
        width: 280px;
        height: 44px;
        text-align: left;
        background: url(../../assets/components/share_lan.png);
        background-size: contain;
        position: absolute;
        top: 0;
        left: 50%;
        transform: translateX(-140px);

        .screen-icon {
          display: inline-block;
          width: 20px;
          height: 20px;
          margin-right: 4px;
          margin-left: 4px;
          background: url(../../assets/components/screen_icon.png);
          background-image: -webkit-image-set(
            url(../../assets/components/screen_icon@2x.png) 2x
          );
          background-size: contain;

        }
        .screen-title {
          color: #333333;
          font-size: 12px;
          line-height: 20px;
          vertical-align: top;
        }
        .screen-img {
          width: 64px;
          height: 36px;
          position: absolute;
          right: 16px;
          top: 4px;
        }
      }
    }

    .setting-dialog {
      .el-dialog__header {
        display: none;
      }
      .el-dialog__body {
        padding: 0;
        height: 500px;
      }
    }

    .whitebroad {
      position: absolute;
      user-select: none;
      width: calc(100% - 300px);
      // width: 100%;
      &[data-operat-ppt="1"] .brtm-whiteboard {
        canvas {
          pointer-events: none !important;
        }
      }
    }

    .painter-tools-container {
      position: absolute;
      top: 20px;
      left: 20px;
      opacity: 0.8;
    }

    .document-tools {
      position: absolute;
      bottom: 30px;
      left: -50px;
      opacity: 0.8;
    }
  }
  .el-tooltip__popper.is-light {
    background: rgba(255,255,255,0.8);
    border-radius: 4px;
    box-shadow: 0px 5px 10px 0px  rgba(5,6,53,0.1);
    border-color: #fff !important;
    color: #333333 !important;
    .popper__arrow {
      border: 7px solid transparent !important;
      border-top-color: #fff;
      top: 34px;
    }
  }
</style>
<style lang="scss"  scoped>
  @import './global.css';
  @import './meeting.scss';
</style>
