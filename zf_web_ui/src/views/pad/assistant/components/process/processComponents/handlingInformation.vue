<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-08-29 15:41:39
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-10-27 17:19:08
 * @FilePath: \zf_web_ui\src\views\pc\operationFlow\components\handlingInformation.vue
 * @Description: 办件详情
-->
<template>
  <div class="handlingInformation">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="基本信息" name="essentialInformation">
        <!-- <p>{{  useInfo.serviceType }}</p> -->
        <div class="body-content">
          <template v-for="(item, index) in essentialinformation">
            <div
              class="items"
              :key="index"
              v-if="item.key === 'pushType' && useInfo.serviceType === '4'"
            >
              <template>
                <el-input v-model="item.value" readonly>
                  <template slot="prepend">{{ item.name }}</template>
                </el-input>
              </template>
            </div>
            <div
              class="items"
              :class="useInfo.serviceType"
              :key="index"
              v-else-if="item.key === 'pushMemo' && useInfo.serviceType === '4'"
            >
              <template>
                <el-input v-model="item.value" readonly>
                  <template slot="prepend">{{ item.name }}</template>
                </el-input>
              </template>
            </div>
            <div class="items" :key="index">
              <template>
                <el-input v-model="item.value" readonly>
                  <template slot="prepend">{{ item.name }}</template>
                </el-input>
              </template>
            </div>
          </template>
        </div>
      </el-tab-pane>
      <el-tab-pane
        label="办事情形"
        v-if="useInfo.serviceType != '1'"
        name="workSituation"
      >
        <div class="body-content" v-loading="workSituationLoading">
          <div
            class="workSituationItem"
            v-for="(item, index) in workSituation"
            :key="index"
          >
            <p class="left">{{ (item.titleName).split("?")[0] }}</p>
            <p class="right">{{ item.valueName }}</p>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="电子表单" v-if="useInfo.serviceType === '2'">
        <!-- <div class="tabHeader" v-if="useInfo.serviceType === '2'">
          <p
            class="tabLeft"
            @click="changeTab('1')"
            :class="tabType === '1' ? 'active' : ''"
          >
            基本信息
          </p>
          <p
            class="tabRight"
            @click="changeTab('2')"
            :class="tabType === '2' ? 'active' : ''"
            v-html="useInfo.serviceMemo"
          ></p>
        </div> -->
        <div
          class="el-table__header-wrapper"
          v-loading="formLoading"
          v-for="item in formFillInfosList"
          :key="item.id"
          :label="item.formName"
          :name="item.name"
        >
          <FormReport
            v-if="item.reportForm.designOid"
            :ref="`reportForm_${item.designOid}`"
            :key="item.designOid"
            :reportForm="item.reportForm"
            :authorizeKey="item.authorizeKey"
            :formNames="item.formNames"
            :isZc="isZc"
            :isOneLevelBindFormData="true"
            @sendRes="item.sendRes"
          />
        </div>
      </el-tab-pane>
      <el-tab-pane
        label="收取材料"
        v-if="useInfo.serviceType === '2'"
        name="colMaterials"
      >
        <el-table
          border
          :data="materialList"
          ref="table"
          height="50vh"
          style="width: 100%; margin-left: 1px"
          v-loading="tableLoading"
          :row-key="getRowKey"
          :expand-row-keys="expandRowKeys"
        >
          <el-table-column type="index" label="序号" width="50">
          </el-table-column>
          <template v-for="(item, index) in columnList">
            <!-- <el-table-column :label="item.label" :prop="item.prop" v-if="item.prop === 'operation'" align="center">
              <template slot-scope="scope">
                <p class="operaBtn" v-if="scope.row.collectionType === '1'">无附件</p>
                <p class="operaBtn" v-else><img :src="require('@/assets/images/home/search.png')" alt="">查看附件</p>
              </template>
            </el-table-column> -->
            <el-table-column
              :key="index"
              :label="item.label"
              :prop="item.prop"
              v-if="item.prop === 'collectionFlag'"
            >
              <template slot-scope="scope">
                <span>{{
                  scope.row.collectionFlag === "1" ? "是" : "否"
                }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :key="index"
              :label="item.label"
              :prop="item.prop"
              v-else-if="item.prop === 'collectionType'"
            >
              <template slot-scope="scope">
                <span>{{
                  scope.row.collectionType === "1" ? "纸质收取" : "附件上传"
                }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :key="index"
              :label="item.label"
              :prop="item.prop"
              v-else
              :show-overflow-tooltip="true"
            >
            </el-table-column>
          </template>
          <el-table-column type="expand" width="1">
            <template slot-scope="scope">
              <div class="handle-data-list">
                <div
                  class="handle-data-list-item"
                  v-for="(dataAtta, idx) in scope.row.qlCaseMaterialAttaList"
                  :key="idx"
                >
                  <div class="grid-content qdcg-text">
                    <div
                      @click="viewFile(dataAtta.attaOid)"
                      class="handle-data-list-item--link"
                    >
                      {{ dataAtta.originName }}
                    </div>
                  </div>
                  <div class="btnArea">
                    <el-button
                      type="text"
                      icon="el-icon-view"
                      @click="viewFile(dataAtta)"
                      >查看</el-button
                    >
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
    <el-dialog
      v-dialog-drag
      title="预览"
      :visible.sync="showPreview"
      custom-class="preview-dialog"
      width="80%"
    >
      <div class="previewArea">
        <iframe
          :src="preViewUrl"
          frameborder="0"
          style="height: 70vh; width: 100%"
        ></iframe>
        <!-- <img :src="preViewUrl" alt="二维码" /> -->
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {
  getFormFillInfos,
  getAllBasicFieldByOid,
  queryQlCaseByCaseOid,
  getCaseTitleValueList,
  getBasicAndFormFieldRel,
  queryQlCaseMaterialListByCaseOid,
  queryFormInfoByCaseOid,
} from "@/api/modules/business";
import { deepClone } from "@/utils/index";
import { cloneDeep } from "lodash";
export default {
  name: "handlingInformation",
  props: {
    caseOid: {
      type: String,
      default: () => "",
    },
    useInfo: {
      type: Object,
      default: () => {},
    },
  },
  data() {
    return {
      activeName: "essentialInformation",
      workSituationLoading: false,
      formLoading: false,
      tableLoading: false,
      showPreview: false,
      valOids: "",
      essentialinformation: [
        {
          key: "name",
          value: "",
          name: "办事人姓名：",
        },
        {
          key: "companyName",
          value: "",
          name: "企业名称：",
        },
        {
          key: "serviceType",
          value: "",
          name: "服务类型：",
        },
        {
          key: "sxServiceName",
          value: "",
          name: "办理事项：",
        },
        {
          key: "sxServiceId",
          value: "",
          name: "事项实施编号：",
        },
        {
          key: "serviceMemo",
          value: "",
          name: "服务内容：",
        },
        {
          key: "serviceStatus",
          value: "",
          name: "服务状态：",
        },
      ],
      workSituation: [],
      materialList: [],
      columnList: [
        { label: "材料名称", prop: "materialName" },
        { label: "是否收取", prop: "collectionFlag" },
        { label: "收取数量", prop: "collectionNumber" },
        { label: "收取方式", prop: "collectionType" },
        { label: "收取时间", prop: "collectionDate" },
        // { label: '操作', prop: 'operation' },
      ],
      formFillInfosList: [],
      formFieldRelList: [],
      formOids: [],
      getRowKey(row) {
        return row.id; // 返回当前行id
      },
      expandRowKeys: [],
      preViewUrl: "",
      tabType: "1",
      isZc: 0,
      fillUserInfo: {},
    };
  },
  mounted() {
    this.getUserInfo();
    this.getQueryQlCaseApplayByCaseOid();
    this.getCaseTitleValueList();
    this.getQueryQlCaseMaterialListByCaseOid();
    this.getQueryFormInfoByCaseOid();
    // this.getBasicAndFormFieldRel();
  },
  methods: {
    //基础表单和电子表单关联字段
    getBasicAndFormFieldRel() {
      const params = {
        serviceOid: this.useInfo.sxServiceId,
      };
      getBasicAndFormFieldRel(params)
        .then((res) => {
          if (res.code === 200) {
            this.formFieldRelList = res.data;
            this.getAllBasicFieldByOid();
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    //表单预填信息
    getAllBasicFieldByOid() {
      const data = {
        caseOid: this.caseOid,
      };
      getAllBasicFieldByOid(data).then((res) => {
        if (res.code === 200) {
          this.formFieldRelList.forEach((item) => {
            this.formFieldResult[item.formField] =
              this.fillUserInfo[item.baseFormField];
          });
          this.formFieldResult = Object(
            this.formFieldResult,
            res.data.formFieldResult
          );
          this.getFormFillInfos();
        }
      });
    },

    //表单字段及预填信息
    getFormFillInfos() {
      const data = {
        serviceOid: this.useInfo.sxServiceId,
        caseOid: this.caseOid,
        valOids: this.valOids,
      };
      getFormFillInfos(data)
        .then((res) => {
          if (res.code === 200) {
            this.formOids = [];
            res.data.forEach((item) => {
              const obj = {
                designOid: item.designOid,
                authorizeKey: item.authorizeKey,
                formCode: item.formCode,
                formName: item.formName,
              };
              this.formOids.push(obj);
            });
            let formFillInfosList = [];
            formFillInfosList =
              res.data?.map?.((item, idx) => {
                return {
                  ...item,
                  childFormName: (item?.childFormName?.split(",") ?? []).filter(
                    (item) => item
                  ),
                  name: String(idx + 2),
                  elecFormTimer: null,
                  fillUserInfo: deepClone(this.fillUserInfo),
                  reportForm: deepClone({
                    designOid: item.designOid,
                    authorizeKey: item.authorizeKey,
                    reportOid: this.caseOid,
                    formData: JSON.stringify(
                      Object.assign(item.formVals, this.formFieldResult)
                    ),
                  }),
                  // 保存表单后返回的接口数据
                  sendRes: (ret) => {
                    return JSON.parse(ret);
                  },
                };
              }) ?? [];
            this.$emit("setFormFillInfosList", cloneDeep(formFillInfosList));
          }
        })
        .catch((err) => {
          console.log(err);
          this.loadingForm = false;
        });
    },

    changeTab(type) {
      this.tabType = type;
    },
    //预览附件
    viewFile(data) {
      if (data.qlSysAtta) {
        this.preViewUrl = data.qlSysAtta.fastdfsNginxUrl;
      } else {
        if (data.fastdfsNginxUrl) {
          this.preViewUrl = data.fastdfsNginxUrl;
        } else {
          this.preViewUrl = data.templatePdfUrl;
        }
      }
      if (/\.(gif|jpg|jpeg|png|GIF|JPG|PNG|pdf|PDF)$/.test(this.preViewUrl)) {
        this.showPreview = true;
      } else {
        window.open(this.preViewUrl);
      }
    },
    getUserInfo() {
      if (this.useInfo.serviceType === "4") {
        const obj1 = {
          key: "pushType",
          value: "",
          name: "推送类型：",
        };
        const obj2 = {
          key: "pushMemo",
          value: "",
          name: "推送内容：",
        };
        this.essentialinformation.push(obj1, obj2);
      }
      this.essentialinformation.forEach((item) => {
        if (item.key === "serviceStatus") {
          item.value = !this.useInfo[item.key]
            ? ""
            : this.useInfo[item.key] === "1"
            ? "已完成"
            : "暂存";
        } else if (item.key === "serviceType") {
          item.value = !this.useInfo[item.key]
            ? ""
            : this.useInfo[item.key] === "1"
            ? "咨询"
            : this.useInfo[item.key] === "2"
            ? "材料准备"
            : this.useInfo[item.key] === "3"
            ? "收件"
            : "一键推送";
        } else if (item.key === "sxServiceName") {
          item.value =
            this.useInfo["serviceName"] ?? this.useInfo["sxServiceName"];
        } else if (item.key === "pushType") {
          item.value = !this.useInfo[item.key]
            ? ""
            : this.useInfo[item.key] === "1"
            ? "办事指南"
            : this.useInfo[item.key] === "2"
            ? "办件信息"
            : "材料信息";
        } else {
          item.value = this.useInfo[item.key];
        }
      });
    },

    //获取办件申请信息
    getQueryQlCaseApplayByCaseOid() {
      queryQlCaseByCaseOid({ caseOid: this.caseOid }).then((res) => {
        if (res.code === 200) {
          this.fillUserInfo = res.data;
        }
      });
    },

    //获取情形选项
    getCaseTitleValueList() {
      this.workSituationLoading = true;
      getCaseTitleValueList({ caseOid: this.caseOid })
        .then((res) => {
          this.workSituationLoading = false;
          if (res.code === 200) {
            this.workSituation = res.data;
            let valOids = [];
            this.workSituation.forEach((item) => {
              valOids.push(item.valueOid);
            });
            this.valOids = valOids.toString();
            this.getFormFillInfos();
          }
        })
        .catch((err) => {
          console.log(err);
          this.workSituationLoading = false;
        });
    },
    //获取办件列表
    getQueryQlCaseMaterialListByCaseOid() {
      this.tableLoading = true;
      queryQlCaseMaterialListByCaseOid({ caseOid: this.caseOid })
        .then((res) => {
          this.tableLoading = false;
          if (res.code === 200) {
            this.materialList = [
              ...res.data.autoProduceMaterialList,
              ...res.data.needUploadMaterialList,
              ...res.data.noSubmissionMaterialList,
            ];
            this.materialList.forEach((item, index) => {
              item.rowIndex = index;
              if (item.qlCaseMaterialAttaList.length) {
                item.qlCaseMaterialAttaList.forEach((ite) => {
                  if (ite.qlSysAtta) {
                    ite.originName = ite.qlSysAtta.originName;
                  } else {
                    ite.originName = item.materialName;
                  }
                });
                this.expandRowKeys.push(item.id);
              }
            });
          }
        })
        .catch((err) => {
          console.log(err);
          this.tableLoading = false;
        });
    },

    //获取电子表单
    getQueryFormInfoByCaseOid() {
      this.formLoading = true;
      queryFormInfoByCaseOid({ caseOid: this.caseOid })
        .then((res) => {
          this.formLoading = false;
          if (res.code === 200) {
            this.formFillInfosList =
              res.data?.map?.((item, idx) => {
                return {
                  ...item,
                  childFormName: (item?.childFormName?.split(",") ?? []).filter(
                    (item) => item
                  ),
                  name: String(idx + 2),
                  elecFormTimer: null,
                  reportForm: {
                    designOid: item.designOid,
                    authorizeKey: item.authorizeKey,
                    reportOid: this.caseOid,
                    formData: JSON.stringify(item.formVals),
                  },

                  // 保存表单后返回的接口数据
                  sendRes: (ret) => {
                    return JSON.parse(ret);
                  },
                };
              }) ?? [];
          }
        })
        .catch((err) => {
          console.log(err);
          this.formLoading = false;
        });
    },
    handleClick() {},
  },
};
</script>
<style lang="scss" scoped>
.handlingInformation {
  width: 100%;
  height: 100%;

  .el-tabs {
    width: 100%;
    height: 100%;

    ::v-deep .el-tabs__content {
      width: 100%;
      height: calc(100% - 55px);
      overflow-y: auto;

      &::-webkit-scrollbar {
        width: 0.4375rem;
        background-color: #fff;
      }

      &::-webkit-scrollbar-thumb {
        width: 0.4375rem;
        height: 0.625rem !important;
        // background: linear-gradient(270deg, #bf9e63 0%, #dfca98 100%);
        background: linear-gradient(270deg, #2473ff 0%, #56b1fd 100%);
        border-radius: 4px;
      }

      .el-tab-pane {
        width: 99.9%;
        height: auto;

        .body-content {
          // width: 100%;
          height: auto;
          display: flex;
          align-items: flex-start;
          justify-content: flex-start;
          border: 0.0625rem solid #c8cdd3;
          margin-top: 1.0625rem;
          box-sizing: content-box;
          flex-wrap: wrap;

          .items {
            width: 50%;
            height: 3.1875rem;
            border-collapse: collapse;

            &:nth-child(odd) {
              .el-input {
                border-right: 0.0625rem solid #c8cdd3;
              }
            }

            &:nth-last-child(1) {
              width: 100%;
            }

            .el-input {
              width: 100%;
              height: 3.1875rem;
              border-bottom: 0.0625rem solid #c8cdd3;
              box-sizing: content-box;
              border-collapse: collapse;
              display: flex;
              align-items: center;
              justify-content: flex-start;

              .el-input-group__prepend {
                width: 13.85rem;
                height: 100%;
                display: flex;
                align-items: center;
                justify-content: flex-end;
                background: rgba(237, 240, 244, 0.55);
                border: none;
                padding: 0;
                padding-right: 1.5rem;
                font-size: 1.375rem;
                font-family: Source Han Sans CN;
                font-weight: 500;
                color: #121b2f;
                border-right: 0.0625rem solid #c8cdd3;
                border-collapse: collapse;
                text-align: right;
              }

              .el-input__inner {
                width: calc(100% - 13.85rem);
                height: 90%;
                line-height: 90%;
                border: none;
                padding: 0 0.9375rem !important;
              }
            }

            .step {
              width: 100%;
              height: 100%;
              display: flex;
              align-items: center;

              .left {
                width: 13.85rem;
                height: 100%;
                background: rgba(237, 240, 244, 0.55);
                border-right: 1px solid #c8cdd3;
                font-size: 1.375rem;
                font-family: Source Han Sans CN;
                font-weight: 500;
                color: #121b2f;
                display: flex;
                align-items: center;
                justify-content: flex-end;
                padding-right: 1.5rem;
              }

              .right {
                width: calc(100% - 13.85rem);
                height: 100%;
                display: flex;
                flex-direction: column;
                align-items: flex-start;
                justify-content: space-around;
                padding: 0 0.9375rem;
              }

              p {
                padding: 0;
                margin: 0;
              }
            }
          }

          .workSituationItem {
            width: 100%;
            height: auto;
            display: flex;
            border-collapse: collapse;
            border-bottom: 1px solid #c8cdd3;

            p {
              padding: 0;
              margin: 0;

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
                padding: 1.5rem;
                display: flex;
                flex-wrap: wrap;
                align-items: center;
                justify-content: flex-start;
              }
            }
          }
        }
      }
    }
  }
}

.operaBtn {
  padding: 0;
  margin: 0;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5714rem;
  font-family: Source Han Sans CN;
  font-weight: 400;
  color: #2473ff;

  img {
    width: 1.8rem;
  }
}

.el-table {
  width: 100%;
  max-height: 80%;

  ::v-deep .el-table__body-wrapper {
    &::-webkit-scrollbar {
      width: 0.4375rem;
      background-color: #fff;
    }

    &::-webkit-scrollbar-thumb {
      width: 0.4375rem;
      height: 0.625rem !important;
      // background: linear-gradient(270deg, #bf9e63 0%, #dfca98 100%);
      background: linear-gradient(270deg, #2473ff 0%, #56b1fd 100%);
      border-radius: 4px;
    }

    .el-table__body {
      .el-table__row {
        .el-table__cell {
          .cell {
            .preview {
              width: 100;
              height: 100%;
              cursor: pointer;
              display: flex;
              align-items: center;
              justify-content: flex-start;

              img {
                width: 2rem;
                margin-right: 0.5rem;
              }
            }
          }
        }
      }
    }
  }

  .el-table__expanded-cell {
    width: 100%;
    height: auto;

    .handle-data-list {
      width: 100%;
      height: auto;

      .handle-data-list-item {
        width: 100%;
        height: 3.125rem;
        padding: 0 2.5rem;
        display: flex;
        align-items: center;
        justify-content: space-between;
      }
    }
  }

  ::v-deep .el-table__expand-icon {
    visibility: hidden;
  }
}
</style>
