<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6" :xs="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>个人信息</span>
          </div>
          <div>
            <div class="text-center">
              <userAvatar :user="user" />
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <svg-icon icon-class="user" />姓名
                  <el-tooltip class="item" effect="dark" :content="user.name" placement="top-end">
                    <div class="pull-right">{{ user.name }} </div>
                  </el-tooltip>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="phone" />手机号码
                <div class="pull-right">{{ user.mobile }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="email" />用户邮箱
                <el-tooltip class="item" effect="dark" :content="user.email" placement="top-end">
                  <div class="pull-right">{{ user.email }} </div>
                </el-tooltip>
              </li>
              <!--<li class="list-group-item">
                <svg-icon icon-class="tree" />所属区划
                <div class="pull-right" v-if="user.districtName">{{ user.districtName }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="peoples" />所属机构
                <div class="pull-right" v-if="user.organName">{{ user.organName }}</div>
              </li>-->
              <li class="list-group-item">
                <svg-icon icon-class="peoples" />职务
                <el-tooltip class="item" effect="dark" :content="user.position" placement="top-end">
                  <div class="pull-right">{{ user.position }} </div>
                </el-tooltip>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="date" />创建日期
                <div class="pull-right">{{ user.createDate }}</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18" :xs="24">
        <el-card>
          <div slot="header" class="clearfix">
            <span>基本资料</span>
          </div>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="userinfo">
              <userInfo :user="user" />
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">
              <resetPwd :user="user" />
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import store from "@/store";
  import userAvatar from "./userAvatar";
  import userInfo from "./userInfo";
  import resetPwd from "./resetPwd";
  import { getInfo ,getKey} from '@/api/login';
  import '@riophae/vue-treeselect/dist/vue-treeselect.css';
  import { getToken, setToken, removeToken } from '@/utils/auth';


  export default {
    name: "Profile",
    components: { userAvatar, userInfo, resetPwd },
    data() {
      return {
        user: {},
        roleGroup: {},
        postGroup: {},
        activeTab: "userinfo"
      };
    },
    created() {
      this.getUser();
    },
    methods: {
      getUser() {
        getInfo().then(response => {
          this.user = response.data.user;
          this.roleGroup = response.data.roleList;
          if(response.data.user&&response.data.user.headImageAttaOid){
            let img = process.env.VUE_APP_BASE_API + "/platform/security/atta/imageDisplay/"+response.data.user.headImageAttaOid + '?access_token=' +  getToken();
            store.commit('SET_AVATAR', img);
          }
          getKey().then(res => {
            this.user.publicKey = res.data.publicKey
            this.user.config = res.data.config
          })
        });
      }
    }
  };
</script>
<style scoped>
  .pull-right{
    text-overflow:ellipsis;
    overflow:hidden;
    white-space:nowrap;
    width:50%;
  }
</style>
