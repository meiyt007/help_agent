<template>
  <div class="app-container">

    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="所属区划" prop="districtOidSelect">
        <treeselect  class="treeselect" :defaultExpandLevel="1" noOptionsText = "暂无数据" noResultsText="暂无数据" :show-count="true" v-model="queryParams.districtOidSelect" :options="districtOptions" placeholder="请输入所属区划" />
      </el-form-item>
      <el-form-item label="所属机构" prop="organOidSelect">
        <treeselect  class="treeselect" :defaultExpandLevel="1" noOptionsText = "暂无数据" noResultsText="暂无数据" :show-count="true" v-model="queryParams.organOidSelect" :options="listOrganOptions" placeholder="请输入所属机构" />
      </el-form-item>
      <el-form-item label="岗位名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入岗位名称"
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
          v-hasPermi="['sys:post:init']"
        >新增
        </el-button>
      </el-col>
    </el-row>
    <el-table v-loading="loading" :data="postList" border>
      <!--<el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="所属区划" align="center" prop="districtName"  :show-overflow-tooltip="true"/>
      <el-table-column label="所属机构" align="center" prop="organName" :show-overflow-tooltip="true"/>
      <el-table-column label="岗位名称" align="center" prop="name" :show-overflow-tooltip="true"/>
      <el-table-column label="排序号"  align="center" prop="sort" :show-overflow-tooltip="true"/>
      <el-table-column label="启用状态" align="center"  width="100" prop="isAble" >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isAble"
            :active-value="1"
            :inactive-value="0"
            @change="handleAble(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="400" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakanyonghu"
            @click="handleUserList(scope.row)"
            v-hasPermi="['sys:organ:userListByOrgan']"
          >查看用户</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['sys:app:view']"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            v-hasPermi="['sys:app:update']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
            v-hasPermi="['sys:app:delete']"
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


    <!-- 添加或修改岗位信息对话框 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="openInit" width="70%" append-to-body>
      <div class="el-table__header-wrapper dialog-table">
        <!--<h3>事项标题</h3>-->
        <el-form ref="form" :model="form" :rules="rules" label-width="0px" class="demo-ruleForm">
          <el-input v-show="false" v-model="form.id" />
          <el-input v-show="false" v-model="form.isAble" />
          <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><i class="require">*</i><b>所属区划：</b></td>
              <td>
                <el-form-item prop="districtOidSelect">
                  <treeselect :defaultExpandLevel="1" noOptionsText = "暂无数据"  noResultsText="暂无数据" :show-count="true" v-model="form.districtOidSelect" :options="districtOptions" placeholder="请输入所属区划" />
                </el-form-item>
              </td>

              <td><i class="require">*</i><b>所属机构：</b></td>
              <td>
                <el-form-item prop="organOidSelect">
                  <treeselect
                    v-model="form.organOidSelect"
                    :options="editOrganOptions"
                    noOptionsText="暂无数据"
                    :defaultExpandLevel="1"
                    placeholder="请输入所属机构"/>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>岗位名称：</b></td>
              <td>
                <el-form-item prop="name">
                  <el-input v-model.trim="form.name" placeholder="请输入岗位名称" maxlength="20" show-word-limit/>
                </el-form-item>
              </td>

              <td><b>岗位职责：</b></td>
              <td>
                <el-form-item >
                  <el-input v-model.trim="form.duty" placeholder="请输入岗位职责" maxlength="200" show-word-limit/>
                </el-form-item>
              </td>
            </tr>

            <tr>
              <td><i class="require">*</i><b>排序号：</b></td>
              <td colspan="3">
                <el-form-item prop="sort">
                  <el-input-number v-model="form.sort" :min="1" :max="9999"/>
                </el-form-item>
              </td>
            </tr>

            <tr>
              <td><b>备注：</b></td>
              <td colspan="3">
                <el-form-item>
                  <el-input v-model.trim="form.note" type="textarea" placeholder="请输入备注" maxlength="500" show-word-limit></el-input>
                </el-form-item>
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>


    <!-- 查看登录信息详细 -->
    <el-dialog :title="title" :visible.sync="openView" width="70%" append-to-body>
      <div class="el-table__header-wrapper dialog-table">
        <el-form :model="form" label-width="0px" >
          <table cellspacing="0" cellpadding="0" border="0" >
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>所属区划：</b></td>
              <td>
                {{form.districtName}}
              </td>
              <td><b>所属机构：</b></td>
              <td>
                {{ form.organName }}
              </td>
            </tr>
            <tr>
              <td><b>岗位名称：</b></td>
              <td>
                {{form.name}}
              </td>
              <td><b>岗位职责：</b></td>
              <td>
                {{form.duty}}
              </td>
            </tr>
            <tr>
              <td><b>排序号：</b></td>
              <td colspan="3">
                {{form.sort}}
              </td>
            </tr>
            <tr>
              <td><b>备注：</b></td>
              <td colspan="3">
                {{form.note}}
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
    <el-dialog v-dialog-drag :visible.sync="user.show"  title="查看用户列表" v-for="user in userDialogOptions" @close="closeUserView" width="70%" append-to-body>
      <el-scrollbar style="height:500px;">
        <index-user :postOid="user.postOid"></index-user>
      </el-scrollbar>
    </el-dialog>
  </div>
</template>

<script>
  import { init, save, del ,getOne, page, able,listExp } from "@/api/sys/post";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import {queryOrganTree} from "@/api/sys/organ";
  import {queryDistrictSimpleTree} from "@/api/sys/district";
  import IndexUser from '@/views/sys/post/indexUser';
  export default {
    name: "Post",
    components: { Treeselect,IndexUser },
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 表格数据
        postList: [],
        // 弹出层标题
        title: "",
        // 新增/修改显示弹出层
        openInit: false,
        // 查看显示弹出层
        openView: false,
        //消息
        ableMap:{'1':'启用','0':'禁用'},
        // 锁定状态
        statusOptions: {'0':'未锁定','1':'已锁定'},
        //区划Tree
        districtOptions:[],
        //列表的机构数据
        listOrganOptions: [],
        //修改的机构数据
        editOrganOptions: [],
        //查看用户弹框
        userDialogOptions:[],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: '',
          lockStatus:'0',
          districtOid: null,
          organOid: null,
          account:'',
          districtOidSelect: null,
          organOidSelect: null
        },
        // 表单参数
        form: {districtOidSelect:''},
        //修改的机构数据
        editOrganOptions: [],
        // 表单校验
        rules: {
          districtOidSelect: [
            {required: true, message: '所属区划不能为空', trigger: 'blur'}
          ],
          organOidSelect: [
            {required: true, message: '所属机构不能为空', trigger: 'blur'}
          ],
          name: [
            {required: true, message: '岗位名称不能为空', trigger: 'blur'}
          ],
          sort: [
            {required: true, message: '排序号不能为空', trigger: 'blur'}
          ]
        }
      };
    },
    created() {
      this.getDistrictTree();
      this.getList();
    },
    watch: {
      'queryParams.districtOidSelect': {
        handler(val,oldVal) {
          let dataId = null!=val?val.substring(val.lastIndexOf('-')+1,val.length):'';
          this.queryParams.districtOid = dataId;
          //机构加载
          this.$set(this.queryParams,'organOid',null);
          this.$set(this.queryParams,'organOidSelect',null);
          this.getListOrganTree(dataId)
        }
      },
      'queryParams.organOidSelect': {
        handler(val, oldVal) {
          let dataId = null!=val?val.substring(val.lastIndexOf('-')+1,val.length):'';
          this.queryParams.organOid = dataId;
        }
      },
      'form.districtOidSelect': {
        handler(val, oldVal) {
          let dataId = null!=val?val.substring(val.lastIndexOf('-')+1,val.length):'';
          this.form.districtOid = dataId;
          //机构加载
          if(undefined != val && undefined != oldVal &&  val != oldVal){
            this.getEditOrganTree(dataId,true);
          }else {
            this.getEditOrganTree(dataId,false);
          }
        }
      },
      'form.organOidSelect': {
        handler(val, oldVal) {
          let dataId = null!=val?val.substring(val.lastIndexOf('-')+1,val.length):'';
          this.form.organOid = dataId;
        }
      }
    },
    methods: {
      /** 查询岗位列表 */
      getList() {
        this.loading = true;
        let that = this;
        page(this.queryParams).then(response => {
          this.postList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        }).catch(function() {that.loading = false;});
      },
      /** 获取区划树 */
      getDistrictTree(districtOid) {
        queryDistrictSimpleTree(districtOid).then(response => {
          this.districtOptions = response.data;
        });
      },
      /** 获取机构数*/
      getListOrganTree(districtOid) {
        this.organOptions = [];
        this.queryParams.organOid = null;
        if (districtOid) {
          queryOrganTree(districtOid).then(response => {
            this.listOrganOptions = response.data;
          });
        } else {
          this.listOrganOptions = [];
        }
      },
      async getEditOrganTree(districtOid,initFlag) {
        if (districtOid) {
          await queryOrganTree(districtOid).then(response => {
            if(initFlag){
              //this.form.organOid = null;
              //this.form.organOidSelect = null;
              this.$set(this.form,'organOid',null);
              this.$set(this.form,'organOidSelect',null);
            }
            this.editOrganOptions = [];
            this.editOrganOptions = response.data;
          });
        }
      },
      // 启用状态
      statusFormat(row) {
        return this.selectMapLabel(this.ableMap, row.isAble);
      },
      // 取消按钮
      cancel() {
        this.openInit = false;
        this.reset();
      },
      //查看用户
      handleUserList(row){
        let item = {show:true,postOid:row.postOid};
        this.userDialogOptions.push(item);
      },
      // 关闭按钮
      closeUserView() {
        this.userDialogOptions.pop();
      },
      // 表单重置
      reset() {
        this.form = {
          name: null
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
        // this.queryParams={};
        Object.assign(this.queryParams, this.$options.data().queryParams)
        this.handleQuery();
      },
      /** 查看按钮操作 */
      handleView(row) {
        this.reset();
        const oid = row.id;
        getOne(oid).then(response => {
          this.form = response.data;
          this.openView = true;
          this.title = "查看岗位信息";
        });
      },
      /** 新增和修改按钮操作 */
      handleInit(row) {
        this.reset();
        const oid = row.id;
        this.editOrganOptions = [];
        if(oid == undefined){
          this.form = {};
          this.openInit = true;
          this.title = "新增岗位信息";
        }else {
          init(oid).then(res => {
            let result=res.data;
            if(null!=result.districtOid && ''!=result.districtOid){
              result.districtOidSelect= 'DISTRICT-'+result.districtOid;
            }
            if(null!=result.organOid && ''!=result.organOid){
              result.organOidSelect='ORGAN-'+result.organOid;
            }
            this.form = result;
            this.openInit = true;
            this.title = "修改岗位信息";
          });
        }
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            save(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("保存成功");
                this.openInit = false;
                this.getList();
              }
            });
          }else {
            return false;
          }
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
      /** 启禁用按钮操作 */
      handleAble(row) {
        const oid = row.id;
        let ableMessage = row.isAble === 1 ? '启用' : '禁用'
        this.$confirm('你确定要' + ableMessage + '吗？', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return able(oid);
        }).then(() => {
          this.msgSuccess(ableMessage+"成功");
        }).catch(function() {
          row.isAble = row.isAble === 0 ? 1 : 0
        });
      },
      /** 导出按钮操作 */
      handleExport() {
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出列表数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          listExp(queryParams);
        }).catch(function() {});
      },

    }
  };
</script>
<style scoped>
  .treeselect{
    width: 200px;
  }
  table {
    border-collapse: separate !important;
  }
</style>

