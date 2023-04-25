/** * @Author: xldong */
<template>
  <div>
    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit"
          v-hasPermi="['service:rel:init']"
          >新增</el-button
        >
        <el-button
          type="default"
          icon="iconfont zfsoft-shanchu"
          size="mini"
          @click="handleDel"
          v-hasPermi="['service:rel:del']"
          >批量删除</el-button
        >
      </el-col>
    </el-row>

    <el-table
      ref="multipleTable"
      tooltip-effect="dark"
      @selection-change="handleSelectionChange"
      border
      v-loading="loading"
      :data="optionRelList"
      border
      :fit="true"
    >
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="选项标题【选项值】"
        align="center"
        prop="optionValueList"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <span v-for="(optionValue, ind) in scope.row.optionValueList">
            {{ ind + 1 }}、{{ optionValue }}<br />
          </span>
        </template>
      </el-table-column>
      <el-table-column
        label="选项标题"
        align="center"
        prop="optionTitleList"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <span v-for="(optionTitle, indTitle) in scope.row.optionTitleList">
            {{ indTitle + 1 }}、{{ optionTitle }}<br />
          </span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        width="250"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['service:rel:view']"
            >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            v-hasPermi="['service:rel:init']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDel(scope.row)"
            v-hasPermi="['service:rel:del']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <pagination
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 新增修改 -->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      title="关系配置"
      :visible.sync="openInit"
      v-if="openInit"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <el-table :data="titleAndValList" tooltip-effect="dark" border>
        <el-table-column label="序号" width="55" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="sxServiceOptionVals"
          align="left"
          width="400"
          label="选项值"
          show-overflow-tooltip
        >
          <template slot-scope="scopeVal">
            <span v-if="scopeVal.row.moreStatus == 0">
              <el-radio-group
                v-model="selectVals[scopeVal.$index]"
                @change="handleChecked(scopeVal.$index, $event)"
              >
                <el-radio
                  v-for="(item, rInd) in scopeVal.row.sxServiceOptionVals"
                  :key="item.oid"
                  :label="item.oid"
                  >{{ item.name }}</el-radio
                >
              </el-radio-group>
            </span>
            <span v-if="scopeVal.row.moreStatus == 1">
              <el-checkbox-group
                v-model="selectCheckVals"
                @change="handleBoxChecked"
              >
                <el-checkbox
                  v-for="(item, cInd) in scopeVal.row.sxServiceOptionVals"
                  :key="item.oid"
                  :label="item.oid"
                  >{{ item.name }}</el-checkbox
                >
              </el-checkbox-group>
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="name" align="center" label="标题">
          <template slot-scope="scope">
            <el-checkbox
              v-model="selectTitles"
              :label="scope.row.oid"
              :key="scope.row.oid"
              >{{ scope.row.name }}</el-checkbox
            >
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" style="text-align:center">
        <el-button type="primary" @click="batchChoose">确 定</el-button>
        <el-button @click="cancelDig">关闭</el-button>
      </div>
    </el-dialog>

    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      title="查看配置信息"
      :visible.sync="viewRel"
      v-if="viewRel"
      width="900px"
      height="600px"
      scrollbar
      append-to-body
    >
      <el-table :data="titleAndValList" tooltip-effect="dark" border>
        <el-table-column label="序号" width="55" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="sxServiceOptionVals"
          align="left"
          width="400"
          label="选项值"
          show-overflow-tooltip
        >
          <template slot-scope="scopeVal">
            <span v-if="scopeVal.row.moreStatus == 0">
              <el-radio-group
                v-model="selectVals[scopeVal.$index]"
                @change="handleChecked(scopeVal.$index, $event)"
              >
                <el-radio
                  v-for="(item, rInd) in scopeVal.row.sxServiceOptionVals"
                  :key="item.oid"
                  :label="item.oid"
                  >{{ item.name }}</el-radio
                >
              </el-radio-group>
            </span>
            <span v-if="scopeVal.row.moreStatus == 1">
              <el-checkbox-group
                v-model="selectCheckVals"
                @change="handleBoxChecked"
              >
                <el-checkbox
                  v-for="(item, cInd) in scopeVal.row.sxServiceOptionVals"
                  :key="item.oid"
                  :label="item.oid"
                  >{{ item.name }}</el-checkbox
                >
              </el-checkbox-group>
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="name" align="center" label="标题">
          <template slot-scope="scope">
            <el-checkbox
              v-model="selectTitles"
              :label="scope.row.oid"
              :key="scope.row.oid"
              >{{ scope.row.name }}</el-checkbox
            >
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" style="text-align:center">
        <el-button @click="viewRel = false">
          关闭
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  relationList,
  pageTitleAndOption,
  getServiceOptionRelByOid,
  saveOrUpdateServiceOptionRel,
  delBatchRel
} from "@/api/zc/qdgl/sxService.js";
export default {
  name: "RelationList",
  props: ["serviceOid"],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      form: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        serviceOid: this.serviceOid
      },
      optionRelList: [],
      openInit: false,
      titleAndValList: [], //标题选项值
      allSelectRel: [],
      selectTitles: [],
      selectVals: [],
      selectCheckVals: [],
      relOid: "",
      multipleSelection: [],
      relViewInfo: {},
      viewRel: false
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      relationList(this.queryParams).then(response => {
        this.optionRelList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
      this.loading = false;
    },
    //标题选项列表
    handleInit(row) {
      this.relOid = row.oid;
      this.selectTitles = [];
      this.selectVals = [];
      this.selectCheckVals = [];
      let _that = this;
      pageTitleAndOption(_that.serviceOid).then(response => {
        _that.titleAndValList = response.data;
        if (_that.titleAndValList && _that.titleAndValList.length > 0) {
          //如果relOid不为空，查询关系
          if (_that.relOid) {
            getServiceOptionRelByOid(_that.relOid).then(res => {
              _that.allSelectRel = res.data;
              _that.selectTitles = _that.allSelectRel.titleOids.split(",");
              let valuOids = _that.allSelectRel.valueOids.split(",");
              //循环查询放入数组
              _that.titleAndValList.forEach((title, index) => {
                let vals = title.sxServiceOptionVals;
                vals.forEach((ite, zindex) => {
                  if (valuOids.indexOf(ite.oid) > -1) {
                    if (title.moreStatus == 0) {
                      //单选
                      _that.$set(_that.selectVals, index, ite.oid);
                    } else {
                      _that.selectCheckVals.push(ite.oid);
                    }
                  }
                });
              });
            });
          }
        }
      });
      this.openInit = true;
    },
    //获取所有选择的名称
    handleChecked(index, val) {
      /*debugger
      console.log('----'+val+"---"+this.selectVals)*/
    },
    handleBoxChecked(val) {
      /*console.log('----'+val+"---"+this.selectCheckVals)*/
    },
    //保存选中的结果
    handleSelectionChange(val) {
      this.multipleSelection = val.map(ite => ite.oid);
      //this.multipleSelection = val;
    },
    /** 删除按钮操作 */
    handleDel(row) {
      let _that = this;
      if (row.oid) {
        this.multipleSelection = [];
        this.multipleSelection.push(row.oid);
      }
      if (!this.multipleSelection || this.multipleSelection.length == 0) {
        this.$message.error("请选择要删除的数据!");
        return false;
      }
      let data = this.multipleSelection.join(",");
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          delBatchRel(data).then(res => {
            _that.$message.success("删除成功!");
            _that.getList();
          });
        })
        .catch(function() {});
    },

    //保存
    batchChoose() {
      if (this.selectTitles && this.selectTitles.length > 0) {
        this.form.titleOids = this.selectTitles.join(",");
      }
      let values = [];
      if (this.selectVals && this.selectVals.length > 0) {
        values = this.selectVals.filter(function(ite) {
          return ite != null && ite != "";
        });
      }

      if (this.selectCheckVals && this.selectCheckVals.length > 0) {
        this.selectCheckVals.forEach(val => {
          values.push(val);
        });
      }
      this.form.valueOids = values.join(",");
      this.form.serviceOid = this.serviceOid;
      this.form.oid = this.relOid;
      saveOrUpdateServiceOptionRel(this.form).then(response => {
        if (!response.data) {
          this.$message.success("保存成功");
          this.openInit = false;
          this.getList();
        } else {
          this.$message.error(response.data);
        }
      });
    },
    cancelDig() {
      this.openInit = false;
    },

    handleView(row) {
      this.relOid = row.oid;
      this.selectTitles = [];
      this.selectVals = [];
      this.selectCheckVals = [];
      let _that = this;
      pageTitleAndOption(_that.serviceOid).then(response => {
        _that.titleAndValList = response.data;
        if (_that.titleAndValList && _that.titleAndValList.length > 0) {
          //如果relOid不为空，查询关系
          if (_that.relOid) {
            getServiceOptionRelByOid(_that.relOid).then(res => {
              _that.allSelectRel = res.data;
              _that.selectTitles = _that.allSelectRel.titleOids.split(",");
              let valuOids = _that.allSelectRel.valueOids.split(",");
              //循环查询放入数组
              _that.titleAndValList.forEach((title, index) => {
                let vals = title.sxServiceOptionVals;
                vals.forEach((ite, zindex) => {
                  if (valuOids.indexOf(ite.oid) > -1) {
                    if (title.moreStatus == 0) {
                      //单选
                      _that.$set(_that.selectVals, index, ite.oid);
                    } else {
                      _that.selectCheckVals.push(ite.oid);
                    }
                  }
                });
              });
            });
          }
        }
      });
      this.relViewInfo = row;
      this.viewRel = true;
    }
  }
};
</script>

<style lang="scss" scoped>
.el-radio {
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  margin: 10px;
}
.el-checkbox {
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  margin: 10px;
}
</style>
