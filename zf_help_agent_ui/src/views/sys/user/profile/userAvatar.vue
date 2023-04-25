<template>
  <div>
    <img v-bind:src="options.img"  class="img-circle img-lg" />
    <el-upload
      class="upload-demo"
      :action="uploadUrl()"
      :headers="getToken()"
      :on-error="uploadError"
      :before-upload="beforeUpload"
      :show-file-list="false"
      :on-success="uploadSuccess">
      <el-button size="small">
        上传
        <i class="iconfont zfsoft-wenjianshangchuan"></i>
      </el-button>
    </el-upload>
  </div>
</template>

<script>
  import store from "@/store";
  import { VueCropper } from "vue-cropper";
  import { uploadCompressImage } from "@/api/sys/atta";
  import { uploadAvatar } from "@/api/sys/user";
  import { getToken, setToken, removeToken } from '@/utils/auth'

  export default {
    components: { VueCropper },
    props: {
      user: {
        type: Object
      }
    },
    data() {
      return {
        // 是否显示弹出层
        open: false,
        // 是否显示cropper
        visible: false,
        // 弹出层标题
        title: "修改头像",
        options: {
          img: store.getters.avatar, //裁剪图片的地址
          autoCrop: true, // 是否默认生成截图框
          autoCropWidth: 200, // 默认生成截图框宽度
          autoCropHeight: 200, // 默认生成截图框高度
          fixedBox: true // 固定截图框大小 不允许改变
        },
        previews: {}
      };
    },
    methods: {
      getToken() {
        return {Authorization: 'Bearer ' + getToken()}
      },
      //成功后返回
      uploadSuccess(resp){
        if (resp.code === 200) {
          uploadAvatar(resp.data.oid).then(res => {
            //this.options.img = resp.data.url;
            this.options.img = process.env.VUE_APP_BASE_API + "/platform/security/atta/imageDisplay/"+resp.data.oid + '?access_token=' +  getToken();
            store.commit('SET_AVATAR', this.options.img);
          })
        }else{
          this.msgError(resp.message);
        }
      },
      //失败后返回
      uploadError(resp){
        this.msgError("文件上传失败");
      },
      uploadUrl(){
        return uploadCompressImage();
      },
      //上传之前
      beforeUpload(file) {
        if(file.name.indexOf("%00") > -1 || file.name.indexOf("./") > -1 || file.name.indexOf(".\\") > -1 ) {
          this.msgError('上传文件名称非法！');
          return false;
        }
        var type = this.fileByType(file.name);
        if(null != type && type !="jpg" && type !="png"&& type !="jpeg"
          && type != "gif" && type !="bmp" ) {
          this.msgError('请上传gif、jpg、jpeg、png或bmp格式的文件！');
          return false;
        }
        const fileSize = file.size;
        if (0 == fileSize) {
          this.msgError('上传文件不能为空！');
          return false;
        }
        const isLt2M = file.size / 1024 / 1024 < 100;
        if (!isLt2M) {
          this.msgError('上传文件大小不能超过 100MB！');
        }
        return isLt2M;
      },
      /**
       * 通过文件后缀返回文件
       */
      fileByType(path) {
        return path.substring(path.lastIndexOf(".") + 1, path.length);
      },
    }
  };
</script>
