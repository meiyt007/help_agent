<template>
  <div class="navbar1">
    <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />
    <search id="header-search" class="right-menu-item show" />
    <!--<breadcrumb id="breadcrumb-container" class="breadcrumb-container" />-->
    <el-carousel ref="carousel" :interval="5000" arrow="always" height="50px" indicator-position="none" :autoplay="false" class="carousel">
      <el-carousel-item  v-for="apps in appList" >
        <div :title="app.name"  v-for="app in apps" :class="[{'carousel-item': (app_oid != app.oid)},{'carousel-item carousel-item-active': (app_oid == app.oid)}]" @click="changeApp(app.oid)">
          <i :class="app.iconName"/>
          <p>{{app.name}}</p>
        </div>
      </el-carousel-item>
    </el-carousel>
    <div class="right-menu-basic">
      <template v-if="device!=='mobile'">
        <el-tooltip content="消息" effect="dark" placement="bottom" style="width: 55px;">
          <zfsoft-message id="read-message" class="right-menu-item hover-effect" ></zfsoft-message>
        </el-tooltip>
        <el-tooltip content="全屏" effect="dark" placement="bottom">
          <screenfull id="screenfull" class="right-menu-item hover-effect" />
        </el-tooltip>
        <el-dropdown class="gjx-dropdown">
            <span class="el-dropdown-link">
              <i class="el-icon-suitcase suitcase-size"></i>
            </span>
          <el-dropdown-menu slot="dropdown">
            <!-- <el-dropdown-item>
               <zfsoft-message id="read-message1" class="right-menu-item hover-effect" ></zfsoft-message>
             </el-dropdown-item>-->
            <el-dropdown-item>
              <zfsoft-doc id="help-doc" class="right-menu-item hover-effect" ></zfsoft-doc>
            </el-dropdown-item>
            <el-dropdown-item>
              <size-select id="size-select" class="right-menu-item hover-effect" />
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <!-- <el-tooltip content="帮助文档" effect="dark" placement="bottom">
          <zfsoft-doc id="help-doc" class="right-menu-item hover-effect" ></zfsoft-doc>
        </el-tooltip>
        <screenfull id="screenfull" class="right-menu-item hover-effect" />
        <el-tooltip content="字体大小" effect="dark" placement="bottom">
          <size-select id="size-select" class="right-menu-item hover-effect" />
        </el-tooltip> -->
        <ThemePicker :initTheme="initTheme"></ThemePicker>
      </template>

      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <img :src="avatar" class="user-avatar">
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown">
          <router-link to="/user/profile">
            <el-dropdown-item>个人中心</el-dropdown-item>
          </router-link>
          <el-dropdown-item @click.native="setting = true">
            <span>布局设置</span>
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="logout">
            <span>退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>

  </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  import Breadcrumb from '@/components/Breadcrumb'
  import Hamburger from '@/components/Hamburger'
  import Screenfull from '@/components/Screenfull'
  import SizeSelect from '@/components/SizeSelect'
  import Search from '@/components/HeaderSearch'
  import ZfsoftDoc from '@/components/zfsoft/doc'
  import ZfsoftMessage from '@/components/zfsoft/message'
  import store from "../../store";
  import router from "../../router";
  import ThemePicker from '@/components/ThemePicker/MainIndex'
  import { getLoginUrl } from '@/utils/auth'
  export default {
    components: {
      Breadcrumb,
      Hamburger,
      Screenfull,
      SizeSelect,
      Search,
      ZfsoftDoc,
      ZfsoftMessage,
      ThemePicker,
    },
    data() {
      return {
        initTheme: '#409EFF', // content of theme-chalk css
      }
    },
    computed: {
      ...mapGetters([
        'sidebar',
        'avatar',
        'device',
        'appList',
        'app_oid'
      ]),
      setting: {
        get() {
          return this.$store.state.settings.showSettings
        },
        set(val) {
          this.$store.dispatch('settings/changeSetting', {
            key: 'showSettings',
            value: val
          })
        }
      }
    },
    created() {
      this.changeClassName();
    },
    methods: {
      toggleSideBar() {
        this.$store.dispatch('app/toggleSideBar')
      },
      changeClassName(){
        if(null!=store.getters.skinClassname && ''!=store.getters.skinClassname){
          let val = store.getters.skinClassname;
          this.initTheme = val;
        }
      },
      changeApp(appOid){
        this.$store.dispatch('setAppOid',appOid);
        //this.$store.dispatch("GenerateRoutes")
        const roles = [];
        store.dispatch('GenerateRoutes').then(accessRoutes => {
          router.addRoutes(accessRoutes) // 动态添加可访问路由表
          //next({ ...to, replace: true }) // hack方法 确保addRoutes已完成
        })
        //关闭所有的主页面
        this.$store.dispatch('tagsView/delAllViews').then(({ visitedViews }) => {
          this.$router.push('/');
        });

      },
      async logout() {
        this.$confirm('确定注销并退出系统吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$store.dispatch('LogOut').then(() => {
            //location.reload()
            // location.href= getLoginUrl();
            this.$router.push(getLoginUrl())
          })
        })
      },

    }
  }
</script>

<style lang="scss">
  .navbar1 {
    display: flex;
    flex-direction: row;
    height: 50px;
    justify-content: space-between;
    overflow: hidden;
    position: relative;
    background: #fff;
    box-shadow: 0 1px 4px rgba(0,21,41,.08);

    .hamburger-container {
      line-height: 46px;
      height: 100%;
      float: left;
      cursor: pointer;
      transition: background .3s;
      -webkit-tap-highlight-color:transparent;

      &:hover {
        background: rgba(0, 0, 0, .025)
      }
    }

    .breadcrumb-container {
      float: left;
    }

    .errLog-container {
      display: inline-block;
      vertical-align: top;
    }

    .right-menu-basic {
      position: relative;
      float: right;
      height: 100%;
      line-height: 50px;

      &:focus {
        outline: none;
      }

      .right-menu-item {
        display: inline-block;
        padding: 0 15px;
        height: 100%;
        font-size: 18px;
        color: #5a5e66;
        vertical-align: text-bottom;
        position: relative;
        &:after{
          position: absolute;
          right: 0;
          top: 17px;
          content: '';
          width: 1px;
          height: 15px;
          background-color: #d8dce5;
        }
        &:last-child:after{
          width: 0;
        }

        &.hover-effect {
          cursor: pointer;
          transition: background .3s;

          &:hover {
            background: rgba(0, 0, 0, .025)
          }
        }
      }

      .avatar-container {
        margin-right: 30px;

        .avatar-wrapper {
          margin-top: 10px;
          position: relative;

          .user-avatar {
            cursor: pointer;
            width: 30px;
            height: 30px;
            border-radius: 10px;
            border-radius: 100%;
          }

          .el-icon-caret-bottom {
            cursor: pointer;
            position: absolute;
            right: -20px;
            top: 10px;
            font-size: 12px;
          }
        }
      }
    }
  }
  .carousel{
    flex-grow: 1;
    min-width: 500px;
    height: 50px;
    overflow-y: hidden;
    display: inline-block;
    vertical-align: top;
    margin-left: 20px;
  }
  .carousel-item{
    width: 100px;
    display: inline-block;
    vertical-align: middle;
    text-align: center;
    font-size: 14px;
    color: #838c98;
    margin: 2px 0 0 0;
    padding-bottom: 7px;
    box-sizing:border-box;
    cursor: pointer;
  }
  .carousel-item:first-child{
    margin-left: 65px;
  }
  .carousel-item p{
    margin: 5px 0 0 0;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    font-size: 14px;
  }
  .carousel-item-active{
    border-bottom: 1px solid #304156;
  }
  .carousel>>>.el-carousel__arrow{
    width: 20px;
    height: 40px;
    border-radius: 5px;
    background-color: #f1f4f8;
    color: #304156;
    opacity: 0.8;
  }
  .carousel>>>.el-carousel__arrow:hover{
    opacity: 1;
  }

  .navbar .right-menu-item{
    display: inline-block;
    padding: 0 10px;
    height: 100%;
    font-size: 18px;
    color: #5a5e66;
    vertical-align: text-bottom;
    position: relative;
    line-height: 48px;
  }
  .gjx-dropdown{
    vertical-align: top;
    cursor: pointer;
  }
  .gjx-dropdown .el-dropdown-menu--medium .el-dropdown-menu__item{
    text-align: center;
    font-size: 12px;
    color: #333;
    padding: 0;
    display: inline-block;
    vertical-align: middle;
    text-align: center;
  }
  .el-dropdown-menu .app-container{
    padding: 0;
  }
  .el-dropdown-menu .right-menu-item{
    display: inline-block;
    vertical-align: middle;
  }
  .suitcase-size{
    font-size: 18px;
    padding: 0px 10px;
  }

</style>
