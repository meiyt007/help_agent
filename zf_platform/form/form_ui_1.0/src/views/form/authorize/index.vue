<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="110px">
      <el-form-item label="接入系统名称" prop="systemName">
        <el-input
          v-model="queryParams.systemName"
          placeholder="请输入接入系统名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item class="fr mr0">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="ml5">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" v-show="isAdminUser" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit"
        >新增</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="authorizeList" border>
      <!--<el-table-column type="selection" width="55" align="center"  />-->
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="接入系统名称" align="center" prop="systemName" :show-overflow-tooltip="true" />
      <el-table-column label="授权key" align="center" prop="authorizeKey" :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createDate" width="200" :show-overflow-tooltip="true"/>
      <el-table-column label="状态" align="center"  width="80" prop="isAble" >
        <template slot-scope="scope">
          <el-switch
            :disabled="!isAdminUser"
            v-model="scope.row.isAble"
            :active-value="1"
            :inactive-value="0"
            @change="handleAble(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="260" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
          >查看</el-button>
          <el-button
            v-if="isAdminUser"
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
          >修改</el-button>
          <el-button
            v-if="isAdminUser"
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
          >删除</el-button>
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


    <!-- 添加或修改接入系统信息对话框 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="openInit" width="70%" append-to-body>
      <div class="el-table__header-wrapper dialog-table">
        <!--<h3>事项标题</h3>-->
        <el-form ref="form" :model="form" :rules="rules" label-width="0px" class="demo-ruleForm">
          <el-input v-show="false" v-model="form.id" />
          <el-input v-show="false" v-model="form.isAble" />
          <el-input v-show="false" v-model="form.isDelete" />
          <el-input v-show="false" v-model="form.createDate" />
          <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><i class="require">*</i><b>接入系统名称：</b></td>
              <td colspan="3">
                <el-form-item prop="systemName">
                  <el-input v-model.trim="form.systemName" placeholder="请输入接入系统名称" maxlength="20" show-word-limit/>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><b>授权key：</b></td>
              <td colspan="3">
                <el-input v-show="false" v-model.trim="form.authorizeKey" />
                <span v-show="null==form.authorizeKey" class="require">授权key自动生成</span>
                <span v-show="null!=form.authorizeKey" class="require">{{form.authorizeKey}}</span>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>授权期限类型：</b></td>
              <td colspan="3">
                <el-form-item prop="timeType">
                  <el-radio-group v-model="form.timeType">
                    <el-radio :label="0">永久</el-radio>
                    <el-radio :label="1">临时</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>
            <tr v-if="form.timeType == 1">
              <td><i class="require">*</i><b>授权开始时间：</b></td>
              <td>
                <el-form-item prop="startTime">
                  <el-date-picker v-model="form.startTime" type="date" value-format="yyyy-MM-dd" format="yyyy-MM-dd"
                                  placeholder="请选择授权开始时间" @change="startTimeStatus"
                                  :picker-options="pickerOptionsStart" style="margin-right: 10px;">
                  </el-date-picker>
                </el-form-item>
              </td>
              <td><i class="require">*</i><b>授权到期时间：</b></td>
              <td>
                <el-form-item prop="endTime">
                  <el-date-picker v-model="form.endTime" type="date" value-format="yyyy-MM-dd" format="yyyy-MM-dd"
                                  placeholder="请选择授权到期时间" @change="endTimeStatus"
                                  :picker-options="pickerOptionsEnd" style="margin-left: 10px;">
                  </el-date-picker>
                </el-form-item>
              </td>
            </tr>

            <tr>
              <td><b>备注：</b></td>
              <td colspan="3">
                <el-form-item>
                  <el-input v-model.trim="form.remark" type="textarea" placeholder="请输入备注" maxlength="500" show-word-limit></el-input>
                </el-form-item>
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


    <!-- 查看接入系统信息详细 -->
    <el-dialog :title="title" :visible.sync="openView" width="70%" append-to-body>
      <div class="el-table__header-wrapper dialog-table">
        <!--<h3>事项标题</h3>-->
        <el-form :model="form" label-width="0px" class="demo-ruleForm">
          <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>接入系统名称：</b></td>
              <td colspan="3">
                {{ form.systemName }}
              </td>
            </tr>
            <tr>
              <td><b>授权key：</b></td>
              <td colspan="3">
                {{ form.authorizeKey }}
              </td>
            </tr>
            <tr>
              <td><b>授权期限类型：</b></td>
              <td colspan="3">
                <span v-if="form.timeType == 1">临时</span>
                <span v-if="form.timeType == 0">永久</span>
              </td>
            </tr>
            <tr v-if="form.timeType == 1">
              <td><b>授权开始时间：</b></td>
              <td>
                {{ form.startTime }}
              </td>
              <td><b>授权到期时间：</b></td>
              <td>
                {{ form.endTime }}
              </td>
            </tr>
            <tr>
              <td><b>备注：</b></td>
              <td colspan="3">
                {{ form.remark }}
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { init, save, del ,getOne, page, able } from "@/api/form/authorize";
  export default {
    name: "Authorize",
    props:["authorizeKey","isAdminUser"],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 接入系统表格数据
        authorizeList: [],
        // 弹出层标题
        title: "",
        // 新增/修改显示弹出层
        openInit: false,
        // 查看显示弹出层
        openView: false,
        // 启用状态
        ableMap:{'1':'启用','0':'禁用'},
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: ''
        },
        // 表单参数
        form: {},
        pickerOptionsStart: {
          disabledDate: time => {
            let endDateVal = this.form.endTime;
            if (endDateVal) {
              return time.getTime() > new Date(endDateVal).getTime();
            }
          }
        },
        pickerOptionsEnd: {
          disabledDate: time => {
            let beginDateVal = this.form.startTime;
            if (beginDateVal) {
              return (
                time.getTime() < new Date(beginDateVal).getTime()
              );
            }
          },
        },
        // 表单校验
        rules: {
          systemName: [
            { required: true, message: "接入系统名称不能为空", trigger: "blur" },
          ],
          timeType: [
            { required: true, message: "请选择授权期限类型", trigger: "blur" },
          ],
          startTime: [
            { required: true, message: "授权开始时间不能为空", trigger: "blur" },
          ],
          endTime: [
            { required: true, message: "授权到期时间不能为空", trigger: "blur" },
          ],
        }
      };
    },
    created() {
      this.getList();
    },
    methods: {
      // 时间开始选择器
      startTimeStatus:function(value){
        this.form.startTime = value
      },
      // 时间结束选择器
      endTimeStatus:function(value){
        this.form.endTime = value
      },
      /** 查询接入系统列表 */
      getList() {
        this.loading = true;
        let that = this;
        page(this.queryParams).then(response => {
          this.authorizeList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        }).catch(function () {
          that.loading = false;
        });
      },
      // 启用状态
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
        this.queryParams = {};
        this.queryParams.pageNum = 1;
        this.queryParams.pageSize = 10;
        this.handleQuery();
      },
      /** 查看按钮操作 */
      handleView(row) {
        this.reset();
        const oid = row.id;
        getOne(oid).then(response => {
          this.form = response.data;
          this.openView = true;
          this.title = "查看接入系统信息";
        });
      },
      /** 新增和修改按钮操作 */
      handleInit(row) {
        this.reset();
        const oid = row.id;
        if(oid == undefined){
          this.form = {timeType:0};
          this.openInit = true;
          this.title = "新增接入系统信息";
        }else {
          init(oid).then(response => {
            this.form = response.data;
            this.openInit = true;
            this.title = "修改接入系统信息";
          });
        }
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            save(this.form).then(response => {
              console.log(response.data)
              if (response.code === 200) {
                this.msgSuccess("保存成功");
                this.openInit = false;
                this.getList();
                if (this.form.authorizeKey == undefined || null == this.form.authorizeKey) {
                  //新增一个节点
                  this.$emit('appendAuthorize',response.data.authorizeKey,this.form.systemName);
                } else {
                  //更新一个节点
                  this.$emit('updateAuthorize',response.data.authorizeKey,this.form.systemName);
                }
              }
            });
          }else {
            return false;
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const oid = row.id;
        this.$confirm('是否确认删除吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return del(oid);
        }).then(() => {
          this.$emit('removeAuthorize',row.authorizeKey);
          this.authorizeKey = null;
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function() {});
      },
      /** 启禁用按钮操作 */
      handleAble(row) {
        const oid = row.id;
        let ableMessage = row.isAble === 1 ? '启用' : '禁用'
        this.$confirm('你确定要' + ableMessage + '吗？', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return able(oid);
        }).then(() => {
          this.msgSuccess(ableMessage+"成功");
          if(row.isAble === 1){
            //启用一个节点
            this.$emit('appendAuthorize',row.authorizeKey,row.systemName);
          }else{
            //禁用一个节点
            this.$emit('removeAuthorize',row.authorizeKey);
          }
        }).catch(function() {
          row.isAble = row.isAble === 0 ? 1 : 0
        });
      },

    }
  };
</script>
<style scoped>
  .app-container{
    padding-top: 0px !important;
  }
</style>
