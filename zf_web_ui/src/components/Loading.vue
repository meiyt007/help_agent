
<template>
  <div class="loading-container">
    <div class="loader-spinner">
        <div></div>
        <div></div>
        <div></div>
        <div></div>
        <div></div>
    </div>
    <span class="bjy-status-text">{{status}}</span>
  </div>
</template>

<script>

export default {
  name: 'Loading',
  data () {
    return {
     
    }
  },
  props: {
    status: String,
  },
  methods: {
  },
  mounted () {
  },
  components: {
  }
}
</script>
<style scoped lang="less">
  .loading-container {
    position: absolute;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    background-color: #1B1B22;
    opacity: 0.7;
    text-align: center;
    display: flex;
    justify-content: center;
    align-items: center;

    .status-text {
      display: inline-block;
      color: #fff;
      margin-top: 8px;
    }
    .loader-spinner {
      > div {
        width: 4px;
        height: 35px;
        background-color: #fff;
        border-radius: 2px;
        margin: 2px;
        display: inline-block;
        animation-fill-mode: both;
        animation: bjy-line-scale-pulse-out 0.9s 0s infinite cubic-bezier(.85, .25, .37, .85);
      }
      > div:nth-child(2),
      > div:nth-child(4) {
        animation-delay: 0.2s;
      }
      > div:nth-child(1),
      > div:nth-child(5) {
        animation-delay: 0.4s;
      }
    }
  }
@keyframes bjy-line-scale-pulse-out {
  0% {
    transform: scaley(1);
  }
  50% {
    transform: scaley(0.4);
  }
  100% {
    transform: scaley(1);
  }
}
</style>
