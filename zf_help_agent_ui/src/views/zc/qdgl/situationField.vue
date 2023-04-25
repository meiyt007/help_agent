<template>
  <div>
    <div class="primary-table">
      <el-table :data="situationList" border :fit="true">
        <el-table-column label="序号" width="55" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="选项标题"
          prop="name"
          align="center"
          :show-overflow-tooltip="true"
        />
        <el-table-column type="expand" label="选项值" width="100">
          <template slot-scope="props">
            <template v-for="(item, index) in props.row.sxServiceOptionVals">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item style="width: 3%" align="center">
                  <span>{{ index + 1 }}</span>
                </el-form-item>
                <el-form-item style="width: 40%" align="center">
                  <span>{{ item.name }}</span>
                </el-form-item>
                <el-form-item style="width: 5%" align="center">
                  <span>
                    <el-button
                      size="mini"
                      type="text"
                      icon="el-icon-folder-add"
                      class="handle-btn"
                      @click="handleGlMaterial(item.titleOid, item.oid)"
                      >关联字段</el-button
                    >
                  </span>
                </el-form-item>
              </el-form>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog
    v-dialog-drag
      :close-on-click-modal="false"
      title="关联字段"
      :visible.sync="openInit"
      v-if="openInit"
      width="900px"
      height="600px"
      scrollbar
      append-to-body
    >
      <el-form>
        <el-select v-model="form.fieldTypeOid" @change="changeType">
          <el-option v-for="item in sxFormInfoList" :key="item.formOid" :label="item.formName" :value="item.fieldTypeOid">
          </el-option>
        </el-select>
      </el-form>

      <el-table
        ref="multipleTable"
        :data="sxOptionFieldList"
        tooltip-effect="dark"
        @selection-change="handleSelectionChange"
        border
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column
          prop="fieldName"
          align="center"
          label="字段名称"
          show-overflow-tooltip
        />
        <el-table-column
          prop="fieldCode"
          align="center"
          label="字段编码"
          show-overflow-tooltip
        />
        <el-table-column
          prop="labelName"
          align="center"
          label="所属标签"
          show-overflow-tooltip
        />
        <el-table-column
          prop="fieldTypeName"
          align="center"
          label="所属分类"
          width="150"
          show-overflow-tooltip
        />
      </el-table>
      <div slot="footer" style="text-align:center">
        <el-button type="primary" @click="batchChoose">确 定</el-button>
        <el-button @click="cancelDig">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {pageTitleAndOption, getSxFormList} from "@/api/zc/qdgl/sxService.js";
import {sxFillFieldList, saveOrUpdateOptionField, valOptionFieldList} from "@/api/zc/qdgl/sxOptionField.js";
import Treeselect from '@riophae/vue-treeselect';
import "@riophae/vue-treeselect/dist/vue-treeselect.css";


export default {
  name: "SituationField",
  components: {Treeselect },
  props: ["serviceOid"],
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      situationList: [],
      multipleSelection: [],
      sxOptionFieldList: [],
      allSelectRel: [],
      sxFormInfoList: [],
      openInit: false,
      relConfigView: false,
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created () {
    this.getList();
  },

  methods: {
    /**
     * 查询所有的标题和选项
     */
    getList () {
      this.loading = true;
      pageTitleAndOption(this.serviceOid).then(response => {
        this.situationList = response.data;
        this.loading = false;
      });
    },
    // 复选
    reselectFun (dataArr, IDArr) {
      if (dataArr && IDArr) {
        this.$nextTick(function () {
          dataArr.forEach(row => {
            if (IDArr.includes(row.fieldOid)) {
              this.$refs.multipleTable.toggleRowSelection(row, true);
            }
          });
        });
      }
    },
    //保存选中的结果
    handleSelectionChange (val) {
      this.multipleSelection = val;
    },
    // 配置选项与字段
    handleGlMaterial (titleOid, oid) {
      this.form.titleOid = titleOid;
      this.form.valOid = oid;
      this.allSelectRel = [];
      this.multipleSelection = [];
      getSxFormList(this.serviceOid).then(resp => {
        // 表单集合
        this.sxFormInfoList = resp.data;
        if(this.sxFormInfoList != null) {
          //默认查询第一个事项表单中所有字段
          let typeOid = this.sxFormInfoList[0].fieldTypeOid;
          this.form.fieldTypeOid = typeOid;
          this.reselect(this.serviceOid, typeOid);
        }
      })
      this.openInit = true;
    },
    // 切换表单
    changeType(val) {
      this.form.fieldTypeOid = val;
      this.reselect(this.serviceOid, val);
    },
    // 重新赋值已选择的字段
    reselect(serviceOid, fieldTypeOid) {
      sxFillFieldList(serviceOid, fieldTypeOid).then(response => {
        this.sxOptionFieldList = response.data;
        //查询val关联的字段信息
        if(response.data) {
          valOptionFieldList(this.form.valOid).then(res => {
            let valRel = res.data;
            this.allSelectRel = valRel.map(ite => ite.fieldOid);
            this.reselectFun(this.sxOptionFieldList, this.allSelectRel);
          })
        }
      })
    },
    // 保存提交
    batchChoose () {
      let fieldOids = "";
      if (this.multipleSelection && this.multipleSelection.length > 0) {
        fieldOids = this.multipleSelection.map(ite => ite.fieldOid).join(",");
      }
      this.form.fieldOids = fieldOids;
      this.form.serviceOid = this.serviceOid;
      saveOrUpdateOptionField(this.form).then(response => {
        if (response.data == 1) {
          this.$message.success("保存成功");
          this.openInit = false;
        } else {
          this.$message.error("保存失败!");
        }
      })
    },
    cancelDig () {
      this.openInit = false;
    },
  },
};
</script>
<style lang="scss" scoped>
.primary-table h3 {
  font-size: 14px;
  font-weight: normal;
  color: #333;
  margin: 0px 0px 10px 0px;
}

.primary-table .handle-btn {
  border: none;
  background: none;
  padding: 0;
  font-size: 16px;
}

.primary-table .el-table,
.primary-table .el-table th {
  font-size: 13px;
}

.primary-table .el-form-item:first-child {
  margin-left: 8px;
}

.primary-table .el-form-item {
  margin-bottom: 0;
  margin-right: 0;
  margin-left: 0px !important;
  padding: 0;
  box-sizing: border-box;
  font-size: 13px;
}

.primary-table .el-form-item__content {
  font-size: 13px;
}

.primary-table .inputTable .el-form-item:first-child {
  margin-left: 0;
}

.primary-table .inputTable .el-form-item {
  width: 100px;
}

.ruleForm-table table {
  border: 1px solid #ebeef5;
  border-collapse: collapse;
}

.ruleForm-table table tr th {
  color: #515a6e;
  font-size: 13px;
  background-color: #f8f8f9;
  text-align: left;
}

.ruleForm-table table tr th,
.ruleForm-table table tr td {
  padding: 17px 10px;
  font-size: 13px;
  color: #606266;
  border: 1px solid #ebeef5;
}

.ruleForm-table .el-form-item {
  margin-left: 0px !important;
  width: 120px;
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

.dialog-table table tr td:nth-of-type(2n) {
  color: #606266;
  text-align: left;
}

.dialog-table .el-form-item {
  margin-bottom: 0;
}

.el-form-item--small .el-form-item_content {
  margin-left: 0px !important;
}

.line {
  text-align: center;
}

.add-btn {
  margin: 10px 0px;
  float: right;
}

.dialog-table table.tc tr td {
  text-align: center;
}
</style>
