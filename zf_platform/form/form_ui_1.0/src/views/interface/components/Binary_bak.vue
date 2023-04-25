<template>
  <div>
    <div>
      <el-upload
        class="upload-demo"
        drag
        action="#"
        :on-change="handleChange"
        :on-remove="handleRemove"
        :http-request="httpRequest"
        >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <!--<div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>-->
      </el-upload>
    </div>
  </div>
</template>

<script>
    export default {
        name: "Binary_bak",
        props: ['value'],
        data: function () {
            return {
                fileBase64:this.value
            }
        },
        methods: {
          handleChange(file, fileList) {
            if (fileList.length > 1) {
              fileList.shift();
            }
          },
          handleRemove(file, fileList) {
            fileList.shift();
            this.fileBase64 = '';
          },
          // 选取图片后自动回调，里面可以获取到文件
          httpRequest(data){  // 也可以用file
            let _this = this  // 这里要转一下是因为在下面的function里 this的指向发生了变化
            let rd = new FileReader()
            let file = data.file
            rd.readAsDataURL(file)
            rd.onloadend = function(e) {
              //console.log(rd.result)
              _this.fileBase64 = file.name + ',' + rd.result;
            }
            },
            updateLocal: function (msg) {
                this.local = msg;
            },
            addText: function (index) {
                this.$refs.inCoder.addScript(this.tips[index].script);
            }
        },
        watch: {
          fileBase64: function () {
                this.$emit('dataChanged', this.fileBase64);
            }
        }
    }
</script>

<style scoped>

</style>
