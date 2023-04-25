<template>
  <div class="sidebar-logo-container" :class="{'collapse':collapse}">
    <transition name="sidebarLogoFade">
      <router-link v-if="collapse" key="collapse" class="sidebar-logo-link" to="/">
        <img v-if="logo" :src="logo" class="sidebar-logo">
        <h1 v-else class="sidebar-title" :title = "title">{{ reversedTitle }}</h1>
      </router-link>
      <router-link v-else key="expand" class="sidebar-logo-link" to="/">
        <img id="sidebar-logo"  v-if="logo" :src="logo" class="sidebar-logo">
        <h1 id="marquee" class="sidebar-title spanStyle" :title = "title"><span>{{ title }}</span><em>{{type}}</em></h1>
      </router-link>
    </transition>
  </div>
</template>

<script>
  import logoImg from '@/assets/logo/logo.png'

  export default {
    name: 'SidebarLogo',
    props: {
      collapse: {
        type: Boolean,
        required: true
      }
    },
    computed: {
      // 计算属性的 getter
      reversedTitle: function () {
        if (this.title) {
          if(this.title.length>6){
            return this.title.slice(0,6)+'...';
          }
          return this.title;
        }
        return ''
      }

    },
    data() {
      return {
        title:this.Common.simpleTitle,
        type:this.Common.version,
        logo: logoImg
      }
    },
    mounted: function() {
      if (this.title) {
        if(this.title.length>6){
          this.move();
        }
      }
    },
    methods:{
      move() {
        // 获取内容区宽度
        let marquee = document.getElementById("marquee");
        var width = marquee.offsetWidth; //宽度
        width=140;
        let log = document.getElementById("sidebar-logo");
        log.style.marginRight = "20px";
        let speed = -20; // 位移距离
        // 设置位移
        setInterval(function() {
          speed = speed - 1;
          // 如果位移超过文字宽度，则回到起点
          if (-speed >= width-40) {
            speed = -20;
          }
          marquee.style.width = width+"px";
          marquee.style.paddingLeft = '10px';
          marquee.style.transform = "translateX(" + speed + "px)";
        }, 200);
      }
    }
  }
</script>

<style lang="scss" scoped>
  .sidebarLogoFade-enter-active {
    transition: opacity 1.5s;
  }

  .sidebarLogoFade-enter,
  .sidebarLogoFade-leave-to {
    opacity: 0;
  }

  .sidebar-logo-container {
    position: relative;
    width: 100%;
    height: 50px;
    line-height: 50px;
    background: #2d506b;
    text-align: center;
    overflow: hidden;

    & .sidebar-logo-link {
      height: 100%;
      width: 100%;

      & .sidebar-logo {
        width: 28px;
        height: 26px;
        vertical-align: middle;
        margin-right: 3px;
      }

      & .sidebar-title {
        display: inline-block;
        margin: 0;
        color: #fff;
        font-weight: 600;
        line-height: 50px;
        font-size: 20px;
        font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
        vertical-align: middle;
        em{
          font-style: normal;
          font-size: 12px;
          display: inline-block;
          vertical-align: top;
          font-weight: normal;
          margin-left: 3px;
        }
      }
    }

    &.collapse {
      .sidebar-logo {
        margin-right: 0px;
      }
    }
  }
  .spanStyle{
    white-space: nowrap;  /*强制span不换行*/
    //display: inline-block;  /*将span当做块级元素对待*/
    //overflow: hidden;  /*超出宽度部分隐藏*/
    line-height: 0.9;  /*数字与之前的文字对齐*/
  }
</style>
