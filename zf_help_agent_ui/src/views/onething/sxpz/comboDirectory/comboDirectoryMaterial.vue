/** * @Author: wangxl */
<template>
  <div>
    <el-tabs>
      <div>
        <div class="zf-zc-table--title">公共材料</div>
        <el-row :gutter="10" class="mb8 fl">
          <el-col :span="1.5">
            <el-button
              type="default"
              icon="el-icon-plus"
              size="mini"
              @click="handleInit"
              v-hasPermi="['combo:directory:init']"
              >新增</el-button
            >
            <el-button
              type="default"
              icon="el-icon-s-promotion"
              size="mini"
              @click="setMaterialStatus"
              v-hasPermi="['combo:directory:init']"
              >确认材料</el-button
            >
          </el-col>
        </el-row>
        <el-table
          v-loading="loading"
          :data="directoryMaterialList"
          border
        >
          <el-table-column label="序号" width="80" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="materialName"
            label="材料名称"
            align="center"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            prop="materialType"
            :formatter="getMaterialType"
            label="材料类型"
            align="center"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            prop="materialFormat"
            :formatter="getMaterialFormat"
            label="材料形式"
            align="center"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            prop="paperNumber"
            label="份数"
            align="center"
            show-overflow-tooltip
          />
          <el-table-column
            prop="mustFlag"
            :formatter="getMustFlag"
            label="必要性"
            align="center"
            show-overflow-tooltip
          />
          <el-table-column
            prop="status"
            :formatter="getStatus"
            align="center"
            label="状态"
            show-overflow-tooltip
          />
          <el-table-column
            label="操作"
            width="450"
            align="center"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-chakan"
                @click="handleView(scope.row)"
                v-hasPermi="['combo:directory:view']"
                >查看</el-button
              >
              <el-button
                v-show="scope.row.status != 1"
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="handleInit(scope.row)"
                v-hasPermi="['combo:directory:update']"
                >修改</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-shanchu"
                @click="handleDelete(scope.row)"
                v-hasPermi="['combo:directory:delete']"
                >删除</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="handleElecInit(scope.row)"
                v-hasPermi="['combo:directory:update']"
                >配置证照</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="handleRefinedInit(scope.row)"
                v-hasPermi="['combo:directory:update']"
                >细化材料配置</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div>
        <div class="zf-zc-table--title">事项材料信息</div>

        <!-- <el-row :gutter="20">
        <el-col :span="24" :xs="24"> -->
        <el-row :gutter="10" class="mb8 fl">
          <el-col :span="1.5">
            <el-button
              type="default"
              icon="el-icon-plus"
              size="mini"
              @click="handleSxMaterialInit"
              v-hasPermi="['combo:directory:init']"
              >新增</el-button
            ><label style="color: #bd2c00"
              >（如果您还需要展示涉及事项材料，请在这里进行操作！）</label
            >
          </el-col>
        </el-row>
        <el-table
          ref="multipleTable"
          :data="sxMaterialList"
          style="width: 100%"
          border
        >
          <el-table-column label="序号" width="80" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="serviceName"
            label="所属事项名称"
            align="center"
            :show-overflow-tooltip="true"
          >
          </el-table-column>
          <el-table-column
            prop="materialName"
            label="材料名称"
            align="center"
            :show-overflow-tooltip="true"
          >
          </el-table-column>
          <el-table-column
            prop="materialType"
            label="材料类型"
            :formatter="getMaterialType"
            align="center"
            :show-overflow-tooltip="true"
          >
          </el-table-column>
          <el-table-column
            prop="materialFormat"
            :formatter="getMaterialFormatSX"
            align="center"
            label="材料形式"
            show-overflow-tooltip
            width="150"
          >
          </el-table-column>
          <el-table-column
            prop="paperNumber"
            align="center"
            label="份数"
            width="150"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            prop="mustFlag"
            :formatter="getMustFlag"
            align="center"
            label="必要性"
            show-overflow-tooltip
            width="150"
          />
          <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-shanchu"
                @click="handleSxDelete(scope.row)"
                v-hasPermi="['combo:directory:delete']"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <!-- </el-col>
      </el-row> -->
      </div>

      <!--目录公共材料添加/修改 开始-->
      <el-dialog
        v-dialog-drag
        :visible.sync="item.show"
        v-for="item in dialogOptions"
        title="添加/修改目录公共材料"
        :key="item.comboDirectoryOid"
        :close-on-click-modal="false"
        width="1100px"
        height="700px"
        scrollbar
        append-to-body
      >
        <service-material
          :comboDireOid="item.comboDirectoryOid"
          :direMaterials="item.direMaterials"
          :pubMaterialOid="item.pubMaterialOid"
          @dialog-close="closeServiceMaterial"
          @delete-close="handleInit"
        >
        </service-material>
      </el-dialog>
      <!--目录公共材料添加/修改 结束-->

      <!--查看目录公共材料开始-->
      <el-dialog
        v-dialog-drag
        :visible.sync="view.show"
        v-for="view in viewDialogOptions"
        title="查看目录公共材料"
        :key="view.materialOid"
        :close-on-click-modal="false"
        width="1100px"
        height='700px'
        scrollbar
        append-to-body
      >
        <view-directory-material
          :materialOid="view.materialOid"
          @view-dialog-close="closeDireMaterial"
        >
        </view-directory-material>

        <div slot="footer" class="zf-text-center">
          <el-button @click="closeDireMaterial">关 闭</el-button>
        </div>
      </el-dialog>
      <!--查看目录公共材料 结束-->
      <!--选择事项材料-->
      <el-dialog
        v-dialog-drag
        :visible.sync="chooseSxMaterialView"
        v-if="chooseSxMaterialView"
        title="事项材料选择"
        :close-on-click-modal="false"
        width="1100px"
        height="700px"
        scrollbar
        append-to-body
      >
        <choose-sx-material
          :direOid="comboDirectoryOid"
          @sx-dialog-close="closeSxMaterial"
        ></choose-sx-material>
        <div slot="footer" class="zf-text-center">
          <el-button @click="closeSxMaterial">关 闭</el-button>
        </div>
      </el-dialog>

      <!-- 添加证照目录关联 -->
      <el-dialog
        v-dialog-drag
        :close-on-click-modal="false"
        :title="title"
        :visible.sync="initElecView"
        v-if="initElecView"
        width="1100px"
        append-to-body
      >
        <el-form
          ref="elecform"
          :model="elecform"
          label-width="0px"
          class="dialog-table"
        >
          <el-input v-show="false" v-model="elecform.id" />
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td>
                <b><i class="require">*</i>选择证照目录：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="billOid">
                  <el-select
                    v-model="elecform.elecBillOid"
                    clearable
                    filterable
                    style="width: 50%"
                  >
                    <el-option
                      v-for="dict in billList"
                      :key="dict.billOid"
                      :label="dict.directoryName"
                      :value="dict.billOid"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </td>
            </tr>
          </table>
        </el-form>
        <div slot="footer" class="zf-text-center">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="initElecView = false">取 消</el-button>
        </div>
      </el-dialog>

      <!--配置细化材料 开始-->
      <el-dialog
        v-dialog-drag
        :visible.sync="item.show"
        v-for="item in dialogRefinedOptions"
        title="配置细化材料"
        :key="item.comboDirectoryOid"
        :close-on-click-modal="false"
        width="1100px"
        height="700px"
        scrollbar
        append-to-body
      >
        <refined-Materials
          :comboDirectoryOid="item.comboDirectoryOid"
          :direMaterials="item.direMaterials"
          :pubMaterialOid="item.pubMaterialOid"
          @dialog-close="closeRefinedMaterial"
          @delete-close="handleInit"
        >
        </refined-Materials>
        <div slot="footer" class="zf-text-center">
          <el-button @click="closeRefinedMaterial">关 闭</el-button>
        </div>
      </el-dialog>
      <!--配置细化材料 结束-->
    </el-tabs>
  </div>
</template>

<script>
import {
  queryDirectoryMaterialList,
  getSxMatersByDireMaterOid,
  del,
  queryComboDireSxMaterList,
  setMaterialStatus,
  delSxMater,
  saveOrUpdateElecBill
} from "@/api/onething/sxpz/comboDirectoryMaterial";
import { pageMaterialSplitList } from "@/api/zc/qdgl/sxService.js";
import { queryMaterBill } from "@/api/zc/businessManagement/sxSerMaterBill.js";
import serviceMaterial from "@/views/onething/sxpz/comboDirectory/serviceMaterial";
import viewDirectoryMaterial from "@/views/onething/sxpz/comboDirectory/viewDirectoryMaterial";
import chooseSxMaterial from "@/views/onething/sxpz/comboDirectory/chooseSxMaterial";
import refinedMaterials from "@/views/onething/sxpz/comboDirectory/refinedMaterials";
export default {
  components: {
    serviceMaterial,
    viewDirectoryMaterial,
    chooseSxMaterial,
    refinedMaterials
  },
  name: "ComboDirectoryMaterial",
  //定义获取父类传过来值的格式
  props: ["comboDireOid"],
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
      chooseSxMaterialView: false,
      viewDialogOptions: [],
      pubMaterialOid: "",
      materialOid: "",
      // 新增/修改显示弹出层
      // openInit: false,
      // 表单参数
      form: {},
      sxServiceList: [],
      sxMaterialList: [],
      billList: [],
      elecform: {},
      initElecView: false,
      dialogRefinedOptions: []

      // 表单校验
      /*rules: {
          billOid: [{
            required: true,
            message: "证照目录不能为空",
            trigger: "blur"
          }]
        },*/
    };
  },
  created() {
    this.comboDirectoryOid = this.comboDireOid;
    //公共材料List
    this.getDirectoryMaterialList();
    //事项材料List
    this.getSxMaterialList();
  },
  methods: {
    submitFormMaterial() {
      console.log(this.$refs.serviceMaterial);
    },
    // 表单重置
    reset() {
      this.resetForm("form");
    },
    getDirectoryMaterialList() {
      this.loading = true;
      queryDirectoryMaterialList(this.comboDireOid).then(response => {
        this.directoryMaterialList = response.data;
        this.loading = false;
      });
    },
    getSxMaterialList() {
      this.loading = true;
      queryComboDireSxMaterList(this.comboDireOid).then(response => {
        this.sxMaterialList = response.data;
        this.loading = false;
      });
    },

    /** 查看按钮操作 */
    handleView(row) {
      let item = {
        show: true,
        materialOid: row.materialOid
      };
      this.viewDialogOptions.push(item);
      //this.viewDirectoryMaterial = true;
      //this.materialOid = row.materialOid;
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
          pubMaterialOid: ""
        };
        this.dialogOptions.push(item);
      }
    },

    //打开细化材料页面
    handleRefinedInit(row) {
      this.reset();
      let oid = row.materialOid;
      this.serviceMaterialView = true;
      this.title = "配置细化材料";
      let item = {
        show: true,
        comboDirectoryOid: this.comboDirectoryOid,
        direMaterials: this.direMaterials,
        pubMaterialOid: oid
      };
      this.dialogRefinedOptions.push(item);
      // alert(JSON.stringify(item))
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      pageMaterialSplitList(row.materialOid).then(response => {
        let xhMaterailList = response.data;
        if (xhMaterailList.length > 0) {
          this.$message.error("该材料包含细化材料需先删除细化材料后才能删除");
        } else {
          const oid = row.id;
          this.$confirm("是否确认删除?", "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          })
            .then(function() {
              return del(oid);
            })
            .then(() => {
              this.msgSuccess("删除成功");
              //公共材料List
              this.getDirectoryMaterialList();
            })
            .catch(function() {});
        }
      });
    },
    closeServiceMaterial() {
      this.dialogOptions.pop();
      this.serviceMaterialView = false;
      //公共材料List
      this.getDirectoryMaterialList();
      //事项材料List
      this.getSxMaterialList();
    },
    closeDireMaterial() {
      this.viewDialogOptions.pop();
      // this.viewDirectoryMaterial = false;
    },
    closeRefinedMaterial() {
      this.dialogRefinedOptions.pop();
    },
    getMaterialFormatSX(val) {
      if (val.materialFormat == 1) {
        return "纸质";
      } else if (val.materialFormat == 2) {
        return "电子版";
      } else if (val.materialFormat == 3) {
        return "证照";
      } else if (val.materialFormat == 4) {
        return "容缺补正";
      } else if (val.materialFormat == 7) {
        return "告知承诺";
      } else {
        return "";
      }
    },
    getMaterialFormat(val) {
      if (val.materialFormat == 1) {
        return "纸质";
      } else if (val.materialFormat == 2) {
        return "电子版";
      } else if (val.materialFormat == 5) {
        return "纸质、电子版";
      } else {
        return "";
      }
    },
    getMaterialType(val) {
      if (val.materialType == 0) {
        return "原件";
      } else if (val.materialType == 1) {
        return "复印件";
      } else if (val.materialType == 2) {
        return "原件和复印件";
      } else if (val.materialType == 3) {
        return "原件或复印件";
      } else {
        return "";
      }
    },
    getMustFlag(val) {
      if (val.mustFlag == 0) {
        return "必要";
      } else if (val.mustFlag == 1) {
        return "非必要";
      } else if (val.mustFlag == 2) {
        return "容缺后补";
      } else if (val.mustFlag == 3) {
        return "信息免交、信用后补";
      }
    },
    getMaterialSource(val) {
      if (val.materialSource == 0) {
        return "申请人自备";
      } else if (val.materialSource == 1) {
        return "政府部门核发";
      } else if (val.materialSource == 2) {
        return "其他";
      }
    },
    getStatus(val) {
      if (val.status == 0) {
        return "未确认";
      } else if (val.status == 1) {
        return "已确认";
      }
    },

    //确认目录公共材料
    setMaterialStatus() {
      let direOid = this.comboDireOid;
      this.$confirm("确认目录公共材料吗?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return setMaterialStatus(direOid);
        })
        .then(() => {
          this.msgSuccess("确认成功");
          //公共材料List
          this.getDirectoryMaterialList();
        })
        .catch(function() {});
    },
    handleSxMaterialInit() {
      this.chooseSxMaterialView = true;
      this.direOid = this.comboDireOid;
    },

    closeSxMaterial() {
      this.chooseSxMaterialView = false;
      //事项材料List
      this.getSxMaterialList();
    },
    //删除事项结果
    handleSxDelete(row) {
      const oid = row.materialOid;
      let direOid = this.comboDireOid;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return delSxMater(direOid, oid);
        })
        .then(() => {
          this.msgSuccess("删除成功");
          //事项材料List
          this.getSxMaterialList();
        })
        .catch(function() {});
    },
    getBillList() {
      let _that = this;
      // 查询证照目录
      queryMaterBill().then(response => {
        if (response.data) {
          _that.billList = response.data;
          _that.billList.forEach(item => {
            if (item.directoryObj == 1) {
              item.directoryName = item.directoryName + "[法人]";
            } else if (item.directoryObj == 2) {
              item.directoryName = item.directoryName + "[混合]";
            } else if (item.directoryObj == 3) {
              item.directoryName = item.directoryName + "[其他]";
            } else if (item.directoryObj == 0) {
              item.directoryName = item.directoryName + "[自然人]";
            }
          });
        }
      });
    },
    handleElecInit(rows) {
      this.elecform = rows;
      this.getBillList();
      this.title = "材料证照配置";
      this.initElecView = true;
    },
    /** 提交按钮 */
    submitForm: function() {
      let _that = this;
      if (!_that.elecform.elecBillOid) {
        _that.$message.error("证照目录不能为空!");
        return false;
      }
      saveOrUpdateElecBill(_that.elecform.id, _that.elecform.elecBillOid).then(
        response => {
          if (response.code == 200) {
            _that.elecform = response.data;
            _that.msgSuccess("保存成功");
            _that.initElecView = false;
          }
        }
      );
    }
  }
};
</script>
