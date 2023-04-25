<template>
  <el-dialog
    v-dialog-drag
    :visible.sync="openViewBzsl"
    height="700px"
    scrollbar
    title="补正受理"
    width="1100px"
    append-to-body
    :close-on-click-modal="false"
  >
    <el-tabs
      v-model="activeName"
      @tab-click="handleClick"
      style="overflow: hidden"
    >
      <el-tab-pane label="基本信息" name="first">
        <!--事项信息-->
        <div>
          <div class="zf-zc-table--title">事项相关信息</div>

          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="12%" />
              <col width="21%" />
              <col width="12%" />
              <col width="21%" />
              <col width="12%" />
              <col width="22%" />
            </colgroup>
            <tr>
              <td><b>事项名称：</b></td>
              <td colspan="3">{{ form.info.serviceName }}</td>
              <td><b>事项类型：</b></td>
              <td>{{ form.info.serviceTypeName }}</td>
            </tr>
            <tr>
              <td><b>实施机构：</b></td>
              <td>
                {{
                  handleOptionalChaining(
                    form,
                    "form.serviceInfo.sxService.organName"
                  )
                }}
              </td>
              <td><b>承诺时限：</b></td>
              <td>
                {{ promiseLimit }}
                <span v-if="showPromiseLimitTime == 'W'">工作日</span>
                <span v-if="showPromiseLimitTime == 'N'">自然日</span>
                <span v-if="showPromiseLimitTime == 'H'">小时</span>
              </td>
              <td><b>法定时限：</b></td>
              <td>
                {{ legalLimit }}
                <span v-if="showLegalLimitTime == 'W'">工作日</span>
                <span v-if="showLegalLimitTime == 'N'">自然日</span>
                <span v-if="showLegalLimitTime == 'H'">小时</span>
              </td>
            </tr>
          </table>
        </div>
        <!--申请人信息-->
        <div>
          <div class="zf-zc-table--title">申请人相关信息</div>

          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="12%" />
              <col width="21%" />
              <col width="12%" />
              <col width="21%" />
              <col width="12%" />
              <col width="22%" />
            </colgroup>
            <tr>
              <td><b>申请项目名称：</b></td>
              <td colspan="5">{{ form.info.projectName }}</td>
            </tr>
            <tr>
              <td><b>受理具体地点：</b></td>
              <td colspan="3">{{ form.applyPerson.specificLocation }}</td>
              <td><b>申请数量：</b></td>
              <td>{{ form.applyPerson.applyNumber }}</td>
            </tr>
            <tr>
              <td v-if="form.applyPerson.applyUserType == '0'">
                <b>申请人姓名：</b>
              </td>
              <td v-else><b>申请单位名称：</b></td>
              <td>{{ form.applyPerson.applyUserName }}</td>
              <td><b>证件类型：</b></td>
              <td>{{ form.applyPerson.credentialType }}</td>
              <td><b>证件号：</b></td>
              <td>{{ form.applyPerson.credentialNumber }}</td>
            </tr>
            <tr>
              <td v-if="form.applyPerson.applyUserType == '0'">
                <b>申请人手机：</b>
              </td>
              <td v-else><b>申请单位手机：</b></td>
              <td>{{ form.applyPerson.applyUserPhone }}</td>
              <td v-if="form.applyPerson.applyUserType == '0'">
                <b>申请人电话：</b>
              </td>
              <td v-else><b>申请单位电话：</b></td>
              <td>{{ form.applyPerson.applyUserTel }}</td>
              <td><b>邮政编码：</b></td>
              <td>{{ form.applyPerson.applyPostCode }}</td>
            </tr>
            <tr>
              <td><b>通讯地址：</b></td>
              <td colspan="5">{{ form.applyPerson.applyUserAddress }}</td>
            </tr>
          </table>
        </div>
        <!--收件相关信息-->
        <div>
          <div class="zf-zc-table--title">收件相关信息</div>

          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="12%" />
              <col width="21%" />
              <col width="12%" />
              <col width="21%" />
              <col width="12%" />
              <col width="22%" />
            </colgroup>
            <tr>
              <td><b>送达方式：</b></td>
              <td colspan="5">
                <span v-if="form.caseExt.resultDeliveryWay == 1">
                  快递送达</span
                >
                <span v-if="form.caseExt.resultDeliveryWay == 2">自行取件</span>
                <span v-if="form.caseExt.resultDeliveryWay == 3">其他</span>
              </td>
            </tr>
            <tr v-if="form.caseExt.resultDeliveryWay == 1">
              <td><b>收件人姓名：</b></td>
              <td>{{ form.applyPerson.addresseeName }}</td>
              <td><b>收件人邮编：</b></td>
              <td>{{ form.applyPerson.addresseePostCode }}</td>
              <td><b>收件人手机：</b></td>
              <td>{{ form.applyPerson.addresseePhone }}</td>
            </tr>
            <tr v-if="form.caseExt.resultDeliveryWay == 1">
              <td><b>收件人电话：</b></td>
              <td>{{ form.applyPerson.addresseeTel }}</td>
              <td><b>收件人地址：</b></td>
              <td colspan="3">{{ form.applyPerson.addresseeAddress }}</td>
            </tr>
            <tr v-if="form.caseExt.resultDeliveryWay == 1">
              <td><b>收件人详细地址：</b></td>
              <td colspan="5">
                {{ form.applyPerson.addresseeDetailAddress }}
              </td>
            </tr>
          </table>
        </div>

        <div v-if="formConfig_show">
          <div class="zf-zc-table--title">表单信息</div>

          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="12%" />
              <col width="31%" />
              <col width="12%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>表单名称：</b></td>
              <td>{{ sxSerForm.formName }}</td>
              <td><b>表单编码：</b></td>
              <td>{{ sxSerForm.formCode }}</td>
            </tr>
            <tr>
              <td><b>表单说明：</b></td>
              <td colspan="3">{{ sxSerForm.formText }}</td>
            </tr>
            <tr>
              <td><b>表单预览：</b></td>
              <td>
                <el-button
                  icon="el-icon-view"
                  class="btn"
                  @click="viewFormFilling"
                  >表单预览</el-button
                >
              </td>
              <td v-if="sxSerForm.simpleAtta"><b>表单样本：</b></td>
              <td v-if="sxSerForm.simpleAtta">
                <el-button
                  icon="el-icon-view"
                  class="btn2"
                  @click="downloadFile(sxSerForm.simpleAtta)"
                  >表单样本下载</el-button
                >
              </td>
            </tr>
          </table>
        </div>
      </el-tab-pane>
      <el-tab-pane label="收取材料" name="third">
        <div>
          <div class="zf-zc-table--title">材料信息</div>
          <el-form
            label-width="0px"
            class="demo-ruleForm"
            :label-position="labelPosition"
          >
            <table
              cellspacing="0"
              cellpadding="0"
              border="0"
              class="zf-zc-table"
            >
              <colgroup>
                <col width="10%" />
                <col width="40%" />
                <col width="10%" />
                <col width="40%" />
              </colgroup>
              <tr>
                <th>序号</th>
                <th>材料名称</th>
                <th>收取份数</th>
                <th>收取方式</th>
              </tr>
              <tbody v-for="(data, index) in form.material" :key="index">
                <tr>
                  <td style="text-align: center; background: #fff">
                    {{ index + 1 }}
                  </td>
                  <td>{{ data.qlCaseMaterial.materialName }}</td>
                  <td style="text-align: center; background: #fff">
                    {{ data.qlCaseMaterial.collectionNumber }}
                  </td>
                  <td>
                    <el-radio-group
                      v-model="data.qlCaseMaterial.collectionType"
                      @change="handleRowChange(data, index)"
                    >
                      <el-radio label="1">纸质收取</el-radio>
                      <el-radio label="2">附件上传</el-radio>
                      <el-radio label="3">扫描</el-radio>
                      <el-radio  :disabled="!data.elecBillOid" label="5">证照库</el-radio>
                    </el-radio-group>
                  </td>
                </tr>

                <tr
                  v-if="
                    data.qlCaseMaterial.collectionType == 2 ||
                      data.qlCaseMaterial.collectionType == 3 ||
                      data.qlCaseMaterial.collectionType == 5
                  "
                >
                  <td colspan="5" style="background-color: #fafbfc">
                    <div class="handle-data">
                      <div class="handle-data-list">
                        <div
                          v-for="(dataAtta, idx) in attaList[index]"
                          :key="idx"
                          class="handle-data-list-item"
                        >
                          <template v-if="dataAtta != null">
                            <div class="grid-content qdcg-text">
                              <div
                                v-if="data.qlCaseMaterial.collectionType == 5"
                                @click="viewElemsInfo(dataAtta.attaOid)"
                                class="handle-data-list-item--link"
                              >
                                {{ dataAtta.elecLicenName }} - {{ dataAtta.elecLicenNumber }}
                              </div>
                              <div
                                v-else
                                @click="viewFile(dataAtta.attaOid)"
                                class="handle-data-list-item--link"
                              >
                                材料名称 - {{ dataAtta.originName }}
                              </div>
                            </div>
                            <div class="handle-data-list-item--btn">
                              <el-button
                                type="text"
                                size="mini"
                                icon="el-icon-view"
                                v-if="
                                  data.qlCaseMaterial.collectionType == 2 ||
                                    data.qlCaseMaterial.collectionType == 3
                                "
                                @click="viewFile(dataAtta.attaOid)"
                              >
                                查看
                              </el-button>
                              <el-button
                                type="text"
                                size="mini"
                                v-if="
                                  data.qlCaseMaterial.collectionType == 2 ||
                                    data.qlCaseMaterial.collectionType == 3
                                "
                                icon="el-icon-delete"
                                @click="
                                  deleteMaterialAtta(
                                    data.qlCaseMaterial.caseMaterialOid,
                                    dataAtta.attaOid,
                                    index
                                  )
                                "
                              >
                                删除
                              </el-button>
                              <el-button
                                type="text"
                                size="mini"
                                icon="el-icon-view"
                                v-if="data.qlCaseMaterial.collectionType == 5"
                                @click="viewElemsInfo(dataAtta.attaOid)"
                              >
                                查看
                              </el-button>
                            </div>
                          </template>
                        </div>
                      </div>
                      <el-upload
                        v-if="data.qlCaseMaterial.collectionType == 2"
                        class="upload-demo"
                        action=""
                        :http-request="uploadFiles"
                        :before-upload="beforeUpload"
                        :on-error="uploadError"
                        :show-file-list="showFileList"
                        accept="accept"
                      >
                        <el-button
                          size="mini"
                          type="primary"
                          icon="el-icon-upload"
                          @click="
                            pushMaterialOid(
                              data.qlCaseMaterial.caseMaterialOid,
                              index
                            )
                          "
                        >
                          点击上传
                        </el-button>
                      </el-upload>
                      <el-button
                        v-if="data.qlCaseMaterial.collectionType == 3"
                        size="mini"
                        type="primary"
                        icon="el-icon-upload"
                        @click="openGPYDialog(data.qlCaseMaterial, index)"
                      >
                        点击扫描
                      </el-button>
                      <el-button
                        v-if="data.qlCaseMaterial.collectionType == 5"
                        size="mini"
                        type="primary"
                        icon="el-icon-upload"
                        @click="
                          getElecLicenInfo(
                            data.qlCaseMaterial.materialName,
                            data.qlCaseMaterial.materialOid,
                            data.qlCaseMaterial.caseMaterialOid,
                            index
                          )
                        "
                      >
                        获取证照
                      </el-button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </el-form>
        </div>
        <div>
          <div class="zf-zc-table--title">补正受理信息</div>
          <el-form :model="form.info" label-width="0px">
            <table
              cellspacing="0"
              cellpadding="0"
              border="0"
              class="zf-zc-table"
            >
              <colgroup>
                <col width="10%" />
                <col width="80%" />
              </colgroup>
              <tr>
                <td><b>办理意见：</b></td>
                <td>
                  <el-form-item prop="patchOpinion">
                    <el-input
                      v-model.trim="formParam.patchOpinion"
                      type="textarea"
                      placeholder="请输入办理意见"
                      maxlength="2000"
                      show-word-limit
                    ></el-input>
                  </el-form-item>
                </td>
              </tr>
              <tr>
                <td><b>备注：</b></td>
                <td>
                  <el-form-item prop="memo">
                    <el-input
                      v-model.trim="formParam.memo"
                      type="textarea"
                      placeholder="请输入备注"
                      maxlength="2000"
                      show-word-limit
                    ></el-input>
                  </el-form-item>
                </td>
              </tr>
            </table>
          </el-form>
          <!--引入文件的预览弹出层-->
          <el-dialog
            v-dialog-drag
            title="文件预览"
            :visible.sync="showFile"
            v-if="showFile"
            @close="closeFileView"
            width="1000px"
            append-to-body
          >
            <case-file-view
              :attaOid="fileAttaOid"
              @father-click="closeFileView"
            ></case-file-view>
            <div slot="footer" class="zf-text-center">
              <el-button @click="closeFileView = false"> 关闭 </el-button>
            </div>
          </el-dialog>

          <el-dialog
            v-dialog-drag
            title="电子表单预览"
            :visible.sync="iframVieweState"
            width="1100px"
            height="100%"
            :modal-append-to-body="true"
            append-to-body
          >
            <iframe-url-view
              :outLink="iframeUrlView"
              @closeIframeView="closeIframeView"
            ></iframe-url-view>
          </el-dialog>
        </div>
      </el-tab-pane>
    </el-tabs>
    <div
      slot="footer"
      class="dialog-footer"
      style="text-align: center; margin-top: 20px"
    >
      <el-button type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="closeDialog()">取 消</el-button>
    </div>

    <GPYUpload
      v-if="dialogVisible"
      :dialogVisible.sync="dialogVisible"
      :qlCaseMaterial="qlCaseMaterial"
    />
  </el-dialog>
</template>
<script>
import {
  getOneCase,
  getOneApplyPerson,
  getQlCaseExt,
  getServiceInfo,
  getOneDict
} from "@/api/zc/businessManagement/viewCaseInfo.js";
import { saveOut } from "@/api/zc/businessManagement/windowAcceptance";
import {
  saveOrUpdate,
  getCorrectMaterialInfo
} from "@/api/zc/businessManagement/caseBqbz";
import { uploadCaseMaterialFile } from "@/api/zc/businessManagement/caseMaterialAtta";
import {
  getElecLicenUrl,
  queryElecLicenseByDirCode
} from "@/api/zc/businessManagement/elemLice";
import caseFileView from "@/views/zc/businessManagement/caseBqbz/caseFileView";
import { CodeToText } from "element-china-area-data";
import {
  caseFormByCaseOid,
  getSxSerFormBysxSerFormOid
} from "@/api/zc/businessManagement/formConfig";
import IframeUrlView from "@/views/iframe/formIndexView";
/** 高拍仪 */
import GPYUpload from "@/views/components/gpy-dialog/index.vue";
export default {
  name: "caseBzsl",
  components: {
    caseFileView,
    IframeUrlView,
    GPYUpload
  },
  props: ["visible", "msgVal"],

  data() {
    return {
      // 遮罩层
      loading: true,
      activeName: "first",
      caseNumber: "",
      caseOid: "",
      id: "",
      correctionOid: "",
      labelPosition: "top",
      fileAttaOid: "",
      showFile: false,
      showFileList: false,
      materialOid: null,
      caseMaterials: [],
      //材料与上传成功的附件集合
      materialAttaList: [],
      materialCollectionType: [],
      elemLicenseList: [],
      //附件集合
      attaList: [],
      open: true,
      formConfig_show: false, //标识表单是否显示
      iframeState: false,
      iframVieweState: false,
      iframeUrl: {}, //表单地址
      iframeUrlView: {}, //查看
      sxSerForm: {}, //配置表单的信息
      caseForm: {},
      // 表单参数
      form: {
        info: {},
        caseExt: {},
        applyPerson: {},
        serviceInfo: [],
        material: []
      },
      formParam: {
        caseOid: "",
        patchOpinion: "",
        memo: "",
        materialAtta: [],
        materialCollect: [],
        elemLicense: []
      },
      dialogVisible: false,
      qlCaseMaterial: {}
    };
  },
  computed: {
    openViewBzsl: {
      get() {
        return this.visible;
      },
      set() {
        this.$emit("update:visible", false);
      }
    },

    promiseLimit() {
      return this.form?.serviceInfo?.sxServiceExtend?.promiseLimit;
    },
    showPromiseLimitTime() {
      return this.form?.serviceInfo?.sxServiceExtend?.promiseLimitType;
    },
    legalLimit() {
      return this.form?.serviceInfo?.sxServiceExtend?.legalLimit;
    },
    showLegalLimitTime() {
      return this.form?.serviceInfo?.sxServiceExtend?.legalLimitType;
    }
  },
  //获取父页面的值
  mounted() {
    this.caseNumber = this.msgVal.caseNumber;
    this.id = this.msgVal.id; //补齐补正主键
    this.correctionOid = this.msgVal.correctionOid;
    this.getOneCase();
  },
  methods: {
    handleRowChange(data, index) {
      this.materialCollectionType.forEach((item, i) => {
        //判断材料主键是否已经存在，存在移除
        if (item.materialOid == data.qlCaseMaterial.caseMaterialOid) {
          this.materialCollectionType.splice(i, 1);
        }
      });
      let list = {};
      list.materialOid = data.qlCaseMaterial.caseMaterialOid;
      list.collectionType = data.qlCaseMaterial.collectionType;
      this.materialCollectionType.push(list);
      //清空附件信息
      this.materialAttaList.forEach((att, j) => {
        if (att.materialOid == data.qlCaseMaterial.caseMaterialOid) {
          this.materialAttaList.splice(j, 1);
        }
      });
      this.$set(this.attaList, index, []);
      //清除电子证照信息
      this.elemLicenseList.forEach((itek, k) => {
        if (itek.materialOid == data.qlCaseMaterial.caseMaterialOid) {
          //移除原来的重新赋值
          this.elemLicenseList.splice(k, 1);
        }
      });

      // if (data.qlCaseMaterial.collectionType == 5) {
      //   this.getElecLicenInfo(
      //     data.qlCaseMaterial.materialName,
      //     data.qlCaseMaterial.materialOid,
      //     data.qlCaseMaterial.caseMaterialOid,
      //     index
      //   );
      // }
    },
    handleClick(tab, event) {},
    pushMaterialOid(materialOid, index) {
      this.materialOid = materialOid;
      this.attaIndex = index; //标识材料索引
    },
    //查询办件信息
    getOneCase() {
      this.form.info = {};
      this.form.caseExt = {};
      this.form.serviceInfo = [];
      this.form.applyPerson = {};
      this.form.material = [];
      getOneCase(this.caseNumber).then(response => {
        this.form.info = response.data;
        this.caseOid = response.data.caseOid;
        this.form.serviceOid = response.data.serviceOid;
        this.getApplyInfo();
        this.getOneCaseForm();
        this.getOneMaterialInfo();
        this.viewServiceInfo(response.data.serviceOid);
        this.getSdfs();
      });
    },
    //获取事项
    viewServiceInfo(serviceOid) {
      getServiceInfo(serviceOid).then(response => {
        this.form.serviceInfo = response.data;
      });
    },
    getApplyInfo() {
      //查询申请人信息
      getOneApplyPerson(this.caseOid).then(response => {
        this.form.applyPerson = response.data;
        this.getCredential(response.data.credentialType);
        if (
          this.form.applyPerson.addresseeAddress.indexOf("[") != -1 &&
          this.form.applyPerson.addresseeAddress.indexOf("]") != -1
        ) {
          this.handleChange();
        }
      });
    },
    // 编辑格式化地址
    handleChange() {
      let str = this.form.applyPerson.addresseeAddress;
      str = str.substring(1, str.length - 1);
      let self = str.toString().replace(/"/g, "");
      var selfArr = self.split(",");
      if (null != selfArr) {
        var provinceCode = "";
        var zhixia = "";
        var city = "";
        if (null != selfArr[0]) {
          provinceCode = CodeToText[selfArr[0]];
        }
        if (null != selfArr[1]) {
          zhixia = "/" + CodeToText[selfArr[1]];
        }
        if (null != selfArr[2]) {
          city = "/" + CodeToText[selfArr[2]];
        }
        this.form.applyPerson.addresseeAddress = provinceCode + zhixia + city;
      }
    },
    //获取证件类型
    getCredential(Type) {
      getOneDict(Type).then(response => {
        this.form.applyPerson.credentialType = response.data.name;
      });
    },
    getOneMaterialInfo() {
      //查询材料信息
      getCorrectMaterialInfo(this.correctionOid, this.caseOid).then(
        response => {
          this.form.material = response.data;
          this.form.material.forEach((item, i) => {
            if (item.qlCaseMaterial.collectionType == 5) {
              //电子证照
              let res = {};
              let eleArr = [];
              res.originName = item.qlCaseMaterial.elemNumber;
              res.attaOid = item.qlCaseMaterial.elemLicenseOid;
              res.materialOid = item.qlCaseMaterial.caseMaterialOid;
              res.elecLicenName = item.qlCaseMaterial.elecLicenName;
              res.elecLicenNumber = item.qlCaseMaterial.elecLicenNumber;
              eleArr[0] = res;
              this.attaList[i] = eleArr;
              this.elemLicenseList.push(res);
            } else {
              this.attaList[i] = item.materialAtta;
              //循环材料附件，保存主键和采集类型，附件主键和材料主键关系
              if (item.materialAtta) {
                item.materialAtta.forEach((childItem, i) => {
                  let list = {};
                  list.materialOid = item.qlCaseMaterial.caseMaterialOid;
                  list.attaOid = childItem.attaOid;
                  this.materialAttaList.push(list);
                });
              }
            }
            //先保存材料采集类型和材料主键关系
            this.materialCollectionType.forEach((itemType, i) => {
              //判断材料主键是否已经存在，存在移除
              if (itemType.materialOid == item.qlCaseMaterial.caseMaterialOid) {
                this.materialCollectionType.splice(i, 1);
              }
            });
            let listType = {};
            listType.materialOid = item.qlCaseMaterial.caseMaterialOid;
            listType.collectionType = item.qlCaseMaterial.collectionType;
            this.materialCollectionType.push(listType);
          });
        }
      );
    },
    getSdfs() {
      //送达方式
      getQlCaseExt(this.caseOid).then(response => {
        this.form.caseExt = response.data;
      });
    },
    /** 提交按钮 */
    submitForm() {
      let saveOutFlag = false; //保存材料出库信息的标识
      let materialOids = ""; //材料出库的材料主键
      this.formParam.caseOid = this.caseOid;
      this.formParam.id = this.id;
      this.formParam.materialAtta = this.materialAttaList;
      this.formParam.materialCollect = this.materialCollectionType;
      this.formParam.elemLicense = this.elemLicenseList;
      if (this.materialCollectionType.length > 0) {
        for (let j = 0; j < this.materialCollectionType.length; j++) {
          let type = this.materialCollectionType[j];
          let mustAttaFlag = false;
          if (type.collectionType) {
            //看是否为纸质，则无材料
            if (type.collectionType == 1) {
              materialOids += this.materialCollectionType[j].materialOid + ";";
              //无材料
              saveOutFlag = true;
              mustAttaFlag = true;
            } else if (type.collectionType == 2 || type.collectionType == 3) {
              if (this.materialAttaList.length > 0) {
                for (let k = 0; k < this.materialAttaList.length; k++) {
                  const atta = this.materialAttaList[k];
                  if (atta.materialOid == type.materialOid) {
                    mustAttaFlag = true;
                    break;
                  }
                }
              }
            } else if (type.collectionType == 4) {
              mustAttaFlag = true;
              //容缺后补
            } else if (type.collectionType == 5) {
              //电子证照
              if (this.elemLicenseList.length > 0) {
                for (let k = 0; k < this.elemLicenseList.length; k++) {
                  const atta = this.elemLicenseList[k];
                  if (atta.materialOid == type.materialOid) {
                    mustAttaFlag = true;
                    break;
                  }
                }
              }
            }
          } else {
            this.$message.error("请选择材料收取方式!");
            return false;
          }
          if (mustAttaFlag == false) {
            this.$message.error("材料未收取!");
            return false;
          }
        }
      }
      saveOrUpdate(this.formParam).then(response => {
        if (response.code === 200) {
          this.msgSuccess("保存成功");
          ////判断如果有纸质收取生成出库材料信息记录
          if (saveOutFlag) {
            this.saveMaterialOut(materialOids);
          }
          //调取父页面方法，关闭页面
          this.closeDialog();
        }
      });
    },
    closeDialog() {
      //调取父页面方法
      this.$emit("hideDialog");
    },
    /** 综窗保存材料出库信息 */
    saveMaterialOut(materialOids) {
      let outMaterial = {};
      outMaterial.regOid = this.form.info.caseOid;
      outMaterial.caseNumber = this.form.info.caseNumber;
      outMaterial.applyUserName = this.form.applyPerson.applyUserName;
      outMaterial.serviceTypeName = this.form.info.serviceTypeName;
      outMaterial.cardNumber = this.form.applyPerson.credentialNumber;
      outMaterial.serviceOid = this.form.serviceOid;
      outMaterial.materialOids = materialOids;
      saveOut(outMaterial).then(response => {
        if (response.data == "") {
          this.msgError("材料出库信息保存失败！");
          return false;
        }
      });
    },
    //预览附件
    viewFile(attaOid) {
      this.fileAttaOid = attaOid;
      this.showFile = true;
    },
    //关闭预览附件
    closeFileView() {
      this.showFile = false;
    },
    //失败后返回
    uploadError(resp) {
      this.msgError("文件上传失败");
    },
    /** 上传附件 */
    uploadFiles(file) {
      let formData = new FormData();
      formData.append("files", file.file);
      uploadCaseMaterialFile(formData).then(response => {
        if (response.data != "") {
          let resultAtta = [];
          if (this.attaList.length > 0) {
            if (this.attaList[this.attaIndex]) {
              resultAtta = [].concat(this.attaList[this.attaIndex]);
            } else {
            }
          }
          response.data.forEach((data, index) => {
            resultAtta.push(data); //存放每一个上传的附件
            //放置材料与附件的关系,生成多条记录
            let list = {};
            list.materialOid = this.materialOid;
            list.attaOid = data.attaOid;
            this.materialAttaList.push(list);
          });
          this.attaList[this.attaIndex] = [].concat(resultAtta);
          this.$forceUpdate(); //强制视图刷新
        }
      });
    },
    //上传之前
    beforeUpload(file) {
      if (
        file.name.indexOf("%00") > -1 ||
        file.name.indexOf("./") > -1 ||
        file.name.indexOf(".\\") > -1
      ) {
        this.msgError("上传文件名称非法！");
        return false;
      }
      const fileSize = file.size;
      if (0 == fileSize) {
        this.msgError("上传文件不能为空！");
        return false;
      }
      const isLt2M = file.size / 1024 / 1024 < 100;
      if (!isLt2M) {
        this.msgError("上传文件大小不能超过 100MB！");
      }
      return isLt2M;
    },
    //删除附件信息
    deleteMaterialAtta(caseMaterialOid, attaOid, index) {
      this.materialAttaList.forEach((item, i) => {
        //判断材料主键是否已经存在，存在移除
        if (item.materialOid == caseMaterialOid && item.attaOid == attaOid) {
          this.materialAttaList.splice(i, 1);
        }
        //剔除附件list中的附件
        if (this.attaList.length > 0) {
          if (this.attaList[index]) {
            //剔除附件信息重新加载页面
            this.attaList[index].forEach((ind, i) => {
              if (ind.attaOid == attaOid) {
                this.attaList[index].splice(i, 1);
              }
            });
          }
        }
      });
      this.$forceUpdate(); //强制更新视图
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
      return new File([ia], fileName, {
        type: "image/jpeg"
      });
    },
    // 接收socket回调函数返回数据的方法
    getConfigResult(data) {
      if (data.status == 0) {
        //高拍仪
        if (data.type) {
          let types = data.type.split(",");
          //所属材料主键
          let materialOid = types[0];
          //扫描所属行索引
          let attaIndex = types[1];
          if (data.device == "HighCamera") {
            if (data.content.Cameras64) {
              //切割路径
              let base64s = data.content.Cameras64.split(",");
              for (let i = 0; i < base64s.length; i++) {
                if (!base64s[i]) {
                  continue;
                }
                let file = this.base64ToFile(
                  base64s[i],
                  "scanPicture" + i + ".jpg"
                );
                let formD = new FormData();
                formD.append("files", file);
                uploadCaseMaterialFile(formD).then(response => {
                  if (response.data != "") {
                    let resultAtta = [];
                    if (this.attaList.length > 0) {
                      if (this.attaList[attaIndex]) {
                        resultAtta = [].concat(this.attaList[attaIndex]);
                      } else {
                      }
                    }
                    response.data.forEach((data, index) => {
                      resultAtta.push(data); //存放每一个上传的附件
                      //放置材料与附件的关系,生成多条记录
                      let list = {};
                      list.materialOid = materialOid;
                      list.attaOid = data.attaOid;
                      this.materialAttaList.push(list);
                    });
                    this.attaList[attaIndex] = [].concat(resultAtta);
                    this.$forceUpdate(); //强制视图刷新
                  }
                });
              }
            }
          }
        }
      }
      if (data.status == 1) {
        this.$message.error(data.message);
      }
    },
    //初始化socket发生错误回调
    socketError() {
      this.$message.error("请检查设备或连接是否正常");
    },
    scanCard(caseMaterialOid, index) {
      //caseMaterialOid--所属材料主键
      //index--所属操作行索引
      //Device：设备类型、
      let info =
        '{"device":"HighCamera", "type":"' +
        caseMaterialOid +
        "," +
        index +
        '"}';
      //建立socket连接
      this.socketApi.initWebSocket(this.socketError);
      this.socketApi.sendSock(info, this.getConfigResult);
    },
    getElecLicenInfo(materialName, materialOid, caseMaterialOid, index) {
      let userName = "";
      let idCard = "";
      idCard = this.form.applyPerson.credentialNumber;
      userName = this.form.applyPerson.applyUserName;
      if (userName && idCard) {
        queryElecLicenseByDirCode(materialOid, userName, idCard, "").then(
          response => {
            if (response.code !== 200) {
              this.$message.error("请检查证照相关配置！");
              return;
            }

            if (response.data) {
              let res = {};
              let eleArr = [];
              res.originName = response.data.licenseNumber;
              res.attaOid = response.data.elecLicenOid;
              res.materialOid = caseMaterialOid;
              res.elecLicenNumber = response.data.elecLicenNumber;
              res.elecLicenName = response.data.elecLicenName;
              eleArr[0] = res;
              this.attaList[index] = eleArr;
              //判断elemLicenseList是否已经存在办件的证照信息
              if (this.elemLicenseList) {
                this.elemLicenseList.forEach((ite, i) => {
                  if (ite.materialOid == caseMaterialOid) {
                    //移除原来的重新赋值
                    this.elemLicenseList.splice(i, 1);
                  }
                });
              }
              this.elemLicenseList.push(res);
              this.$forceUpdate();
            } else {
              this.$message.warning(
                `未查到姓名【${userName}】,证照号【${idCard}】下材料名为【${materialName}】的证照信息`
              );
              return;
            }
          }
        );
      } else {
        this.$message.warning("申请人/申请单位和证件号不能为空！");
        return;
      }
    },
    //电子证照预览
    viewElemsInfo(eleLincenseOid) {
      getElecLicenUrl(eleLincenseOid).then(response => {
        let urlElem = [];
        if (response.data) {
          urlElem = response.data;
          if (urlElem[0].viewOfdUrl) {
            window.open(urlElem[0].viewOfdUrl, "width=1200px;height=800px;");
          } else {
            this.$message.error("暂时无法查看证照信息！");
          }
        }
      });
    },
    getOneCaseForm() {
      //根据办件业务主键和表单form查询表单配置信息
      caseFormByCaseOid(this.caseOid, "").then(response => {
        if (response.data) {
          this.caseForm = response.data;
          //判断是否需要表单显示
          getSxSerFormBysxSerFormOid(this.caseForm.serFormOid).then(
            response => {
              if (response.data) {
                this.formConfig_show = true;
                this.sxSerForm = response.data;
                this.sxSerForm.regOid = this.caseOid;
              }
            }
          );
        }
      });
    },
    //查看电子表单
    viewFormFilling() {
      if (this.sxSerForm) {
        if (this.sxSerForm.formType == 0) {
          //自定义表单
          if (this.sxSerForm.formAddr) {
            window.open(this.sxSerForm.formAddr, "width=1200px;height=800px;");
          } else {
            this.$message.error("请配置表单地址！");
          }
        } else if (this.sxSerForm.formType == 1) {
          //电子表单
          this.iframVieweState = true;
          this.iframeUrlView =
            process.env.VUE_APP_DZBD_CK_ROUTE_PATH +
            "&reportOid=" +
            this.caseForm.formDataId;
        }
      }
    },
    //关闭表单预览
    closeIframeView() {
      this.iframVieweState = false;
    },
    //下载附件
    downloadFile(attaOid) {
      this.download(attaOid);
    },

    /** 打开高拍仪拍照 */
    openGPYDialog(data, index) {
      console.log(
        "%c [data,index]:",
        "color:red;font-weight:700;",
        data,
        index
      );
      this.qlCaseMaterial = { ...data };
      this.dialogVisible = true;
    }
  },

  destroyed() {
    //在离开此页面的时候主动关闭socket
    this.socketApi.webSocketClose();
  }
};
</script>
<style lang="scss" scoped>
.handle-data {
  display: flex;
  justify-content: flex-end;
  padding: 0 20px;

  &-list {
    flex: 1;
    display: flex;
    flex-direction: column;

    &-item {
      flex: 1;
      display: flex;
      padding: 0 20px;
      justify-content: space-between;
      align-items: center;

      &--link {
        text-decoration: underline;
        cursor: pointer;

        &:hover {
          color: #409eff;
        }
      }
    }
  }
}
.el-scrollbar__wrap {
  overflow: hidden;
}

.dialog-table {
  padding: 5px;
}

ul {
  list-style: none;
}

.qdcg-text {
  text-align: left;
}

.qdcg-text > img {
  width: 24px;
  height: 24px;
  vertical-align: middle;
}

.qdcg-text > span {
  font-size: 15px;
  margin-left: 20px;
}

.qdcg-btn button {
  font-size: 17px;
}

.attachment-list .el-col {
  background-color: #fff;
  padding: 5px 30px 5px 12px;
}

.attachment-list .el-col:nth-child(1) {
  border-top-left-radius: 10px;
  border-bottom-left-radius: 10px;
}

.attachment-list .el-col:nth-child(2) {
  border-top-right-radius: 10px;
  border-bottom-right-radius: 10px;
}
</style>
