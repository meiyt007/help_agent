<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-24 18:00:18
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-10-21 14:18:02
 * @FilePath: \zf_web_ui\src\views\pc\roundTableAssistant\database.vue
 * @Description: 资料库
-->
<template>
  <div class="dataBase-container">
    <div class="base-header">
      <p @click="addDocument('1')">添加文件夹</p>
      <p @click="addDocument('2')">添加资料</p>
    </div>
    <div class="documentArea" v-loading="loadingData">
      <p class="nodata" v-if="showNoData && documentIdList.length < 2">
        暂无资料
      </p>
      <div
        class="item-block reback"
        @click="reback"
        v-if="documentIdList.length > 1"
      >
        <div class="headPre">
          <img :src="require('@/assets/images/home/reback.png')" alt="" />
        </div>
        <div class="docFoot">
          <p>返回上一级</p>
        </div>
      </div>
      <div
        class="item-block folder"
        :class="item.type === '1' ? 'folder' : 'file'"
        v-for="(item, index) in fileList"
        :key="index"
      >
        <div class="headPre">
          <img
            :src="
              item.type === '1'
                ? require('@/assets/images/home/folder.png')
                : require('@/assets/images/home/document.png')
            "
            alt=""
          />
        </div>
        <div class="docFoot">
          <p>{{ item.name }}</p>
        </div>
        <div class="operate">
          <p @click="toDetail(item)">查看</p>
          <p @click="editDoc(item)">编辑</p>
          <p @click="deleteDoc(item)">删除</p>
          <p @click="toShare(item)">分享给帮办</p>
          <p @click="toSend(item)">发送办事人</p>
        </div>
      </div>
    </div>
    <el-dialog
      :title="titleValue"
      :visible.sync="showAddDocument"
      width="80%"
      center
      class="addDocumentDialog"
      append-to-body
      v-dialogDrag
    >
      <el-form
        v-if="showAddDocument"
        :model="documentInfo"
        :rules="documentRules"
        ref="document"
        label-width="10rem"
      >
        <el-form-item label="资源名称" prop="name">
          <el-input
            v-model.trim="documentInfo.name"
            placeholder="请输入资源名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="资源类型" prop="type">
          <el-select
            v-model="documentInfo.type"
            placeholder="请选择资源类型"
            :disabled="isEdit"
          >
            <el-option label="文件夹" value="1"></el-option>
            <el-option label="文件" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label="附件上传"
          prop="upload"
          v-if="documentInfo.type === '2'"
        >
          <el-upload
            class="upload-demo"
            action
            :http-request="uploadFiles"
            :before-upload="beforeUpload"
            :on-error="uploadError"
            :limit="1"
            :file-list="upLoadFileList"
          >
            <img
              width="100%"
              :src="require('@/assets/images/home/downLoadIcon.png')"
              alt=""
            />
          </el-upload>
        </el-form-item>
        <!-- <el-form-item label="备注" prop="resourceInfo">
          <el-input type="textarea" v-model="documentInfo.resourceInfo"></el-input>
        </el-form-item> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <p @click="showAddDocument = false">取消</p>
        <p @click="compliteNext">确定</p>
      </div>
    </el-dialog>
    <el-dialog
      v-dialog-drag
      title="预览"
      :visible.sync="showPreview"
      class="preview-dialog"
      width="80%"
    >
      <div class="previewArea">
        <iframe
          v-if="preType == 'pdf'"
          ref="iframe"
          id="pdf-box"
          :src="preViewUrl"
          frameborder="0"
          style="height: 70vh; width: 100%; overflow: hidden"
        ></iframe>
        <img v-if="preType == 'img'" :src="preViewUrl" alt="img" />
      </div>
      <!-- <div class="foot">
        <p @click="shareAssistant">分享帮办人员</p>
        <p>发送给办事人</p>
      </div> -->
    </el-dialog>
    <el-dialog
      v-dialog-drag
      title="分享给帮办人员"
      :visible.sync="shareDocument"
      class="share-dialog"
      width="60%"
    >
      <div class="container-body" v-if="shareDocument">
        <div class="left">
          <el-input
            placeholder="输入部门姓名查找"
            prefix-icon="el-icon-search"
            v-model="filterText"
            clearable
          >
          </el-input>
          <div class="tree-body">
            <el-tree
              :data="groupList"
              show-checkbox
              default-expand-all
              node-key="id"
              ref="tree"
              highlight-current
              :props="defaultProps"
              :filter-node-method="filterNode"
              @check-change="handleChange"
            >
            </el-tree>
          </div>
        </div>
        <div class="right">
          <div class="header">选中人员</div>
          <div class="right-content">
            <div
              class="item-block"
              v-for="(item, index) in chooseAssistant"
              :key="index"
            >
              <p>{{ item.label }}</p>
              <i class="el-icon-close" @click="deleteAssistant(item)"></i>
            </div>
          </div>
          <div class="right-foot">
            <p>已选{{ chooseAssistant.length }}项</p>
            <p @click="clearData">清空</p>
          </div>
        </div>
      </div>
      <div class="content-foot">
        <p @click="shareData">确定</p>
      </div>
    </el-dialog>
     <!-- 分享办事人 -->
      <el-dialog :show-close="false" title="分享给办事人员" top="100px　!important" :visible.sync="showHandle" width="800px"
          center class="pageDialog" append-to-body v-dialogDrag>
          <shareHandle @handleClose="handleClose" v-if="showHandle" :tableradioGroup="tableradioGroup" :fileData="fileData"/>
      </el-dialog>
  </div>
</template>
<script>
import {
  listResource,
  getResourceInfo,
  uploadFile,
  saveResourceInfo,
  deleteResource,
  shareResource,
} from "@/api/modules/resourceInformation";
import { getWorkUserList } from "@/api/modules/guideService";
import shareHandle from '@/views/pad/materialStore/shareHandle.vue';
export default {
  name: "database",
  components:{shareHandle},
  data() {
    return {
      fileList: [],
      isEdit: false,
      editData: {},
      fileInfo: {
        parentId: "",
        name: "",
      },
      showPreview: false,
      showNoData: false,
      loadingData: false,
      shareDocument: false,
      preViewUrl: "",
      preType:'', //预览文件类型
      titleValue: "",
      documentIdList: [""],
      showAddDocument: false,
      shareDocumentId: "",
      documentInfo: {
        name: "",
        type: "",
        fileName: "",
        upload: "",
      },
      documentRules: {
        name: [{ required: true, message: "请输入资源名称", trigger: "blur" }],
        type: [
          { required: true, message: "请选择资源类型", trigger: "change" },
        ],
        upload: [{ required: true, message: "请上传文件", trigger: "change" }],
      },
      upLoadFileList: [],
      defaultProps: {
        children: "children",
        label: "label",
      },
      filterText: "",
      groupList: [],
      chooseAssistant: [],
      showHandle:false, 
      fileData:{},
      tableradioGroup:{}
    };
  },
  computed: {
    basicUserInfo() {
      return this.$store.state.user.basicUserInfo;
    },
  },
  mounted() {
    this.getListResource();
    this.getWorkUserList();
  },
  methods: {
    //打开分享帮办弹窗
    shareAssistant() {
      this.shareDocument = true;
    },
    //获取帮办人员列表
    getWorkUserList() {
      const data = {
        name: "",
        haType: "",
      };
      getWorkUserList(data).then((res) => {
        if (res.code === 200) {
          res.data.forEach((item) => {
            const obj = {
              id: item.groupId,
              label: item.groupName,
              children: [],
            };
            const result = this.groupList.find((data) => {
              return data.id === item.groupId;
            });
            if (!result) {
              this.groupList.push(obj);
            }
          });

          res.data.forEach((item) => {
            this.groupList.forEach((ite) => {
              if (item.groupId === ite.id) {
                let obj = {};
                if (item.id === this.basicUserInfo.id) {
                  obj = {
                    id: item.id,
                    label: item.name,
                    disabled: true,
                  };
                } else {
                  obj = {
                    id: item.id,
                    label: item.name,
                  };
                }
                ite.children.push(obj);
              }
            });
          });
          this.groupList = [...this.groupList];
        }
      });
    },

    //选择帮办人员
    handleChange() {
      this.chooseAssistant = [];
      this.$refs.tree.getCheckedNodes().forEach((item) => {
        if (!item.children) {
          this.chooseAssistant.push(item);
        }
      });
    },
    //删除选中帮办人员
    deleteAssistant(data) {
      this.chooseAssistant.forEach((item, index) => {
        if (data.id === item.id) {
          this.chooseAssistant.splice(index, 1);
        }
      });
      this.$refs.tree.setCheckedNodes(this.chooseAssistant);
    },
    //清空选中帮办人员
    clearData() {
      this.chooseAssistant = [];
      this.$refs.tree.setCheckedKeys([]);
    },

    //分享帮办人员
    shareData() {
      const params = {
        id: this.shareDocumentId,
        workUserIds: "",
      };
      this.chooseAssistant.forEach((item, index) => {
        if (index === this.chooseAssistant.length - 1) {
          params.workUserIds += item.id;
        } else {
          params.workUserIds += item.id + ",";
        }
      });
      if (!params.workUserIds) {
        this.$message.warning("请选择需要分享的帮办人员");
        return;
      }
      shareResource(params)
        .then((res) => {
          if (res.code === 200) {
            this.$message.success("资料分享成功");
            this.clearData();
            this.shareDocument = false;
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },

    filterNode(value, data, node) {
      if (!value) return true;
      let parentNode = node.parent;
      let labels = [node.label];
      let level = 1;
      while (level < node.level) {
        labels = [...labels, parentNode.label];
        parentNode = parentNode.parent;
        level++;
      }
      return labels.some((label) => label.indexOf(value) !== -1);
    },

    compliteNext() {
      this.$refs["document"].validate((valid) => {
        if (valid) {
          // console.log(this.documentInfo.name);
          // console.log(this.documentInfo.name.length);
          const params = {
            id: this.isEdit ? this.editData.id : "",
            type: this.documentInfo.type,
            name: this.documentInfo.name,
            parentId: this.fileInfo.parentId,
            fileName: this.documentInfo.fileName,
            resourceInfo:
              this.documentInfo.type === "2" ? this.resourceInfo : "",
          };
          saveResourceInfo(params).then((res) => {
            if (res.code === 200) {
              this.showAddDocument = false;
              // if (this.fileInfo.parentId) {
              //   this.fileInfo.name = this.documentInfo.name;
              // }
              this.getListResource();
            }
          });
        }
      });
    },
    addDocument(type) {
      this.showAddDocument = true;
      this.documentInfo.type = type;
    },
    getListResource() {
      this.loadingData = true;
      listResource(this.fileInfo)
        .then((res) => {
          this.loadingData = false;
          if (res.code === 200) {
            this.fileList = res.data;
            if (!res.data.length) {
              this.showNoData = true;
            } else {
              this.showNoData = false;
            }
          }
        })
        .catch((err) => {
          console.log(err);
          this.loadingData = false;
        });
    },
    //点击文件夹详情
    toDetail(data) {
      if (data.type === "1") {
        this.documentIdList.push(data.id);
        this.fileInfo.parentId = data.id;
        this.getListResource();
      } else {
        this.getResourceInfo(data.id);
        this.shareDocumentId = data.id;
      }
    },
    //获取资源详情
    getResourceInfo(id) {
      getResourceInfo({ id: id })
        .then((res) => {
          if (res.code === 200) {
            this.preViewUrl = res.data.fastdfsUploadUrl;
            if (
                /\.(pdf|PDF)$/.test(this.preViewUrl)
            ) {
                this.showPreview = true;
                this.preType = 'pdf';
            } else if (
              /\.(gif|jpg|jpeg|png|GIF|JPG|PNG|pdf|PDF)$/.test(this.preViewUrl)
            ) {
              this.preType = 'img';
              this.showPreview = true;
            } else {
              window.open(this.preViewUrl);
            }
          }
        })
        .catch((err) => {
          this.$message.warning("资源详情获取失败");
        });
    },

    //返回上一级
    reback() {
      this.documentIdList.splice(this.documentIdList.length - 1, 1);
      this.fileInfo.parentId =
        this.documentIdList[this.documentIdList.length - 1];
      this.getListResource();
    },
    /** 上传附件 */
    uploadFiles(file) {
      const loading = this.$loading({
        lock: true,
        text: "正在上传文件",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
      let formData = new FormData();
      formData.append("file", file.file);
      return uploadFile(formData)
        .then((res) => {
          if (res.code === 200) {
            this.resourceInfo = res.data.filePath;
            this.documentInfo.upload = res.data.filePath;
            this.documentInfo.fileName = file.file.name;
            loading.close();
          }
        })
        .catch((err) => {
          loading.close();
          this.uploadError();
        });
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
      if (
        !/\.(gif|jpg|jpeg|png|bmp|GIF|JPG|PNG|swf|flv|mp3|wav|wma|wmv|mid|avi|mpg|asf|rm|fmvb|mp4|mp5|doc|docx|xls|xlsx|ppt|txt|zip|rar|gz|bz2|pdf|PDF)$/.test(
          file.name
        )
      ) {
        this.$message.warning("上传文件格式错误");
        return false;
      }
      const fileSize = file.size;
      if (0 == fileSize) {
        this.msgError("上传文件不能为空！");
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
      // this.$message.error("文件上传失败");
    },
    //修改资料
    editDoc(data) {
      this.isEdit = true;
      this.editData = data;
      console.log(data);
      this.upLoadFileList = [];
      this.showAddDocument = true;
      this.documentInfo = {
        name: data.name,
        type: data.type,
        upload: data.resourceInfo,
      };
      const obj = { name: data.fileName, url: data.resourceInfo };
      this.upLoadFileList.push(obj);
    },
    deleteDoc(data) {
      this.$confirm("确定删除该文件?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.deleteResource(data);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    toShare(data){
      this.shareDocumentId = data.id;
      this.shareDocument = true;

    },
    toSend(data){
      if(data.type == '1'){
        this.$message.warning("正在开发中...");
      }else{
        this.fileData = data;
        this.tableradioGroup = {};
        this.showHandle = true;
      }
    },
    handleClose(){
      this.showHandle = false;
    },  
    deleteResource(data) {
      deleteResource({ ids: data.id }).then((res) => {
        if (res.code === 200) {
          this.$message.success("已成功删除");
          this.getListResource();
        }
      });
    },
  },
  watch: {
    //
    showAddDocument: {
      handler(val) {
        if (!val) {
          this.documentInfo = {
            name: "",
            type: "",
            fileName: "",
            upload: "",
          };
          this.upLoadFileList = [];
        }
      },
    },
    filterText(val) {
      this.$refs.tree.filter(val);
    },
  },
};
</script>
<style lang="scss" scoped>
.previewArea{
    img{
        max-width: 100%;
    }
}
.dataBase-container {
  width: 100%;
  height: 100%;

  .base-header {
    width: 100%;
    height: 3.5714rem;
    display: flex;
    align-items: center;
    justify-content: flex-end;

    p {
      margin: 0;
      height: 100%;
      padding: 0 2.2857rem;
      background: linear-gradient(90deg, #6d93e8 0%, #77b0fe 100%);
      border-radius: 2.2143rem;
      font-size: 1.5rem;
      font-family: Microsoft YaHei;
      font-weight: 400;
      color: #ffffff;
      display: flex;
      align-items: center;
      justify-content: center;

      &:nth-child(1) {
        margin-right: 2rem;
      }
    }
  }

  .documentArea {
    width: 100%;
    height: calc(100% - 3.5714rem);
    padding: 1.5rem 0;
    display: flex;
    flex-wrap: wrap;
    align-items: flex-start;
    justify-content: flex-start;
    overflow-y: auto;
    .nodata {
      width: 100%;
      text-align: center;
    }
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
    .nodata {
      widows: 100%;
      text-align: center;
    }
    .item-block {
      min-width: 200px;
      width: 19%;
      height: 18.9714rem;
      background: #f3f6f8;
      border-radius: 0.7143rem;
      padding: 0.7143rem;
      margin-right: 1%;
      margin-bottom: 1.5rem;

      .headPre {
        width: 100%;
        height: 8.6429rem;
        display: flex;
        align-items: center;
        justify-content: center;
        background-color: #ffffff;

        img {
          width: 6.7143rem;
        }
      }

      .docFoot {
        width: 100%;
        height: calc(100% - 8.6429rem);
        display: flex;
        align-items: center;
        justify-content: flex-start;
        font-size: 1.5714rem;
        font-family: Source Han Sans CN;
        font-weight: 500;
        color: #373737;
        p {
          width: 100%;
          height: auto;
          max-height: 4.2rem;
          overflow: hidden; // 溢出隐藏
          display: -webkit-box; //  自适应布局 弹性伸缩盒子
          -webkit-box-orient: vertical; //垂直排列子元素 伸缩盒子的子元素排列
          -webkit-line-clamp: 2; //最多显示几行 多出部分。。。显示
          text-overflow: ellipsis; // 显示省略号
        }
      }

      .operate {
        display: none;
        width: 100%;
        height: 3.5rem;
        margin: .3rem 0;
        align-items: center;
        justify-content: space-around;
        flex-wrap: wrap;

        p {
          padding: 0.9167rem 1.2rem;
          margin: 0;
          display: flex;
          align-items: center;
          justify-content: center;
          background: linear-gradient(143deg, #fdfbf8 0%, #e4ebf7 100%);
          border-radius: 1.75rem;
          font-size: 1.6667rem;
          font-family: Source Han Sans CN;
          font-weight: 400;
          color: #4072c7;
          cursor: pointer;
          margin-top: 5px;

          &:nth-child(3) {
            background: #f56c6c;
            color: #fff;
          }

          &:nth-child(1) {
            background: #216adb;
            color: #fff;
          }
        }
      }

      &:hover {
        border: none;
        background: url("@/assets/images/home/materialHoverBak.png") no-repeat;

        .itemSelect {
          color: #ffffff;
          text-shadow: 0px 0px 8px rgba(55, 83, 175, 0.45);
        }

        .docFoot {
          display: none;
        }

        .operate {
          display: flex;
        }
      }
    }

    .reback {
      .headPre {
        img {
          width: 6.7143rem;
        }
      }
    }

    .folder {
      .headPre {
        img {
          width: 4.7143rem;
          height: 3.7857rem;
        }
      }
    }

    // .file {
    //   .headPre {
    //     img {
    //       width: 100%;
    //       height: 3.7857rem;
    //     }
    //   }
    // }
  }
}

.el-form {
  width: 100%;
  height: auto;
  margin-top: 2.625rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;

  .el-form-item {
    height: auto;
    width: 50%;

    ::v-deep .el-form-item__label {
      font-size: 1.5rem;
      font-family: Source Han Sans CN;
      font-weight: 500;
      color: #373737;
    }

    ::v-deep .el-form-item__content {
      height: 100%;

      .el-select {
        width: 100%;
      }

      .upload-demo {
        width: auto;
        height: auto;

        .el-upload {
          width: 14rem;
          height: 8.0714rem;
          background: rgba(148, 189, 253, 0.21);
          border: 1px solid #216adb;
          border-radius: 0.7143rem;
          display: flex;
          align-items: center;
          justify-content: center;

          img {
            width: 4rem;
          }
        }
      }
    }
  }
}

.addDocumentDialog {
  ::v-deep .el-dialog {
    height: 75vh;

    .el-dialog__body {
      height: calc(100% - 13.75rem);
    }
  }
}

.dialog-footer {
  p {
    &:nth-child(1) {
      background: #ffffff;
      border: 1px solid #4988f2;
      box-shadow: 0px 0px 1.8125rem 0px rgba(204, 177, 121, 0.31);
      color: #2473ff;
      margin-right: 2.1875rem;
      cursor: pointer;
    }

    &:nth-child(2) {
      background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
      box-shadow: 0px 0px 1.5rem 0px rgb(40 107 198 / 31%);
      color: #ffffff;
    }
  }
}
.preview-dialog {
  .el-dialog {
    .el-dialog__body {
      .previewArea {
        width: 100%;
        height: calc(100% - 5rem);
        overflow-y: auto;
      }
      .foot {
        width: 100%;
        height: 5rem;
        display: flex;
        align-items: flex-end;
        justify-content: center;
        p {
          cursor: pointer;
          padding: 0 1.2rem;
          margin: 0;
          height: 3.5714rem;
          background: linear-gradient(90deg, #6d93e8 0%, #77b0fe 100%);
          border-radius: 3.5714rem;
          font-size: 1.5rem;
          font-family: Microsoft YaHei;
          font-weight: 400;
          color: #ffffff;
          display: flex;
          align-items: center;
          justify-content: center;
          &:nth-child(1) {
            margin-right: 1.5rem;
          }
          // &:nth-child(2) {
          // }
        }
      }
    }
  }
}
.share-dialog {
  ::v-deep .el-dialog {
    height: 60vh !important;
    margin-top: 20vh !important;
    .container-body {
      width: 100%;
      height: calc(100% - 5rem);
      padding: 1.5rem;
      display: flex;
      align-items: center;
      justify-content: center;
      .left {
        width: 50%;
        height: 100%;
        border: 1px solid #e4e6e8;
        .el-input {
          border-bottom: 1px solid #e4e6e8;
          .el-input__inner {
            border: none !important;
          }
        }
        .tree-body {
          width: 100%;
          height: calc(100% - 50px);
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
          .el-tree {
            .el-tree-node {
              .el-tree-node__content {
                .el-tree-node__label {
                  font-size: 1.5714rem;
                  font-family: Source Han Sans CN;
                  font-weight: 400;
                  color: #373737;
                }
              }
              .el-tree-node__children {
                .el-tree-node {
                  .el-tree-node__content {
                    .el-tree-node__label {
                      font-size: 1.5714rem;
                      font-family: Microsoft YaHei;
                      font-weight: 400;
                      color: #666666;
                    }
                  }
                }
              }
            }
          }
        }
      }
      .right {
        width: 50%;
        height: 100%;
        border: 1px solid #e4e6e8;
        border-left: none;
        .header {
          width: 100%;
          height: 41px;
          padding-left: 2.5rem;
          border-bottom: 1px solid #e4e6e8;
          text-align: left;
          line-height: 40px;
        }
        .right-content {
          width: 100%;
          height: calc(100% - 76px);
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
          .item-block {
            width: 100%;
            height: 40px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 0 1.2rem;
            p {
              padding: 0;
              margin: 0;
              font-size: 1.5714rem;
              font-family: Source Han Sans CN;
              font-weight: 400;
              color: #373737;
            }
            i {
              font-size: 1.5714rem;
              cursor: pointer;
            }
            &:hover {
              background-color: #99bcf0;
            }
          }
        }
        .right-foot {
          width: 100%;
          height: 35px;
          border-top: 1px solid #e4e6e8;
          padding: 0 1rem;
          display: flex;
          align-items: center;
          justify-content: space-between;
          p {
            padding: 0;
            margin: 0;
            &:nth-child(1) {
              font-size: 1.4286rem;
              font-family: Source Han Sans CN;
              font-weight: 400;
              color: #999999;
            }
            &:nth-child(2) {
              font-size: 1.4286rem;
              font-family: Source Han Sans CN;
              font-weight: 400;
              color: #397df1;
              cursor: pointer;
            }
          }
        }
      }
    }
    .content-foot {
      width: 100%;
      height: 5rem;
      display: flex;
      align-items: center;
      justify-content: center;
      p {
        cursor: pointer;
        padding: 0 2.5rem;
        margin: 0;
        height: 3.5714rem;
        background: linear-gradient(90deg, #6d93e8 0%, #77b0fe 100%);
        border-radius: 3.5714rem;
        font-size: 1.5rem;
        font-family: Microsoft YaHei;
        font-weight: 400;
        color: #ffffff;
        display: flex;
        align-items: center;
        justify-content: center;
      }
    }
  }
}
</style>
