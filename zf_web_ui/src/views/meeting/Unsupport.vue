<template>
  <div class="router-item router-unsupport">
    <div class="center">
      <div class="top"></div>
      <div class="tip">当前浏览器暂不支持，请下载最新版本的 Chrome 浏览器。</div>
      <Button class="download-btn" type="primary" @click="download">下载 Chrome 浏览器</Button>
    </div>
  </div>
</template>

<script>
import { Button } from 'element-ui'
export default {
  name: 'Unsupport',
  data () {
    return {
      
    }
  },

  mounted: function () {
    
  },

  methods: {
    download: function () {
      window.open('https://www.google.cn/chrome/', '_blank')
    }
  },
  components: {
    Button
  }
}
</script>
<style lang='less' scoped>
  .router-unsupport {
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    background: url(../../assets/unsupport/background.png) no-repeat;
    background-image: -webkit-image-set(url(
      ../../assets/unsupport/background@2x.png
    ) 2x);
    background-size: cover;

    .center {
      width: 389px;
      background: #ffffff;
      border-radius: 8px;
      padding: 42px;

      .top {
        width: 305px;
        height: 135px;
        background: url(../../assets/unsupport/unsupport.png) no-repeat;
        background-image: -webkit-image-set(url(
          ../../assets/unsupport/unsupport@2x.png
        ) 2x);
      }
      .tip {
        margin-top: 21px;
        font-size: 16px;
        font-weight: 400;
        text-align: left;
        color: #595959;
        line-height: 24px;
      }
      .el-button {
        margin-top: 50px;
        border-radius: 20px;
        width: 240px;
        background: linear-gradient(270deg,#84bdff, #4d88fe);
        margin-bottom: 60px;
      }
    }
  }
</style>
