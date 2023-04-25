<template>
  <div class="app-container typeApp">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="100px"
    >
      <el-form-item label="点位名称" prop="name">
        <el-input
          v-model.trim="queryParams.name"
          placeholder="请输入点位名称"
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
      :data="pointInfoList"
      tooltip-effect="dark"
      style="width: 100%"
    >
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="点位名称"
        align="center"
        prop="name"
        show-overflow-tooltip
      />
      <el-table-column
        label="所属区划"
        align="center"
        prop="districtName"
        show-overflow-tooltip
      />
      <el-table-column
        label="点位地址"
        align="center"
        prop="address"
        show-overflow-tooltip
      />
      <el-table-column
        label="点位坐标"
        align="center"
        prop="lonLat"
        show-overflow-tooltip
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
        :model="pointInfo"
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
            <td><i class="require">*</i><b>点位名称：</b></td>
            <td>
              {{ pointInfo.name }}
            </td>
            <td><i class="require">*</i><b>所属区划：</b></td>
            <td>
              {{ pointInfo.districtName }}
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>点位地址：</b></td>
            <td colspan="3">
              {{ pointInfo.address }}
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>点位坐标：</b></td>
            <td>
              {{ pointInfo.lonLat }}
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
            <td><i class="require">*</i><b>点位名称：</b></td>
            <td>
              <el-form-item prop="terminalCode">
                <el-input
                  v-model.trim="form.name"
                  placeholder="请输入点位名称"
                  maxlength="64"
                  show-word-limit
                />
              </el-form-item>
            </td>
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
          </tr>
          <tr>
            <td><i class="require">*</i><b>点位地址：</b></td>
            <td colspan="3">
              <el-form-item prop="address">
                <el-input
                  v-model.trim="form.address"
                  placeholder="请输入点位地址"
                  maxlength="64"
                  show-word-limit
                />
              </el-form-item>
              <el-button
                type="default"
                icon="el-icon-plus"
                size="mini"
                @click="getCoordinate"
              >查询坐标</el-button>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>点位坐标：</b></td>
            <td>
              <el-form-item prop="lonLat">
                <el-input
                  disabled
                  v-model.trim="lonLat"
                  maxlength="64"
                  show-word-limit
                />
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
  getPoint,
  del,
  getCoordinate,
  saveOrUpdatePoint
} from '@/api/terminal/point'
import { queryDistrictSimpleTree } from '@/api/sys/district'
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  components: {Treeselect},
  name: 'Point',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      title: '',
      // 数据表格
      pointInfoList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: '',
        districtOid: null
      },
      //新增修改标识
      openInit: false,
      //查看标识
      openView:false,
      //弹框数据
      form: {
        //点位名称
        name:'',
        //地址
        address:'',
        //区划
        districtOid:'',
        //坐标
        lonLat:''
      },
      lonLat:'',
      //区划数据
      districtMap: [],
      //查看单个数据
      pointInfo:{},
      rules: {}
    }
  },
  watch: {},
  created() {
    //获取所属区划
    this.getDistricts()
    this.getList()
  },
  methods: {
    //获取区划
    getDistricts() {
      queryDistrictSimpleTree().then(response => {
        this.districtMap = response.data;
      })
    },

    //新增或修改提交
    submitForm: function() {
      //因为rule很难用，直接动手校验吧。。。希望后来人可以修改成rule
      //点位名称
      if (this.form.name == undefined || this.form.name == '') {
        this.$alert('请输入点位名称')
        return false
      }
      //区划
      if (this.form.districtOid == undefined || this.form.districtOid == '') {
        this.$alert('请选择区划')
        return false
      }
      //地址
      if (this.form.address == undefined || this.form.address == '') {
        this.$alert('请输入点位地址')
        return false
      }
      //坐标
      if (this.form.lonLat == undefined || this.form.lonLat == '') {
        this.$alert('请获取点位坐标')
        return false
      }
      saveOrUpdatePoint(this.form).then(response => {
        if (response.code === 200) {
          this.msgSuccess('保存成功')
          this.openInit = false
          this.getList()
        }
      })

    },

    //根据地址获取坐标
    getCoordinate() {
      if (this.form.address == undefined || this.form.address == '') {
        this.$alert('请输入点位地址')
        return false
      }
      //这边的操作，获取到坐标并回填到坐标input
      getCoordinate(this.form.address).then(response => {
        this.form.lonLat = response.data
        this.lonLat = response.data
      })
    },

    //查询点位列表
    getList() {
      this.loading = true
      page(this.queryParams).then(response => {
        this.pointInfoList = response.data.data
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
        getPoint(id).then(response => {
          this.form = response.data
          this.lonLat = response.data.lonLat
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
      getPoint(id).then(response => {
        this.pointInfo = response.data
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
      this.lonLat=''
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
