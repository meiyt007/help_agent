/*
* @Description:查看情形
* @Author: dxl
**/
<template>
    <div>
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td align="right">
                <b>情形名称：</b>
              </td>
              <td colspan="3">{{form.situationName}}
              </td>
            </tr>
            <tr>
              <td>
                <b>展示类型：</b>
              </td>
              <td>
                <div v-if="form.columnType==1">单选</div>
                <div v-if="form.columnType==2">多选</div>
                <div v-if="form.columnType==3">文本</div>
              </td>
              <td>
                <b>是否必填：</b>
              </td>
              <td>
                <div v-if="form.mustStatus==1">是</div>
                <div v-if="form.mustStatus==0">否</div>
              </td>
            </tr>
            <tr>
              <td align="right">
                <b>必填提示信息：</b>
              </td>
              <td colspan="3">{{form.tips}}
              </td>
            </tr>
            <tr>
              <td>
                <b>标题是否展示：</b>
              </td>
              <td>
                <div v-if="form.titleShowStyle==1">是</div>
                <div v-if="form.titleShowStyle==0">否</div>
              </td>
              <td>
                <b>排序号：</b>
              </td>
              <td>{{form.sort}}
              </td>
            </tr>
            <tr>
              <td>
                <b>默认值是否不可改：</b>
              </td>
              <td>
                <div v-if="form.isModificationStatus==1">不可改</div>
                <div v-if="form.isModificationStatus==0">可改</div>
              </td>
              <td>
                <b>是否链接：</b>
              </td>
              <td>
                <div v-if="form.linkStatus==1">是</div>
                <div v-if="form.linkStatus==0">否</div>
              </td>
            </tr>
            <tr>
              <td align="right">
                <b>链接地址：</b>
              </td>
              <td colspan="3">{{form.linkUrl}}
              </td>
            </tr>
          </table>
        <div style="margin:20px 0">
          <el-table :data="comboOptionValList" border :fit="true" >
            <el-table-column label="选项值名称" prop="name" align="center" width="200" show-overflow-tooltip/>
            <el-table-column label="编码[相同互斥]" prop="optionCode" align="center" width="150" show-overflow-tooltip/>
            <el-table-column label="营业范围校验名称" prop="verifyName" align="center" width="150" show-overflow-tooltip/>
            <el-table-column label="校验规则" align="center" prop="validRule" width="250" show-overflow-tooltip/>
            <el-table-column label="备注" align="center" prop="remark" show-overflow-tooltip/>
            <el-table-column label="备注显示方式" prop="remarkShowStyle"  align="center" width="130" show-overflow-tooltip/>
            <el-table-column label="是否默认" align="center" width="100">
              <template slot-scope="scope">
                <span v-if="scope.row.defaultFlag==1">是</span>
              </template>
            </el-table-column>
            <el-table-column label="排序号" align="center" prop="sort" width="100" show-overflow-tooltip/>
          </el-table>
    </div>

 </div>
</template>
<script>
import {
  initSituation
} from "@/api/onething/comboSituation/comboSituationIndex.js";
export default {
  components: {},
  name: "SituationView",
  //定义获取父类传过来值的格式
  props: ["situationOid", "comboDirectoryOid"],
  data() {
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
        moreStatus: 0
      },
    }
  },
  created() {
    this.handleInit();
  },
  methods: {

    // 表单重置
    reset() {
      this.form = {
        fillFlag: 0,
        moreStatus: 0
      },
        this.resetForm("form")
    },

    /** 新增和修改按钮操作 */
    handleInit() {
      let _that = this;
      this.reset();
      let oid = this.situationOid;
      if (oid) { //修改
        initSituation(oid).then(response => {
          if (response.data != undefined) {
            this.form = response.data;
            this.comboOptionValList = response.data.optionValsList;
            this.comboOptionValList.forEach(optVal => {
              if (null != optVal.defaultFlag && optVal.defaultFlag === 1) {
                optVal.ifChecked = true;
              } else {
                optVal.ifChecked = false;
              }
            });
          }
        });
      } else { //新增
        _that.title = "选项标题新增";
      }
    },

    //关闭配置选项页
    cancelComboOptionTitle() {
      this.$emit('combo-directory');
    },
  }

}

</script>
