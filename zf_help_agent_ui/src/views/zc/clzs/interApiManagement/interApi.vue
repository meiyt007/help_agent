
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--咨询数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="70px" @submit.native.prevent>
          <el-form-item label="接口名称" prop="name">
            <el-input v-model.trim="queryParams.name" placeholder="请输入接口名称" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置</el-button>
          </el-form-item>
        </el-form>


       <el-row :gutter="10" class="mb8">

         <el-col :span="1.5">
           <el-button
             type="default"
             icon="el-icon-plus"
             size="mini"
             @click="handleInit"
           >新增</el-button>
         </el-col>
        </el-row>

        <el-table v-loading="loading" :data="interApiList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="接口编码"  align="center" prop="code" :show-overflow-tooltip="true"/>
          <el-table-column label="接口名称"  align="center" prop="name" :show-overflow-tooltip="true"/>
          <el-table-column label="创建时间"align="center" prop="createDate" :show-overflow-tooltip="true"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button  size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)"  >查看</el-button>
              <el-button  size="mini" type="text" icon="iconfont zfsoft-xiugai"  @click="handleInit(scope.row)" >修改</el-button>
              <el-button  size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDelete(scope.row)" >删除</el-button>
<!--              <el-button  size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleElement(scope.row)" >目录元素</el-button>-->
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
      </el-col>
    </el-row>
    <!-- 添加或修改卡证目录 -->
      <el-dialog
        v-dialog-drag
        height="600px"
        scrollbar
        :close-on-click-modal="false"
        :title="title"
        :visible.sync="openInit"
        v-if="openInit"
        width="1100px"
        append-to-body
      >

      <el-form ref="form" :model="form" :rules="rules" label-width="0px" >
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b><i class="require">*</i>所属服务：</b>
            </td>
            <td >
              <el-form-item required prop="serverOid">
                <el-select v-model="form.serverOid" placeholder="所属服务" >
                  <el-option
                    v-for="item in serverList"
                    :key="item.oid"
                    :label="item.serverName"
                    :value="item.oid"
                  ></el-option>
                </el-select>
              </el-form-item>
            </td>
            <td>
              <b><i class="require">*</i>服务接口：</b>
            </td>
            <td >
              <el-form-item required prop="interfaceOid">
                <el-select  v-model="form.interfaceOid" placeholder="服务接口" >
                  <el-option
                    v-for="item in serverInterfaceList"
                    :key="item.oid"
                    :label="item.interfaceName"
                    :value="item.oid"
                  ></el-option>
                </el-select>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b><i class="require">*</i>接口编码：</b>
            </td>
            <td >
              <el-form-item prop="code">
                <el-col :span="24">
                  <el-input v-model.trim="form.code" placeholder="请输入接口编码" maxlength="100" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
            <td>
              <b><i class="require">*</i>接口名称：</b>
            </td>
            <td >
              <el-form-item prop="name">
                <el-col :span="24">
                  <el-input v-model.trim="form.name" placeholder="请输入接口名称" maxlength="100" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
          </tr>
        </table>


        <el-button type="primary" @click="addHtml" class="add-btn">增加</el-button
        >
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table--common zf-zc-table--td-center">
          <colgroup>
            <col width="220" />
            <col width="220" />
            <col width="160" />
          </colgroup>
          <tr>
            <th>接口响应编码</th>
            <th>接口响应名称</th>
            <th>操作</th>
          </tr>
          <template v-for="(ruleForm, index) in form.responseList">
            <template v-if="ruleForm.deleteFlag === 0">
              <tr>

                <td>
                  <el-form-item
                    :prop="'responseList.' + index + '.responseCode'"
                    :rules="rules.responseCode"
                  >
                    <el-input
                      v-model.trim="ruleForm.responseCode" maxlength="100" show-word-limit
                    ></el-input>
                  </el-form-item>
                </td>

                <td>
                  <el-form-item
                    :prop="'responseList.' + index + '.responseName'"
                    :rules="rules.responseName"
                  >
                    <el-input v-model.trim="ruleForm.responseName" maxlength="100" show-word-limit></el-input>
                  </el-form-item>
                </td>

                <td>
                  <el-form-item>
                    <el-button
                      style="border: 0px"
                      icon="el-icon-delete"
                      @click="delHtml(ruleForm,index)"
                      class="handle-btn"
                    ></el-button>
                  </el-form-item>
                </td>
              </tr>
            </template>
          </template>
        </table>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">保存</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 信息详细 -->
<!--    <el-dialog v-dialog-drag :title="title" :visible.sync="openView" width="800px" v-if="openView" append-to-body>-->

    <el-dialog
      v-dialog-drag
      height="600px"
      scrollbar
      :title="title"
      :visible.sync="openView"
      v-if="openView"
      width="openView"
      append-to-body
    >
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b>所属服务：</b>
            </td>
            <td>
              <el-select v-model="form.serverOid" placeholder="所属服务" disabled="true">
                <el-option
                  v-for="item in serverList"
                  :key="item.oid"
                  :label="item.serverName"
                  :value="item.oid"
                ></el-option>
              </el-select>
            </td>
            <td>
              <b>服务接口：</b>
            </td>
            <td>
              <el-select  v-model="form.interfaceOid" placeholder="服务接口" disabled="true">
                <el-option
                  v-for="item in serverInterfaceList"
                  :key="item.oid"
                  :label="item.interfaceName"
                  :value="item.oid"
                ></el-option>
              </el-select>
            </td>
          </tr>
          <tr>
            <td>
              <b>接口编码：</b>
            </td>
            <td>
              {{form.code}}
            </td>
            <td>
              <b>接口名称：</b>
            </td>
            <td>
              {{form.name}}
            </td>
          </tr>
        </table>


      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table--common zf-zc-table--td-center">
        <colgroup>
          <col width="220" />
          <col width="220" />
        </colgroup>
        <tr>
          <th>接口响应编码</th>
          <th>接口响应名称</th>
        </tr>
        <template v-for="(ruleForm, index) in form.responseList">
          <template v-if="ruleForm.deleteFlag === 0">
            <tr>
              <td>
               {{ruleForm.responseCode}}
              </td>
              <td>
                {{ruleForm.responseName}}
              </td>
            </tr>
          </template>
        </template>
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
  getOne,
  saveOrUpdate,
  del,
  checkHasRepeat,
  queryServerList,
  queryServerInterfaceList
} from "@/api/zc/clzs/interApiManagement/interAPi";
export default {
  name: "InterApi",
  components: {},
  editFirstFlag: false,
  watch: {
    "form.serverOid": function (newVal, oldVal) {
      if (!this.editFirstFlag) {
        this.$set(this.form, 'interfaceOid', '')
        this.serverInterfaceList = [];
        if (newVal) {
          queryServerInterfaceList(newVal).then(response => {
            this.serverInterfaceList = response.data;
          });
        }
      }
    },
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      interApiList: [],
      serverList: [],
      serverInterfaceList: [],
      id:"",
      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: null,
        name: null,
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      interApiResopse:{},
      // 表单参数
      form: {
        id: '', //逻辑主键
        code: '', // 接口编码
        name: '', // 接口名称
        type:'',
        responseList: [],
      },
      // 表单校验
      rules: {
        code: [
          { required: true, message: "接口编码不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "接口名称不能为空", trigger: "blur" }
        ],
        serverOid: [
          { required: true, message: "所属服务不能为空", trigger: "change" }
        ],
        interfaceOid: [
          { required: true, message: "服务接口不能为空", trigger: "change" }
        ],
        responseCode: [{
          required: true,
          message: "接口响应编码不能为空",
          trigger: "blur"
        }],
        responseName: [{
          required: true,
          message: "接口响应名称不能为空",
          trigger: "blur"
        }],


      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    //添加子项模块
    addHtml: function () {
      this.form.responseList.push({
        deleteFlag: 0,
      })
    },
    //删除子项模块
    delHtml: function (form, index) {
      if(this.form.responseList[index].id) {
        this.form.responseList[index].deleteFlag = 1;
      }else{
        this.form.responseList.splice(index, 1);
      }
    },
    /** 查询卡证目录列表 */
    getList() {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.interApiList = response.data.data;
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
      _that.editFirstFlag = true;
      queryServerList().then(response => {
        _that.serverList = response.data;
      });
      queryServerInterfaceList(row.serverOid).then(res => {
        _that.serverInterfaceList = res.data;
      });
      getOne(row.id).then(response => {
        _that.form = response.data;
        _that.openView = true;
        _that.title = "查看接口目录信息";
        setTimeout(() => {
          _that.editFirstFlag = false;
        }, 10);
      });
    },
    /** 新增和修改按钮操作 */
    handleInit(row) {
      let _that = this;
      _that.reset();
     // _that.getBillList();
      queryServerList().then(response => {
        _that.serverList = response.data;
      });
      if(row.id) {
        _that.editFirstFlag = true;
        queryServerInterfaceList(row.serverOid).then(res => {
          _that.serverInterfaceList = res.data;
        });
        this.id=row.id;
        getOne(row.id).then(response => {
          _that.openInit = true;
          _that.form = response.data;
          setTimeout(() => {
            _that.editFirstFlag = false;
          }, 10);
        });
      } else {
        this.id="";
        _that.openInit = true;
        _that.form ={
          id: '', //逻辑主键
          code: '', // 电子接口目录名称
          name: '', // 电子接口目录编码
          responseList: [],
        };
      }
      _that.title = row.id ? "修改接口信息" : "新增接口信息";
      },
    /** 提交按钮 */
    submitForm: function() {
      let _that = this;
      _that.$refs["form"].validate(valid => {
        if (valid) {
          checkHasRepeat(_that.id ,_that.form.code).then(responseGet => {
            if(responseGet.data=== 'false'){
              saveOrUpdate(_that.form).then(response => {
                if (response.code === 200) {
                  _that.msgSuccess("保存成功");
                  _that.openInit = false;
                  setTimeout(() => {
                    _that.getList();
                  }, 10);
                }
              });
            }else {
              _that.msgWarning("该接口已添加，不能重复添加！");
            }
            this.id="";
          });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      let _that = this;
      const id = row.id;
      _that.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
          return del(id);
        }).then(() => {
          _that.getList();
          _that.msgSuccess("删除成功");
        });
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
</style>
