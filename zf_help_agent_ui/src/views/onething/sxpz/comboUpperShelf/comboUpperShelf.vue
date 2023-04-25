/** * @Author: wangns */
<template>
  <div class="app-container">
    <!---->
    <!--数据-->
    <el-col :span="24" :xs="24">
      <el-form
        :model="queryParamsForCombo"
        ref="queryFormForCombo"
        :inline="true"
        label-width="110px"
      >
        <el-form-item label="目录名称" prop="comboDirectoryName">
          <el-input
            v-model.trim="queryParamsForCombo.comboDirectoryName"
            placeholder="请输入目录名称"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="分类名称" prop="themeName">
          <el-input
            v-model.trim="queryParamsForCombo.themeName"
            placeholder="请输入分类名称"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="上架状态" prop="comboUpperShelfStatus">
          <el-select
            v-model="queryParamsForCombo.comboUpperShelfStatus"
            clearable
            placeholder="请选择"
          >
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <br />
        <el-form-item label="服务端类型" prop="elements">
          <el-select
            v-model="queryParamsForCombo.elements"
            placeholder="请选择"
          >
            <el-option
              v-for="item in elements"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item class="fr mr0">
          <el-button
            type="primary"
            icon="el-icon-search"
            size="mini"
            @click="handleQuery()"
            >搜索</el-button
          >
          <el-button
            type="warning"
            icon="el-icon-refresh"
            size="mini"
            @click="resetQuery()"
            class="btn-reset"
            >重置</el-button
          >
          <el-button
            type="danger"
            icon="el-icon-sort"
            size="mini"
            @click="sortDirectory()"
            >目录排序</el-button
          >
        </el-form-item>
      </el-form>
      <el-table v-loading="loading" :data="comboDirectories" border :fit="true">
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="目录名称"
          align="center"
          prop="comboDirectoryName"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="分类名称"
          align="center"
          prop="themeName"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="编码"
          align="center"
          prop="comboDirectoryCode"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="发布状态"
          align="center"
          prop="status"
          :formatter="statusName"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="上架状态"
          align="center"
          prop="comboUpperShelfStatus"
        >
          <template slot-scope="scope">
            <span
              v-if="
                (scope.row.comboUpperShelfStatus == 1 ||
                  scope.row.comboUpperShelfStatus == 3) &&
                  scope.row.windowStatus == 1
              "
              title="窗口端已上架"
              ><i
                class="iconfont zfsoft-chuangkoubanliliucheng"
                style="color: #1890FF;"
              ></i
            ></span>
            <span
              v-if="
                (scope.row.comboUpperShelfStatus == 0 &&
                  scope.row.elements &&
                  scope.row.elements.indexOf('1') != -1) ||
                  scope.row.windowStatus == 0
              "
              title="窗口端已下架"
              ><i
                class="iconfont zfsoft-chuangkoubanliliucheng"
                style="color: #999988;"
              ></i
            ></span>
            <span
              v-if="
                scope.row.windowStatus == 2 &&
                  scope.row.elements &&
                  scope.row.elements.indexOf('1') != -1
              "
              title="窗口端未上架"
              ><i
                class="iconfont zfsoft-chuangkoubanliliucheng"
                style="color: #999988;"
              ></i
            ></span>

            <span
              v-if="
                (scope.row.comboUpperShelfStatus == 1 ||
                  scope.row.comboUpperShelfStatus == 3) &&
                  scope.row.webStatus == 1
              "
              title="网站端已上架"
              ><i
                class="iconfont zfsoft-wangzhanguanli"
                style="color: #1890FF;"
              ></i
            ></span>
            <span
              v-if="
                (scope.row.comboUpperShelfStatus == 0 &&
                  scope.row.elements &&
                  scope.row.elements.indexOf('2') != -1) ||
                  scope.row.webStatus == 0
              "
              title="网站端已下架"
              ><i
                class="iconfont zfsoft-wangzhanguanli"
                style="color: #999988;"
              ></i
            ></span>
            <span
              v-if="
                scope.row.webStatus == 2 &&
                  scope.row.elements &&
                  scope.row.elements.indexOf('2') != -1
              "
              title="网站端未上架"
              ><i
                class="iconfont zfsoft-wangzhanguanli"
                style="color: #999988;"
              ></i
            ></span>

            <span
              v-if="
                (scope.row.comboUpperShelfStatus == 1 ||
                  scope.row.comboUpperShelfStatus == 3) &&
                  scope.row.mobileStatus == 1
              "
              title="移动端已上架"
              ><i
                class="iconfont zfsoft-yidongduanshangjia"
                style="color: #1890FF;"
              ></i
            ></span>
            <span
              v-if="
                (scope.row.comboUpperShelfStatus == 0 &&
                  scope.row.elements &&
                  scope.row.elements.indexOf('3') != -1) ||
                  scope.row.mobileStatus == 0
              "
              title="移动端已下架"
              ><i
                class="iconfont zfsoft-yidongduanshangjia"
                style="color: #999988;"
              ></i
            ></span>
            <span
              v-if="
                scope.row.mobileStatus == 2 &&
                  scope.row.elements &&
                  scope.row.elements.indexOf('3') != -1
              "
              title="移动端未上架"
              ><i
                class="iconfont zfsoft-yidongduanshangjia"
                style="color: #999988;"
              ></i
            ></span>

            <span
              v-if="
                (scope.row.comboUpperShelfStatus == 1 ||
                  scope.row.comboUpperShelfStatus == 3) &&
                  scope.row.selfHelpStatus == 1
              "
              title="自助终端已上架"
              ><i
                class="iconfont zfsoft-zizhuzhongduanshangjia"
                style="color: #1890FF;"
              ></i
            ></span>
            <span
              v-if="
                (scope.row.comboUpperShelfStatus == 0 &&
                  scope.row.elements &&
                  scope.row.elements.indexOf('4') != -1) ||
                  scope.row.selfHelpStatus == 0
              "
              title="自助终端已下架"
              ><i
                class="iconfont zfsoft-zizhuzhongduanshangjia"
                style="color: #999988;"
              ></i
            ></span>
            <span
              v-if="
                scope.row.selfHelpStatus == 2 &&
                  scope.row.elements &&
                  scope.row.elements.indexOf('4') != -1
              "
              title="自助终端未上架"
              ><i
                class="iconfont zfsoft-zizhuzhongduanshangjia"
                style="color: #999988;"
              ></i
            ></span>
          </template>
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
              v-hasPermi="['combo:directory:view']"
              >查看</el-button
            >
            <el-button
              size="mini"
              type="text"
              icon="iconfont zfsoft-xiugai"
              @click="handleComboUpper(scope.row)"
              v-hasPermi="['sys:comboUpperShelf:update']"
              >目录上下架</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="comboTotal > 0"
        :total="comboTotal"
        :page.sync="queryParamsForCombo.pageNumber"
        :limit.sync="queryParamsForCombo.pageSize"
        @pagination="getListForCombo"
      />
    </el-col>

    <!--  上架/下架操作-->
    <el-dialog
      v-dialog-drag
      :title="comboUpperShelfTitle"
      :visible.sync="openComboUpperShelf"
      v-if="openComboUpperShelf"
      width="900px"
      append-to-body
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="0px"
        class="dialog-table"
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
              <b>服务端类型：</b>
            </td>
            <td colspan="3">
              <el-checkbox
                v-model="form.windowStatus == 1"
                @change="chooseType('1')"
                v-if="window"
                >窗口</el-checkbox
              >
              <el-checkbox
                v-model="form.webStatus == 1"
                @change="chooseType('2')"
                v-if="web"
                >网站端</el-checkbox
              >
              <el-checkbox
                v-model="form.mobileStatus == 1"
                @change="chooseType('3')"
                v-if="mobile"
                >移动端</el-checkbox
              >
              <el-checkbox
                v-model="form.selfHelpStatus == 1"
                @change="chooseType('4')"
                v-if="selfHelp"
                >自助终端</el-checkbox
              >
            </td>
          </tr>
          <!--网站端-->
          <tr v-if="form.webStatus == 1">
            <td>
              <i class="require">*</i>
              <b>网站指南地址类型：</b>
            </td>
            <td colspan="3">
              <el-radio-group v-model="form.webType">
                <el-radio :label="0">标准化地址</el-radio>
                <el-radio :label="1">定制化地址</el-radio>
              </el-radio-group>
              <el-button
                v-if="form.webType == 1"
                size="mini"
                type="success"
                style="margin-left: 100px;"
                @click="previewAddress('2')"
                >办事指南预览</el-button
              >
            </td>
          </tr>
          <tr v-if="form.webStatus == 1 && form.webType == 1">
            <td>
              <i class="require">*</i>
              <b>网站指南定制化地址：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="webCustomAddress">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.webCustomAddress"
                    placeholder="请输入网站指南定制化地址"
                    maxlength="100"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <!--移动端-->
          <tr v-if="form.mobileStatus">
            <td>
              <i class="require">*</i>
              <b>移动指南地址类型：</b>
            </td>
            <td colspan="3">
              <el-radio-group v-model="form.mobileType">
                <el-radio :label="0">标准化地址</el-radio>
                <el-radio :label="1">定制化地址</el-radio>
              </el-radio-group>
              <el-button
                v-if="form.mobileType == 1"
                size="mini"
                type="success"
                style="margin-left: 100px;"
                @click="previewAddress('3')"
                >办事指南预览</el-button
              >
            </td>
          </tr>
          <tr v-if="form.mobileStatus && form.mobileType == 1">
            <td>
              <i class="require">*</i>
              <b>移动指南定制化地址：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="mobileCustomAddress">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.mobileCustomAddress"
                    placeholder="请输入移动指南定制化地址"
                    maxlength="100"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <!--自助终端-->
          <tr v-if="form.selfHelpStatus">
            <td>
              <i class="require">*</i>
              <b>自助终端指南地址类型：</b>
            </td>
            <td colspan="3">
              <el-radio-group v-model="form.selfHelpType">
                <el-radio :label="0">标准化地址</el-radio>
                <el-radio :label="1">定制化地址</el-radio>
              </el-radio-group>
              <el-button
                v-if="form.selfHelpType == 1"
                size="mini"
                type="success"
                style="margin-left: 100px;"
                @click="previewAddress('4')"
                >办事指南预览</el-button
              >
            </td>
          </tr>
          <tr v-if="form.selfHelpStatus && form.selfHelpType == 1">
            <td>
              <i class="require">*</i>
              <b>自助终端指南定制化地址：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="selfHelpCustomAddress">
                <el-col :span="24">
                  <el-input
                    v-model.trim="form.selfHelpCustomAddress"
                    placeholder="请输入自助终端指南定制化地址"
                    maxlength="100"
                    show-word-limit
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitUpper">确 定</el-button>
        <el-button
          @click="
            () => {
              openComboUpperShelf = false;
              reset();
            }
          "
          >取 消</el-button
        >
      </div>
    </el-dialog>

    <!--办事指南地址预览-->
    <el-dialog
      v-dialog-drag
      title="预览"
      :visible.sync="dialogVisible"
      width="1100px"
      height="70px"
    >
      <iframe
        :src="previewAddressVal"
        frameborder="0"
        width="100%"
        height="600px"
      ></iframe>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible = false"
          >确 定</el-button
        >
      </span>
    </el-dialog>

    <!--  网站移动、自助终端上架排序  -->
    <el-dialog
      v-dialog-drag
      :visible.sync="sortView"
      title="上架排序"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
      :close-on-click-modal="false"
    >
      <upper-shelf-sort @hideDialog="hideDialog"></upper-shelf-sort>
      <div slot="footer" class="zf-text-center">
        <el-button @click="sortView = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 分类信息详细 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="openView"
      v-if="openView"
      title="目录信息"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <combo-directory-view
        :comboDirectoryOid="comboDirectoryOidView"
        @combo-directory="comboDirectoryClose"
      ></combo-directory-view>

      <div slot="footer" class="zf-text-center">
        <el-button @click="comboDirectoryClose">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  handleComboUpper,
  page,
  getOne
} from "@/api/onething/sxpz/comboUpperShelf";
import upperShelfSort from "@/views/onething/sxpz/comboUpperShelf/upperShelfSort";
import ComboDirectoryView from "@/views/onething/sxpz/comboDirectory/comboDirectoryView";
export default {
  name: "ComboUpperShelf",
  components: { ComboDirectoryView, upperShelfSort },
  data() {
    return {
      // 遮罩层
      loading: true,
      //查看的目录主键
      comboDirectoryOidView: "",
      //总条数
      comboTotal: 0,
      //上下架弹出层标题
      comboUpperShelfTitle: "",
      //上下架记录总数
      comboUpperShelfTotal: 0,
      //配置信息总数
      comboConfigurationTotal: 0,
      //目录数据
      comboDirectories: [],
      //上下架记录数据
      comboUpperShelfs: [],
      //弹出层 dialog
      openComboUpperShelf: false,
      dialogVisible: false,
      //查询参数
      queryParamsForCombo: {
        pageNumber: 1,
        pageSize: 10,
        comboDirectoryName: "",
        comboDirectoryOid: "",
        comboDirectoryCode: "",
        comboUpperShelfStatus: "",
        themeName: "",
        elements: ""
      },
      //上下架记录
      queryParamsForUpperShelfRecord: {
        pageNumber: 1,
        pageSize: 10,
        comboDirectoryOid: "",
        comboDirectoryName: ""
      },
      //上下架
      comboUpper: {
        comboDirectoryOid: null,
        comboUpperShelfStatus: null
      },
      //上下架数据
      form: {
        //所属目录
        comboDirectoryOid: "",
        //服务端类型
        elements: "",

        windowStatus: "",

        webStatus: "",
        webType: 0,
        webCustomAddress: "",

        mobileStatus: "",
        mobileType: 0,
        mobileCustomAddress: "",

        selfHelpStatus: "",
        selfHelpType: 0,
        selfHelpCustomAddress: ""
      },
      // 查看显示弹出层
      openView: false,
      //服务端类型
      window: false,
      mobile: false,
      web: false,
      selfHelp: false,
      // 表单校验
      rules: {
        windowCustomAddress: [
          { required: true, message: "窗口端定制化地址必填项", trigger: "blur" }
        ],
        webCustomAddress: [
          { required: true, message: "网站端定制化地址必填项", trigger: "blur" }
        ],
        mobileCustomAddress: [
          { required: true, message: "移动端定制化地址必填项", trigger: "blur" }
        ],
        selfHelpCustomAddress: [
          {
            required: true,
            message: "自助终端定制化地址必填项",
            trigger: "blur"
          }
        ]
      },
      // 选项
      options: [
        {
          value: 1,
          label: "已上架"
        },
        {
          value: 2,
          label: "未上架"
        },
        {
          value: 3,
          label: "部分上架"
        },
        {
          value: 31,
          label: "窗口端上架"
        },
        {
          value: 32,
          label: "网站端上架"
        },
        {
          value: 33,
          label: "移动端上架"
        },
        {
          value: 34,
          label: "自助终端上架"
        }
      ],
      // 服务端类型
      elements: [
        {
          value: "",
          label: "全部"
        },
        {
          value: 1,
          label: "窗口端"
        },
        {
          value: 2,
          label: "移动端"
        },
        {
          value: 3,
          label: "网站端"
        },
        {
          value: 4,
          label: "自助终端"
        }
      ],
      //预览地址
      previewAddressVal: "",
      //排序
      sortView: false
    };
  },
  created() {
    this.getListForCombo();
  },
  methods: {
    comboDirectoryClose() {
      this.openView = false;
    },
    /** 查询列表*/
    getListForCombo() {
      this.loading = true;
      // alert(JSON.stringify(this.queryParamsForCombo))
      page(this.queryParamsForCombo).then(response => {
        this.comboDirectories = response.data.data;
        this.comboTotal = response.data.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParamsForCombo.pageNumber = 1;
      this.getListForCombo();
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryFormForCombo");
      this.handleQuery();
    },

    /*上下架页面*/
    handleComboUpper(row) {
      let _that = this;
      _that.comboUpperShelfTitle = "目录上下架";
      //查询是否有上下架记录
      //处理服务端所选择的类型
      _that.serverType(row.elements);
      _that.form.comboDirectoryOid = row.comboDirectoryOid;
      _that.openComboUpperShelf = true;
      getOne(row.comboDirectoryOid).then(response => {
        if (response.data) {
          _that.form = response.data;
        }
        _that.form.elements = row.elements;
      });
    },
    /** 查看按钮操作 */
    handleView(row) {
      this.comboDirectoryOidView = row.comboDirectoryOid;
      this.openView = true;
      this.title = "查看目录信息";
    },
    /*上下架处理*/
    submitUpper(row) {
      let _that = this;
      //判断是否首次操作（涉及端均为未上架）
      const canUp = true;
      let sum = 0;
      let es = _that.form.elements.split(",");
      es.forEach(e => {
        if (e == 1 && !_that.form.windowStatus) {
          sum++;
        }
        if (e == 2 && !_that.form.webStatus) {
          sum++;
        }
        if (e == 3 && !_that.form.mobileStatus) {
          sum++;
        }
        if (e == 4 && !_that.form.selfHelpStatus) {
          sum++;
        }
      });
      /*if (sum == es.length) {
        _that.msgWarning("服务端均未上架，请选择！");
        return false;
      }*/
      _that.$refs["form"].validate(valid => {
        if (valid) {
          handleComboUpper(_that.form).then(response => {
            _that.msgSuccess(_that.comboUpperShelfTitle + "成功");
            _that.openComboUpperShelf = false;
            setTimeout(() => {
              _that.getListForCombo();
            }, 10);
            _that.reset();
          });
        }
      });
    },
    //处理目录已选服务端
    serverType(elements) {
      //处理已选服务端类型
      if (elements) {
        let sts = elements.split(",");
        sts.forEach(val => {
          if (val == 1) {
            this.window = true;
          } else if (val == 2) {
            this.web = true;
          } else if (val == 3) {
            this.mobile = true;
          } else if (val == 4) {
            this.selfHelp = true;
          } else {
          }
        });
      }
    },
    //服务端类型选中
    chooseType(val) {
      if (val == 1) {
        if (this.form.windowStatus == 1) {
          this.form.windowStatus = 0;
        } else {
          this.form.windowStatus = 1;
        }
      } else if (val == 2) {
        //网站
        if (this.form.webStatus == 1) {
          this.form.webStatus = 0;
        } else {
          this.form.webStatus = 1;
        }
      } else if (val == 3) {
        //移动端
        if (this.form.mobileStatus == 1) {
          this.form.mobileStatus = 0;
        } else {
          this.form.mobileStatus = 1;
        }
      } else if (val == 4) {
        //自助终端
        if (this.form.selfHelpStatus == 1) {
          this.form.selfHelpStatus = 0;
        } else {
          this.form.selfHelpStatus = 1;
        }
      } else {
      }
    },
    //办事指南预览
    previewAddress(val) {
      if (val == 1) {
        if (this.form.windowType) {
          if (this.form.windowType == 1) {
            //定制化地址
            if (this.form.windowCustomAddress) {
              this.previewAddressVal = this.form.windowCustomAddress;
              this.dialogVisible = true;
            } else {
              this.msgWarning("请输入窗口端定制化地址");
            }
          }
        } else {
          this.msgWarning("请选择窗口端办事指南类型");
        }
      } else if (val == 2) {
        if (this.form.webType) {
          if (this.form.webType == 1) {
            //定制化地址
            if (this.form.webCustomAddress) {
              this.previewAddressVal = this.form.webCustomAddress;
              this.dialogVisible = true;
            } else {
              this.msgWarning("请输入网站端定制化地址");
            }
          }
        } else {
          this.msgWarning("请选择网站端办事指南类型");
        }
      } else if (val == 3) {
        if (this.form.mobileType) {
          if (this.form.mobileType == 1) {
            //定制化地址
            if (this.form.mobileCustomAddress) {
              this.previewAddressVal = this.form.mobileCustomAddress;
              this.dialogVisible = true;
            } else {
              this.msgWarning("请输入移动端定制化地址");
            }
          }
        } else {
          this.msgWarning("请选择移动端办事指南类型");
        }
      } else if (val == 4) {
        if (this.form.selfHelpType) {
          if (this.form.selfHelpType == 1) {
            //定制化地址
            if (this.form.selfHelpCustomAddress) {
              this.previewAddressVal = this.form.selfHelpCustomAddress;
              this.dialogVisible = true;
            } else {
              this.msgWarning("请输入自助终端定制化地址");
            }
          }
        } else {
          this.msgWarning("请选择自助终端办事指南类型");
        }
      } else {
      }
    },
    //重置数据
    reset() {
      //默认均不显示
      this.window = false;
      this.web = false;
      this.mobile = false;
      this.selfHelp = false;

      this.form = {
        //所属目录
        comboDirectoryOid: "",
        elements: "",

        windowStatus: "",

        webStatus: "",
        webType: "0",
        webCustomAddress: "",

        mobileStatus: "",
        mobileType: "0",
        mobileCustomAddress: "",

        selfHelpStatus: "",
        selfHelpType: "0",
        selfHelpCustomAddress: ""
      };
    },
    getComboUpperShelfStatus(val) {
      if (val.comboUpperShelfStatus == 0) {
        return "已下架";
      } else if (val.comboUpperShelfStatus == 2) {
        return "未上架";
      } else if (
        val.comboUpperShelfStatus == 1 ||
        val.comboUpperShelfStatus == 3
      ) {
        //处理
        let sjInfo = "";
        if (val.windowStatus) {
          sjInfo += "窗口上架/";
        }
        if (val.webStatus) {
          sjInfo += "网站上架/";
        }
        if (val.mobileStatus) {
          sjInfo += "移动上架/";
        }
        if (val.selfHelpStatus) {
          sjInfo += "自助终端上架/";
        }
        return sjInfo.substring(0, sjInfo.lastIndexOf("/"));
      } else {
        return "";
      }
    },

    statusName(val) {
      if (val.status == 0) {
        return "未发布";
      } else if (val.status == 1) {
        return "已发布";
      } else {
        return "";
      }
    },
    //排序页面
    sortDirectory() {
      this.sortView = true;
    },
    //关闭排序页面
    hideDialog() {
      this.sortView = false;
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
.required {
  color: #ff0000;
  font-size: 20px;
  display: inline-block;
  vertical-align: middle;
  margin: 3px 5px 0px 0px;
}
</style>
