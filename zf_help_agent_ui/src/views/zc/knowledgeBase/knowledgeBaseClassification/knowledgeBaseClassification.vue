/**
* @Author: kkfan
* @Date: 2020-10-28 13:46:23
* @Last Modified by: kkfan
* @Last Modified time: 2020-10-31 10:41:52
* @Description: 知识库分类管理页面
*/
<template>
  <div class="app-container">
    <el-row :gutter="20" style="height: calc(100% - 32px - 30px)">
      <!--数字字段树数据-->
      <el-col :span="4" :xs="24" class="app-left" style="height: 100%">
        <div class="tree-title">知识库分类字典</div>
        <div class="head-container">
          <el-input
            v-model.trim="dictName"
            placeholder="请输入字典名称"
            clearable
            size="small"
            prefix-icon="iconfont zfsoft-chakan"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container" style="height: calc(100% - 52px - 35px)">
          <el-tree
            v-if="dictOptions.length > 0"
            :data="dictOptions"
            :props="defaultProps"
            :expand-on-click-node="true"
            :highlight-current="true"
            :filter-node-method="filterNode"
            ref="tree"
            accordion
            node-key="id"
            :default-expanded-keys="[1]"
            @node-click="handleNodeClick"
          >
            <template slot-scope="{ node, data }">
              <div
                style="
                  font-size: 14px;
                  width: 240px;
                  overflow: hidden;
                  white-space: nowrap;
                  text-overflow: ellipsis;
                "
                :title="node.label"
              >
                <span class="overflow-x: auto"> {{ node.label }}</span>
              </div>
            </template>
          </el-tree>
          <el-empty v-else style="height: 100%" />
        </div>
      </el-col>
      <!--数字字典数据-->
      <el-col :span="20" :xs="24" class="app-right" style="height: 100%">
        <el-form
          :model="queryParams"
          ref="queryForm"
          :inline="true"
          label-width="70px"
        >
          <el-form-item label="字典代码" prop="code">
            <el-input
              v-model.trim="queryParams.code"
              placeholder="请输入字典代码"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="字典名称" prop="name">
            <el-input
              v-model.trim="queryParams.name"
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
              v-hasPermi="['sys:knowleType:init']"
              >新增</el-button
            >
          </el-col>
        </el-row>

        <el-table
          v-loading="loading"
          :data="dictList"
          border
          :fit="true"
          height="calc(100% - 160px)"
        >
          <!--<el-table-column type="selection" width="55" align="center" />-->
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="字典代码" align="left" prop="code" />
          <el-table-column
            label="字典名称"
            align="left"
            prop="name"
            :show-overflow-tooltip="true"
          />
          <!--<el-table-column label="创建人" width="150" align="left" prop="createUserName" />-->
          <el-table-column
            label="备注"
            align="left"
            prop="remark"
            :show-overflow-tooltip="true"
          />
          <!--<el-table-column label="修改时间"  width="180"  align="center" prop="modifyDate" />-->
          <el-table-column label="创建时间" align="center" prop="createDate" />
          <el-table-column
            label="操作"
            width="175"
            align="center"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-chakan"
                @click="handleView(scope.row)"
                v-hasPermi="['sys:knowleType:view']"
                >查看</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="handleInit(scope.row)"
                v-hasPermi="['sys:knowleType:update']"
                >修改</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-shanchu"
                @click="handleDelete(scope.row)"
                v-hasPermi="['sys:knowleType:delete']"
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
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
    </el-row>
    <!-- 添加或修改数字字典信息对话框 -->

    <el-dialog
    v-dialog-drag
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="addDialogShow"
      v-if="addDialogShow"
      width="900px"
      append-to-body
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="0"
        class="dialog-table"
      >
        <table
          cellspacing="0"
          cellpadding="0"
          border="0"
          class="zf-zc-table"
        >
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b>上级字典名称：</b>
            </td>
            <td>
              <el-form-item prop="parentName">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.parentName"
                    placeholder=""
                    :disabled="true"
                    maxlength="50"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>字典名称：</b></td>
            <td>
              <el-form-item prop="name">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.name"
                    placeholder="请输入字典名称"
                    maxlength="50"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>字典代码：</b></td>
            <td>
              <el-form-item prop="code">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.code"
                    placeholder="请输入字典代码"
                    maxlength="32"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>排序号：</b></td>
            <td>
              <el-form-item prop="sort">
                <el-col :span="24">
                  <el-input-number v-model="form.sort" :min="1" :max="9999" />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>备注：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="remark">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.remark"
                    type="textarea"
                    maxlength="100"
                    show-word-limit
                    placeholder="请输入备注"
                  ></el-input>
                </el-col>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 字典信息详细 -->
    <el-dialog
    v-dialog-drag
      :title="title"
      :visible.sync="openView"
      width="800px"
      v-if="openView"
      append-to-body
    >

        <table
          cellspacing="0"
          cellpadding="0"
          border="0"
          class="zf-zc-table"
        >
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b>上级字典名称：</b>
            </td>
            <td>
       
                <el-col :span="24">
                  {{ currentNode.name }}
                </el-col>
      
            </td>
            <td>
              <b>字典名称：</b>
            </td>
            <td>
    
                <el-col :span="24">
                  {{ form.name }}
                </el-col>
      
            </td>
          </tr>
          <tr>
            <td>
              <b>字典代码：</b>
            </td>
            <td>
      
                <el-col :span="24">
                  {{ form.code }}
                </el-col>
      
            </td>
            <td>
              <b>排序号：</b>
            </td>
            <td>
          
                <el-col :span="24">
                  {{ form.sort }}
                </el-col>
          
            </td>
          </tr>
          <tr>
            <td>
              <b>备注：</b>
            </td>
            <td colspan="3">
         
                <el-col :span="24">
                  {{ form.remark }}
                </el-col>
           
            </td>
          </tr>
        </table>
   
      <div slot="footer" class="zf-text-center">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  page,
  queryTree,
  checkisExist,
  getOne,
  saveOrUpdate,
  validateRepeat,
  deletes
} from "@/api/zc/knowledgeBase/knowledgeBaseClassification";
import {
  validIntNoZero
} from '@/utils/validate';
import {
  deepClone
} from "@/utils/index";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  components: {
    Treeselect
  },
  name: "KnowledgeBaseClassification",
  data () {
    return {
      // 当前选中节点
      currentNode: {},
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      dictList: [],
      //查询字典名称参数
      dictName: '',
      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      addDialogShow: false,
      // 查看显示弹出层
      openView: false,
      //启禁用状态
      ableMap: {
        'Y': '启用',
        'N': '禁用'
      },
      // 数字字典树选项
      dictOptions: undefined,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: '',
        code: '',
        parentOid: ''
      },
      defaultProps: {
        children: "childrenList",
        label: "name"
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [{
          required: true,
          message: "字典名称不能为空",
          trigger: "blur"
        }],
        code: [{
          required: true,
          message: "字典代码不能为空",
          trigger: "blur"
        }],
        sort: [{
          required: true,
          message: "排序号不能为空",
          trigger: "change"
        },
        {
          validator: validIntNoZero,
          trigger: 'blur'
        }
        ]
      },
    };
  },
  methods: {
    /** 查询数字字典列表 */
    getList () {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.dictList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 获取数字字典树 */
    getTree (parentOid, callback) {
      queryTree(parentOid).then(response => {
        this.dictOptions = response.data;
        callback && callback(response.data);
      });
    },
    // 筛选节点
    filterNode (value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    // 节点单击事件
    handleNodeClick (data) {
      this.currentNode = data;
      this.queryParams.parentOid = data.zskDictOid;
      this.getList();
    },
    //新增一个节点
    append (node, newId, label) {
      const newChild = {
        id: newId,
        name: label,
        childrenList: []
      };
      if (!node.children) {
        this.$set(node, 'children', []);
      }
      node.childrenList.push(newChild);
    },
    //更新一个节点
    update (node, id, name) {
      const childrenList = node.childrenList;
      const updateNode = childrenList.find(d => d.id === id);
      updateNode.name = name;
      this.$refs.tree.updateKeyChildren(updateNode.id, updateNode);
    },
    //移除节点
    remove (node, id) {
      const childrenList = node.childrenList || node;
      const index = childrenList.findIndex(d => d.id === id);
      childrenList.splice(index, 1);
    },
    // 启禁用状态
    statusFormat (row) {
      return this.selectMapLabel(this.ableMap, row.isAble);
    },
    // 取消按钮
    cancel () {
      this.addDialogShow = false;
      this.reset();
    },
    // 表单重置
    reset () {
      this.form = {
        name: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 查看按钮操作 */
    handleView (row) {
      this.reset();
      const id = row.id;
      getOne(id).then(response => {
        this.form = response.data;
        this.openView = true;
        this.title = "查看知识库分类信息";
      });
    },
    /** 新增和修改按钮操作 */
    handleInit (row) {
      let _that = this;
      _that.reset();
      if (row.id) {
        getOne(row.id).then(response => {
          _that.addDialogShow = true;
          _that.form = deepClone(response.data);
          _that.form.parentName = _that.currentNode.name;
        });
      } else {
        _that.addDialogShow = true;
      }
      _that.form.parentName = _that.currentNode ? _that.currentNode.name : null;
      _that.form.parentOid = _that.currentNode ? _that.currentNode.zskDictOid : null;
      _that.dialogTitle = row.id ? "修改" : "新增";
      this.title = row.id ? "修改知识库分类信息" : "新增知识库分类信息";
    },
    /** 提交按钮 */
    submitForm: function () {
      let _that = this;
      _that.$refs["form"].validate(valid => {
        if (valid) {
          if (_that.form.code.trim() === '' || _that.form.name.trim() === '') {
            this.msgSuccess("提交的字段存在只输入了空格，请重新输入");
          } else {
            //验证是否字典名称或者代码是否重复
            validateRepeat(_that.form.zskDictOid, _that.form.code, _that.form.name, _that.form.parentOid).then(response => {
              if (response.data) {
                this.msgWarning(response.data);
              } else {
                saveOrUpdate(_that.form).then(response => {
                  if (response.code === 200) {
                    _that.msgSuccess("保存成功");
                    _that.addDialogShow = false;
                    if (_that.form.id == undefined || null == _that.form.id) {
                      //新增一个节点
                      const node = _that.$refs.tree.getCurrentNode();
                      _that.append(node, response.data.id, response.data.name);
                    } else {
                      //更新一个节点
                      const node = _that.$refs.tree.getCurrentNode();
                      if (node) {
                        _that.update(node, response.data.id, response.data.name);
                      }
                    }
                    _that.getList();
                  }
                });
              }
            });
          }

        }
      });
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      const id = row.id;
      checkisExist(row.zskDictOid).then(response => {
        if (response.data == 'N') {
          this.$confirm('是否确认删除?', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function () {
            return deletes(id);
          }).then(() => {
            const node = this.$refs.tree.getCurrentNode();
            this.remove(node, id);
            this.getList();
            this.msgSuccess("删除成功");
          }).catch(function () { });
        } else {
          this.msgSuccess("存在相关联的知识库分类不可删除");
        }
      });
    },
  },
  watch: {
    // 根据名称筛选部门树
    dictName (val) {
      this.$refs.tree.filter(val);
    }
  },
  created () {
    let _that = this;
    _that.getTree(null, (data) => {
      if (!data) {
        return;
      }
      _that.$nextTick(() => {
        document.querySelector('.el-tree-node__content').click()
      })
    });
  }
};
</script>
<style lang="scss" scoped>
.el-tree > .el-tree-node {
  min-width: 100%;
  display: inline-block;
}

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
  text-align: left;
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

>>> .el-input-number--medium {
  line-height: 30px;
}
</style>
