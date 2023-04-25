<template>
  <div class="app-container">
    <div style="color: red;margin-bottom: 10px">注意：最新的一条版本记录不能删除</div>
    <el-table v-loading="loading" :data="designList" border>
      <!--<el-table-column type="selection" width="55" align="center"  />-->
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="表单名称" align="center" prop="designName" :show-overflow-tooltip="true" />
      <el-table-column label="版本" align="center" prop="version" :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createDate" :show-overflow-tooltip="true"/>
      <el-table-column label="操作" width="220" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="icon iconfont zfsoft-fangdichanpinggu"
            @click="handleFormView(scope.row)"
          >查看表单</el-button>
          <el-button
            v-if="scope.row.designOid != newDesignOid"
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <el-dialog v-dialog-drag :visible.sync="view.designShow"  v-for="view in formViewOptions" title="查看表单" @close="closeFormView"  width="90%" append-to-body>
      <FormView :key="'view'+view.designOid" :designOid="view.designOid" :authorizeKey="authorizeKey" :disabled="true" :isShowDefaultVal="true"></FormView>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeFormView">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {page,del,getFormDesignByFormMainOid} from "@/api/form/design";
  export default {
    name: "Design",
    props:['formMainOid','authorizeKey'],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 存储对象表格数据
        designList: [],
        //查看表单弹框
        formViewOptions:[],
        //参数
        params:{},
        // 弹出层标题
        title: "",
        // 新增/修改显示弹出层
        openInit: false,
        // 查看显示弹出层
        openView: false,
        // 表单参数
        form: {},
        newDesignOid:'',
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
        },
      };
    },
    created() {
      this.getFormDesignByMainOid();
      this.getList();
    },
    watch:{

    },
    methods: {
      /** 查询存储对象列表 */
      getList() {
        this.loading = true;
        let that = this;
        this.queryParams.formMainOid = this.formMainOid;
        page(this.queryParams).then(response => {
          this.designList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        }).catch(function () {
          that.loading = false;
        });
      },
      // 表单查看
      handleFormView(row) {
        let formView = {'designShow':true,'designOid':row.designOid,'authorizeKey':this.authorizeKey};
        this.formViewOptions.push(formView);
        //window.open("/formView?formMainOid="+row.formMainOid,'_blank')
      },
      closeFormView(){
        this.formViewOptions.pop();
      },
      getFormDesignByMainOid(){
        getFormDesignByFormMainOid(this.formMainOid).then(response => {
            this.newDesignOid = response.data.designOid;
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const oid = row.id;
        this.$confirm('是否确认删除吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return del(oid);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
    }
  };
</script>
<style scoped>
  .app-container{
    padding-top: 0px !important;
  }
</style>
