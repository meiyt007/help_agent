git/** * @Author: xldong */
<template>
  <div style="padding:0 10px">
    <el-form ref="form" :model="form" :rules="rules" label-width="0px">
      <el-tabs v-model="activeName">
        <el-tab-pane label="基本信息" name="first">
          <div class="zf-zc-table--title" style="margin-top: unset">基本信息</div>
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <tr>
              <td>
                <i class="require">*</i>
                <b>事项名称：</b>
              </td>
              <td >
                <el-form-item prop="serviceName">
                  <el-input v-model.trim="form.serviceName" placeholder="请输入事项名称" maxlength="200" show-word-limit />
                </el-form-item>
              </td>
              <td><i class="require">*</i><b>所在机构：</b></td>
              <td >
                <el-form-item prop="organOid">
                  <el-select v-model.trim="form.organOid"  placeholder="请选择">
                    <el-option
                      v-for="item in getOrganOptions"
                      :key="item.organOid"
                      :label="item.name"
                      :value="item.organOid">
                    </el-option>
                  </el-select>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <i class="require">*</i>
                <b>实施编码：</b>
              </td>
              <td>
                <el-form-item prop="implementCode">
                  <el-input v-model.trim="form.implementCode" placeholder="请输入实施编码" maxlength="34" show-word-limit />
                </el-form-item>
              </td>
              <td>
                <i class="require">*</i>
                <b>事项类型：</b>
              </td>
              <td>
                <el-form-item prop="serviceTypeOid">
                  <treeselect
                    v-model.trim="form.serviceTypeOid"
                    :options="serviceTypeOptions"
                    style="width: 240px"
                    noOptionsText="暂无数据"
                    :defaultExpandLevel="1"
                    placeholder="请选择事项类型"
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
<!--                <i class="require">*</i>-->
                <b>行使层级：</b>
              </td>
              <td>
                <el-form-item prop="levelName">
                  <el-select
                    v-model="form.levelName"
                    filterable
                    clearable
                    style="width: 50%"
                    placeholder="请选择行使层级"
                  >
                    <el-option
                      v-for="dict in levelList"
                      :key="dict.oid"
                      :label="dict.name"
                      :value="dict.oid"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </td>
              <td>
<!--                <i class="require">*</i>-->
                <b>是否网办：</b>
              </td>
              <td>
                <el-form-item prop="isOnlineHandle">
                  <el-radio-group v-model="form.isOnlineHandle">
                    <el-radio :label="0">否</el-radio>
                    <el-radio :label="1">是</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>
            <!--            <tr>
                          <td>
                            <i class="require">*</i>
                            <b>办理类型：</b>
                          </td>
                          <td colspan="3">
                            <el-form-item prop="handleType">
                              <el-radio-group v-model="form.handleType">
                                <el-radio v-for="item in handleTypes" :label="item.key">{{ item.value }}</el-radio>
                              </el-radio-group>
                            </el-form-item>
                          </td>
                        </tr>-->
            <tr>
              <td>
<!--                <i class="require">*</i>-->
                <b>服务对象：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="serviceObject">
                  <el-checkbox-group v-model="serviceObject">
                    <el-checkbox v-for="item in serviceObjectOptions" :label="item.key">{{ item.value }}</el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
<!--                <i class="require">*</i>-->
                <b>权力来源：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="origin">
                  <el-radio-group v-model="form.origin">
                    <el-radio v-for="item in originOptions" :label="item.oid">
                      {{
                      item.name
                      }}
                    </el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <i class="require">*</i>
                <b>实施主体性质：</b>
              </td>
              <td>
                <el-form-item prop="implementOrganProperty">
                  <el-radio-group v-model="form.implementOrganProperty">
                    <el-radio v-for="item in implementOptions" :label="item.oid">{{ item.name }}</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
              <td>
                <i class="require">*</i>
                <b>办件类型：</b>
              </td>
              <td>
                <el-form-item prop="caseType">
                  <el-radio-group v-model="form.caseType">
                    <el-radio v-for="item in caseTypeOptions" :label="item.key">{{ item.value }}</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
<!--                <i class="require">*</i>-->
                <b>事项分类：</b>
              </td>
              <td colspan="2">
                <el-form-item prop="subjectClassificationName">
                  <el-input v-model.trim="form.subjectClassificationName" placeholder="请选择主题分类" readonly />
                </el-form-item>
              </td>
              <td style="padding-left: 10px; background-color: #fff">
                <el-button style="float: left" size="mini" @click="getSubjectClass" type="primary">选择分类</el-button>
              </td>
            </tr>
            <tr>
              <td>
<!--                <i class="require">*</i>-->
                <b>生命周期分类：</b>
              </td>
              <td colspan="2">
                <el-form-item prop="lifeCycleClassificationName">
                  <el-input v-model.trim="form.lifeCycleClassificationName" placeholder="请选择生命周期分类" readonly />
                </el-form-item>
              </td>
              <td style="padding-left: 10px; background-color: #fff">
                <el-button style="float: left" size="mini" @click="getlifeClass" type="primary">选择分类</el-button>
              </td>
            </tr>
            <tr>
              <td>
<!--                <i class="require">*</i>-->
                <b>办事群体分类：</b>
              </td>
              <td colspan="2">
                <el-form-item prop="serviceGroupClassificationName">
                  <el-input v-model.trim="form.serviceGroupClassificationName" placeholder="请选择办事群体分类" readonly />
                </el-form-item>
              </td>
              <td style="padding-left: 10px; background-color: #fff">
                <el-button style="float: left" size="mini" @click="getBsGroupClass" type="primary">选择分类</el-button>
              </td>
            </tr>
            <tr>
              <td>
                <b>实施主体：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="hostOffices">
                  <el-input v-model.trim="form.hostOffices" placeholder="请输入主办处室" maxlength="100" show-word-limit />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
<!--                <i class="require">*</i>-->
                <b>办理形式：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="handleForm">
                  <el-checkbox-group v-model="handleForm">
                    <el-checkbox v-for="item in handleFormOptions" :label="item.key">{{ item.value }}</el-checkbox>
                  </el-checkbox-group>

                  <!--                  <el-radio-group v-model="form.handleForm">
                    <el-radio
                      v-for="item in handleFormOptions"
                      :label="item.key"
                      >{{ item.value }}</el-radio
                    >
                  </el-radio-group>-->
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
<!--                <i class="require">*</i>-->
                <b>办理深度：</b>
              </td>
              <td colspan="3" class="handle-depth">
                <el-form-item prop="handleDepth">
                  <div class="handle-depth--item" v-for="(item, index) in handleDepthOptions" :key="index">
                    <el-checkbox v-model="handleDepth" :label="item.oid">
                      {{
                      item.name
                      }}
                    </el-checkbox>
                    <span style="padding-left: 20px">{{ item.memo }}</span>
                  </div>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
<!--                <i class="require">*</i>-->
                <b>是否支持网上支付：</b>
              </td>
              <td>
                <el-form-item prop="onlinePayFlag">
                  <el-radio-group v-model="form.onlinePayFlag">
                    <el-radio :label="1">支持</el-radio>
                    <el-radio :label="0">不支持</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
              <td>
<!--                <i class="require">*</i>-->
                <b>是否支持物流快递：</b>
              </td>
              <td>
                <el-form-item prop="expressFlag">
                  <el-radio-group v-model="form.expressFlag">
                    <el-radio :label="1">不支持</el-radio>
                    <el-radio :label="0">支持</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
<!--                <i class="require">*</i>-->
                <b>行政管辖地：</b>
              </td>
              <td>
                <el-form-item prop="adminJurisdiction">
                  <el-radio-group v-model="form.adminJurisdiction">
                    <el-radio :label="'0'">定点办理</el-radio>
                    <el-radio :label="'1'">跨区域办理</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
              <td>
<!--                <i class="require">*</i>-->
                <b>是否联合办理：</b>
              </td>
              <td>
                <el-form-item prop="unionOrganFlag">
                  <el-radio-group v-model="form.unionOrganFlag">
                    <el-radio :label="0">否</el-radio>
                    <el-radio :label="1">是</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>
            <tr v-if="form.adminJurisdiction == 1">
              <td>
                <b>通办范围：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="handleScope">
                  <el-checkbox-group v-model="handleScope">
                    <el-checkbox v-for="item in tbfwOptions" :label="item.oid">{{ item.name }}</el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
              </td>
            </tr>
            <tr v-if="form.unionOrganFlag == 1">
              <td>
                <b>联合办理机构：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="unionOrgan">
                  <el-input v-model.trim="form.unionOrgan" placeholder="请输入联合办理机构" maxlength="250" show-word-limit />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
<!--                <i class="require">*</i>-->
                <b>投诉方式：</b>
              </td>
              <td>
                <el-form-item prop="tsType">
                  <el-checkbox-group v-model="tsType" class="ts-type" @change="changeTsText">
                    <el-checkbox :label="'0'">
                      窗口
                      <el-input
                        :readonly="tsCk"
                        style="margin: 5px 10px"
                        v-model.trim="form.tsCkText"
                        placeholder="窗口投诉方式内容"
                        maxlength="50"
                        show-word-limit
                      />
                    </el-checkbox>
                    <el-checkbox :label="'1'">
                      网络
                      <el-input
                        :readonly="tsWl"
                        style="margin: 5px 10px"
                        v-model.trim="form.tsWlText"
                        placeholder="网络投诉方式内容"
                        maxlength="50"
                        show-word-limit
                      />
                    </el-checkbox>
                    <el-checkbox :label="'2'">
                      邮件
                      <el-input
                        :readonly="tsYj"
                        style="margin: 5px 10px"
                        v-model.trim="form.tsYjText"
                        placeholder="邮件投诉方式内容"
                        maxlength="50"
                        show-word-limit
                      />
                    </el-checkbox>
                    <el-checkbox :label="'3'">
                      信件
                      <el-input
                        :readonly="tsXj"
                        style="margin: 5px 10px"
                        v-model.trim="form.tsXjText"
                        placeholder="信件投诉方式内容"
                        maxlength="50"
                        show-word-limit
                      />
                    </el-checkbox>
                    <el-checkbox :label="'4'">
                      电话
                      <el-input
                        :readonly="tsDh"
                        style="margin: 5px 10px"
                        v-model.trim="form.tsDhText"
                        placeholder="电话投诉方式内容"
                        maxlength="50"
                        show-word-limit
                      />
                    </el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
              </td>
              <td>
                <i class="require">*</i>
                <b>咨询方式：</b>
              </td>
              <td>
                <el-form-item prop="zxType">
                  <el-checkbox-group v-model="zxType" class="ts-type" @change="changeZxText">
                    <el-checkbox :label="'4'">
                      电话
                      <el-input
                        :readonly="zxDh"
                        style="margin: 5px 10px"
                        v-model.trim="form.zxDhText"
                        placeholder="电话咨询方式内容"
                        maxlength="50"
                        show-word-limit
                      />
                    </el-checkbox>
                    <el-checkbox :label="'1'">
                      网络
                      <el-input
                        :readonly="zxWl"
                        style="margin: 5px 10px"
                        v-model.trim="form.zxWlText"
                        placeholder="网络咨询方式内容"
                        maxlength="50"
                        show-word-limit
                      />
                    </el-checkbox>
                    <el-checkbox :label="'0'">
                      窗口
                      <el-input
                        :readonly="zxCk"
                        style="margin: 5px 10px"
                        v-model.trim="form.zxCkText"
                        placeholder="窗口咨询方式内容"
                        maxlength="50"
                        show-word-limit
                      />
                    </el-checkbox>
                    <el-checkbox :label="'3'">
                      信件
                      <el-input
                        :readonly="zxXj"
                        style="margin: 5px 10px"
                        v-model.trim="form.zxXjText"
                        placeholder="信件咨询方式内容"
                        maxlength="200"
                        show-word-limit
                      />
                    </el-checkbox>
                    <el-checkbox :label="'2'">
                      邮件
                      <el-input
                        :readonly="zxYj"
                        style="margin: 5px 10px"
                        v-model.trim="form.zxYjText"
                        placeholder="邮件咨询方式内容"
                        maxlength="50"
                        show-word-limit
                      />
                    </el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
<!--                <i class="require">*</i>-->
                <b>是否有中介服务：</b>
              </td>
              <td>
                <el-form-item prop="isZjfw">
                  <el-radio-group v-model="form.isZjfw">
                    <el-radio :label="0">否</el-radio>
                    <el-radio :label="1">是</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
              <td>
<!--                <i class="require">*</i>-->
                <b>是否含有特别程序：</b>
              </td>
              <td>
                <el-form-item prop="isSpecial">
                  <el-radio-group v-model="form.isSpecial">
                    <el-radio :label="0">否</el-radio>
                    <el-radio :label="1">是</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>
            <tr v-if="form.isZjfw == 1">
              <td>
<!--                <i class="require">*</i>-->
                <b>中介服务名称：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="mediationName">
                  <el-input v-model.trim="form.mediationName" placeholder="请输入中介服务名称" maxlength="100" show-word-limit />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>网站或系统名称：</b>
              </td>
              <td>
                <el-form-item prop="webName">
                  <el-input v-model.trim="form.webName" placeholder="请输入网站或系统名称" maxlength="50" show-word-limit />
                </el-form-item>
              </td>
              <td>
                <i class="require">*</i>
                <b>网办地址：</b>
              </td>
              <td>
                <el-form-item prop="onlineApplyLink">
                  <el-input
                    v-model.trim="form.onlineApplyLink"
                    placeholder="请输入网上申请链接"
                    maxlength="500"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
<!--                <i class="require">*</i>-->
                <b>移动端对接单点登录：</b>
              </td>
              <td>
                <el-form-item prop="appIssingleLogin">
                  <el-radio-group v-model="form.appIssingleLogin">
                    <el-radio :label="0">否</el-radio>
                    <el-radio :label="1">是</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
              <td>
                <i class="require" v-if="form.appIssingleLogin == 1">*</i>
                <b>移动端办理地址：</b>
              </td>
              <td>
                <el-form-item prop="mobileTerminalUrl">
                  <el-input
                    v-model.trim="form.mobileTerminalUrl"
                    placeholder="请输入移动端办理地址"
                    maxlength="200"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
<!--                <i class="require">*</i>-->
                <b>窗口端对接单点登录：</b>
              </td>
              <td>
                <el-form-item prop="isSingleLogin">
                  <el-radio-group v-model="form.isSingleLogin">
                    <el-radio :label="'0'">否</el-radio>
                    <el-radio :label="'1'">是</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
              <td>
                <i class="require" v-if="form.isSingleLogin == '1'">*</i>
                <b>窗口端办理地址：</b>
              </td>
              <td>
                <el-form-item prop="linkAddr">
                  <el-input v-model.trim="form.linkAddr" placeholder="请输入窗口端办理地址" maxlength="200" show-word-limit />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
<!--                <i class="require">*</i>-->
                <b>是否支持自助终端办理：</b>
              </td>
              <td>
                <el-form-item prop="terminalProcessing">
                  <el-radio-group v-model="form.terminalProcessing">
                    <el-radio :label="0">否</el-radio>
                    <el-radio :label="1">是</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
              <td>
                <i class="require" v-if="form.terminalProcessing == '1'">*</i>
                <b>自助终端办理地址：</b>
              </td>
              <td>
                <el-form-item prop="terminalProcessingUrl">
                  <el-input v-model.trim="form.terminalProcessingUrl" placeholder="请输入自助终端办理地址" maxlength="200" show-word-limit />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
<!--                <i class="require">*</i>-->
                <b>到现场次数：</b>
              </td>
              <td>
                <el-form-item prop="countToScence">
                  <el-radio-group v-model="form.countToScence">
                    <el-radio :label="'0'">0次</el-radio>
                    <el-radio :label="'1'">1次</el-radio>
                    <el-radio :label="'2'">2次</el-radio>
                    <el-radio :label="'3'">多次</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
              <td>
                <i class="require">*</i>
                <b>事项导入时是否屏蔽：</b>
              </td>
              <td>
                <el-form-item prop="isShield">
                  <el-radio-group v-model="form.isShield">
                    <el-radio v-for="item in isShieldOptions" :label="item.key">{{ item.value }}</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>
            <tr v-if="form.countToScence && form.countToScence != 0">
              <td>
                <b>必须现场办理原因说明：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="reasonToScence">
                  <el-input
                    type="textarea"
                    v-model.trim="form.reasonToScence"
                    placeholder="请输入必须现场办理原因说明"
                    maxlength="500"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>是否需要人证核验：</b>
              </td>
              <td>
                <el-form-item prop="showRzhs">
                  <el-radio-group v-model="form.showRzhs">
                    <el-radio :label="0">不需要</el-radio>
                    <el-radio :label="1">需要</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
              <td>
<!--                <i class="require">*</i>-->
                <b>公开方式：</b>
              </td>
              <td>
                <el-form-item prop="openWay">
                  <el-radio-group v-model="form.openWay">
                    <el-radio :label="1">主动公开</el-radio>
                    <el-radio :label="2">依申请公开</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
<!--                <i class="require">*</i>-->
                <b>公开渠道：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="openChannel">
                  <el-radio-group v-model="form.openChannel">
                    <el-radio :label="1">政府公报</el-radio>
                    <el-radio :label="2">政府网站</el-radio>
                    <el-radio :label="3">新闻发布会</el-radio>
                    <el-radio :label="4">报刊、广播、电视</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>公开内容：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="openContent">
                  <el-input
                    type="textarea"
                    v-model.trim="form.openContent"
                    placeholder="请输入公开内容"
                    maxlength="500"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>公开文书内容：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="openDocumentContent">
                  <el-input
                    type="textarea"
                    v-model.trim="form.openDocumentContent"
                    placeholder="请输入公开文书内容"
                    maxlength="500"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>收件人：</b>
              </td>
              <td>
                <el-form-item prop="postAcceptName">
                  <el-input v-model.trim="form.postAcceptName" placeholder="请输入收件人" maxlength="100" show-word-limit />
                </el-form-item>
              </td>
              <td>
                <b>邮递地址：</b>
              </td>
              <td>
                <el-form-item prop="postAddr">
                  <el-input v-model.trim="form.postAddr" placeholder="请输入邮递地址" maxlength="100" show-word-limit />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
<!--                <i class="require">*</i>-->
                <b>是否支持容缺受理：</b>
              </td>
              <td>
                <el-form-item prop="isSupportTolerance">
                  <el-radio-group v-model="form.isSupportTolerance">
                    <el-radio :label="1">支持</el-radio>
                    <el-radio :label="0">不支持</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
              <td>
<!--                <i class="require">*</i>-->
                <b>是否支持告知承诺：</b>
              </td>
              <td>
                <el-form-item prop="isSupportInformCommitment">
                  <el-radio-group v-model="form.isSupportInformCommitment">
                    <el-radio :label="1">支持</el-radio>
                    <el-radio :label="0">不支持</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>审批对象：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="approvedBy">
                  <el-input
                    v-model.trim="form.approvedBy"
                    placeholder="请输入审批对象"
                    maxlength="255"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>事项办理特别程序</b>
              </td>
              <td colspan="3">
                <el-form-item prop="specialProcedure">
                  <el-input
                    v-model.trim="form.specialProcedure"
                    placeholder="特别程序，没有写无"
                    maxlength="255"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>网办深度</b>
              </td>
              <td colspan="3">
                <el-form-item prop="onlineHandleDepth">
                  <el-input
                    v-model.trim="form.onlineHandleDepth"
                    placeholder="请输入网办深度"
                    maxlength="255"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>办理地点：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="handleAddress">
                  <el-input
                    v-model.trim="form.handleAddress"
                    placeholder="请输入办理地点"
                    maxlength="255"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>日常用语：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="popularTerms">
                  <el-input
                    type="textarea"
                    v-model.trim="form.popularTerms"
                    placeholder="请输入日常用语"
                    maxlength="500"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>适用范围：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="applyRange">
                  <el-input
                    type="textarea"
                    v-model.trim="form.applyRange"
                    placeholder="请输入适用范围"
                    maxlength="5000"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>填报须知：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="fillNotice">
                  <el-input
                    type="textarea"
                    v-model.trim="form.fillNotice"
                    placeholder="请输入填报须知"
                    maxlength="5000"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
          </table>

          <div class="zf-zc-table--title">办理结果</div>
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <tr>
              <td>
<!--                <i class="require">*</i>-->
                <b>办理结果送达方式：</b>
              </td>
              <td>
                <el-form-item prop="resultDeliveryWay">
                  <el-checkbox-group v-model="resultDeliveryWay">
                    <!--                    <el-checkbox :label="'6'">自行取件</el-checkbox>
                    <el-checkbox :label="'4'">快递送达</el-checkbox>
                    <el-checkbox :label="'2'">其他</el-checkbox>-->
                    <el-checkbox :label="'6'">自件</el-checkbox>
                    <el-checkbox :label="'4'">物流</el-checkbox>
                    <el-checkbox :label="'2'">公告</el-checkbox>
                    <el-checkbox :label="'7'">直接送达</el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
              </td>
              <td>
                <b>结果样本类型：</b>
              </td>
              <td>
                <el-form-item prop="sxServiceExtend.resultSampleType">
                  <el-radio-group v-model="form.sxServiceExtend.resultSampleType">
                    <el-radio :label="0">证照</el-radio>
                    <el-radio :label="1">批文</el-radio>
                    <el-radio :label="2">其他</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>审批结果名称：</b>
              </td>
              <td>
                <el-form-item prop="sxServiceExtend.resultName">
                  <el-input
                    v-model.trim="form.sxServiceExtend.resultName"
                    placeholder="请输入结果名称"
                    maxlength="50"
                    show-word-limit
                  />
                </el-form-item>
              </td>
              <td>
                <b>送达期限：</b>
              </td>
              <td>
                <el-form-item prop="deliverTerm">
                  <el-input
                    v-model.trim="form.deliverTerm"
                    placeholder="请输入送达期限"
                    maxlength="50"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>审批结果样本：</b>
              </td>
              <td>
                <el-form-item prop="resultSampleAddr">
                  <el-upload
                    class="upload-demo"
                    action
                    :multiple="false"
                    :http-request="uploadFiles"
                    :before-upload="beforeUpload"
                    :on-error="uploadError"
                    :file-list="fileList"
                    :on-change="handleChange"
                    :show-file-list="false"
                    accept="accept"
                  >
                    <el-button size="mini" type="primary" icon="el-icon-upload">点击上传</el-button>
                  </el-upload>
                  <div v-if="form.sxServiceExtend.resultSampleAddr">
                    <span
                      v-if="
                        form.sxServiceExtend.resultSampleAddrFile != null &&
                        form.sxServiceExtend.resultSampleAddrFile != ''
                      "
                    >
                      {{
                        form.sxServiceExtend.resultSampleAddrFile.originName
                      }}
                    </span>
                    <el-link
                      type="primary"
                      @click="
                        downLoadFileUrl(form.sxServiceExtend.resultSampleAddr)
                      "
                    >下载</el-link>|
                    <el-link
                      type="primary"
                      @click="
                        delresultFile(form.sxServiceExtend.resultSampleAddr)
                      "
                    >删除</el-link>
                  </div>
                </el-form-item>
              </td>
              <td>
                <b>是否生成电子证照：</b>
              </td>
              <td>
                <el-form-item prop="elecCertProduceFlag">
                  <el-radio-group v-model="form.elecCertProduceFlag">
                    <el-radio :label="1">生成</el-radio>
                    <el-radio :label="0">不生成</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>
          </table>

          <div class="zf-zc-table--title">办理流程</div>
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <tr>
              <td>
<!--                <i class="require">*</i>-->
                <b>办理流程图：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="handleFlow">
                  <el-upload
                    class="upload-demo"
                    action=""
                    multiple
                    :http-request="uploadFilesFlow"
                    :on-remove="removeHandleFlow"
                    :before-upload="beforeUpload"
                    :on-error="uploadError"
                    :file-list="fileFlowList"
                    :on-change="handleChangeFlow"
                    :show-file-list="false"
                    accept="accept"
                  >
                    <el-button size="mini" type="primary" icon="el-icon-upload">点击上传</el-button>
                  </el-upload>
                  <div v-if="form.sxServiceExtend.handleFlow">
                    <span>
                      {{
                        form.sxServiceExtend.handleFlowFile.originName
                      }}
                    </span>
                    <el-link type="primary" @click="downLoadFileUrl(form.sxServiceExtend.handleFlow)">下载</el-link>|
                    <el-link type="primary" @click="delFlowFile(form.sxServiceExtend.handleFlow)">删除</el-link>
                  </div>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>办理流程说明：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="sxServiceExtend.handleFlowInstruction">
                  <el-input
                    type="textarea"
                    v-model.trim="form.sxServiceExtend.handleFlowInstruction"
                    placeholder="请输入办理流程说明"
                    maxlength="500"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
          </table>
        </el-tab-pane>

        <el-tab-pane label="扩展信息" name="second">
          <div class="zf-zc-table--title" style="margin-top: unset">扩展信息</div>
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <tr>
              <td>
                <i class="require">*</i>
                <b>法定时限：</b>
              </td>
              <td>
                <el-form-item prop="sxServiceExtend.legalLimit">
                  <el-input-number v-model="form.sxServiceExtend.legalLimit" :min="1" :max="999" />
                </el-form-item>
              </td>
              <td>
                <i class="require">*</i>
                <b>法定时限类型：</b>
              </td>
              <td>
                <el-form-item prop="sxServiceExtend.legalLimitType">
                  <el-radio-group v-model.trim="form.sxServiceExtend.legalLimitType">
                    <el-radio :label="'W'">工作日</el-radio>
                    <el-radio :label="'N'">自然日</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <i class="require">*</i>
                <b>承诺时限：</b>
              </td>
              <td>
                <el-form-item prop="sxServiceExtend.promiseLimit">
                  <el-input-number v-model="form.sxServiceExtend.promiseLimit" :min="1" :max="999" />
                </el-form-item>
              </td>
              <td>
                <i class="require">*</i>
                <b>承诺时限类型：</b>
              </td>
              <td>
                <el-form-item prop="sxServiceExtend.promiseLimitType">
                  <el-radio-group v-model.trim="form.sxServiceExtend.promiseLimitType">
                    <el-radio :label="'W'">工作日</el-radio>
                    <el-radio :label="'N'">自然日</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>承诺期限说明：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="promiseTimeDesc">
                  <el-input
                    v-model.trim="form.promiseTimeDesc"
                    placeholder="请输入承诺期限说明"
                    maxlength="200"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>设定依据：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="promiseTimeDesc">
                  <el-input
                    v-model.trim="form.sxServiceExtend.setAccord"
                    placeholder="请输入设定依据"
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>法定办结时限说明：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="sxServiceExtend.legalLimitTest">
                  <el-input
                    type="textarea"
                    v-model.trim="form.sxServiceExtend.legalLimitTest"
                    placeholder="请输入法定办结时限说明"
                    maxlength="500"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td rowspan="2">
                <b>收件凭证：</b>
              </td>
              <td style="padding-top: unset">
                <b>送达渠道：</b>
              </td>
              <td colspan="3" style="background-color: #fff; padding: 20px 10px 0 10px">
                <el-form-item prop="sxServiceExtend.recipientVoucherChannel">
                  <el-input
                    v-model.trim="form.sxServiceExtend.recipientVoucherChannel"
                    placeholder="请输入送达渠道"
                    maxlength="200"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td style="text-align: left; background-color: #fff; padding: 10px">
                <b>送达相关要求：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="sxServiceExtend.recipientVoucherRequirement">
                  <el-input
                    v-model.trim="
                      form.sxServiceExtend.recipientVoucherRequirement
                    "
                    placeholder="请输入送达相关要求"
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>面向自然人地方特色主题分类：</b>
              </td>
              <td>
                <el-form-item prop="sxServiceExtend.naturalCharacteristic">
                  <el-checkbox-group v-model="naturalCharacteristicObject">
                    <el-checkbox v-for="item in naturalCharacteristicOptions" :label="item.key">{{ item.value }}</el-checkbox>
                  </el-checkbox-group>
                  <!--                  <el-input
                    v-model.trim="form.sxServiceExtend.naturalCharacteristic"
                    placeholder="请输入面向自然人地方特色主题分类名称"
                    maxlength="500"
                    show-word-limit
                  />-->
                </el-form-item>
              </td>
              <td>
                <b>面向法人地方特色主题分类：</b>
              </td>
              <td>
                <el-form-item prop="sxServiceExtend.legalCharacteristic">
                  <el-checkbox-group v-model="legalCharacteristicObject">
                    <el-checkbox v-for="item in legalCharacteristicOptions" :label="item.key">{{ item.value }}</el-checkbox>
                  </el-checkbox-group>
                  <!--                  <el-input
                    v-model.trim="form.sxServiceExtend.legalCharacteristic"
                    placeholder="请输入面向法人地方特色主题分类名称"
                    maxlength="500"
                    show-word-limit
                  />-->
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>审批内容：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="sxServiceExtend.exerciseContent">
                  <el-input
                    type="textarea"
                    v-model.trim="form.sxServiceExtend.exerciseContent"
                    placeholder="请输入审批内容"
                    maxlength="500"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
<!--                <i class="require">*</i>-->
                <b>运行系统：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="sxServiceExtend.runSystem">
                  <el-radio-group v-model.trim="form.sxServiceExtend.runSystem">
                    <el-radio :label="'0'">国家级</el-radio>
                    <el-radio :label="'1'">省级</el-radio>
                    <el-radio :label="'2'">市级</el-radio>
                    <el-radio :label="'3'">区级</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
<!--                <i class="require">*</i>-->
                <b>证照目录类型：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="sxServiceExtend.directoryType">
                  <el-radio-group v-model.trim="form.sxServiceExtend.directoryType">
                    <el-radio :label="0">个人基本信息</el-radio>
                    <el-radio :label="1">个人资格信息</el-radio>
                    <el-radio :label="2">法人基本信息</el-radio>
                    <el-radio :label="3">企业资格信息</el-radio>
                    <el-radio :label="4">投资项目审批环节结果信息</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>审批条件：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="sxServiceExtend.censorStandard">
                  <el-input
                    type="textarea"
                    v-model.trim="form.sxServiceExtend.censorStandard"
                    placeholder="请输入审查标准"
                    maxlength="5000"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
<!--                <i class="require">*</i>-->
                <b>是否长期有效：</b>
              </td>
              <td>
                <el-form-item prop="sxServiceExtend.expipyFlag">
                  <el-radio-group v-model.trim="form.sxServiceExtend.expipyFlag">
                    <el-radio :label="0">否</el-radio>
                    <el-radio :label="1">是</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
              <td>
                <b>有效期时间：</b>
              </td>
              <td v-if="form.sxServiceExtend.expipyFlag == 0">
                <el-form-item prop="sxServiceExtend.expipyDate">
                  <el-input
                    v-model.trim="form.sxServiceExtend.expipyDate"
                    placeholder="请输入有效期时间"
                    maxlength="3"
                    show-word-limit
                  />
                </el-form-item>月
              </td>
              <td v-else>长期有效</td>
            </tr>
            <tr>
              <td>
<!--                <i class="require">*</i>-->
                <b>是否支持预约：</b>
              </td>
              <td>
                <el-form-item prop="appointmentFlag">
                  <el-radio-group v-model.trim="form.appointmentFlag">
                    <el-radio :label="0">不支持</el-radio>
                    <el-radio :label="1">支持</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
              <td>
                <b>预约渠道：</b>
              </td>
              <td>
                <el-form-item prop="bookChannel">
                  <el-checkbox-group v-model="bookChannel">
                    <el-checkbox :label="'0'">网址</el-checkbox>
                    <el-checkbox :label="'1'">地点</el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>预约网址：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="sxServiceExtend.bookUrl">
                  <el-input
                    v-model.trim="form.sxServiceExtend.bookUrl"
                    placeholder="请输入预约网址"
                    maxlength="200"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>预约地点：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="sxServiceExtend.bookSite">
                  <el-input
                    v-model.trim="form.sxServiceExtend.bookSite"
                    placeholder="请输入预约地点"
                    maxlength="100"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
<!--                <i class="require">*</i>-->
                <b>是否权限划分：</b>
              </td>
              <td>
                <el-form-item prop="sxServiceExtend.isPermisionDivid">
                  <el-radio-group v-model.trim="form.sxServiceExtend.isPermisionDivid">
                    <el-radio :label="0">否</el-radio>
                    <el-radio :label="1">是</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
              <td>
<!--                <i class="require">*</i>-->
                <b>是否有数量限制：</b>
              </td>
              <td>
                <el-form-item prop="sxServiceExtend.numberLimitType">
                  <el-radio-group v-model.trim="form.sxServiceExtend.numberLimitType">
                    <el-radio :label="0">否</el-radio>
                    <el-radio :label="1">是</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>权限划分标准：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="sxServiceExtend.dividStandard">
                  <el-input
                    type="textarea"
                    v-model.trim="form.sxServiceExtend.dividStandard"
                    placeholder="请输入权限划分标准"
                    maxlength="500"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>数量限制说明：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="sxServiceExtend.numlimitContent">
                  <el-input
                    type="textarea"
                    v-model.trim="form.sxServiceExtend.numlimitContent"
                    placeholder="请输入数量限制说明"
                    maxlength="500"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>数量限制依据：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="sxServiceExtend.numlimitDesc">
                  <el-input
                    type="textarea"
                    v-model.trim="form.sxServiceExtend.numlimitDesc"
                    placeholder="请输入数量限制依据"
                    maxlength="500"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>年审年检：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="sxServiceExtend.auditChannel">
                  <el-input
                    v-model.trim="form.sxServiceExtend.auditChannel"
                    placeholder="请输入年审年检"
                    maxlength="500"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
<!--                <i class="require">*</i>-->
                <b>阶段性办理：</b>
              </td>
              <td>
                <el-form-item prop="sxServiceExtend.isStageHandle">
                  <el-radio-group v-model.trim="form.sxServiceExtend.isStageHandle">
                    <el-radio :label="0">否</el-radio>
                    <el-radio :label="1">是</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
              <td>
<!--                <i class="require">*</i>-->
                <b>是否进驻政务大厅：</b>
              </td>
              <td>
                <el-form-item prop="sxServiceExtend.isEntryCenter">
                  <el-radio-group v-model.trim="form.sxServiceExtend.isEntryCenter">
                    <el-radio :label="0">否</el-radio>
                    <el-radio :label="1">是</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
<!--                <i class="require">*</i>-->
                <b>是否收件及受理：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="sjStatus">
                  <el-radio-group v-model.trim="form.sjStatus">
                    <el-radio :label="1">收件</el-radio>
                    <el-radio :label="2">收件即受理</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>是否收费：</b>
              </td>
              <td>
                <el-form-item prop="chargeFlag">
                  <el-radio-group v-model.trim="form.chargeFlag">
                    <el-radio :label="0">否</el-radio>
                    <el-radio :label="1">是</el-radio>
                  </el-radio-group>
                </el-form-item>
              </td>
              <td>
                <b>收费依据：</b>
              </td>
              <td>
                <el-form-item prop="chargeEvidence">
                  <el-upload
                    class="upload-demo"
                    action
                    :multiple="false"
                    :http-request="uploadFilesSfyj"
                    :before-upload="beforeUpload"
                    :on-error="uploadError"
                    :file-list="fileSfyjList"
                    :on-change="handleChangeSfyj"
                    :show-file-list="false"
                    accept="accept"
                  >
                    <el-button size="mini" type="primary" icon="el-icon-upload">点击上传</el-button>
                  </el-upload>
                  <div v-if="form.sxServiceExtend.chargeEvidence">
                    <span>
                      {{
                      form.sxServiceExtend.chargeEvidenceFile.originName
                      }}
                    </span>
                    <el-link
                      type="primary"
                      @click="
                        downLoadFileInfo(form.sxServiceExtend.chargeEvidence)
                      "
                    >下载</el-link>|
                    <el-link type="primary" @click="delSfyjFile(form.sxServiceExtend.chargeEvidence)">删除</el-link>
                  </div>
                </el-form-item>
              </td>
            </tr>
            <tr v-if="form.chargeFlag == 1">
              <td>
                <b>收费环节：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="chargeLinkOid">
                  <treeselect
                    v-model.trim="form.chargeLinkOid"
                    :options="chargeLinkList"
                    style="width: 240px"
                    noOptionsText="暂无数据"
                    :defaultExpandLevel="1"
                    placeholder="请选择收费环节"
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>收费标准：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="sxServiceExtend.chargeStandard">
                  <el-input
                    type="textarea"
                    v-model.trim="form.sxServiceExtend.chargeStandard"
                    placeholder="请输入收费标准"
                    maxlength="500"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>收费依据：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="sxServiceExtend.chargeAccord">
                  <el-input
                    type="textarea"
                    v-model.trim="form.sxServiceExtend.chargeAccord"
                    placeholder="请输入收费依据"
                    maxlength="500"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>乡镇街道名称：</b>
              </td>
              <td>
                <el-form-item prop="sxServiceExtend.townName">
                  <el-input
                    v-model.trim="form.sxServiceExtend.townName"
                    placeholder="请输入乡镇街道名称"
                    maxlength="100"
                    show-word-limit
                  />
                </el-form-item>
              </td>
              <td>
                <b>乡镇街道代码：</b>
              </td>
              <td>
                <el-form-item prop="sxServiceExtend.townCode">
                  <el-input
                    v-model.trim="form.sxServiceExtend.townCode"
                    placeholder="请输入乡镇街道代码"
                    maxlength="50"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>村居社区名称：</b>
              </td>
              <td>
                <el-form-item prop="sxServiceExtend.villageName">
                  <el-input
                    v-model.trim="form.sxServiceExtend.villageName"
                    placeholder="请输入村居社区名称"
                    maxlength="100"
                    show-word-limit
                  />
                </el-form-item>
              </td>
              <td>
                <b>村居社区代码：</b>
              </td>
              <td>
                <el-form-item prop="sxServiceExtend.villageCode">
                  <el-input
                    v-model.trim="form.sxServiceExtend.villageCode"
                    placeholder="请输入村居社区代码"
                    maxlength="50"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <i class="require">*</i>
                <b>办理时间段：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="sxServiceExtend.hanleTimeRange">
                  <el-input
                    v-model.trim="form.sxServiceExtend.hanleTimeRange"
                    placeholder="请输入办理时间段"
                    maxlength="200"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <b>备注：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="sxServiceExtend.remark">
                  <el-input
                    type="textarea"
                    v-model.trim="form.sxServiceExtend.remark"
                    placeholder="请输入备注"
                    maxlength="500"
                    show-word-limit
                  />
                </el-form-item>
              </td>
            </tr>
            <tr>
              <td>
                <!--                <i class="require">*</i>-->
                <b>咨询用户：</b>
              </td>
              <td colspan="3">
                <el-tree
                  show-checkbox
                  node-key="id"
                  ref="userTree"
                  highlight-current
                  :props="props"
                  :data="userTreeData"
                  :default-expanded-keys="userExpandedKeys"
                  :default-checked-keys="userCheckedKeys"
                  check-strictly
                  @check="handleUserTreeCheck"
                />
              </td>
            </tr>
          </table>
        </el-tab-pane>

        <el-tab-pane label="前置核验" name="third">
          <PreVerification :serviceOid="serviceOid" ref="preVerification" />
        </el-tab-pane>
      </el-tabs>
    </el-form>
    <!-- 主题分类-->
    <div v-if="subView">
      <el-dialog
        v-dialog-drag
        :visible.sync="subView"
        width="50%"
        title="主题分类"
        :close-on-click-modal="false"
        append-to-body
      >
        <div class="zf-zc-table--title">自然人</div>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="100%" />
          </colgroup>
          <tr>
            <td>
              <el-checkbox-group v-model="subjectCheck" @change="handleCheckedSubject">
                <el-checkbox v-for="(item, index) in subjectList['ZTZRR']" :label="item" :key="index">{{ item.name }}</el-checkbox>
              </el-checkbox-group>
            </td>
          </tr>
        </table>

        <div class="zf-zc-table--title">法人</div>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="100%" />
          </colgroup>
          <tr>
            <td>
              <el-checkbox-group v-model="subjectCheck" @change="handleCheckedSubject">
                <el-checkbox v-for="(item, index) in subjectList['ZTFR']" :label="item" :key="index">{{ item.name }}</el-checkbox>
              </el-checkbox-group>
            </td>
          </tr>
        </table>

        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="saveSubject">确 定</el-button>
          <el-button @click="cancelSubject">取 消</el-button>
        </div>
      </el-dialog>
    </div>
    <!-- 生命周期分类-->
    <div v-if="lifeView">
      <el-dialog
        v-dialog-drag
        :visible.sync="lifeView"
        width="50%"
        title="生命周期分类"
        :close-on-click-modal="false"
        append-to-body
      >
        <div class="zf-zc-table--title">自然人</div>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="80%" />
          </colgroup>
          <tr v-for="item in lifeList['SMZQZRR'].childSysDict">
            <td>{{ item.name }}:</td>
            <td>
              <el-checkbox-group v-model="lifeCheck" @change="handleCheckedLife">
                <el-checkbox
                  style="width: 150px"
                  v-for="(itemChild, index) in item.childSysDict"
                  :label="itemChild"
                  :key="itemChild.oid"
                >{{ itemChild.name }}</el-checkbox>
              </el-checkbox-group>
            </td>
          </tr>
        </table>

        <div class="zf-zc-table--title">法人</div>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="80%" />
          </colgroup>
          <tr v-for="item in lifeList['SMZQFR'].childSysDict">
            <td>{{ item.name }}:</td>
            <td>
              <el-checkbox-group v-model="lifeCheck" @change="handleCheckedLife">
                <el-checkbox
                  style="width: 150px"
                  v-for="(itemChild, index) in item.childSysDict"
                  :label="itemChild"
                  :key="itemChild.oid"
                >{{ itemChild.name }}</el-checkbox>
              </el-checkbox-group>
            </td>
          </tr>
        </table>

        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="saveLifeClass">确 定</el-button>
          <el-button @click="cancelLifeClass">取 消</el-button>
        </div>
      </el-dialog>
    </div>
    <!-- 办事群体分类-->
    <div v-if="bsGroupView">
      <el-dialog
        v-dialog-drag
        :visible.sync="bsGroupView"
        width="50%"
        title="办事群体分类"
        :close-on-click-modal="false"
        append-to-body
      >
        <div class="zf-zc-table--title">自然人</div>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="100%" />
          </colgroup>
          <tr>
            <td>
              <el-checkbox-group v-model="bsGroupCheck" @change="handleCheckedGroup">
                <el-checkbox v-for="(item, index) in bsGroupList['BSQTZRR']" :label="item" :key="index">{{ item.name }}</el-checkbox>
              </el-checkbox-group>
            </td>
          </tr>
        </table>

        <div class="zf-zc-table--title">法人</div>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="100%" />
          </colgroup>
          <tr>
            <td>
              <el-checkbox-group v-model="bsGroupCheck" @change="handleCheckedGroup">
                <el-checkbox
                  v-for="(item, index) in bsGroupList['BSQTFR']"
                  :label="item"
                  :key="item.oid"
                >{{ item.name }}</el-checkbox>
              </el-checkbox-group>
            </td>
          </tr>
        </table>

        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="saveGroup">确 定</el-button>
          <el-button @click="cancelBsGroupClass">取 消</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
  import {
    validatePhone,
    validatePostCode,
    validateEmails,
    validUnifiedCredit
  } from "@/utils/validate";
  import {
    getDistInfo,
    getSxServiceByOid,
    uploadsxFile,
    saveOrUpdate,
    downLoadFile,
    saveOrUpdateList,
    queryOrganSelectOptions
  } from "@/api/zc/qdgl/sxService";
  import { queryServiceTypeSimpleTree, queryUserTree } from "@/api/sxpt/serviceType";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import PreVerification from './preVerification.vue';
  export default {
    components: { Treeselect, PreVerification },
    name: "InitSxService",
    props: ["serviceOid"],
    data () {
      //中介名称验证
      const validateZjfw = (rule, value, callback) => {
        if (this.form.isZjfw == 1) {
          if (value != undefined && value != null) {
            callback();
          } else {
            callback(new Error("请填写中介服务名称！"));
          }
        } else {
          callback();
        }
      };
      return {
        activeName: "first",
        form: {
          serviceOid: this.serviceOid,
          onlinePayFlag: 1,
          adminJurisdiction: "0",
          unionOrganFlag: 0,
          openChannel: 1,
          openWay: 1,
          countToScence: "0",
          caseType: 2,
          handleForm: "2",
          expressFlag: 1,
          isZjfw: 0,
          isSpecial: 0,
          appIssingleLogin: 0,
          isSingleLogin: "0",
          terminalProcessing: 0,
          appointmentFlag: 1,
          chargeFlag: 0,
          sjStatus:1,
          elecCertProduceFlag:0,
          isOnlineHandle:0,
          serviceCharacter: "",
          tsCkText: "",
          tsWlText: "",
          tsDhText: "",
          tsXjText: "",
          tsYjText: "",
          zxCkText: "",
          zxWlText: "",
          zxDhText: "",
          zxXjText: "",
          zxYjText: "",
          levelName: "",
          levelDicts: "",
          implementOrganProperty: "",
          handleDepth: [],
          handleType: "",
          serviceObject: [],
          serviceGroupClassificationName: "",
          lifeCycleClassificationName: "",
          subjectClassificationName: "",
          resultDeliveryWay: "",
          deliverTerm:"",
          sxServiceExtend: {
            resultName: "",
            resultSampleType: 0,
            legalLimitType: "W",
            promiseLimitType: "W",
            isStageHandle: 1,
            isEntryCenter: 1,
            runSystem: "0",
            expipyFlag: 0,
            numberLimitType: 0,
            isPermisionDivid: 0,
            directoryType: 0,
            resultSampleAddr: "",
            handleFlow: "",
            consultUserOid: ''
          },
          isShield: "0",
        },
        rules: {
          serviceName: [
            {
              required: true,
              message: "必填项",
              trigger: "blur"
            }
          ],
          implementCode: [
            {
              required: true,
              message: "必填项",
              trigger: "blur"
            }
          ],
          serviceTypeOid: [
            {
              required: true,
              message: "必填项",
              trigger: "blur"
            }
          ],
          // levelDicts: [
          //   {
          //     required: true,
          //     message: "必填项",
          //     trigger: "blur"
          //   }
          // ],
          // handleType: [
          //   {
          //     required: true,
          //     message: "必填项",
          //     trigger: "blur"
          //   }
          // ],
          // serviceObject: [
          //   {
          //     required: true,
          //     message: "必填项",
          //     trigger: "blur"
          //   }
          // ],
          // origin: [
          //   {
          //     required: true,
          //     message: "必填项",
          //     trigger: "blur"
          //   }
          // ],
          implementOrganProperty: [
            {
              required: true,
              message: "必填项",
              trigger: "blur"
            }
          ],
          // subjectClassificationName: [
          //   {
          //     required: true,
          //     message: "必填项",
          //     trigger: "blur"
          //   }
          // ],
          // lifeCycleClassificationName: [
          //   {
          //     required: true,
          //     message: "必填项",
          //     trigger: "blur"
          //   }
          // ],
          // serviceGroupClassificationName: [
          //   {
          //     required: true,
          //     message: "必填项",
          //     trigger: "blur"
          //   }
          // ],
          //
          // handleDepth: [
          //   {
          //     required: true,
          //     message: "必填项",
          //     trigger: "blur"
          //   }
          // ],
          // handleForm: [
          //   {
          //     required: true,
          //     message: "必填项",
          //     trigger: "blur"
          //   }
          // ],
          // resultDeliveryWay: [
          //   {
          //     required: true,
          //     message: "必填项",
          //     trigger: "blur"
          //   }
          // ],
          // mediationName: [
          //   {
          //     required: true,
          //     message: "必填项",
          //     trigger: "blur",
          //     validator: validateZjfw
          //   }
          // ],
          // appointmentFlag: [
          //   {
          //     required: true,
          //     message: "必填项",
          //     trigger: "blur"
          //   }
          // ],
          // openWay: [
          //   {
          //     required: true,
          //     message: "必填项",
          //     trigger: "blur"
          //   }
          // ],
          // openChannel: [
          //   {
          //     required: true,
          //     message: "必填项",
          //     trigger: "blur"
          //   }
          // ],
          onlineApplyLink: [
            {
              required: true,
              message: "必填项",
              trigger: "blur"
            }
          ],
          // chargeFlag: [
          //   {
          //     required: true,
          //     message: "必填项",
          //     trigger: "blur"
          //   }
          // ],
          // sjStatus: [
          //   {
          //     required: true,
          //     message: "必填项",
          //     trigger: "blur"
          //   }
          // ],

          "sxServiceExtend.legalLimit": [
            {
              required: true,
              message: "必填项",
              trigger: "blur"
            }
          ],
          "sxServiceExtend.promiseLimit": [
            {
              required: true,
              message: "必填项",
              trigger: "blur"
            }
          ],
          "sxServiceExtend.legalLimitType": [
            {
              required: true,
              message: "必填项",
              trigger: "blur"
            }
          ],
          "sxServiceExtend.promiseLimitType": [
            {
              required: true,
              message: "必填项",
              trigger: "blur"
            }
          ],
          // "sxServiceExtend.runSystem": [
          //   {
          //     required: true,
          //     message: "必填项",
          //     trigger: "blur"
          //   }
          // ],
          // "sxServiceExtend.directoryType": [
          //   {
          //     required: true,
          //     message: "必填项",
          //     trigger: "blur"
          //   }
          // ],
          // "sxServiceExtend.expipyFlag": [
          //   {
          //     required: true,
          //     message: "必填项",
          //     trigger: "blur"
          //   }
          // ],
          // "sxServiceExtend.isPermisionDivid": [
          //   {
          //     required: true,
          //     message: "必填项",
          //     trigger: "blur"
          //   }
          // ],
          // "sxServiceExtend.numberLimitType": [
          //   {
          //     required: true,
          //     message: "必填项",
          //     trigger: "blur"
          //   }
          // ],
          // "sxServiceExtend.isStageHandle": [
          //   {
          //     required: true,
          //     message: "必填项",
          //     trigger: "blur"
          //   }
          // ],
          // "sxServiceExtend.isEntryCenter": [
          //   {
          //     required: true,
          //     message: "必填项",
          //     trigger: "blur"
          //   }
          // ],
          "sxServiceExtend.hanleTimeRange": [
              {
                required: true,
                message: "必填项",
                trigger: "blur"
              }
            ]

        },
        // 事项类型
        serviceTypeOptions: [],
        levelList: [], //行使层级
        serviceObject: [],
        handleType: [],
        naturalCharacteristicObject: [],
        legalCharacteristicObject: [],
        handleScope: [],
        tsType: [],
        zxType: [],
        resultDeliveryWay: [],
        bookChannel: [],
        //办理类型
        handleTypes:[
          { key: "1", value: "好办快办" },
          { key: "2", value: "秒批秒办" },
          { key: "3", value: "标准" }
        ],
        //服务对象
        serviceObjectOptions: [
          { key: "1", value: "自然人" },
          { key: "2", value: "企业法人" },
          { key: "3", value: "事业法人" },
          { key: "4", value: "社会组织法人" },
          { key: "5", value: "非法人企业" },
          { key: "6", value: "行政机关" },
          { key: "9", value: "其他组织" }
        ],
        originOptions: [], //权力来源
        implementOptions: [], //实施主体
        isShieldOptions: [
          { key: "0", value: "未屏蔽" },
          { key: "1", value: "屏蔽" }
        ],
        caseTypeOptions: [
          { key: 1, value: "即办件" },
          { key: 2, value: "承诺件" }
        ],
        handleFormOptions: [
          { key: "0", value: "窗口办理" },
          { key: "1", value: "网上办理" },
          { key: "2", value: "快递申请" }
        ],
        //面向自然人
        naturalCharacteristicOptions: [
          { key: "1", value: "社保卡" },
          { key: "2", value: "综合查询" },
          { key: "3", value: "医疗保险" }
        ],
        //面向法人
        legalCharacteristicOptions: [
          { key: "1", value: "设立登记" },
          { key: "2", value: "名校校准" },
          { key: "3", value: "税务登记" }
        ],
        handleDepthOptions: [], //办理深度
        tbfwOptions: [], //通办范围
        fileList: [], //结果样本
        fileFlowList: [], //办理流程图
        fileSfyjList: [], //收费依据
        subView: false, //主题分类
        subjectList: [], //主题分类信息
        subjectCheck: [], //选择的主题分类
        subjectCheckOids: [], //修改时存放所有选项
        subNameSelect: "", //选择的分类名称
        lifeView: false, //生命周期分类
        lifeList: [{ oid: "", name: "" }], //生命周期分类信息
        lifeCheck: [], //选择的生命周期
        lifeCheckOids: [],
        bsGroupView: false, //办事群体
        bsGroupList: [], //办事群体分类信息
        bsGroupCheck: [], //办事群体生命周期
        bsGroupCheckOids: [],
        chargeLinkList: [], //收费环节
        tsCk: true,
        tsWl: true,
        tsDh: true,
        tsXj: true,
        tsYj: true,
        zxCk: true,
        zxWl: true,
        zxDh: true,
        zxXj: true,
        zxYj: true,
        handleForm: [],
        handleDepth: [],
        userExpandedKeys: [],
        userCheckedKeys: [],
        props: {
          label: "label",//这里是树结构中需显示的数据（即接口返回的需展示在页面上的参数）
          children: 'children',
        },
        userTreeData: [],
        userTreeDataFlat: [],
        totalExtend:0,
         //机构列表
        getOrganOptions:[],
      };
    },
    async created () {
      if (this.serviceOid) {
        await this.getServiceInfo();
      }

      this.getServiceTypeTree();
      //获取所有的需要的字典数据
      this.getAllDistInfo();
      this.queryUserTree();
      this.getOrgan();
    },
    methods: {
      /** 获取机构分组列表 */
     getOrgan () {
      queryOrganSelectOptions().then(response => {
        this.getOrganOptions = response.data;
      });
    },
      // 表单重置
      reset () {
        this.form = {
          name: null
        };
        this.resetForm("form");
      },
      //获取事项信息
      getServiceInfo () {
        return getSxServiceByOid(this.serviceOid).then(response => {
          this.form = response.data;
          if (this.form.serviceObject) {
            this.serviceObject = this.form.serviceObject.split(",");
          }
          if (this.form.tsType) {
            this.tsType = this.form.tsType.split(",");
          }
          if (this.form.zxType) {
            this.zxType = this.form.zxType.split(",");
          }
          this.changeTsText();
          this.changeZxText();
          if (this.form.handleDepth) {
            //解决交换数据不一致问题
            var result = [];
            var handleDepthArray = this.form.handleDepth.split(",");
            for (var i = 0; i < handleDepthArray.length; i++) {
              var hdOid = handleDepthArray[i];
              if (hdOid === "1") {
                result.push("402881ef5ba7c065015ba82672b90001");
              } else if (hdOid === "2") {
                result.push("402881ef5ba7c065015ba82708050002");
              } else if (hdOid === "3") {
                result.push("402881ef5ba7c065015ba82771f10003");
              } else if (hdOid === "4") {
                result.push("402881ef5ba7c065015ba82810e20004");
              } else if (hdOid === "5") {
                result.push("2c287bb66ae75fe7016ae79fd7850012");
              } else if (hdOid === "6") {
                result.push("2c287bb66ae75fe7016ae7a0fca40013");
              } else if (hdOid === "7") {
                result.push("2c287b8e6d19dfeb016d1a9bf54a0000");
              } else if (hdOid === "9") {
                result.push("2c287bb66ae75fe7016ae7a214080014");
              } else {
                result.push(hdOid);
              }
            }
            this.handleDepth = result;
          }
          if (this.form.handleForm) {
            this.handleForm = this.form.handleForm.split(",");
          }
          if (this.form.resultDeliveryWay) {
            this.resultDeliveryWay = this.form.resultDeliveryWay.split(",");
          }
          if (this.form.sxServiceExtend.handleScope) {
            this.handleScope = this.form.sxServiceExtend.handleScope.split(",");
          }
          if (this.form.sxServiceExtend.bookChannel) {
            this.bookChannel = this.form.sxServiceExtend.bookChannel.split(",");
          }

          if (this.form.sxServiceExtend.legalCharacteristic) {
            this.legalCharacteristicObject = this.form.sxServiceExtend.legalCharacteristic.split(
              ","
            );
          }
          if (this.form.sxServiceExtend.naturalCharacteristic) {
            this.naturalCharacteristicObject = this.form.sxServiceExtend.naturalCharacteristic.split(
              ","
            );
          }
          if (this.form.subjectClassification) {
            this.subjectCheckOids = this.form.subjectClassification.split(",");
          }
          if(this.form.serviceGroupClassification){
            this.bsGroupCheckOids = this.form.serviceGroupClassification.split(",");
          }
          if(this.form.lifeCycleClassification){
            this.lifeCheckOids = this.form.lifeCycleClassification.split(",");
          }
        });
      },
      /** 获取事项类型树 */
      getServiceTypeTree (serviceType) {
        let _that = this;
        queryServiceTypeSimpleTree(serviceType).then(response => {
          _that.serviceTypeOptions = response.data;
        });
      },
      //获取字典配置
      getAllDistInfo () {
        let _that = this;
        getDistInfo().then(respon => {
          if (respon.data) {
            _that.originOptions = respon.data.qlly;
            _that.implementOptions = respon.data.ssztxz;
            _that.handleDepthOptions = respon.data.blsd;
            _that.tbfwOptions = respon.data.tbfw;
            _that.subjectList = respon.data.ztfl;
            _that.lifeList = respon.data.smzq;
            _that.bsGroupList = respon.data.bsqt;
            _that.levelList = respon.data.XSCJ;
            let sfhj = respon.data.XZSP;
            if (sfhj) {
              sfhj.forEach(sfItem => {
                let treeinfo = {};
                treeinfo.id = sfItem.oid;
                treeinfo.label = sfItem.name;
                _that.chargeLinkList.push(treeinfo);
              });
            }
          }
        });
      },

      /** 上传附件请求操作 */
      beforeUpload (file) {
        let _that = this;
        let isRightSize = file.size / 1024 / 1024 < 100;
        if (!isRightSize) {
          _that.$message.error("文件大小超过 100MB");
        }
        return isRightSize;
      },
      /** 失败后返回 */
      uploadError (resp) {
        let _that = this;
        _that.msgError("文件上传失败");
      },
      //文件改变时
      handleChange (file, fileList) {
        this.fileList = fileList.slice(-1);
      },
      /** 上传附件 */
      uploadFiles (file) {
        let _that = this;
        let formData = new FormData();
        formData.append("file", file.file);
        uploadsxFile(formData).then(respon => {
          if (respon.data) {
            _that.form.sxServiceExtend.resultSampleAddr = respon.data.attaUrl;
            if (null == _that.form.sxServiceExtend.resultSampleAddrFile) {
              _that.form.sxServiceExtend.resultSampleAddrFile = {};
            }
            _that.form.sxServiceExtend.resultSampleAddrFile.originName =
              respon.data.name;
          } else {
            _that.$message.error("上传文件失败！");
          }
        });
      },
      delresultFile (val) {
        this.form.sxServiceExtend.resultSampleAddr = "";
        this.form.sxServiceExtend.resultSampleAddrFile.originName = "";
      },
      removeHandleFlow () {
        this.fileList = [];
        this.form.handleFlow = "";
      },
      //流程图文件改变时
      handleChangeFlow (file, fileFlowList) {
        this.fileFlowList = fileFlowList.slice(-1);
      },
      /** 上传附件 */
      uploadFilesFlow (file) {
        let _that = this;
        let formData = new FormData();
        formData.append("file", file.file);
        uploadsxFile(formData).then(respon => {
          if (respon.data) {
            _that.form.sxServiceExtend.handleFlow = respon.data.attaUrl;
            if (null == _that.form.sxServiceExtend.handleFlowFile) {
              _that.form.sxServiceExtend.handleFlowFile = {};
            }
            _that.form.sxServiceExtend.handleFlowFile.originName =
              respon.data.name;
          } else {
            _that.$message.error("上传文件失败！");
          }
        });
      },
      delFlowFile (val) {
        this.form.sxServiceExtend.handleFlow = "";
        this.form.sxServiceExtend.handleFlowFile.originName = "";
      },

      //收费依据文件改变时
      handleChangeSfyj (file, fileSfyjList) {
        this.fileSfyjList = fileSfyjList.slice(-1);
      },
      /** 上传附件 */
      uploadFilesSfyj (file) {
        let _that = this;
        let formData = new FormData();
        formData.append("file", file.file);
        uploadsxFile(formData).then(respon => {
          if (respon.data) {
            _that.form.sxServiceExtend.chargeEvidence = respon.data.oid;
            if (null == _that.form.sxServiceExtend.chargeEvidenceFile) {
              _that.form.sxServiceExtend.chargeEvidenceFile = {};
            }
            _that.form.sxServiceExtend.chargeEvidenceFile.originName = respon.data.name;
          } else {
            _that.$message.error("上传文件失败！");
          }
        });
      },
      delSfyjFile (val) {
        this.form.sxServiceExtend.chargeEvidence = "";
        this.form.sxServiceExtend.chargeEvidenceFile.originName = "";
      },
      downLoadFileInfo (attaOid) {
        downLoadFile(attaOid);
      },
      downLoadFileUrl(url){
        window.open(url);
      },
      getSubjectClass () {
        let _that = this;
        this.subView = true;
        //展示已经选择的
        if (_that.subjectCheckOids && _that.subjectCheckOids.length > 0) {
          let azrr = this.subjectList["ZTZRR"].filter(function (item) {
            return _that.subjectCheckOids.indexOf(item.oid) >= 0;
          });
          let afr = this.subjectList["ZTFR"].filter(function (item) {
            return _that.subjectCheckOids.indexOf(item.oid) >= 0;
          });
          _that.subjectCheck = azrr.concat(afr);
        }
      },
      cancelSubject () {
        this.subView = false;
      },
      getlifeClass () {
        this.lifeView = true;
        let _that = this;
        //展示已经选择的
        if (_that.lifeCheckOids && _that.lifeCheckOids.length > 0) {
          let zrrArry = [];
          let zrrchild = this.lifeList["SMZQZRR"].childSysDict;
          zrrchild.map(ite => {
            //循环
            let itechild = ite.childSysDict;
            let childarr = itechild.filter(function (child) {
              return _that.lifeCheckOids.indexOf(child.oid) >= 0;
            });
            zrrArry = zrrArry.concat(childarr);
          });
          //法人循环
          let frchild = this.lifeList["SMZQFR"].childSysDict;
          frchild.map(ite => {
            //循环
            let itechild = ite.childSysDict;
            let childarr = itechild.filter(function (child) {
              return _that.lifeCheckOids.indexOf(child.oid) >= 0;
            });
            zrrArry = zrrArry.concat(childarr);
          });
          _that.lifeCheck = zrrArry;
        }
      },
      cancelLifeClass () {
        this.lifeView = false;
      },
      getBsGroupClass () {
        this.bsGroupView = true;
        let _that = this;
        //展示已经选择的
        if (_that.bsGroupCheckOids && _that.bsGroupCheckOids.length > 0) {
          let azrr = this.bsGroupList["BSQTZRR"].filter(function (item) {
            return _that.bsGroupCheckOids.indexOf(item.oid) >= 0;
          });
          let afr = this.bsGroupList["BSQTFR"].filter(function (item) {
            return _that.bsGroupCheckOids.indexOf(item.oid) >= 0;
          });
          _that.bsGroupCheck = azrr.concat(afr);
        }
      },
      cancelBsGroupClass () {
        this.bsGroupView = false;
      },

      //获取所有选择的名称
      handleCheckedSubject (val) {
        this.subNameSelect = val.map(ite => ite.name).join(";");
      },
      saveSubject () {
        this.form.subjectClassificationName = this.subNameSelect;
        this.form.subjectClassification = this.subjectCheck
          .map(ite => ite.oid)
          .join(",");
        this.subjectCheckOids = this.form.subjectClassification;
        this.subView = false;
      },
      handleCheckedLife (val) { },
      saveLifeClass () {
        this.form.lifeCycleClassificationName = this.lifeCheck
          .map(ite => ite.name)
          .join(";");
        this.form.lifeCycleClassification = this.lifeCheck
          .map(ite => ite.oid)
          .join(",");
        this.lifeCheckOids = this.form.lifeCycleClassification;
        this.lifeView = false;
      },
      handleCheckedGroup (val) { },
      saveGroup () {
        this.form.serviceGroupClassificationName = this.bsGroupCheck
          .map(ite => ite.name)
          .join(";");
        this.form.serviceGroupClassification = this.bsGroupCheck
          .map(ite => ite.oid)
          .join(",");
        this.bsGroupCheckOids = this.form.serviceGroupClassification;
        this.bsGroupView = false;
      },
      //动态改变投诉方式
      changeTsText () {
        if (this.tsType) {
          if (this.tsType.indexOf("0") >= 0) {
            this.tsCk = false;
          } else {
            this.tsCk = true;
            this.form.tsCkText = "";
          }
          if (this.tsType.indexOf("1") >= 0) {
            this.tsWl = false;
          } else {
            this.tsWl = true;
            this.form.tsWlText = "";
          }
          if (this.tsType.indexOf("2") >= 0) {
            this.tsYj = false;
          } else {
            this.tsYj = true;
            this.form.tsYjText = "";
          }
          if (this.tsType.indexOf("3") >= 0) {
            this.tsXj = false;
          } else {
            this.tsXj = true;
            this.form.tsXjText = "";
          }
          if (this.tsType.indexOf("4") >= 0) {
            this.tsDh = false;
          } else {
            this.tsDh = true;
            this.form.tsDhText = "";
          }
        }
      },
      //动态改变咨询方式
      changeZxText () {
        if (this.zxType) {
          if (this.zxType.indexOf("0") >= 0) {
            this.zxCk = false;
          } else {
            this.zxCk = true;
            this.form.zxCkText = "";
          }
          if (this.zxType.indexOf("1") >= 0) {
            this.zxWl = false;
          } else {
            this.zxWl = true;
            this.form.zxWlText = "";
          }
          if (this.zxType.indexOf("2") >= 0) {
            this.zxYj = false;
          } else {
            this.zxYj = true;
            this.form.zxYjText = "";
          }
          if (this.zxType.indexOf("3") >= 0) {
            this.zxXj = false;
          } else {
            this.zxXj = true;
            this.form.zxXjText = "";
          }
          if (this.zxType.indexOf("4") >= 0) {
            this.zxDh = false;
          } else {
            this.zxDh = true;
            this.form.zxDhText = "";
          }
        }
      },

      /** 提交按钮 */
      submitForm () {
        this.form.handleForm = this.handleForm.join(",");
        this.form.handleDepth = this.handleDepth.join(",");
        this.form.resultDeliveryWay = this.resultDeliveryWay.join(",");
        this.form.sxServiceExtend.handleScope = this.handleScope.join(",");
        this.form.serviceObject = this.serviceObject.join(",");
        this.form.sxServiceExtend.legalCharacteristic = this.legalCharacteristicObject.join(
          ","
        );
        this.form.sxServiceExtend.naturalCharacteristic = this.naturalCharacteristicObject.join(
          ","
        );
        let usersOid = this.$refs.userTree.getCheckedNodes().find(i => i.identity === 'USER')?.id || '';
        this.form.sxServiceExtend.consultUserOid = usersOid;
        this.$refs["form"].validate((valid,obj) => {
          if (valid) {
            // if (!this.form.sxServiceExtend.consultUserOid) {
            //   this.$message.error("请选择咨询用户信息!");
            //   return false;
            // }
            // if (this.tsType && this.tsType != '') {
            //   if (this.tsType.indexOf("0") >= 0) {
            //     if (!this.form.tsCkText) {
            //       this.$message.error("请填写窗口投诉内容!");
            //       return false;
            //     }
            //   }
            //   if (this.tsType.indexOf("1") >= 0) {
            //     if (!this.form.tsWlText) {
            //       this.$message.error("请填写网络投诉内容!");
            //       return false;
            //     }
            //   }
            //   if (this.tsType.indexOf("2") >= 0) {
            //     if (!this.form.tsYjText) {
            //       this.$message.error("请填写邮件投诉内容!");
            //       return false;
            //     }
            //   }
            //   if (this.tsType.indexOf("3") >= 0) {
            //     if (!this.form.tsXjText) {
            //       this.$message.error("请填写信件投诉内容!");
            //       return false;
            //     }
            //   }
            //   if (this.tsType.indexOf("4") >= 0) {
            //     if (!this.form.tsDhText) {
            //       this.$message.error("请填写电话投诉内容!");
            //       return false;
            //     }
            //   }
            // } else {
            //   this.$message.error("请选择投诉方式!");
            //   return false;
            // }
            if (this.zxType && this.zxType != '') {
              if (this.zxType.indexOf("0") >= 0) {
                if (!this.form.zxCkText) {
                  this.$message.error("请填写窗口咨询内容!");
                  return false;
                }
              }
              if (this.zxType.indexOf("1") >= 0) {
                if (!this.form.zxWlText) {
                  this.$message.error("请填写网络咨询内容!");
                  return false;
                }
              }
              if (this.zxType.indexOf("2") >= 0) {
                if (!this.form.zxYjText) {
                  this.$message.error("请填写邮件咨询内容!");
                  return false;
                }
              }
              if (this.zxType.indexOf("3") >= 0) {
                if (!this.form.zxXjText) {
                  this.$message.error("请填写信件咨询内容!");
                  return false;
                }
              }
              if (this.zxType.indexOf("4") >= 0) {
                if (!this.form.zxDhText) {
                  this.$message.error("请填写电话咨询内容!");
                  return false;
                }
              }
            } else {
              this.$message.error("请选择咨询方式!");
              return false;
            }
            // if (!this.form.sxServiceExtend.handleFlow) {
            //   this.$message.error("请上传办理流程图!");
            //   return false;
            // }
            //处理事项性质
            this.serviceTypeOptions.forEach(ite => {
              if (ite.id == this.form.serviceTypeOid) {
                this.form.serviceCharacter = ite.identity;
              }
            });
            //处理行使层级
            this.levelList.forEach(ite => {
              if (ite.oid == this.form.levelDicts) {
                this.form.levelName = ite.name;
              }
            });
            //转换所有的数组
            this.form.tsType = this.tsType.join(",");
            this.form.zxType = this.zxType.join(",");
            this.form.sxServiceExtend.bookChannel = this.bookChannel.join(",");
            if (this.form.sxServiceExtend.expipyFlag == 1) {
              this.form.sxServiceExtend.expipyDate = "长期有效";
            }
            saveOrUpdate(this.form).then(response => {
              let res = response;
              if (res.code == 200) {
                let serviceOid= res.data.serviceOid;
                this.$message.success("保存成功！");
                // 判断前置核验是否存在空数据
                const message = this.$refs.preVerification.validate();
                if (message) {
                  return this.$message.error(message);
                }
                if(this.$refs.preVerification.tableData != null) {
                  this.$refs.preVerification.tableData.forEach(item=>{
                    item.serviceOid = serviceOid;
                  })
                  saveOrUpdateList(this.$refs.preVerification.tableData);
                }
                //刷新列表
                this.$emit("initServiceClose");
              } else {
                this.$message.error("保存失败！");
              }
            });
          } /*else {
          this.$message.error("存在必填项未填！")
        }*/
          this.totalExtend=0;
          Object.keys(obj).forEach(item=>{
            if(item.indexOf('.')!=-1){
              this.totalExtend++;
            }
          })
          if(this.activeName=='first'&&this.totalExtend>0&&this.totalExtend==Object.keys(obj).length){
            this.$message.error('扩展信息中有必填项未填写！');
          }else if(this.activeName=='second'&&this.totalExtend==0&&this.totalExtend!=Object.keys(obj).length){
            this.$message.error('基本信息中有必填项未填写！');
          }
        });
      },
      cancel () {
        this.$emit("initServiceClose");
      },
      queryUserTree () {
        this.$getResponse(queryUserTree(), (error, res) => {
          if (error || res.code !== 200) return;
          this.userTreeData = res.data || [];
          // 扁平化数组
          this.userTreeDataFlat = this.flatTree(this.userTreeData);
          // 默认展开第一级
          this.userExpandedKeys.push(this.userTreeData?.[0]?.id);
          // 回显树形结构
          this.handleTreeExtend();
        })
      },

      flatTree (data, arr = []) {
        data.forEach(item => {
          arr.push({ ...item, children: [] });
          if (item.children) {
            this.flatTree(item.children, arr);
          }
        })
        return arr;
      },

      findId (parentId, arr = []) {
        let target = this.userTreeDataFlat.find(p => p.id === parentId);
        if (target.parentId) {
          arr.push(target.parentId);
          this.findId(target.parentId, arr);
        } else {
          arr.push(target.id);
        }
        return arr;
      },

      handleUserTreeCheck (data) {
        let arr = [data.id];
        if (data.parentId) {
          arr = this.findId(data.parentId, [...arr, data.parentId]);
        }
        // 找到父级id
        this.$refs.userTree.setCheckedKeys(arr);
      },

      handleTreeExtend () {
        let consultUserOid = this.form.sxServiceExtend.consultUserOid;
        if (consultUserOid) {
          let target = this.userTreeDataFlat.find(item => item.id === consultUserOid);
          let arr = [consultUserOid];
          if (target.parentId) {
            arr = this.findId(target.parentId, [...arr, target.parentId]);
          }
          this.userExpandedKeys = arr;
          this.userCheckedKeys = arr;
        }
      }
    }
  };
</script>
<style scoped lang="scss">
  .el-tree > .el-tree-node {
    min-width: 100%;
    display: inline-block;
  }
  .dialog-table table tr td {
    padding: 5px 8px;
  }

  >>> .el-input-number--medium {
    line-height: 30px;
  }

  >>> .el-checkbox-group {
    display: flex;
    flex-wrap: wrap;
    padding-left: 10px;
    .el-checkbox {
      width: 200px;
      margin: unset;
      text-align: left;
    }
  }

  .handle-depth {
    padding: unset !important;

    &--item {
      border-bottom: 1px solid #e0e6f0;
      padding-left: 10px;
      display: flex;
      align-items: center;

      >>> .el-radio {
        width: 80px;
      }

      &:last-child {
        border: unset;
      }
    }
  }

  .ts-type {
    >>> .el-checkbox {
      width: 180px;
    }
  }
</style>
