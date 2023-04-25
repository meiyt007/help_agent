<template>
  <div>
    <el-form ref="form" :rules="rules" label-width="0px" class="demo-ruleForm">
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <tr>
          <td><b>总金额：</b></td>
          <td>22</td>
          <td><b>应收金额：</b></td>
          <td>22</td>
        </tr>
        <tr>
          <td><b>实收金额：</b></td>
          <td colspan="3">22</td>
        </tr>
      </table>
    </el-form>

    <el-table :data="tableData" style="width: 100%">
      <el-table-column prop="num" label="序号" width="180"> </el-table-column>
      <el-table-column prop="name" label="收费项目名称" width="180">
      </el-table-column>
      <el-table-column prop="totalNum" label="数量/项目总额"> </el-table-column>
      <el-table-column prop="danjia" label="单价/费率"> </el-table-column>
      <el-table-column prop="jiamian" label="减免金额"> </el-table-column>
      <el-table-column prop="danwei" label="单位"> </el-table-column>
      <el-table-column prop="jine" label="收费金额"> </el-table-column>
    </el-table>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">关 闭</el-button>
    </div>
  </div>
</template>

<script>
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import iconfont from '@/views/common/iconfontSelect'
export default {
  components: { Treeselect, iconfont },
  name: "App",
  data () {
    return {
      // 遮罩层
      loading: true,
      // 表单参数
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
    closeDialog: function () {
      let _that = this;
      _that.$emit('case-close');
    },
  }
};
</script>
