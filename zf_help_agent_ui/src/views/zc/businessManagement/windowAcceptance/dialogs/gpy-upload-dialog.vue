<template>
  <el-dialog v-dialog-drag :visible.sync="visible" :close-on-click-modal="false" :close-on-press-escape="false" title="材料批量上传" append-to-body custom-class="dialog-gpy" width="1158px" height="634px" :before-close="beforeClose">
    <!-- 样表 -->
    <div class="scan-picture">
      <!-- 高拍仪 -->
      <HiSpeedCamera ref="hiSpeedCamera" class="scan-picture-left" @takePhoto="beforeTakePhoto" @sendScanMessage="getScanMessage" />

      <div class="scan-picture-right">
        <span class="scan-picture--title">扫描结果如下：</span>
        <!-- 高拍仪拍照文件夹列表 -->
        <div class="scan-result" id="scan-result">
          <el-scrollbar style="height: calc(100% - 50px)">
            <div ref="scanResultList" class="scan-result-list">
              <!-- 高拍仪拍照文件夹列表 -->
              <div class="scan-result-list--item" v-for="(item, idx) in folderList" :key="idx" @click="handleFolderClick(item)">
                <!-- 文件夹样式 -->
                <!-- 未能识别 -->

                <div class="list-item--folder" v-if="item.notRecogizedFolder">
                  <div class="list-item--folder-img">
                    <img src="@/assets/image/gpy/notregc.png" width="64" height="61" alt />
                    <span class="list-item--folder-num">{{ item.qlCaseMaterialAttaList.length }}</span>
                  </div>
                  <span class="list-item--folder-title" style="color: #fe7445">{{ item.title }}</span>
                </div>
                <!-- 已识别 -->
                <div v-else class="list-item--folder">
                  <div class="list-item--folder-img" v-if="item.hasUploadPage === 0">
                    <img src="@/assets/image/gpy/common.png" width="64" height="61" alt />
                   <span class="list-item--folder-num">
                        <span>{{ item.hasUploadPage }}</span>
                        <span v-if="item.total > 0" style="color: #85d9fb">/</span>
                        <span v-if="item.total > 0" style="color: #85d9fb">{{ item.total }}</span>
                      </span>
                  </div>
                  <div class="list-item--folder-img list-item--folder-img--hasUpload" v-else>
                    <!-- 堆叠图 -->
                    <ImgLoading v-for="pic in item.foldedPics" :key="pic.attaOid" :pic="pic" />
                    <div class="list-item--folder-img__num">
                      <span class="list-item--folder-num">
                        <span>{{ item.hasUploadPage }}</span>
                        <span v-if="item.total > 0" style="color: #85d9fb">/</span>
                        <span v-if="item.total > 0" style="color: #85d9fb">{{ item.total }}</span>
                      </span>
                    </div>
                  </div>
                  <span class="list-item--folder-title" style="color: #232f51">{{ item.title }}</span>
                </div>
              </div>
            </div>
          </el-scrollbar>
          <!-- 文件夹内部 -->
          <!-- <transition name="show-content">
            <GpyUploadContent ref="gpyUploadContent" v-if="openGpyUploadContent" :folder="targetUploadFolder" @close="handleClose" @delete="handleDelete" @update="handleDraggableUpdate" />
          </transition> -->
          <!-- <transition name="show-content"> -->
            <GpyUploadContent ref="gpyUploadContent" v-if="openGpyUploadContent" :folder="targetUploadFolder" @close="handleClose" @handleContextmenu="handleContextmenu" @resolveSort="handleSort" @cancleSort="hanldeSortCancle" @update="handleDraggableUpdate" />
          <!-- </transition> -->
          <!-- 底部按钮 -->
          <div class="scan-btn">
            <template v-if="!changed">
              <el-button ref="reset" type="info" @click="handleReset">重置</el-button>
              <el-button type="primary" @click="nextStep(true)">下一步</el-button>
            </template>
            <el-button v-else type="primary" @click="nextStep(true)">保存</el-button>
          </div>
        </div>
      </div>
    </div>

    <ZfLoading v-if="zfloading" :text="zftext" />
    <ContextMenu
      ref="contextmenu"
      v-if="showContextMenu"
      v-clickoutside="() => (showContextMenu = false)"
      :contextmenuList="contextmenuList"
      :contextMenuStyle="contextMenuStyle"
      :direction="direction"
      @move-node="handleNodeMove"
      @remove-node="handleNodeRemove"
    />
  </el-dialog>
</template>

<script src="./gpy-upload.js"></script>

<style lang="scss">
  @import './gpy-upload.scss';
</style>
