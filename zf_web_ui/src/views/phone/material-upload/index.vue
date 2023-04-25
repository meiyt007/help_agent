<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-19 09:43:15
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-10-18 16:35:00
 * @FilePath: \zf_web_ui\src\views\phone\material-upload\index.vue
 * @Description: 扫码上传页面
-->
<template>
  <div class="container">
    <div class="materialUpload">
      <p class="dialogContentTitle">材料上传</p>
      <p class="remark remark-title">注：</p>
      <p class="remark">1. 允许上传文件格式：JPG、PNG、PDF；</p>
      <p class="remark">2. 最大允许上传文件大小：20M；</p>
      <div class="uploadArea">
        <!-- <el-upload class="uploadFileList" action :http-request="uploadFiles" :before-upload="beforeUpload"
                   :on-error="uploadError" :file-list="fileList" :show-file-list="showFileList">
          <div slot="default" class="operate">
            <i class="el-icon-plus"></i>
            <span class="el-upload__text">点击上传</span>
          </div>
        </el-upload> -->
        <div class="list" >
          <div class="item"   :key="index" v-for=" (img, index) in qlCaseMaterialAttaList">
            <el-image style="width: 100px; height: 100px" 
              :src="img.src" v-if="img.src" 
              :preview-src-list="srcList"
              @click="handlePictureCardPreview(img.src)"
              >
            </el-image>
            <i class="el-icon-circle-close" @click="cancelImg(index)"></i>
          </div>
          <el-upload ref="upload" class="uploadFileList" list-type="picture-card" action :before-upload="beforeUpload"
          :http-request="uploadFiles"  :on-remove="handleRemove"
          :on-change="handleFileChange" :before-remove="handleFileRemove" :file-list="upload.fileList">

            <div slot="default" class="operate">
              <i class="el-icon-plus"></i>
              <span class="el-upload__text">点击上传</span>
            </div>
          </el-upload>
        </div>
        <el-dialog title="预览" :visible.sync="dialogVisible">
          <img width="100%" :src="dialogImageUrl" alt="" />
        </el-dialog>
      </div>
    </div>
    <div class="upload-footer">
      <p class="footBtn" @click="resetFile">重置</p>
      <p class="footBtn" @click="submitFileForm">确认</p>
    </div>
  </div>
</template>
<script>
import {
  outerUploadCaseMaterialFile,
  outerSaveOrUpdateCaseMaterialAttaList,
  queryQlCaseMaterialAttaByCaseMaterialOid
} from "@/api/modules/business.js";
export default {
  name: "materialUpload",
  data() {
    return {
      dialogImageUrl: "",
      dialogVisible: false,
      fileList: [],
      showFileList: false,
      qlCaseMaterialAttaList: [],
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        // 上传的地址
        url: "#",
        fileList: [],
        fileName: [],
        materialData: {},
      },
      loading: '',
      srcList: [], //预览列表
      url: '',
    };
  },
  mounted() {
    this.materialData = this.$route.query;
    this.getCurrentList();
  },
  methods: {
    //删除文件
    cancelImg(index){
      this.qlCaseMaterialAttaList.splice(index,1);
    },
    //获取当前文件列表
    getCurrentList() {
      let that = this;
      queryQlCaseMaterialAttaByCaseMaterialOid({ caseMaterialOid: this.materialData.caseMaterialOid }).then(res => {
        console.log(res);
        if (res.code == 200) {
          if (res.data.length) {
            res.data.forEach(function (list) {
              const obj = {
                attaOid: list.attaOid,
                caseMaterialOid: that.materialData.caseMaterialOid,
                src:list.qlSysAtta.fastdfsNginxUrl,
              };
              that.qlCaseMaterialAttaList.push(obj);
            })

          }
        }
      })
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    //上传之前
    beforeUpload(file) {
      if (
        file.name.indexOf("%00") > -1 ||
        file.name.indexOf("./") > -1 ||
        file.name.indexOf(".\\") > -1
      ) {
        this.msgError("上传文件名称非法！");
        return false;
      }
      const fileSize = file.size;
      if (0 == fileSize) {
        this.msgError("上传文件不能为空！");
        return false;
      }
      if (!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG|pdf|PDF)$/.test(file.name)) {
        this.$message.warning("只允许上传图片和pdf文件");
        return false;
      }
      const isLt2M = file.size / 1024 / 1024 < 30;
      if (!isLt2M) {
        this.msgError("上传文件大小不能超过 30MB！");
      }
      return isLt2M;
    },
    //失败后返回
    uploadError(resp) {
      this.$message.error(resp);
    },
    /** 上传附件 */
    uploadFiles(file) {
      this.loading = this.$loading({
        lock: true,
        text: '文件上传中',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      const that = this;
      let formData = new FormData();
      formData.append("files", file.file);
      outerUploadCaseMaterialFile(formData)
        .then((res) => {
          that.loading.close();
          if (res.code === 200) {
            const obj = {
              attaOid: res.data[0].attaOid,
              caseMaterialOid: that.materialData.caseMaterialOid,
              src: res.data[0].fastdfsNginxUrl,
            };
            that.qlCaseMaterialAttaList.push(obj);
          }
        })
        .catch((err) => {
          that.loading.close();
          console.log(err);
          that.uploadError();
        });
    },
    handlePictureCardPreview(url) {
      this.srcList = []
      this.srcList.push(url)
    },
    handleFileChange(file, fileList) {
      this.upload.fileList = fileList;
    },
    // 删除之前钩子
    handleFileRemove(file, fileList) {
      this.upload.fileList = fileList;
    },
    resetFile() {
      this.$refs.upload.clearFiles();
      this.qlCaseMaterialAttaList = [];
    },
    // 提交上传文件
    submitFileForm() {
      const params = [
        {
          caseMaterialOid: this.materialData.caseMaterialOid,
          collectionType: "2",
          collectionFlag: 1,
          materialOid: this.materialData.materialOid,
          qlCaseMaterialAttaList: [],
        },
      ];
      this.qlCaseMaterialAttaList.forEach((item) => {
        const obj = {
          attaOid: item.attaOid,
          caseMaterialOid: this.materialData.caseMaterialOid,
          src: item.src,
        };
        params[0].qlCaseMaterialAttaList.push(obj);
      });
      outerSaveOrUpdateCaseMaterialAttaList(params)
        .then((res) => {
          if (res.code === 200) {
            this.$message.success("上传成功");
            // this.$store.commit("setScanCodeUpload", true);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>
<style lang="scss" scoped>
  .list{
    .item{
      position: relative;
      float: left;
      margin:10px;
      i{
        position: absolute;
        top:-5px;
        right: -5px;
        color: red;
        font-size: 25px;
      }
    }

    &::after{
      content: '';
      clear: both;
      display: block;
    }
  }
  .container {
    width: 100%;
    height: 100%;

  .materialUpload {
    width: 100%;
    height: calc(100% - 10rem);
    background-color: #fff;
    padding: 2.4286rem 1.8571rem;

    .dialogContentTitle {
      padding: 0;
      margin: 0;

      text-align: left;
      padding-left: 1.375rem;
      font-size: 1.625rem;
      font-family: Source Han Sans CN;
      font-weight: 500;
      color: #2a344c;
      position: relative;

      &::before {
        content: "";
        position: absolute;
        left: 0;
        top: 50%;
        transform: translateY(-50%);
        width: 0.5625rem;
        height: 1.375rem;
        background: linear-gradient(270deg, #2473ff 0%, #56b1fd 100%);
        border-radius: 0.3125rem;
      }
    }

    .uploadArea {
      width: 100%;
      height: auto;
      margin-top: 3.0714rem;
      overflow-y: auto;

      &::-webkit-scrollbar {
        width: 0.4375rem;
        background-color: #fff;
      }

      &::-webkit-scrollbar-thumb {
        width: 0.4375rem;
        height: 0.625rem !important;
        // background: linear-gradient(270deg, #bf9e63 0%, #dfca98 100%);
        background: linear-gradient(270deg, #2473ff 0%, #56b1fd 100%);
        border-radius: 4px;
      }

      .uploadFileList {
        float: left;
        // width: 100%;
        // height: auto;
        // display: flex;
        // align-items: center;
        // justify-content: flex-start;
        ::v-deep .el-upload-list{
          display: none;
        }
        ::v-deep .el-upload {
          width: 13.25rem;
          height: 10.3125rem;
          width: 100px;
          height: 102px;
          margin: 10px;
          background: #f4f4f4;
          border: 1px solid #e8e8e8;
          border-radius: 0.2857rem;

          .el-upload__text {
            font-size: 1.875rem;
            font-family: Microsoft YaHei;
            font-weight: bold;
            color: #2473ff;
            line-height: normal !important;
          }
        }

        ::v-deep .el-upload--picture-card {
          .operate {
            width: 100%;
            height: 100%;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            line-height: normal !important;
          }
        }

        ::v-deep .el-upload-list {
          .el-upload-list__item {
            width: 13.25rem;
            height: 10.3125rem;
            background: #f4f4f4;
            border: 1px solid #e8e8e8;
            border-radius: 0.2857rem;
            overflow: visible;

            .el-upload-list__item-actions {
              opacity: 1;
              background-color: transparent;

              .el-upload-list__item-delete {
                display: block;
                position: absolute;
                right: -0.8438rem;
                top: -0.8438rem;
                width: 2.3125rem;
                height: 2.3125rem;
                background: url("@/assets/images/phone/delImg.png") no-repeat;
                background-size: 100% 100%;

                .el-icon-delete {
                  display: none;
                }
              }
            }
          }
        }
      }
    }
  }

  .upload-footer {
    width: 100%;
    height: 9.375rem;
    position: fixed;
    bottom: 0;
    left: 0;
    display: flex;
    align-items: center;
    justify-content: center;

    p {
      padding: 0;
      margin: 0;
      width: 15rem;
      height: 5rem;
      border-radius: 0.375rem;
      font-size: 2rem;
      font-family: Microsoft YaHei;
      font-weight: 400;
      display: flex;
      align-items: center;
      justify-content: center;

      &:nth-child(1) {
        background: #ffffff;
        border: 1px solid #4988f2;
        color: #2473ff;
        margin-right: 1.875rem;
      }

      &:nth-child(2) {
        background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
        box-shadow: 0px 0px 1.5rem 0px rgba(40, 107, 198, 0.31);
        color: #fff;
      }
    }
  }
}

.remark-title {
  margin-top: 10px;
}

.remark {
  text-align: left;
}
</style>
