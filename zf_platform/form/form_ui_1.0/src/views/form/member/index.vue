<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="90px">
      <el-form-item style="margin-left: -40px;" label="姓名" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入姓名"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item class="fr mr0">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="ml5">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit"
        >新增</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="memberList" border>
      <!--<el-table-column type="selection" width="55" align="center"  />-->
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="姓名" align="center" prop="userName" :show-overflow-tooltip="true" />
      <el-table-column label="新增时间" align="center" prop="createDate" width="200" :show-overflow-tooltip="true"/>
      <el-table-column label="操作" width="260" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="isAdminUser && currentUserOid != scope.row.userOid"
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


    <!-- 添加或修改成员信息对话框 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="openInit" width="70%" append-to-body>
      <div class="el-table__header-wrapper dialog-table">
        <!--<h3>标题</h3>-->
        <el-form ref="form" :model="form" :rules="rules" label-width="0px" class="demo-ruleForm">
          <el-input v-show="false" v-model="form.id" />
          <el-input v-show="false" v-model="form.isDelete" />
          <el-input v-show="false" v-model="form.createDate" />
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
                <el-form-item prop="districtOid">
                  <treeselect
                    class="treeselect"
                    :defaultExpandLevel="1"
                    noOptionsText = "暂无数据"
                    noResultsText="暂无数据"
                    @input="districtOidChange"
                    v-model="form.districtOid"
                    :options="districtOptions" placeholder="请选择所属区划"
                    :appendToBody="true"
                    :zIndex="9999"  />
                </el-form-item>
              </td>
              <td><i class="require">*</i><b>所属机构：</b></td>
              <td>
                <el-form-item prop="organOids">
                  <treeselect
                    :multiple="true"
                    :options="organOptions"
                    noOptionsText = "暂无数据"
                    noResultsText="暂无数据"
                    placeholder="请选择所属机构"
                    :defaultExpandLevel="1"
                    v-model="organOids"
                    @input="organOidChange"
                    :limit="3"
                    :limitText="limitText"
                    :appendToBody="true"
                    :zIndex="9999"
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>成员：</b></td>
              <td colspan="3">
                <el-form-item prop="userOids" >
                  <el-select  style="width: 100%" v-model="form.userOids" multiple 
                              @change="select_status"
                              @remove-tag="removeTag"
                              clearable
                              filterable
                              placeholder="请选择">
                    <el-option
                      v-for="dict in userOptions"
                      :key="dict.id"
                      :label="dict.label"
                      :value="dict.id"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              </td>
            </tr>
            <tr style="height: 50px"/>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { init, save, del ,getOne, page, able } from "@/api/form/member";
  import {queryAllDistrictSimpleTree } from "@/api/sys/district";
  import {queryUserTreeByOrganOid } from "@/api/sys/user";
  import Treeselect from '@riophae/vue-treeselect'
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  export default {
    name: "Member",
    components: { Treeselect },
    props:['authorizeKey'],
    data() {
      return {
        //授权id
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 成员表格数据
        memberList: [],
        // 弹出层标题
        title: "",
        // 新增/修改显示弹出层
        openInit: false,
        // 查看显示弹出层
        openView: false,
        //区划数据
        districtOptions: [],
        //修改的机构数据
        organOptions: [],
        userOptions:[],
        organOids:[],
        // 启用状态
        ableMap:{'1':'启用','0':'禁用'},
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: ''
        },
        // 表单参数
        form: {
        },
        oldOptions:[],
        value:[],
        //当前登录用户oid
        currentUserOid:'',
        isAdminUser:false,
        // 表单校验
        rules: {
          districtOid: [
            { required: true, message: "所属区划不能为空", trigger: "change" }
          ],
          organOids: [
            { required: true, message: "所属机构不能为空", trigger: "change" }
          ],
          userOids: [
            { required: true, message: "成员不能为空", trigger: "blur" }
          ],

        }
      };
    },
    created() {
      this.getDistrictTree();
      this.getList();
    },
    methods: {
      // 多选模式下移除tag时触发,val为移除的tag值
      removeTag(val) {
        if (val === "allSelect") {
          this.value = [];
        }
      },
      select_status(valArr){
        this.$forceUpdate();
        const allIdArr = []
        // 保存所有选项的id
        for (const argumentId of this.userOptions) {
          allIdArr.push(argumentId.id)
        }
        const oldVal = this.oldOptions.length === 1 ? this.oldOptions[0] : []
        // 当前选中的有'全选'
        if (valArr.includes('allSelect')) {
          this.value = allIdArr
        }
        // 旧数据包含'全选'，当前选中数据不包含全选
        if (oldVal.includes('allSelect') && !valArr.includes('allSelect')) {
          this.value = []
        }
        // 旧数据包含'全选'，当前选中数据包含全选
        if (oldVal.includes('allSelect') && valArr.includes('allSelect')) {
          const index = valArr.indexOf('allSelect')
          valArr.splice(index, 1) // 排除全选选项
          this.value = valArr
        }
        // 旧数据不包含'全选'，当前选中数据不包含'全选'
        if (!oldVal.includes('allSelect') && !valArr.includes('allSelect')) {
          // 除了全选外 其他全部选中时
          if (valArr.length === allIdArr.length - 1) {
            this.value = ['allSelect'].concat(valArr)
          }else {
            this.value = valArr
          }
        }
        // 数据发生变化时保存数据，作为下次对比的旧数据
        this.oldOptions[0] = this.value
        this.form.userOids = this.value
      },
      limitText(count){
        return `更多+${count}`;
      },
      /** 获取所有区划树 */
      getDistrictTree(districtOid) {
        queryAllDistrictSimpleTree(districtOid).then(response => {
          this.districtOptions = JSON.parse(JSON.stringify(response.data).replace(/DISTRICT-/g,""));
        });
      },
      /**  区划tree选择触发事件 并切换组织机构 */
      async districtOidChange(districtOid) {
        this.organOids = [];
        this.form.userOids = [];
        this.organOptions = [];
        if(undefined!=districtOid){
          this.organOptions = [];
          await this.queryOrganTree(districtOid).then(response => {
            this.form.userOids = [];
            this.userOptions = [];
            this.organOptions =  JSON.parse(JSON.stringify(response.data).replace(/ORGAN-/g,""));
          });
        }
      },
      async organOidChange(organOids){
        this.form.organOids = this.organOids;
        this.userOptions = [];
        this.form.userOids = [];
        for(const i in organOids){
          let organOid = organOids[i];
          await queryUserTreeByOrganOid(organOid).then(response => {
            response.data = JSON.parse(JSON.stringify(response.data).replace(/USER-/g,""));;
            if(null!=response.data){
              const allSelect = {
                label: '全选',
                id: 'allSelect'
              }
              this.userOptions.unshift(allSelect)
              for (let j in response.data){
                this.userOptions.push(response.data[j]);
              }
            }
          }).catch(function() {
          });
        }
      },
      /** 查询成员列表 */
      getList() {
        this.loading = true;
        let that = this;
        if(undefined == this.queryParams.authorizeKey)
          this.queryParams.authorizeKey = this.authorizeKey;
        page(this.queryParams).then(response => {
          this.currentUserOid = response.data.currentUserOid;
          this.isAdminUser = response.data.isAdminUser;
          this.memberList = response.data.pageResult.data;
          this.total = response.data.pageResult.total;
          this.loading = false;
        }).catch(function () {
          that.loading = false;
        });
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
        this.queryParams = {};
        this.queryParams.pageNum = 1;
        this.queryParams.pageSize = 10;
        this.handleQuery();
      },
      /** 查看按钮操作 */
      handleView(row) {
        this.reset();
        const oid = row.id;
        getOne(oid).then(response => {
          this.form = response.data;
          this.openView = true;
          this.title = "查看成员信息";
        });
      },
      /** 新增和修改按钮操作 */
      handleInit(row) {
        this.reset();
        const oid = row.id;
        if(undefined == this.form.authorizeKey){
          this.form.authorizeKey = this.authorizeKey;
        }
        if(oid == undefined){
          this.form = {};
          this.openInit = true;
          this.title = "新增成员信息";
        }else {
          init(oid).then(response => {
            this.form = response.data;
            this.openInit = true;
            this.title = "修改成员信息";
          });
        }
      },
      /** 提交按钮 */
      submitForm: function() {
        if(undefined == this.form.authorizeKey){
          this.form.authorizeKey = this.authorizeKey;
        }
        this.$refs["form"].validate(valid => {
          if (valid) {
            let formData =  JSON.parse(JSON.stringify(this.form));
            formData.userOids.forEach((item,index,arr) => {
              if(item == 'allSelect'){
                arr.splice(index,1)
              }
            });
            save(formData).then(response => {
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

    }
  };
</script>
<style scoped>
  textarea{
    min-height: 100px !important;
  }
  table {
    border-collapse: separate !important;
  }
  .treeselect{
    width: 100% !important;
  }
</style>
