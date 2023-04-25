<template>
  <div
    v-loading="loading"
    :style="'height:' + height"
    style="position: relative"
  >
    <iframe
      ref="caseFormIframe"
      :src="src"
      frameborder="0"
      width="100%"
      :height="iframeHeight"
      scrolling="auto"
      id="caseFormIframe"
    />
    <div slot="footer" align="center" class="hideIframeBackground">
      <el-button @click="colseDialog">关 闭</el-button>
      <!--        <el-button type="primary" @click="saveFormInfo">确 定</el-button>-->
    </div>
  </div>
</template>
<script>
export default {
  name: "FormIframe",
  props: ['outLink'],
  data () {
    return {
      src: this.outLink,
      reportOid: "",
      height: document.documentElement.clientHeight - 94.5 + "px;",
      iframeHeight: document.documentElement.clientHeight - 94.5 + "px;",
      loading: true,
    };
  },
  mounted: function () {
    setTimeout(() => {
      this.loading = false;
    }, 230);
    const that = this;
    window.onresize = function temp () {
      that.height = document.documentElement.clientHeight - 94.5 + "px;";
    };
  },
  methods: {
    saveFormInfo () {
      let urlArray = this.src.split("?");
      let ss = process.env.VUE_APP_DZBD_BC_ROUTE_PATH + urlArray[1];//"&formOid=FORM20180926HFJUOA6N";
      let msg = { loginName: process.env.VUE_APP_DZBD_ROUTE_PATH, loginPassword: "listenerPassword" };
      let flag = false;
      let _that = this;
      this?.$refs?.caseFormIframe?.contentWindow?.postMessage?.(msg, ss);
      window.addEventListener("message", function (obj) {
        const str = obj.data;
        if (str) {
          let strJSON = JSON.parse(str)
          flag = strJSON.success
          if (strJSON.success) {
            _that.reportOid = strJSON.data;
            _that.colseDialog();
          }
        }
      }, false);
    },
    colseDialog () {
      this.$emit('closeIframeView', this.reportOid);
      this.reportOid = "";
    }
  }
};
</script>
<style scoped>
.hideIframeBackground {
  width: 100%;
  height: 50px;
  background-color: #fff;
  position: absolute;
  bottom: 30px;
  left: 0;
}
</style>
