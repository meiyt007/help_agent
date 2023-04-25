/** * 窗口受理管理 * @author: wangxl * @date: 2020-12-7 */
<template>
  <div class="app-container">
    <div class="process-box" id="print">
      <!-- 第五步 -->
      <div class="step-content step-last" v-if="show_4">
        <h3 class="title"><i class="el-icon-s-grid"></i>申请者相关信息</h3>
        <el-form
          :model="acceptForm"
          :rules="rules"
          ref="acceptForm"
          label-width="0px"
          class="demo-acceptForm"
          :label-position="labelPosition"
        >
          <table
            cellspacing="0"
            cellpadding="0"
            border="0"
            class="el-table__body"
          >
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <el-input v-show="false" v-model="acceptForm.caseOid" />
            <!--<tr>
              <td><b>审批部门：</b></td>
              <td colspan="3">{{loginUser.organName}}</td>
            </tr>-->
            <tr>
              <td><b>是否受理：</b></td>
              <td colspan="3">
                <el-form-item prop="ifAccept">
                  <el-radio-group v-model="acceptForm.ifAccept">
                    <el-radio :label="1">受理通过</el-radio>
                    <el-radio :label="2">不予受理</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><b>意见说明：</b></td>
              <td colspan="3">
                <el-form-item prop="acceptOpinionDesc">
                  <el-col :span="24">
                    <el-input
                      type="textarea"
                      v-model.trim="acceptForm.acceptOpinionDesc"
                      name="acceptOpinionDesc"
                      maxlength="200"
                      show-word-limit
                    ></el-input>
                  </el-col>
                </el-form-item>
              </td>
            </tr>
          </table>

          <div class="btn-wrap">
            <div class="btn-list mt10">
              <el-button
                type="primary"
                icon="el-icon-circle-check"
                @click="next(3, -1)"
                >上一步</el-button
              >
              <el-button
                type="primary"
                icon="el-icon-video-pause"
                @click="caseAccpet"
                >受理</el-button
              >
            </div>
          </div>
        </el-form>
      </div>
      <!-- 右侧边栏 -->
      <div class="rightSideBar" v-if="rightShow">
        <div class="sideItem">
          <i class="el-icon-edit-outline"></i>
          <div class="sideText">查看事项</div>
        </div>
        <div class="sideItem">
          <i class="el-icon-c-scale-to-original"></i>
          <div class="sideText">暂存登记</div>
        </div>
      </div>
      <!--表单嵌套页面-->
      <el-dialog
        title="电子表单"
        :visible.sync="iframeState"
        width="80%"
        height="100%"
        v-if="iframeState"
        :modal-append-to-body="true"
        append-to-body
      >
        <iframe-url
          :outLink="iframeUrl"
          @closeIframe="closeIframe"
        ></iframe-url>
      </el-dialog>

      <el-dialog
        title="电子表单预览"
        :visible.sync="iframVieweState"
        width="80%"
        v-if="iframVieweState"
        :modal-append-to-body="true"
        append-to-body
        height="100%"
      >
        <iframe-url-view
          :outLink="iframeUrlView"
          @closeIframeView="closeIframeView"
        ></iframe-url-view>
      </el-dialog>
    </div>
    <!--</el-dialog>-->
    <!--引入文件的预览弹出层-->
    <el-dialog
      v-dialog-drag
      title="文件预览"
      :visible.sync="view.show"
      v-for="view in viewDialogOptions"
      width="60%"
      append-to-body
    >
      <combo-case-file-view
        :attaOid="view.attaOid"
        @father-click="closeFileView"
      ></combo-case-file-view>
    </el-dialog>

    <el-dialog
      v-dialog-drag
      :visible.sync="item.show"
      v-for="item in materialComparisonOptions"
      @close="outerVisible"
      :title="item.title"
      width="80%"
      height="700px"
      append-to-body
    >
      <!--  <iframe :src=fileViewurlList  rameborder="0" width="100%" height="600px"  @father-click="closeFileView"  ></iframe>-->
      <el-scrollbar style="height:650px;">
        <industry-material-comparison
          :sampleInfoOid="item.sampleInfoOid"
          :comboDirectoryOid="item.comboDirectoryOid"
          :materiaOid="item.materiaOid"
          :caseOid="item.caseOid"
          :fileViewurl="item.fileViewurl"
          :title="item.title"
          @father-click="openTempletePic"
        >
        </industry-material-comparison>
      </el-scrollbar>
    </el-dialog>
  </div>
</template>
<script>
import {
  getCaseSituationList,
  getCertificateType,
  saveIndustryCase,
  uploadCaseMaterialFile,
  saveCaseMaterialAtta,
  saveCaseAccpet,
  pushPbpjCheckLogin,
  regIndustryCaseInfo
} from "@/api/onelicence/industryManager/industryCaseAccept/updateIndustryCase";
import IframeUrl from "@/views/iframe/formIndex";
import IframeUrlView from "@/views/iframe/formIndexView";
import { queryDistrictSimpleTree } from "@/api/sys/district";
import { delFile } from "@/api/onelicence/industryManager/industryCaseAccept/industryCaseAtta";
import {
  validatePhone,
  validIDCard,
  validatePostCode,
  validateEmails
} from "@/utils/validate";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { VueCropper } from "vue-cropper";
import { uploadFileByPaths } from "@/api/zc/businessManagement/fileUpload";
import industryMaterialComparison from "@/views/onelicence/clzs/materialCheckKeyManage/industryMaterialComparison";
import {
  saveOrUpdateCaseForm,
  selectBySxSerForm,
  caseFormByCase
} from "@/api/zc/businessManagement/formConfig";

import industryCaseFileView from "@/views/onelicence/industryManager/industryCaseAccept/industryCaseFileView";
export default {
  inheritAttrs: false,
  components: {
    Treeselect,
    VueCropper,
    industryCaseFileView,
    industryMaterialComparison,
    IframeUrl,
    IframeUrlView
  },
  name: "updateCaseInfo",
  //定义获取父类传过来值的格式
  props: ["caseOid"],
  data() {
    return {
      i: 0,
      num: 0,
      num1: 1,
      num2: 2,
      show_0: true,
      show_1: false,
      show_2: false,
      show_3: false,
      show_4: false,
      rightShow: true,
      address_show: true,
      proxy_show: false,
      compare_show: false,
      show_upload: [],
      show_scan: [],
      scan_item: {},
      //材料比对页面
      materialComparisonOptions: [],
      radio: "",
      radio1: [],
      radio2: "1",
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 事项情形数据
      situationList: [],
      // 事项情形与选项关系数据
      direSituationOptionTitles: [],
      // 事项选项值数据
      situationOptionVals: [],
      viewDialogOptions: [],
      //查询办件主键
      id: "",
      //查询办件业务主键
      caseOid: "",
      //办件状态
      caseStatus: "",
      //材料办件业务主键
      caseMaterialOids: [],
      //登记类型 法人1 自然人0
      cegisterType: null,
      //证件类型
      certificateTypeList: [],
      // 机构
      listOrganOptions: [],
      // 区划
      districtOptions: [],
      //事项材料
      comboDireMaterials: [],
      //办件材料
      industryCaseMaterials: [],
      mateOptRels: [],
      materialOptList: [],
      formConfig_show: false, //标识表单是否显示
      iframeState: false,
      iframVieweState: false,
      iframeUrl: {}, //表单地址
      iframeUrlView: {}, //查看
      sxSerFormList: [], //多个表单信息
      caseForm: [],
      tempFormDataId: [], //临时存放表单填报的返回值
      indexForm: 0,

      situationOid: null,
      situationName: "默认自定情形",
      fileList: [],
      showFileList: false,
      accept: {
        type: String,
        default: ".jpg,.jpeg,.png,.pdf,.PDF,.doc,.docx,.DOC,.DOCX"
      },
      //附件集合
      attaList: [],
      //材料业务主键
      materialOid: null,
      //材料与上传成功的附件集合
      materialAttaList: [],
      //材料与上传成功的附件集合
      materialAttaOidList: [],
      //收取材料方式
      collectionTypeList: [],
      // 弹出层标题
      title: "办件修改",
      // 显示弹出层
      openInit: true,
      // 查看显示弹出层
      //openView: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      queryForm: {},
      // 表单参数
      ruleForm: {
        resultDeliveryWay: "1",
        //deliverFlag: 1,
        proxyFlag: 0
        //sourceApp: 1,
      },
      comboDireForm: {},
      optionForm: {},
      formData: {},
      materialForm: {},
      acceptForm: { ifAccept: 1 },
      labelPosition: "top",
      comboDireSituationOid: "",
      checkList: [],
      stepData: [
        { index: "0", title: "情形选择" },
        { index: "1", title: "材料核验" },
        { index: "2", title: "信息登记" },
        { index: "3", title: "收取材料" },
        { index: "4", title: "进入受理" }
      ],
      selectData: [],
      // 表单校验
      rules: {
        projectName: [
          { required: true, message: "必填项", trigger: "blur" },
          {
            min: 3,
            max: 100,
            message: "长度在 3 到 100 个字符",
            trigger: "blur"
          }
        ],
        applyPostCode: [
          { required: true, message: "必填项", trigger: "blur" },
          { validator: validatePostCode, trigger: "blur" }
        ],
        bussVenueDistrictOid: [
          { required: true, message: "请选择业务管辖地", trigger: "blur" }
        ],
        specificLocation: [
          { required: true, message: "必填项", trigger: "blur" }
        ],
        applyUserName: [{ required: true, message: "必填项", trigger: "blur" }],
        applyUserPhone: [
          { required: true, message: "必填项", trigger: "blur" },
          { validator: validatePhone, trigger: "blur" }
        ],
        applyUserTel: [
          { required: true, message: "必填项", trigger: "blur" },
          { validator: validatePhone, trigger: "blur" }
        ],
        credentialType: [
          { required: true, message: "请选择证件类型", trigger: "change" }
        ],
        credentialNumber: [
          { required: true, message: "必填项", trigger: "blur" },
          { validator: validIDCard, trigger: "blur" }
        ],
        legalPersonName: [
          { required: true, message: "必填项", trigger: "blur" }
        ],
        contactUserName: [
          { required: true, message: "必填项", trigger: "blur" },
          {
            min: 3,
            max: 100,
            message: "长度在 3 到 100 个字符",
            trigger: "blur"
          }
        ],
        contactCredentialNumber: [
          { required: true, message: "必填项", trigger: "blur" },
          { validator: validIDCard, trigger: "blur" }
        ],
        contactUserPhone: [
          { required: true, message: "必填项", trigger: "blur" },
          { validator: validatePhone, trigger: "blur" }
        ],
        contactUserTel: [{ validator: validatePhone, trigger: "blur" }],
        contactEmail: [{ validator: validateEmails, trigger: "blur" }],
        addresseeName: [{ required: true, message: "必填项", trigger: "blur" }],
        addresseePostCode: [
          { required: true, message: "必填项", trigger: "blur" },
          { validator: validatePostCode, trigger: "blur" }
        ],
        addresseePhone: [
          { required: true, message: "必填项", trigger: "blur" },
          { validator: validatePhone, trigger: "blur" }
        ],
        addresseeTel: [{ validator: validatePhone, trigger: "blur" }],
        addresseeAddress: [
          { required: true, message: "必填项", trigger: "blur" }
        ],
        addresseeDetailAddress: [
          { required: true, message: "必填项", trigger: "blur" }
        ]
      }
    };
  },
  watch: {},
  computed: {
    reversedIfCharge: function() {
      if (this.comboDireForm.ifCharge) {
        if (this.comboDireForm.ifCharge == 0) {
          return "否";
        } else if (this.comboDireForm.ifCharge == 1) {
          return "是";
        }
      }
      return "";
    }
  },
  created() {
    //一件事情形获取
    this.getIndustryCaseSituationList();
  },
  methods: {
    next(index, count) {
      if (count > 0) {
        this.i = index;
        if (index == 1) {
          this.show_0 = false;
          this.show_1 = true;
        }
        if (index == 2) {
          this.getCheckbox(index);
        }
        if (index == 3) {
          this.show_2 = false;
          this.show_3 = true;
          this.show_upload = [];
          this.show_scan = [];
          //保存办件信息
          this.saveIndustryCaseForm(index);
        }
        if (index == 4) {
          this.show_3 = false;
          this.show_4 = true;
          this.saveMaterialAtta(index);
        }
      } else {
        this.i = index;
        if (index == 0) {
          this.show_0 = true;
          this.show_1 = false;
        }
        if (index == 1) {
          this.show_1 = true;
          this.show_2 = false;
        }
        if (index == 2) {
          this.getDistrictTree("", this.bussVenueDistrictOids);
          this.show_2 = true;
          this.show_3 = false;
        }
        if (index == 3) {
          this.show_3 = true;
          this.show_4 = false;
        }
      }
    },

    /** 获取区划树 */
    getDistrictTree(districtOid, bussVenueDistrictOids) {
      let _that = this;
      let oids = [];
      if (bussVenueDistrictOids) {
        let districtOids =
          bussVenueDistrictOids != "" ? bussVenueDistrictOids.split(",") : [];
        for (let oid of districtOids) {
          if (oid != "") {
            oids.push(oid);
          }
        }
        _that.ruleForm.bussVenueDistrictOid = oids;
      }
      queryDistrictSimpleTree(districtOid).then(response => {
        _that.districtOptions = response.data;
      });
    },
    /** 勾选核验材料选项 */
    checkedBox(val) {
      this.radio = val;
    },
    /** 核验材料勾选 */
    getCheckbox(index) {
      if (this.radio) {
        this.i = index;
        this.show_1 = false;
        this.show_2 = true;
        let tempForm = [];
        selectBySxSerForm(this.comboDireOid).then(response => {
          if (response.data) {
            this.formConfig_show = true;
            this.sxSerFormList = response.data;
            //this.sxSerForm.regOid=this.caseOid;
            //查询办件表单是否存在
            caseFormByCase(this.comboDireOid, this.caseOid).then(response => {
              if (response.data) {
                tempForm = response.data;
              }
              if (tempForm) {
                //循环事项表单配置添加表单关联信息
                this.sxSerFormList.forEach((items, i) => {
                  tempForm.forEach((childItem, j) => {
                    if (childItem.serFormOid == items.oid) {
                      this.caseForm[i] = childItem;
                      this.tempFormDataId[i] = childItem.formDataId;
                    }
                  });
                });
                this.$forceUpdate();
              }
            });
          }
        });
      } else {
        this.msgWarning("请勾选我已核验上述材料并确定齐全！");
        return false;
      }
    },
    /** 获取证件类型 */
    getSelectCertificateType() {
      getCertificateType(this.cegisterType).then(response => {
        this.certificateTypeList = response.data;
      });
    },
    /** 事项情形获取 */
    getIndustryCaseSituationList() {
      //查询事项情形
      getCaseSituationList(this.caseOid).then(response => {
        // this.situationName = "默认自定情形";
        let situation = response.data.comboSituations;
        if (situation) {
          this.comboDireSituationOid = situation.oid;
          this.getSituationOpinion(situation);
        }
        let optionTitles = response.data.comboOptionTitleList;
        if (optionTitles) {
          optionTitles.forEach(title => {
            if (title.comboOptionVals) {
              title.comboOptionVals.forEach(val => {
                this.checkList.push(val.valOid);
              });
            }
          });
        }

        this.getSituationOpinionTitle(optionTitles);
        //给办件相关赋值 data.industryCase
        this.comboDireOid = response.data.industryCase.comboDireOid;
        this.cegisterType = response.data.industryCase.applyUserType;
        //获取证照类型
        this.getSelectCertificateType();
        //获取一件事目录信息
        this.comboDireForm = response.data.comboDirectory;
        this.ruleForm = response.data.industryCase;
        //获取一件事材料
        this.comboDireMaterials = response.data.directoryMaterials;
        //办件信息
        this.id = response.data.industryCase.id;
        this.caseOid = response.data.industryCase.caseOid;
        this.caseNumber = response.data.industryCase.caseNumber;
        this.caseStatus = response.data.industryCase.caseStatus;
        //办件材料信息
        this.industryCaseMaterials =
          response.data.industryCase.industryCaseMaterials;
        this.$nextTick(() => {
          this.materialAttaList = [];
          this.industryCaseMaterials.forEach(mat => {
            let bb = new Object();
            bb.collectionType = mat.collectionType;
            bb.caseMaterialOid = mat.caseMaterialOid;
            bb.collectionNumber = mat.collectionNumber;
            this.collectionTypeList.push(bb);
            mat.attaList.forEach(att => {
              let at = new Object();
              at.caseMaterialOid = mat.caseMaterialOid;
              at.attaOid = att.oid;
              this.materialAttaList.push(at);
            });
            this.radio1[mat.caseMaterialOid] = mat.collectionType;
          });
        });
        this.changeDeliveryWay(response.data.industryCase.resultDeliveryWay);
        this.changeProxyFlag(response.data.industryCase.proxyFlag);
        this.getDistrictTree(
          "",
          response.data.industryCase.bussVenueDistrictOid
        );
      });
    },
    /** 填充选项 */
    getSituationOpinion(situation) {
      if (situation) {
        // situations.forEach((item, key) => {
        // let situation = {};
        // situation.index = key + 1;
        //situation.title = item.situationName;
        //situation.oid = item.situationOid;
        this.selectData.push(situation);
        // });
      }
    },
    /** 填充标题和选项值 */
    getSituationOpinionTitle(optionTitles) {
      if (optionTitles) {
        optionTitles.forEach(optionTitle => {
          let titleValues = {};
          titleValues.titleName = optionTitle.name;
          titleValues.titleOid = optionTitle.oid;
          titleValues.comboOptionVals = optionTitle.comboOptionVals;
          this.direSituationOptionTitles.push(titleValues);
        });
      }
    },
    /** 办件信息下一步保存 */
    saveIndustryCaseForm(index) {
      this.$refs["ruleForm"].validate(valid => {
        if (valid) {
          if (
            undefined != this.ruleForm.bussVenueDistrictOid &&
            this.ruleForm.bussVenueDistrictOid.length > 0
          ) {
            let oid = "";
            for (
              let i = 0;
              i < this.ruleForm.bussVenueDistrictOid.length;
              i++
            ) {
              oid += this.ruleForm.bussVenueDistrictOid[i] + ",";
            }
            this.ruleForm.bussVenueDistrictOid = oid;
          }
          this.ruleForm.id = this.id;
          this.ruleForm.caseOid = this.caseOid;
          this.ruleForm.caseNumber = this.caseNumber;
          this.ruleForm.caseStatus = this.caseStatus;
          this.ruleForm.applyUserType = this.cegisterType;
          this.ruleForm.comboDireOid = this.comboDireOid;
          this.ruleForm.comboDireMaterials = this.comboDireMaterials;
          this.ruleForm.updateFlag = "Y";
          this.ruleForm.pbpjCaseOkUrl = process.env.VUE_APP_ZC_API_PAGE;
          //选项值
          this.ruleForm.valList = this.checkList;
          //情形
          this.ruleForm.comboDireSituationOid = this.comboDireSituationOid;
          //选项值
          this.ruleForm.valList = this.checkList;
          this.confirmPbpj(this.ruleForm);
          saveIndustryCase(this.ruleForm).then(response => {
            if (response.data != "") {
              this.caseOid = response.data.caseOid;
              this.id = response.data.id;
              this.caseNumber = response.data.caseNumber;
              this.industryCaseMaterials = response.data.industryCaseMaterials;
              this.bussVenueDistrictOids = response.data.bussVenueDistrictOid;
              this.getDistrictTree("", response.data.bussVenueDistrictOid);
              //this.ruleForm = response.data;
              this.saveCaseForm(); //保存办件表单信息
              this.msgSuccess("保存办件成功！");
              if (index != "") {
                this.i = index;
                this.show_2 = false;
                this.show_3 = true;
              }
            } else {
              this.msgError("保存办件失败！");
              return false;
            }
          });
        }
      });

      this.$nextTick(() => {
        this.industryCaseMaterials.forEach(ma => {
          if (ma.collectionType == "2") {
            this.show_upload[ma.caseMaterialOid] = true;
          } else if (ma.collectionType == "3") {
            this.show_scan[ma.caseMaterialOid] = true;
          } else {
            this.show_upload[ma.caseMaterialOid] = false;
            this.show_scan[ma.caseMaterialOid] = false;
          }
        });
      });
    },
    /** 保存材料附件信息 */
    saveMaterialAtta(index) {
      if (this.fileList.length == 0 && this.materialAttaList.length == 0) {
        this.msgError("请至少一个上传附件");
        return false;
      } else {
        let dataForm = {};
        dataForm.caseOid = this.caseOid;
        // dataForm. caseMaterialOid= this.materialOid;
        dataForm.attList = this.materialAttaList;
        dataForm.collectionTypeVos = this.collectionTypeList;
        saveCaseMaterialAtta(dataForm).then(response => {
          if (response.code === 200) {
            this.msgSuccess("保存材料附件成功！");
            this.i = index;
            this.show_3 = false;
            this.show_4 = true;
          }
        });
      }
    },
    /** 办件受理 */
    caseAccpet() {
      if (this.caseOid != "") {
        this.acceptForm.caseOid = this.caseOid;
        saveCaseAccpet(this.acceptForm).then(response => {
          if (response.data != "") {
            this.msgSuccess("办件进入受理成功！");
            this.$emit("case-close");
          }
        });
      } else {
        this.msgError("办件信息未保存，请先保存！");
        return false;
      }
    },
    /** 送达方式 */
    changeDeliveryWay(val) {
      this.address_show = val === "1" ? true : false;
    },
    /** 是否代理人 */
    changeProxyFlag(val) {
      this.proxy_show = val === 1 ? true : false;
    },
    /** 材料形式 */
    chooseCollectionType(val, item, index) {
      let bb = new Object();
      if (val == "2") {
        this.show_upload[index] = true;
        this.show_scan[index] = false;
      } else if (val == "3") {
        this.show_scan[index] = true;
        this.show_upload[index] = false;
      } else {
        this.show_upload[index] = false;
        this.show_scan[index] = false;
      }
      bb.collectionType = val;
      bb.caseMaterialOid = item.caseMaterialOid;
      bb.collectionNumber = item.collectionNumber;
      if (this.collectionTypeList) {
        this.collectionTypeList.forEach(da => {
          if (bb.caseMaterialOid === da.caseMaterialOid) {
            da.collectionType = val;
            da.collectionNumber = item.collectionNumber;
          } else {
            this.collectionTypeList.push(bb);
          }
        });
      } else {
        this.collectionTypeList.push(bb);
      }
      //除了附件上传方式或扫描，别的方式需要移除附件OID
      if (val != "2" && val != "3") {
        var index = this.materialAttaList.findIndex(atta => {
          if (item.caseMaterialOid == atta.caseMaterialOid) {
            return true;
          }
        });
        this.materialAttaList.splice(index, 1);
      }
      this.radio1.push(index);
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 隐藏右侧 */
    hiddenRightSideBar() {
      this.rightShow = false;
    },
    /** 显示右侧 */
    showRightSideBar() {
      this.rightShow = true;
    },
    /** 表单重置 */
    reset() {
      Object.assign(this.queryForm, this.$options.data().queryForm);
      this.resetForm("queryForm");
    },
    /** 取消按钮 */
    cancel() {
      this.openInit = false;
      this.reset();
    },

    /** 关闭方法 */
    closeDialog() {
      this.comboDireMaterials = [];
      this.situationOptionVals = [];
      this.checkList = [];
      this.situationList = [];
      this.materialOptList = [];
      this.mateOptRels = [];
      this.radio1 = [];
    },
    pushMaterialOid(materialOid, rawMaterialOid) {
      if (materialOid != undefined) {
        this.materialOid = materialOid;
      }
      if (rawMaterialOid != undefined) {
        this.materialOid = rawMaterialOid;
      }
    },
    /** 失败后返回 */
    uploadError(resp) {
      this.msgError("文件上传失败");
    },

    /** 上传附件 */
    uploadFiles(file) {
      let formData = new FormData();
      formData.append("files", file.file);
      uploadCaseMaterialFile(formData).then(response => {
        if (response.data != "") {
          response.data.forEach(data => {
            let atta = {};
            //let list={};
            atta.index = data;
            file.data.attaList.push(data);
            let at = new Object();
            at.caseMaterialOid = file.data.caseMaterialOid;
            at.attaOid = data.oid;
            this.materialAttaList.push(at);
          });
        }
      });
    },
    /** 上传附件请求操作 */
    beforeUpload(file) {
      let isRightSize = file.size / 1024 / 1024 < 2;
      if (!isRightSize) {
        this.$message.error("文件大小超过 2MB");
      }
      this.fileList.push(file);
      return isRightSize;
    },
    //预览附件
    viewFile(attaOid) {
      let item = { show: true, attaOid: attaOid };
      this.viewDialogOptions.push(item);
    },
    //关闭预览附件
    closeFileView() {
      this.viewDialogOptions.pop();
    },
    deleteFile(bean, atta) {
      delFile(bean.caseMaterialOid, atta.oid).then(response => {
        if (response.data != "") {
          var index = this.materialAttaList.findIndex(item => {
            if (item.attaOid == atta.oid) {
              return true;
            }
          });
          this.materialAttaList.splice(index, 1);
          var index2 = bean.attaList.findIndex(item2 => {
            if (item2.oid == atta.oid) {
              return true;
            }
          });
          bean.attaList.splice(index2, 1);
        }
      });
    },
    base64ToFile(urlData, fileName) {
      let arr = urlData.split(",");
      // let mime = arr[0].match(/:(.*?);/)[1];
      let bytes = atob(arr[0]); // 解码base64
      let n = bytes.length;
      let ia = new Uint8Array(n);
      while (n--) {
        ia[n] = bytes.charCodeAt(n);
      }
      return new File([ia], fileName, { type: "image/jpeg" });
    },
    // 接收socket回调函数返回数据的方法
    getConfigResult(data) {
      let _that = this;
      if (data.status == 0) {
        //读卡
        if (data.device == "IdCard") {
          //申请人模块--0
          //联系人模块--1
          if (data.type == "0") {
            _that.ruleForm.credentialNumber = data.content.CardNum;
            _that.ruleForm.applyUserName = data.content.CardBelongName;
            _that.ruleForm.credentialType = "身份证";
          }
          if (data.type == "1") {
            _that.ruleForm.contactCredentialNumber = data.content.CardNum;
            _that.ruleForm.contactUserName = data.content.CardBelongName;
          }
        }

        //扫描
        if (data.device == "HighCamera") {
          if (data.type) {
            const caseMaterialOid = data.type;
            if (data.content.Cameras64) {
              let base64s = data.content.Cameras64.split(",");
              for (let i = 0; i < base64s.length; i++) {
                if (!base64s[i]) {
                  continue;
                }
                let file = _that.base64ToFile(
                  base64s[i],
                  "scanPicture" + i + ".jpg"
                );
                let formD = new FormData();
                formD.append("files", file);
                _that.fileList.push(file);
                uploadCaseMaterialFile(formD).then(response => {
                  if (response.data != "") {
                    response.data.forEach(data => {
                      _that.scan_item.attaList.push(data);
                      let at = new Object();
                      at.caseMaterialOid = caseMaterialOid;
                      at.attaOid = data.oid;
                      _that.materialAttaList.push(at);
                    });
                  }
                });
              }
            }
          }
        }
      }
      if (data.status == 1) {
        _that.$message.error(data.message);
      }
    },
    //初始化socket发生错误回调
    socketError() {
      this.$message.error("请检查设备或连接是否正常");
    },
    scanCard(scanType) {
      //申请人模块--0
      //联系人模块--1
      //Device：设备类型、
      let info = '{"device":"IdCard", "type":"' + scanType + '"}';
      //建立socket连接
      this.socketApi.initWebSocket(this.socketError);
      this.socketApi.sendSock(info, this.getConfigResult);
    },
    scanPicture(caseMaterialOid, index, item) {
      //caseMaterialOid--所属材料主键
      //index--所属操作行索引
      //item--所属操作行数据
      //Device：设备类型、
      this.scan_item = item;
      let info = '{"device":"HighCamera", "type":"' + caseMaterialOid + '"}';
      //建立socket连接
      this.socketApi.initWebSocket(this.socketError);
      this.socketApi.sendSock(info, this.getConfigResult);
    },
    openMaterialComparison(materiaitem) {
      let sampleInfoOid = "";
      let comboDirectoryOid = this.ruleForm.comboDireOid;
      let materiaOid = materiaitem.materialOid;
      let caseOid = "";
      let fileViewurls =
        process.env.BASE_URL +
        "picture/prviewList.html?materiaOid=" +
        materiaOid +
        "&sampleInfoOid=" +
        sampleInfoOid +
        "&comboDirectoryOid=" +
        comboDirectoryOid;
      let item = {
        show: true,
        title: "材料比对",
        sampleInfoOid: sampleInfoOid,
        comboDirectoryOid: comboDirectoryOid,
        materiaOid: materiaOid,
        fileViewurl: fileViewurls,
        caseOid: caseOid
      };
      this.materialComparisonOptions.push(item);
      console.log(JSON.stringify(item));
    },
    //表单填报
    formFilling(dataForm, index) {
      let _that = this;
      _that.indexForm = index;
      if (dataForm) {
        if (dataForm.formType == 0) {
          //自定义表单
          if (dataForm.formAddr) {
            window.open(dataForm.formAddr, "width=1200px;height=800px;");
          } else {
            _that.$message.error("请配置表单地址！");
          }
        } else if (dataForm.formType == 1) {
          //电子表单
          _that.iframeState = true;
          //查询电子表单地址配置
          // "http://172.168.249.2:8081/form//loadInitFormReport.do?authorizeKey=00b98ec063984da0afca427d3637fe2a&formOid=FORM20180927GOXSMMER";
          _that.iframeUrl =
            process.env.VUE_APP_DZBD_TB_ROUTE_PATH +
            "&formOid=" +
            dataForm.formCode;
          if (_that.caseForm[index].formDataId) {
            _that.iframeUrl += "&reportOid=" + _that.caseForm[index].formDataId;
          }
        }
      }
    },
    //修改表单关闭
    closeIframe(reportOid) {
      if (reportOid) {
        this.tempFormDataId[this.indexForm] = reportOid;
        reportOid = ""; //防止点击取消的关闭
      }
      this.iframeState = false;
    },
    //关闭表单预览
    closeIframeView() {
      this.iframVieweState = false;
    },
    //保存电子表单
    saveCaseForm() {
      let _that = this;
      if (_that.sxSerFormList) {
        _that.sxSerFormList.forEach((items, i) => {
          let formData = {};
          let formCaseTemp = _that.caseForm[i];
          formData.regOid = _that.caseOid;
          formData.serFormOid = items.oid;
          formData.comboDireOid = _that.comboDireOid;
          formData.formDataId = _that.tempFormDataId[i];
          if (_that.caseForm[i]) {
            formData.id = formCaseTemp.id;
            formData.oid = formCaseTemp.oid;
          }
          saveOrUpdateCaseForm(formData).then(response => {
            if (response.data) {
              _that.caseForm[i] = response.data;
            }
          });
        });
      }
    },
    //查看电子表单
    viewFormFilling(items, index) {
      let _that = this;
      if (_that.sxSerFormList[index]) {
        if (_that.sxSerFormList[index].formType == 0) {
          //自定义表单
          if (_that.sxSerFormList[index].formAddr) {
            window.open(
              _that.sxSerFormList[index].formAddr,
              "width=1200px;height=800px;"
            );
          } else {
            _that.$message.error("请配置表单地址！");
          }
        } else if (_that.sxSerFormList[index].formType == 1) {
          //电子表单
          if (items == null) {
            _that.$message.error("未查询到表单！");
          } else {
            _that.iframVieweState = true;
            _that.iframeUrlView =
              process.env.VUE_APP_DZBD_CK_ROUTE_PATH +
              "&reportOid=" +
              items.formDataId;
          }
        }
      }
    },
    //平板评价确认信息
    confirmPbpj(ruleForm) {
      ruleForm.pbpjCaseOkUrl = process.env.VUE_APP_ZC_API_PAGE;
      pushPbpjCheckLogin().then(response => {
        if (response.data) {
          //检查平板评价登录
          this.$confirm("你确定要进行办件信息确认吗？", "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          })
            .then(() => {
              regIndustryCaseInfo(ruleForm).then(response => {
                if (response.data == true) {
                  alert("dddd");
                  // window.open("http://172.168.252.42:8899/pbpj/pbpjCase.html?content="+JSON.stringify(content));
                }
              });
            })
            .then(() => {
              this.msgSuccess("办件信息确认成功！");
            });
        } else {
          this.msgWarning("平板评价未登录，请登录平板评价器！");
        }
      });
    }
  },
  destroyed: function() {
    //在离开此页面的时候主动关闭socket
    this.socketApi.webSocketClose();
  }
};
</script>
<style lang="scss" scoped>
.el-tree > .el-tree-node {
  min-width: 100%;
  display: inline-block;
}

.process-box {
  padding: 15px;
  box-sizing: border-box;
  text-align: left;
}

.process-box .step-title {
  font-size: 14px;
  color: #333;
}

.process-box .step-title span {
  display: inline-block;
  vertical-align: middle;
  background: url(../../../../assets/image/step-lastbg.png) no-repeat center;
  width: 100px;
  height: 30px;
  line-height: 30px;
  text-align: center;
  cursor: pointer;
}

.process-box .step-title span:first-child {
  background: url(../../../../assets/image/step-firstbg.png) no-repeat center;
  width: 100px;
  height: 30px;
}

.process-box .step-title span.active {
  background: url(../../../../assets/image/step-lastbg-active.png) no-repeat
    center;
  width: 100px;
  height: 30px;
  color: #fff;
}

.process-box .step-title span:first-child.active {
  background: url(../../../../assets/image/step-firstbg-active.png) no-repeat
    center;
  width: 100px;
  height: 30px;
  color: #fff;
}

.process-box .title {
  font-size: 12px;
  color: #333;
  font-weight: normal;
  margin-top: 30px;
  text-align: left;
}

.select-list span {
  display: inline-block;
  vertical-align: middle;
  color: #47657d;
  font-size: 14px;
  background-color: #f1f5f9;
  height: 40px;
  line-height: 40px;
  margin: 0px 10px 10px 0px;
  padding: 0px 35px 0px 20px;
  border-radius: 5px;
  cursor: pointer;
}

.select-list span:first-child {
  background-color: #fff6f1;
  color: #2d506b;
}

.select-list span.current {
  background-color: #4d87b5;
  color: #fff;
  position: relative;
}

.select-list span.current:after {
  content: "";
  position: absolute;
  width: 19px;
  height: 13px;
  right: 10px;
  top: 13px;
  background: url(../../../../assets/image/check-icon.png) no-repeat center;
  background-size: cover;
}

.option-box .option-title {
  margin-top: 20px;
  font-size: 12px;
  color: #333;
}

.option-box .option-item {
  margin-right: 10px;
}

.option-box .option-item,
.option-box .chose-item {
  display: inline-block;
  vertical-align: top;
}

.option-box .chose-item {
  color: #4d87b5;
}

.process-box table {
  width: 100%;
}

.process-box table {
  border: 1px solid #dfe6ec;
  border-collapse: collapse;
}

.process-box table tr td,
.process-box table tr th {
  border: 1px solid #dfe6ec;
  padding: 17px 10px;
  font-size: 12px;
  color: #515a6e;
  text-align: right;
}

.process-box table tr td:nth-of-type(2n + 1) {
  background-color: #f8f8f9;
}

.process-box table tr td:nth-of-type(2n) {
  color: #606266;
  text-align: left;
}

.process-box table.data-table tr td,
.process-box table.data-table tr th {
  text-align: center;
  padding: 12px 10px;
}

.process-box table.data-table tr th {
  background-color: #f8f9fb;
}

.process-box table.data-table tr td:nth-of-type(2n + 1) {
  background: none;
}

.process-box table.data-table .bage-necessery {
  color: #ff3000;
  background-color: #fff2f2;
  height: 30px;
  line-height: 30px;
  padding: 0px 20px;
  border-radius: 20px;
  display: inline-block;
}

.process-box .check-list {
  border-bottom: 1px solid #e5e5e5;
  margin: 0px 20px;
  padding-left: 10px;
  box-sizing: border-box;
  padding-bottom: 20px;
}

.process-box .check-list h3 {
  font-size: 12px;
  color: #333;
}

.process-box .el-form-item {
  margin-bottom: 0;
}

.require {
  color: #ff0000;
  font-style: normal;
  font-size: 14px;
  display: inline-block;
  vertical-align: middle;
  margin-right: 5px;
}

.process-box .next-btn {
  display: block;
  margin: 30px auto;
  font-size: 14px;
}

.process-box .data-box h4 {
  font-size: 12px;
  color: #333;
  margin: 20px 0px 10px 0px;
}

.process-box .btn-wrap {
  text-align: center;
  margin: 30px 0;
}

.process-box .form-box-inline {
  display: inline-block;
  vertical-align: middle;
  border: 1px solid #2d506b;
  width: 100%;
  padding: 30px;
  box-sizing: border-box;
}

.process-box .el-button--info {
  background-color: #3b5f7b;
}

.process-box .el-button--info:hover {
  background-color: #426886;
}

.process-box .form-box-inline {
  position: relative;
}

.process-box .form-box-inline .btn-write {
  position: absolute;
  right: 20px;
  top: 20px;
  font-size: 12px;
  padding: 8px 15px;
}

.process-box .form-box-inline ul {
  margin: 0;
  padding: 0;
}

.process-box .form-box-inline ul li {
  padding: 10px 20px;
  background-color: #f3f6f9;
  border-radius: 10px;
  margin-bottom: 10px;
  list-style: none;
}

.process-box .form-box-inline ul li > .icon {
  display: inline-block;
  vertical-align: middle;
  width: 36px;
  height: 36px;
  line-height: 36px;
  text-align: center;
  border-radius: 100%;
  color: #fff;
  font-size: 20px;
}

.process-box .form-box-inline ul li > .bdbm-icon {
  background-color: #a8cfec;
}

.process-box .form-box-inline ul li > .bdsm-icon {
  background-color: #e6b893;
}

.process-box .form-box-inline .input-text {
  display: inline-block;
  vertical-align: middle;
  padding-left: 10px;
  font-size: 12px;
  color: #333;
}

.process-box .form-box-inline > h4 {
  font-weight: normal;
  color: #003259;
  font-size: 14px;
}

.process-box .form-box-inline .input-text > h4 {
  margin: 0px 0px 5px 0px;
  color: #3b5f7b;
  font-weight: normal;
}

.process-box .form-box-inline .input-text > p {
  margin: 0px;
}

.process-box .form-btn-group {
  position: absolute;
  right: 20px;
  top: 20px;
}

.process-box .form-btn-group .btn {
  border: 1px solid #097dd6;
  color: #097dd6;
  font-size: 12px;
  padding: 8px 15px;
}

.handle-data {
  padding: 25px 40px;
  background-color: #f8f9fb;
  position: relative;
}

.handle-data ul {
  padding: 0px;
  margin: 0;
}

.handle-data ul li {
  list-style: none;
  text-align: left;
  margin-top: 10px;
}

.right-btn-group {
  position: absolute;
  right: 130px;
  top: 10px;
}

.right-btn-group-two {
  position: absolute;
  right: 10px;
  top: 10px;
}

.right-btn-group .el-button {
  padding: 6px 8px;
  font-size: 12px;
  margin-left: 5px;
  background-color: #0ba2b8;
  border: 1px solid #0ba2b8;
}

.right-btn-group .el-button:last-child {
  background-color: #47657d;
  border: 1px solid #47657d;
}

.right-btn-group .el-button:last-child:hover {
  background-color: #4d708b;
  border: 1px solid #4d708b;
}

.qdcg-success {
  font-size: 12px;
  color: #00b45e;
  margin-top: 5px;
  text-decoration: underline;
}

.qdcg-fail {
  font-size: 12px;
  color: #ff0000;
  margin-top: 5px;
  text-decoration: underline;
}

.qdcg-success > img,
.qdcg-fail > img {
  display: inline-block;
  vertical-align: middle;
  margin: -2px 5px 0 0;
}

.qdcg-text img,
.qdcg-text p {
  display: inline-block;
  vertical-align: middle;
}

.qdcg-text img {
  margin-right: 5px;
}

.qdcg-text p {
  margin: 0;
}

.qdcg-text p > span {
  font-size: 12px;
  color: #999;
  padding-left: 20px;
}

.qdcg-btn .el-button {
  color: #333;
}

.qdcg-btn {
  margin-top: -5px;
}

.input-number {
  text-align: left;
  margin-top: 20px;
  margin-bottom: 10px;
}

.input-number-label {
  display: inline-block;
  vertical-align: middle;
  margin-right: 20px;
}

.rightSideBar {
  position: fixed;
  right: 0px;
  bottom: 100px;
  box-shadow: -1px 1px 10px #ddd;
  border-top-left-radius: 5px;
  border-bottom-left-radius: 5px;
}

.rightSideBar .sideItem {
  width: 42px;
  height: 42px;
  line-height: 42px;
  text-align: center;
  color: #0b76c7;
  background-color: #fff;
  border-top-left-radius: 5px;
  cursor: pointer;
  font-size: 20px;
  position: relative;
  -webkit-transition: all 0.3s ease-in-out 0.1s;
  transition: all 0.3s ease-in-out 0.1s;
  z-index: 2;
}

.rightSideBar .sideItem:last-child {
  border-top-left-radius: 0px;
  border-bottom-left-radius: 5px;
}

.rightSideBar .sideItem:hover {
  background-color: #0b76c7;
  color: #fff;
  border-top-left-radius: 0px;
  border-bottom-left-radius: 0px;
}

.rightSideBar .sideItem .sideText {
  position: absolute;
  left: 42px;
  top: 0px;
  height: 42px;
  line-height: 42px;
  padding: 0px 10px 0px 20px;
  border-top-left-radius: 30px;
  border-bottom-left-radius: 30px;
  color: #fff;
  background-color: #0b76c7;
  font-size: 14px;
  -webkit-transition: left 0.3s ease-in-out 0.1s;
  transition: left 0.3s ease-in-out 0.1s;
  z-index: 1;
}

.rightSideBar .sideItem:hover .sideText {
  left: -86px;
}

.title-detail {
  text-align: center;
}
</style>
