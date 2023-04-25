/** * @Author: xldong */
<template>
  <div>
    <!--事项数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="108px"
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
      <el-form-item label="实施编码" prop="implementCode">
        <el-input
          v-model.trim="queryParams.implementCode"
          placeholder="请输入实施编码"
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
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['service:recommend:save']"
          >保存所选事项</el-button
        >
      </el-col>
    </el-row>
    <el-table
      ref="multipleTable"
      tooltip-effect="dark"
      @selection-change="handleSelectionChange"
      border
      v-loading="loading"
      :data="optionRelList"
      border
      :fit="true"
    >
      <el-table-column type="selection" width="50" align="center" :selectable="checkSelectMember" />
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="事项名称"
        align="center"
        prop="serviceName"
        :show-overflow-tooltip="true"
      >
      </el-table-column>
      <el-table-column
        label="实施编码"
        align="center"
        prop="implementCode"
        :show-overflow-tooltip="true"
      >
      </el-table-column>
    </el-table>
    <pagination
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import {
  saveOrUpdateServiceOptionRel,
 page, saveOrUpdate,pageRecommendList
} from "@/api/zc/qdgl/sxService.js";
import {getSxServiceOne} from "@/api/onething/clzs/materialCheckKeyManage/comboMaterialCheckKeyManage";
export default {
  name: "RelationList",
  props: ["serviceOid"],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      form: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        serviceName: "",
        implementCode: "",
        serviceStatus: "3",
        serviceOid: this.serviceOid
      },
      optionRelList: [],
      checkedDataList:[],
      openInit: false,
      titleAndValList: [], //标题选项值
      allSelectRel: [],
      selectTitles: [],
      selectVals: [],
      selectCheckVals: [],
      relOid: "",
      multipleSelection: [],
      relViewInfo: {},
      viewRel: false
    };
  },
  created() {
    this.getList();
  },
  methods: {
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
    /** 查询列表 */
    getList() {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.optionRelList = response.data.data;
        this.total = response.data.total;
        pageRecommendList(this.queryParams).then(response => {
          this.checkedDataList = response.data;
        });
        this.loading = false;
      });
    },
    //保存选中的结果
    handleSelectionChange(val) {
      this.multipleSelection = val.map(ite => ite.serviceOid);
      //this.multipleSelection = val;
    },
    /** 添加按钮操作 */
    handleAdd(row) {
      let _that = this;
      if (!this.multipleSelection || this.multipleSelection.length == 0) {
        this.$message.error("请选择要添加的数据!");
        return false;
      }
      let data = this.multipleSelection.join(",");
      this.$confirm("是否确认添加关联事项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          //查询
          _that.loading = true;
          getSxServiceOne(_that.serviceOid).then(response => {
              _that.selectVals = response.data.sxService;
              var recommendserviceoids = _that.selectVals.recommendedServiceOids;
              let serviceValsStr=[];
              if(recommendserviceoids == null || recommendserviceoids == ""){
                serviceValsStr.push(_that.multipleSelection);
              }else{
                serviceValsStr= recommendserviceoids.split(",");
                serviceValsStr.push(_that.multipleSelection);
              }
              let returnRecommendServiceOids = serviceValsStr.join(",");
              let returnOids = returnRecommendServiceOids.split(",");
              let s1 = new Set(returnOids);
              returnRecommendServiceOids =  Array.from(s1);
              _that.selectVals.recommendedServiceOids=returnRecommendServiceOids.join(",");
              //saveOrUpdate
              saveOrUpdate(_that.selectVals).then(response => {
                let res = response;
                if (res.code == 200) {
                  let serviceOid = res.data.serviceOid;
                  _that.$message.success("保存成功！");
                  _that.getList();
                  // cancelDig();
                }
              }
              );
            });
        })
        .catch(function() {});
    },
    checkSelectMember(row, index) {
      this.loading = true;
      let isChecked = true;
          for(let j = 0; j <this.checkedDataList.length; j++ ){
            if (row.serviceOid === this.checkedDataList[j].serviceOid) {
                isChecked = false
              }
      }
      this.loading = false;
      return isChecked
    }
  }
};
</script>

<style lang="scss" scoped>
.el-radio {
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  margin: 10px;
}
.el-checkbox {
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  margin: 10px;
}
</style>
