<template>
  <FormReport ref="formReport" :baseFormData="baseFormData" :reportForm="reportForm" :checkFlag="true" :isShowDefaultVal="true" :formNames="formNames" :isShowExportPdfBtn="isShowExportPdfBtn" @closeFormView="closeFormView" @sendRes="sendRes"></FormReport>
</template>

<script>
  import '@babel/polyfill'
  export default {
    components: {},
    data() {
      return {
        authorizeKey: "",
        origin:'',
        reportForm:{
          designOid :'',
          formData :'',
          reportOid :'',
        },
        baseFormData: {

        },
        formNames:'',
        saveBackFormData:{},
        isShowExportPdfBtn:false,
      };
    },
    mounted() {
        const that = this;
        window.addEventListener('message', function(event) {
          const origin = event.origin;
          that.origin = origin;
          //event.data获取传过来的数据
          //console.log(event.data)
          //验证必填
          if(event.data == 'validateForm'){
            return that.validateForm().then(function onFulfilled(value){
              if(value.status){
                window.parent.postMessage('validateForm-true',origin);
              }else {
                window.parent.postMessage('validateForm-false',origin);
              }
            }).catch(function onRejected(error){
              window.parent.postMessage('validateForm-false',origin);
            });
          }
          if(event.data == 'saveFormData'){
            that.saveFormData().then(function onFulfilled(value){
              window.parent.postMessage('saveFormData-'+that.getReportForm(),origin);
            }).catch(function onRejected(error){
              window.parent.postMessage('saveFormData-',origin);
            });
          }

          //获取表单的高度
          if(event.data == 'getFormHeight'){
            setTimeout(function() {
              let offsetHeight = that.$refs.formReport.$el.offsetHeight;
              //console.log('offsetHeight',offsetHeight)
              window.parent.postMessage('getFormHeight-'+offsetHeight,origin);
            },500)
          }
          if(event.data == 'getFormData'){
            let formDataStr = JSON.stringify(that.$refs.formReport.getFormData());
            window.parent.postMessage('getFormData-'+formDataStr,origin);
          }
          if('object' != typeof (event.data) && event.data.indexOf('setFormData') == 0){
            let data = event.data.replace('setFormData-','');
            let formData = eval("(" + data + ")");
            that.setFormData(formData);
          }
          if('object' != typeof (event.data) && event.data.indexOf('setInitBaseFormData') == 0){
            let data = event.data.replace('setInitBaseFormData-','');
            let initBaseFormData = eval("(" + data + ")");
            that.setInitBaseFormData(initBaseFormData);
          }
          if(event.data == 'getReportForm'){
            setTimeout(function () {
              // let formDataStr = JSON.stringify(that.$refs.formReport.getReportForm());
              window.parent.postMessage('getReportForm-'+that.getReportForm(),origin);
            },1000);
          }
          if (event.data == 'getFormUiHeight'){
            let formTag = that.reportForm.formCode?that.reportForm.formCode: that.reportForm.designOid;
            let height = Math.max(document.body.scrollHeight, document.documentElement.scrollHeight);
            // window.parent.postMessage('getFormUiHeight-'+that.getFormUiHeight()+"@"+formTag,'*');
            window.parent.postMessage('getFormUiHeight-'+height+"@"+formTag,'*');
          }
        }, true);

    },
    beforeDestroy() {
      if(''!=this.origin){
        window.removeEventListener('message', this.origin)
      }
    },
    methods:{
      //验证必填
      validateForm(){
        return this.$refs.formReport
          .validateForm();
      },
      //保存成功关闭
      closeFormView(){
        //window.parent.initFormView();
        if(''!=this.origin){
          //window.parent.postMessage('close',this.origin);
        }
      },
      //获取表单数据
      getFormData() {
        return this.$refs.formReport
          .getFormData();
      },
      //设置表单数据
      setFormData(data) {
        this.$refs.formReport
          .setFormData(data);
      },
      //设置动态表单数据 已填数据不会覆盖
      setInitBaseFormData(data) {
        this.$refs.formReport
          .initBaseFormData(data);
      },
      //保存表单数据
      saveFormData() {
        return this.$refs.formReport.saveFormData();
      },
      //保存表单数据后回调
      sendRes(res){
        this.msgSuccess("保存成功");
        this.closeFormView();

        this.saveBackFormData = res;
      },
      //获取保存后的主键
      getReportForm() {
        return this.saveBackFormData;
      },
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
      this.reportForm.formCode = queryPar.formCode;
      this.reportForm.authorizeKey = queryPar.authorizeKey;
      this.reportForm.reportOid = queryPar.reportOid;
      this.reportForm.designOid = queryPar.designOid;
      this.reportForm.formData = queryPar.formData;
      this.formNames = queryPar.formNames;

    },
  };
</script>

<style>

</style>
