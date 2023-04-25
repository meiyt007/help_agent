/*
* @Description:一件事情形配置列表
* @Author: dxl
**/
<template>
  <div>
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="70px"
      @submit.native.prevent
    >
      <el-form-item label="情形名称" prop="situationName">
        <el-input
          v-model.trim="queryParams.situationName"
          placeholder="请输入情形名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
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
          class="btn-reset"
          >重置
        </el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit"
          v-hasPermi="['combo:form:save']"
          >新增</el-button
        >
      </el-col>
    </el-row>
    <el-table v-loading="loading" :data="situationList" :fit="true">
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="情形名称"
        align="center"
        prop="situationName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="展示类型"
        align="center"
        prop="columnType"
        :formatter="typeFormat"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="是否必填"
        align="center"
        prop="mustStatus"
        :formatter="mustFormat"
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
        width="200"
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
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            v-hasPermi="['combo:form:save']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handledel(scope.row)"
            v-hasPermi="['combo:option:save']"
            >删除</el-button
          >
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

    <!--查看-->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :visible.sync="openView"
      v-if="openView"
      title="查看情形"
      append-to-body
      width="1100px"
      height="700px"
      scrollbar
    >
      <situation-view :situationOid="allValOptions.situationOid">
      </situation-view>
      <div slot="footer" style="text-align: center">
        <el-button @click="cancelComboOptionTitle()">关闭</el-button>
      </div>
    </el-dialog>

    <!--新增/修改-->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :visible.sync="openInit"
      v-if="openInit"
      :title="title"
      append-to-body
      width="1100px"
      height="700px"
      scrollbar
    >
      <situation-add
        v-if="openInit"
        :situationOid="allValOptions.situationOid"
        ref="situationAdd"
        @combo-form="comboFormClose"
        :comboDirectoryOid="allValOptions.comboDirectoryOid"
      >
      </situation-add>
      <div slot="footer" style="text-align: center">
        <el-button
          type="primary"
          @click="
            () => {
              $refs.situationAdd.submitForm()
            }
          "
        >
          确 定
        </el-button>
        <el-button @click="comboFormClose">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  situationPage,
  delSituation
} from "@/api/onething/comboSituation/comboSituationIndex.js";
import situationView from "@/views/onething/sxpz/comboSituation/situation/situationView.vue";
import situationAdd from "@//views/onething/sxpz/comboSituation/situation/situationAdd.vue"
export default {
  components: {
    situationView,
    situationAdd
  },
  props: {
    //一件事目录主键
    comboDirectoryOid: {
      type: String
    }
  },
  name: "SituationIndex",
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      situationList: [],
      // 弹出层标题
      title: "",
      // 查看显示弹出层
      openView: false,
      // 修改显示弹出层
      openInit: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        comboDirectoryOid: '',
        situationName: ''
      },
      allValOptions: {},
    };
  },
  created () {
    this.getList();
  },
  methods: {
    /** 查询情形列表 */
    getList () {
      this.loading = true;
      this.queryParams.comboDirectoryOid = this.comboDirectoryOid;
      situationPage(this.queryParams).then(response => {
        this.situationList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 查看按钮操作 */
    handleView (row) {
      this.allValOptions.situationOid = row.situationOid;
      this.openView = true;
    },
    //关闭
    comboDirectoryClose () {
      this.allValOptions = {};
      this.openView = false;
    },
    //关闭刷新
    comboFormClose (val) {
      this.allValOptions = {};
      this.openInit = false;
      if (val == 1) {
        this.getList();
      }
    },
    typeFormat (row) {
      return this.selectMapLabel({
        '1': '单选',
        '2': '多选',
        '3': '文本'
      }, row.columnType);
    },
    mustFormat (row) {
      return this.selectMapLabel({
        '1': '是',
        '0': '否'
      }, row.mustStatus);
    },
    //编辑
    handleInit (row) {
      this.openInit = true;
      this.allValOptions.situationOid = row.situationOid;
      this.allValOptions.comboDirectoryOid = this.comboDirectoryOid;
      if (row.situationOid == undefined) {
        this.title = "新增情形";
      } else {
        this.title = "编辑情形";
      }
    },
    //删除
    handledel (row) {
      const oid = row.id;
      this.$confirm('是否确认删除?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delSituation(oid);
      }).then(() => {
        this.msgSuccess("删除成功");
        this.getList();
      }).catch(function () { });
    },
    cancelComboOptionTitle () {
      this.openView = false;
    }
  }
};

</script>
