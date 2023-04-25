<template>
  <div class="whitebroad" v-loading="loading" element-loading-text="正在加载资源...">
    <div ref="parent" class="container"></div>
    <span v-for="item in lasers" :key="item.userId" :style="{transform: `translate(${item.left + offsetX}px, ${item.top + offsetY}px)`}"  class="cursor"></span>
    <div class="white-footer" >
      <div @click="toggleScreenShare">{{  screenShared ? '停止共享' : '共享屏幕' }}</div>
      <div @click="endWhitebroad">结束</div>
    </div>

    <el-dialog
      title="提示"
      :visible.sync="whiteboarddialogVisible"
      width="30%"
      custom-class="boardDialog"
      center>
      <span>确认关闭白板吗？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="whiteboarddialogVisible = false">取消</el-button>
        <!-- <el-button @click="toggleWhitebroad(true, true)">关闭白板</el-button> -->
        <el-button type="primary" @click="toggleWhitebroad(true, false)">关闭白板</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>

import { mapState } from 'vuex'
import debounce from '../util/debounce'
import Queue from '../util/Queue'
import { downloadFile } from '../util/download'

export default {
  name: 'DocumentPlayer',
  data () {
    return {
      page: 0,
      step: 0,
      loading: false,
      lasers: [],
      offsetX: 0,
      offsetY: 0,
      whiteboarddialogVisible:false,
    }
  },
  props: {
    docId: String,
    config: Object,
    client: Object,
    docToolsOptions: Object,
    whitebroadOpened: {
      type: Boolean,
      default: false
    },
    screenShared: {
      type: Boolean,
      default: false
    },
  },
  computed: {
    ...mapState({
      useLaser: state => state.useLaser
    })
  },
  watch: {
    docId: function(id) {
      this.createPlayer(id)
    }
  },
  methods: {
    toggleScreenShare () {
      this.$emit('toggleScreenShare');
    },
    endWhitebroad(){
      this.whiteboarddialogVisible = true;
    },
    toggleWhitebroad(force, local) {
      if (this.whitebroadOpened && !force) {
        this.whiteboarddialogVisible = true
      }
      else {
        this.$emit('toggleWhitebroad', local)
        this.whiteboarddialogVisible = false
      }
    },
    startLoading() {
      if (this.startLoadingTimer) {
        return
      }
      this.startLoadingTimer = setTimeout(() => {
        this.loading = true
        this.startLoadingTimer = null
      }, 500)
    },
    stopLoading() {
      if (this.startLoadingTimer) {
        clearTimeout(this.startLoadingTimer)
        this.startLoadingTimer = null
      }
      this.loading = false
    },
    createPlayer(id) {
      const instance = this
      const doc = this.client.document.docData.getDocumentById(id)
      if (doc) {
        if (this.player) {
          this.player.destroy()
        }
        const container = this.$refs.parent
        this.player = BRTM.createDocumentPlayer(doc, this.client, {
          container: container,
          allowClick: true,
          hasPainter: true,
          canDrawing: true, 
          preserveDrawingBuffer: true,
          onLoadingStart: function(page, step, pageChanged) {
            // 翻页了显示加载
            if (pageChanged) {
              instance.startLoading()
            }
            const imageWidth = instance.player.imageWidth
            const imageHeight = instance.player.imageHeight
            const parentWidth = container.parentNode.offsetWidth
            const parentHeight = container.parentNode.offsetHeight

            if (doc.id === '0') {
              container.style.left = '0px'
              container.style.top = '0px'
            }
            else {
              instance.offsetX = Math.floor((parentWidth - imageWidth) / 2)
              instance.offsetY = Math.floor((parentHeight - imageHeight) / 2)
              container.style.left = instance.offsetX + 'px'
              container.style.top = instance.offsetY + 'px'
            }
            
          },
          onLoadingEnd: function(page, step, pageChanged, status) {
            instance.stopLoading()
            if (status === 'timeout') {
              instance.$message.error('图片加载超时')
            }
            else if (status === 'failure') {
              instance.$message.error('图片加载失败')
            }
          },
          onPageChange: function (page, step) {
            instance.docToolsOptions.canPrev = this.hasPrev()
            instance.docToolsOptions.canNext = this.hasNext()
            instance.docToolsOptions.currentPage = page + 1
          },
          onStepChange: function (page, step) {
            instance.docToolsOptions.canPrev = this.hasPrev()
            instance.docToolsOptions.canNext = this.hasNext()
            instance.docToolsOptions.currentPage = page + 1
          },
          onPageCountChage: function(totalPage) {
            instance.docToolsOptions.canPrev = this.hasPrev()
            instance.docToolsOptions.canNext = this.hasNext()
            instance.docToolsOptions.totalPage = totalPage
            instance.docToolsOptions.canDeletePage = this.canOperatePage() && totalPage !== 1
            instance.docToolsOptions.canAddPage = this.canOperatePage() && totalPage < 10
          },
          getImageDimension: function(width, height) {
            const parentWidth = container.parentNode.offsetWidth
            const parentHeight = container.parentNode.offsetHeight
            
            if (doc.id === '0') {
              if (parentWidth / 2 < parentHeight) {
                return {
                  width: parentHeight * 2,
                  height: parentHeight
                }
              }
              else {
                return {
                  width: parentWidth,
                  height: Math.floor(parentWidth / 2)
                }
              }
            }
            else {
              const scaleX = width / parentWidth;
              const scaleY = height / parentHeight;
              const maxScale = Math.max(scaleX, scaleY);

              return {
                width: Math.floor(width / maxScale),
                height: Math.floor(height / maxScale)
              }
            }
          }
        }, this.config)
        this.docToolsOptions.canPrev = this.player.hasPrev()
        this.docToolsOptions.canNext = this.player.hasNext()
        this.docToolsOptions.isShowOperatePage = !this.player.canOperatePage()
        this.docToolsOptions.canAddPage = this.player.canOperatePage() && this.player.getTotalPage() < 10
        this.docToolsOptions.canDeletePage = this.player.canOperatePage() && this.player.getTotalPage() > 1
        this.docToolsOptions.totalPage = this.player.getTotalPage()
        this.docToolsOptions.currentPage = this.player.getCurrentPage() + 1
        this.docToolsOptions.canOperatePPT = this.player.isAnimatedDocument()

        this.player.on('mouse-move', data => {
          if (this.useLaser) {
            this.client.document.sendLaser({
              x: data.x,
              y: data.y,
            }, this.player.getCurrentDocId(), this.player.getCurrentPage())
          }
        })
      }
    },

    addPage() {
      if (this.player && this.player.canOperatePage()) {
        this.player.addPage().then(() => {
          
        })
      }
    },
    deletePage(page) {
      if (this.player && this.player.canOperatePage()) {
        this.player.removePage(page).then(() => {
          if (page === this.docToolsOptions.currentPage - 1) {
            this.changePage(Math.max(0, page - 1))
          }
        })
      }
    },

    prev() {
      if (this.player && this.player.hasPrev()) {
        this.startLoading()
        this.player.prev().catch(error => {
          console.log(error)
          this.loading = false
          this.$message.error('操作失败，请重试')
        })
      }
    },

    next() {
      if (this.player && this.player.hasNext()) {
        this.startLoading()
        this.player.next().catch(error => {
          console.log(error)
          this.$message.error('操作失败，请重试')
        })
      }
    },

    changePage(page, step) {
      if (this.player) {
        this.startLoading()
        this.player.changePage(page, step).catch(error => {
          console.log(error)
          this.$message.error('操作失败，请重试')
        })
      }
    },

    changeDoc(docId) {
      this.docId = docId
    },

    clear(select) {
      if (this.player) {
        if (select) {
          this.player.deleteSelectShape()
        }
        else {
          this.player.clearShapes()
        }
      }
    },

    getLaser(userId) {
      let result
      for (let i = 0; i < this.lasers.length; i++) {
        if (this.lasers[i].userId === userId) {
          result = this.lasers[i]
          break
        }
      }
      return result
    },

    removeLaser(userId) {
      let index = -1
      for (let i = 0; i < this.lasers.length; i++) {
        if (this.lasers[i].userId === userId) {
          index = i
          break
        }
      }
      if (index > -1) {
        this.lasers.splice(index, 1)
      }
    },

    download() {
      if (this.player) {
        const doc = this.client.document.docData.getDocumentById(this.docId)
        downloadFile(doc.name + '_' + (this.player.getCurrentPage() + 1) + '.png', this.player.snapshot())
      }
    },
    reload() {
      if (this.player) {
        this.player.reload()
      }
    }
  },
  mounted () {
    this.createPlayer(this.docId)

    const instance = this

    this.client.document.on(BRTM.Events.Document.LASER_RECEIVED, data => {
      let points = data.history
      if (!points.length) {
        points.push({
          x: data.laser.x,
          y: data.laser.y
        })
      }
      const laser = instance.getLaser(data.fromId)
      if (laser) {
        let interval = ~~(data.interval / points.length)
        interval = interval < 20 ? 20 : interval
        laser.queue.clear()
        points.forEach(function (point) {
          laser.queue.add(
            function(nextTask) {
              laser.left = Math.floor(instance.player.imageWidth * point.x) - 16
              laser.top = Math.floor(instance.player.imageHeight * point.y) - 16
              clearTimeout(laser.timer)
              laser.timer = setTimeout(() => {
                laser.queue.clear()
                laser.queue.destroy()
                instance.removeLaser(data.fromId)
              }, 2000)
              setTimeout(
                nextTask,
                interval
              )
            }
          )
        })
      }
      else {
        const queue = new Queue((handler, callback) => {
          handler(callback);
        })
        const laser = {
          userId: data.fromId,
          queue,
          timer: setTimeout(() => {
            queue.clear()
            queue.destroy()
            instance.removeLaser(data.fromId)
          }, 2000),
          left: 0,
          top: 0
        }
        instance.lasers.push(laser)
        let interval = ~~(data.interval / points.length)
        interval = interval < 20 ? 20 : interval
        points.forEach(function (point) {
          queue.add(
            function(nextTask) {
              laser.left = Math.floor(instance.player.imageWidth * point.x)
              laser.top = Math.floor(instance.player.imageHeight * point.y)
              clearTimeout(laser.timer)
              laser.timer = setTimeout(() => {
                queue.clear()
                queue.destroy()
                instance.removeLaser(data.fromId)
              }, 2000)
              setTimeout(
                nextTask,
                interval
              )
            }
          )
        })
      }
    })
  },
  components: {
  },

  boforeDestroy () {
    if (this.player) {
      this.player.destroy()
      this.player = null
    }
  }
}
</script>
<style scoped lang="less">
  .white-footer{
    height: 35px;
    width: 100%;
    background: #fff;
    position: absolute;
    left: 0;
    bottom: 0;
    display: flex;
    justify-content: end;
    padding-right: 20px;
    &:hover{
      opacity: 0.8;
    }
    
    &>div{
      margin-top: 7.5px;
      // width: 50px;
      height: 20px;
      text-align: center;
      line-height: 20px;
      border-radius: 4px;
      color: #fff;
      background: red;
      cursor: pointer;
      padding: 0 20px;
      margin-left: 20px;
      &:hover{
        opacity: 0.8;
      }
    }
  }
  .whitebroad /deep/ .brtm-whiteboard {
    // height: 100%;
    img{
      // height: 100% !important;
      // width: 100% !important;
    }
    canvas {
      // height: 100%;
      // width: 100% !important;
      cursor: url(../assets/cursor/select.png) 5 5, auto;
      cursor: -webkit-image-set(url(../assets/cursor/select@2x.png) 2x) 5 5, auto;
      &:active {
        cursor: url(../assets/cursor/select-active.png) 5 5, auto;
        cursor: -webkit-image-set(url(../assets/cursor/select-active@2x.png) 2x) 5 5, auto;
      }
      &[data-tool="doodle"] {
        cursor: url(../assets/cursor/doodle.png) 5 32, auto;
        cursor: -webkit-image-set(url(../assets/cursor/doodle@2x.png) 2x) 5 32, auto;
        &:active {
          cursor: url(../assets/cursor/doodle-active.png) 5 32, auto;
          cursor: -webkit-image-set(url(../assets/cursor/doodle-active@2x.png) 2x) 5 32, auto;
        }
      }
      &[data-tool="eraser"] {
        cursor: url(../assets/cursor/eraser.png) 14 14, auto;
        cursor: -webkit-image-set(url(../assets/cursor/eraser@2x.png) 2x) 14 14, auto;
        &:active {
          cursor: url(../assets/cursor/eraser-active.png) 14 14, auto;
          cursor: -webkit-image-set(url(../assets/cursor/eraser-active@2x.png) 2x) 14 14, auto;
        }
      }
      &[data-tool="point"] {
        cursor: url(../assets/cursor/point.png) 16 16, auto;
        cursor: -webkit-image-set(url(../assets/cursor/point@2x.png) 2x) 16 16, auto;
      }
    }

    .brtm-ppt-iframe {
      opacity: 0;
      &.brtm-active {
        opacity: 1
      }
    }
  }

  .whitebroad {
    width: 100%;
    height: 100%;
    .container {
      position: absolute;
      // height: 100%;
      // width: 100%;
      .brtm-whiteboard{
        height: 100%;
      }
    }
    .cursor {
      position: absolute;
      left: 0;
      top: 0;
      display: inline-block;
      width: 32px;
      height: 32px;
      background: url(../assets/cursor/point.png);
      background-image: -webkit-image-set(
        url(../assets/cursor/point@2x.png) 2x
      );
    }

    & /deep/ .el-loading-mask {
      z-index: 0;
    }
  }

    .boardDialog  /deep/ .el-dialog__headerbtn{
      margin-right: 8px !important;
    }
</style>
