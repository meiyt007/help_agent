/** 签名 */
import { saveSignRecord } from "@/api/zc/businessManagement/signRecord";
import { showCallMessage } from "@/api/zc/businessManagement/doubleScreenInteraction";
import { mapGetters } from "vuex";
import { FASTDFS_PATH } from "@/utils/config";
export default {
    data () {
        return {
            timer: null,
            imageUrl: "",
        }
    },
    deactivated () {
        clearInterval(this.timer)
    },
    computed: {
        ...mapGetters(["deviceMap"]),
    },
    methods: {
        closeAjax () {
            $.ajax({
                url: process.env.VUE_APP_SIGN_API + '/HWPenSign/HWFinalize', //请求地址
                type: "get", //请求方式
                dataType: 'jsonp',
                success: (data) => { //成功回调
                    console.log('close  sucess');
                },
                fail: (data) => {
                    console.log(' close  fail');
                }
            });
        },
        //获取签字base图片
        getSignAjax () {
            $.ajax({
                url: process.env.VUE_APP_SIGN_API + '/HWPenSign/HWGetSign', //请求地址
                type: "get", //请求方式
                dataType: 'jsonp',
                success: (data) => { //成功回调
                    console.log("间隔获取签字base图片");
                    console.log(data);
                    if (data.msgID == 0) {
                        console.log("成功获取签字base图片");
                        console.log(data.message);
                        if (data.message != null) {
                            clearInterval(this.timer);
                            this.saveSign(data.message);
                        }
                    } else if (data.msgID == 2) {
                        clearInterval(this.timer);
                        //关闭
                        this.closeAjax();
                    }
                },
                fail: function (data) {
                    console.log('fail')
                }
            });
        },
        signInitializeAjax () {
            $.ajax({
                url: process.env.VUE_APP_SIGN_API + '/HWPenSign/HWInitialize', //请求地址
                type: "get", //请求方式
                data: {
                    "nLogo": "签字",
                    "nPenwidth": "3",
                    "nOrgX": "100",
                    "nOrgY": "200",
                    "width": "500",
                    "height": "300",
                    "nImageType": "3",
                    "nImageWidth": "500",
                    "nImageHeight": "300"
                },
                dataType: 'jsonp',
                success: (data) => { //成功回调
                    this.timer = setInterval(() => {
                        this.getSignAjax();
                    }, 1000)
                },
                fail: function (data) {
                    console.log('fail')
                }
            });
        },
        closeAndInit () {
            this.closeAjax();
            clearInterval(this.timer);
            $.ajax({
                url: process.env.VUE_APP_SIGN_API + '/HWPenSign/HWFinalize', //请求地址
                type: "get", //请求方式
                dataType: 'jsonp',
                success: (data) => { //成功回调
                    console.log('close  sucess');
                    this.signInitializeAjax();
                },
                fail: (data) => {
                    console.log(' close  fail');
                }
            });
        },

        showSignImage () {
            let url = window.location.origin;
            let pushUrl = url + "/serviceHall/signConfirmation.html";
            let data = {};
            let materialList = [];
            if (this.sxServiceMaterialList.length > 0) {
                this.sxServiceMaterialList.forEach(material => {
                    console.log(material);
                    let ma = {};
                    ma.materialName = material.materialName;
                    materialList.push(ma);
                });
            }
            data.userName = this.loginUser.userName;
            data.organName = this.loginUser.organName;
            data.projectName = this.ruleForm.projectName;
            data.caseNumber = this.ruleForm.caseNumber;
            data.sxServiceMaterialList = materialList;
            data.imageUrl = this.imageUrl;
            data.proxyUrl = url + process.env.VUE_APP_IMAGE_API;
            // data.fastdfsUrl = process.env.VUE_APP_FASTDFS_API;
            data.fastdfsUrl = this.deviceMap?.[FASTDFS_PATH];
            pushUrl = pushUrl + "?jsonObject=" + JSON.stringify(data);
            //test
            //window.open(pushUrl);
            showCallMessage(pushUrl).then(response => {
                console.log(response);
            });
        },
        async saveSign (message) {
            let data = {};
            data.caseOid = this.caseOid;
            data.cardNo = this.ruleForm.credentialNumber;
            data.base64Img = message;
            await saveSignRecord(data).then(response => {
                console.log(response.data);
                this.imageUrl = response.data;
                this.closeAjax();
            }).then(() => {
                this.showSignImage();
            }).catch(() => {
                this.closeAjax();
            })
        },
    }
}
