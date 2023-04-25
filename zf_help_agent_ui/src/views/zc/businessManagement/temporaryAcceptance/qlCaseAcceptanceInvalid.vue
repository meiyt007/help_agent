/**
* 暂存受理办件作废
* @author: wangwg
* @date: 2020-12-08
*/
<template>
  <div class="process-box" id="print">
    <div class="step-content">
      <div class="zf-zc-table--title">办件作废信息</div>
      <el-form
        :model="ruleForm"
        :rules="rules"
        ref="ruleForm"
        label-width="0px"
        class="demo-ruleForm"
        :label-position="labelPosition"
      >
        <table
          cellspacing="0"
          cellpadding="0"
          border="0"
          class="zf-zc-table"
        >
          <colgroup>
            <col width="15%" />
            <col width="35%" />
            <col width="15%" />
            <col width="35%" />
          </colgroup>
          <tr>
            <td><b>审批部门：</b></td>
            <td>{{ loginUser.organName }}</td>
            <td><b>是否需要退件：</b></td>
            <td>
              <el-radio-group v-model.trim="ruleForm.isTjFlag">
                <el-radio label="1">需要</el-radio>
                <el-radio label="2">不需要</el-radio>
              </el-radio-group>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>作废理由：</b></td>
            <td colspan="3">
              <el-form-item label="" prop="invalidReason">
                <el-col :span="24">
                  <el-input
                    type="textarea"
                    v-model.trim="ruleForm.invalidReason"
                    maxlength="500"
                    name="invalidReason"
                    show-word-limit
                  ></el-input>
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr></tr>
        </table>
        <div class="btn-wrap">
          <div class="btn-list mt10">
            <el-button
              type="primary"
              @click="caseInvalid"
              >确定</el-button
            >
          </div>
        </div>
      </el-form>
    </div>
  </div>
</template>
<script>
import { getloginUser } from "@/api/zc/businessManagement/windowAcceptance";
import { saveCaseInvalid } from "@/api/zc/businessManagement/qlCaseAcceptanceInvalid";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  inheritAttrs: false,
  props: ['caseOid'],
  data () {
    return {
      //登录信息
      loginUser: {},
      // 弹出层标题
      title: "",
      // 表单参数
      ruleForm: {
        isTjFlag: "2",
        invalidReason: "",
      },
      labelPosition: "top",
      // 表单校验
      rules: {
        invalidReason: [
          { required: true, message: "必填项", trigger: "blur" },
          { min: 3, max: 500, message: "长度在 3 到 500 个字符", trigger: "blur" },
        ],
      },
    };
  },
  created () {
    this.queryLoginInfo();
  },
  methods: {
    /** 登录信息 */
    queryLoginInfo () {
      let _that = this;
      getloginUser().then(response => {
        _that.loginUser = response.data;
      });
    },
    /** 办件受理 */
    caseInvalid () {
      let _that = this;
      _that.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          if (_that.caseOid != "") {
            let formData = new FormData();
            formData.append("caseOid", _that.caseOid);
            formData.append("invalidReason", _that.ruleForm.invalidReason);
            if (_that.ruleForm.isTjFlag == 1) {
              formData.append("caseStatus", 5);
            } else {
              formData.append("caseStatus", -1);
            }
            saveCaseInvalid(formData).then(response => {
              if (response.data != "") {
                _that.linkResultOid = response.data.linkResultOid;
                _that.msgSuccess("办件已作废！");
                this.$emit('case-close');
              }
            });
          } else {
            _that.msgError("办件信息获取失败！");
            return false;
          }
        }
      });
    },
  }
};
</script>
<style lang="scss" scoped>
.el-tree > .el-tree-node {
  min-width: 100%;
  display: inline-block;
}

.process-box {
  padding: 15px;
  box-sizing: border-box;
  text-align: left;
}

.process-box .step-title {
  font-size: 14px;
  color: #333;
}

.process-box .title {
  font-size: 12px;
  color: #333;
  font-weight: normal;
  margin-top: 30px;
  text-align: left;
}

.select-list span {
  display: inline-block;
  vertical-align: middle;
  color: #47657d;
  font-size: 14px;
  background-color: #f1f5f9;
  height: 40px;
  line-height: 40px;
  margin: 0px 10px 10px 0px;
  padding: 0px 35px 0px 20px;
  border-radius: 5px;
  cursor: pointer;
}

.select-list span:first-child {
  background-color: #fff6f1;
  color: #2d506b;
}

.select-list span.current {
  background-color: #4d87b5;
  color: #fff;
  position: relative;
}

.select-list span.current:after {
  content: '';
  position: absolute;
  width: 19px;
  height: 13px;
  right: 10px;
  top: 13px;
  background: url(../../../../assets/image/check-icon.png) no-repeat center;
  background-size: cover;
}

.option-box .option-title {
  margin-top: 20px;
  font-size: 12px;
  color: #333;
}

.option-box .option-item {
  margin-right: 10px;
}

.option-box .option-item,
.option-box .chose-item {
  display: inline-block;
  vertical-align: top;
}

.option-box .chose-item {
  color: #4d87b5;
}

.process-box table {
  width: 100%;
}

.process-box table {
  border: 1px solid #dfe6ec;
  border-collapse: collapse;
}

.process-box table tr td,
.process-box table tr th {
  border: 1px solid #dfe6ec;
  padding: 17px 10px;
  font-size: 12px;
  color: #515a6e;
  text-align: right;
}

.process-box table tr td:nth-of-type(2n + 1) {
  background-color: #f8f8f9;
}

.process-box table tr td:nth-of-type(2n) {
  color: #606266;
  text-align: left;
}

.process-box table.data-table tr td,
.process-box table.data-table tr th {
  text-align: center;
  padding: 12px 10px;
}

.process-box table.data-table tr th {
  background-color: #f8f9fb;
}

.process-box table.data-table tr td:nth-of-type(2n + 1) {
  background: none;
}

.process-box table.data-table .bage-necessery {
  color: #ff3000;
  background-color: #fff2f2;
  height: 30px;
  line-height: 30px;
  padding: 0px 20px;
  border-radius: 20px;
  display: inline-block;
}

.process-box .check-list {
  border-bottom: 1px solid #e5e5e5;
  margin: 0px 20px;
  padding-left: 10px;
  box-sizing: border-box;
  padding-bottom: 20px;
}

.process-box .check-list h3 {
  font-size: 12px;
  color: #333;
}

.process-box .el-form-item {
  margin-bottom: 0;
}

.require {
  color: #ff0000;
  font-style: normal;
  font-size: 14px;
  display: inline-block;
  vertical-align: middle;
  margin-right: 5px;
}

.process-box .next-btn {
  display: block;
  margin: 30px auto;
  font-size: 14px;
}

.process-box .data-box h4 {
  font-size: 12px;
  color: #333;
  margin: 20px 0px 10px 0px;
}

.process-box .btn-wrap {
  text-align: center;
  margin: 30px 0;
}

.process-box .form-box-inline {
  display: inline-block;
  vertical-align: middle;
  border: 1px solid #2d506b;
  width: 100%;
  padding: 30px;
  box-sizing: border-box;
}

.process-box .el-button--info {
  background-color: #3b5f7b;
}

.process-box .el-button--info:hover {
  background-color: #426886;
}

.process-box .form-box-inline {
  position: relative;
}

.process-box .form-box-inline .btn-write {
  position: absolute;
  right: 20px;
  top: 20px;
  font-size: 12px;
  padding: 8px 15px;
}

.process-box .form-box-inline ul {
  margin: 0;
  padding: 0;
}

.process-box .form-box-inline ul li {
  padding: 10px 20px;
  background-color: #f3f6f9;
  border-radius: 10px;
  margin-bottom: 10px;
  list-style: none;
}

.process-box .form-box-inline ul li > .icon {
  display: inline-block;
  vertical-align: middle;
  width: 36px;
  height: 36px;
  line-height: 36px;
  text-align: center;
  border-radius: 100%;
  color: #fff;
  font-size: 20px;
}

.process-box .form-box-inline ul li > .bdbm-icon {
  background-color: #a8cfec;
}

.process-box .form-box-inline ul li > .bdsm-icon {
  background-color: #e6b893;
}

.process-box .form-box-inline .input-text {
  display: inline-block;
  vertical-align: middle;
  padding-left: 10px;
  font-size: 12px;
  color: #333;
}

.process-box .form-box-inline > h4 {
  font-weight: normal;
  color: #003259;
  font-size: 14px;
}

.process-box .form-box-inline .input-text > h4 {
  margin: 0px 0px 5px 0px;
  color: #3b5f7b;
  font-weight: normal;
}

.process-box .form-box-inline .input-text > p {
  margin: 0px;
}

.process-box .form-btn-group {
  position: absolute;
  right: 20px;
  top: 20px;
}

.process-box .form-btn-group .btn {
  border: 1px solid #097dd6;
  color: #097dd6;
  font-size: 12px;
  padding: 8px 15px;
}

.handle-data {
  padding: 25px 40px;
  background-color: #f8f9fb;
  position: relative;
}

.handle-data ul {
  padding: 0px;
  margin: 0;
}

.handle-data ul li {
  list-style: none;
  text-align: left;
  margin-top: 10px;
}

.right-btn-group {
  position: absolute;
  right: 130px;
  top: 10px;
}

.right-btn-group-two {
  position: absolute;
  right: 10px;
  top: 10px;
}

.right-btn-group .el-button {
  padding: 6px 8px;
  font-size: 12px;
  margin-left: 5px;
  background-color: #0ba2b8;
  border: 1px solid #0ba2b8;
}

.right-btn-group .el-button:last-child {
  background-color: #47657d;
  border: 1px solid #47657d;
}

.right-btn-group .el-button:last-child:hover {
  background-color: #4d708b;
  border: 1px solid #4d708b;
}

.qdcg-success {
  font-size: 12px;
  color: #00b45e;
  margin-top: 5px;
  text-decoration: underline;
}

.qdcg-fail {
  font-size: 12px;
  color: #ff0000;
  margin-top: 5px;
  text-decoration: underline;
}

.qdcg-success > img,
.qdcg-fail > img {
  display: inline-block;
  vertical-align: middle;
  margin: -2px 5px 0 0;
}

.qdcg-text img,
.qdcg-text p {
  display: inline-block;
  vertical-align: middle;
}

.qdcg-text img {
  margin-right: 5px;
}

.qdcg-text p {
  margin: 0;
}

.qdcg-text p > span {
  font-size: 12px;
  color: #999;
  padding-left: 20px;
}

.qdcg-btn .el-button {
  color: #333;
}

.qdcg-btn {
  margin-top: -5px;
}

.input-number {
  text-align: left;
  margin-top: 20px;
  margin-bottom: 10px;
}

.input-number-label {
  display: inline-block;
  vertical-align: middle;
  margin-right: 20px;
}

.rightSideBar {
  position: fixed;
  right: 0px;
  bottom: 100px;
  box-shadow: -1px 1px 10px #ddd;
  border-top-left-radius: 5px;
  border-bottom-left-radius: 5px;
}

.rightSideBar .sideItem {
  width: 42px;
  height: 42px;
  line-height: 42px;
  text-align: center;
  color: #0b76c7;
  background-color: #fff;
  border-top-left-radius: 5px;
  cursor: pointer;
  font-size: 20px;
  position: relative;
  -webkit-transition: all 0.3s ease-in-out 0.1s;
  transition: all 0.3s ease-in-out 0.1s;
  z-index: 2;
}

.rightSideBar .sideItem:last-child {
  border-top-left-radius: 0px;
  border-bottom-left-radius: 5px;
}

.rightSideBar .sideItem:hover {
  background-color: #0b76c7;
  color: #fff;
  border-top-left-radius: 0px;
  border-bottom-left-radius: 0px;
}

.rightSideBar .sideItem .sideText {
  position: absolute;
  left: 42px;
  top: 0px;
  height: 42px;
  line-height: 42px;
  padding: 0px 10px 0px 20px;
  border-top-left-radius: 30px;
  border-bottom-left-radius: 30px;
  color: #fff;
  background-color: #0b76c7;
  font-size: 14px;
  -webkit-transition: left 0.3s ease-in-out 0.1s;
  transition: left 0.3s ease-in-out 0.1s;
  z-index: 1;
}

.rightSideBar .sideItem:hover .sideText {
  left: -86px;
}

.title-detail {
  text-align: center;
}
</style>
