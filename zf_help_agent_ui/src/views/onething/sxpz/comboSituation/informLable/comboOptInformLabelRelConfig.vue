/*
* @Description:告知单标签关系配置
* @Author: dxl
**/
<template>
  <div>
    <el-row class="mb8 fl">
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

    <el-table
      v-loading="loading"
      :data="comboLabelOptInformLabelRelList"
      :fit="true"
    >
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="一件事情形选项集合" align="center">
        <template slot-scope="scope">
          <div
            align="left"
            class="div-ellipsis"
            v-for="(item, index) in scope.row.situationOptNames"
            :key="index"
            :title="item"
          >
            {{ index + 1 }}、{{ item }}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="告知单标签编码" align="center">
        <template slot-scope="scope">
          <div
            align="left"
            class="div-ellipsis"
            v-for="(item, index) in scope.row.labelCodes"
            :key="index"
            :title="item"
          >
            {{ index + 1 }}、{{ item }}
          </div>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        width="420"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            v-hasPermi="['combo:directory:view']"
            >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
            v-hasPermi="['combo:directory:view']"
            >删除
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

    <!-- 告知单标签配置开始 -->
    <el-dialog
      v-dialog-drag
      title="告知单标签关系配置"
      :visible.sync="openInit"
      v-if="openInit"
      :close-on-click-modal="false"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <opt-inform-label-rel-config-add
        :comboDirectoryOid="comboDirectoryOid"
        :optInformLabelRelOid="optInformLabelRelOid"
        :optInformLabelRelId="optInformLabelRelId"
        ref="rel"
        @close-label-rel-config-add="closeInformLabelRelConfigAdd"
      >
      </opt-inform-label-rel-config-add>
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
        <el-button @click="closeInformLabelRelConfigAdd">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  page,
  del
} from "@/api/onething/sxpz/inform/comboOptInformLabelRelConfig.js";
import optInformLabelRelConfigAdd
  from "@/views/onething/sxpz/comboSituation/informLable/OptInformLabelRelConfigAdd.vue";

export default {
  props: ["comboDirectoryOid"],
  components: {
    optInformLabelRelConfigAdd
  },
  name: "comboOptInformLabelRelConfig",
  //定义获取父类传过来值的格式
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      comboLabelOptInformLabelRelList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      openOptInformLabelRels: [],
      optInformLabelRelOid: "",
      optInformLabelRelId: "",
      openInit: false,
      title: ""
    }
  },
  created () {
    if (this.comboDirectoryOid !== undefined) {
      this.getList();
    }
  },
  methods: {
    getList () {
      this.loading = true;
      this.queryParams.comboDirectoryOid = this.comboDirectoryOid;
      page(this.queryParams).then(response => {
        this.comboLabelOptInformLabelRelList = response.data.data;
        console.log(this.comboLabelOptInformLabelRelList, '---')
        this.total = response.data.total;
        this.loading = false;
      })
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      const id = row.id;
      this.$confirm('是否确认删除?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return del(id);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(function () {
      });
    },
    // 取消按钮
    close () {
      this.$emit("close-inform-label-rel-config");
    },
    //增加或修改
    handleInit (row) {
      this.optInformLabelRelId = row.id;
      this.optInformLabelRelOid = row.oid;
      this.openInit = true;
    },
    //关闭告知单标签配置情形页
    closeInformLabelRelConfigAdd () {
      this.openInit = false;
      this.getList();
    }
  }
}

</script>
