/**
* @Author: wangxl
*/
<template>
  <!--目录信息 开始-->
  <el-tabs>

    <!--目录信息 结束-->
    <div class="el-table__header-wrapper dialog-table">
      <h3><i class="el-icon-document"></i>事项信息</h3>
      <el-row :gutter="20">
        <el-col :span="24" :xs="24">
          <el-row :gutter="10" class="mb8 fl">
            <el-col :span="1.5">
              <!--<el-button
                type="default"
                icon="el-icon-plus"
                size="mini"
                @click="handleInit"
                v-hasPermi="['combo:directory:init']"
              >新增</el-button>-->
              <el-button
                type="default"
                icon="el-icon-plus"
                size="mini"
                @click="setStatusAll"
                v-hasPermi="['combo:directory:init']"
              >确认配置</el-button>
             <!-- <el-button
                type="default"
                icon="el-icon-plus"
                size="mini"
                @click="batchDelete"
                v-hasPermi="['combo:directory:init']"
              >批量启/禁用</el-button>-->
              <!--<el-button
                type="default"
                icon="el-icon-plus"
                size="mini"
                @click="batchStatus"
                v-hasPermi="['combo:directory:init']"
              >批量配置</el-button>-->
            </el-col>
          </el-row>
          <!--@selection-change="handleSelectionChange"tooltip-effect="dark"-->
          <el-table
            ref="multipleTable"
            :data="comboServiceList"
            tooltip-effect="dark"
            style="width: 100%"
            @select='onTableSelect'
            @selection-change="handleSelectionChange">
            <el-table-column
              type="selection"
              width="60" :show-overflow-tooltip="true">
            </el-table-column>

            <el-table-column
              prop="organName"
              label="实施机构"
              width="300" :show-overflow-tooltip="true">
            </el-table-column>
            <el-table-column
              prop="serviceName"
              label="事项名称"
              width="350" :show-overflow-tooltip="true">
            </el-table-column>
            <el-table-column
              prop="implementCode"
              label="实施编码"
              show-overflow-tooltip width="300">
            </el-table-column>
            <el-table-column
              prop="status"
              label="状态"
              :formatter="getStatus"
              width="130" :show-overflow-tooltip="true">
            </el-table-column>
            <!--<el-table-column label="操作" align="center" class-name="small-padding fixed-width" >
              <template slot-scope="scope">
                &lt;!&ndash;<el-button
                  size="mini"
                  type="text"
                  icon="iconfont zfsoft-chakan"
                  @click="handleView(scope.row)"
                  v-hasPermi="['combo:directory:view']"
                >查看</el-button>&ndash;&gt;
                <el-button
                  size="mini"
                  type="text"
                  icon="iconfont zfsoft-penzhi"
                  @click="deleSx(scope.row)"
                  v-hasPermi="['combo:directory:service']"
                >禁用</el-button>
               &lt;!&ndash; <el-button
                  size="mini"
                  v-if="scope.row.status == 0"
                  type="text"
                  icon="iconfont zfsoft-penzhi"
                  @click="statusSx(scope.row,1)"
                  v-hasPermi="['combo:directory:service']"
                >配置</el-button>
                <el-button
                  size="mini"
                  v-if="scope.row.status == 1"
                  type="text"
                  icon="iconfont zfsoft-penzhi"
                  @click="statusSx(scope.row,0)"
                  v-hasPermi="['combo:directory:service']"
                >取消配置</el-button>&ndash;&gt;
              </template>
            </el-table-column>-->
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
      <!--新增关联事项-->
      <!--<el-dialog v-dialog-drag :visible.sync="chooseSxService" title="事项关联" width="80%" append-to-body>
        <el-scrollbar style="height: 500px;">
          <choose-sx-service :comboDireOid="comboDirectoryOid",valOid="valOid" @dialog-close="closeChooseService"></choose-sx-service>
        </el-scrollbar>
      </el-dialog>-->
    </div>
  </el-tabs>
</template>

<script>
  import { delSxservice , page,configAllService,pubMaterialNumByServiceOid} from "@/api/onething/sxpz/comboService";
  import { getOne } from "@/api/onething/sxpz/comboDirectory";
  import chooseSxService from "@/views/onething/sxpz/comboDirectory/chooseSxService";

  export default {
    components: {chooseSxService},
    name: "ComboService",
    //定义获取父类传过来值的格式
    props:["comboDireOid","valOid"],

    provide(){
      return {
        reload:this.reload
      }
    },
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        attatotal:0,
        // 应用表格数据
        comboServiceList: [],
        // 弹出层标题
        title: "",
        comboDirectoryOid:"",
        valOida:"",
        //关联事项框
        chooseSxService:false,
        // 查询参数
        queryParams: {
          comboDirectoryOid:this.comboDireOid,
          valOida:this.valOid,
          pageNum: 1,
          pageSize: 10,
        },
        defaultProps: {
          children: "children",
          label: "label"
        },
        // 表单参数
        form: {
        },
        rules: {
        },
        multipleSelection: []
      };
    },
    watch: {
    },
    computed: {
      reversedIfCharge: function () {
        if (this.form.ifCharge) {
          if(this.form.ifCharge==0){
            return '否';
          }else if(this.form.ifCharge==1){
            return '是';
          }
        }
        return ''
      }
    },
    created() {
      this.getList();
      this.mounted();
    },

    methods: {
      //获取父页面的值
      mounted() {
        this.comboDirectoryOid = this.comboDireOid;
        this.valOida = this.valOid;
        this.getDirectory();
      },
      reload () {
        this.isRouterAlive = false
        this.$nextTick(() => (this.isRouterAlive = true))
      },
      /** 查询目录列表 */
      getList() {
        this.loading = true;
        console.log(this.queryParams);
        page(this.queryParams).then(response => {
          this.comboServiceList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        });
      },
      //获取目录信息
      getDirectory(){
        getOne(this.comboDirectoryOid).then(response => {
          this.form = response.data;
        });
      },
      // 取消按钮
      cancel() {
        this.chooseSxService = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {}
        this.resetForm("form")
      },
      /** 查看按钮操作 */
      handleView(row) {
        /*  this.reset();
          const oid = row.comboDirectoryOid;
          getComboDirectory(oid).then(response => {
            this.form = response.data;
            this.openView = true;
            this.title = "查看目录信息";
          });*/
      },
      /** 新增和修改按钮操作 */
      handleInit() {
        this.comboDireOid = this.comboDirectoryOid;
        this.chooseSxService=true;
      },
   /*  //保存选中结果
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },*/
  /*    /!**  批量选择**!/
      batchDelete(){
        let ids = "";
        //获取选中结果
        this.multipleSelection.forEach(ser =>{
            ids+=ser.id+",";
          }
        );
        this.$confirm('是否确认批量删除吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delSxservice(ids);
        }).then(() => {
          this.msgSuccess("删除成功");
          this.getList();
        }).catch(function() {});
      },*/

      //删除事项
      deleSx(row){
        const oid = row.id;
        pubMaterialNumByServiceOid(row.serviceOid).then(response => {
          if(response.data>0){
            this.msgWarning("当前事项下有材料被整合，不能被禁用！");
            return false;
          }else{
            this.$confirm('是否确认禁用?', "警告", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning"
            }).then(function() {
              return delSxservice(oid);
            }).then(() => {
              this.msgSuccess("禁用成功");
              this.getList();
            }).catch(function() {});
          }
        });

      },
      closeChooseService(){
        this.chooseSxService = false;
        this.getList();
      },
      /*  批量配置*
      batchStatus(){
        let ids = "";
        //获取选中结果
        this.multipleSelection.forEach(ser =>{
            ids+=ser.id+",";
          }
        );
        this.$confirm('是否确认批量配置吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return configService(ids);
        }).then(() => {
          this.msgSuccess("配置成功");
          this.getList();
        }).catch(function() {});
      },*/

      getStatus(val){
        if(val.status == 0){
          return '暂存';
        }else if(val.status == 1){
          return '已配置'
        }else{
          return '';
        }
      },
      onTableSelect(rows, row) {
        let selected = rows.length && rows.indexOf(row) !== -1;
        if(selected){
          this.multipleSelection.push(row.materialOid);
        }else{
          this.multipleSelection.splice(this.multipleSelection.indexOf(row.materialOid),1);
        }
        //alert(selected);
        // console.log(selected)  // true就是选中，0或者false是取消选中
      },


      //确认配置当前目录下的所有事项
      setStatusAll(){
        const oid = this.comboDireOid;
        this.$confirm('是否确认配置?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return configAllService(oid);
        }).then(() => {
            this.msgSuccess("配置成功");
          this.getList();
        }).catch(function() {});

      }
     /* //配置事项
      ,statusSx(row,type){
        const oid = row.id;
        this.$confirm('是否确认配置吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return configService(oid,type);
        }).then(() => {
          if(type==1){
            this.msgSuccess("配置成功");
          }else{
            this.msgSuccess("取消配置成功");
          }
          this.getList();
        }).catch(function() {});
      }*/

    }
  };
</script>
