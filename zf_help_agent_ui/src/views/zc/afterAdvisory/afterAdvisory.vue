/**
* @Author: liangss
* @Date: 2020-11-02 10:04:48
* @Last Modified by: liangss
* @Last Modified time: 2020-11-02 11:49:59
* @Description: 后援咨询记录
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--咨询数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="108px">

          <el-form-item label="当前状态" prop="replyState" class="status-content">
            <template   v-if="newReplyState === 1 || newReplyState === '1'">
              <span  @click = "replyStateml(0)"><i class="red-radius color-radius"></i>忙碌</span>
            </template>
            <template  @click = "replyStatekx"   v-else-if="newReplyState === 0 || newReplyState === '0'">
              <span   @click = "replyStatekx(1)"><i class="green-radius color-radius"></i>空闲</span>
            </template>
          </el-form-item>

        </el-form>


        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="default"
              icon="el-icon-plus"
              size="mini"
              @click="handleInit"
            >援助申请</el-button>
          </el-col>
        </el-row>
        <h3><i class="el-icon-document"></i>今日被呼叫记录</h3>

        <el-table v-loading="loading" :data="advisoryRegistrationList">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="姓名" width="200" align="center" prop="replyUserName"/>
          <el-table-column label="单位" width="200" align="center" prop="replyOrganName"/>
          <el-table-column label="时间" width="350" align="center" prop="advisoryTime"/>
          <el-table-column label="结果" align="center"  prop="replyState">>
            <template slot-scope="{row: {replyState}}">
              <span v-if="+replyState === 0">等待</span>
              <span v-else-if="+replyState === 1">接受</span>
              <span v-else-if="+replyState === 2">拒绝</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="scopew">
              <template v-if="scopew.row.replyState==0||scopew.row.replyState=='0'">
                <el-button   size="mini" type="text"  @click="updateReplyState(scopew.row,1)" class="handle-btn"> 接受</el-button>
                <el-button   size="mini" type="text"   @click="updateReplyState(scopew.row,2)" class="handle-btn"> 拒绝</el-button>
              </template>
           </template>
          </el-table-column>

        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>
      </el-col>
    </el-row>

    <!-- 添加或修改材料类别 -->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="openInit" v-if="openInit" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="0px" class="dialog-table">
        <table cellspacing="0" cellpadding="0" border="0">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <!--   elementName  elementCode-->
        <!--  <tr>
            <td><i class="require">*</i><b>咨询人：</b></td>
            <td>
              <el-form-item prop="userOids">
                <treeselect class="treeselect"
                            v-model="form.userOids"
                            :options="userOptions"
                            :multiple="true"
                            noOptionsText="暂无数据"
                            noResultsText="暂无数据"
                            :show-count="true"
                            :defaultExpandLevel="1"
                            placeholder="请选择咨询人"/>
              </el-form-item>
            </td>
            <td><b>外部链接：</b></td>
            <td>
              <el-form-item>
                <el-input v-model.trim="form.url" placeholder="请输入外部链接" maxlength="100" show-word-limit/>
              </el-form-item>
            </td>
          </tr>-->

          <tr>
            <td>
              <b><i class="require">*</i>援助人：</b>
            </td>
            <td  colspan="3" style="text-align: left !important;">
              <el-form-item prop="replyUserOid">
                <el-select @change="selectModel" v-model="form.replyUserOid" placeholder="请选择援助人">
                  <el-option
                    v-for="mco in sysUserOptions"
                    :key="mco.id"
                    :label="mco.label"
                    :value="mco.id"
                  ></el-option>
                </el-select>
              </el-form-item>
            </td>
        <!--    <td>
              <b><i class="require">*</i>目录名称：</b>
            </td>
            <td >
              <el-form-item prop="catalogName">
                <el-col :span="24">
                  <el-input v-model="form.catalogName" placeholder="请输入目录名称" maxlength="50" show-word-limit />
                </el-col>
              </el-form-item>
            </td>-->
          </tr>




        </table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">保存</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { presonalpage,setReplyState,saveAfterAdvisory,getFreeUserOrgan} from "@/api/zc/afterAdvisory/afterAdvisory.js";
import Treeselect from '@riophae/vue-treeselect';
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  name: "advisoryRegistration",
  components: {Treeselect},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      advisoryRegistrationList: [],
      userOptions:[],
      sysUserOptions:[],
      afterAdvisoryry:{},
      // 弹出层标题
      title: "",
      openInit: false,
      newReplyState:0,
      organOid:"",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,

      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      form: {
        id: '', //逻辑主键
        replyUserOid:'',
      },
      // 表单校验
      rules: {
        replyUserOid: [
          { required: true, message: "请选择援助人", trigger: "blur" }
        ],
      }

    };
  },
  created() {
    this.getList();
  },

  methods: {
    // 取消按钮
    cancel() {
      this.openInit = false;
      this.reset();
    },
    replyStateml(flag){
      this.$confirm("是否确认设置为【空闲】?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return setReplyState(flag);
        })
        .then(() => {
          this.getList();
          /*this.newReplyState=0;*/
          this.msgSuccess("状态修改成功");
        })
        .catch(function() {});

    },
    replyStatekx(flag){
      this.$confirm("是否确认设置为【忙碌】?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {
          return setReplyState(flag);
        })
        .then(() => {
          this.getList();
          /*this.newReplyState=1;*/
          this.msgSuccess("状态修改成功");
        })
        .catch(function() {});
    },
    /** 查询后援咨询记录列表 */
    getList() {
      this.loading = true;
      presonalpage(this.queryParams).then(response => {
        this.advisoryRegistrationList = response.data.pageResult.data;
        this.total = response.data.pageResult.data.total;
        this.newReplyState=response.data.advistatus;
        this.organOid=response.data.organOid;
        this.loading = false;
      });
    },
    handleInit(){
      let _that = this;
      _that.openInit = true;
      this.userOptions = [];
      _that.form.replyUserOid="";
      getFreeUserOrgan(this.organOid).then(response => {
       this.sysUserOptions=response.data;
       console.log(JSON.stringify(this.sysUserOptions))
      }).catch(function() {
      });

    },
    /** 提交按钮 */
    submitForm: function() {
      let _that = this;
      _that.$refs["form"].validate(valid => {
        if (valid) {
          /*if(_that.form.replyUserOid){
            _that.form.replyUserOid=_that.form.replyUserOid.split("-")[1];
          }*/
          saveAfterAdvisory(_that.form).then(response => {
            if (response.code === 200) {
              _that.msgSuccess("保存成功");
              _that.openInit = false;
              setTimeout(() => {
                _that.getList();
              }, 10);

            }
          });
        }
      });
    },
    updateReplyState(item,replyState){
      let message0="";
      let message="";
      if(replyState==1||replyState=='1'){
        message="接受";
        message0="是否确认接受?";
      }else{
        message="拒绝";
        message0="是否确认拒绝?";
      }
      this.afterAdvisoryry=item;
      this.afterAdvisoryry.replyState=replyState;
      saveAfterAdvisory(this.afterAdvisoryry).then(response => {
        if (response.code === 200) {
          this.msgSuccess(message+"成功");
          this.openInit = false;
          setTimeout(() => {
            this.getList();
          }, 10);

        }
      });
 /*     this.$confirm(message0, "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function() {


        })*/

    }
  },
};
</script>
<style lang="scss" scoped>
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
.app-container .el-form{
  text-align: center;
}
.app-container .el-form .el-form-item__content span{
  padding: 5px 20px;
  background-color: #eee;
}
.status-content{
  cursor:pointer;
}
.color-radius{
  cursor:pointer;
  display: inline-block;
  vertical-align: middle;
  margin-right: 5px;
  width: 8px;
  height: 8px;
  border-radius: 100%;
  background-color: #ff0000;
}
.red-radius{
  background-color: #ff0000;
}
.green-radius{
  background-color: green;
}




</style>
