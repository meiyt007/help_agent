<template>
  <div
    class="gpy-upload-content"
    :class="{
      'gpy-upload-content--full-screen': isFullScreen,
      'gpy-upload-content--full-screen-suspension': isFullScreen && isShow,
      'gpy-upload-content--full-screen-suspensioned': isFullScreen && isShowSuspension
    }"
  >
    <!-- 头 -->
    <div class="gpy-upload-content--header">
      <!-- 关闭按钮 -->
      <div v-animate class="gpy-upload-content--close" @click="handleClose">
        <i class="el-icon-close"></i>
      </div>
      <div class="header-num">
        <span v-if="!uploadNum.isCommon">
          <span>{{ uploadNum.uploadNum }}</span>
          <span style="color: #85d9fb">/</span>
          <span style="color: #85d9fb">{{ uploadNum.total }}</span>
        </span>
        <span v-else>
          <span>{{ uploadNum.uploadNum }}</span>
        </span>
      </div>
      <div class="header-title">{{ folder.title }}</div>
      <div class="header-sort" :class="{ 'header-sort-not-allowed': notAllowedSort }" @click="handleSort" v-animate>
        <img :src="sortSrc" width="64" height="61" alt :title="sortTitle" />
      </div>
      <div v-animate class="header-full-screen" @click="handleFullScreen">
        <img v-if="!isFullScreen" src="@/assets/image/gpy/full-screen.png" alt title="全屏" />
        <img v-else src="@/assets/image/gpy/cancel-fullscreen.png" alt title="取消全屏" />
      </div>
    </div>
    <!-- body -->
    <el-scrollbar class="gpy-upload-content--body">
      <draggable
        :value="draggableList"
        ghostClass="gpy-upload-content--item-ghost"
        draggable=".gpy-upload-content--item"
        @change="handleDraggableChange"
        :scroll="true"
      >
        <transition-group name="upload" tag="div" class="gpy-upload-content--list">
          <div
            class="gpy-upload-content--item"
            v-for="(item, index) in uploadList"
            :key="item.attaOid"
            @contextmenu="
              handleContextmenu($event, {
                parent: folder,
                parentIdx: folder.index,
                child: item,
                childIdx: index,
              })
            "
          >
            <div class="gpy-upload-content--item-img">
              <!-- 已匹配标志 -->
              <img
                v-if="item.src && item.isRefinedMaterial"
                class="gpy-upload-content--item-img-matched"
                src="@/assets/image/gpy/matched.png"
                width="26"
                height="26"
                alt
              />
              <ZfImageLoading
                style="width: 100%; height: 100%"
                :preview-src-list="folder.previewList"
                :src="item.src"
                :loadingStyle="{ left: '32%', top: '35%' }"
              />
            </div>
            <template v-if="folder.isRefinedMaterialFolder">
              <span v-if="item.src && item.isRefinedMaterial">第 {{ item.refinedMaterialNum }} 页</span>
              <span v-else>待匹配</span>
            </template>
            <template v-else>
              <span>第 {{ item.index }} 页</span>
            </template>
          </div>
        </transition-group>
      </draggable>
    </el-scrollbar>
    <!-- 左侧悬浮 待匹配材料 -->
    <div
      v-if="isShowSuspension"
      class="gpy-upload-content--suspension"
      :class="[isShow ? 'gpy-upload-content--suspension-right' : 'gpy-upload-content--suspension-left',]"
    >
      <div class="suspension" @click="handleSuspensionShow">
        <img src="@/assets/image/gpy/match.png" width="12" height="13" alt />
        <div class="suspension-num">
          <span class="suspension-num--text">待匹配材料</span>
          <span class="suspension-num--num">{{ refinedMaterialList.length }}</span>
        </div>
        <span class="suspension-arrow">
          <img v-if="!isShow" src="@/assets/image/gpy/go-left.png" width="6" height="9" alt />
          <img v-else src="@/assets/image/gpy/go-right.png" width="6" height="9" alt />
        </span>
      </div>
      <div class="suspension-content" :class="[isShow ? 'suspension-content--right' : 'suspension-content--left',]">
        <div class="suspension-content--title">辅助审查材料目录匹配</div>
        <div class="suspension-content--process">
          <el-progress :percentage="percentage" :format="format" />
        </div>
        <el-scrollbar ref="suspensionScrollbar" native class="suspension-content--steps">
          <!-- 左侧虚线 -->
          <div
            class="suspension-content--dot-line"
            :style="{ height: (refinedMaterialList.length - 1) * 50 + 'px', maxHeight: isFullScreen && isShowSuspension ? 'none' : '245px' }"
          ></div>
          <!-- 列表数据 -->
          <div class="suspension-content--step" v-for="(item, index) in refinedMaterialList" :key="index">
            <div class="suspension-content--step-dot" :class="{ matched: item.src }"></div>
            <el-image
              fit="contain"
              style="width: 13; height: 13px"
              :src="require('@/assets/image/gpy/zhanwei.png')"
              :preview-src-list="handlePreviewList(index)"
            />
            <div class="suspension-content--step-title" :class="{ matched: item.src }">{{ item.refinedMaterialName }}</div>
            <div class="suspension-content--step-match" :class="{ matched: item.src }">{{ item.src ? '已匹配' : '待匹配' }}</div>
          </div>
        </el-scrollbar>
      </div>
    </div>
  </div>
</template>

<script>
import ZfImageLoading from '@/components/ZfImageLoading';
import { debounce } from '@/utils/utils.js';
import { deepClone } from "@/utils/index";
import { getFileSortingInformation } from '@/api/materialCategory.js';
import draggable from 'vuedraggable'
export default {
  name: 'GpyUploadContent',
  props: {
    folder: Object
  },
  components: { ZfImageLoading, draggable },
  data () {
    return {
      isShow: false,
      sortListCache: [], // 缓存排序前的数据
      isSort: true,
      isFullScreen: false,
    }
  },
  watch: {
    'uploadList.length': {
      handler () {
        // 重置排序
        this.reSort();
      }
    }
  },
  computed: {
    // 自动排序还是退回排序
    sortSrc () {
      return this.isSort ? require('@/assets/image/gpy/sort.png') : require('@/assets/image/gpy/sort-back.png')
    },

    sortTitle () {
      return this.isSort ? '自动排序' : '取消自动排序';
    },

    uploadNum ({ folder: { notRecogizedFolder, comboCaseMaterialAttaList, hasUploadPage, total, isRefinedMaterialFolder } }) {
      if (notRecogizedFolder) {
        return {
          uploadNum: comboCaseMaterialAttaList.length,
          isCommon: true,
        }
      } else {
        return {
          uploadNum: hasUploadPage,
          total: total,
          isCommon: !isRefinedMaterialFolder,
        }
      }
    },
    /** 已上传文件列表 */
    uploadList ({ folder: { comboCaseMaterialAttaList } }) {
      return comboCaseMaterialAttaList.filter(item => item.src).map((e, i) => ({ ...e, index: i + 1 }));
    },
    /** 精细化材料列表 */
    refinedMaterialList ({ folder: { comboCaseMaterialAttaList, isRefinedMaterialFolder } }) {
      return isRefinedMaterialFolder ?
        comboCaseMaterialAttaList.filter(item => item.isRefinedMaterial).sort((a, b) => a.refinedMaterialNum - b.refinedMaterialNum) : [];
    },

    draggableList ({ uploadList, refinedMaterialList }) {
      return [...uploadList, ...refinedMaterialList].reduce((prev, cur) => {
        // 判断是否重复attaOid
        const dupAttaOid = prev.find(item => item.attaOid === cur.attaOid && cur.attaOid);
        if (!dupAttaOid) {
          prev.push(cur);
        }
        return prev;
      }, []);
    },

    /** 是否显示悬浮框 */
    isShowSuspension ({ refinedMaterialList, folder: { notRecogizedFolder } }) {
      return refinedMaterialList.length > 0 && !notRecogizedFolder;
    },
    /** 是否允许自动排序 */
    notAllowedSort ({ folder: { notRecogizedFolder, isRefinedMaterialFolder } }) {
      return notRecogizedFolder || isRefinedMaterialFolder;
    },

    percentage ({ folder: { total, comboCaseMaterialAttaList } }) {
      const uploadNum = comboCaseMaterialAttaList.filter(i => i.src && i.isRefinedMaterial).length;
      return uploadNum / total * 100;
    },
  },
  mounted () {
    this.appendBody();

    this.onresize = () => {
      // 获取父元素相对位置
      if (!this.isFullScreen) {
        const { left, top } = document.querySelector('#scan-result').getBoundingClientRect();
        this.$el.style.left = left + 12 + 'px';
        this.$el.style.top = top + 8 + 'px';
      }
    }
    window.addEventListener('resize', this.onresize);

    this.onresize();

    // 快捷键esc键退出全屏
    this.keyBoardEvent = e => {
      if (e.keyCode === 27 && this.isFullScreen) {
        this.handleFullScreen();
      }
    };

    window.addEventListener("keyup", this.keyBoardEvent);
  },
  beforeDestroy () {
    window.removeEventListener('resize', this.onresize);
    window.removeEventListener("keyup", this.keyBoardEvent);
  },
  methods: {
    appendBody () {
      try {
        // 获取el-dialog的z-index
        const dialog = document.querySelector('.el-dialog.dialog-gpy');
        this.$el.style.zIndex = parseInt(dialog.parentNode.style['z-index']);
        // 把元素放到body中
        document.body.appendChild(this.$el);
      } catch (error) {
        console.log(error);
      }
    },

    handleClose () {
      // Fix: 这里清除el的动画属性 否则会有两次动画 分析原因可能是和transition组件冲突了
      this.$el.style.transition = 'none';
      this.$emit('close');
    },

    /** 防抖 排序 */
    handleSort: debounce(function () {
      // 未识别文件夹和非精细化材料的文件夹 不做自动分类功能
      if (this.notAllowedSort || this.folder.comboCaseMaterialAttaList.length === 1) return;
      if (this.isSort) {
        // 获取列表idlist
        const idList = this.uploadList.map(item => item.beforeAttaOid).filter(i => i).join(',');
        if (idList) {
          this.$getResponse(getFileSortingInformation({ idList }), (error, { code, data }) => {
            if (code !== 200 || !data || error) return;
            this.isSort = false;
            this.sortListCache = deepClone(this.draggableList);
            this.$emit('resolveSort', data);
          })
        }
      } else {
        // 取消排序
        this.$emit('cancleSort', this.sortListCache);
        this.reSort();
      }
    }, 500, true),

    /** 排序后可以立即回调 指定拍照模式下 重新拍照/删除/拖拽移动后 可以再次排序 */
    reSort () {
      this.sortListCache = [];
      this.isSort = true;
    },

    /** 全屏 */
    handleFullScreen () {
      this.isFullScreen = !this.isFullScreen;
      if (this.isFullScreen) {
        this.$el.style.left = '20px';
        this.$el.style.top = '30px';
        if (this.isShowSuspension) {
          this.isShow = true;
          this.$el.style.left = '300px';
        }
      } else {
        this.onresize();
      }
    },

    /** 右击事件 */
    handleContextmenu (e, item) {
      this.$emit('handleContextmenu', e, item);
    },

    handleSuspensionShow () {
      this.isShow = !this.isShow;
    },

    format () {
      const { folder: { comboCaseMaterialAttaList, total } } = this;
      const uploadNum = comboCaseMaterialAttaList.filter(i => i.src && i.isRefinedMaterial).length;
      return `${uploadNum} / ${total}`;
    },

    handlePreviewList (index) {
      const { folder: { comboCaseMaterialAttaList } } = this;
      const list = comboCaseMaterialAttaList.map(item => item.materialSampleAddr);
      const [before, after] = [list.slice(0, index), list.slice(index)];
      return [...after, ...before];
    },

    handleDraggableChange (e) {
      console.log('%c [handleDraggableChange]:', 'color:red;font-weight:700;', e);
      const { moved: { newIndex, oldIndex, element } } = e;
      const { draggableList } = this;
      // 向后拖拽
      if (newIndex > oldIndex) {
        const [prev, cur, last] = [
          draggableList.slice(0, oldIndex),
          draggableList.slice(oldIndex, newIndex + 1),
          draggableList.slice(newIndex + 1)
        ];
        const list = [...prev, ...[...cur.slice(1), element], ...last];
        this.$emit('update', list);
      }

      // 向前拖拽
      if (oldIndex > newIndex) {
        const [prev, cur, last] = [
          draggableList.slice(0, newIndex),
          draggableList.slice(newIndex, oldIndex + 1),
          draggableList.slice(oldIndex + 1)
        ];
        const list = [...prev, ...[element, ...cur.slice(0, -1)], ...last];
        this.$emit('update', list);
      }

      // 重置排序
      this.reSort();
    },

  }
}
</script>

<style scoped lang="scss">
@import "@/views/zc/businessManagement/windowAcceptance/dialogs/gpy-upload-content.scss";
</style>
