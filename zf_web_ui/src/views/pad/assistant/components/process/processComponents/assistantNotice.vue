<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-11-10 13:41:14
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-11-10 14:18:56
 * @FilePath: \zf_web_ui\src\views\pad\assistant\components\process\processComponents\assistantNotice.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div class="assistantNotice-content">
    <div class="header">
      <div class="left">
        <span>上海市黄浦区政务服务中心监制</span>
        <!-- <vue-barcode
            value="barcodeValue"
            height="50%"
            displayValue="false"
          ></vue-barcode> -->
      </div>
      <div class="right">
        <img :src="qrcodeValue" alt="" />
      </div>
    </div>
    <p class="assNum">
      帮办号：<span>{{ basicUserInfo.id }}</span>
    </p>
    <p class="title">上海市黄浦区政务服务 帮办告知单</p>
    <div class="situationList">
      <div class="body-content">
        <div class="workSituationItem">
          <p class="left">
            办理事项：{{ baseUserInfo.specificMatters.serviceName }}
          </p>
        </div>
        <div class="workSituationItem">
          <p class="left">
            {{
              baseUserInfo.fillUserInfo.applyUserType === "1"
                ? "申请人："
                : "申请单位："
            }}
            {{ baseUserInfo.fillUserInfo.applyUserName }}
          </p>
        </div>
        <div class="workSituationItem">
          <p class="left">经办人：{{ baseUserInfo.name }}</p>
          <p class="left">经办人电话：{{ baseUserInfo.phone }}</p>
        </div>
        <div class="workSituationItem">
          <p class="left">帮办时间：{{ serviceTime }}</p>
<!--          <p class="left">-->
<!--            办理状态：{{ caseStatus === 0 ? "暂存" : "已完成" }}-->
<!--          </p>-->
        </div>
        <div class="time">
          <p></p>
        </div>
        <div class="workSituationItem"></div>
      </div>
    </div>
    <div class="materialList">
      <p class="dialogContentTitle">本次已准备完成的材料</p>
      <el-table
        :data="submittedDataList"
        border
        v-loading="tableLoading"
        element-loading-text="拼命加载中"
        element-loading-background="transparent"
      >
        <el-table-column type="index" width="50" label="序号">
        </el-table-column>
        <template v-for="(item, index) in submittedColumns">
          <el-table-column
            :key="index"
            :prop="item.prop"
            :label="item.label"
            align="center"
            v-if="item.prop === 'materialFormat'"
            show-overflow-tooltip
          >
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
          <el-table-column
            :key="index"
            :prop="item.prop"
            :label="item.label"
            align="center"
            v-else-if="item.prop === 'materialType'"
            show-overflow-tooltip
          >
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
          <el-table-column
            v-else
            :key="index"
            :prop="item.prop"
            :label="item.label"
            align="center"
          ></el-table-column>
        </template>
      </el-table>
    </div>
    <div class="materialList" v-if="needSubmittedDataList.length">
      <p class="tips">您还需要提交以下材料才能完成该事项的申请</p>
      <el-table
        :data="needSubmittedDataList"
        border
        v-loading="tableLoading"
        element-loading-text="拼命加载中"
        element-loading-background="transparent"
      >
        <el-table-column type="index" width="50" label="序号">
        </el-table-column>
        <template v-for="(item, index) in needSubmittedColumns">
          <el-table-column
            :key="index"
            :prop="item.prop"
            :label="item.label"
            align="center"
            v-if="item.prop === 'materialFormat'"
            show-overflow-tooltip
          >
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
          <el-table-column
            :key="index"
            :prop="item.prop"
            :label="item.label"
            align="center"
            v-else-if="item.prop === 'materialType'"
            show-overflow-tooltip
          >
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
          <el-table-column
            v-else
            :key="index"
            :prop="item.prop"
            :label="item.label"
            align="center"
          ></el-table-column>
        </template>
      </el-table>
    </div>
    <div class="signOff">
      <p>帮办服务人员：{{ basicUserInfo.name }}</p>
      <p>帮办服务人员编号：{{ basicUserInfo.id }}</p>
      <p>咨询电话：021-63529090</p>
    </div>
<!--    <div class="time">-->
<!--      <p>{{ time }}</p>-->
<!--    </div>-->
    <p class="tips">
      （本告知单一式两份，一份交予经办人，一份帮办服务部门留存。）
    </p>
  </div>
</template>
<script>
import { formatDateSimple } from "@/utils/index";
// import VueBarcode from "vue-barcode";
import QRCode from "qrcode";
import { setFormatDate } from "@/utils/index";
import {
  queryQlCaseMaterialListByCaseOid,
  queryQlCaseByCaseOid,
} from "@/api/modules/business.js";
export default {
  name: "AssistantNotice",
  // components: {
  //   VueBarcode,
  // },
  props: {
    baseUserInfo: {
      type: Object,
      default: () => {},
    },
  },
  data() {
    return {
      tableLoading: false,
      serviceTime: "",
      barcodeValue: "测试",
      qrcodeValue: "",
      submittedDataList: [],
      needSubmittedDataList: [],
      submittedColumns: [
        { label: "材料名称", prop: "materialName" },
        { label: "材料类型", prop: "materialType" },
        { label: "材料形式", prop: "materialFormat" },
        { label: "已提交份数", prop: "collectionFlag" },
      ], //已提交材料列表
      needSubmittedColumns: [
        { label: "材料名称", prop: "materialName" },
        { label: "材料类型", prop: "materialType" },
        { label: "材料形式", prop: "materialFormat" },
        { label: "份数", prop: "needPaper" },
      ], //还需提交材料列表
      materialList: [],
      caseStatus: null,
      time: null,
    };
  },
  computed: {
    basicUserInfo() {
      return this.$store.state.user.basicUserInfo;
    },
  },
  mounted() {
    this.serviceTime = formatDateSimple(new Date());
    this.getSituationMaterialListByOids();
    this.getQueryQlCaseByCaseOid();
    this.getQrcodeValue();
    this.getTime();
  },
  methods: {
    //获取二维码
    async getQrcodeValue() {
      this.qrcodeValue = await QRCode.toDataURL("http://172.21.177.44:8085/");
    },
    getTime() {
      const date = new Date();
      this.time = setFormatDate(date, "yyyy-MM-dd");
    },
    //查询办件信息
    getQueryQlCaseByCaseOid() {
      queryQlCaseByCaseOid({ caseOid: this.baseUserInfo.caseOid }).then(
        (res) => {
          if (res.code === 200) {
            this.caseStatus = res.data.caseStatus;
          }
        }
      );
    },

    //获取材料列表
    getSituationMaterialListByOids() {
      const data = {
        caseOid: this.baseUserInfo.caseOid,
      };
      this.tableLoading = true;
      queryQlCaseMaterialListByCaseOid(data)
        .then((res) => {
          if (res.code === 200) {
            this.submittedDataList = [];
            this.tableLoading = false;
            res.data.autoProduceMaterialList.forEach((item) => {
              item.flag = "auto";
            });
            res.data.needUploadMaterialList.forEach((item) => {
              item.flag = "needUpload";
            });
            res.data.noSubmissionMaterialList.forEach((item) => {
              item.flag = "noSubmission";
            });
            this.materialList = [
              ...res.data.autoProduceMaterialList,
              ...res.data.needUploadMaterialList,
              ...res.data.noSubmissionMaterialList,
            ];
            this.materialList.forEach((item, index) => {
              item.rowIndex = index;
              if (item.qlCaseMaterialAttaList.length) {
                const result = item.qlCaseMaterialAttaList.some((item) => {
                  return item.qlSysAtta || item.materialType === "upload";
                });
                if (!result) {
                  if (!item.collectionFlag) {
                    item.needPaper = item.paperNumber;
                  }
                  this.needSubmittedDataList.push(item);
                }
                if (result) {
                  this.submittedDataList.push(item);
                }
              } else {
                item.needPaper = item.paperNumber;
                this.needSubmittedDataList.push(item);
              }
            });
          }
        })
        .catch((err) => {
          this.tableLoading = false;
        });
    },
  },
};
</script>
<style lang="scss">
.assistantNotice-content {
  width: 100%;
  height: 96%;
  padding: 1.5rem;
  .header {
    width: 100%;
    height: 8.8235rem;
    display: flex;
    align-items: center;
    justify-content: space-between;
    .left {
      width: 40%;
      height: 100%;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      align-items: flex-start;
      span {
        margin-left: 1rem;
      }
    }
    .right {
      width: 40%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: flex-end;
      img {
        width: 8.8235rem;
      }
    }
  }
  .assNum {
    padding: 0;
    margin: 0;
    padding: 1rem 0;
    text-align: left;
    margin-left: 1rem;
  }
  .title {
    padding: 0;
    margin: 0;
    padding: 2rem 0 2.5rem 0;
    text-align: center;
    width: 100%;
    font-size: 2.1176rem;
    font-family: Source Han Sans CN;
    font-weight: 600;
  }
  .situationList {
    width: 100%;
    height: auto;
    overflow-y: auto;
    &::-webkit-scrollbar {
      width: 0.4375rem;
      background: #fff;
    }

    &::-webkit-scrollbar-thumb {
      width: 0.4375rem;
      height: 0.625rem !important;
      // background: linear-gradient(270deg, #bf9e63 0%, #dfca98 100%);
      background: linear-gradient(270deg, #2473ff 0%, #56b1fd 100%);
      border-radius: 4px;
    }
    .body-content {
      margin-top: 1.125rem;
      width: 100%;
      height: auto;
      display: flex;
      align-items: flex-start;
      justify-content: flex-start;
      box-sizing: border-box;
      flex-wrap: wrap;
      border-collapse: collapse;

      .workSituationItem {
        width: 100%;
        height: auto;
        min-height: 6rem;
        display: flex;
        // &:nth-last-child(1) {
        //   width: auto;
        // }
        // &:nth-last-child(2) {
        //   width: auto;
        // }
        // &:nth-last-child(3) {
        //   width: auto;
        // }
        // &:nth-last-child(4) {
        //   width: auto;
        // }
        p {
          padding: 0;
          margin: 0;
          height: auto;
        }
        .left {
          // flex: 1;
          width: auto;
          display: flex;
          align-items: center;
          justify-content: flex-start;
          padding-right: 1.5rem;
          margin-right: 20%;
        }
        .situation {
          display: block;
        }
      }
    }
  }
  .materialList {
    width: 100%;
    height: auto;
    padding-bottom: 1.5rem;
    .tips {
      width: 100%;
      height: auto;
      margin-bottom: 15px;
      text-align: left;
      font-size: 1.625rem;
      font-family: Source Han Sans CN;
      font-weight: 500;
      color: #d6d20d;
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
    .el-table {
      margin-top: 1.125rem;
      width: 100%;
    }
  }
  .signOff {
    width: 100%;

    text-align: left;
    margin-top: 1.7647rem;
    p {
      font-family: Source Han Sans CN;
      color: #353535;
      padding-top: 1.5rem;
    }
  }
  .time {
    width: 100%;
    text-align: right;
    margin-top: 2.9412rem;
    padding-right: 8.8235rem;
    p {
      padding: 0;
      margin: 0;
      font-family: Source Han Sans CN;
      font-weight: bold;
      color: #333333;
    }
  }
  .tips {
    font-size: 1.7647rem;
    margin-top: 11.7647rem;
    font-family: Source Han Sans CN;
    font-weight: bold;
    color: red;
    text-align: left;
  }
  .assistantNotice-footer {
    width: 100%;
    height: 6.75rem;
    padding: 0;
    margin-top: 2rem;
    display: flex;
    align-items: center;
    justify-content: center;
    p {
      padding: 1.3125rem 2.875rem 1.4375rem 2.9375rem;
      border-radius: 2.1875rem;
      font-size: 1.625rem;
      font-family: Microsoft YaHei;
      font-weight: 400;
      &:nth-child(1) {
        background: #ffffff;
        border: 1px solid #4988f2;
        box-shadow: 0px 0px 1.8125rem 0px rgb(204 177 121 / 31%);
        color: #2473ff;
        margin-right: 2.1875rem;
        cursor: pointer;
      }
      &:nth-child(2) {
        background: linear-gradient(90deg, #2473ff 0%, #56b1fd 100%);
        box-shadow: 0px 0px 1.5rem 0px rgb(40 107 198 / 31%);
        color: #ffffff;
      }
    }
  }
}
</style>
