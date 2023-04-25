/**
* @Author: dingsn
* @Date: 2022-11-09
* @Description: 组长待审核状态下的组员加分时长统计总数
*/
<template>
  <div class="app-container">
    <!--区划数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="查询时间">
        <el-date-picker
          v-model.trim="queryParams.beginTime"
          type="date"
          value-format="yyyy-MM-dd HH:mm:ss"
          :picker-options="pickerOptionsStart"
          placeholder="请选择开始日期"
        >
        </el-date-picker
        >-
        <el-date-picker
          v-model.trim="queryParams.endTime"
          type="date"
          value-format="yyyy-MM-dd HH:mm:ss"
          :picker-options="pickerOptionsEnd"
          placeholder="请选择结束日期"
        >
        </el-date-picker>
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
        >重置</el-button>
        <el-button type="danger" size="mini" v-hasPermi="['im:groupLeaderNoReview:akeyReview']" @click="batchAkeyReviewByGroupLeaderIds()">批量一键推送</el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="loading"
      :data="haPerformanceAppraisalStatisticVoList"
      border
      :fit="true"
      height="calc(100% - 160px)"
      style="width: 100%" @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"/>
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="组长姓名"
        align="center"
        prop="groupLeaderName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="组长手机号码"
        align="center"
        prop="groupLeaderPhone"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="所属分组"
        align="center"
        prop="groupName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="几条待审"
        align="center"
        prop="noAuditStatusSum"
        :show-overflow-tooltip="true"
      />

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="akeyReviewByGroupLeaderId(scope.row)" v-hasPermi="['im:groupLeaderNoReview:akeyReview']" >一键推送</el-button>
        </template>
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
  import { queryGroupLeaderPageListByAuditStatus, batchAkeyReviewByGroupLeaderIds,  akeyReviewByGroupLeaderId} from "@/api/ha/workUserStatisticManager/performanceAppraisalStatistic";
  export default {
    name: "performanceAppraisalStatisticList",
    // components: { Treeselect },
    data () {
      return {
        // 遮罩层
        loading: false,
        // 总条数
        total: 0,
        // 应用表格数据
        haPerformanceAppraisalStatisticVoList: [],
        multipleSelection: [], //批量选择中选择的记录列表
        // 弹出层标题
        title: "",
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          beginTime: null,
          endTime: null,
        },
        pickerOptionsStart: {
          disabledDate: time => {
            const endTimeVal = new Date(this.queryParams.endTime).getTime()
            if (endTimeVal) {
              return time.getTime() > endTimeVal - 0;
            }
          }
        },
        pickerOptionsEnd: {
          disabledDate: time => {
            const beginTimeVal = new Date((new Date(this.queryParams.beginTime) - 24 * 60 * 60 * 1000)).getTime()
            if (beginTimeVal) {
              return time.getTime() < beginTimeVal - 0;
            }
          }
        },
      };
    },
    created () {
      this.getList();
    },
    watch: {

    },
    methods: {
      /** 查询绩效统计列表 */
      getList () {
        this.loading = true;
        queryGroupLeaderPageListByAuditStatus(this.queryParams).then(response => {
          this.haPerformanceAppraisalStatisticVoList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        });
      },
      //一键推送某个组长去提醒审核组员加分时长记录
      akeyReviewByGroupLeaderId (row) {
        this.$confirm('此操作将向组长推送提醒审核组员加分时长, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {//确定执行then方法
          //调用接口方法
          akeyReviewByGroupLeaderId(row.groupLeaderId)
            .then(response => {
              //提示信息
              this.$message({
                type: 'success',
                message: '一键推送成功!'
              })
              //刷新页面
              this.getList()
            })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消一键推送'
          });
        });
      },
      // 表单重置
      reset () {
        Object.assign(this.form, this.$options.data().form)
        this.resetForm("form");
      },
      //获取选择复选框的id值
      handleSelectionChange(selection){
        this.multipleSelection = selection;
      },
      //批量一键推送按钮
      batchAkeyReviewByGroupLeaderIds(){
        if (this.multipleSelection.length <= 0) {
          //提示信息
          this.$message({
            type: 'info',
            message: '至少选择一个做操作'
          })
        } else {
          this.$confirm('此操作将批量向组长推送提醒审核组员加分时长, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {//确定执行then方法
            var idList =[];
            //遍历数组得到每个id值，设置到idlist中
            for(var i=0;i<this.multipleSelection.length;i++){
              var obj = this.multipleSelection[i];
              var id = obj.groupLeaderId;
              idList.push(id);
            }
            //调用接口方法
            batchAkeyReviewByGroupLeaderIds(idList)
              .then(response => {
                //提示信息
                this.$message({
                  type: 'success',
                  message: '批量一键推送成功!'
                })
                //刷新页面
                this.getList();
              })
          })
        }
      },
      /** 搜索按钮操作 */
      handleQuery () {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery () {
        this.resetForm("queryForm");
        this.queryParams = {
          pageNum: 1,
          pageSize: 10,
          beginTime: "",
          endTime: "",
        };
        this.handleQuery();
      },


    },
  };
</script>
<style lang="scss" scoped>
  table {
    border-collapse: collapse;
  }
  /*.treeselect {
    width: 200px;
  }*/
</style>
