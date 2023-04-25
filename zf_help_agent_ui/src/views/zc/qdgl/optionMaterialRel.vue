/**
* @Author: xldong
*/
<template>
  <div>
    <div class="primary-table">
      <el-table :data="titleValList" border :fit="true">
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
        <el-table-column
          label="是否必填"
          prop="fillFlag"
          align="center"
          width="100"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.fillFlag == 0">否</span>
            <span v-if="scope.row.fillFlag == 1">是</span>
          </template>
        </el-table-column>
        <el-table-column
          label="是否多选"
          prop="moreStatus"
          align="center"
          width="100"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.moreStatus == 0">否</span>
            <span v-if="scope.row.moreStatus == 1">是</span>
          </template>
        </el-table-column>
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
                      @click="handleGlMaterial(item.oid)"
                      >关联材料</el-button
                    >
                  </span>
                </el-form-item>
              </el-form>
            </template>
          </template>
        </el-table-column>

        <!--            <el-table-column label="操作" prop="handle" align="center" width="180">
              <template slot-scope="scopew">
                <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scopew.row)">查看</el-button>
                <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDelete(scopew.row)">删除</el-button>
                <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" @click="handleInit(scopew.row)">配置</el-button>
              </template>

            </el-table-column>-->
      </el-table>
    </div>
    <el-dialog
    v-dialog-drag
      :close-on-click-modal="false"
      title="关联材料"
      :visible.sync="openInit"
      v-if="openInit"
      width="900px"
      height="600px"
      scrollbar
      append-to-body
    >
      <el-table
        ref="multipleTable"
        :data="sxServiceMaterialList"
        tooltip-effect="dark"
        @selection-change="handleSelectionChange"
        border
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column
          prop="materialName"
          align="center"
          label="材料名称"
          show-overflow-tooltip
        />
        <el-table-column
          prop="materialFormat"
          align="center"
          width="100"
          label="材料形式"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.materialFormat == 1">纸质</span>
            <span v-if="scope.row.materialFormat == 2">电子版</span>
            <span v-if="scope.row.materialFormat == 3">证照</span>
            <span v-if="scope.row.materialFormat == 4">容缺补正</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="materialType"
          align="center"
          label="材料类型"
          width="150"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.materialType == 0">原件</span>
            <span v-if="scope.row.materialType == 1">复印件</span>
            <span v-if="scope.row.materialType == 2">原件和复印件</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="materialSampleName"
          align="center"
          label="材料样本名称"
          width="150"
          show-overflow-tooltip
        />
        <el-table-column
          prop="materialSource"
          align="center"
          label="来源渠道"
          width="150"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.materialSource == 0">申请人自备</span>
            <span v-if="scope.row.materialSource == 1">政府部门核发</span>
            <span v-if="scope.row.materialSource == 2">其它</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="paperNumber"
          align="center"
          width="100"
          label="材料份数"
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
import { pageTitleAndOption, sxMaterialList, valMaterialList, saveOrUpdateMaterOptRel,getOptionMaterialByValOid } from "@/api/zc/qdgl/sxService.js";
import Treeselect from '@riophae/vue-treeselect';
import "@riophae/vue-treeselect/dist/vue-treeselect.css";


export default {
  name: "OptionMaterialRel",
  components: {Treeselect },
  props: ["serviceOid"],
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      titleValList: [],
      multipleSelection: [],
      sxServiceMaterialList: [],
      allSelectRel: [],
      openInit: false,
      relConfigView: false,
      relOid: "",
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
        this.titleValList = response.data;
        this.loading = false;
      });
    },
    //保存选中的结果
    handleSelectionChange (val) {
      this.multipleSelection = val;
    },
    handleGlMaterial (valOid) {
      this.form.valOid = valOid;
      this.allSelectRel = [];
      //查询事项下面所有的事项材料
      sxMaterialList(this.serviceOid).then(response => {
        this.sxServiceMaterialList = response.data;
        //查询val关联的材料信息
        if (response.data) {
          valMaterialList(valOid).then(res => {
            let valRel = res.data;
            this.allSelectRel = valRel.map(ite => ite.sxMaterialOid);
            this.reselectFun(this.sxServiceMaterialList, this.allSelectRel);
            //console.log("SSSSthis.allSelectRel="+this.allSelectRel)
           /* getOptionMaterialByValOid(valOid).then(ress => {
              let materialOid="";
              if(ress.data.materialOid!=null){
                materialOid=ress.data.materialOid;
              }
              console.log("materialOid="+materialOid)
              if(this.allSelectRel!=""){
                if(materialOid!=''){
                  this.allSelectRel=this.allSelectRel+","+materialOid;
                }
              }else{
                this.allSelectRel=materialOid;
              }
              console.log("this.allSelectRel="+this.allSelectRel)
              this.reselectFun(this.sxServiceMaterialList, this.allSelectRel);
            })*/

          })
        }
      })
      this.openInit = true;
    },
    // 复选
    reselectFun (dataArr, IDArr) {
      if (dataArr && IDArr) {
        this.$nextTick(function () {
          dataArr.forEach(row => {
            if (IDArr.includes(row.materialOid)) {
              this.$refs.multipleTable.toggleRowSelection(row, true);
            }
          });
        });
      }
    },

    batchChoose () {
      let materialOids = "";
      if (this.multipleSelection && this.multipleSelection.length > 0) {
        materialOids = this.multipleSelection.map(ite => ite.materialOid).join(",");
      }
      this.form.materialOids = materialOids;
      saveOrUpdateMaterOptRel(this.form).then(response => {
        if (response.data) {
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
