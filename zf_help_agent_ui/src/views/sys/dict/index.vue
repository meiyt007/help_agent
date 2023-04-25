<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--数字字段树数据-->
      <el-col :span="4" :xs="24" class="app-left" style="padding-right:0px">
        <div class="tree-title">数据字典管理</div>
        <div class="head-container">
          <el-input
            v-model="dictName"
            placeholder="请输入字典名称"
            clearable
            size="small"
            prefix-icon="iconfont zfsoft-chakan"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="dictOptions"
            :props="defaultProps"
            :expand-on-click-node="true"
            :highlight-current="true"
            :filter-node-method="filterNode"
            ref="tree"
            accordion
            node-key="id"
            :default-expanded-keys="ArrData"
            @node-click="handleNodeClick"
          >
            <span
              class="custom-tree-node tree-ellipsis"
              slot-scope="{ node, data }"
            >
              <el-tooltip
                v-if="node.label.length > 8"
                class="item"
                effect="dark"
                :content="node.label"
                placement="right-start"
              >
                <span style="font-size: 14px"
                  >{{ node.label | ellipsis(8) }}
                </span>
              </el-tooltip>
              <span v-if="node.label.length < 9" style="font-size: 14px"
                >{{ node.label }}
              </span>
            </span>
          </el-tree>
        </div>
      </el-col>
      <!--数字字典数据-->
      <el-col :span="20" :xs="24" class="app-right">
        <el-form
          :model="queryParams"
          ref="queryForm"
          :inline="true"
          label-width="68px"
        >
          <el-form-item label="字典编码" prop="code">
            <el-input
              v-model="queryParams.code"
              placeholder="请输入字典编码"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="字典名称" prop="name">
            <el-input
              v-model="queryParams.name"
              placeholder="请输入字典名称"
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
              >重置</el-button
            >
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8 fl">
          <el-col :span="1.5">
            <el-button
              type="default"
              icon="el-icon-plus"
              size="mini"
              @click="handleInit"
              v-hasPermi="['sys:dict:init']"
              >新增</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="default"
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              v-hasPermi="['sys:dict:listExp']"
              >导出</el-button
            >
          </el-col>
        </el-row>

        <el-table v-loading="loading" :data="dictList" border>
          <!--<el-table-column type="selection" width="55" align="center" />-->
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="字典代码"
            width="180"
            align="center"
            prop="code"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="字典名称"
            align="center"
            prop="name"
            :show-overflow-tooltip="true"
          />
          <!--<el-table-column label="启用状态" align="left"  width="100" prop="isAble" :formatter="statusFormat"/>-->
          <el-table-column
            label="启用状态"
            width="100"
            prop="isAble"
            align="center"
          >
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.isAble"
                :active-value="1"
                :inactive-value="0"
                @change="handleAble(scope.row)"
              ></el-switch>
            </template>
          </el-table-column>
          <el-table-column
            label="创建时间"
            width="180"
            align="left"
            prop="createDate"
          />
          <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-chakan"
                @click="handleView(scope.row)"
                v-hasPermi="['sys:dict:view']"
                >查看</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="handleInit(scope.row)"
                v-hasPermi="['sys:dict:update']"
                >修改</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-shanchu"
                @click="handleDelete(scope.row)"
                v-hasPermi="['sys:dict:delete']"
                >删除</el-button
              >
              <!--<el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-jinyong"
                @click="handleAble(scope.row)"
                v-hasPermi="['sys:dict:able']"
              >启禁用</el-button>-->
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
    <!-- 添加或修改数字字典信息对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openInit"
      width="70%"
      append-to-body
      height="600px"
      scrollbar
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <el-input v-show="false" v-model="form.id" />
        <el-input v-show="false" v-model="form.dictOid" />
        <el-input v-show="false" v-model="form.isAble" />
        <el-input v-show="false" v-model="form.parentOid" />
        <el-input v-show="false" v-model="form.createUserOid" />
        <el-input v-show="false" v-model="form.isDelete" />
        <el-input v-show="false" v-model="form.createDate" />
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="80%" />
          </colgroup>
          <tr>
            <td><b>上级字典名称：</b></td>
            <td>
              <el-form-item>
                <span v-if="'' == form.parentName">上级字典名称</span>
                <span v-if="form.parentName">{{ form.parentName }}</span>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>字典名称：</b></td>
            <td>
              <el-form-item prop="name">
                <el-input
                  v-model.trim="form.name"
                  placeholder="请输入字典名称"
                  maxlength="50"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>字典代码：</b></td>
            <td>
              <el-form-item prop="code">
                <el-input
                  v-model.trim="form.code"
                  placeholder="请输入字典代码"
                  maxlength="50"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>排序号：</b></td>
            <td>
              <el-form-item prop="sort">
                <el-input-number v-model="form.sort" :min="1" :max="9999" />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><b>备注：</b></td>
            <td>
              <el-form-item prop="memo">
                <el-input
                  v-model.trim="form.memo"
                  type="textarea"
                  maxlength="500"
                  show-word-limit
                  placeholder="请输入备注"
                ></el-input>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看数字字典信息详细 -->
    <el-dialog
      :title="title"
      :visible.sync="openView"
      width="70%"
      append-to-body
    >
      <div>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><b>上级字典名称：</b></td>
            <td>
              {{ form.parentName }}
            </td>
            <td><b>字典名称：</b></td>
            <td>
              {{ form.name }}
            </td>
          </tr>
          <tr>
            <td><b>字典代码：</b></td>
            <td>
              {{ form.code }}
            </td>
            <td><b>启用状态：</b></td>
            <td
              v-for="(value, key) in ableMap"
              :key="key"
              v-if="form.isAble == key"
            >
              {{ value }}
            </td>
          </tr>
          <tr>
            <td><b>排序号：</b></td>
            <td colspan="3">
              {{ form.sort }}
            </td>
          </tr>
          <tr>
            <td><b>备注：</b></td>
            <td colspan="3">
              {{ form.memo }}
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
import {
  init,
  save,
  del,
  getOne,
  page,
  able,
  listExp,
  queryDictTree
} from "@/api/sys/dict";
import { validIntNoZero } from "@/utils/validate";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  components: { Treeselect },
  name: "Dict",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      dictList: [],
      //查询字典名称参数
      dictName: "",
      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      //消息
      ableMap: { "1": "启用", "0": "禁用" },
      // 数字字典树选项
      dictOptions: undefined,
      ArrData: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: "",
        code: "",
        parentOid: ""
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
          { required: true, message: "字典名称不能为空", trigger: "blur" }
        ],
        code: [
          { required: true, message: "字典代码不能为空", trigger: "blur" }
        ],
        sort: [
          { required: true, message: "排序号不能为空", trigger: "blur" },
          { validator: validIntNoZero, trigger: "blur" }
        ]
      }
    };
  },
  watch: {
    // 根据名称筛选部门树
    dictName(val) {
      if (null != val) {
        val = val.replace(/^\s+|\s+$/g, "");
      }
      this.$refs.tree.filter(val);
    }
  },
  created() {
    this.getDictTree();
    //this.getList();
  },
  methods: {
    /** 查询数字字典列表 */
    getList() {
      this.loading = true;
      let that = this;
      page(this.queryParams)
        .then(response => {
          this.dictList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        })
        .catch(function() {
          that.loading = false;
        });
    },
    /** 获取数字字典树 */
    getDictTree(parentOid) {
      queryDictTree(parentOid)
        .then(response => {
          this.dictOptions = response.data;
          this.defaultFrist(response.data);
        })
        .then(res => {
          let id = "";
          if (!this.queryParams.parentOid) {
            id = this.dictOptions[0].id;
            this.$nextTick(function() {
              this.$refs.tree.setCurrentKey(id);
              this.queryParams.parentOid = id;
              this.getList();
            });
          }
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
    append(node, newId, label) {
      const newChild = { id: newId, label: label, children: [] };
      if (!node.children) {
        this.$set(node, "children", []);
      }
      node.children.push(newChild);
    },
    //更新一个节点
    update(node, oid, name) {
      const children = node.children;
      const updateNode = children.find(d => d.id == oid);
      updateNode.label = name;
      this.$refs.tree.updateKeyChildren(updateNode.id, updateNode);
    },
    //移除节点
    remove(node, id) {
      const children = node.children || node;
      const index = children.findIndex(d => d.id == id);
      children.splice(index, 1);
    },
    // 消息
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
      const id = row.id;
      getOne(id).then(response => {
        this.form = response.data;
        this.openView = true;
        this.title = "查看字典信息";
      });
    },
    /** 新增和修改按钮操作 */
    handleInit(row) {
      this.reset();
      let id = row.id;
      init(id, this.queryParams.parentOid).then(response => {
        if (response.data.sysDict != undefined) {
          this.form = response.data.sysDict;
        } else {
          this.form.parentOid = this.queryParams.parentOid;
        }
        if (response.data.parentSysDict != undefined) {
          this.form.parentName = response.data.parentSysDict.name;
        }
        this.openInit = true;
        if (id == undefined || id == null) {
          this.title = "新增数据字典信息";
        } else {
          this.title = "修改数据字典信息";
        }
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          save(this.form).then(response => {
            if (response.code === 200) {
              this.msgSuccess("保存成功");
              this.openInit = false;
              if (this.form.dictOid == undefined || null == this.form.dictOid) {
                //新增一个节点
                const node = this.$refs.tree.getCurrentNode();
                this.append(node, response.data.dictOid, response.data.name);
              } else {
                //更新一个节点
                const node = this.$refs.tree.getCurrentNode();
                if (node) {
                  this.update(node, response.data.dictOid, response.data.name);
                }
              }
              this.getList();
            }
          });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const id = row.id;
      const dictOid = row.dictOid;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return del(id, dictOid);
        })
        .then(() => {
          const node = this.$refs.tree.getCurrentNode();
          this.remove(node, id);
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function() {});
    },
    /** 启禁用按钮操作 */
    handleAble(row) {
      const id = row.id;
      let ableMessage = row.isAble === 1 ? "启用" : "禁用";
      this.$confirm("你确定要" + ableMessage + "吗？", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return able(id);
        })
        .then(() => {
          this.msgSuccess(ableMessage + "成功");
        })
        .catch(function() {
          row.isAble = row.isAble === 0 ? 1 : 0;
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否确认导出列表数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          listExp(queryParams);
        })
        .catch(function() {});
    },

    //默认展开第一级
    defaultFrist(treeData) {
      var arr = [];
      for (let i = 0; i < treeData.length; i++) {
        if (
          "undefined" != typeof treeData[i].children &&
          treeData[i].children.length > 0
        ) {
          arr.push(treeData[i].id);
        }
        this.ArrData = arr;
      }
    }
  }
};
</script>
<style scoped>
.el-tree > .el-tree-node {
  width: 100%;
  display: inline-block;
}
.tree-ellipsis {
  width: 100% !important;
  overflow: hidden !important;
  text-overflow: ellipsis !important;
  white-space: nowrap !important;
}
</style>
