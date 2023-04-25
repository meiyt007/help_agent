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
              <el-table-column label="序号" width="80" type="index" align="center">
                <template slot-scope="scope">
                  <span>{{ scope.$index + 1 }}</span>
                </template>
              </el-table-column>
              <el-table-column label="选项标题" align="center" width="100" prop="name" :show-overflow-tooltip="true" />
              <el-table-column label="选项值" align="center">
                <template slot-scope="scope">
                  <template v-if="scope.row.moreStatus=='0'">
                    <el-radio-group v-model="situValCheck[scope.$index]">
                      <el-radio v-for="(comboObj,index) in scope.row.arrayVal" :key='index'
                        @change="changeRadio(scope.row.titleOid,comboObj.key)" :label="comboObj.key">{{comboObj.name}}
                      </el-radio>
                    </el-radio-group>
                  </template>
                  <template v-if="scope.row.moreStatus=='1'">
                    <el-checkbox-group v-model="situValCheck">
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
            <div>
            <div class="zf-zc-table--title">统一证照</div>

              <el-table v-loading="loading" :data="directoryResultList" ref="multipleTableTy"
                @selection-change="onTableSelect" border>
                <el-table-column width="70" align="center" type="selection">
                </el-table-column>
                <el-table-column prop="resultName" label="证照名称" align="center" 
                  :show-overflow-tooltip="true" />
              </el-table>
            </div>
            <div >
            <div class="zf-zc-table--title">事项证照</div>

              <el-table v-loading="loading" :data="comboSxResultList" ref="multipleTableSx"
                @selection-change="onTableSelect1">
                <el-table-column width="70" align="center" type="selection">
                </el-table-column>
                <el-table-column prop="serviceName" label="事项名称" :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="resultName" label="证照名称" :show-overflow-tooltip="true">
                </el-table-column>
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
    chooseOptionResult
  } from "@/api/onething/sxpz/optionResult";
  import {
    queryComOptiontitleByRelForResult
  } from "@/api/onething/sxpz/comboDireOptandStu";
  import serviceMaterial from "@/views/onething/sxpz/comboDirectory/serviceMaterial";
  import viewDirectoryMaterial from "@/views/onething/sxpz/comboDirectory/viewDirectoryMaterial";

  import {
    queryDirectoryResultList,
    querySxServiceResultList
  } from "@/api/onething/sxpz/comboDirectoryResult";
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
        directoryResultList: [],
        comboSxResultList: [],
        direResults: [],
        serviceResultView: false,
        dialogOptions: [],
        viewDirectoryMaterial: false,
        pubResultOid: "",
        resultOid: "",
        resultOidArry: "",
        resultOidArray: [],
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
        // 新增/修改显示弹出层
        // openInit: false,
        // 表单参数
        form: {},
        sxServiceList: [],
        rules: {}
      };
    },
    created() {
      this.comboDirectoryOid = this.comboDireOid;
      this.reloid = this.relOid;
      //情形List
      this.handleOptionRel();
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
        let resultOid = "";
        this.multipleSelectionM = [];
        _that.situValCheck = [];
        queryComOptiontitleByRelForResult(_that.comboDirectoryOid, this.reloid).then(response => {
          _that.comboOptionValListMust = [];
          if (response.data != undefined) {
            this.comboOptionValList = response.data;
            this.$nextTick(() => {
              this.comboOptionValList.forEach(d => {
                if (resultOid == "" && d.resultOids != "" && d.resultOids != null) {
                  resultOid = d.resultOids;
                  this.resultOidArry = resultOid;
                  this.resultOidArray = d.resultOids.split(",")
                }
                this.getDirectoryResultList();
                this.getComboSxResultList();
                this.arrayVals = d.arrayVal;
              });
            })
            _that.$nextTick(() => {
              _that.comboOptionValList.forEach(d => {
                d.arrayVal.forEach(a => {
                  if (a.isStatus == '1') {
                    _that.situValCheck.push(a.key);
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

      getDirectoryResultList() {
        this.loading = true;
        queryDirectoryResultList(this.comboDireOid).then(response => {
          this.directoryResultList = response.data;
          this.$nextTick(() => {
            this.directoryResultList.forEach(ma => {
              if (this.resultOidArry.indexOf(ma.resultOid) != -1) {
                this.$refs.multipleTableTy.toggleRowSelection(ma, true);
                if (this.multipleSelectionM.indexOf(ma.resultOid) == -1) {
                  this.multipleSelectionM.push(ma.resultOid);
                }
              }
            });
          })
          this.loading = false;
        });
      },

      getComboSxResultList() {
        this.loading = true;
        querySxServiceResultList(this.comboDireOid).then(response => {
          this.comboSxResultList = response.data;
          this.$nextTick(() => {
            this.comboSxResultList.forEach(ma => {
              if (this.resultOidArry.indexOf(ma.sxResultOid) != -1) {
                this.$refs.multipleTableSx.toggleRowSelection(ma, true);
                if (this.multipleSelectionM.indexOf(ma.sxResultOid) == -1) {
                  this.multipleSelectionM.push(ma.sxResultOid);
                }
              }
            });
          })
          this.loading = false;
        });
      },
      onTableSelect(val) {
        this.multipleSelectionM = val;
      },
      onTableSelect1(val) {
        this.multipleSelection1 = val;
      },
      //保存情形证照信息
      batchChoose() {
        let resultOids = "";
        let _that = this;
        //获取选中证照
        //resultOids=this.multipleSelectionM.toString();
        this.multipleSelectionM.forEach(ser => {
          if (ser.resultOid != null && ser.resultOid != "undefined") {
            resultOids += ser.resultOid + ",";
          }

        });

        this.multipleSelection1.forEach(ser => {
          if (ser.sxResultOid != null && ser.sxResultOid != "undefined") {
            resultOids += ser.sxResultOid + ",";
          }

        });

        this.valOidArrayM = this.situValCheck.toString();
        if(!this.valOidArrayM){
          this.msgWarning("请选择选项值");
          return false;
        }
        for (let mustChoose of this.comboOptionValListMust) {
          if (mustChoose.fillFlag == '1' && (mustChoose.valuePut == '' || mustChoose.valuePut == '0')) {
            this.msgWarning(mustChoose.titleName + "请选择选项值！");
            return false;
            break;
          }
        }
        if (resultOids != "") {
          chooseOptionResult(this.comboDirectoryOid, resultOids, this.valOidArrayM, this.relOid).then(response => {
            if (response.code === 200) {
              this.msgSuccess("暂存成功");
              this.reset();
              this.$emit('closeMater-Option');
            }
          });
        } else {
          this.msgWarning("请选择证照！")
        }
      }
    }
  };
</script>
