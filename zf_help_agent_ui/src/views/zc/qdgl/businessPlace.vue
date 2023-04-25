<template>
  <div>
    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['qdgl:place:addUpdate']"
          >新增
        </el-button>
      </el-col>
    </el-row>
    <el-table v-loading="loading" :data="placeList" border>
      <el-table-column label="序号" align="center" type="index" />
      <el-table-column
        label="办事地点名称"
        align="center"
        prop="locationName"
      />
      <el-table-column
        label="办理时间"
        align="center"
        prop="acceptDate"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="办理地点地址"
        align="center"
        prop="locationAddr"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="乘车路线"
        align="center"
        prop="busRoute"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="咨询电话"
        align="center"
        prop="phone"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="操作"
        align="center"
        width="260"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['qdgl:place:view']"
            >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['qdgl:place:addUpdate']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
            v-hasPermi="['qdgl:place:delete']"
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

    <!-- 信息新增修改 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="addUpdateFlag"
      v-if="addUpdateFlag"
      :title="addUpdateTitle"
      width="1100px"
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <tr>
            <td width="15%"><i class="require">*</i><b>办事地点名称：</b></td>
            <td width="85%">
              <el-form-item prop="locationName">
                <el-input
                  v-model.trim="form.locationName"
                  placeholder="请输入办事地点名称"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td width="15%"><i class="require">*</i><b>办事时间：</b></td>
            <td width="85%">
              <el-form-item prop="acceptDate">
                <el-input
                  v-model.trim="form.acceptDate"
                  placeholder="请输入办事时间"
                  maxlength="500"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td width="15%"><i class="require">*</i><b>办事地点地址：</b></td>
            <td width="85%">
              <el-form-item prop="locationAddr">
                <el-input
                  v-model.trim="form.locationAddr"
                  placeholder="请输入办事地点地址"
                  maxlength="500"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td width="15%"><b>乘车路线：</b></td>
            <td width="85%">
              <el-form-item prop="busRoute">
                <el-input
                  type="textarea"
                  v-model.trim="form.busRoute"
                  placeholder="请输入乘车路线"
                  maxlength="400"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td width="15%"><i class="require">*</i><b>咨询电话：</b></td>
            <td width="85%">
              <el-form-item prop="phone">
                <el-input
                  v-model.trim="form.phone"
                  placeholder="请输入咨询电话"
                  maxlength="20"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" style="text-align: center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看用户配置对话框 -->
    <el-dialog
    v-dialog-drag
      title="查看办事地点信息"
      :visible.sync="placeView"
      v-if="placeView"
      :close-on-click-modal="false"
      width="1100px"
      append-to-body
    >

        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td width="15%"><b>办事地点名称：</b></td>
            <td width="85%">
              {{ form.locationName }}
            </td>
          </tr>
          <tr>
            <td width="15%"><b>办事时间：</b></td>
            <td width="85%">
              {{ form.acceptDate }}
            </td>
          </tr>
          <tr>
            <td width="15%"><b>办事地点地址：</b></td>
            <td width="85%">
              {{ form.locationAddr }}
            </td>
          </tr>
          <tr>
            <td width="15%"><b>乘车路线：</b></td>
            <td width="85%">
              {{ form.busRoute }}
            </td>
          </tr>
          <tr>
            <td width="15%"><b>咨询电话：</b></td>
            <td width="85%">
              {{ form.phone }}
            </td>
          </tr>
        </table>

      <div slot="footer"  style="text-align: center">
        <el-button @click="viewCancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { page, getSxServiceExtendByServiceOid, save, detail, del } from '@/api/zc/qdgl/businessPlace'

export default {
  name: 'BusinessPlace',
  components: {},
  props: ['serviceOid'],
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      placeList: [],
      addUpdateTitle: "",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        extendOid: "",
      },
      addUpdateFlag: false,
      placeView: false,
      form: {
        id: "",
        serviceLocationOid: "",
        handleLocationOid: "",
        locationName: "",
        acceptDate: "",
        locationAddr: "",
        busRoute: "",
        phone: "",
        extendOid: "",
        delFlag: "",
      },
      rules: {
        locationName: [
          { required: true, message: '办事地点名称不能为空', trigger: 'blur' }
        ],
        acceptDate: [
          { required: true, message: '办事时间不能为空', trigger: 'blur' }
        ],
        locationAddr: [
          { required: true, message: '办事地点地址不能为空', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '咨询电话不能为空', trigger: 'blur' }
        ]
      },
    };
  },
  created () {
    this.getList()
  },
  methods: {
    /** 查询办事地点列表 */
    getList () {
      getSxServiceExtendByServiceOid(this.serviceOid).then(res => {
        if (res.code == 200) {
          this.queryParams.extendOid = res.data.extendOid;
          this.loading = true
          let that = this;
          page(this.queryParams).then(response => {
            this.placeList = response.data.data;
            this.total = response.data.total;
            this.loading = false;
          }
          ).catch(function () { that.loading = false; });
        }
      })
    },
    handleAdd () {
      this.addUpdateTitle = "新增办事地点";
      this.addUpdateFlag = true;
      this.form.id = "";
      this.form.serviceLocationOid = "";
      this.form.handleLocationOid = "";
      this.form.locationName = "";
      this.form.locationAddr = "";
      this.form.acceptDate = "";
      this.form.busRoute = "";
      this.form.delFlag = "";
      this.form.phone = "";
    },
    handleUpdate (row) {
      this.addUpdateTitle = "修改办事地点";
      this.addUpdateFlag = true;
      detail(row.id).then(res => {
        if (res.code == 200) {
          this.form.id = res.data.id;
          this.form.serviceLocationOid = res.data.serviceLocationOid;
          this.form.handleLocationOid = res.data.handleLocationOid;
          this.form.locationName = res.data.locationName;
          this.form.locationAddr = res.data.locationAddr;
          this.form.acceptDate = res.data.acceptDate;
          this.form.busRoute = res.data.busRoute;
          this.form.delFlag = res.data.delFlag;
          this.form.phone = res.data.phone;
        }
      })
    },
    submitForm () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.extendOid = this.queryParams.extendOid;
          save(this.form).then(response => {
            if (response.code == 200) {
              this.$message.success("保存成功");
              this.addUpdateFlag = false;
              this.getList();
            } else {
              this.$message.error("保存失败！");
            }
          }).catch(function () { this.addUpdateFlag = false; });
        }
      })
    },
    cancel () {
      this.addUpdateFlag = false;
      this.form.id = "";
      this.form.serviceLocationOid = "";
      this.form.handleLocationOid = "";
      this.form.locationName = "";
      this.form.locationAddr = "";
      this.form.acceptDate = "";
      this.form.busRoute = "";
      this.form.delFlag = "";
    },
    handleView (row) {
      detail(row.id).then(res => {
        if (res.code == 200) {
          this.placeView = true;
          this.form.locationName = res.data.locationName;
          this.form.locationAddr = res.data.locationAddr;
          this.form.acceptDate = res.data.acceptDate;
          this.form.busRoute = res.data.busRoute;
          this.form.phone = res.data.phone;
        } else {
          this.placeView = false;
        }
      })
    },
    viewCancel () {
      this.placeView = false;
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      const name = row.locationName
      this.$confirm('是否确认删除"' + name + '"的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function () {
        return del(row.id)
      }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
      }).catch(function () {
      })
    },
  },
}
</script>
