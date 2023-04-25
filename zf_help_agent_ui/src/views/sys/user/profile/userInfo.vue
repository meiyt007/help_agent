<template>
  <el-form ref="form" :model="user" :rules="rules" label-width="80px">
    <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
      <el-input v-show="false" v-model="user.userOid" />
      <el-input v-show="false" v-model="user.id" />
      <el-input v-show="false" v-model="user.type" />
      <el-input v-show="false" v-model="user.sort" />
      <colgroup>
        <col width="20%" />
        <col width="30%" />
        <col width="20%" />
        <col width="30%" />
      </colgroup>
      <!--<tr>
          <td><i class="require">*</i><b>所属区划：</b></td>
          <td>
            <el-form-item prop="districtOid">
              <treeselect
                v-model="user.districtOid"
                :options="districtOptions"
                noOptionsText="暂无数据"
                :defaultExpandLevel="1"
                placeholder="请输入所属区划"
              />
            </el-form-item>
          </td>

          <td><i class="require">*</i><b>所属机构：</b></td>
          <td>
            <el-form-item prop="organOid">
              <treeselect
                v-model="user.organOid"
                :options="editOrganOptions"
                noOptionsText="暂无数据"
                :defaultExpandLevel="1"
                placeholder="请输入所属机构"/>
            </el-form-item>
          </td>
        </tr>-->
      <tr>
        <td><i class="require">*</i><b>姓名：</b></td>
        <td>
          <el-form-item prop="name" label-width="0">
            <el-input
              v-model="user.name"
              placeholder="请输入用户姓名"
              maxlength="50"
              show-word-limit
            />
          </el-form-item>
        </td>

        <td><b>出生年月：</b></td>
        <td>
          <!--<el-form-item prop="birthdate">
              <el-date-picker
                v-model="user.birthdate"
                value-format="yyyy-MM-dd"
                type="date"
                placeholder="选择出生年月">
              </el-date-picker>
            </el-form-item>-->
          <el-form-item prop="birthdate" label-width="0">
            <el-date-picker
              v-model="user.birthdate"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="选择出生年月"
            >
            </el-date-picker>
          </el-form-item>
        </td>
      </tr>
      <tr>
        <td><b>用户性别：</b></td>
        <td>
          <el-form-item label-width="0">
            <el-radio v-model="user.sex" label="M">男</el-radio>
            <el-radio v-model="user.sex" label="W">女</el-radio>
          </el-form-item>
        </td>

        <td><i class="require">*</i><b>职务：</b></td>
        <td>
          <el-form-item prop="position" label-width="0">
            <el-input
              v-model="user.position"
              placeholder="请输入职务"
              maxlength="50"
              show-word-limit
            />
          </el-form-item>
        </td>
      </tr>
      <tr>
        <td><i class="require">*</i><b>身份证号：</b></td>
        <td>
          <el-form-item prop="cardNo" label-width="0">
            <el-input
              v-model="user.cardNo"
              placeholder="请输入身份证号"
              maxlength="18"
              show-word-limit
            />
          </el-form-item>
        </td>

        <td><i class="require">*</i><b>邮箱：</b></td>
        <td>
          <el-form-item prop="email" label-width="0">
            <el-input
              v-model="user.email"
              placeholder="请输入邮箱"
              maxlength="100"
              show-word-limit
            />
          </el-form-item>
        </td>
      </tr>
      <tr>
        <td><i class="require">*</i><b>手机：</b></td>
        <td>
          <el-form-item prop="mobile" label-width="0">
            <el-input
              v-model="user.mobile"
              placeholder="请输入手机"
              maxlength="11"
              show-word-limit
            />
          </el-form-item>
        </td>

        <td><b>座机：</b></td>
        <td>
          <el-form-item prop="telphone" label-width="0">
            <el-input
              v-model="user.telphone"
              placeholder="请输入座机"
              maxlength="13"
              show-word-limit
            />
          </el-form-item>
        </td>
      </tr>
      <!--<tr>
          <td><b>类型：</b></td>
          <td>
            <el-form-item prop="type">
              <el-select v-model="user.type" placeholder="请选择类型">
                <el-option
                  v-for="dict in typeOptions"
                  :key="dict.dictOid"
                  :label="dict.name"
                  :value="dict.dictOid"
                ></el-option>
              </el-select>
            </el-form-item>
          </td>
        </tr>-->
    </table>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">保存</el-button>
      <el-button type="danger" size="mini" @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { validIDCard, validatePhoneTwo } from '@/utils/validate'
import { queryOrganTree } from "@/api/sys/organ";
import { queryDistrictSimpleTree } from "@/api/sys/district";
import { getDictList } from "@/api/sys/dict";
import { save } from '@/api/sys/user'

export default {
  components: { Treeselect },
  props: {
    user: {
      type: Object
    }
  },
  data () {
    return {
      // 表单校验
      rules: {
        districtOid: [
          { required: true, message: '所属区划不能为空', trigger: 'blur' }
        ],
        organOid: [
          { required: true, message: '所属机构不能为空', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '姓名不能为空', trigger: 'blur' }
        ],
        cardNo: [
          { required: true, message: '身份证号码不能为空', trigger: 'blur' },
          { validator: validIDCard, trigger: 'blur' }
        ],
        mobile: [
          { required: true, message: '手机号码不能为空', trigger: 'blur' },
          { validator: validatePhoneTwo, message: '请输入正确的手机号码', trigger: 'blur' }
        ],
        telphone: [
          { validator: validatePhoneTwo, message: '请输入正确的电话号码', trigger: 'blur' }
        ],
        position: [
          { required: true, message: '职务不能为空', trigger: 'blur' }
        ],
        email: [
          { required: true, type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ]
      },
      //区划数据
      districtOptions: [],
      //修改的机构数据
      editOrganOptions: [],
      typeOptions: []
    };
  },
  watch: {
    'user.districtOid': function (val, oldVal) {
      //区划加载
      this.getEditOrganTree(val)
    }
  },
  created () {
    //this.getDistrictTree()
    this.getUserTypt()
    //this.getEditOrganTree(this.user.districtOid)
  },
  methods: {
    submit () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          save(this.user).then(res => {
            if (res.code === 200) {
              this.msgSuccess('保存成功')
            }
          })
        }
      });
    },
    close () {
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({ path: "/home" });
    },
    /** 获取区划树 */
    getDistrictTree (districtOid) {
      queryDistrictSimpleTree(districtOid).then(response => {
        this.districtOptions = response.data;
      });
    },
    getEditOrganTree (districtOid) {
      if (districtOid) {
        queryOrganTree(districtOid).then(response => {
          this.editOrganOptions = response.data;
        });
      } else {
        this.editOrganOptions = []
        this.user.organOid = null
      }
    },
    //用户类型
    getUserTypt () {
      getDictList('YHLX').then(response => {
        this.typeOptions = response.data
      }
      )
    },
  }
};
</script>
