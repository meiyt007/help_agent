<template>
  <div class="app-container typeApp">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="100px"
    >
      <el-form-item label="资源名称" prop="name">
        <el-input
          v-model.trim="queryParams.name"
          placeholder="请输入资源名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="所属工作人员" prop="workName">
        <el-input
          v-model.trim="queryParams.workName"
          placeholder="请输入所属工作人员"
          clearable
          size="medium"
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
<!--        <el-button-->
<!--          type="warning"-->
<!--          icon="el-icon-refresh"-->
<!--          size="mini"-->
<!--          @click="resetQuery"-->
<!--          class="ml5"-->
<!--        >重置</el-button-->
<!--        >-->
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit"
          v-hasPermi="['im:userResourceManager:save']"
        >新增</el-button
        >
      </el-col>
    </el-row>

    <el-table
      ref="multipleTable"
      :data="middleUserList"
      tooltip-effect="dark"
      style="width: 100%"
    >
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="资源名称"
        align="center"
        prop="name"
        show-overflow-tooltip
      />
      <el-table-column
        label="资源类型"
        align="center"
        prop="type"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <p v-if="scope.row.type==1">文件夹</p>
          <p v-if="scope.row.type==2">文件</p>
        </template>
      </el-table-column>
      <el-table-column
        label="所属工作人员"
        align="center"
        prop="workUserName"
        show-overflow-tooltip
      />
      <el-table-column
        label="创建人"
        align="center"
        prop="createBy"
        show-overflow-tooltip
      >
<!--        <template slot-scope="scope">-->
<!--          <p v-if="scope.row.userStatus==0">新建</p>-->
<!--          <p v-if="scope.row.userStatus==1">待审核</p>-->
<!--          <p v-if="scope.row.userStatus==2">审核成功</p>-->
<!--          <p v-if="scope.row.userStatus==3">审核失败</p>-->
<!--        </template>-->
      </el-table-column>
      <el-table-column
        label="更新时间"
        align="center"
        prop="updateDate"
        show-overflow-tooltip
      >
      </el-table-column>
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
            v-hasPermi="['im:userResourceManager:view']"
          >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            v-hasPermi="['im:userResourceManager:update']"
          >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
            v-hasPermi="['im:userResourceManager:delete']"
          >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNumber"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

<!--    &lt;!&ndash; 添加或修改应用信息对话框 &ndash;&gt;-->
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
        <el-input v-show="false" v-model="form.userStatus='1'" />
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><i class="require">*</i><b>资源名称：</b></td>
            <td>
              <el-form-item prop="name">
                <el-input
                  v-model.trim="form.name"
                  placeholder="资源名称"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>资源类型：</b></td>
            <td>
              <el-form-item prop="type">
                <el-select
                  v-model="form.type"
                  size="small"
                  style="width: 240px"
                >
                <el-option
                  v-for="(value, key) in iconTypeMap"
                  :key="key"
                  :label="value"
                  :value="key"
                />
                </el-select>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require"></i><b>文件上级：</b></td>
            <td>
              <el-form-item prop="parentId">
                <treeselect
                  class="treeselect"
                  :defaultExpandLevel="1"
                  noOptionsText="暂无数据"
                  noResultsText="暂无数据"
                  :show-count="true"
                  v-model="form.parentId"
                  :options="resourceOptions"
                  placeholder="请输入文件上级"
                />
              </el-form-item>
<!--              <el-form-item prop="parentId">-->
<!--                <el-select-->
<!--                  v-model="form.parentId"-->
<!--                  placeholder="文件上级"-->
<!--                  clearable-->
<!--                  filterable-->
<!--                  style="width: 170px"-->
<!--                >-->
<!--                  <el-option-->
<!--                    v-for="item in resourceParList"-->
<!--                    :key="item.id"-->
<!--                    :label="item.name"-->
<!--                    :value="item.id"-->
<!--                  >-->
<!--                  </el-option>-->
<!--                </el-select>-->
<!--              </el-form-item>-->
            </td>
            <td><i class="require">*</i><b>所属工作人员：</b></td>
            <td>
              <el-form-item prop="workUserId">
                <el-select
                  v-model="form.workUserId"
                  placeholder="请选择所属工作人员"
                  clearable
                  filterable
                  style="width: 170px"
                >
                  <el-option
                    v-for="item in userList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </td>
          </tr>
          <tr v-if="form.type==='2'">
            <td><i class="require">*</i><b>附件上传：</b></td>
            <td colspan="3">
              <el-form-item prop="resourceInfo">
                <el-upload
                  v-model.trim="form.resourceInfo"
                  class="upload-demo"
                  action=""
                  :on-remove="removeElectronicFormAddr"
                  :http-request="uploadFilesElectronicFormAddr"
                  :before-upload="beforeUploadElectronicFormAddr"
                  :on-error="uploadError"
                  :file-list="fileElectronicFormAddrList"
                  accept="accept"
                >
                  <el-button size="mini" type="primary" icon="el-icon-upload"
                  >点击上传</el-button>
                </el-upload>
                <div v-if="attaListStatic ">
                    <span>
                      {{
                      attaList.originName
                      }}
                    </span>
  <!--                  <el-link type="primary" @click="downLoadFileInfo(form.sxServiceExtend.handleFlow)">下载</el-link>|-->
                  <el-link type="primary" @click="delFlowFile()">删除</el-link>
                </div>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require"></i><b>备注：</b></td>
            <td colspan="3">
              <el-form-item prop="remark">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.remark"
                    type="textarea"
                    maxlength="500"
                    show-word-limit
                    placeholder="请输入备注"
                  ></el-input>
                </el-col>
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
    <!-- 查看应用信息详细 -->
    <el-dialog
      title="查看"
      :close-on-click-modal="false"
      :visible.sync="openView"
      width="900px"
      append-to-body
      height="800px"
      scrollbar
    >
      <div class="zf-zc-table--title">用户资源信息</div>
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
            <td><b>资源名称：</b></td>
            <td>
              {{ middleUser.name }}
            </td>
            <td><b>资源类型：</b></td>
            <td>
              {{ middleUser.type == '1'?'文件夹':'文件' }}
            </td>
          </tr>
          <tr>
            <td><b>工作人员编号：</b></td>
            <td>
              {{ middleUser.workUserId }}
            </td>
            <td><b>工作人员姓名：</b></td>
            <td>
              <span  v-for="user in userList" v-if="user.id=== middleUser.workUserId">
              {{ user.name}}
              </span>
            </td>
          </tr>
          <tr>
            <td><b>文件上级：</b></td>
            <td>
              <span  v-for="res in resourceParList" v-if="res.id=== middleUser.parentId">
              {{ res.name}}
            </span>
            </td>

<!--            <td>-->
<!--              {{ middleUser.parentId }}-->
<!--            </td>-->
            <td><b>文件编号：</b></td>
            <td>
              {{ middleUser.resourceInfo }}
            </td>
          </tr>
          <tr>
            <td><b>创建人：</b></td>
            <td>
<!--            <td colspan="3">-->
              {{ middleUser.createBy }}
            </td>

            <td><b>创建时间：</b></td>
            <td>
              {{ middleUser.createDate }}
            </td>
          </tr>
          <tr>
            <td><b>更新人：</b></td>
            <td>
              {{ middleUser.updateBy }}
            </td>
            <td><b>更新时间：</b></td>
            <td>
              {{ middleUser.updateDate }}
            </td>
          </tr>

        </table>
      <el-table
        ref="multipleTable"
        :data="viewAttaList"
        tooltip-effect="dark"
        style="width: 100%"
      >
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="资源名称"
          align="center"
          prop="name"
          show-overflow-tooltip
        />
        <el-table-column
          label="资源类型"
          align="center"
          prop="type"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <p v-if="scope.row.type==1">文件夹</p>
            <p v-if="scope.row.type==2">文件</p>
          </template>
        </el-table-column>
        <el-table-column
          label="文件名称"
          align="center"
          prop="attaOriginName"
          show-overflow-tooltip
        >
          <!--        <template slot-scope="scope">-->
          <!--          <p v-if="scope.row.userStatus==0">新建</p>-->
          <!--          <p v-if="scope.row.userStatus==1">待审核</p>-->
          <!--          <p v-if="scope.row.userStatus==2">审核成功</p>-->
          <!--          <p v-if="scope.row.userStatus==3">审核失败</p>-->
          <!--        </template>-->
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="iconfont zfsoft-xiazai"
              @click="downAtta(scope.row)"
              v-hasPermi="['im:userResourceManager:down']"
              v-if="scope.row.attaOid!=null"
            >下载</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>


  </div>
</template>
<script>
import {
  pageUser,
  initUserList,
  saveUser,
  initUser,
  del,
  uploadsxFile,
  initResourceFolder,
  querySysAttaByOid,
  downloadSysAtta,
  queryHaUserResourceDataByIdAndType,
  queryResourcrSimpleTree
} from "@/api/ha/userManagement/userResourceManager.js";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  components: {Treeselect},
  name: "userResourceManager",
  data () {
    //定义校验规则
    let validateFileUrl = (rule, value, callback) => {
      // console.log("attaList"+this.form.resourceInfo);
      if ( (typeof this.form.resourceInfo === 'undefined')||(this.form.resourceInfo === ""||this.form.resourceInfo === null)) {
        callback(new Error("请上传文件"))
      } else {
        callback()
      }
    };
    return {
      viewAttaList: [],
      fileElectronicFormAddrList: [],
      // 遮罩层
      loading: true,
      statusMap: { "0": "新建", "1": "待审核", "2": "审核成功", "3": "审核失败" },
      userStatus: { "N": "禁用", "Y": "启用" },
      iconTypeMap: { "1": "文件夹", "2": "文件" },
      // 总条数
      total: 0,
      title: "",
      // 数据表格
      middleUserList: [],
      //userList
      userList: [],
      resourceParList: [],
      //根据文件夹文件判断是否需要上传
      attaListStatic: false,
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      // 审核
      openCheck: false,
      // 启用状态
      ableMap: { "1": "启用", "0": "禁用" },
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
        name: "",
        workName: ""
      },
      middleUser:{},
      userAuthRecList:[],
      resourceOptions: [],
      form: {
      },
      formCheck: {
        userStatus: "2",
        memo: "",
        middleUserOid: ""
      },
      attaList: "",
      rules: {
        name: [
          { required: true, message: "必填项", trigger: "blur" }
        ],
        type: [
          { required: true, message: '必填项', trigger: 'blur' },
          // { validator:validatePhone, trigger: 'blur' }
        ],
        resourcePar: [
          // {required:true,message:'必填项',trigger:"blur"}
        ],
        workUserName: [
          {required:true,message:'必填项',trigger:"blur"}
        ],
        workUserId: [
          {required:true,message:'必填项',trigger:"blur"}
        ],
        resourceInfo: [
          {required:this.attaListStatic,message:'必填项',validator: validateFileUrl, trigger: 'change'}
        ],
        remark: [
          // {required:true,message:'必填项',trigger:"blur"}
          // { validator:validateEmails, trigger: 'blur' }
        ]
      },
      //预览
      viewDialogOptions:[],
    };
  },
  watch: {
    'this.viewAttaList': {
    },
    'this.attaList':{
    }
  },
  created () {
    this.getList();
    this.getResourceTree();
  },
  methods: {
    /** 查询业务层级列表 */
    getList () {
      this.loading = true;
      pageUser(this.queryParams).then(response => {
        this.middleUserList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },

    /** 查看按钮操作 */
    handleView (row) {
      this.reset();
      const id = row.id;
      this.viewAttaList = [];
      initUser(id).then(response => {
        this.middleUser = response.data;
        this.openView = true;
        this.title = "查看应用信息";
        initUserList().then(response1 => {
          // console.log(response.data.data)
          this.userList = response1.data.data;
        });
        initResourceFolder().then(response2 => {
          this.resourceParList = response2.data;
          // console.log(response)
        });
        queryHaUserResourceDataByIdAndType(id,row.type).then(response3 => {
          this.viewAttaList = response3.data;
          // console.log(response)
        });
      });
    },
    // // 是否展示个人登记按钮
    // isShowAuth (row) {
    //   return row.userStatus && row.userStatus.indexOf("1") != -1;
    // },

    downAtta(row){
      this.loading = true;
      downloadSysAtta(row.attaOid);
      this.loading = false;
    },
    // 初始化新增
    handleInit (row) {
      // console.log(this.rules);
      const id = row.id;
      this.handleDeleteElectronicForm();
      this.getResourceTree();
      if (id === undefined) {
        initUserList().then(response => {
          this.userList = response.data.data;
        });
        initResourceFolder().then(response => {
          this.resourceParList = response.data;
        });
        this.attaList="";
        this.attaListStatic = false;
        this.form = {};
        this.openInit = true;
        this.title = "新增";
      } else {
        initUser(id).then(response => {
          initResourceFolder().then(response => {
              this.resourceParList = response.data;
            });
          initUserList().then(response => {
            this.userList = response.data.data;
          });
          if(row.resourceInfo!==""&&row.resourceInfo!==null){
            querySysAttaByOid(row.resourceInfo).then(response => {
              this.attaList = response.data;
              // console.log(this.attaList)
              if(this.attaList!==""||this.attaList!==null){
                // console.log(this.attaListStatic)
                this.attaListStatic = true;
              }
            });
          }
          this.form = response.data;
          this.openInit = true;
          this.title = "修改";
        });
      }
    },
    /** 获取资源树 */
    getResourceTree () {
      queryResourcrSimpleTree().then(response => {
        this.resourceOptions = response.data;
        // console.log("resourceOptions"+this.resourceOptions)
      });
    },
    // handleCheck (row) {
    //   const oid = row.id;
    //   checkView(oid).then(response => {
    //     this.middleUser = response.data.middleUser;
    //     this.formCheck.middleUserOid =  response.data.middleUser.oid;
    //     this.openCheck = true;
    //     this.title = "审核";
    //   });
    // },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          saveUser(this.form).then(response => {
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
    // // 审核提交
    // submitFormCheck: function () {
    //   this.$refs["formCheck"].validate(valid => {
    //     if (valid) {
    //       saveCheck(this.formCheck).then(response => {
    //         if (response.code === 200) {
    //           this.msgSuccess("保存成功");
    //           this.openCheck = false;
    //           this.getList();
    //         }
    //       });
    //     } else {
    //       return false;
    //     }
    //   });
    // },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNumber = 1;
      this.queryParams.pageSize = 10;
      this.getList();
    },
    // /** 重置按钮操作 */
    // resetQuery () {
    //   this.resetForm("queryForm");
    //   this.queryParams = {};
    //   this.handleQuery();
    // },
    // 表单重置
    reset () {
      this.form = {};
      this.resetForm("form");
    },
    // 取消按钮
    cancel () {
      this.openInit = false;
      this.openView = false;
      this.openCheck = false;
      this.reset();
    },
    // handleAble (row) {
    //   const oid = row.id;
    //   let ableMessage = "";
    //   let msg = "";
    //   if (row.ableStatus === 'Y') {
    //     ableMessage = "你确定要启用吗？";
    //     msg = "启用";
    //   } else {
    //     ableMessage = "禁用类别可能会导致对应标签数据出错，是否确认禁用?";
    //     msg = "禁用";
    //   }
    //   this.$confirm(ableMessage, "警告", {
    //     confirmButtonText: "确定",
    //     cancelButtonText: "取消",
    //     type: "warning"
    //   })
    //     .then(function () {
    //       return able(oid);
    //     })
    //     .then(() => {
    //       this.msgSuccess(msg + "成功");
    //     })
    //     .catch(function () {
    //       row.ableStatus = row.ableStatus === 'N' ? 'Y' : 'N';
    //     });
    // },
    /** 删除按钮操作 */
    handleDelete (row) {
      const id = row.id;
      this.$confirm(
        "删除资源可能会导致资源数据出错，是否确认删除?",
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(function () {
          return del(id);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function () { });
    },
    /** 上传附件 相关方法 */
    delFlowFile () {
      this.form.resourceInfo = "";
      this.attaListStatic = false;
    },

    /**
     * 清除附加列表
     */
    handleDeleteElectronicForm () {
      this.fileElectronicFormAddrList = [];
    },
    uploadFilesElectronicFormAddr (file) {
      let _that = this;
      let formData = new FormData();
      formData.append("file", file.file);
      uploadsxFile(formData).then(respon => {
        // this.mForm.electronicFormAddr = respon.data.oid;
        // this.mForm.electronicFormOriginName = respon.data.name;
        this.form.resourceInfo = respon.data.fastdfsUploadUrl;
        // this.attaList =  respon.data;

      });
    },
    beforeUploadElectronicFormAddr (file) {
      let _that = this;
      let isRightSize = file.size / 1024 / 1024 < 100;
      if (!isRightSize) {
        _that.$message.error("文件大小超过 100MB");
      }
      _that.fileElectronicFormAddrList = [];
      _that.fileElectronicFormAddrList.push(file);
      return isRightSize;
    },
    uploadError (resp) {
      let _that = this;
      _that.msgError("文件上传失败");
    },
    removeElectronicFormAddr () {
      this.fileElectronicFormAddrList = [];
      // this.mForm.electronicFormAddr = "";
    },
  },
  filters:{
    statusHandle(data) {
      if(!data) {
        return '待审核'
      }
      const statusMap = {
        0: '新建',
        1: '待审核',
        2: '审核成功',
        3: '审核失败'
      }
      return statusMap[data]
    }
  }
};
</script>
<style scoped>
.treeselect {
  width: 200px !important;
}</style>
