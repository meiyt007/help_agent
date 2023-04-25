/**
* @Author: liangxm
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--目录列表数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="108px">
          <el-form-item label="目录名称" prop="comboDirectoryName">
            <el-input v-model.trim="queryParams.comboDirectoryName" placeholder="请输入目录名称" clearable size="small"
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置
            </el-button>
          </el-form-item>
        </el-form>
        <el-table v-loading="loading" :data="comboDirectoryList" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column label="分类名称" align="center" prop="themeName" :show-overflow-tooltip="true" />
          <!--<el-table-column label="目录编号" align="center" prop="comboDirectoryCode" />-->
          <el-table-column label="目录名称" align="center" prop="comboDirectoryName" :show-overflow-tooltip="true" />
          <el-table-column label="所属区划" width="110" align="center" prop="districtName" :show-overflow-tooltip="true" />
          <el-table-column label="主办部门" width="110" align="center" prop="mainOrganName" :show-overflow-tooltip="true" />
          <el-table-column label="操作" width="500" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="handleView(scope.row)"
                v-hasPermi="['combo:directory:view']">查看</el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" @click="handleOption(scope.row)"
                v-hasPermi="['combo:directory:update']">配置选项</el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" @click="queryMaterialList(scope.row)"
                v-hasPermi="['combo:directory:update']">选项材料与证照</el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-tubiao" @click="handleSituationList(scope.row)"
                v-hasPermi="['combo:directory:delete']">配置情形</el-button>
              <!--    <el-button
                    size="mini"
                    type="text"
                    icon="iconfont zfsoft-penzhi"
                    @click="queryOptionSituationList(scope.row)"
                    v-hasPermi="['combo:directory:service']"
                  >查看选项情形</el-button>-->
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
          @pagination="getList" />
      </el-col>
    </el-row>
    <!-- 查看选项情形列表 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="title" :visible.sync="openOptionSituationList" width="900px"
      append-to-body>
      <div>
        <div class="zf-zc-table--title">热门情形列表信息</div>
        <el-table v-loading="loading" :data="hotComboSituationList">
          <el-table-column label="序号" width="70" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="情形名称" align="center" width="300" prop="situationName" :show-overflow-tooltip="true" />
          <el-table-column label="排序号" align="center" width="300" prop="sort" :show-overflow-tooltip="true" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">

              <el-button size="mini" type="text" icon="iconfont zfsoft-chakan"
                @click="openSituationoptionList(scope.row)" v-hasPermi="['sys:reg:view']">查看情形选项配置</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="zf-zc-table--title">阻塞情形列表信息</div>

        <el-table v-loading="loading" :data="hotComboOptionTitleList">
          <el-table-column label="序号" width="70" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="情形名称" align="center" width="300" prop="situationName" :show-overflow-tooltip="true" />
          <el-table-column label="排序号" align="center" width="300" prop="sort" :show-overflow-tooltip="true" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">

              <el-button size="mini" type="text" icon="iconfont zfsoft-chakan"
                @click="openSituationoptionList(scope.row)" v-hasPermi="['sys:reg:view']">查看情形选项配置</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div>
        <div class="zf-zc-table--title">选项列表信息</div>

        <el-table v-loading="loading" :data="hotComboOptionTitleList">
          <el-table-column label="序号" width="70" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="选项标题" align="center" width="300" prop="name" :show-overflow-tooltip="true" />
          <el-table-column label="是否必填" align="center" width="150">
            <template slot-scope="scope">
              <p v-if="scope.row.fillFlag==1">是</p>
              <p v-if="scope.row.fillFlag==0">否</p>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="openTitleVal(scope.row)"
                v-hasPermi="['sys:reg:view']">查看</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelOandS">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 选项标题信息详细及选项值列表 -->
    <el-dialog v-dialog-drag :title="title" :visible.sync="openTitleViewVal" width="900px" append-to-body>
      <div>
        <el-form ref="form" :model="form" label-width="140px" size="mini">
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td>
                <b>选项标题：</b>
              </td>
              <td colspan="3">
                {{form.name}}
              </td>
            </tr>

            <tr>
              <td>
                <b>是否必填：</b>
              </td>
              <td colspan="3">
                <p v-if="form.fillFlag==1">是</p>
                <p v-if="form.fillFlag==0">否</p>
              </td>
            </tr>
          </table>

        </el-form>
      </div>
      <div>
        <div class="zf-zc-table--title">选项值列表信息</div>

        <el-table v-loading="loading" :data="comboOptionValList">
          <el-table-column label="序号" width="170" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="选项值名称" align="center" width="300" prop="name" :show-overflow-tooltip="true" />
          <el-table-column label="是否默认" align="center" width="250">
            <template slot-scope="scope">
              <p v-if="scope.row.defaultFlag==1">是</p>
              <p v-if="scope.row.defaultFlag==0">否</p>
            </template>

          </el-table-column>

        </el-table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelOandS">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 情形配置选项列表信息对话框 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="title" :visible.sync="openSituationOptionList" width="900px"
      append-to-body>
      <div>


        <div class="zf-zc-table--title">选项列表信息</div>

        <el-table v-loading="loading" :data="hotComboOptionTitleList">
          <el-table-column label="序号" width="70" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="选项标题" align="center" width="300" prop="name" :show-overflow-tooltip="true" />
          <el-table-column label="是否必填" align="center" width="150">
            <template slot-scope="scope">
              <p v-if="scope.row.fillFlag==1">是</p>
              <p v-if="scope.row.fillFlag==0">否</p>
            </template>
          </el-table-column>
          <el-table-column label="配置状态" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">

              <p v-if="scope.row.isStatus==1">已关联</p>
              <p v-if="scope.row.isStatus!=1">未关联</p>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 添加或修改情形列表对话框 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="title" :visible.sync="openSituationList" width="900px"
      append-to-body>
      <div id="tabqx">
        <el-tabs v-model="activeNameQx" @tab-click="handleClickQx">
          <el-tab-pane label="热门情形" name="first" :key="'first'">
          </el-tab-pane>

          <el-tab-pane label="阻塞情形" name="second" :key="'second'">
          </el-tab-pane>
        </el-tabs>
      </div>
      <div>
        <el-form ref="form" :model="form" :rules="rules" label-width="140px" v-show="strqx==='first'">
          <el-input v-show="false" v-model="form.comboDirectoryOid" />
          <!--<p><h3><i class="el-icon-document"></i>热门情形列表信息</h3></p>-->
          <el-row :gutter="20">
            <el-col :span="24" :xs="24" style='margin-bottom:10px'>
              <el-button type="default" icon="el-icon-plus" size="mini" @click="handleInitSituation(form,1)"
                v-hasPermi="['combo:directory:init']">新增热门情形</el-button>
            </el-col>
          </el-row>
          <el-table v-loading="loading" :data="hotComboSituationList">
            <el-table-column label="序号" width="70" type="index" align="center">
              <template slot-scope="scope">
                <span>{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column label="情形名称" align="center" prop="situationName" :show-overflow-tooltip="true" />
            <el-table-column label="排序号" align="center" prop="sort" :show-overflow-tooltip="true" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai"
                  @click="handleInitSituation(scope.row,1)" v-hasPermi="['sys:bill:update']">修改</el-button>
                <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu"
                  @click="handleSituationDelete(scope.row)" v-hasPermi="['sys:reg:view']">删除</el-button>
                <el-button size="mini" type="text" icon="iconfont zfsoft-tubiao"
                  @click="handleSituOptionRel(scope.row,1)" v-hasPermi="['sys:reg:view']">情形选项配置</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form>
        <el-form ref="form" :model="form" :rules="rules" label-width="140px" v-show="strqx==='second'">
          <el-input v-show="false" v-model="form.comboDirectoryOid" />
          <!--  <p><h3><i class="el-icon-document"></i>阻塞情形列表信息</h3></p>-->
          <el-row :gutter="20">
            <el-col :span="24" :xs="24" style='margin-bottom:10px'>
              <el-button type="default" icon="el-icon-plus" size="mini" @click="handleInitSituation(form,2)"
                v-hasPermi="['combo:directory:init']">新增阻塞情形</el-button>
            </el-col>
          </el-row>
          <el-table v-loading="loading" :data="stopComboSituationList">
            <el-table-column label="序号" width="70" type="index" align="center">
              <template slot-scope="scope">
                <span>{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column label="情形名称" align="center" prop="situationName" :show-overflow-tooltip="true" />
            <el-table-column label="排序号" align="center" prop="sort" :show-overflow-tooltip="true" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai"
                  @click="handleInitSituation(scope.row,2)" v-hasPermi="['sys:bill:update']">修改</el-button>
                <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu"
                  @click="handleSituationDelete(scope.row)" v-hasPermi="['sys:reg:view']">删除</el-button>
                <el-button size="mini" type="text" icon="iconfont zfsoft-tubiao"
                  @click="handleSituOptionRel(scope.row,2)" v-hasPermi="['sys:reg:view']">情形选项配置</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form>
      </div>
      <div slot="footer" class="zf-text-center">
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 新增或修改情形 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="initTitle" :visible.sync="openInitSituation" width="900px"
      append-to-body>
      <div >
        <el-form ref="form" :model="form" :rules="qxrules" label-width="0px">
          <el-input v-show="false" v-model="form.id" />
          <el-input v-show="false" v-model="form.situationOid" />
          <el-input v-show="false" v-model="form.comboDirectoryOid" />
          <table cellspacing="0" cellpadding="0" border="0" class='zf-zc-table'>
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td>
                <b>情形名称：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="situationName">
                  <el-col :span="24">
                    <el-input v-model.trim="form.situationName" type="text" maxlength="50" show-word-limit
                      placeholder="请输入选项标题"></el-input>
                  </el-col>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>排序号：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="sort">
                  <el-col :span="24">
                    <el-input-number v-model="form.sort" :min="1" :max="9999" />
                  </el-col>
                </el-form-item>
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitSituForm">确 定</el-button>
        <el-button @click="cancelSituation(form.comboDirectoryOid)">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 情形配置选项列表信息对话框 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="title" :visible.sync="openSituationOption" width="900px"
      append-to-body>
      <div>

        <el-row :gutter="10" class="mb8 fl">
          <el-col :span="1.5">
            <el-button type="default" icon="el-icon-plus" size="mini" @click="chooseOptionsitu"
              v-hasPermi="['combo:directory:init']">保存</el-button>
          </el-col>
        </el-row>
        <!--情形选项值保存-->
        <el-table v-loading="loading" :data="comboOptionValList">
          <el-table-column label="序号" width="100" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="选项标题" align="center" width="200" prop="name" :show-overflow-tooltip="true" />
          <!-- <el-table-column label="选项值名称" align="center" width="200" prop="name"/>-->
          <el-table-column label="选项值" align="center" fixed-width>
            <template slot-scope="scope">
              <template v-if="scope.row.moreStatus=='0'">
                <el-radio-group v-model="situValCheck[scope.row.titleOid]">
                  <el-radio v-for="(comboObj,i) in scope.row.arrayVal" :key='i'
                    @change="changeRadio(scope.row.titleOid,comboObj.key)" :label="comboObj.key">{{comboObj.name}}
                  </el-radio>
                </el-radio-group>
              </template>
              <template v-if="scope.row.moreStatus=='1'">
                <el-checkbox-group v-model="situValCheck[scope.row.titleOid]">
                  <el-checkbox v-for="(comboObj,index) in scope.row.arrayVal" :key='index' :label="comboObj.key"
                    @change="changeCheckBox(scope.row.titleOid,comboObj.key)">{{comboObj.name}}</el-checkbox>
                </el-checkbox-group>
              </template>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer" class="zf-text-center">
        <el-button @click="cancelSituationOption">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 添加或修改选项列表信息对话框  "-->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="title" :visible.sync="openOption" width="900px" height='600px' scrollbar append-to-body>
      <div>
        <el-form ref="form" :model="form" :rules="rules" label-width="140px" v-show="strxx==='first'">
          <el-input v-show="false" v-model="form.comboDirectoryOid" />
          <!--          <h3><i class="el-icon-document"></i>选项列表信息</h3>-->
              <div style='margin:3px 0'>
              <el-button type="default" icon="el-icon-plus" size="mini" @click="handleInit(form,1)"
                v-hasPermi="['combo:directory:init']">新增选项信息</el-button>
              <el-button type="default" icon="el-icon-plus" size="mini" @click="handleOptionRelList(form,1)"
                v-hasPermi="['combo:directory:init']">选项关系配置</el-button>
                </div>
          <br />
          <el-table v-loading="loading" :data="hotComboOptionTitleList" border>
            <el-table-column label="序号" width="70" type="index" align="center">
              <template slot-scope="scope">
                <span>{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column label="选项标题" align="center" prop="name" :show-overflow-tooltip="true" />
            <el-table-column label="是否必填" align="center">
              <template slot-scope="scope">
                <p v-if="scope.row.fillFlag==1">是</p>
                <p v-if="scope.row.fillFlag==0">否</p>
              </template>
            </el-table-column>
            <el-table-column label="排序号" align="center" prop="sort" />
            <el-table-column label="操作" align="center" width="300px" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" @click="handleInit(scope.row,1)"
                  v-hasPermi="['sys:bill:update']">修改</el-button>
                <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="openTitleView(scope.row)"
                  v-hasPermi="['sys:reg:view']">选项值配置</el-button>
                <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDelete(scope.row)"
                  v-hasPermi="['sys:reg:view']">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form>
      </div>
      <div slot="footer" class="zf-text-center">
        <el-button @click="cancel">关 闭</el-button>
      </div>
    </el-dialog>
    <!-- 新增选项关系列表页面   v-if="scope.row.isStatus==0" -->
    <el-dialog v-dialog-drag :title="title" :visible.sync="openOptionRelList" width="900px" height='600px' scrollbar append-to-body>
      <div>
        <el-form ref="form" :model="form" label-width="140px" size="mini">
          <el-row :gutter="10" class="mb8 fl">
            <el-col :span="1.5">
              <el-button type="default" icon="el-icon-plus" size="mini" @click="handleOptionRel(form)"
                v-hasPermi="['combo:directory:init']">新增选项关系</el-button>
            </el-col>
          </el-row>
          <el-table v-loading="loading" :data="comboOptionRelList" border>
            <el-table-column label="序号" width="70" type="index" align="center">
              <template slot-scope="scope">
                <span>{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column label="选项标题" align="center" width="200" prop="titleName" :show-overflow-tooltip="true" />
            <el-table-column label="选项值名称" align="center" width="200" prop="titleValNames"
              :show-overflow-tooltip="true" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" @click="handleOptionRel(scope.row)"
                  v-hasPermi="['sys:reg:view']">选项关系配置</el-button>
                <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="deleteOptionRel(scope.row)"
                  v-hasPermi="['sys:reg:view']">删除</el-button>

              </template>
            </el-table-column>
          </el-table>
        </el-form>
      </div>
     <div slot="footer" class="zf-text-center">
        <el-button @click="openOptionRelList = false">关 闭</el-button>
      </div>
    </el-dialog>
    <!-- 新增选项关系页面   -->
    <el-dialog v-dialog-drag :title="title" :visible.sync="openOptionRel" width="900px" height='600px' scrollbar append-to-body>
      <div>
        <el-form ref="form" :model="form" label-width="140px" size="mini">
          <el-row :gutter="10" class="mb8 fl">
            <el-col :span="1.5">
              <el-button type="default" icon="el-icon-plus" size="mini" @click="batchChoose"
                v-hasPermi="['combo:directory:init']">保存</el-button>&nbsp;&nbsp;&nbsp;(原则上选项值与所属选项不能建立关联关系)
            </el-col>
          </el-row>
          <el-table ref="multipleTable" v-loading="loading" :data="comboOptionValList" tooltip-effect="dark"
            style="width: 100%" @selection-change="onTableSelect" class="xxgxpz">
            <el-table-column width="70" align="center" type="selection">
            </el-table-column>
            <el-table-column label="选项标题" align="center"  prop="name" :show-overflow-tooltip="true" />
            <!-- <el-table-column label="选项值名称" align="center" width="200" prop="name"/>-->
            <el-table-column label="选项值" align="center" >
              <!-- <template slot-scope="scope">
                <el-radio-group v-model="relCheckVals[scope.$index]">
                  <el-radio  v-for="comboObj in scope.row.arrayVal" :disabled="comboObj.ifDis"
                             :label="comboObj.key">{{comboObj.name}}</el-radio>
                </el-radio-group>
              </template>
-->
              <template slot-scope="scope">
                <template v-if="scope.row.moreStatus=='0'">
                  <el-radio-group v-model="relCheckVals[scope.$index]">
                    <el-radio v-for="(comboObj,index) in scope.row.arrayVal"
                    :key='index'
                      @change="changeRadio(scope.row.titleOid,comboObj.key)" :disabled="comboObj.ifDis"
                      :label="comboObj.key">{{comboObj.name}}</el-radio>
                  </el-radio-group>
                </template>
                <template v-if="scope.row.moreStatus=='1'">
                  <el-checkbox-group v-model="relCheckVals">
                    <el-checkbox v-for="(comboObj,idx) in scope.row.arrayVal" :key='idx' :label="comboObj.key" :disabled="comboObj.ifDis"
                      @change="changeCheckBox(scope.row.titleOid,comboObj.key)">{{comboObj.name}}</el-checkbox>
                  </el-checkbox-group>
                </template>
              </template>
            </el-table-column>
          </el-table>
        </el-form>
      </div>
      <div slot="footer" class="zf-text-center">
        <el-button @click="openOptionRel = false">关 闭</el-button>
      </div>
    </el-dialog>
    <!--新增修改选项标题页面-->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="titleXX" :visible.sync="openInitMater" v-if="openInitMater"
      width="900px" height='600px' scrollbar append-to-body>
      <div>
        <el-form ref="form" :model="form" :rules="rules" label-width="0px">
          <!--<el-input v-show="false" v-model="form.id" />-->
          <el-input v-show="false" v-model="form.titleOid" />
          <el-input v-show="false" v-model="form.comboDirectoryOid" />
          <table cellspacing="0" cellpadding="0" border="0" class='zf-zc-table'>
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td align="right"><i class="require">*</i>
                <b>选项标题：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="name">
                  <el-col :span="24">
                    <el-input v-model.trim="form.name" type="text" maxlength="50" show-word-limit placeholder="请输入选项标题">
                    </el-input>
                  </el-col>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i>
                <b>选项来源：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="optionSource">
                  <el-col :span="24">
                    <el-input v-model.trim="form.optionSource" type="text" maxlength="50" show-word-limit
                      placeholder="请输入选项来源"></el-input>
                  </el-col>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>是否必填：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="fillFlag">
                  <el-radio-group v-model="form.fillFlag">
                    <el-col :span="24">
                      <el-radio :label="0">否</el-radio>
                      <el-radio :label="1">是</el-radio>
                    </el-col>
                  </el-radio-group>
                </el-form-item>

              </td>
            </tr>
            <tr>
              <td>
                <b>是否多选：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="moreStatus">
                  <el-radio-group v-model="form.moreStatus">
                    <el-col :span="24">
                      <el-radio :label="0">否</el-radio>
                      <el-radio :label="1">是</el-radio>
                    </el-col>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td><i class="require">*</i>
                <b>排序号：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="sort">
                  <el-col :span="24">
                    <el-input-number v-model="form.sort" :min="1" :max="9999" />
                  </el-col>
                </el-form-item>
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancelMater(form.comboDirectoryOid)">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 选项标题信息详细及选项值列表 -->
    <el-dialog v-dialog-drag :title="titleXXZ" :visible.sync="openTitleV" width="900px" height='600px' scrollbar append-to-body>
      <el-form ref="form" :model="form" label-width="140px" size="mini">
      <div>
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td>
                <b>选项标题：</b>
              </td>
              <td colspan="3">
                {{form.name}}
              </td>
            </tr>

            <tr>
              <td>
                <b>是否必填：</b>
              </td>
              <td colspan="3">
                <p v-if="form.fillFlag==1">是</p>
                <p v-if="form.fillFlag==0">否</p>
              </td>
            </tr>
          </table>
      </div>
      <div style="padding: 0 10px;">
      <el-row :gutter="10" class="mb8 fl mt10">
          <el-col :span="1.5">
            <el-button type="default" icon="el-icon-plus" size="mini" @click="handleInitVal(form)"
              v-hasPermi="['combo:directory:init']">新增选项值</el-button>
          </el-col>
        </el-row>
        <el-table v-loading="loading" :data="comboOptionValList" border>
          <el-table-column label="序号" width="70" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="选项值名称" align="center" width="200" prop="name" :show-overflow-tooltip="true" />
          <el-table-column label="是否默认" align="center" width="150">
            <template slot-scope="scope">
              <p v-if="scope.row.defaultFlag==1">是</p>
              <p v-if="scope.row.defaultFlag==0">否</p>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" @click="handleInitVal(scope.row)"
                v-hasPermi="['sys:bill:update']">修改</el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-chakan"
                @click="queryServiceList(form,scope.row)" v-hasPermi="['sys:reg:view']">事项配置</el-button>
              <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleValDelete(scope.row)"
                v-hasPermi="['sys:reg:view']">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        </div>
      </el-form>
      <div slot="footer" class="zf-text-center">
        <el-button @click="cancelTitle(form.comboDirectoryOid)">关 闭</el-button>
      </div>
    </el-dialog>
    <!--新增修改选项值页面-->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="titleXXZOne" :visible.sync="openInitVal" v-if="openInitVal"
      width="900px" height='600px' scrollbar append-to-body>
      <div>
        <el-form ref="form" :model="form" :rules="rules" label-width="0px">
          <el-input v-show="false" v-model="form.id" />
          <el-input v-show="false" v-model="form.titleOid" />

          <table cellspacing="0" cellpadding="0" border="0" class='zf-zc-table'>
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td>
                <b>选项值：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="name">
                  <el-col :span="24">
                    <el-input v-model.trim="form.name" type="text" maxlength="50" show-word-limit placeholder="请输入选项标题">
                    </el-input>
                  </el-col>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>是否默认：</b>
              </td>
              <td colspan="3">

                <el-form-item prop="defaultFlag">
                  <el-radio-group v-model="form.defaultFlag">
                    <el-col :span="24">
                      <el-radio :label="1">是</el-radio>
                      <el-radio :label="0">否</el-radio>
                    </el-col>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>排序号：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="sort">
                  <el-col :span="24">
                    <el-input-number v-model="form.sort" :min="1" :max="9999" />
                  </el-col>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>关联政务百科：</b>
              </td>

              <td colspan="3">
                <el-form-item prop="manageOid">
                  <el-select v-model="form.manageOid" placeholder="请选择" >
                    <el-option
                      v-for="item in direTypes"
                      :key="item.manageOid"
                      :label="item.caryopterisName"
                      :value="item.manageOid"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitValForm">确 定</el-button>
        <el-button @click="cancelVal(form.titleOid)">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 分类信息详细 -->
    <el-dialog v-dialog-drag :visible.sync="openView" v-if="openView" title="目录信息" width="1100px" height='700px'  scrollbar append-to-body>

        <combo-directory-view :comboDirectoryOid="comboDirectoryOidView" @combo-directory="comboDirectoryClose">
        </combo-directory-view>
        <div slot="footer" class="zf-text-center">
        <el-button @click="comboDirectoryClose">关 闭</el-button>
      </div>

    </el-dialog>
    <!--目录公共材料开始-->
    <el-dialog v-dialog-drag :visible.sync="service.show" title="选项材料与证照" v-for="service in materialDialogOptions" :key='service.comboDirectoryOid'
      @close="closeMaterView" width="1100px" height="700px" scrollbar append-to-body>
      <div id="tabcl">
        <el-tabs v-model="activeNameCl" @tab-click="handleClickCl">
          <el-tab-pane label="选项材料" name="first" :key="'first'">
          </el-tab-pane>
          <el-tab-pane label="选项证照" name="second" :key="'second'">
          </el-tab-pane>
        </el-tabs>
      </div>

        <combo-option-materialList v-show="strcl==='first'" :comboDireOid="service.comboDirectoryOid"></combo-option-materialList>


        <combo-option-resultList v-show="strcl==='second'" :comboDireOid="service.comboDirectoryOid"></combo-option-resultList>
    <div slot="footer" class="zf-text-center">
        <el-button @click="closeMaterView">关 闭</el-button>
      </div>
    </el-dialog>
    <!--目录公共材料结束-->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="sxtitle" :visible.sync="openSerVal" v-if="openSerVal"
      width="900px" height='600px' scrollbar append-to-body>
      <!--目录信息 结束-->
      <div>
        <!--   <el-input v-show="false" v-model="valOid" />-->
        <div class="zf-zc-table--title">事项信息</div>
        <el-row :gutter="20">
          <el-col :span="24" :xs="24">
            <el-row :gutter="10" class="mb8 fl">
              <el-col :span="1.5">

              </el-col>
            </el-row>

            <!--@selection-change="handleSelectionChange"tooltip-effect="dark"-->
            <el-table ref="multipleTablea" :data="comboServiceList" border>
              <el-table-column label="序号" width="80" type="index" align="center">
                <template slot-scope="scope">
                  <span>{{scope.$index + 1}}</span>
                </template>
              </el-table-column>

              <el-table-column prop="organName" label="实施机构" width="300" :show-overflow-tooltip="true">
              </el-table-column>
              <el-table-column prop="serviceName" label="事项名称" width="350" :show-overflow-tooltip="true">
              </el-table-column>
              <el-table-column prop="implementCode" label="实施编码" show-overflow-tooltip width="250">
              </el-table-column>
              <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                  <el-button size="mini" type="text" v-if="scope.row.isStatus==0" icon="iconfont zfsoft-xiugai"
                    @click="chooseSx(scope.row)" v-hasPermi="['combo:directory:service']">关联</el-button>
                  <el-button size="mini" type="text" v-if="scope.row.isStatus==1" icon="iconfont zfsoft-xiugai"
                    @click="chooseSx(scope.row)" v-hasPermi="['combo:directory:service']">取消关联</el-button>
                </template>
              </el-table-column>

            </el-table>
            <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum"
              :limit.sync="queryParams.pageSize" @pagination="getList" />
          </el-col>
        </el-row>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import {
    init,
    save,
    delOptionRel,
    queryComOptiontitleStu,
    del,
    getOne,
    queryComOptiontitle,
    page,
    getComboServiceObject,
    handleOption,
    queryCombo,
    openTitleView,
    handleInitVal,
    saveVal,
    queryOptionVal,
    delVal
  } from "@/api/onething/sxpz/comboDireOptandStu";
  import {pageList} from "@/api/onething/sxpz/comboServiceManage";
  import {
    pageService,
    saveOptionService
  } from "@/api/onething/sxpz/optionService";
  import {
    queryComOptionVal,
    chooseOptionrel,
    queryComOptionRel
  } from "@/api/onething/sxpz/comboOptionRel";
  import {
    queryComboThemeSimpleTree
  } from "@/api/onething/sxpz/comboTheme";
  import {
    saveSituation,
    initSituation,
    queryComboSituation,
    chooseOptionsitution,
    delSituation
  } from "@/api/onething/sxpz/comboDireSituation";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import comboOptionMaterialList from "@/views/onething/sxpz/comboDireOptandStu/comboOptionMaterialList";
  import comboOptionResultList from "@/views/onething/sxpz/comboDireOptandStu/comboOptionResultList";
  import optionService from "@/views/onething/sxpz/comboDireOptandStu/optionService";
  import ComboDirectoryView from "@/views/onething/sxpz/comboDirectory/comboDirectoryView";
  export default {
    components: {
      ComboDirectoryView,
      Treeselect,
      optionService,
      comboOptionMaterialList,
      comboOptionResultList
    },
    name: "ComboDireOptandStu",
    data() {
      return {
        //默认第一个选项卡
        activeNameQx: "first",
        activeNameXx: "first",
        activeNameCl: "first",
        strqx: "first",
        strxx: "first",
        strcl: "first",
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        attatotal: 0,
        // 应用表格数据
        comboDirectoryList: [],
        //选项标题表格数据  --热门
        hotComboOptionTitleList: [],

        //选项标题表格数据  --阻塞
        stopComboOptionTitleList: [],
        // 应用表格数据
        comboServiceList: [],
        //选项值表格数据
        comboOptionValList: [],
        //选项值被选中
        comboCheckedValOids: [],
        direTypes:[],
        //选项标题
        comboOptionTitleOidList: [],
        // 默认值必选集合
        comboOptionValListMust: [],
        comboOptionValListFor: [],
        //查看选项值列表
        openTitleViewVal: false,
        valOidArray: "",
        valOidArrays: [],
        //选项关系表
        comboOptionRelList: [],
        //事项列表数据
        comboDialogOptions: [],
        //查询目录名称参数
        comboDirectoryName: '',
        // 弹出层标题
        title: "",
        // 选项标题弹出层标题
        titleXX: "",
        // 选项标题选项值列表弹出层标题
        titleXXZ: "",
        // 选项标题选项值配置弹出层标题
        titleXXZOne: "",

        sxtitle: "",
        initTitle: "",
        //主键
        comboDireOid: "",
        comboDirectoryOid: "",
        //选项关系业务主键
        relOid: "",
        valOids: "",
        // 新增/修改显示弹出层
        openInitMater: false,
        //新增/修改选项值弹出层
        openInitVal: false,
        //选项详情与选项值列表弹出层
        openTitleV: false,
        openOptionRel: false,
        radio1: [],
        relCheckVals: [],
        situValCheck: [],
        radio2: "2",
        radio3: "3",
        titleOidArray: "",
        //情形列表页面
        openSituationList: false,

        //情形初始化页面
        openInitSituation: false,
        //热门情形列表
        hotComboSituationList: [],
        //阻塞情形列表
        stopComboSituationList: [],
        //情形主键
        situationOid: "",
        //选项关系列表页面
        openOptionRelList: false,
        //查看情形与选项列表
        openSituationOptionList: false,
        //选项标题列表
        openOption: false,
        //选项情形列表
        openOptionSituationList: false,
        //选项材料页面
        materialDialogOptions: [],
        //事项列表
        openSerVal: false,
        //选项情形列表
        openSituationOption: false,
        // 出库状态
        statusOptions: {
          '0': '否',
          '1': '是'
        },
        valOid: "",
        multipleSelection: [],
        // 查看显示弹出层
        openView: false,
        //选择附件项附件
        openComboService: false,
        fileList: [],
        qxType: "",
        xxType: "",
        relType: "",
        //启禁用状态
        //ableMap:{'Y':'启用','N':'禁用'},
        // 区划树选项
        //themeOptions: undefined,
        // 级别字典
        // comboThemes: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          comboDirectoryName: '',
          comboDirectoryOid: '',

        },
        //附件参数
        attaQueryParams: {
          pageNum: 1,
          pageSize: 10
        },
        defaultProps: {
          children: "children",
          label: "label"
        },
        // 表单参数
        form: {
          fillFlag: 0,
          moreStatus: 0
        },
        //分类Tree
        themeOptions: [],
        //区划Tree
        districtOptions: [],
        //主办部门Tree
        mainOrganOptions: [],
        //协办部门Tree
        assistOrganOptions: [],
        //服务对象
        comboServiceObjects: [],
        //必选验证用
        chooseMust: [],
        arrayVal: [],
        //附件列表
        attaList: [],
        //办理深度
        //handleForms:[],
        // 表单校验
        checkList: [],
        qxrules: {

          situationName: [{
            required: true,
            message: "情形名称不能为空",
            trigger: "blur"
          }],
        },
        rules: {

          name: [{
            required: true,
            message: "标题不能为空",
            trigger: "blur"
          }],
          optionSource: [{
            required: true,
            message: "选项来源不能为空",
            trigger: "blur"
          }],
          sort: [{
            required: true,
            message: "排序号不能为空",
            trigger: "blur"
          }],
          comboDirectoryName: [{
            required: true,
            message: "目录名称不能为空",
            trigger: "blur"
          }]
        },
      };
    },

    computed: {
      // 计算属性的 getter
      reversedCountToScence: function() {
        if (this.form.countToScence) {
          if (this.form.countToScence == "0") {
            return '0次';
          } else if (this.form.countToScence == "1") {
            return '1次';
          } else if (this.form.countToScence == "2") {
            return '2次';
          } else if (this.form.sex == "3") {
            return '多次';
          }
        }
        return ''
      },
      reversedExpressFlag: function() {
        if (this.form.expressFlag) {
          if (this.form.expressFlag == 0) {
            return '否';
          } else if (this.form.expressFlag == 1) {
            return '是';
          }
        }
        return ''
      },
      reversedOnlinePayFlag: function() {
        if (this.form.onlinePayFlag) {
          if (this.form.onlinePayFlag == 0) {
            return '否';
          } else if (this.form.onlinePayFlag == 1) {
            return '是';
          }
        }
        return ''
      },
      reversedHandleForm: function() {
        if (this.form.handleForm) {
          if (this.form.handleForm == "0") {
            return '窗口办理';
          } else if (this.form.handleForm == "1") {
            return '网上办理';
          } else if (this.form.handleForm == "2") {
            return '一体化办理';
          }
        }
        return ''
      },
      reversedAppointmentFlag: function() {
        if (this.form.appointmentFlag) {
          if (this.form.appointmentFlag == 0) {
            return '否';
          } else if (this.form.appointmentFlag == 1) {
            return '是';
          }
        }
        return ''
      },

      reversedUnionOrganFlag: function() {
        if (this.form.unionOrganFlag) {
          if (this.form.unionOrganFlag == 0) {
            return '否';
          } else if (this.form.unionOrganFlag == 1) {
            return '是';
          }
        }
        return ''
      },
      reversedIsZjfw: function() {
        if (this.form.isZjfw) {
          if (this.form.isZjfw == 0) {
            return '否';
          } else if (this.form.isZjfw == 1) {
            return '是';
          }
        }
        return ''
      },
      reversedIfCharge: function() {
        if (this.form.ifCharge) {
          if (this.form.ifCharge == 0) {
            return '否';
          } else if (this.form.ifCharge == 1) {
            return '是';
          }
        }
        return ''
      }

    },
    created() {
      this.getList();
     // this.direType();
      //分类树
      /*this.getThemeTree();
      //区划树
      this.getDistrictTree();
      //服务对象
      getComboServiceObject().then(response => {
        this.comboServiceObjects = response.data;//[{"key":1,"value":"自然人"},{"key":2,"value":"法人"}];
      });*/
    },
    methods: {
      comboDirectoryClose() {
        this.openView = false;
      },
      direType() {
        //this.direTypes=[];
        this.loading = true;
        this.queryParams.comboDirectoryOid = this.comboDireOid;
        pageList(this.queryParams).then(response => {
          console.log(response.data.data);
          this.direTypes = response.data.data;
          /*this.total = response.data.total;*/
          this.loading = false;
        });
      },
      /**
       * tab切换
       */
      handleClickQx(tabqx) {
        if (tabqx.name == 'second') {
          // 触发‘事项’事件
          this.strqx = "second";
        } else {
          // 触发‘普通事项’事件
          this.strqx = "first";
        }
      },
      handleClickXx(tabxx) {
        if (tabxx.name == 'second') {
          // 触发‘事项’事件
          this.strxx = "second";
        } else {
          // 触发‘普通事项’事件
          this.strxx = "first";
        }
      },
      handleClickCl(tabcl) {
        if (tabcl.name == 'second') {
          // 触发‘事项’事件
          this.strcl = "second";
        } else {
          // 触发‘普通事项’事件
          this.strcl = "first";
        }
      },
      /** 查询目录列表 */
      getList() {
        this.loading = true;
        page(this.queryParams).then(response => {
          this.comboDirectoryList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        });
      },


      /** 查询选项标题列表 */
      getTitleList(comboDirectoryOid) {
        this.loading = true;
        queryCombo(comboDirectoryOid, null, 1).then(response => {
          this.hotComboOptionTitleList = response.data;
          this.loading = false;
        });
      },

      /** 查询选项值列表 */
      getTitleValList(titleOid) {
        this.loading = true;
        queryOptionVal(titleOid).then(response => {
          this.comboOptionValList = response.data;
          //this.total = response.data.total;
          this.loading = false;
        });
      },

      /**获取分类树**/
      getThemeTree(themeOid) {
        queryComboThemeSimpleTree(themeOid).then(response => {
          this.themeOptions = response.data;
        });
      },
      /** 获取区划树 */
      getDistrictTree(districtOid) {
        this.queryDistrictSimpleTree(districtOid).then(response => {
          this.districtOptions = response.data;
        });
      },
      // 取消按钮
      cancel() {
        this.openOption = false;
        this.openSituationList = false;

        this.openSituationOptionList = false;
        this.reset();
      },
      cancelSituationOption() {
        this.openSituationOption = false;
      },
      cancelOandS() {
        this.openTitleViewVal = false;
        this.openOptionSituationList = false;
        this.reset();
      },
      cancelMater(oid) {
        this.openInitMater = false;
        handleOption(oid).then(response => {
          if (response.data.comboDirectory != undefined) {
            this.form = response.data.comboDirectory;
            this.getTitleList(oid);
            //this.checkList=response.data.comboDirectory.comboServiceObjectAttr;
          }
          this.openOption = true;
          this.dialogTitle = oid ? "修改" : "新增";
        });

      },
      //情形初始化页面取消按钮
      cancelSituation(oid) {
        this.openInitSituation = false;
        handleOption(oid).then(response => {
          if (response.data.comboDirectory != undefined) {
            this.form = response.data.comboDirectory;
            this.getSituationList(oid);
            //this.checkList=response.data.comboDirectory.comboServiceObjectAttr;
          }
          this.openSituationList = true;
          this.title = "情形列表页面";
        });

      },
      cancelVal(oid) {
        this.openInitVal = false;
        openTitleView(oid).then(response => {
          if (response.data != undefined) {
            this.form = response.data;
            this.getTitleValList(oid);
            //this.checkList=response.data.comboDirectory.comboServiceObjectAttr;
          }
          this.openTitleV = true;
          this.loading = false;
          this.titleXXZ = "选项配置详细页面";
        });

      },
      cancelTitle(oid) {
        this.openTitleV = false;
        handleOption(oid).then(response => {
          if (response.data.comboDirectory != undefined) {
            this.form = response.data.comboDirectory;
            this.getTitleList(oid);
            //this.checkList=response.data.comboDirectory.comboServiceObjectAttr;
          }
          this.openOption = true;
          this.dialogTitle = oid ? "修改" : "新增";
        });

      },
      // 表单重置
      reset() {
        this.form = {
          fillFlag: 0,
          moreStatus: 0
        }

        this.resetForm("form")
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 查看按钮操作 */
      handleView(row) {
        this.comboDirectoryOidView = row.comboDirectoryOid;
        this.openView = true;
        this.title = "查看目录信息";
      },
      /** 新增和修改按钮操作 */
      handleInit(row, xxType) {
        let _that = this;
        this.reset();
        let oid = row.titleOid;
        this.xxType = xxType;
        init(oid, this.comboDirectoryOid).then(response => {
          if (response.data.comboOptionTitle != undefined) {
            this.form = response.data.comboOptionTitle;

          }
          _that.openInitMater = true;
          let titleNameAdd = "选项标题新增";
          let titleNameUpdate = "选项标题修改";
          _that.titleXX = oid ? titleNameUpdate : titleNameAdd;
        });
      },
      /** 新增和修改按钮操作 */
      handleInitVal(row) {
        let _that = this;
        this.reset();
        let oid = row.id;
        handleInitVal(oid, row.titleOid).then(response => {
          if (response.data.ComboOptionVal != undefined) {
            this.form = response.data.ComboOptionVal;
          }
          this.direType();
          _that.openInitVal = true;
          _that.titleXXZOne = "选项值配置";
        });
      },

      /** 配置选项值按钮操作 */
      openTitleView(row) {
        let _that = this;
        let oid = row.titleOid;
        openTitleView(oid).then(response => {
          if (response.data != undefined) {
            this.form = response.data;
            this.getTitleValList(row.titleOid);
            _that.comboDireOid = row.comboDirectoryOid;
            //this.checkList=response.data.comboDirectory.comboServiceObjectAttr;
          }
          _that.openTitleV = true;
          _that.titleXXZ = "选项配置详细页面";
        });
      },
      handleOptionRelList(row, relType) {
        let _that = this;
        this.relType = relType;
        queryComOptionRel("", _that.comboDirectoryOid, relType).then(response => {
          if (response.data != undefined) {
            this.comboOptionRelList = response.data;
          }
          _that.openOptionRelList = true;
        });
      },

      //选项关系配置
      handleOptionRel(row) {
        let _that = this;
        let oid = _that.titleOid;
        this.multipleSelection = [];
        this.valOidArray = "";
        this.relCheckVals = [];
        queryComOptiontitle(_that.comboDirectoryOid, row.relOid, this.relType).then(response => {
          if (response.data != undefined) {
            this.comboOptionValList = response.data;
            _that.loading = false;
            _that.relOid = row.relOid;
          }
          _that.$nextTick(() => {
            _that.comboOptionValList.forEach(d => {
              if (d.isStatus === 1) {
                _that.$refs.multipleTable.toggleRowSelection(d, true);
                // _that.multipleSelection.push(d);
              }
              d.arrayVal.forEach(a => {
                if (d.isStatus === 1) {
                  a.ifDis = true;
                } else {
                  a.ifDis = false;
                }
                if (a.isStatus == '1') {
                  _that.relCheckVals.push(a.key);
                }
                let titleValues;
                titleValues = {};
                titleValues.titleName = d.name;
                titleValues.bigTitle = d.titleOid;
                titleValues.fillFlag = d.fillFlag;
                titleValues.titleOid = a.titleOid;
                if (a.isStatus == '1') {
                  titleValues.valuePut = a.titleOid + a.key;
                } else {
                  titleValues.valuePut = "0";
                }
                if (_that.comboOptionValListMust.length > 0) {
                  let hasTitle = {};
                  let addMust = true;
                  for (let mustHas of _that.comboOptionValListMust) {
                    hasTitle = mustHas;
                    if (titleValues.bigTitle == hasTitle.bigTitle) {
                      addMust = false;
                    } else {
                      addMust = true;
                    }
                  }
                  if (addMust == false) {
                    if (titleValues.valuePut == "0") {} else {
                      if (hasTitle.valuePut.indexOf(titleValues.valuePut) > -1) {} else {
                        hasTitle.valuePut = hasTitle.valuePut + "," + titleValues.valuePut;
                      }
                    }
                  } else {
                    _that.comboOptionValListMust.push(titleValues);
                  }
                } else {
                  _that.comboOptionValListMust.push(titleValues);
                }
              });
            });
          });
          _that.openOptionRel = true;
          _that.title = "选项关系配置页面";
        });
      },
      chooseOptionVal(row) {
        const valOid = row.valOid;
        const comboDirectoryOid = row.comboDirectoryOid;
        const titleOid = this.titleOid;
        chooseOptionrel(comboDirectoryOid, titleOid, valOid, this.relOid).then(response => {
          if (response.code === 200) {
            this.msgSuccess("暂存成功");
            this.$emit('dialog-close');
            queryComOptionVal(titleOid, comboDirectoryOid, this.relOid).then(response => {
              if (response.data != undefined) {
                this.comboOptionValList = response.data;
                this.comboOptionValList.forEach(d => {
                  if (d.isStatus == '1') {
                    outerItem.toggleRowSelection(d, true);
                  }
                });
                this.comboDireOid = row.comboDirectoryOid;
              }
              this.openOptionRel = true;
            });
          }
        });
      },
      /** 删除按钮操作 */
      deleteOptionRel(row) {
        const oid = row.id;
        this.$confirm('是否确认删除?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delOptionRel(oid);
        }).then(() => {
          this.msgSuccess("删除成功");
          queryComOptionRel("", row.comboDirectoryOid, this.relType).then(response => {
            if (response.data != undefined) {
              this.comboOptionRelList = response.data;
            }
            _that.openOptionRelList = true;
          });
        }).catch(function() {});
      },
      getStatus(val) {
        if (val.status == 0) {
          return '暂存';
        } else if (val.status == 1) {
          return '已配置'
        } else {
          return '';
        }
      },

      /** 配置选项按钮操作 */
      handleOption(row) {
        let _that = this;

        let oid = row.comboDirectoryOid;

        handleOption(oid).then(response => {
          if (response.data.comboDirectory != undefined) {
            this.form = response.data.comboDirectory;
            this.getTitleList(oid);
            //this.checkList=response.data.comboDirectory.comboServiceObjectAttr;
          }
          _that.comboDirectoryOid = row.comboDirectoryOid;
          _that.openOption = true;
          _that.title = "选项配置";
        });
      },

      /** 选项标题提交按钮 */
      submitForm: function() {
        if (this.checkList.length > 0) {
          this.form.comboServiceObject = this.checkList.toString();
        }
        this.form.comboDirectoryOid = this.comboDirectoryOid;
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.form.optionType = this.xxType;
            save(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("保存成功");
                this.openInitMater = false;
                this.getTitleList(this.comboDirectoryOid);
                this.openOption = true;
                this.dialogTitle = "修改";
                /*});*/
                this.resetForm("form");
              }
            });
          }
        });
      },
      /** 选项值提交按钮 */
      submitValForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            saveVal(this.form, this.comboDireOid).then(response => {
              if (response.code === 200) {
                this.msgSuccess("保存成功");
                this.openInitVal = false;
                //this.getList();
                openTitleView(this.form.titleOid).then(response => {
                  if (response.data != undefined) {
                    this.form = response.data;
                    this.getTitleValList(this.form.titleOid);
                    //this.checkList=response.data.comboDirectory.comboServiceObjectAttr;
                  }
                  this.openTitleV = true;
                  this.title = "选项配置详细页面";
                });
              }
            });
          }
        });
      },

      /** 删除按钮操作 */
      handleDelete(row) {
        const oid = row.id;
        this.$confirm('是否确认删除?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return del(oid);
        }).then(() => {
          this.msgSuccess("删除成功");
          this.getTitleList(row.comboDirectoryOid);
        }).catch(function() {});
      },
      /** 删除按钮操作 */
      handleValDelete(row) {
        const oid = row.id;
        this.$confirm('是否确认删除?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delVal(oid);
        }).then(() => {
          this.msgSuccess("删除成功");
          this.getTitleValList(row.titleOid);
        }).catch(function() {});
      },
      //事项关联
      queryServiceList(form, row) {
        /** 查询目录列表 */
        this.queryParams.comboDirectoryOid = this.comboDirectoryOid;
        this.openSerVal = true;
        pageService(this.queryParams, row.valOid).then(response => {
          this.comboServiceList = response.data.data;
          this.valOid = row.valOid;
          this.sxtitle = "事项配置列表页面";
          this.total = response.data.total;
          this.loading = false;
        });

      },
      //确认配置当前目录下的所有事项
      setStatusAll() {
        const oid = this.comboDireOid;
        this.$confirm('是否确认配置?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return configAllService(oid);
        }).then(() => {
          this.msgSuccess("配置成功");
          //this.getList();
        }).catch(function() {});

      },
      /**  选择事项 **/
      chooseSx(row) {
        const serviceOid = row.serviceOid;
        const valOid = this.valOid;
        const comboDireOid = this.comboDireOid;
        saveOptionService(serviceOid, valOid, comboDireOid).then(response => {
          if (response.code === 200) {
            this.msgSuccess("操作成功");
            this.openSerVal = false;
          }
        });
      },

      // 关闭按钮
      closeUserView() {
        this.comboDialogOptions.pop();
      },
      onTableSelect(val) {
        let _that = this;
        _that.multipleSelection = val;

        if (_that.comboOptionValList) {
          _that.comboOptionValList.forEach(d => {
            if (_that.multipleSelection) {
              var index = this.multipleSelection.findIndex(titval => {
                if (d.titleOid == titval.titleOid) {
                  return true;
                }
              });
              if (index > -1) { //存在
                d.arrayVal.forEach(value => {
                  value.ifDis = true;
                  //去除选中
                  var index2 = this.relCheckVals.findIndex(chev => {
                    if (chev == value.key) {
                      return true;
                    }
                  });
                  if (index2 > -1) { //存在
                    this.relCheckVals.splice(index2, 1);
                  }
                });
              } else { //不存在
                d.arrayVal.forEach(value => {
                  value.ifDis = false;

                });
              }
            } else {
              d.arrayVal.forEach(value => {
                //去除选中
                value.ifDis = false;
                /* //去除选中
                 var index2 = this.relCheckVals.findIndex(chev => {
                   if (chev== value.key) {
                     return true;
                   }
                 });
                 if(index2>-1){//存在
                   this.relCheckVals.splice(index2,1);
                 }*/
              });
            }
          });
        }
        // this.$forceUpdate();
      },

      //多组单选框数据处理方法
      chooseValOid(valOid, titOid) {
        if (this.titleOidArray == "") {
          this.valOidArrays.push(valOid);
          this.titleOidArray += titOid + ";";
        } else {
          if (this.titleOidArray.indexOf(titOid) == -1) {

            this.valOidArrays.push(valOid);
            this.titleOidArray += titOid + ";";
          } else {

            this.$nextTick(() => {
              this.arrayVal.forEach(a => {

                if (a.titleOid == titOid) {

                  this.valOidArrays.forEach(b => {
                    if (a.key == b && b != valOid) {
                      if (this.valOidArray.indexOf(valOid) == -1) {
                        let bl = b + ";"

                        this.valOidArrays.splice(this.valOidArrays.indexOf(b), 1);

                        this.valOidArrays.push(valOid);

                      }

                    }

                  });
                }


              });

            })

          }
        }
      },

      /**  批量选择**/
      batchChoose() {
        let titleOids = "";
        let _that = this;
        //获取选中证照
        //titleOids=this.multipleSelection.toString();
        this.multipleSelection.forEach(ser => {
          titleOids += ser.titleOid + ",";
        });
        // this.valOidArray=this.relCheckVals.toString();
        if (titleOids) { //去除最后的,号
          titleOids = titleOids.substring(0, titleOids.lastIndexOf(','));
        }
        if (titleOids == "") {
          this.msgWarning("请选择标题！");
          return false;
        }
        if (null == this.relCheckVals || this.relCheckVals.length == 0) {
          this.msgWarning("请选择选项值！");
          return false;
        }
        const comboDirectoryOid = this.comboDirectoryOid;
        chooseOptionrel(comboDirectoryOid, titleOids, this.relCheckVals, this.relOid, this.relType).then(response => {
          if (response.code === 200) {
            this.msgSuccess("保存成功");
            this.$emit('dialog-close');
            this.multipleSelection = [];
            this.valOidArrays = [];
            this.titleOidArray = [];
            _that.openOptionRel = false;
            queryComOptionRel("", _that.comboDirectoryOid, this.relType).then(response => {
              if (response.data != undefined) {
                this.comboOptionRelList = response.data;
              }
              _that.openOptionRelList = true;
            });
          }
        });
      },

      //情形功能js方法

      /** 配置情形按钮操作 */
      handleSituationList(row) {

        let _that = this;

        let oid = row.comboDirectoryOid;

        handleOption(oid).then(response => {
          if (response.data.comboDirectory != undefined) {
            this.form = response.data.comboDirectory;
            this.getSituationList(oid);
            //this.checkList=response.data.comboDirectory.comboServiceObjectAttr;
          }
          _that.openSituationList = true;
          _that.title = "情形列表页面";
        });
      },
      /** 新增和修改情形按钮操作 */
      handleInitSituation(row, qxType) {
        this.reset();
        let _that = this;
        this.qxType = qxType;
        let oid = row.situationOid;
        this.loading = true;

        initSituation(oid, row.comboDirectoryOid).then(response => {
          if (response.data.comboDirectorySituation != undefined) {
            this.form = response.data.comboDirectorySituation;

          }
          this.loading = false;
          _that.openInitSituation = true;
          let titleNameAdd = "";
          let titleNameUpdate = "";
          if (qxType == 1) {
            titleNameAdd = "热门情形新增";
            titleNameUpdate = "热门情形修改";
          } else {
            titleNameAdd = "阻塞情形新增";
            titleNameUpdate = "热门情形修改";
          }
          _that.initTitle = oid ? titleNameUpdate : titleNameAdd;
        });
      },

      /** 情形提交按钮 */
      submitSituForm: function() {
        if (this.checkList.length > 0) {
          this.form.comboServiceObject = this.checkList.toString();
        }
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.form.situationType = this.qxType;
            saveSituation(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("保存成功");
                this.openInitSituation = false;
                //this.getList();
                handleOption(this.form.comboDirectoryOid).then(response => {
                  if (response.data.comboDirectory != undefined) {
                    this.form = response.data.comboDirectory;
                    this.getSituationList(this.form.comboDirectoryOid);
                    this.resetForm("form");
                    //this.checkList=response.data.comboDirectory.comboServiceObjectAttr;
                  }
                  this.openSituationList = true;
                  this.title = "情形列表页面";
                });
              }
            });
          }
        });
      },
      /** 查询情形列表 */
      getSituationList(comboDirectoryOid) {
        this.loading = true;
        queryComboSituation(comboDirectoryOid, 1).then(response => {
          this.hotComboSituationList = response.data;
        });
        queryComboSituation(comboDirectoryOid, 2).then(response => {
          this.stopComboSituationList = response.data;
          //this.total = response.data.total;
          this.loading = false;
        });
      },
      changeRadio(titleOid, chooseVal) {
        for (let oneTitle of this.comboOptionValListMust) {
          if (oneTitle.titleOid == titleOid) {
            oneTitle.valuePut = '1';
          }
        }
        this.comboCheckedValOids.forEach(item => {
          if (item == chooseVal) {
            this.comboCheckedValOids.splice(this.comboCheckedValOids.indexOf(item), 1)
          }
        });
        this.comboCheckedValOids.push(chooseVal);
      },
      changeCheckBox(titleOid, chooseVal) {
        for (let oneTitle of this.comboOptionValListMust) {
          if (oneTitle.titleOid == titleOid) {
            if (oneTitle.valuePut.indexOf(titleOid + chooseVal) != -1) {
              oneTitle.valuePut = oneTitle.valuePut.replace("," + titleOid + chooseVal, '');
              oneTitle.valuePut = oneTitle.valuePut.replace(titleOid + chooseVal, '');
            } else {
              oneTitle.valuePut = oneTitle.valuePut + "," + titleOid + chooseVal;
            }
          }
        }
      },
      /** 查询选项标题列表 */
      handleSituOptionRel(row, pzType) {
        let _that = this;
        _that.situValCheck = [];
        _that.comboOptionTitleOidList = [];
        queryComOptiontitleStu(row.comboDirectoryOid, row.situationOid, pzType).then(response => {
          _that.comboOptionValListMust = [];
          if (response.data != undefined) {
            _that.comboOptionValList = response.data;
            _that.$nextTick(() => {
              _that.comboOptionValList.forEach(d => {
                let checkbox = [];
                _that.comboOptionTitleOidList.push(d.titleOid);
                d.arrayVal.forEach(a => {
                  //单选
                  if (d.moreStatus == '0') {
                    if (a.isStatus == '1') {
                      this.$set(_that.situValCheck, d.titleOid, a.key)
                    }
                  }
                  //多选
                  if (d.moreStatus == '1') {
                    if (a.isStatus == '1') {
                      checkbox.push(a.key);
                      this.$set(_that.situValCheck, d.titleOid, checkbox)
                    } else {
                      checkbox.push("");
                      this.$set(_that.situValCheck, d.titleOid, checkbox)
                    }
                  }
                  let titleValues;
                  titleValues = {};
                  titleValues.titleName = d.name;
                  titleValues.bigTitle = d.titleOid;
                  titleValues.fillFlag = d.fillFlag;
                  titleValues.titleOid = a.titleOid;
                  if (a.isStatus == '1') {
                    titleValues.valuePut = a.titleOid + a.key;
                  } else {
                    titleValues.valuePut = "0";
                  }
                  if (_that.comboOptionValListMust.length > 0) {
                    let hasTitle = {};
                    let addMust = true;
                    for (let mustHas of _that.comboOptionValListMust) {
                      hasTitle = mustHas;
                      if (titleValues.bigTitle == hasTitle.bigTitle) {
                        addMust = false;
                      } else {
                        addMust = true;
                      }
                    }
                    if (addMust == false) {
                      if (titleValues.valuePut == "0") {} else {
                        if (hasTitle.valuePut.indexOf(titleValues.valuePut) > -1) {} else {
                          hasTitle.valuePut = hasTitle.valuePut + "," + titleValues.valuePut;
                        }
                      }
                    } else {
                      _that.comboOptionValListMust.push(titleValues);
                    }
                  } else {
                    _that.comboOptionValListMust.push(titleValues);
                  }
                });
              });
            })
            _that.openSituationOption = true;
            _that.situationOid = row.situationOid;
            _that.comboDirectoryOid = row.comboDirectoryOid;
            _that.loading = false;
            _that.loading = false;

          }
          _that.openSituationOption = true;
          _that.title = "情形关系配置页面";
        });
      },
      //选择情形选项
      chooseOptionsitu() {
        let valOids = "";
        this.comboOptionTitleOidList.forEach(titleOid => {
          if (typeof(this.situValCheck[titleOid]) == "object") {
            let checklist = this.situValCheck[titleOid];
            checklist.forEach(oid => {
              if (oid != "") {
                valOids += oid + ",";
              }
            })
          } else {
            valOids += this.situValCheck[titleOid] + ",";
          }
        });
        const situationOid = this.situationOid;
        const comboDirectoryOid = this.comboDirectoryOid;
        for (let mustChoose of this.comboOptionValListMust) {
          if (mustChoose.fillFlag == '1' && (mustChoose.valuePut == '' || mustChoose.valuePut == '0')) {
            this.msgWarning(mustChoose.titleName + "请选择选项值！");
            return false;
            break;
          }
        }
        chooseOptionsitution(comboDirectoryOid, valOids, situationOid).then(response => {
          if (response.code === 200) {
            this.msgSuccess("配置成功");
            this.$emit('dialog-close');
            this.multipleSelection = [];
            this.valOidArrays = [];
            this.titleOidArray = [];
            this.openSituationOption = false;

            queryComboSituation(comboDirectoryOid, 1).then(response => {
              this.hotComboSituationList = response.data;
            });
            queryComboSituation(comboDirectoryOid, 2).then(response => {
              this.stopComboSituationList = response.data;
              this.openSituationList = true;
              this.loading = false;
            });
          }
        });
      },
      /** 删除按钮操作 */
      handleSituationDelete(row) {
        const oid = row.id;
        this.$confirm('是否确认删除?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delSituation(oid);
        }).then(() => {
          this.msgSuccess("删除成功");
          this.getSituationList(row.comboDirectoryOid);
        }).catch(function() {});
      },


      //查看选项情形列表

      queryOptionSituationList(row) {
        queryComboSituation(row.comboDirectoryOid, 1).then(response => {
          this.hotComboSituationList = response.data;
        });
        queryComboSituation(row.comboDirectoryOid, 2).then(response => {
          this.stopComboSituationList = response.data;
          this.loading = false;
        });
        queryCombo(row.comboDirectoryOid, row.situationOid, 1).then(response => {
          this.hotComboOptionTitleList = response.data;
          this.openOptionSituationList = true;
          this.title = "选项情形列表页面";
          this.loading = false;
          // this.title =  "选项情形列表页面";
        });
        /*queryCombo(row.comboDirectoryOid,row.situationOid,2).then(response => {
          this.stopComboOptionTitleList = response.data;

        });*/
      },

      //选择情形选项
      openSituationoptionList(row) {
        this.loading = true;
        queryCombo(row.comboDirectoryOid, row.situationOid, 1).then(response => {
          this.hotComboOptionTitleList = response.data;
          this.openSituationOptionList = true;
          this.loading = false;
        });
        /* queryCombo(row.comboDirectoryOid, row.situationOid,2).then(response => {
           this.stopComboOptionTitleList = response.data;
           this.openSituationOptionList = true;
           this.loading = false;
         });*/
      },
      /** 配置选项值按钮操作 */
      openTitleVal(row) {
        let _that = this;

        let oid = row.titleOid;

        openTitleView(oid).then(response => {
          if (response.data != undefined) {
            this.form = response.data;
            this.getTitleValList(row.titleOid);
            _that.comboDireOid = row.comboDirectoryOid;
            //this.checkList=response.data.comboDirectory.comboServiceObjectAttr;
          }
          _that.openTitleViewVal = true;
          _that.title = "选项详细页面";
        });
      },

      //配置选项材料
      //目录材料整合
      queryMaterialList(row) {
        let item = {
          show: true,
          comboDirectoryOid: row.comboDirectoryOid
        };
        this.materialDialogOptions.push(item)
      },
      // 关闭按钮
      closeMaterView() {
        this.materialDialogOptions.pop();
      },
    }
  };
</script>
<style lang="scss" scoped>
  .dialog-table {
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
    text-align: left !important;
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

  .required {
    color: #FF0000;
    font-size: 20px;
    display: inline-block;
    vertical-align: middle;
    margin: 3px 5px 0px 0px;
  }
</style>
