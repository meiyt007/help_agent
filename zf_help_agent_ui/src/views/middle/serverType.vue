<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="24" :xs="24">
        <el-form
          :model="queryParams"
          ref="queryForm"
          :inline="true"
          label-width="70px"
          @selection-change="handleSelectionChange"
          @submit.native.prevent
        >

          <el-form-item label="分类名称" prop="catalogName">
            <el-input
              v-model.trim="queryParams.typeName"
              placeholder="请输入分类名称"
              clearable
              size="small"
            />
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button
              type="primary"
              icon="el-icon-search"
              size="mini"
              @click="handleQuery"
            >搜索
            </el-button
            >
            <el-button
              type="warning"
              icon="el-icon-refresh"
              size="mini"
              @click="resetQuery"
              class="btn-reset"
            >重置
            </el-button
            >
          </el-form-item>
        </el-form>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="default"
              icon="el-icon-edit"
              size="mini"
              @click="handleEdit"
            >编辑
            </el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="default"
              icon="el-icon-delete"
              size="mini"
              @click="submitBatchForm"
            >删除
            </el-button
            >
          </el-col>
        </el-row>
        <div class="primary-table">
          <el-table :data="serverTypeList"  :fit="true"  @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="60" align="center"></el-table-column>
            <el-table-column label="序号" width="55" align="center">
              <template slot-scope="scope">
                <span>{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              label="服务分类名称"
              prop="typeName"
              align="center"
              :show-overflow-tooltip="true"
            >
            </el-table-column>
            <el-table-column
              label="排序"
              prop="sort"
              align="center"
              :show-overflow-tooltip="true"
            >
            </el-table-column>
            <el-table-column
              label="创建时间"
              prop="createDate"
              align="center"
              :show-overflow-tooltip="true"
            >
            </el-table-column>
            <el-table-column
              label="操作"
              prop="handle"
              align="center"
              width="180"
            >
              <template slot-scope="scopew">
                <el-button
                  size="mini"
                  type="text"
                  icon="iconfont zfsoft-chakan"
                  @click="handleView(scopew.row)"
                  v-hasPermi="['im:serverType:view']"
                >查看
                </el-button
                >
                <el-button
                  size="mini"
                  type="text"
                  icon="iconfont zfsoft-xiugai"
                  @click="handleEdit(scopew.row)"
                  v-hasPermi="['im:serverType:update']"
                >编辑下级
                </el-button
                >
              </template>
            </el-table-column>
            <el-table-column type="expand">
              <template slot-scope="props">
                <template v-for="(item, index) in props.row.serverTypeDtoList">
                  <el-form
                    label-position="left"
                    inline
                    class="demo-table-expand"
                  >
                    <el-form-item style="width: 10%" align="center">
                      <span>{{ index + 1 }}</span>
                    </el-form-item>
                    <el-form-item style="width: 15%" align="center">
                      <span>{{ item.typeName }}</span>
                    </el-form-item>
                    <el-form-item style="width: 5%" align="center">
                      <span>
                        <el-button
                          icon="el-icon-sort-up"
                          class="handle-btn"
                          @click="up(item.oid)"
                          v-hasPermi="['im:serverType:up']"
                        >上移</el-button>
                      </span>
                    </el-form-item>
                    <el-form-item style="width: 5%" align="center">
                      <span>
                        <el-button
                          icon="el-icon-sort-down"
                          class="handle-btn"
                          @click="down(item.oid)"
                          v-hasPermi="['im:serverType:down']"
                        >下移</el-button>
                      </span>
                    </el-form-item>
                    <el-form-item style="width: 5%" align="center">
                      <span>
                        <el-button
                          icon="el-icon-delete"
                          class="handle-btn"
                          @click="delChild(item.oid)"
                          v-hasPermi="['im:serverType:delete']"
                        >删除</el-button>
                      </span>
                    </el-form-item>
                  </el-form>
                </template>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>

    <!-- 编辑、编辑下级弹框 -->
    <el-dialog
      v-dialog-drag
      height="600px"
      scrollbar
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="serverTypeView"
      v-if="serverTypeView"
      width="1100px"
      append-to-body
    >
      <div id="printTest">
        <el-form ref="form" :model="form" :rules="rules" label-width="0px">
          <el-button type="primary" @click="addHtml" class="add-btn"
                     v-hasPermi="['im:serverType:save']"
          >增加</el-button
          >
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table--common zf-zc-table--td-center">
            <colgroup>
              <col width="50" />
              <col width="220" />
              <col width="160" />
              <col width="70" />
            </colgroup>
            <tr>
              <th>序号</th>
              <th>类别名称</th>
              <th>排序</th>
              <th>操作</th>
            </tr>
            <template v-for="(ruleForm, index) in form.serverTypeDtoList">
              <template>
                <tr>
                  <td>
                    <el-form-item prop="xuhao">
                      {{ index + 1 }}
                    </el-form-item>
                  </td>
                  <td>
                    <el-form-item>
                      <el-input
                        maxlength="32"
                        v-model="ruleForm.typeName"
                      ></el-input>
                    </el-form-item>
                  </td>
                  <td>
                    <el-form-item>
                      <el-input
                        v-model.trim="ruleForm.sort"
                        maxlength="3"
                      ></el-input>
                    </el-form-item>
                  </td>
                  <td>
                    <el-form-item>
                      <el-button
                        style="border: 0px"
                        icon="el-icon-delete"
                        @click="delHtml(index)"
                        v-if="isShowDel(ruleForm)"
                        class="handle-btn"
                      ></el-button>
                    </el-form-item>
                  </td>
                </tr>
              </template>
            </template>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">保 存</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看服务分类信息弹框 -->
    <el-dialog
      title="查看"
      :close-on-click-modal="false"
      :visible.sync="openView"
      width="900px"
      append-to-body
    >
      <table
        cellspacing="0"
        cellpadding="0"
        border="0"
        class="zf-zc-table"
      >
        <colgroup>
          <col width="20%"/>
          <col width="30%"/>
          <col width="20%"/>
          <col width="30%"/>
        </colgroup>
        <tr>
          <td><b>分类名称：</b></td>
          <td>
            {{ form.typeName }}

          <td><b>排序号：</b></td>
          <td>
            {{ form.sort }}
          </td>
        </tr>
      </table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  page,
  editServerType,
  getOne,
  batchDelete,
  upMove,
  downMove,
  save,
  del
} from '@/api/middle/serverType'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: 'serverType',
  components: {
    Treeselect
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 服务分类列表数据
      serverTypeList: [],
      //服务分类数据
      serverType:{},
      // 弹出层标题
      title: '',
      // 编辑、编辑下级弹框
      serverTypeView: false,
      // 查看显示弹出层
      openView: false,
      // 查询参数
      queryParams: {
        typeName: ''
      },
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      // 表单参数
      form: {
        serverTypeDtoList:[],
        parentTypeOid:''
      },
      // 表单校验
      rules: {
        typeName: [
          { required: true, message: "类别名称不能为空", trigger: "blur" },
        ],
        sort: [{ required: true, message: "排序号不能为空", trigger: "blur" }],
      }
    }
  },
  created() {
    this.getList()
  },

  methods: {
    /** 查询服务类型列表 */
    getList() {
      this.loading = true
      page(this.queryParams).then(response => {
        this.serverTypeList = response.data
        this.loading = false
      })
    },
    //删除子级
    delChild(oid){
      this.$confirm(
        '是否确认删除?',
        '警告',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
        .then(function() {
          return del(oid);
        })
        .then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        })
        .catch(function() {

        })
    },
    //批量删除
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    submitBatchForm: function() {
      let _that = this
      var oid = []
      if (_that.multipleSelection.length <= 0) {
        _that.$message.error('请选择删除!')
        return false
      }
      for (let ss = 0; ss < _that.multipleSelection.length; ss++) {
        oid.push(_that.multipleSelection[ss].oid)
      }
      var oids = oid.join(',')
      batchDelete(oids).then(response => {
        if (response.code === 200) {
          _that.msgSuccess('删除成功')
          this.getList()
        }
      })
    },
    // 取消按钮
    cancel() {
      this.openInit = false
      this.serverTypeView = false
      this.reset()
    },
    //上移
    up(oid){
      upMove(oid).then(response => {
        if (response.code == 200) {
          this.msgSuccess("移动成功！")
          this.getList()
        }
      })
    },
    //下移
    down(oid){
      downMove(oid).then(response => {
          if (response.code == 200) {
            this.msgSuccess("移动成功！")
            this.getList()
          }
      })
    },
    // 表单重置
    reset() {
      Object.assign(this.form, this.$options.data().form)
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.queryParams = {}
      this.handleQuery()
    },
    /** 查看按钮操作 */
    handleView(row) {
      let _that = this
      _that.reset()
      const oid = row.oid
      getOne(oid).then(response => {
        _that.form = response.data
        _that.openView = true
        _that.title = '查看服务分类信息'
      })
    },

    //编辑
    handleEdit(row) {
      const oid = row.oid
      if (oid === undefined) {
        editServerType().then(response => {
          this.form.serverTypeDtoList = response.data
          this.serverTypeView = true
          this.title = '编辑'
        })
      } else {
        editServerType(oid).then(response => {
          this.form.serverTypeDtoList = response.data
          this.form.parentTypeOid = oid;
          this.serverTypeView = true
          this.title = '编辑下级'
        })
      }
    },
    //添加子项模块
    addHtml: function () {
      this.form.serverTypeDtoList.push({

      })
    },
    //删除子项模块
    delHtml: function (index) {
      this.serverType = {};
      this.serverType = this.form.serverTypeDtoList[index];
      this.form.serverTypeDtoList.splice(index, 1);
    },
    //是否展示删除
    isShowDel (row) {
      return !row.oid && row.oid== undefined;
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.serverTypeDtoList == null && this.form.serverTypeDtoList.length == 0) {
            this.msgWarning("请至少录入一个类别！");
            return false;
          }else {
            let chlidName = [];
            let chlidSort = [];
            for (let serverType of this.form.serverTypeDtoList) {
              if(serverType.typeName==null||serverType.typeName==''){
                this.msgWarning("类别名称不能为空！");
                return false;
              }
              if(serverType.sort==null||serverType.sort==''){
                this.msgWarning("排序号不能为空！");
                return false;
              }
              const re = /^[1-9]{1,}[\d]*$/;
              if(!re.test(serverType.sort)||parseInt(serverType.sort) <= 0){
                this.msgWarning("排序号必须为正整数！");
                return false;
              }
              chlidName.push(serverType.typeName);
              chlidSort.push(serverType.sort);
            }
            let repeat = chlidName.join(",") + ",";
            let repeatSort = chlidSort.join(",") + ",";
            for (let i = 0; i < chlidName.length; i++) {
              for(let j =i+1;j<chlidName.length;j++){
                if(chlidName[i] == chlidName[j]){
                  this.msgWarning("子级名称:" + chlidName[i] + " 已存在，不能重复添加！");
                  return false;
                }
                if(chlidSort[i] == chlidSort[j]){
                  this.msgWarning("排序号:" + chlidSort[i] + " 已存在，不能重复添加！");
                  return false;
                }
              }
             /* if (repeat.replace(chlidName[i] + ",", "").indexOf(chlidName[i] + ",") > -1) {
                this.msgWarning("子级名称:" + repeat[i] + " 已存在，不能重复添加！");
                return false;
              }
              if (repeatSort.replace(chlidSort[i] + ",", "").indexOf(chlidSort[i] + ",") > -1) {
                this.msgWarning("不允许有重复排序号！");
                return false;
              }*/
            }
          }
          save(this.form).then((response) => {
            if (response.code === 200) {
              this.msgSuccess("保存成功");
              this.serverTypeView = false;
              this.getList();
            }
          });
        } else {
          return false;
        }
      });
    },
  }
}
</script>
<style lang="scss" scoped>
.primary-table .handle-btn {
  border: none;
  background: none;
  padding: 0;
  font-size: 16px;
}
</style>
