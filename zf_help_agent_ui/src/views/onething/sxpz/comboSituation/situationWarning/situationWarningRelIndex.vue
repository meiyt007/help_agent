/*
* @Description:一件事情形阻塞配置列表
* @Author: dxl
**/
<template>
  <div>
    <el-row :gutter="20">
      <!--一件事目录列表数据-->
      <el-col :span="24" :xs="24">
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
        <el-table
          v-loading="loading"
          :data="situationWarningRelList"
          :fit="true"
        >
          <el-table-column
            label="序号"
            min-width="15%"
            type="index"
            align="center"
          >
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="阻塞选项"
            min-width="40%"
            align="center"
            :show-overflow-tooltip="true"
          >
            <template slot-scope="scope">
              <div
                align="left"
                class="div-ellipsis"
                v-for="(item, idx) in scope.row.valList"
                :key="idx"
                :title="item"
              >
                {{ idx + 1 }}、{{ item }}
              </div>
            </template>
          </el-table-column>
          <el-table-column
            label="阻塞提示"
            min-width="30%"
            prop="warningMsg"
            align="center"
            :show-overflow-tooltip="true"
          >
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
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
    </el-row>

    <!--新增/修改-->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :visible.sync="openInit"
      v-if="openInit"
      title="情形阻塞信息配置"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <situation-warning-rel-add
        v-if="openInit"
        :relOid="allValOptions.relOid"
        :comboDirectoryOid="allValOptions.comboDirectoryOid"
        ref="rel"
        @combo-form="comboFormClose"
      >
      </situation-warning-rel-add>
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
  page,
  delWarningRel
} from "@/api/onething/comboSituation/optionWarning.js";
import situationWarningRelAdd from "@/views/onething/sxpz/comboSituation/situationWarning/situationWarningRelAdd.vue"
export default {
  components: {
    situationWarningRelAdd
  },
  props: {
    //一件事目录主键
    comboDirectoryOid: {
      type: String
    }
  },
  name: "ComboSituationWarningRelIndex",
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      situationWarningRelList: [],
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
      page(this.queryParams).then(response => {
        this.situationWarningRelList = response.data.data;
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
      if (row.oid != undefined) {
        this.allValOptions.relOid = row.oid;
      } else {
        this.allValOptions.relOid = '';
      }
      this.allValOptions.comboDirectoryOid = this.comboDirectoryOid;
    },
    //删除
    handledel (row) {
      const id = row.oid;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(function () {
        return delWarningRel(id);
      }).then(() => {
        this.msgSuccess("删除成功");
        this.getList();
      }).catch(function () { });
    },
  }
};

</script>
