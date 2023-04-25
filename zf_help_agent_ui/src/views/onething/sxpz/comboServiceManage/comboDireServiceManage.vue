/** * @Author: liangxm */
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--目录列表数据-->
      <el-col :span="24" :xs="24">
        <el-form
          :model="queryParams"
          ref="queryForm"
          :inline="true"
          label-width="108px"
          @submit.native.prevent
        >
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
        <el-table
          v-loading="loading"
          :data="comboDirectoryList"
          border
          :fit="true"
        >
          <el-table-column label="序号" width="50" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="分类名称"
            align="center"
            prop="themeName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="目录名称"
            align="center"
            prop="comboDirectoryName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="所属区划"
            align="center"
            prop="districtName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="主办部门"
            align="center"
            prop="mainOrganName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
            width="500px"
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
                icon="iconfont zfsoft-xiugai"
                @click="queryComboServiceManage(scope.row)"
                v-hasPermi="['combo:directory:wiki']"
                >政务百科梳理</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="queryComboPostposionService(scope.row)"
                v-hasPermi="['combo:directory:update']"
                >后置事项推荐服务</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="queryDiscountManage(scope.row)"
                v-hasPermi="['combo:directory:discount']"
                >优惠政策梳理</el-button
              >
              <!-- <el-button
                 size="mini"
                 type="text"
                 icon="iconfont zfsoft-tubiao"
                 @click="handleSituationList(scope.row)"
                 v-hasPermi="['combo:directory:delete']"
               >配置情形</el-button>-->
              <!--    <el-button
                    size="mini"
                    type="text"
                    icon="iconfont zfsoft-penzhi"
                    @click="queryOptionSituationList(scope.row)"
                    v-hasPermi="['combo:directory:service']"
                  >查看选项情形</el-button>-->
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
      </el-col>
    </el-row>
    <!--目录百科梳理开始-->
    <el-dialog
      v-dialog-drag
      :visible.sync="service.show"
      title="百科梳理列表"
      v-for="service in materialDialogOptions"
      :key="service.comboDirectoryOid"
      @close="closeMaterView"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <combo-service-manage
        :comboDireOid="service.comboDirectoryOid"
      ></combo-service-manage>
      <div slot="footer" class="zf-text-center">
        <el-button @click="closeMaterView">关 闭</el-button>
      </div>
    </el-dialog>
    <el-dialog
      v-dialog-drag
      :visible.sync="service.show"
      title="后置事项服务配置"
      v-for="service in postpositionDialogOptions"
      :key="service.comboDirectoryOid"
      width="900px"
      append-to-body
    >
      <combo-Postpistion-service
        :comboDireOid="service.comboDirectoryOid"
        @close-materView="closeMaterView"
      ></combo-Postpistion-service>
    </el-dialog>

    <!--目录优惠政策-->
    <el-dialog
      v-dialog-drag
      :visible.sync="service.show"
      title="优惠政策梳理列表"
      v-for="service in discountDialogOptions"
      :key="service.comboDirectoryOid"
      @close="closeMaterView"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <combo-service-discount
        :comboDireOid="service.comboDirectoryOid"
      ></combo-service-discount>
      <div slot="footer" class="zf-text-center">
        <el-button @click="closeMaterView">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 分类信息详细 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="openView"
      v-if="openView"
      title="目录信息"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <combo-directory-view
        :comboDirectoryOid="comboDirectoryOidView"
        @combo-directory="comboDirectoryClose"
      ></combo-directory-view>
      <div slot="footer" class="zf-text-center">
        <el-button @click="comboDirectoryClose">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  init,
  save,
  delOptionRel,
  getOne,
  page
} from "@/api/onething/sxpz/comboDireOptandStu";
/*import { queryServiceTree} from "@/api/zc/businessManagement/sxServiceRegistrar";*/
import { queryComOptionRel } from "@/api/onething/sxpz/comboOptionRel";
import {
  queryComboDirectoryTree,
  queryServiceTree
} from "@/api/onething/sxpz/comboPostpositionService";
import { get } from "@/api/sys/user";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import iconfont from "@/views/common/iconfontSelect";
import { queryOrganTree } from "@/api/sys/organ";
import { queryDistrictSimpleTree } from "@/api/sys/district";
import { getDictList } from "@/api/sys/dict";
import comboServiceManage from "@/views/onething/sxpz/comboServiceManage/comboServiceManage";
import comboPostpistionService from "@/views/onething/sxpz/comboServiceManage/comboPostpistionService";
import comboServiceDiscount from "@/views/onething/sxpz/comboServiceManage/comboServiceDiscount";
import ComboDirectoryView from "@/views/onething/sxpz/comboDirectory/comboDirectoryView";
export default {
  components: {
    ComboDirectoryView,
    Treeselect,
    comboPostpistionService,
    comboServiceManage,
    iconfont,
    comboServiceDiscount
  },
  name: "ComboDirectory",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      attatotal: 0,
      title: "",
      // 应用表格数据
      comboDirectoryList: [],
      //查询目录名称参数
      comboDirectoryName: "",
      //主键
      comboDireOid: "",
      // 查看显示弹出层
      openView: false,
      openRole: false,
      materialDialogOptions: [],
      postpositionDialogOptions: [],
      discountDialogOptions: [],
      comboDirectoryOid: "",
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        comboDirectoryName: "",
        comboDirectoryOid: ""
      },
      //附件参数
      attaQueryParams: {
        pageNum: 1,
        pageSize: 10
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单参数
      form: {
        comboDirectoryOid: "",
        comboDirectoryOids: "",
        serviceOids: "",
        moreStatus: 0
      },
      roleForm: { serviceOids: "" },
      //事项
      appRoleOptions: [],

      //办理深度
      //handleForms:[],
      // 表单校验
      checkList: []
    };
  },

  computed: {},
  created() {
    this.getList();
    queryServiceTree().then(res => {
      this.appRoleOptions = res.data;

      console.log(this.appRoleOptions);
    });
  },
  watch: {
    "form.districtOid": function(val) {
      //区划加载
      this.getOrganTree(val);
      this.getAssisOrganTree(val, this.assistOrganOids);
    },
    "roleForm.serviceOids": function(val) {
      if (this.roleForm.serviceOids && this.roleForm.serviceOids > 0) {
        this.roleForm.alidRoleOids = this.roleForm.serviceOids[0];
      }
    }
  },

  methods: {
    comboDirectoryClose() {
      this.openView = false;
    },
    /** 查询目录列表 */
    getList() {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.comboDirectoryList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 获取区划树 */
    getDistrictTree(districtOid) {
      this.queryDistrictSimpleTree(districtOid).then(response => {
        this.districtOptions = response.data;
      });
    },
    /**获取主办部门树**/
    getOrganTree(districtOid) {
      if (districtOid) {
        this.queryOrganTree(districtOid).then(response => {
          this.mainOrganOptions = response.data;
        });
      } else {
        this.mainOrganOptions = [];
        this.form.mainOrganOid = null;
      }
    },
    queryComboServiceManage(row) {
      let item = { show: true, comboDirectoryOid: row.comboDirectoryOid };
      this.materialDialogOptions.push(item);
    },
    queryComboPostposionService(row) {
      let item = { show: true, comboDirectoryOid: row.comboDirectoryOid };
      this.postpositionDialogOptions.push(item);
    },
    // 关闭按钮
    closeMaterView() {
      this.materialDialogOptions.pop();
      this.postpositionDialogOptions.pop();
      this.discountDialogOptions.pop();
      let item = {
        show: false
      };
      this.getList();
    },
    //优惠政策列表
    queryDiscountManage(row) {
      let item = { show: true, comboDirectoryOid: row.comboDirectoryOid };
      this.discountDialogOptions.push(item);
    },

    // 表单重置
    reset() {
      this.form = {
        fillFlag: 0,
        moreStatus: 0
      };

      this.resetForm("form");
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
      this.comboDirectoryOidView = row.comboDirectoryOid;
      this.openView = true;
      this.title = "查看目录信息";
    },
    /** 新增和修改按钮操作 */
    handleInit(row) {
      let _that = this;
      this.reset();
      let oid = row.comboDirectoryOid;
      let oids = [];
      init(oid, this.comboDirectoryOid).then(response => {
        /* if(response.data.comboPostpositionService != undefined){
            this.form = response.data.comboPostpositionService;

            let serviceOidArr = this.form.serviceOids ? this.form.serviceOids.split(',') : [];
            for(let service of serviceOidArr){
              oids.push(service)
            }
            this.roleForm.serviceOids = oids;
          }*/
        _that.openRole = true;
        _that.title = "后置事项选择";
      });
    },
    submitRole() {
      if (this.roleForm.serviceOids && this.roleForm.serviceOids.length > 0) {
        this.roleForm.userOids = this.roleForm.serviceOids.join(";");
        /*saveOrUpdatePersonReg(this.roleForm).then(res => {
            if (res.code === 200) {
              this.msgSuccess('保存成功')
              this.openRole = false
              this.getList()
            }
          })*/
      } else {
        this.$refs["roleForm"].validate(valid => {});
      }
    },
    /** 删除按钮操作 */
    deleteOptionRel(row) {
      const oid = row.id;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return delOptionRel(oid);
        })
        .then(() => {
          this.msgSuccess("删除成功");
          queryComOptionRel("", row.comboDirectoryOid).then(response => {
            if (response.data != undefined) {
              this.comboOptionRelList = response.data;
            }
            _that.openOptionRelList = true;
            //_that.title =  "选项配置详细页面";
          });
        })
        .catch(function() {});
    },
    getStatus(val) {
      if (val.status == 0) {
        return "暂存";
      } else if (val.status == 1) {
        return "已配置";
      } else {
        return "";
      }
    },

    /** 选项标题提交按钮 */
    submitForm: function() {
      if (this.checkList.length > 0) {
        this.form.comboServiceObject = this.checkList.toString();
      }
      this.form.comboDirectoryOid = this.comboDirectoryOid;
      this.$refs["form"].validate(valid => {
        if (valid) {
          save(this.form).then(response => {
            if (response.code === 200) {
              this.msgSuccess("保存成功");
              this.openInitMater = false;
              this.getTitleList(this.comboDirectoryOid);
              this.openOption = true;
              this.dialogTitle = "修改";
              /*});*/
              this.resetForm("form");
            }
          });
        }
      });
    }
  }
};
</script>
<style lang="scss" scoped>
.treeselect {
  width: 200px;
}
.treeselect240 {
  width: 240px;
}
</style>
