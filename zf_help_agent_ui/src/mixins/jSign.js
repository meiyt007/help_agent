/** 签名 */
import { saveSignRecord } from "@/api/zc/businessManagement/signRecord";
import { showCallMessage } from "@/api/zc/businessManagement/doubleScreenInteraction";
import { mapGetters } from "vuex";
import { FASTDFS_PATH } from "@/utils/config";
import jSign from '@/utils/jSign';
export default {
    data () {
        return {
            imageUrl: "",
            webSign: null, /** 签名空间实例化对象 */
            signCount: 0,
        }
    },
    deactivated () {
        if (this.webSign) {
            this.webSign.Destroy();
            this.webSign = null;
        }
    },
    computed: {
        ...mapGetters(["deviceMap"]),
    },
    methods: {
        /** 初始化 */
        closeAndInit () {
            this.signCount = 0;
            console.log("closeAndInit signCount: " + this.signCount);
            if (!this.webSign) {
                this.webSign = new jSign();

                this.webSign.Init((status) => {
                    if (status) {
                        console.log("WebSign connect success! ");
                        //设置笔尖宽度
                        this.setPenSize();

                        /** 开始签名 */
                        this.beginSign();

                        //取消事件
                        this.webSign.onCancel = () => {
                            console.log("Event onCancel");
                            this.endSign();
                        };

                        //清除事件
                        this.webSign.onClear = () => {
                            console.log("Event onClear");
                            this.clearSign();
                        };

                        /**
                         * 用户点击了设备上的“确定”按键，或管理员点击了签名窗口的“确定”按钮触发确定事件
                         */
                        this.webSign.onConfirm = () => {
                            if (this.signCount  ===0) {
                              console.log("Event onConfirm");
                              this.getSignBase64(500, 300, true);
                              this.signCount = 1;
                            }

                        };

                    } else {
                        console.log("WebSign connect fail! ");
                        this.$message.warning('连接签名服务失败, 请联系管理员');
                    }
                });
            } else {
                this.beginSign();
            }
        },

        setPenSize () {
            this.webSign.SetPenSize(8, 15, (status, args) => {
                console.log("SetPenSize:" + status.toString());
            })
        },

        beginSign () {
            this.webSign.BeginSign((status, args) => {
                console.log("BeginSign:" + status.toString());
            });
        },

        /** 完成一次签名 */
        endSign () {
            if (!!this.webSign) {
                this.webSign.EndSign((status, args) => {
                    console.log("EndSign:" + status.toString());
                });
            }
        },

        clearSign () {
            this.webSign.ClearSign((status) => {
                if (status) {
                    this.imageUrl = '';
                }
            });
        },

        getSignBase64 (w, h, transparent) {
            this.webSign.GetSignBase64(w, h, transparent, (status, args) => {
                console.log("GetSignBase64:" + status.toString());
                if (status) {
                    var base64 = args[0];
                    if (base64 === "")
                        return;

                    this.imageUrl = base64;
                    // this.imageUrl = "data:image/png;base64," + base64;

                    this.saveSign(this.imageUrl);

                    //完成一次签名
                    this.endSign();
                } else {
                    this.imageUrl = '';
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
                    // console.log(material);
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
            }).then(() => {
                this.showSignImage();
            })
        },
    }
}
