<template>
  <el-tabs
    v-model="activeName"
    @tab-click="handleClick"
    style="overflow: hidden;"
  >
    <el-tab-pane label="目录信息" name="first">
      <div>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b>目录编号：</b>
            </td>
            <td>
              {{ form.comboDirectoryCode }}
            </td>
            <td>
              <b>目录名称：</b>
            </td>
            <td>
              {{ form.comboDirectoryName }}
            </td>
          </tr>
          <tr>
            <td>
              <b>所属区划：</b>
            </td>
            <td>
              {{ form.districtName }}
            </td>
            <td>
              <b>所属分类：</b>
            </td>
            <td>
              {{ form.themeName }}
            </td>
          </tr>
          <tr>
            <td>
              <b>事项大类：</b>
            </td>
            <td>
              <template>
                <span v-if="form.direType == 1">单部门行政审批类</span>
                <span v-if="form.direType == 2">跨部门业务协同类</span>
                <span v-if="form.direType == 3">政府服务类</span>
                <span v-if="form.direType == 4">跨层级业务协同类</span>
                <span v-if="form.direType == 5">跨区域业务协同类</span>
              </template>
            </td>
            <td>
              <b>主办部门：</b>
            </td>
            <td>
              {{ form.mainOrganName }}
            </td>
          </tr>
          <tr>
            <td>
              <b>协办部门：</b>
            </td>
            <td colspan="3">
              {{ form.assistOrganName }}
            </td>
          </tr>
          <tr>
            <td>
              <b>服务对象：</b>
            </td>
            <td colspan="3">
              {{ form.comboServiceObjectName }}
            </td>
          </tr>
          <tr>
            <td>
              <b>申报须知：</b>
            </td>
            <td colspan="3">
              {{ form.declareNeedKnow }}
            </td>
          </tr>
          <tr v-if="form.serviceOids != ''">
            <td>
              <b>前置事项：</b>
            </td>
            <td colspan="3">
              {{ form.serviceNames }}
            </td>
          </tr>
          <tr v-if="form.serviceOids != ''">
            <td>
              <b>事项系统类型：</b>
            </td>
            <td colspan="3">
              {{ form.servicextlx }}
            </td>
          </tr>
          <tr v-if="form.serviceOids != ''">
            <td>
              <b>审批系统名称：</b>
            </td>
            <td colspan="3">
              {{ form.servicespxt }}
            </td>
          </tr>
          <tr v-if="form.serviceOids != ''">
            <td>
              <b>申报地址链接：</b>
            </td>
            <td colspan="3">
              {{ form.serviceApplyAddr }}
            </td>
          </tr>
          <tr>
            <td>
              <b>是否收费：</b>
            </td>
            <td>
              {{ reversedIfCharge }}
            </td>
            <td>
              <b>是否需要人证核验：</b>
            </td>
            <td>
              <span v-if="form.showRzhs==1">是</span>
              <span v-else>否</span>
            </td>

          </tr>
          <tr>
            <td>
              <b>网办地址：</b>
            </td>
            <td colspan="3">
              {{ form.webUrl }}
            </td>
          </tr>
          <tr v-if="form.ifCharge > 0">
            <td>
              <b>收费标准：</b>
            </td>
            <td colspan="3">
              {{ form.chargeStandard }}
            </td>
          </tr>
          <tr v-if="form.ifCharge > 0">
            <td>
              <b>收费依据：</b>
            </td>
            <td colspan="3">
              {{ form.chargeGist }}
            </td>
          </tr>
          <tr>
            <td>
              <b>办理地址：</b>
            </td>
            <td colspan="3">
              {{ form.manageAddr }}
            </td>
          </tr>
          <tr>
            <td>
              <b>办理时间：</b>
            </td>
            <td colspan="3">
              {{ form.manageTime }}
            </td>
          </tr>
          <tr>
            <td>
              <b>办事流程图：</b>
            </td>
            <td>
              <div v-show="null != form.handleFlow && '' != form.handleFlow">
                <span>{{ form.handleFlowName }}</span>
                <el-link type="primary" @click="downloadFile(form.handleFlow)"
                  >下载</el-link
                >
                |
                <el-link type="primary" @click="viewFile(form.handleFlow)"
                  >预览</el-link
                >
              </div>
            </td>
            <td>
              <b>有无中介服务：</b>
            </td>
            <td>
              {{ reversedIsZjfw }}
            </td>
          </tr>
          <tr>
            <td>
              <b>办理流程图说明：</b>
            </td>
            <td colspan="3">
              {{ form.handleDesc }}
            </td>
          </tr>
          <tr>
            <td>
              <b>设定依据：</b>
            </td>
            <td colspan="3">
              {{ form.setAccord }}
            </td>
          </tr>
          <tr v-if="form.isZjfw == 1">
            <td>
              <b>中介名称：</b>
            </td>
            <td colspan="3">
              {{ form.zjfwName }}
            </td>
          </tr>
          <tr>
            <td>
              <b>咨询方式：</b>
            </td>
            <td colspan="3">
              {{ form.zixunType }}
            </td>
          </tr>
          <tr>
            <td>
              <b>监督方式：</b>
            </td>
            <td colspan="3">
              {{ form.jianduType }}
            </td>
          </tr>
          <tr>
            <td>
              <b>联办能力：</b>
            </td>
            <td>
              {{ reversedUnionOrganFlag }}
            </td>
            <td>
              <b>是否支持预约办理：</b>
            </td>
            <td>
              {{ reversedAppointmentFlag }}
            </td>
          </tr>
          <tr>
            <td>
              <b>办理形式：</b>
            </td>
            <td colspan="3">
              {{ reversedHandleForm }}
            </td>
          </tr>
          <tr>
            <td>
              <b>办理端要素：</b>
            </td>
            <td colspan="3">
              {{ elements }}
            </td>
          </tr>
          <tr>
            <td>
              <b>承诺时限(工作日)：</b>
            </td>
            <td>
              {{ form.promiseLimit }}
            </td>
            <td>
              <b>法定时限(工作日)：</b>
            </td>
            <td>
              {{ form.legalLimit }}
            </td>
          </tr>
          <tr>
            <td>
              <b>是否网上支付：</b>
            </td>
            <td>
              {{ reversedOnlinePayFlag }}
            </td>
            <td>
              <b>是否支持物流快递：</b>
            </td>
            <td>
              {{ reversedExpressFlag }}
            </td>
          </tr>
          <tr>
            <td>
              <b>线下跑动次数：</b>
            </td>
            <td colspan="3">
              {{ reversedCountToScence }}
            </td>
          </tr>
          <tr v-if="form.countToScence > 0">
            <td>
              <b>线下跑动的原因和环节：</b>
            </td>
            <td colspan="3">
              {{ form.reasonToScence }}
            </td>
          </tr>
        </table>
      </div>
    </el-tab-pane>
    <el-tab-pane label="事项信息" name="second">
      <div>
        <el-table :data="comboServiceList" border>
          <el-table-column label="序号" width="80" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="organName"
            label="实施机构"
            align="center"
            :show-overflow-tooltip="true"
          >
          </el-table-column>
          <el-table-column
            prop="serviceName"
            label="事项名称"
            align="center"
            :show-overflow-tooltip="true"
          >
          </el-table-column>
          <el-table-column
            prop="implementCode"
            label="实施编码"
            align="center"
            :show-overflow-tooltip="true"
          >
          </el-table-column>
          <el-table-column
            prop="status"
            label="状态"
            align="center"
            :formatter="getStatus"
            class-name="small-padding fixed-width"
          >
          </el-table-column>
        </el-table>
        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getServiceList"
        />
      </div>
    </el-tab-pane>
    <el-tab-pane label="材料信息" name="third">
      <div>
        <div class="zf-zc-table--title">公共材料</div>
        <el-table :data="directoryMaterialList" border>
          <el-table-column label="序号" width="80" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="materialName"
            label="材料名称"
            align="center"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            prop="materialType"
            :formatter="getMaterialType"
            label="材料类型"
            align="center"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            prop="materialFormat"
            align="center"
            :formatter="getMaterialFormat"
            label="材料形式"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            prop="paperNumber"
            label="份数"
            align="center"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            prop="mustFlag"
            :formatter="getMustFlag"
            label="必要性"
            align="center"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            prop="status"
            :formatter="getStatus"
            align="center"
            label="状态"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-chakan"
                @click="handleView(scope.row)"
                v-hasPermi="['combo:directory:view']"
                >查看</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div>
        <div class="zf-zc-table--title">事项材料信息</div>
        <el-table ref="multipleTable" :data="sxMaterialList" border>
          <el-table-column label="序号" width="80" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="serviceName"
            label="所属事项名称"
            align="center"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            prop="materialName"
            label="材料名称"
            align="center"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            prop="materialType"
            label="材料类型"
            align="center"
            :formatter="getMaterialType"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            prop="materialFormat"
            :formatter="getSxMaterialFormat"
            label="材料形式"
            align="center"
            :show-overflow-tooltip="true"
          />
          <el-table-column prop="paperNumber" align="center" label="份数" />
          <el-table-column
            prop="mustFlag"
            :formatter="getMustFlag"
            align="center"
            label="必要性"
            :show-overflow-tooltip="true"
            class-name="small-padding fixed-width"
          />
        </el-table>
      </div>
    </el-tab-pane>
    <el-tab-pane label="证照信息" name="fourth">
      <div>
        <div class="zf-zc-table--title">统一证照</div>
        <el-table :data="directoryResultList" border>
          <el-table-column label="序号" width="80" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="resultName"
            label="证照名称"
            align="center"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            prop="resultSampleName"
            label="证照样本"
            align="center"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            prop="status"
            label="状态"
            :formatter="getStatus"
            align="center"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-chakan"
                @click="resultDirectoryView(scope.row)"
                v-hasPermi="['combo:directory:view']"
                >查看证照目录配置</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div>
        <div class="zf-zc-table--title">事项证照信息</div>
        <el-table :data="comboSxResultList" border>
          <el-table-column label="序号" width="80" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="serviceName"
            label="事项名称"
            align="center"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            prop="resultName"
            label="证照名称"
            align="center"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            prop="resultSampleName"
            label="证照样本"
            align="center"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-chakan"
                @click="resultServiceView(scope.row)"
                v-hasPermi="['combo:directory:view']"
                >查看证照目录配置</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-tab-pane>
    <div class="btn-wrap">
      <div class="btn-list mt10">
        <!-- <el-button style="margin-left: 90%;" @click="viewDialog()">关闭</el-button> -->
      </div>
    </div>

    <!--引入文件的预览弹出层-->
    <el-dialog
      v-dialog-drag
      title="文件预览"
      :visible.sync="view.show"
      v-for="(view, index) in viewDialogOptions"
      :key="index"
      :close-on-click-modal="false"
      @close="closeFileView"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <combo-dire-file-view
        :attaOid="view.attaOid"
        @father-click="closeFileView"
      ></combo-dire-file-view>
      <div slot="footer" class="zf-text-center">
        <el-button @click="closeFileView">关 闭</el-button>
      </div>
    </el-dialog>

    <!--查看目录公共材料开始-->
    <el-dialog
      v-dialog-drag
      :visible.sync="view.show"
      v-for="(view, idx) in viewMaterialDialogOptions"
      :key="idx"
      title="查看目录公共材料"
      :close-on-click-modal="false"
      width="1100px"
      append-to-body
    >
      <view-directory-material
        :materialOid="view.materialOid"

      >
      </view-directory-material>
      <div slot="footer" style="text-align:center">
        <el-button @click="closeDireMaterial">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 目录配置查看 -->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openInitMater"
      v-if="openInitMater"
      width="1100px"
      append-to-body
    >
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="20%" />
          <col width="30%" />
          <col width="20%" />
          <col width="30%" />
        </colgroup>
        <tr>
          <td>
            <b>所属证照目录：</b>
          </td>
          <td colspan="3">
            {{ formResult.billOid }}
          </td>
        </tr>
        <tr></tr>
        <tr>
          <td><b>证照目录编码：</b></td>
          <td>{{ formResult.directoryCode }}</td>
          <td>
            <b>
              证照目录类型：
            </b>
          </td>
          <td>
            <b v-if="formResult.directoryType == '0'">证照</b>
            <b v-if="formResult.directoryType == '1'">批文</b>
          </td>
        </tr>
      </table>

      <div slot="footer" class="zf-text-center">
        <el-button @click="cancelMater">关 闭</el-button>
      </div>
    </el-dialog>
  </el-tabs>
</template>
<script>
import { getOne } from "@/api/onething/sxpz/comboDirectory";
import comboDireFileView from "@/views/onething/sxpz/comboDirectory/comboDireFileView";
import { downloadFile } from "@/api/onething/sxpz/comboAtta";
import { page } from "@/api/onething/sxpz/comboService";
import {
  queryDirectoryResultList,
  querySxServiceResultList
} from "@/api/onething/sxpz/comboDirectoryResult";
import {
  queryComboDireSxMaterList,
  queryDirectoryMaterialList
} from "@/api/onething/sxpz/comboDirectoryMaterial";
import ViewDirectoryMaterial from "@/views/onething/sxpz/comboDirectory/viewDirectoryMaterial";
import {
  handleInitResultMater,
  queryMaterBill
} from "@/api/onething/sxpz/sxSerMaterBill";

export default {
  name: "comboDirectoryView",
  props: ["comboDirectoryOid"],
  components: {
    ViewDirectoryMaterial,
    comboDireFileView
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      activeName: "first",
      directoryOid: this.comboDirectoryOid,
      //关联事项总条数
      total: 0,
      //关联事项数据
      comboServiceList: [],
      //统一证照
      directoryResultList: [],
      //事项证照信息
      comboSxResultList: [],
      //公共材料
      directoryMaterialList: [],
      //事项材料
      sxMaterialList: [],
      //目录信息
      form: {
        comboServiceObject: "1",
        ifCharge: 0,
        isZjfw: 0,
        appointmentFlag: 0,
        handleForm: "0",
        onlinePayFlag: 0,
        expressFlag: 0,
        unionOrganFlag: 0,
        countToScence: 0
      },
      //证照目录查看
      formResult: {},
      // 查询参数
      queryParams: {
        comboDirectoryOid: this.comboDirectoryOid,
        pageNum: 1,
        pageSize: 10
      },
      //预览
      viewDialogOptions: [],
      //公共材料查看
      viewMaterialDialogOptions: [],
      //目录配置查看
      openInitMater: false,
      //证照列表
      billList: []
    };
  },
  computed: {
    // 计算属性的 getter
    reversedCountToScence: function() {
      if (this.form.countToScence == 0) {
        return "0次";
      } else if (this.form.countToScence == 1) {
        return "1次";
      } else if (this.form.countToScence == 2) {
        return "2次";
      } else if (this.form.countToScence == 3) {
        return "多次";
      }
      return "";
    },
    reversedExpressFlag: function() {
      if (this.form.expressFlag == 0) {
        return "否";
      } else if (this.form.expressFlag == 1) {
        return "是";
      }
      return "";
    },
    reversedOnlinePayFlag: function() {
      if (this.form.onlinePayFlag == 0) {
        return "否";
      } else if (this.form.onlinePayFlag == 1) {
        return "是";
      }
      return "";
    },
    reversedHandleForm: function() {
      if (this.form.handleForm) {
        if (this.form.handleForm == "0") {
          return "窗口办理";
        } else if (this.form.handleForm == "1") {
          return "网上办理";
        } else if (this.form.handleForm == "2") {
          return "一体化办理";
        }
      }
      return "";
    },
    elements: function() {
      if (this.form.elements) {
        let result = "";
        let split = this.form.elements.split(",");
        split.forEach(element => {
          if (element == 1) {
            result += "窗口端，";
          } else if (element == 3) {
            result += "移动端，";
          } else if (element == 2) {
            result += "网站端，";
          } else if (element == 4) {
            result += "自助终端，";
          }
        });
        return result.substr(0, result.length - 1);
      }
      return "";
    },
    reversedAppointmentFlag: function() {
      if (this.form.appointmentFlag == 0) {
        return "否";
      } else if (this.form.appointmentFlag == 1) {
        return "是";
      }
      return "";
    },
    reversedUnionOrganFlag: function() {
      if (this.form.unionOrganFlag == 0) {
        return "否";
      } else if (this.form.unionOrganFlag == 1) {
        return "是";
      }
      return "";
    },
    reversedIsZjfw: function() {
      if (this.form.isZjfw == 0) {
        return "否";
      } else if (this.form.isZjfw == 1) {
        return "是";
      }
      return "";
    },
    reversedIfCharge: function() {
      if (this.form.ifCharge == 0) {
        return "否";
      } else if (this.form.ifCharge == 1) {
        return "是";
      }
      return "";
    }
  },
  created() {
    this.getOneDirectory();
    this.getBillList();
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    },
    viewDialog() {
      this.$emit("combo-directory");
    },
    //查询目录信息
    getOneDirectory() {
      getOne(this.directoryOid).then(response => {
        this.form = response.data;
        //查询目录关联事项列表
        this.getServiceList();
        //材料
        this.getDirectoryMaterialList();
        this.getSxMaterialList();

        //证照
        this.getDirectoryResultlList();
        this.getSxResultList();
      });
    },
    /** 查询目录关联事项列表 */
    getServiceList() {
      page(this.queryParams).then(response => {
        this.comboServiceList = response.data.data;
        this.total = response.data.total;
      });
    },
    //事项配置确认状态
    getStatus(val) {
      if (val.status == 0) {
        return "暂存";
      } else if (val.status == 1) {
        return "已配置";
      } else {
        return "";
      }
    },
    //统一证照
    getDirectoryResultlList() {
      queryDirectoryResultList(this.directoryOid).then(response => {
        this.directoryResultList = response.data;
      });
    },
    //事项证照信息
    getSxResultList() {
      querySxServiceResultList(this.directoryOid).then(response => {
        this.comboSxResultList = response.data;
      });
    },
    /** 公共证照目录按钮操作 */
    resultDirectoryView(row) {
      let _that = this;
      if (row.resultOid) {
        handleInitResultMater(row.resultOid).then(response => {
          _that.openInitMater = true;
          _that.formResult = response.data;
          this.formResult.billOid = this.getResultName(this.formResult.billOid);
        });
        _that.title = "证照目录关联查看";
      }
    },
    /*事项证照目录查看*/
    resultServiceView(row) {
      let _that = this;
      if (row.sxResultOid) {
        handleInitResultMater(row.sxResultOid).then(response => {
          _that.openInitMater = true;
          _that.formResult = response.data;
          this.formResult.billOid = this.getResultName(this.formResult.billOid);
        });
        _that.title = "证照目录关联查看";
      }
    },
    cancelMater() {
      this.openInitMater = false;
      this.title = "目录信息";
    },
    getBillList() {
      let _that = this;
      // 查询文书模板数据
      queryMaterBill().then(response => {
        _that.billList = response.data;
      });
    },
    //获取证照目录名称
    getResultName(billOid) {
      let directoryName = "";
      this.billList.find(b => {
        if (b.billOid == billOid) {
          directoryName = b.directoryName;
        }
      });
      return directoryName;
    },
    //公共材料
    getDirectoryMaterialList() {
      queryDirectoryMaterialList(this.directoryOid).then(response => {
        this.directoryMaterialList = response.data;
      });
    },
    /** 公共材料查看按钮操作 */
    handleView(row) {
      let item = {
        show: true,
        materialOid: row.materialOid
      };
      this.viewMaterialDialogOptions.push(item);
    },
    closeDireMaterial() {
      this.viewMaterialDialogOptions.pop();
    },
    //事项材料
    getSxMaterialList() {
      queryComboDireSxMaterList(this.directoryOid).then(response => {
        this.sxMaterialList = response.data;
      });
    },
    getMaterialFormat(val) {
      if (val.materialFormat == 1) {
        return "纸质";
      } else if (val.materialFormat == 2) {
        return "电子版";
      } else if (val.materialFormat == 5) {
        return "纸质、电子版";
      } else {
        return "";
      }
    },
    getSxMaterialFormat(val) {
      if (val.materialFormat == 1) {
        return "纸质";
      } else if (val.materialFormat == 2) {
        return "电子版";
      } else if (val.materialFormat == 3) {
        return '证照';
      }else if (val.materialFormat == 4) {
        return '容缺补正';
      }else if (val.materialFormat == 7) {
        return '告知承诺';
      }else  {
        return '';
      }
    },
    getMaterialType(val) {
      if (val.materialType == 0) {
        return "原件";
      } else if (val.materialType == 1) {
        return "复印件";
      } else if (val.materialType == 2) {
        return "原件和复印件";
      }else if (val.materialType == 3) {
        return "原件或复印件";
      } else {
        return "";
      }
    },
    getMustFlag(val) {
      if (val.mustFlag == 0) {
        return "必要";
      } else if (val.mustFlag == 1) {
        return "非必要";
      } else if (val.mustFlag == 2) {
        return "容缺后补";
      } else if (val.mustFlag == 3) {
        return "信息免交、信用后补";
      }else {
        return "";
      }
    },
    //下载附件
    downloadFile(attaOid) {
      downloadFile(attaOid);
    },
    //预览附件
    viewFile(attaOid) {
      let item = {
        show: true,
        attaOid: attaOid
      };
      this.viewDialogOptions.push(item);
    },
    //关闭预览附件
    closeFileView() {
      this.viewDialogOptions.pop();
    }
  }
};
</script>
<!-- <style lang="scss" scoped>
  .el-scrollbar__wrap{overflow: hidden}
  .dialog-table{padding: 5px;}
</style> -->
<style lang="scss" scoped>
.other-table table tr td {
  color: #606266;
  text-align: left !important;
}
</style>
