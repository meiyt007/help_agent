<template>
  <div class="router-item router-join" style="display: none;">
    <div class='small-container'>
      <h1 class='title'>加入通话</h1>
      <Input v-model="roomNumber" placeholder="请输入房间号"
        class='room-input'
      />
      <Input v-model="userId" placeholder="请输入用户 ID"
        class='name-input' type="text"
      />
      <Input v-model="userName" placeholder="请输入用户名（选填）"
        class='name-input' type="text"
      />
      <div class='deviceSetting'>
        <Checkbox v-model="cameraOn" >开启摄像头</Checkbox>
        <Checkbox v-model="micOn" >开启麦克风</Checkbox>
      </div>
      <Button class="join-btn" type="primary"
        @click='joinMeetFn'
      >立即开始</Button>
    </div>
  </div>
</template>

<script>
import { Button, Input, Checkbox } from 'element-ui'
import {createRoom} from "@/api/modules/video";
export default {
  name: 'Join',
  data () {
    return {
      roomNumber: '1000', //'' + Math.floor(Math.random() * 1000000000),
      userId: '100',
      userName: '',
      cameraOn: true,
      micOn: true
    }
  },
  watch: {
    // userId: function (newValue, oldValue) {
    //   if (newValue) {
    //     this.userId = newValue.replace(/[^0-9]/g, '')
    //   }
    // }
  },

  mounted: function () {
    // 创建视频帮办房间
    let params = {
      name:'王宇航',
      cardNo:'342422199902283295',
      phone:'17317731102',
      companyName:'',
      companyCode:'',
      workUserIds:'',
      takeNumberType:'5',
      groupId:'240497770774106112',
      haType:'2'
    }
    createRoom(params).then(res=>{
      localStorage.setItem("mainSig",res.data.sig);
      localStorage.setItem("appId",res.data.appId);
      localStorage.setItem("roomId",res.data.roomId);
      this.roomNumber = res.data.roomNumber;
      this.userId = res.data.videoNum; 
      BRTC.getPermissions().then(() => {
        console.log('获取权限成功')
        this.joinMeetFn();
      }, (error) => {
        console.log(error)
      })
    })
  },

  methods: {
    joinMeetFn(){
      if (!this.userId) {
        this.$message({
          type: 'error',
          message: '请输入用户id'
        })
        return
      }
      if (!this.roomNumber) {
        this.$message({
          type: 'error',
          message: '请输入房间号'
        })
        return
      }

      const userId = +this.userId
      const userName = this.userName + ''

      if (userId == 0) {
        this.$message({
          type: 'error',
          message: 'user id 不能为 0'
        })
        return
      }

      // if (userId > Math.pow(2, 31) - 1 || userId < -Math.pow(2, 31) || Math.floor(userId) !== userId) {
      //   this.$message({
      //     type: 'error',
      //     message: '用户 ID 为不超过 11 位数的数字'
      //   })
      //   return
      // }
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
      this.$router.push({path:'meeting'})
    }
  },
  components: {
    Input,
    Button,
    Checkbox
  }
}
</script>
<style lang='less' scoped>
  .router-join{
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    background: url(../../assets/join/join_bg.png) no-repeat;
    background-size: cover;
    .small-container{
      width: 480px;
      box-sizing: border-box;
      display: flex;
      flex-direction: column;
      align-items: center;
      background: #fff;
      border-radius: 8px;
      padding: 80px 90px 80px 90px;
      .title{
        color: #333333;
        font-size: 20px;
        margin: 0;
        margin-bottom: 20px;
      }
      .room-input{
        margin-bottom: 20px;
      }
      .name-input{
        margin-bottom: 10px;
      }
      .join-btn{
        margin-top: 44px;
        width: 100%;
      }
    }
    .switch-webrtc-type {
      margin-top: 20px;
    }
    .debug-token {
      margin-top: 20px;
    }
    .debug-channel {
      margin-right: 25px;
    }
    .el-button {
      line-height: 0;
      padding: 19px 20px;
    }

    .appid-input /deep/ .el-input__inner {
      height: 28px;
      width: 244px;
      margin-top: 6px;
    }

    .codec {
      margin-top: 10px;
      .el-select {
        width: 115px;
        & /deep/ .el-input__inner {
          height: 28px;
        }
        & /deep/ .el-input__icon {
          line-height: 28px;
        }
      }
      .bitrate-input {
        width: 52px;

        & /deep/ .el-input__inner {
          height: 28px;
          padding: 0 5px;
        }
      }
      .label {
        font-size: 14px;
        padding-left: 6px;
      }
    }
  }
</style>
<style lang="scss" scoped>
  @import './global.css';
</style>
