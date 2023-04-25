/*
* @Description:一件事情形结果关系配置列表
* @Author: dxl
**/
<template>
  <div>
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
    <el-table v-loading="loading" :data="situationResultRelList" :fit="true">
      <el-table-column label="序号" min-width="15%" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="前置条件选项" min-width="40%" align="center">
        <template slot-scope="scope">
          <div
            align="left"
            class="div-ellipsis"
            v-for="(item, idx) in scope.row.valList"
            :title="item"
            :key="idx"
          >
            {{ idx + 1 }}、{{ item }}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="事项结果" min-width="30%" align="center">
        <template slot-scope="scope">
          <div
            align="left"
            class="div-ellipsis"
            v-for="(item, idx) in scope.row.resultList"
            :title="item.serviceName"
            :key="idx"
          >
            {{ idx + 1 }}、{{ item.serviceName }}
          </div>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        min-width="15%"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
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
      :page.sync="queryParams.pageNumber"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!--新增/修改-->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :visible.sync="openInit"
      v-if="openInit"
      title="情形结果关联信息配置"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <situation-result-rel-add
        v-if="openInit"
        ref="rel"
        :relOid="allValOptions.relOid"
        :comboDirectoryOid="allValOptions.comboDirectoryOid"
        @combo-form="comboFormClose"
      >
      </situation-result-rel-add>
      <div slot="footer" align="center">
        <el-button
          type="primary"
          @click="
            () => {
              $refs.rel.submitForm()
            }
          "
        >
          确 定
        </el-button>
        <el-button @click="comboFormClose">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  optionResultVoPage,
  delResultRel
} from "@/api/onething/comboSituation/optionResult.js";
import situationResultRelAdd from "@/views/onething/sxpz/comboSituation/situationResult/situationResultRelAdd.vue"
export default {
  components: {
    situationResultRelAdd
  },
  props: {
    //一件事目录主键
    comboDirectoryOid: {
      type: String
    }
  },
  name: "ComboSituationResultRelIndex",
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      situationResultRelList: [],
      // 弹出层标题
      title: "",
      // 修改显示弹出层
      openInit: false,
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
        comboDirectoryOid: ''
      },
      allValOptions: {},
    };
  },
  created () {
    this.getList();
  },
  methods: {
    /** 查询情形关系配置列表 */
    getList () {
      this.loading = true;
      this.queryParams.comboDirectoryOid = this.comboDirectoryOid;
      optionResultVoPage(this.queryParams).then(response => {
        if (response.data) {
          this.situationResultRelList = response.data.data;
          this.total = response.data.total;
        }
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
    //关闭刷新
    comboFormClose (val) {
      this.allValOptions = {};
      this.openInit = false;
      if (val == 1) {
        this.getList();
      }
    },
    //编辑
    handleInit (row) {
      this.openInit = true;
      if (row.relOid != undefined) {
        this.allValOptions.relOid = row.relOid;
      } else {
        this.allValOptions.relOid = '';
      }
      this.allValOptions.comboDirectoryOid = this.comboDirectoryOid;
    },
    //删除
    handledel (row) {
      const id = row.relOid;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(function () {
        return delResultRel(id);
      }).then(() => {
        this.msgSuccess("删除成功");
        this.getList();
      }).catch(function () { });
    },
  }
};

</script>
