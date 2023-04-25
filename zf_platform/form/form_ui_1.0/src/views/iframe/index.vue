<template>
  <div v-loading="loading" :style="'height:'+ height">
    <iframe :src="src" frameborder="no" style="width: 100%;height: 100%" scrolling="auto" />
  </div>
</template>
<script>
  import store from "../../store";
export default {
  name: "Iframe",
  props:['outLink'],
  data() {
    return {
      src:  '',
      height: document.documentElement.clientHeight - 94.5 + "px;",
      loading: true
    };
  },
  created() {
    if(null!=store.getters.userOid && ''!=store.getters.userOid){
        this.src = this.outLink + '?userOid='+store.getters.userOid;
    }else{
      this.src = this.outLink;
    }
  },
  mounted: function() {
    setTimeout(() => {
      this.loading = false;
    }, 230);
    const that = this;
    window.onresize = function temp() {
      that.height = document.documentElement.clientHeight - 94.5 + "px;";
    };
  }
};
</script>
