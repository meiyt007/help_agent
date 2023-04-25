/**
* @Author: liangxm
*/
<template>
  <div class="clearfix" style='padding:10px'>
    <div slot="footer" class="dialog-footer">
      <el-button type="default" icon="el-icon-plus" size="mini" @click="batchChoose"
        v-hasPermi="['combo:directory:init']">保存</el-button>
    </div>
    <el-form ref="form" :model="form" class="xxgl">
      <el-row :gutter="20">
        <el-col :span="12">
          <div >
            <div class="zf-zc-table--title">选项值列表</div>
            <el-table v-loading="loading" :data="comboOptionValList" border>
              <el-table-column label="序号" width="55" type="index" align="center">
                <template slot-scope="scope">
                  <span>{{ scope.$index + 1 }}</span>
                </template>
              </el-table-column>
              <el-table-column label="选项标题" align="center" width="100" prop="name" :show-overflow-tooltip="true" />
              <!-- <el-table-column label="选项值名称" align="center" width="200" prop="name"/>-->
              <el-table-column label="选项值" align="center">
                <template slot-scope="scope">
                  <template v-if="scope.row.moreStatus=='0'">
                      <el-radio-group  v-model="situValCheck[scope.$index]">
                         <el-radio v-for="(comboObj,index) in scope.row.arrayVal" :key='index' @change="changeRadio(scope.row.titleOid,comboObj.key)" :label="comboObj.key">{{comboObj.name}}
                         </el-radio>
                      </el-radio-group>
                  </template>
                  <template v-if="scope.row.moreStatus=='1'">
                    <el-checkbox-group v-model="situValCheckbox">
                      <el-checkbox v-for="(comboObj,idx) in scope.row.arrayVal" :key='idx' :label="comboObj.key"
                        @change="changeCheckBox(scope.row.titleOid,comboObj.key)">{{comboObj.name}}</el-checkbox>
                    </el-checkbox-group>
                  </template>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-col>
        <el-col :span="12">
          <div >
            <div class="zf-zc-table--title">公共材料</div>

            <el-table v-loading="loading" :data="directoryMaterialList" ref="multipleTable"
              @selection-change="onTableSelect($event,directoryMaterialList.length)" border>
              <el-table-column width="70" align="center" type="selection">
              </el-table-column>
              <el-table-column prop="materialName" label="材料名称" align="center"
                :show-overflow-tooltip="true" />
            </el-table>
          </div>
          <div class="dialog-table2" :model="sxServiceList" v-for="(info,i) in sxServiceList" :key='i'>
            <div class="zf-zc-table--title">{{info.serviceName}}</div>

            <el-table v-loading="loading" :data="info.materials"  tooltip-effect="dark" style="width: 100%"
              ref="multipleTablea" @selection-change="onTableSelect1($event,i,info.materials.length)" border>
              <el-table-column width="70" align="center" type="selection"/>
              <el-table-column prop="materialName" label="材料名称" align="center"
                :show-overflow-tooltip="true" />
            </el-table>
          </div>
        </el-col>
      </el-row>
    </el-form>
  </div>

  <!--查看目录公共材料 结束-->
</template>

<script>
  import {
    queryDirectoryMaterialList,
    getSxMatersByDireMaterOid,
    querySxServiceMaterList,
    setMaterialStatus,
    querySxServiceAddList
  } from "@/api/onething/sxpz/comboDirectoryMaterial";
  import {
    chooseOptionMaterial
  } from "@/api/onething/sxpz/optionMaterial";
  import {
    queryComOptiontitleByRel,
    queryComOptiontitleStu
  } from "@/api/onething/sxpz/comboDireOptandStu";
  import serviceMaterial from "@/views/onething/sxpz/comboDirectory/serviceMaterial";
  import viewDirectoryMaterial from "@/views/onething/sxpz/comboDirectory/viewDirectoryMaterial";
  export default {
    components: {
      serviceMaterial,
      viewDirectoryMaterial
    },
    name: "ComboDirectoryMaterial",
    //定义获取父类传过来值的格式
    props: ["relOid", "comboDireOid"],
    data() {
      return {
        // 遮罩层
        loading: true,
        comboDirectoryOid: "",
        directoryMaterialList: [],
        direMaterials: [],
        serviceMaterialView: false,
        dialogOptions: [],
        viewDirectoryMaterial: false,
        pubMaterialOid: "",
        materialOid: "",
        materialOidArry: "",
        materialOidArray: [],
        arrayVals: [],
        reloid: "",
        //选项值表格数据
        radio1: [],
        comboOptionValList: [],
        comboOptionValListMust: [],
        situValCheck: [],
        valOidArrayM: "",
        valOidArrayMs: [],
        titleOidArray: "",
        multipleSelectionM: [],
        multipleSelection1: [],
        serviceMaterials: [],
        situValCheckbox:[],
        // 新增/修改显示弹出层
        // openInit: false,
        // 表单参数
        form: {},
        sxServiceList: [],
        rules: {},
        checked:false
      };
    },
    created() {
      this.comboDirectoryOid = this.comboDireOid;
      this.reloid = this.relOid;

      //公共材料List

      this.handleOptionRel();
      //未被合并的事项材料List
      //this.getSxServiceList();
    },
    methods: {
      // 表单重置
      reset() {
        this.resetForm("form")
      },
      changeRadio(titleOid, chooseVal) {
        for (let oneTitle of this.comboOptionValListMust) {
          if (oneTitle.titleOid == titleOid) {
            oneTitle.valuePut = '1';
          }
        }
      },
      changeCheckBox(titleOid, chooseVal) {
        for (let oneTitle of this.comboOptionValListMust) {
          if (oneTitle.titleOid == titleOid) {
            if (oneTitle.valuePut.indexOf(titleOid + chooseVal) != -1) {
              oneTitle.valuePut = oneTitle.valuePut.replace("," + titleOid + chooseVal, '');
              oneTitle.valuePut = oneTitle.valuePut.replace(titleOid + chooseVal, '');
            } else {
              oneTitle.valuePut = oneTitle.valuePut + "," + titleOid + chooseVal;
            }
          }
        }
      },
      handleOptionRel() {
        let _that = this;
        let materialOid = "";
        //this.valOidArrayM = "";
        this.multipleSelectionM = [];
        this.multipleSelection1=[];
        _that.situValCheck = [];

        queryComOptiontitleByRel(_that.comboDirectoryOid, this.reloid).then(response => {
          _that.comboOptionValListMust = [];
          if (response.data != undefined) {
            this.comboOptionValList = response.data;
            this.$nextTick(() => {
              this.comboOptionValList.forEach((d,index) => {
                if (materialOid == "" && d.materialOids != "" && d.materialOids != null) {
                  materialOid = d.materialOids;
                  this.materialOidArry = materialOid;
                  this.materialOidArray = d.materialOids.split(",")
                }
                this.getDirectoryMaterialList();
                this.getSxServiceList();
                this.arrayVals = d.arrayVal;
                this.arrayVals.forEach(a => {
                  if (a.isStatus == '1') {
                    if(d.moreStatus==1){
                      _that.situValCheckbox.push(a.key)
                    }else{
                      _that.$set(_that.situValCheck, index, a.key);
                    }
                  }
                  let titleValues;
                  titleValues = {};
                  titleValues.titleName = d.name;
                  titleValues.bigTitle = d.titleOid;
                  titleValues.fillFlag = d.fillFlag;
                  titleValues.titleOid = a.titleOid;
                  if (a.isStatus == '1') {
                    titleValues.valuePut = a.titleOid + a.key;
                  } else {
                    titleValues.valuePut = "0";
                  }
                  if (_that.comboOptionValListMust.length > 0) {
                    let hasTitle = {};
                    let addMust = true;
                    for (let mustHas of _that.comboOptionValListMust) {
                      hasTitle = mustHas;
                      if (titleValues.bigTitle == hasTitle.bigTitle) {
                        addMust = false;
                      } else {
                        addMust = true;
                      }
                    }
                    if (addMust == false) {
                      if (titleValues.valuePut == "0") {} else {
                        if (hasTitle.valuePut.indexOf(titleValues.valuePut) > -1) {} else {
                          hasTitle.valuePut = hasTitle.valuePut + "," + titleValues.valuePut;
                        }
                      }
                    } else {
                      _that.comboOptionValListMust.push(titleValues);
                    }
                  } else {
                    _that.comboOptionValListMust.push(titleValues);
                  }
                });
              });
            })
            this.loading = false;
          }
        });
      },
      //多组单选框数据处理方法
      chooseValOid(valOid, titOid) {
        if (this.titleOidArray == "") {
          this.valOidArrayMs.push(valOid);
          this.titleOidArray += titOid + ";";
        } else {
          if (this.titleOidArray.indexOf(titOid) == -1) {
            this.valOidArrayMs.push(valOid);
            this.titleOidArray += titOid + ";";
          } else {
            this.$nextTick(() => {
              this.arrayVals.forEach(a => {
                if (a.titleOid == titOid) {
                  this.valOidArrayMs.forEach(b => {
                    if (a.key == b && b != valOid) {
                      if (this.valOidArrayM.indexOf(valOid) == -1) {
                        let bl = b + ";"
                        this.valOidArrayMs.splice(this.valOidArrayMs.indexOf(b), 1);
                        this.valOidArrayMs.push(valOid);

                      }

                    }
                  });
                }


              });

            })

          }
        }
      },

      getDirectoryMaterialList() {
        this.loading = true;
        queryDirectoryMaterialList(this.comboDireOid).then(response => {
          this.directoryMaterialList = response.data;

          this.$nextTick(() => {
            this.directoryMaterialList.forEach(ma => {
              if (this.materialOidArry.indexOf(ma.materialOid) != -1) {//包含选中
                this.$refs.multipleTable.toggleRowSelection(ma, true);
              }
            });
          })
          this.loading = false;
        });
      },
      getSxServiceList() {
        this.loading = true;
        querySxServiceAddList(this.comboDireOid).then(response => {
          this.sxServiceList = response.data;
          if (this.materialOidArray) {
            this.$nextTick(() => {
              this.$refs.multipleTablea.forEach((outerItem,z) => {
                outerItem.data.forEach(d => {
                  this.materialOidArray.forEach(ma => {
                    if (d.materialOid == ma) {
                      outerItem.toggleRowSelection(d, true);
                    }
                  });
                });
              })
            });


          }

          this.loading = false;
        })
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

        if (oid) {
          this.pubMaterialOid = oid;
          getSxMatersByDireMaterOid(oid).then(response => {
            this.direMaterials = response.data;
            this.serviceMaterialView = true;
            this.title = oid ? "修改" : "新增";
            let item = {
              show: true,
              comboDirectoryOid: this.comboDirectoryOid,
              direMaterials: this.direMaterials,
              pubMaterialOid: this.pubMaterialOid
            };
            this.dialogOptions.push(item);
          });
        } else {
          this.serviceMaterialView = true;
          this.title = oid ? "修改" : "新增";
          let item = {
            show: true,
            comboDirectoryOid: this.comboDirectoryOid,
            pubMaterialOid: ''
          };
          this.dialogOptions.push(item);
        }
      },
      onTableSelect(val,len) {
        //
        this.multipleSelectionM=[];
        val.forEach((item)=>{
          this.multipleSelectionM.push(item.materialOid)
        })
        /*if(!val || val.length==0){
          this.multipleSelectionM=[];
        }else{
          if(len==val.length){//全选，防止来回取消存在数据
            val.forEach((item)=>{
              if(this.multipleSelectionM.indexOf(item.materialOid)!=-1){//包含

              }else{
                this.multipleSelectionM.push(item.materialOid)
              }
            })
          }else{
            val.forEach((item)=>{
              if(this.multipleSelectionM.indexOf(item.materialOid)!=-1){//包含
                this.multipleSelectionM.splice(this.multipleSelectionM.indexOf(item.materialOid),1);
              }
              this.multipleSelectionM.push(item.materialOid)
            })
          }
        }*/
        console.log(this.multipleSelectionM.toString())
      },
      onTableSelect1(val,i,len) {
        if(!val || val.length==0){//取消的时候
          this.multipleSelection1[i]=[];
        }else{
          if(len==val.length){//全选的时候
            val.forEach((item)=>{
              let arr=[];
              if(this.multipleSelection1[i]&&this.multipleSelection1[i].length>0){
                arr=this.multipleSelection1[i];
                if(arr.indexOf(item.materialOid)!=-1){//包含

                }else{
                  arr.push(item.materialOid)
                }
              }else{
                arr.push(item.materialOid)
              }
              this.multipleSelection1[i]=arr;
            })
          }else{//不是全选
            val.forEach((item)=>{
              let arr=[];
              if(this.multipleSelection1[i]&&this.multipleSelection1[i].length>0){
                arr=this.multipleSelection1[i];
                if(arr.indexOf(item.materialOid)!=-1){//包含
                  arr.splice(arr.indexOf(item.materialOid),1);
                }/*else{
                  arr.push(item.materialOid)
                }*/
                arr.push(item.materialOid)
              }else{
                arr.push(item.materialOid)
              }
              this.multipleSelection1[i]=arr;
            })
          }

        }
        /*console.log("数组绑定地方："+this.multipleSelection1.toString())
        console.log("选中的地方："+i)*/
      },
      batchChoose() {
        let materialOids = "";
        let _that = this;
        //获取选中结果

        let arr=this.multipleSelectionM.concat(this.multipleSelection1);
        materialOids=arr.join(",");
        this.valOidArrayM = this.situValCheck.toString();
        if(this.situValCheckbox){//拼接多选
          this.valOidArrayM+=","+this.situValCheckbox.toString();
        }
        for (let mustChoose of this.comboOptionValListMust) {
          if (mustChoose.fillFlag == '1' && (mustChoose.valuePut == '' || mustChoose.valuePut == '0')) {
            this.msgWarning(mustChoose.titleName + "请选择选项值！");
            return false;
            break;
          }
        }
        if (materialOids != "") {
          chooseOptionMaterial(this.comboDirectoryOid, materialOids, this.valOidArrayM, this.relOid).then(response => {
            if (response.code === 200) {
              this.msgSuccess("保存成功");
              this.reset();
              this.$emit('closeMater-Option');
            }
          });
        } else {
          this.msgWarning("请选择材料！")
        }


      },
      //确认目录公共材料
      setMaterialStatus() {
        let direOid = this.comboDireOid;
        this.$confirm('确认目录公共材料?', "警告", {
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
