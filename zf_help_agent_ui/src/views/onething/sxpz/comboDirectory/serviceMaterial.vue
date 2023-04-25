/**
* @Author: wangxl
*/
<template>
  <div>
    <div class="el-table__header-wrapper dialog-table" style="padding: 0;">
      <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <table cellspacing="0" cellpadding="0" border="0">
          <el-input v-show="false" v-model="form.id" />
          <el-input v-show="false" v-model="form.comboDirectoryOid" />
          <el-input v-show="false" v-model="form.materials" />
          <el-input v-show="false" v-model="form.status" />
          <el-input v-show="false" v-model="form.delFlag" />
          <el-input v-show="false" v-model="form.createUser" />
          <colgroup>
            <col width="15%" />
            <col width="35%" />
            <col width="15%" />
            <col width="35%" />
          </colgroup>
          <tr>
            <td>
              <b><i class="require">*</i>材料标准名称：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="materialName">
                <el-input v-model.trim="form.materialName" placeholder="请输入材料标准名称" maxlength="100" show-word-limit />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>材料类型：</b>
            </td>
            <td>
              <el-form-item>
                <el-radio-group v-model="form.materialType">
                  <el-radio :label="0">原件</el-radio>
                  <el-radio :label="1">复印件</el-radio>
                  <el-radio :label="2">原件和复印件</el-radio>
                  <el-radio :label="3">原件或复印件</el-radio>
                </el-radio-group>
              </el-form-item>
            </td>
            <td>
              <b>材料形式：</b>
            </td>
            <td>
              <el-form-item>
                <el-radio-group v-model="form.materialFormat">
                  <el-radio :label="1">纸质</el-radio>
                  <el-radio :label="2">电子版</el-radio>
                  <el-radio :label="5">纸质、电子版</el-radio>
                </el-radio-group>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>来源渠道：</b>
            </td>
            <td>
              <el-form-item prop="materialSource">
                <el-radio-group v-model="form.materialSource">
                  <el-radio :label="0">申请人自备</el-radio>
                  <el-radio :label="1">政府部门核发</el-radio>
                  <el-radio :label="2">其他</el-radio>
                </el-radio-group>
              </el-form-item>
            </td>
            <td>
              <b>出具部门：</b>
            </td>
            <td>
              <el-form-item prop="otherMaterialSource">
                <el-input v-model.trim="form.otherMaterialSource" placeholder="请输入出具部门" maxlength="100"
                  show-word-limit />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>纸质材料份数：</b>
            </td>
            <td>
              <el-form-item prop="paperNumber">
                <el-input-number v-model="form.paperNumber" :min="1" :max="9999" />
              </el-form-item>
            </td>
            <td>
              <b>材料必要性：</b>
            </td>
            <td>
              <el-form-item>
                <el-radio-group v-model="form.mustFlag">
                  <el-radio :label="1">非必要</el-radio>
                  <el-radio :label="0">必要</el-radio>
                  <el-radio :label="2">容缺后补</el-radio>
                  <el-radio :label="3">信息免交、信用后补</el-radio>
                </el-radio-group>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
    </div>
    <!-- 已选择的材料-->
    <div style="width: 100%">
        <div class="zf-zc-table--title">已选择材料</div>

      <el-table :data="allCheckMaterialList" border>
        <el-table-column prop="materialName" label="材料名称" align="center" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="materialType" :formatter="getMaterialType" label="材料类型" align="center" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="materialFormat" :formatter="getMaterialFormat" label="材料形式" align="center" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="paperNumber" label="份数" align="center" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="mustFlag" :formatter="getMustFlag" label="必要性" align="center" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="materialSource" :formatter="getMaterialSource" label="来源渠道" align="center"
                         show-overflow-tooltip width="150">
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="iconfont zfsoft-shanchu"
              @click="handleMaterialDelete(scope.row)"
              v-hasPermi="['combo:directory:delete']"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- 未选择的材料-->
    <div class="el-table__header-wrapper " :model="sxServiceList" v-for="(info,index) in sxServiceList" :key='index' style="width: 100%">

        <div class="zf-zc-table--title">{{info.serviceName}}</div>

      <el-table ref="multipleTable" :data="info.materials" tooltip-effect="dark" class="bjtj"
        @selection-change="onTableSelect" border>
        <el-table-column type="selection" align="center" width="60">
        </el-table-column>
        <el-table-column prop="materialName" label="材料名称" align="center" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="materialType" :formatter="getMaterialType" label="材料类型" align="center" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="materialFormat" :formatter="getMaterialFormat" label="材料形式" align="center" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="paperNumber" label="份数" align="center" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="mustFlag" :formatter="getMustFlag" label="必要性" align="center" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="materialSource" :formatter="getMaterialSource" label="来源渠道" align="center"
          show-overflow-tooltip width="150">
        </el-table-column>
        <!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width" >
           <el-checkbox v-model="sb">使用样表</el-checkbox>
           <el-checkbox v-model="kb" >使用空表</el-checkbox>
         </el-table-column>-->
      </el-table>
    </div>
    <div slot="footer" class="mt30 zf-text-center">
      <el-button type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </div>
</template>

<script>
  import {
    querySxServiceList,
    saveMaterial,
    getSxMatersByDireMaterOid,delMaterialRel
  } from "@/api/onething/sxpz/serviceMaterial";
  export default {
    components: {},
    name: "ServiceMaterial",
    //定义获取父类传过来值的格式
    props: ["comboDireOid", "pubMaterialOid", "direMaterials"],
    data() {
      return {
        // 遮罩层
        loading: true,
        comboDirectoryOid: "",
        multipleSelection: [],
        sb: '',
        kb: '',
        // 表单参数
        form: {
          materialType: 0,
          materialFormat: 1,
          materialSource: 0,
          mustFlag: 0,
          paperNumber: 1
        },
        sxServiceList: [],
        rules: {
          materialName: [{
            required: true,
            message: "请输入材料标准名称",
            trigger: "blur"
          }]
        },
        allCheckMaterialList: []
      };
    },
    watch: {},
    created() {
      this.multipleSelection=[];
      if (this.direMaterials) {
        this.allCheckMaterialList=this.direMaterials;
        this.direMaterials.forEach(ma => {
          this.multipleSelection.push(ma.materialOid);
        });
      }
      //公共材料详细信息
      this.initDirectoryMater();
      //事项列表
      this.getSxServiceList();
    },
    methods: {
      // 取消按钮
      cancel() {
        this.reset();
        this.$emit('dialog-close');
      },
      // 表单重置
      reset() {
        this.resetForm("form")
      },
      //获取公共材料详细
      initDirectoryMater() {
        this.reset();
        if (this.pubMaterialOid) {
          getSxMatersByDireMaterOid(this.pubMaterialOid).then(response => {
            if (response.data.comboDirectoryMaterial != undefined) {
              this.form = response.data.comboDirectoryMaterial;
            }
            this.dialogTitle = this.pubMaterialOid ? "修改" : "新增";
          });
        }
      },
      getSxServiceList() {
        this.loading = true;
        querySxServiceList(this.comboDireOid).then(response => {
          this.sxServiceList = response.data;
          this.loading = false;

        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.form.materials = this.multipleSelection.toString();
        this.form.comboDirectoryOid = this.comboDireOid;
        this.$refs["form"].validate(valid => {
          if (valid) {
            saveMaterial(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("保存成功");
                this.reset();
                this.$emit('dialog-close');
              }
            });
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
          return '必要';
        } else if (val.mustFlag == 1) {
          return '非必要';
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
      },
      //保存选中的结果
      handleSelectionChange(val) {
        val.forEach(v => {
          if (v.materialOid) {
            if (this.multipleSelection.indexOf(v.materialOid) == -1) {
              this.multipleSelection.push(v.materialOid);
            }
          }
        });
      },
      onTableSelect(rows) {
        rows.forEach((item)=>{
          if(this.multipleSelection.indexOf(item.materialOid)!=-1){//存在
            this.multipleSelection.splice(this.multipleSelection.indexOf(item.materialOid),1);
          }else{//不存在
            this.multipleSelection.push(item.materialOid);
          }
        })
      },
      handleMaterialDelete(rows){
        //删除选择
        delMaterialRel(this.pubMaterialOid,rows.materialOid).then(response => {
          if (response.code === 200) {
            this.msgSuccess("删除成功!");
            this.$emit('dialog-close');//先关闭防止出现多个页面
            this.$emit('delete-close',this.form);
          }else{
            this.msgSuccess("删除失败!");
          }
        });
      },
    }
  };
</script>
