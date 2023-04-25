/**
* @Author: liangxm
* @Date: 2020-10-27 10:21:27
* @Last Modified by: liangxm
* @Last Modified time: 2020-10-27 18:54:27
* @Description: 平板人员管理
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--数字字段树数据-->

      <!--数字平板人员数据-->
      <el-col :span="24" :xs="24" class="app-right">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="108px" @submit.native.prevent>

          <el-form-item label="平板人员名称" prop="name">
            <el-input
              v-model.trim="queryParams.name"
              placeholder="请输入平板人员名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
           <!-- <el-button type="warning" icon="el-icon-refresh" size="mini" @click="handleInit">新增</el-button>-->
          </el-form-item>
        </el-form>

        <!--<el-row :gutter="10" class="mb8 fl">
          <el-col :span="1.5">
            <el-button
              type="default"
              icon="el-icon-plus"
              size="mini"
              @click="handleInit"
            >新增</el-button>
          </el-col>

        </el-row>-->

        <el-table  v-loading="loading" :data="dictList" border :fit="true">
          <!--<el-table-column type="selection" width="55" align="center" />-->
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column label="所属区划"  align="center" prop="districtName"/>
          <el-table-column label="所属机构"  align="center" prop="organName"/>
          <el-table-column label="平板人员名称"   align="left" prop="name" />

          <el-table-column label="是否启用平板评价" align="center"   prop="appraiseFlag" >

            <template slot-scope="scope">
              <el-switch :value="scope.row.isAble == '1'" @change="handleAppraise(scope.row)"></el-switch>
            </template>
          </el-table-column>
          <el-table-column label="是否启用平板确认" align="center"   prop="confirmFlag" >

            <template slot-scope="scope">
              <el-switch :value="scope.row.isDelete == '1'" @change="handleConfirmFlag(scope.row)"></el-switch>
            </template>
          </el-table-column>


          <el-table-column label="创建时间"    align="left" prop="createDate" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-chakan"
                @click="handleView(scope.row)"
                v-hasPermi="['sys:pbpjUser:view']"
              >查看</el-button>
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


    <!-- 数字平板人员信息详细 -->
    <el-dialog v-dialog-drag :title="title" :visible.sync="openView" width="800px" append-to-body>
      <div>
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
            <td style="text-align: left">
              {{ form.districtName }}
            </td>
            <td>
              <b>所属机构：</b>
            </td>
            <td >
              {{ form.organName }}
            </td>
          </tr>
          <tr>
            <td>
              <b>平板人员名称：</b>
            </td>
            <td colspan="3">
              {{ form.name }}
            </td>
          </tr>
          <tr>
            <td>
              <b>是否启用平板评价：</b>
            </td>
            <td style="text-align: left">
              <template v-if="form.isAble===1">
                是
              </template>
              <template v-else>
                否
              </template>
            </td>
            <td>
              <b>是否启用平板确认：</b>
            </td>
            <td >
              <template v-if="form.isDelete===1">
                是
              </template>
              <template v-else>
                否
              </template>
            </td>
          </tr>
        </table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {getOne, page,del,able } from "@/api/zc/sysRunConfiguration/pbpjUser";
  import { validIntNoZero } from '@/utils/validate';
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  export default {
    components: { Treeselect },
    name: "PbpjUser",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 应用表格数据
        dictList: [],
        //查询平板人员名称参数
        dictName:'',
        // 弹出层标题
        title: "",
        // 新增/修改显示弹出层
        openInit: false,
        // 查看显示弹出层
        openView: false,
        //启禁用状态
        ableMap:{'Y':'启用','N':'禁用'},
        // 数字平板人员树选项
        dictOptions: undefined,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: '',
          runCode:'',
          parentOid:''
        },
        defaultProps: {
          children: "children",
          label: "label"
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          name: [
            { required: true, message: "平板人员名称不能为空", trigger: "blur" }
          ],
          runCode: [
            { required: true, message: "平板人员编码不能为空", trigger: "blur" }
          ],
          sort: [
            { required: true, message: "排序号不能为空", trigger: "blur" },
            {validator:validIntNoZero, trigger: 'blur'}
          ]
        },
      };
    },
    watch: {
      // 根据名称筛选部门树
      dictName(val) {
        this.$refs.tree.filter(val);
      }
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询数字平板人员列表 */
      getList() {
        this.loading = true;
        page(this.queryParams).then(response => {
          this.dictList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        });
      },

      // 筛选节点
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      // 节点单击事件
      handleNodeClick(data) {
        this.queryParams.parentOid = data.id;
        this.getList();
      },
      //新增一个节点
      append(node,newId,label) {
        const newChild = { id:newId, label: label, children: [] };
        if (!node.children) {
          this.$set(node, 'children', []);
        }
        node.children.push(newChild);
      },
      //更新一个节点
      update(node,oid,name){
        const children = node.children;
        const updateNode = children.find(d => d.id === oid);
        updateNode.label = name;
        this.$refs.tree.updateKeyChildren(updateNode.id,updateNode);
      },
      //移除节点
      remove(node, id) {
        const children = node.children || node;
        const index = children.findIndex(d => d.id === id);
        children.splice(index, 1);
      },
      // 启禁用状态
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
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 查看按钮操作 */
      handleView(row) {
        this.reset();
        const oid = row.userOid;
        getOne(oid).then(response => {
          this.form = response.data;
          this.openView = true;
          this.title = "查看平板人员信息";
        });
      },
      /** 新增和修改按钮操作 */
      handleInit(row) {
        this.reset();
        let _that = this;

        let oid = row.id;
        alert(row.id);
        if(row.id) {
          getOne(row.id).then(response => {
            _that.openInit = true;
            _that.form = response.data;

          });
        } else {
          _that.openInit = true;
        }
        _that.title = row.id ? "修改平板人员信息" : "新增平板人员信息";
      },
      /** 提交按钮 */
      submitForm: function() {
        let _that = this;
        _that.$refs["form"].validate(valid => {
          if (valid) {

            save(_that.form).then(response => {
              if (response.code === 200) {
                _that.msgSuccess("保存成功");
                _that.openInit = false;
                setTimeout(() => {
                  _that.getList();
                }, 10);

              }
            });
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const oid = row.oid;
        this.$confirm('是否确认删除?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return del(oid);
        }).then(() => {
          const node = this.$refs.tree.getCurrentNode();
          this.remove(node,oid);
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 启禁用按钮操作 */
      handleAble(row) {
        const oid = row.oid;
        let ableMessage = row.isAble === 'Y' ? '启用' : '禁用'
        this.$confirm('你确定要' + ableMessage + '吗？', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return able(oid);
        }).then(() => {
          this.msgSuccess(ableMessage+"成功");
        }).catch(function() {
          row.isAble = row.isAble === 'N' ? 'Y' : 'N'
        });
      },
      handleAppraise(row) {
        const oid = row.userOid;
        let ableMessage = row.isAble == '1' ? '禁用' : '启用'
        this.$confirm('你确定要' + ableMessage + '吗？', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return able(oid);
        }).then(() => {
          this.msgSuccess(ableMessage+"成功");
          this.getList();
        }).catch(function() {
          // row.isAble = row.isAble == '0' ? '1' : '0'
        });
      },
      handleConfirmFlag(row) {
        const oid = row.userOid;
        let ableMessage = row.isDelete == '0' ? '启用' : '禁用'
        this.$confirm('你确定要' + ableMessage + '吗？', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return del(oid);
        }).then(() => {
          this.msgSuccess(ableMessage+"成功");
          this.getList();
        }).catch(function() {
          // row.isDelete = row.isDelete == '0' ? '1' : '0'
        });
      }

    }
  };
</script>
<style lang="scss" scoped>
  .el-tree>.el-tree-node{
    min-width:100%;
    display: inline-block;
  }
</style>
