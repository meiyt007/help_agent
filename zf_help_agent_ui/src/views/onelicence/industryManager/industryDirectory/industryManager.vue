/**
* 一业一证业态管理
* @Author: wangwg
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--业态目录列表数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="108px">
          <el-form-item label="业态目录名称" prop="comboDirectoryName">
            <el-input
              v-model.trim="queryParams.comboDirectoryName"
              placeholder="请输入业态目录名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8 fl">
          <el-col :span="1.5">
            <el-button
              type="default"
              icon="el-icon-plus"
              size="mini"
              @click="handleInit"
              v-hasPermi="['one:industry:init']"
            >新增</el-button>
          </el-col>
        </el-row>

        <el-table v-loading="loading" :data="comboDirectoryList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column label="业态分类名称"   align="center" :show-overflow-tooltip="true" prop="themeName" />
          <el-table-column label="业态目录名称" align="center" :show-overflow-tooltip="true" prop="comboDirectoryName" />
          <el-table-column label="所属区划"  align="center" :show-overflow-tooltip="true" prop="districtName" />
          <el-table-column label="主办部门"  align="center" :show-overflow-tooltip="true" prop="mainOrganName" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-chakan"
                @click="handleView(scope.row)"
                v-hasPermi="['one:industry:view']"
              >查看</el-button>
<!--              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="handleEdit(scope.row)"
                v-hasPermi="['one:industry:update']"
              >修改</el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-shanchu"
                @click="handleDelete(scope.row)"
                v-hasPermi="['one:industry:delete']"
              >删除</el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-penzhi"
                @click="queryServiceList(scope.row)"
                v-hasPermi="['one:industry:service']"
              >事项</el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-penzhi"
                @click="queryMaterialList(scope.row)"
                v-hasPermi="['one:industry:material']"
              >材料</el-button>
             <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-penzhi"
                @click="queryResultList(scope.row)"
                v-hasPermi="['one:industry:result']"
              >结果</el-button>-->
              <el-button
              size="mini"
              type="text"
               icon="iconfont zfsoft-penzhi"
              @click="handleResultList(scope.row)"
               v-hasPermi="['one:industry:result']"
               >证照梳理</el-button>
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
      </el-col>
    </el-row>

    <!-- 添加业态信息 -->
    <el-dialog v-dialog-drag :visible.sync="openInit" v-if="openInit"  title="新增业态目录" width="70%" :close-on-click-modal="false" :modal-append-to-body="false" append-to-body>
      <el-scrollbar style="height:500px;">
        <combo-directory  @case-close="closeIndustry"></combo-directory>
      </el-scrollbar>
    </el-dialog>

    <!-- 业态目录信息详细 -->
    <el-dialog v-dialog-drag :visible.sync="openView" v-if="openView"  title="业态目录信息" width="1100px" height="700px" scrollbar append-to-body>
        <combo-directory-view :comboDirectoryOid="comboDirectoryOidView" @case-close="closeView"></combo-directory-view>
         <div slot="footer" class="zf-text-center">
        <el-button @click="closeView">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 业态目录修改 -->
    <el-dialog v-dialog-drag :visible.sync="openEditView" v-if="openEditView"  title="业态目录修改" width="1100px" height="700px" scrollbar append-to-body>
        <combo-directory-edit :comboDirectoryOid="comboDirectoryOid"></combo-directory-edit>
    </el-dialog>

    <!--业态事项关联-->
    <el-dialog v-dialog-drag :visible.sync="combo.show"  title="业态事项关联" v-for="combo in comboDialogOptions" @close="closeUserView" :close-on-click-modal="false" width="1100px" height="700px" scrollbar append-to-body>
        <combo-service :comboDireOid="combo.comboDirectoryOid"></combo-service>
    </el-dialog>
    <!--业态事项关联结束-->

    <!--业态目录公共材料开始-->
    <el-dialog v-dialog-drag :visible.sync="service.show"  title="业态目录材料" v-for="service in materialDialogOptions" @close="closeMaterView" :close-on-click-modal="false" width="1100px" height="700px" scrollbar append-to-body>
        <combo-directory-material :comboDireOid="service.comboDirectoryOid"></combo-directory-material>
    </el-dialog>
    <!--业态目录公共材料结束-->
    <!--业态目录统一结果开始-->
    <el-dialog v-dialog-drag :visible.sync="result.show"  title="业态目录结果" v-for="result in resultDialogOptions" @close="closeResultView" :close-on-click-modal="false" width="1100px" height="700px" scrollbar append-to-body>
        <combo-directory-result :comboDireOid="result.comboDirectoryOid"></combo-directory-result>
    </el-dialog>
    <!--业态目录统一结果结束-->
    <!--业态目录证照梳理开始-->
        <el-dialog class="dialog-header" v-dialog-drag :visible.sync="result.show"  title="业态证照梳理" v-for="result in handleResultDialogOptions" @close="closeResultView" :close-on-click-modal="false" width="1100px" height="700px" scrollbar append-to-body>
            <combo-handle-result :comboDireOid="result.comboDirectoryOid"></combo-handle-result>
        </el-dialog>
    <!--业态目录证照梳理结束-->
    <!--引入文件的预览弹出层-->
    <el-dialog v-dialog-drag title="文件预览" :visible.sync="view.show" v-for="view in viewDialogOptions" :close-on-click-modal="false"
               @close="closeFileView" width="1000px"  append-to-body>
      <combo-dire-file-view :attaOid="view.attaOid"  @father-click="closeFileView"></combo-dire-file-view>
    </el-dialog>

  </div>
</template>

<script>
import {page} from "@/api/onelicence/industryManager/industryManager";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import comboService from "@/views/onething/sxpz/comboDirectory/comboService";
  import comboDirectoryMaterial from "@/views/onething/sxpz/comboDirectory/comboDirectoryMaterial";
  import comboDirectoryResult from "@/views/onething/sxpz/comboDirectory/comboDirectoryResult";
  import comboDireFileView from "@/views/onething/sxpz/comboDirectory/comboDireFileView";
  import comboDirectoryView from "@/views/onelicence/industryManager/industryDirectory/comboDirectoryView";
  import comboHandleResult from "@/views/onelicence/industryManager/industryDirectory/comboHandleResult";
  import comboDirectoryEdit from "@/views/onelicence/industryManager/industryDirectory/comboDirectoryEdit";
  import comboDirectory from "@/views/onelicence/industryManager/industryDirectory/comboDirectory";
  export default {
    components: {Treeselect,comboService,comboDirectoryMaterial,comboDirectoryResult,comboHandleResult,comboDireFileView,comboDirectoryView,comboDirectoryEdit,comboDirectory},
    name: "IndustryManager",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        attatotal:0,
        // 应用表格数据
        comboDirectoryList: [],
        viewDialogOptions:[],
        //事项查看
        comboDialogOptions:[],
        materialDialogOptions:[],
        resultDialogOptions:[],
        handleResultDialogOptions:[],

        ifDis:false,
        direTypes:[{
          value: 1,
          label: '单部门行政审批类'
        }, {
          value: 2,
          label: '跨部门业务协同类'
        }, {
          value: 3,
          label: '政府服务类'
        }, {
          value: 4,
          label: '跨层级业务协同类'
        }, {
          value: 5,
          label: '跨区域业务协同类'
        }],
        //查询一件事目录名称参数
        comboDirectoryName:'',
        // 弹出层标题
        title: "",
        // 新增/修改显示弹出层
        openInit: false,
        // 查看显示弹出层
        openView: false,
        openEditView: false,
        //选择附件项附件
        openComboService:false,
        comboDireOid:"",
        //目录查看使用
        comboDirectoryOidView:"",
        comboDirectoryOid:"",

        fileList:[],
        elements: ['1'],
        // 区划树选项
        //themeOptions: undefined,
        // 级别字典
        // comboThemes: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          comboDirectoryName: '',
          status: '1',
          industryType: 1,
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
          comboServiceObject:'1',
          ifCharge : 0,
          isZjfw:0,
          appointmentFlag:0,
          handleForm:'0',
          onlinePayFlag:0,
          expressFlag:0,
          unionOrganFlag:0,
          countToScence:0,
          direType: '',
          serviceOids:'',
          servicextlx:'',
          servicespxt:'',
          serviceApplyAddr:''
        },
        //一件事分类Tree
        themeOptions:[],
        roleForm:{serviceOids:''},
        //区划Tree
        districtOptions:[],
        //角色
        appRoleOptions:[],
        //主办部门Tree
        mainOrganOptions:[],
        //协办部门Tree
        assistOrganOptions:[],
        //服务对象
        comboServiceObjects:[],
        assistOrganOids:[],
        handleFlowFlag:false,
        statusOptions: {'0':'未发布','1':'已发布'},
      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询业态目录列表 */
      getList() {
        this.loading = true;
        page(this.queryParams).then(response => {
          this.comboDirectoryList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        });
      },
      // 取消按钮
      cancel() {
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          comboServiceObject:'1',
          ifCharge:0,
          isZjfw:0,
          appointmentFlag:0,
          handleForm:'0',
          onlinePayFlag:0,
          expressFlag:0,
          unionOrganFlag:0,
          countToScence:0,
        }
        this.elements = ['1'];
        this.assistOrganOptions =[];
        this.form.assistOrganOid = null;
        this.resetForm("form")
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
        this.title = "查看一件事目录信息";
      },
      /** 新增按钮操作 */
      handleInit: function (row) {
         let _that = this;
        _that.openInit = true;
        _that.dialogTitle = "新增";
      },
      /** 修改按钮操作 */
      handleEdit: function (row) {
        let _that = this;
        _that.comboDirectoryOid = row.comboDirectoryOid;
        _that.openEditView = true;
        _that.dialogTitle = "修改" ;
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const oid = row.id;
        this.$confirm('是否确认删除?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          return del(oid);
        }).then(() => {
          this.msgSuccess("删除成功");
          this.getList();
        }).catch(() => {});
      },

      //事项关联
      queryServiceList(row){
        let item = {show:true,comboDirectoryOid:row.comboDirectoryOid};
        this.comboDialogOptions.push(item);
      },
      //一件事目录材料整合
      queryMaterialList(row){
        let item = {show:true,comboDirectoryOid:row.comboDirectoryOid};
        this.materialDialogOptions.push(item)
      },
      //一件事目录结果
      queryResultList(row){
        let item = {show:true,comboDirectoryOid:row.comboDirectoryOid};
        this.resultDialogOptions.push(item)
      },
      //业态证照梳理
        handleResultList(row){
           let item = {show:true,comboDirectoryOid:row.comboDirectoryOid};
           this.handleResultDialogOptions.push(item)
      },
      closeIndustry() {
        this.openInit = false;
        this.getList();
      },
      // 关闭按钮
      closeView() {
        this.openView= false;
        this.getList();
      },
      // 关闭按钮
      closeMaterView() {
        this.materialDialogOptions.pop();
      },
      // 关闭按钮
      closeResultView() {
        this.materialDialogOptions.pop();
      },
      getPublishName(val){
        if(val.status == 0 || val.status == 4){
          return '未发布';
        }else if(val.status == 1){
          return '已发布';
        }else{
          return '';
        }
      },
      /** 根据不同事项大类、获取协办部门 **/
      getDireType(val){
        if(val === 1) {//单部门
          this.ifDis = true;
          this.getAssisOrganTree(this.form.districtOid,this.form.mainOrganOid);
        } else {
          //跨部门、政府服务、跨层级、跨区域
          this.ifDis = false;
          this.form.assistOrganOid = null;
          this.assistOrganOptions =[];
          this.getAssisOrganTree(this.form.districtOid);
        }
      }
    }
  };
</script>
<style lang="scss" scoped>
  .treeselect{
    width: 200px;
  }
  .treeselect240{
    width: 240px;
  }
</style>
