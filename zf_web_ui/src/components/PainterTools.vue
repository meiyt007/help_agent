<template>
  <div class="painter-tools-container" v-outside="closeMenu">
    <ul class="painter-tools">
      <template v-for="item in tools">
        <el-tooltip :key="item.key" class="item" effect="light" :content="item.name" placement="top">
          <li :key="item.key" class="item" :class="[{'active': item.key === activeTool}, 'icon-' + ( item.key === 'shape' ? activeShape : item.key)]" @click="selectTool(item.key)">
            <span v-show="item.key === 'palette'" class="color" :style="{background: activeColor}"></span>
          </li>
        </el-tooltip>
      </template>
    </ul>
    <div class="tool-menu" :class="activeTool" v-show="showMenu">
      <ul class="painter-line" v-if="activeTool === 'doodle'" >
        <li class="item painter-line-1" :class="lineType === 1 ? 'active' : ''" @click="changLineType(1)"></li>
        <li class="item painter-line-2" :class="lineType === 2 ? 'active' : ''" @click="changLineType(2)"></li>
        <li class="item painter-line-3" :class="lineType === 3 ? 'active' : ''" @click="changLineType(3)"></li>
        <li class="item painter-line-4" :class="lineType === 4 ? 'active' : ''" @click="changLineType(4)"></li>
      </ul>
      <ul class="palette-color" v-if="activeTool === 'palette'" >
        <li class="list" v-for="(list, index) in paletteColor" :key="index">
          <span class="color-item" v-for="color in list" :key="color" :style="{background: color}" :class="color === activeColor ? 'active' : ''" @click="selectColor(color)"></span>
        </li>
      </ul>
      <ul class="shape-tools" v-if="activeTool === 'shape'">
        <li class="list" v-for="(list, index) in shapeTools" :key="index">
          <span class="shape-item" v-for="shape in list" :key="shape" :class="shape === activeShape ? 'active' : ''" @click="selectShape(shape)" :data-type="shape"></span>
        </li>
      </ul>
      <div class="text-tools" v-if="activeTool === 'text'">
        <span class="font-size">{{fontSize}}</span>
        <el-slider
          class="slider"
          v-model="fontIndex"
          :min="0"
          :max="FONT_DATA.length - 1"
          :show-tooltip="false"
          :format-tooltip="formatFontSize"
          :step="1">
        </el-slider>
        <span class="blod" :class="blod ? 'active' : ''" @click="toggleBlod"></span>
        <span class="italic" :class="italic ? 'active' : ''" @click="toggleItalic"></span>
      </div>
      <div class="delete-tools" v-if="activeTool === 'delete'">
        <el-tooltip class="item" effect="light" content="橡皮檫" placement="top">
          <span class="item icon-eraser" @click="selectEraser" :class="useEraser ? 'active' : ''"></span>
        </el-tooltip>
        <el-tooltip class="item" effect="light" content="清空" placement="top">
          <span class="item icon-clear" @click="dialogVisible = true"></span>
        </el-tooltip>
      </div>
    </div>
    <el-dialog
      title="提示"
      :visible.sync="dialogVisible"
      append-to-body
      width="30%"
      center>
      <span style="text-align: center;display: inline-block;width: 100%;margin-top: 50px;">确认清除所有画笔吗？</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="clear">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>

import { mapState } from 'vuex'

const FONT_DATA = [12,14,16,18,20,22,24,26,28,30,40,80]

export default {
  name: 'PainterTools',
  data () {
    return {
      tools: [
        {
          key: this.config.tools.SELECT,
          name: '选取工具'
        },
        {
          key: this.config.tools.DOODLE,
          name: '画笔'
        },
        {
          key: 'point',
          name: '激光笔'
        },
        {
          key: 'shape',
          name: '形状'
        },
        {
          key: this.config.tools.TEXT,
          name: '文字'
        },
        {
          key: 'palette',
          name: '调色板'
        },
        {
          key: 'delete',
          name: '删除'
        },
        {
          key: 'download',
          name: '下载'
        }
      ],
      lastActiveTool: null,
      activeTool: this.config.tools.SELECT,
      lineType: 1,
      shapeType: this.config.tools.RECT,
      oval: false,
      showMenu: false,
      activeColor: '#f69a21',
      activeShape: 'rect-o',
      paletteColor: [
        ['#000000', '#333333', '#555555', '#7f7f7f', '#aaaaaa', '#d7d7d7', '#f1f1f1', '#ffffff'],
        ['#ee808b', '#face92', '#ffff83', '#cbf883', '#86fdff', '#80d4f6', '#1795ff', '#c27fff'],
        ['#dc0019', '#f69a21', '#fbff01', '#97f008', '#03fdff', '#06a5f0', '#0100fc', '#8401ff'],
        ['#a40015', '#b77419', '#bebf01', '#72b504', '#04bdbf', '#057bb1', '#0000c0', '#480080'],
        ['#6f000d', '#7d4c12', '#808000', '#468331', '#057e7f', '#08516f', '#3487b8', '#026aa7']
      ],
      shapeTools: [
        ['rect-o', 'triangle-o', 'circle-o'],
        ['rect', 'triangle', 'circle'],
        ['line', 'arrow', 'arrows']
      ],
      fontIndex: 4,
      blod: false,
      italic: false,
      useEraser: false,

      dialogVisible: false,

      FONT_DATA: FONT_DATA
    }
  },
  props: {
    config: Object
  },
  computed: {
    fontSize: function () {
      return FONT_DATA[this.fontIndex]
    }
  },
  watch: {
    fontSize: function (fontSize) {
      this.config.setFontSize(Math.ceil(fontSize / 1000 * this.$parent.$refs.docPlayer.player.getImageWidth()))
    },

    activeTool: function (tool) {
      if (tool === 'point') {
        this.$store.commit('useLaser', {
          useLaser: true
        })
      }
      else {
        this.$store.commit('useLaser', {
          useLaser: false
        })
      }
    }
  },
  methods: {
    closeMenu: function () {
      this.showMenu = false
      if (this.activeTool === 'palette' && this.lastActiveTool) {
        this.selectTool(this.lastActiveTool)
        this.showMenu = false
      }
      if (this.activeTool === 'delete' && this.lastActiveTool && !this.useEraser) {
        this.selectTool(this.lastActiveTool)
        this.showMenu = false
      }
    },
    selectTool: function (key) {

      if (key === 'download') {
        this.$emit('download')
        return
      }

      this.showMenu = true
      if (this.activeTool !== 'palette' && this.activeTool !== 'delete') {
        this.lastActiveTool = this.activeTool
      }
      this.activeTool = key
      if (key !== 'palette' && key !== 'delete') {
        if (key === 'shape') {
          this.selectShape(this.activeShape, true)
        }
        else {
          this.config.setShapeType(key)
          if (key === 'doodle') {
            this.config.setLineWidth(this.lineType * 2)
            // 涂鸦设置描边不填充
            this.config.setStroke(true)
            this.config.setFill(false)
          }
          if (key === 'text') {
            // 文字不填充
            this.config.setFill(false)
          }
        }
        this.useEraser = false
      }
    },
    changLineType: function (index) {
      this.lineType = index
      this.config.setLineWidth(index * 2);
    },
    closeLineWidth() {
      this.open = false
    },
    selectColor(color) {
      this.activeColor = color
      this.config.setStrokeColor(color)
      this.config.setFillColor(color)

      this.closeMenu()
    },
    selectShape(shape, open) {
      this.activeShape = shape

      if (shape === 'rect-o') {
        this.config.setShapeType(this.config.tools.RECT)
        this.config.setFill(false)
        this.config.setStroke(true)
      }
      else if (shape === 'rect') {
        this.config.setShapeType(this.config.tools.RECT)
        this.config.setFill(true)
        this.config.setStroke(false)
      }
      else if (shape === 'triangle-o') {
        this.config.setShapeType(this.config.tools.REGULAR_POLYGON)
        this.config.setFill(false)
        this.config.setStroke(true)
        this.config.setSides(3)
      }
      else if (shape === 'triangle') {
        this.config.setShapeType(this.config.tools.REGULAR_POLYGON)
        this.config.setFill(true)
        this.config.setStroke(false)
        this.config.setSides(3)
      }
      else if (shape === 'circle-o') {
        this.config.setShapeType(this.config.tools.CIRCLE)
        this.config.setFill(false)
        this.config.setStroke(true)
      }
      else if (shape === 'circle') {
        this.config.setShapeType(this.config.tools.CIRCLE)
        this.config.setFill(true)
        this.config.setStroke(false)
      }
      else if (shape === 'line') {
        this.config.setShapeType(this.config.tools.LINE)
      }
      else if (shape === 'arrow') {
        this.config.setShapeType(this.config.tools.ARROW)
        this.config.setDouble(false)
      }
      else if (shape === 'arrows') {
        this.config.setShapeType(this.config.tools.ARROW)
        this.config.setDouble(true)
      }

      this.config.setLineWidth(2)

      if (!open) {
        this.closeMenu()
      }
    },
    toggleBlod() {
      this.blod = !this.blod
      this.config.enableBold(this.blod)
    },
    toggleItalic() {
      this.italic = !this.italic
      this.config.enableFontItalic(this.italic)
    },
    formatFontSize(index) {
      return FONT_DATA[index]
    },
    selectEraser() {
      this.config.setShapeType(this.config.tools.ERASER)
      this.useEraser = true
      this.closeMenu()
      this.$emit('clear', true)
    },
    clear() {
      this.$emit('clear')
      this.dialogVisible = false
      this.closeMenu()
    },
    resetFontSize() {
      this.config.setFontSize(Math.ceil(this.fontSize / 1000 * this.$parent.$refs.docPlayer.player.getImageWidth()))
    }
  },
  mounted () {
    // 设置默认描边和填充颜色
    if (this.activeColor) {
      this.config.setStrokeColor(this.activeColor)
      this.config.setFillColor(this.activeColor)
    }
    // 设置默认字体大小
    this.config.setFontSize(this.fontSize)
  },
  components: {
  },

  boforeDestroy () {
    
  }
}
</script>
<style scoped lang="less">
  .painter-tools-container {
    .painter-tools {
      margin: 0;
      padding: 3px 10px;
      background: rgba(255,255,255,0.6);
      border-radius: 8px;
      box-shadow: 0px 5px 10px 0px rgba(5,6,53,0.1);
      display: inline-block;
      height: 40px;
    }
    .tool-menu {
      position: absolute;

      &.doodle {
        top: 48px;
      }

      &.palette {
        left: 0;
        top: 48px;
      }

      &.shape {
        left: 112px;
        top: 48px;
      }
      &.text {
        left: 124px;
        top: 48px;
      }
      &.delete {
        left: 215px;
        top: 48px;
      }
    }
    .painter-line {
      height: 56px;
      padding: 10px 20px;
      background: rgba(255,255,255,0.6);
      border-radius: 8px;
      box-shadow: 0px 5px 10px 0px rgba(5,6,53,0.1);
      display: inline-block;
      >li {
        display: inline-block;
        width: 36px;
        height: 36px;
        background: url(../assets/tools/painter-tool.png);
        background-image: -webkit-image-set(
          url(../assets/tools/painter-tool@2x.png) 2x
        );
        cursor: pointer;
        & + li {
          margin-left: 20px;
        }
        &.item {
          &.painter-line-1 {
            background-position: -252px 0;
            &:hover {
              background-position: -252px -36px;
            }
            &.active {
              background-position: -252px -72px;
            }
          }
          &.painter-line-2 {
            background-position: -288px 0;
            &:hover {
              background-position: -288px -36px;
            }
            &.active {
              background-position: -288px -72px;
            }
          }
          &.painter-line-3 {
            background-position: -324px 0;
            &:hover {
              background-position: -324px -36px;
            }
            &.active {
              background-position: -324px -72px;
            }
          }
          &.painter-line-4 {
            background-position: -360px 0;
            &:hover {
              background-position: -360px -36px;
            }
            &.active {
              background-position: -360px -72px;
            }
          }
        }
      }
    }
    .item {
      position: relative;
      display: inline-block;
      width: 36px;
      height: 36px;
      background: url(../assets/tools/painter-tool.png);
      background-image: -webkit-image-set(
        url(../assets/tools/painter-tool@2x.png) 2x
      );
      background-position-y: 0;
      cursor: pointer;
      transform: scale(0.7);

      & + .item {
        // margin-left: 20px;
      }

      &:hover {
        background-position-y: -36px;
      }
      &.active {
        background-position-y: -72px;
      }

      &.icon-select {
        background-position-x: 0;
      }

      &.icon-doodle {
        background-position-x: -36px;
      }

      &.icon-delete {
        background-position-x: -72px;
      }
      &.icon-point {
        background-position-x: -108px;
      }
      &.icon-shape {
        background-position-x: -144px;
      }
      &.icon-text {
        background-position-x: -180px;
      }
      &.icon-palette {
        background-position-x: -864px;
      }
      &.icon-download {
        background-position-x: -216px;
      }

      &.icon-rect-o {
        background-position-x: -396px;
      }
      &.icon-triangle-o {
        background-position-x: -432px;
      }
      &.icon-circle-o {
        background-position-x: -468px;
      }
      &.icon-rect {
        background-position-x: -504px;
      }
      &.icon-triangle {
        background-position-x: -540px;
      }
      &.icon-circle {
        background-position-x: -576px;
      }
      &.icon-line {
        background-position-x: -612px;
      }
      &.icon-arrow {
        background-position-x: -648px;
      }
      &.icon-arrows {
        background-position-x: -684px;
      }

      .color {
        display: inline-block;
        width: 20px;
        height: 20px;
        border-radius: 50%;
        position: absolute;
        left: 8px;
        top: 8px;
      }
    }

    .palette-color {
      list-style: none;
      padding: 10px;
      background: #ffffff;
      border: 1px solid #e4e7ed;
      border-radius: 8px;
      .list {
        height: 32px;
        .color-item {
          position: relative;
          display: inline-block;
          width: 32px;
          height: 32px;
          cursor: pointer;

          &:after {
            content: '';
            display: none;
            width: 32px;
            height: 32px;
            border: 2px solid #1795ff;
            position: absolute;
            top: -2px;
            left: -2px;
            z-index: 1;
          }

          &:hover {
            &:after {
              display: inline-block;
              opacity: 0.72;
            }
          }
          &.active {
            &:after {
              display: inline-block;
            }
          }
        }
      }
    }

    .shape-tools {
      list-style: none;
      padding: 12px 20px;
      background: #ffffff;
      box-shadow: 0px 5px 10px 0px rgba(5,6,53,0.1); 
      border-radius: 8px;

      .list {
        height: 36px;
        .shape-item {
          display: inline-block;
          width: 36px;
          height: 36px;
          background: url(../assets/tools/painter-tool.png);
          background-image: -webkit-image-set(
            url(../assets/tools/painter-tool@2x.png) 2x
          );
          cursor: pointer;
          &:hover {
            background-position-y: -36px;
          }
          &.active {
            background-position-y: -72px;
          }
          &[data-type="rect-o"] {
            background-position-x: -396px;
          }
          &[data-type="triangle-o"] {
            background-position-x: -432px;
          }
          &[data-type="circle-o"] {
            background-position-x: -468px;
          }
          &[data-type="rect"] {
            background-position-x: -504px;
          }
          &[data-type="triangle"] {
            background-position-x: -540px;
          }
          &[data-type="circle"] {
            background-position-x: -576px;
          }
          &[data-type="line"] {
            background-position-x: -612px;
          }
          &[data-type="arrow"] {
            background-position-x: -648px;
          }
          &[data-type="arrows"] {
            background-position-x: -684px;
          }

          & + .shape-item {
            margin-left: 20px;
          }
        }

        & + .list {
          margin-top: 10px;
        }
      }

    }

    .text-tools {
      padding: 0;
      background: #ffffff;
      box-shadow: 0px 5px 10px 0px rgba(5,6,53,0.1); 
      border-radius: 8px;
      height: 60px;
      display: flex;
      align-items: center;
      .font-size {
        display: inline-block;
        font-size: 18px;
        line-height: 42px;
        width: 36px;
        height: 36px;
        text-align: center;
        vertical-align: top;
        transform: scale(0.7);
      }

      .slider {
        display: inline-block;
        width: 80px;
        height: 36px;
        // margin-left: 10px;
        background-image: url(../assets/sprites/font-size-slider.png);
        background-image: -webkit-image-set(
          url(../assets/sprites/font-size-slider@2x.png) 2x
        );
        background-repeat: no-repeat;
        background-position: 0;
        padding: 0 10px;
        transform: scale(0.7);
        & /deep/ .el-slider__bar {
          background-color: rgba(0,0,0,0);
        }
        & /deep/ .el-slider__runway {
          background-color: rgba(0,0,0,0);
        }
        & /deep/ .el-slider__button {
          width: 20px;
          height: 20px;
          background: #1795ff;
          border: 2px solid #ffffff;
          box-shadow: 0px 5px 10px 0px rgba(5,6,53,0.1); 
        }
      }

      .blod {
        display: inline-block;
        width: 36px;
        height: 36px;
        // margin-left: 20px;
        background: url(../assets/tools/painter-tool.png);
        background-image: -webkit-image-set(
          url(../assets/tools/painter-tool@2x.png) 2x
        );
        background-position-y: 0;
        background-position-x: -720px;
        transform: scale(0.7);
        &:hover {
          background-position-y: -36px;
        }
        &.active {
          background-position-y: -72px;
        }
        cursor: pointer;
      }
      .italic {
        display: inline-block;
        width: 36px;
        height: 36px;
        // margin-left: 20px;
        background: url(../assets/tools/painter-tool.png);
        background-image: -webkit-image-set(
          url(../assets/tools/painter-tool@2x.png) 2x
        );
        background-position-y: 0;
        background-position-x: -756px;
        transform: scale(0.7);
        &:hover {
          background-position-y: -36px;
        }
        &.active {
          background-position-y: -72px;
        }
        cursor: pointer;
      }
    }

    .delete-tools {
      background: rgba(255,255,255,0.6);
      border-radius: 8px;
      box-shadow: 0px 5px 10px 0px rgba(5,6,53,0.1);
      padding: 5px;
      height: 45px;

      .item {
        display: inline-block;
        width: 36px;
        height: 36px;

        background: url(../assets/tools/painter-tool.png);
        background-image: -webkit-image-set(
          url(../assets/tools/painter-tool@2x.png) 2x
        );
      }

      .icon-eraser {
        background-position: -792px 0;

        &:hover {
          background-position: -792px -36px;
        }
        &.active {
          background-position: -792px -72px;
        }
      }
      .icon-clear {
        background-position: -828px 0;
        &:hover {
          background-position: -828px -36px;
        }
        &:active {
          background-position: -828px -72px;
        }
      }
    }
  }
</style>
