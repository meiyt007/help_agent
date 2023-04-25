<template>
  <div class="canvas_box">
    <FormTemplate :templateOid="templateOid" />
  </div>
</template>

<script>
  import '@babel/polyfill'
  export default {
    components: {},
    data() {
      return {
        templateOid: "",
      };
    },
    created() {
      const queryPar = this.$route.query;
      this.templateOid = queryPar.templateOid;
    },
  };
</script>

<style>
  /*
  body {
    background-color: #eceff7;
  } */
  .app-header {
    margin-bottom: 0px !important;
    box-shadow: 1px 3px 10px #e8ebf2 !important;
    background-color: #fff;
  }
  .app-header .app-header-title {
    color: #3d495e !important;
    font-size: 25px !important;
    position: relative;
    padding-left: 35px;
  }
  .app-header .app-header-title:before {
    position: absolute;
    content: "";
    background: url(../../assets/logo/frender-logo.png) no-repeat center top;
    background-size: 100% 100%;
    width: 30px;
    height: 30px;
    border-radius: 100%;
    left: 0;
    top: 0;
  }
  .app-header .app-header-right .el-button {
    color: #325ff1;
  }

  .f-render {
    height: calc(100vh - 65px) !important;
    background-color: #eceff7;
    padding-top: 10px;
    padding-bottom: 10px;
  }
  .f-render,
  .f-render-header,
  .f-render > .multipane-resizer {
    border: none;
  }

  .f-render .f-render-left {
    background-color: #e7e9ef;
    box-shadow: 0px 0px 10px #e7eaf2;
    overflow-x: hidden;
    z-index: 2 !important;
  }
  .f-render .f-render-comp-searcher {
    padding: 0;
    background-color: #f9fbff;
    height: 40px;
    line-height: 40px;
  }
  .f-render .f-render-comp-searcher .el-input__inner {
    height: 40px;
    line-height: 40px;
    border: none;
    text-indent: 12px;
  }
  .f-render .f-render-comps {
    padding: 25px 5px 0px 5px;
    box-sizing: border-box;
    width: 260px;
    margin-top: 0;
    margin-bottom: 0;
  }
  .f-render .f-render-left .f-render-scrollarea {
    overflow-x: hidden !important;
    width: 260px;
    position: inherit;
    height: calc(100% - 50px);
  }
  .f-render .f-render-left .ps__rail-x {
    left: -999px !important;
    bottom: 0px !important;
  }
  .f-render .f-render-comps .f-render-comp {
    width: 113px;
    height: 32px;
    line-height: 32px;
    padding: 0;
    border: none;
    background-color: #fff;
    box-shadow: 1px 1px 10px #e7eaf2;
    border: 1px solid inherit;
    margin: 3px 5px;
  }
  .f-render .basic-components .title {
    font-weight: normal;
    font-size: 16px;
    color: #3d495e;
  }
  .f-render > .multipane-resizer:before {
    content: "";
    background: url(../../assets/image/resizer-icon.png) no-repeat center top;
    background-size: 100% 100%;
    border: none;
    width: 15px;
    height: 40px;
    margin-left: -15px;
    z-index: 1;
  }
  .f-render > .multipane-resizer {
    margin: 0 10px;
  }
  .f-render .set-button {
    right: -155px;
    top: -25px;
  }
  .f-render .set-button .el-button {
    background-color: #ffffff;
    border: none;
    color: #3d495e;
    box-shadow: 0 0 10px #dde0eb;
  }
  .f-render .sortable-ghost {
    background-color: #dee4fc !important;
    border: 1px solid #325ff1 !important;
    color: #325ff1 !important;
  }
  .f-render .f-render-main {
    z-index: 10 !important;
  }

  .f-render .f-render-main .main-header {
    margin-bottom: 10px;
    background-color: #fff;
    border-top-left-radius: 5px;
    border-top-right-radius: 5px;
  }
  .f-render .f-render-main .main-content{
    height: calc(100% - 70px);
  }
  .f-render .f-render-main .main-content .ele-form .el-row .el-col-18{
    width: 98%;
  }


  .f-render .f-render-main .main-header .main-header-btns {
    padding: 0;
  }
  .f-render .f-render-main .main-header .el-button {
    padding: 0;
    margin: 0;
    vertical-align: top;
    color: #5d7092;
    height: 60px;
    width: 90px;
  }
  .f-render .f-render-main .main-header div:last-child .el-button:last-child {
    color: #fff;
    border-bottom-left-radius: 0;
    border-top-left-radius: 0;
  }
  .f-render .f-render-main .main-header .el-button:hover {
    color: #fff;
    background-color: #325ff1;
    border-radius: 0;
  }
  .f-render .f-render-header > div:first-child .el-button:first-child:hover {
    border-top-left-radius: 5px;
  }
  .f-render
  .f-render-main
  .main-header
  .main-header-btns
  > div:last-child
  .el-button:last-child:hover {
    border-top-right-radius: 5px;
  }

  .f-render .f-render-main .main-header .el-button > i {
    font-size: 17px;
  }
  .f-render .f-render-main .main-header .el-button span {
    display: block;
    text-align: center;
    margin-top: 5px;
  }
  .f-render .f-render-main .main-content {
    background-color: #fff;
    box-shadow: 0px 0px 10px #e7eaf2;
    z-index: 2;
    border: 1px solid #eceff7;
    border-radius: 5px;
  }
  .f-render .main-content .form-item .form-item-copy-btn,
  .f-render .main-content .form-item .form-item-delete-btn {
    background-color: #405eff;
  }
  .f-render .main-content .form-item-active {
    border: 1px dashed #405eff !important;
  }
  .f-render .el-button--primary {
    color: #fff;
    background-color: #405eff;
    border-color: #405eff;
  }
  .f-render .el-button--default {
    color: #5d7092;
  }

  .f-render > div:nth-of-type(4):before {
    left: 17px;
    content: "";
    background: url(../../assets/image/right-arrow.png) no-repeat center top;
    background-size: 100% 100%;
    border: none;
    width: 15px;
    height: 40px;
    z-index: 1;
    position: absolute;
  }

  .f-render .f-render-right .f-render-header {
    height: 40px;
    line-height: 40px;
  }
  .f-render .f-render-right-link {
    background-color: #f2f4f7;
    padding: 8px 0;
    box-sizing: border-box;
    margin: 0 10px 10px 10px;
    padding-left: 25px;
    width: 295px;
    border-left: none;
    position: relative;
  }
  .f-render .f-render-right-link:before {
    position: absolute;
    top: 13px;
    left: 13px;
    content: "";
    width: 7px;
    height: 7px;
    background-color: #3a5ffe;
    border-radius: 100%;
    border: 2px solid #cad4f9;
  }
  .f-render .el-link.el-link--primary {
    color: #325ff1;
  }
  .f-render .el-link.el-link--primary:hover {
    color: #4770f5;
  }
  .f-render .f-render-right .f-render-right-content {
    padding: 15px 0;
    background-color: #e7e9ef;
  }
  .f-render .f-render-right .el-tabs__nav-wrap {
    padding: 0;
  }
  .f-render .f-render-right .f-render-scrollarea {
    height: calc(100% - 40px);
    border: 1px solid #cfd5e3;
    background-color: #e7e9ef;
    -webkit-box-shadow: -2px 0px 10px #d1d7e4;
    box-shadow: -2px 0px 10px #d1d7e4;
  }

  .f-render .el-tabs__nav.is-stretch {
    display: block;
  }
  .f-render .f-render-right .el-tabs__header {
    padding-top: 0;
    padding-right: 0;
    margin: 0;
  }
  .f-render .f-render-right .el-tabs__item {
    background: url(../../assets/image/tabs-bg.png) no-repeat center top;
    background-size: 100% 100%;
    display: inline-block;
    width: 114px;
    height: 40px;
    line-height: 40px;
    font-size: 13px;
    padding: 0;
    color: #73798c;
  }
  .f-render .f-render-right .el-tabs__item:last-child {
    width: 85px;
  }

  .f-render .f-render-right-search {
    padding: 0;
  }
  .f-render .f-render-right-search .el-input__inner {
    border: none;
    border-radius: 0;
    text-indent: 20px;
  }
  .f-render .f-render-right-content .ele-form .el-row .el-col-20 {
    padding: 0 !important;
    width: 90%;
  }
  .f-render .el-form--label-top .el-form-item__label {
    padding-left: 0 !important;
  }
  .f-render .f-render-right-content .ele-form .el-row .el-col-24 {
    width: 100%;
  }
  .f-render .el-form-item--small.el-form-item {
    margin-top: 15px;
  }
  .f-render .obj-info {
    margin: 5px 20px 0px 10px;
    color: #405eff;
  }
  .f-render .f-render-right-content label.el-form-item__label {
    position: relative;
    padding-left: 10px !important;
  }

  .f-render .f-render-right-content label.el-form-item__label:after {
    position: absolute;
    content: "";
    width: 3px;
    height: 13px;
    background-color: #325ff1;
    left: 0;
    top: 10px;
  }

  .f-render .el-radio-button__orig-radio:checked + .el-radio-button__inner {
    color: #ffffff;
    background-color: #325ff1;
    border-color: #325ff1;
    -webkit-box-shadow: -1px 0 0 0 #325ff1;
    box-shadow: -1px 0 0 0 #325ff1;
  }
  .f-render .el-select .el-input.is-focus .el-input__inner {
    border-color: #325ff1;
  }
  .f-render .el-textarea__inner:focus {
    outline: none;
    border-color: #325ff1;
  }
  .f-render .el-tabs__active-bar {
    background-color: #325ff1;
  }
  .f-render .el-tabs__item.is-active,
  .f-render .el-tabs__item:hover {
    color: #325ff1;
  }
  /* .el-dialog {
      border: 7px solid rgba(0, 0, 0, 0.5);
      border-radius: 5px;
    } */
  .f-render .el-tabs__active-bar {
    width: 0 !important;
  }
  .f-render .el-tabs__item.is-active,
  .f-render .el-tabs__item:hover {
    color: #3555f0;
  }
  .f-render .f-render-right .el-tabs__item.is-active {
    background: url(../../assets/image/tabs-bg-active.png) no-repeat center top;
    background-size: 100% 100%;
  }
  .f-render .label-item {
    display: inline-block;
    vertical-align: top;
    width: 32px;
    height: 32px;
    background-color: #eceffb;
    text-align: center;
  }
  .f-render .f-render-comps .f-render-comp span {
    padding-left: 10px;
    font-size: 13px;
  }
  .el-transfer-panel {
    width: 245px;
  }

  .el-button.is-disabled, .el-button.is-disabled:focus, .el-button.is-disabled:hover {
    color: #c0c4cc!important;
  }
</style>
