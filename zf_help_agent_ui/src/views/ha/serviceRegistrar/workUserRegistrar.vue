/** * @Author: yupeng */
<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-form :model="queryParams" ref="queryUserForm" :inline="true" label-width="80px">
      <el-form-item label="组名" prop="groupName">
        <el-select v-model.trim="queryParams.groupName" placeholder="请选择组名">
          <el-option
            v-for="group in groupList"
            :key="group.name"
            :label="group.name"
            :value="group.name">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="分组职务" prop="postName">
        <el-col :span="24">
          <el-select v-model.trim="queryParams.postName" placeholder="请选择分组职务" size="small">
            <el-option label="组长" value="1"></el-option>
            <el-option label="副组长" value="2"></el-option>
            <el-option label="组员" value="3"></el-option>
          </el-select>
        </el-col>
      </el-form-item>

      <el-form-item label="姓名" prop="workUserName">
        <el-input v-model.trim="queryParams.workUserName" placeholder="请输入姓名" clearable size="small"/>
      </el-form-item>

      <el-form-item class="fr mr0">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          搜索
        </el-button>
        <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">
          重置
        </el-button>
      </el-form-item>
    </el-form>
    <!-- 帮代办人员表头 -->
    <el-table v-loading="loading" :data="workUserList" stripe style="width: 100%">

      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>

      <el-table-column label="组名" align="center" prop="groupName" :show-overflow-tooltip="true"/>
      <el-table-column label="分组职务" align="center" prop="groupPost" :show-overflow-tooltip="true"/>
      <el-table-column label="姓名" align="center" prop="workUserName" :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" width="340">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="iconfont zfsoft-shouquan" v-hasPermi="['ha:service:review']"
                     @click="handleServiceView(scope.row)">
            查看
          </el-button>
          <el-button size="mini" type="text" icon="iconfont zfsoft-shouquan" v-hasPermi="['ha:service:registrar']"
                     @click="handleServiceRgistrar(scope.row)">
            授权
          </el-button>
        </template>
      </el-table-column>

    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
                style="padding: 30px 0; text-align: center;" layout="total, sizes, prev, pager, next, jumper"
                @pagination="getList"/>

    <!-- 点击人员查看弹窗栏 -->
    <el-dialog title="事项列表" :close-on-click-modal="false" :visible.sync="openServiceView" width="1400px" append-to-body
               height="800px">

      <!-- 搜索栏 -->
      <el-form :model="queryServiceParams" ref="queryServiceForm" :inline="true" label-width="80px">
        <el-form-item label="事项名称" prop="serviceName">
          <el-input v-model.trim="queryServiceParams.serviceName" placeholder="请输入事项名称" clearable size="small"
                    @keyup.enter.native="handleQuery"/>
        </el-form-item>
        <el-form-item label="实施编码" prop="implementCode">
          <el-input v-model.trim="queryServiceParams.implementCode" placeholder="请输入实施编码" clearable size="small"
                    @keyup.enter.native="handleQuery"/>
        </el-form-item>

        <el-form-item class="fr mr0">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleServiceQuery">
            搜索
          </el-button>
          <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetServiceQuery" class="btn-reset">
            重置
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 事项表头 -->
      <el-table v-loading="loading" :data="serviceList" border :fit="true" height="calc(100% - 120px)">

        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="所属区划" align="center" prop="districtName" :show-overflow-tooltip="true"/>
        <el-table-column label="所属机构" align="center" prop="organName" :show-overflow-tooltip="true"/>
        <el-table-column label="事项名称" align="center" prop="serviceName" :show-overflow-tooltip="true"/>
        <el-table-column label="实施编码" align="center" prop="implementCode" :show-overflow-tooltip="true"/>
        <el-table-column label="事项类型" align="center" prop="serviceTypeName" :show-overflow-tooltip="true"/>
        <el-table-column label="服务类型" align="center" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <el-checkbox v-if="scope.row.prepareMaterialStatus === '1' " v-model="checked" disabled>
              材料准备
            </el-checkbox>
            <el-checkbox v-if="scope.row.prepareMaterialStatus !=='1' " disabled>
              材料准备
            </el-checkbox>
            <el-checkbox v-if="scope.row.receiveMaterialStatus === '1' " v-model="checked" disabled>
              收件
            </el-checkbox>
            <el-checkbox v-if="scope.row.receiveMaterialStatus !== '1' " disabled>
              收件
            </el-checkbox>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="iconfont zfsoft-shouquan"
                       @click="handleServiceTypeView(scope.row)">
              授权
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" :page.sync="queryServiceParams.pageNum"
                  :limit.sync="queryServiceParams.pageSize"
                  @pagination="getServiceList"/>

      <div slot="footer" align="center">
        <el-button @click="openServiceView=false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 点击事项授权弹窗栏 -->
    <el-dialog title="服务类型" :close-on-click-modal="false" :visible.sync="openServiceTypeView" width="800px"
               append-to-body
               height="200px">
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col/>
          <col/>
        </colgroup>
        <tr>
          <td>
            <b>服务类型：</b>
          </td>
          <td>
            <el-checkbox-group v-model="checkList">
              <el-checkbox label="CLZB">材料准备</el-checkbox>
              <el-checkbox label="SJ">收件</el-checkbox>
            </el-checkbox-group>
          </td>
        </tr>
      </table>

      <div slot="footer" align="center">
        <el-button @click="submit()">确定</el-button>
        <el-button @click="openServiceTypeView=false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 点击人员授权弹窗栏 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="title" :visible.sync="showWorkUserRegistrarView"
               v-if="showWorkUserRegistrarView" width="900px" height="700px" append-to-body>
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="15%"/>
          <col width="15%"/>
        </colgroup>
        <tr>
          <td><b>组名：</b></td>
          <td>
            <el-col :span="24">{{ workUser.groupName }}</el-col>
          </td>
          <td><b>分组职务：</b></td>
          <td>
            <el-col :span="24">{{ workUser.groupPost }}</el-col>
          </td>
          <td><b>姓名：</b></td>
          <td>
            <el-col :span="24">{{ workUser.workUserName }}</el-col>
          </td>
        </tr>
        <tr>
          <td><b>选择授权事项：</b></td>
          <td colspan="5">
            <el-tree :data="treeList"
                     show-checkbox
                     node-key="id"
                     ref="serviceTree"
                     v-loading="loading"
                     :default-checked-keys="defaultCheckedList"
                     highlight-current
                     @check="checkService"
                     :props="defaultProps">
            </el-tree>
          </td>
        </tr>
      </table>

      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="showWorkUserRegistrarView=false">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {
  page,
  getAllGroup,
  servicePage,
  getServiceTree,
  saveOrUpdateWorkUserRegistrar, bathSaveOrUpdateWorkUserRegistrar
} from "@/api/ha/serviceRegistrar/serviceOrWorkUserRegistrar.js";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "workUserRegistrar",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      //人员查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        groupName: "",
        postName: "",
        workUserName: ""
      },
      //事项查询参数
      queryServiceParams: {
        pageNum: 1,
        pageSize: 10,
        serviceName: "",
        implementCode: "",
        serviceStatus: "3",
        workUserId: ""
      },
      // 表单参数
      form: {},
      //查看事项弹出层
      openServiceView: false,
      //查看显示服务类型弹出层
      openServiceTypeView: false,
      //查看人员授权弹出层
      showWorkUserRegistrarView: false,
      checked: true,
      groupList: [],
      workUserList: [],
      workUserId: "",
      serviceOid: "",
      serviceList: [],
      checkList: [],
      workUser: {},
      title: "事项授权",
      treeList: [],
      checkedNodeList: [],
      defaultCheckedList: [],
      allChildNodeList: [],
      defaultProps: {
        children: "member",
        label: "name"
      },
      props: {
        label: "name",//这里是树结构中需显示的数据（即接口返回的需展示在页面上的参数）
        children: 'member',
        isLeaf: "leaf"
      }
    };
  },
  created() {
    this.getList();
    this.getGroup();
  },
  methods: {
    //搜索
    getList() {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.workUserList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },

    //获取分组
    getGroup() {
      getAllGroup().then(response => {
        this.groupList = response.data;
      })
    },

    //人员列表搜索按钮操作
    handleQuery() {
      this.queryParams.pageNumber = 1;
      this.queryParams.pageSize = 10;
      this.getList();
    },

    //人员列表重置
    resetQuery() {
      this.resetForm("queryUserForm");
      this.handleQuery();
    },

    //人员列表查看操作
    handleServiceView(row) {
      this.workUserId = row.id;
      this.queryServiceParams.workUserId = row.id;
      this.openServiceView = true;
      this.getServiceList();
    },

    //查询事项列表
    getServiceList() {
      this.loading = true;
      servicePage(this.queryServiceParams).then(response => {
        this.serviceList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
    },

    //事项查询
    handleServiceQuery() {
      this.queryServiceParams.pageNum = 1;
      this.getServiceList();
    },

    //重置事项查询
    resetServiceQuery() {
      this.resetForm("queryServiceForm");
      this.handleServiceQuery();
    },

    //事项列表页面授权按钮操作
    handleServiceTypeView(row) {
      this.checkList = [];
      if (row.prepareMaterialStatus === '1') {
        this.checkList.push("CLZB");
      }
      if (row.receiveMaterialStatus === '1') {
        this.checkList.push("SJ");
      }
      this.serviceOid = row.serviceOid;
      this.openServiceTypeView = true;
    },

    //人员列表授权操作
    handleServiceRgistrar(row) {
      this.loading = true;
      this.workUser = row;
      this.showWorkUserRegistrarView = true;
      getServiceTree(row.id).then(response => {
        this.treeList = response.data.treeArray;
        this.defaultCheckedList = response.data.defaultCheckedArray;
        this.checkedNodeList = response.data.checkedArray;
        this.allChildNodeList = response.data.allChildNodeArray;
        this.loading = false;
      })
    },

    //下拉tree勾选事项
    checkService() {
      let checkedData = this.$refs.serviceTree.getCheckedNodes(true, false);
      this.checkedNodeList = checkedData;
    },


    //批量提交授权
    submitForm() {
      let workUserId = this.workUser.id;
      let requestData = {
        "workUserId": workUserId,
        "allNodeList": this.allChildNodeList,
        "checkedNodeList": this.checkedNodeList
      };
      bathSaveOrUpdateWorkUserRegistrar(requestData).then(response => {
        if (response.code === 200) {
          this.msgSuccess("保存成功");
          this.showWorkUserRegistrarView = false;
        } else {
          this.msgError(response.message);
        }
      });
    },

    //单个事项授权
    submit() {
      let dataArray = [];
      let checkedList = this.checkList;
      let typeArray = [];
      let type1 = {"typeName": "CLZB", "status": "2"};
      let type2 = {"typeName": "SJ", "status": "2"};
      for (let i = 0; i < checkedList.length; i++) {
        let typeName = checkedList[i];
        if (typeName == "CLZB") {
          type1.status = "1";
        }
        if (typeName == "SJ") {
          type2.status = "1";
        }
      }
      typeArray.push(type1);
      typeArray.push(type2);
      let data = {
        "helpWorkUserId": this.workUserId,
        "serviceOid": this.serviceOid,
        "serviceType": typeArray
      }

      dataArray.push(data);
      saveOrUpdateWorkUserRegistrar(dataArray).then(res => {
        if (res.code === 200) {
          this.msgSuccess("保存成功");
          this.openServiceTypeView = false;
          this.getServiceList();
        } else {
          this.msgError(data.message);
        }
      })
    }
  },
};
</script>
<style lang="scss" scoped>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.dialog-table {
  padding: 20px;
  box-sizing: border-box;
}

.dialog-table h3 {
  font-size: 14px;
  font-weight: normal;
  color: #333;
  margin: 0px 0px 10px 0px;
}

.dialog-table table {
  width: 100%;
}

.dialog-table table {
  border: 1px solid #dfe6ec;
  border-collapse: collapse;
}

.dialog-table table tr td {
  border: 1px solid #dfe6ec;
  padding: 17px 8px;
  font-size: 12px;
  color: #515a6e;
  text-align: right;
}

.dialog-table table tr td:nth-of-type(2n) {
  color: #606266;
  text-align: left;
}

.dialog-table .el-form-item {
  margin-bottom: 0;
}

.el-form-item--small .el-form-item_content {
  margin-left: 0px !important;
}

.line {
  text-align: center;
}

</style>

<style lang="scss">

.el-checkbox__input.is-disabled.is-checked .el-checkbox__inner {
  background-color: #1890FF !important;
  border-color: #1890FF !important;
}

.el-checkbox__input.is-disabled + span.el-checkbox__label {
  color: #1890FF !important;
}

.el-checkbox__input.is-disabled.is-checked .el-checkbox__inner::after {
  border-color: white !important;
}

.el-checkbox__input.is-disabled .el-checkbox__inner {
  border-color: #1890FF !important;
}
</style>
