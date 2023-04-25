<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="110px"
    >
      <el-form-item label="所属区划">
        <treeselect
          class="treeselect"
          :defaultExpandLevel="1"
          noOptionsText="暂无数据"
          noResultsText="暂无数据"
          :show-count="true"
          v-model="queryParams.districtOidSelect"
          :options="districtOptions"
          placeholder="请输入所属区划"
        />
      </el-form-item>
      <el-form-item label="组织机构名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入组织机构名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="屏蔽状态" prop="shield">
                  <el-col :span="24">
                    <el-select
                      v-model.trim="queryParams.shield"
                      placeholder="请选择屏蔽状态类型"
                      size="small" >
                      <el-option label="未屏蔽" value="0"></el-option>
                      <el-option label="已屏蔽" value="1"></el-option>
                    </el-select>
                  </el-col>
        </el-form-item>
      <el-form-item class="fr mr0">
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button
          type="warning"
          icon="el-icon-refresh"
          size="mini"
          @click="resetQuery"
          class="btn-reset"
          >重置</el-button
        >
      </el-form-item>
    </el-form>
    <td v-if="isshield !=='1'">
      <el-row :gutter="10" class="mb8 fl">
       <el-button type="danger" size="mini" @click="shieldRows(isshield)">批量屏蔽</el-button>
      </el-row>
    </td>
    <td v-if="isshield =='1'">
      <el-row :gutter="10" class="mb8 fl">
       <el-button type="danger" size="mini" @click="shieldRows(isshield)">批量还原屏蔽</el-button>
      </el-row>
    </td>

    <el-table v-loading="loading" :data="organList" 
      stripe
      style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"/>
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="所属区划"
        align="center"
        prop="districtName"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" placement="top">
            <div v-html="scope.row.districtName" slot="content"></div>
            <div class="oneLine">{{ scope.row.districtName }}</div>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column
        label="组织机构名称"
        align="center"
        prop="name"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="组织机构全称"
        align="center"
        prop="fullName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="组织机构代码"
        align="center"
        prop="code"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="组织机构电话" align="center" prop="telphone" />
      <el-table-column
        label="组织机构类别"
        align="center"
        prop="typeDictName"
      />
      <el-table-column
        label="操作"
        width="325"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <td v-if="isshield !=='1'">
            <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="handleShield(scope.row.id,isshield)"
                v-hasPermi="['sys:frontorgan:shield']"
                >屏蔽</el-button
              >
          </td>
          <td v-if="isshield =='1'">
            <el-button
                size="mini"
                type="text"
                icon="iconfont zfsoft-xiugai"
                @click="handleShield(scope.row.id,isshield)"
                v-hasPermi="['sys:frontorgan:shield']"
                >还原屏蔽</el-button
              >
          </td>
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
  </div>
</template>

<script>
import { querySysOrganShieldWithPage, able,batchShieldOrgan,shieldById } from "@/api/sys/organ";
import { validatePhoneTwo, validIntNoZero, validateLegalStr, validateUniteCode } from '@/utils/validate';
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import IndexChild from '@/views/sys/organ/indexChild';
import IndexUser from '@/views/sys/organ/indexUser';
export default {
  name: "Organ",
  components: { Treeselect, IndexChild, IndexUser },
  data () {
    return {
      //事项是否屏蔽状态
      isshield:'0',
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 组织机构表格数据
      organList: [],
      multipleSelection: [],  //批量选择中选择的记录列表
      //消息
      ableMap: { '1': '启用', '0': '禁用' },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: '',
        districtOid: null,
        parentOid: '',
        districtOidSelect: null,
        shield: null
      },
      // 表单参数
      form: {},
      //父类参数
      parentOrgan: [],
      //区划Tree
      districtOptions: [],
      // 表单校验
      rules: {
        districtOidSelect: [
          { required: true, message: "请选择所属区划", trigger: "blur" }
        ],
        name: [
          { required: true, message: "组织机构名称不能为空", trigger: "blur" }
        ],
        fullName: [
          { required: true, message: "组织机构全称不能为空", trigger: "blur" }
        ],
        typeDictOid: [
          { required: true, message: "请选择组织机构类别", trigger: "blur" }
        ],
        code: [
          { required: true, message: "组织机构代码不能为空", trigger: "blur" },
          { min: 9, max: 9, message: "请输入9位长度的组织机构代码" },
          { validator: validateLegalStr, trigger: 'blur' }
        ],
        simpleCode: [
          { required: true, message: "组织机构简码不能为空", trigger: "blur" },
          { validator: validateLegalStr, trigger: 'blur' }
        ],
        address: [
          { required: true, message: "组织机构地址不能为空", trigger: "blur" }
        ],
        telphone: [
          { required: true, message: "组织机构电话不能为空", trigger: "blur" },
          { validator: validatePhoneTwo, trigger: 'blur' }
        ],
        uniteCode: [
          { validator: validateUniteCode, trigger: 'blur' }
        ],
        sort: [
          { required: true, message: "排序号不能为空", trigger: "blur" },
          { validator: validIntNoZero, trigger: 'blur' }
        ]
      }
    };
  },
  watch: {
    'queryParams.districtOidSelect': {
      handler (val, oldVal) {
        let dataId = null != val ? val.substring(val.lastIndexOf('-') + 1, val.length) : '';
        this.queryParams.districtOid = dataId;
      }
    },
    'form.districtOidSelect': {
      handler (val, oldVal) {
        let dataId = null != val ? val.substring(val.lastIndexOf('-') + 1, val.length) : '';
        this.form.districtOid = dataId;
      }
    },
  },
  created () {
    this.getDistrictTree();
    this.getList();
    /** 数据字典中保存组织机构类型的数据项 */
    this.getDictList("ZZJGLX").then(response => {
      this.typeDictOptions = response.data;
    });
  },

  methods: {
    //屏蔽
    handleShield(id,isshield){
        this.$confirm('此操作将屏蔽该部门, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {//确定执行then方法
          //调用接口方法
          shieldById(id,isshield)
           .then(response => {
               //提示信息
                this.$message({
                type: 'success',
                message: '屏蔽成功!'
              })
               //刷新页面
               this.getList()
           })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消屏蔽'
          });
        });
    },
     //批量屏蔽按钮
     shieldRows(isshield){
          this.$confirm('此操作将屏蔽选择部门, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {//确定执行then方法
            var idList =[]
            //遍历数组得到每个id值，设置到idlist中
            for(var i=0;i<this.multipleSelection.length;i++){
                var obj = this.multipleSelection[i]
                var id = obj.id
                idList.push(id)
            }
            //调用接口方法
            batchShieldOrgan(idList,isshield)
            .then(response => {
                //提示信息
                  this.$message({
                  type: 'success',
                  message: '批量屏蔽成功!'
                })
                //刷新页面
                this.getList()
            })
          })
      },
    //获取选择复选框的id值
    handleSelectionChange(selection){
        this.multipleSelection = selection
    },
    /** 查询组织机构列表 */
    getList () {
      this.loading = true;
      let that = this;
      querySysOrganShieldWithPage(this.queryParams).then(response => {
        //this.organList = handleTree(response.data.data, "organOid");
        this.organList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      }).catch(function () { that.loading = false; });
    },
    /** 获取区划树 */
    getDistrictTree (districtOid) {
      this.queryDistrictSimpleTree(districtOid).then(response => {
        this.districtOptions = response.data;
      });
    },
    // 消息
    statusFormat (row) {
      return this.selectMapLabel(this.ableMap, row.isAble);
    },
    // 取消按钮
    cancel () {
      this.openInit = false;
      this.reset();
    },
    // 表单重置
    reset () {
      this.form = {
        name: null,
        districtOid: null,
        districtName: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.isshield = this.queryParams.shield;
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.queryParams.districtOidSelect = undefined;
      this.queryParams.districtOid = '';
      this.handleQuery();
    },
    
   
    //关闭
    outerVisible () {
      this.dialogOptions.pop();
      this.getList();
    },
    // 关闭按钮
    closeUserView () {
      this.userDialogOptions.pop();
    },
    
    
   
    
    /** 启禁用按钮操作 */
    handleAble (row) {
      const oid = row.id;
      let ableMessage = row.isAble == 1 ? '启用' : '禁用'
      this.$confirm('你确定要' + ableMessage + '吗？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return able(oid);
      }).then(() => {
        this.msgSuccess(ableMessage + "成功");
      }).catch(function () {
        row.isAble = row.isAble == 0 ? 1 : 0
      });
    },
  
  }
};
</script>
<style scoped>
.treeselect {
  width: 200px !important;
}
/*.treeselect240{
    width: 240px !important;
  }*/
table {
  border-collapse: collapse;
}
.oneLine {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>
