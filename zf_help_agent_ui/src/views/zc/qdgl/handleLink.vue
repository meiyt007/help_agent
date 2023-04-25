<template>
  <div>
    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['qdgl:link:addUpdate']"
          >新增
        </el-button>
      </el-col>
    </el-row>
    <el-table v-loading="loading" :data="placeList" border>
      <el-table-column label="序号" align="center" type="index" width="60" />
      <el-table-column
        label="环节名称"
        align="center"
        prop="linkName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="办理时限"
        align="center"
        prop="handleTime"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="办理时限类型"
        align="center"
        prop="timeUnit"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="{ row: { timeUnit } }">
          <span v-if="timeUnit === 'W'">工作日</span>
          <span v-else-if="timeUnit === 'N'">自然日</span>
        </template>
      </el-table-column>
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
            v-hasPermi="['qdgl:link:view']"
            >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['qdgl:link:addUpdate']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleDelete(scope.row)"
            v-hasPermi="['qdgl:link:delete']"
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

    <!-- 添加或修改用户配置对话框 -->
    <el-dialog
      v-dialog-drag
      :title="title"
      :visible.sync="addUpdateFlag"
      :close-on-click-modal="false"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <el-input v-show="false" v-model="form.userOid" />
          <el-input v-show="false" v-model="form.id" />
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b><i class="require">*</i>环节名称：</b>
            </td>
            <td>
              <el-form-item prop="linkName">
                <el-input
                  v-model.trim="form.linkName"
                  placeholder="请输入环节名称"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td>
              <b><i class="require">*</i>所属办理环节：</b>
            </td>
            <td>
              <el-form-item prop="linkOid">
                <el-select
                  v-model="form.linkOid"
                  placeholder="请选择所属办理环节"
                >
                  <el-option
                    v-for="data in chargeLinkList"
                    :key="data.id"
                    :label="data.label"
                    :value="data.id"
                  ></el-option>
                </el-select>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b><i class="require">*</i>办理时限：</b>
            </td>
            <td>
              <el-form-item prop="handleTime">
                <el-input
                  v-model.trim="form.handleTime"
                  placeholder="时限"
                  style="width: 100px"
                  maxlength="10"
                  type="number"
                  max="999999"
                  show-word-limit
                />
                <el-radio v-model="form.timeUnit" label="W">工作日</el-radio>
                <el-radio v-model="form.timeUnit" label="N">自然日</el-radio>
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>排序号：</b></td>
            <td>
              <el-form-item prop="linkSort">
                <el-input-number
                  v-model.trim="form.linkSort"
                  placeholder="排序号"
                  :min="1"
                  :max="9999"  :step="1" step-strictly
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><b>办理岗位：</b></td>
            <td colspan="3">
              <el-form-item prop="postOidList">
                <el-select
                  v-model="postOidList"
                  multiple
                  collapse-tags
                  style="width: 200px"
                  @change="getDuty"
                  placeholder="请选择办理岗位"
                >
                  <el-option
                    v-for="item in postList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><b>岗位职责：</b></td>
            <td colspan="3">
              <el-form-item prop="postDuty">
                <el-input
                  type="textarea"
                  v-model.trim="form.postDuty"
                  readonly
                  placeholder="请输入岗位职责"
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><b>办理人：</b></td>
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
                  :default-checked-keys="[]"
                  :default-expanded-keys="userExpandedKeys"
                  @check="clickUserDeal"
                  @check-change="handleCheckChange"
                  :expand-on-click-node="false"
                  :props="defauProps"
                >
                  <span class="custom-tree-node" slot-scope="{ node, data }">
                    <el-tooltip
                      v-if="node.label.length > 8"
                      class="item"
                      effect="dark"
                      :content="node.label"
                      placement="right-start"
                    >
                      <span style="font-size: 14px"
                        >{{ node.label | ellipsis(8) }}
                      </span>
                    </el-tooltip>
                    <span v-if="node.label.length < 9" style="font-size: 14px"
                      >{{ node.label }}
                    </span>
                  </span>
                </el-tree>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><b>手机号码：</b></td>
            <td colspan="3">
              <el-form-item prop="handleUserPhone">
                <el-input
                  type="textarea"
                  placeholder="请输入手机号码"
                  readonly
                  v-model="form.handleUserPhone"
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><b>人员资质集合：</b></td>
            <td colspan="3">
              <el-form-item prop="handleUserQualified">
                <el-input
                  type="textarea"
                  placeholder="请输入人员资质集合"
                  v-model="form.handleUserQualified"
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><b>内部流程说明：</b></td>
            <td colspan="3">
              <el-form-item prop="serviceDescribe">
                <el-input
                  type="textarea"
                  placeholder="请输入内部流程说明"
                  v-model="form.serviceDescribe"
                />
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>

      <div slot="footer" class="dialog-footer" style="text-align: center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看用户配置对话框 -->
    <el-dialog
      v-dialog-drag
      title="查看办理环节信息"
      :visible.sync="linkView"
      v-if="linkView"
      :close-on-click-modal="false"
      width="1100px"
      height="500px"
      scrollbar
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
          <td><b>环节名称：</b></td>
          <td>
            {{ form.linkName }}
          </td>
          <td><b>所属办理环节：</b></td>
          <td>
            <!-- <el-form-item
                v-for="data in chargeLinkList"
                v-if="data.id == form.linkOid"
              >
                {{ data.label }}
              </el-form-item> -->
            <div v-for="data in chargeLinkList" v-if="data.id == form.linkOid">
              {{ data.label }}
            </div>
          </td>
        </tr>
        <tr>
          <td><b>办理时限：</b></td>
          <td>{{ form.handleTime }} {{ reversedSex }}</td>
          <td><b>排序号：</b></td>
          <td>
            {{ form.linkSort }}
          </td>
        </tr>
        <tr>
          <td><b>办理岗位：</b></td>
          <td colspan="3">
            {{ form.postNames }}
          </td>
        </tr>
        <tr>
          <td><b>岗位职责：</b></td>
          <td colspan="3">
            {{ form.postDuty }}
          </td>
        </tr>
        <tr>
          <td><b>办理人：</b></td>
          <td colspan="3">
            {{ form.handleUserNames }}
          </td>
        </tr>
        <tr>
          <td><b>手机号码：</b></td>
          <td colspan="3">
            {{ form.handleUserPhone }}
          </td>
        </tr>
        <tr>
          <td><b>人员资质集合：</b></td>
          <td colspan="3">
            {{ form.handleUserQualified }}
          </td>
        </tr>
        <tr>
          <td><b>内部流程说明：</b></td>
          <td colspan="3">
            {{ form.serviceDescribe }}
          </td>
        </tr>
      </table>

      <div slot="footer" style="text-align: center">
        <el-button @click="viewCancel">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { page, save, detail, del, get } from "@/api/zc/qdgl/handleLink";
import { getDistInfo } from "@/api/zc/qdgl/sxService";
import { queryDistrictSimpleTree } from "@/api/sys/district";
import { queryOrganTree } from "@/api/sys/organ";
import { list } from "@/api/sys/post";
import { getDictList } from "@/api/sys/dict";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { validatePhoneTwo ,validatePositiveNumber} from "@/utils/validate";

export default {
  name: "handleLink",
  components: { Treeselect },
  props: ["districtOid", "organOid", "createUser", "serviceOid"],
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      placeList: [],
      title: "",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        serviceOid: this.serviceOid
      },
      addUpdateFlag: false,
      linkView: false,
      readonly: true,
      districtOptions: [],
      editOrganOptions: [],
      postList: [],
      checkList: [],
      chargeLinkList: [],
      postOidList: [],
      defaultKeys: [],
      userExpandedKeys: [],
      defauProps: {
        label: "label", //这里是树结构中需显示的数据（即接口返回的需展示在页面上的参数）
        children: "children",
        isLeaf: "leaf"
      },
      userOids: [],
      userOidAppend: "",
      userNames: [],
      userNameAppend: "",
      form: {
        id: "",
        linkName: "",
        linkOid: "",
        handleTime: "",
        timeUnit: "W",
        linkSort: "",
        postOids: "",
        postNames: "",
        postDuty: "",
        handleUserOids: "",
        handleUserNames: "",
        districtOidSelect: "",
        handleUserPhone: "",
        handleUserQualified: "",
        serviceDescribe: "",
        createUser: this.createUser,
        serviceOid: this.serviceOid
      },
      rules: {
        linkName: [
          { required: true, message: "环节名称不能为空", trigger: "blur" }
        ],
        linkOid: [
          { required: true, message: "所属办理环节不能为空", trigger: "blur" }
        ],
        handleTime: [
          { required: true, message: "办理时限不能为空", trigger: "blur" },
          { validator: validatePositiveNumber , trigger: "blur"}
        ],
        linkSort: [
          { required: true, message: "排序号不能为空", trigger: "blur" }
        ]
      }
    };
  },
  watch:{
    'form.handleTime':{
      handler(newOld,oldVal){
        if(newOld>999999){
          this.form.handleTime=999999;
          this.$message.warning('办理时限最大值为999999');
        }
      }
    }
  },
  computed: {
    // 计算属性的 getter
    reversedSex: function () {
      if (this.form.timeUnit) {
        return this.form.timeUnit == "W" ? "工作日" : "自然日";
      }
      return "";
    }
  },
  created () {
    this.getList();
    this.getAllDistInfo();
    this.getPostList(this.districtOid, this.organOid);
  },
  methods: {
    /** 查询办理环节列表 */
    getList () {
      this.loading = true;
      let that = this;
      page(this.queryParams)
        .then(response => {
          this.placeList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        })
        .catch(function () {
          that.loading = false;
        });
    },
    handleAdd () {
      this.addUpdateFlag = true;
      this.title = "新增办理环节";
      this.form.id = "";
      this.form.linkName = "";
      this.form.linkOid = "";
      this.form.handleTime = "";
      this.form.timeUnit = "W";
      this.form.linkSort = "";
      this.form.postOids = "";
      this.form.postNames = "";
      this.form.postDuty = "";
      this.form.handleUserOids = "";
      this.form.handleUserNames = "";
      this.form.districtOidSelect = "";
      this.form.handleUserPhone = "";
      this.form.handleUserQualified = "";
      this.form.serviceDescribe = "";
      this.userOids = [];
      this.userNames = [];
      this.postOidList = [];
      this.$nextTick(() => {
        this.$refs.userTree.setCheckedKeys([]);
      });
    },
    handleUpdate (row) {
      this.title = "修改办理环节";
      this.addUpdateFlag = true;
      this.form.id = "";
      this.form.linkName = "";
      this.form.linkOid = "";
      this.form.handleTime = "";
      this.form.timeUnit = "W";
      this.form.linkSort = "";
      this.form.postOids = "";
      this.form.postNames = "";
      this.form.postDuty = "";
      this.form.handleUserOids = "";
      this.form.handleUserNames = "";
      this.form.districtOidSelect = "";
      this.form.handleUserPhone = "";
      this.form.handleUserQualified = "";
      this.form.serviceDescribe = "";
      this.postOidList = [];
      this.userOids = [];
      this.userNames = [];
      // 下面代码导致手机号码展示有问题，暂注释（bug：6456）
      // this.$nextTick(() => {
      //   this.$refs.userTree.setCheckedKeys([]);
      // });
      detail(row.id).then(res => {
        if (res.code == 200) {
          this.form.id = res.data.id;
          this.form.linkName = res.data.linkName;
          this.form.linkOid = res.data.linkOid;
          this.form.handleTime = res.data.handleTime;
          this.form.timeUnit = res.data.timeUnit;
          this.form.linkSort = res.data.linkSort;

          this.form.postOids = res.data.postOids;
          this.form.postNames = res.data.postNames;
          this.form.postDuty = res.data.postDuty;

          this.form.handleUserOids = res.data.handleUserOids;
          this.form.handleUserNames = res.data.handleUserNames;
          this.form.districtOidSelect = res.data.districtOidSelect;
          this.form.handleUserPhone = res.data.handleUserPhone;
          this.form.handleUserQualified = res.data.handleUserQualified;
          this.form.serviceDescribe = res.data.serviceDescribe;

          if (this.form.postOids.length > 0) {
            var data = this.form.postOids.split(",");
            for (let i = 0; i < data.length; i++) {
              if (data[i] != "" && data[i] != null) {
                this.postOidList.push(data[i]);
              }
            }
          }
          if (this.form.handleUserOids.length > 0) {
            var data = this.form.handleUserOids.split(",");
            for (let i = 0; i < data.length; i++) {
              if (data[i] != "" && data[i] != null) {
                this.userOids.push("USER-" + data[i]);
              }
            }
            this.setUserTreeChecked();
          }
          if (this.form.handleUserNames.length > 0) {
            var data = this.form.handleUserNames.split(",");
            for (let i = 0; i < data.length; i++) {
              if (data[i] != "" && data[i] != null) {
                this.userNames.push(data[i]);
              }
            }
          }
          this.$forceUpdate(); //强制刷新页面
        }
      });
    },
    submitForm () {
      this.form.postOids = "";
      this.form.postNames = "";
      this.form.handleUserOids = "";
      this.form.handleUserNames = "";
      for (var i = 0; i < this.userOids.length; i++) {
        let dataOid = this.userOids[i];
        if (dataOid.indexOf("USER-") > -1) {
          dataOid =
            null != dataOid
              ? dataOid.substring(dataOid.lastIndexOf("-") + 1, dataOid.length)
              : "";
          this.form.handleUserOids += dataOid + ",";
        }
      }
      for (var i = 0; i < this.userNames.length; i++) {
        if (this.userNames[i]) {
          this.form.handleUserNames += this.userNames[i] + ";";
        }
        this.form.handleUserNames = this.form.handleUserNames.substring(this.form.handleUserNames.length)
      }
      if (this.postOidList.length > 0) {
        for (let i = 0; i < this.postList.length; i++) {
          for (let j = 0; j < this.postOidList.length; j++) {
            if (this.postList[i].id == this.postOidList[j]) {
              this.form.postOids += this.postList[i].id + ",";
              if (this.postList[i].name) {
                this.form.postNames += this.postList[i].name + ";";
              }
            }
          }
        }
      }
      this.$refs["form"].validate(valid => {
        if (valid) {
          save(this.form)
            .then(response => {
              if (response.code == 200) {
                this.$message.success("保存成功");
                this.addUpdateFlag = false;
                this.getList();
              } else {
                this.$message.error("保存失败！");
              }
            })
            .catch(function () {
              this.addUpdateFlag = false;
            });
        }
      });
    },
    cancel () {
      this.addUpdateFlag = false;
      this.form.id = "";
      this.form.linkName = "";
      this.form.linkOid = "";
      this.form.handleTime = "";
      this.form.timeUnit = "W";
      this.form.linkSort = "";
      this.form.postOids = "";
      this.form.postNames = "";
      this.form.postDuty = "";
      this.form.handleUserOids = "";
      this.form.handleUserNames = "";
      this.form.districtOidSelect = "";
      this.form.handleUserPhone = "";
      this.form.handleUserQualified = "";
      this.form.serviceDescribe = "";
    },
    handleView (row) {
      detail(row.id).then(res => {
        if (res.code == 200) {
          this.linkView = true;
          this.form.id = res.data.id;
          this.form.linkName = res.data.linkName;
          this.form.linkOid = res.data.linkOid;
          this.form.handleTime = res.data.handleTime;
          this.form.timeUnit = res.data.timeUnit;
          this.form.linkSort = res.data.linkSort;
          this.form.postOids = res.data.postOids;
         // this.form.postNames = res.data.postNames;
          if(res.data.postNames){
            if(res.data.postNames.lastIndexOf(";",res.data.postNames.length)>-1 || res.data.postNames.lastIndexOf(",",res.data.postNames.length)>-1){
              this.form.postNames= res.data.postNames.substring(0,res.data.postNames.length-1);
            }else{
              this.form.postNames = res.data.postNames;
            }
          }
          //this.form.postDuty = res.data.postDuty;
          if(res.data.postDuty){
            if(res.data.postDuty.lastIndexOf(";",res.data.postDuty.length)>-1 || res.data.postDuty.lastIndexOf(",",res.data.postDuty.length)>-1){
              this.form.postDuty= res.data.postDuty.substring(0,res.data.postDuty.length-1);
            }else{
              this.form.postDuty = res.data.postDuty;
            }
          }
          this.form.handleUserOids = res.data.handleUserOids;
         // this.form.handleUserNames = res.data.handleUserNames;
          if(res.data.handleUserNames){
            if(res.data.handleUserNames.lastIndexOf(";",res.data.handleUserNames.length)>-1|| res.data.handleUserNames.lastIndexOf(",",res.data.handleUserNames.length)>-1){
              this.form.handleUserNames= res.data.handleUserNames.substring(0,res.data.handleUserNames.length-1);
            }else{
              this.form.handleUserNames = res.data.handleUserNames;
            }
          }
          this.form.districtOidSelect = res.data.districtOidSelect;
          if(res.data.handleUserPhone){
            if(res.data.handleUserPhone.lastIndexOf(";",res.data.handleUserPhone.length)>-1|| res.data.handleUserPhone.lastIndexOf(",",res.data.handleUserPhone.length)>-1){
              this.form.handleUserPhone= res.data.handleUserPhone.substring(0,res.data.handleUserPhone.length-1);
            }else{
              this.form.handleUserPhone = res.data.handleUserPhone;
            }
          }
          this.form.handleUserQualified = res.data.handleUserQualified;
          this.form.serviceDescribe = res.data.serviceDescribe;
        } else {
          this.linkView = false;
        }
      });
    },
    viewCancel () {
      this.linkView = false;
      this.form.id = "";
      this.form.linkName = "";
      this.form.linkOid = "";
      this.form.handleTime = "";
      this.form.timeUnit = "W";
      this.form.linkSort = "";
      this.form.postOids = "";
      this.form.postNames = "";
      this.form.postDuty = "";
      this.form.handleUserOids = "";
      this.form.handleUserNames = "";
      this.form.districtOidSelect = "";
      this.form.handleUserPhone = "";
      this.form.handleUserQualified = "";
      this.form.serviceDescribe = "";
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      const name = row.linkName;
      this.$confirm('是否确认删除"' + name + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function () {
          return del(row.id);
        })
        .then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
        .catch(function () { });
    },
    getDuty (val) {
      this.form.postDuty = "";
      if (val.length > 0) {
        for (let i = 0; i < this.postList.length; i++) {
          for (let j = 0; j < val.length; j++) {
            if (this.postList[i].id == val[j]) {
              if (this.postList[i].duty != "" && this.postList[i].duty != null && this.postList[i].duty != 'undefined') {
                this.form.postDuty += this.postList[i].duty + ";";
              }
            }
          }
        }
      }
    },
    selectUser () { },
    //获取字典配置
    getAllDistInfo () {
      let _that = this;
      getDistInfo().then(respon => {
        if (respon.data) {
          let sfhj = respon.data.XZSP;
          if (sfhj) {
            sfhj.forEach(sfItem => {
              let treeinfo = {};
              treeinfo.id = sfItem.oid;
              treeinfo.label = sfItem.name;
              _that.chargeLinkList.push(treeinfo);
            });
          }
        }
      });
    },
    ////根据组织机构oid和区划oid查询岗位列表
    getPostList (districtOid, organOid) {
      const data = {
        districtOid: districtOid,
        organOid: organOid
      };
      list().then(response => {
        this.postList = response.data;
      });
    },
    // 筛选节点
    filterNode (value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    loadNode (node, resolve) {
      // 一级节点处理
      if (node.level === 0) {
        this.getIndex(node, resolve);
      }
      // 其余节点处理
      if (node.level >= 1) {
        // 注意！把resolve传到你自己的异步中去
        this.getTree(node, resolve);
      }
    },
    getIndex (node, resolve) {
      let query = {
        oid: "",
        identity: "DISTRICT"
      };
      this.queryDistrictOrganUserTree(query).then(response => {
        this.userExpandedKeys = [];
        if (undefined != response.data[0]) {
          this.userExpandedKeys.push(response.data[0].id);
          resolve(response.data);
        }
      });
    },
    getTree (node, resolve) {
      let dataId =
        null != node.data.id
          ? node.data.id.substring(
            node.data.id.lastIndexOf("-") + 1,
            node.data.id.length
          )
          : "";
      if (node.data.identity === "USER") {
        resolve([]);
      }
      let query = {
        oid: dataId,
        identity: node.data.identity
      };
      this.queryDistrictOrganUserTree(query).then(response => {
        if (undefined != response) {
          node.data.children = response.data;
          this.userExpandedKeys.push(node.data.children.id);
          this.$refs.userTree.updateKeyChildren(node.data.id, node.data);
          resolve(response.data);
        }
      });
      this.setUserTreeChecked();
    },
    handleCheckChange (data, checked, indeterminate) {
      if (data.id.indexOf("USER-") > -1) {
        if (checked) {
          if (this.userOids.indexOf(data.id) == -1) {
            this.userOids.push(data.id);
            this.userNames.push(data.label);
            var dataOid =
              null != data.id
                ? data.id.substring(
                  data.id.lastIndexOf("-") + 1,
                  data.id.length
                )
                : "";
            if (dataOid != "") {
              get(dataOid).then(res => {
                this.form.handleUserPhone +=
                  data.label + "(" + res.data.mobile + ")" + ";";

                this.$forceUpdate(); //强制刷新页面
              });
            }
          }
        } else {
          //表示先获取这个元素的下标，然后从这个下标开始计算，删除长度为1的元素
          this.userOids.splice(this.userOids.indexOf(data.id), 1);
          this.userNames.splice(this.userNames.indexOf(data.label), 1);
          var dataOid =
            null != data.id
              ? data.id.substring(data.id.lastIndexOf("-") + 1, data.id.length)
              : "";
          if (dataOid != "") {
            get(dataOid).then(res => {
              this.form.handleUserPhone = this.form.handleUserPhone.replace(
                data.label + "(" + res.data.mobile + ")" + ";",
                ""
              );
              this.$forceUpdate(); //强制刷新页面
            });
          }
        }
      }
    },
    //数组去重
    unique (arr) {
      for (var i = 0, len = arr.length; i < len; i++) {
        for (var j = i + 1, len = arr.length; j < len; j++) {
          if (arr[i] === arr[j]) {
            arr.splice(j, 1);
            j--; // 每删除一个数j的值就减1
            len--; // j值减小时len也要相应减1（减少循环次数，节省性能）
          }
        }
      }
      return arr;
    },
    setUserTreeChecked () {
      this.$nextTick(() => {
        if (null != this.userOids) {
          let nodes = this.$refs.userTree.getCheckedNodes();
          if (nodes) {
            for (let user of nodes) {
              this.userOids.push(user.id);
            }
          }
          this.userOids = this.unique(this.userOids);
          this.$refs.userTree.setCheckedKeys(this.userOids);
        }
      });
    },
    //选择用户
    clickUserDeal (currentObj, treeStatus) {
      // 用于：父子节点严格互不关联时，父节点勾选变化时通知子节点同步变化，实现单向关联。
      let selected = treeStatus.checkedKeys.indexOf(currentObj.id); // -1未选中
      // 选中
      var userTree = this.$refs.userTree;
      if (selected !== -1) {
        // 子节点只要被选中父节点就被选中
        this.selectedParent(currentObj, userTree);
        // 统一处理子节点为相同的勾选状态
        this.uniteChildSame(currentObj, true, userTree);
      } else {
        // 未选中 处理子节点全部未选中
        if (
          currentObj.children !== undefined &&
          currentObj.children.length !== 0
        ) {
          this.uniteChildSame(currentObj, false, userTree);
          //表示先获取这个元素的下标，然后从这个下标开始计算，删除长度为1的元素
          //this.users.splice(this.users.indexOf(user),1);
        }
      }
    },
    uniteChildSame (treeList, isSelected, treeObject) {
      treeObject.setChecked(treeList.id, isSelected);
      if (
        treeList.children &&
        null != treeList.children &&
        treeList.children.length !== undefined
      ) {
        for (let i = 0; i < treeList.children.length; i++) {
          this.uniteChildSame(treeList.children[i], isSelected, treeObject);
        }
      }
    },
    // 统一处理父节点为选中
    selectedParent (currentObj, treeObject) {
      let currentNode = treeObject.getNode(currentObj);
      if (currentNode.parent != null && currentNode.parent.key !== undefined) {
        treeObject.setChecked(currentNode.parent, true);
        this.selectedParent(currentNode.parent, treeObject);
      }
    }
  }
};
</script>
<style scoped>
.postClass .treeselect {
  width: 98% !important;
}
</style>
