<template>
  <!-- start print -->
  <div class="process-box" id="print">
    <div class="step-title">
      <span
        v-for="(item, index) in stepData"
        :key="item.index"
        :class="{ active: index == i }"
      >{{ item.title }}</span
      >
    </div>
    <!-- 第一步 -->
    <div class="step-content step-first" v-if="first_show">
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
            <col width="15%"/>
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
      <el-button type="primary" icon="el-icon-circle-check" class="next-btn" @click="stepChange(1,1)"
      >下一步
      </el-button
      >
    </div>
    <!-- 第二步 -->
    <div class="step-content step-second" v-if="second_show">
      <div class="situation-box">
        <h3 class="title-detail">变更社会保险费缴费信息告知单</h3>
        <el-form
          :model="ruleForm"
          :rules="rules"
          ref="ruleForm"
          label-width="0px"
          class="demo-ruleForm"
          :label-position="labelPosition"
        >
          <table cellspacing="0" cellpadding="0" border="0">
            <colgroup>
              <col width="15%"/>
              <col width="35%"/>
              <col width="15%"/>
              <col width="35%"/>
            </colgroup>
            <tr>
              <td><b>事项名称：</b></td>
              <td>变更社会保险费缴费信息</td>
              <td><b>事项类型：</b></td>
              <td>行政权力</td>
            </tr>
            <tr>
              <td><b>实施机构：</b></td>
              <td>安徽省社保局</td>
              <td><b>办理地点：</b></td>
              <td>合肥市政务服务大厅</td>
            </tr>
            <tr>
              <td><b>办理时间：</b></td>
              <td>一周至周五 9:00-17:30</td>
              <td><b>咨询电话：</b></td>
              <td>0551-69082901</td>
            </tr>
            <tr>
              <td><b>承诺时限：</b></td>
              <td>10工作日</td>
              <td><b>法定时限：</b></td>
              <td>10工作日</td>
            </tr>
            <tr>
              <td><b>申请受理条件：</b></td>
              <td colspan="3">申请受理条件</td>
            </tr>
            <tr>
              <td><b>事项情形：</b></td>
              <td colspan="3">事项情形</td>
            </tr>
            <tr>
              <td><b>情形选项：</b></td>
              <td colspan="3">情形选项</td>
            </tr>
            <tr>
              <td><b>申请材料：</b></td>
              <td colspan="3">1、申请材料1<br>2、申请材料2</td>
            </tr>
            <tr>
              <td><b>办事流程：</b></td>
              <td colspan="3">办事流程</td>
            </tr>
            <tr>
              <td><b>常见问题：</b></td>
              <td colspan="3">常见问题</td>
            </tr>
            <tr>
              <td><b>说明：</b></td>
              <td colspan="3">常见问题</td>
            </tr>
          </table>
          <div class="btn-wrap">
            <div class="btn-list mt10">
              <el-button type="primary" icon="el-icon-circle-check" @click="stepChange(0,-1)"
              >上一步</el-button
              >
              <el-button type="info" icon="el-icon-video-pause" class="print-btn" v-print="'#print'"
              >打印一次性告知单</el-button
              >
            </div>
          </div>
        </el-form>
      </div>
    </div>
  </div>
  <!-- end print -->
</template>

<script>
export default {
  data() {
    return {
      i: 0,
      num: 0,
      num1: 1,
      num2: 2,
      first_show: true,
      second_show: false,
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
          title: "一次性告知单",
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
          {required: true, message: "必填项", trigger: "blur"},
          {min: 3, max: 5, message: "长度在 3 到 5 个字符", trigger: "blur"},
        ],
        region: [
          {required: true, message: "请选择证件类型", trigger: "change"},
        ],
      },
    };
  },
  methods: {
    stepChange(index, count) {
      this.i = index;
      if (count > 0) {
        this.first_show = false;
        this.second_show = true;
      } else {
        this.first_show = true;
        this.second_show = false;
      }
    },
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
    //  点击打印按钮
    clickPrinting() {
      //  找到需要隐藏的DOM节点
      let app_main = document.getElementsByClassName('app-main')[0];
      let tags_view_container = document.getElementsByClassName('tags-view-container')[0];
      let tags_view_wrapper = document.getElementsByClassName('scroll-container tags-view-wrapper el-scrollbar')[0];
      let el_scrollbar_wrap = document.getElementsByClassName('el-scrollbar__wrap')[0];
      let navbar = document.getElementsByClassName('navbar')[0];
      let hamburger_container = document.getElementsByClassName('hamburger-container')[0];
      let step_title = document.getElementsByClassName('step-title')[0];
      let btn_wrap = document.getElementsByClassName('btn-wrap')[0];

      alert(navbar);
      alert(hamburger_container);
      //  给对应DOM添加class
      tags_view_container.classList.add("printHideCss");
      app_main.classList.add("printHideCss");
      navbar.classList.add("printHideCss");
      hamburger_container.classList.add("printHideCss");
      tags_view_wrapper.classList.add("printHideCss");
      el_scrollbar_wrap.classList.add("printHideCss");
      step_title.classList.add("printHideCss");
      btn_wrap.classList.add("printHideCss");

      window.print(); //  调用打印功能
      window.location.reload(); //  点击取消打印后刷新页面，恢复点击打印按钮之前的原始数据
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
  background: url(../assets/image/step-lastbg.png) no-repeat center;
  width: 100px;
  height: 30px;
  line-height: 30px;
  text-align: center;
  cursor: pointer;
}

.process-box .step-title span:first-child {
  background: url(../assets/image/step-firstbg.png) no-repeat center;
  width: 100px;
  height: 30px;
}

.process-box .step-title span.active {
  background: url(../assets/image/step-lastbg-active.png) no-repeat center;
  width: 100px;
  height: 30px;
  color: #fff;
}

.process-box .step-title span:first-child.active {
  background: url(../assets/image/step-firstbg-active.png) no-repeat center;
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
  background: url(../assets/image/check-icon.png) no-repeat center;
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
.title-detail{
  text-align: center;
}
.printHideCss{
  display:none;
}

</style>
