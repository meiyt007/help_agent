<template>
  <div class="app-container" @click="handleMessage">
    <svg-icon icon-class="message" style="margin-top: 16px"></svg-icon>
    <span>消息列表</span>
    <i :class="infNumClass" v-show="numFlag" v-text="numberOfUnreadMessage">
      0
    </i>
    <el-dialog
      v-dialog-drag
      v-show="false"
      :visible.sync="show"
      title="查看消息列表"
      @close="closeUserView"
      width="1000px"
      height="500px"
      scrollbar
      append-to-body
    >
      <index-message :barFlag="1" @father-click="getMessageNum"></index-message>
    </el-dialog>
  </div>
</template>

<script>
import { getCountOfUnReadMessage } from "@/api/sys/message";
import IndexMessage from '@/views/sys/message/index';
export default {
  name: 'ZfsoftMessage',
  components: { IndexMessage },
  data () {
    return {
      numberOfUnreadMessage: 0,
      numFlag: false,
      show: false,
      infNumClass: 'inf-num'
    }
  },
  created () {
    this.getMessageNum();
    /*  setInterval(() => {
        this.getMessageNum();
      }, 120000);*/
  },
  methods: {
    getMessageNum () {
      getCountOfUnReadMessage().then(response => {
        if (response.data > 0) {
          this.numFlag = true;
          this.numberOfUnreadMessage = response.data;
          if (this.numberOfUnreadMessage > 100) {
            this.numberOfUnreadMessage = '99+';
            this.infNumClass = 'inf-num1';
          } else {
            this.infNumClass = 'inf-num';
          }
        } else {
          this.numFlag = false;
        }
      }).catch(function () {
        this.numberOfUnreadMessage = 0;
      });
    },
    handleMessage () {
      this.show = true;
    },
    closeUserView () {
      this.getMessageNum();
      this.show = false;
    }
  }
}
</script>
<style>
.inf-num {
  display: block;
  position: absolute;
  left: 32px;
  top: 3px;
  width: 16px;
  height: 15px;
  background-color: #ff0000;
  border-radius: 100%;
  color: #fff;
  font-size: 9px;
  line-height: 15px;
  text-align: center;
  font-style: normal;
}
.inf-num1 {
  display: block;
  position: absolute;
  left: 32px;
  top: 3px;
  width: 26px;
  height: 15px;
  background-color: #ff0000;
  border-radius: 100%;
  color: #fff;
  font-size: 9px;
  line-height: 15px;
  text-align: center;
  font-style: normal;
}
</style>
