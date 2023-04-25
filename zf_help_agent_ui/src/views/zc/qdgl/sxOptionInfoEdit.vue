/*
* @Description:新增选项
* @Author: shimh
* @Date: 2021/8/7
**/
<template>
  <div>
    <el-form ref="form" :model="form" :rules="rules" label-width="0px">
      <div class="zf-zc-table--title">选项标题信息</div>
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="15%" />
          <col width="35%" />
          <col width="15%" />
          <col width="35%" />
        </colgroup>
        <tr>
          <td><i class="require">*</i><b>选项标题：</b></td>
          <td width="35%" colspan="3">
            <el-form-item prop="name">
              <el-input
                v-model.trim="form.name"
                placeholder="请输入名称选项标题"
                maxlength="50"
                show-word-limit
              />
            </el-form-item>
          </td>
        </tr>
        <tr>
          <td><b>是否必填：</b></td>
          <td>
            <el-form-item prop="fillFlag">
              <el-radio-group v-model="form.fillFlag">
                <el-radio :label="0">否</el-radio>
                <el-radio :label="1">是</el-radio>
              </el-radio-group>
            </el-form-item>
          </td>
          <td width="15%">
            <b>是否多选：</b>
          </td>
          <td width="35%">
            <el-form-item prop="moreStatus">
              <el-radio-group v-model="form.moreStatus">
                <el-radio :label="0">否</el-radio>
                <el-radio :label="1">是</el-radio>
              </el-radio-group>
            </el-form-item>
          </td>
        </tr>
        <tr>
          <td><b>选项来源：</b></td>
          <td>
            <el-form-item prop="optionSource">
              <el-input
                v-model.trim="form.optionSource"
                placeholder="请输入选项来源"
                maxlength="20"
                show-word-limit
              />
            </el-form-item>
          </td>
          <td><i class="require">*</i><b>排序号：</b></td>
          <td>
            <el-form-item prop="sort">
              <el-input-number v-model="form.sort" :min="1" :max="9999" />
            </el-form-item>
          </td>
        </tr>
        <tr>
          <td><b>是否在告知单中展示：</b></td>
          <td>
            <el-form-item prop="noticeFormFlag">
              <el-radio-group v-model="form.noticeFormFlag">
                <el-radio :label="0">否</el-radio>
                <el-radio :label="1">是</el-radio>
              </el-radio-group>
            </el-form-item>
          </td>
        </tr>
      </table>
      <el-button
        type="primary"
        style="float: right;margin-bottom:10px;margin-right: 20px;"
        @click="handleInitVal"
        class="data-btn"
      >
        增加
      </el-button>
      <div class="zf-zc-table--title">选项值</div>
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="35%" />
          <col width="20%" />
          <col width="25%" />
          <col width="10%" />
        </colgroup>
        <tr>
          <th>选项值名称</th>
          <th>是否默认</th>
          <th>排序号</th>
          <th>操作</th>
        </tr>
        <tbody v-for="(item, index) in form.sxServiceOptionVals" :key='index'>
          <tr>
            <td style="background-color: #fff; padding: 0 10px">
              <el-form-item
                :prop="'sxServiceOptionVals.' + index + '.name'"
                :rules="bgRules.name"
              >
                <el-input
                  v-model.trim="item.name"
                  placeholder="请输入选项值名称"
                  maxlength="100"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td>
              <el-form-item
                :prop="'sxServiceOptionVals.' + index + '.defaultFlag'"
                :rules="bgRules.defaultFlag"
              >
                <el-radio-group v-model="item.defaultFlag">
                  <el-radio :label="0">否</el-radio>
                  <el-radio :label="1">是</el-radio>
                </el-radio-group>
              </el-form-item>
            </td>
            <td
              style="
                background-color: #fff;
                padding: 0 10px;
                text-align: center;
              "
            >
              <el-form-item
                :prop="'sxServiceOptionVals.' + index + '.sort'"
                :rules="bgRules.sort"
                label-width="0"
              >
                <el-input-number v-model="item.sort" :min="1" :max="9999" />
              </el-form-item>
            </td>
            <td>
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-shanchu"
                @click="delOptionVal(index)"
                >删除
              </el-button>
            </td>
          </tr>
        </tbody>
      </table>
    </el-form>
    <!-- <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div> -->
  </div>
</template>

<script>
import { initOptionTitle } from "../../../api/zc/qdgl/sxOptionInfo";
import { saveServiceOption } from "@/api/zc/qdgl/sxOptionInfo.js";

export default {
  components: {},
  name: "sxOptionInfoEdit",
  //定义获取父类传过来值的格式
  props: ["titleOid", "serviceOid"],
  data () {
    return {

      form: { fillFlag: 1, moreStatus: 0,noticeFormFlag: 0, name: "", sxServiceOptionVals: [{ name: "", defaultFlag: 0 }], serviceOid: this.serviceOid },

      rules: {
        name: [{
          required: true,
          message: "必填项",
          trigger: "change"
        }],
        sort: [{
          required: true,
          message: "必填项",
          trigger: "change"
        }]
      },
      //动态表单验证
      bgRules: {
        name: [{
          required: true,
          message: "必填项",
          trigger: "change"
        }],
        sort: [{
          required: true,
          message: "必填项",
          trigger: "change"
        }],
      },
    };
  },
  created () {
    this.handleInit();
  },
  methods: {
    // 表单重置
    reset () {
      this.form = {
        fillFlag: 0,
        moreStatus: 0
      };
      this.resetForm("form");
    },
    handleInitVal(){
      let optionVal={oid:"",name:"",titleOid:"",sort:"",defaultFlag:0};
      this.form.sxServiceOptionVals.push(optionVal)
    },
    delOptionVal (index) {
      this.form.sxServiceOptionVals.splice(index, 1);
    },

    /** 提交按钮 */
    submitForm () {
      if(!this.form.sxServiceOptionVals || this.form.sxServiceOptionVals.length==0){
        this.$message.error("请添加选项值！");
        return false;
      }

      this.$refs["form"].validate(valid => {
        if (valid) {
          saveServiceOption(this.form).then(response => {
            let res = response.data;
            if (res) {
              this.$message.success("保存成功！");
              this.$emit('init-serv-option-close');
            } else {
              this.$message.error("保存失败！");
            }
          })
        }
      });
    },
    handleInit () {
      if (this.titleOid) {
        initOptionTitle(this.titleOid).then(response => {
          this.form = response.data;

        })
      }

    },
    changeFlag (val) {
      let _that = this;
    },
    cancel () {
      this.$emit('init-serv-option-close');
    }
  }
}
</script>
<style scoped>
.el-tree > .el-tree-node {
  min-width: 100%;
  display: inline-block;
}
</style>
