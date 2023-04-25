/**
* @Author: kkfan
* @Date: 2020-10-26 18:21:27
 * @Last Modified by: kkfan
 * @Last Modified time: 2020-10-31 10:42:51
* @Description: 受理辖区设置
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--区划数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="108px" @submit.native.prevent>
          <el-form-item label="设备编号" prop="deviceId">
            <el-input v-model.trim="queryParams.deviceId" placeholder="请输入设备编号" clearable size="deviceId" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置</el-button>
          </el-form-item>
        </el-form>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="default" icon="el-icon-plus" size="mini" @click="handleInit" v-hasPermi="['sys:AreaSite:init']">新增</el-button>
          </el-col>
        </el-row>

        <el-table v-loading="loading" :data="humanEvidenceDeviceList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="设备编号"  align="center" prop="deviceId"/>
          <el-table-column label="SALT"  align="center" prop="salt"/>
          <el-table-column label="IP地址" align="center"  prop="ipAddress"/>
          <el-table-column label="绑定窗口"  align="center" prop="bindingWindowNum"/>
          <el-table-column label="创建时间"  align="center" prop="createTime"/>

          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)" v-hasPermi="['sys:district:view']" >查看</el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai"  @click="handleInit(scope.row)" v-hasPermi="['sys:district:update']">修改</el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDelete(scope.row)" v-hasPermi="['sys:district:delete']">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="queryParams.total > 0" :total="queryParams.total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
      </el-col>
    </el-row>
    <!-- 选择窗口弹窗页面 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" title="选择窗口" :visible.sync="selWinShow" v-if="selWinShow" width="900px" append-to-body>
      <el-table ref="multipleTable" :data="windowList" tooltip-effect="dark" height="255" style="width: 100%" @current-change="clickChange">
        <el-table-column label="请选择" width="100" align="center">
          <template slot-scope="scope">
            <el-radio  v-model="windowOid" :label="scope.row.windowOid"><i></i></el-radio>
          </template>
        </el-table-column>
        <el-table-column label="序号" header-align="center" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="districtName" label="所属区划" header-align="center" align="center"></el-table-column>
        <el-table-column prop="organName" label="所属机构" header-align="center" align="center"></el-table-column>
        <el-table-column prop="windowNum" label="窗口编号" header-align="center" align="center"></el-table-column>
      </el-table>
      <pagination v-show="queryParams1.total > 0" :total="queryParams1.total" :page.sync="queryParams1.pageNum" :limit.sync="queryParams1.pageSize" @pagination="getListForWin"/>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitSelWin">确 定</el-button>
        <el-button @click="() => {selWinShow = false}">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 添加或修改对话框 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="dialogTitle" v-if="addDialogShow" :visible.sync="addDialogShow" width="900px" append-to-body>
      <div>
      <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><i class="require">*</i>
              <b>设备编号：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="deviceId">
                <el-col :span="24">
                  <el-input v-model.trim="form.deviceId" placeholder="请输入设备编号" maxlength="50" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i>
              <b>所属区划：</b>
            </td>
            <td>
              <el-form-item prop="districtName">
                <el-col :span="24">
                  <el-form-item prop="districtOid">
                    <el-col :span="24">
                      <treeselect
                        :options="districtOptions"
                        :default-expand-level="1"
                        placeholder="请选择区划"
                        v-model="form.districtOid"
                        @select="districtSel"
                      />
                    </el-col>
                  </el-form-item>
                  <!-- <el-input v-model="form.districtName" placeholder="请输入所属区划" maxlength="25" show-word-limit /> -->
                </el-col>
              </el-form-item>
            </td>
            <td><i class="require">*</i>
              <b>所属组织机构：</b>
            </td>
            <td>
              <el-form-item prop="organName">
                <el-col :span="24">
                  <el-form-item prop="organOid">
                    <el-col :span="24">
                      <treeselect
                        :options="listOrganOptions"
                        noOptionsText="暂无数据"
                        :default-expand-level="1"
                        placeholder="请选择所属组织机构"
                        v-model="form.organOid"
                        @select="organSel"
                      />
                    </el-col>
                  </el-form-item>
                  <!-- <el-input v-model="form.organName" placeholder="请选择所属组织机构" maxlength="25" show-word-limit /> -->
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i>
              <b>绑定窗口：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="bindingWindowNum">
                <el-col :span="24" id="windowListSel">
                  <el-input @focus="selWinShowEvt" v-model="form.bindingWindowNum" placeholder="请选择绑定窗口" maxlength="100" show-word-limit/>
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i>
              <b>IP地址：</b>
            </td>
            <td>
              <el-form-item prop="ipAddress">
                <el-col :span="24">
                  <el-input v-model.trim="form.ipAddress" placeholder="请输入IP地址" maxlength="25" show-word-limit/>
                </el-col>
              </el-form-item>
            </td>
            <td><i class="require">*</i>
              <b>MAC地址：</b>
            </td>
            <td>
              <el-form-item prop="macAddress">
                <el-col :span="24">
                  <el-input v-model.trim="form.macAddress" placeholder="请输入MAC地址" maxlength="50" show-word-limit/>
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i>
              <b>salt：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="salt">
                <el-col :span="24">
                  <el-input v-model.trim="form.salt" placeholder="请输入salt" maxlength="100" show-word-limit/>
                </el-col>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="() => {addDialogShow = false; reset()}">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 区划信息详细 -->
    <el-dialog v-dialog-drag :title="dialogTitle" v-if="detailDialogShow" :visible.sync="detailDialogShow" width="800px" append-to-body>
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
              <b>设备编号：</b>
            </td>
            <td colspan="3">
              {{ form.deviceId }}
            </td>
          </tr>
          <tr>
            <td>
              <b>所属区划：</b>
            </td>
            <td>
              <treeselect
                :options="districtOptions"
                :default-expand-level="1"
                placeholder=""
                v-model="form.districtOid"
                @select="districtSel"
                :disabled="true"
              />
            </td>
            <td>
              <b>所属组织机构：</b>
            </td>
            <td>
              <treeselect
                :options="listOrganOptions"
                noOptionsText="暂无数据"
                :default-expand-level="1"
                placeholder=""
                v-model="form.organOid"
                @select="organSel"
                :disabled="true"
              />
            </td>
          </tr>
          <tr>
            <td>
              <b>绑定窗口：</b>
            </td>
            <td colspan="3">
              {{ form.bindingWindowNum }}
            </td>
          </tr>
          <tr>
            <td>
              <b>IP地址：</b>
            </td>
            <td>
              {{ form.ipAddress }}
            </td>
            <td>
              <b>MAC地址：</b>
            </td>
            <td>
              {{ form.macAddress }}
            </td>
          </tr>
          <tr>
            <td>
              <b>salt：</b>
            </td>
            <td colspan="3">
              {{ form.salt }}
            </td>
          </tr>
        </table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogShow = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { page, saveOrUpdate, getOne, deletes } from "@/api/zc/sysRunConfiguration/humanEvidenceDeviceMgt.js";
  import { page as winListPage } from "@/api/zc/sysRunConfiguration/entityWindowManage";
  import { queryDistrictSimpleTree } from "@/api/sys/district";
  import { validIpAddress,validMacAddress} from "@/utils/validate";
  import { queryOrganTree } from "@/api/sys/organ";
  import { deepClone } from "@/utils/index";
  // import the component
  import Treeselect from '@riophae/vue-treeselect';
  // import the styles
  import '@riophae/vue-treeselect/dist/vue-treeselect.css';
  export default {
    name: "HumanEvidenceDeviceMgt",
    components: {Treeselect},
    data() {
      return {
        // 窗口选中数据
        tableRadio: null,
        windowOid: null,
        selWinShow: false,  // 选择窗口弹窗
        windowList: [],
        // 机构数据
        listOrganOptions: [],
        // 列表数据
        humanEvidenceDeviceList: [],
        // 弹窗标题
        dialogTitle: '',
        addDialogShow: false,
        detailDialogShow: false,
        // 区划树
        districtOptions: [],
        // 列表查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          total: 0, // 分页参数
          deviceId: ""
        },
        // 窗口弹窗查询参数
        queryParams1: {
          pageNum: 1,
          pageSize: 10,
          total: 0, // 分页参数
          organOid: ""
        },
        // 表单参数
        form: {
          id: '', //逻辑主键
          deviceId: '', // 设备编号
          districtName: null, // 所属区划
          districtOid: null, // 区划id
          organName: null,  // 所属组织机构
          organOid: null,  // 所属组织id
          bindingWindowNum: '', // 绑定窗口编号
          bindingWindow: '', // 绑定窗口id
          ipAddress: '',  // ip地址
          macAddress: '', // mac地址
          salt: '', // salt
        },
        // 表单校验
        rules: {
          deviceId: [
            { required: true, message: "设备编号不能为空", trigger: "blur" }
          ],
          districtOid: [
            { required: true, message: "请选择所属区划", trigger: "blur" }
          ],
          organOid: [
            { required: true, message: "请选择所属组织机构", trigger: "blur" }
          ],
          bindingWindowNum: [
            { validator: this.validBanWin, trigger: "blur" },
            { required: true, message: "请选择绑定窗口", trigger: "change" }
          ],
          ipAddress: [
            { required: true, message: "ip地址不能为空", trigger: "blur" },
            {validator: validIpAddress, trigger: 'blur'}
          ],
          macAddress: [
            { required: true, message: "mac地址不能为空", trigger: "blur" },
            {validator: validMacAddress, trigger: 'blur'}
          ],
          salt: [
            { required: true, message: "salt不能为空", trigger: "blur" }
          ],
        }
      };
    },
    methods: {
      /** 删除按钮操作 */
      handleDelete(row) {
        let id = row.id;
        this.$confirm("是否确认删除?", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(function() {
            return deletes(id);
          })
          .then(() => {
          this.getList();
        this.msgSuccess("删除成功");
      })
      .catch(function() {});
      },
      /** 查看按钮操作 */
      handleView(row) {
        let _that = this;
        _that.reset();
        const oid = row.id;
        getOne(oid).then(response => {
          if(response.data.districtOid) {
          _that.getListOrganTree(response.data.districtOid, () => {
            _that.form.organOid = response.data.organOid;
          _that.form.districtOid = response.data.districtOid;
          _that.windowOid = response.data.bindingWindow;
        });
        }
        _that.form = deepClone(response.data);
        _that.detailDialogShow = true;
        _that.dialogTitle = "查看详情";
      });
      },
      validBanWin(rule, value, callback) {
        let _that = this;
        if(!_that.form.districtOid) {
          callback(new Error('请先选择所属区划！'));
        } else if(!_that.form.organOid) {
          callback(new Error('请先选择所属组织机构！'));
        } else {
          callback();
        }
      },
      // 选择窗口确定按钮
      submitSelWin(row) {
        if(!this.tableRadio) {
          this.$message.error('尚未选择窗口!');
          return;
        }
        this.form.bindingWindowNum = this.tableRadio.windowNum;
        this.form.bindingWindow = this.tableRadio.windowOid;
        this.selWinShow = false;
      },
      // 选择窗口弹窗
      selWinShowEvt(e) {
        e.currentTarget.blur();
        if(!this.form.districtOid) {
          return;
        }
        if(!this.form.organOid) {
          return;
        }
        Object.assign(this.queryParams1, this.$options.data().queryParams1)
        this.selWinShow = true;
        this.getListForWin();
      },
      clickChange (item) {
        this.tableRadio = item
      },
      districtSel(node, instanceId) {
        this.form.districtName = node.label;
      },
      organSel(node, instanceId) {
        this.form.organName = node.label;
      },
      /** 获取机构数据 */
      getListOrganTree(districtOid, callback) {
        if (districtOid) {
          districtOid = null!=districtOid?districtOid.substring(districtOid.lastIndexOf('-')+1,districtOid.length):'';
          queryOrganTree(districtOid).then(response => {
            this.listOrganOptions = response.data;
          callback && callback();
        });
        } else {
          this.listOrganOptions = []
          this.queryParams.organOid = null
        }
      },
      /** 获取区划树 */
      getDistrictTree(districtOid) {
        let _that = this;
        queryDistrictSimpleTree(districtOid).then(response => {
          _that.districtOptions = response.data;
      });
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 查询受理辖区列表 */
      getList() {
        this.loading = true;
        page(this.queryParams).then(response => {
          this.humanEvidenceDeviceList = response.data.data;
        this.queryParams.total = response.data.total;
        this.loading = false;
      });
      },
      /** 查询弹窗窗口列表 */
      getListForWin() {
        let _that = this;
        _that.queryParams1.organOid = _that.form.organOid;
        winListPage(_that.queryParams1).then(response => {
          _that.windowList = response.data.data;
        _that.queryParams1.total = response.data.total;
        _that.loading = false;
      });
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 表单重置
      reset() {
        Object.assign(this.form, this.$options.data().form)
        this.resetForm("form");
      },
      /** 新增和修改按钮操作 */
      handleInit(row) {
        let _that = this;
        _that.reset();
        if(row.id) {
          getOne(row.id).then(response => {
            _that.addDialogShow = true;
          if(response.data.districtOid) {
            _that.getListOrganTree(response.data.districtOid, () => {
              _that.form.organOid = response.data.organOid;
            _that.form.districtOid = response.data.districtOid;
            _that.windowOid = response.data.bindingWindow;
          });
          }
          _that.form = deepClone(response.data);
        });
        } else {
          _that.addDialogShow = true;
        }
        _that.dialogTitle = row.id ? "修改" : "新增";
      },
      /** 提交按钮 */
      submitForm: function() {
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
            }
          });
          }
        });
      },
    },
    watch: {
      'form.districtOid': function (val, oldVal) {
        if(!val) {
          this.form.districtName = null;
        }
        this.form.organOid = null;
        this.form.organName = null;
        //机构加载
        this.getListOrganTree(val)
      },
      'form.organOid': function (val, oldVal) {
        if(!val) {
          this.form.organName = null;
        }
      },
    },
    created() {
      this.getList();
      this.getDistrictTree();
    },
  };
</script>
