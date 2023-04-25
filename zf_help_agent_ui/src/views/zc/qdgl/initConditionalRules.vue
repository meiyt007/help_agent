<template>
  <div class="pre-verification" v-loading="loading">
    <div v-if="ruleType=='2'" class="zf-zc-table--title" style="margin-top: unset">秒批文案</div>
    <table v-if="ruleType=='2'" class="zf-zc-table--common zf-zc-table--td-center">
      <tr>
        <td>
          <b>通过文案：</b>
        </td>
        <td colspan="3">
          <el-input
            type="textarea"
            v-model.trim="form.mpPass"
            placeholder="请输入通过文案"
            maxlength="500"
            show-word-limit
          />
        </td>
      </tr>
      <tr>
        <td>
          <b>不通过文案：</b>
        </td>
        <td colspan="3">
          <el-input
            type="textarea"
            v-model.trim="form.mpNoPass"
            placeholder="请输入不通过文案"
            maxlength="500"
            show-word-limit
          />
        </td>
      </tr>
    </table>
    <el-button type="primary" style="margin-bottom:10px;" @click="handleAdd">新增</el-button>
      <table class="zf-zc-table--common zf-zc-table--td-center">
        <colgroup>
          <col width="20%" />
          <col width="20%" />
          <col width="20%" />
          <col width="20%" />
          <col width="12%" />
          <col width="8%" />
        </colgroup>
        <tr>
          <th v-if="ruleType=='1'"><i class="require">*</i>预检项名称</th>
          <th v-if="ruleType=='1'">预检规则</th>
          <th v-if="ruleType=='2'"><i class="require">*</i>秒批项名称</th>
          <th v-if="ruleType=='2'">秒批规则</th>
          <th>核验说明</th>
          <th>核验地址</th>
          <th>排序</th>
          <th>操作</th>
        </tr>
        <tbody v-for="(item, index) in form.tableData" :key="index">
          <tr>
            <td>
              <el-input v-model.trim="item.rulesName" placeholder maxlength="200"show-word-limit ></el-input>
            </td>
            <td>
              <el-select
                v-model.trim="item.interApiCode"
                placeholder="请选择所属服务"
                size="small"
                filterable
                style="width: 240px"
                clearable
              >
                <el-option
                  v-for="inter in interApiList"
                  :key="inter.oid"
                  :label="inter.name"
                  :value="inter.code"
                />
              </el-select>
            </td>
            <td>
              <el-input v-model.trim="item.rulesMemo" placeholder maxlength="200"show-word-limit ></el-input>
            </td>
            <td>
              <el-input v-model.trim="item.rulesAddress" placeholder maxlength="200"show-word-limit ></el-input>
            </td>
            <td>
              <el-input-number v-model.trim="item.sort" placeholder :min="1" :max="50" :step="1"  oninput ="value=value.replace(/[^\d]/g,'')"></el-input-number>
            </td>
            <td>
              <el-button type="danger" icon="el-icon-delete" circle @click="handleDelete(item)"></el-button>
            </td>
          </tr>
        </tbody>
      </table>
  </div>
</template>

<script>
import {
  delConditionalRulesById,
  queryAllConditionalRulesList,
  queryAllInterApi,
  saveOrUpdate
} from "@/api/zc/qdgl/initConditionalRules";
import {delSxServiceByOid, getServiceRegistrarByServiceOid, querySxServicePrecheckList} from "@/api/zc/qdgl/sxService";
export default {
  name: 'initConditionalRules',
  props: ['serviceOid','ruleType'],
  data () {
    return {
      // 遮罩层
      loading: false,
      interApiList: [],
      openInit: false,
      form: {
        mpPass: "",
        mpNoPass: "",
        tableData: []
      }
    }
  },
  created () {
    this.getInterApiList();
    this.getConditionalRulesList();
  },
  methods: {

    /** 获取内部接口实体的所有数据 */
    getInterApiList(){
      queryAllInterApi().then(res => {
        this.interApiList = res.data;
      })
    },

    /** 获取条件预检配置列表 */
    getConditionalRulesList(){
      this.$getResponse(queryAllConditionalRulesList({ serviceOid: this.serviceOid,ruleType:this.ruleType }), (error, res) => {
        if (error || res.code !== 200) return;
        this.form = res.data||[];
      })
    },

    /** 增加按钮操作 */
    handleAdd () {
      let sort = this.form.tableData[this.form.tableData.length - 1]?.sort;
      this.form.tableData.push({
        sort: sort ? sort + 1 : 1,
        rulesName: '',
        _id: Date.now(),
        conditionalRules: '',
        interApiCode: '',
        serviceOid: this.serviceOid,
        ruleType: this.ruleType
      });
    },

    /** 删除按钮操作 */
    handleDelete (row) {
      if (row.id) {
        this.$confirm('是否确认删除?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return true;
        }).then(() => {
          delConditionalRulesById(row.id).then(res => {
            if(res.code == 200){
              this.$message.success("删除成功！");
              this.form.tableData = this.form.tableData.filter(item => item.id !== row.id);
            }
          })
        }).catch(function () { });
      } else {
        this.form.tableData = this.form.tableData.filter(item => item._id !== row._id);
      }
    },

    /** 关闭按钮 */
    cancel () {
      this.$emit("serviceRulesClose");
    },

    /** 提交按钮 */
    submitForm () {
      this.loading = true;
      const message = this.validate();
      if (message) {
        this.loading = false;
        return this.$message.error(message);
      }
      saveOrUpdate(this.form).then(response => {
        let res = response;
        if (res.code == 200) {
          this.$message.success("保存成功！");
          this.loading = false;
          this.$emit("serviceRulesClose");
        }else{
          this.$message.error("保存失败！");
        }
      })
    },

    /** 表单验证 */
    /** 预检规则不为必填  20220925 zhaobf修改 */
    validate () {
      if(this.form.tableData){
        let rulesNameAll = this.form.tableData.some(item => !item.rulesName);
        // let interApiCodeAll = this.form.tableData.some(item => !item.interApiCode);
        if (rulesNameAll) {
          return '条件预检配置-【预检项名称】不能为空';
        // } else if (interApiCodeAll) {
        //   return '条件预检配置-【预检规则】不能为空';
        } else {
          return null;
        }
      }
    }
  }
}
</script>
<style scoped lang="scss">
</style>
