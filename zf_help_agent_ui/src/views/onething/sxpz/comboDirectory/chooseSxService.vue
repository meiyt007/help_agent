/**
* @Author: wangxl
*/
<template>
  <div class="">
    <!-- <el-row :gutter="20"> -->
      <!-- <el-col :span="24" :xs="24"> -->
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="108px">
          <el-form-item label="实施编码" prop="implementCode">
            <el-input v-model.trim="queryParams.implementCode" placeholder="实施编码" clearable size="small"
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="事项名称" prop="serviceName">
            <el-input v-model.trim="queryParams.serviceName" placeholder="事项名称" clearable size="small"
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置
            </el-button>
            <el-button type="primary" icon="el-icon-refresh" size="mini" @click="batchChoose" class="btn-reset">批量选择
            </el-button>
          </el-form-item>
        </el-form>
        <el-table ref="multipleTable" :data="sxServiceList" tooltip-effect="dark" style="width: 100%"
          @selection-change="handleSelectionChange" border>
          <el-table-column type="selection" align="center" width="45">
          </el-table-column>
          <el-table-column label="序号" width="50" type="index" align="center">
            <template slot-scope="scope">
              <span>{{scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="districtName" label="所属区划" align="center" :show-overflow-tooltip="true">
            <!--<template slot-scope="scope">{{ scope.row.date }}</template>-->
          </el-table-column>
          <el-table-column prop="organName" label="实施机构" align="center" width="150" :show-overflow-tooltip="true">
          </el-table-column>
          <el-table-column prop="serviceName" label="事项名称" align="center" :show-overflow-tooltip="true">
          </el-table-column>
          <el-table-column prop="implementCode" label="实施编码" align="center" :show-overflow-tooltip="true">
          </el-table-column>
          <el-table-column prop="serviceStatusName" label="状态" align="center" show-overflow-tooltip width="80">
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)"
                v-hasPermi="['combo:directory:view']">查看</el-button>
              <el-button size="mini" type="text" v-if="scope.row.ifCombo==0" icon="iconfont zfsoft-penzhi"
                @click="chooseSx(scope.row)" v-hasPermi="['combo:directory:service']">选择</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
          @pagination="getSxServiceList" />
      <!-- </el-col> -->
    <!-- </el-row> -->
    <!--实施清单详细-->
    <el-dialog v-dialog-drag :visible.sync="sxServiceInfo.show" v-for="sxServiceInfo in serviceDialogOptions" :key='sxServiceInfo.sxServiceOid'
      title="查看实施清单详情" width="1100px" height='700px' scrollbar append-to-body>
   
        <view-sx-service-info :sxServiceOid="sxServiceInfo.sxServiceOid" @view-service="viewServiceClose">
        </view-sx-service-info>
  
    </el-dialog>

  </div>
</template>

<script>
  import {
    saveComboService,
    querySxServicePage
  } from "@/api/onething/sxpz/chooseSxService";
  import viewSxServiceInfo from "@/views/zc/businessSupervise/viewSxServiceInfo";
  export default {
    components: {
      viewSxServiceInfo
    },
    name: "SxServiceList",
    props: ["comboDireOid"],
    inject: ['reload'],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        attatotal: 0,
        // 应用表格数据
        sxServiceList: [],
        //serviceDialogOptions:[],
        //查询目录名称参数
        sxServiceName: '',
        comboDirectoryOid: "",
        //事项oid
        sxServiceOid: "",
        // 弹出层标题
        title: "",
        multipleSelection: [],
        serviceDialogOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
        },
        defaultProps: {
          children: "children",
          label: "label"
        },
        // 表单参数
        form: {},
        rules: {
          //表单验证
        }
      };
    },
    watch: {

    },

    created() {
      this.getSxServiceList();
      this.mounted();
    },
    methods: {
      //获取父页面的值
      mounted() {
        this.comboDirectoryOid = this.comboDireOid;
      },

      /** 查询目录列表 */
      getSxServiceList() {
        this.loading = true;
        querySxServicePage(this.queryParams, this.comboDireOid).then(response => {
          this.sxServiceList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        });
      },

      // 取消按钮
      cancel() {
        this.openInit = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.resetForm("form")
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getSxServiceList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /*toggleSelection(rows) {
        if (rows) {
          rows.forEach(row => {
            this.$refs.multipleTable.toggleRowSelection(row.serviceOid);
          });
        } else {
          this.$refs.multipleTable.clearSelection();
        }
      },*/
      //保存选中的结果
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      /**  批量选择**/
      batchChoose() {
        let serviceOids = "";
        //获取选中结果
        this.multipleSelection.forEach(ser => {
          serviceOids += ser.serviceOid + ",";
        });
        if (serviceOids == "") {
          this.msgWarning("请选择事项！");
          return false;
        }
        if (serviceOids) {
          serviceOids = serviceOids.substring(0, serviceOids.length - 1);
        }

        saveComboService(serviceOids, this.comboDirectoryOid).then(response => {
          if (response.code === 200) {
            this.msgSuccess("暂存成功");
            this.$emit('dialog-close');

          }
        });
      },
      /**  选择事项 **/
      chooseSx(row) {
        const serviceOid = row.serviceOid;
        saveComboService(serviceOid, this.comboDirectoryOid).then(response => {
          if (response.code === 200) {
            this.msgSuccess("暂存成功");
            this.$emit('dialog-close');
          }
        });
      },

      /** 查看按钮操作 */
      handleView(row) {
        let item = {
          show: true,
          sxServiceOid: row.serviceOid
        };
        this.serviceDialogOptions.push(item)
      },
      viewServiceClose() {
        this.serviceDialogOptions.pop();
      }
    }
  };
</script>
<style lang="scss" scoped>
  // .el-table__body tr td:nth-last-child(1) {
  //   border-right: 2px solid #dfe6ec;
  // }
</style>
