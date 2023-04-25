/**
* @Author: wangxl
*/
<template>
  <div >
    <div class="el-table__header-wrapper dialog-table">
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b>材料标准名称：</b>
            </td>
            <td colspan="3">
              {{form.materialName}}
            </td>
          </tr>
          <tr>
            <td>
              <b>材料类型：</b>
            </td>
            <td>
              <span v-if="form.materialType == 0">
                原件
              </span>
              <span v-else-if="form.materialType == 1">
                复印件
              </span>
              <span v-else-if="form.materialType == 2">
                原件和复印件
              </span>
              <span v-else-if="form.materialType == 3">
                原件或复印件
              </span>
            </td>
            <td>
              <b>材料形式：</b>
            </td>
            <td>
              <span v-if="form.materialFormat == 1">
                纸质
              </span>
              <span v-else-if="form.materialFormat == 2">
                电子版
              </span>
              <span v-else-if="form.materialFormat == 5">
                纸质、电子版
              </span>
            </td>
          </tr>
          <tr>
            <td>
              <b>来源渠道：</b>
            </td>
            <td>
              <span v-if="form.materialSource == 0">
                申请人自备
              </span>
              <span v-else-if="form.materialSource == 1">
                政府部门核发
              </span>
              <span v-else-if="form.materialSource == 2">
                其他
              </span>
            </td>
            <td>
              <b>出具部门：</b>
            </td>
            <td>
              {{form.otherMaterialSource}}
            </td>
          </tr>
          <tr>
            <td>
              <b>纸质材料份数：</b>
            </td>
            <td>
              {{form.paperNumber}}
            </td>
            <td>
              <b>材料必要性：</b>
            </td>
            <td>
              <span v-if="form.mustFlag == 1">
                非必要
              </span>
              <span v-else-if="form.mustFlag == 0">
                必要
              </span>
              <span v-else-if="form.mustFlag == 2">
                容缺后补
              </span>
              <span v-else-if="form.mustFlag == 3">
                信息免交、信用后补
              </span>

            </td>
          </tr>
        </table>
    </div>
    <div :model="sxServiceList" v-for="(info,i) in sxServiceList" :key="i" style="width: 100%">
      <div class="zf-zc-table--title">{{info.serviceName}}</div>
      <el-table ref="multipleTable" :data="info.materials"  border>
        <el-table-column prop="materialName" label="材料名称" align="center" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="materialType" :formatter="getMaterialType" label="材料类型" align="center" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="materialFormat" :formatter="getMaterialFormat" label="材料形式" align="center"
          :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="paperNumber" label="份数" align="center" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="mustFlag" label="必要性" align="center" :formatter="getMustFlag" show-overflow-tooltip >
        </el-table-column>
        <el-table-column prop="materialSource" :formatter="getMaterialSource" label="来源渠道" show-overflow-tooltip
          align="center">
        </el-table-column>
        <!--<el-table-column label="操作" align="center" class-name="small-padding fixed-width" >

                </el-table-column>-->
      </el-table>
    </div>
    <!-- <div slot="footer" class="dialog-footer mt30">
      <el-button @click="cancel">取 消</el-button>
    </div> -->
  </div>
</template>

<script>
  import {
    querySxServiceList
  } from "@/api/onething/sxpz/viewDirectoryMaterial";
  import {
    getSxMatersByDireMaterOid
  } from "@/api/onething/sxpz/serviceMaterial";
  export default {
    components: {},
    name: "ViewDirectoryMaterial",
    //定义获取父类传过来值的格式
    props: ["materialOid"],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 表单参数
        form: {},
        sxServiceList: [],
        rules: {}
      };
    },
    computed: {
      reversedMaterialFormat: function() {
        if (this.form.materialFormat) {
          if (this.form.materialFormat == "1") {
            return '纸质';
          } else if (this.form.materialFormat == "2") {
            return '电子版';
          }
        }
        return ''
      }
    },
    created() {
      //事项列表
      this.getSxServiceList();
      //公共材料详细信息
      this.initDirectoryMater();
    },
    methods: {
      // 取消按钮
      cancel() {
        // this.reset();
        this.$emit('view-dialog-close');
      },
      //获取公共材料详细
      initDirectoryMater() {
        getSxMatersByDireMaterOid(this.materialOid).then(response => {
          if (response.data.comboDirectoryMaterial != undefined) {
            this.form = response.data.comboDirectoryMaterial;
          }
        });
      },
      getSxServiceList() {
        this.loading = true;
        querySxServiceList(this.materialOid).then(response => {
          this.sxServiceList = response.data;
          this.loading = false;
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
      },
      getMaterialSource(val) {
        if (val.materialSource == 0) {
          return '申请人自备';
        } else if (val.materialSource == 1) {
          return '政府部门核发';
        } else if (val.materialSource == 2) {
          return '其他';
        }
      }
    }
  };
</script>
<style scoped>
  .dialog-table{
    padding: 0 !important;
  }
</style>
