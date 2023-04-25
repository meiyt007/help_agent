/** * @Author: liangxm * @Date: 2020-11-29 * @Description: 材料出库 */
<template>
  <div class="app-container">
    <!--区划数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="70px"
    >
      <el-row>
        <el-form-item label="所属区划" prop="districtOid">
          <treeselect
            class="treeselect"
            :defaultExpandLevel="1"
            noOptionsText="暂无数据"
            noResultsText="暂无数据"
            :show-count="true"
            v-model="queryParams.districtOid"
            :options="districtOptions"
            placeholder="请输入所属区划"
          />
        </el-form-item>
        <el-form-item label="申请人" prop="applyUserName">
          <el-input
            v-model.trim="queryParams.applyUserName"
            placeholder="请输入申请人"
            clearable
            size="100"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="扫码出库" prop="caseNumber">
          <el-input
            v-model.trim="queryParams.caseNumber"
            placeholder="请扫描办件编号二维码"
            clearable
            size="100"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="出库状态" label-width="108px">
          <el-radio-group v-model="queryParams.outStatus">
            <el-radio
              v-for="(status, key) in statusOptions"
              :key="key"
              :label="key"
              >{{ status }}</el-radio
            >
          </el-radio-group>
        </el-form-item>
        <el-form-item class="fr mr0">
          <el-button
            type="primary"
            icon="el-icon-search"
            size="mini"
            @click="handleQuery"
            >搜索</el-button
          >
          <el-button
            type="warning"
            icon="el-icon-refresh"
            size="mini"
            @click="resetQuery"
            class="btn-reset"
            >重置</el-button
          >
        </el-form-item>
      </el-row>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleBatch"
          v-hasPermi="['sys:materialOut:init']"
          >批量扫码出库</el-button
        >
      </el-col>
    </el-row>
    <!-- 列表信息-->
    <el-table :data="caseRegList" v-loading="loading" border :fit="true">
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="办件编号"
        align="center"
        prop="caseNumber"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="申请人"
        align="center"
        prop="applyUserName"
        :show-overflow-tooltip="true"
      />
      <!-- <el-table-column label="事项类型" align="center" width="200" prop="serviceTypeName"/>-->
      <el-table-column
        label="证件号"
        align="center"
        prop="cardNumber"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="登记日期"
        align="center"
        prop="caseCreateDate"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="当前状态"
        align="center"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <p v-if="scope.row.outStatus == 1">已出库</p>
          <p v-if="scope.row.outStatus == 0">待出库</p>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        width="300"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="viewAccept(scope.row)"
            v-hasPermi="['sys:materialOut:view']"
            >查看</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            v-if="scope.row.outStatus == 0"
            @click="handleInit(scope.row)"
            v-hasPermi="['sys:materialOut:out']"
            >出库</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-yanzhengma"
            v-if="scope.row.outStatus == 0"
            @click="confirmMaterialMenu(scope.row.id)"
            v-hasPermi="['sys:materialOut:out']"
            >打印出库流转单</el-button
          >

          <!-- 该处打印调用pageoffice-->
          <!--<el-button size="mini" type="text" icon="iconfont zfsoft-chakan"  @click="handlePrint(scope.row)" v-hasPermi="['sys:materialOut:view']" >打印</el-button>-->
          <!-- <el-button size="mini" type="text" icon="iconfont zfsoft-chakan"  @click="printMaterialMenu(scope.row)" v-hasPermi="['sys:materialOut:view']" >打印</el-button>-->
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="queryParams.total > 0"
      :total="queryParams.total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 材料出库信息详细 -->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :title="dialogTitle"
      v-if="addDialogShow"
      :visible.sync="addDialogShow"
      width="900px"
      height="600px"
      scrollbar
      append-to-body
    >
      <el-table v-loading="loading" :data="caseMaterialList">
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="材料名称" align="center" prop="materialName" />
        <el-table-column label="是否已收取" width="150" align="center">
          <template slot-scope="scope">
            <p v-if="scope.row.collectionFlag == 1">已收取</p>
            <p v-if="scope.row.collectionFlag == 0">未收取</p>
          </template>
        </el-table-column>
        <el-table-column label="收取方式" align="center" prop="collectionType">
          <template slot-scope="scope">
            <p v-if="scope.row.collectionType == 1">纸质收取</p>
            <p v-if="scope.row.collectionType == 2">附件上传</p>
            <p v-if="scope.row.collectionType == 3">扫描</p>
            <p v-if="scope.row.collectionType == 4">容缺后补</p>
          </template>
        </el-table-column>

        <el-table-column
          label="收取数量"
          width="100"
          align="center"
          prop="collectionNumber"
        />
      </el-table>
      <div class="el-table__header-wrapper dialog-table">
        <div class="zf-zc-table--title">出库信息</div>
        <el-form label-width="0px" :model="form" :rules="rules" ref="form">
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><i class="require">*</i><b>出库方式：</b></td>
              <td colspan="3">
                <el-radio-group v-model="outType" @change="handleRowChange">
                  <el-radio :label="1">递送员出库</el-radio>
                  <!-- <el-radio :label="2">快递送达</el-radio> -->
                  <el-radio :label="3">机器人出库</el-radio>
                </el-radio-group>
              </td>
            </tr>
            <tr v-if="outType == 1">
              <td><i class="require">*</i><b>领取人姓名：</b></td>
              <td>
                <el-form-item prop="receiverName">
                  <el-input
                    v-model.trim="form.receiverName"
                    name="receiverName"
                    maxlength="25"
                    show-word-limit
                    placeholder="请输入姓名"
                  ></el-input>
                </el-form-item>
              </td>
              <td><i class="require">*</i><b>领取人手机号：</b></td>
              <td>
                <el-form-item prop="receiverPhone">
                  <el-input
                    v-model.trim="form.receiverPhone"
                    name="receiverPhone"
                    maxlength="11"
                    show-word-limit
                    placeholder="请输入手机号"
                  ></el-input>
                </el-form-item>
              </td>
            </tr>
            <tr v-if="outType == 2">
              <td><i class="require">*</i><b>快递公司：</b></td>
              <td>
                <el-form-item prop="kdCompany">
                  <el-input
                    v-model.trim="form.kdCompany"
                    name="kdCompany"
                    maxlength="30"
                    show-word-limit
                    placeholder="请输入快递公司"
                  ></el-input>
                </el-form-item>
              </td>
              <td><i class="require">*</i><b>快递编号：</b></td>
              <td>
                <el-form-item prop="kdCode">
                  <el-input
                    v-model.trim="form.kdCode"
                    name="kdCode"
                    maxlength="100"
                    show-word-limit
                    placeholder="请输入快递编号"
                  ></el-input>
                </el-form-item>
              </td>
            </tr>
            <tr v-if="outType == 2">
              <td><i class="require">*</i><b>寄件人名称：</b></td>
              <td>
                <el-form-item prop="senderUserName">
                  <el-input
                    v-model.trim="form.senderUserName"
                    name="senderUserName"
                    maxlength="50"
                    show-word-limit
                    placeholder="请输入寄件人名称"
                  ></el-input>
                </el-form-item>
              </td>
              <td><i class="require">*</i><b>寄件人手机：</b></td>
              <td>
                <el-form-item prop="senderUserPhone">
                  <el-input
                    v-model.trim="form.senderUserPhone"
                    name="senderUserPhone"
                    maxlength="11"
                    show-word-limit
                    placeholder="请输入寄件人手机"
                  ></el-input>
                </el-form-item>
              </td>
            </tr>
            <tr v-if="outType !== 3">
              <td><i class="require">*</i><b>身份证号码：</b></td>
              <td colspan="3">
                <el-form-item prop="idCard">
                  <el-input
                    v-model.trim="form.idCard"
                    name="idCard"
                    maxlength="18"
                    show-word-limit
                    placeholder="请输入身份证号码"
                    style="width: 75%"
                  ></el-input>
                  <el-button type="primary" @click="scanCard">
                    扫描身份证
                  </el-button>
                </el-form-item>
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">出库</el-button>
        <el-button
          @click="
            () => {
              addDialogShow = false;
              reset();
            }
          "
          >关闭</el-button
        >
      </div>
    </el-dialog>

    <!-- 批量材料出库信息详细 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="dialogTitle"
      v-if="batchView"
      :visible.sync="batchView"
      width="900px"
      append-to-body
    >
      <el-row>
        <el-input
          v-model.trim="batch_caseNumber"
          placeholder="请扫描二维码"
          align="center"
          clearable
          size="100"
          @keyup.enter.native="handleBatchQuery"
        />
      </el-row>
      <div class="zf-zc-table--title">批量出库列表</div>
      <el-table
        v-loading="loading"
        :data="tableData"
        tooltip-effect="dark"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="办件编号"
          width="250"
          align="center"
          prop="caseNumber_pl"
        />
        <el-table-column
          label="申请人名称"
          width="250"
          align="center"
          prop="applyUserName_pl"
        />
        <!--        <el-table-column label="事项类型" align="center" width="150" prop="serviceTypeName_pl"/>-->
        <el-table-column
          label="登记日期"
          width="150"
          align="center"
          prop="caseCreateDate_pl"
        />
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitBatchForm">确定</el-button>
        <el-button
          @click="
            () => {
              batchView = false;
              reset();
            }
          "
          >关闭</el-button
        >
      </div>
    </el-dialog>
    <!--利用vue插件进行打印-->
    <!-- <el-dialog :close-on-click-modal="false" :title="dialogTitle" v-if="printShow" :visible.sync="printShow" width="900px" append-to-body>
      <div id="print">
        <div class="el-table__header-wrapper dialog-table" :model="printCaseInfo" v-for="(info,idx) in printCaseInfo" :key='idx'>
      <div class="zf-zc-table--title">材料流转通知单</div>
        
        <el-form   v-loading="loading" label-width="0" class="dialog-table">
          <table cellspacing="0" cellpadding="0"  border="0" >
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td style="text-align: center"><b>申请人(单位)名称</b></td>
              <td><el-form-item><el-col :span="24">{{info.PO_applyUserName}}</el-col></el-form-item></td>
              <td style="text-align: center"><b>办件编号</b></td>
              <td><el-form-item ><el-col :span="24">{{info.PO_regNumber}}</el-col></el-form-item></td>
            </tr>
            <tr>
              <td style="text-align: center"><b>登记日期：</b></td>
              <td><el-form-item><el-col :span="24">{{info.PO_createDate}}</el-col></el-form-item></td>
              <td style="text-align: center"><b>通知单编号：</b></td>
              <td><el-form-item><el-col :span="24">{{info.PO_noticeNum}}</el-col></el-form-item></td>
            </tr> -->
    <!--            <tr>
              <td style="text-align: center"><b>出库方式：</b></td>
              <td><el-form-item>
                <span v-if="info.PO_outType=='1'">现场领取</span>
                <span v-if="info.PO_outType=='2'">快递送达</span>
              </el-form-item>
              </td>
              <td style="text-align: center"><b>身份证号码：</b></td>
              <td><el-form-item>{{info.PO_idCard}}</el-form-item>
              </td>
            </tr>-->
    <!-- <tr v-if="info.PO_outType=='1'">
              <td style="text-align: center"><b>领取人姓名：</b></td>
              <td><el-form-item>{{info.PO_receiverName}}</el-form-item>
              </td>
              <td style="text-align: center"><b>领取人手机号：</b></td>
              <td><el-form-item>{{info.PO_receiverPhone}}</el-form-item>
              </td>
            </tr>
            <tr v-if="info.PO_outType=='2'">
              <td style="text-align: center"><b>快递公司：</b></td>
              <td><el-form-item>{{info.PO_kdCompany}}</el-form-item>
              </td>
              <td style="text-align: center"><b>快递编号：</b></td>
              <td><el-form-item>{{info.PO_kdCode}}</el-form-item>
              </td>
            </tr>
            <tr v-if="info.PO_outType=='2'">
              <td style="text-align: center"><b>寄件人名称：</b></td>
              <td><el-form-item>{{info.PO_senderUserName}}</el-form-item>
              </td>
              <td style="text-align: center"><b>寄件人手机：</b></td>
              <td><el-form-item>{{info.PO_senderUserPhone}}</el-form-item>
              </td>
            </tr>
            <tr><td style="text-align: center"><b>办件材料：</b></td>
              <td colspan="3"><el-form-item><el-col :span="24">{{info.PO_materialsInfo}}</el-col></el-form-item></td>
            </tr>
          </table>
        </el-form>
        <h3 align="center"><i class="el-icon-document"></i>材料流转记录</h3>
          <el-form   v-loading="loading" label-width="0" class="dialog-table">
            <table cellspacing="0" cellpadding="0"  border="0">
              <colgroup>
                <col width="30%" />
                <col width="30%" />
                <col width="40%" />
              </colgroup>
              <tr>
                <td style="text-align: center;"><b>流转类型</b></td>
                <td style="text-align: center;background-color:#f8f8f9"><b>时间</b></td>
                <td style="text-align: center;"><b>签字</b></td>
              </tr>
              <tr>
                <td style="text-align: center;background-color:#fff"><el-form-item ><el-col :span="24">{{info.PO_circulationType}}</el-col></el-form-item></td>
                <td style="text-align: center;background-color:#fff"><el-form-item ><el-col :span="24">{{info.PO_circulationDate}}</el-col></el-form-item></td>
                <td style="text-align: center;background-color:#fff"><el-form-item ><el-col :span="24">{{info.PO_signature}}</el-col></el-form-item></td>
              </tr>
            </table>
          </el-form>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" v-print="printObjLz">打印</el-button>
        <el-button @click="closePrint">关 闭</el-button>
      </div>
    </el-dialog> -->

    <!-- 办件详细信息 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="openView"
      v-if="openView"
      :title="title"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <view-case-info
        @view-case="closeView"
        :caseOid="industryCaseOid"
      ></view-case-info>
      <div slot="footer" class="zf-text-center">
        <el-button @click="closeView">关 闭</el-button>
      </div>
    </el-dialog>
    <!--    确认出库材料清单-->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      :title="dialogTitle"
      v-if="confirmShow"
      :visible.sync="confirmShow"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <div>
        <div
          :model="printCaseInfo"
          v-for="(info, index) in printCaseInfo"
          :key="index"
        >
          <div class="zf-zc-table--title">确认出库材料清单</div>
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td><b>申请人(单位)名称：</b></td>
              <td colspan="3">{{ info.PO_applyUserName }}</td>
              <!--              <td><b>办件编号：</b></td>
              <td >{{ info.PO_regNumber }}</td>-->
            </tr>
            <tr>
              <td><b>办件编号：</b></td>
              <td colspan="3">
                {{ info.PO_regNumber }}
              </td>
            </tr>
            <tr>
              <td><b>登记日期：</b></td>
              <td>{{ info.PO_createDate }}</td>
              <td><b>通知单编号：</b></td>
              <td>{{ info.PO_noticeNum }}</td>
            </tr>
            <tr v-if="info.PO_outType == '1'">
              <td><b>领取人姓名：</b></td>
              <td>
                <el-form-item>{{ info.PO_receiverName }}</el-form-item>
              </td>
              <td><b>领取人手机号：</b></td>
              <td>
                <el-form-item>{{ info.PO_receiverPhone }}</el-form-item>
              </td>
            </tr>
            <tr v-if="info.PO_outType == '2'">
              <td><b>快递公司：</b></td>
              <td>
                <el-form-item>{{ info.PO_kdCompany }}</el-form-item>
              </td>
              <td><b>快递编号：</b></td>
              <td>
                <el-form-item>{{ info.PO_kdCode }}</el-form-item>
              </td>
            </tr>
            <tr v-if="info.PO_outType == '2'">
              <td><b>寄件人名称：</b></td>
              <td>
                <el-form-item>{{ info.PO_senderUserName }}</el-form-item>
              </td>
              <td><b>寄件人手机：</b></td>
              <td>
                <el-form-item>{{ info.PO_senderUserPhone }}</el-form-item>
              </td>
            </tr>
          </table>

          <el-table
            v-loading="loading"
            ref="multipleTable"
            :data="info.caseRegMaterialList"
            @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" align="center" width="60" />
            <el-table-column
              label="序号"
              width="55"
              type="index"
              align="center"
            >
              <template slot-scope="scope">
                <span>{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              label="材料名称"
              align="center"
              prop="materialName"
              :show-overflow-tooltip="true"
            />
            <el-table-column label="是否已收取" width="150" align="center">
              <template slot-scope="scope">
                <p v-if="scope.row.collectionFlag == 1">已收取</p>
                <p v-if="scope.row.collectionFlag == 0">未收取</p>
              </template>
            </el-table-column>
            <el-table-column
              label="收取方式"
              align="center"
              width="200"
              prop="collectionType"
            >
              <template slot-scope="scope">
                <p v-if="scope.row.collectionType == 1">纸质收取</p>
                <p v-if="scope.row.collectionType == 2">附件上传</p>
                <p v-if="scope.row.collectionType == 3">扫描</p>
                <p v-if="scope.row.collectionType == 4">容缺后补</p>
                <p v-if="scope.row.collectionType == 5">证照库</p>
                <p v-if="scope.row.collectionType == 7">告知承诺</p>
              </template>
            </el-table-column>

            <el-table-column
              label="收取数量"
              width="100"
              align="center"
              prop="collectionNumber"
            />
          </el-table>
          <div class="zf-zc-table--title">材料流转记录</div>

          <el-table :data="info.caseMaterialOutOfStockRecordList" border>
            <el-table-column label="序号" align="center" width="50">
              <template slot-scope="scope">
                {{ scope.$index + 1 }}
              </template>
            </el-table-column>

            <el-table-column
              label="材料流转类型"
              align="center"
              :show-overflow-tooltip="true"
              prop="collectionFlag"
            >
              <template slot-scope="scope">
                <p v-if="scope.row.materialsFlowType == '1'">打印材料流转单</p>
                <p v-if="scope.row.materialsFlowType == '2'">材料出库</p>
                <p v-if="scope.row.materialsFlowType == '3'">材料送达签收</p>
              </template>
            </el-table-column>

            <el-table-column
              label="流转时间"
              align="center"
              :show-overflow-tooltip="true"
              prop="createDate"
            />
            <el-table-column
              label="操作人"
              align="center"
              :show-overflow-tooltip="true"
              prop="receiverName"
            />
            <el-table-column
              label="操作人手机"
              align="center"
              :show-overflow-tooltip="true"
              prop="receiverPhone"
            />
          </el-table>
          <!--          <table
            cellspacing="0"
            cellpadding="0"
            border="0"
            class="zf-zc-table&#45;&#45;common zf-zc-table&#45;&#45;td-center"
          >
            <colgroup>
              <col width="30%" />
              <col width="30%" />
              <col width="40%" />
            </colgroup>
            <tr>
              <th><b>流转类型</b></th>
              <th>
                <b>时间</b>
              </th>
              <th><b>签字</b></th>
            </tr>
            <tr>
              <td>
                {{ info.PO_circulationType }}
              </td>
              <td>
                {{ info.PO_circulationDate }}
              </td>
              <td>
                {{ info.PO_signature }}
              </td>
            </tr>
          </table>-->
        </div>
      </div>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="confirmMaterialOUt"
          >确认出库材料清单</el-button
        >
        <el-button @click="closeConfirm">关 闭</el-button>
      </div>
    </el-dialog>
    <!--利用vue插件进行打印-->
    <el-dialog
      v-dialog-drag
      :close-on-click-modal="false"
      title="打印出库流转单"
      v-if="printShow"
      :visible.sync="printShow"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <div id="print">
        <div
          :model="printCaseInfo"
          v-for="(info, index) in printCaseInfo"
          :key="index"
        >
          <div
            style="
              display: flex;
              align-items: center;
              justify-content: space-between;
              padding: 0 70px;
            "
          >
            <h3>综合窗口: 【001】</h3>
            <div>
              <template v-if="caseMaterialList.length != 0">
                <vue-qr :text="info.appUrl" :size="120"></vue-qr>
              </template>
            </div>
          </div>
          <table
            cellspacing="0"
            cellpadding="0"
            border="0"
            class="zf-zc-table--common zf-zc-table--td-center print-table"
            style="margin-bottom: -1px; margin-top: -60px"
          >
            <caption class="caption">
              材料出库单
            </caption>
            <colgroup>
              <col width="20%" />
              <col width="20%" />
              <col width="30%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td rowspan="6" style="text-align: center"><b>基础信息</b></td>
              <!--              <td><b>申请人(单位)名称：</b></td>
                            <td>{{ info.PO_applyUserName }}</td>
                            <td colspan="2">
                              <div style="float: left; margin-left: 20px">
                                <template
                                  v-if="
                                    info.selectMaterialOids != '' &&
                                    info.selectMaterialOids != null
                                  "
                                >
                                  <vue-qr :text="info.appUrl" :size="93"></vue-qr>
                                </template>
                                <template v-else>
                                  <div>办件编号: {{ info.PO_regNumber }}</div>
                                </template>
                              </div>
                            </td>-->
            </tr>
            <tr>
              <td style="text-align: center"><b>出库单号</b></td>
              <td colspan="3">
                {{ info.PO_regNumber }}
              </td>
            </tr>
            <tr>
              <td style="text-align: center"><b>申请人(单位)名称</b></td>
              <td colspan="3">
                {{ info.PO_applyUserName }}
              </td>
            </tr>
            <tr>
              <td style="text-align: center"><b>办件编号</b></td>
              <td colspan="3">
                {{ info.PO_regNumber }}
              </td>
            </tr>
            <tr>
              <td style="text-align: center"><b>登记日期</b></td>
              <td colspan="3">{{ info.PO_createDate }}</td>
            </tr>
            <tr>
              <td style="text-align: center"><b>所属部门</b></td>
              <td colspan="3">{{ info.PO_organName }}</td>
            </tr>

            <!--            <tr>
                          <td><b>登记日期：</b></td>
                          <td>{{ info.PO_createDate }}</td>
                          <td><b>通知单编号：</b></td>
                          <td>{{ info.PO_noticeNum }}</td>
                        </tr>-->
            <tr v-if="info.PO_outType == '1'">
              <td><b>领取人姓名：</b></td>
              <td>
                {{ info.PO_receiverName }}
              </td>
              <td><b>领取人手机号：</b></td>
              <td>
                {{ info.PO_receiverPhone }}
              </td>
            </tr>
            <tr v-if="info.PO_outType == '2'">
              <td><b>快递公司：</b></td>
              <td>
                {{ info.PO_kdCompany }}
              </td>
              <td><b>快递编号：</b></td>
              <td>
                {{ info.PO_kdCode }}
              </td>
            </tr>
            <tr v-if="info.PO_outType == '2'">
              <td><b>寄件人名称：</b></td>
              <td>
                {{ info.PO_senderUserName }}
              </td>
              <td><b>寄件人手机：</b></td>
              <td>
                {{ info.PO_senderUserPhone }}
              </td>
            </tr>
          </table>

          <table
            cellspacing="0"
            cellpadding="0"
            border="0"
            class="zf-zc-table--common zf-zc-table--td-center"
          >
            <colgroup>
              <col width="20%" />
              <col width="20%" />
              <col width="30%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td
                :rowspan="caseMaterialList.length + 2"
                style="text-align: center"
              >
                <b>办件材料</b>
              </td>
            </tr>
            <tr>
              <th style="text-align: center">序号</th>
              <th style="text-align: left; padding-left: 10px" colspan="2">
                材料名称
              </th>
            </tr>
            <tr v-for="(ite, index) in caseMaterialList">
              <td style="text-align: center; font-weight: bold">
                {{ index + 1 }}
              </td>
              <td colspan="3" style="text-align: left">
                {{ ite.materialName }}
              </td>
            </tr>
          </table>
        </div>
      </div>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="updateMaterialOUt"
          >更改出库材料清单</el-button
        >
        <el-button type="primary" v-print="printObjLz">打印</el-button>
        <el-button @click="closePrint">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  page,
  saveOrUpdate,
  getOneByCaseNumber,
  batchOutMaterial,
  printOutMaterial,
  getOneMaterialInfo,
  getIndustryOneMaterialInfo,
  updateIndustryMaterialOutOfstock
} from "@/api/onelicence/industryManager/industryCaseAccept/industryCaseMaterialOut";
import { queryDistrictSimpleTree } from "@/api/sys/district";
import Treeselect from "@riophae/vue-treeselect";
// import the styles
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import viewCaseInfo from "@/views/onelicence/industryManager/industryCaseAccept/viewCaseInfo";
import vueQr from "vue-qr";
import { queryOrganTree } from "@/api/sys/common";
import { validatePhone, validIDCard } from "@/utils/validate";
import { getOpenRobot } from "@/api/zc/businessManagement/materialOut.js";
import { getIdCardInfo, openIdCard, findIdCard } from "@/api/sys/hardwareScan";
import GPYDrive from "@/api/handwareDrive.js";
import DEVEICETYPE, {
  ID_CARD_V1,
  ID_CARD_V2,
  ID_CARD_V3
} from "@/components/HiSpeedCamera/config.js";
import {
  openIdcardv3,
  closeIdcardv3,
  getdataIdcardv3
} from "@/api/handwarev3.js";
export default {
  name: "materialOut",
  components: { viewCaseInfo, Treeselect, vueQr },
  data() {
    return {
      printObjLz: {
        id: "print",
        popTitle: "材料流转通知单",
        extraHead: '<meta http-equiv="Content-Language"content="zh-cn"/>'
      },
      // 列表数据
      caseRegList: [],
      tableData: [],
      rowNum: 1,
      // 弹窗标题
      dialogTitle: "",
      caseViewOptions: [],
      addDialogShow: false,
      openView: false,
      batchView: false,
      printShow: false,
      industryCaseOid: "",
      batch_caseNumber: "",
      // 机构
      listOrganOptions: [],
      // 区划树
      districtOptions: [],
      //材料信息
      caseMaterialList: [],
      allSmCaseNum: [], //用于存放所扫描的办件编号
      materialOidsArr: [],
      multipleSelection: [],
      printCaseInfo: [],
      indexCaseNumber: "",
      outType: 1,
      form: { id: "", materialOids: "" },
      rules: {
        receiverName: [
          { required: true, message: "领取人姓名不能为空", trigger: "blur" }
        ],
        receiverPhone: [
          { required: true, message: "领取人手机号不能为空", trigger: "blur" },
          {
            validator: validatePhone,
            message: "请输入正确的领取人手机号",
            trigger: "blur"
          }
        ],
        kdCompany: [
          { required: true, message: "快递公司不能为空", trigger: "blur" }
        ],
        kdCode: [
          { required: true, message: "快递编号不能为空", trigger: "blur" }
        ],
        senderUserName: [
          { required: true, message: "寄件人名称不能为空", trigger: "blur" }
        ],
        senderUserPhone: [
          { required: true, message: "寄件人手机不能为空", trigger: "blur" },
          {
            validator: validatePhone,
            message: "请输入寄件人手机号",
            trigger: "blur"
          }
        ],
        idCard: [
          { required: true, message: "身份证号不能为空", trigger: "blur" },
          { validator: validIDCard, trigger: "blur" }
        ]
      },
      // 出库状态
      statusOptions: { "0": "待出库", "1": "已出库" },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        caseNumber: "",
        outStatus: "0",
        total: 0
      },
      id: "",
      MATERIAL_OUT_URL: "",
      confirmShow: false,
      selectMaterialoids: [],
      caseMaterialOutOfStock: { id: "" }
    };
  },
  methods: {
    handleRowChange() {},
    scanCard() {
      if (DEVEICETYPE.IDCARD_CONFIG === ID_CARD_V1) {
        this.getIdcardDatav1();
      }
      if (DEVEICETYPE.IDCARD_CONFIG === ID_CARD_V2) {
        this.getIdcardDatav2();
      }
      if (DEVEICETYPE.IDCARD_CONFIG === ID_CARD_V3) {
        this.getIdcardDatav3();
      }
    },
    async getIdcardDatav3() {
      openIdcardv3().then(res => {
        if (res.StateCode == 0 || res.StateCode == -1) {
          return this.getIdcardDataByv3(); //获取身份证信息
        } else {
          return this.$message.warning("请确认设备连接是否正常");
        }
      });
    },
    async getIdcardDataByv3() {
      getdataIdcardv3().then(resData => {
        if (resData.state == "sucess" && resData.StateCode == 0) {
          let resInfo = JSON.parse(resData.data);
          this.$set(this.form, "idCard", resInfo.number);
          this.$set(this.form, "receiverName", resInfo.name.trim());
        } else if (resData.StateCode == -1) {
          //0:成功-1:设备没有打开-2:寻卡失败-3选卡失败-4读卡失败
          //判断有没有设备
          //打开身份证
          openIdcardv3().then(res => {
            if (res.StateCode == 0 || res.StateCode == -1) {
              this.getIdcardDataByv3(); //重新获取身份证信息
            }
          });
        } else {
          this.$message.error(resData.tips);
          return false;
        }
      });
    },
    async getIdcardDatav2() {
      const resData = await GPYDrive.readCardInfo();
      if (!resData || resData.code !== 200) {
        this.$message.error("请检查设备或连接是否正常!");
        return false;
      }
      let res = {
        CNName: resData.data.name,
        carID: resData.data.number
      };
      this.$set(this.form, "idCard", res.carID);
      this.$set(this.form, "receiverName", res.CNName.trim());
    },
    getIdcardDatav1() {
      openIdCard().then(response => {
        //打开设备
        if (response.state == "sucess") {
          this.getIdcardData(); //重新获取身份证信息
        } else {
          //查找设备
          findIdCard().then(resFind => {
            if ((resFind.state = "sucess" && resFind.StateCode == 0)) {
              this.getIdcardData();
            } else {
              this.$message.error("无法找到设备");
              return false;
            }
          });
        }
      });
    },
    getIdcardData() {
      getIdCardInfo().then(response => {
        let res = response;
        if (!res) {
          this.$message.error("请检查设备或连接是否正常!");
          return false;
        }
        if (res.state == "sucess") {
          // this.form.idCard = res.carID;
          this.$set(this.form, "idCard", res.carID);
          this.$set(this.form, "receiverName", res.CNName.trim());
        } else if (res.StateCode == -1) {
          //0:成功-1:设备没有打开-2:寻卡失败-3选卡失败-4读卡失败
          //判断有没有设备
          //打开身份证
          openIdCard().then(response => {
            if ((response.state = "sucess")) {
              this.getIdcardData(); //重新获取身份证信息
            }
          });
        } else {
          this.$message.error(res.tips);
          return false;
        }
      });
    },
    //初始化确认材料清单页面
    confirmMaterialMenu(id) {
      this.id = id;
      getIndustryOneMaterialInfo(id).then(response => {
        this.caseMaterialList = response.data;
        //alert(null!=this.caseMaterialList&&this.caseMaterialList!="")
        //根据传过来的值获取流转单信息
        printOutMaterial(id).then(response => {
          if (response.code === 200) {
            this.printCaseInfo = response.data;
            if (this.printCaseInfo.length > 0) {
              //this.printCaseInfo[0].appUrl = process.env.VUE_APP_OUT_API_PAGE + "?id=" + this.printCaseInfo[0].id +"&type=onething";
              this.printCaseInfo[0].appUrl =
                this.MATERIAL_OUT_URL +
                "?id=" +
                this.printCaseInfo[0].id +
                "&type=onething";
            }
            this.$nextTick(() => {
              this.printCaseInfo[0].caseRegMaterialList?.forEach?.(item => {
                if (item.auditType === "Y") {
                  this?.$refs?.multipleTable?.[0]?.toggleRowSelection?.(item);
                }
              });
            });
          }
        });

        if (null != this.caseMaterialList && this.caseMaterialList != "") {
          //已确认过就直接到打印页面
          this.printShow = true;
        } else {
          this.confirmShow = true;
        }
      });
    },
    //确认材料清单页面
    confirmMaterialOUt() {
      let materialOids = "";
      //获取选中结果
      this.multipleSelection.forEach(ser => {
        materialOids += ser.caseMaterialOid + ";";
      });
      this.selectMaterialoids = materialOids;
      if (this.selectMaterialoids == "") {
        this.$message.error("选择出库材料不能为空!");
        return false;
      }
      this.caseMaterialOutOfStock.id = this.id;
      this.caseMaterialOutOfStock.selectMaterialOids = materialOids;
      updateIndustryMaterialOutOfstock(this.caseMaterialOutOfStock).then(
        response => {
          if (response.code === 200) {
            this.confirmShow = false;
            this.printShow = true;
            this.printMaterialMenu(this.id);
          }
        }
      );
    },
    closeConfirm() {
      this.confirmShow = false;
      this.getList();
    },
    //跳转到更新材料出库页面
    updateMaterialOUt() {
      this.confirmShow = true;
      this.printShow = true;
      this.confirmMaterialMenu(this.id);
    },
    //复选框的多选
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    districtSel(node, instanceId) {
      this.form.districtName = node.label;
    },
    organSel(node, instanceId) {
      this.form.organName = node.label;
    },
    /** 获取机构数据 */
    getListOrganTree(districtOid, callback) {
      if (districtOid) {
        queryOrganTree(districtOid).then(response => {
          this.listOrganOptions = response.data;
          callback && callback();
        });
      } else {
        this.listOrganOptions = [];
        this.queryParams.organOid = null;
      }
    },
    /** 获取区划树 */
    getDistrictTree(districtOid) {
      let _that = this;
      queryDistrictSimpleTree(districtOid).then(response => {
        _that.districtOptions = response.data;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.caseRegList = response.data.data;
        this.queryParams.total = response.data.total;
        this.loading = false;
      });
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams.outStatus = "0"; //重置radio无效
      this.handleQuery();
    },
    // 表单重置
    reset() {
      Object.assign(this.form, this.$options.data().form);
      this.resetForm("form");
    },
    /** 查看按钮操作 */
    handleView(row) {
      this.indexCaseNumber = row.caseNumber;
      this.openView = true;
      this.dialogTitle = "查看详情";
    },
    /**业态办件查看**/
    viewAccept(row) {
      this.industryCaseOid = row.caseOid;
      this.openView = true;
      this.title = "办件详细信息";
    },
    /** 关闭按钮 */
    closeView() {
      let _that = this;
      _that.openView = false;
      _that.getList();
    },
    /** 出库操作 */
    handleInit(row) {
      this.form = {};
      if (row.caseOid) {
        this.form.id = row.id;
        getIndustryOneMaterialInfo(row.id).then(response => {
          this.caseMaterialList = response.data;
          if (this.caseMaterialList?.length > 0) {
            this.addDialogShow = true;
          } else {
            this.$message.warning("请先打印出库流转单!");
          }
          // for(let material of this.caseMaterialList){
          //    this.materialOidsArr.push(material.caseMaterialOid)
          // }
        });
      } else {
        this.addDialogShow = true;
      }
      this.dialogTitle = "材料出库";
    },
    /** 提交按钮 */
    submitForm: function() {
      // 机器人
      if (this.outType === 3) {
        this.openRobot();
        return;
      }
      let _that = this;
      //  _that.form.materialOids=_that.materialOidsArr.join(';')
      this.form.outType = this.outType;
      this.$refs["form"].validate(valid => {
        if (valid) {
          saveOrUpdate(_that.form).then(response => {
            if (response.code === 200) {
              _that.msgSuccess("保存成功");
              _that.addDialogShow = false;
              setTimeout(() => {
                _that.getList();
              }, 10);
            }
          });
        }
      });
    },
    openRobot() {
      getOpenRobot().then(response => {
        let res = response.data;
        if (res) {
          if (res.code == 200 && !res.message) {
            this.msgSuccess("开门成功！");
          } else {
            this.$message.error(res.message);
          }
        } else {
          this.$message.error("开门失败！");
        }
      });
    },
    /** 批量出库操作 */
    handleBatch() {
      this.batchView = true;
      this.dialogTitle = "批量受理出库";
      this.batch_caseNumber = "";
      this.tableData = [];
      this.allSmCaseNum = [];
    },
    //批量扫描enter查询
    handleBatchQuery() {
      if (this.batch_caseNumber) {
        if (this.allSmCaseNum) {
          if (this.allSmCaseNum.indexOf(this.batch_caseNumber) != -1) {
            this.$message.error("办件已存在!");
            return false;
          }
        }
        getOneByCaseNumber(this.batch_caseNumber).then(response => {
          if (response.data) {
            this.addRow(response.data);
            this.allSmCaseNum.push(this.batch_caseNumber);
          } else {
            this.$message.error("未查询到相关数据!");
          }
          this.batch_caseNumber = "";
        });
      } else {
        this.$message.error("办件编号不能为空!");
      }
    },
    // 增加行
    addRow(caseInfo) {
      var list = {
        rowNum: this.rowNum,
        caseNumber_pl: caseInfo.caseNumber,
        applyUserName_pl: caseInfo.applyUserName,
        serviceTypeName_pl: caseInfo.serviceTypeName,
        caseCreateDate_pl: caseInfo.caseCreateDate,
        id: caseInfo.id
      };
      this.tableData.unshift(list);
      this.rowNum += 1;
    },
    submitBatchForm: function() {
      let _that = this;
      var id = [];
      if (_that.multipleSelection.length <= 0) {
        _that.$message.error("请选择出库数据!");
        return false;
      }
      for (let ss = 0; ss < _that.multipleSelection.length; ss++) {
        id.push(_that.multipleSelection[ss].id);
      }
      var allId = id.join(";");
      batchOutMaterial(allId).then(response => {
        if (response.code === 200) {
          _that.msgSuccess("保存成功");
          _that.batchView = false;
          setTimeout(() => {
            //询问是否打印材料出库单
            this.$confirm("是否打印材料流转单?", "提示", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning"
            })
              .then(() => {
                _that.printMaterialMenu(allId);
              })
              .catch(() => {
                _that.getList();
              });
          }, 10);
        }
      });
    },
    printMaterialMenu(id) {
      this.printShow = true;
      //根据传过来的值获取流转单信息
      getIndustryOneMaterialInfo(id).then(response => {
        this.caseMaterialList = response.data;
      });
    },
    //调用pageoffice的打印
    handlePrint(row) {
      const oid = row.caseOid;
      const id = row.id;
      window.open(
        process.env.VUE_APP_BASE_API_PAGE +
          "/manage/zhuozheng/initTickertape?caseOid=" +
          oid +
          "&id=" +
          id,
        "width=1200px;height=800px;"
      );
    },
    closePrint() {
      this.printShow = false;
      this.getList();
    }
  },
  watch: {
    "form.districtOid": function(val, oldVal) {
      if (!val) {
        this.form.districtName = null;
      }
      this.form.organOid = null;
      this.form.organName = null;
      //机构加载
      this.getListOrganTree(val);
    },
    "form.organOid": function(val, oldVal) {
      if (!val) {
        this.form.organName = null;
      }
    }
  },
  created() {
    this.getList();
    this.getDistrictTree();
  }
};
</script>
<style lang="scss" scoped>
.treeselect {
  width: 200px;
}
.treeselect240 {
  width: 240px;
}
>>> .el-dialog {
  .dialog-footer {
    text-align: center;
  }
}

.caption {
  font-size: 20px;
  margin-bottom: 30px;
  font-weight: bold;
  color: #121b2f;
  margin-top: 40px;
}

.print-table td[colspan="3"] {
  text-align: left;
}

.card-item {
  .el-form-item {
    position: relative;
  }
  .el-input {
    margin-bottom: 0px;
    border-radius: 3px;
    &:last-child {
      margin-bottom: 0;
    }
  }
}

::v-deep .el-input__suffix {
  display: flex;
  align-items: center;
}

.scan-btn {
  position: absolute;
  height: 30px;
  background: #c1d0d9 url(~@/assets/image/scan-icon.png) no-repeat center center !important;
  border: none;
  box-shadow: none;
  top: 10px;
  border-radius: 1px;
}
</style>
