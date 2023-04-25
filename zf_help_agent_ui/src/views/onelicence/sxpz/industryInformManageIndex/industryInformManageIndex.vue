/* * @Description: 一业一证告知单管理 * @Author: dxl **/



<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--一件事告知单管理列表数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true">
          <el-form-item label="所属主题" prop="comboDirectoryOid">
            <el-select v-model="queryParams.comboDirectoryOid" filterable placeholder="请选择所属主题" size="small"
              label-width="95px" clearable>
              <el-option v-for="comboDirectory in comboDirectoryList" :key="comboDirectory.comboDirectoryOid"
                :label="comboDirectory.comboDirectoryName" :value="comboDirectory.comboDirectoryOid" />
            </el-select>
          </el-form-item>
          <el-form-item label="告知单标题" prop="informTitle" label-width="95px">
            <el-input v-model="queryParams.informTitle" placeholder="请输入告知单标题" clearable size="small" />
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="ml5">
              重置
            </el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8 fl">
          <el-col :span="1.5">
            <el-button type="default" icon="el-icon-plus" size="mini" @click="handleInit"
              v-hasPermi="['combo:inform:save']">新增
            </el-button>
          </el-col>
        </el-row>

        <el-table v-loading="loading" :data="comboInformList" :fit="true">
          <el-table-column label="序号" width="50" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="所属主题" align="center" prop="comboDirectoryName" :show-overflow-tooltip="true" />
          <el-table-column label="告知单标题" align="center" prop="informTitle" :show-overflow-tooltip="true" />
          <el-table-column label="排序号" align="center" prop="sort" :show-overflow-tooltip="true" />
          <el-table-column label="创建时间" align="center" prop="createDate" :show-overflow-tooltip="true" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="400px">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)"
                v-hasPermi="['combo:inform:view']">查看
              </el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" @click="handleInit(scope.row)"
                v-hasPermi="['combo:inform:update']">修改
              </el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDelete(scope.row)"
                v-hasPermi="['combo:inform:delete']">删除
              </el-button>
              <el-button size="mini" type="text" icon="iconfont  zfsoft-xiugai"
                @click="comboInformLabelManageIndex(scope.row)" v-hasPermi="['combo:inform:labelManage']">告知单标签管理
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize" @pagination="getList" />
      </el-col>
    </el-row>

    <!--一件事告知单新增/修改开始-->
    <el-dialog v-dialog-drag :visible.sync="openInit" :close-on-click-modal="false" v-if="openInit" :title="title"
      width="1100px" append-to-body>
      <combo-inform-add :comboInformOid="comboInformOid" :industryType="industryType" @init-close="comboInformAddClose">
      </combo-inform-add>
    </el-dialog>
    <!--一件事告知单新增/修改结束-->

    <!--一件事告知单查看页面开始-->
    <el-dialog v-dialog-drag :visible.sync="openView" v-if="openView" :close-on-click-modal="false" :title="title"
      width="1100px" height="700px" scrollbar append-to-body>
      <combo-inform-info :comboInformOid="comboInformOid"> </combo-inform-info>
      <div slot="footer" style="text-align: center">
        <el-button @click="comboInformInfoClose">关闭</el-button>
      </div>
    </el-dialog>
    <!--一件事告知单查看页面结束-->

    <!--一件事告知单标签管理开始-->
    <el-dialog v-dialog-drag :visible.sync="openComboInformLabelManage" v-if="openComboInformLabelManage"
      :close-on-click-modal="false" :title="title" width="1100px" height="700px" scrollbar append-to-body>
      <combo-inform-label-manage-index ref="comboInformLabelManageIndex" :comboInformOid="comboInformOid"
        :comboDirectoryOid="comboDirectoryOid" @init-close="comboInformLabelManageClose">
      </combo-inform-label-manage-index>
      <div slot="footer" style="text-align: center">
        <el-button type="primary" @click="
            () => {
              $refs.comboInformLabelManageIndex.submitForm()
            }
          ">确 定
        </el-button>
        <el-button @click="comboInformLabelManageClose">关 闭</el-button>
      </div>
    </el-dialog>
    <!--一件事告知单标签管理结束-->
  </div>
</template>

<script>
  import {
    page,
    del
  } from "@/api/onething/sxpz/inform/comboInform";
  import {
    queryAllDirectoryPublishByIndustryType
  } from "@/api/onething/sxpz/comboDirectory";




  import comboInformAdd from "@/views/onething/sxpz/comboInformManage/comboInformAdd.vue";
  import comboInformInfo from "@/views/onething/sxpz/comboInformManage/comboInformInfo.vue";
  import comboInformLabelManageIndex from "@/views/onething/sxpz/comboInformManage/comboInformLabelManageIndex.vue";


  export default {
    components: {
      comboInformAdd,
      comboInformInfo,
      comboInformLabelManageIndex
    },
    name: "IndustryInformManageIndex",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        title: "",
        comboInformList: [],
        // 主题列表
        comboDirectoryList: [],
        // 告知单列表
        combInformList: [],
        // 查询一件事目录名称参数
        comboDirectoryName: "",
        // 查看显示弹出层
        openView: false,
        openInit: false,
        openComboInformLabelManage: false,
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          comboDirectoryName: "",
          comboDirectoryOid: "",
          informTitle: "",
          status: 1,
          industryType: 1
        },
        // 告知单oid
        comboInformOid: "",
        // 主题Oid
        comboDirectoryOid: "",
        //一业一证标识
        industryType: 1,
      };
    },
    computed: {},
    created() {
      this.getList();
      this.getComboDirectoryList();
    },
    methods: {
      /** 查询一件事告知单列表 */
      getList() {
        this.loading = true;
        page(this.queryParams).then(response => {
          this.comboInformList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        });
      },
      /** 查询一件事主题列表 */
      getComboDirectoryList() {
        this.loading = true;
        queryAllDirectoryPublishByIndustryType(this.industryType).then(response => {
          this.comboDirectoryList = response.data;
          this.loading = false;
        });
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 查看按钮操作 */
      handleView(row) {
        this.comboInformOid = row.oid;
        this.openView = true;
        this.title = "查看告知单";
      },
      /** 新增/修改按钮操作 */
      handleInit(row) {
        this.comboInformOid = row.oid;
        this.openInit = true;
        if (row.oid == undefined) {
          this.title = "新增告知单";
        } else {
          this.title = "修改告知单";
        }
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const id = row.id;
        this.$confirm("是否确认删除?", "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          })
          .then(function () {
            return del(id);
          })
          .then(() => {
            this.msgSuccess("删除成功");
            this.getList();
          })
          .catch(function () {});
      },
      /** 告知单标签管理 */
      comboInformLabelManageIndex(row) {
        this.comboInformOid = row.oid;
        this.comboDirectoryOid = row.comboDirectoryOid;
        this.openComboInformLabelManage = true;
        this.title = "告知单标签管理";
      },
      /*关闭告知单新增/编辑弹窗*/
      comboInformAddClose() {
        this.openInit = false;
        this.getList();
      },
      /*关闭告知查看弹窗*/
      comboInformInfoClose() {
        this.openView = false;
      },
      /*关闭标签管理弹窗*/
      comboInformLabelManageClose() {
        this.openComboInformLabelManage = false;
      }
    }
  };

</script>
