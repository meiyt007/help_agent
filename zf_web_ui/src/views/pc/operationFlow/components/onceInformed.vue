<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-10 17:17:15
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-11-10 16:58:39
 * @FilePath: \zf_web_ui\src\views\pc\operationFlow\components\onceInformed.vue
 * @Description: 一次告知弹窗
-->
<template>
  <div class="content-body" v-loading="loadingMaterial">
    <div class="content-area">
      <div class="essentialinformation" ref="fadenum0">
        <p class="dialogContentTitle">事项基本信息</p>
        <div class="essentialinformationForm">
          <template v-for="(item, index) in essentialinformation">
            <template v-if="item.key === 'caseType'">
              <div class="items" :key="index">
                <p class="left">{{ item.name }}</p>
                <p class="right" v-html="item.value"></p>
              </div>
            </template>
            <template v-else-if="
              item.key === 'unionOrganFlagName' || item.key === 'unionOrgan'
            ">
              <div class="items" :key="index">
                <p class="left">{{ item.name }}</p>
                <p class="right">{{ item.value }}</p>
              </div>
            </template>
            <template v-else-if="
              item.key === 'sxServiceExtend.setAccord' ||
              item.key === 'sxAcceptConditions'
            ">
              <div class="items" :key="index">
                <p class="left">{{ item.name }}</p>
                <p class="contain" v-html="item.value"></p>
              </div>
            </template>
            <template v-else-if="item.key === 'onlineApplyLink'">
              <div class="items" :key="index">
                <p class="left">{{ item.name }}</p>
                <p class="contain">
                  <a :href="item.value" target="black">{{ item.value }}</a>
                </p>
              </div>
            </template>
            <template v-else>
              <div class="items" :key="index">
                <p class="left">{{ item.name }}</p>
                <p class="right" v-html="item.value"></p>
              </div>
            </template>
          </template>
        </div>
      </div>
      <!-- <div class="instructionsFilling" ref="fadenum1">
        <p class="dialogContentTitle">填报须知</p>
        <div class="fillContent">
          <div
            class="fillItem"
            v-for="(item, index) in fillNotice"
            :key="index"
          >
            <p v-if="item.question">{{ item.question }}</p>
            <p v-if="item.answer">{{ item.answer }}</p>
          </div>
        </div>
      </div> -->
      <div class="preinspectionConditions" ref="fadenum2">
        <p class="dialogContentTitle">办前须知</p>
        <el-table :data="sxConditionalRules" border>
          <el-table-column align="center" type="index" width="50" label="序号">
          </el-table-column>
          <template v-for="(item, index) in conditionalRulesColumnList">
            <el-table-column :key="item.prop" :prop="item.prop" :label="item.label" align="center"
              show-overflow-tooltip></el-table-column>
          </template>
        </el-table>
      </div>
      <div class="handlingProcess" ref="fadenum3">
        <p class="dialogContentTitle">办理流程</p>
        <div class="body-content">
          <p class="previewBtn" @click="previewProcess">查看办理流程图</p>
        </div>
      </div>
      <!-- <div class="handlingResults situationList" ref="fadenum4">
        <p class="dialogContentTitle">办理结果</p>
        <div class="body-content">
          <div class="workSituationItem">
            <p class="left">审批结果名称</p>
            <p class="right">{{ sxServiceExtend.resultName }}</p>
          </div>
          <div class="workSituationItem">
            <p class="left">送达期限</p>
            <p class="right" v-if="sxService.deliverTerm">
              作出决定之日起{{ sxService.deliverTerm }}个工作日
            </p>
          </div>
          <div class="workSituationItem">
            <p class="left">审批结果样本</p>
            <p class="right">
              <span
                class="sample"
                @click="toPreview(sxServiceExtend.resultSampleAddr)"
                v-if="sxServiceExtend.resultSampleAddr"
                >样本下载</span
              ><span v-else>无</span>
            </p>
          </div>
          <div class="workSituationItem">
            <p class="left">是否生成电子证照</p>
            <p class="right">
              {{ sxService.elecCertProduceFlag === 0 ? "不生成" : "生成" }}
            </p>
          </div>
          <div class="workSituationItem">
            <p class="left">送达方式</p>
            <p class="right">
              {{
                sxService.resultDeliveryWay === "2"
                  ? "公告"
                  : sxService.resultDeliveryWay === "4"
                  ? "物流"
                  : sxService.resultDeliveryWay === "6"
                  ? "自行取件"
                  : "直接送达"
              }}
            </p>
          </div>
          <div class="workSituationItem">
            <p class="left">审批结果类型</p>
            <p class="right">
              {{
                sxServiceExtend.resultSampleType === 0
                  ? "证照"
                  : sxServiceExtend.resultSampleType === 1
                  ? "批文"
                  : "其他"
              }}
            </p>
          </div>
          <div class="workSituationItem">
            <p class="left">审批条件</p>
            <p class="right">{{ sxServiceExtend.censorStandard }}</p>
          </div>
          <div class="workSituationItem">
            <p class="left">审批内容</p>
            <p class="right">{{ sxServiceExtend.exerciseContent }}</p>
          </div>
          <div class="workSituationItem">
            <p class="left">权限划分</p>
            <p class="right">{{ sxServiceExtend.dividStandard }}</p>
          </div>
        </div>
      </div> -->
      <div class="commonProblem" v-if="sxServiceQuestions" ref="fadenum5">
        <p class="dialogContentTitle">常见问题</p>
        <div class="problemList" v-for="(item, index) in sxServiceQuestions" :key="index">
          <p class="left">{{ index + 1 }}</p>
          <div class="rightContent">
            <p>{{ item.title }}</p>
            <p>{{ item.answer }}</p>
          </div>
        </div>
      </div>
      <div class="situation situationList" v-if="showSituation">
        <p class="dialogContentTitle">办事情形</p>
        <div class="body-content">
          <div class="workSituationItem" v-for="(item, index) in workSituation" :key="index">
            <p class="left">{{ item.titleName }}</p>
            <p class="right">{{ item.valueName }}</p>
          </div>
        </div>
      </div>
      <div class="materialList" ref="fadenum6">
        <p class="dialogContentTitle">材料列表</p>
        <el-table :data="materialList" border>
          <el-table-column align="center" type="index" width="50" label="序号">
          </el-table-column>
          <template v-for="(item, index) in materialColumnList">
            <el-table-column :key="index" :prop="item.prop" :label="item.label" header-align="center" align="left"
              v-if="item.prop === 'materialName'" width="300">
              <template slot-scope="scope">
                <div class="preview">
                  <span>{{ scope.row.materialName }}</span>
                  <img style="cursor: default" v-show="scope.row.madeMaterialType === 1"
                    :src="require('@/assets/images/home/exemption.png')" alt="" />

                  <!-- @click="preViewFile(scope.row, 'empty')" -->
                  <a v-if="scope.row.previewEmptyType == 'download'" :href="scope.row.previewEmptyUrl">
                    <img :src="require('@/assets/images/home/emptyTable.png')" alt="" />
                  </a>
                  <img v-if="scope.row.previewEmptyType == 'picture'" @click="preViewFile(scope.row, 'empty')"
                    :src="require('@/assets/images/home/emptyTable.png')" alt="" />

                  <a v-if="(scope.row.preViewSampleUrl && scope.row.previewSampleType == 'download')"
                    :href="scope.row.preViewSampleUrl">
                    <img v-if="scope.row.preViewSampleUrl" :src="require('@/assets/images/home/sampleTable.png')"
                      alt="" />
                  </a>
                  <img v-if="(scope.row.preViewSampleUrl && scope.row.previewSampleType == 'picture')"
                    @click="preViewFile(scope.row, 'sample')" :src="require('@/assets/images/home/sampleTable.png')"
                    alt="" />
                  <!-- <img
                    @click="preViewFile(scope.row, 'sample')"
                    v-if="
                      scope.row.materialSampleAddrYl ||
                      scope.row.materialSimpleAddrYl
                    "
                    :src="require('@/assets/images/home/sampleTable.png')"
                    alt=""
                  /> -->
                </div>
              </template>
            </el-table-column>
            <el-table-column :key="index" :prop="item.prop" :label="item.label" align="center"
              v-else-if="item.prop === 'materialSource'">
              <template slot-scope="scope">
                <span>{{
                    scope.row.materialSource === 0
                      ? "申请人自备"
                      : scope.row.materialSource === 1
                        ? "政府部门核发"
                        : scope.row.otherMaterialSource
                }}</span>
              </template>
            </el-table-column>
            <el-table-column :key="index" :prop="item.prop" :label="item.label" align="center"
              v-else-if="item.prop === 'materialType'" show-overflow-tooltip>
              <template slot-scope="scope">
                <span>{{
                    scope.row.materialType === 0
                      ? "原件"
                      : scope.row.materialType === 1
                        ? "复印件"
                        : "原件和复印件"
                }}</span>
              </template>
            </el-table-column>
            <el-table-column :key="index" :prop="item.prop" :label="item.label" align="center"
              v-else-if="item.prop === 'materialFormat'" show-overflow-tooltip>
              <template slot-scope="scope">
                <span>{{
                    scope.row.materialFormat === 1
                      ? "纸质"
                      : scope.row.materialFormat === 2
                        ? "电子版"
                        : scope.row.materialFormat === 3
                          ? "证照"
                          : scope.row.materialFormat === 4
                            ? "容缺补正"
                            : "告知承诺"
                }}</span>
              </template>
            </el-table-column>
            <el-table-column :key="index" :prop="item.prop" :label="item.label" align="center"
              v-else-if="item.prop === 'materialMustFlag'" show-overflow-tooltip>
              <template slot-scope="scope">
                <!-- <span>{{ scope.row.mustFlag === '0' ? '必要' : scope.row.mustFlag === '1' ? '非必要' : '容缺'
                  }}</span> -->
                <p class="bage-necessery" v-if="scope.row.materialMustFlag === 0">
                  <span> 必要</span>
                </p>
                <span v-if="scope.row.materialMustFlag === 1">非必要</span>
                <span v-if="scope.row.materialMustFlag === 2">容缺</span>
              </template>
            </el-table-column>
            <el-table-column :key="index" v-else-if="item.prop === 'remark'" :prop="item.prop" :label="item.label"
              align="center">
              <template slot-scope="scope">
                <el-tooltip class="item" v-if="scope.row.remark" effect="dark" :content="scope.row.remark"
                  placement="left">
                  <el-button>查看</el-button>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column :key="index" v-else :prop="item.prop" :label="item.label" align="center">
            </el-table-column>
          </template>
        </el-table>
      </div>
    </div>

    <div class="specialEffects" ref="specialEffects" v-show="transmissionData">
      <img ref="specialEffectsImg" :src="
        transmissionCompleted
          ? require('@/assets/images/pad/endDelivery.png')
          : require('@/assets/images/pad/startDelivery.png')
      " alt="" />
    </div>
    <el-dialog v-dialog-drag title="办理流程图" :visible.sync="previewDialog" custom-class="preview-dialog" width="1100px">
      <div class="materialPreviewArea">
        <img :src="sxServiceExtend.handleFlow" alt="" />
      </div>
    </el-dialog>
    <el-dialog v-dialog-drag title="预览" :visible.sync="showPreview" custom-class="preview-dialog" width="80%">
      <div class="previewArea">
        <iframe v-if="preType == 'pdf'" :src="preViewUrl" frameborder="0" style="height: 70vh; width: 100%"></iframe>
        <img v-if="preType == 'img'" :src="preViewUrl" alt="img" />
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {
  getSituationMaterialListByOids,
  viewSxService,
  getCaseTitleValueList,
  queryComPro,
  querySysAttaByOid,
} from "@/api/modules/business";
export default {
  name: "OnceInformed",
  props: {
    serviceOid: {
      type: String,
      default: () => "",
    },
    caseOid: {
      type: String,
      default: () => "",
    },
    valOids: {
      type: String,
      default: () => "",
    },
    staffInformation: {
      type: Array,
      default: () => [],
    },
    currentServiceIndex: {
      type: Number,
      default: () => 0,
    },
    showSituation: {
      type: Boolean,
      default: () => false,
    },
    transmissionData: {
      type: Boolean,
      default: () => false,
    },
  },
  data() {
    return {
      materialList: [],
      sxService: {},
      sxAcceptConditions: [],
      workSituation: [], //办事情形
      handlingResultsList: [], //办理结果
      sxServiceQuestions: [], //常见问题
      sxServiceExtend: {},
      // fillNotice: [], //填报须知
      previewDialog: false,
      showPreview: false,
      transmissionCompleted: false,
      preViewUrl: "",
      preType: '', //预览文件类型
      essentialinformation: [
        {
          key: "serviceName",
          value: "",
          name: "事项名称：",
        },
        // { key: "serviceTypeName", value: "", name: "事项类型：" },
        {
          key: "hostOffices",
          value: "",
          name: "实施主体：",
        },
        {
          key: "caseType",
          value: "",
          name: "办件类型：",
        },
        {
          key: "sxServiceExtend.legalLimit",
          value: "",
          name: "法定时限：",
        },
        {
          key: "sxServiceExtend.promiseLimit",
          value: "",
          name: "承诺时限：",
        },
        {
          key: "onlineApplyLink",
          value: "",
          name: "网办地址：",
        },
        // {
        //   key: "onlineHandleDepth",
        //   value: "",
        //   name: "网办深度：",
        // },
        // {
        //   key: "specialProcedure",
        //   value: "",
        //   name: "特别程序：",
        // },
        // {
        //   key: "unionOrganFlagName",
        //   value: "",
        //   name: "联办机构：",
        // },
        // {
        //   key: "isZjfwName",
        //   value: "",
        //   name: "中介服务：",
        // },
        // {
        //   key: "appointmentFlag",
        //   value: "",
        //   name: "是否支持预约办理：",
        // },
        // {
        //   key: "isSupportTolerance",
        //   value: "",
        //   name: "是否支持容缺受理：",
        // },
        // {
        //   key: "isSupportInformCommitment",
        //   value: "",
        //   name: "是否支持告知承诺：",
        // },
        // {
        //   key: "terminalProcessing",
        //   value: "",
        //   name: "是否支持自助终端办理：",
        // },
        // {
        //   key: "approvedBy",
        //   value: "",
        //   name: "审批对象：",
        // },
        // {
        //   key: "sxAcceptConditions",
        //   value: "",
        //   name: "受理条件：",
        // },
        // {
        //   key: "applyRange",
        //   value: "",
        //   name: "适用范围：",
        // },
        // {
        //   key: "handleAddress",
        //   value: "",
        //   name: "办理地点：",
        // },
        {
          key: "sxServiceExtend.hanleTimeRange",
          value: "",
          name: "办理时间：",
        },
        {
          key: "zxType",
          value: "",
          name: "咨询方式：",
        },
      ],
      materialColumnList: [
        {
          prop: "materialName",
          label: "材料名称",
        },
        {
          prop: "materialSource",
          label: "来源渠道说明",
        },
        {
          prop: "materialType",
          label: "材料类型",
        },
        {
          prop: "materialFormat",
          label: "材料形式",
        },
        {
          prop: "paperNumber",
          label: "份数",
          width: 50,
        },
        {
          prop: "materialMustFlag",
          label: "必要性",
        },
        { prop: "remark", label: "备注" },
      ],
      conditionalRulesColumnList: [
        {
          prop: "rulesName",
          label: "须知",
        },
        {
          prop: "rulesMemo",
          label: "说明",
        },
        {
          prop: "rulesAddress",
          label: "核验地址",
        },
      ],
      loadingMaterial: false,
      sxConditionalRules: [],
      offsetTopList: [],
    };
  },
  mounted() {
    this.getViewSxService();
    this.getCaseTitleValueList();
    this.getSituationMaterialListByOids();
  },
  methods: {
    getAnimate() {
      for (var i = 0; i < 7; i++) {
        const dom = this.$refs[`fadenum${i}`];
        dom.style.transform = "scale(0, 0)";
        // dom.style.display = "none";
        let transitionY = -(dom.offsetTop - 300 + dom.clientHeight / 2);
        this.offsetTopList.push(transitionY);
      }
      this.offsetTopList.forEach((item, index) => {
        // essentialinformation.style
        let style = document.createElement("style");
        style.setAttribute("type", "text/css");
        document.head.appendChild(style);
        let sheet = style.sheet;
        sheet.insertRule(
          `@keyframes fadenum${index}` +
          `{
            from {
                transform: rotate(0);
            }
             to {
                 transform:rotate(90deg) translate(${item}px) scale(0, 0);
                }
            }`,
          0
        );
      });
      this.deleteStylesheetRule();
    },

    //删除样式表
    deleteStylesheetRule() {
      // let myStyles = Array.from(document.styleSheets).filter(
      //   (p) => p.rules[0].name.indexOf("fadenum") > -1
      // );
      Array.from(document.styleSheets).forEach((item) => {
        if (item.rules[0].name) {
          console.log("2222", item.rules[0].name);
        }

        // if (item.rules[0].name.indexOf("fadenum") > -1) {
        //   console.log("2222");
        // }
      });
      // myStyles.forEach((style) => {
      //   if (style.cssRules.length > index) style.deleteRule(index);
      // });
    },

    //流程图预览
    previewProcess() {
      this.previewDialog = true;
    },
    toPreview(data) {
      // window.open(data);
      this.downloadFile(data);
    },
    //获取填报须知
    // getQueryComPro() {
    //   const params = {
    //     serviceOid: this.serviceOid,
    //     optionValOids: this.valOids,
    //   };
    //   queryComPro(params).then((res) => {
    //     if (res.code === 200) {
    //       this.fillNotice = res.data;
    //       if (this.sxService.fillNotice) {
    //         const obj = { question: "", answer: this.sxService.fillNotice };
    //         this.fillNotice.push(obj);
    //       }
    //     }
    //   });
    // },

    //获取情形选项
    getCaseTitleValueList() {
      if (!this.showSituation && !this.caseOid) {
        return;
      }
      getCaseTitleValueList({ caseOid: this.caseOid })
        .then((res) => {
          if (res.code === 200) {
            this.workSituation = res.data;
            let valueArr = [];
            this.workSituation.forEach((item) => {
              valueArr.push(item.valueOid);
            });
            this.valOids = valueArr.toString().replace(/,/g, ";");
            this.getSituationMaterialListByOids();
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    //获取材料列表
    getSituationMaterialListByOids() {
      // if (!this.valOids) {
      //   return;
      // }
      const data = {
        serviceOid: this.serviceOid,
        optionValOids: this.valOids,
      };
      let that = this;
      this.loadingMaterial = true;
      getSituationMaterialListByOids(data)
        .then((res) => {
          if (res.code === 200) {
            res.data = res.data.sort(this.sortArr("materialSort"));

              res.data.forEach(function (file, index) {
                let targetData = res.data[index];
                //样表
                if (targetData.materialSampleAddrYl || targetData.materialSimpleAddrYl) {
                  if (targetData.materialSampleAddrYl) {
                    targetData['preViewSampleUrl'] = targetData.materialSampleAddrYl;
                  }
                  if (targetData.materialSimpleAddrYl) {
                    targetData['preViewSampleUrl'] = targetData.materialSimpleAddrYl;
                  }
                  if (/\.(gif|jpg|jpeg|png|GIF|JPG|PNG|pdf|PDF)$/.test(targetData.preViewSampleUrl)) {
                    targetData['previewSampleType'] = 'picture';
                  } else {
                    targetData['previewSampleType'] = 'download';
                    const attFix = targetData.preViewSampleUrl.substring(targetData.preViewSampleUrl.lastIndexOf(".") + 1)
                    // const url = targetData.preViewSampleUrl + "?attname=" + targetData.materialName + "." + attFix;
                    const url = targetData.preViewSampleUrl + "?attname=" + targetData.simpleOriginName;

                    targetData.preViewSampleUrl = url;
                  }
                }
                let getEmpty = function (oid, callBack) {
                  const params = {
                    oid: oid,
                  };
                  querySysAttaByOid(params).then(item => {
                    if (item.data.filePath) {
                      callBack(item.data.filePath);
                    } else {
                      callBack(0);
                    }
                  })
                }
                //空表
                if (targetData.materialEmptyAddr) {
                   getEmpty(targetData.materialEmptyAddr, function (url) {
                    if (url) {
                      targetData['previewEmptyUrl'] = url;
                      if (
                        /\.(gif|jpg|jpeg|png|GIF|JPG|PNG|pdf|PDF)$/.test(url)
                      ) {
                        targetData['previewEmptyType'] = 'picture';
                      } else {
                        targetData['previewEmptyType'] = 'download';
                        const attFix = targetData['previewEmptyUrl'].substring(targetData['previewEmptyUrl'].lastIndexOf(".") + 1)
                        // const url = targetData['previewEmptyUrl'] + "?attname=" + targetData.materialName.split(".")[0] + "." + attFix;
                        const url = targetData['previewEmptyUrl'] + "?attname=" + targetData.emptyOriginName;

                        targetData['previewEmptyUrl'] = url;
                      }
                    } else {
                      targetData['previewEmptyType'] = '';
                      targetData['previewEmptyUrl'] = '';
                    }

                  })

                }

              })
            
            setTimeout(function () {
              console.log(res.data);
              that.materialList = res.data;
              that.loadingMaterial = false;
            }, 1500)

            this.$emit("setMaterialList", res.data);
            // this.initCase()

          }
        })
        .catch((err) => {
          console.log(err);
          this.loadingMaterial = false;
        });
      // }
      // if (this.caseOid) {
      //   const data = {
      //     caseOid: this.caseOid,
      //   };
      //   this.loadingMaterial = true;
      //   queryQlCaseMaterialListByCaseOid(data)
      //     .then((res) => {
      //       if (res.code === 200) {
      //         this.loadingMaterial = false;
      //         this.materialList = [
      //           ...res.data.autoProduceMaterialList,
      //           ...res.data.needUploadMaterialList,
      //           ...res.data.noSubmissionMaterialList,
      //         ];
      //       }
      //     })
      //     .catch((err) => {
      //       console.log(err);
      //       this.loadingMaterial = false;
      //     });
      // }
    },
    sortArr(attr) {
      return function (a, b) {
        return Number(a[attr]) - Number(b[attr]);
      };
    },
    preViewFile(data, type) {
      console.log("--------"+data);
      if (type == 'sample') {
        this.preViewUrl = data.preViewSampleUrl;
      } else {
        this.preViewUrl = data.previewEmptyUrl;
      }
      if (
        /\.(pdf|PDF)$/.test(this.preViewUrl)
      ) {
        this.preType = 'pdf';
      } else if (/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(this.preViewUrl)) {
        this.preType = 'img';
      }
      this.showPreview = true;
      return;
      this.preViewUrl = '';
      let originName = data.materialName;
      console.log("originName" + originName)
      if (type === "sample") {

        if (data.materialSampleAddrYl) {
          this.preViewUrl = data.materialSampleAddrYl;
          // window.open(data.materialSampleAddrYl);
        }
        if (data.materialSimpleAddrYl) {
          this.preViewUrl = data.materialSimpleAddrYl;
          // window.open(data.materialSimpleAddrYl);
        }
        if (/\.(gif|jpg|jpeg|png|GIF|JPG|PNG|pdf|PDF)$/.test(this.preViewUrl)) {
          this.showPreview = true;
        } else {
          // window.open(this.preViewUrl);
          const attFix = this.preViewUrl.substring(this.preViewUrl.lastIndexOf(".") + 1)
          const url = this.preViewUrl + "?attname=" + originName + "." + attFix;
          // console.log("url:"+url)
          // window.open(url);
          this.downloadFile(this.preViewUrl);
        }
      } else {
        this.querySysAttaByOid(data.materialEmptyAddr, originName);
      }
    },
    querySysAttaByOid(oid, originName) {
      const params = {
        oid: oid,
      };
      querySysAttaByOid(params).then((res) => {
        if (res.code === 200) {
          if (res.data.filePath) {
            this.preViewUrl = res.data.filePath;
            // this.originName = res.data.originName;
            if (
              /\.(gif|jpg|jpeg|png|GIF|JPG|PNG|pdf|PDF)$/.test(this.preViewUrl)
            ) {
              this.showPreview = true;
            } else {
              const attFix = this.preViewUrl.substring(this.preViewUrl.lastIndexOf(".") + 1)
              const url = this.preViewUrl + "?attname=" + res.data.originName.split(".")[0] + "." + attFix;
              console.log("url:" + url)
              // window.open(url);
              this.downloadFile(this.preViewUrl);
              return;
            }
          }
        }
      });
    },
    // 下载文件
    downloadFile(url) {
      $(".uploadBtn").click();
      return;
      // //获得id为downLoadListFrame的frame
      var divFrame = window.parent.document.getElementById("downLoadListFrame")
      //判断是否存在，如果存在先移除，再重新创建
      if (divFrame != null) {
        window.parent.document.body.removeChild(divFrame)
      }

      // var iframe = window.parent.document.createElement("iframe");
      // iframe.setAttribute("id", "downLoadListFrame");
      // //download_file.id = "downFrame";
      // window.parent.document.body.appendChild(iframe);
      // divFrame = window.parent.document.getElementById("downLoadListFrame");
      // //隐式调用，（注意：window.parent.document 适应对于，farme内部代码，调用frame 外部dom；如果项目未用frame，改写成 document即可）
      // divFrame.src = url;
      // divFrame.style.display = "none";
      //重新创建
      var iframe = document.createElement('iframe');
      iframe.setAttribute("id", "downLoadListFrame");
      addEvent("load", iframe, function () { iframeLoad(iframe) });
      iframe.src = url
      // iframe.src= "about:blank";
      document.body.appendChild(iframe);
      setTimeout(function () { iframe.src = ''; }, 1000)

      function iframeLoad(iframe) {
        var src = (iframe.src) ? iframe.src : iframe.contentWindow.location.href;
        // document.body.appendChild(document.createElement("br"));
        // document.body.appendChild(document.createTextNode("IFAME 标记 src 值为 "+ src + " 的 onload 事件触发"));
      }
      function addEvent(eventName, element, fn) {
        if (element.attachEvent) element.attachEvent("on" + eventName, fn);
        else element.addEventListener(eventName, fn, false);
      }
    },
    //获取实施清单信息
    getViewSxService() {
      const data = {
        serviceOid: this.serviceOid,
      };
      viewSxService(data).then((res) => {
        if (res.code === 200) {
          this.sxServiceExtend = res.data.sxServiceExtend ?? {};
          this.sxService = res.data.sxService;
          this.sxAcceptConditions = res.data.sxAcceptConditions;
          this.sxConditionalRules = res.data.sxConditionalRules;
          this.sxServiceQuestions = res.data.sxServiceQuestions;
          // this.getQueryComPro();
          this.getEssentialinformation();
        }
      });
    },
    //获取事项详细
    getEssentialinformation() {
      this.essentialinformation.forEach((item) => {
        if (item.key.indexOf("sxServiceExtend") > -1) {
          const arr = item.key.split(".");
          item.value = this.sxServiceExtend[[arr[1]]];
          if (item.key === "sxServiceExtend.promiseLimit") {
            if (this.sxService.promiseTimeDesc) {
              item.value = this.sxService.promiseTimeDesc;
            } else {
              if (this.sxServiceExtend.promiseLimitType === "W") {
                item.value = item.value ? item.value + "(工作日)" : "";
              }
              if (this.sxServiceExtend.promiseLimitType === "N") {
                item.value = item.value ? item.value + "(自然日)" : "";
              }
              if (this.sxServiceExtend.promiseLimitType === "H") {
                item.value = item.value ? item.value + "(小时)" : "";
              }
            }
          }
          if (item.key === "sxServiceExtend.legalLimit") {
            if (this.sxServiceExtend.legalLimitType === "W") {
              item.value = item.value ? item.value + "(工作日)" : "";
            }
            if (this.sxServiceExtend.legalLimitType === "N") {
              item.value = item.value ? item.value + "(自然日)" : "";
            }
            if (this.sxServiceExtend.legalLimitType === "H") {
              item.value = item.value ? item.value + "(小时)" : "";
            }
          }
          if (item.key === "sxServiceExtend.resultSampleType") {
            if (item.value === "0") {
              item.value = "证照";
            }
            if (item.value === "1") {
              item.value = "批文";
            }
            if (item.value === "2") {
              item.value = "其他";
            }
          }
        } else if (item.key === "sxAcceptConditions") {
          if (this.sxAcceptConditions && this.sxAcceptConditions.length) {
            this.sxAcceptConditions.forEach((ite, index) => {
              item.value +=
                index === this.sxAcceptConditions.length - 1
                  ? ite.conditionText
                  : ite.conditionText + "<br/>";
            });
          }
        }
        // else if (item.key === "serviceObject") {
        //   item.value =
        //     this.sxService[item.key] === "1"
        //       ? "自然人"
        //       : this.sxService[item.key] === "2"
        //       ? "企业法人"
        //       : this.sxService[item.key] === "3"
        //       ? "事业法人"
        //       : this.sxService[item.key] === "4"
        //       ? "社会组织法人"
        //       : this.sxService[item.key] === "5"
        //       ? "非企业法人"
        //       : this.sxService[item.key] === "5"
        //       ? "行政机关"
        //       : "其他组织";
        // }
        else if (item.key === "caseType") {
          item.value = this.sxService[item.key] === 1 ? "即办件" : "承诺件";
        } else if (item.key === "isOnlineHandle") {
          item.value = this.sxService[item.key] === 0 ? "否" : "是";
        } else if (item.key === "appointmentFlag") {
          item.value = this.sxService[item.key] === 0 ? "不支持" : "支持";
        } else if (item.key === "isSupportTolerance") {
          item.value = this.sxService[item.key] === 0 ? "不支持" : "支持";
        } else if (item.key === "isSupportInformCommitment") {
          item.value = this.sxService[item.key] === 0 ? "不支持" : "支持";
        } else if (item.key === "terminalProcessing") {
          item.value = this.sxService[item.key] === 0 ? "不支持" : "支持";
        } else if (item.key === "zxType") {
          if (this.sxService[item.key].indexOf(",") > -1) {
            const arr = this.sxService[item.key].split(",");
            arr.forEach((ite, idx) => {
              const value =
                ite === "0"
                  ? "窗口咨询：" + this.sxService.zxCkText
                  : ite === "1"
                    ? "网上咨询：" + this.sxService.zxWlText
                    : ite === "2"
                      ? "邮件咨询：" + this.sxService.zxYjText
                      : ite === "3"
                        ? "信函咨询：" + this.sxService.zxXjText
                        : "电话咨询：" + this.sxService.zxDhText;
              item.value += idx === arr.length - 1 ? value : value + "<br/>";
            });
          } else {
            item.value =
              this.sxService[item.key] === "0"
                ? "窗口咨询：" + this.sxService.zxCkText
                : this.sxService[item.key] === "1"
                  ? "网上咨询：" + this.sxService.zxWlText
                  : this.sxService[item.key] === "2"
                    ? "邮件咨询：" + this.sxService.zxYjText
                    : this.sxService[item.key] === "3"
                      ? "信函咨询：" + this.sxService.zxXjText
                      : "电话咨询：" + this.sxService.zxDhText;
          }
        } else {
          item.value = this.sxService[item.key];
        }
      });
    },
  },
  watch: {
    // transmissionData: {
    //   handler(val) {
    //     if (val) {
    //       debugger;
    //       this.getAnimate();
    //       setTimeout(() => {
    //         this.transmissionCompleted = true;
    //       }, 6000);
    //       setTimeout(() => {
    //         this.$emit("setTransmissionData", false);
    //       }, 9000);
    //     }
    //   },
    // },
  },
};
</script>
<style lang="scss" scoped>
.uploadBtn {
  opacity: 0;
  width: 0;
  height: 0;
}

.content-body {
  width: 100%;
  height: 100%;

  .content-area {
    width: 100%;
    height: 100%;
  }

  .dialogContentTitle {
    text-align: left;
    padding-left: 1.375rem;
    font-size: 1.625rem;
    font-family: Source Han Sans CN;
    font-weight: 500;
    color: #2a344c;
    position: relative;
    margin: 15px 0;

    &::before {
      content: "";
      position: absolute;
      left: 0;
      top: 50%;
      transform: translateY(-50%);
      width: 0.5625rem;
      height: 1.375rem;
      background: linear-gradient(270deg, #2473ff 0%, #56b1fd 100%);
      border-radius: 0.3125rem;
    }
  }

  .essentialinformation {
    width: 100%;
    height: auto;
    // transform: scale(0, 0);
    animation: fadenum0 3s;

    .dialogContentTitle {
      margin-top: 5px;
    }

    .essentialinformationForm {
      width: 100%;
      height: auto;
      display: flex;
      border: 0.0625rem solid #c8cdd3;
      box-sizing: content-box;
      flex-wrap: wrap;

      .items {
        width: 50%;
        min-height: 5.2rem;
        border-collapse: collapse;
        height: auto;
        display: flex;
        border-collapse: collapse;
        border-bottom: 1px solid #c8cdd3;

        &:nth-child(odd) {
          border-right: 0.0625rem solid #c8cdd3;
        }

        // &:nth-last-child(4) {
        //   width: 100%;
        //   height: auto;
        //   border-right: none;
        // }
        // &:nth-last-child(3) {
        //   width: 100%;
        //   height: auto;
        // }

        &:nth-last-child(2) {
          width: 100%;
          height: auto;
        }

        &:nth-last-child(1) {
          width: 100%;
          height: auto;
          border-bottom: none;
          border-right: none;
        }

        p {
          padding: 0;
          margin: 0;
          height: auto;

          &:nth-child(1) {
            flex: 1;
            display: flex;
            align-items: center;
            justify-content: flex-end;
            padding-right: 1.5rem;
            background-color: rgba(237, 240, 244, 0.55);
            border-right: 1px solid #c8cdd3;
          }

          &:nth-child(2) {
            width: calc(100% - 18rem);
            height: auto;
            padding: 1rem 0.5rem 1rem 1.5rem;
            display: flex;
            flex-wrap: wrap;
            align-items: center;
            text-align: left;

            .sample {
              padding: 0.5rem 0.8rem;
              border-radius: 1.5rem;
              cursor: pointer;
              font-size: 0.875rem;
              box-shadow: 0px 0px 1.5rem 0px rgb(40 107 198 / 31%);
              color: #2473ff;
              border: 1px solid #56b1fd;
            }
          }
        }

        // .setAccord {
        //   width: 100%;
        //   border-bottom: 0.0625rem solid #c8cdd3;
        //   height: auto;
        //   min-height: 5.2rem;
        //   display: flex;

        //   p {
        //     padding: 0;
        //     margin: 0;
        //   }

        //   .left {
        //     width: 18rem;
        //     height: auto;
        //     background: rgba(237, 240, 244, 0.55);
        //     border-right: 1px solid #c8cdd3;
        //     font-size: 1.375rem;
        //     font-family: Source Han Sans CN;
        //     font-weight: 500;
        //     color: #121b2f;
        //     display: flex;
        //     align-items: center;
        //     justify-content: flex-end;
        //     padding-right: 1.5rem;
        //   }

        //   .contain {
        //     width: calc(100% - 18rem);
        //     height: auto;
        //     padding: 0.5rem 0.9375rem;
        //     display: flex;
        //     align-items: center;
        //     justify-content: flex-start;
        //     text-align: left;
        //   }
        // }
      }
    }
  }

  .instructionsFilling {
    width: 100%;
    height: auto;
    animation: fadenum1 4s;

    .fillContent {
      width: 100%;
      height: auto;

      .fillItem {
        width: 100%;
        height: auto;

        p {
          padding: 0;
          margin: 0;
          text-align: left;
          text-indent: 1.375rem;
        }
      }
    }
  }

  .preinspectionConditions {
    width: 100%;
    height: auto;
    animation: fadenum2 4.5s;
  }

  .bage-necessery {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;

    span {
      margin: 0;
      width: 5.7857rem;
      height: 2.5rem;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 1.25rem;
      background-color: #d3e6f5;
      font-size: 1.2857rem;
      font-family: Microsoft YaHei;
      font-weight: 400;
      color: #3ba6ff;
    }
  }

  .preview {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: flex-start;

    span {
      font-size: 14px;
      margin-right: 17px;
      font-family: Microsoft YaHei;
      font-weight: 400;
      color: #2a344c;
    }

    a {
      display: inline-flex;
    }

    img {
      width: 17px;
      cursor: pointer;
      margin-right: 19px;
      cursor: pointer;
    }
  }

  .commonProblem {
    width: 100%;
    height: auto;
    animation: fadenum5 6s;

    .problemList {
      width: 100%;
      height: auto;
      display: flex;
      border: 1px solid #c8cdd3;
      border-bottom: none;

      &:nth-last-child(1) {
        border-bottom: 1px solid #c8cdd3;
      }

      .left {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: center;
        background-color: rgba(237, 240, 244, 0.55);
        margin: 0;
        border-right: 1px solid #c8cdd3;
      }

      .rightContent {
        width: calc(100% - 5rem);
        height: auto;

        p {
          margin: 0;
          width: 100%;
          height: auto;
          min-height: 5rem;
          display: flex;
          align-items: center;
          justify-content: flex-start;
          padding-left: 1.5rem;
          text-align: left;

          &:nth-child(1) {
            border-bottom: 1px solid #c8cdd3;
          }
        }
      }
    }
  }

  .situationList {
    width: 100%;
    height: auto;

    .body-content {
      margin-top: 1.125rem;
      width: 100%;
      height: auto;
      display: flex;
      align-items: flex-start;
      justify-content: flex-start;
      border: 1px solid #c8cdd3;
      box-sizing: content-box;
      flex-wrap: wrap;
      border-collapse: collapse;

      .workSituationItem {
        width: 100%;
        height: auto;
        min-height: 6rem;
        display: flex;
        border-collapse: collapse;
        border-bottom: 1px solid #c8cdd3;

        &:nth-child(even) {
          p {
            &:nth-child(1) {
              border-left: 1px solid #c8cdd3;
            }
          }
        }

        &:nth-last-child(1) {
          border: none;
        }

        p {
          padding: 0;
          margin: 0;
          height: auto;

          &:nth-child(1) {
            flex: 1;
            display: flex;
            align-items: center;
            justify-content: flex-end;
            padding-right: 1.5rem;
            background-color: rgba(237, 240, 244, 0.55);
            border-right: 1px solid #c8cdd3;
          }

          &:nth-child(2) {
            width: calc(100% - 18rem);
            height: auto;
            padding: 1rem 0.5rem 1rem 1.5rem;
            display: flex;
            flex-wrap: wrap;
            align-items: center;
            text-align: left;

            .sample {
              padding: 0.5rem 0.8rem;
              border-radius: 1.5rem;
              cursor: pointer;
              font-size: 0.875rem;
              box-shadow: 0px 0px 1.5rem 0px rgb(40 107 198 / 31%);
              color: #2473ff;
              border: 1px solid #56b1fd;
            }
          }
        }
      }
    }
  }

  .handlingProcess {
    width: 100%;
    height: auto;
    animation: fadenum3 5s;

    .body-content {
      width: 100%;
      height: auto;

      .previewBtn {
        width: 15rem;
        margin: 0;
        padding: 0.8rem 0;
        border-radius: 0.9375rem;
        background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
        color: #fff;
        cursor: pointer;
      }

      img {
        width: 80%;
      }
    }
  }

  .handlingResults {
    animation: fadenum4 5.5s;

    .body-content {
      .workSituationItem {
        width: 50%;

        &:nth-last-child(1),
        &:nth-last-child(2),
        &:nth-last-child(3) {
          width: 100%;

          p {
            &:nth-child(1) {
              border-left: none;
            }
          }
        }
      }
    }
  }

  .materialList {
    width: 100%;
    height: auto;
    animation: fadenum6 6.5s;
    padding-bottom: 1.5rem;

    .el-table {
      margin-top: 1.125rem;
    }
  }

  .specialEffects {
    width: 100%;
    height: 80vh;
    position: absolute;
    left: 0;
    top: 0;
    display: flex;
    align-items: center;
    justify-content: center;

    img {
      width: 17.5294rem;
      height: 22.5294rem;
    }
  }
}

// @keyframes fadenum1 {
//   100% {
//     transform: rotate(90deg) translate(-200.0008px) scale(0, 0);
//     display: none !important;
//   }
// }

// @keyframes fadenum2 {
//   100% {
//     transform: rotate(90deg) translate(-690px) scale(0, 0);
//     display: none !important;
//   }
// }

// @keyframes fadenum3 {
//   100% {
//     transform: rotate(90deg) translate(-850px) scale(0, 0);
//     display: none !important;
//   }
// }
// @keyframes fadenum4 {
//   100% {
//     transform: rotate(90deg) translate(-1000px) scale(0, 0);
//     display: none !important;
//   }
// }
// @keyframes fadenum5 {
//   100% {
//     transform: rotate(90deg) translate(-1570px) scale(0, 0);
//     display: none !important;
//   }
// }
// @keyframes fadenum6 {
//   100% {
//     transform: rotate(90deg) translate(-1650px) scale(0, 0);
//     display: none !important;
//   }
// }
// @keyframes fadenum7 {
//   // 50% {
//   //   transform: rotate(90deg) translate(-2300px) scale(0.5, 0.5);
//   //   display: none !important;
//   // }
//   100% {
//     transform: rotate(90deg) translate(-1900px) scale(0, 0);
//     display: none !important;
//   }
// }
.el-dialog__wrapper {
  .el-dialog {
    .el-dialog__body {
      .materialPreviewArea {
        width: 100%;
        height: 85vh;

        img {
          width: 100%;
        }
      }
    }
  }
}

.el-table .el-table__cell {
  text-align: center;
}

.el-table tr>td:nth-child(2) {
  text-align: left;
}
</style>
