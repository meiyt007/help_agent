<template>
  <FormView ref="formView" :designOid="designOid" :formNames="formNames" :isShowExportPdfBtn="isShowExportPdfBtn" :reportOid="reportOid" :initFormData="initFormData" :formCode="formCode" :authorizeKey="authorizeKey" :isShowDefaultVal="isShowDefaultVal" :disabled="true"/>
</template>

<script>
  export default {
    components: {},
    data() {
      return {
        authorizeKey: "",
        formCode: "",
        designOid: "",
        formNames:'',
        initFormData:{},
        isShowDefaultVal: false,
        isShowExportPdfBtn:false,
        origin:''
      };
    },
    mounted() {
      const that = this;
      window.addEventListener('message', function(event) {
        const origin = event.origin;
        that.origin = origin;
        //获取表单的高度
        if(event.data == 'getFormHeight'){
          setTimeout(function() {
            let offsetHeight = that.$refs.formView.$el.offsetHeight;
            //console.log('offsetHeight',offsetHeight)
            window.parent.postMessage('getFormHeight-'+offsetHeight,origin);
          },500)
        }
      }, true);
    },
    created() {
      const queryPar = this.$route.query;
      if (undefined != queryPar.isShowExportPdfBtn && '1' == queryPar.isShowExportPdfBtn) {
        this.isShowExportPdfBtn = true;
      }
      this.authorizeKey = queryPar.authorizeKey;
      if (undefined == this.authorizeKey || '' == this.authorizeKey) {
        this.msgError("表单授权key不能为空！");
        return false;
      }
      this.formCode = queryPar.formCode;
      this.designOid = queryPar.designOid;
      this.reportOid = queryPar.reportOid;
      this.formNames = queryPar.formNames;
      this.initFormData = queryPar.initFormData;
      this.isShowDefaultVal = queryPar.isShowDefaultVal;
    },
  };
</script>

<style>

</style>
