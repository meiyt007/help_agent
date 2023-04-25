<template>
  <div class="pack-list">
    <h3>请选择需要打包的事项</h3>
    <div style="text-align: center">
      <el-transfer
        style="text-align: left; display: inline-block"
        :titles="['全选', '全选']"
        :filterable="true"
        filter-placeholder="请输入搜索内容"
        v-model="transferPlaceholder"
        :data="transferData"
        @right-check-change="checkChangeHandle"
        @change="handleChange"
      >
      </el-transfer>
    </div>
    <div class="pack-right">
      <h4><span class="pack-right--require">打包说明</span></h4>
      <el-input
        type="textarea"
        :autosize="{ minRows: 2, maxRows: 5 }"
        maxlength="500"
        show-word-limit
        placeholder="请输入打包说明"
        v-model="textarea"
      >
      </el-input>
    </div>
  </div>
</template>

<script>
import {
  getDictApi,
  commpressPicToZip
} from "@/api/zc/businessManagement/dict";
export default {
  name: "packList",
  data () {
    return {
      dictList: [],
      checked: false,
      textarea: "",
      serviceIdList: [],
      selection: [],
      transferData: [],
      transferPlaceholder: [],
      loading: false
    };
  },
  props: {},
  created () {
    this.getAllDictList();
  },
  methods: {
    //获取全部事项
    getAllDictList () {
      getDictApi().then(res => {
        if (res.code == 200) {
          this.dictList = res.data;
          this.dictList.forEach((item, index) => {
            this.transferData.push({
              label: item.serviceName,
              key: item.serviceOid
            });
          });
        }
      });
    },
    //右侧列表选中
    checkChangeHandle (idGroup) {
      this.serviceIdList = idGroup;
    },

    handleChange (idGroup, dire) {
      if (dire === 'right') {
        this.$nextTick(() => {
          this.serviceIdList = idGroup;
        })
      }
    },

    //打包数据集
    packZipHandle () {
      if (!this.textarea) return this.$message.warning('请输入打包说明');
      this.loading = true;
      commpressPicToZip({
        serviceIds: this.serviceIdList,
        publishExplain: this.textarea
      })
        .then(res => {
          if (res.code == 200) {
            this.$message.success("打包成功");
            this.loading = false;
            this.$emit("packZipEmit");
          }
        })
        .catch(err => {
          this.loading = false;
        });
    }
  }
};
</script>

<style lang="less" scoped>
.pack-list {
  padding: 20px;
  h3 {
    font-size: 14px;
    color: #2a344c;
    position: relative;
    margin: 0 0 30px 0;
    &::before {
      position: absolute;
      content: '';
      left: -10px;
      top: 0px;
      content: '';
      width: 3px;
      height: 15px;
      background-color: #2e7dff;
    }
  }
  .pack-right {
    border: 1px solid #e0e6f0;
    border-radius: 3px;
    margin-top: 20px;
    h4 {
      margin: 0;
      width: 100%;
      text-align: center;
      color: #121b2f;
      background: #edf0f5;
      height: 41px;
      line-height: 41px;
    }
    ::v-deep .el-textarea__inner {
      border: none;
      border-radius: 0;
      // background-color: #fff !important;
    }
    ::v-deep.el-input__count {
      height: 30px !important;
      line-height: 30px !important;
      background-color: #fff !important;
    }
  }
  .btn-pack {
    display: block;
    margin: 30px auto 0 auto;
  }
  ::v-deep .el-transfer-panel {
    width: 400px;
  }
  ::v-deep .el-transfer__buttons {
    padding: 0px 40px;
  }
}

/* 去掉全选按钮 */
::v-deep .el-table__header,
::v-deep .el-table__body {
  width: 100% !important;
}

::v-deep.el-table .disabledCheck .cell .el-checkbox__inner {
  display: none !important;
}

::v-deep.el-table .disabledCheck .cell::before {
  content: '操作';
  text-align: center;
  line-height: 37px;
}
::v-deep .el-table .el-table__header-wrapper th,
.el-table .el-table__fixed-header-wrapper th {
  padding: 2px 0;
}

.pack-right--require {
  position: relative;
  &::before {
    content: '*';
    position: absolute;
    right: -8px;
    top: -15px;
    font-size: 18px;
    color: red;
  }
}
</style>
