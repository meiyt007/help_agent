<template>
    <div>
      <div>
        <FlowViewer ref="viewer" :params="params" @handleClose="handleClose"></FlowViewer>
      </div>
      <!--<span slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
    </span>-->
    </div>
</template>


<script>
import FlowViewer from './components/FlowViewer'
  export default {
    components: {
      FlowViewer
    },
    data() {
      return {
        params:''
      };
    },
    created() {
      this.params = this.getUrlKey('processInstanceId');
    },
    methods: {
      handleClose() {
        this.$emit('handleFlowViewModelVisiable')
      },
      getUrlKey: function (name) {
        return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null
      }
    }
  };
</script>
