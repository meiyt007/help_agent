import {
  validatePhone,
  validateTel,
  validIDCard,
  validatePostCode,
  validateEmails,
  validateNumberNoPonint,
} from "@/utils/validate";

// 表单参数
export const ruleForm = {
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
  chooseAddress: "",
  finalOpinionDesc: "",
  applyNumber: 1,
  resultDeliveryWay: "1",
  proxyFlag: "0",
  sourceType: 1,
  sourceApp: 1,
  acceptradio: "1",
  credentialNumber: "",
  applyUserName: "",
  credentialType: "",
  contactCredentialNumber: "",
  contactUserName: "",
  bussVenueDistrictOid: [],
  certWay: "",
  expressCompany: "",
};
// 表单校验
export const rules = {
  projectName: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
    {
      min: 3,
      max: 200,
      message: "长度在 3 到 200 个字符",
      trigger: "blur",
    },
  ],
  applyNumber: [
    {
      trigger: "blur",
      validator: (rule, value, callback) => {
        if (!value) {
          return callback(new Error("必填项"));
        } else if (isNaN(value)) {
          callback(new Error("请输入数字值"));
        } else {
          if (value?.toString?.()?.length > 3) {
            callback(new Error("不能超过3位数"));
          } else {
            callback();
          }
        }
      },
    },
  ],
  applyPostCode: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
    {
      validator: validatePostCode,
      trigger: "blur",
    },
  ],
  specificLocation: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  applyUserName: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  certWay: [
    {
      required: true,
      message: "请选择发证方式",
      trigger: "change",
    },
  ],
  expressCompany: [
    {
      required: true,
      message: "请选择配送公司",
      trigger: "change",
    },
  ],
  applyUserPhone: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
    {
      validator: validatePhone,
      trigger: "blur",
    },
  ],
  applyUserTel: [
    {
      validator: validateTel,
      message: "请输入正确的申请人/申请单位号码",
      trigger: "blur",
    },
  ],
  credentialType: [
    {
      required: true,
      message: "请选择证件类型",
      trigger: "change",
    },
  ],
  credentialNumber: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  legalPersonName: [
    {
      required: true,
      message: "请填写法定代表人",
      trigger: "blur",
    },
  ],
  contactUserName: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  contactCredentialNumber: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
    {
      validator: validIDCard,
      trigger: "blur",
    },
  ],
  contactUserPhone: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
    {
      validator: validatePhone,
      trigger: "blur",
    },
  ],
  contactUserTel: [
    {
      validator: validateTel,
      message: "请输入正确的固定电话",
      trigger: "blur",
    },
  ],
  contactEmail: [
    {
      validator: validateEmails,
      trigger: "blur",
    },
  ],
  addresseeName: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  addresseePostCode: [
    {
      required: true,
      message: "请输入邮政编码",
      trigger: "blur",
    },
    {
      validator: validatePostCode,
      trigger: "blur",
    },
  ],
  addresseePhone: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
    {
      validator: validatePhone,
      trigger: "blur",
    },
  ],
  addresseeTel: [
    {
      validator: validateTel,
      message: "请输入正确的收件人电话",
      trigger: "blur",
    },
  ],
  addresseeAddress: [
    {
      required: true,
      message: "请选择收件人地址",
      trigger: "change",
    },
  ],
  addresseeDetailAddress: [
    {
      required: true,
      message: "请填写详细地址",
      trigger: "blur",
    },
  ],
  chooseAddress: [
    {
      required: true,
      message: "请选择地址",
      trigger: "change",
    },
  ],
  proxyFlag: [
    {
      validator: (rule, value, callback) => {
        if (value === "" || value === undefined || value === null) {
          callback(new Error("请选择是否为代理人"));
        } else if (value != "1" && value != "0") {
          callback(new Error("请选择是否为代理人"));
        } else {
          callback();
        }
      },
      trigger: "change",
    },
  ],
  resultDeliveryWay: [
    {
      required: true,
      validator: (rule, value, callback) => {
        if (value === "" || value === undefined || value === null) {
          callback(new Error("请选择送达方式"));
        } else if (value != "1" && value != "2" && value != "3") {
          callback(new Error("请选择送达方式"));
        } else {
          callback();
        }
      },
      trigger: "change",
    },
  ],
};

export const rulesNzgs = {
  legalPersonName: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  national: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  legalPersonPost: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  prodMethod: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  legalPersonZjType: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  legalPersonNumber: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  legalPersonMobile: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  legalPersonPhone: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
    {
      validator: validatePhone,
      trigger: "blur",
    },
  ],
  legalPersonAddress: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  legalPersonEmail: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
    {
      validator: validateEmails,
      trigger: "blur",
    },
  ],
};
