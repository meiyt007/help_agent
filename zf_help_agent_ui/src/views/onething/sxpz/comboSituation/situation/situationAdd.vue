/*
* @Description:新增情形
* @Author: dxl
**/
<template>
  <div>
    <el-form ref="form" :model="form" :rules="rules" label-width="0">
      <el-input v-show="false" v-model="form.comboDirectoryOid" />
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="20%" />
          <col width="30%" />
          <col width="20%" />
          <col width="30%" />
        </colgroup>
        <tr>
          <td align="right">
            <i class="require">*</i>
            <b>情形名称：</b>
          </td>
          <td colspan="3">
            <el-form-item prop="situationName">
              <el-col :span="24">
                <el-input
                  v-model.trim="form.situationName"
                  type="text"
                  maxlength="100"
                  show-word-limit
                  placeholder="请输入情形名称"
                >
                </el-input>
              </el-col>
            </el-form-item>
          </td>
        </tr>
        <tr>
          <td>
            <i class="require">*</i>
            <b>展示类型：</b>
          </td>
          <td>
            <el-form-item prop="columnType">
              <el-radio-group v-model="form.columnType">
                <el-radio :label="1">单选</el-radio>
                <el-radio :label="2">多选</el-radio>
                <el-radio :label="3">文本</el-radio>
              </el-radio-group>
            </el-form-item>
          </td>
          <td>
            <i class="require">*</i>
            <b>是否必填：</b>
          </td>
          <td>
            <el-form-item prop="mustStatus">
              <el-radio-group v-model="form.mustStatus">
                <el-col :span="24">
                  <el-radio :label="1">是</el-radio>
                  <el-radio :label="0">否</el-radio>
                </el-col>
              </el-radio-group>
            </el-form-item>
          </td>
        </tr>
        <tr>
          <td align="right">
            <b>必填提示信息：</b>
          </td>
          <td colspan="3">
            <el-form-item prop="tips">
              <el-col :span="24">
                <el-input
                  v-model.trim="form.tips"
                  type="text"
                  maxlength="100"
                  show-word-limit
                  placeholder="请输入必填提示信息"
                >
                </el-input>
              </el-col>
            </el-form-item>
          </td>
        </tr>
        <tr>
          <td>
            <b>标题是否展示：</b>
          </td>
          <td>
            <el-form-item prop="titleShowStyle">
              <el-radio-group v-model="form.titleShowStyle">
                <el-col :span="24">
                  <el-radio :label="1">是</el-radio>
                  <el-radio :label="0">否</el-radio>
                </el-col>
              </el-radio-group>
            </el-form-item>
          </td>
          <td>
            <i class="require">*</i>
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
            <b>默认值是否不可改：</b>
          </td>
          <td>
            <el-form-item prop="isModificationStatus">
              <el-radio-group v-model="form.isModificationStatus">
                <el-col :span="24">
                  <el-radio :label="1">不可改</el-radio>
                  <el-radio :label="0">可改</el-radio>
                </el-col>
              </el-radio-group>
            </el-form-item>
          </td>
          <td>
            <b>是否链接：</b>
          </td>
          <td>
            <el-form-item prop="linkStatus">
              <el-radio-group v-model="form.linkStatus">
                <el-col :span="24">
                  <el-radio :label="1">是</el-radio>
                  <el-radio :label="0">否</el-radio>
                </el-col>
              </el-radio-group>
            </el-form-item>
          </td>
        </tr>
        <tr>
          <td align="right">
            <b>链接地址：</b>
          </td>
          <td colspan="3">
            <el-form-item prop="linkUrl">
              <el-col :span="24">
                <el-input
                  v-model.trim="form.linkUrl"
                  type="text"
                  maxlength="200"
                  show-word-limit
                  placeholder="请输入链接地址"
                >
                </el-input>
              </el-col>
            </el-form-item>
          </td>
        </tr>
      </table>
      <div style="margin: 10px 0 20px">
        <el-row :gutter="10" class="mb8 fl">
          <el-col :span="1.5">
            <el-button
              type="default"
              icon="el-icon-plus"
              size="mini"
              @click="handleInitVal"
              v-hasPermi="['combo:directory:save']"
              >新增选项值</el-button
            >
          </el-col>
        </el-row>
        <el-table
          :data="comboOptionValList"
          border
          :fit="true"
          style="width: 100%"
        >
          <el-table-column
            label="排序号"
            align="center"
            prop="sortOpt"
            width="240"
          >
            <template slot-scope="scope">
              <el-form-item prop="sortOpt">
                <el-input-number
                  v-model="scope.row.sort"
                  :min="1"
                  :max="9999"
                  style="width: 200px"
                />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="选项值名称" align="center" width="250">
            <template slot-scope="scope">
              <el-input v-show="false" v-model="scope.row.valOid" />
              <el-form-item prop="optionName">
                <el-input
                  v-model.trim="scope.row.name"
                  placeholder="选项值名称"
                  maxlength="100"
                  show-word-limit
                />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="编码[相同互斥]" align="center" width="200">
            <template slot-scope="scope">
              <el-form-item prop="optionCode">
                <el-input
                  v-model.trim="scope.row.optionCode"
                  placeholder="编码"
                  maxlength="50"
                  show-word-limit
                />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="营业范围校验名称" align="center" width="250">
            <template slot-scope="scope">
              <el-form-item prop="verifyName">
                <el-input
                  v-model.trim="scope.row.verifyName"
                  placeholder="营业范围校验名称"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="校验规则" align="center" width="250">
            <template slot-scope="scope">
              <el-form-item prop="validRule">
                <el-input
                  v-model.trim="scope.row.validRule"
                  placeholder="校验规则如：[100,500]"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="备注" align="center" width="200">
            <template slot-scope="scope">
              <el-form-item prop="remark">
                <el-input
                  v-model.trim="scope.row.remark"
                  placeholder="备注"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="备注显示方式" align="center" width="200">
            <template slot-scope="scope">
              <el-form-item prop="remarkShowStyle">
                <el-input
                  v-model.trim="scope.row.remarkShowStyle"
                  placeholder="备注显示方式"
                  maxlength="50"
                  show-word-limit
                />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column label="是否默认" align="center" width="100">
            <template slot-scope="scope">
              <el-form-item
                prop="ifChecked"
                style="text-align: center !important"
              >
                <el-checkbox v-model="scope.row.defaultFlag" lable="1" />
              </el-form-item>
            </template>
          </el-table-column>

          <el-table-column
            label="操作"
            align="center"
            fixed="right"
            class-name="small-padding fixed-width"
            width="100"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-shanchu"
                @click="handleValDelete(scope.$index)"
                v-hasPermi="['combo:option:save']"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-form>
  </div>
</template>
<script>
import {
  initSituation,
  saveSituation
} from "@/api/onething/comboSituation/comboSituationIndex.js";
import {
  validIntNoZero
} from '@/utils/validate'
export default {
  components: {},
  name: "SituationAdd",
  //定义获取父类传过来值的格式
  props: ["situationOid", "comboDirectoryOid"],
  data () {
    return {
      // 遮罩层
      loading: true,
      title: "",
      ifChecked: false,
      //选项值列表
      comboOptionValList: [],
      // 表单参数
      form: {
        fillFlag: 0,
        moreStatus: 0,
        isModificationStatus: 1,
        columnType: 1,
        titleShowStyle: 1,
        linkStatus: 0,
        mustStatus: 0,
      },
      rules: {
        situationName: [{
          required: true,
          message: "情形名称不能为空",
          trigger: "blur"
        }],
        columnType: [{
          required: true,
          message: "请选择展示类型",
          trigger: "blur"
        }],
        mustStatus: [{
          required: true,
          message: "请选择是否必填",
          trigger: "blur"
        }],
        sort: [{
          required: true,
          message: '排序号必须是正整数',
          trigger: 'change'
        },
        {
          validator: validIntNoZero,
          trigger: 'change'
        }
        ]
      },
    }
  },
  created () {
    this.handleInit();
  },
  methods: {

    // 表单重置
    reset () {
      this.form = {
        fillFlag: 0,
        moreStatus: 0,
        isModificationStatus: 1,
        columnType: 1,
        titleShowStyle: 1,
        linkStatus: 0, mustStatus: 0,
      },
        this.resetForm("form")
    },

    /** 新增和修改按钮操作 */
    handleInit () {
      let _that = this;
      this.reset();
      let oid = this.situationOid;
      if (oid && oid != 'undefined') { //修改
        initSituation(oid).then(response => {
          if (response.data != undefined) {
            this.form = response.data;
            this.comboOptionValList = response.data.optionValsList;
            this.comboOptionValList.forEach(optVal => {
              if (optVal.defaultFlag == 1) {
                optVal.defaultFlag = true;
              } else {
                optVal.defaultFlag = false;
              }
            });
          }
        });
      } else { //新增
        _that.title = "选项标题新增";
      }
    },

    //新增选项值
    handleInitVal (row) {
      this.comboOptionValList.push({});
    },
    //删除子项模块
    handleValDelete: function (index) {
      this.comboOptionValList.splice(index, 1);
    },
    /** 选项标题提交按钮 */
    submitForm: function () {
      let _that = this;
      this.form.comboDirectoryOid = this.comboDirectoryOid;
      this.$refs["form"].validate(valid => {
        if (valid) {
          let b = true;
          let checkNumb = 0;
          if (_that.comboOptionValList) {
            try {
              _that.comboOptionValList.forEach(eleOption => {
                if (eleOption.defaultFlag) {
                  eleOption.defaultFlag = 1;
                } else {
                  eleOption.defaultFlag = 0;
                }
                if (null == eleOption.name || '' == eleOption.name) {
                  this.msgWarning("选项值名称不能为空！");
                  b = false;
                  return false;
                }
                if (null == eleOption.sort || '' == eleOption.sort) {
                  this.msgWarning("选项值排序号不能为空！");
                  b = false;
                  return false;
                }
                if (eleOption.ifChecked) {
                  checkNumb += 1;
                }
              });
            } catch (e) {
              b = false;
              _that.msgWarning(e);
              return false
            }
            /*if (_that.form.columnType == 3 && _that.comboOptionValList.length > 0) {
              b = false;
              this.msgWarning("当前情形为文本框，不需要添加选项值！")
              return false;
            } else */
            if ((_that.form.columnType == 1 || _that.form.columnType == 2) &&
              _that.comboOptionValList.length <= 0 && _that.form.linkStatus !== 1) {
              b = false;
              this.msgWarning("请至少添加一条选项值！")
              return false;
            }
          }
          if (_that.form.linkStatus === 1 && !_that.form.linkUrl) {
            b = false;
            this.msgWarning("请输入链接地址!")
            return false;
          }
          if (_that.form.columnType == 1) {
            if (checkNumb > 1) {
              b = false;
              this.msgWarning("当前选项不是多选，不能有多个【默认项】，请重新选择！")
              return false;
            }
          }
          if (b) {
            _that.form.optionValsList = _that.comboOptionValList;
            saveSituation(_that.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("保存成功");
                this.$emit('combo-form', 1);
              }
            });
          }
        }
      });
    },
    //关闭配置选项页
    cancelComboOptionTitle () {
      this.$emit('combo-form');
    },
  }

}

</script>
