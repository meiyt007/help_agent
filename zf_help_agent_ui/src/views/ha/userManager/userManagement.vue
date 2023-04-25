/** * @Author: wangns */
<!-- 请求条件 -->
<template>
  <div class="app-container">
    <!--用户数据-->
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="80px"
    >
      <el-form-item label="姓名" prop="name">
        <el-input
          v-model.trim="queryParams.name"
          placeholder="姓名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input
          v-model.trim="queryParams.phone"
          placeholder="手机号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属区划">
        <treeselect
          v-model="queryParams.districtOidSelect"
          :options="districtOptions"
          style="width: 240px"
          noOptionsText="暂无数据"
          :defaultExpandLevel="1"
          placeholder="请输入所属区划"
        />
      </el-form-item>
      <el-form-item label="人员类型" prop="userType">
        <el-col :span="24">
          <el-select
            v-model.trim="queryParams.userType"
            placeholder="请选择人员类型"
            size="small">
            <el-option label="导服人员" value="1"></el-option>
            <el-option label="帮代办人员" value="2"></el-option>
            <el-option label="委办局人员" value="3"></el-option>
          </el-select>
        </el-col>
      </el-form-item>
      <el-form-item class="fr mr0">
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
        >搜索
        </el-button
        >
        <el-button
          type="warning"
          icon="el-icon-refresh"
          size="mini"
          @click="resetQuery"
          class="btn-reset"
        >重置
        </el-button
        >
      </el-form-item>
    </el-form>
    <!-- 工具条 -->
    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleInit"
          v-hasPermi="['im:user:save']"
        >新增
        </el-button
        >
      </el-col>
      <el-button type="danger" size="mini" @click="removeRows()">批量删除</el-button>
    </el-row>
    <!-- 表头 -->
    <el-table
      v-loading="loading"
      :data="districtList"
      stripe
      style="width: 100%" @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"/>
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="所属区划"
        align="center"
        prop="districtOid"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="姓名"
        align="center"
        prop="name"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="账号"
        align="center"
        prop="account"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="手机号"
        align="center"
        prop="phone"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="用户类型"
        align="center"
        prop="userType"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="帮代办类型"
        align="center"
        prop="haType"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="在线状态"
        align="center"
        prop="status"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createDate"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="操作"
        align="center"
        width="340"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)"
            v-hasPermi="['im:user:update']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="removeUserById(scope.row.id)"
            v-hasPermi="['im:user:delete']">删除
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            class="btn-reset"
            @click="resetPassword(scope.row)"
            v-hasPermi="['im:user:reset']"
          >重置密码
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-search zfsoft-xiugai"
            class="btn-reset"
            @click="onlineSearch(scope.row)"
            v-hasPermi="['im:user:query']"
          >在线时长记录
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      style="padding: 30px 0; text-align: center;"
      layout="total, sizes, prev, pager, next, jumper"
      @pagination="getList"
    />

    <!-- 信息详细 -->
    <!-- 添加或修改应用信息对话框 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openInit"
      width="1100px"
      height="700px"
      append-to-body
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="0px"
        class="demo-ruleForm"
      >
        <el-input v-show="false"/>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="20%"/>
            <col width="30%"/>
            <col width="20%"/>
            <col width="30%"/>
          </colgroup>
          <tr>
            <td><i class="require">*</i><b>姓名：</b></td>
            <td>
              <el-form-item prop="name">
                <el-input
                  v-model.trim="form.name"
                  placeholder="请输入姓名"
                  maxlength="100"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td rowspan="4"><i class="require"></i><b>头像：</b></td>
            <td rowspan="4">
              <el-form-item prop="image">
                <el-upload
                  class="image-upload-pic"
                  :action="uploadImagerUrl"
                  :show-file-list="false"
                  accept=".jpg,.png,.jpeg,.bmp"
                  :on-success="handleAvatarSuccess"
                  :before-upload="beforeAvatarUpload">
                  <img v-if="imageUrl" :src="imageUrl" class="avatar">
                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require"></i><b>所属区划：</b></td>
            <td>
              <el-form-item prop="districtOidSelect">
                <treeselect
                  :defaultExpandLevel="1"
                  noOptionsText="暂无数据"
                  noResultsText="暂无数据"
                  :show-count="true"
                  v-model.trim="form.districtOidSelect"
                  :options="districtOptions"
                  placeholder="请输入所属区划"
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>手机号码：</b></td>
            <td>
              <el-form-item prop="phone">
                <el-input
                  v-model.trim="form.phone"
                  placeholder="请输入手机号码："
                  maxlength="11"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require"></i><b>电子邮箱：</b></td>
            <td>
              <el-form-item prop="email">
                <el-input
                  v-model.trim="form.email"
                  placeholder="请输入电子邮箱"
                  maxlength="50"
                  show-word-limit
                  type=""
                />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>人员类型：</b></td>
            <td>
              <el-form-item prop="userType">
                <el-col :span="24">
                  <el-select
                    v-model.trim="form.userType"
                    placeholder="请选择人员类型"
                    size="small">
                    <el-option label="导服人员" value="1"></el-option>
                    <el-option label="帮代办人员" value="2"></el-option>
                    <el-option label="委办局人员" value="3"></el-option>
                  </el-select>
                </el-col>
              </el-form-item>
            </td>
            <td><i class="require"></i><b>帮代办类型：</b></td>
            <td v-if="form.userType!=='2'">
              <el-form-item prop="haType">
                <el-col :span="24">
                  <el-select
                    disabled
                    v-model.trim="form.haType"
                    placeholder="请选择帮代办类型"
                    size="small">
                    <el-option label="快捷帮办" value="1"></el-option>
                    <el-option label="圆桌帮办" value="2"></el-option>
                  </el-select>
                </el-col>
              </el-form-item>
            </td>
            <td v-if="form.userType==='2'">
              <el-form-item prop="haType" :required="userTypeFla">
                <el-col :span="24">
                  <el-select
                    v-model.trim="form.haType"
                    placeholder="请选择帮代办类型"
                    size="small">
                    <el-option label="快捷帮办" value="1"></el-option>
                    <el-option label="圆桌帮办" value="2"></el-option>
                  </el-select>
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr v-if="title=== '新增'">
            <td><i class="require">*</i><b>账号：</b></td>
            <td>
              <el-form-item prop="account">
                <el-input
                  v-model.trim="form.account"
                  placeholder="请输入账号"
                  maxlength="50"
                  show-word-limit
                  type=""
                />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>登录密码：</b></td>
            <td>
              <el-form-item prop="password">
                <el-input placeholder="请输入登录密码" v-model.trim="form.password" show-password></el-input>
              </el-form-item>
            </td>
          </tr>
          <tr v-if="form.userType!=='1'">
            <td><i class="require">*</i><b>工号：</b></td>
            <td>
              <el-form-item prop="workNumber">
                <el-input
                  v-model.trim="form.workNumber"
                  placeholder="请输入工号："
                  maxlength="255"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require"></i><b>云客服工号：</b></td>
            <td>
              <el-form-item prop="cloudServiceNumber">
                <el-input
                  v-model.trim="form.cloudServiceNumber"
                  placeholder="请输入工号："
                  maxlength="255"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr v-if="form.userType!=='1'">
            <td><i class="require">*</i><b>最大服务人数：</b></td>
            <td>
              <el-form-item prop="maxServiceNum">
                <el-input
                  v-model.trim="form.maxServiceNum"
                  placeholder="请输入最大服务人数："
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>每个人平均服务时长：</b></td>
            <td>
              <el-form-item prop="avgServiceTime">
                <el-input
                  v-model.trim="form.avgServiceTime"
                  placeholder="请输入每个人平均服务时长："
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr v-if="form.userType!=='1'">
            <td><i class="require">*</i><b>服务位置：</b></td>
            <td>
              <el-form-item prop="servicePostion">
                <el-input
                  v-model.trim="form.servicePostion"
                  placeholder="请输入服务位置："
                  maxlength="300"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><b>登录位置：</b></td>
            <td>
              <el-form-item prop="loginLocationName">
                <el-input
                  v-model.trim="form.loginLocationName"
                  placeholder="请输入登录位置："
                  maxlength="300"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr v-if="form.userType!=='1'">
            <td><i class="require">*</i><b>所在分组：</b></td>
            <td>
              <el-form-item prop="groupId">
                <el-select v-model.trim="form.groupId" placeholder="请选择" @change="changeGroup(form.groupId)">
                  <el-option
                    v-for="item in groupIdOptions"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </td>
            <td><b>所在小组：</b></td>
            <td>
              <el-form-item prop="groupId">
                <el-select v-model.trim="form.groupSplitId" placeholder="请选择" filterable clearable="">
                  <el-option
                    v-for="item in groupSplitIdOptions"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>分组职务：</b></td>
            <td>
              <el-form-item prop="groupPost">
                <el-col :span="24">
                  <el-select
                    v-model.trim="form.groupPost"
                    placeholder="请选择分组职务"
                    size="small">
                    <el-option label="组长" value="1"></el-option>
                    <el-option label="副组长" value="2"></el-option>
                    <el-option label="组员" value="3"></el-option>
                  </el-select>
                </el-col>
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>转派配置：</b></td>
            <td>
              <el-form-item prop="turnConfig">
                <el-col :span="24">
                  <el-select
                    v-model.trim="form.turnConfig"
                    placeholder="请选择转派配置"
                    size="small">
                    <el-option label="手动接收" value="1"></el-option>
                    <el-option label="自动接收" value="2"></el-option>
                  </el-select>
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>预约类型：</b></td>
            <td>
              <el-form-item prop="apponStatus">
                <el-col :span="24">
                  <el-select
                    v-model.trim="form.apponStatus"
                    placeholder="请选择预约类型"
                    size="small">
                    <el-option label="可预约" value="1"></el-option>
                    <el-option label="不可预约" value="2"></el-option>
                  </el-select>
                </el-col>
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>取号类型：</b></td>
            <td>
              <el-form-item prop="takeNumStatus">
                <el-col :span="24">
                  <el-select
                    v-model.trim="form.takeNumStatus"
                    placeholder="请选择取号类型"
                    size="small">
                    <el-option label="可取号" value="1"></el-option>
                    <el-option label="不可取号" value="2"></el-option>
                  </el-select>
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>绩效类型：</b></td>
            <td>
              <el-form-item prop="evalStatus">
                <el-col :span="24">
                  <el-select
                    v-model.trim="form.evalStatus"
                    placeholder="请选择绩效类型"
                    size="small">
                    <el-option label="有绩效" value="1"></el-option>
                    <el-option label="无绩效" value="2"></el-option>
                  </el-select>
                </el-col>
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>资料库类型：</b></td>
            <td>
              <el-form-item prop="resourceStatus">
                <el-col :span="24">
                  <el-select
                    v-model.trim="form.resourceStatus"
                    placeholder="请选择资料库类型"
                    size="small">
                    <el-option label="有资料库" value="1"></el-option>
                    <el-option label="无资料库" value="2"></el-option>
                  </el-select>
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>常见问题类型：</b></td>
            <td>
              <el-form-item prop="questionStatus">
                <el-col :span="24">
                  <el-select
                    v-model.trim="form.questionStatus"
                    placeholder="请选择常见问题类型"
                    size="small">
                    <el-option label="有常见问题" value="1"></el-option>
                    <el-option label="无常见问题" value="2"></el-option>
                  </el-select>
                </el-col>
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>智能问答类型：</b></td>
            <td>
              <el-form-item prop="qaStatus">
                <el-col :span="24">
                  <el-select
                    v-model.trim="form.qaStatus"
                    placeholder="请选择取号类型"
                    size="small">
                    <el-option label="有智能问答" value="1"></el-option>
                    <el-option label="无智能问答" value="2"></el-option>
                  </el-select>
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr v-if="form.userType!=='1'">
            <td><i class="require"></i><b>对接用户编号字段：</b></td>
            <td>
              <el-form-item prop="connectUserId">
                <el-input
                  v-model.trim="form.connectUserId"
                  placeholder="请输入对接用户编号字段："
                  maxlength="20"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require"></i><b>视频呼叫超时时间（秒）：</b></td>
            <td>
              <el-form-item prop="callTimeout">
                <el-input
                  v-model.trim="form.callTimeout"
                  placeholder="视频呼叫超时时间（秒）"
                  maxlength="10"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
          <tr v-if="form.userType!=='1'">
            <td><i class="require">*</i><b>9个政务窗口是否屏蔽：</b></td>
            <td>
              <el-form-item prop="nineZwWindowShieldStatus">
                <el-col :span="24">
                  <el-select
                    v-model.trim="form.nineZwWindowShieldStatus"
                    placeholder="请输入9个政务窗口是否屏蔽"
                    size="small">
                    <el-option label="屏蔽" value="1"></el-option>
                    <el-option label="未屏蔽" value="2"></el-option>
                  </el-select>
                </el-col>
              </el-form-item>
            </td>
            <td><i class="require"></i><b>10+N个营商服务点是否屏蔽：</b></td>
              <td>
              <el-form-item prop="tenYsFwShieldStatus">
                <el-col :span="24">
                  <el-select
                    v-model.trim="form.tenYsFwShieldStatus"
                    placeholder="请选择10+N个营商服务点是否屏蔽"
                    size="small">
                    <el-option label="屏蔽" value="1"></el-option>
                    <el-option label="未屏蔽" value="2"></el-option>
                  </el-select>
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require"></i><b>备注：</b></td>
            <td colspan="3">
              <el-form-item prop="memo">
                <el-input
                  v-model.trim="form.memo"
                  placeholder="请输入备注"
                  maxlength="500"
                  show-word-limit
                  type="textarea"
                />
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" align="center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 查询在线时长表记录 -->
    <el-dialog
      :close-on-click-modal="false"
      :title="titleOnline"
      :visible.sync="openOnline"
      width="1100px"
      append-to-body
    >
      <!--用户数据-->
      <el-form
        :model="queryParams"
        ref="queryForm"
        :inline="true"
        label-width="80px"
      >
        <el-form-item label="登录时间" prop="loginTime">
          <el-date-picker
            v-model="onlineQueryParams.loginTime"
            type="datetime"
            placeholder="选择日期时间"
            align="right"
            :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="退出时间" prop="logoutTime">
          <el-date-picker
            v-model="onlineQueryParams.logoutTime"
            type="datetime"
            placeholder="选择日期时间"
            align="right"
            :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="退出类型" prop="logoutType">
          <el-select v-model="onlineQueryParams.logoutType" clearable placeholder="请选择">
            <el-option
              v-for="item in logoutTypes"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item class="fr mr0">
          <el-button
            type="primary"
            icon="el-icon-search"
            size="mini"
            @click="handleQueryOnline"
          >搜索
          </el-button>
        </el-form-item>
      </el-form>
      <el-table
        v-loading="loading"
        :data="onlineList"
        stripe
        style="width: 100%"
      >
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="登录时间"
          align="center"
          prop="loginTime"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="退出时间"
          align="center"
          prop="logoutTime"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="在线时长（秒）"
          align="center"
          prop="onlineTime"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="退出类型"
          align="center"
          prop="logoutType"
          :show-overflow-tooltip="true"
        />
        <!-- <el-table-column
          label="退出类型"
          align="center"
          prop="logoutType"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <p v-if="scope.row.logoutType==1">手动退出</p>
            <p v-if="scope.row.logoutType==2">异常退出</p>
            <p v-if="scope.row.logoutType==3">登录</p>
          </template>
        </el-table-column> -->
        <el-table-column
          label="登录IP"
          align="center"
          prop="loginIp"
          :show-overflow-tooltip="true"
        />
      </el-table>
      <!-- 分页组件 -->
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="onlineQueryParams.pageNum"
        :limit.sync="onlineQueryParams.pageSize"
        style="padding: 30px 0; text-align: center;"
        layout="total, sizes, prev, pager, next, jumper"
        @pagination="onlineListSerch"
      />
    </el-dialog>

  </div>
</template>

<script>
import {
  batchRemoveUser,
  initUser,
  onlinePage,
  page,
  queryWorkGroupTree,
  queryWorkGroupSplitTree,
  reserpassword,
  saveUser,
  uploadCompressImage,
  UserById
} from "@/api/ha/userManagement/userManagement.js";
import {queryDistrictSimpleTree} from "@/api/sys/district";
import Treeselect from '@riophae/vue-treeselect'
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  components: {Treeselect},
  data() {
    // 验证帮代办类型的函数
    let validateName = (rule, value, callback) => {
      // debugger
      // 当帮代办类型为空值且为必填时，抛出错误，反之通过校验
      if(this.form.userType==='2'){
        if (this.form.haType === undefined || this.form.haType === '' && this.userTypeFla) {
          callback(new Error('必填项'))
        } else {
          callback()
        }
      }else{
        callback()
      }
    }
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      districtList: [],
      multipleSelection: [],  //批量选择中选择的记录列表
      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      // openView: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: "",
        account: "",
        phone: "",
        districtOid: "",
        userType: "",
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单参数
      form: {districtOidSelect: ''},
      // 表单校验
      rules: {
        name: [
          {required: true, message: "必填项", trigger: "blur"}
        ],
        account: [
          {required: true, message: '必填项', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '必填项', trigger: "blur"}
        ],
        userType: [
          {required: true, message: '必填项', trigger: "blur"}
        ],
        haType: [{validator: validateName}],
        districtOid: [
          {required: true, message: '必填项', trigger: "blur"}
        ],
        avgServiceTime: [
          {required: true, message: '必填项', trigger: "blur"}
        ],
        workNumber: [
          {required: true, message: '必填项', trigger: "blur"}
        ],
        servicePostion: [
          {required: true, message: '必填项', trigger: "blur"}
        ],
        maxServiceNum: [
          {required: true, message: '必填项', trigger: "blur"}
        ],
        groupPost: [
          {required: true, message: '必填项', trigger: "blur"}
        ],
        turnConfig: [
          {required: true, message: '必填项', trigger: "blur"}
        ],
        groupId: [
          {required: true, message: '必填项', trigger: "blur"}
        ],
        phone: [
          {required: true, message: '必填项', trigger: "blur"}
        ],

      },
      userOidArr: [],
      props: {
        label: "label", //这里是树结构中需显示的数据（即接口返回的需展示在页面上的参数）
        children: "children",
        isLeaf: "leaf"
      },
      options: [{
        value: '1',
        label: '导服人员'
      }, {
        value: '2',
        label: '帮代办人员'
      }, {
        value: '3',
        label: '委办局人员'
      }],
      logoutTypes: [{
        value: '1',
        label: '手动退出'
      }, {
        value: '2',
        label: '异常退出'
      }, {
        value: '3',
        label: '登录'
      }],

      imageUrl: '',//头像
      uploadImagerUrl: process.env.VUE_APP_BASE_API + process.env.VUE_APP_DSXBL_ROUTE_PATH + '/work/user/uploadImage',
      //区划数据
      districtOptions: [],
      groupIdOptions: [], //分组集合
      groupSplitIdOptions: [], //小组集合
      //在线时长弹窗
      openOnline: false,
      //在线时长标题
      titleOnline: "",
      //在线时长数据
      onlineList: [],
      // 查询参数
      onlineQueryParams: {
        pageNum: 1,  //
        pageSize: 10,
        loginTime: "", //登录时间
        logoutTime: "", //退出时间
        logoutType: "", //退出类型;1-手动退出，2-异常退出，3-登录
        workUserId: "", //用户主键
      },
      pickerOptions: {
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', new Date());
          }
        }, {
          text: '昨天',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', date);
          }
        }, {
          text: '一周前',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', date);
          }
        }]
      },
    };
  },
  watch: {
    'queryParams.districtOidSelect': {
      handler(val, oldVal) {
        let dataId = null != val ? val.substring(val.lastIndexOf('-') + 1, val.length) : '';
        this.queryParams.districtOid = dataId;
      }
    },

    'form.districtOidSelect': {
      handler(val, oldVal) {
        let dataId = null != val ? val.substring(val.lastIndexOf('-') + 1, val.length) : '';
        this.form.districtOid = dataId;
      }
    },
  },
  created() {
    this.getList();
    this.getDistrictTree();

  },
  computed: {
    userTypeFla: function () {
      return this.form.userType !== `1`;
    }
  },
  methods: {
    /**在线时长表查询 */
    onlineSearch(row) {
      this.onlineList = {};
      this.onlineQueryParams = {}
      this.onlineQueryParams.workUserId = row.id;
      this.onlineListSerch();
    },
    onlineListSerch() {
      onlinePage(this.onlineQueryParams).then(response => {
        this.onlineList = response.data.data;
        this.total = response.data.total;
        this.openOnline = true;
        this.titleOnline = "登录时长记录";
      });
    },
    /** 获取分组表树 */
    getGroup(id) {
      queryWorkGroupTree(id).then(response => {
        this.groupIdOptions = response.data;
      });
    },
    changeGroup(groupId){
      this.form.groupSplitId = "";
      this.getGroupSplit(groupId);
    },
    /** 获取分组表树 */
    getGroupSplit(groupId) {
      queryWorkGroupSplitTree(groupId).then(response => {
        this.groupSplitIdOptions = response.data;
      });
    },
    /** 获取区划树 */
    getDistrictTree(districtOid) {
      queryDistrictSimpleTree(districtOid).then(response => {
        this.districtOptions = response.data;
      });
    },
    uploadUrl() {
      return uploadCompressImage();
    },
    //成功后返回
    handleAvatarSuccess(response) {
      this.imageUrl = response.data.url; // 请求成功之后赋给头像的URL
      this.form.image = response.data.url;
    },
    //上传之前
    beforeAvatarUpload(file) {
      if (file.name.indexOf("%00") > -1 || file.name.indexOf("./") > -1 || file.name.indexOf(".\\") > -1) {
        this.$message.error('上传文件名称非法！');
        return false;
      }
      const isJPG = file.type === 'image/jpeg' || 'image/png';
      const isLt2M = file.size / 10240 / 10240 < 2;
      if (!isJPG) {
        this.$message.error('请上传gif、jpg、jpeg、png或bmp格式的文件！');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 10MB!');
      }
      return isJPG && isLt2M;
    },

    //批量删除按钮
    removeRows() {
      this.$confirm('此操作将永久删除用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {//确定执行then方法
        var idList = []
        //遍历数组得到每个id值，设置到idlist中
        for (var i = 0; i < this.multipleSelection.length; i++) {
          var obj = this.multipleSelection[i]
          var id = obj.id
          idList.push(id)
        }
        //调用接口方法
        batchRemoveUser(idList)
          .then(response => {
            //提示信息
            this.$message({
              type: 'success',
              message: '批量删除成功!'
            })
            //刷新页面
            this.getList()
          })
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          saveUser(this.form).then(response => {
            if (response.code === 200) {
              this.msgSuccess("保存成功");
              this.openInit = false;
              this.getList();
            }
          });
        } else {
          return false;
        }
      });
    },
    //重置密码
    resetPassword(row) {
      this.$confirm('此操作将用户密码重置,重置密码为初始密码:123456 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {//确定执行then方法
        //调用接口方法
        reserpassword(row.id)
          .then(response => {
            //提示信息
            this.$message({
              type: 'success',
              message: '重置成功'
            })
            //刷新页面
            this.getList()
          })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消重置'
        });
      });
    },
    //删除
    removeUserById(id) {
      this.$confirm('此操作将该用户数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {//确定执行then方法
        //调用接口方法
        UserById(id)
          .then(response => {
            //提示信息
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            //刷新页面
            this.getList()
          })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    /** 初始化新增或修改 */
    handleInit(row) {
      const id = row.id;
      if (id === undefined) {
        this.getGroup();
        this.getGroupSplit();
        this.form = {};
        this.imageUrl = '';
        this.openInit = true;
        this.title = "新增";
      } else {
        initUser(id).then(response => {
          this.getGroup();
          this.getGroupSplit(row.groupId);
          let result = response.data;
          if (null != result.districtOid && '' != result.districtOid) {
            result.districtOidSelect = 'DISTRICT-' + result.districtOid;
          }
          this.imageUrl = response.data.image;
          this.form = result;
          this.openInit = true;
          this.title = "修改";
        });
      }
    },

    /** 查询列表 */
    getList() {
      this.loading = true;
      const asa = this.queryParams.pageNum
      page(this.queryParams).then(response => {
        this.districtList = response.data.data;
        console.log(response)
        this.total = response.data.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.openInit = false;
      // this.reset();
    },
    // 表单重置
    reset() {
      Object.assign(this.form, this.$options.data().form);
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /**在线时长表弹窗搜索按钮操作 */
    handleQueryOnline() {
      this.onlineQueryParams.pageNum = 1;
      this.onlineListSerch();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },

    viewServiceClose() {
      this.serviceDialogOptions.pop();
    },
    //获取选择复选框的id值
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    }
  },
};
</script>

<style lang="scss" scoped>
//头像样式start
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 100px;
  display: block;
}

// end
.dialog-table {
  padding: 20px;
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
  text-align: left;
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

.zf-zc-table tr td {
  padding: 2px 10px !important;
}
</style>
