<template>
  <div class="welcome-content">
  <!--<img src="@/assets/image/welcome-picv3.1.png" alt="" srcset="">-->
   <img :src="bgImage" alt="" srcset="" :onerror="defaultImg">
  </div>
</template>

<script>
  export default {
    data() {
      return {
        bgImage:'',
      }
    },
    created() {
      try {
        this.bgImage = require('@/assets/image/welcome-pic'+this.Common.version+'.png');
      }catch (e) {

      }

    },
    methods: {

    },
    computed: {
      defaultImg () {
        return 'this.src="' + require('@/assets/image/welcome-pic.png') + '"';
      }
    }
  }
</script>
<style lang="scss" scoped>

  .welcome-content{
    background: url(~@/assets/image/welcome-bg.png) no-repeat;
    width: 100%;
    min-height: calc(100vh - 100px);
    text-align: center;
    padding-top:15%;
    box-sizing: border-box;
  }

</style>
