<template>
  <div class="app-container typeApp">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="100px"
    >
      <el-form-item label="终端编号" prop="terminalCode">
        <el-input
          v-model.trim="queryParams.terminalCode"
          placeholder="请输入终端编号"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="所属区划" prop="districtOid">
        <treeselect
          class="treeselect"
          :defaultExpandLevel="1"
          noOptionsText="暂无数据"
          noResultsText="暂无数据"
          :show-count="true"
          v-model="queryParams.districtOid"
          :options="districtMap"
          placeholder="请输入所属区划"
        />
      </el-form-item>
      <el-form-item label="所属点位">
        <el-select
          v-model="queryParams.pointOid"
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="data in pointMap"
            :label="data.name"
            :value="data.label"
          />
        </el-select>
      </el-form-item>
      <el-form-item class="fr mr0">
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
        >搜索
        </el-button
        >
        <el-button
          type="warning"
          icon="el-icon-refresh"
          size="mini"
          @click="resetQuery"
          class="ml5"
        >重置
        </el-button
        >
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit"
        >新增
        </el-button
        >
      </el-col>
    </el-row>

    <el-table
      ref="multipleTable"
      :data="terminalInfoList"
      tooltip-effect="dark"
      style="width: 100%"
    >
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="终端编号"
        align="center"
        prop="terminalCode"
        show-overflow-tooltip
      />
      <el-table-column
        label="终端类型"
        align="center"
        prop="terminalType"
        show-overflow-tooltip
      >
      <template slot-scope="scope">
        <p v-for="data in terminalTypeMap" v-if="scope.row.terminalType==data.label">{{data.name}}</p>
      </template>
      </el-table-column>
      <el-table-column
        label="所属区划"
        align="center"
        prop="districtName"
        show-overflow-tooltip
      />
      <el-table-column
        label="所属点位"
        align="center"
        prop="pointOid"
        show-overflow-tooltip
      >
      <template slot-scope="scope">
        <p v-for="data in pointMap" v-if="scope.row.pointOid==data.label">{{data.name}}</p>
      </template>
      </el-table-column>
<!--      <el-table-column-->
<!--        label="外设"-->
<!--        align="center"-->
<!--        prop="peripheral"-->
<!--        show-overflow-tooltip-->
<!--      >-->
<!--        <template slot-scope="scope">-->
<!--          <p v-for="data in pointMap" v-if="scope.row.peripheral==data.label">{{data.name}}</p>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['im:icon:view']"
          >查看
          </el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            v-hasPermi="['im:icon:update']"
          >修改
          </el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
            v-hasPermi="['im:icon:delete']"
          >删除
          </el-button
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

    <!-- 查看 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openView"
      width="900px"
      append-to-body
    >
      <el-form
        ref="form"
        :model="terminalInfo"
        :rules="rules"
        label-width="0px"
        class="demo-ruleForm"
      >
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%"/>
            <col width="30%"/>
            <col width="20%"/>
            <col width="30%"/>
          </colgroup>
          <tr>
            <td><i class="require">*</i><b>终端编号：</b></td>
            <td>
              {{ terminalInfo.terminalCode }}
            </td>
            <td><i class="require">*</i><b>终端类型：</b></td>
            <td>
              <p v-for="data in terminalTypeMap" v-if="terminalInfo.terminalType==data.label">{{data.name}}</p>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>所属区划：</b></td>
            <td>
              {{ terminalInfo.districtName }}
            </td>
            <td><i class="require">*</i><b>所属点位：</b></td>
            <td>
              <p v-for="data in pointMap" v-if="terminalInfo.pointOid==data.label">{{data.name}}</p>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>外设：</b></td>
            <td colspan="3">
              <el-form-item prop="peripheral">
                <el-checkbox-group v-model="peripheralList" @change="handleChange">
                  <el-checkbox disabled v-for="data in peripheralMap" :label="data.label" :key="data.name">{{data.name}}
                  </el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" align="center">
        <el-button @click="openView=false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 新增或修改 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openInit"
      width="900px"
      append-to-body
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="0px"
        class="demo-ruleForm"
      >
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%"/>
            <col width="30%"/>
            <col width="20%"/>
            <col width="30%"/>
          </colgroup>
          <tr>
            <td><i class="require">*</i><b>终端编号：</b></td>
            <td>
              <el-form-item prop="terminalCode">
                <el-input
                  v-model.trim="form.terminalCode"
                  placeholder="请输入终端编号"
                  maxlength="64"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>终端类型：</b></td>
            <td>
              <el-select
                v-model="form.terminalType"
                size="small"
                style="width: 240px"
              >
                <el-option
                  v-for="data in terminalTypeMap"
                  :label="data.name"
                  :value="data.label"
                />
              </el-select>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>所属区划：</b></td>
            <td>
              <treeselect
                class="treeselect"
                :defaultExpandLevel="1"
                noOptionsText="暂无数据"
                noResultsText="暂无数据"
                :show-count="true"
                v-model="form.districtOid"
                :options="districtMap"
                placeholder="请输入所属区划"
              />
            </td>
            <td><i class="require">*</i><b>所属点位：</b></td>
            <td>
              <el-select
                v-model="form.pointOid"
                size="small"
                style="width: 240px"
              >
                <el-option
                  v-for="data in pointMap"
                  :label="data.name"
                  :value="data.label"
                />
              </el-select>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>外设：</b></td>
            <td colspan="3">
              <el-form-item prop="peripheral">
                <el-checkbox-group v-model="peripheralList" @change="handleChange">
                  <el-checkbox v-for="data in peripheralMap" :label="data.label" :key="data.name">{{data.name}}
                  </el-checkbox>
                </el-checkbox-group>
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
  page,
  saveTerminal,
  getOne,
  del,
  getLonLat
} from '@/api/terminal/terminalInfo'
import { queryDistrictSimpleTree } from '@/api/sys/district'
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  components: {Treeselect},
  name: 'Terminal',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      title: '',
      // 数据表格
      terminalInfoList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        terminalCode: '',
        districtOid: null,
        pointOid: '',
        unit: ''
      },
      //新增修改标识
      openInit: false,
      //查看标识
      openView:false,
      //弹框数据
      form: {
        //终端编号
        terminalCode:'',
        //终端类型
        terminalType:'',
        //区划
        districtOid:'',
        //点位
        pointOid:'',
        //外设
        peripheral:''
      },
      //区划数据
      districtMap: [],
      //点位数据
      pointMap: [],
      //终端类型
      terminalTypeMap: [{
        name: 'A型终端',
        label: 'A'
      },
        {
          name: 'B型终端',
          label: 'B'
        },
        {
          name: 'C型终端',
          label: 'C'
        }],
      //外设
      peripheralList: [],
      peripheralMap: [{
        name: '文件柜',
        label: 'W'
      },
        {
          name: '高拍仪',
          label: 'G'
        },
        {
          name: '打印机',
          label: 'D'
        }],
      //查看单个数据
      terminalInfo:{},
      rules: {}
    }
  },
  watch: {},
  created() {
    //获取所属区划
    this.getDistricts()
    //获取所属点位
    this.getPoint()
    this.getList()
  },
  methods: {
    //获取区划
    getDistricts() {
      queryDistrictSimpleTree().then(response => {
        this.districtMap = response.data;
      })
    },

    //获取所属点位
    getPoint() {
      //这边专门写一个只有oid和name的数据，数据格式类似外设类型这种
      getLonLat().then(response => {
        console.log("=====点位数据=====")
        console.log(response.data)
        this.pointMap = response.data;
      })
    },

    //选择外设
    handleChange(value) {
      let _that = this
      _that.form.peripheral = value.join(',')
    },

    //新增或修改提交
    submitForm: function() {
      //因为rule很难用，直接动手校验吧。。。希望后来人可以修改成rule
      //终端编号
      if (this.form.terminalCode == undefined || this.form.terminalCode == '') {
        this.$alert('请输入终端编号')
        return false
      }
      //终端类型
      if (this.form.terminalType == undefined || this.form.terminalType == '') {
        this.$alert('请选择终端类型')
        return false
      }
      //区划
      if (this.form.districtOid == undefined || this.form.districtOid == '') {
        this.$alert('请选择区划')
        return false
      }
      //点位
      if (this.form.pointOid == undefined || this.form.pointOid == '') {
        this.$alert('请选择点位')
        return false
      }
      //外设 -- 不一定必填吧
      if(this.form.peripheral == undefined || this.form.peripheral == ''){
        this.$alert("至少选择一个外设")
        return false
      }
      saveTerminal(this.form).then(response => {
        if (response.code === 200) {
          this.msgSuccess('保存成功')
          this.openInit = false
          this.getList()
        }
      })

    },

    //查询终端列表
    getList() {
      this.loading = true
      page(this.queryParams).then(response => {
        this.terminalInfoList = response.data.data
        this.total = response.data.total
        this.loading = false
      })
    },

    //搜索
    handleQuery() {
      this.queryParams.pageNum = 1
      this.queryParams.pageSize = 10
      this.getList()
    },

    //新增或修改弹框
    handleInit(row) {
      const id = row.id
      if (id === undefined) {
        this.form = {}
        this.openInit = true
        this.title = '新增'
      } else {
        getOne(id).then(response => {
          this.form = response.data
          this.peripheralList = this.form.peripheral.split(',')
          this.openInit = true
          this.title = '修改'
        })
      }
    },

    //重置
    resetQuery() {
      this.queryParams = {}
      this.handleQuery()
    },

    //删除
    handleDelete(row) {
      const id = row.id
      this.$confirm(
        '是否确认删除?',
        '警告',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
        .then(function() {
          return del(id)
        })
        .then(() => {
          this.getList()
          this.msgSuccess('删除成功')
        })
        .catch(function() {
        })
    },

    //查看弹框
    handleView(row) {
      const id = row.id
      getOne(id).then(response => {
        this.terminalInfo = response.data
        this.peripheralList = this.terminalInfo.peripheral.split(',')
        this.openView = true
        this.title = '查看终端信息'
      })
    },

    //取消
    cancel() {
      this.openInit = false
      this.reset()
    },
    reset() {
      this.form={}
      this.peripheralList=[]
      this.resetForm('form')
    },
  }
}
</script>
<style lang="scss" scoped>
.treeselect {
  width: 200px;
}
</style>
