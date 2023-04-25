<template>
  <div class="app-container typeApp">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="100px"
    >
      <el-form-item label="姓名" prop="name">
        <el-input
          v-model.trim="queryParams.name"
          placeholder="请输入姓名"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="企业名称" prop="companyName">
        <el-input
          v-model.trim="queryParams.companyName"
          placeholder="请输入所属企业名称"
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
          v-hasPermi="['im:workQueue:save']"
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
        label="姓名"
        align="center"
        prop="name"
        show-overflow-tooltip
      />
<!--      <el-table-column-->
<!--        label="身份证号"-->
<!--        align="center"-->
<!--        prop="cardNo"-->
<!--        show-overflow-tooltip-->
<!--      >-->
<!--        <template slot-scope="scope">-->
<!--          <p v-if="scope.row.type==1">文件夹</p>-->
<!--          <p v-if="scope.row.type==2">文件</p>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column
        label="手机号"
        align="center"
        prop="phone"
        show-overflow-tooltip
      />
      <el-table-column
        label="企业名称"
        align="center"
        prop="companyName"
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
        label="统一社会信用代码"
        align="center"
        prop="companyCode"
        show-overflow-tooltip
      />
      <el-table-column
        label="排队状态"
        align="center"
        prop="queueStatus"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <p v-if="scope.row.queueStatus==1">扫码排队中</p>
          <p v-if="scope.row.queueStatus==2">导服已分配</p>
        </template>
      </el-table-column>
      <el-table-column
        label="分配状态"
        align="center"
        prop="distributeStatus"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <p v-if="scope.row.distributeStatus==1">指定人员</p>
          <p v-if="scope.row.distributeStatus==2">随机分配</p>
          <p v-if="scope.row.distributeStatus==3">窗口办理</p>
        </template>
      </el-table-column>
      <el-table-column
        label="服务状态"
        align="center"
        prop="serviceStatus"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <p v-if="scope.row.serviceStatus==1">等待服务</p>
          <p v-if="scope.row.serviceStatus==2">服务中</p>
          <p v-if="scope.row.serviceStatus==3">服务完成</p>
        </template>
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
            v-hasPermi="['im:workQueue:view']"
          >查看</el-button
          >
         <!-- <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            v-hasPermi="['im:workQueue:update']"
          >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
            v-hasPermi="['im:workQueue:delete']"
          >删除</el-button
          >-->
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
            <td><i class="require">*</i><b>姓名：</b></td>
            <td>
              <el-form-item prop="name">
                <el-input
                  v-model.trim="form.name"
                  placeholder="姓名"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>手机号：</b></td>
            <td>
              <el-form-item prop="phone">
                <el-input
                  v-model.trim="form.phone"
                  placeholder="手机号"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require"></i><b>身份证号码：</b></td>
            <td colspan="3">
              <el-form-item prop="cardNo">
                <el-input
                  v-model.trim="form.cardNo"
                  placeholder="身份证号码"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>企业名称：</b></td>
            <td>
              <el-form-item prop="companyName">
                <el-input
                  v-model.trim="form.companyName"
                  placeholder="姓名"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>统一社会信用代码：</b></td>
            <td>
              <el-form-item prop="companyCode">
                <el-select
                  v-model="form.companyCode"
                  size="small"
                  style="width: 240px"
                >

                </el-select>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>排队状态：</b></td>
            <td>
              <el-form-item prop="queueStatus">
                <el-select
                  v-model="form.queueStatus"
                  size="small"
                  style="width: 240px"
                >
                <el-option
                  v-for="(value, key) in queueStatusMap"
                  :key="key"
                  :label="value"
                  :value="key"
                />
                </el-select>
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>分配状态：</b></td>
            <td>
              <el-form-item prop="distributeStatus">
                <el-select
                  v-model="form.distributeStatus"
                  size="small"
                  style="width: 240px"
                >
                <el-option
                  v-for="(value, key) in distributeStatusMap"
                  :key="key"
                  :label="value"
                  :value="key"
                />
                </el-select>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>分配时间：</b></td>
            <td>
              <el-form-item prop="distributeTime">
                <el-input
                  v-model.trim="form.distributeTime"
                  placeholder="分配时间"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>服务状态：</b></td>
            <td>
              <el-form-item prop="serviceStatus">
                <el-select
                  v-model="form.serviceStatus"
                  size="small"
                  style="width: 240px"
                >
                  <el-option
                    v-for="(value, key) in serviceStatusMap"
                    :key="key"
                    :label="value"
                    :value="key"
                  />
                </el-select>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>服务工作人员：</b></td>
            <td>
              <el-form-item prop="serviceWorkUserId">
                <el-select
                  v-model="form.serviceWorkUserId"
                  placeholder="请选择服务工作人员"
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
            <td><i class="require"></i><b>服务时长：单位：秒：</b></td>
            <td>
              <el-form-item prop="serviceDuration">
                <el-input
                  v-model.trim="form.serviceDuration"
                  placeholder="服务时长：单位：秒"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>下次服务建议：</b></td>
            <td>
              <el-form-item prop="nextServiceAdvise">
                <el-select
                  v-model="form.nextServiceAdvise"
                  placeholder="下次服务建议"
                  clearable
                  filterable
                  style="width: 170px"
                > <el-option
                  v-for="(value, key) in nextServiceAdviseMap"
                  :key="key"
                  :label="value"
                  :value="key"
                />
                </el-select>
              </el-form-item>
            </td>
            <td><i class="require"></i><b>建议内容</b></td>
            <td>
              <el-form-item prop="adviseMemo">
                <el-input
                  v-model.trim="form.adviseMemo"
                  placeholder="建议内容"
                  maxlength="200"
                  show-word-limit
                />
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
          <td><b>姓名：</b></td>
          <td>
            {{ middleUser.name }}
          </td>
          <td><b>手机号：</b></td>
          <td>
            {{ middleUser.phone }}
          </td>
        </tr>

        <tr>
          <td><b>企业名称：</b></td>
          <td>
            {{ middleUser.companyName }}
          </td>
          <td><b>统一社会信用代码：</b></td>
          <td>
            {{ middleUser.companyCode }}
          </td>
        </tr>
        <tr>
          <td><b>排队状态：</b></td>
          <td>
            {{ middleUser.queueStatus == '1'?'扫码排队中':'导服已分配' }}
          </td>
          <td><b>服务状态：</b></td>
          <td>
            {{ middleUser.serviceStatus | serviceStatusHandle}}
          </td>
        </tr>

        <tr>
          <td><b>分配时间：</b></td>
          <td>
            {{ middleUser.distributeTime  }}
          </td>
          <td><b>分配状态：</b></td>
          <td>
            {{ middleUser.distributeStatus | distributeStatusHandle}}
          </td>
        </tr>
        <tr>
          <td><b>服务工作人员编号：</b></td>
          <td>
            {{ middleUser.serviceWorkUserId }}
          </td>
          <td><b>服务工作人员姓名：</b></td>
          <td>
              <span  v-for="user in userList" v-if="user.id=== middleUser.serviceWorkUserId">
              {{ user.name}}
              </span>
          </td>
        </tr>
        <tr>
          <td><b>服务开始时间：</b></td>
          <td>
            {{ middleUser.serviceBeginTime  }}
          </td>
          <td><b>服务结束时间：</b></td>
          <td>
            {{ middleUser.serviceEndTime }}
          </td>
        </tr>
        <tr>
          <td><b>服务时长：</b></td>
          <td>
            {{ middleUser.serviceDuration  }}
          </td>
          <td><b>下次服务建议：</b></td>
          <td>
            {{ middleUser.nextServiceAdvise | nextServiceAdviseHandle }}
          </td>
        </tr>
        <tr>
          <td><b>建议内容：</b></td>
          <td>
            {{ middleUser.serviceBeginTime  }}
          </td>
          <td><b>排队号：</b></td>
          <td>
            {{ middleUser.windowsNumber }}
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
      <div class="zf-zc-table--title" 
        v-if="middleUser.videoRecordList!=null && middleUser.videoRecordList.length >0">视频帮办记录</div>
      <el-table
        ref="multipleTable"
        :data="middleUser.videoRecordList"
        tooltip-effect="dark"
        style="width: 100%"
        v-if="middleUser.videoRecordList!=null && middleUser.videoRecordList.length >0"
      >
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="名称"
          align="center"
          prop="name"
          show-overflow-tooltip
        />
        <el-table-column
          label="大小（byte）"
          align="center"
          prop="size"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column
          label="创建时间"
          align="center"
          prop="date"
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
              icon="iconfont zfsoft-shizhongshihoujianguan"
              @click="viewVideo(scope.row)"
              v-if="scope.row.url!=null"
            >预览</el-button
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
  del
} from "@/api/ha/QueueManagement/workQueueManager.js";
export default {
  components: {},
  name: "workQueueManager",
  data () {
    return {
      // 遮罩层
      loading: true,
      // statusMap: { "1": "指定人员", "2": "随机分配", "3": "窗口办理" },
      distributeStatusMap: { "1": "指定人员", "2": "随机分配", "3": "窗口办理" },
      serviceStatusMap: { "1": "等待服务", "2": "服务中", "3": "服务完成" },
      nextServiceAdviseMap: { "1": "待服务", "2": "随机分配帮办人员", "3": "手动分配帮办人员" , "4": "窗口取号", "5": "完结"},
      userStatus: { "N": "禁用", "Y": "启用" },
      queueStatusMap: { "1": "扫码排队中", "2": "导服已分配" },
      // 总条数
      total: 0,
      title: "",
      // 数据表格
      middleUserList: [],
      //userList
      userList: [],
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
        companyName: ""
      },
      middleUser:{},
      userAuthRecList:[],
      form: {
      },
      formCheck: {
        userStatus: "2",
        memo: "",
        middleUserOid: ""
      }
      ,
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
        file: [
          // {required:true,message:'必填项',trigger:"blur"}
        ],
        remark: [
          // {required:true,message:'必填项',trigger:"blur"}
          // { validator:validateEmails, trigger: 'blur' }
        ]


      }
    };
  },
  watch: {},
  created () {
    this.getList();
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
      initUser(id).then(response => {
        console.log(response.data)
        this.middleUser = response.data;
        this.openView = true;
        this.title = "查看应用信息";
        initUserList().then(response => {
          console.log(response.data.data)
          this.userList = response.data.data;
        });
      });
    },
    viewVideo(video){
      window.open(video.url);
    },
    // // 是否展示个人登记按钮
    // isShowAuth (row) {
    //   return row.userStatus && row.userStatus.indexOf("1") != -1;
    // },
    // 初始化新增
    handleInit (row) {
      // console.log(row);
      const id = row.id;
      if (id === undefined) {
        initUserList().then(response => {
          this.userList = response.data.data;
        });
        this.form = {};
        this.openInit = true;
        this.title = "新增";
      } else {
        initUser(id).then(response => {
          initUserList().then(response => {
            this.userList = response.data.data;
          });
          this.form = response.data;
          this.openInit = true;
          this.title = "修改";
        });
      }
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
  },
  filters:{
    distributeStatusHandle(data) {
      if(!data) {
        return '随机分配'
      }
      const distributeStatusMap = {
        1: '指定人员',
        2: '随机分配',
        3: '窗口办理'
      }
      return distributeStatusMap[data]
    },
    serviceStatusHandle(data) {
      if(!data) {
        return '等待服务'
      }
      const serviceStatusMap = {
        1: '等待服务',
        2: '服务中',
        3: '服务完成'
      }
      return serviceStatusMap[data]
    },
    nextServiceAdviseHandle(data) {
      if(!data) {
        return '待服务'
      }
      const nextServiceAdviseMap = {
        1: '待服务',
        2: '随机分配帮办人员',
        3: '手动分配帮办人员',
        4: '窗口取号',
        5: '完结'
      }
      return nextServiceAdviseMap[data]
    }
  }
};
</script>
<style></style>
