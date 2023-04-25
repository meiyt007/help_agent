/**
* @Author: liangxm
* @Date: 2020-10-26 18:21:27
* @Last Modified by: liangxm
* @Last Modified time: 2020-10-27 10:54:27
* @Description: 平板设备管理
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--数字字段树数据-->

      <!--数字平板设备数据-->
      <el-col :span="24" :xs="24" class="app-right">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="108px">
          <el-form-item label="平板设备代码" prop="runCode">
            <el-input
              v-model.trim="queryParams.runCode"
              placeholder="请输入平板设备代码"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="平板设备名称" prop="name">
            <el-input
              v-model.trim="queryParams.name"
              placeholder="请输入平板设备名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8 fl">
          <el-col :span="1.5">
            <el-button
              type="default"
              icon="el-icon-plus"
              size="mini"
              @click="handleInit"
              v-hasPermi="['sys:manage:init']"
            >新增</el-button>
          </el-col>

        </el-row>

        <el-table  v-loading="loading" :data="dictList" border :fit="true">
          <!--<el-table-column type="selection" width="55" align="center" />-->
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column label="平板设备代码"   align="left" prop="runCode" :show-overflow-tooltip="true"/>
          <el-table-column label="平板设备名称"   align="left" prop="name" :show-overflow-tooltip="true"/>
<!--          <el-table-column label="平板设备类型" width="250"  align="left" prop="pbpjType" >
            <template slot-scope="scope">
              <span v-if="scope.row.pbpjType == 0">平板平板</span>
              <span v-if="scope.row.pbpjType == 1">智能平板</span>
            </template>
          </el-table-column>-->

          <!--<el-table-column label="启用状态" align="left"  width="100" prop="status" :formatter="statusFormat"/>-->
          <el-table-column label="启用状态" align="center"   prop="status" >
            <!--<template slot-scope="scope">
              <el-switch
                v-model="scope.row.status"
                active-value="1"
                inactive-value="0"
                @change="handleAble(scope.row)"
              ></el-switch>
            </template>-->
            <template slot-scope="scope">
              <el-switch :value="scope.row.status == 1" @change="handleAble(scope.row)"></el-switch>
            </template>
          </el-table-column>
          <el-table-column label="创建时间"    align="left" prop="dateTime" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-chakan"
                @click="handleView(scope.row)"
                v-hasPermi="['sys:pbpjManage:view']"
              >查看</el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="handleInit(scope.row)"
                v-hasPermi="['sys:pbpjManage:update']"
              >修改</el-button>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-shanchu"
                @click="handleDelete(scope.row)"
                v-hasPermi="['sys:pbpjManage:delete']"
              >删除</el-button>
              <!--<el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-jinyong"
                @click="handleAble(scope.row)"
                v-hasPermi="['sys:pbpjManage:able']"
              >启禁用</el-button>-->
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
    <!-- 添加或修改平板设备信息对话框 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="title" :visible.sync="openInit" width="900px" append-to-body  class="el-dialog-type">
      <div class="dia-content" style="width:100%; min-height:300px; max-height:700px; overflow-y:scroll; ">
      <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <el-input v-show="false" v-model="form.id" />
        <el-input v-show="false" v-model="form.status" />

        <el-input v-show="false" v-model="form.runCode" />
        <el-input v-show="false" v-model="form.isDelete" />
        <el-input v-show="false" v-model="form.dateTime" />
        <el-input v-show="false" v-model="form.pbpjType" />
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><i class="require">*</i>
              <b>平板设备名称：</b>
            </td>
            <td colspan="3">

              <el-row>

                <el-col :span="24">
                  <el-form-item  prop="name">
                    <el-input v-model.trim="form.name" placeholder="请输入平板设备名称" maxlength="50" show-word-limit/>
                  </el-form-item>
                </el-col>
              </el-row>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i>
              <b>平板设备代码：</b>
            </td>
            <td colspan="3">

              <el-col :span="24">
                <el-form-item  prop="runCode">
                  <el-input v-model.trim="form.runCode" placeholder="请输入平板设备代码" maxlength="50" show-word-limit/>
                </el-form-item>
              </el-col>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i>
              <b>平板设备ip：</b>
            </td>
            <td colspan="3">

              <el-row>
                <el-col :span="24">
                  <el-form-item  prop="ip">
                    <el-input v-model.trim="form.ip" placeholder="请输入平板ip" maxlength="50" show-word-limit/>
                  </el-form-item>
                </el-col>
              </el-row>
            </td>
          </tr>
          <tr>
            <td>
              <b>备注：</b>
            </td>
            <td colspan="3">

              <el-col :span="24">
                <el-form-item  prop="remark">
                  <el-input v-model.trim="form.remark" type="textarea" maxlength="500" show-word-limit placeholder="请输入备注"></el-input>
                </el-form-item>
              </el-col>
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


    <!-- 平板设备信息详细 -->
    <el-dialog :title="title" :visible.sync="openView" width="800px" append-to-body>
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
              <b>平板设备名称：</b>
            </td>
            <td>
              {{ form.name }}
            </td>
            <td>
              <b>平板设备代码：</b>
            </td>
            <td>
              {{ form.runCode }}
            </td>
          </tr>
          <tr>
            <td>
              <b>平板设备ip：</b>
            </td>
            <td colspan="3">
              {{ form.ip }}
            </td>

          </tr>
          <tr>
            <td>
              <b>备注：</b>
            </td>
            <td colspan="3">
              {{ form.remark }}
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
  import { save, del ,getOne, page,able } from "@/api/zc/sysRunConfiguration/pbpjManage";
  import { queryDistrictSimpleTree } from "@/api/sys/district";
  import { queryOrganTree } from "@/api/sys/organ";
  import {validIntNoZero, validIpAddressNot} from '@/utils/validate';
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  export default {
    components: { Treeselect },
    name: "PbpjManage",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 应用表格数据
        dictList: [],
        //查询平板设备名称参数
        dictName:'',
        // 弹出层标题
        title: "",
        // 新增/修改显示弹出层
        openInit: false,
        // 查看显示弹出层
        openView: false,
        //启禁用状态
        ableMap:{'Y':'启用','N':'禁用'},
        // 平板设备设备树选项
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
            { required: true, message: "平板设备名称不能为空", trigger: "blur" }
          ],
          runCode: [
            { required: true, message: "平板设备编码不能为空", trigger: "blur" }
          ],
          ip: [
            {required: true, message: "平板设备ip不能为空", validator: validIpAddressNot, trigger: 'blur'}
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
      /** 查询数字平板设备列表 */
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
        return this.selectMapLabel(this.ableMap, row.status);
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
        let _that = this;
        _that.reset();
        const oid = row.id;
        getOne(oid).then(response => {
          _that.form = response.data;
          _that.openView = true;

          _that.title = "查看平板设备信息";
        });
      },
      /** 新增和修改按钮操作 */
      handleInit(row) {
        this.reset();
        let _that = this;

        let oid = row.id;
        if(row.id) {
          getOne(row.id).then(response => {
            _that.openInit = true;
            _that.form = response.data;

          });
        } else {
          _that.openInit = true;
        }
        _that.title = row.id ? "修改平板设备信息" : "新增平板设备信息";
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
        const oid = row.id;
        this.$confirm("是否确认删除?", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(function() {
            return del(oid);
          })
          .then(() => {
            const node = this.$refs.tree.getCurrentNode();
            this.remove(node, oid);
            this.getList();
            this.msgSuccess("删除成功");
          })
          .catch(function() {});
      },
      /** 启禁用按钮操作 */
      handleAble(row) {
        const oid = row.id;
        let ableMessage = row.status == '0' ? '启用' : '禁用'
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
          row.status = row.status == '0' ? '1' : '0'
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
