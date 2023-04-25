/* * @Description:事项表单配置 * @Author: xiayj * @Date: 2021/7/14 **/
<template>
  <div class="app-container">
    <el-row :gutter="20" style="height: 100%">
<!--      <el-col :lg="5" :xl="3" class="app-left" style="height: 100%">
        <div class="tree-title mb10">事项类型</div>
        <div class="item-type">
          <div
            v-for="(serviceType, index) in serviceTypeList"
            :key="index"
            :class="[
              activeClass === index ? 'active' : '',
              'item-type&#45;&#45;children',
            ]"
            @click="queryServiceAudit(index, serviceType.oid)"
          >
            {{ serviceType.name }}&nbsp;
            <el-badge :value="serviceType.number" class="item"></el-badge>
          </div>
        </div>
      </el-col>-->
      <!--实施清单已发布列表数据-->
      <el-col :lg="24" :xl="24" class="app-right">
        <el-form
          class="dynamic-search-form"
          :model="queryParams"
          ref="queryForm"
          :inline="true"
          @submit.native.prevent
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
          <el-form-item label="办件状态" prop="caseType">
            <el-radio-group v-model="queryParams.caseType">
              <el-radio
                v-for="(status, key) in statusOptions"
                :key="key"
                :label="key"
              >{{ status }}</el-radio
              >
            </el-radio-group>
          </el-form-item>
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
              @click="resetQuery"
              class="ml5"
            >重置</el-button
            >
          </el-form-item>
        </el-form>
        <el-table v-loading="loading" :data="sxServiceList">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="所属部门"
            width="200"
            align="center"
            prop="organName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="事项名称"
            align="center"
            prop="serviceName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="状态"
            width="100"
            :formatter="auditFormat"
            align="center"
            prop="contentStatus"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="操作"
            width="560"
            align="center"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-chakan"
                @click="handleView(scope.row)"
                v-hasPermi="['form:config:view']"
              >
                查看
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="handleLabel(scope.row)"
                v-hasPermi="['form:config:label']"
              >
                字段标签配置
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="handleField(scope.row)"
                v-hasPermi="['form:config:field']"
              >
                字段配置
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="handleFillField(scope.row)"
                v-hasPermi="['form:config:add']"
              >
                字段值填充配置
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="handleFormConfig(scope.row)"
                v-hasPermi="['form:config:add']"
              >
                表单配置
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

    <!--字段标签开始-->
    <el-dialog
      v-dialog-drag
      :visible.sync="openLabel"
      v-if="openLabel"
      :title="title"
      width="1100px"
      scrollbar
      height="800px"
      append-to-body
    >
      <form-fill-label-list
        :serviceOid="serviceOid"
        :serviceName="serviceName"
        :openLabel="openLabel"
        @service-option-close="labelClose"
      >
      </form-fill-label-list>
    </el-dialog>
    <!--字段标签结束-->

    <!--字段开始-->
    <el-dialog
      v-dialog-drag
      :visible.sync="openField"
      v-if="openField"
      :title="title"
      width="1100px"
      scrollbar
      height="800px"
      append-to-body
    >
      <form-fill-field-list
        :serviceOid="serviceOid"
        :serviceName="serviceName"
        @service-option-close="fieldClose"
      >
      </form-fill-field-list>
    </el-dialog>
    <!--字段结束-->
    <!--字段值填充配置-->
    <el-dialog
      v-dialog-drag
      :visible.sync="openFillField"
      v-if="openFillField"
      :title="title"
      width="1100px"
      scrollbar
      height="800px"
      append-to-body
    >
      <form-field-rel-config
        :serviceOid="serviceOid"
        :serviceName="serviceName"
        @service-option-close="fieldFillClose"
      >
      </form-field-rel-config>
    </el-dialog>

    <!--表单配置开始-->
    <el-dialog
      v-dialog-drag
      :visible.sync="openDesign"
      v-if="openDesign"
      :title="title"
      width="1100px"
      scrollbar
      height="calc(100vh - 220px)"
      append-to-body
    >
      <form-design-list
        :serviceOid="serviceOid"
        :serviceName="serviceName"
        @service-option-close="designClose"
      >
      </form-design-list>
    </el-dialog>
    <!--表单配置结束-->
    <!-- 表单配置开始2.0 -->
    <el-dialog
      v-if="formConfigVisible"
      v-dialog-drag
      :visible.sync="formConfigVisible"
      :title="title"
      width="1100px"
      height="700px"
      append-to-body
      bodyBackgroundColor="#E3E8EB"
    >
      <FormConfig
        :serviceOid="serviceOid"
        :serviceName="serviceName"
        @close="formConfigVisible = false"
      />
    </el-dialog>
    <!-- 表单配置结束2.0 -->
    <!-- 信息详细 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="infoView"
      v-if="infoView"
      title="查看实施清单详情"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <view-sx-service-info
        :sxServiceOid="serviceOid"
        @view-service="viewServiceClose"
      ></view-sx-service-info>
      <div slot="footer" class="zf-text-center">
        <el-button @click="infoView = false"> 关闭 </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import formFillLabelList from "@/views/zc/sxService/formConfig/formFillLabelList";
import formFillFieldList from "@/views/zc/sxService/formConfig/formFillFieldList";
import formDesignList from "@/views/zc/sxService/formConfig/formDesignList";
import FormConfig from './formConfig.vue';
import { sxServicePage } from "@/api/sxpt/service";
import { getServiceTypeNumber } from "@/api/sxpt/serviceType";
import auditStatus from "@/api/zc/sxService/auditStatus";
import { ELEMENTTYPE } from "@/api/zc/formUtil/enum";
import formFieldRelConfig from "@/views/zc/sxService/formConfig/formFieldRelConfig.vue";
import viewSxServiceInfo from "@/views/zc/businessSupervise/viewSxServiceInfo";
import FormFieldRelConfig from "@/views/zc/sxService/formConfig/formFieldRelConfig";
export default {
  components: {
    FormFieldRelConfig,
    formFillLabelList,
    formFillFieldList,
    formDesignList,
    viewSxServiceInfo,
    FormConfig,formFieldRelConfig
  },
  name: "sxServiceFormConfigIndex",
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      attatotal: 0,
      // 应用表格数据
      sxServiceList: [],
      //查询控件数组
      factorArr: [],
      //表单类型枚举
      ELEMENTTYPE: ELEMENTTYPE,
      // 查看弹出层
      viewDialogOptions: {},
      // 弹出层标题
      title: "",
      // 查看显示弹出层
      infoView: false,
      statusOptions: { "1": "即办件", "2": "承诺件" },
      openLabel: false,
      openField: false,
      openDesign: false,
      serviceOid: "",
      serviceName: "",
      // 查询参数 查询已发布数据
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        serviceName: "",
        serviceOid: "",
        implementCode: "",
        serviceStatus: "3",
        existChildItem: 0,
        caseType: "",
        serviceTypeOid: ""
      },
      // 事项类型
      serviceTypeList: [],
      activeClass: false,

      // 表单配置
      formConfigVisible: false,
      openFillField:false,
    };
  },
  created () {
    //this.getServiceTypeTree(this.queryParams.serviceStatus);
    this.getList();
  },
  methods: {
    /** 获取事项类型树 */
    getServiceTypeTree (serviceStatus) {
      getServiceTypeNumber(serviceStatus).then(response => {
        this.serviceTypeList = response.data;
      });
    },
    queryServiceAudit (index, serviceTypeOid) {
      this.queryParams.caseType = '';
      this.queryParams.serviceTypeOid = serviceTypeOid;
      this.getList();
      this.activeClass = index;
    },
    /** 查询一实施清单列表 */
    getList () {
      this.loading = true;
      sxServicePage(this.queryParams).then(response => {
        this.sxServiceList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    // 事项状态
    auditFormat (row) {
      return this.selectMapLabel(new auditStatus(), row.serviceStatus);
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      /**获取当前选中值 */
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 查看按钮操作 */
    handleView (row) {
      this.serviceOid = row.serviceOid;
      this.infoView = true;
      this.viewDialogOptions.serviceOid = row.serviceOid;
      this.viewDialogOptions.lableTypeOid = row.lableTypeOid;
    },
    viewServiceClose () {
      this.infoView = false;
    },
    handleLabel (row) {
      this.openLabel = true;
      this.serviceOid = row.serviceOid;
      this.serviceName = row.serviceName;
      this.title = "字段标签配置【" + row.serviceName + "】";
    },

    labelClose () {
      this.openLabel = false;
    },

    handleField (row) {
      this.openField = true;
      this.serviceOid = row.serviceOid;
      this.serviceName = row.serviceName;
      this.title = "字段配置【" + this.serviceName + "】";
    },

    fieldClose () {
      this.openField = false;
    },

    fieldFillClose () {
      this.openFillField = false;
    },

    handleDesign (row) {
      this.openDesign = true;
      this.serviceOid = row.serviceOid;
      this.serviceName = row.serviceName;
      this.title = "表单配置【" + this.serviceName + "】";
    },

    handleFormConfig (row) {
      this.formConfigVisible = true;
      this.serviceOid = row.serviceOid;
      this.serviceName = row.serviceName;
      this.title = "表单配置【" + this.serviceName + "】";
    },

    designClose () {
      this.openDesign = false;
    },
    handleFillField(row){
      this.openFillField=true;
      this.serviceOid = row.serviceOid;
      this.serviceName = row.serviceName;
      this.title = "字段值填充配置【" + this.serviceName + "】";
    }
  }
};
</script>
<style lang="scss" scoped>
.item-type {
  height: calc(100% - 45px);
  overflow-y: auto;
  .item-type--children {
    background-color: #fff;
    display: flex;
    align-items: center;
    padding: 10px;
    margin-bottom: 4px;
    cursor: pointer;
    border-radius: 3px;

    &.active {
      background-color: #9abcfb;
    }

    &:not(.active):hover {
      background-color: #d7e5ff;
    }
  }
}
</style>
