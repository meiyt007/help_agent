import applyUserOnethingList from '@/views/onething/comboManager/comboAccept/applyUserOnethingList';

import { rules } from "@/views/zc/businessManagement/windowAcceptance/components/constant.js";
import { validUnifiedCredit, validIDCard } from "@/utils/validate";

import { regionData, CodeToText, TextToCode } from "element-china-area-data";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

import { queryDistrictSimpleTree } from "@/api/sys/district";
import { findIdCard, getIdCardInfo, openIdCard } from "@/api/sys/hardwareScan";
import {
  getCertificateType,
  changeCredentialType, getRegUserInfoByUserOid
} from "@/api/zc/businessManagement/windowAcceptance";

import { getUserInfoList } from "@/api/onething/comboManager/comboAccept/applyUserList";
import { deepClone } from "@/utils/index";
import { debounce } from "@/utils/utils.js";
import { getAllBasicFieldByOid, getBasicAndFormFieldRel } from "@/api/onething/comboForm/comboformFieldRelConfig.js";
import GPYDrive from "@/api/handwareDrive.js";
import DEVEICETYPE,{
  ID_CARD_V1,
  ID_CARD_V2,
  ID_CARD_V3
} from '@/components/HiSpeedCamera/config.js'
import {
  openIdcardv3,
  closeIdcardv3,
  getdataIdcardv3
} from "@/api/handwarev3.js";
export default {
  name: "BaseForm",
  props: {
    pCegisterType: {
      type: [String, Number],
      default: "0"
    },
    rqbzDueDate: String,
    caseOid: String,
    comboDireOid: String,
    isTempComponent: Boolean,
    lastComponentId: Number
  },
  components: { Treeselect, applyUserOnethingList },
  data() {
    return {
      // 表单参数
      ruleForm: {
        applyUserPhone: "",
        applyUserTel: "",
        applyUserAddress: "",
        legalPersonName: "",
        projectName: "",
        applyPostCode: "",
        specificLocation: "",
        investProjecName: "",
        investProjectCode: "",
        projectAbstract: "",
        contactEmail: "",
        contactUserPhone: "",
        contactUserTel: "",
        contactRemark: "",
        addresseeName: "",
        addresseePostCode: "",
        addresseePhone: "",
        addresseeTel: "",
        addresseeAddress: "",
        addresseeDetailAddress: "",
        finalOpinionDesc: "",
        applyNumber: "1",
        resultDeliveryWay: "1",
        proxyFlag: 0,
        sourceType: 1,
        sourceApp: 1,
        acceptradio: "1",
        credentialNumber: "",
        applyUserName: "",
        credentialType: "",
        contactCredentialNumber: "",
        contactUserName: "",
        bussVenueDistrictOid: [],
        ifAccept: 1, // 是否受理
        rqhbTime: ""
      },
      // 表单校验
      rules: deepClone(rules),
      isShowAddress: true,
      applyCardNum: "",
      userInfoShow: false,
      isProxyPerson: false, // 是否是代理人
      // 区划
      districtOptions: [],
      certificateTypeList: [],
      provinceCityOptions: regionData,
      addressList: [],
      searchValue: "",
      areaText: "",
      // 是否提示历史信息列表信息
      isShowTipsOfUserInfo: true,
      // 证件号码副本
      credentialNumberCache: '',
      ruleFormMap: [],// 基础表单和电子表单映射关系
      isNeedWatcher: true,
      formFieldResult: {},
      enterNum: 0, // 进入的次数
      isTempComponentLater: false,
    };
  },
  computed: {
    ruleFormForWatch() {
      const form = {};
      this.ruleFormMap.forEach(item => {
        const { baseFormField } = item;
        form[baseFormField] = this.ruleForm[baseFormField];
      });
      return form;
    }
  },
  watch: {
    ruleFormForWatch: {
      deep: true,
      handler: debounce(function (value, oldValue) {
        // 暂存受理初次进入
        if (this.isTempComponent && !this.isTempComponentLater) {
          this.isTempComponentLater = true;
          return;
        }
        const form = {};
        this.ruleFormMap.forEach(item => {
          const { baseFormField, formField } = item;
          // 如果存在于初次赋值中
          if (formField in this.formFieldResult && !this.isNeedWatcher) {
            return;
          }
          if (value[baseFormField] !== oldValue[baseFormField]) {
            form[formField] = value[baseFormField];
          }
        });
        this.isNeedWatcher = true;
        if (Object.keys(form).length > 0) {
          this.$emit('setFormField', form, 'watch');
        }
      }, 500)
    }
  },
  async activated() {
    if (this.rqbzDueDate) {
      this.ruleForm.rqhbTime = this.rqbzDueDate;
    }

    // 暂存受理首次进入不请求
    if (!this.isTempComponent || this.enterNum > 0) {
      // 如果是从智审下来的不做请求
      if (this.lastComponentId !== null && this.lastComponentId > 4) return;
      // 获取电子表单字段
      await this.getAllBasicFieldByOid();
    }

    // 如果是暂存受理页面
    if (this.isTempComponent) {
      this.enterNum++;
    }

    if (!this.ruleForm.applyUserName && !this.ruleForm.credentialNumber) {
      // 判断是否缓存过身份信息
      const idCardInfo = localStorage?.idCardInfo ? JSON.parse(localStorage.idCardInfo) : false;

      if (idCardInfo) {
        // 如果是个人
        if (this.pCegisterType === '0') {
          this.ruleForm.applyUserName = idCardInfo.CNName || this.ruleForm.applyUserName;
          this.ruleForm.credentialNumber = idCardInfo.carID || this.ruleForm.credentialNumber;
        }

        if (this.pCegisterType == '1') {
          this.ruleForm.applyUserName = idCardInfo.legalUserName || this.ruleForm.applyUserName;
          this.ruleForm.credentialNumber = idCardInfo.legalIdCard || this.ruleForm.credentialNumber;
        }

        this.handleUserInfo();
      }
    }

    if (this.ruleForm.credentialNumber) {
      this.credentialNumberCache = this.ruleForm.credentialNumber;
    }
  },
  mounted() {
    // 获取基础表单和电子表单对应字段
    this.getBasicAndFormFieldRel();
  },
  methods: {
    handleBlur(isChangeType = false) {
      // 如果相等 就不处理
      if (!isChangeType && this.credentialNumberCache === this.ruleForm.credentialNumber && this.ruleForm.credentialNumber !== '') return;
      this.credentialNumberCache = this.ruleForm.credentialNumber;
      if (!this.ruleForm.credentialNumber || this.userInfoShow) return;
      this.$refs.ruleForm.validateField('credentialNumber', (validMessage) => {
        if (!validMessage) {
          this.$nextTick(() => {
            this.getUserInfoList().then(({ code, data }) => {
              if (code == 200 && data.total > 0) {
                this.userInfoShow = true;
              }
            });
          })
        }
      })
    },

    getUserInfoList() {
      return getUserInfoList({
        credentialNumber: this.ruleForm.credentialNumber,
        cegisterType: this.pCegisterType,
        pageNum: 1,
        pageSize: 10,
      })
    },

    handleUserInfo() {
      if (!this.isShowTipsOfUserInfo || this.isTempComponent) return;
      this.$refs.ruleForm.validateField('credentialNumber', (validMessage) => {
        if (!validMessage) {
          this.$nextTick(() => {
            this.getUserInfoList().then(({ code, data }) => {
              if (code == 200 && data.total > 0) {
                this.$confirm('是否打开历史申请人信息快速提取列表', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
                }).then(() => {
                  this.userInfoShow = true;
                  this.isShowTipsOfUserInfo = false;
                }).catch(() => {
                  this.isShowTipsOfUserInfo = false;
                });
              }
            });
          })
        }
      })
    },

    //调用高德地图api搜索地点
    AMapInit(cb) {
      AMap.plugin("AMap.Autocomplete", () => {
        // 实例化Autocomplete
        let autoOptions = {
          //city 限定城市，默认全国
          city: "全国"
        };
        let autoComplete = new AMap.Autocomplete(autoOptions);
        autoComplete.search(this.searchValue, (status, result) => {
          // 搜索成功时，result即是对应的匹配数据
          if (!result?.tips) return;
          result.tips.forEach(location => {
            let restaurant = {};
            let value = location.name + " - " + location.district;
            let name = location.id;
            restaurant.value = value;
            restaurant.name = name;
            this.addressList.push(restaurant);
            cb(this.addressList);
          });
        });
      });
    },
    querySearch(queryString, cb) {
      this.addressList = [];
      this.searchValue = this.areaText + queryString;
      this.AMapInit(cb);
    },
    handleSelect(item) {
      let { value } = item;
      value = value.split("-")[0];
      this.ruleForm.addresseeDetailAddress = value;
    },
    /** 送达方式 */
    changeDeliveryWay(val) {
      this.isShowAddress = val === "1";
    },

    closeUserInfoShow(userCase = {}) {
      try {
        Reflect.deleteProperty(userCase, 'bussVenueDistrictOid');
        Reflect.deleteProperty(userCase, 'rqhbTime');
        this.ruleForm = {
          ...this.ruleForm,
          ...userCase,
          acceptOpinionDesc: "",
          id: "",
          caseNumber: "",
          applyUserType: this.pCegisterType,
          comboDireOid: this.comboDireOid,
          createUserOid: "",
          acceptanceDate: "",
          concludeDate: "",
          sourceFlag: "",
          ifAccept: 1,
          caseOid: "",
        };
        Reflect.deleteProperty(this.ruleForm, 'createDate');
        Reflect.deleteProperty(this.ruleForm, 'comboCaseMaterials');
        Reflect.deleteProperty(this.ruleForm, 'comboDireMaterials');

        this.changeProxyFlag(this.ruleForm.proxyFlag);
        this.changeDeliveryWay(this.ruleForm.resultDeliveryWay);
        // this.ruleForm.bussVenueDistrictOid = this.ruleForm.bussVenueDistrictOid.split(',');
        this.getAddressTree(this.ruleForm.addresseeAddress);
        this.changeType(this.ruleForm.credentialType, true, false);
        this.$nextTick(() => {
          this.userInfoShow = false;
          this.$refs.ruleForm.validate();
        })
      } catch (error) {
        console.log('%c [error]:', 'color:red;font-weight:700;', error);
        this.$nextTick(() => {
          this.userInfoShow = false;
          this.$refs.ruleForm.validate();
        })
      }
    },

    getUserInfo(credentialNumber) {
      this.$refs.ruleForm.validateField('credentialNumber', (msg) => {
        if (!msg) {
          this.applyCardNum = credentialNumber;
          this.userInfoShow = true;
        }
      });
    },

    // 处理身份证
    handleScanCard(scanType) {
      if (DEVEICETYPE.IDCARD_CONFIG === ID_CARD_V1) {
        this.getIdcardDatav1(scanType);
      }
      if (DEVEICETYPE.IDCARD_CONFIG === ID_CARD_V2) {
        this.getIdcardDatav2(scanType);
      }
      if (DEVEICETYPE.IDCARD_CONFIG === ID_CARD_V3) {
        this.getIdcardDatav3(scanType);
      }

    },
    async getIdcardDatav3(scanType) {
      openIdcardv3().then(res => {
        if (res.StateCode == 0 || res.StateCode == -1) {
          return this.getIdcardDataByv3(scanType); //获取身份证信息
        } else {
          return this.$message.warning("请确认设备连接是否正常");
        }
      })
    },
    async getIdcardDataByv3(scanType) {
      getdataIdcardv3().then(resData => {
        if (resData.state == 'sucess' && resData.StateCode == 0) {
          let resInfo = JSON.parse(resData.data)
          let res = {
            CNName: resInfo.name,
            sex: resInfo.sex,
            carID: resInfo.number,
            nation: resInfo.nation,
            address: resInfo.department,
            bron: resInfo.birthday,
            imgData: 'data:image/jpeg;base64,' + resInfo.Image
          }
          if (scanType == 0) {
            this.ruleForm.applyUserName = res.CNName.trim();
            this.ruleForm.credentialNumber = res.carID;
            // this.ruleForm.credentialType = "c8d2fac3baf14697a476cbf5eb8d41c5";
            this.ruleForm.credentialType = this.certificateTypeList.find(item => item.code === 'SFZ').dictOid;

            this.isShowTipsOfUserInfo = true;
            this.handleUserInfo();
          } else {
            this.ruleForm.contactCredentialNumber = res.carID;
            this.ruleForm.contactUserName = res.CNName.trim();
          }
          this.$nextTick(() => {
            this.$refs.ruleForm.validate();
          });
          // 缓存身份证信息 方便后面 人证对比 使用
          // 如果是代理人 就不保存
          if (this.ruleForm.proxyFlag == '1') return;
          // 判断是否缓存过身份信息
          const idCardInfo = localStorage?.idCardInfo ? JSON.parse(localStorage.idCardInfo) : {};
          localStorage.setItem('idCardInfo', JSON.stringify({ ...idCardInfo, ...res }));

        }
        else if (resData.StateCode == -1) {
          //0:成功-1:设备没有打开-2:寻卡失败-3选卡失败-4读卡失败
          //判断有没有设备
          //打开身份证
          openIdcardv3().then(res => {
            if (res.StateCode == 0 || res.StateCode == -1) {
              return this.getIdcardDataByv3(scanType); //获取身份证信息
            }
          });
        } else {
          resData.tips && this.$message.error(resData.tips);
          return false;
        }
      })

    },
    async getIdcardDatav1(scanType) {
      openIdCard().then(response => {
        //打开设备
        if (response.state == "sucess") {
          return this.getIdcardDataByv1(scanType); //重新获取身份证信息
        }
        //查找设备
        findIdCard().then(resFind => {
          if ((resFind.state = "sucess" && resFind.StateCode == 0)) {
            return this.getIdcardDataByv1(scanType);
          }
          this.$message.error("无法找到设备");
          return false;
        });
      });
    },
    async getIdcardDataByv1(scanType) {
      getIdCardInfo().then(response => {
        let res = response;
        if (!res) {
          this.$message.error("请检查身份证设备或连接是否正常！");
          return false;
        }
        if (res.state == "sucess") {
          if (scanType == 0) {
            this.ruleForm.applyUserName = res.CNName.trim();
            this.ruleForm.credentialNumber = res.carID;
            // this.ruleForm.credentialType = "c8d2fac3baf14697a476cbf5eb8d41c5";
            this.ruleForm.credentialType = this.certificateTypeList.find(item => item.code === 'SFZ').dictOid;

            this.isShowTipsOfUserInfo = true;
            this.handleUserInfo();
          } else {
            this.ruleForm.contactCredentialNumber = res.carID;
            this.ruleForm.contactUserName = res.CNName.trim();
          }
          this.$nextTick(() => {
            this.$refs.ruleForm.validate();
          });
          // 缓存身份证信息 方便后面 人证对比 使用
          // 如果是代理人 就不保存
          if (this.ruleForm.proxyFlag == '1') return;
          // 判断是否缓存过身份信息
          const idCardInfo = localStorage?.idCardInfo ? JSON.parse(localStorage.idCardInfo) : {};
          localStorage.setItem('idCardInfo', JSON.stringify({ ...idCardInfo, ...res }));
        } else if (res.StateCode == -1) {
          //0:成功-1:设备没有打开-2:寻卡失败-3选卡失败-4读卡失败
          //判断有没有设备
          //打开身份证
          openIdCard().then(response => {
            if ((response.state = "sucess")) {
              this.getIdcardDataByv1(scanType); //重新获取身份证信息
            }
          });
        } else {
          res.tips && this.$message.error(res.tips);
          return false;
        }
      });
    },

    async getIdcardDatav2(scanType) {
      const resData = await GPYDrive.readCardInfo();
      if (!resData || resData.code !== 200) {
        this.$message.error("读取身份证信息失败");
        return false;
      }
      let res = {
        CNName: resData.data.name,
        sex: resData.data.sex,
        carID: resData.data.number,
        nation: resData.data.nation,
        address: resData.data.address,
        bron: resData.data.birthday,
        imgData: 'data:image/jpeg;base64,' + resData.data.image
      }
      if (scanType == 0) {
        this.ruleForm.applyUserName = res.CNName.trim();
        this.ruleForm.credentialNumber = res.carID;
        // this.ruleForm.credentialType = "c8d2fac3baf14697a476cbf5eb8d41c5";
        this.ruleForm.credentialType = this.certificateTypeList.find(item => item.code === 'SFZ').dictOid;

        this.isShowTipsOfUserInfo = true;
        this.handleUserInfo();
      } else {
        this.ruleForm.contactCredentialNumber = res.carID;
        this.ruleForm.contactUserName = res.CNName.trim();
      }
      this.$nextTick(() => {
        this.$refs.ruleForm.validate();
      });
      // 缓存身份证信息 方便后面 人证对比 使用
      // 如果是代理人 就不保存
      if (this.ruleForm.proxyFlag == '1') return;
      // 判断是否缓存过身份信息
      const idCardInfo = localStorage?.idCardInfo ? JSON.parse(localStorage.idCardInfo) : {};
      localStorage.setItem('idCardInfo', JSON.stringify({ ...idCardInfo, ...res }));
    },

    /** 是否代理人 */
    changeProxyFlag(val) {
      this.isProxyPerson = val == "1";
    },

    /** 获取证件类型 */
    getSelectCertificateType() {
      getCertificateType(this.pCegisterType).then(response => {
        this.certificateTypeList = response.data || [];

        if (this.isTempComponent) return;

        if (this.cegisterType === "1") {
          if (!this.ruleForm.credentialType) {
            this.ruleForm.credentialType = this.certificateTypeList.find(
              // 身份证
              item => item.code === "402881945c147ae2015c156f5272000d"
            ).dictOid;
          }
        } else {
          if (this.ruleForm.credentialType) {
            this.ruleForm.credentialType = this.certificateTypeList.find(
              // 统一信用代码
              item => item.code === "402881945c147ae2015c1575b1980012"
            ).dictOid;
          }
        }

        this.changeType(this.ruleForm.credentialType, false, false);
      });
    },

    /** 获取区划树 */
    async getDistrictTree(districtOid) {
      const { data } = await queryDistrictSimpleTree(districtOid);
      this.districtOptions = data;
      return Promise.resolve(data);
    },

    changeValue() {
      this.$refs.ruleForm.validateField("bussVenueDistrictOid");
    },

    /** 改变证件类型
     * @param {boolean} valid 是否触发校验
     * @param {boolean} getUserInfo 是否触发获取申请人信息方法
    */
    changeType(item, valid = true, getUserInfo = true) {
      changeCredentialType(item).then(response => {
        const cardType = response.data.code;
        let type = {};
        this.rules.credentialNumber.forEach((item, index) => {
          if (cardType == "SFZ") {
            type.validator = validIDCard;
            type.trigger = "blur";
            this.rules.credentialNumber[1] = type;
          } else if (cardType == "XYDMZ") {
            type.validator = validUnifiedCredit;
            type.trigger = "blur";
            this.rules.credentialNumber[1] = type;
          } else {
            if (index == 1) {
              this.rules.credentialNumber.splice(
                this.rules.credentialNumber.indexOf(item),
                1
              );
            }
          }
        });

        // 触发校验规则
        valid && this.$refs.ruleForm.validateField('credentialNumber');

        getUserInfo && this.handleBlur(true);
      });
    },

    /** 提取送达信息 */
    getDeliveryInfo() {
      this.ruleForm.addresseeName = this.ruleForm.applyUserName;
      this.ruleForm.addresseePostCode = this.ruleForm.applyPostCode;
      this.ruleForm.addresseePhone = this.ruleForm.applyUserPhone;
      this.ruleForm.addresseeTel = this.ruleForm.applyUserTel;
      this.$refs.ruleForm.validate();
    },

    /** 获取区划 */
    getDistrictTreeNew(bussVenueDistrictOids) {
      if (!!bussVenueDistrictOids) {
        this.ruleForm.bussVenueDistrictOid = bussVenueDistrictOids
          .split(",")
          .filter(oid => oid)
          .map(oid => oid);
      }
    },

    getAddressTree(addresseeAddress) {
      if (!!addresseeAddress) {
        try {
          let codeArray = JSON.parse(addresseeAddress)
          this.ruleForm.addresseeAddress = codeArray;
          this.areaText = this.getCodeToText(codeArray);
        } catch (error) { }
      }
    },

    //获取选择的地址中文全称
    handleChange(value) {
      this.areaText = this.getCodeToText(value);
    },

    getCodeToText(codeArray) {
      if (null === codeArray) {
        return null;
      }

      let area = "";
      switch (codeArray.length) {
        case 1:
          area += CodeToText[codeArray[0]];
          break;
        case 2:
          area += CodeToText[codeArray[0]] + "" + CodeToText[codeArray[1]];
          break;
        case 3:
          area +=
            CodeToText[codeArray[0]] +
            "" +
            CodeToText[codeArray[1]] +
            "" +
            CodeToText[codeArray[2]];
          break;
        default:
          break;
      }
      return area;
    },

    // 基础表单校验
    async validate() {
      try {
        return await this.$refs.ruleForm.validate();
      } catch (error) {
        return false;
      }
    },
    getregUserInfo() {
      getRegUserInfoByUserOid().then(respon => {
        if (respon.data) {
          this.ruleForm.specificLocation = respon.data.specificLocation;
          this.ruleForm.applyPostCode = respon.data.postCode;
          this.getDistrictTreeNew(respon.data.bussVenueDistrictOid);
        }
      })
    },
    // 基础表单电子表单ocr/证照信息赋值
    getAllBasicFieldByOid() {
      return getAllBasicFieldByOid(this.caseOid).then(({ code, data }) => {
        if (code !== 200) return;
        const { basicFieldResult, formFieldResult } = data;
        this.formFieldResult = formFieldResult;
        // 电子表单
        if (Object.keys(formFieldResult).length > 0) {
          // 如果有值 则开始不监听 初始不监听
          this.isNeedWatcher = false;
          this.$emit('setFormField', formFieldResult, 'init');
        }
        // 基础表单
        if (Object.keys(basicFieldResult).length > 0) {
          this.ruleForm = { ...this.ruleForm, ...basicFieldResult };
          basicFieldResult.proxyFlag && (this.ruleForm.proxyFlag = Number(basicFieldResult.proxyFlag));
          this.$nextTick(() => {
            if (this.ruleForm.credentialNumber) {
              this.handleUserInfo();
            }

            if (basicFieldResult.resultDeliveryWay) {
              this.changeDeliveryWay(this.ruleForm.resultDeliveryWay);
            }

            if (basicFieldResult.proxyFlag) {
              this.changeProxyFlag(this.ruleForm.proxyFlag);
            }
          })
        }

      })
    },
    getBasicAndFormFieldRel() {
      return getBasicAndFormFieldRel(this.comboDireOid).then(({ code, data }) => {
        if (code !== 200) return;
        this.ruleFormMap = data || [];
      })
    },
  }
};
