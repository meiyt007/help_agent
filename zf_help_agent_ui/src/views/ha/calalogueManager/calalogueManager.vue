/** * @Author: wangns */
<!-- 请求条件 -->
<template>
  <div class="app-container">
    <!--用户数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="80px"
    >
      <el-form-item label="目录名称" prop="cateGoryName">
        <el-input
          v-model.trim="queryParams.cateGoryName"
          placeholder="请目录名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="启禁用" prop="showFlag">
        <el-col :span="24">
          <el-select
            v-model.trim="queryParams.showFlag"
            placeholder="请选择是否启用"
            size="small" >
            <el-option label="启用" value="1"></el-option>
            <el-option label="禁用" value="0"></el-option>
          </el-select>
        </el-col>
      </el-form-item>
<!--      <el-form-item label="状态" prop="deleteStatus">-->
<!--        <el-col :span="24">-->
<!--          <el-select-->
<!--            v-model.trim="queryParams.deleteStatus"-->
<!--            placeholder="请选择状态类型"-->
<!--            size="small" >-->
<!--            <el-option label="未删除" value="1"></el-option>-->
<!--            <el-option label="已删除" value="2"></el-option>-->
<!--          </el-select>-->
<!--        </el-col>-->
<!--      </el-form-item>-->
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
        >重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 工具条 -->
    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit"
          v-hasPermi="['im:catalogue:save']"
        >新增</el-button>
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleLoadWandaCatalogue"
          v-hasPermi="['im:catalogue:getWandaCata']"
        >同步万达窗口目录</el-button
        >
      </el-col>
<!--      <el-button type="danger" size="mini" @click="removeRows()">批量删除</el-button>-->
    </el-row>
    <!-- 表头 -->
    <el-table
      v-loading="loading"
      :data="districtList"
      stripe
      style="width: 100%" @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"/>
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="主键"
        align="center"
        prop="id"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="目录名"
        align="center"
        prop="cateGoryName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="描述"
        align="center"
        prop="desc"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="父级目录"
        align="center"
        prop="desc"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <p  v-for="res in resourceParList" v-if="res.id=== scope.row.parentId">
              {{ res.cateGoryName}}</p>
        </template>
      </el-table-column>
      <el-table-column
        label="是否启用"
        align="center"
        prop="showFlag"
        :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <p v-if="scope.row.showFlag==1">启用</p>
          <p v-if="scope.row.showFlag==0">禁用</p>
        </template>
      </el-table-column>
      <el-table-column
        label="排序"
        align="center"
        prop="order"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="操作"
        align="center"
        width="340"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['im:catalogue:view']"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            v-hasPermi="['im:catalogue:update']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="removeById(scope.row.id)"
            v-hasPermi="['im:catalogue:delete']">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      style="padding: 30px 0; text-align: center;"
      layout="total, sizes, prev, pager, next, jumper"
      @pagination="getList"
    />

    <!-- 信息详细 -->
    <!-- 添加或修改应用信息对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openInit"
      width="900px"
      append-to-body
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="0px"
        class="demo-ruleForm"
      >
        <el-input v-show="false" />
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><i class="require">*</i><b>目录名称：</b></td>
            <td>
              <el-form-item prop="cateGoryName">
                <el-input
                  v-model.trim="form.cateGoryName"
                  placeholder="请输入目录名称"
                  maxlength="100"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>是否启用：</b></td>
            <td>
              <el-form-item prop="showFlag">
                <el-select
                  v-model.trim="form.showFlag"
                  placeholder="请选择是否启用"
                  size="small" >
                  <el-option label="启用" :value="1"></el-option>
                  <el-option label="禁用" :value="0"></el-option>
                </el-select>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>万达目录主键：</b></td>
            <td>
              <el-form-item prop="cateGoryId">
                <el-input
                  v-model.trim="form.cateGoryId"
                  placeholder="请输入目录名称"
                  maxlength="100"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require"></i><b>万达目录编号：</b></td>
            <td>
              <el-form-item prop="cateGoryCode">
                <el-input
                  v-model.trim="form.cateGoryCode"
                  placeholder="请输入目录名称"
                  maxlength="100"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>父级目录：</b></td>
            <td>
              <el-form-item prop="parentId">
                <el-select v-model="form.parentId" placeholder="请选择父级目录">
                  <el-option
                    v-for="dict in resourceParList"
                    :key="dict.id"
                    :label="dict.cateGoryName"
                    :value="dict.id"
                  ></el-option>
                </el-select>
              </el-form-item>
            </td>
            <td><i class="require"></i><b>排序：</b></td>
            <td>
              <el-form-item prop="order">
                <el-input
                  v-model.trim="form.order"
                  placeholder="请输入排序号"
                  maxlength="100"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td><i class="require"></i><b>备注：</b></td>
            <td colspan="3">
              <el-form-item prop="desc">
                <el-input
                  v-model.trim="form.desc"
                  placeholder="请输入备注"
                  maxlength="500"
                  show-word-limit
                  type="textarea"
                />
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" align="center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 查看banner信息 -->
    <el-dialog
      title="查看"
      :close-on-click-modal="false"
      :visible.sync="openView"
      width="900px"
      append-to-body
      height="800px"
      scrollbar
    >
      <div class="zf-zc-table--title">窗口取号目录信息</div>
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
          <td><b>目录名称：</b></td>
          <td>
            {{ middleCata.cateGoryName }}
          </td>
          <td><b>启用标识：</b></td>
          <td>
            {{ middleCata.showFlag == '1'?'启用':'禁用' }}
          </td>
        </tr>

        <tr>
          <td><b>目录上级：</b></td>
          <td>
              <span  v-for="res in resourceParList" v-if="res.id=== middleCata.parentId">
              {{ res.cateGoryName}}
            </span>
          </td>
          <td><b>排序：</b></td>
          <td>
            {{ middleCata.order }}
          </td>
        </tr>
        <tr>
          <td><b>万达目录主键：</b></td>
          <td>
            <!--            <td colspan="3">-->
            {{ middleCata.cateGoryId }}
          </td>

          <td><b>万达目录编号：</b></td>
          <td>
            {{ middleCata.cateGoryCode }}
          </td>
        </tr>
        <tr>
          <td><b>备注：</b></td>
          <td colspan="3">
            {{ middleCata.desc }}
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
import { page,saveOrUpdate,init,deleteById,getAndLoadWandaCatalogue } from "@/api/ha/catalogueManagement/catalogueManagement.js";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  name: "",
  components: { Treeselect },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      districtList: [],
      multipleSelection: [],  //批量选择中选择的记录列表
      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      // 查看显示弹出层
      openView: false,
      openInit: false,
      // 查看显示弹出层
      // openView: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        cateGoryName: "",
        showFlag: "",
        parentId: "",
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        cateGoryName: [
          { required: true, message: "必填项", trigger: "blur" }
        ],
        showFlag: [
          { required: true, message: '必填项', trigger: 'blur' },
          // { validator:validatePhone, trigger: 'blur' }
        ],
        cateGoryId: [
          {required:true,message:'必填项',trigger:"blur"}
        ]

      },
      userOidArr: [],
      props: {
        label: "label", //这里是树结构中需显示的数据（即接口返回的需启用在页面上的参数）
        children: "children",
        isLeaf: "leaf"
      },

      middleCata:{},
      resourceParList: [],

      //  uploadImagerUrl: process.env.VUE_APP_BJFW_ROUTE_PATH + '/work/user/uploadImage',
    };
  },
  created() {
    this.getListInit();
  },
  methods: {


    // //批量删除按钮
    // removeRows(){
    //   this.$confirm('此操作将永久删除目录, 是否继续?', '提示', {
    //     confirmButtonText: '确定',
    //     cancelButtonText: '取消',
    //     type: 'warning'
    //   }).then(() => { //确定执行then方法
    //     var idList =[]
    //     //遍历数组得到每个id值，设置到idlist中
    //     for(var i=0;i<this.multipleSelection.length;i++){
    //       var obj = this.multipleSelection[i]
    //       var id = obj.id
    //       idList.push(id)
    //     }
    //     //调用接口方法
    //     batchRemoveBanner(idList)
    //       .then(response => {
    //         //提示信息
    //         this.$message({
    //           type: 'success',
    //           message: '批量删除成功!'
    //         })
    //         //刷新页面
    //         this.getList()
    //       })
    //   })
    // },

    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          saveOrUpdate(this.form).then(response => {
            if (response.code === 200) {
              this.msgSuccess("保存成功");
              this.openInit = false;
              this.getList();
            }
          });
        } else {
          return false;
        }
      });
    },

    /** 查看按钮操作 */
    handleView (row) {
      debugger
      this.reset();
      const id = row.id;
      init(id).then(response => {
        console.log(response.data)
        this.middleCata= response.data;
        console.log(this.data)
        this.openView = true;
        this.title = "查看应用信息";
      });
    },
    //删除
    removeById(id){
      this.$confirm('此操作将该目录删除, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {//确定执行then方法
        //调用接口方法
        deleteById(id)
          .then(response => {
            //提示信息
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            //刷新页面
            this.getList()
          })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    /** 初始化新增或修改 */
    handleInit (row) {
      const id = row.id;
      if (id === undefined) {
        this.imageUrl ='';
        this.form = {};
        this.openInit = true;
        this.title = "新增";
      } else {
        init(id).then(response => {
          this.form = response.data;
          this.imageUrl = response.data.images;
          this.openInit = true;
          this.title = "修改";
        });
      }
    },
    /** 同步万达事项**/
    handleLoadWandaCatalogue(){
      this.loading = true;
      getAndLoadWandaCatalogue().then(response => {
        if (response.data && response.code === 200) {
          //提示信息
          this.$message({
            type: 'success',
            message: '同步万达窗口目录完成!'
          })
          //刷新页面
          this.getList()
        }else{
          this.msgError('同步万达窗口目录失败!'+response.message);
        }
      }).catch((error) => {
        //提示信息
        this.$message({
          type: 'error',
          message: '同步万达窗口目录失败!'
        })
        console.log(error);
      }).finally(()=>{
        this.loading = false;
      });
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.districtList = response.data.data;

        this.total = response.data.total;
        this.loading = false;
      });
    },

    /** 查询列表 */
    getListInit() {
      this.loading = true;
      page(this.queryParams).then(response => {

        this.districtList = response.data.data;
        this.resourceParList= response.data.data;
        this.total = response.data.total;
        this.loading = false;

      });

    },
    // 取消按钮
    cancel() {
      this.openInit = false;
      // this.reset();
    },
    // 表单重置
    reset() {
      // Object.assign(this.form, this.$options.data().form);
      this.form = {};
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

    //获取选择复选框的id值
    handleSelectionChange(selection){
      this.multipleSelection = selection
    }
  },
};
</script>
<style lang="scss" scoped>
//头像样式start
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
// end
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
