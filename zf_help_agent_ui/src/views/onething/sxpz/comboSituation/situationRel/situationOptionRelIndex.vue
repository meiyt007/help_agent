/*
* @Description:一件事情形关系配置列表
* @Author: dxl
**/
<template>
  <div>
    <div style="margin-bottom: 10px">
      <el-button
        type="default"
        icon="el-icon-plus"
        size="mini"
        @click="handleInit"
        v-hasPermi="['combo:form:save']"
      >
        新增
      </el-button>
      <el-button
        type="default"
        icon="el-icon-connection"
        size="mini"
        @click="viewPic"
        v-hasPermi="['combo:form:save']"
      >
        情形关系图
      </el-button>
    </div>
    <el-table v-loading="loading" :data="situationOptionRelList" :fit="true">
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
            v-for="(item, idx) in scope.row.optList"
            :key="idx"
            :title="item"
          >
            {{ idx + 1 }}、{{ item }}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="展示情形" min-width="30%" align="center">
        <template slot-scope="scope">
          <div
            align="left"
            class="div-ellipsis"
            v-for="(item, idx) in scope.row.situList"
            :key="idx"
            :title="item"
          >
            {{ idx + 1 }}、{{ item }}
          </div>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        min-width="25%"
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
      title="情形选项关联信息查看"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <situation-option-rel-view
        :relOid="allValOptions.relOid"
        :comboDirectoryOid="allValOptions.comboDirectoryOid"
      >
      </situation-option-rel-view>
      <div slot="footer" align="center">
        <el-button @click="comboDirectoryClose">取 消</el-button>
      </div>
    </el-dialog>

    <!--新增/修改-->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :visible.sync="openInit"
      v-if="openInit"
      title="情形选项关联信息配置"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <situation-option-rel-add
        v-if="openInit"
        ref="situationOptionAdd"
        :relOid="allValOptions.relOid"
        :comboDirectoryOid="allValOptions.comboDirectoryOid"
        @combo-form="comboFormClose"
      >
      </situation-option-rel-add>
      <div slot="footer" style="text-align: center">
        <el-button
          type="primary"
          @click="
            () => {
              $refs.situationOptionAdd.submitForm()
            }
          "
          >确 定</el-button
        >
        <el-button @click="comboFormClose">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 选项关系图 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="openPic"
      v-if="openPic"
      title="选项关系图"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <situation-option-rel-picture
        v-if="openPic"
        :comboDirectoryOid="allValOptions.comboDirectoryOid"
        @combo-form="comboFormClose"
      >
      </situation-option-rel-picture>
    </el-dialog>
  </div>
</template>

<script>
import {
  situationOptionRelPage,
  delOptionRel
} from "@/api/onething/comboSituation/comboOptionRel.js";
import situationOptionRelView from "@/views/onething/sxpz/comboSituation/situationRel/situationOptionRelView.vue";
import situationOptionRelAdd from "@/views/onething/sxpz/comboSituation/situationRel/situationOptionRelAdd.vue"
import situationOptionRelPicture from "@/views/onething/sxpz/comboSituation/situationRel/situationOptionRelPicture.vue"
export default {
  components: {
    situationOptionRelView,
    situationOptionRelAdd,
    situationOptionRelPicture
  },
  props: {
    //一件事目录主键
    comboDirectoryOid: {
      type: String
    }
  },
  name: "SituationOptionRelIndex",
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      situationOptionRelList: [],
      // 弹出层标题
      title: "",
      // 查看显示弹出层
      openView: false,
      // 修改显示弹出层
      openInit: false,
      // 选项关系图
      openPic: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
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
      situationOptionRelPage(this.queryParams).then(response => {
        this.situationOptionRelList = response.data.data;
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
      this.allValOptions.relOid = row.comboOptionRel.relOid;
      this.allValOptions.comboDirectoryOid = this.comboDirectoryOid;
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
    //编辑
    handleInit (row) {
      this.openInit = true;
      if (row.comboOptionRel != undefined) {
        this.allValOptions.relOid = row.comboOptionRel.relOid;
      } else {
        this.allValOptions.relOid = '';
      }
      this.allValOptions.comboDirectoryOid = this.comboDirectoryOid;
    },
    //删除
    handledel (row) {
      const id = row.comboOptionRel.id;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(function () {
        return delOptionRel(id);
      }).then(() => {
        this.msgSuccess("删除成功");
        this.getList();
      }).catch(function () { });
    },
    // 查看情形关系图
    viewPic () {
      this.openPic = true;
      this.allValOptions.comboDirectoryOid = this.comboDirectoryOid;
    },
  }
};

</script>
