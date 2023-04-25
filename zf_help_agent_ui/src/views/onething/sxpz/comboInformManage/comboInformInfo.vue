/*
* @Description: 一件事告知单信息页面
* @Author: dxl
**/
<template>
  <div>
    <table cellspacing="0" cellpadding="0" border="0" v-loading="loading" class="zf-zc-table">
      <colgroup>
        <col width="20%" />
        <col width="80%" />
      </colgroup>
      <tr>
        <td>
          <b>所属主题：</b>
        </td>
        <td>
          {{form.comboDirectoryName}}
        </td>
      </tr>

      <tr>
        <td>
          <b>告知单标题：</b>
        </td>
        <td>
          {{form.informTitle}}
        </td>
      </tr>
      <tr>
        <td>
          <b>排序号：</b>
        </td>
        <td>
          {{form.sort}}
        </td>
      </tr>
      <tr>
        <td height="100%">
          <b>告知单内容：</b>
        </td>
        <td height="100%">
          <div class="inform-content" v-html=form.informContent></div>
        </td>
      </tr>
    </table>
  </div>

</template>
<script>
  import {
    init
  } from "@/api/onething/sxpz/inform/comboInform.js";

  export default {
    name: 'ComboInformInfo',
    props: ['comboInformOid'],
    data() {
      return {
        // 遮罩层
        loading: true,
        //目录信息
        form: {}
      };
    },
    created() {
      this.getInit();
    },
    methods: {
      /*初始化告知单信息*/
      getInit() {
        this.loading = true;
        init(this.comboInformOid).then((response) => {
          this.form = response.data;
          this.loading = false;
        });
      },
    }
  };

</script>
<style lang="scss" scoped>
  .other-table table tr td {
    color: #606266;
    text-align: left !important;
  }
  .inform-content >>> img{
    width: 100%;
  }

</style>
