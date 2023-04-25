<template>
  <div class="onething-once-in-form">
    <div id="print" ref="print">
      <h3
        style="
          display: block;
          margin-block-start: 1em;
          margin-block-end: 1em;
          margin-inline-start: 0px;
          margin-inline-end: 0px;
          font-weight: bold;
          text-align: center;
          font-size: 27px;
          margin-top: 15px;
          margin-bottom: 15px;
        "
      >
        《{{ comboDirectoryName }}》一次性告知单
      </h3>

      <table class="zf-zc-table">
        <colgroup>
          <col width="20%" />
          <col width="80%" />
        </colgroup>
        <tr v-for="item in informLabel" :key="item.id">
          <td
            style="
              padding-right: 20px;
              font-size: 16px;
              font-weight: bold;
              color: #2a344c;
            "
          >
            {{ item.informTitle }}
          </td>
          <!-- 字体不能是仿宋的不然无法使得字体变粗 -->
          <td
            style="text-indent: 1em; font-size: 16px !important; color: #2a344c"
            v-html="item.informContent"
          ></td>
        </tr>
      </table>
    </div>
  </div>
</template>
<script>
export default {
  name: "OnceInForm",
  inheritAttrs: false,
  props: {
    comboDirectoryOid: {
      type: String,
      default: ""
    },
    comboDirectoryName: {
      type: String,
      default: ""
    },
    informLabel: {
      type: Array,
      default: () => []
    },
  },
  data () {
    return {

    };
  },
  created () {
    this.initServiceInfo();
  },
  methods: {
    /** 初始化事项 */
    initServiceInfo () {
      setTimeout(() => {
        this.clickPrinting();
      }, 1000);
    },

    /** 点击打印按钮 */
    clickPrinting () {
      this.$nextTick(() => {
        document.getElementById('trueBtn').click();
      });
    }
  }
};
</script>
<style lang="scss" scoped>
.onething-once-in-form {
  height: 100%;
}

@page {
  margin: 15cm;
}
</style>
