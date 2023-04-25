import { debounce } from '@/utils/utils.js';
// 接口api
import { takePhoto } from '@/api/gaopaiyi.js';
import {
  uploadFile as uploadMaterialsFile,
  getAllComboCaseMaterialList,
  saveComboCaseMaterialAttaList,
} from '@/api/onething/comboManager/comboAccept/materialCategory.js';

import ZfImageLoading from '@/components/ZfImageLoading';
import ImgLoading from '@/components/ImgLoading';
import HiSpeedCamera from '@/components/HiSpeedCamera/hi-speed-camera.vue';
import ZfLoading from '@/components/ZfLoading';
import GpyUploadContent from './gpy-upload-content.vue';

import GPYDrive from "@/api/handwareDrive.js";
import { takePic } from "@/api/handwarev3.js";
import DEVEICETYPE, { GAO_PAI_YI_V1, GAO_PAI_YI_V2, GAO_PAI_YI_V3 } from '@/components/HiSpeedCamera/config.js';

let _pagenum = null, _pagenumlen = 0, _pagenumlist = [];
export default {
  name: 'GpyUploadDialog',
  inheritAttrs: false,
  props: {
    dialogVisible: {
      type: Boolean,
      default: false,
    },
    caseOid: String,
    comboDirectoryName: String,
    comboDirectoryOid: String,
    changed: Boolean, // 是否是从智审点击修改来的
    qlCaseMaterial: {
      type: Object,
      default: () => ({})
    },

    folderCacheQlAttaList: Array,

    // 附件保存数据
    materialAttaList: {
      type: Array,
      default: () => []
    }
  },
  components: { ZfImageLoading, ImgLoading, HiSpeedCamera, ZfLoading, GpyUploadContent },
  data () {
    return {
      // 顶部滚动盒子
      arrowList: [],
      folderList: [],
      openDelay: 500, // popover打开延迟
      loading: false, // loading 动画
      startDistinguish: false, // 开始识别
      scanMaterialClassifyList: [], /** 高扫仪材料分类接口结果排序处理 */
      zfloading: false,
      zftext: '',
      direction: 'left',
      openGpyUploadContent: false,
      targetUploadFolder: {}, // 当前打开的文件夹信息
    }
  },
  computed: {
    visible: {
      get () {
        return this.dialogVisible;
      },
      set () {
        this.$emit('update:dialogVisible', false);
      }
    },
    currentUploadFolderIndex () {
      return this.targetUploadFolder.index ?? -1;
    },

    neddCls ({ openGpyUploadContent }) {
      return openGpyUploadContent ? 'no' : 'yes';
    }
  },
  mounted () {
    this.getAllComboCaseMaterialList();
  },
  methods: {
    /**
     * 获取所有材料列表
     */
    async getAllComboCaseMaterialList () {
      this.loadingBox('正在获取材料列表数据');
      try {
        let { data, code, message } = await getAllComboCaseMaterialList({ caseOid: this.caseOid });
        if (code != 200) {
          this.loading.close();
          return message && this.$message.warning(message);
        }
        this.loading.close();
        data = data.filter(item => item.collectionType == '3');
        this.handleMaterialList(data);
      } catch (error) {
        this.loading.close();
      }
    },

    /** 处理材料列表 */
    handleMaterialList (data) {
      // 文件夹
      this.folderList = data.map(this.handleFolder);

      this.setPreviewList();
      this.setStackingMap();
      this.setUploadedMaterialNums();

      // 默认打开第一个
      this.$nextTick(() => {
        if (this.folderList[0]) {
          this.handleFolderClick(this.folderList[0]);
        }
      })
    },

    /** 初始化设置文件夹 */
    handleFolder (item, idx) {
      return this.setFolder(item, idx);
    },

    /** ---------------------------------------------------------- 高拍仪操作代码 ---------------------------------------------------------- */
    beforeTakePhoto () {
      // 请指定一个材料上传
      if (!this.openGpyUploadContent) {
        return this.$message.warning('请指定一个材料上传!');
      }
      if (DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V1) {
        this.takePhotoV1();
      }

      if (DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V2) {
        this.takePhotoV2();
      }
      if (DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V3) {
        this.takePhotoV3();
      }
    },
    takePhotoV1: debounce(async function () {
      if (!this.$refs.hiSpeedCamera.isGpyOpen) return this.$message.warning('请先开启高拍仪设备!');
      if (this.startDistinguish) return this.$message.warning('正在识别中, 请稍后重试!');

      try {
        // 开始识别
        this.startDistinguish = true;
        this.setDotsFolder();

        const { data, returnCode, returnMsg } = await takePhoto({
          savepath: '/tmp',
          quality: 80
        });
        if (returnCode != 0) {
          this.$message.warning(returnMsg || '拍摄失败');
          this.startDistinguish = false;
          this.clearDotsFolder();
          return;
        }

        this.handleUploadImg(res.data);

      } catch (error) {
        this.startDistinguish = false;
        this.clearDotsFolder();
      }

      this.resetData();

    }, 500, true),
    takePhotoV2: debounce(async function () {
      if (!this.$refs.hiSpeedCamera.isGpyOpen) return this.$message.warning('请先开启高拍仪设备!');
      if (this.startDistinguish) return this.$message.warning('正在识别中, 请稍后重试!');

      try {
        // 开始识别
        this.startDistinguish = true;
        this.setDotsFolder();

        if (GPYDrive.isOpen) {
          let res = null;

          if (GPYDrive.singleOpenType === "face") {
            res = await GPYDrive.getFacePicture();
          }

          if (GPYDrive.singleOpenType === "high") {
            res = await GPYDrive.getHighPicture();
          }

          if (res && res.code === 200) {
            this.handleUploadImg(res.data);
          } else {
            this.$message.warning(returnMsg || '拍摄失败');
            this.startDistinguish = false;
            this.clearDotsFolder();
            return;
          }
        }
      } catch (error) {
        this.startDistinguish = false;
        this.clearDotsFolder();
      }

      this.resetData();

    }, 500, true),

    takePhotoV3: debounce(async function () {
      if (!this.$refs.hiSpeedCamera.isGpyOpen) return this.$message.warning('请先开启高拍仪设备!');
      if (this.startDistinguish) return this.$message.warning('正在识别中, 请稍后重试!');

      try {
        // 开始识别
        this.startDistinguish = true;
        this.setDotsFolder();

        const res = await takePic();
        if (res.StateCode != 0) {
          this.$message.warning(returnMsg || '拍摄失败');
          this.startDistinguish = false;
          this.clearDotsFolder();
          return;
        }

        this.handleUploadImg(res.data);

      } catch (error) {
        this.startDistinguish = false;
        this.clearDotsFolder();
      }

      this.resetData();

    }, 500, true),

    handleUploadImg (img) {
      return this.$getResponse(this.uploadGPYImg(img), (error, res) => {
        this.startDistinguish = false;
        this.clearDotsFolder();
        if (error || res.code !== 200) return this.$message.warning('文件上传失败, 请重新拍照上传');
        this.handleMaterialClassify(res?.data?.[0] ?? {});
      })
    },

    resetData () {
      // 堆叠图
      this.setStackingMap();
      // 设置预览
      this.setPreviewList();
      // 处理上传文件数量
      this.setUploadedMaterialNums();
    },


    // 上传高拍仪图片
    uploadGPYImg (img) {
      let file = this.base64ToFile(img, Date.now() + ".jpg");
      let formData = new FormData();
      formData.append("files", file);
      return uploadMaterialsFile(formData);
    },

    // 识别失败的 和 识别成功的但是未匹配文件夹的
    handleRecogizedFail (classifyRec) {
      return {
        materialAttaOid: '', // 新增的不取后台生成  修改的取之前带过来的值
        caseMaterialOid: this.targetUploadFolder.caseMaterialOid || '',
        attaOid: classifyRec.attaOid || '',
        materialCatalogOid: '',
        src: classifyRec.src || classifyRec.fastdfsNginxUrl || '',
        beforeAttaOid: classifyRec.beforeAttaOid, // 用于排序
        materailName: classifyRec.name,
      }
    },

    /** 高扫仪 */
    async getScanMessage (data) {
      const { pagenum, message } = data;
      // console.log('%c [data]:', 'color:red;font-weight:700;', data);

      if (pagenum != '0') {
        _pagenumlist.push(pagenum);
        this.scanMaterialClassifyList.push(async () => await this.handleScanMessage(message, pagenum));
      }

      if (pagenum == '0') {
        if (_pagenumlist.length > 0) {
          _pagenumlen = [...new Set([..._pagenumlist])].length;
        }

        for (const fn of this.scanMaterialClassifyList) {
          await fn();
        }

        _pagenum = null;
        _pagenumlen = 0;
        _pagenumlist = [];

        // 清除动画
        this.loading?.close?.();

        this.scanMaterialClassifyList = [];
      }
    },


    /** 高扫仪 */
    async handleScanMessage (img, pagenum) {
      try {
        if (pagenum != _pagenum) {
          let str = _pagenumlen == 1 ? '即将完成上传' : `还剩${--_pagenumlen}张文件未上传`;
          if (this.loading.visible) {
            this.loading.text = (`正在上传第${pagenum}张文件, ${str}, 请耐心等候...`);
          } else {
            this.loadingBox(`正在上传第${pagenum}张文件, ${str}, 请耐心等候...`);
          }
          _pagenum = pagenum;
        }

        await this.handleUploadImg(img);

        this.resetData()

      } catch (error) {
        console.log('%c [error]:', 'color:red;font-weight:700;', error);
      }
    },

    /** 处理材料分类 */
    handleMaterialClassify (ret) {
      const { attaOid, name, fastdfsNginxUrl, catalogOid } = ret;
      // 判断当前是否有打开了文件夹
      if (this.targetUploadFolder.caseMaterialOid) {
        this.targetUploadFolder.comboCaseMaterialAttaList.push({
          ...this.handleRecogizedFail({
            attaOid,
            name,
            src: fastdfsNginxUrl,
            catalogOid
          })
        });
      }
    },
    /** ---------------------------------------------------------- 公共代码 ---------------------------------------------------------- */

    async beforeClose (done) {
      this.$refs?.gpyUploadContent?.handleClose();

      if (this.$refs.hiSpeedCamera.loadingGpy) return this.$message.warning('正在启动高拍仪设备, 请稍后关闭');
      // 保存材料
      try {
        await this.nextStep(false);
        done();
      } catch (error) {
        done();
      }
    },

    // 打开loading动画
    loadingBox (text = '正在拍摄中...') {
      this.loading = this.$loading({
        lock: true,
        text: text,
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
    },

    // 图片转文件对象
    base64ToFile (urlData, fileName) {
      let bytes = atob(urlData); // 解码base64
      let n = bytes.length;
      let ia = new Uint8Array(n);
      while (n--) {
        ia[n] = bytes.charCodeAt(n);
      }
      return new File([ia], fileName, {
        type: 'image/jpeg'
      });
    },

    // 重置
    handleReset () {
      this.$refs.reset.$el.blur();
      this.$confirm('此操作将删除所有已经上传过的文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        closeOnClickModal: false,
        closeOnPressEscape: false,
        type: 'warning'
      }).then(() => {
        if (this.startDistinguish) return this.$message.warning('正在识别中, 请稍后重试!');
        this.clearDotsFolder();
        // this.clearNotRecognizedFolder();
        this.folderList
          .forEach(item => {
            item.previewList = [];
            item.foldedPics = [];
            if (item.isRefinedMaterialFolder) {
              item.comboCaseMaterialAttaList = item.comboCaseMaterialAttaList
                .filter(child => child.isRefinedMaterial)
                .map(child => ((child.isRefinedMaterial && child.src && child.origin) ? { ...child.origin, origin: child.origin } : { ...child }));
            } else {
              item.comboCaseMaterialAttaList = [];
            }
          });
        this.resetData();
      }).catch(() => { });
    },

    // 下一步
    async nextStep (isNext = true) {
      if (this.startDistinguish) return this.$message.warning('正在识别中, 请稍后重试!');
      this.$refs?.gpyUploadContent?.handleClose();

      this.clearDotsFolder();

      const params = [...this.getSaveFilesParams(isNext), ...this.materialAttaList];

      if (params.some(item => item.comboCaseMaterialAttaList.length == 0) && isNext) return this.$message.warning('暂无保存材料信息');

      let loading;

      loading = this.$loading({
        lock: true,
        text: '正在保存材料附件中',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });

      try {
        const { code, message, data } = await saveComboCaseMaterialAttaList(params);
        if (code != 200 || (data.success && !data.success)) {
          loading && loading.close();
          this.zfloading = false;
          return this.$message.warning(message || data.message || '保存材料附件失败');
        }

        loading && loading.close();

        if (isNext) {
          this.$emit('update:dialogVisible', false);
          this.$emit('nextStep', 4);
        } else {
          this.$emit('update:dialogVisible', false);
        }

      } catch (error) {
        loading && loading.close();
        this.zfloading = false;
      }
    },

    /**
     * 获取保存附件的传参
     * @param {boolean} isNext 是否是点击下一步
     */
    getSaveFilesParams (isNext = true) {
      return this.folderList
        .map(item => {
          return {
            id: item.id,
            caseMaterialOid: item.caseMaterialOid,
            materialOid: item.materialOid,
            materailName: item.materailName,
            caseOid: item.caseOid,
            materialSampleAddr: item.materialSampleAddr,
            materialSampleAddrYl: item.materialSampleAddrYl,
            collectionType: item.collectionType,
            comboCaseMaterialAttaList:
              isNext ?
                item.comboCaseMaterialAttaList
                  .map(child => ({ ...child, id: child.isCache ? child.id : '', caseMaterialOid: item.caseMaterialOid, }))
                :
                item.comboCaseMaterialAttaList
                  .filter(child => child.src)
                  .map(child => ({ ...child, id: child.isCache ? child.id : '', caseMaterialOid: item.caseMaterialOid, })),
          }
        })
    },

    /** ---------------------------------------------------------- 文件夹相关操作 ---------------------------------------------------------- */
    // 设置文件夹信息
    setFolder (item, idx) {
      // 处理细化材料
      // let list = [];
      // if (item.refinedMaterialList.length > 0) {
      //   list =
      //     item.refinedMaterialList
      //       .map(i => {
      //         return {
      //           id: i.id,
      //           oid: i.oid,
      //           materialOid: i.materialOid,
      //           refinedMaterialName: i.refinedMaterialName,
      //           needStatus: i.needStatus, // 1 为必传
      //           materialSampleAddr: i.materialSampleAddr,
      //           materialSampleOid: i.materialSampleOid,
      //           serialNumber: i.serialNumber,
      //           materialCatalogOid: i.materialCatalogOid,
      //           comboDirectoryOid: i.comboDirectoryOid,
      //           licenceOid: i.licenceOid,
      //           licenceName: i.licenceName,
      //           isRefinedMaterial: true, // 是否是精细化材料
      //           refinedMaterialNum: i.serialNumber, // 精细化材料页码引用
      //           resultStatus: i.resultStatus, // 是否通过智审 0 通过 1 不通过  6 附件不符合审核模板
      //         }
      //       })
      //       .map(i => ({ ...i, origin: i }))
      // }

      // if (item.comboCaseMaterialAttaList.length > 0) {
      //   item.comboCaseMaterialAttaList.forEach(i => {
      //     if (i.refinedMaterialOid) {
      //       let idx = list.findIndex(k => k.oid == i.refinedMaterialOid);
      //       if (idx != -1) {
      //         list[idx] = {
      //           ...list[idx],
      //           ...i,
      //           isCache: true,
      //           origin: { ...list[idx], resultStatus: undefined },
      //         }
      //       }
      //     }
      //   })
      // }

      // // 合并不是精细化材料的数据
      // list = [...list, ...item.comboCaseMaterialAttaList.filter(i => !i.refinedMaterialOid)];

      // list = list.map(child => ({ ...this.setQlCaseMaterialAttaListInfo(child, item) }));

      return {
        id: item.id,
        caseMaterialOid: item.caseMaterialOid,
        materialOid: item.materialOid,
        materialName: item.materialName,
        caseOid: item.caseOid,
        materialSampleAddr: item.materialSampleAddr,
        materialSampleAddrYl: item.materialSampleAddrYl,
        comboCaseMaterialAttaList: item.comboCaseMaterialAttaList,
        title: item.materialName,
        showPopover: false,
        foldedPics: [], // 堆叠图
        previewList: [], // 预览
        // total: item.refinedMaterialList.length,
        total: 0,
        hasUploadPage: item.comboCaseMaterialAttaList.filter(i => i.src).length,
        isIntelligentFail: item.comboCaseMaterialAttaList.some(i => i.resultStatus == '1' || i.resultStatus == '6'), // 智审不通过
        // mustFlag: item.mustFlag, // 事项材料 是否为必传
        // isRefinedMaterialFolder: item.refinedMaterialList.length > 0, // 是否含有精细化材料
        // isShowTooltip: isShowTooltip(item.materialName, 100),
        index: idx + 1,
        collectionType: item.collectionType || '3'
      }
    },

    /**
     * 设置上传文件信息
     * @param {object} item 分类成功的信息
     * @param {object} folder 文件夹的信息
     */
    // setQlCaseMaterialAttaListInfo (item = {}, folder = {}) {
    //   return {
    //     ...item,
    //     id: item.id || Date.now(),
    //     materialAttaOid: item.materialAttaOid, // 新增的不取后台生成  修改的取之前带过来的值
    //     caseMaterialOid: item.caseMaterialOid || folder.caseMaterialOid || '', // 1.分类成功带过来的 2.分类失败 拖拽从文件夹带过来
    //     attaOid: item.attaOid || folder.attaOid || '',
    //     materialCatalogOid: item.materialCatalogOid || '', // 分类成功带过来的 拖拽不用赋值
    //     src: item.src || item.fastdfsNginxUrl || folder.fastdfsNginxUrl || folder.src || '',
    //   }
    // },

    // 设置预览图片列表
    setPreviewList () {
      this.folderList
        .forEach(item => {
          item.previewList = item.comboCaseMaterialAttaList.filter(item => item.src).map(child => child.src);
        })
    },

    // 拼接堆叠图
    setStackingMap () {
      const pos = [
        { left: '10px', top: '-10px', zIndex: 5 },
        { left: '8px', top: '-8px', zIndex: 4 },
        { left: '6px', top: '-6px', zIndex: 3 },
        { left: '4px', top: '-4px', zIndex: 2 },
        { left: '2px', top: '-2px', zIndex: 1 },
      ];
      this.folderList.forEach(item => {
        item.foldedPics = item.comboCaseMaterialAttaList
          .filter(i => i.src)
          .slice(0, 5)
          .map((child, idx) => {
            return {
              ...child,
              src: child.src,
              left: pos[idx].left,
              top: pos[idx].top,
              width: '52px',
              height: '74px',
              zIndex: pos[idx].zIndex
            }
          });
      })
    },

    // 设置dots文件夹 识别动画
    setDotsFolder () {
      this.$refs.hiSpeedCamera.loadingText = '正在识别';
      this.$refs.hiSpeedCamera.loadingMaterials = true;
    },

    // 清除dots动画
    clearDotsFolder () {
      this.$refs.hiSpeedCamera.loadingMaterials = false;
      this.$refs.hiSpeedCamera.loadingText = '正在启动设备';
    },

    // 预览
    handlePreview (ref) {
      const refs = this.$refs[`${ref}`][0];
      if (refs) {
        if (refs.loading) return this.$message.warning('正在加载中,请稍后');
        refs.handlePreview();
      }
    },

    /**
     * 设置已经上传的材料页数
     */
    setUploadedMaterialNums () {
      this.folderList
        .forEach(item => {
          try {
            item.hasUploadPage = item.comboCaseMaterialAttaList.filter(i => i.src).length;
          } catch (error) {
            console.log(error, item);
          }
        });
    },

    handleFolderClick (folder) {
      this.folderList.forEach(item => item.showPopover = false);
      folder.showPopover = true;
      // 指定材料
      this.targetUploadFolder = folder;
      // 切换模式
      this.$refs.hiSpeedCamera.setModeToggle();
      this.openGpyUploadContent = true;
    },

    handleClose () {
      // 切换模式
      this.$refs.hiSpeedCamera.setModeToggle();
      this.openGpyUploadContent = false;
      this.targetUploadFolder.showPopover = false;
      this.targetUploadFolder = {};
      this.resetData();
    },

    handleDraggableUpdate (value) {
      this.targetUploadFolder.comboCaseMaterialAttaList = value;
    },

    handleMaterailAnimate () {
      const img = document.querySelectorAll('.list-item--folder-img')[0];
      if (img) {
        img.classList.add('list-item--folder-img-animate');
        setTimeout(() => {
          img.classList.remove('list-item--folder-img-animate');
        }, 500);
      }
    },

    handleDelete (attaOid) {
      this.targetUploadFolder.comboCaseMaterialAttaList = this.targetUploadFolder.comboCaseMaterialAttaList.filter(item => item.attaOid !== attaOid);
    }

  }
}
