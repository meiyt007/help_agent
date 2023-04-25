<template>
  <el-dialog
    v-dialog-drag
    :visible.sync="visible"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    title="材料上传"
    append-to-body
    width="1158px"
    :before-close="beforeClose"
  >
    <!-- 样表 -->
    <!-- <ArrowBar :arrowList="arrowList" /> -->
    <div class="scan-picture">
      <!-- 高拍仪 -->
      <HiSpeedCamera ref="hiSpeedCamera" class="scan-picture-left" not-use-gsy @takePhoto="takePhoto" />

      <div class="scan-picture-right--content">
        <span class="scan-picture--title">扫描结果如下：</span>
        <!-- 高拍仪拍照文件夹列表 -->
        <div class="scan-result-dic">
          <div
            ref="scanResultList"
            class="scan-result-list"
            :style="{
              'overflow-y': overflowY,
            }"
          >
            <!-- 高拍仪拍照文件列表 -->
            <div class="scan-result-list--item" v-for="(item, idx) in uploadFilesList" :key="item.id">
              <img
                src="@/assets/image/delete.png"
                alt
                width="22"
                height="22"
                class="content-item-close"
                @click="deleteAttaItem(idx)"
              />
              <ZfImageLoading
                :ref="`zfImage-${idx}`"
                style="width: 100%; height: 100%"
                :preview-src-list="previewList"
                :src="item.src"
              />
              <span class="content-item-preview" @click="handlePreview(`zfImage-${idx}`)">点击预览</span>
            </div>
          </div>
          <!-- 底部按钮 -->
          <div class="scan-btn">
            <el-button type="primary" @click="nextStep">保存</el-button>
          </div>
        </div>
      </div>
    </div>
  </el-dialog>
</template>
  
<script src="./index.js"></script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
