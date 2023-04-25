<template>
  <div class="app-container">
    <el-form ref="form" label-width="0px" class="demo-ruleForm">
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <tr>
          <td><i class="require">*</i><b>总金额：</b></td>
          <td>22</td>
          <td><i class="require">*</i><b>应收金额：</b></td>
          <td>22</td>
        </tr>
        <tr>
          <td><i class="require">*</i><b>实收金额：</b></td>
          <td colspan="3">
            <el-form-item prop="standardName">
              <el-input
                v-model.trim="standardName"
                placeholder="请输入实收金额"
                maxlength="10"
                show-word-limit
              />
            </el-form-item>
          </td>
        </tr>
      </table>
    </el-form>

    <el-table :data="tableData" style="width: 100%">
      <el-table-column prop="num" label="序号" width="50"> </el-table-column>
      <el-table-column prop="name" label="收费项目名称" width="220">
      </el-table-column>
      <el-table-column prop="totalNum" label="数量/项目总额"> </el-table-column>
      <el-table-column prop="danjia" label="单价/费率"> </el-table-column>
      <el-table-column prop="jiamian" label="减免金额"> </el-table-column>
      <el-table-column prop="danwei" label="单位"> </el-table-column>
      <el-table-column prop="jine" label="收费金额"> </el-table-column>
    </el-table>
    <div slot="footer" class="dialog-footer">
      <el-button type="warning" @click="saoma">扫码</el-button>
      <el-button type="primary" @click="submitForm">确定缴费</el-button>
      <el-button @click="closeDialog">关 闭</el-button>
    </div>
  </div>
</template>

<script>
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import iconfont from '@/views/common/iconfontSelect'
import {
  openScanningGun, activeScanningGun, getActiveMessage
} from "@/api/zc/businessManagement/charge";
export default {
  components: { Treeselect, iconfont },
  name: "App",
  data () {
    return {
      // 遮罩层
      loading: true,
      checkFlag: false,
      loadingShow: false,
      activeMessage: "",
      // 表单参数
      standardName: "22",
      tableData: [{
        num: '1',
        name: '材料打印手续费1',
        totalNum: '2',
        danjia: '5.0',
        jiamian: '0.0',
        danwei: '元',
        jine: '10'
      }, {
        num: '2',
        name: '材料打印手续费2',
        totalNum: '10',
        danjia: '2.0',
        jiamian: '0.0',
        danwei: '元',
        jine: '20'
      }, {
        num: '3',
        name: '材料打印手续费3',
        totalNum: '1',
        danjia: '2.0',
        jiamian: '0.0',
        danwei: '元',
        jine: '2'
      }],
    };
  },
  created () {
  },
  methods: {
    // 表单重置
    reset () {
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 提交按钮 */
    submitForm: function () {
      let _that = this;
      _that.validateNumber(_that.standardName);
      if (!_that.checkFlag) {
        _that.msgSuccess("缴费成功！");
        let charge = {};
        charge.flag = "2";
        _that.$emit('case-close', charge);
      }
    },
    /** 提交按钮 */
    saoma: function () {
      //打开扫描枪
      this.validateNumber(this.standardName);
      if (!this.checkFlag) {
        const loading = this.$loading({
          lock: true,
          text: '正在扫描中，请等待...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        openScanningGun().then(response => {
          if (response != null) {
            let json = response;
            if (json.state == 'sucess') {
              //成功后激活扫描枪
              activeScanningGun().then(res => {
                if (res.data != null) {
                  //if(res.data.data !=""){
                  loading.close();
                  this.msgSuccess("扫码缴费成功！")
                  let charge = {};
                  charge.flag = "1";
                  this.$emit('case-close', charge);
                  //}
                } else {
                  loading.close();
                  this.msgError("扫码缴费失败!")
                  return false;
                }
              });
            } else {
              loading.close();
              this.msgError("扫码缴费失败!")
              return false;
            }
          } else {
            loading.close();
            this.msgError("扫码缴费失败!")
            return false;
          }
        });
      }

    },

    closeDialog: function () {
      let _that = this;
      _that.$emit('case-close', '');
    },
    validateNumber (value) {
      this.checkFlag = false;
      let numberReg = /^\d+$|^\d+[.]?\d+$/
      if (value !== '') {
        if (!numberReg.test(value)) {
          this.checkFlag = true;
          this.msgError("请输入数字！");
          return false;
        }
      } else {
        this.checkFlag = true;
        this.msgError("实际金额不能为空！");
        return false;
      }
    }
  }
};
</script>
