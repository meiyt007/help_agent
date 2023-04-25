<template>
  <div class="process-box">
    <div class="step-title">
      <span
        v-for="(item, index) in stepData"
        :key="item.index"
        :class="{ active: index == i }"
        >{{ item.title }}</span
      >
    </div>
    <!-- 第一步 -->
    <div class="step-content step-first">
      <div class="situation-box">
        <h3 class="title"><i class="el-icon-s-grid"></i>情形选择</h3>
        <div class="select-list">
          <span
            v-for="(item, index) in selectData"
            :key="item.index"
            :class="{ current: index == num }"
            @click="selectChange(index)"
            >{{ item.title }}</span
          >
        </div>
      </div>
      <div class="option-box">
        <div class="option-title">
          <div class="option-item"><i class="el-icon-s-grid"></i>选项信息</div>
          <div class="option-item">
            <i class="el-icon-s-grid"></i>
            <div class="chose-item">[<span>我要办理社会保险缴费</span>]</div>
            情景选项信息
          </div>
        </div>
      </div>
      <el-form
        label-width="0px"
        class="demo-ruleForm"
        :label-position="labelPosition"
      >
        <table
          cellspacing="0"
          cellpadding="0"
          border="0"
          class="el-table__body mt20"
        >
          <colgroup>
            <col width="15%" />
          </colgroup>
          <tr>
            <td><b>上级字典名称：</b></td>
            <td colspan="3">
              <div class="check-list">
                <h3>经办人</h3>
                <el-checkbox-group v-model="checkList">
                  <el-checkbox label="变更经营地址"></el-checkbox>
                  <el-checkbox label="变更法定代表人、负责人"></el-checkbox>
                  <el-checkbox label="变更单位名称"></el-checkbox>
                </el-checkbox-group>
              </div>
              <div class="check-list">
                <h3>新办</h3>
                <el-checkbox-group v-model="checkList02">
                  <el-checkbox label="个体工商户02"></el-checkbox>
                  <el-checkbox label="内资企业02"></el-checkbox>
                  <el-checkbox label="内资企业分公司02"></el-checkbox>
                </el-checkbox-group>
              </div>
              <div class="check-list">
                <h3>经办人</h3>
                <el-checkbox-group v-model="checkList03">
                  <el-checkbox label="变更经营地址03"></el-checkbox>
                  <el-checkbox label="变更法定代表人、负责人03"></el-checkbox>
                  <el-checkbox label="变更单位名称03"></el-checkbox>
                </el-checkbox-group>
              </div>
            </td>
          </tr>
        </table>
      </el-form>
      <el-button type="primary" icon="el-icon-circle-check" class="next-btn"
        >下一步</el-button
      >
    </div>
    <!-- 右侧边栏 -->
    <div class="rightSideBar">
      <div class="sideItem">
        <i class="el-icon-edit-outline"></i>
        <div class="sideText">查看事项</div>
      </div>
      <div class="sideItem">
        <i class="el-icon-c-scale-to-original"></i>
        <div class="sideText">暂存登记</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      i: 0,
      num: 0,
      num1: 1,
      num2: 2,
      show: false,
      radio: "1",
      radio1: "2",
      radio2: "1",
      stepData: [
        {
          index: "0",
          title: "情形选择",
        },
        {
          index: "1",
          title: "材料核验",
        },
        {
          index: "2",
          title: "信息登记",
        },
        {
          index: "3",
          title: "收取材料",
        },
        {
          index: "4",
          title: "进入受理",
        },
      ],
      selectData: [
        {
          index: "0",
          title: "默认自定情形",
        },
        {
          index: "1",
          title: "我要办理社会保险缴费",
        },
        {
          index: "2",
          title: "我要办理占用、挖掘公路、公路用地或者使公路改线审批",
        },
        {
          index: "3",
          title: "我要办理占用、挖掘公路、公路用地或者使公路改线审批",
        },
        {
          index: "4",
          title: "我要办理建设项目环评",
        },
        {
          index: "5",
          title: "我要办理农业转基因生物加工审批",
        },
        {
          index: "6",
          title: "我要办理占用、挖掘公路、公路用地或者使公路改线审批",
        },
        {
          index: "7",
          title: "普通高校毕业生就业报到证补发",
        },
        {
          index: "8",
          title: "我要办理建设项目环评",
        },
        {
          index: "9",
          title: "我要办理农业转基因生物加工审批",
        },
      ],
      labelPosition: "top",
      checkList: ["变更经营地址"],
      checkList02: ["内资企业02"],
      checkList03: ["变更单位名称03"],
      ruleForm: {
        name: "",
        region: "",
      },
      rules: {
        name: [
          { required: true, message: "必填项", trigger: "blur" },
          { min: 3, max: 5, message: "长度在 3 到 5 个字符", trigger: "blur" },
        ],
        region: [
          { required: true, message: "请选择证件类型", trigger: "change" },
        ],
      },
    };
  },
  methods: {
    // stepChange(index) {
    //   this.i = index;
    // },
    selectChange(index) {
      this.num = index;
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          alert("submit!");
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
  },
};
</script>
<style lang="scss" scoped>
.process-box {
  padding: 15px;
  box-sizing: border-box;
  text-align: left;
}
.process-box .step-title {
  font-size: 14px;
  color: #333;
}
.process-box .step-title span {
  display: inline-block;
  vertical-align: middle;
  background: url(../../assets/image/step-lastbg.png) no-repeat center;
  width: 100px;
  height: 30px;
  line-height: 30px;
  text-align: center;
  cursor: pointer;
}
.process-box .step-title span:first-child {
  background: url(../../assets/image/step-firstbg.png) no-repeat center;
  width: 100px;
  height: 30px;
}
.process-box .step-title span.active {
  background: url(../../assets/image/step-lastbg-active.png) no-repeat center;
  width: 100px;
  height: 30px;
  color: #fff;
}
.process-box .step-title span:first-child.active {
  background: url(../../assets/image/step-firstbg-active.png) no-repeat center;
  width: 100px;
  height: 30px;
  color: #fff;
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
  content: "";
  position: absolute;
  width: 19px;
  height: 13px;
  right: 10px;
  top: 13px;
  background: url(../../assets/image/check-icon.png) no-repeat center;
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
</style>
