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
              >新增选项材料关系</el-button>
            </el-col>
          </el-row>
          <el-table v-loading="loading" :data="comboOptionRelList" border>
            <el-table-column label="序号" width="70" type="index" align="center">
              <template slot-scope="scope">
                <span>{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column label="选项值" align="center" width="250" prop="titleValNames" :show-overflow-tooltip="true"/>
            <el-table-column label="材料" align="center"  prop="materialName" :show-overflow-tooltip="true"/>
            <el-table-column label="操作" align="center" width="200"  class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" @click="handleOptionRel(scope.row)" v-hasPermi="['sys:reg:view']" >选项材料配置</el-button>
                <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDelete(scope.row)" v-hasPermi="['sys:reg:view']" >删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form>
      <!-- </el-col>
    </el-row> -->
    <!--目录公共材料开始-->
    <el-dialog v-dialog-drag :visible.sync="service.show" class="dialog-header"  title="选项关联材料" v-for="service in materialDialogOptions" :key='service.comboDirectoryOid'  width="1100px" height='700px' scrollbar  append-to-body>
      
        <combo-option-material :relOid="service.relOid" :comboDireOid="service.comboDirectoryOid" @closeMater-Option="closeMaterOption"></combo-option-material>
        <div slot="footer" class="zf-text-center">
        <el-button @click="closeMaterOption">关 闭</el-button>
      </div>
  
    </el-dialog>
  </div>

    <!--查看目录公共材料 结束-->
</template>

<script>
  import {queryDirectoryMaterialList,getSxMatersByDireMaterOid,querySxServiceMaterList,setMaterialStatus} from "@/api/onething/sxpz/comboDirectoryMaterial";
  import {chooseOptionMaterial,del,queryOptionMaterial} from "@/api/onething/sxpz/optionMaterial";
  import {  queryComOptiontitle } from "@/api/onething/sxpz/comboDireOptandStu";
  import serviceMaterial from "@/views/onething/sxpz/comboDirectory/serviceMaterial";
  import viewDirectoryMaterial from "@/views/onething/sxpz/comboDirectory/viewDirectoryMaterial";
  import comboOptionMaterial from "@/views/onething/sxpz/comboDireOptandStu/comboOptionMaterial";
  export default {
    components: {serviceMaterial,viewDirectoryMaterial,comboOptionMaterial},
    name: "ComboDirectoryMaterial",
    //定义获取父类传过来值的格式
    props:["comboDireOid"],
    data() {
      return {
        // 遮罩层
        loading: true,
        comboDirectoryOid:"",
        directoryMaterialList: [],
        direMaterials:[],
        comboOptionRelList:[],
        materialOid:"",
        arrayVals:[],
        materialDialogOptions:[],
        //选项值表格数据
        radio1:[],
        comboOptionValList:[],
        valOidArrayM:"",
        valOidArrayMs:[],
        multipleSelectionM:[],
        serviceMaterials:[],
        // 新增/修改显示弹出层
        // openInit: false,
        // 表单参数
        form: {},
        sxServiceList:[],
        rules: {
        }
      };
    },
    created() {
      this.comboDirectoryOid = this.comboDireOid;

      //公共材料List
      //this.getDirectoryMaterialList();
      this.handleOptionMaterialList();
      //未被合并的事项材料List
     // this.getSxServiceList();
    },
    methods: {
      // 表单重置
      reset() {
        this.resetForm("form")
      },
      handleOptionMaterialList() {
        let _that = this;
        queryOptionMaterial(this.comboDirectoryOid).then(response => {
          if(response.data != undefined){
            this.comboOptionRelList = response.data;
            this.loading = false;
          }

          //_that.title =  "选项配置详细页面";
        });
      },
      handleOptionRel(row){
        this.materialDialogOptions = [];
        let item = {show:true,relOid:row.relOid ,comboDirectoryOid:this.comboDirectoryOid};
        this.materialDialogOptions.push(item);
      },
      closeMaterOption() {
        this.materialDialogOptions.pop();
        let item = {
          show:false
        };
        this.handleOptionMaterialList();

      },
      //多组单选框数据处理方法
      chooseValOid(valOid){
        if(this.valOidArrayM == "" ){
          this.valOidArrayM += valOid+";";
          this.valOidArrayMs.push(valOid);
        }else if(this.valOidArrayM.indexOf(valOid)== -1){
          let titOid = "";
          this.$nextTick(() => {
            this.arrayVals.forEach(a => {

              if(a.key==valOid){

                titOid = a.titleOid;

              }
            });
            this.arrayVals.forEach(a => {

                this.valOidArrayMs.forEach(b => {
                  let titOid1 = "";

                    titOid1 = a.titleOid;

                  if(titOid!="" && titOid1!="" && titOid==titOid1 && a.key==valOid){
                    let bl = b+";"
                    if(this.valOidArrayM.indexOf(valOid)==-1){
                    this.valOidArrayM = this.valOidArrayM.replace(bl,"");
                    this.valOidArrayMs.splice(this.valOidArrayMs.indexOf(bl),1);

                      this.valOidArrayM += valOid+";";
                      this.valOidArrayMs.push(valOid);

                      titOid1 = "";
                    }
                  }else{
                    if(this.valOidArrayM.indexOf(valOid)==-1 && a.key==valOid){

                      this.valOidArrayM += valOid+";";
                      this.valOidArrayMs.push(valOid);

                      titOid1 = "";
                    }
                  }

                });


            });
          })
        }

      },
      getDirectoryMaterialList(){
        this.loading = true;
        queryDirectoryMaterialList(this.comboDireOid).then(response => {
          this.directoryMaterialList = response.data;
          this.loading = false;
        });
      },
      getSxServiceList(){
        this.loading = true;
        querySxServiceMaterList(this.comboDireOid).then(response => {
          this.sxServiceList = response.data;


          this.loading = false;
        });
      },
      /** 查看按钮操作 */
      handleView(row) {
        this.viewDirectoryMaterial = true;
        this.materialOid = row.materialOid;
      },
      /** 新增和修改按钮操作 */
      handleInit(row) {
        this.reset();
        let oid = row.materialOid;

        if(oid){
          this.pubMaterialOid = oid;
          getSxMatersByDireMaterOid(oid).then(response => {
            this.direMaterials = response.data;
            this.serviceMaterialView = true;
            this.title = oid ? "修改" : "新增";
            let item = {
              show:true,
              comboDirectoryOid:this.comboDirectoryOid,
              direMaterials:this.direMaterials,
              pubMaterialOid:this.pubMaterialOid
            };
            this.dialogOptions.push(item);
          });
        }else{
          this.serviceMaterialView = true;
          this.title = oid ? "修改" : "新增";
          let item = {
            show:true,
            comboDirectoryOid:this.comboDirectoryOid,
            pubMaterialOid:''
          };
          this.dialogOptions.push(item);
        }
      },
      /** 删除按钮操作 */

      closeServiceMaterial(){
        this.dialogOptions.pop();
        this.serviceMaterialView = false;
        //公共材料List
        this.getDirectoryMaterialList();
        //事项材料List
        this.getSxServiceList();
      },
      closeDireMaterial(){
        this.viewDirectoryMaterial = false;
      },
      getMaterialFormat(val){
        if(val.materialFormat == 1){
          return '纸质';
        }else if(val.materialFormat == 2){
          return '电子版'
        }else{
          return '';
        }
      },
      getMaterialType(val){
        if(val.materialType == 0){
          return '原件';
        }else if(val.materialType == 1){
          return '复印件';
        }else if(val.materialType == 2){
          return '原件或复印件'
        }else{
          return '';
        }
      },
      getMustFlag(val){
        if(val.mustFlag == 0){
          return '非必要';
        }else if(val.mustFlag == 1){
          return '必要';
        }
      },
      getStatus(val){
        if(val.status == 0){
          return '未确认';
        }else if(val.status == 1){
          return '已确认';
        }
      },
      //复选框数据处理
      onTableSelect(rows, row) {

        let selected = rows.length && rows.indexOf(row) !== -1;
        if(selected){
          this.multipleSelectionM.push(row.materialOid);
        }else{
          this.multipleSelectionM.splice(this.multipleSelectionM.indexOf(row.materialOid),1);
        }

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
          this.handleOptionMaterialList();
        }).catch(function() {});
      },
      batchChoose(){
        let materialOids = "";
        let  _that=this;
        //获取选中结果
        materialOids=this.multipleSelectionM.toString();

        chooseOptionMaterial(this.comboDirectoryOid,materialOids,this.valOidArrayM,this.relOid).then(response => {
          if (response.code === 200) {
            this.msgSuccess("暂存成功");
            this.$emit('dialog-close');
            this.multipleSelection = [];
            /*_that.openOptionRel = false;
            queryComOptionRel("",_that.comboDirectoryOid).then(response => {
              if(response.data != undefined){
                this.comboOptionRelList = response.data;
              }

              _that.openOptionRelList = true;
              //_that.title =  "选项配置详细页面";
            });*/
            this.reset();
            this.$emit('dialog-close');
          }
        });

      },
      //确认目录公共材料
      setMaterialStatus(){
        let direOid = this.comboDireOid;
        this.$confirm('确认目录公共材料吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return setMaterialStatus(direOid);
        }).then(() => {
          this.msgSuccess("确认成功");
          //公共材料List
          this.getDirectoryMaterialList();
        }).catch(function() {});
      }
    }
  };
</script>
