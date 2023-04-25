import {
    validatePhone,
    validateTel,
    validIDCard,
    validatePostCode,
    validateEmails,
    validateNumberNoPonint
} from '@/utils/validate';

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
};
// 表单校验
export const rules = {
    projectName: [{
        required: true,
        message: "必填项",
        trigger: "blur"
    },
    {
        min: 3,
        max: 100,
        message: "长度在 3 到 100 个字符",
        trigger: "blur"
    },
    ],
    applyNumber: [{
        required: true,
        message: "必填项",
        trigger: "blur"
    },],
    applyPostCode: [{
        required: true,
        message: "必填项",
        trigger: "blur"
    },
    {
        validator: validatePostCode,
        trigger: 'blur'
    }
    ],
    bussVenueDistrictOid: [{
        required: true,
        message: "请选择业务管辖地",
        trigger: "input"
    },],
    specificLocation: [{
        required: true,
        message: "必填项",
        trigger: "blur"
    },],
    applyUserName: [{
        required: true,
        message: "必填项",
        trigger: "blur"
    },],

    applyUserPhone: [{
        required: true,
        message: "必填项",
        trigger: "blur"
    },
    {
        validator: validatePhone,
        trigger: 'blur'
    }
    ],
    applyUserTel: [{
        validator: validateTel,
        message: "请输入正确的申请人/申请单位号码",
        trigger: 'blur'
    }],
    credentialType: [{
        required: true,
        message: "请选择证件类型",
        trigger: "change"
    },],
    credentialNumber: [{
        required: true,
        message: "必填项",
        trigger: "blur"
    },],
    legalPersonName: [{
        required: true,
        message: "必填项",
        trigger: "blur"
    },],
    contactUserName: [{
        required: true,
        message: "必填项",
        trigger: "blur"
    }],
    contactCredentialNumber: [{
        required: true,
        message: "必填项",
        trigger: "blur"
    },
    {
        validator: validIDCard,
        trigger: 'blur'
    }
    ],
    contactUserPhone: [{
        required: true,
        message: "必填项",
        trigger: "blur"
    },
    {
        validator: validatePhone,
        trigger: 'blur'
    }
    ],
    contactUserTel: [{
        validator: validateTel,
        message: "请输入正确的固定电话",
        trigger: 'blur'
    }],
    contactEmail: [{
        validator: validateEmails,
        trigger: 'blur'
    }],
    addresseeName: [{
        required: true,
        message: "必填项",
        trigger: "blur"
    },],
    addresseePostCode: [{
        required: true,
        message: "必填项",
        trigger: "blur"
    },
    {
        validator: validatePostCode,
        trigger: 'blur'
    }
    ],
    addresseePhone: [{
        required: true,
        message: "必填项",
        trigger: "blur"
    },
    {
        validator: validatePhone,
        trigger: 'blur'
    }
    ],
    addresseeTel: [{
        validator: validateTel,
        message: "请输入正确的收件人电话",
        trigger: 'blur'
    }],
    addresseeAddress: [{
        required: true,
        message: "请选择收件人地址",
        trigger: "change"
    },],
    addresseeDetailAddress: [{
        required: true,
        message: "必填项",
        trigger: "blur"
    },],
};

export const rulesNzgs = {
    legalPersonName: [{
        required: true,
        message: "必填项",
        trigger: "blur"
    }],
    national: [{
        required: true,
        message: "必填项",
        trigger: "blur"
    }],
    legalPersonPost: [{
        required: true,
        message: "必填项",
        trigger: "blur"
    }],
    prodMethod: [{
        required: true,
        message: "必填项",
        trigger: "blur"
    }],
    legalPersonZjType: [{
        required: true,
        message: "必填项",
        trigger: "blur"
    }],
    legalPersonNumber: [{
        required: true,
        message: "必填项",
        trigger: "blur"
    }],
    legalPersonMobile: [{
        required: true,
        message: "必填项",
        trigger: "blur"
    }],
    legalPersonPhone: [{
        required: true,
        message: "必填项",
        trigger: "blur"
    }, {
        validator: validatePhone,
        trigger: 'blur'
    }],
    legalPersonAddress: [{
        required: true,
        message: "必填项",
        trigger: "blur"
    }],
    legalPersonEmail: [{
        required: true,
        message: "必填项",
        trigger: "blur"
    }, {
        validator: validateEmails,
        trigger: 'blur'
    }],
};