<template>
  <div class="el-table__header-wrapper dialog-table">
    <h3><i class="el-icon-document"></i>事项标题</h3>
    <el-form
      :model="ruleForm"
      :rules="rules"
      ref="ruleForm"
      label-width="0px"
      class="demo-ruleForm"
      :label-position="labelPosition"
    >
      <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
        <colgroup>
          <col width="15%" />
          <col width="35%" />
          <col width="15%" />
          <col width="35%" />
        </colgroup>
        <tr>
          <td><b>上级字典名称：</b></td>
          <td>数据字典</td>
          <td><b>字典代码：</b></td>
          <td>ZZJGLX</td>
        </tr>
        <tr>
          <td><b>上级字典名称：</b></td>
          <td>数据字典</td>
          <td><b>字典代码：</b></td>
          <td>ZZJGLX</td>
        </tr>
        <tr>
          <td><b>上级字典名称：</b></td>
          <td colspan="3">数据字典</td>
        </tr>
        <tr>
          <td><b>上级字典名称：</b></td>
          <td>
            <el-form-item prop="name">
              <el-col :span="20">
                <el-input v-model="ruleForm.name"></el-input>
              </el-col>
            </el-form-item>
          </td>
          <td><b>字典代码：</b></td>
          <td>ZZJGLX</td>
        </tr>
        <tr>
          <td><b>活动区域：</b></td>
          <td>
            <el-form-item prop="region">
              <el-col :span="24">
                <el-select
                  v-model="ruleForm.region"
                  placeholder="请选择活动区域"
                >
                  <el-option label="区域一" value="shanghai"></el-option>
                  <el-option label="区域二" value="beijing"></el-option>
                </el-select>
              </el-col>
            </el-form-item>
          </td>
          <td><b>活动时间：</b></td>
          <td>
            <el-form-item required>
              <el-col :span="11">
                <el-form-item prop="date1">
                  <el-date-picker
                    type="date"
                    placeholder="选择日期"
                    v-model="ruleForm.date1"
                    style="width: 100%"
                  ></el-date-picker>
                </el-form-item>
              </el-col>
              <el-col class="line" :span="2">-</el-col>
              <el-col :span="11">
                <el-form-item prop="date2">
                  <el-time-picker
                    placeholder="选择时间"
                    v-model="ruleForm.date2"
                    style="width: 100%"
                  ></el-time-picker>
                </el-form-item>
              </el-col>
            </el-form-item>
          </td>
        </tr>
        <tr>
          <td><b>即时配送：</b></td>
          <td>111</td>
          <td><b>字典代码：</b></td>
          <td>
            <el-radio v-model="radio" label="1">选项1</el-radio>
            <el-radio v-model="radio" label="2">选项2</el-radio>
          </td>
        </tr>
        <tr>
          <td><b>即时配送：</b></td>
          <td>
            <el-form-item label="" prop="delivery">
              <el-col :span="24">
                <el-switch v-model="ruleForm.delivery"></el-switch>
              </el-col>
            </el-form-item>
          </td>
          <td><b>字典代码：</b></td>
          <td>
            <el-checkbox-group v-model="checkList">
              <el-checkbox label="复选框 A"></el-checkbox>
              <el-checkbox label="复选框 B"></el-checkbox>
              <el-checkbox label="复选框 C"></el-checkbox>
            </el-checkbox-group>
          </td>
        </tr>
        <tr>
          <td><b>文本域：</b></td>
          <td colspan="3">
            <el-form-item label="" prop="desc">
              <el-col :span="24">
                <el-input type="textarea" v-model="ruleForm.desc"></el-input>
              </el-col>
            </el-form-item>
          </td>
        </tr>
        <tr>
          <td><b>附件下载：</b></td>
          <td colspan="3">
            <label for="文件名">附件名称.doc</label>
            <el-button type="primary" class="el-button el-button--mini ml5"
            ><i class="el-icon-download el-icon--right"></i>下载</el-button
            >
          </td>
        </tr>
      </table>
    </el-form>
    <div class="mt20">
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="name" label="附件名称" width="300">
        </el-table-column>
        <el-table-column prop="size" label="附件大小" width="300">
        </el-table-column>
        <el-table-column prop="handle" label="操作">
          <el-button type="success" size="mini">下载</el-button>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        tableData: [
          {
            name: "01.png",
            size: "14M",
            handle: "下载",
          },
          {
            name: "01.png",
            size: "14M",
            handle: "下载",
          },
          {
            name: "01.png",
            size: "14M",
            handle: "下载",
          }
        ],
        labelPosition: "top",
        radio: 1,
        checkList: ["复选框 A"],
        ruleForm: {
          name: "",
          region: "",
          date1: "",
          date2: "",
          delivery: false,
          type: [],
          resource: "",
          desc: "",
        },
        rules: {
          name: [
            { required: true, message: "必填项", trigger: "blur" },
            { min: 3, max: 5, message: "长度在 3 到 5 个字符", trigger: "blur" },
          ],
          region: [
            { required: true, message: "请选择活动区域", trigger: "change" },
          ],
          date1: [
            {
              type: "date",
              required: true,
              message: "请选择日期",
              trigger: "change",
            },
          ],
          date2: [
            {
              type: "date",
              required: true,
              message: "请选择时间",
              trigger: "change",
            },
          ],
          type: [
            {
              type: "array",
              required: true,
              message: "请至少选择一个活动性质",
              trigger: "change",
            },
          ],
          resource: [
            { required: true, message: "请选择活动资源", trigger: "change" },
          ],
          desc: [{ required: true, message: "请填写活动形式", trigger: "blur" }],
        },
      };
    },
    methods: {
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
  .dialog-table table tr td:nth-of-type(2n+1){
    background-color: #f8f8f9;
  }
  .dialog-table table tr td:nth-of-type(2n) {
    color: #606266;
    text-align: left;
  }
  .dialog-table .el-form-item {
    margin-bottom: 0;
  }

  .dialog-table .line {
    text-align: center;
  }


</style>
