/**
* @Author: dongxl
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--事项数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="110px">
          <el-form-item label="目录名称" prop="comboDirectoryName">
            <el-input v-model.trim="queryParams.comboDirectoryName" placeholder="请输入目录名称" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="编码" prop="comboDirectoryCode">
            <el-input v-model.trim="queryParams.comboDirectoryCode" placeholder="请输入编码" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置</el-button>
          </el-form-item>
        </el-form>

        <el-table v-loading="loading" :data="districtList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="所属区划" align="center"  prop="districtName" :show-overflow-tooltip="true"/>
          <el-table-column label="主办部门" align="center"  prop="mainOrganName" :show-overflow-tooltip="true"/>
          <el-table-column label="目录名称"  align="center" prop="comboDirectoryName" :show-overflow-tooltip="true"/>
          <el-table-column label="编码"  align="center" prop="comboDirectoryCode" :show-overflow-tooltip="true"/>
          <el-table-column label="发布状态"  align="center" prop="ifPublish" :formatter="getPublishName" :show-overflow-tooltip="true"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)" v-hasPermi="['sys:reg:view']" >查看</el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-shouquan"  @click="handleInit(scope.row)" v-hasPermi="['sys:reg:update']">授权</el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-quxiao"  @click="deleteSQ(scope.row)" v-hasPermi="['sys:reg:update']">取消</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
      </el-col>
    </el-row>
    <!-- 添加或修改信息对话框 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="title" :visible.sync="openInit" v-if="openInit" scrollbar width="900px" height='600px' append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>

          <tr>
            <td>
              <b>所属区划：</b>
            </td>
            <td>
              <el-form-item prop="districtName">
                <el-col :span="24">
                  {{ form.districtName }}
                </el-col>
              </el-form-item>
            </td>
            <td>
              <b>主办部门：</b>
            </td>
            <td>
              <el-form-item prop="mainOrganName">
                <el-col :span="24">
                  {{ form.mainOrganName }}
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>目录名称：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="comboDirectoryName">
                <el-col :span="24">
                  {{ form.comboDirectoryName }}
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>实施编码：</b>
            </td>
            <td>
              <el-form-item prop="comboDirectoryCode">
                <el-col :span="24">
                  {{ form.comboDirectoryCode }}
                </el-col>
              </el-form-item>
            </td>
            <td>
              <b>申报须知：</b>
            </td>
            <td>
              <el-form-item prop="declareNeedKnow">
                <el-col :span="24">
                  {{ form.declareNeedKnow }}
                </el-col>
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td>
              <b>选择辖区：</b>
            </td>
            <td colspan="3">
              <el-row>
                <el-col :span="24">
                  <el-form-item prop="siteOids">
                    <treeselect :multiple="true" :disable-branch-nodes="true" ref="userTree" :defaultExpandLevel="1" noOptionsText = "暂无数据"  noResultsText="暂无数据" :show-count="true" v-model="form.siteOids" :options="appSiteOptions" placeholder="请选择辖区" append-to-body/>
                  </el-form-item>
                </el-col>
              </el-row>
            </td>
          </tr>

        </table>
      </el-form>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitRole">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 信息详细 -->
    <el-dialog v-dialog-drag :title="title" :visible.sync="openView" width="900px" scrollbar height='600px' v-if="openView" append-to-body>
      <el-form ref="form" :model="form" label-width="0" size="mini" >
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b>所属区划：</b>
            </td>
            <td>
              <el-form-item prop="districtName">
                <el-col :span="24">
                  {{ form.districtName }}
                </el-col>
              </el-form-item>
            </td>
            <td>
              <b>主办部门：</b>
            </td>
            <td>
              <el-form-item prop="mainOrganName">
                <el-col :span="24">
                  {{ form.mainOrganName }}
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>目录名称：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="comboDirectoryName">
                <el-col :span="24">
                  {{ form.comboDirectoryName }}
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>编码：</b>
            </td>
            <td>
              <el-form-item prop="comboDirectoryCode">
                <el-col :span="24">
                  {{ form.comboDirectoryCode }}
                </el-col>
              </el-form-item>
            </td>
            <td>
              <b>申报须知：</b>
            </td>
            <td>
              <el-form-item prop="declareNeedKnow">
                <el-col :span="24">
                  {{ form.declareNeedKnow }}
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>分类名称：</b>
            </td>
            <td>
              <el-form-item prop="themeOid">
                <el-col :span="24">
                  {{ form.themeName }}
                </el-col>
              </el-form-item>
            </td>
            <td>
              <b>办理形式：</b>
            </td>
            <td>
              <span v-if="form.handleForm==0">
                窗口办理
              </span>
              <span v-else-if="form.handleForm==1">
                网上办理
              </span>
              <span v-else-if="form.handleForm==2">
                一体化办理
              </span>
            </td>
          </tr>
          <tr>
            <td>
              <b>服务对象：</b>
            </td>
            <td>
            <span v-if="form.comboServiceObject==1">
                自然人
              </span>
              <span v-else>
                法人
              </span>
            </td>
            <td>
              <b>是否收费：</b>
            </td>
            <td>
              <el-form-item prop="ifCharge">
                <el-col :span="24">
                  {{ form.ifChargeName }}
                </el-col>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="zf-text-center">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {pageOfCombo} from "@/api/onething/comboAuthorize/comboRegisterAuthorize";
import {getComboOne,saveOrUpdateForSite,querySiteAuthorizeTree,getSiteOidsByComboDirectoryOid,delComboDireSite} from "@/api/onething/comboAuthorize/comboAuthorize";
// import the styles
import '@riophae/vue-treeselect/dist/vue-treeselect.css';
import Treeselect from '@riophae/vue-treeselect';
export default {
  name: "ComboSiteAuthorize",
  components: {Treeselect},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      districtList: [],

      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      appSiteOptions:[],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        comboDirectoryName: "",
        comboDirectoryCode:"",
        status:"1"

      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {siteOids: [
          {type:'array',required: true, message: '所选辖区不能为空', trigger: 'change'}
        ]},
      comboReg:{comboDirectoryOid:'',siteOid:''},
      siteOidArr:[],
      props: {
        label: "label",//这里是树结构中需显示的数据（即接口返回的需展示在页面上的参数）
        children: 'children',
        isLeaf: "leaf"
      },
      watch: {
        'form.siteOids':function(val){
          if(this.form.siteOids&&this.form.siteOids>0){
            this.form.alidRoleOids= this.form.siteOids[0];
          }
        }
      },

    };
  },
  created() {
    this.getList();

  },
  methods: {

    /** 查询列表 */
    getList() {
      this.loading = true;
      pageOfCombo(this.queryParams).then(response => {
        this.districtList = response.data.data;
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
      Object.assign(this.form, this.$options.data().form)
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
      let _that = this;
      _that.reset();
      const oid = row.comboDirectoryOid;
      getComboOne(oid).then(response => {
        _that.form = response.data;
        _that.openView = true;
        _that.title = "查看目录信息";
      });
    },
    deleteSQ(row){//删除授权
      const oid = row.comboDirectoryOid;
      this.$confirm('确认取消授权?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return delComboDireSite(oid);
      }).then(() => {
        this.msgSuccess("取消成功");
        this.getList();
      }).catch(function() {});
    },
    /** 新增和修改按钮操作 */
    handleInit(row) {
      let _that = this;
      _that.reset();
      if(row.comboDirectoryOid) {
        this.comboReg.comboDirectoryOid=row.comboDirectoryOid;
        getComboOne(row.comboDirectoryOid).then(response => {
          _that.openInit = true;
          _that.form = response.data;
        });

        let treeQuery = {
          appOid: '',
          disable: this.disable
        }
        getSiteOidsByComboDirectoryOid(row.comboDirectoryOid).then(response => {
          _that.openInit = true;
          _that.siteOidArr = response.data ? response.data.split(';') : [];

          //辖区树
          querySiteAuthorizeTree(treeQuery).then(res => {
            this.appSiteOptions=res.data;
            this.openInit=true;
            _that.form.siteOids=_that.siteOidArr;
          })

        });

      } else {
        _that.openInit = true;
      }

      _that.title ="辖区授权";

    },
    submitRole(){
      if(this.form.siteOids&&this.form.siteOids.length>0){
        this.comboReg.siteOid = this.form.siteOids.join(';');
        saveOrUpdateForSite(this.comboReg).then(res => {
          if (res.code === 200) {
            this.msgSuccess('保存成功')
            this.openInit = false
            this.getList()
          }
        })
      }else{
        this.$refs['form'].validate(valid => {
        })
      }
    },
  getPublishName(val){
    if(val.status == 0){
      return '未发布';
    }else if(val.status == 1){
      return '已发布';
    }else{
      return '';
    }
  }

  },
};
</script>
<style lang="scss" scoped>
.dialog-table {
  padding: 20px;
  box-sizing: border-box;
}
.dialog-table h3 {
  font-size: 14px;
  font-weight: normal;
  color: #333;
  margin: 0px 0px 10px 0px;
}
.dialog-table table {
  width: 100%;
}
.dialog-table table {
  border: 1px solid #dfe6ec;
  border-collapse: collapse;
}

.dialog-table table tr td {
  border: 1px solid #dfe6ec;
  padding: 17px 8px;
  font-size: 12px;
  color: #515a6e;
  text-align: right;
}
.dialog-table table tr td:nth-of-type(2n) {
  color: #606266;
  text-align: left !important;
}
.dialog-table .el-form-item {
  margin-bottom: 0;
}
.el-form-item--small .el-form-item_content {
  margin-left: 0px !important;
}
.line {
  text-align: center;
}
</style>
