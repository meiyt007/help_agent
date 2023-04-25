<template>
  <div class="app-container smart-registration">
    <!--窗口受理查询数据-->
    <el-form
      style="width: 100%"
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="70px"
    >
      <el-form-item label="事项名称" prop="serviceName">
        <el-input
          v-model.trim="queryParams.serviceName"
          placeholder="请输入事项名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="事项类型" prop="serviceType">
        <treeselect
          v-model.trim="queryParams.serviceType"
          :options="serviceTypeOptions"
          style="width: 240px"
          noOptionsText="暂无数据"
          :defaultExpandLevel="1"
          placeholder="请选择事项类型"
        />
      </el-form-item>
      <!--<el-form-item label="所属机构">-->
        <!--<treeselect-->
          <!--v-model="queryParams.organOid"-->
          <!--:options="listOrganOptions"-->
          <!--style="width: 240px"-->
          <!--noOptionsText="暂无数据"-->
          <!--:defaultExpandLevel="1"-->
          <!--placeholder="请输入所属机构"-->
        <!--/>-->
      <!--</el-form-item>-->
      <el-form-item class="fr mr0">
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
        >
          搜索
        </el-button>
        <el-button
          type="warning"
          icon="el-icon-refresh"
          size="mini"
          @click="resetQuery"
        >
          重置
        </el-button>
      </el-form-item>
    </el-form>
    <div class="container">
      <div
        class="table-container"
        :class="{ tableInfo: true }"
      >
        <el-table
          v-loading="loading"
          :data="serviceList"
          border
          :height="'calc(100% - 160px)'"
        >
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="事项名称"
            min-width="200"
            align="center"
            header-align="center"
            prop="serviceName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="事项类型"
            min-width="100"
            align="center"
            header-align="center"
            prop="serviceTypeName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="所属机构"
            align="center"
            prop="organName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="操作"
            min-width="100"
            align="center"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                type="text"
                size="mini"
                icon="iconfont zfsoft-dengji"
                v-if="isShowRegisterCase(scope.row, '1')"
                @click="registerCase(scope.row, '1')"
                v-hasPermi="['business:acceptance:grregister']"
              >
                个人登记
              </el-button>
              <el-button
                type="text"
                size="mini"
                icon="iconfont zfsoft-dengji"
                @click="registerCase(scope.row, '2')"
                v-if="isShowRegisterCase(scope.row, '2')"
                v-hasPermi="['business:acceptance:grregister']"
              >
                企业法人登记
              </el-button>
              <el-button
                type="text"
                size="mini"
                icon="iconfont zfsoft-dengji"
                @click="registerCase(scope.row, '3')"
                v-if="isShowRegisterCase(scope.row, '3')"
                v-hasPermi="['business:acceptance:grregister']"
              >
                事业法人登记
              </el-button>
              <el-button
                type="text"
                size="mini"
                icon="iconfont zfsoft-dengji"
                @click="registerCase(scope.row, '4')"
                v-if="isShowRegisterCase(scope.row, 4)"
                v-hasPermi="['business:acceptance:grregister']"
              >
                社会组织法人登记
              </el-button>
              <el-button
                type="text"
                size="mini"
                icon="iconfont zfsoft-dengji"
                @click="registerCase(scope.row, '5')"
                v-if="isShowRegisterCase(scope.row, '5')"
                v-hasPermi="['business:acceptance:grregister']"
              >
                非法人企业登记
              </el-button>
              <el-button
                type="text"
                size="mini"
                icon="iconfont zfsoft-dengji"
                @click="registerCase(scope.row, '6')"
                v-if="isShowRegisterCase(scope.row, '6')"
                v-hasPermi="['business:acceptance:grregister']"
              >
                行政机关登记
              </el-button>
              <el-button
                type="text"
                size="mini"
                icon="iconfont zfsoft-dengji"
                @click="registerCase(scope.row, '9')"
                v-if="isShowRegisterCase(scope.row, '9')"
                v-hasPermi="['business:acceptance:grregister']"
              >
                其他组织登记
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList(null)"
        />
      </div>
    </div>
    <CommonDialog
      v-if="commonVisible"
      ref="commonDialog"
      :commmonVisible.sync="commonVisible"
      :handleType="handleType"
      :cegisterType.sync="cegisterType"
      :serviceOid="serviceOid"
      :loginUser="loginUser"
      :serviceName="serviceName"
      :serviceRoot="serviceRoot"
      :comeFormArtific="comeFormArtific"
      @case-close="closeCaseRegisterView"
    />
  </div>
</template>
<script>
import {
  page,
  getloginUser,
  getFileDownPath,
  downloadPrintFile, downGzcnsWord,
} from "@/api/zc/businessManagement/windowAcceptance";
import { showCallMessage } from "@/api/zc/businessManagement/doubleScreenInteraction";
import { queryDistrictSimpleTree } from "@/api/sys/district";
import { queryOrganTree } from "@/api/sys/organ";
import { queryServiceTypeSimpleTree } from "@/api/sxpt/serviceType";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { VueCropper } from "vue-cropper";
import Utils from "@/assets/js/util.js";
import CommonDialog from "./dialogs/common-dialog.vue";
/** 智能登记混入 */
import LeftItem from "./components/intelligentTalk/LeftItem";
import { mapGetters } from "vuex";
export default {
  name: "SmartRegistration",
  inheritAttrs: false,
  components: {
    Treeselect,
    VueCropper,
    CommonDialog,
    LeftItem
  },
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 业务办理事项数据
      serviceList: [],
      //登记类型 法人1 自然人0
      cegisterType: "",
      // 机构
      listOrganOptions: [],
      // 区划
      districtOptions: [],
      // 事项类型
      serviceTypeOptions: [],
      //事项
      sxService: null,
      // 弹出层标题
      title: "",
      // 显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      // 查询参数
      queryParams: {
        serviceName: "",
        pageNum: 1,
        pageSize: 10,
        organOid: null,
        startDate: null,
        endDate: null,
        serviceOids: []
      },
      queryForm: {
        registrarOid: "", //逻辑主键
        organOid: null, // 所属组织机构
        serviceType: "" // 事项类型
      },
      formData: {},
      labelPosition: "top",
      showMenu: false,
      topVal: "0px",
      leftVal: "0px",
      orderNum: 1,
      commonVisible: false,
      serviceOid: "", // 事项id
      loginUser: {},
      serviceRoot: [false, false], // 服务对象权限
      comeFormArtific: false,
      handleType:'3' //办理类型[1好办易办 2秒批秒办 3标准]
    };
  },
  computed: {
    ...mapGetters([
      "wgpjFlag"
    ]),
  },
  watch: {
    "queryParams.districtOid": function (val, oldVal) {
      //机构加载
      this.getListOrganTree(val);
    },
  },
  created () {
    this.getList();
    this.getServiceTypeTree();
    this.getDistrictTree();
    this.getListOrganTree();
    this.queryLoginInfo();
  },
  methods: {
    // 是否展示登记按钮
    isShowRegisterCase (row, RegisterType) {
      return row.serviceObject && row.serviceObject.indexOf(RegisterType) != -1;
    },

    /*closeCamera () {
      if (this.wgpjFlag == true && this.$route.path.includes("smartRegistration")) {
        Utils.$emit("stopCamera", "摄像头已关闭");
      }
    },*/
    showMenuInfo (e, menuMark, index) {
      if (menuMark == "0") {
        let mTop = e.path[0].offsetParent.offsetTop;
        let mWidth = e.path[0].offsetParent.offsetWidth;
        let mLeft = e.path[0].offsetParent.offsetLeft;
        this.topVal = mTop + mWidth / 2 + "px";
        this.leftVal = mLeft + mWidth / 2 + "px";
        this.orderNum = index;
      }
      if (e.path[3].className == "ic-result" || menuMark == "1") {
        this.showMenu = true;
      }
    },
    moveToSort (e, idx) {
      let sortItem = document.getElementById("order" + idx);
      sortItem.appendChild(e.path[3].childNodes[0].children[this.orderNum]);
      this.showMenu = false;
    },
    deleteSortOne (e) {
      e.path[1].remove();
    },
    deleteScanList (e, id) {
      this.photoList = this.photoList.filter(i => i.id !== id);
    },
    hideMenuInfo () {
      this.showMenu = false;
    },
    /** 获取事项类型树 */
    getServiceTypeTree (serviceType) {
      queryServiceTypeSimpleTree(serviceType).then(response => {
        this.serviceTypeOptions = response.data;
      });
    },

    /** 获取区划树 */
    getDistrictTree (districtOid) {
      queryDistrictSimpleTree(districtOid).then(response => {
        this.districtOptions = response.data;
      });
    },

    /** 获取机构数据 */
    getListOrganTree(districtOid, callback) {
      let _that = this;
      queryOrganTree("").then(response => {
        this.listOrganOptions = response.data;
        callback && callback();
      });
      /*if (districtOid) {
        queryOrganTree(districtOid).then(response => {
          _that.listOrganOptions = response.data;
          callback && callback();
        });
      } else {
        _that.listOrganOptions = [];
        _that.queryParams.organOid = null;
      }*/
    },

    /** 获取机构数据 */
   /* getListOrganTree (districtOid, callback) {
      if (districtOid) {
        queryOrganTree(districtOid).then(response => {
          this.listOrganOptions = response.data;
          callback && callback();
        });
      } else {
        this.listOrganOptions = [];
        this.queryParams.organOid = null;
      }
    },*/
    /** 查询窗口受理事项列表 */
    getList (oids) {
      this.loading = true;
      // 智能问答传参
      return page({ ...this.queryParams, serviceOids: oids || this.queryParams.serviceOids }).then(response => {
        this.serviceList = response.data.data;
        this.total = parseInt(response.data.total);
        this.loading = false;
        return Promise.resolve(response);
      });
    },

    // 办件修改关闭按钮调用wps打印
    closeCaseRegisterView (obj) {
      let mesTitle = "";
      //当obj不为空时打印
      if (obj && obj.isRqslFlag) {
        mesTitle = "是否打印容缺补正受理通知书?";
      } else if (obj && obj.isSl == "false" && obj.caseNumber) {
        mesTitle = "是否打印不予受理通知单?";
      } else if (obj && obj.isSl == "true") {
        if(obj.sjStatus && obj.sjStatus==2){
          mesTitle = "是否打印受理通知单?";
        }else{
          mesTitle = "是否打印收件通知单?";
        }
      } else if (obj && obj.isGzSl == "true") {
        mesTitle = "是否打印告知承诺单?";
      }
      if (!mesTitle) {
        this.getList();
        return;
        //return this.$router.push("materialOut");
      }
      this.$confirm(mesTitle, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        closeOnClickModal: false,
        type: "warning"
      })
        .then(() => {
            this.downGzWord(obj);
        })
        .catch(action => {
          if (action === "cancel") {
            console.log("打印取消...");
          }
          //this.$router.push("materialOut");
        });
      this.getList();
    },


    //调用c++服务打印
    handlePrintAll (obj) {
      getFileDownPath(obj).then(response => {
        if (response.data) {
          let url = response.data;
          downloadPrintFile(url).then(res => {
            console.log(res.StateCode)
            if(res.StateCode!=0){
              if(res.StateCode==-3){
                this.$message.error("打开文档失败!");
              }else if(res.StateCode==-5){
                this.$message.error("下载失败!");
              }
            }
            // 跳转到材料出库
            //this.$router.push("materialOut");
            this.getList();
          });
          //window.location.href ='http://172.168.253.56:10001/Download?url='+response.data;
        } else {
          // 跳转到材料出库
          //this.$router.push("materialOut");
          this.getList();
        }
      });
    },

    //调用pageoffice的打印
    handlePrint (obj) {
      const applyUserName = encodeURIComponent(obj.applyUserName);
      const caseNumber = obj.caseNumber;
      const sqTime = obj.createDate;
      POBrowser.openWindowModeless(
        process.env.VUE_APP_BASE_API_PAGE +
        "/manage/zhuozheng/notAcceptedNoticePrint?caseNumber=" +
        caseNumber +
        "&applyUserName=" +
        applyUserName +
        "&sqTime=" +
        sqTime,
        "width=1200px;height=800px;"
      );
    },
    //调用pageoffice的打印
    handleRqbzPrint (obj) {
      const caseName = encodeURIComponent(obj.caseName);
      const applyUserName = encodeURIComponent(obj.applyUserName);
      const sqTime = obj.createDate;
      POBrowser.openWindowModeless(
        process.env.VUE_APP_BASE_API_PAGE +
        "/manage/zhuozheng/rqbzAcceptedNoticePrint?caseName=" +
        caseName +
        "&applyUserName=" +
        applyUserName +
        "&sqTime=" +
        sqTime,
        "width=1200px;height=800px;"
      );
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 表单重置 */
    reset () {
      // Object.assign(this.queryForm, this.$options.data().queryForm);
      this.resetForm("queryForm");
    },
    /** 取消按钮 */
    cancel () {
      this.openInit = false;
      this.reset();
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.getList();
    },

    // 点击弹框
    registerCase (row, cegisterType, artificialService) {
      this.handleType = row.handleType
      this.serviceOid = row.serviceOid;
      this.serviceName = row.serviceName;
      this.commonVisible = true;
      this.serviceRoot = [this.isShowRegisterCase(row, cegisterType), this.isShowRegisterCase(row, cegisterType)];
     /* if (artificialService) {
        if (this.serviceRoot[0] && !this.serviceRoot[1]) {
          this.cegisterType = '0';
        } else if (!this.serviceRoot[0] && this.serviceRoot[1]) {
          this.cegisterType = '1';
        } else {
          this.cegisterType = '0';
        }

        this.comeFormArtific = true;
      } else {*/
        this.cegisterType = cegisterType;
        this.comeFormArtific = false;
     // }
    },
    /** 登录信息 */
    queryLoginInfo () {
      getloginUser().then(response => {
        this.loginUser = response.data;
      });
    },
    downGzWord (obj) {
      downGzcnsWord(obj).then(response => {
        let url = response.data;
        /*downloadPrintFile(url).then(res => {
          console.log(res.StateCode)
          if(res.StateCode!=0){
            if(res.StateCode==-3){
              this.$message.error("打开文档失败!");
            }else if(res.StateCode==-5){
              this.$message.error("下载失败!");
            }
          }
          // 跳转到材料出库
          this.$router.push("materialOut");
        });*/
        //调用pageoffice的打印接口
        POBrowser.openWindowModeless(
          process.env.VUE_APP_BASE_API_PAGE +
          "/manage/zhuozheng/pageOfficePrint?fileUrl="+url, "width=1200px;height=800px;"
        );
      })

    }
  }
};
</script>
<style lang="scss" scoped>
@import './smartRegistration.scss';
.el-tree > .el-tree-node {
  min-width: 100%;
  display: inline-block;
}
</style>
