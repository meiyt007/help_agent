/** * @Author: wangxl */
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--分类树数据-->
      <el-col :span="4" :xs="24" class="app-left">
        <div class="tree-title">分类管理</div>
        <div class="head-container">
          <el-input
            v-model.trim="themeName"
            placeholder="请输入分类名称"
            clearable
            size="small"
            prefix-icon="iconfont zfsoft-chakan"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="themeOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :highlight-current="true"
            :filter-node-method="filterNode"
            ref="tree"
            node-key="id"
            :default-expanded-keys="[1]"
            default-expand-all
            @node-click="handleNodeClick"
          >
            <span class="span-ellipsis" slot-scope="{ node }">
              <span :title="node.label">{{ node.label }}</span>
            </span>
          </el-tree>
        </div>
      </el-col>
      <!--分类列表数据-->
      <el-col :span="20" :xs="24" class="app-right">
        <el-form
          :model="queryParams"
          ref="queryForm"
          :inline="true"
          label-width="68px"
          @submit.native.prevent
        >
          <el-form-item label="分类名称" prop="themeName">
            <el-input
              v-model.trim="queryParams.themeName"
              placeholder="请输入分类名称"
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

        <el-row :gutter="10" class="mb8 fl">
          <el-col :span="1.5">
            <el-button
              type="default"
              icon="el-icon-plus"
              size="mini"
              @click="handleInit"
              v-hasPermi="['combo:theme:init']"
              >新增</el-button
            >
          </el-col>
          <!-- <el-col :span="1.5">
            <el-button
              type="default"
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              v-hasPermi="['combo:theme:listExp']"
            >导出</el-button>
          </el-col>-->
        </el-row>

        <el-table v-loading="loading" :data="comboThemeList" border :fit="true">
          <!--<el-table-column type="selection" width="55" align="center" />-->
          <el-table-column label="序号" width="55" type="index" align="center">
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
          <el-table-column label="分类图标" align="center" prop="themeIcon">
            <template slot-scope="scope">
              <i :class="scope.row.themeIcon" />
            </template>
          </el-table-column>
          <el-table-column
            label="排序号"
            align="center"
            prop="themeSort"
            :show-overflow-tooltip="true"
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
                v-hasPermi="['combo:theme:view']"
                >查看</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="handleInit(scope.row)"
                v-hasPermi="['combo:theme:init']"
                >修改</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-shanchu"
                @click="handleDelete(scope.row)"
                v-hasPermi="['combo:theme:delete']"
                >删除</el-button
              >
              <!--<el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-jinyong"
                @click="handleAble(scope.row)"
                v-hasPermi="['sys:district:able']"
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
    <!-- 添加或修改分类信息对话框 -->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :title="dialogTitle"
      :visible.sync="openInit"
      width="1100px"
      append-to-body
    >
      <div>
        <el-form ref="form" :model="form" :rules="rules" label-width="0">
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <el-input v-show="false" v-model="form.themeOid" />
            <el-input v-show="false" v-model="form.themeParentOid" />
            <el-input v-show="false" v-model="form.delFlag" />
            <el-input v-show="false" v-model="form.path" />
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td>
                <b>上级分类名称：</b>
              </td>
              <td>
                <span>{{ form.parentName }}</span>
              </td>
              <td><i class="require">*</i><b>分类名称：</b></td>
              <td>
                <el-form-item prop="themeName">
                  <el-input
                    v-model.trim="form.themeName"
                    placeholder="请输入分类名称"
                    maxlength="30"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>分类代码：</b></td>
              <td>
                <el-form-item prop="themeCode">
                  <el-input
                    v-model.trim="form.themeCode"
                    placeholder="请输入分类代码"
                    maxlength="32"
                    show-word-limit
                  />
                </el-form-item>
              </td>
              <td><i class="require">*</i><b>分类图标：</b></td>
              <td>
                <el-form-item prop="themeIcon">
                  <el-input v-show="false" v-model="form.themeIcon" />
                  <i style="font-size: 30px;" :class="form.themeIcon" />
                  <el-button
                    type="primary"
                    size="mini"
                    @click="handleIconSelect"
                    >选择图标
                  </el-button>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>排序号：</b></td>
              <td colspan="3">
                <el-form-item prop="themeSort">
                  <el-input-number
                    v-model="form.themeSort"
                    :min="1"
                    :max="9999"
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>备注：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="themeRemark">
                  <el-input
                    v-model.trim="form.themeRemark"
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
      </div>
      <div slot="footer" style="text-align:center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>

      <!--引入图标选择弹出层-->
      <el-dialog
        title="选择按钮图标"
        v-dialog-drag
        :visible.sync="openIconSelectView"
        :close-on-click-modal="false"
        width="1100px"
        height="700px"
        scrollbar
        append-to-body
      >
        <iconfont @father-click="getSelectClass"></iconfont>
        <div slot="footer" style="text-align:center">
          <el-button @click="openIconSelectView = false">关 闭</el-button>
        </div>
      </el-dialog>
    </el-dialog>

    <!-- 分类信息详细 -->
    <el-dialog
      v-dialog-drag
      :title="title"
      :visible.sync="openView"
      width="1100px"
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
            <td>
              <b>上级分类名称：</b>
            </td>
            <td>
              {{ form.themeParentName }}
            </td>
            <td>
              <b>分类名称：</b>
            </td>
            <td>
              {{ form.themeName }}
            </td>
          </tr>
          <tr>
            <td>
              <b>分类代码：</b>
            </td>
            <td>
              {{ form.themeCode }}
            </td>
            <td>
              <b>分类图标：</b>
            </td>
            <td>
              <i style="font-size: 30px;" :class="form.themeIcon" />
            </td>
          </tr>
          <tr>
            <td>
              <b>排序号：</b>
            </td>
            <td colspan="3">
              {{ form.themeSort }}
            </td>
          </tr>
          <tr>
            <td>
              <b>备注：</b>
            </td>
            <td colspan="3">
              {{ form.themeRemark }}
            </td>
          </tr>
        </table>
      </div>
      <div slot="footer" style="text-align:center">
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
  queryComboThemeSimpleTree,
  getCountByThemeOid,
  getComboDireCountByParentThemeOid
} from "@/api/onething/sxpz/comboTheme";
import { validateNumber, validIntNoZero } from "@/utils/validate";
import Treeselect from "@riophae/vue-treeselect";
import iconfont from "@/views/common/iconfontSelect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  components: { Treeselect, iconfont },
  name: "ComboTheme",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      comboThemeList: [],
      //查询分类名称参数
      themeName: "",
      // 弹出层标题
      title: "",
      dialogTitle: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      //图标样式弹出层
      openIconSelectView: false,
      //启禁用状态
      //ableMap:{'Y':'启用','N':'禁用'},
      // 区划树选项
      themeOptions: undefined,
      // 级别字典
      //levelDictOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: "",
        themeParentOid: ""
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        themeName: [
          { required: true, message: "分类名称不能为空", trigger: "blur" }
        ],
        themeCode: [
          { required: true, message: "分类代码不能为空", trigger: "blur" }
        ],
        /*themeIcon: [
          { validator: validatePass ,trigger: "blur" }
        ],*/
        themeSort: [
          { required: true, message: "排序号不能为空", trigger: "blur" },
          { validator: validIntNoZero, trigger: "blur" }
        ]
      }
    };
    /*var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('分类图标不能为空'));
      } else {
        if (this.form.themeIcon !== '') {
          this.$refs.form.validateField('form.themeIcon');
        }
        callback();
      }
    };*/
  },
  watch: {
    // 根据名称筛选树
    themeName(val) {
      this.$refs.tree.filter(val);
    }
  },
  created() {
    this.getList();
    this.getThemeTree();
  },
  methods: {
    /** 查询分类列表 */
    getList() {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.comboThemeList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 获取分类树 */
    getThemeTree(themeOid) {
      queryComboThemeSimpleTree(themeOid).then(response => {
        this.themeOptions = response.data;
      });
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.themeParentOid = data.id;
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
      const updateNode = children.find(d => d.id === oid);
      updateNode.label = name;
      this.$refs.tree.updateKeyChildren(updateNode.id, updateNode);
    },
    //移除节点
    remove(node, id) {
      const children = node.children || node;
      const index = children.findIndex(d => d.id === id);
      children.splice(index, 1);
    },
    /** 选择图标 */
    handleIconSelect() {
      this.openIconSelectView = true;
    },
    //从子组件中获取选择的图标
    getSelectClass(className) {
      this.form.themeIcon = className;
      this.openIconSelectView = false;
      this.$forceUpdate();
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
      const oid = row.themeOid;
      getOne(oid).then(response => {
        this.form = response.data;
        this.openView = true;
        this.title = "查看分类信息";
      });
    },
    /** 新增和修改按钮操作 */
    handleInit(row) {
      const node = this.$refs.tree.getCurrentNode();
      if (null != node && node.parentId != 0) {
        //只能加到二级
        this.msgWarning("请选择一级分类！");
        return false;
      }
      let _that = this;
      _that.reset();
      let oid = row.themeOid;
      if (oid) {
        getCountByThemeOid(oid).then(response => {
          if (response.data > 0) {
            this.msgWarning("当前分类被目录引用，不能修改操作！");
            return false;
          } else {
            this.init(oid)
          }
        });
      } else {
        this.init(oid)
      }
    },
    init(oid) {
      init(oid, this.queryParams.themeParentOid).then(response => {
        if (response.data.comboTheme != undefined) {
          this.form = response.data.comboTheme;
        } else {
          this.form.themeParentOid = this.queryParams.themeParentOid;
        }
        if (response.data.parentComboTheme != undefined) {
          this.form.parentName = response.data.parentComboTheme.themeName;
        }
        this.openInit = true;
        // this.title = "新增或者修改字典信息";
        this.dialogTitle = oid?"修改":"新增";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (!this.form.themeIcon) {
            this.$message.error("分类图标不能为空！");
            return false;
          }
          save(this.form).then(response => {
            if (response.code === 200) {
              this.msgSuccess("保存成功");
              this.openInit = false;
              if (this.form.id == undefined || null == this.form.id) {
                //新增一个节点
                const node = this.$refs.tree.getCurrentNode();
                if (null == node) {
                  this.getThemeTree();
                } else {
                  this.append(
                    node,
                    response.data.themeOid,
                    response.data.themeName
                  );
                }
              } else {
                //更新一个节点
                const node = this.$refs.tree.getCurrentNode();
                if (node) {
                  this.update(
                    node,
                    response.data.themeOid,
                    response.data.themeName
                  );
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
      const oid = row.id;
      getComboDireCountByParentThemeOid(row.themeOid).then(response => {
        if (response.data > 0) {
          this.msgWarning("当前分类有子分类不能删除，请先删除子分类！");
          return false;
        } else {
          getCountByThemeOid(row.themeOid).then(response => {
            if (response.data > 0) {
              this.msgWarning("当前分类被目录引用，不能删除操作！");
              return false;
            } else {
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
                  if (null == node) {
                    this.getThemeTree();
                  } else {
                    this.remove(node, row.themeOid);
                  }
                  this.msgSuccess("删除成功");
                  this.getList();
                })
                .catch(function() {});
            }
          });
        }
      });
    }

    /** 导出按钮操作 */
    /*handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出列表数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          listExp(queryParams);
        }).catch(function() {});
    }*/
  }
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
.span-ellipsis {
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 14px;
}
</style>
