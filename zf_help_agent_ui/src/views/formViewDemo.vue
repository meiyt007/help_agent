/**
* @Author: xiayj
* @date:2021-07-15
*/
<template>
  <div class="app-container">
    <el-tabs v-model="activeName">
      <el-tab-pane label="基本信息" name="baseInfo">
        <img v-if="type === 1" src="@/assets/image/img/FORM20210814WAVFO47G.png" />
        <img v-if="type === 2" src="@/assets/image/img/FORM20210814OYFFDOJY.png" />
        <img v-if="type === 3" src="@/assets/image/img/FORM202108146IHDN7TD.png" />
      </el-tab-pane>
      <el-tab-pane label="经营信息" name="bussInfo">
        <img v-if="type === 1" src="@/assets/image/img/FORM20210814YV6RVGRD.png" />
        <img v-if="type === 2" src="@/assets/image/img/FORM20210814EV2DJVLA.png" />
        <img v-if="type === 3" src="@/assets/image/img/FORM202108145CWSR3QC.png" />
      </el-tab-pane>
      <el-tab-pane label="人员信息" name="personInfo">
        <img v-if="type === 1" src="@/assets/image/img/FORM20210814HDM5JX4A.png" />
        <img v-if="type === 2" src="@/assets/image/img/FORM20210814D8FRV0T6.png" />
        <img v-if="type === 3" src="@/assets/image/img/FORM20210814EKNULBOB.png" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>


  export default {
    name: "formView",
    //定义获取父类传过来值的格式
    props: ["formCode"],
    data() {
      return {
        activeName: 'baseInfo',
        type:1,
      };
    },
    created() {
      this.init();
    },
    watch: {
      formCode: {
        deep: true,
        handler: function () {
          console.log("修改");
           this.init();
        },
      },
    },
    methods: {
      init(){
        console.log(this.formCode);
        let formCode = this.formCode;
        if(formCode === 'FORM20210814WAVFO47G'){
          this.activeName = 'baseInfo';
          this.type = 1;
        }else if(formCode === 'FORM20210814YV6RVGRD'){
          this.activeName = 'bussInfo';
          this.type = 1;
        }else if(formCode === 'FORM20210814HDM5JX4A'){
          this.activeName = 'personInfo';
          this.type = 1;
        }else if(formCode === 'FORM20210814OYFFDOJY'){
          this.activeName = 'baseInfo';
          this.type = 2;
        }else if(formCode === 'FORM20210814D8FRV0T6'){
          this.activeName = 'personInfo';
          this.type = 2;
        }else if(formCode === 'FORM20210814EV2DJVLA'){
          this.activeName = 'bussInfo';
          this.type = 2;
        }else if(formCode === 'FORM202108146IHDN7TD'){
          this.activeName = 'baseInfo';
          this.type = 3;
        }else if(formCode === 'FORM202108145CWSR3QC'){
          this.activeName = 'bussInfo';
          this.type = 3;
        }else if(formCode === 'FORM20210814EKNULBOB'){
          this.activeName = 'personInfo';
          this.type = 3;
        }

      }

    }
  }
</script>
