/**
* @Author: wangxl
*/
<template>
  <!--目录信息 开始-->
  <div>
    <el-tabs>
      <div>
        <div class="zf-zc-table--title">目录信息</div>

          <table cellspacing="0" cellpadding="0" border="0" class='zf-zc-table'>
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>目录名称：</b></td>
              <td colspan="3">{{form.comboDirectoryName}}</td>
            </tr>
            <tr>
              <td><b>所属分类：</b></td>
              <td>{{form.themeName}}</td>
              <td><b>所属区划：</b></td>
              <td>{{form.districtName}}</td>
            </tr>
            <tr>
              <td><b>服务对象：</b></td>
              <td>{{form.comboServiceObjectName}}</td>
              <td><b>主办部门：</b></td>
              <td>{{form.mainOrganName}}</td>
            </tr>
            <tr>
              <td><b>协办部门：</b></td>
              <td colspan="3">{{form.assistOrganName}}</td>
            </tr>
            <tr>
              <td><b>线下跑动次数：</b></td>
              <td>{{form.countToScence}}</td>
              <td><b>是否收费：</b></td>
              <td>
                <span v-if="form.ifCharge == 0">否</span>
                <span v-if="form.ifCharge == 1">是</span>
              </td>
            </tr>
            <tr>
              <td><b>承诺时限(工作日)：</b></td>
              <td>{{form.promiseLimit}}</td>
              <td><b>法定时限(工作日)：</b></td>
              <td>{{form.legalLimit}}</td>
            </tr>
          </table>

      </div>
      <!--目录信息 结束-->
      <div>
        <div class="zf-zc-table--title">事项信息</div>
        <el-row :gutter="10" class="mb8 fl">
          <el-col :span="1.5">
            <el-button type="default" icon="el-icon-plus" size="mini" @click="handleInit"
              v-hasPermi="['combo:directory:init']">新增</el-button>
            <el-button type="default" icon="el-icon-plus" size="mini" @click="setStatusAll"
              v-hasPermi="['combo:directory:init']">确认配置</el-button>
          </el-col>
        </el-row>
        <el-table v-loading="loading" ref="multipleTable" :data="comboServiceList" border>
          <el-table-column label="序号" width="80" type="index" align="center">
            <template slot-scope="scope">
              <span>{{scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="organName" label="实施机构" align="center" :show-overflow-tooltip="true">
          </el-table-column>
          <el-table-column prop="serviceName" label="事项名称" align="center" :show-overflow-tooltip="true">
          </el-table-column>
          <el-table-column prop="implementCode" label="实施编码" align="center" :show-overflow-tooltip="true">
          </el-table-column>
          <el-table-column prop="status" label="状态" align="center" :formatter="getStatus" :show-overflow-tooltip="true">
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-penzhi" @click="deleSx(scope.row)"
                v-if="scope.row.status == 1" v-hasPermi="['combo:directory:service']">取消</el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-penzhi" @click="delComboService(scope.row)"
                v-if="scope.row.status == 0" v-hasPermi="['combo:directory:service']">删除</el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-penzhi"
                @click="selectServiceSpecial(scope.row,1)" v-if="scope.row.isSpecial == 0 || scope.row.isSpecial==null"
                v-hasPermi="['combo:directory:service']">选择特别程序</el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-penzhi"
                @click="selectServiceSpecial(scope.row,0)" v-if="scope.row.isSpecial == 1"
                v-hasPermi="['combo:directory:service']">取消特别程序</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
          @pagination="getList" />
      </div>
      <!--新增关联事项-->
      <el-dialog v-dialog-drag :visible.sync="chooseSxService" v-if="ifReload" title="事项关联" width="1100px" height='700px' scrollbar append-to-body>

          <choose-sx-service :comboDireOid="comboDirectoryOid" @dialog-close="closeChooseService"></choose-sx-service>
          <div slot="footer" class="zf-text-center">
        <el-button @click="closeChooseService">关 闭</el-button>
      </div>
      </el-dialog>
    </el-tabs>
  </div>
</template>

<script>
  import {
    delSxservice,
    delComboService,
    selectSpecial,
    page,
    configAllService,
    pubMaterialNumByServiceOid
  } from "@/api/onething/sxpz/comboService";
  import {
    getOne
  } from "@/api/onething/sxpz/comboDirectory";
  import chooseSxService from "@/views/onething/sxpz/comboDirectory/chooseSxService";

  export default {
    components: {
      chooseSxService
    },
    name: "ComboService",
    //定义获取父类传过来值的格式
    props: ["comboDireOid"],
    provide() {
      return {
        reload: this.reload
      }
    },
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        attatotal: 0,
        // 应用表格数据
        comboServiceList: [],
        ifReload: true,
        // 弹出层标题
        title: "",
        comboDirectoryOid: "",
        //关联事项框
        chooseSxService: false,
        // 查询参数
        queryParams: {
          comboDirectoryOid: this.comboDireOid,
          pageNum: 1,
          pageSize: 10,
        },
        defaultProps: {
          children: "children",
          label: "label"
        },
        // 表单参数
        form: {},
        rules: {},
        multipleSelection: []
      };
    },
    watch: {},
    computed: {

    },
    created() {
      this.getList();
      this.mounted();
    },

    methods: {
      //获取父页面的值
      mounted() {
        this.comboDirectoryOid = this.comboDireOid;
        this.getDirectory();
      },
      reload() {
        this.isRouterAlive = false
        this.$nextTick(() => (this.isRouterAlive = true))
      },
      /** 查询目录列表 */
      getList() {
        this.loading = true;
        page(this.queryParams).then(response => {
          this.comboServiceList = response.data.data;
          console.log(response.data.data)
          this.total = response.data.total;
          this.loading = false;
        });
      },
      //获取目录信息
      getDirectory() {
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
        this.chooseSxService = true;
        this.ifReload = true;
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
      deleSx(row) {
        const oid = row.id;
        pubMaterialNumByServiceOid(this.comboDireOid, row.serviceOid).then(response => {
          if (response.data > 0) {
            this.msgWarning("当前事项下有材料被整合，不能被取消！");
            return false;
          } else {
            this.$confirm('是否确认取消?', "警告", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning"
            }).then(function() {
              return delSxservice(oid);
            }).then(() => {
              this.msgSuccess("取消成功");
              this.getList();
            }).catch(function() {});
          }
        });

      },
      /**删除目录事项
       * */
      delComboService(row) {
        const oid = row.id;
        pubMaterialNumByServiceOid(this.comboDireOid, row.serviceOid).then(response => {
          if (response.data > 0) {
            this.msgWarning("当前事项下有材料被整合，不能被取消！");
            return false;
          } else {
            this.$confirm('是否确认取消?', "警告", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning"
            }).then(function() {
              return delComboService(oid);
            }).then(() => {
              this.msgSuccess("删除成功");
              this.getList();
            }).catch(function() {});
          }
        });
      },
      /**选择特别程序
       * */
      selectServiceSpecial(row, isSpecial) {
        const oid = row.id;
        if (isSpecial == 0) {
          this.$confirm('是否确认取消?', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function() {
            return selectSpecial(oid);
          }).then(() => {
            this.msgSuccess("取消成功");
            this.getList();
          }).catch(function() {});
        } else {
          this.$confirm('是否确认选择?', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function() {
            return selectSpecial(oid);
          }).then(() => {
            this.msgSuccess("选择成功");
            this.getList();
          }).catch(function() {});
        }




      },
      closeChooseService() {
        this.chooseSxService = false;
        this.ifReload = false;
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

      getStatus(val) {
        if (val.status == 0) {
          return '暂存';
        } else if (val.status == 1) {
          return '已配置'
        } else {
          return '';
        }
      },
      //确认配置当前目录下的所有事项
      setStatusAll() {
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
    }
  };
</script>
