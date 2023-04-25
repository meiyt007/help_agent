/** * @Author: dongxl * @date:2021-11-24 */
<template>
  <div>
    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit"
          >新增</el-button
        >
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="labelList" border="">
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="填充来源"
        align="center"
        prop="fillSource"
        :show-overflow-tooltip="true"
      >
        <template scope="scope">
          <span v-if="scope.row.fillSource==0">基础表单</span>
          <span v-if="scope.row.fillSource==1">电子证照</span>
          <span v-if="scope.row.fillSource==2">OCR识别结果</span>
        </template>
      </el-table-column>
      <el-table-column
        label="填充字段"
        align="center"
        prop="temFieldName"
        :show-overflow-tooltip="true"
      />
       <el-table-column
        label="表单字段"
        align="center"
        prop="fillFieldName"
        :show-overflow-tooltip="true"
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
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改应用信息对话框 -->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openInit"
      v-if="openInit"
      width="1100px"
      append-to-body
    >
      <!--<h3>事项标题</h3>-->
      <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><b>目录名称：</b></td>
            <td colspan="3">{{ comboDirectoryName }}</td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>填充来源：</b></td>
            <td colspan="3">
              <el-form-item prop="fillSource">
                <el-radio-group v-model="form.fillSource" @change="getNeedFieldList">
                  <el-radio :label="0">基础表单</el-radio>
                  <el-radio :label="1">电子证照</el-radio>
                  <el-radio :label="2">OCR识别结果</el-radio>
                </el-radio-group>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>填充字段：</b></td>
            <td>
              <el-form-item prop="tempFieldOid">
                <el-select v-if="form.fillSource==0"
                  v-model.trim="form.tempFieldOid"
                  placeholder="基础表单字段"
                  size="small"
                  style="width: 240px"
                  clearable
                  filterable
                >
                  <el-option
                    v-for="dept in basicFieldList"
                    :key="dept.oid"
                    :label="dept.fieldName+'【'+dept.fieldKey+'】'"
                    :value="dept.oid"
                  />
                </el-select>
                <el-cascader v-if="form.fillSource==1"
                  v-model="form.tempFieldOid"
                  :options="elecLicenList"
                  :props="{ expandTrigger: 'hover' }"
                  :show-all-levels="false"
                  placeholder="电子证照元素"
                  size="small"
                  style="width: 240px"
                  clearable
                  filterable
                >
                </el-cascader>

                <el-cascader v-if="form.fillSource==2"
                             v-model="form.tempFieldOid"
                             :options="ocrElementList"
                             :props="{ expandTrigger: 'hover' }"
                             :show-all-levels="false"
                             placeholder="ocr识别结果元素"
                             size="small"
                             style="width: 240px"
                             clearable
                             filterable
                >
                </el-cascader>
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>电子表单字段名称：</b></td>
            <td>
              <el-form-item prop="fillFieldOid">
                <el-select
                  v-model.trim="form.fillFieldOid"
                  placeholder="字段名称"
                  size="small"
                  style="width: 240px"
                  clearable
                  filterable
                >
                  <el-option
                    v-for="dept in fieldList"
                    :key="dept.fieldOid"
                    :label="dept.fieldName+'【'+dept.fieldCode+'】'"
                    :value="dept.fieldOid"
                  />
                </el-select>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" align="center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  queryBaseFieldList,queryUseComboFillFieldListByThingOid,
  page,
  saveOrUpdate,
  del,
  init,
  queryElecAndElementTree,
  queryMaterialAndCataAndElementTree,
  checkHasRepeat
} from '@/api/onething/comboForm/comboformFieldRelConfig'
export default {
  components: {},
  props: ["comboDirectoryOid", "comboDirectoryName"],
  name: "comboformFieldRelConfig",
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      basicFieldList: [],
      elecLicenList:[],
      ocrElementList:[],
      fieldList: [],
      labelList:[],
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        comboDirectoryOid: "",
        fillType: 1,
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单参数
      form: { fillSource: 0,fillType:1 },
      // 表单校验
      rules: {
        tempFieldOid: [
          { required: true, message: "填充字段不能为空", trigger: "blur" }
        ],
        fillFieldOid: [
          { required: true, message: "电子表单字段不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created () {
    this.getList();
  },
  watch: {
    openLabel: {
      deep: true,
      handler: function (newVal) {
        if (newVal) {
          this.getList();
        }
      }
    }
  },
  methods: {
    /** 查询目录清单列表 */
    getList () {
      this.loading = true;
      this.queryParams.comboDirectoryOid = this.comboDirectoryOid;
      page(this.queryParams).then(response => {
        this.labelList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    close () {
      this.openView = false;
    },
    // 取消按钮
    cancel () {
      this.openInit = false;
      this.reset();
    },
    // 表单重置
    reset () {
      this.form = {
        name: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    //被填充数据类型查询
    getNeedFieldList(val,temFlag){
      this.basicFieldList=[];
      if(temFlag!='1'){
        this.form.tempFieldOid="";
      }
      if(val==0){//基础表单字段
        queryBaseFieldList().then(response=>{
          if(response.data){
            this.basicFieldList=response.data
          }
        })
      }else if(val==1){//电子证照
        queryElecAndElementTree(this.comboDirectoryOid).then(response=>{
          this.elecLicenList=response.data;
        })
      }else if(val==2){//ocr识别
        queryMaterialAndCataAndElementTree(this.queryParams).then(response => {
          this.ocrElementList = response.data;
        });
      }

    },
    //查询事项下面所有非动态标签的字段
    getAllFormFieldList(){
      queryUseComboFillFieldListByThingOid(this.comboDirectoryOid).then(response=>{
        this.fieldList=response.data;
      })
    },


    /** 新增和修改按钮操作 */
   async handleInit (row) {
      const id = row.id;
      if (id === undefined) {
        this.form = {
          comboDirectoryOid: this.comboDirectoryOid,
          fillSource: 0,
          fillType:1
        };
        this.getNeedFieldList(this.form.fillSource,'1');
      } else {
       await init(id).then(response => {
          this.form = response.data;
          console.log(JSON.stringify(this.form))
          if (this.form.fillSource ===2 || this.form.fillSource==1) {
            this.form.tempFieldOid = this.form.tempFieldOid.split(",");
            console.log(this.form.tempFieldOid);
          }
          this.getNeedFieldList(this.form.fillSource,'1');
        });
      }
      this.openInit = true;
      this.title = "电子表单字段值填充";
      //查询表单字段信息
      this.getAllFormFieldList();
    },

    /** 提交按钮 */
    submitForm: function () {
      if(this.form.tempFieldOid && this.form.tempFieldOid.length==2){
        this.form.tempFieldOid=this.form.tempFieldOid[1];
      }
      //ocr元素信息
      if (this.form.fillSource ==2) {
        /*var materialOid = this.form.tempFieldOid[0].split(";")[0];  //材料oid 可以待用
        var refineOid = this.form.tempFieldOid[1].split(";")[0]; //细化材料oid
        var cataOid = this.form.tempFieldOid[1].split(";")[2]; //材料目录oid 可以待用
        var fieldOid = this.form.tempFieldOid[2].split(";")[0]; //字段oid
        this.form.tempFieldOid = refineOid + ";" + fieldOid;*/
        this.form.tempFieldOid = this.form.tempFieldOid.toString();
      }
      this.$refs["form"].validate(valid => {
        if (valid) {
          checkHasRepeat(this.comboDirectoryOid, this.form.fillType, this.form.fillFieldOid, this.form.oid).then(response => {
            if (response.data === 'false') {
              saveOrUpdate(this.form).then(response => {
                if (response.code === 200 && !response.data) {
                  this.msgSuccess("保存成功");
                  this.openInit = false;
                  this.getList();
                }
              });
            } else {
              this.msgWarning("电子表单字段已经被填充，请勿重复填充");
            }
          })
        } else {
          return false;
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      const id = row.id;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function () {
          return del(id);
        })
        .then(() => {
          this.msgSuccess("删除成功");
          this.getList();
        })
        .catch(function () { });
    },
  }
};
</script>
