<template>
  <div class="app-container data-dictionary">
    <el-row :gutter="20" style="height: 100%">
      <!--数字字段树数据-->
      <el-col :span="4" :xs="24" class="app-left">
        <!-- <div class="tree-title">数据字典管理</div> -->
        <div class="head-container">
          <el-input
            v-model="dictName"
            placeholder="请输入事项名称"
            clearable
            size="small"
            prefix-icon="iconfont zfsoft-chakan"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container" style="height: calc(100% - 52px)">
          <el-scrollbar style="height: 100%" v-loading="loading">
            <el-tree
              :load="loadNode"
              lazy
              :props="defaultProps"
              :expand-on-click-node="true"
              :highlight-current="true"
              :filter-node-method="filterNode"
              ref="tree"
              accordion
              node-key="id"
              :current-node-key="nodeKey"
              :default-expanded-keys="[ArrData]"
              @node-click="handleNodeClick"
            >
              <span class="custom-tree-node" slot-scope="{ node }">
                <el-tooltip
                  v-if="node.label && node.label.length > stringLength"
                  class="item"
                  effect="dark"
                  :content="node.label"
                  placement="right-start"
                >
                  <span style="font-size: 14px"
                    >{{ node.label | ellipsis(stringLength) }}
                  </span>
                </el-tooltip>
                <span
                  v-if="node.label && node.label.length <= stringLength"
                  style="font-size: 14px"
                  >{{ node.label }}
                </span>
              </span>
            </el-tree>
          </el-scrollbar>
        </div>
      </el-col>
      <!--数字字典数据-->
      <el-col :span="20" :xs="24" class="app-right">
        <div class="data-right">
          <div class="tip-header">
            <div class="tip-title">
              <span class="tip-item">提示：</span
              >如材料为空表，请点击材料左上角<img
                src="@/assets/image/blank-icon.png"
                alt=""
                srcset=""
              />区域后标记为<img
                src="@/assets/image/blank-icon-active.png"
                alt=""
                srcset=""
              />。
            </div>
            <el-button
              type="primary"
              icon="el-icon-tickets"
              class="jar-btn"
              size="mini"
              @click="packHandle"
              >打包数据集</el-button
            >
          </div>
          <!-- 高拍仪 -->
          <gpy-uplaod
            :refinedMaterialId="dictParamsId.refinedMaterialId"
            :dictParamsId="dictParamsId"
            :allMaterialList="allMaterialList"
          ></gpy-uplaod>
        </div>
      </el-col>
    </el-row>
    <!-- 打包数据集 -->
    <el-dialog
      v-dialog-drag
      title="打包数据集"
      :visible.sync="packDialogVisible"
      width="1100px"
      height="700px"
      scrollbar
      v-if="packDialogVisible"
    >
      <pack-list ref="package" @packZipEmit="packZipHandle" />
      <div slot="footer" class="zf-text-center">
        <el-button
          type="primary"
          size="medium"
          class="btn-pack"
          @click="
            () => {
              $refs.package.packZipHandle()
            }
          "
        >
          打包
        </el-button>
        <el-button @click="packDialogVisible = false"> 关闭 </el-button>
      </div>
    </el-dialog>
    <!-- 数据集列表 -->
    <el-dialog
      v-dialog-drag
      title="数据集列表"
      :visible.sync="dataListDialogVisible"
      width="1100px"
      height="700px"
      scrollbar
      v-if="dataListDialogVisible"
    >
      <data-list />
      <div slot="footer" class="zf-text-center">
        <el-button
          type="primary"
          size="medium"
          class="btn-pack"
          @click="packListHandle"
        >
          打包数据集
        </el-button>
        <el-button @click="dataListDialogVisible = false"> 关闭 </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  init,
  save,
  del,
  getOne,
  page,
  able,
  listExp,
  queryDictTree
} from "@/api/sys/dict";
import {
  querDictApi,
  getDictApi,
  getDictListApi,
  allMaterialImage
} from "@/api/zc/businessManagement/dict";
import { validIntNoZero } from "@/utils/validate";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import GpyUplaod from "./components/gpy-upload.vue";
import PackList from "./components/pack-list.vue";
import DataList from "./components/data-list.vue";
export default {
  components: { Treeselect, GpyUplaod, PackList, DataList },
  name: "Dict",
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      dictList: [],
      //查询字典名称参数
      dictName: "",
      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      //消息
      ableMap: { 1: "启用", 0: "禁用" },
      // 数字字典树选项
      dictOptions: undefined,
      ArrData: '',
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: "",
        code: "",
        parentOid: ""
      },
      defaultProps: {
        // children: [],
        label: "serviceName",
        isLeaf: "leaf"
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "字典名称不能为空", trigger: "blur" }
        ],
        code: [
          { required: true, message: "字典代码不能为空", trigger: "blur" }
        ],
        sort: [
          { required: true, message: "排序号不能为空", trigger: "blur" },
          { validator: validIntNoZero, trigger: "blur" }
        ]
      },
      //事项列表数据
      dictList: [],
      //细化材料列表
      dictItemList: [],

      dictParamsId: {
        //事项id
        serviceId: "",
        //细化材料列表id
        refinedMaterialId: "",
        //事项材料id
        materialId: "",
        //证照名称
        licenceName: "",
        //证照id
        licenceOid: ""
      },
      //全部细化材料上传图片列表
      allMaterialList: [],
      packDialogVisible: false,
      dataListDialogVisible: false,
      nodeKey: "",
      screen: 1920,
    };
  },
  watch: {
    // 根据名称筛选部门树
    dictName (val) {
      if (null != val) {
        val = val.replace(/^\s+|\s+$/g, "");
      }
      this.$refs.tree.filter(val);
    }

    //选中的key
    // nodeKey(val) {
    //   if (val) {
    //     this.$nextTick(() => {
    //       this.$refs.tree.setCurrentKey(val);
    //       this.$nextTick(() => {
    //         document
    //           .querySelector(".el-tree-node__children .el-tree-node")
    //           .firstChild.click();
    //       });
    //     });
    //   }
    // },
  },
  computed: {
    stringLength () {
      return this.screen === 1280 ? 8 : 18;
    }
  },
  mounted () {
    // this.getDictTree();

    this.$nextTick(() => {
      this.screen = screen.width;
    });
  },
  methods: {
    /* 异步树加载 */
    loadNode (node, resolve) {
      //一级节点
      if (node.level == 0) {
        return this.requestTree(resolve);
      }
      //其余节点
      if (node.level >= 1) {
        return this.getIndexTree(node, resolve);
      }
    },
    /* 异步加载叶子节点 */
    getIndexTree (node, resolve) {
      getDictListApi({ serviceId: node.data.serviceOid }).then(res => {
        if (res.code == 200 && res.data.length) {
          let data = res.data;
          this.nodeKey = data[0].oid;
          data.forEach(value => {
            value.serviceName = (value?.refinedMaterialName ?? '') + (value?.materialName ? '【' + value.materialName + '】' : '');
            value.leaf = true;
          });
          this.dictItemList = data;
          this.ArrData = '';
          resolve(data);
        }
      });
    },
    /* 初始化加载树一级节点 */
    requestTree (resolve) {
      getDictApi()
        .then(res => {
          this.loading = false;
          if (res.code == 200) {
            this.dictList = res.data;
            // console.log(this.dictList);
            this.dictList.forEach(value => {
              if (value.refinedMaterialNum == "0") {
                value.leaf = true;
              } else {
                value.leaf = false;
              }
            });
            if (this.dictList[0].refinedMaterialNum == "0") {
              this.$message.info("暂无细化材料");
            } else {
              this.defaultFrist(this.dictList);
            }
            let data = this.dictList;
            resolve(data);
          }
        })
        .catch(err => { this.loading = false; });
    },

    /** 获取数字字典树 */
    getDictTree (parentOid) {
      queryDictTree(parentOid)
        .then(response => {
          this.dictOptions = response.data;
          // console.log(response);
          // this.defaultFrist(response.data);
        })
        .then(res => {
          let id = "";
          if (!this.queryParams.parentOid) {
            id = this.dictOptions[0].id;
            this.$nextTick(function () {
              this.$refs.tree.setCurrentKey(id);
              this.queryParams.parentOid = id;
              // this.getList();
            });
          }
        });
    },
    // 筛选节点
    filterNode (value, data, node) {
      if (!value) {
        node.expanded = false;
        return true;
      }
      // return data.name.indexOf(value) !== -1;
      return this.checkBelongToChooseNode(value, data, node);
    },
    // 判断传入的节点是不是选中节点的子节点
    checkBelongToChooseNode (value, data, node) {
      if (data.serviceName.indexOf(value) !== -1) {
        return true;
      }
      const level = node.level;
      // 如果传入的节点本身就是一级节点就不用校验了
      if (level === 1) {
        return false;
      }
      // 先取当前节点的父节点
      let parentData = node.parent;
      // 遍历当前节点的父节点
      let index = 0;
      while (index < level - 1) {
        // 如果匹配到直接返回
        if (parentData.data.serviceName.indexOf(value) != -1) {
          return true;
        }
        // 否则的话再往上一层做匹配
        parentData = parentData.parent;
        index++;
      }
      // 没匹配到返回false
      return false;
    },

    // 节点单击事件
    handleNodeClick (data, e, self) {
      // this.queryParams.parentOid = data.id;
      // this.getList();
      if (data.refinedMaterialNum == 0) {
        return this.$message.info("暂无细化材料");
      }
      if (data.leaf && self.node.level > 1) {
        this.dictParamsId.refinedMaterialId = data.oid;
        this.dictParamsId.materialId = data.materialOid;
        this.dictParamsId.licenceName = data.licenceName;
        this.dictParamsId.licenceOid = data.licenceOid;
        this.dictParamsId.serviceId = e.parent.data.serviceOid;
        this.dictParamsId.isEmpty = 0;
        this.dictParamsId.picType = "png";
        //点击子节点获取全部图片
        allMaterialImage({ refinedMaterialId: data.oid }).then(res => {
          if (res.code == 200) {
            this.allMaterialList = res.data;
            // console.log(this.allMaterialList)
          }
        });
        this.ArrData = '';
      }
    },
    //默认展开第一级
    defaultFrist (treeData) {
      var arr = [];
      for (let i = 0; i < treeData.length; i++) {
        arr.push(treeData[i].id);
        this.ArrData = arr[0];
      }
    },
    //点击打开打包数据集弹窗显示
    packHandle () {
      this.dataListDialogVisible = true;
    },
    //打包数据集
    packListHandle () {
      this.dataListDialogVisible = false;
      this.packDialogVisible = true;
    },
    //打包数据集成功列表数据加载
    packZipHandle () {
      this.dataListDialogVisible = true;
      this.packDialogVisible = false;
    }
  }
};
</script>
<style lang="less" scoped>
.data-dictionary {
  margin-left: 10px;
  .app-left {
    background-color: #fff;
    border-radius: 5px;
    // height: calc(100vh - 100px);
    height: 100%;
    padding-left: 6px !important;
    padding-right: 6px !important;
    ::v-deep.el-input--small .el-input__inner,
    .vue-treeselect__control,
    .el-input__inner {
      border: 1px solid #e7eaed !important;
    }
    .head-container {
      position: relative;
      .query-search {
        position: absolute;
        right: 1px;
        top: 10px;
        padding: 9px 13px;
      }
    }
    ::v-deep.el-tree-node__content {
      height: 36px;
      padding-left: 10px !important;
      position: relative;
    }
    ::v-deep.el-tree-node__content > .el-tree-node__expand-icon {
      position: absolute;
      right: 0;
      top: 0;
      padding: 12px;
      background-color: #d1d5d8;
      color: #fff;
      &::after {
        content: '';
      }
    }
    ::v-deep
      .el-tree-node.is-current
      .el-tree-node__content
      > .el-tree-node__expand-icon {
      background-color: #4a545f;
    }
    ::v-deep
      .el-tree-node__children
      .el-tree-node__content
      > .el-tree-node__expand-icon {
      background: none;
    }
    ::v-deep .el-tree-node {
      margin-bottom: 5px;
      .el-tree-node__children {
        padding: 5px 0 0 0;
        .el-tree-node {
          height: 31px;
        }
      }
    }
    ::v-deep.el-tree--highlight-current
      .el-tree-node.is-current
      > .el-tree-node__content,
    ::v-deep .el-tree > .el-tree-node.is-expanded > .el-tree-node__content {
      background-color: #656e78;
      color: #fff;
      &::before {
        content: '';
        position: absolute;
        left: -6px;
        top: 0;
        height: 36px;
        width: 3px;
        background-color: #656e78;
      }
    }
    ::v-deep
      .el-tree--highlight-current
      .is-expanded.is-current
      .el-tree-node__content
      > .el-tree-node__expand-icon {
      background-color: #4a545f;
    }
    ::v-deep
      .el-tree-node__children
      .el-tree-node
      .el-tree-node__content
      .el-tree-node__expand-icon {
      background: none !important;
    }

    ::v-deep .el-tree > .el-tree-node > .el-tree-node__content {
      background-color: #e8ebed;
      position: relative;
      &::before {
        content: '';
        position: absolute;
        left: -6px;
        top: 0;
        height: 36px;
        width: 3px;
        background-color: #e8ebed;
      }
    }
    ::v-deep.el-tree--highlight-current
      .el-tree-node__children
      .el-tree-node.is-current
      > .el-tree-node__content {
      background-image: linear-gradient(
        to right,
        #d4e7fd 0%,
        #d4e7fd 10%,
        #d9eafe 20%,
        #deedfe 33%,
        #e7f2fe 66%,
        #f6faff 100%
      );
      position: relative;
      color: #2b88ff;
      &::after {
        position: absolute;
        right: 12px;
        top: 13px;
        content: '';
        background: url(~@/assets/image/select-item-icon.png) no-repeat center;
        background-size: 100% 100%;
        width: 14px;
        height: 10px;
      }
    }
    ::v-deep .el-icon-caret-right:before {
      content: '\e6df';
    }
    ::v-deep.el-tree-node__expand-icon.expanded {
      -webkit-transform: rotate(180deg);
      transform: rotate(180deg);
      background-color: #4a545f;
    }
  }
  .app-right {
    .data-right {
      background-color: #fff;
      min-height: calc(100vh - 100px);
      border-radius: 5px;
      padding: 15px;
      .tip-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        .tip-title {
          padding: 0 10px 0px 0px;
          height: 35px;
          line-height: 35px;
          background: #fff5e4;
          border: 1px solid #ff7e00;
          border-radius: 3px;
          font-size: 14px;
          font-family: Microsoft YaHei;
          font-weight: 400;
          color: #bb7908;
          img {
            display: inline-block;
            vertical-align: middle;
            margin: 0 5px;
          }
          .tip-item {
            display: inline-block;
            vertical-align: top;
            background-color: #ffeccc;
            color: #ff7109;
            padding: 0 5px 0px 10px;
            height: 33px;
            margin-right: 10px;
            border-top-left-radius: 5px;
            border-bottom-left-radius: 5px;
          }
        }
        .jar-btn {
          background: #007ee8;
          box-shadow: 0px 3px 7px 0px rgba(16, 140, 238, 0.35);
          border-radius: 5px;
          padding: 7px 10px;
        }
      }
    }
  }
}

.el-tree > .el-tree-node {
  min-width: 100%;
  display: inline-block;
}
.btn-pack {
  margin: 30px auto 0 auto;
}
</style>
