/** * @Author: kkfan * @Date: 2020-10-23 14:04:48 * @Last Modified by: kkfan *
@Last Modified time: 2020-10-31 10:43:16 * @Description: 受理辖区管理 */
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--区划数据-->
      <el-col :span="24" :xs="24">
        <el-form
          :model="queryParams"
          ref="queryForm"
          :inline="true"
          label-width="108px"
          @submit.native.prevent
        >
          <el-form-item label="受理辖区名称" prop="siteName">
            <el-input
              v-model.trim="queryParams.siteName"
              placeholder="请输入受理辖区名称"
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
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="default"
              icon="el-icon-plus"
              size="mini"
              @click="handleInit"
              v-hasPermi="['sys:AreaSite:init']"
              >新增</el-button
            >
          </el-col>
        </el-row>

        <el-table v-loading="loading" :data="districtList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="受理辖区名称"
            align="center"
            prop="siteName"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="受理辖区编号"
            align="center"
            prop="siteNumber"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="辖区区划"
            align="center"
            prop="districtionName"
            :show-overflow-tooltip="true"
          />
          <el-table-column label="启用状态" align="center" prop="isAble">
            <template slot-scope="scope">
              <el-switch
                :value="scope.row.isAble == 1"
                @change="handleAble(scope.row)"
              ></el-switch>
            </template>
          </el-table-column>
          <el-table-column
            label="创建时间"
            width="200"
            align="center"
            prop="createDate"
            :show-overflow-tooltip="true"
          />
          <!--          <el-table-column label="排序号" width="100" align="center" prop="sort"/>-->
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
                v-hasPermi="['sys:AreaSite:view']"
                >查看</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="handleInit(scope.row)"
                v-hasPermi="['sys:AreaSite:update']"
                >修改</el-button
              >
              <!-- <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDelete(scope.row)" v-hasPermi="['sys:district:delete']">删除</el-button> -->
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
    <!-- 添加或修改区划信息对话框 -->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openInit"
      v-if="openInit"
      width="900px"
      append-to-body
    >
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
              <td>
                <i class="require">*</i>
                <b>受理辖区名称：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="siteName">
                  <el-col :span="24">
                    <el-input
                      v-model.trim="form.siteName"
                      placeholder="请输入受理辖区名称"
                      maxlength="50"
                      show-word-limit
                    />
                  </el-col>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <i class="require">*</i>
                <b>受理辖区编号：</b>
              </td>
              <td>
                <el-form-item prop="siteNumber">
                  <el-col :span="24">
                    <el-input
                      v-model.trim="form.siteNumber"
                      placeholder="请输入受理辖区编号"
                      maxlength="25"
                      show-word-limit
                    />
                  </el-col>
                </el-form-item>
              </td>
              <td>
                <b>排序号：</b>
              </td>
              <td>
                <el-form-item prop="sort">
                  <el-col :span="24">
                    <el-input-number v-model="form.sort" :min="1" :max="9999" />
                  </el-col>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <i class="require">*</i>
                <b>受理辖区办事地址：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="siteAddress">
                  <el-col :span="24">
                    <el-input
                      v-model.trim="form.siteAddress"
                      placeholder="请输入受理辖区办事地址"
                      maxlength="250"
                      show-word-limit
                    />
                  </el-col>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>辖区区划：</b>
              </td>
              <td colspan="3">
                <!--<el-form-item prop="districtOidArr">
                <el-col :span="24">
                  <treeselect
                    :multiple="true"
                    :options="districtOptions"
                    :flat="true"
                    :default-expand-level="1"

                    placeholder="请选择辖区区划"
                    v-model="form.districtOidArr"
                  />
                </el-col>
              </el-form-item>-->

                <el-row>
                  <el-col :span="24">
                    <el-form-item prop="districtOidArr">
                      <!-- <treeselect
                      :multiple="true"
                      :options="districtOptions"
                      :flat="true"
                      :default-expand-level="1"

                      placeholder="请选择辖区区划"
                      v-model="form.districtOidArr"
                    />-->
                      <treeselect
                        class="jurisdiction"
                        :multiple="true"
                        :options="districtOptions"
                        :flat="true"
                        :default-expand-level="1"
                        placeholder="请选择辖区区划"
                        v-model="form.districtOidArr"
                      />
                    </el-form-item>
                  </el-col>
                </el-row>
              </td>
            </tr>
            <tr>
              <td>
                <b>备注：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="remark">
                  <el-col :span="24">
                    <el-input
                      type="textarea"
                      placeholder="请选择输入备注"
                      v-model.trim="form.remark"
                      maxlength="2000"
                      show-word-limit
                    ></el-input>
                  </el-col>
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

    <!-- 区划信息详细 -->
    <el-dialog
      v-dialog-drag
      :title="title"
      :visible.sync="openView"
      width="800px"
      v-if="openView"
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
              <b>受理辖区名称：</b>
            </td>
            <td colspan="3">
              {{ form.siteName }}
            </td>
          </tr>
          <tr>
            <td>
              <b>受理辖区编号：</b>
            </td>
            <td>
              {{ form.siteNumber }}
            </td>
            <td>
              <b>排序号：</b>
            </td>
            <td>
              {{ form.sort }}
            </td>
          </tr>
          <tr>
            <td>
              <b>受理辖区办事地址：</b>
            </td>
            <td colspan="3">
              {{ form.siteAddress }}
            </td>
          </tr>
          <tr>
            <td>
              <b>辖区区划：</b>
            </td>
            <td colspan="3">
              <treeselect
                :multiple="true"
                :options="districtOptions"
                :flat="true"
                :default-expand-level="1"
                placeholder=""
                v-model="form.districtOidArr"
                :disabled="true"
              />
            </td>
          </tr>
          <tr>
            <td>
              <b>备注：</b>
            </td>
            <td colspan="3">
              {{ form.remark }}
            </td>
          </tr>
        </table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  page,
  able,
  saveOrUpdate,
  getOne
} from "@/api/zc/sysRunConfiguration/sysAreaSite";
import { queryDistrictSimpleTreeForSelected } from "@/api/sys/district";
import { validateNumber, validIntNoZero } from "@/utils/validate";
// import the component
import Treeselect from "@riophae/vue-treeselect";
// import the styles
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  name: "SysAreaSite",
  components: { Treeselect },
  data() {
    return {
      selectArr: [],
      // 区划树
      districtOptions: [],
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      districtList: [],
      //查询区划名称参数
      districtName: "",
      //查询所用参数
      districtOidsForParam: "",
      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      //启禁用状态
      ableMap: { Y: "启用", N: "禁用" },
      // 级别字典
      levelDictOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        siteName: ""
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单参数
      form: {
        id: "", //逻辑主键
        siteName: "", // 受理辖区名称
        siteNumber: "", // 受理辖区编号
        sort: 0, // 排序号
        siteAddress: "", // 受理辖区办事地址
        districtOids: "", // 辖区区划
        districtOidArr: [],
        remark: "" // 备注
      },
      // 表单校验
      rules: {
        siteName: [
          { required: true, message: "受理辖区名称不能为空", trigger: "blur" }
        ],
        siteNumber: [
          { required: true, message: "受理辖区编号不能为空", trigger: "blur" }
        ],
        siteAddress: [
          {
            required: true,
            message: "受理辖区办事地址不能为空",
            trigger: "blur"
          }
        ],
        code: [
          { required: true, message: "区划编码不能为空", trigger: "blur" },
          { validator: validateNumber, trigger: "blur" },
          { min: 6, max: 10, message: "请输入6-10位长度的数字" }
        ],
        levelDictOid: [
          { required: true, message: "请选择级别", trigger: "blur" }
        ],
        sort: [
          { required: true, message: "排序号不能为空", trigger: "blur" },
          { validator: validIntNoZero, trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    // this.getDistrictTree();
  },
  methods: {
    /** 获取区划树 */
    getDistrictTree(districtOid) {
      let _that = this;
      queryDistrictSimpleTreeForSelected(
        this.districtOidsForParam,
        districtOid
      ).then(response => {
        _that.districtOptions = response.data;
        _that.districtOptions.forEach(options => {
          if (options.disabled == true) {
            options.isDisabled = true;
          } else {
            options.isDisabled = false;
          }
          if (null != options.children && options.children.length > 0) {
            let firstChlidList = options.children;
            firstChlidList.forEach(firstChlid => {
              if (firstChlid.disabled == true) {
                firstChlid.isDisabled = true;
              } else {
                firstChlid.isDisabled = false;
              }

              if (
                null != firstChlid.children &&
                firstChlid.children.length > 0
              ) {
                let secondChlidList = firstChlid.children;
                secondChlidList.forEach(secondChlid => {
                  if (secondChlid.disabled == true) {
                    secondChlid.isDisabled = true;
                  } else {
                    secondChlid.isDisabled = false;
                  }

                  if (
                    null != secondChlid.children &&
                    secondChlid.children.length > 0
                  ) {
                    let lastChlidList = secondChlid.children;
                    lastChlidList.forEach(lastChlid => {
                      if (lastChlid.disabled == true) {
                        lastChlid.isDisabled = true;
                      } else {
                        lastChlid.isDisabled = false;
                      }
                    });
                  }
                });
              }
            });
          }
        });
      });
    },
    /** 查询受理辖区列表 */
    getList() {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.districtList = response.data.data;
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
      Object.assign(this.form, this.$options.data().form);
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
      const oid = row.id;
      getOne(oid).then(response => {
        _that.form = response.data;
        _that.openView = true;
        _that.form.districtOidArr = response.data.districtOids
          ? response.data.districtOids.split(";")
          : [];
        _that.title = "查看受理辖区信息";
        _that.getDistrictTree();
      });
    },
    /** 新增和修改按钮操作 */
    handleInit(row) {
      let _that = this;
      _that.reset();
      if (row.id) {
        getOne(row.id).then(response => {
          _that.openInit = true;
          _that.form = response.data;
          _that.districtOidsForParam = response.data.districtOidsForParam;
          _that.form.districtOidArr = response.data.districtOids
            ? response.data.districtOids.split(";")
            : [];
          _that.getDistrictTree();
        });
      } else {
        _that.openInit = true;
        _that.districtOidsForParam = "";
        _that.getDistrictTree();
      }
      _that.title = row.id ? "修改受理辖区信息" : "新增受理辖区信息";
    },
    /** 提交按钮 */
    submitForm: function() {
      let _that = this;
      _that.$refs["form"].validate(valid => {
        if (valid) {
          _that.form.districtOids = _that.form.districtOidArr.join(";");
          saveOrUpdate(_that.form).then(response => {
            if (response.code === 200) {
              _that.msgSuccess("保存成功");
              _that.openInit = false;
              setTimeout(() => {
                _that.getList();
              }, 10);
            }
          });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const oid = row.oid;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          return del(oid);
        })
        .then(() => {
          const node = this.$refs.tree.getCurrentNode();
          this.remove(node, oid);
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 启禁用按钮操作 */
    handleAble(row) {
      let id = row.id,
        ableMessage = row.isAble == "0" ? "启用" : "禁用";
      row.isAble = row.isAble == 0 ? 1 : 0;
      this.$confirm("你确定要" + ableMessage + "吗？", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          return able(id);
        })
        .then(() => {
          this.msgSuccess(ableMessage + "成功");
        })
        .catch(() => {
          row.isAble = row.isAble == 0 ? 1 : 0;
        });
    }
  }
};
</script>
<style lang="scss" scoped>
table {
  border-collapse: collapse;
}
</style>
