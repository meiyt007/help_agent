<!--  -->
<template>
  <div class="uploader-dlg-wrap" :class="{'mini-wrap': mini}" v-show="isShow">
    <div class="uploader-dlg">
      <div class="header">
        <div class="title">文件上传</div>
        <div class="icon-group">
          <i class="iconfont iconminimum" title="最小化" @click="minisize(true)" v-if="!mini"></i>
          <i class="iconfont iconzuidahua1" title="最大化" @click="minisize(false)" v-if="mini"></i>
          <i class="iconfont el-icon-close iconguanbi" title="关闭" @click="close"></i>
        </div>
      </div>

      <div class="btn-wrap">
        <el-button :id="buttonId" type="primary" :disabled="fileBtnDisabled">选择文件</el-button>
        <span v-show="fileBtnDisabled" class="disable-tips">当前最大上传数为{{maxNum}}个，请删除后重新上传!</span>
        <span v-show="extensions.length>0" class="extensions-tips">只允许上传（{{extensions}}）类型的文件</span>
      </div>

      <div class="file-list-header">
        <div class="filename">文件名</div>
        <div class="filesize">大小</div>
        <div class="uploader-state">状态</div>
        <div class="file-todo">操作</div>
      </div>

      <div class="file-list scrollbar">
        <div class="list-item" v-for="(file,index) in fileList" :key="file.id">
          <div class="upload-progress-wrap" v-if="file.status==2">
            <div class="uploader-progress" :style="'width:'+file.percent+'%'"></div>
          </div>
          <div class="filename">
            <div class="file-icon"></div>
            <span class="name-text" :title="file.filename">{{file.filename}}</span>
          </div>
          <div class="filesize">{{file.sizeStr}}</div>
          <div class="uploader-state" >
            <span v-if="file.status==2">正在上传（{{file.percent}}%）</span>
            <span v-if="file.status==1">排队中</span>
            <span v-if="file.status==3">上传完成</span>
          </div>
          <div class="file-todo">
            <i class="iconfont iconzanting el-icon-video-pause" title="暂停" v-if="ShowStop(file,index)" @click.stop="uploaderStop"></i>
            <i class="iconfont iconjixu el-icon-video-play" title="继续"  v-if="ShowContinue(file,index)" @click.stop="uploaderStart"></i>
            <i class="iconfont iconshanchu el-icon-tickets" title="预览" v-if="checkPng(file.type)" @click.stop="openExaminePointCardingView(file.oid)"></i>
            <i class="iconfont iconshanchu el-icon-setting" title="配置" v-if="checkPng(file.type)" @click.stop="openExaminePointCardingManage(file.oid)"></i>
            <i class="iconfont iconshanchu el-icon-delete" title="删除" @click.stop="delFile(file.id)"></i>
          </div>
        </div>
      </div>

      <div class="footer">
        <el-button @click="close">取消</el-button>
        <el-button type="primary" @click="sure">确认</el-button>
      </div>
    </div>
    <!--预览审查要点配置-->
    <el-dialog v-dialog-drag title="预览审查要点配置" :visible.sync="initPointOptionsView"
               @close="closeFileView" width="60%"  append-to-body>
      <iframe :src=fileViewurl  rameborder="0" width="100%" height="600px"  @father-click="closeFileView"  ></iframe>
      <div slot="footer" class="dialog-footer">
        <el-button @click="initPointOptionsView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import initUpload from "./config/uploader";

import { RandomStr , getfiles,getFileIndex } from "./config/utils.js";
export default {
  name: "",
  props: {
    maxNum: {
      type: Number,
      default: 10
    },
    fileNum: {
      type: Number,
      default: 0
    },
    extensions: {
      type: String,
      default: ''
    }
  },
  computed: {
    multi_selection() {
      return this.maxNum>1
    },
    fileBtnDisabled() {
      return this.fileList.length>=this.maxNum
    },
    fileExceeds() {
      return this.fileList.length>this.maxNum
    },
    isMaxNum() {
      return this.maxNum-this.fileNum;
    },
    ShowStop() {
      return function(file,index) {
        return file.status==2 && !this.isStop
      }
    },
    ShowContinue() {
      return function(file,index) {
        return file.status==2 && this.isStop
      }
    }
  },
  data() {
    return {
      buttonId: "",
      params: {
        chunkSize: "10mb",
        ctxPath: '/sxSys/file/uploadFile',
      },
      fileList: [],
      isStop: false,
      uploader: '',
      isShow: false,
      mini: false,
      initPointOptionsView: false,
      //查看配置审查要点
      fileViewurl:''
    };
  },
  watch: {
    isShow: {
      deep: true,
      handler: function(newVal) {
        //this.fileList = []
      }
    },
    extensions: {
      deep: true,
      handler: function() {
          try{
            this.uploader.destroy()
          }catch (e) {
            //
          }
        //this.uploader.destroy()
        this._initUpload()
      }
    }
  },
  mounted() {
    this._initUpload();
  },
  methods: {
    _initUpload() {
      this.buttonId = RandomStr(8);
      setTimeout(() => {
        this.uploader = initUpload({
          ...this.params,
          buttonId: this.buttonId,
          multi_selection: this.multi_selection,
          files_added: this.files_added,
          upload_progress: this.upload_progress,
          file_uploaded: this.file_uploaded,
          maxNum: this.maxNum,
          extensions: this.extensions,
          error_fun: this.error_fun
        });
      }, 1000);
    },

    /**选择文件时 */
    files_added(up,files) {
        if(files[0].name.indexOf("%00")!==-1){
            this.msgError('上传文件名不合法！');
            return;
        }
      const files_arr = getfiles(files)
      files_arr.forEach((item,idx) => {
        if(this.fileList.length>=this.isMaxNum) {
            this.$utils.msgError(`当前最大上传数为${this.maxNum}个`)
            this.uploader.removeFile(files[idx])
            return
        }
        this.fileList.push(item)
      })
    },

    /**上传过程中判断文件数是否超标 */
    judgeFileLength(file) {
      if(this.fileExceeds) {
        this.$utils.msgError(`当前最大上传数量为${this.maxNum}个文件，请删除一部分后重新上传`)
        this.uploader.removeFile(file)
        const index = getFileIndex(this.fileList,file.id)
        this.fileList.splice(index,1)
      }
    },

    /**上传文件过程 */
    upload_progress(uploader,file) {
      this.isStop = false
      const index = getFileIndex(this.fileList,file.id)
      if(index==-1) {
        this.uploader.removeFile(file)
        return
      }
      if(this.fileList[index].percent>file.percent) {
        return
      }
      this.fileList[index].status = 2
      this.fileList[index].percent = file.percent
      this.judgeFileLength()
    },

    /**文件上传错误提示 */
    error_fun(errVal) {
      this.msgError(errVal);
    },

    /**文件上传完成触发 */
    file_uploaded(up, file, info) {
      const index = getFileIndex(this.fileList,file.id)
      this.fileList[index].status = 3
      const res = JSON.parse(info.response)
      if(info.status==200) {
        this.fileList[index].oid = res.data.attaOid;
      }
    },

    /**暂停上传 */
    uploaderStop() {
      this.uploader.stop();
      this.isStop = true
    },
    /**开始上传 */
    uploaderStart() {
      this.uploader.start()
      this.isStop = false
    },
    /**删除文件 */
    delFile(id) {
       const index = getFileIndex(this.fileList,id)
       if(this.fileList[index].status=='2') {
         this.uploader.start();
       }
       this.fileList.splice(index,1);
    },
    sure() {
      const fileList = this.fileList;
      let completeFiles = []
      let complete = true
      fileList.forEach(item => {
        if(item.status==1||item.status==2) {
          complete = false
        }else if(item.status==3) {
          completeFiles.push(item)
        }
      })
      if(!complete) {
        this.$confirm('文件未全部上传完毕，是否确认提交上传文件','提示',{
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() =>{
          this.$emit('getFiles',completeFiles)
          this.close()
        }).catch(() => {
           complete = false
        })
      }else {
        this.$emit('getFiles',completeFiles)
        this.close()
      }
    },
    show(files) {
      if (files != '' && files != undefined) {
        this.fileList = files;
      } else {
        this.fileList = [];
      }
      this.isShow = true;
    },
    close() {
      this.isShow = false
    },
    /**最小化 */
    minisize(bool) {
      this.mini = bool
    },
    //打开验证规则配置页面
    openExaminePointCardingManage(attaOid){
      this.url= process.env.BASE_URL+'picture/edit.html?attaOid='+attaOid,
      window.open(this.url);
    },
    //打开审查要点配置详细
    openExaminePointCardingView(attaOid){
      this.fileViewurl = process.env.BASE_URL+'picture/prview.html?attaOid='+attaOid,
      this.initPointOptionsView = true;
    },
    closeFileView(){
      this.initPointOptionsView = false;
    },
    checkPng(type) {
      return type.toString().includes('image');
    }
  },
};
</script>

<style lang="scss" scoped>
@import "./style/index.scss";
</style>
