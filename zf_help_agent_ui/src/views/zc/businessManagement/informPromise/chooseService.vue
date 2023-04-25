/**
* @Author: wangxl
*/
<template>
  <div >
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="70px"
    >
      <el-form-item label="实施编码" prop="implementCode">
        <el-input
          v-model.trim="queryParams.implementCode"
          placeholder="实施编码"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="事项名称" prop="serviceName">
        <el-input
          v-model.trim="queryParams.serviceName"
          placeholder="事项名称"
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
          >重置</el-button
        >
        <el-button
          type="primary"
          icon="el-icon-refresh"
          size="mini"
          @click="batchChoose"
          class="btn-reset"
          >选择</el-button
        >
      </el-form-item>
    </el-form>
    <el-table
      ref="multipleTable"
      :data="sxServiceList"
      tooltip-effect="dark"
      @selection-change="handleSelectionChange"
      border

    >
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column
        prop="organName"
        align="center"
        label="实施机构"
        show-overflow-tooltip
      />
      <el-table-column
        prop="serviceName"
        align="center"
        width="250"
        label="事项名称"
        show-overflow-tooltip
      />
      <el-table-column
        prop="implementCode"
        align="center"
        label="实施编码"
        show-overflow-tooltip
      />
      <el-table-column
        prop="serviceTypeName"
        align="center"
        label="事项类型"
        show-overflow-tooltip
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
          <el-button
            size="mini"
            type="text"
            v-if="scope.row.ifCombo == 0"
            icon="iconfont zfsoft-penzhi"
            @click="chooseSx(scope.row)"
            v-hasPermi="['combo:directory:service']"
            >选择</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <pagination
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getSxServiceList"
    />
    <!-- <div slot="footer" class="zf-text-center">
      <el-button @click="cancelDig">关闭</el-button>
    </div> -->
    <!--实施清单详细-->
    <el-dialog
      v-dialog-drag
      :visible.sync="sxView"
      v-if="sxView"
      title="查看实施清单详情"
      width="1100px"
      height="700px"
      append-to-body
      scrollbar
    >
      <view-sx-service-info
        :sxServiceOid="sxServiceOid"
        @view-service="viewServiceClose"
      ></view-sx-service-info>
      <div slot="footer" class="zf-text-center">
        <el-button @click="sxView = false">
          关闭
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  pageSxService,
  saveInformPromise,
  allInformServiceOids
} from "@/api/zc/businessManagement/informPromise.js";
import viewSxServiceInfo from "@/views/zc/businessSupervise/viewSxServiceInfo";
export default {
  components: {
    viewSxServiceInfo
  },
  name: "SxServiceList",
  data () {
    return {
      // 遮罩层
      loading: true,
      sxView: false,
      // 总条数
      total: 0,
      // 应用表格数据
      sxServiceList: [],
      //事项oid
      sxServiceOid: "",
      // 弹出层标题
      title: "",
      multipleSelection: [],
      inforList: [],
      selectServiceOids: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      // 表单参数
      form: {},
    };
  },
  watch: {

  },

  created () {
    this.getAllInformServiceOids();
    setTimeout(() => {
      this.getSxServiceList();
    }, 10);
  },
  methods: {
    getSxServiceList () {
      this.loading = true;
      pageSxService(this.queryParams).then(response => {
        this.sxServiceList = response.data.data;
        this.total = response.data.total;
        this.reselectFun(this.sxServiceList, this.selectServiceOids);
        this.loading = false;
      });
    },
    getAllInformServiceOids () {
      allInformServiceOids(this.queryParams).then(response => {
        let str = response.data;
        this.selectServiceOids = str.split(",");
      });
    },

    // 取消按钮
    cancel () {
      this.openInit = false;
      this.reset();
    },
    // 表单重置
    reset () {
      this.resetForm("form")
    },
    cancelDig () {
      this.$emit("dialog-close");
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.getSxServiceList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.handleQuery();
    },

    //保存选中的结果
    handleSelectionChange (val) {
      this.multipleSelection = val;
    },
    /**  批量选择**/
    batchChoose () {
      let _that = this;
      if (this.multipleSelection.length == 0) {
        this.msgWarning("请选择事项！");
        return false;
      } else {
        let temServiceOids=[];
        this.multipleSelection.forEach((item, i) => {
          let sxService = {};
          sxService.serviceOid = item.serviceOid;
          sxService.serviceName = item.serviceName;
          sxService.implementCode = item.implementCode;
          sxService.districtOid = item.districtOid;
          sxService.districtName = item.districtName;
          sxService.organOid = item.organOid;
          sxService.organName = item.organName;
          sxService.serviceTypeOid = item.serviceTypeOid;
          sxService.serviceTypeName = item.serviceTypeName;
          _that.inforList.push(sxService);
          temServiceOids.push(item.serviceOid);
        })
        let newArr=[];
        if(this.selectServiceOids){
          if(temServiceOids){
            //过滤需要删除的事项信息
            this.selectServiceOids.forEach(it=>{
              if(temServiceOids.indexOf(it)<0){
                newArr.push(it);
              }
            })
          }else{
            newArr=this.selectServiceOids;
          }
        }
        _that.form.delServiceOids=newArr;
        _that.form.inforList = _that.inforList;
        saveInformPromise(_that.form).then(response => {
          if (response.code === 200) {
            this.msgSuccess("保存成功");
            this.$emit('dialog-close');
          }
        });
      }
    },

    /** 查看按钮操作 */
    handleView (row) {
      this.sxServiceOid = row.serviceOid;
      this.sxView = true;
    },
    viewServiceClose () {
      this.sxView = false;
    },
    // 复选
    reselectFun (dataArr, IDArr) {
      if (dataArr && IDArr) {
        this.$nextTick(function () {
          dataArr.forEach(row => {
            if (IDArr.includes(row.serviceOid)) {
              this.$refs.multipleTable.toggleRowSelection(row, true);
            }
          });
        });
      }
    },
  }
};
</script>
<style lang="scss" scoped>
</style>
