<!-- 其他服务-自助打证-预览打印 -->
<template>
  <div class="app-container">
      <div
        id="licPreview"
        :style="{
          width: rectStyle,
          maxHeight: '670px',
          overflow: 'hidden auto'
        }"
      ></div>
      <div class="operation-btns">
        <el-button type="primary" @click.native="save">打印</el-button>
        <el-button @click="viewDialog">关 闭</el-button>
      </div>
  </div>
</template>
<script>
import "../../../../../public/lrcPrint/print.js"
import {savePrintRecord} from "@/api/onelicence/fzgl/licenseSignIndustry";
export default {
  name: "previewPrint",
  props:["industryCaseOid","resultOid","licenseName","faModelTemplateOid","elementValueList","zcPath","industryPath","metaDataJsonArrayStr"],
  data() {
    return {
      rectStyle: "1550px",
    };
  },
  mounted() {
    let _that =this;
    _that.initReact(1);
    window.addEventListener(
      "resize",
      () => {
        _that.initReact();
      },
      false
    );
  },
  methods: {
    // 初始化打印预览
    initReact(val) {
      let _that =this;
      let dom = document.body.clientWidth;
      if (dom === 1280) {
        _that.rectStyle = "1100px";
      } else {
        _that.rectStyle = "1550px";
      }
      this.$nextTick(() => {
        if (val) {
          _that.openFullScreen();
        }
        try {
          LIC_PRINT.setCtxPath(_that.zcPath);
          LIC_PRINT.setFilePlatAddr(_that.industryPath);
          LIC_PRINT.setResultOid(_that.resultOid);
          LIC_PRINT.setFaModelTemplateOid(_that.faModelTemplateOid);
          LIC_PRINT.setElementValues(_that.elementValueList);
          LIC_PRINT.setMetaDataJsonArrayStr(_that.metaDataJsonArrayStr);
          LIC_PRINT.previewLic(document.querySelector("#licPreview"), () => {
              _that.closeFullScreen(_that.openFullScreen());
          });
        } catch (error) {
          console.log(error);
          _that.closeFullScreen(_that.openFullScreen());
        }
      });
    },
    save() {
      let record ={};
      record.industryCaseOid=this.industryCaseOid;
      record.resultOid=this.resultOid;
      record.licenseName=this.licenseName;
      savePrintRecord(record).then(response => {
        if(response.data !=null){
           this.printLicese();
           this.$emit('case-close');
        }
      })
    },
    // 打印
    printLicese() {
      let _that =this;
      try {
        _that.openFullScreen();
        // 打印 回调函数/是否传递打印地图true 是， false 不打印
        LIC_PRINT.printLic(function() {
          setTimeout(() => {
            _that.closeFullScreen(_that.openFullScreen());
          }, 5000);
        }, true);
        _that.closeFullScreen(_that.openFullScreen());
      } catch (error) {
        _that.closeFullScreen(_that.openFullScreen());
      }
    },
    openFullScreen() {
      let _that =this;
      const loading = _that.$loading({
        lock: true,
        text: '正在加载中...',
        spinner: 'el-icon-loading',
        background: 'rgba(94,89,89,0.7)'
      });
      return loading;
    },
    closeFullScreen(loading){
      loading.close();
    },
    viewDialog(){
      let _that =this;
      _that.closeFullScreen(_that.openFullScreen());
      _that.$emit('case-close');
    },
  }
};
</script>
