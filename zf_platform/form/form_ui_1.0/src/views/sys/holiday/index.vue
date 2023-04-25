<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="节假日期">
        <el-date-picker
          v-model="queryParams.holidayDate"
          type="date" value-format="yyyy-MM-dd"
          placeholder="选择节假日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="类型">
        <el-select
          v-model="queryParams.holidayType"
          placeholder="类型"
          clearable
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="(value, key) in holidayTypeMap" :key="key"
            :label="value"
            :value="key"
          />
        </el-select>
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
          v-hasPermi="['sys:holiday:init']"
        >新增
        </el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="holidayList" border>
      <!--<el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="节假日期" align="center" prop="holidayDate"/>
      <el-table-column label="类型" align="center" :formatter="holidayTypeFormat"/>
      <el-table-column label="描述" align="center" prop="memo">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" placement="top">
            <div v-html="ToBreak(scope.row.memo)" slot="content"></div>
            <div class="oneLine">{{scope.row.memo}}</div>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            v-hasPermi="['sys:holiday:init']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
            v-hasPermi="['sys:holiday:delete']"
          >删除
          </el-button>
          <!--<el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-jinyong"
            @click="handleAble(scope.row)"
            v-hasPermi="['sys:holiday:able']"
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


    <!-- 添加或修改应用信息对话框 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="openInit" width="70%" holidayend-to-body>
      <div class="el-table__header-wrapper dialog-table">
        <el-form ref="form" :model="form" :rules="rules" label-width="0px">
          <el-input v-show="false" v-model="form.id"/>
          <el-input v-show="false" v-model="form.isAble"/>
          <el-input v-show="false" v-model="form.isDelete"/>
          <el-input v-show="false" v-model="form.createDate"/>
          <table cellspacing="0" cellpadding="0" border="0" >
            <colgroup>
              <col width="20%" />
              <col width="80%" />
            </colgroup>
            <tr>
              <td><i class="require">*</i><b>节假日期：</b></td>
              <td>
                <el-form-item prop="holidayDate">
                  <el-date-picker
                    v-model="form.holidayDate"
                    type="date" value-format="yyyy-MM-dd"
                    placeholder="选择节假日期">
                  </el-date-picker>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i><b>类型：</b></td>
              <td>
                <el-form-item prop="holidayType">
                  <el-select
                    v-model="form.holidayType"
                    placeholder="请选择类型"
                    clearable
                    size="small"
                    style="width: 240px"
                  >
                    <el-option
                      v-for="(value, key) in holidayTypeMap" :key="key"
                      :label="value"
                      :value="key"
                    />
                  </el-select>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><b>描述：</b></td>
              <td>
                <el-form-item>
                  <el-input v-model.trim="form.memo" type="textarea" placeholder="请输入描述" maxlength="200" show-word-limit></el-input>
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

  </div>
</template>

<script>
  import {init, save, del, getOne, page} from "@/api/sys/holiday";

  export default {
    name: "Holiday",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 表格数据
        holidayList: [],
        // 弹出层标题
        title: "",
        // 新增/修改显示弹出层
        openInit: false,
        // 查看显示弹出层
        openView: false,
        //消息
        ableMap: {'1': '启用', '0': '禁用'},
        holidayTypeMap: {},
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          holidayDate: undefined,
          holidayType: undefined
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          holidayDate: [
            {required: true, message: "请选择节假日期", trigger: "blur"}
          ],
          holidayType: [
            {required: true, message: "请选择类型", trigger: "change"}
          ]
        }
      }
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询节假日列表 */
      getList() {
        this.loading = true;
        let that = this;
        page(this.queryParams).then(response => {
          this.holidayList = response.data.pageResult.data;
          this.holidayTypeMap = response.data.sysHoliday;
          this.total = response.data.pageResult.total;
          this.loading = false;
        }).catch(function() {that.loading = false;});
      },
      // 将 \n 换为 <br/>标签
      ToBreak (val) {
        if(''==val || null==val){
          return val;
        }
        if(val.length>150){
          val = val.slice(0,50) + '<br/>' + val.slice(50,100) + '<br/>' + val.slice(100,150) + '<br/>' + val.slice(150,val.length);
        }else if(val.length>100){
          val = val.slice(0,50) + '<br/>' + val.slice(50,100) + val.slice(100,val.length);
        }else if(val.length>50){
          val = val.slice(0,50) + '<br/>' + val.slice(50,val.length);
        }
        return val;
      },
      // 消息
      statusFormat(row) {
        return this.selectMapLabel(this.ableMap, row.isAble);
      },
      // 类型
      holidayTypeFormat(row) {
        return this.selectMapLabel(this.holidayTypeMap, row.holidayType);
      },
      // 取消按钮
      cancel() {
        this.openInit = false;
        this.reset();
      },
      // 表单重置
      reset() {
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
        this.queryParams={
          pageNum: 1,
          pageSize: 10,
          holidayDate: null,
          holidayType: null
        },
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 新增和修改按钮操作 */
      handleInit(row) {
        this.reset();
        const oid = row.id;
        if (oid == undefined) {
          this.openInit = true;
          this.title = "新增节假日信息";
        } else {
          init(oid).then(response => {
            this.form = response.data;
            this.openInit = true;
            this.title = "修改节假日信息";
          });
        }
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            save(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("保存成功");
                this.openInit = false;
                this.getList();
              }
            });
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
        }).then(function () {
          return del(oid);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      /** 启禁用按钮操作 */
      handleAble(row) {
        const oid = row.id;
        let ableMessage = row.isAble == 1 ? '启用' : '禁用'
        this.$confirm('你确定要' + ableMessage + '吗？', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return able(oid);
        }).then(() => {
          this.msgSuccess(ableMessage + "成功");
        }).catch(function () {
          row.isAble = row.isAble == 0 ? 1 : 0
        });
      }
    }
  }
</script>
<style lang='scss'>
  .oneLine {
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
</style>
