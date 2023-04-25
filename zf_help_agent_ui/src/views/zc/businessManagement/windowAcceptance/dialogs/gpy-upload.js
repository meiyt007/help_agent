import { isShowTooltip, debounce } from "@/utils/utils.js";
// 接口api
import { takePhoto } from "@/api/gaopaiyi.js";
import {
  uploadMaterialsFile,
  intelligentPretrialmaterialPrePrialAllNew,
  getAllQlCaseMaterialList,
  saveOrUpdateCaseMaterialAttaList,
  intelligentPretrialmaterialPrePrialByCaseMaterialOid,
  getEditImageAndClassifilerResult,
  getClassifilerMateial
} from "@/api/materialCategory.js";

import ZfImageLoading from "@/components/ZfImageLoading";
import ImgLoading from "@/components/ImgLoading";
import HiSpeedCamera from "@/components/HiSpeedCamera/hi-speed-camera.vue";
import ZfLoading from "@/components/ZfLoading";
import GpyUploadContent from "./gpy-upload-content.vue";
import ContextMenu from "./contentmenu/contextmenu-panel.vue";
import Clickoutside from "./clickoutside";
import GPYDrive from "@/api/handwareDrive.js";
import { takePic } from "@/api/handwarev3.js";
import { mapGetters } from "vuex";
import DEVEICETYPE, {
  GAO_PAI_YI_V1,
  GAO_PAI_YI_V2,
  GAO_PAI_YI_V3,
  GAO_PAI_YI_V4
} from "@/components/HiSpeedCamera/config.js";
function getRandomIntInclusive(min, max) {
  min = Math.ceil(min);
  max = Math.floor(max);
  return Math.floor(Math.random() * (max - min + 1)) + min; //含最大值，含最小值
}
let _pagenum = null,
  _pagenumlen = 0,
  _pagenumlist = [];
export default {
  name: "GpyUploadDialog",
  inheritAttrs: false,
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    },
    caseOid: String,
    serviceName: String,
    serviceOid: String,
    changed: Boolean, // 是否是从智审点击修改来的
    qlCaseMaterial: {
      type: Object,
      default: () => ({})
    },

    // 附件保存数据
    materialAttaList: {
      type: Array,
      default: () => []
    }
  },
  directives: { Clickoutside },
  components: {
    ZfImageLoading,
    ImgLoading,
    HiSpeedCamera,
    ZfLoading,
    ContextMenu,
    GpyUploadContent
  },
  data() {
    return {
      // 顶部滚动盒子
      arrowList: [],
      folderList: [],
      openDelay: 500, // popover打开延迟
      loading: false, // loading 动画
      startDistinguish: false, // 开始识别
      scanMaterialClassifyList: [] /** 高扫仪材料分类接口结果排序处理 */,
      zfloading: false,
      zftext: "",
      direction: "left",
      showContextMenu: false,
      openGpyUploadContent: false,
      contextmenuList: [],
      targetUploadFolder: {}, // 当前打开的文件夹信息
      ErdsPhotoCounts: 0,
      message: "0000",
      contextMenuStyle: {},
      currentContextMenuMaterial: {} // 当前右击材料
    };
  },
  computed: {
    visible: {
      get() {
        return this.dialogVisible;
      },
      set() {
        this.$emit("update:dialogVisible", false);
      }
    },
    currentUploadFolderIndex() {
      return this.targetUploadFolder.index ?? -1;
    },

    neddCls({ openGpyUploadContent }) {
      return openGpyUploadContent ? "no" : "yes";
    },
    ...mapGetters(["clflFlag"])
  },
  mounted() {
    //hi-快拍组件传图片过来，本组件进行显示。
    this.$bus.$on("paizhao", img => {
      // console.log(`我收到了${img}`)
      this.ErdsPhotoCounts++;
      // this.handleUploadImg(img)
      // this.beforeTakePhoto(img)
    });
    // this.$bus.$on('minusPhoto',()=>{
    //   this.ErdsPhotoCounts--
    // })

    this.getClassifilerMateial();
    this.getAllQlCaseMaterialList();
  },
  created() {
    this.$bus.$on("message", message => {
      if (message != "") {
        console.log("message-mounted");
      }
      this.message = message;
    });
  },

  methods: {
    async getClassifilerMateial() {
      const { code, data } = await getClassifilerMateial({
        caseOid: this.caseOid,
        serviceOid: this.serviceOid,
        serviceName: this.serviceName
      });
      if (code !== 200) return;
      this.fileList = data.fileList;
      this.fileListMap = data.fileListMap;
    },
    /**
     * 获取所有材料列表
     */
    async getAllQlCaseMaterialList() {
      this.loadingBox("正在获取材料列表数据");
      try {
        let { data, code, message } = await getAllQlCaseMaterialList({
          caseOid: this.caseOid
        });
        if (code != 200) {
          this.loading.close();
          return message && this.$message.warning(message);
        }
        this.loading.close();
        data = data.filter(item => item.collectionType == "3");
        console.log(data);
        this.handleMaterialList(data);
      } catch (error) {
        this.loading.close();
      }
    },

    /** 处理材料列表 */
    handleMaterialList(data) {
      // 文件夹
      this.folderList = data.map(this.handleFolder);
      // alert(JSON.stringify(folderList))
      console.log("rrrrrrr", this.folderList);
      this.setNotRecognizedFolder();
      this.setContextmenuList();
      this.setPreviewList();
      this.setStackingMap();
      this.setUploadedMaterialNums();
      // 默认打开第一个
      // this.$nextTick(() => {
      //   if (this.folderList[0]) {
      //     this.handleFolderClick(this.folderList[0]);
      //   }
      // })
    },

    /** 初始化设置文件夹 */
    handleFolder(item, idx) {
      return this.setFolder(item, idx);
    },

    setContextmenuList() {
      const child = this.folderList
        .filter(item => !item.notRecogizedFolder)
        .map((item, idx) => {
          const children = item.qlCaseMaterialAttaList
            .filter(item => item.isRefinedMaterial)
            .sort((a, b) => a.refinedMaterialNum - b.refinedMaterialNum)
            .map((i, index) => {
              return {
                children: [],
                level: 3,
                label: `第${i.refinedMaterialNum}页(${i.refinedMaterialName})`,
                id: i.id,
                hasChildren: false,
                isExtend: false,
                folderIdx: idx + 1,
                fileIdx: index,
                hasUpload: !!i.src,
                showTooltip: !isShowTooltip(
                  `第${i.refinedMaterialNum}页(${i.refinedMaterialName})`,
                  140
                )
              };
            });

          if (children.length > 0) {
            children.push({
              children: [],
              level: 3,
              label: "插入文件到目录最后",
              id: (Date.now() + getRandomIntInclusive(1, 1000)).toString(),
              hasChildren: false,
              isExtend: false,
              isInsert: true,
              folderIdx: idx + 1
            });
          }

          return {
            children: children || [],
            level: 2,
            label: item.title,
            id: item.id,
            hasChildren: children.length > 0,
            isExtend: false,
            folderIdx: idx + 1,
            showTooltip: !isShowTooltip(item.title, 140),
            isInsert: !item.qlCaseMaterialAttaList.some(
              item => item.isRefinedMaterial
            )
          };
        });
      // 树形结构数据
      this.contextmenuList = [
        {
          children: child,
          label: "移动到",
          id: "$$0",
          level: 1,
          hasChildren: child.length > 0,
          isExtend: false
        },
        {
          children: [],
          label: "删除",
          id: "$$2",
          level: 1,
          hasChildren: false,
          isExtend: false,
          isDelete: true
        }
      ];
    },

    /** ---------------------------------------------------------- 高拍仪操作代码 ---------------------------------------------------------- */
    beforeTakePhoto() {
      // 请指定一个材料上传
      //良田高拍仪调试暂不需要
      // if (
      //   !this.openGpyUploadContent &&
      //   DEVEICETYPE.GPY_CONFIG != GAO_PAI_YI_V2
      // ) {
      //   return this.$message.warning("请指定一个材料上传!");
      // }
      if (DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V1) {
        this.takePhotoV1();
      }

      if (DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V2) {
        this.takePhotoV2();
      }
      if (DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V3) {
        this.takePhotoV3();
      }
      if (DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V4) {
        console.log("测试");
        this.takePhotoV4();
      }
    },
    takePhotoV1: debounce(
      async function() {
        if (!this.$refs.hiSpeedCamera.isGpyOpen)
          return this.$message.warning("请先开启高拍仪设备!");
        if (this.startDistinguish)
          return this.$message.warning("正在识别中, 请稍后重试!");

        try {
          // 开始识别
          this.startDistinguish = true;
          this.setDotsFolder();

          const { data, returnCode, returnMsg } = await takePhoto({
            savepath: "/tmp",
            quality: 80
          });
          if (returnCode != 0) {
            this.$message.warning(returnMsg || "拍摄失败");
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
      },
      500,
      true
    ),

    takePhotoV2: debounce(
      async function() {
        if (!this.$refs.hiSpeedCamera.isGpyOpen)
          return this.$message.warning("请先开启高拍仪设备!");
        if (this.startDistinguish)
          return this.$message.warning("正在识别中, 请稍后重试!");

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
              // 是否进行材料分类
              if (this.clflFlag) {
                await this.getEditImageAndClassifilerResult(res.data);
              } else {
                await this.uploadGPYImg(res.data);
              }
            } else {
              this.$message.warning(returnMsg || "拍摄失败");
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
      },
      500,
      true
    ),

    takePhotoV3: debounce(
      async function() {
        if (!this.$refs.hiSpeedCamera.isGpyOpen)
          return this.$message.warning("请先开启高拍仪设备!");
        if (this.startDistinguish)
          return this.$message.warning("正在识别中, 请稍后重试!");

        try {
          // 开始识别
          this.startDistinguish = true;
          this.setDotsFolder();

          const res = await takePic();
          if (res.StateCode != 0) {
            this.$message.warning(returnMsg || "拍摄失败");
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
      },
      500,
      true
    ),
    takePhotoV4: debounce(
      async function() {
        if (this.clflFlag) {
          await this.getEditImageAndClassifilerResult(this.message);
        } else {
          await this.uploadGPYImg(this.message);
        }
        this.resetData();
      },
      500,
      true
    ),

    handleUploadImg(img) {
      return this.$getResponse(this.uploadGPYImg(img), (error, res) => {
        this.startDistinguish = false;
        this.clearDotsFolder();
        if (error || res.code !== 200)
          return this.$message.warning("文件上传失败, 请重新拍照上传");
        this.handleMaterialClassify(res?.data?.[0] ?? {});
      });
    },

    async getEditImageAndClassifilerResult(value) {
      return this.$getResponse(
        getEditImageAndClassifilerResult({
          caseOid: this.caseOid,
          serviceName: this.serviceName,
          serviceOid: this.serviceOid,
          fileList: this.fileList,
          fileListMap: this.fileListMap,
          baseValue: value,
          neddCls: this.neddCls, // 是否分类, 1 需要排序结果的 0 不需要排序结果
          materialOid: this.targetUploadFolder.materialOid
        }),
        (error, ret) => {
          if (ret?.code != 200 || !ret?.data?.classifyRec || error) {
            this.startDistinguish = false;
            this.clearDotsFolder();
            return this.$message.warning("材料分类请求失败, 请重新拍摄");
          }
          // 清除动画
          this.startDistinguish = false;
          this.clearDotsFolder();

          this.handleMaterialClassify(ret.data);
        }
      );
    },

    resetData() {
      // 堆叠图
      this.setStackingMap();
      // 设置预览
      this.setPreviewList();
      // 处理上传文件数量
      this.setUploadedMaterialNums();
    },

    // 鄂尔多斯上传高拍仪图片
    uploadGPYImgEeds(img) {
      let file = this.base64ToFile(img, Date.now() + ".jpg");
      let formData = new FormData();
      formData.append("files", file);
      console.log(formData, "formData");
      return uploadMaterialsFile(formData);
    },
    // 良田上传高拍仪照片
    uploadGPYImg(img) {
      let file = this.base64ToFile(img, Date.now() + ".jpg");
      let formData = new FormData();
      formData.append("files", file);
      uploadMaterialsFile(formData).then(res => {
        if (res.code == 200) {
          // 动画
          this.handleMaterailAnimate();
          // 判断当前是否有打开了文件夹
          if (
            this.targetUploadFolder.caseMaterialOid &&
            !this.targetUploadFolder.notRecogizedFolder
          ) {
            this.targetUploadFolder.qlCaseMaterialAttaList.push({
              ...this.handleData(res.data[0])
            });
          } else {
            this.folderList[0].qlCaseMaterialAttaList.push({
              ...this.handleData(res.data[0])
            });
          }
          this.startDistinguish = false;
          this.clearDotsFolder();
        }
      });
    },

    // 处理数据
    handleData(data) {
      return {
        attaOid: data.attaOid,
        src: data.fastdfsNginxUrl,
        id: data.id || Date.now(),
        userOid: data.userOid
      };
    },

    // 识别失败的 和 识别成功的但是未匹配文件夹的
    handleRecogizedFail(classifyRec) {
      return {
        materialAttaOid: "", // 新增的不取后台生成  修改的取之前带过来的值
        caseMaterialOid: this.targetUploadFolder.caseMaterialOid || "",
        attaOid: classifyRec.attaOid || "",
        materialCatalogOid: "",
        src: classifyRec.src || classifyRec.fastdfsNginxUrl || "",
        materailName: classifyRec.name
      };
    },

    /** 高扫仪 */
    async getScanMessage(data) {
      const { pagenum, message } = data;
      console.log("%c [data]:", "color:red;font-weight:700;", data);

      if (pagenum != "0") {
        _pagenumlist.push(pagenum);
        this.scanMaterialClassifyList.push(
          async () => await this.handleScanMessage(message, pagenum)
        );
      }

      if (pagenum == "0") {
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

    async handleScanMessage(img, pagenum) {
      try {
        if (pagenum != _pagenum) {
          let str =
            _pagenumlen == 1
              ? "即将完成上传"
              : `还剩${--_pagenumlen}张文件未上传`;
          if (this.loading.visible) {
            this.loading.text = `正在上传第${pagenum}张文件, ${str}, 请耐心等候...`;
          } else {
            this.loadingBox(
              `正在上传第${pagenum}张文件, ${str}, 请耐心等候...`
            );
          }
          _pagenum = pagenum;
        }

        await this.handleUploadImg(img);

        // 堆叠图
        this.setStackingMap();
        // 设置预览
        this.setPreviewList();
        // 处理上传文件数量
        this.setUploadedMaterialNums();
      } catch (error) {
        console.log("%c [error]:", "color:red;font-weight:700;", error);
      }
    },

    /** 鄂尔多斯处理材料分类 */
    handleMaterialClassifyEeds(ret) {
      // console.log('11w1w',ret)
      const { attaOid, name, fastdfsNginxUrl, catalogOid } = ret;
      console.log(this.targetUploadFolder, "this.targetUploadFolder");
      if (this.targetUploadFolder.caseMaterialOid) {
        this.targetUploadFolder.qlCaseMaterialAttaList.push({
          ...this.handleRecogizedFail({
            attaOid,
            name,
            src: fastdfsNginxUrl,
            catalogOid
          })
        });
      }
    },
    //此处使用的是良田的处理材料分类进行
    handleMaterialClassify(ret) {
      const { success, classifyRec } = ret;
      if (!classifyRec) return;
      // 判断当前是否有打开了文件夹
      if (
        this.targetUploadFolder.caseMaterialOid &&
        !this.targetUploadFolder.notRecogizedFolder
      ) {
        return this.handleAppointFolderUpload(
          success,
          classifyRec,
          this.targetUploadFolder
        );
      } else {
        if (classifyRec.materialOid) {
          const targetFolder = this.folderList.find(
            item => item.materialOid === classifyRec.materialOid
          );
          if (targetFolder) {
            return this.handleAppointFolderUpload(
              success,
              classifyRec,
              targetFolder
            );
          }
        }
      }
      // 未识别 识别失败的
      if (success == false) {
        // 分类动画
        this.handleMaterailAnimate();
        // 未识别文件夹推送文件信息
        this.folderList[0].qlCaseMaterialAttaList.push({
          ...this.handleRecogizedFail(classifyRec)
        });
      } else {
        // 删除oid
        Reflect.deleteProperty(classifyRec, "oid");

        const materialOids = this.folderList.map(item => item.caseMaterialOid);
        if (!materialOids.includes(classifyRec.caseMaterialOid)) {
          // 分类动画
          this.handleMaterailAnimate();
          // 未匹配文件夹推送文件信息
          this.folderList[0].qlCaseMaterialAttaList.push({
            ...this.handleRecogizedFail(classifyRec)
          });
        } else {
          const targetFolder = this.folderList.findIndex(
            item => item.caseMaterialOid == classifyRec.caseMaterialOid
          );
          // 判断是否是精细化材料
          if (classifyRec.refinedMaterialOid && targetFolder != -1) {
            const targetIndex = this.folderList[
              targetFolder
            ].qlCaseMaterialAttaList.findIndex(
              item => item.oid == classifyRec.refinedMaterialOid
            );
            if (targetIndex >= 0) {
              // 如果之前已经存在了 也直接覆盖
              this.folderList[targetFolder].qlCaseMaterialAttaList[
                targetIndex
              ] = {
                ...this.folderList[targetFolder].qlCaseMaterialAttaList[
                  targetIndex
                ],
                ...classifyRec,
                src: classifyRec.src || classifyRec.fastdfsNginxUrl || "",
                resultStatus: "0" // FIX: 智能审核，纠正材料，重新上传后，不应该再标记该材料是审核不通过的
              };
            } else {
              console.warn("未匹配相关精细化材料");
            }
          }
        }
      }
    },

    /** 指定材料上传 */
    handleAppointFolderUpload(success, classifyRec, targetFolder) {
      if (success == false) {
        targetFolder.qlCaseMaterialAttaList.push({
          ...this.handleRecogizedFail(classifyRec)
        });
      } else {
        // 删除oid
        Reflect.deleteProperty(classifyRec, "oid");

        const materialOids = this.folderList.map(item => item.caseMaterialOid);
        if (!materialOids.includes(classifyRec.caseMaterialOid)) {
          // 未匹配文件夹推送文件信息
          targetFolder.qlCaseMaterialAttaList.push({
            ...this.handleRecogizedFail(classifyRec)
          });
        } else {
          // 判断是否是精细化材料
          if (classifyRec.refinedMaterialOid) {
            const targetIndex = targetFolder.qlCaseMaterialAttaList.findIndex(
              item => item.oid == classifyRec.refinedMaterialOid
            );
            if (targetIndex >= 0) {
              // 如果之前已经存在了 也直接覆盖
              targetFolder.qlCaseMaterialAttaList.splice(targetIndex, 1, {
                ...targetFolder.qlCaseMaterialAttaList[targetIndex],
                ...classifyRec,
                src: classifyRec.src || classifyRec.fastdfsNginxUrl || "",
                resultStatus: "0" // FIX: 智能审核，纠正材料，重新上传后，不应该再标记该材料是审核不通过的
              });
            } else {
              console.warn("未匹配相关精细化材料");
              targetFolder.qlCaseMaterialAttaList.push({
                ...this.handleRecogizedFail(classifyRec)
              });
            }
          }
        }
      }
    },

    /** ---------------------------------------------------------- 公共代码 ---------------------------------------------------------- */

    async beforeClose(done) {
      this.$refs?.gpyUploadContent?.handleClose();

      if (this.$refs.hiSpeedCamera.loadingGpy)
        return this.$message.warning("正在启动高拍仪设备, 请稍后关闭");
      // 保存材料
      try {
        await this.nextStep(false);
        done();
      } catch (error) {
        done();
      }
    },

    // 打开loading动画
    loadingBox(text = "正在拍摄中...") {
      this.loading = this.$loading({
        lock: true,
        text: text,
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
    },

    // 图片转文件对象
    base64ToFile(urlData, fileName) {
      let bytes = atob(urlData); // 解码base64
      let n = bytes.length;
      let ia = new Uint8Array(n);
      while (n--) {
        ia[n] = bytes.charCodeAt(n);
      }
      return new File([ia], fileName, {
        type: "image/jpeg"
      });
    },

    // 重置
    handleReset() {
      this.ErdsPhotoCounts = 0;
      this.$refs.reset.$el.blur();
      this.$confirm("此操作将删除所有已经上传过的文件, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        closeOnClickModal: false,
        closeOnPressEscape: false,
        type: "warning"
      })
        .then(() => {
          if (this.startDistinguish)
            return this.$message.warning("正在识别中, 请稍后重试!");
          this.clearDotsFolder();
          this.folderList.forEach(item => {
            item.previewList = [];
            item.foldedPics = [];
            if (item.isRefinedMaterialFolder) {
              item.qlCaseMaterialAttaList = item.qlCaseMaterialAttaList
                .filter(child => child.isRefinedMaterial)
                .map(child =>
                  child.isRefinedMaterial && child.src && child.origin
                    ? { ...child.origin, origin: child.origin }
                    : { ...child }
                );
            } else {
              item.qlCaseMaterialAttaList = [];
            }
          });
          this.resetData();
        })
        .catch(() => {});
    },

    // 下一步
    async nextStep(isNext = true) {
      this.$bus.$emit("nextStepin");
      if (this.startDistinguish)
        return this.$message.warning("正在识别中, 请稍后重试!");
      this.$refs?.gpyUploadContent?.handleClose();

      this.clearDotsFolder();
      // console.log('我的材料eeeeeeeeeeeeeeeeeeeeeeeeee', this.getSaveFilesParams(isNext))
      // console.log('wodefujian',this.materialAttaList)
      let params = [
        ...this.getSaveFilesParams(isNext),
        ...this.materialAttaList
      ];
      // params = params.filter(item => item.qlCaseMaterialAttaList.length == 0)
      if (
        params.every(item => item.qlCaseMaterialAttaList.length == 0) &&
        isNext
      )
        return this.$message.warning("暂无保存材料信息");

      let loading;

      loading = this.$loading({
        lock: true,
        text: "正在保存材料附件中",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });

      try {
        const { code, message, data } = await saveOrUpdateCaseMaterialAttaList(
          params
        );
        if (code != 200 || (data.success && !data.success)) {
          loading && loading.close();
          this.zfloading = false;
          return this.$message.warning(
            message || data.message || "保存材料附件失败"
          );
        }

        loading && loading.close();

        // if (isNext) {
        //   this.$emit('update:dialogVisible', false);
        //   this.$emit('nextStep', 4);
        // } else {
        //   this.$emit('update:dialogVisible', false);
        // }
        if (isNext) {
          // 智审界面点击保存
          if (this.changed) {
            // this.zftext = '材料智能预审中，请稍后...';
            const caseMaterialOid = this.folderList.filter(
              item => !item.notRecogizedFolder
            )[0].caseMaterialOid;
            const {
              code: _code,
              message: _message
            } = await intelligentPretrialmaterialPrePrialByCaseMaterialOid({
              caseMaterialOid
            });
            if (_code != 200) {
              // loading.close();
              this.zfloading = false;
              return this.$message.warning(_message || "材料预审失败");
            }
            // loading.close();
            this.zfloading = false;

            // 刷新界面
            this.$emit("refreshIntelligentList");
            this.$emit("update:dialogVisible", false);
          } else {
            // this.zftext = '材料智能预审中，请稍后...';
            const {
              code: _code,
              message: _message
            } = await intelligentPretrialmaterialPrePrialAllNew({
              caseOid: this.caseOid
            });
            if (_code != 200) {
              // loading.close();
              this.zfloading = false;
              return this.$message.warning(_message || "材料预审失败");
            }
            // loading.close();
            this.zfloading = false;
            this.$emit("update:dialogVisible", false);
            this.$emit("nextStep", 4);
          }
        } else {
          // loading.close();
          this.zfloading = false;
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
    getSaveFilesParams(isNext = true) {
      return this.folderList.map(item => {
        return {
          id: item.id,
          caseMaterialOid: item.caseMaterialOid,
          materialOid: item.materialOid,
          materailName: item.materailName,
          caseOid: item.caseOid,
          materialSampleAddr: item.materialSampleAddr,
          materialSampleAddrYl: item.materialSampleAddrYl,
          collectionType: item.collectionType,
          qlCaseMaterialAttaList: isNext
            ? item.qlCaseMaterialAttaList.map(child => ({
                ...child,
                id: child.isCache ? child.id : "",
                caseMaterialOid: item.caseMaterialOid
              }))
            : item.qlCaseMaterialAttaList
                .filter(child => child.src)
                .map(child => ({
                  ...child,
                  id: child.isCache ? child.id : "",
                  caseMaterialOid: item.caseMaterialOid
                }))
        };
      });
    },

    /** ---------------------------------------------------------- 文件夹相关操作 ---------------------------------------------------------- */
    // 设置文件夹信息
    setFolderEeds(item, idx) {
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
      //           serviceOid: i.serviceOid,
      //           licenceOid: i.licenceOid,
      //           licenceName: i.licenceName,
      //           isRefinedMaterial: true, // 是否是精细化材料
      //           refinedMaterialNum: i.serialNumber, // 精细化材料页码引用
      //           resultStatus: i.resultStatus, // 是否通过智审 0 通过 1 不通过 6 附件不符合审核模板
      //         }
      //       })
      //       .map(i => ({ ...i, origin: i }))
      // }

      // if (item.qlCaseMaterialAttaList.length > 0) {
      //   item.qlCaseMaterialAttaList.forEach(i => {
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

      // 合并不是精细化材料的数据
      // list = [...list, ...item.qlCaseMaterialAttaList.filter(i => !i.refinedMaterialOid)];

      // list = list.map(child => ({ ...this.setQlCaseMaterialAttaListInfo(child, item) }));
      console.log(item.qlCaseMaterialAttaList, "hasUploadPage");
      return {
        id: item.id,
        caseMaterialOid: item.caseMaterialOid,
        materialOid: item.materialOid,
        materialName: item.materialName,
        caseOid: item.caseOid,
        materialSampleAddr: item.materialSampleAddr,
        materialSampleAddrYl: item.materialSampleAddrYl,
        qlCaseMaterialAttaList: item.qlCaseMaterialAttaList,
        title: item.materialName,
        showPopover: false,
        foldedPics: [], // 堆叠图
        previewList: [], // 预览
        // total: item.refinedMaterialList.length,
        total: 0,
        hasUploadPage:
          item.qlCaseMaterialAttaList.filter(i => i.src).length + 1,
        // isIntelligentFail: item.qlCaseMaterialAttaList.some(i => i.resultStatus == '1' || i.resultStatus == '6'), // 智审不通过
        // mustFlag: item.mustFlag, // 事项材料 是否为必传
        // isRefinedMaterialFolder: item.refinedMaterialList.length > 0, // 是否含有精细化材料
        // isShowTooltip: isShowTooltip(item.materialName, 100),
        index: idx + 1,
        collectionType: item.collectionType || "3"
      };
    },

    //良田设置文件夹方法
    setFolder(item, idx) {
      // 处理细化材料
      let list = [];
      if (item.refinedMaterialList.length > 0) {
        list = item.refinedMaterialList
          .map(i => {
            return {
              id: i.id,
              oid: i.oid,
              materialOid: i.materialOid,
              refinedMaterialName: i.refinedMaterialName,
              needStatus: i.needStatus, // 1 为必传
              materialSampleAddr: i.materialSampleAddr,
              materialSampleOid: i.materialSampleOid,
              serialNumber: i.serialNumber,
              materialCatalogOid: i.materialCatalogOid,
              serviceOid: i.serviceOid,
              licenceOid: i.licenceOid,
              licenceName: i.licenceName,
              isRefinedMaterial: true, // 是否是精细化材料
              refinedMaterialNum: i.serialNumber, // 精细化材料页码引用
              resultStatus: i.resultStatus // 是否通过智审 0 通过 1 不通过 6 附件不符合审核模板
            };
          })
          .map(i => ({ ...i, origin: i }));
      }
      if (item.qlCaseMaterialAttaList.length > 0) {
        item.qlCaseMaterialAttaList.forEach(i => {
          if (i.refinedMaterialOid) {
            let idx = list.findIndex(k => k.oid == i.refinedMaterialOid);
            if (idx != -1) {
              list[idx] = {
                ...list[idx],
                ...i,
                isCache: true,
                origin: { ...list[idx], resultStatus: undefined }
              };
            }
          }
        });
      }

      // 合并不是精细化材料的数据
      list = [
        ...list,
        ...item.qlCaseMaterialAttaList.filter(i => !i.refinedMaterialOid)
      ];

      list = list.map(child => ({
        ...this.setQlCaseMaterialAttaListInfo(child, item)
      }));
      const obj = {};
      return {
        id: item.id,
        caseMaterialOid: item.caseMaterialOid,
        materialOid: item.materialOid,
        materialName: item.materialName,
        caseOid: item.caseOid,
        materialSampleAddr: item.materialSampleAddr,
        materialSampleAddrYl: item.materialSampleAddrYl,
        qlCaseMaterialAttaList: list,
        title: item.materialName,
        showPopover: false,
        foldedPics: [], // 堆叠图
        previewList: [], // 预览
        total: item.refinedMaterialList.length,
        hasUploadPage: list.filter(i => i.src).length,
        isIntelligentFail: list.some(
          i => i.resultStatus == "1" || i.resultStatus == "6"
        ), // 智审不通过
        mustFlag: item.mustFlag, // 事项材料 是否为必传
        isRefinedMaterialFolder: item.refinedMaterialList.length > 0, // 是否含有精细化材料
        isShowTooltip: isShowTooltip(item.materialName, 100),
        index: idx + 1,
        collectionType: item.collectionType || "3"
      };
    },

    /**
     * 设置上传文件信息folderList
     * @param {object} item 分类成功的信息
     * @param {object} folder 文件夹的信息
     */
    setQlCaseMaterialAttaListInfo(item = {}, folder = {}) {
      return {
        ...item,
        id: item.id || Date.now(),
        materialAttaOid: item.materialAttaOid, // 新增的不取后台生成  修改的取之前带过来的值
        caseMaterialOid: item.caseMaterialOid || folder.caseMaterialOid || "", // 1.分类成功带过来的 2.分类失败 拖拽从文件夹带过来
        attaOid: item.attaOid || folder.attaOid || "",
        materialCatalogOid: item.materialCatalogOid || "", // 分类成功带过来的 拖拽不用赋值
        src:
          item.src ||
          item.fastdfsNginxUrl ||
          folder.fastdfsNginxUrl ||
          folder.src ||
          ""
      };
    },

    // 设置预览图片列表
    setPreviewList() {
      this.folderList.forEach(item => {
        item.previewList = item.qlCaseMaterialAttaList
          .filter(item => item.src)
          .map(child => child.src);
      });
    },

    // 拼接堆叠图
    setStackingMap() {
      const pos = [
        { left: "10px", top: "-10px", zIndex: 5 },
        { left: "8px", top: "-8px", zIndex: 4 },
        { left: "6px", top: "-6px", zIndex: 3 },
        { left: "4px", top: "-4px", zIndex: 2 },
        { left: "2px", top: "-2px", zIndex: 1 }
      ];
      this.folderList.forEach(item => {
        item.foldedPics = item.qlCaseMaterialAttaList
          .filter(i => i.src)
          .slice(0, 5)
          .map((child, idx) => {
            return {
              ...child,
              src: child.src,
              left: pos[idx].left,
              top: pos[idx].top,
              width: "52px",
              height: "74px",
              zIndex: pos[idx].zIndex
            };
          });
      });
    },

    // 设置未能识别文件夹
    setNotRecognizedFolder() {
      const hasNoRecognizedFolder = this.folderList.find(
        item => item.notRecogizedFolder
      );
      if (hasNoRecognizedFolder) return;
      this.folderList.unshift({
        id: Date.now(),
        title: "未能识别",
        qlCaseMaterialAttaList: [],
        fontColor: "#fff",
        background:
          "linear-gradient(270deg, rgba(255, 112, 26, .8) 0%, rgba(255, 63, 25, .8) 100%)",
        showPopover: false,
        foldedPics: [],
        previewList: [],
        notRecogizedFolder: true,
        isShowTooltip: true,
        index: 0
      });
    },

    // 设置dots文件夹 识别动画
    setDotsFolder() {
      this.$refs.hiSpeedCamera.loadingText = "正在识别";
      this.$refs.hiSpeedCamera.loadingMaterials = true;
    },

    // 清除dots动画
    clearDotsFolder() {
      this.$refs.hiSpeedCamera.loadingMaterials = false;
      this.$refs.hiSpeedCamera.loadingText = "正在启动设备";
    },

    // 预览
    handlePreview(ref) {
      const refs = this.$refs[`${ref}`][0];
      if (refs) {
        if (refs.loading) return this.$message.warning("正在加载中,请稍后");
        refs.handlePreview();
      }
    },

    /**
     * 设置已经上传的材料页数
     */
    setUploadedMaterialNums() {
      this.folderList.forEach(item => {
        try {
          item.hasUploadPage = item.qlCaseMaterialAttaList.filter(
            i => i.src
          ).length;
        } catch (error) {
          console.log(error, item);
        }
      });
    },

    /** 右键事件 */
    handleContextmenu(e, content) {
      const { child, parentIdx } = content;
      // this.showContextMenu = false;
      // if (!child.src) return;
      e.preventDefault();
      this.setContextmenuList();
      this.$nextTick(() => {
        const { clientX, clientY } = e;
        let top, left;
        // 判断鼠标位置
        if (clientY + 210 >= document.body.offsetHeight) {
          top = clientY - 215;
        } else {
          top = clientY + 5;
        }

        if (clientX + 530 >= document.body.offsetWidth) {
          this.direction = "right";
          left = clientX - 180;
        } else {
          this.direction = "left";
          left = clientX;
        }
        this.contextMenuStyle = { top: top + "px", left: left + "px" };
        this.setCurrentContextmenu();
        this.currentContextMenuMaterial = child;

        this.$nextTick(() => {
          this.showContextMenu = true;
        });
      });
    },

    setCurrentContextmenu() {
      const children = this.targetUploadFolder.qlCaseMaterialAttaList
        .filter(item => item.isRefinedMaterial)
        .sort((a, b) => a.refinedMaterialNum - b.refinedMaterialNum)
        .map((i, index) => {
          return {
            children: [],
            level: 2,
            label: `第${i.refinedMaterialNum}页(${i.refinedMaterialName})`,
            id: i.id,
            hasChildren: false,
            isExtend: false,
            isCurrent: true,
            fileIdx: index,
            hasUpload: !!i.src,
            showTooltip: !isShowTooltip(
              `第${i.refinedMaterialNum}页(${i.refinedMaterialName})`,
              140
            )
          };
        });
      const currentIdx = this.contextmenuList.findIndex(item => item.isCurrent);
      if (children.length) {
        children.push({
          children: [],
          level: 2,
          label: "插入文件到目录最后",
          id: (Date.now() + getRandomIntInclusive(1, 1000)).toString(),
          hasChildren: false,
          isExtend: false,
          isInsert: true,
          folderIdx: this.currentUploadFolderIndex
        });
        const current = {
          children: children,
          label: "当前(匹配)",
          id: "$$1",
          level: 1,
          hasChildren: children.length > 0,
          isCurrent: true,
          isExtend: false
        };
        if (currentIdx > 0) {
          this.contextmenuList[currentIdx] = current;
        } else {
          this.contextmenuList.splice(1, 0, current);
        }
      } else {
        // 移除当前
        if (currentIdx > 0) {
          this.contextmenuList.splice(1, 1);
        }
      }
    },

    /** 点击popover其他地方关闭右键 */
    handleContentMainClick(e) {
      if (this.showContextMenu) {
        if (!e.target.contains(this.$refs.contextmenu.$el)) {
          this.showContextMenu = false;
        }
      }
    },

    handleNodeMove(node) {
      let { fileIdx, folderIdx, isInsert, isCurrent } = node;
      const { currentContextMenuMaterial, currentUploadFolderIndex } = this;
      if (isCurrent) folderIdx = this.currentUploadFolderIndex;

      const targetIdx = this.folderList[
        currentUploadFolderIndex
      ].qlCaseMaterialAttaList.findIndex(
        item => item.attaOid === currentContextMenuMaterial.attaOid
      );
      if (isInsert) {
        // 判断是否是精细化材料
        if (currentContextMenuMaterial.isRefinedMaterial) {
          this.folderList[folderIdx].qlCaseMaterialAttaList.push(
            this.handleRefiedMaterialToCommon(currentContextMenuMaterial)
          );
          // 重置
          this.folderList[
            currentUploadFolderIndex
          ].qlCaseMaterialAttaList.splice(targetIdx, 1, {
            ...currentContextMenuMaterial.origin
          });
        } else {
          this.folderList[folderIdx].qlCaseMaterialAttaList.push({
            ...currentContextMenuMaterial
          });
          // 销毁原始数据
          this.folderList[
            currentUploadFolderIndex
          ].qlCaseMaterialAttaList.splice(targetIdx, 1);
        }
      } else if (folderIdx >= 0 && fileIdx >= 0) {
        // 移入的位置的材料
        const material = this.folderList[folderIdx].qlCaseMaterialAttaList[
          fileIdx
        ];
        // 如果当前右击的材料是精细化材料
        if (currentContextMenuMaterial.isRefinedMaterial) {
          // 如果是精细化材料
          const movingMaterial = this.handleRefiedMaterialToCommon(
            currentContextMenuMaterial
          );
          if (material.src) {
            this.folderList[folderIdx].qlCaseMaterialAttaList.push(
              this.handleRefiedMaterialToCommon(material)
            );
          }
          this.folderList[folderIdx].qlCaseMaterialAttaList[fileIdx] = {
            ...movingMaterial,
            ...material,
            attaOid: movingMaterial.attaOid,
            refinedMaterialOid: material.oid,
            origin: material.origin || material,
            src: movingMaterial.src,
            resultStatus: "0"
          };
          // 销毁原始数据
          this.folderList[
            currentUploadFolderIndex
          ].qlCaseMaterialAttaList.splice(targetIdx, 1, {
            ...currentContextMenuMaterial.origin
          });
        } else {
          if (material.src) {
            this.folderList[folderIdx].qlCaseMaterialAttaList.push(
              this.handleRefiedMaterialToCommon(material)
            );
          }
          this.folderList[folderIdx].qlCaseMaterialAttaList[fileIdx] = {
            ...currentContextMenuMaterial,
            ...material,
            attaOid: currentContextMenuMaterial.attaOid,
            refinedMaterialOid: material.oid,
            origin: material.origin || material,
            src: currentContextMenuMaterial.src,
            resultStatus: "0"
          };
          this.folderList[
            currentUploadFolderIndex
          ].qlCaseMaterialAttaList.splice(targetIdx, 1);
        }
      }

      this.showContextMenu = false;

      this.$nextTick(() => {
        // 重新计算
        this.setStackingMap();
        this.setPreviewList();
        // 已经上传的材料
        this.setUploadedMaterialNums();
      });
    },

    handleNodeRemove() {
      const {
        currentUploadFolderIndex,
        folderList,
        currentContextMenuMaterial
      } = this;
      if (currentUploadFolderIndex >= 0) {
        const childIndex = folderList[
          currentUploadFolderIndex
        ].qlCaseMaterialAttaList.findIndex(
          item => item.attaOid === currentContextMenuMaterial.attaOid
        );
        if (childIndex >= 0) {
          const target =
            folderList[currentUploadFolderIndex].qlCaseMaterialAttaList[
              childIndex
            ];
          if (target.isRefinedMaterial) {
            folderList[currentUploadFolderIndex].qlCaseMaterialAttaList.splice(
              childIndex,
              1,
              { ...target.origin, origin: target.origin }
            );
          } else {
            folderList[currentUploadFolderIndex].qlCaseMaterialAttaList.splice(
              childIndex,
              1
            );
          }
        }
      }

      this.resetData();
    },

    // 将精细化材料传普通材料
    handleRefiedMaterialToCommon(material) {
      return {
        attaOid: material.attaOid,
        caseMaterialOid: material.caseMaterialOid,
        id: Date.now(),
        materialAttaOid: "",
        materialCatalogOid: "",
        resultStatus: "0",
        src: material.src
      };
    },

    handleFolderClick(folder) {
      this.folderList.forEach(item => (item.showPopover = false));
      folder.showPopover = true;
      // 指定材料
      this.targetUploadFolder = folder;
      // 切换模式
      this.$refs.hiSpeedCamera.setModeToggle();
      this.openGpyUploadContent = true;
    },

    handleClose() {
      // 切换模式
      this.$refs.hiSpeedCamera.setModeToggle();
      this.openGpyUploadContent = false;
      this.targetUploadFolder.showPopover = false;
      this.targetUploadFolder = {};
      this.resetData();
    },

    handleSort(data) {
      let list = [];
      Object.entries(data).forEach(item => {
        const [value, key] = item;
        list[key] = value;
      });
      list = list.filter(i => i);
      const { qlCaseMaterialAttaList } = this.targetUploadFolder;
      const [refinedMaterialList, materialList] = [
        qlCaseMaterialAttaList.filter(item => !item.src),
        qlCaseMaterialAttaList.filter(item => item.src)
      ];
      const sortList = [];
      list.forEach(item => {
        const target = materialList.find(i => i.beforeAttaOid === item);
        if (target) {
          sortList.push(target);
        }
      });
      this.targetUploadFolder.qlCaseMaterialAttaList = [
        ...refinedMaterialList,
        ...sortList
      ];
    },

    handleDraggableUpdate(value) {
      this.targetUploadFolder.qlCaseMaterialAttaList = value;
    },
    hanldeSortCancle(data) {
      this.targetUploadFolder.qlCaseMaterialAttaList = data;
    },
    handleMaterailAnimate() {
      const img = document.querySelectorAll(".list-item--folder-img")[0];
      if (img) {
        img.classList.add("list-item--folder-img-animate");
        setTimeout(() => {
          img.classList.remove("list-item--folder-img-animate");
        }, 500);
      }
    },

    handleDelete(attaOid) {
      this.ErdsPhotoCounts--; //鄂尔多斯拍照单独计数
      this.targetUploadFolder.qlCaseMaterialAttaList = this.targetUploadFolder.qlCaseMaterialAttaList.filter(
        item => item.attaOid !== attaOid
      );
    }
  }
};
