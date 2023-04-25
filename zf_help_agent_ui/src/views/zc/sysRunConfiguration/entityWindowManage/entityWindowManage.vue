/**
* @Author: kkfan
* @Date: 2020-10-26 18:21:27
 * @Last Modified by: kkfan
 * @Last Modified time: 2020-10-31 10:42:25
* @Description: 实体窗口管理
*/
<template>
  <div class="app-container">
    <!--区划数据-->
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="108px">
      <el-row>
        <el-form-item label="所属区划" prop="districtOid">
          <treeselect  class="treeselect" :defaultExpandLevel="1" noOptionsText = "暂无数据" noResultsText="暂无数据" :show-count="true" v-model="queryParams.districtOid" :options="districtOptions" placeholder="请输入所属区划" />
        </el-form-item>
        <el-form-item label="窗口名称" prop="windowName">
          <el-input v-model.trim="queryParams.windowName" placeholder="请输入窗口名称" clearable size="100" @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item class="fr mr0">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置</el-button>
        </el-form-item>
      </el-row>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="default" icon="el-icon-plus" size="mini" @click="handleInit" v-hasPermi="['sys:AreaSite:init']">新增</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="humanEvidenceDeviceList" border :fit="true">
    <el-table-column label="序号" width="55" type="index" align="center">
      <template slot-scope="scope">
        <span>{{ scope.$index + 1 }}</span>
      </template>
    </el-table-column>
    <el-table-column label="所属区划"  align="center" prop="districtName" :show-overflow-tooltip="true"/>
    <el-table-column label="所属机构"  align="center" prop="organName" :show-overflow-tooltip="true"/>
    <el-table-column label="窗口名称" align="center"  prop="windowName" :show-overflow-tooltip="true"/>
    <el-table-column label="窗口编号"  align="center" prop="windowNum" :show-overflow-tooltip="true"/>
    <!-- <el-table-column label="创建时间" width="200" align="center" prop="createDate"/>
     <el-table-column label="更新时间" width="200" align="center" prop="modifyDate"/>-->
    <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)" v-hasPermi="['sys:district:view']" >查看</el-button>
          <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai"  @click="handleInit(scope.row)" v-hasPermi="['sys:district:update']">修改</el-button>
          <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDelete(scope.row)" v-hasPermi="['sys:district:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="queryParams.total > 0" :total="queryParams.total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
    <!-- 添加或修改对话框 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="dialogTitle" v-if="addDialogShow" :visible.sync="addDialogShow" width="900px" append-to-body>
      <div>
      <el-form ref="form" :model="form" :rules="rules" label-width="0px" >
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td><i class="require">*</i>
              <b>所属区划：</b>
            </td>
            <td>
              <el-form-item prop="districtOid">
                <el-col :span="24">
                  <treeselect
                    :options="districtOptions"
                    :default-expand-level="1"
                    placeholder="请选择区划"
                    v-model="form.districtOid"
                    @select="districtSel"
                  />
                </el-col>
              </el-form-item>
            </td>
            <td><i class="require">*</i>
              <b>所属组织机构：</b>
            </td>
            <td>
              <el-form-item prop="organOid">
                <el-col :span="24">
                  <treeselect
                    :options="listOrganOptions"
                    noOptionsText="暂无数据"
                    :default-expand-level="1"
                    placeholder="请选择所属组织机构"
                    v-model="form.organOid"
                    @select="organSel"
                  />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i>
              <b>窗口名称：</b>
            </td>
            <td>
              <el-form-item prop="windowName">
                <el-col :span="24">
                  <el-input v-model.trim="form.windowName" placeholder="请输入窗口名称" maxlength="100" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
            <td><i class="require">*</i>
              <b>窗口编号：</b>
            </td>
            <td>
              <el-form-item prop="windowNum">
                <el-col :span="24">
                  <el-input v-model.trim="form.windowNum" placeholder="请输入窗口编号" maxlength="10" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i>
              <b>窗口所在楼层：</b>
            </td>
            <td>
              <el-form-item prop="inFloor">
                <el-col :span="24">
                  <el-select v-model="form.inFloor" placeholder="请选择窗口所在楼层">
                    <el-option v-for="dict in inFloorList" :key="dict.dictOid" :label="dict.name" :value="dict.dictOid"></el-option>
                  </el-select>
                  <!-- <el-input v-model="form.inFloor" placeholder="请输入窗口所在楼层" maxlength="32" show-word-limit /> -->
                </el-col>
              </el-form-item>
            </td>
            <td><i class="require">*</i>
              <b>平板MAC地址：</b>
            </td>
            <td>
              <el-form-item prop="macAddress">
                <el-col :span="24">
                  <el-input v-model.trim="form.macAddress" placeholder="请输入平板MAC地址" maxlength="100" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i>
              <b>窗口位置：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="windowSeat">
                <el-col :span="24">
                  <el-input v-model.trim="form.windowSeat" placeholder="请输入窗口位置" maxlength="200" show-word-limit/>
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i>
              <b>窗口电脑IP地址：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="ipAddress">
                <el-col :span="24">
                  <el-input v-model.trim="form.ipAddress" placeholder="请输入窗口电脑IP地址" maxlength="100" show-word-limit/>
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b>选择用户：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="userOids">
                <el-tree
                  show-checkbox
                  node-key="id"
                  ref="userTree"
                  :load="loadNode"
                  :filter-node-method="filterNode"
                  lazy
                  highlight-current
                  :check-strictly="true"
                  :default-expanded-keys="userExpandedKeys"
                  @check="clickUserDeal"
                  v-model="form.userOids"
                  :props="props">
                </el-tree>
              </el-form-item>
<!--              <el-form-item prop="userOids">
                <el-tree show-checkbox node-key="id" ref="userTree" :load="loadNode" lazy highlight-current
                         :check-strictly="true"
                         :default-expanded-keys="userExpandedKeys"
                         v-model="form.userOids"
                         >
                </el-tree>
              </el-form-item>-->
            </td>
          </tr>
        </table>
      </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="() => {addDialogShow = false; reset()}">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 区划信息详细 -->
    <el-dialog v-dialog-drag :title="dialogTitle" :visible.sync="detailDialogShow" v-if="detailDialogShow" width="800px" append-to-body>
      <div>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b>所属区划：</b>
            </td>
            <td>
                <el-col :span="24">
                  <treeselect
                    :options="districtOptions"
                    :default-expand-level="1"
                    v-model="form.districtOid"
                    :disabled="true"
                    placeholder=""
                  />
                </el-col>
            </td>
            <td>
              <b>所属组织机构：</b>
            </td>
            <td>
                  <treeselect
                    :options="listOrganOptions"
                    noOptionsText="暂无数据"
                    :default-expand-level="1"
                    v-model="form.organOid"
                    placeholder=""
                    :disabled="true"
                  />
            </td>
          </tr>
          <tr>
            <td>
              <b>窗口名称：</b>
            </td>
            <td>
              {{ form.windowName }}
            </td>
            <td>
              <b>窗口编号：</b>
            </td>
            <td>
              {{ form.windowNum }}
            </td>
          </tr>
          <tr>
            <td>
              <b>窗口所在楼层：</b>
            </td>
            <td>
              <el-select v-model="form.inFloor" disabled>
                <el-option v-for="(dict, key) in inFloorList" :key="key" :label="dict.name" :value="dict.dictOid" ></el-option>
              </el-select>
            </td>
            <td>
              <b>平板MAC地址：</b>
            </td>
            <td>
              {{ form.macAddress }}
            </td>
          </tr>
          <tr>
            <td>
              <b>窗口位置：</b>
            </td>
            <td colspan="3">
              {{ form.windowSeat }}
            </td>
          </tr>
          <tr>
            <td>
              <b>窗口电脑IP地址：</b>
            </td>
            <td colspan="3">
              {{ form.ipAddress }}
            </td>
          </tr>
          <tr>
            <td>
              <b>选择用户：</b>
            </td>
            <td colspan="3">
              {{ form.userNames }}
            </td>
          </tr>
        </table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogShow = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { page, deletes, saveOrUpdate, getOne } from "@/api/zc/sysRunConfiguration/entityWindowManage";
  import { queryDistrictSimpleTree } from "@/api/sys/district";
  import {validateNumber, validIDCard, validIntNoZero,validIpAddress,validMacAddress,validNumForWin} from "@/utils/validate";
  import { deepClone } from "@/utils/index";
  import { queryOrganTree } from "@/api/sys/organ";
  import { getDictList } from "@/api/sys/common";

  // import the component
  import Treeselect from '@riophae/vue-treeselect';
  // import the styles
  import '@riophae/vue-treeselect/dist/vue-treeselect.css';
  export default {
    name: "EntityWindowManage",
    components: {Treeselect},
    data() {
      return {
        // 列表数据
        humanEvidenceDeviceList: [],
        // 弹窗标题
        dialogTitle: '',
        addDialogShow: false,
        detailDialogShow: false,
        // 楼层数据列表
        inFloorList: [],
        // 机构
        listOrganOptions: [],
        // 区划树
        districtOptions: [],
        userExpandedKeys:[],
        userOidArr:[],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          total: 0, // 分页参数
          siteName: ""
        },
        props: {
          label: "label",//这里是树结构中需显示的数据（即接口返回的需展示在页面上的参数）
          children: 'children',
          isLeaf: "leaf"
        },
        // 表单参数
        form: {
          id: '', //逻辑主键
          districtOid: null, // 区划id
          districtName: null, // 区划名称
          organOid: null, // 所属组织机构
          organName: null,    // 机构名称
          windowName: '', // 窗口名称
          windowNum: '',  // 窗口编号
          inFloor: '',    // 窗口所在楼层
          macAddress: '',  // 平板mac地址
          windowSeat: '', // 窗口所在地址
          ipAddress: '',  // 窗口电脑ip
        },
        // 表单校验
        rules: {
          districtOid: [
            { required: true, message: "请选择所属区划", trigger: "blur" }
          ],
          organOid: [
            { required: true, message: "请选择所属组织机构", trigger: "blur" }
          ],
          windowName: [
            { required: true, message: "窗口名称不能为空", trigger: "blur" }
          ],
          windowNum: [
            { required: true, message: "窗口编号不能为空", trigger: "blur" },
            {validator: validateNumber, trigger: 'blur'}
          ],
          inFloor: [
            { required: true, message: "请选择窗口所在楼层", trigger: "blur" }
          ],
          macAddress: [
            { required: true, message: "平板MAC地址不能为空", trigger: "blur" },
            {validator: validMacAddress, trigger: 'blur'}
          ],
          windowSeat: [
            { required: true, message: "窗口位置不能为空", trigger: "blur" }
          ],
          ipAddress: [
            { required: true, message: "窗口电脑IP地址不能为空", trigger: "blur" },
            {validator: validIpAddress, trigger: 'blur'}
          ],
        }
      };
    },
    methods: {
      districtSel(node, instanceId) {
        this.form.districtName = node.label;
      },
      organSel(node, instanceId) {
        this.form.organName = node.label;
      },
      /** 获取楼层数据 */
      getDictList() {
        let _that = this;
        // 查询楼层字典数据
        getDictList('STCKLC').then(response => {
          _that.inFloorList = response.data;
      });
      },
      /** 获取机构数据 */
      getListOrganTree(districtOid, callback) {
        if (districtOid) {
          districtOid = null!=districtOid?districtOid.substring(districtOid.lastIndexOf('-')+1,districtOid.length):'';
          queryOrganTree(districtOid).then(response => {
            this.listOrganOptions = response.data;
          callback && callback();
        });
        } else {
          this.listOrganOptions = []
          this.queryParams.organOid = null
        }
      },
      /** 获取区划树 */
      getDistrictTree(districtOid) {
        let _that = this;
        queryDistrictSimpleTree(districtOid).then(response => {
          _that.districtOptions = response.data;
        });
      },
      setUserTreeChecked(){
        this.$nextTick(() => {
          this.$refs.userTree.setCheckedKeys(this.userOidArr);
        })
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 查询受理辖区列表 */
      getList() {
        this.loading = true;
        page(this.queryParams).then(response => {
          this.humanEvidenceDeviceList = response.data.data;
        this.queryParams.total = response.data.total;
        this.loading = false;
      });
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 表单重置
      reset() {
        Object.assign(this.form, this.$options.data().form)
        this.resetForm("form");
      },
      /** 查看按钮操作 */
      handleView(row) {
        let _that = this;
        _that.reset();
        const oid = row.id;
        getOne(oid).then(response => {
          if(response.data.districtOid) {
          _that.getListOrganTree(response.data.districtOid, () => {
            _that.form.organOid = response.data.organOid;
          _that.form.organName = response.data.organName;
        });
        }
        _that.form = deepClone(response.data);
        _that.detailDialogShow = true;
        _that.dialogTitle = "查看详情";
      });
      },
      /** 新增和修改按钮操作 */
      handleInit(row) {
        let _that = this;
        _that.reset();
        if(row.id) {
          getOne(row.id).then(response => {
            _that.addDialogShow = true;
          if(response.data.districtOid) {
              _that.getListOrganTree(response.data.districtOid, () => {
                _that.form.organOid = response.data.organOid;
              _that.form.organName = response.data.organName;
              });
            _that.userOidArr = response.data.userOids ? response.data.userOids.split(';') : [];
            this.setUserTreeChecked();
          }
          _that.form = deepClone(response.data);
        });
        } else {
          _that.addDialogShow = true;
        }
        _that.dialogTitle = row.id ? "修改" : "新增";
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        let id = row.id;
        this.$confirm("是否确认删除?", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(function() {
            return deletes(id);
          })
          .then(() => {
          this.getList();
        this.msgSuccess("删除成功");
      })
      .catch(function() {});
      },
      /** 提交按钮 */
      submitForm: function() {
        let _that = this;
        _that.$refs["form"].validate(valid => {
          if (valid) {
            let nodes=this.$refs.userTree.getCheckedNodes();
            let oids="";
            let names="";
            if(nodes){
              for(let user of nodes){
                if(user.identity==="USER"){
                  oids+=user.id+";"
                  names+=user.label+";";
                }
              }
            }
            this.form.userOids=oids;
            this.form.userNames=names;
            saveOrUpdate(_that.form).then(response => {
              if (response.code === 200) {
              _that.msgSuccess("保存成功");
              _that.addDialogShow = false;
              setTimeout(() => {
                _that.getList();
            }, 10);
            }
          });
          }
        });
      },
      loadNode(node, resolve) {
        // 一级节点处理
        if (node.level === 0) {
          this.getIndex(node,resolve);
        }
        // 其余节点处理
        if (node.level >= 1) {
          // 注意！把resolve传到你自己的异步中去
          this.getTree(node, resolve);
        }
      },
      getIndex(node,resolve){
        let query={
          oid:'',
          identity: 'DISTRICT'
        }
        this.queryDistrictOrganUserTree(query).then(response => {
          this.userExpandedKeys = [];
          this.userExpandedKeys.push(response.data[0].id)
          this.userExpandedKeys.push(response.data[0].children[0].id)
          resolve(response.data);
        });
      },
      getTree(node,resolve){
        if(node.data.identity==="USER"){
          resolve([])
        }
        let query={
          oid:node.data.id,
          identity: node.data.identity
        }
        this.queryDistrictOrganUserTree(query).then(response => {
          node.data.children = response.data;
          this.$refs.userTree.updateKeyChildren(node.data.id,node.data);
          resolve(response.data);
        });
        //this.setUserTreeChecked();
      },
      //选择用户
      clickUserDeal (currentObj, treeStatus) {
        // 用于：父子节点严格互不关联时，父节点勾选变化时通知子节点同步变化，实现单向关联。
        let selected = treeStatus.checkedKeys.indexOf(currentObj.id) // -1未选中
        // 选中
        var userTree = this.$refs.userTree;
        if (selected !== -1) {
          // 子节点只要被选中父节点就被选中
          this.selectedParent(currentObj,userTree)
          // 统一处理子节点为相同的勾选状态
          this.uniteChildSame(currentObj, true,userTree)
        } else {
          // 未选中 处理子节点全部未选中
          if (currentObj.children !== undefined && currentObj.children.length !== 0) {
            this.uniteChildSame(currentObj, false,userTree)
          }
        }
      },
      uniteChildSame (treeList, isSelected,treeObject) {
        treeObject.setChecked(treeList.id, isSelected)
        if (treeList.children && null!=treeList.children && treeList.children.length !== undefined) {
          for (let i = 0; i < treeList.children.length; i++) {
            this.uniteChildSame(treeList.children[i], isSelected,treeObject)
          }
        }

      },
      // 统一处理父节点为选中
      selectedParent (currentObj,treeObject) {
        let currentNode = treeObject.getNode(currentObj)
        if (currentNode.parent != null && currentNode.parent.key !== undefined) {
          treeObject.setChecked(currentNode.parent, true)
          this.selectedParent(currentNode.parent,treeObject)
        }
      },
    },
    watch: {
      'form.districtOid': function (val, oldVal) {
        if(!val) {
          this.form.districtName = null;
        }
        this.form.organOid = null;
        this.form.organName = null;
        //机构加载
        this.getListOrganTree(val)
      },
      'form.organOid': function (val, oldVal) {
        if(!val) {
          this.form.organName = null;
        }
      },
    },
    created() {
      this.getList();
      this.getDistrictTree();
      this.getDictList();
    },
  };
</script>
<style lang="scss" scoped>
  .treeselect{
    width: 200px;
  }
  .treeselect240{
    width: 240px;
  }
</style>
