<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="表单设计" name="first">
        <Main v-if="show0" :ref="`item0`" :authorizeKey="authorizeKey"></Main>
      </el-tab-pane>
      <el-tab-pane label="模块管理" name="second">
        <Module v-if="show1" :ref="`item1`" :authorizeKey="authorizeKey"></Module>
      </el-tab-pane>
      <el-tab-pane label="数据源管理" name="third">
        <Datasource v-if="show2" :ref="`item2`" :authorizeKey="authorizeKey"></Datasource>
      </el-tab-pane>
      <el-tab-pane label="存储对象" name="fourth">
        <Object v-if="show3" :ref="`item3`" :authorizeKey="authorizeKey"></Object>
      </el-tab-pane>
      <el-tab-pane label="填报管理" name="five">
        <Report v-if="show4" :ref="`item4`" :authorizeKey="authorizeKey"></Report>
      </el-tab-pane>
      <el-tab-pane v-if="null==userOid || ''==userOid || undefined == userOid" label="成员管理" name="seven">
        <Member v-if="show5" :ref="`item5`" :authorizeKey="authorizeKey"></Member>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
  import Member from '@/views/form/member';
  import Module from '@/views/form/module';
  import Datasource from '@/views/form/datasource';
  import Object from '@/views/form/object';
  import Main from '@/views/form/main';
  import Report from '@/views/form/report';
  export default {
    props:['authorizeKey','userOid'],
    components: { Member,Module,Datasource,Object,Main,Report },
    data() {
      return {
        activeName: 'first',
        show0:false,
        show1:false,
        show2:false,
        show3:false,
        show4:false,
        show5:false,
      };
    },
    created() {
      this.show0 = true;
    },
    methods: {
      handleClick(tab, event) {
        // if(0!=tab.index){
        //    this.show0 = false;
        // }
        //console.log(tab.index);
        this.$refs['item' + tab.index] && this.$refs['item' + tab.index].init && this.$refs['item' + tab.index].init();
        this.$set(this,'show'+tab.index,true);
      }
    }
  };
</script>
<style scoped>
  .app-container{
    padding: 0px !important;
  }
</style>
