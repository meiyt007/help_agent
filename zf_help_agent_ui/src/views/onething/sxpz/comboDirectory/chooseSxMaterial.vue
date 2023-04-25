/**
* @Author: wangxl
*/
<template>
  <div>
    <!-- <el-row :gutter="20">
      <el-col :span="24" :xs="24"> -->
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="108px">
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-refresh" size="mini" @click="batchChoose" class="btn-reset">批量选择
            </el-button>
          </el-form-item>
        </el-form>
        <el-table ref="multipleTable" :data="sxMaterialList" tooltip-effect="dark" style="width: 100%"
          @selection-change="handleSelectionChange" border>
          <el-table-column type="selection" align="center" width="60">
          </el-table-column>
          <el-table-column prop="materialName" label="材料名称" align="center" width="250" :show-overflow-tooltip="true">
          </el-table-column>
          <el-table-column prop="materialType" label="材料类型" :formatter="getMaterialType" align="center" width="150"
            :show-overflow-tooltip="true">
          </el-table-column>
          <el-table-column prop="materialFormat" :formatter="getMaterialFormat" align="center" label="材料形式"
            show-overflow-tooltip width="150">
          </el-table-column>
          <el-table-column prop="paperNumber" align="center" label="份数" width="150" :show-overflow-tooltip="true" />
          <el-table-column prop="mustFlag" :formatter="getMustFlag" align="center" label="必要性" show-overflow-tooltip
            width="150" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-penzhi" @click="chooseRe(scope.row)"
                v-hasPermi="['combo:directory:service']">选择</el-button>
            </template>
          </el-table-column>
        </el-table>
        <!--<pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getSxServiceList"
        />-->
      <!-- </el-col>
    </el-row> -->
  </div>
</template>

<script>
  import {
    saveComboMaterial,
    queryChooseSxMaterList
  } from "@/api/onething/sxpz/chooseSxMaterial";
  export default {
    components: {},
    name: "ChooseSxMaterialList",
    props: ["direOid"],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        attatotal: 0,
        // 应用表格数据
        sxMaterialList: [],
        //查询目录名称参数
        sxServiceName: '',
        comboDirectoryOid: "",
        // 弹出层标题
        title: "",
        multipleSelection: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
        },
        defaultProps: {
          children: "children",
          label: "label"
        }
      };
    },

    created() {
      this.getSxMaterialList();
      this.mounted();
    },
    methods: {
      //获取父页面的值
      mounted() {
        this.comboDirectoryOid = this.direOid;
      },

      /** 查询目录列表 */
      getSxMaterialList() {
        this.loading = true;
        queryChooseSxMaterList(this.queryParams, this.direOid).then(response => {
          this.sxMaterialList = response.data;
          // this.total = response.data.total;
          this.loading = false;
        });
      },

      // 取消按钮
      cancel() {
        this.reset();
      },
      // 表单重置
      reset() {
        this.resetForm("form")
      },
      //保存选中的结果
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      /**  批量选择**/
      batchChoose() {
        let materialOids = "";
        //获取选中结果
        this.multipleSelection.forEach(ser => {
          materialOids += ser.materialOid + ",";
        });
        if (materialOids == "") {
          this.msgWarning("请选择事项材料！");
          return false;
        }
        if (materialOids) {
          materialOids = materialOids.substring(0, materialOids.length - 1);
        }
        saveComboMaterial(materialOids, this.direOid).then(response => {
          if (response.code === 200) {
            this.msgSuccess("选择成功");
            this.$emit('sx-dialog-close');
          }
        });
      },
      /**  选择事项材料**/
      chooseRe(row) {
        const materialOid = row.materialOid;
        saveComboMaterial(materialOid, this.direOid).then(response => {
          if (response.code === 200) {
            this.msgSuccess(" 选择成功");
            this.$emit('sx-dialog-close');
          }
        });
      },
      getMaterialFormat(val) {
        if (val.materialFormat == 1) {
          return '纸质';
        } else if (val.materialFormat == 2) {
          return '电子版'
        } else if (val.materialFormat == 3) {
          return '证照';
        }else if (val.materialFormat == 4) {
          return '容缺补正';
        }else if (val.materialFormat == 7) {
          return '告知承诺';
        }else  {
          return '';
        }
      },
      getMaterialType(val) {
        if (val.materialType == 0) {
          return '原件';
        } else if (val.materialType == 1) {
          return '复印件';
        } else if (val.materialType == 2) {
          return '原件或复印件'
        } else {
          return '';
        }
      },
      getMustFlag(val) {
        if (val.mustFlag == 0) {
          return '非必要';
        } else if (val.mustFlag == 1) {
          return '必要';
        } else if (val.mustFlag == 2) {
          return '容缺后补';
        }
      }
    }
  }
</script>
