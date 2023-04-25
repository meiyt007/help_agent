<!--author:liyanqing-->
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--首页-->
      <el-col :span="24" :xs="24">
        <!--提交表单-->
        <el-form ref="queryForm" :model="queryParams" :inline="true" label-width="108px" @submit.native.prevent>
          <el-form-item label="征询办件名称" prop="caseName">
            <el-input v-model.trim="queryParams.caseName" placeholder="请输入征询办件名称" clearable size="small"
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="回复状态">
            <el-radio-group v-model="queryParams.replyStatus">
              <el-radio v-for="(status, key) in statusOptions" :key="key" :label="key">{{ status }}</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置
            </el-button>
          </el-form-item>
        </el-form>

        <!--   新增    -->
        <el-row :gutter="10" class="mb8 fl">
          <el-col :span="1.5">
            <el-button type="default" icon="el-icon-plus" size="mini" @click="handleInit"
              v-hasPermi="['sys:consultManage:init']">新增</el-button>
          </el-col>
        </el-row>

        <!--列表-->
        <el-table v-loading="loading" :data="consultManageList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>

          <el-table-column label="征询办件名称" align="center" prop="caseName" />
          <el-table-column label="涉及部门" align="center" prop="organName" />
          <el-table-column label="希望回复时间" align="center" prop="hopeDate" :formatter="dateFormat" />
          <el-table-column label="发起时间" align="center" prop="createDate" />

          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)"
                v-hasPermi="['sys:consultManage:view']">查看</el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" @click="handleInit(scope.row)"
                v-if="scope.row.replyStatus == 0" v-hasPermi="['sys:consultManage:init']">修改</el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDelete(scope.row)"
                v-if="scope.row.replyStatus == 0" v-hasPermi="['sys:consultManage:delete']">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize" @pagination="getList" />
      </el-col>
    </el-row>

    <!-- 添加或修改对话框 -->
    <el-dialog v-dialog-drag :title="title" v-if="addDialogShow" :visible.sync="addDialogShow" width="900px"
      append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="0px" class="dialog-table">
        <table cellspacing="0" cellpadding="0" border="0" class='zf-zc-table'>
          <colgroup>
            <col width="15%" />
            <col width="35%" />
            <col width="15%" />
            <col width="35%" />
          </colgroup>
          <tr>
            <td>
              <i class="require">*</i>
              <b>征询办件选择：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="caseName">
                <el-col :span="24">
                  <el-input :title="form.caseName" @focus="selWinShowEvt" v-model.trim="form.caseName"
                    placeholder="请选择征询办件" maxlength="100" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <i class="require">*</i>
              <b>涉及部门：</b>
            </td>
            <td>
              <el-form-item prop="organOid">
                <el-col :span="24">
                  <el-select v-model="form.organOid" placeholder="请选择涉及部门" :popper-append-to-body="false"
                    @change="changeType">
                    <el-option v-for="organ in organList" :key="organ.organOid" :label="organ.name"
                      :value="organ.organOid" class="category_style">
                    </el-option>
                  </el-select>
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <i class="require">*</i>
              <b>希望回复时间：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="hopeDate">
                <el-col :span="24">
                  <!--                  <el-date-picker v-model="form.hopeDate" type="date" placeholder="选择日期" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss">-->
                  <!--                  </el-date-picker>-->
                  <el-date-picker v-model="form.hopeDate" type="date" placeholder="选择希望回复时间" @change="dataSearch"
                    value-format="yyyy-MM-dd HH:mm:ss" :picker-options="optionDate"></el-date-picker>
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <i class="require">*</i>
              <b>征询内容：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="consultContent">
                <el-col :span="24">
                  <el-input type="textarea" v-model.trim="form.consultContent" placeholder="请输征询内容" maxlength="500"
                    show-word-limit />
                </el-col>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="
            () => {
              addDialogShow = false;
              reset();
            }
          ">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 详细 -->
    <el-dialog v-dialog-drag :title="dialogTitle" :visible.sync="detailDialogShow" v-if="detailDialogShow" scrollbar width="900px"
     height="800px"  append-to-body>
      <div class="el-table__header-wrapper dialog-table">
        <el-form :model="form" label-width="0px">
          <table cellspacing="0" cellpadding="0" border="0">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <div v-if="form.replyStatus == 1" class="option-item">
              <i class="el-icon-s-grid"></i>征询信息
            </div>
            <tr>
              <td>
                <b>征询办件名称：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="name">
                  <el-col :span="24">
                    {{ form.caseName }}
                  </el-col>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>涉及部门：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="cardNumber">
                  <el-col :span="24">
                    {{ form.organName }}
                  </el-col>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>希望回复时间：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="phone">
                  <el-col :span="24">
                    {{ form.hopeDate }}
                  </el-col>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>征询内容：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="dishonestReason">
                  <el-col :span="24">
                    {{ form.consultContent }}
                  </el-col>
                </el-form-item>
              </td>
            </tr>
            <div v-if="form.replyStatus == 1" class="option-item">
              <i class="el-icon-s-grid"></i>回复信息
            </div>
            <tr v-if="form.replyStatus == 1">
              <td>
                <b>回复内容：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="replyContent">
                  <el-col :span="24" style="word-break: break-word;">
                    {{ form.replyContent }}
                  </el-col>
                </el-form-item>
              </td>
            </tr>
            <tr v-if="form.replyStatus == 1">
              <td><b>附件信息：</b></td>
              <td colspan="3">
                <el-col :span="24">
                  <el-form-item label="">
                    <div v-show="null != form.attaOid && '' != form.attaOid">
                      <span>{{ form.attaName }}</span>
                      <el-link type="primary" @click="downloadFile(form.attaOid)">下载</el-link>
                      |
                      <el-link type="primary" @click="viewFileNew(form.attaOid)">预览</el-link>
                    </div>
                  </el-form-item>
                </el-col>
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="zf-text-center">
        <el-button @click="detailDialogShow = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 选择窗口弹窗页面 -->
    <el-dialog v-dialog-drag title="选择征询办件" :visible.sync="selWinShow" v-if="selWinShow" width="900px" append-to-body>
      <el-form :model="queryParams1" ref="queryForm1" :inline="true" label-width="108px">
        <el-row>
          <el-form-item label="办件编号" prop="caseNumber">
            <el-input v-model.trim="queryParams1.caseNumber" placeholder="请输入办件编号" clearable size="100"
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="申请人" prop="applyUserName">
            <el-input v-model.trim="queryParams1.applyUserName" placeholder="请输入申请人" clearable size="100"
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery1">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery1" class="btn-reset">重置
            </el-button>
          </el-form-item>
        </el-row>
      </el-form>
      <el-table ref="multipleTable" :data="windowList" tooltip-effect="dark" height="255" style="width: 100%"
        @current-change="clickChange">
        <el-table-column label="请选择" width="100" align="center">
          <template slot-scope="scope">
            <el-radio v-model="form.caseOid" :label="scope.row.caseOid"><i></i></el-radio>
          </template>
        </el-table-column>
        <el-table-column label="序号" width="80" header-align="center" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="一件事目录名称" align="center" prop="comboDirectoryName" :show-overflow-tooltip="true" />
        <el-table-column label="办件编号" align="center" prop="caseNumber" :show-overflow-tooltip="true" />
        <el-table-column label="项目名称" align="center" prop="projectName" :show-overflow-tooltip="true" />
        <el-table-column label="申请人" align="center" prop="applyUserName" :show-overflow-tooltip="true" />
        <el-table-column label="登记日期" align="center" prop="createDate" :show-overflow-tooltip="true" />
      </el-table>
      <pagination v-show="queryParams1.total > 0" :total="queryParams1.total" :page.sync="queryParams1.pageNum"
        :limit.sync="queryParams1.pageSize" @pagination="getListForWin" />
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitSelWin">确 定</el-button>
        <el-button @click="
            () => {
              selWinShow = false;
            }
          ">取 消</el-button>
      </div>
    </el-dialog>

    <!--引入文件的预览弹出层-->
    <el-dialog v-dialog-drag title="文件预览" :visible.sync="view.show" v-for="view in viewDialogOptions"
      :key='view.attaOid' @close="closeFileView" width="1100px" height='700px' scrollbar append-to-body>
      <file-view :attaOid="view.attaOid" @father-click="closeFileView"></file-view>
    </el-dialog>
  </div>
</template>

<script>
  var moment = require('moment');
  import {
    page,
    get,
    saveOrUpdate,
    deletes,
    casePage,
    getOrganList,
    getDirectory
  } from "@/api/zc/zxgl/consultManage";
  import {
    deepClone
  } from "@/utils";
  import fileView from "@/views/common/fileView";
  import {
    getOne
  } from "@/api/sys/organ";
  export default {
    name: "ConsultManage",
    components: {
      fileView
    },
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 应用表格数据
        consultManageList: [],
        //查看信息弹框标识
        detailDialogShow: false,
        addDialogShow: false,
        cancelDialogShow: false,
        viewDialogOptions: [],
        // 弹出层标题
        title: "",
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          replyStatus: "0",
          caseName: ""
        },
        // 失信状态
        statusOptions: {
          "0": "未回复",
          "1": "已回复"
        },
        // 表单参数
        form: {
          caseName: "",
          caseOid: "",
          organName: "",
          hopeDate: "",
          comboDireOid: ""
        },
        // 表单校验
        rules: {
          caseName: [{
            required: true,
            message: "请选择一件事办件",
            trigger: "blur"
          }],
          organOid: [{
            required: true,
            message: "请选择涉及部门",
            trigger: "blur"
          }],
          hopeDate: [{
            required: true,
            message: "请选择希望回复时间",
            trigger: "blur"
          }],
          consultContent: [{
            required: true,
            message: "请输入征询内容",
            trigger: "blur"
          }],
          replyContent: [{
            required: true,
            message: "请输入回复内容",
            trigger: "blur"
          }]
        },
        //涉及部门
        organList: [],
        // 设置只能选择当前日期及之后的日期
        pickerOptions0: {
          disabledDate(time) {
            return time.getTime() < Date.now() - 8.64e7; //如果没有后面的-8.64e7就是不可以选择今天的
          }
        },
        // 选择一件事办件弹窗查询参数
        queryParams1: {
          pageNum: 1,
          pageSize: 10,
          total: 0 // 分页参数
        },
        windowList: [], // 一件事办件列表
        selWinShow: false, // 一件事办件弹窗
        tableRadio: null, // 一件事办件选中行数据
        optionDate: {
          disabledDate(time) {
            return time.getTime() < Date.now(); // 选当前时间之后的时间
          }
        }
      };
    },
    created() {
      this.getList();
      this.initData({});
    },
    methods: {
      dateFormat(row, column, cellValue) {
        return cellValue ? moment(cellValue).format("YYYY-MM-DD") : "";
      },
      dataSearch() {
        this.getListByDataTime();
      },
      async initData(data) {
        //获取当前时间
        var now = new Date();
        var monthn = now.getMonth() + 1;
        var yearn = now.getFullYear();
        var dayn = now.getDate();
        var h = now.getHours();
        var m = now.getMinutes();
        var s = now.getSeconds();
        this.form.hopeDate =
          yearn + "-" + monthn + "-" + dayn + " " + h + ":" + m + ":" + s;

        this.getListByDataTime();
      },
      async getListByDataTime(data) {},
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 查询列表 */
      getList() {
        this.loading = true;
        page(this.queryParams).then(response => {
          this.consultManageList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        });
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 表单重置
      reset() {
        (this.form = {
          caseName: "",
          caseOid: "",
          organName: ""
        }),
        this.resetForm("form");
      },
      /** 新增和修改按钮操作 */
      handleInit(row) {
        let _that = this;
        _that.reset();
        if (row.id) {
          get(row.id).then(response => {
            _that.form = deepClone(response.data);
            if (_that.form.comboDireOid) {
              getDirectory(_that.form.comboDireOid).then(response => {
                if (response.data) {
                  let os = "";
                  //主办部门
                  let mainOrgan = response.data.mainOrganOid;
                  //协办部门
                  let assistOrgan = response.data.assistOrganOid;
                  //处理主办协办部门主键串
                  if (assistOrgan.indexOf(mainOrgan) != -1) {
                    //包含
                    os = assistOrgan;
                  } else {
                    os = mainOrgan + "," + assistOrgan;
                  }
                  os = os.replace("ORGAN-", "");
                  getOrganList(os).then(response => {
                    if (response) {
                      this.organList = response;
                      _that.addDialogShow = true;
                    }
                  });
                }
              });
            } else {
              _that.addDialogShow = true;
            }
          });
        } else {
          _that.addDialogShow = true;
        }
        _that.title = row.id ? "修改征询信息" : "新增征询信息";
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        let id = row.id;
        this.$confirm("是否确认删除?", "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          })
          .then(() => {
            return deletes(id);
          })
          .then(() => {
            this.getList();
            this.msgSuccess("删除成功");
          })
          .catch(() => {});
      },
      /** 提交按钮 */
      submitForm: function () {
        let _that = this;
        _that.$refs["form"].validate(valid => {
          if (valid) {
            saveOrUpdate(_that.form).then(response => {
              if (response.code === 200) {
                _that.msgSuccess("保存成功");
                _that.addDialogShow = false;
                setTimeout(() => {
                  _that.getList();
                }, 10);
                _that.reset();
              }
            });
          }
        });
      },
      /** 查看按钮操作 */
      handleView(row) {
        let _that = this;
        _that.reset();
        const id = row.id;
        get(id).then(response => {
          _that.form = deepClone(response.data);
          _that.form.hopeDate = _that.dateFormat('', '', _that.form.hopeDate);
          _that.detailDialogShow = true;
          _that.dialogTitle = "查看详情";
        });
      },
      // 选择一件事办件
      selWinShowEvt(e) {
        e.currentTarget.blur();
        Object.assign(this.queryParams1, this.$options.data().queryParams1);
        this.selWinShow = true;
        this.getListForWin();
      },
      /** 一件事办件搜索按钮操作 */
      handleQuery1() {
        this.queryParams1.pageNum = 1;
        this.getListForWin();
      },
      /** 一件事办件重置按钮操作 */
      resetQuery1() {
        this.resetForm("queryForm1");
        this.handleQuery1();
      },
      /** 查询目录列表 */
      getListForWin() {
        let _that = this;
        _that.loading = true;
        casePage(this.queryParams1).then(response => {
          _that.windowList = response.data.data;
          _that.queryParams1.total = response.data.total;
          _that.loading = false;
        });
      },
      // 选择窗口确定按钮
      submitSelWin(row) {
        if (!this.tableRadio) {
          let oidGet = this.form.caseOid;
          if (oidGet != "") {
            let nameGet = this.form.caseName;
            this.tableRadio = new Object();
            this.tableRadio.caseOid = oidGet;
            this.tableRadio.caseName = nameGet;
            this.tableRadio.projectName = nameGet;
          }
        }
        if (!this.tableRadio) {
          this.$message.error("尚未选择一件事办件!");
          return;
        }
        this.form.caseName = this.tableRadio.projectName;
        this.form.caseOid = this.tableRadio.caseOid;
        let comboDireOid = this.tableRadio.comboDireOid;
        this.selWinShow = false;
        this.tableRadio = null;
        //加载目录的相关部门
        this.getOrganList(comboDireOid);
      },
      clickChange(item) {
        this.tableRadio = item;
      },
      //办件的相关部门
      getOrganList(comboDireOid) {
        let _that = this;
        if (comboDireOid) {
          this.form.comboDireOid = comboDireOid;
          getDirectory(comboDireOid).then(response => {
            if (response.data) {
              let os = "";
              //主办部门
              let mainOrgan = response.data.mainOrganOid;
              //协办部门
              let assistOrgan = response.data.assistOrganOid;
              //处理主办协办部门主键串
              if (assistOrgan.indexOf(mainOrgan) != -1) {
                //包含
                os = assistOrgan;
              } else {
                os = mainOrgan + "," + assistOrgan;
              }
              os = os.replace("ORGAN-", "");
              //alert(os.replace('ORGAN-',''))
              getOrganList(os).then(response => {
                if (response) {
                  this.organList = response;
                }
              });
            }
          });
        }
      },
      /** 改变涉及部门 */
      changeType(item) {
        let _that = this;
        _that.organList.forEach(organ => {
          if (organ.organOid == item) {
            _that.form.organName = organ.name;
          }
        });
      },
      //下载附件
      downloadFile(attaOid) {
        this.download(attaOid);
      },
      //预览附件
      viewFileNew(attaOid) {
        let item = {
          show: true,
          attaOid: attaOid
        };
        this.viewDialogOptions.push(item);
      },
      //关闭预览附件
      closeFileView() {
        this.viewDialogOptions.pop();
      }
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
<style lang="scss" scoped>
  .treeselect {
    width: 200px;
  }

  .treeselect240 {
    width: 240px;
  }

</style>
