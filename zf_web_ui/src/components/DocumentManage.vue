<template>
  <div class="document-manage">
    <template v-for="(doc, index) in docList">
      <el-tooltip class="item" effect="light" :content="doc.name" placement="top" :key="doc.id">
        <span class="button" :key="doc.id" @click="openDoc(doc.id)" :class="activeDocId === doc.id ? 'active' : ''">
          {{doc.id === '0' ? '白板' : ('文档' + index)}}
          <i v-if="doc.id !== '0'" class="button-icon del" :class="activeDocId === doc.id ? 'active' : ''" @click="deleteDoc(doc)"></i>
        </span>
      </el-tooltip>
    </template>
    <template v-for="doc in uploadDocs">
      <el-tooltip class="item" effect="light" :content="doc.name" placement="top" :key="doc.id">
        <span class="button" :key="doc.id" >
          {{doc | uplaodStatusText}}
          <i class="button-icon del" @click="cancel(doc)"></i>
        </span>
      </el-tooltip>
    </template>
    <div class="button" @click="showOpenType = true" v-outside="closeShowOpen">
      <span>导入文档</span>
      <i class="button-icon add"></i>
      <div class="open-type" v-show="showOpenType">
        <label class="item-doc">
          <span class="icon icon-doc"></span>
          <input class="file-open" type="file" @change="openDocFile" />
          <span class="content">
            <span class="title">静态文档</span>
            <span class="accept">jpg\png\jpeg\pdf\docx\pptx</span>
          </span>
        </label>
        <label class="item-ppt">
          <span class="icon icon-ppt"></span>
          <input class="file-open" type="file" @change="openPPTFile" />
          <span class="content">
            <span class="title">动态文档</span>
            <span class="accept">pptx\zip</span>
          </span>
        </label>
      </div>
    </div>
  </div>
</template>

<script>

import { mapState } from 'vuex'

const STATUS_MD5 = 0
const STATUS_WAIT_UPLOAD = 1
const STATUS_UPLOAD = 2
const STATUS_WAIT_TRANSFORM = 3
const STATUS_TRANSFORM = 4
const STATUS_SUCCESS = 5

const ACCEPT_FORMAT_OF_STATIC = ['doc', 'docx', 'ppt', 'pptx', 'pdf', 'jpg', 'jpeg', 'png'];
const ACCEPT_FORMAT_OF_ANIMATION = ['ppt', 'pptx', 'zip'];


let fileId = 0

const STATUS_TEXT = {
  [STATUS_MD5]: 'MD5',
  [STATUS_WAIT_UPLOAD]: '等待上传',
  [STATUS_UPLOAD]: '上传中',
  [STATUS_WAIT_TRANSFORM]: '正在排队',
  [STATUS_TRANSFORM]: '正在转码',
  [STATUS_SUCCESS]: '添加成功'
}

const KEY_OPEN_DOC_ID = 'open_doc_id'

export default {
  name: 'DocumentManage',
  data () {
    return {
      docList: [],
      uploadDocs: [],
      STATUS_WAIT_UPLOAD,
      STATUS_UPLOAD,
      STATUS_WAIT_TRANSFORM,
      STATUS_TRANSFORM,
      STATUS_SUCCESS,
      activeDocId: '0',
      showOpenType: false
    }
  },
  props: {
    client: Object
  },
  computed: {
    
  },
  watch: {
    
  },

  filters: {
    uplaodStatusText(uploadDoc) {
      let text = STATUS_TEXT[uploadDoc.status]
      if (uploadDoc.progress != null) {
        text += (': ' + uploadDoc.progress + '%')
      }
      return text
    }
  },

  methods: {
    closeShowOpen() {
      this.showOpenType = false
    },
    findUploadFile(file) {
      let index = -1
      this.uploadDocs.forEach((doc, i) => {
        if (doc.file === file) {
          index = i
        }
      })
      return this.uploadDocs[index]
    },

    removeUploadFile(file) {
      let index = -1
      this.uploadDocs.forEach((doc, i) => {
        if (doc.file === file) {
          index = i
        }
      })
      if (index > -1) {
        this.uploadDocs.splice(index, 1)
      }
    },

    uploadFile(file, isAnimated) {
      if (this.docList.length >= 5) {
        this.$message.error(`Demo 只支持最多上传 4 个文档`)
        return
      }

      this.uploadDocs.push({
        id: 'uplaod_doc_' + fileId++,
        file,
        status: STATUS_MD5,
        name: file.name,
        progress: null
      })

      const extra = {}

      this.docUploader.addDoc(file, {
        fileName: file.name,
        isAnimated
      }, extra).then(doc => {
        this.removeUploadFile(file)
        this.docList.push(doc)
        this.$message.success(`文档 “${file.name}” 添加成功`)
      })
      .catch((error) => {
        if (error === 'abort') {
          this.$message.success(`文档 “${file.name}” 已取消上传`)
        }
        else if (error && error.getCode && error.getCode() === 6001) {
          // 动态转失败，直接使用静态的
          this.docUploader.switchToStaticDocument(extra.fid, {
            fileName: file.name,
            ext: '.' + file.name.split('.').pop()
          }).then((doc) => {
            this.docList.push(doc)
            this.$message.success(`文档 “${file.name}” 已使用静态格式`)
          })
        }
        else {
          this.$message.error(`文档 “${file.name}” 添加失败`)
        }
        this.removeUploadFile(file)
      })
    },

     /**
     * 取消上传
     */
    cancel(doc) {
      this.docUploader.cancel(doc.file)
    },

    openDocFile(event) {
      const file = event.target.files[0]
      if (!this.isAccept(ACCEPT_FORMAT_OF_STATIC, file.name.split('.').pop())) {
        this.$message.error(`文件 “${file.name}” 不支持上传`)
      }
      else {
        this.uploadFile(file, false)
      }
      event.target.value = ''
      this.closeShowOpen()
    },

    openPPTFile(event) {
      const file = event.target.files[0]
      if (!this.isAccept(ACCEPT_FORMAT_OF_ANIMATION, file.name.split('.').pop())) {
        this.$message.error(`文件 “${file.name}” 不支持上传`)
      }
      else {
        this.uploadFile(file, true)
      }
      event.target.value = ''
      this.closeShowOpen()
    },

    /**
     * 同步移除本地文档
     */
    removeDoc(id) {
      let index = -1
      this.docList.forEach((doc, i) => {
        if (doc.id === id) {
          index = i
        }
      })
      if (index > -1) {
        this.docList.splice(index, 1)
      }
    },

    /**
     * 移除房间内的文档
     */
    deleteDoc(doc) {
      this.client.document.removeDoc(doc.id).then(() => {
        this.$message.success(`文档 “${doc.name}” 已移除`)
        this.removeDoc(doc.id)
        if (doc.id === this.activeDocId) {
          this.openDoc('0')
        }
      })
    },

    setUploadStatus(file, type, progress) {
      let uploadDoc = this.findUploadFile(file)
      if (uploadDoc) {
        uploadDoc.progress = progress
        if (type === 'md5') {
          uploadDoc.status = STATUS_MD5
        }
        else if (type === 'wait') {
          uploadDoc.status = STATUS_WAIT_UPLOAD
        }
        else if (type === 'upload') {
          uploadDoc.status = STATUS_UPLOAD
        }
        else if (type === 'queue') {
          uploadDoc.status = STATUS_WAIT_TRANSFORM
        }
        else if (type === 'transform') {
          uploadDoc.status = STATUS_TRANSFORM
        }
        else if (type === 'success') {
          uploadDoc.status = STATUS_SUCCESS
        }
        
      }
    },

    /**
     * 打开文档
     */
    openDoc(id) {
      this.activeDocId = id
      this.$emit('openDoc', id)
      this.client.sendBroadcastMessage(KEY_OPEN_DOC_ID, id)
    },

    isAccept(exts, ext) {
      let has = false
      for (let i = 0; i < exts.length; i++) {
        if (exts[i] === ext) {
          has = true
        }
      }
      return has
    }
  },
  mounted () {
    this.docUploader = this.client.createDocUploader({
      onprogress: (file, type, progress) => {
        this.setUploadStatus(file, type, progress)
      }
    })

    this.client.document
      /**
       * 远端添加了一个文档
       */
      .on(BRTM.Events.Document.DOC_ADDED, data => {
        this.docList.push(data.doc)
      })
      /**
       * 远端删除了一个文档
       */
      .on(BRTM.Events.Document.DOC_REMOVED, data => {
        this.removeDoc(data.docId)
      });
      
    /**
     * 监听收到广播消息
     */
    this.client.onBroadcastMessage(KEY_OPEN_DOC_ID, (message, fromId) => {
      this.activeDocId = message || '0'
      this.$emit('openDoc', this.activeDocId)
    })

    this.client.document.requestAllDocs().then(data => {
      this.docList = data.docList
      this.client.getCachedBroadcastMessage(KEY_OPEN_DOC_ID).then(data => {
        this.activeDocId = data.message || '0'
        this.$emit('openDoc', this.activeDocId)
      })
    })
  },
  components: {
  },

  boforeDestroy () {
    this.docUploader.destroy()
  }
}
</script>
<style scoped lang="less">
  .document-manage {
    .button {
      position: relative;
      display: inline-block;
      margin-left: 10px;
      min-width: 100px;
      height: 28px;
      background: rgba(255,255,255,0.6);
      border-radius: 8px;
      border: 1px solid rgba(255,255,255,0.6);
      box-shadow: 0px 5px 10px 0px rgba(5,6,53,0.1);
      font-size: 14px;
      font-family: PingFangSC, PingFangSC-Regular;
      font-weight: 400;
      text-align: left;
      color: #000000;
      line-height: 26px;
      padding: 0 24px 0 12px;
      cursor: pointer;
      &:hover {
        background: rgba(187,222,251,0.6);
        border: 1px solid rgba(23,149,255,0.2);
      }
      &.active {
        background: #1795ff;
        border: 1px solid #1795ff;
        color: #fff;
      }
      .button-icon {
        float: right;
        margin-top: 4px;
        width: 16px;
        height: 16px;
        background: url(../assets/sprites/top-icon-sprites@2x.png) no-repeat;
        background-size: 169px 32px;
        background-position: -31px 0;
        position: absolute;
        right: 3px;
        &.del {
          background-position: -31px 0;
          &.active {
            background-position-x: -63px;
          }
        }
        &.add {
          background-position: -47px 0;
        }
        &.active {
          background-position: -63px 0;
          &:hover {
            background-position: -31px -16px;
          }
        }
        &:hover {
          background-position-y: -16px;
        }
      }

      .open-type {
        position: absolute;
        background: rgba(255, 255, 255, 0.6);
        border-radius: 8px;
        box-shadow: 0px 5px 10px 0px rgba(5, 6, 53, 0.1);
        user-select: none;
        padding: 16px;
        text-align: center;
        margin-top: 8px;
        left: -178px;

        .item-doc,
        .item-ppt {
          display: block;
          cursor: pointer;
          width: 240px;
          height: 54px;
          background: #fff;
          border-radius: 12px;
          border: 1px solid #EEEEEE;
          text-align: left;
          padding-top: 6px;

          &:hover {
            color: #1795FF;
          }
        }

        .icon {
          display: inline-block;
          width: 36px;
          height: 36px;
          margin-left: 8px;
          vertical-align: text-bottom;
          background: url(../assets/components/document-type.png);
          background-image: -webkit-image-set(
            url(../assets/components/document-type@2x.png) 2x
          );
        }
        .content {
          display: inline-block;
          margin-left: 6px;
          line-height: 20px;

          .title {
            display: block;
            text-align: left;
          }
          .accept {
            display: block;
            font-size: 12px;
            text-align: left;
          }
        }

        .item-ppt {
          margin-top: 8px;

          .icon {
            background-position: 0 -36px;
          }

          &:hover {
            .icon {
              background-position: -36px -36px;
            }
          }
        }

        .item-doc {
          &:hover {
            .icon {
              background-position: -36px 0;
            }
          }
        }
      }
    }

    .file-open {
      position: absolute;
      width: 100%;
      top: 0;
      visibility: hidden;
    }
  }
</style>
