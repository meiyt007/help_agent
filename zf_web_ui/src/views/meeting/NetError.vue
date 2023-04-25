<template>
  <div class="router-item router-neterr">
    <img src='../../assets/components/neterr_bg.png'
      class='neterr-bg'
    />
    <span class='neterr-label'>网络异常，请检查您的网络，点击重试！</span>
    <Button type='primary'
      class='retry-btn'
    >重试</Button>
  </div>
</template>

<script>

import { Button } from 'element-ui'
export default {
  name: 'NetError',
  data () {
    return {
    }
  },
  methods: {
  },
  mounted: function() {
    if (navigator.onLine) {
      this.$router.push({path: 'index'})
    }
  },
  components: {
    Button
  }
}
</script>
<style lang='less' scoped>
  .router-neterr{
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    .neterr-bg{
      width: 400px;
      height: 400px;
    }
    .neterr-label{
      color: #666;
      font-size: 20px;
      margin: 20px 0;
    }
    .retry-btn{
      width: 100px;
      height: 40px;
    }
  }
</style>
