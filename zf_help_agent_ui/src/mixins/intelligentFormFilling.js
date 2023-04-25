import {
  getElectronicSealInfo2,
  getElectronicSealInfo3,
  getElectronicSealInfo4
} from "@/api/zc/businessManagement/windowAcceptance";
import {
  getCertificateType,
  save,
  saveRegUserInfo,
  getloginUser,
  updateFormCase
} from "@/api/zc/businessManagement/windowAcceptance";
import HardWareScan from "@/views/common/hardwareScan";
import { getOne } from "@/api/zc/sysRunConfiguration/pbpjUser";
import {
  getPjServiceSystem,
  showCallMessage,
  getCallBackInfo,
  getSpeechConfigInfo
} from "@/api/zc/businessManagement/doubleScreenInteraction";
import ZfLoading from "@/components/ZfLoading";

import "./styles/intelligentFormFilling.scss";

import { deepClone } from "@/utils/index";

import { mapGetters } from "vuex";
import Vue from "vue";
import QRCode from "qrcode";

export default {
  name: "IntelligentFormFilling",
  inheritAttrs: false,
  props: {
    serviceOid: {
      type: String,
      default: ""
    },
    cegisterType: {
      type: String,
      default: "0"
    },
    caseOid: {
      type: String,
      default: ""
    },
    caseNumber: {
      type: String,
      default: ""
    },
    loginUser: {
      type: Object,
      default: () => ({})
    },
    serviceName: String,
    rqbzDueDate: String,
    onlyMounted: Boolean
  },
  components: { HardWareScan, ZfLoading },
  data() {
    return {
      activeName: "1",
      systemType: "", // 评价服务系统
      sxSerForm: {}, // 配置表单的信息
      caseForm: {},
      reportFormList: [], // 电子表单列表
      timer: null,
      credentialName: "",
      //登录信息
      //loginUser: {},
      //是否可以是否进行小屏信息确认
      isCanConfirm: "0",
      isCanPj: "0",
      formOids: [],
      zfloading: false,
      zftext: "正在进行信息确认中",
      isClickELeTab: false, // 是否点击过点击表单
      isZc: 1, // 是否是暂存
      smallspeech: "N", //是否打开引导语
      speechAddress: "",
      getFormState: true, // 是否正在获取表单数据
      formFieldTimer: null,
      openView: false,
      viewDialogOptions: [], //预览的数据源
      title: "电子材料签章",
      ElectronicSealList: [], //电子材料签章信息
      eleShow: false, //电子材料是否展示
      imgUrl: "", //生成的二维码
      industryResultList: [],
      flowId: "" //流程id
    };
  },
  computed: {
    saveComponentIndex() {
      return this.$store.state.config.saveComponentIndex;
    },
    ...mapGetters(["xxqrTime"]),
    ...mapGetters(["deviceMap"]),

    ruleForm({ $refs }) {
      if ($refs.ruleForm) {
        return $refs.ruleForm.ruleForm;
      }
      return {};
    }
  },
  created() {
    this.queryPjSystem();
    this.getIsCan();
  },
  mounted() {
    this.$nextTick(() => {
      this.$refs.ruleForm.ruleForm.projectName = this.serviceName.substr(0, 25);
    });
    //  this.ElectronicSealInfo()
  },
  deactivated() {
    this.$emit("setRuleForm", this.ruleForm);
  },
  beforeDestroy() {
    clearInterval(this.timer);
    clearInterval(this.formFieldTimer);
    this.reportFormList.forEach(form => clearInterval(form.elecFormTimer));
  },
  watch: {
    saveComponentIndex: {
      handler(val) {
        if (val === 3) {
          // this.initCase();
          this.saveApplyCaseForm();
        }
      }
    }
  },
  methods: {
    //电子签章弹窗二维码
    async ElectronicSeal(row, index) {
      const { id } = row;
      // alert(index)
      try {
        const { applyUserName, applyUserPhone } = this.$refs.ruleForm.ruleForm;
        // alert(11111)
        console.log(this.ElectronicSealList);
        const templateName =
          this.ElectronicSealList[index].templateName + "pdf";
        const { attaUrl } = this.ElectronicSealList[index];
        console.log("ttttttttttt", attaUrl);
        // alert(attaUrl)
        const Edata = {
          name: applyUserName,
          mobile: applyUserPhone,
          fileList: [{ fileName: templateName, fileUrl: attaUrl }]
        };
        console.log("rrrrrrrr", Edata);
        // console.log('Edata',Edata)
        const { data, code, message } = await getElectronicSealInfo2(Edata);
        const { signUrl } = JSON.parse(data).data.signTasks[index];
        this.flowId = JSON.parse(data).data.flowId;
        //  console.log(this.flowId)
        //  debugger
        //  console.log(JSON.parse(data))
        //  debugger
        //  alert(signUrl)
        //  debugger
        //  console.log('data',data)
        // debugger
        // this.$refs.ruleForm.ruleForm.applyUserName
        // const Edata = [{}]
        // const { data, code, message } = await getElectronicSealInfo2(id);
        this.imgUrl = await QRCode.toDataURL(signUrl);
        const Edata3 = {
          caseOid: this.caseOid,
          materialOid: this.ElectronicSealList[index].materialOid,
          flowId: this.flowId,
          status: 2
        };
        console.log(Edata3);
        console.log("签章中", this.ElectronicSealList);
        await getElectronicSealInfo3(Edata3);
        // this.ElectronicSealList[index] = data
        console.log(this.imgUrl);
        this.eleShow = true;
        const newdata = { templateName: "hhhhhhhhhhhhhhhhh", isSeal: "已签章" };
        // alert(data)
        // Vue.set(this.ElectronicSealList,index,newdata)
        this.ElectronicSealList[index].isSeal = "签章中";
        Vue.set(this.ElectronicSealList[index], isSeal, "签章中");
        console.log("签章中", this.ElectronicSealList);
      } catch (error) {}
    },
    //电子签章弹窗预览按钮功能
    preview(row) {
      const { attaOid, addr } = row;
      if (true) {
        let item = {
          show: true,
          attaOid: attaOid
          // ext: addr?.split?.(".")?.splice?.(-1)?.[0],
        };
        this.viewDialogOptions.push(item);
      } else {
        this.$message.warning("暂无材料查看！");
      }
    },
    //关闭预览附件
    closeFileViewNew() {
      this.viewDialogOptions.pop();
    },
    async getElectronicSealStatu(row, index) {
      const { materialOid } = this.ElectronicSealList[index];
      // alert(materialOid);
      const caseOid = this.caseNumber;
      const { data } = await getElectronicSealInfo4(this.caseOid, materialOid);
      switch (1) {
        case 0:
          this.ElectronicSealList[index].isSeal = "qz";
          break;
        case 1:
          this.ElectronicSealList[index].isSeal = "qz";
          break;
        case 2:
          this.ElectronicSealList[index].isSeal = "qz";
          break;
        case 3:
          this.ElectronicSealList[index].isSeal = "qz";
          break;
      }
    },
    //获取是否进行小屏信息确认
    getIsCan2() {
      getloginUser().then(response => {
        this.loginUser = response.data;
        getOne(this.loginUser.userOid).then(response => {
          this.isCanConfirm = response.data.isDelete;
          this.isCanPj = response.data.isAble;
        });
      });
    },
    getIsCan() {
      getOne(this.loginUser.userOid).then(response => {
        this.isCanPj = response.data.isAble;
        this.isCanConfirm = response.data.isDelete;
      });
    },

    //验证表单以及办件评价信息
    async checkPbpjInfo(isStorage) {
      const valid = await this.validateForms();
      if (valid) {
        if (this.systemType == "1") {
          if (this.isCanConfirm == "1") {
            await this.getSpeechConfig();
            this.confirmScreenInteraction(isStorage);
          } else if (this.isCanConfirm == "0") {
            // alert('在这里出现对话弹框')
            this.saveApplyCaseForm(isStorage);
            // this.saveApplyCaseForm(isStorage);
          }
          //this.confirmScreenInteraction(isStorage);
        } else {
          // alert('在这里出现对话弹框')
          this.openView = true;
          this.saveApplyCaseForm(isStorage);
        }
      }
    },
    //查询引导语配置
    getSpeechConfig() {
      return getSpeechConfigInfo("SMALL_SPEECH").then(response => {
        this.smallspeech = response.data.value;
        this.speechAddress = this.deviceMap?.["AUDIO_SPEECH"];
        /*if (this.smallspeech == 'Y') {
          //查询配置地址
          getSpeechConfigInfo("AUDIO_SPEECH").then(response => {
            this.speechAddress = response.data.value;
          })
        }*/
      });
    },
    /** 初始化评价服务 */
    queryPjSystem() {
      getPjServiceSystem().then(response => {
        //1超级综窗柜台2其他
        this.systemType = response.data;
        console.log(response.data);
        this.$emit("systemType", response.data);
      });
    },
    //超级综窗柜台双屏互动信息确认
    confirmScreenInteraction(isStorage) {
      // ---NOTICE: 方便操作开发环境保留 线上环境不保留---
      if (process.env.NODE_ENV !== "production") {
        this.$confirm("你确定要进行办件信息确认吗？", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          distinguishCancelAndClose: true,
          type: "warning"
        })
          .then(() => {
            this.handleMessageConfirm(isStorage);
          })
          .catch(action => {
            if (action === "cancel") {
              //保存办件
              this.saveApplyCaseForm(isStorage);
            }
          });
      } else {
        this.handleMessageConfirm(isStorage);
      }
    },

    async handleMessageConfirm(isStorage) {
      try {
        const response = await getCertificateType(this.cegisterType);
        for (let i = 0; i < response.data.length; i++) {
          if (response.data[i].dictOid == this.ruleForm.credentialType) {
            this.credentialName = response.data[i].name;
          }
        }
      } catch {}
      let url = window.location.origin;
      let data = {};
      let content = {};
      let pushUrl = url + "/serviceHall/smallConfirmation.html";
      content.applyProjectName = this.ruleForm.projectName;
      content.applyUserType = this.ruleForm.applyUserType;
      if (this.ruleForm.applyUserType == "0") {
        content.applyUserType = "个人";
      } else {
        content.applyUserType = "法人";
      }

      content.applyUserName = this.ruleForm.applyUserName;
      content.credentialNumber = this.ruleForm.credentialNumber;
      content.applyPostCode = this.ruleForm.applyPostCode;
      content.applyUserAddress = this.ruleForm.applyUserAddress;
      content.applyUserPhone = this.ruleForm.applyUserPhone;
      if (this.ruleForm.proxyFlag == 1) {
        content.contactUserName = this.ruleForm.contactUserName;
        content.contactCredentialNumber = this.ruleForm.contactCredentialNumber;
        content.contactUserPhone = this.ruleForm.contactUserPhone;
      } else {
        content.contactUserName = "";
        content.contactCredentialNumber = "";
        content.contactUserPhone = "";
      }
      content.credentialName = this.credentialName
        ? this.credentialName
        : "身份证";
      data.userName = this.loginUser.userName;
      data.organName = this.loginUser.organName;
      data.userOid = this.caseOid;
      data.content = content;
      data.cegisterType = this.cegisterType;
      data.smallspeech = this.smallspeech; //引导语标识
      data.speechAddress = this.speechAddress;
      // 信息确认时间
      data.xxqrTime = this.xxqrTime;
      let jsonObject = JSON.stringify(data);
      pushUrl = pushUrl + "?jsonObject=" + jsonObject;
      process.env.NODE_ENV === "development" && window.open(pushUrl);
      this.zfloading = true;

      showCallMessage(pushUrl)
        .then(response => {
          console.log(response.data);
          // 开启轮训操作
          this.getCallBackInfo(isStorage);
        })
        .catch(() => {
          this.zfloading = false;
        });
    },

    /**
     * 轮询获取小屏幕的信息
     */
    getCallBackInfo(isStorage) {
      // const loading = this.$loading({
      //   lock: true,
      //   text: '信息确认中...',
      //   spinner: 'el-icon-loading',
      //   background: 'rgba(0, 0, 0, 0.7)',
      //   customClass: 'get-call-back-info--loading'
      // });
      let count = 0;
      clearInterval(this.timer);
      this.timer = setInterval(() => {
        getCallBackInfo({ userOid: this.caseOid })
          .then(({ data }) => {
            count++;
            // 点击有误
            if (data === "0") {
              // loading.close();
              this.zfloading = false;
              clearInterval(this.timer);
              this.$message({
                message: "申请人确认信息有误，请核对信息!",
                type: "error",
                duration: 5000
              });
              this.openWelcome(); //防止再次点击，推送首页
            }

            // 点击确认
            if (data === "1" || count >= this.xxqrTime) {
              // loading.close();
              this.zfloading = false;
              clearInterval(this.timer);
              //保存办件
              this.saveApplyCaseForm(isStorage);
              this.openWelcome();
            }
          })
          .catch(err => {
            // loading.close()
            this.zfloading = false;
            clearInterval(this.timer);
          });
      }, 1000);
    },

    /** 办件信息下一步保存 点击暂存关闭这个弹框 */
    async saveApplyCaseForm(isStorage) {
      let valid;
      if (isStorage) {
        // 校验基础表单
        valid = await this.$refs.ruleForm.validate();
        this.isZc = 1;
        this.handleElecFormData();
      } else {
        this.isZc = 0;
        valid = await this.validateForms();
      }
      if (valid) {
        const loading = this.$loading({
          lock: true,
          text: "正在保存表单信息",
          spinner: "el-icon-loading",
          background: "rgba(0, 0, 0, 0.7)"
        });

        // 如果电子表单保存失败 不给往下走
        const { success, formName, error } = await this.saveReportForm();
        if (!success) {
          loading.close();
          return this.$message.warning(
            `【${formName}】，${error.message || "保存失败"}`
          );
        }
        // 更新电子表单
        await this.saveCaseFormInfo(JSON.stringify(this.formOids));

        save({
          ...this.ruleForm,
          caseOid: this.caseOid,
          caseStatus: "0",
          applyUserType: this.cegisterType,
          serviceOid: this.serviceOid,
          caseNumber: this.caseNumber,
          sxServiceMaterialList: this.$attrs.sxServiceMaterialList,
          qlCaseTitleValList: this.$attrs.qlCaseTitleValList
        })
          .then(async response => {
            // alert(this.caseOid)

            loading.close();
            if (response.data && response.code === 200) {
              // this.$message.success("申请信息保存成功！");
              if (!isStorage && this.saveComponentIndex != 3) {
                this.ruleForm.caseNumber = response.data.caseNumber;
                this.ruleForm.createDate = response.data.createDate;
                this.$emit("nextStep", 4);
              } else {
                //关闭页面调用列表页面
                // this.$emit("case-close");
                this.$store.commit("setSaveComponentIndex", 0);
                this.$emit("close");
                return false;
              }
              //保存登记人信息
              this.saveRegUser();
            } else {
              this.$message.warning("申请信息保存失败！");
              this.$store.commit("setSaveComponentIndex", 0);
              loading.close();
              return false;
            }
          })
          .catch(() => {
            this.$store.commit("setSaveComponentIndex", 0);
            loading.close();
          });
      }
    },
    // 判断是否签章进入下一步
    async IFsaveApplyCaseForm(isStorage) {
      if (true) {
        this.openView = false;
        this.saveApplyCaseForm(isStorage);
      } else {
        alert("签章未完成");
      }
    },
    //超级综窗柜台双屏登录打开用户信息
    async openWelcome() {
      let url = window.location.origin;
      let data = {};
      data.userName = this.loginUser.userName;
      data.organName = this.loginUser.organName;
      data.isWorking = 1;
      let pushUrl =
        url +
        "/serviceHall/suspendedService.html?jsonObject=" +
        JSON.stringify(data);
      await showCallMessage(pushUrl).then(response => {
        console.log(response);
      });
    },

    // 保存电子表单
    async saveReportForm() {
      console.log("this.reportFormList====", this.reportFormList);
      this.formOids = [];
      for (const form of this.reportFormList) {
        const formObj = {};
        if (this.$refs[`reportForm_${form.designOid}`][0]) {
          try {
            this.$refs[
              `reportForm_${form.designOid}`
            ][0].formData.caseOid = this.caseOid;
            await this.$refs[
              `reportForm_${form.designOid}`
            ][0].saveFormDataAsync();
            formObj.designOid = form.designOid;
            formObj.authorizeKey = form.authorizeKey;
            formObj.formCode = form.formCode;
            formObj.formName = form.formName;
            this.formOids.push(formObj);
          } catch (error) {
            console.log("%c [error]:", "color:red;font-weight:700;", error);
            return {
              success: false,
              formName: form.formName,
              error
            };
          }
        } else {
          return {
            success: false,
            formName: form.formName
          };
        }
      }

      return {
        success: true
      };
    },
    // 更新电子表单
    saveCaseFormInfo(list) {
      let obj = {};
      obj.caseOid = this.caseOid;
      obj.formOids = list;
      updateFormCase(obj).then(response => {
        console.log(response.data);
      });
    },
    // 电子表单校验
    async validateReportForm() {
      const result = [];
      for (const item of this.reportFormList) {
        if (this.$refs[`reportForm_${item.designOid}`][0]) {
          let ret = await this.$refs[
            `reportForm_${item.designOid}`
          ][0].validateForm();
          if (!ret.status) {
            ret.designOid = item.designOid;
          }
          result.push(ret);
        }
      }
      return [
        result.every(item => item.status),
        result.filter(item => item.designOid)[0]
      ];
    },

    // 基础表单校验和电子表单校验
    async validateForms() {
      // 校验基础表单
      const ruleFormValid = await this.$refs.ruleForm.validate();

      if (!ruleFormValid) {
        this.activeName = "1";
        return false;
      }

      if (this.reportFormList.length === 0) return ruleFormValid;

      // 校验电子表单
      const [reportValid, designOids] = await this.validateReportForm();

      if (!(ruleFormValid && reportValid)) {
        this.$message.warning("请填写完整的表单");
      }

      if (ruleFormValid && !reportValid && designOids.designOid) {
        this.activeName =
          this.reportFormList.find(d => d.designOid === designOids.designOid)
            .name || "1";
        return false;
      }

      return ruleFormValid && reportValid;
    },

    handleTabClick(active) {
      if (active.name > 1) {
        this.handleElecFormData();
      }
    },

    handleElecFormData() {
      return;
      if (this.onlyMounted || this.isClickELeTab) return;

      // 是否点击过电子表单的tab 如果点击过了 就不会再赋值
      this.isClickELeTab = true;

      for (const form of this.reportFormList) {
        if (this.$refs[`reportForm_${form.designOid}`][0]) {
          try {
            this.$nextTick(() => {
              this.$refs[`reportForm_${form.designOid}`][0].initBaseFormData(
                deepClone(this.ruleForm)
              );
            });
          } catch (error) {}
        }
      }
    },

    // handleElecFormData () {
    //     for (const form of this.reportFormList) {
    //         let { applyUserName, credentialNumber, applyUserPhone } = this.ruleForm;
    //         let obj = {}, ruleForm = { applyUserName, credentialNumber, applyUserPhone };

    //         if (form.childFormName.length > 0) {
    //             obj = form.childFormName.reduce((prev, cur) => {
    //                 prev[cur] = ruleForm;
    //                 return prev;
    //             }, {});
    //         }

    //         obj = { ...ruleForm, ...obj };

    //         if (this.$refs[`reportForm_${form.designOid}`][0]) {
    //             try {
    //                 this.$nextTick(() => {
    //                     this.$refs[`reportForm_${form.designOid}`][0].initBaseFormData(obj);
    //                 })
    //             } catch (error) {
    //             }
    //         }
    //     }
    // },

    // 点击下一步
    async nextStep() {
      // shi fou you cai liao biao dan
      // alert(this.DesingnFromList.length)
      // this.save()
      if (this.DesingnFromList.length !== 0) {
        this.handleElecFormData();
        await this.saveReportForm();
        this.checkPbpjInfo();
      } else {
        // this.save()
        this.$emit("nextStep", 4);
      }
    },

    saveRegUser() {
      let regUser = {};
      regUser.postCode = this.ruleForm.applyPostCode;
      regUser.bussVenueDistrictOid = this.ruleForm.bussVenueDistrictOid.join(
        ","
      );
      regUser.specificLocation = this.ruleForm.specificLocation;
      saveRegUserInfo(regUser).then(res => {});
    },

    handleFormField(value, type) {
      // 如果此时表单还没渲染出来
      if (this.reportFormList.length === 0) {
        clearInterval(this.formFieldTimer);
        this.formFieldTimer = setInterval(() => {
          if (!this.getFormState) {
            this._handleFormField(value);
            clearInterval(this.formFieldTimer);
          }
        }, 200);
      } else {
        clearInterval(this.formFieldTimer);
        this._handleFormField(value);
      }
    },

    _handleFormField(value) {
      this.$nextTick(() => {
        try {
          for (const form of this.reportFormList) {
            clearInterval(form.elecFormTimer);
            if (!this.$refs[`reportForm_${form.designOid}`][0]?.newLoading) {
              this.$refs[
                `reportForm_${form.designOid}`
              ][0].oneLevelBindFormData(value, true);
            } else {
              form.elecFormTimer = setInterval(() => {
                if (
                  !this.$refs[`reportForm_${form.designOid}`][0]?.newLoading
                ) {
                  clearInterval(form.elecFormTimer);
                  this.$refs[
                    `reportForm_${form.designOid}`
                  ][0].oneLevelBindFormData(value, true);
                }
              }, 100);
            }
          }
        } catch (error) {
          console.log(
            "%c [handleFormField error]:",
            "color:red;font-weight:700;",
            error
          );
        }
      });
    }
  }
};
