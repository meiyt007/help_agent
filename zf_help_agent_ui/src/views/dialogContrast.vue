<template>
  <div class="dialogContrast">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="材料1" name="first">
            <div>
              <div class="tools">
                <el-button
                  :theme="'default'"
                  type="submit"
                  :title="'基础按钮'"
                  @click.stop="prePage"
                  class="mr10"
                >
                  上一页</el-button
                >
                <el-button
                  :theme="'default'"
                  type="submit"
                  :title="'基础按钮'"
                  @click.stop="nextPage"
                  class="mr10"
                >
                  下一页</el-button
                >
                <div class="page">{{ pageNum }}/{{ pageTotalNum }}</div>
                <el-button
                  :theme="'default'"
                  type="submit"
                  :title="'基础按钮'"
                  @click.stop="clock"
                  class="mr10"
                >
                  顺时针</el-button
                >
                <el-button
                  :theme="'default'"
                  type="submit"
                  :title="'基础按钮'"
                  @click.stop="counterClock"
                  class="mr10"
                >
                  逆时针</el-button
                >
              </div>
              <pdf
                ref="pdf"
                :src="url"
                :page="pageNum"
                :rotate="pageRotate"
                @progress="loadedRatio = $event"
                @page-loaded="pageLoaded($event)"
                @num-pages="pageTotalNum = $event"
                @error="pdfError($event)"
                @link-clicked="page = $event"
              >
              </pdf>
            </div>
          </el-tab-pane>
          <el-tab-pane label="材料2" name="second">配置管理</el-tab-pane>
          <el-tab-pane label="材料3" name="third">角色管理</el-tab-pane>
          <el-tab-pane label="材料4" name="fourth">定时任务补偿</el-tab-pane>
        </el-tabs>
      </el-col>
      <el-col :span="12">
        <h3><i class="el-icon-document"></i>材料模板信息</h3>
        
      </el-col>
    </el-row>
  </div>
</template>

<script>
import pdf from "vue-pdf";
export default {
  data() {
    return {
      labelPosition: "top",
      activeName: "first",
      url:"http://storage.xuetangx.com/public_assets/xuetangx/PDF/PlayerAPI_v1.0.6.pdf",
      pageNum: 1,
      pageTotalNum: 1,
      pageRotate: 0,
      // 加载进度
      loadedRatio: 0,
      curPageNum: 0,
    };
  },
  name: "Home",
  components: {
    pdf,
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    },
    // 上一页函数，
    prePage() {
      var page = this.pageNum;
      page = page > 1 ? page - 1 : this.pageTotalNum;
      this.pageNum = page;
    },
    // 下一页函数
    nextPage() {
      var page = this.pageNum;
      page = page < this.pageTotalNum ? page + 1 : 1;
      this.pageNum = page;
    },
    // 页面顺时针翻转90度。
    clock() {
      this.pageRotate += 90;
    },
    // 页面逆时针翻转90度。
    counterClock() {
      this.pageRotate -= 90;
    },
    // 页面加载回调函数，其中e为当前页数
    pageLoaded(e) {
      this.curPageNum = e;
    },
    // 其他的一些回调函数。
    pdfError(error) {
      console.error(error);
    },
  },
};
</script>

<style lang="scss" scoped>
.dialogContrast {
  font-size: 12px;
  padding: 20px;
  box-sizing: border-box;
  color: #333;
}
.dialogContrast h3 {
  font-size: 14px;
  font-weight: normal;
  color: #333;
  margin: 0px 0px 10px 0px;
}
.contract iframe {
  width: 100%;
  height: 100vh;
  border: none;
}
.page{
  display: inline-block;
  vertical-align: middle;
  margin: 0px 10px;
}

.el-container {
  flex-direction: column;
}
.panel-body {
  margin-top: 10px;
}
img,
input {
  display: none;
}
.demo {
  min-height: 500px;
}
.demo div.canvas-container {
  border: 1px solid red;
}
canvas {
  border: 1px dashed black;
}
.draw-btn-group {
  // width: 1270px;
  margin-bottom: 10px;
  & > div {
    display: inline-block;
    vertical-align: middle;
    background: #fafafa;
    width: auto;
    text-align: center;
    padding: 5px 10px;
    cursor: pointer;
    &:hover {
      background: #eee;
    }
    i {
      display: block;
      margin: auto;
      background-repeat: no-repeat;
      background-size: 80%;
      background-position: 50% 50%;
      height: 25px;
      width: 25px;
    }
    .icon-1 {
      background-image: url("../assets/icons/draw/1.png");
    }
    .icon-pentagram {
      background-image: url("../assets/icons/draw/pentagram.png");
    }
    .icon-2 {
      background-image: url("../assets/icons/draw/2.png");
    }
    .icon-3 {
      background-image: url("../assets/icons/draw/3.png");
    }
    .icon-4 {
      background-image: url("../assets/icons/draw/4.png");
      background-size: 75%;
    }
    .icon-5 {
      background-image: url("../assets/icons/draw/5.png");
      background-size: 70%;
    }
    .icon-6 {
      background-image: url("../assets/icons/draw/6.png");
    }
    .icon-7 {
      background-image: url("../assets/icons/draw/7.png");
      background-size: 80%;
    }
    .icon-del {
      background-image: url("../assets/icons/draw/del.png");
      background-size: 90%;
    }
    .icon-img {
      background-image: url("../assets/icons/draw/img.png");
      background-size: 80%;
    }
    .icon-back {
      background-image: url("../assets/icons/draw/back.png");
      background-size: 75%;
    }
    .icon-save {
      background-image: url("../assets/icons/draw/save.png");
      background-size: 80%;
    }
    .icon-mouse {
      background-image: url("../assets/icons/draw/mouse.png");
      background-size: 60%;
    }
  }
  .active {
    background: #eee;
  }
}
.maintenancePlanAdd,.marker-right{
  display: inline-block;
  vertical-align: top;
}
.marker-right{
  font-size: 16px;
  color: #333;
  width: 200px;
  margin-top: 60px;
}
.marker-right h3{
  font-weight: bold;
  font-size: 14px;
  margin-bottom: 10px;
}
.marker-right h4{
  font-weight: normal;
  margin: 0;
  font-size: 12px;
  margin-bottom: 5px;
}
.marker-right textarea{
  display: block;
  border: 1px solid #ddd;
  font-size: 14px;
  padding: 5px;
  box-sizing: border-box;
  outline: none;
  width: 100%;
  margin-bottom: 15px;
  border-radius: 3px;
  min-height: 70px!important;
}

</style>
