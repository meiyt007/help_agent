/**
* @Author: liangxm
*/
<template>
  <div>
    <!-- <el-row :gutter="20">
      <el-col :span="24" :xs="24"> -->
        <el-form :model="form" ref="form" label-width="108px" size="mini">
          <el-row :gutter="10" class="mb8 fl">
            <el-col :span="1.5">
              <el-button
                type="default"
                icon="el-icon-plus"
                size="mini"
                @click="handleOptionRel(form)"
                v-hasPermi="['combo:directory:init']"
              >新增选项证照关系</el-button>
            </el-col>
          </el-row>
          <el-table v-loading="loading" :data="comboOptionRelList" border>
            <el-table-column label="序号" width="70" type="index" align="center">
              <template slot-scope="scope">
                <span>{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column label="选项值" align="center" width="250" prop="titleValNames" :show-overflow-tooltip="true"/>
            <el-table-column label="证照名称" align="center"  prop="resultName" :show-overflow-tooltip="true"/>
            <el-table-column label="操作" align="center" width="200"  class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" @click="handleOptionRel(scope.row)" v-hasPermi="['sys:reg:view']" >选项证照配置</el-button>
                <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDelete(scope.row)" v-hasPermi="['sys:reg:view']" >删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form>
      <!-- </el-col>
    </el-row> -->
    <!--目录公共材料开始-->
    <el-dialog v-dialog-drag :visible.sync="service.show"  title="选项关联证照" v-for="service in resultDialogOptions" :key='service.comboDirectoryOid'  width="1100px" height='700px' scrollbar  append-to-body>

        <combo-option-result :relOid="service.relOid" :comboDireOid="service.comboDirectoryOid" @closeMater-Option="closeMaterOption"></combo-option-result>
        <div slot="footer" class="zf-text-center">
        <el-button @click="closeMaterOption">关 闭</el-button>
      </div>

    </el-dialog>
  </div>

    <!--查看目录公共材料 结束-->
</template>

<script>
  import {queryDirectoryMaterialList,getSxMatersByDireMaterOid,querySxServiceMaterList} from "@/api/onething/sxpz/comboDirectoryMaterial";
  import {chooseOptionResult,del,queryOptionResult} from "@/api/onething/sxpz/optionResult";
  import {  queryComOptiontitle } from "@/api/onething/sxpz/comboDireOptandStu";
  import serviceMaterial from "@/views/onething/sxpz/comboDirectory/serviceMaterial";
  import viewDirectoryMaterial from "@/views/onething/sxpz/comboDirectory/viewDirectoryMaterial";
  import comboOptionResult from "@/views/onething/sxpz/comboDireOptandStu/comboOptionResult";
  export default {
    components: {serviceMaterial,viewDirectoryMaterial,comboOptionResult},
    name: "ComboDirectoryMaterial",
    //定义获取父类传过来值的格式
    props:["comboDireOid"],
    data() {
      return {
        // 遮罩层A
        loading: true,
        comboDirectoryOid:"",
        directoryMaterialList: [],
        direResults:[],
        comboOptionRelList:[],
        resultOid:"",
        resultDialogOptions:[],
        //选项值表格数据
        // 新增/修改显示弹出层
        // openInit: false,
        // 表单参数
        form: {},
        rules: {
        }
      };
    },
    created() {
      this.comboDirectoryOid = this.comboDireOid;

      this.handleOptionResultList();
    },
    methods: {
      // 表单重置
      reset() {
        this.resetForm("form")
      },
      handleOptionResultList() {
        let _that = this;
        queryOptionResult(this.comboDirectoryOid).then(response => {
          if(response.data != undefined){
            this.comboOptionRelList = response.data;
            this.loading = false;
          }

          //_that.title =  "选项配置详细页面";
        });
      },
      handleOptionRel(row){
        let item = {show:true,relOid:row.relOid ,comboDirectoryOid:this.comboDirectoryOid};
        this.resultDialogOptions.push(item);
      },
      closeMaterOption() {
        this.resultDialogOptions.pop();
        let item = {
          show:false
        };
        this.handleOptionResultList();

      },
      /** 查看按钮操作 */
      handleView(row) {
        this.viewDirectoryMaterial = true;
        this.resultOid = row.resultOid;
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const oid = row.id;
        this.$confirm('是否确认删除?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return del(oid);
        }).then(() => {
          this.msgSuccess("删除成功");
          this.handleOptionResultList();
        }).catch(function() {});
      },


    }
  };
</script>


