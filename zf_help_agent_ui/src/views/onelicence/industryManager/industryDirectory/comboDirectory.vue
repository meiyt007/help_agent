/**
* 业态管理选择业态目录
* @Author: wangwg
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--业态目录列表数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="78px">
          <el-form-item label="目录名称" prop="comboDirectoryName">
            <el-input
              v-model.trim="queryParams.comboDirectoryName"
              placeholder="请输入目录名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置
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
              v-hasPermi="['combo:directory:init']"
            >添加
            </el-button>
          </el-col>
        </el-row>
        <el-table ref="selectedTable"
                  v-loading="loading"
                  :data="comboDirectoryList" border
                  :fit="true"
                  :row-key="(row) => { return row.comboDirectoryOid }"
                  @selection-change="onTableSelect">
          <el-table-column width="55" align="center" type="selection" :reserve-selection="true"/>
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column label="业态分类名称" align="center" :show-overflow-tooltip="true" prop="themeName"/>
          <el-table-column label="目录名称" align="center" :show-overflow-tooltip="true" prop="comboDirectoryName"/>
          <el-table-column label="所属区划" :show-overflow-tooltip="true" align="center" prop="districtName"/>
          <el-table-column label="主办部门" :show-overflow-tooltip="true" align="center" prop="mainOrganName"/>
          <el-table-column label="状态" width="80" align="center" :formatter="getPublishName" prop="status"/>
        </el-table>

        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {page, saveIndustry,queryChekedList} from "@/api/onelicence/industryManager/industryManager";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  components: {Treeselect},
  name: "ComboDirectory",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      directoryOidList: [],
      comboDirectoryList: [],
      multipleSelectionList: [],
      comboDirectoryCheckedList: [],
      selectedOidList: [],
      dataList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        comboDirectoryName: '',
        status: '1',
      },
      statusOptions: {'0': '未发布', '1': '已发布'},
    };
  },
  created() {
    this.initChekedList();
    this.getList();
  },
  methods: {
    /** 查询业态目录列表 */
    getList() {
      let _that = this;
      _that.loading = true;
      _that.directoryOidList=[];
      page(_that.queryParams).then(response => {
        _that.chekedDirectory(response.data.data);
        _that.comboDirectoryList = response.data.data;
        _that.comboDirectoryList.forEach(directory => {
          _that.directoryOidList.push(directory.comboDirectoryOid);
        })
        _that.total = response.data.total;
        _that.loading = false;
      });
    },
    /** 初始化被选中的目录信息 */
   initChekedList() {
      let _that = this;
      let industryType = 1;
      queryChekedList(industryType).then(response => {
        _that.comboDirectoryCheckedList = response.data;
      });
    },
    chekedDirectory(list){
      let _that = this;
        if (list != undefined) {
          list.forEach(data => {
            _that.comboDirectoryCheckedList.forEach(item => {
              if (data.comboDirectoryOid === item.comboDirectoryOid) {
                _that.$refs.selectedTable.toggleRowSelection(item, true);
              }
            })
          })
        } else {
          _that.$refs.selectedTable.clearSelection()
        }
    },
    getPublishName(val) {
      if (val.status == 0 || val.status == 4) {
        return '未发布';
      } else if (val.status == 1) {
        return '已发布';
      } else {
        return '';
      }
    },
    /** 表单重置 */
    reset() {
      this.reset();
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
    /** 新增 */
    handleInit() {
      this.dataList = [];
      if (this.multipleSelectionList.length == 0) {
        this.msgError("请至少选择一个目录！")
        return false;
      } else {
        if (this.directoryOidList.length > 0) {
          if (this.selectedOidList.length > 0) {
              this.directoryOidList.forEach(oid => {
                if(this.selectedOidList.indexOf(oid) !=-1){
                   this.dataList.push("in_"+oid);
                }else{
                    this.dataList.push("un_"+oid);
                }
              })
          }
        }
        let directoryOids = "";
        //筛选需要保存的id
        this.dataList.forEach(selection => {
          directoryOids += selection + ";"
        });
        saveIndustry(directoryOids).then(response => {
          if (response.data.success == true) {
            this.msgSuccess("保存成功！")
            this.$emit('case-close');
          } else {
            this.msgError(response.data.message);
            return false;
          }
        });
      }
    },
    /** 多选 */
    onTableSelect(val) {
      this.selectedOidList=[];
      this.multipleSelectionList = val;
      this.multipleSelectionList.forEach(directory => {
        this.selectedOidList.push(directory.comboDirectoryOid);
      })
    },
  }
};
</script>
