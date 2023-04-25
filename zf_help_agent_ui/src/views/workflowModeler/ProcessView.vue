<template>
    <div>
      <div>
        <Viewer ref="viewer" :params="params" @handleClose="handleClose"></Viewer>
      </div>
      <!--<span slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
    </span>-->
    </div>
</template>


<script>
import Viewer from './components/Viewer'
  export default {
    //props: ['dialogViewModelVisible', 'params'],
    components: {
      Viewer
    },
    data() {
      return {
        params:{},
      };
    },
    created() {
      this.initProcessView();
    },
    methods: {
      handleClose() {
        this.$emit('handleViewModelVisiable', { visiable: false })
      },
      initProcessView(){
        let infoOid = this.getUrlKey('infoOid');
        let processDefId = this.getUrlKey('processDefId');
        this.params.infoOid = infoOid;
        this.params.processDefId = processDefId;
      },
      getUrlKey: function (name) {
        return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null
      },
    }
  };
</script>
