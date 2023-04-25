<template>
  <div>
    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit"
        >新增</el-button
        >
      </el-col>
    </el-row>
    <el-table v-loading="loading" :data="situationFieldFillList" :fit="true">
      <el-table-column label="序号" min-width="15%" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="选项值名称"
        min-width="40%"
        align="center"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <div
            align="left"
            v-for="(item, idx) in scope.row.optionNames"
            :key="idx"
          >
            {{ idx + 1 }}、{{ item }}
          </div>
        </template>
      </el-table-column>
      <el-table-column
        label="字段填充信息"
        min-width="30%"
        align="center"
        prop="fieldName"
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
          >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleDel(scope.row)"
          >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!--新增/修改-->
    <el-dialog
      v-dialog-drag
      :visible.sync="openInit"
      v-if="openInit"
      title="情形字段值编辑"
      width="900px"
      height="700px"
      scrollbar
      append-to-body
    >
      <situation-field-val-info
        ref="rel"
        v-if="openInit"
        :oid="allValOptions.oid"
        :serviceOid="allValOptions.serviceOid"
        @combo-form="closeInfo"
      >
      </situation-field-val-info>
      <div slot="footer" align="center">
        <el-button
          type="primary"
          @click="
            () => {
              $refs.rel.submitForm();
            }
          "
        >
          确 定
        </el-button>
        <el-button @click="closeInfo">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {getFieldFillValList, delSxOptionFieldVal} from "@/api/zc/qdgl/sxOptionField.js";
  import situationFieldValInfo from "@/views/zc/qdgl/situationFieldValInfo.vue";
  export default {
    components: {situationFieldValInfo},
    props: ["serviceOid"],
    name: "SituationFieldFill",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 应用表格数据
        situationFieldFillList: [],
        // 弹出层标题
        title: "",
        // 修改显示弹出层
        openInit: false,
        allValOptions: {}
      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询情形关系配置列表 */
      getList() {
        this.loading = true;
        getFieldFillValList(this.serviceOid).then(response => {
          this.situationFieldFillList = response.data;
          this.loading = false;
        });
      },
      //新增/修改
      handleInit(row) {
        this.openInit = true;
        if (row.oid != undefined) {
          this.allValOptions.oid = row.oid;
        }
        this.allValOptions.serviceOid = this.serviceOid;
      },
      //关闭刷新
      closeInfo() {
        this.allValOptions = {};
        this.openInit = false;
        this.getList();
      },
      //删除
      handleDel(row) {
        this.$confirm("是否确认删除?", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(function() {
            return delSxOptionFieldVal(row.oid);
          })
          .then(() => {
            this.msgSuccess("删除成功");
            this.getList();
          })
          .catch(function() {});
      }
    }
  };
</script>
