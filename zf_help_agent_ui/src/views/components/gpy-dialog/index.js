import { isShowTooltip, debounce } from '@/utils/utils.js';
// 接口api
import { takePhoto } from '@/api/gaopaiyi.js';
import {
    uploadMaterialsFile,
    updateCaseMaterialAttaList,
    getQlCaseMaterialAttaList,
    deleteByMaterialOid
} from '@/api/materialCategory.js';

import { uploadFile } from '@/api/onething/comboManager/comboAccept/materialCategory.js';

import { getComboCaseMaterialAttaList, updateComboCaseMateriallList, deleteByComboMaterialOid } from '@/api/onething/comboManager/comboAccept/materialCategory';

import ZfImageLoading from '@/components/ZfImageLoading';
import ImgLoading from '@/components/ImgLoading';
import ArrowBar from '@/components/HiSpeedCamera/arrow-bar.vue';
import HiSpeedCamera from '@/components/HiSpeedCamera/hi-speed-camera.vue';

import DEVEICETYPE, { GAO_PAI_YI_V1, GAO_PAI_YI_V2, GAO_PAI_YI_V3 } from '@/components/HiSpeedCamera/config.js';

import { takePic } from "@/api/handwarev3.js";

export default {
    name: 'GpyUploadDialog',
    inheritAttrs: false,
    props: {
        dialogVisible: {
            type: Boolean,
            default: false,
        },
        qlCaseMaterial: {
            type: Object,
            default: () => ({})
        },
        /** 是否是一件事 */
        isOnethingRqbz: Boolean
    },
    components: { ZfImageLoading, ImgLoading, ArrowBar, HiSpeedCamera },
    data () {
        return {
            uploadFilesList: [],
            overflowY: 'unset', // 是否展示滚动条
            loading: false, // loading 动画
            startDistinguish: false, // 开始识别
            previewList: [],
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
        caseOid ({ qlCaseMaterial }) {
            return qlCaseMaterial?.caseOid;
        },
        serviceOid ({ qlCaseMaterial }) {
            return qlCaseMaterial?.serviceOid;
        },
        serviceName ({ qlCaseMaterial }) {
            return qlCaseMaterial?.serviceName;
        },
    },
    watch: {
        'uploadFilesList.length': {
            handler () {
                this.$nextTick(() => {
                    const oneLine = Math.floor(this.$refs.scanResultList.offsetWidth / 130);
                    if (this.uploadFilesList.length > oneLine * 3) {
                        this.overflowY = 'scroll';
                    } else {
                        this.overflowY = 'unset';
                    }
                })
            },
            immediate: true,
        }
    },
    mounted () {
        this.getAllQlCaseMaterialList();
    },
    methods: {
        isShowTooltip,
        /**
         * 获取所有材料列表
         */
        async getAllQlCaseMaterialList () {
            this.loadingBox('正在获取材料列表数据');
            try {
                const { data, code, message } = this.isOnethingRqbz ?
                    await getComboCaseMaterialAttaList({ caseMaterialOid: this.qlCaseMaterial.caseMaterialOid })
                    :
                    await getQlCaseMaterialAttaList({ caseMaterialOid: this.qlCaseMaterial.caseMaterialOid });
                if (code !== 200) {
                    this.loading.close();
                    return message && this.$message.warning(message);
                }
                this.loading.close();
                this.uploadFilesList = data ?? [];
                this.setPreviewList();
            } catch (error) {
                this.loading.close();
            }
        },

        /** 初始化设置样表 */
        handleArrowList (item) {
            return {
                id: item.id,
                title: item.materialName,
                srcList: item?.refinedMaterialList?.slice(0, 2)?.map(child => {
                    return {
                        ...child,
                        src: child.materialSampleAddr || '',
                        imgName: child.refinedMaterialName,
                    }
                }),
                arrowItemPopover: false,
                canPreview: false,
            }
        },

        takePhoto: debounce(async function () {
            // console.log('%c [点击拍照]:', 'color:aqua;font-weight:700;');
            if (!this.$refs.hiSpeedCamera.isGpyOpen) return this.$message.warning('请先开启高拍仪设备!');
            if (this.startDistinguish) return this.$message.warning('正在识别中, 请稍后重试!');

            try {
                this.loadingBox();
                // 开始识别
                this.startDistinguish = true;

                let uploadRet = {};

                if (DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V1) {
                    const { data, returnCode, returnMsg } = await takePhoto({
                        savepath: '/tmp',
                        quality: 80
                    });
                    if (returnCode !== 0) {
                        this.$message.warning(returnMsg || '拍摄失败');
                        this.startDistinguish = false;
                        this.loading.close();
                        return;
                    }
                    uploadRet = await this.uploadGPYImg(data.img);
                }

                if (DEVEICETYPE.GPY_CONFIG === GAO_PAI_YI_V3) {
                    const res = await takePic();
                    if (res.StateCode !== 0) {
                        this.$message.warning('拍摄失败');
                        this.startDistinguish = false;
                        this.loading.close();
                        return;
                    }
                    uploadRet = await this.uploadGPYImg(res.data);
                }

                // 上传
                if (uploadRet.code !== 200 || !uploadRet.data) {
                    this.startDistinguish = false;
                    this.loading.close();
                    return this.$message.warning('上传文件失败, 请重新拍摄');
                }

                const uploadRetData = uploadRet.data[0];

                // 清除动画
                this.startDistinguish = false;
                this.loading.close();

                this.uploadFilesList.push({
                    ...uploadRetData,
                    id: null,
                    src: uploadRetData.fastdfsNginxUrl,
                });
            } catch (error) {
                this.startDistinguish = false;
                this.loading.close();
            }

            // 设置预览
            this.setPreviewList();

        }, 500, true),

        // 上传高拍仪图片
        uploadGPYImg (img) {
            let file = this.base64ToFile(img, Date.now() + ".jpg");
            let formData = new FormData();
            formData.append("files", file);
            return this.isOnethingRqbz ? uploadFile(formData) : uploadMaterialsFile(formData);
        },

        async beforeClose (done) {
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

        // 下一步
        async nextStep () {
            if (this.startDistinguish) return this.$message.warning('正在识别中, 请稍后重试!');
            if (this.uploadFilesList.length === 0) return this.$emit('update:dialogVisible', false);

            const loading = this.$loading({
                lock: true,
                text: '正在保存材料附件中',
                spinner: 'el-icon-loading',
                background: 'rgba(0, 0, 0, 0.7)'
            });

            try {
                const params = this.uploadFilesList.map(item => ({ ...item, caseMaterialOid: this.qlCaseMaterial.caseMaterialOid }))
                const { code, message } = this.isOnethingRqbz ?
                    await updateComboCaseMateriallList(params) : await updateCaseMaterialAttaList(params);
                if (code !== 200) {
                    loading.close();
                    return this.$message.warning(message || '保存材料附件失败');
                }
                loading.close();
                this.$message.success('保存材料附件成功');
                this.$emit('sendUploadFilesList', params);
                this.$emit('update:dialogVisible', false);
            } catch (error) {
                loading.close();
            }
        },

        // 删除图片 前端删除
        deleteAttaItem (idx) {
            try {
                if (this.isOnethingRqbz) {
                    deleteByComboMaterialOid(this.uploadFilesList[idx].materialAttaOid);
                } else {
                    deleteByMaterialOid(this.uploadFilesList[idx].materialAttaOid);
                }
                this.uploadFilesList.splice(idx, 1);
                this.setPreviewList();
            } catch (error) {

            }
        },

        // 设置预览图片列表
        setPreviewList () {
            this.previewList = this.uploadFilesList.map(item => item.src);
        },

        // 预览
        handlePreview (ref) {
            const refs = this.$refs[`${ref}`][0];
            if (refs) {
                if (refs.loading) return this.$message.warning('正在加载中,请稍后');
                refs.handlePreview();
            }
        },
    }
}
