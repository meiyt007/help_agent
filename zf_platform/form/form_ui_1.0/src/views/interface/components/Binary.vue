<template>
  <div>
    <div>
      <el-upload
        class="upload-demo"
        drag
        :on-change="handleChange"
        :on-remove="handleRemove"
        :action="uploadUrl()"
        :on-error="uploadError"
        :on-success="uploadSuccess"
        >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <!--<div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>-->
      </el-upload>
    </div>
  </div>
</template>

<script>
  import { uploadFile } from "@/api/interface/manager";
    export default {
        name: "Binary",
        props: ['value'],
        data: function () {
            return {
                fileUrl:this.value,
                type:''
            }
        },
        methods: {
          //成功后返回
          uploadSuccess(resp,file, fileList){
            if(200 !== resp.code){
              fileList.shift();
              this.fileUrl = '';
              return this.msgError(resp.message);
            }else {
              this.fileUrl = resp.data.name +'~'+ this.mediaType + '~'+ resp.data.url;
            }

          },
          //失败后返回
          uploadError(resp){
            this.msgError("文件上传失败");
          },
          uploadUrl(){
            return uploadFile();
          },
          handleChange(file, fileList) {
            if (fileList.length > 1) {
              fileList.shift();
            }
            this.mediaType = file.raw.type;
          },
          handleRemove(file, fileList) {
            fileList.shift();
            this.fileUrl = '';
          },
        },
        watch: {
          fileUrl: function () {
                this.$emit('dataChanged', this.fileUrl);
            }
        }
    }
</script>

<style scoped>

</style>
