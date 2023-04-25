<!--
 * @Author: haoxinguo haoxinguo@zhuofansoft.com
 * @Date: 2022-09-16 13:58:25
 * @LastEditors: haoxinguo haoxinguo@zhuofansoft.com
 * @LastEditTime: 2022-11-08 15:56:38
 * @FilePath: \zf_web_ui\src\views\pc\operationFlow\components\assistantNotice.vue
 * @Description: 帮办告知单
-->
<template>
  <div class="assistantNotice-content" style="page-break-after: always;">
    <div class="header">
      <div class="left">
        <span style="margin-bottom:5px;margin-left: 0;">上海市黄浦区政务服务中心监制</span>
        <svg v-if="materialIsAll == '1'" style="width: 200px;text-align: left;" id="barcode"></svg>
<!--        margin: 0 auto-->
        <span style="margin-bottom:5px;margin-left: 0;">{{ barcodeValue }}</span>
      </div>
      <div class="right">
        <!-- <img :src="qrcodeValue" alt="" /> -->
        <vue-qr
         :text="imgCodeUrl"
         :size="250"
         :logoSrc="logo"
         :logoScale="0.2">
      </vue-qr>
      </div>
    </div>
    <!-- <p class="assNum">
      帮办号：<span>{{ basicUserInfo.id }}</span>
    </p> -->
    <p class="title">上海市黄浦区政务服务 帮办告知单</p>
    <div class="situationList">
      <div class="body-content">
        <div class="workSituationItem">
          <p class="left">办理事项：{{ baseUserInfo.specificMatters.serviceName }}</p>
        </div>
        <div class="workSituationItem" v-if="situationList.length">
          <p class="left">办理情形：           
            <span :key="list.valueOid" v-for="(list,index) in situationList"><span>{{ index != 0?'-':''}}</span><span>{{list.valueName }}</span></span> 
          </p>
        </div>
        <div class="workSituationItem">
          <p class="left">{{
              baseUserInfo.fillUserInfo.applyUserType === "1"
                ? "申请人："
                : "申请单位："
          }}
            {{ baseUserInfo.fillUserInfo.applyUserName }}
          </p>
        </div>
        <div class="workSituationItem">
          <p class="left">经办人：{{ baseUserInfo.name }}</p>
        </div>
        <div class="workSituationItem">
          <p class="left">经办人电话：{{ baseUserInfo.phone }}</p>
        </div>
        <div class="workSituationItem">
          <p class="left">帮办时间：{{ serviceTime }}</p>
        </div>
        <!-- <div class="workSituationItem">
          <p class="left">办理状态：{{ caseStatus === 0 ? "暂存" : "已完成" }}
          </p>
        </div>
        <div class="time">
          <p></p>
        </div> -->
        <!-- <div class="workSituationItem"></div> -->
      </div>
    </div>
    <div class="materialList" v-if="submittedDataList.length">
      <p class="dialogContentTitle" style="text-align:center">本次已准备完成的材料</p>
      <div class="print-table">
        <div class="item printTitle">
          <div class="num">序号</div>
          <div class="name">材料名称</div>
          <div class="type">材料类型</div>
          <div class="method">材料形式</div>
        </div>
        <div class="item " :key="item" v-for=" (item,index) in submittedDataList">
          <div class="num">{{index+1}}</div>
          <div class="name left">{{item.materialName}}</div>
          <div class="type">{{
                  item.materialFormat === 1
                    ? "纸质"
                    : item.materialFormat === 2
                      ? "电子版"
                      : item.materialFormat === 3
                        ? "证照"
                        : item.materialFormat === 4
                          ? "容缺补正"
                          : "告知承诺"
              }}</div>
          <div class="method">{{
                  item.materialType === 0
                    ? "原件"
                    : item.materialType === 1
                      ? "复印件"
                      : "原件和复印件"
              }}</div>
        </div>
      </div>
      <!-- <el-table :data="submittedDataList" v-loading="tableLoading">
        <el-table-column align="center" type="index" width="50" label="序号">
        </el-table-column>
        <template v-for="(item, index) in submittedColumns">
          <el-table-column width="100"  :key="index" :prop="item.prop" :label="item.label" align="center"
            v-if="item.prop === 'materialFormat'" show-overflow-tooltip>
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
          <el-table-column width="150"  :key="index" :prop="item.prop" :label="item.label" align="center"
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
          <el-table-column header-align="center" v-else-if="item.prop === 'materialName'" :key="index" :prop="item.prop" :label="item.label" align="left"></el-table-column>
          <el-table-column width="100" v-else :key="index" :prop="item.prop" :label="item.label" align="center"></el-table-column>
        </template>
      </el-table> -->
    </div>
    <div class="materialList" v-if="needSubmittedDataList.length">
      <p class="tips" style="text-align:center">您还需要提交以下材料才能完成该事项的申请</p>
      <div class="print-table">
        <div class="item printTitle">
          <div class="num">序号</div>
          <div class="name">材料名称</div>
          <div class="type">材料类型</div>
          <div class="method">材料形式</div>
        </div>
        <div class="item " :key="item" v-for=" (item,index) in needSubmittedDataList">
          <div class="num">{{index+1}}</div>
          <div class="name left">{{item.materialName}}</div>
          <div class="type">{{
                  item.materialFormat === 1
                    ? "纸质"
                    : item.materialFormat === 2
                      ? "电子版"
                      : item.materialFormat === 3
                        ? "证照"
                        : item.materialFormat === 4
                          ? "容缺补正"
                          : "告知承诺"
              }}</div>
          <div class="method">{{
                  item.materialType === 0
                    ? "原件"
                    : item.materialType === 1
                      ? "复印件"
                      : "原件和复印件"
              }}</div>
        </div>
      </div>
      <!-- <el-table  :data="needSubmittedDataList" v-loading="tableLoading">
        <el-table-column align="center" type="index" width="50" label="序号">
        </el-table-column>
        <template v-for="(item, index) in needSubmittedColumns">
          <el-table-column width="100"  :key="index" :prop="item.prop" :label="item.label" align="center"
            v-if="item.prop === 'materialFormat'" show-overflow-tooltip>
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
          <el-table-column width="150" :key="index" :prop="item.prop" :label="item.label" align="center"
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
          <el-table-column header-align="center" v-else-if="item.prop === 'materialName'" :key="index" :prop="item.prop" :label="item.label" align="left"></el-table-column>
          <el-table-column width="100" v-else :key="index" :prop="item.prop" :label="item.label" align="center"></el-table-column>
        </template>
      </el-table> -->
    </div>
    <div class="signOff">
      <p>帮办服务人员：{{ basicUserInfo.name }}</p>
      <p>帮办服务人员编号：{{ basicUserInfo.workNumber }}</p>
      <p>咨询电话：021-63529090</p>
    </div>
<!--    <div class="time">-->
<!--      <p>{{ time }}</p>-->
<!--    </div>-->
    <p class="tips" style="text-align:center">
      （本告知单一式两份，一份交予经办人，一份帮办服务部门留存。）
    </p>
  </div>
</template>
<script>
import { formatDateSimple } from "@/utils/index";
import barcode from "jsbarcode";
// import QRCode from "qrcode";
import VueQr from 'vue-qr'
import { setFormatDate } from "@/utils/index";
import {
  queryQlCaseMaterialListByCaseOid,
  getCaseTitleValueList,
  queryQlCaseByCaseOid,
  querySimpleQlCaseMaterialListByCaseOid, sendHPSms,
} from "@/api/modules/business.js";
export default {
  name: "AssistantNotice",
  components: {
    barcode,
    VueQr
  },
  props: {
    imgCodeUrl:{
      type: String,
      default: () => '',
    },
    materialIsAll: {
      type: String,
      default: () => 0,
    },
    baseUserInfo: {
      type: Object,
      default: () => { },
    },
  },
  data() {
    return {
      logo: require('@/assets/images/qi.png'),
      tableLoading: false,
      serviceTime: "",
      barcodeValue: "",
      qrcodeValue: "",
      submittedDataList: [],
      needSubmittedDataList: [],
      submittedColumns: [
        { label: "材料名称", prop: "materialName" },
        { label: "材料类型", prop: "materialType" },
        { label: "材料形式", prop: "materialFormat" },
        // { label: "已提交份数", prop: "collectionNumber" },
      ], //已提交材料列表
      needSubmittedColumns: [
        { label: "材料名称", prop: "materialName" },
        { label: "材料类型", prop: "materialType" },
        { label: "材料形式", prop: "materialFormat" },
        // { label: "份数", prop: "needPaper" },
      ], //还需提交材料列表
      materialList: [],
      caseStatus: null,
      time: null,
      situationList:[]
    };
  },
  computed: {
    basicUserInfo() {
      return this.$store.state.user.basicUserInfo;
    },
  },
  mounted() {
    console.log(this.imgCodeUrl);
    this.serviceTime = formatDateSimple(new Date());
    this.getSituationMaterialListByOids();
    this.getValueList();
    this.getQueryQlCaseByCaseOid();
    // this.getQrcodeValue();
    this.getTime();
  },
  methods: {
    //获取二维码
    async getQrcodeValue(type,number) {
      this.qrcodeValue = await QRCode.toDataURL(type + ';' + number);
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
            // this.getQrcodeValue(res.data.applay.credentialType,res.data.applay.credentialNumber);
            //办件材料齐全
            if(this.materialIsAll == '1'){
              this.barcodeValue = res.data.caseNumber;
              // this.barcodeValue = '010071723000RC2';
              let options = {
                fontSize: 12,
                background: "#cccccc",
                width:2,
                displayValue:false,
                margin:2,
              };
              JsBarcode("#barcode", this.barcodeValue, options); 
            }
            
          }
        }
      );
    },
    getValueList(){
      this.situationList = [];
      const data = {
        caseOid: this.baseUserInfo.caseOid,
      };
      let that = this;
      getCaseTitleValueList(data)
        .then((res) => {
          // console.log(res);
          if(res.code == 200){
            let arr = [];
            res.data.forEach(function(situa){
              if(situa.noticeFormFlag == '1'){
                arr.push(situa);
              }
            })
            that.situationList = arr;
          }
        })
        .catch((err) => {
        });
    },

    //获取材料列表
    getSituationMaterialListByOids() {
      const loading = this.$loading({
        lock: true,
        text: "正在获取文件",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
        customClass:'loadCoupon'
      });
      const data = {
        caseOid: this.baseUserInfo.caseOid,
      };
      this.tableLoading = true;
      querySimpleQlCaseMaterialListByCaseOid(data)
        .then((res) => {
          loading.close();
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
              // console.log("item--------------" + JSON.stringify(item));
              if(item.collectionFlag === 1){
                this.submittedDataList.push(item);
              }
              else if (item.qlCaseMaterialAttaList.length) {
                const result = item.qlCaseMaterialAttaList.some((item) => {
                  return item.qlSysAtta || item.materialType === "upload" ;
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
        }).catch((err) => {
          this.tableLoading = false;
        }).finally(()=>{
          if(this.needSubmittedDataList.length!==0){
            this.sendHPSms();
          }
        });

    },
    sendHPSms() {
      /**title,phone,message,workUserId,workUserName**/
      const data = {
        title: "材料补交提醒",
        phone: this.baseUserInfo.phone,
        message: this.baseUserInfo.fillUserInfo.applyUserName+"您好，您当前办理的"+this.baseUserInfo.specificMatters.serviceName+"事项，还有"+this.needSubmittedDataList.length+"个材料需要补交，您可以登录随身办--》黄浦旗舰店中查看事项信息和补交材料。",
        workUserId: this.basicUserInfo.id,
        workUserName: this.basicUserInfo.name,
      };
      sendHPSms(data);
    },
  },
};
</script>
<style lang="scss">
@import './assistantNotice.scss';
.loadCoupon{
  z-index: 9999 !important;
}
</style>
