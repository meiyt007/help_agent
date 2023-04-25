<template>
  <div class="el-table__header-wrapper dialog-table">
    <el-row :gutter="20">
      <el-col :span="18">
        <h3><i class="el-icon-document"></i>智审材料</h3>
        <el-form
          label-width="0px"
          class="demo-ruleForm"
          :label-position="labelPosition"
        >
          <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
            <colgroup>
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>申请单位：</b></td>
              <td>
                卓繁信息
                <div class="fault-text">
                  <img src="../assets/image/fail-icon.png" alt="" srcset="">
                  智审不通过文子描述
                </div>
              </td>
            </tr>
            <tr>
              <td><b>联系人：</b></td>
              <td>
                郭曼曼
                <div class="fault-text">
                  <img src="../assets/image/fail-icon.png" alt="" srcset="">
                  智审不通过文子描述
                </div>
              </td>
            </tr>
            <tr>
              <td><b>电话：</b></td>
              <td>
                18256914330
                 <div class="success-text">
                  <img src="../assets/image/success-icon.png" alt="" srcset="">
                  智审通过
                </div>
              </td>
            </tr>
            <tr>
              <td><b>地址：</b></td>
              <td>
                安徽省合肥市蜀山区华邦蜀山里
                <div class="success-text">
                  <img src="../assets/image/success-icon.png" alt="" srcset="">
                  智审通过
                </div>
              </td>
            </tr>
            <tr>
              <td><b>占用地段：</b></td>
              <td>
                大蜀山
                <div class="success-text">
                  <img src="../assets/image/success-icon.png" alt="" srcset="">
                  智审通过
                </div>                
              </td>
            </tr>
            <tr>
              <td><b>占用地段：</b></td>
              <td>
                大蜀山
                <div class="success-text">
                  <img src="../assets/image/success-icon.png" alt="" srcset="">
                  智审通过
                </div>                
              </td>
            </tr>
            <tr>
              <td><b>占用面积、类型：</b></td>
              <td>
                120,100m3
                <div class="success-text">
                  <img src="../assets/image/success-icon.png" alt="" srcset="">
                  智审通过
                </div>                
              </td>
            </tr>
            <tr>
              <td><b>占用期限：</b></td>
              <td>
                3年
                <div class="success-text">
                  <img src="../assets/image/success-icon.png" alt="" srcset="">
                  智审通过
                </div>                
              </td>
            </tr>
            <tr>
              <td><b>影响交通市政设施情况：</b></td>
              <td>
                占用人行道占用人行道占用人行道占用人行道
                <div class="success-text">
                  <img src="../assets/image/success-icon.png" alt="" srcset="">
                  智审通过
                </div>
              </td>
            </tr>

          </table>
        </el-form>
      </el-col>
      <el-col :span="6">
        <h3><i class="el-icon-document"></i>材料模板信息</h3>
        <div class="moudle-box">
          <img src="../assets/image/moudle-pic.png" alt="" srcset="">
        </div>
      </el-col>
    </el-row>

  </div>
</template>

<script>
export default {
  data() {
    return {
      labelPosition: "top",
   };
  },
  methods: {

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
.fault-text{
  margin: 5px 0 0 0;
  font-size: 12px;
  color: #ff0000;
}
.fault-text>img{
  display: inline-block;
  vertical-align: top;
  width: 12px;
  height: 12px;
}
.success-text{
  margin: 5px 0 0 0;
  font-size: 12px;
  color: #00b45e;
}
.success-text>img{
  display: inline-block;
  vertical-align: top;
  width: 12px;
  height: 12px;
}
.moudle-box{
  padding: 20px;
  background-color: #f5f6f8;
  box-sizing: border-box;
}
.moudle-box>img{
  width: 100%;
}



</style>
