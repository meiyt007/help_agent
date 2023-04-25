<template>
    <el-row class="right-btn-group-two" >

    </el-row>
</template>
<script>
import store from "@/store";
import { VueCropper } from "vue-cropper";
import { uploadCaseMaterialFile,getCaseMaterialAttaList } from "@/api/zc/businessManagement/caseMaterialAtta";
export default {
  inheritAttrs: false,
  components: { VueCropper },
  props: {
    materialAtta: {
      type: Object
    }
  },
  data() {
    return {
      rules: {},
    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {
    //成功后返回
    uploadSuccess(resp){
      if (resp.code === 200) {
        getCaseMaterialAttaList(resp.data.oid).then(res => {
          this.options.img = process.env.VUE_APP_BASE_API + "/platform/security/atta/imageDisplay/"+resp.data.oid;
          store.commit('SET_AVATAR', this.options.img);
        })
      }else{
        this.msgError("文件上传失败");
      }
    },
    //失败后返回
    uploadError(resp){
      this.msgError("文件上传失败");
    },
    uploadUrl(){
      return uploadCaseMaterialFile();
    },
    beforeUpload(file) {
      let isRightSize = file.size / 1024 / 1024 < 2
      if (!isRightSize) {
        this.$message.error('文件大小超过 2MB')
      }
      return isRightSize
    },
  }
}

</script>
<style lang="scss" scoped>
.el-upload__tip {
  line-height: 1.2;
}
.right-btn-group-two {
  position: absolute;
  right:-110px;
  top: -26px;
}

</style>
