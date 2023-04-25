<template>
  <div>
    <div>
      <Modeler ref="modeler" @handleClose="handleClose" @saveHandleClose="saveHandleClose" :params="params"></Modeler>
    </div>
    <!--<span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="handleOk">确 定</el-button>
    <el-button @click="handleClose">取 消</el-button>
  </span>-->
  </div>
</template>


<script>
import Modeler from './components/Modeler'
  export default {
    components: {
      Modeler
    },
    data() {
      return {
        params:{}
      };
    },
    created() {
      let oid = this.getUrlKey('infoOid');
      this.params.infoOid = oid;
    },
    methods: {
      handleClose(done) {
        this.$emit('handleVisiable')
      },
      saveHandleClose(){
        this.$emit('saveHandleClose')
      },
      getUrlKey: function (name) {
        return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null
      },
    }
  };
</script>
<style>
  .el-dialog__body{
    padding: 0px 5px;
  }
  .containers {
    background: white;
    overflow: auto;
    background-image: linear-gradient(
      90deg,
      rgba(220, 220, 220, 0.5) 6%,
      transparent 0
    ),
    linear-gradient(rgba(192, 192, 192, 0.5) 6%, transparent 0);
    background-size: 12px 12px;
    width: 100%;
    height: calc(100vh - 82px);
    -webkit-tap-highlight-color: rgba(255, 255, 255, 0);
  }
</style>
