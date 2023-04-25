/** * 窗口受理管理 * @author: wangwg * @date: 2020-10-29 */
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--窗口受理查询数据-->
      <el-col :span="24" :xs="24" class="app-right">
        <el-form
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
              size="small"
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

          <el-form-item label="所属机构">
            <treeselect
              v-model="queryParams.organOid"
              :options="listOrganOptions"
              style="width: 240px"
              noOptionsText="暂无数据"
              :defaultExpandLevel="1"
              placeholder="请输入所属机构"
            />
          </el-form-item>
          <!--<el-form-item label="所属区划" prop="districtOid">
            <treeselect
              v-model="queryParams.districtOid"
              :options="districtOptions"
              style="width: 240px"
              noOptionsText="暂无数据"
              :defaultExpandLevel="1"
              placeholder="请输入所属区划"/>
          </el-form-item>
          <el-form-item label="所属机构" prop="organOid">
            <treeselect
              v-model="queryParams.organOid"
              :options="listOrganOptions"
              style="width: 240px"
              noOptionsText="暂无数据"
              :defaultExpandLevel="1"
              placeholder="请输入所属机构"/>
          </el-form-item>-->
          <el-form-item class="fr mr0">
            <el-button
              type="primary"
              icon="el-icon-search"
              size="mini"
              @click="handleQuery"
              >搜索</el-button
            >
            <el-button
              type="warning"
              icon="el-icon-refresh"
              size="mini"
              @click="handleAcceptPrint"
              >重置</el-button
            >
          </el-form-item>
        </el-form>

        <el-table
          v-loading="loading"
          :data="serviceList"
          :border="true"
          :fit="true"
        >
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="事项名称"
            align="center"
            prop="serviceName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="所属机构"
            align="center"
            prop="organName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="事项类型"
            align="center"
            prop="serviceTypeName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="创建时间"
            align="center"
            prop="createDate"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="操作"
            width="270"
            align="center"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-dengji"
                v-if="
                  scope.row.serviceObject &&
                    scope.row.serviceObject.indexOf('1') != -1
                "
                @click="caseRegister(scope.row, '0')"
                v-hasPermi="['business:acceptance:grregister']"
                >个人登记
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-dengji"
                v-if="
                  scope.row.serviceObject &&
                    (scope.row.serviceObject.indexOf('2') != -1 ||
                      scope.row.serviceObject.indexOf('3') != -1 ||
                      scope.row.serviceObject.indexOf('4') != -1 ||
                      scope.row.serviceObject.indexOf('5') != -1 ||
                      scope.row.serviceObject.indexOf('6') != -1 ||
                      scope.row.serviceObject.indexOf('9') != -1)
                "
                @click="caseRegister(scope.row, '1')"
                v-hasPermi="['business:acceptance:frregister']"
                >法人登记
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="onceNoticeForm(scope.row)"
                v-hasPermi="['business:acceptance:update']"
                >一次性告知
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
    </el-row>

    <el-dialog
      v-dialog-drag
      :visible.sync="data.show"
      v-for="data in onceNoticeOptions"
      :key="data.serviceOid"
      :title="data.title"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
      :close-on-click-modal="false"
    >
      <once-notice-form
        :serviceOid="data.serviceOid"
        :title="data.title"
        @father-click="closeChild"
      >
      </once-notice-form>
    </el-dialog>

    <el-dialog
      v-dialog-drag
      :visible.sync="data.show"
      v-for="data in registerOptions"
      :key="data.serviceOid"
      :title="data.title"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
      :close-on-click-modal="false"
    >
      <register-acceptance
        :serviceOid="data.serviceOid"
        :cegisterType="data.cegisterType"
        :title="data.title"
        @case-close="closeCaseRegisterView"
      >
      </register-acceptance>
    </el-dialog>
  </div>
</template>
<script>
import {
  page,
  getFileDownPath,
  downloadPrintFile
} from "@/api/zc/businessManagement/windowAcceptance";
import { queryDistrictSimpleTree } from "@/api/sys/district";
import { queryOrganTree } from "@/api/sys/organ";
import { queryServiceTypeSimpleTree } from "@/api/sxpt/serviceType";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { VueCropper } from "vue-cropper";
import onceNoticeForm from "@/views/zc/businessManagement/onceNoticeForm/onceNoticeForm";
import registerAcceptance from "@/views/zc/businessManagement/windowAcceptance/registerAcceptance";
export default {
  inheritAttrs: false,
  components: { Treeselect, VueCropper, onceNoticeForm, registerAcceptance },
  name: "WindowAcceptance",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      onceNoticeOptions: [],
      registerOptions: [],
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
        endDate: null
      },
      queryForm: {
        registrarOid: "", //逻辑主键
        organOid: "", // 所属组织机构
        serviceType: "" // 事项类型
      },
      formData: {},
      labelPosition: "top"
    };
  },
  watch: {
    "queryParams.districtOid": function(val, oldVal) {
      //机构加载
      this.getListOrganTree(val);
    }
  },
  created() {
    this.getList();
    this.getServiceTypeTree();
    this.getDistrictTree();
    this.getListOrganTree();
  },
  methods: {
    /** 获取事项类型树 */
    getServiceTypeTree(serviceType) {
      let _that = this;
      queryServiceTypeSimpleTree(serviceType).then(response => {
        _that.serviceTypeOptions = response.data;
      });
    },

    /** 获取区划树 */
    getDistrictTree(districtOid) {
      let _that = this;
      queryDistrictSimpleTree(districtOid).then(response => {
        _that.districtOptions = response.data;
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
 /*   getListOrganTree(districtOid, callback) {
      let _that = this;
      if (districtOid) {
        queryOrganTree(districtOid).then(response => {
          _that.listOrganOptions = response.data;
          callback && callback();
        });
      } else {
        _that.listOrganOptions = [];
        _that.queryParams.organOid = null;
      }
    },*/
    /** 查询窗口受理事项列表 */
    getList() {
      let _that = this;
      _that.loading = true;
      page(this.queryParams).then(response => {
        _that.serviceList = response.data.data;
        _that.total = response.data.total;
        _that.loading = false;
      });
    },
    /** 登记按钮操作 */
    caseRegister(row, cegisterType) {
      let _that = this;
      let register = {
        serviceOid: row.serviceOid,
        cegisterType: cegisterType,
        show: true,
        title: "办件登记"
      };
      _that.registerOptions.push(register);
    },
    // 办件修改关闭按钮
    closeCaseRegisterView1(obj) {
      this.registerOptions.pop();
      let item = {
        show: false
      };
      //当obj不为空时打印
      if (obj && obj.isRqslFlag) {
        this.$confirm("是否打印容缺补正受理通知书?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            this.handleRqbzPrint(obj);
          })
          .catch(() => {});
      } else if (obj && obj.isSl == "false" && obj.caseNumber) {
        this.$confirm("是否打印不予受理通知单?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            this.handlePrint(obj);
          })
          .catch(() => {});
      } else if (obj && obj.isSl == "true") {
        this.$confirm("是否打印受理通知单?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          closeOnClickModal: false,
          type: "warning"
        })
          .then(() => {
            this.handleAcceptPrint(obj);
          })
          .catch(() => {});
      }
      this.getList();
    },
    // 办件修改关闭按钮
    closeCaseRegisterView(obj) {
      this.registerOptions.pop();
      let item = {
        show: false
      };
      let mesTitle = "";
      //当obj不为空时打印
      if (obj && obj.isRqslFlag) {
        mesTitle = "是否打印容缺补正受理通知书?";
      } else if (obj && obj.isSl == "false" && obj.caseNumber) {
        mesTitle = "是否打印不予受理通知单?";
      } else if (obj && obj.isSl == "true") {
        mesTitle = "是否打印受理通知单?";
      }
      this.$confirm(mesTitle, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.handlePrintAll(obj);
        })
        .catch(() => {});
      this.getList();
    },
    //调用c++服务打印
    handlePrintAll(obj) {
      getFileDownPath(obj).then(response => {
        // debugger
        if (response.data) {
          downloadPrintFile(encodeURIComponent(response.data));
        }
      });
    },
    //调用pageoffice的打印
    handlePrint1(obj) {
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
    handleRqbzPrint(obj) {
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
    handleAcceptPrint(obj) {
      const applyUserName = encodeURIComponent(obj.applyUserName);
      const caseNumber = obj.caseNumber;
      const sqTime = obj.createDate;
      getFileDownPath(applyUserName, caseNumber, sqTime).then(response => {
        // debugger
        if (response.data) {
          downloadPrintFile(encodeURIComponent(response.data));
        }
      });
      //POBrowser.openWindowModeless(process.env.VUE_APP_BASE_API_PAGE+'/manage/zhuozheng/acceptedNoticePrint?caseNumber='+caseNumber+'&applyUserName='+applyUserName+'&sqTime='+sqTime,'width=1200px;height=800px;');
    },
    /** 一次性告知 */
    onceNoticeForm(row) {
      let _that = this;
      let onceNotice = {
        serviceOid: row.serviceOid,
        show: true,
        title: "一次性告知单"
      };
      _that.onceNoticeOptions.push(onceNotice);
    },
    closeChild() {
      //关闭一次性告知页面
      let _that = this;
      _that.onceNoticeOptions = [];
      let item = { serviceOid: serviceOid, show: false, title: "一次性告知单" };
      _that.onceNoticeOptions.push(item);
    },
    /** 重置按钮操作 */
    resetQuery() {
      let _that = this;
      _that.resetForm("queryForm");
      _that.handleQuery();
    },
    /** 表单重置 */
    reset() {
      let _that = this;
      Object.assign(_that.queryForm, _that.$options.data().queryForm);
      _that.resetForm("queryForm");
    },
    /** 取消按钮 */
    cancel() {
      let _that = this;
      _that.openInit = false;
      _that.reset();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      let _that = this;
      _that.queryParams.pageNum = 1;
      _that.getList();
    }
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
